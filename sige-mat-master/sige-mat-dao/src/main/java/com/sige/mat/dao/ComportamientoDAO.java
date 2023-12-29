package com.sige.mat.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ComportamientoDAOImpl;
import com.tesla.colegio.model.Comportamiento;
import com.tesla.colegio.model.Nota;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad comportamiento.
 * @author MV
 *
 */
@Repository
public class ComportamientoDAO extends ComportamientoDAOImpl{
	final static Logger logger = Logger.getLogger(ComportamientoDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaNivelTutor(int id_tra, int id_anio) {
		
		String sql = "SELECT DISTINCT niv.`id`, niv.`nom` as value FROM `cat_nivel` niv LEFT JOIN `per_periodo` per ON niv.`id`=per.`id_niv`"
				+ " LEFT JOIN `col_aula` ca ON per.`id`=ca.`id_per`"
				+ " LEFT JOIN `col_tutor_aula` cta ON ca.`id`=cta.`id_au`"
				+ " WHERE id_tra=? AND per.`id_anio`=?;";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra, id_anio });

	}
	
	public List<Row> listaNivelAuxiliar(int id_tra, int id_anio, int id_gir) {
		
		String sql = "SELECT DISTINCT niv.`id`, niv.`nom` as value FROM `cat_nivel` niv INNER JOIN `per_periodo` per ON niv.`id`=per.`id_niv`"
				+ " INNER JOIN `col_aula` ca ON per.`id`=ca.`id_per` AND ca.id_per=per.id"
				+ " INNER JOIN col_aula_detalle aud ON ca.id=aud.id_au "
				+ " INNER JOIN ges_servicio srv ON per.id_srv=srv.id"
				+ " WHERE aud.id_aux=? AND per.`id_anio`=? AND srv.id_gir=? ";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra, id_anio, id_gir });

	}
	
	public List<Row> listarSucursalTutor(int id_tra, int id_anio) {
		
		String sql = "SELECT DISTINCT suc.id, suc.nom as value "
				+ " FROM col_tutor_aula cta LEFT JOIN col_aula ca ON cta.id_au=ca.id"
				+ " LEFT JOIN per_periodo per ON ca.id_per=per.id"
				+ " LEFT JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " WHERE cta.id_tra=? and per.id_anio=?";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
   public List<Row> listarSucursalAuxiliar(int id_tra, int id_anio) {
		
		String sql = "SELECT DISTINCT suc.id, suc.nom as value "
				+ " FROM col_aula_detalle aud LEFT JOIN col_aula ca ON aud.id_au=ca.id"
				+ " LEFT JOIN per_periodo per ON ca.id_per=per.id"
				+ " LEFT JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " WHERE aud.id_aux=? and per.id_anio=? ";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listaGradosTutor(int id_tra, int id_anio, int id_niv) {
		
		String sql = "SELECT DISTINCT g.id, g.nom as value from col_tutor_aula cta JOIN col_aula ca ON cta.id_au=ca.id"
				+ " LEFT JOIN cat_grad g ON ca.id_grad=g.id  "
				+ " LEFT JOIN per_periodo p ON ca.id_per=p.id "
				+ " WHERE cta.id_tra=? AND p.id_anio=? AND p.id_niv=? ORDER BY g.id";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra, id_anio, id_niv });

	}
	
	public List<Row> listaGradosAuxiliar(int id_tra, int id_anio, int id_niv) {
		
		String sql = "SELECT DISTINCT g.id, g.nom as value from col_aula ca"
				+ " INNER JOIN col_aula_detalle aud ON ca.id=aud.id_au"
				+ " INNER JOIN cat_grad g ON ca.id_grad=g.id  "
				+ " INNER JOIN per_periodo p ON ca.id_per=p.id "
				+ " WHERE aud.id_aux=? AND p.id_anio=? AND p.id_niv=? ORDER BY g.id";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra, id_anio, id_niv });

	}
	
	public List<Row> listarAulaTutor(int id_tra, int id_grad, int id_suc, int id_anio) {
		
		String sql = "SELECT ca.id, ca.secc as value FROM col_tutor_aula cat LEFT JOIN col_aula ca ON cat.id_au=ca.id"
				+ " LEFT JOIN per_periodo per ON ca.id_per=per.id"
				+ " WHERE cat.id_tra=? AND ca.id_grad=? AND per.id_suc=? and per.id_anio=?";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_grad, id_suc, id_anio});

	}
	
	public List<Row> listarAulaAuxiliar(Integer id_tra, Integer id_grad, Integer id_cic) {
		
		String sql = "SELECT ca.id, ca.secc as value FROM col_aula ca "
				+ " INNER JOIN col_aula_detalle cad ON ca.id=cad.id_au "
				+ " INNER JOIN col_ciclo cic ON ca.id_cic=cic.id"
				+ " WHERE cad.id_aux=? AND ca.id_grad=? AND cic.id=? ";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_grad, id_cic});

	}
	
	public List<Row> listarCursosTutor() {
		
			String sql = "SELECT distinct cu.id , cu.nom  as value FROM cat_curso cu where nom='Comportamiento'";
			//logger.info(sql);
			return sqlUtil.query(sql);
	}
	
	public List<Row> listaCapacidades(int id_cur, int id_niv) {
		
		String sql = "SELECT DISTINCT cap.id, cap.nom capacidad FROM col_capacidad cap LEFT JOIN col_competencia com ON cap.id_com=com.id"
				+ " WHERE com.id_cur=? AND com.id_niv=? and com.est='A'";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_cur, id_niv });

	}
	
	public List<Row> obtenerPeso(Integer id_cap){
		String sql = "SELECT distinct com.peso "
				+ " FROM col_capacidad cap "
				+ " INNER JOIN col_competencia com ON com.id =  cap.id_com"
				+ " WHERE cap.id=?";
		
		return sqlUtil.query(sql, new Object[]{id_cap});
	}

	
	public Map<String, Object> listarAlumnoCapacidades(int id_cur, int id_niv,int id_cpu, int id_au, int id_anio, String tip_gra){
		List<Row> capacidades = listaCapacidades(id_cur, id_niv);
		String sql=null;
		if(tip_gra.equals("N")){
			sql="SELECT  alu.id,alu.ape_pat, alu.ape_mat, alu.nom";
			for (Row row : capacidades) {
				sql = sql + ", nc.prom, ncc"+row.getInteger("id")+".id ncc"+row.getInteger("id")+"_id , ncc"+row.getInteger("id")+".nota ncc"+row.getInteger("id")+"_nota ";
			}
			sql = sql + "\n FROM alu_alumno alu INNER JOIN mat_matricula mat ON  alu.id=mat.id_alu";
			sql = sql + "\n INNER JOIN col_aula au ON mat.id_au=au.id";
			sql = sql + "\n INNER JOIN per_periodo per ON au.id_per=per.id";
			sql = sql + "\n LEFT JOIN not_comportamiento nc ON alu.id=nc.id_alu AND nc.id_cpu="+id_cpu;
			for (Row row : capacidades) {
				sql = sql + "\n LEFT JOIN col_capacidad cap"+row.getInteger("id")+" ON cap"+row.getInteger("id")+".id="+row.getInteger("id");
				sql = sql + "\n LEFT JOIN not_cap_com ncc"+row.getInteger("id")+" ON nc.id=ncc"+row.getInteger("id")+".id_nc AND  ncc"+row.getInteger("id")+".id_cap=cap"+row.getInteger("id")+".id";
			}
			sql = sql + "\n WHERE mat.id_au_asi="+id_au+" AND per.id_anio="+id_anio;
			sql = sql + "\n ORDER BY alu.ape_pat, alu.ape_mat";
		} else if(tip_gra.equals("F")){
			sql ="SELECT  alu.id,alu.ape_pat, alu.ape_mat, alu.nom";
			for (Row row : capacidades) {
				sql = sql + ", nc.prom, ncc"+row.getInteger("id")+".id ncc"+row.getInteger("id")+"_id , ncc"+row.getInteger("id")+".nota ncc"+row.getInteger("id")+"_nota ";
			}
			sql = sql +"\n FROM alu_alumno alu"; 
			sql = sql + "\n INNER JOIN mat_matricula mat ON  alu.id=mat.id_alu";
			sql = sql + "\n INNER JOIN cat_grad gra ON gra.id=mat.`id_gra`";
			sql = sql + "\n INNER JOIN col_aula au ON `gra`.`id` = `au`.`id_grad`";
			sql = sql + "\n INNER JOIN `col_aula_especial` esp ON esp.id_au=au.id AND esp.`id_mat`=mat.`id`";
			sql = sql + "\n INNER JOIN per_periodo per ON au.id_per=per.id";
			sql = sql + "\n LEFT JOIN not_comportamiento nc ON alu.id=nc.id_alu AND nc.id_cpu=" + id_cpu;
			for (Row row : capacidades) {
				sql = sql + "\n LEFT JOIN col_capacidad cap"+row.getInteger("id")+" ON cap"+row.getInteger("id")+".id="+row.getInteger("id");
				sql = sql + "\n LEFT JOIN not_cap_com ncc"+row.getInteger("id")+" ON nc.id=ncc"+row.getInteger("id")+".id_nc AND  ncc"+row.getInteger("id")+".id_cap=cap"+row.getInteger("id")+".id";
			}
			sql = sql +"\n WHERE per.id_anio="+id_anio;
			sql = sql + "\n ORDER BY alu.ape_pat, alu.ape_mat";
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Row> alumnosCapacidades = sqlUtil.query(sql);
		map.put("alumnosCapacidades", alumnosCapacidades);		
		map.put("capacidades", capacidades);
		//logger.info(sql);

		return map;
	}
	
	public void actualizaNotaComportamiento(Integer id, Integer nota,Integer usr_act){
		String sql ="update not_cap_com set nota=?, usr_act=?, fec_act=?  where id=?";
		sqlUtil.update(sql, new Object[]{nota , usr_act, new Date(), id});
		
	}
	
	public void actualizaPromedio(Comportamiento comportamiento){
		String sql ="update not_comportamiento set prom=? where id=?";
		sqlUtil.update(sql, new Object[]{comportamiento.getProm(), comportamiento.getId()});
	}
	
	
	/**
	 * Listar las notas de las capacidades a partir del padre (not_comportamiento)
	 * @return
	 */
	public List<Row> listNotasCapacidades(Integer id_nc){
		String sql = "select cap.nom cap, nc.nota from not_cap_com nc"
				+ " inner join col_capacidad cap on cap.id = nc.id_cap where id_nc=?";
		
		return sqlUtil.query(sql, new Object[]{id_nc});
	}
	
	public List<Row> listarNotasComportamiento(int id_cpu, int id_anio, int id_niv, String id_suc, Integer id_tra) {
		String sql_suc = " ";
		String sql_tra = " ";
		if (id_suc != "") {
			sql_suc = " and per.id_suc=" + id_suc;
		}
		if (id_tra!=null){
			sql_tra = " and tra.id="+id_tra;
		}
		String sql = "SELECT DISTINCT CONCAT( tra.`ape_pat`, ' ',tra.`ape_mat`, ' ',tra.`nom`) AS tutor, tra.cel, niv.`nom` nivel, gra.`nom` grado, "
				+ " au.`secc`, com_not.cant_notas AS cantidad "
				+ " FROM  `col_tutor_aula` cta "
				+ " LEFT JOIN `aeedu_asistencia`.`ges_trabajador` tra ON cta.`id_tra`=tra.`id` "
				+ " LEFT JOIN `seg_usuario` usu ON usu.`id_tra`=tra.`id`"
				+ " LEFT JOIN `seg_usuario_rol` urol ON usu.`id`=urol.`id_rol` AND urol.`id_rol`=5"
				+ " LEFT JOIN col_aula au ON cta.`id_au`=au.`id`"
				+ " LEFT JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
				+ " LEFT JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `per_periodo` per ON au.`id_per`=per.id AND per.`id_niv`=niv.id"
				+ " LEFT JOIN comportamiento_nota com_not ON  com_not.id_au = cta.id_au AND com_not.gra_id=au.id_grad AND com_not.id_cpu="+id_cpu
				+ " WHERE  per.`id_anio`="+id_anio+" AND per.`id_niv`="+id_niv+"  "+sql_suc+" "+sql_tra
				+ "ORDER BY tra.`ape_pat`, tra.`ape_mat`, tra.nom, au.`id_grad` ASC, au.`secc`";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	
	public Integer promedioAnual(Integer id_alu, Integer  id_au){

		Param param = new Param();
		//param.put("id_au", id_au);
		param.put("id_alu", id_alu);

		String sql = "select round(avg(ncm.nota)) promedio from not_cap_com ncm "
				+ " inner join not_comportamiento c on c.id= ncm.id_nc "
				+ " where c.id_alu=:id_alu";
		
				
		BigDecimal promedio = sqlUtil.queryForObject(sql, param, BigDecimal.class); 
		
		if (promedio ==null)
			return 0;
		else
			return promedio.intValue();
	}

}
