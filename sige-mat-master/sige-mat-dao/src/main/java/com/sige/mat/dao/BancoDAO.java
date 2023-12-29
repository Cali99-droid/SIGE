package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.BancoDAOImpl;

/**
 * Define m�todos DAO operations para la entidad banco.
 * 
 * @author MV
 *
 */
@Repository
public class BancoDAO extends BancoDAOImpl {
	final static Logger logger = Logger.getLogger(BancoDAO.class);
	
    @Autowired
    private SQLUtil sqlUtil;

	public List<Map<String, Object>> pagosMes(Integer id_anio, Integer[] meses, Integer id_anio_ant, Integer id_bco) {

		List<Map<String, Object>> pagosMes = jdbcTemplate.queryForList(getQueryPagos(id_anio, meses,id_anio_ant, id_bco));

		return pagosMes;
	}

	public List<Map<String, Object>> pagosMes(Integer id_anio, Integer mes) {

		List<Map<String, Object>> pagosMes = jdbcTemplate.queryForList(getQueryPagos(id_anio, null,null,null));

		return pagosMes;
	}

	private String getQueryPagos(Integer id_anio, Integer[] meses, Integer id_anio_ant, Integer id_bco) {
		/*String sql = "SELECT fac.id, mes.id id_mes, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom nom, fac.monto, fac.monto_total, mes.nom mes, mat.id id_mat, mat.id_niv, mat.num_cont, per.id id_per, per.id_anio id_anio_ult_mat,";
			   sql += "\n (select count(*) from mat_matricula m1 where m1.num_cont=mat.num_cont and (m1.id_sit IS NULL OR m1.id_sit<>5) ) hermanos, des.mensualidad_bco, ";
				//+ " sol.id solicitud, "
			   sql += "cm.dia_mora, ";
			   sql += "\n cm.monto monto_conf, IFNULL(desc_banco,0) desc_banco" 
			   + "\n FROM mat_matricula mat ";
			   sql += "\n INNER JOIN col_aula au ON mat.id_au_asi=au.id";
			   sql += " INNER JOIN per_periodo per ON au.id_per=per.id ";
			   sql += " INNER JOIN mat_conf_mensualidad cm ON cm.id_per=per.id";
				//+ " left JOIN mat_solicitud sol ON sol.id_mat=mat.id_alu"
			   sql += " left JOIN fac_alumno_descuento des ON des.id_mat=mat.id";
			   sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat";
			   sql += " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id" + " INNER JOIN cat_mes mes ON fac.mens=mes.id";
			   sql += " LEFT JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=alu.id";
			   
			   //+ " WHERE alu.nro_doc='62015031' andAND mat.id='15878' "
			   sql += " WHERE fac.tip='MEN' AND fac.tip='MAT' AND fac.canc='0' AND fac.mens not in (1,2) " + " and (mat.id_sit is null or mat.id_sit not in (4,5,6) ) "; //and mat.id='16276'*/ 
		//IF(mes.id=NULL OR mes.id=0,0,mes.id) id_mes
		String sql="SELECT * FROM (";
		//if(id_anio_ant!=null && id_bco!=1) {
		if(id_anio_ant!=null) {
			sql +=" SELECT DISTINCT fac.id,fac.mens id_mes, p.nro_doc, p.ape_pat, p.ape_mat, p.nom nom, fac.monto, fac.monto_total, fac.id_bec, \r\n" ;
			sql += " IF(mes.id IS NULL OR mes.id=0,'MATRICULA', mes.nom) mes, \r\n";
			sql += "mat.id id_mat, mat.id_niv, mat.num_cont, per.id id_per, per.id_anio id_anio_ult_mat, mat.id_au_asi, \r\n"; 
			//sql += " (SELECT COUNT(*) FROM mat_matricula m1 WHERE m1.num_cont=mat.num_cont AND (m1.id_sit IS NULL OR m1.id_sit<>5) ) hermanos, \r\n";
			sql += " (SELECT COUNT(*) FROM mat_matricula m1 INNER JOIN per_periodo per1 ON m1.id_per=per1.id WHERE m1.id_fam=mat.id_fam AND (m1.id_sit IS NULL OR m1.id_sit NOT IN (5,4)) AND per1.id_anio=per.id_anio AND per1.id_tpe=1) hermanos, \r\n"; //Esto funcionara a partir de lunes
			sql += " des.mensualidad_bco, cm.dia_mora, \r\n";
			sql += " cm.monto monto_conf, IFNULL(desc_banco,0) desc_banco\r\n"; 
			sql += " FROM mat_matricula mat \r\n";
			sql += " INNER JOIN col_aula au on mat.id_au_asi=au.id ";
			sql += " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id \r\n" ;
			sql += " INNER JOIN `col_persona` p ON alu.`id_per`=p.`id`\r\n"; 
			sql += " INNER JOIN per_periodo per ON mat.id_per=per.id AND au.id_per=per.id \r\n";
			sql += " INNER JOIN mat_conf_mensualidad cm ON cm.id_per=per.id AND au.id_cme=cm.id_cme AND mat.`id_cct`=cm.`id_cct` \r\n" ; 
			sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat  \r\n";
			sql += " LEFT JOIN fac_alumno_descuento des ON des.id_mat=mat.id \r\n";
			sql += " LEFT JOIN cat_mes mes ON fac.mens=mes.id \r\n" ;
			//sql += " INNER JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=alu.id \r\n" ;
			sql += " WHERE (fac.tip='MEN' OR fac.tip='MAT') AND fac.canc='0' AND fac.id_bco_pag="+id_bco+" \r\n" ;
			sql += "  AND (fac.mens NOT IN (1,2) OR fac.`mens` IS NULL) AND (mat.id_sit IS NULL OR mat.id_sit NOT IN (4,5,6)) ";
			//if (id_anio!=null)
		   sql += " AND per.id_anio=" + id_anio_ant +"  "; // and mat.id IN (17349,17350)  95436
		  // sql += " ORDER BY p.ape_pat, p.ape_mat , p.nom, mes.id";
		  // sql += " AND mat.id=15540";
		   sql += " UNION ALL ";
		}
		
	//if(id_bco!=1) { // Creo q ya deberia salir para todos los bancos funcionara a partir del lunes
			   sql += "SELECT DISTINCT fac.id,fac.mens id_mes, p.nro_doc, p.ape_pat, p.ape_mat, p.nom nom, fac.monto, fac.monto_total, fac.id_bec, \r\n" ;
				//sql += " IF(mes.id IS NULL OR mes.id=0,'MATRICULA', mes.nom) mes, \r\n";
				sql += " IF(fac.tip='MAT','MATRICULA', 'CUOTA DE INGRESO') mes, \r\n";
				sql += "mat.id id_mat, mat.id_niv, mat.num_cont, per.id id_per, per.id_anio id_anio_ult_mat, mat.id_au_asi, \r\n"; 
				//sql += " (SELECT COUNT(*) FROM mat_matricula m1 WHERE m1.num_cont=mat.num_cont AND (m1.id_sit IS NULL OR m1.id_sit<>5) ) hermanos, \r\n";
				sql += " (SELECT COUNT(*) FROM mat_matricula m1 INNER JOIN per_periodo per1 ON m1.id_per=per1.id WHERE m1.id_fam=mat.id_fam AND (m1.id_sit IS NULL OR m1.id_sit NOT IN (5,4)) AND per1.id_anio=per.id_anio AND per1.id_tpe=1) hermanos, \r\n"; //Esto funcionara a partir de lunes
				sql += " des.mensualidad_bco, NULL dia_mora , \r\n";
				sql += " cm.matricula monto_conf, 0 desc_banco \r\n"; 
				sql += " FROM mat_matricula mat \r\n";
				sql += " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id \r\n" ;
				sql += " INNER JOIN `col_persona` p ON alu.`id_per`=p.`id`\r\n"; 
				sql += " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.id ";
				sql += " INNER JOIN `col_turno_aula` cta ON au.id=cta.id_au";
				sql += " INNER JOIN per_periodo per ON mat.id_per=per.id  AND au.id_per=per.`id`";
				sql += " INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit";
				sql += " INNER JOIN `mat_conf_cuota` cm ON cm.id_per=per.id AND cm.`id_cct`=cit.id AND cm.id_cme=au.id_cme";
				sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat  \r\n";
				sql += " LEFT JOIN fac_alumno_descuento des ON des.id_mat=mat.id \r\n";
				sql += " LEFT JOIN cat_mes mes ON fac.mens=mes.id \r\n" ;
				//sql += " LEFT JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=alu.id \r\n" ;
				sql += " WHERE (fac.tip='MAT' OR fac.tip='ING') AND fac.canc='0' AND mat.con_val=1  \r\n" ; // (fac.tip='MEN' OR 
				sql += " AND fac.id_bco_pag="+id_bco+" "; // Porque ya deberia salir para todos los bancos
				sql += "  AND (fac.mens NOT IN (1,2) OR fac.`mens` IS NULL) AND (mat.id_sit IS NULL OR mat.id_sit NOT IN (4,5,6) ) ";
		//if (id_anio!=null)
	   sql += " AND per.id_anio=" + id_anio +" "; // and mat.id IN (17349,17350)  95436 // and mat.id=21047
	   //Mensaulidades del año 
	   sql += " UNION ALL ";
	   //}
	   sql += "SELECT DISTINCT fac.id,fac.mens id_mes, p.nro_doc, p.ape_pat, p.ape_mat, p.nom nom, fac.monto, fac.monto_total, fac.id_bec, \r\n" ;
		//sql += " IF(mes.id IS NULL OR mes.id=0,'MATRICULA', mes.nom) mes, \r\n";
		sql += " mes.nom mes, \r\n";
		sql += "mat.id id_mat, mat.id_niv, mat.num_cont, per.id id_per, per.id_anio id_anio_ult_mat, mat.id_au_asi, \r\n"; 
		//sql += " (SELECT COUNT(*) FROM mat_matricula m1 WHERE m1.num_cont=mat.num_cont AND (m1.id_sit IS NULL OR m1.id_sit<>5) ) hermanos, \r\n"; // Dejo de funcionar porque ahora el contrato es x familia, ya no por apoderdo
		sql += " (SELECT COUNT(*) FROM mat_matricula m1 INNER JOIN per_periodo per1 ON m1.id_per=per1.id WHERE m1.id_fam=mat.id_fam AND (m1.id_sit IS NULL OR m1.id_sit NOT IN (5,4)) AND per1.id_anio=per.id_anio AND per1.id_tpe=1) hermanos, \r\n"; //Esto funcionara a partir de lunes
		sql += " des.mensualidad_bco, cm.dia_mora, \r\n";
		sql += " cm.monto monto_conf, IFNULL(desc_banco,0) desc_banco\r\n"; 
		sql += " FROM mat_matricula mat \r\n";
		sql += " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id \r\n" ;
		sql += " INNER JOIN `col_persona` p ON alu.`id_per`=p.`id`\r\n"; 
		sql += " INNER JOIN per_periodo per ON mat.id_per=per.id  \r\n";
		sql += " INNER JOIN col_aula au ON mat.`id_au_asi`=au.id AND au.id_per=per.id";
		sql += " INNER JOIN `col_ciclo` cic ON cic.id_per=per.`id`";
		sql += " INNER JOIN col_turno_aula cta ON au.id=cta.id_au";
		sql += " INNER JOIN col_ciclo_turno cit ON cit.id=cta.id_cit";
		sql += " INNER JOIN mat_conf_mensualidad cm ON cm.id_per=per.id AND cm.`id_cct`=cit.id AND au.id_cme=cm.`id_cme` ";
		sql += " INNER JOIN fac_academico_pago fac ON mat.id=fac.id_mat  \r\n";
		sql += " LEFT JOIN fac_alumno_descuento des ON des.id_mat=mat.id \r\n";
		sql += " LEFT JOIN cat_mes mes ON fac.mens=mes.id \r\n" ;
		//sql += " LEFT JOIN cvi_inscripcion_campus cvi ON cvi.id_alu=alu.id \r\n" ;
		sql += " WHERE fac.tip='MEN'  AND fac.canc='0' AND fac.id_bco_pag="+id_bco+" AND mat.con_val=1 \r\n" ; // (fac.tip='MEN' OR 
		sql += "  AND (mat.id_sit IS NULL OR mat.id_sit NOT IN (4,5,6) ) AND ( "; // AND fac.id_mat=17249  95436
		for (int i = 0; i < meses.length; i++) {
			if(i<(meses.length-1))
			sql += " fac.mens="+meses[i]+" OR";
			else if(i==(meses.length-1))
				sql += " fac.mens="+meses[i];	
			else
				sql += "";
		}
		sql += ")";
//if (id_anio!=null)
sql += " AND per.id_anio=" + id_anio +" )t"; 
		// + " AND alu.ape_mat<='K'";
		// + " AND mes.id<6 and fac.id!=26490";
		// + " AND mes.id>6 and mes.id<10";
		// + " AND mes.id>=10 ";
		/*cambio 2021if (mes != null && mes.intValue() != 0)
			sql += " AND fac.mens=" + mes;*/

		//sql += " ORDER BY p.ape_pat, p.ape_mat , p.nom, mes.id";
		sql += " ORDER BY t.ape_pat, t.ape_mat , t.nom, t.id_mes";

		return sql;
	}
	
	public void actualizarBanco(Integer id_mat, Integer id_bco){
		String sql = "update fac_academico_pago set id_bco_pag=? WHERE id_mat=? AND canc=0 AND est='A' ";
		sqlUtil.update(sql,new Object[]{ id_bco,id_mat});		
	}
	
}
