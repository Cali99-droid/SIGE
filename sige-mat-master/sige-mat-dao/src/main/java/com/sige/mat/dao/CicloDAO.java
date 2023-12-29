package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.*;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad ciclo.
 * @author MV
 *
 */
@Repository
public class CicloDAO extends CicloDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	/*Lista de instrumentos usados*/
	public List<Row> listarCiclosxAnio(Integer id_anio, Integer id_gir, Integer id_niv, Integer id_tpe, Integer id_suc) {
		String sql = "SELECT cic.id, CONCAT(cic.`nom`,'-',suc.nom,' - ',niv.nom) AS value, per.id_anio AS aux1, per.id_niv AS aux2, per.id_suc AS aux3, per.id AS aux4, gir.nom giro, tpe.nom tipo_periodo, cic.est \r\n" ; 
				sql = sql + " FROM col_ciclo cic INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\r\n";
				sql = sql + " INNER JOIN cat_tip_periodo tpe ON per.id_tpe=tpe.id ";
				sql = sql + " INNER JOIN cat_nivel niv ON per.id_niv=niv.id \r\n"; 
				sql = sql + " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id \r\n" ;
				sql = sql + " INNER JOIN ges_servicio srv ON srv.id=per.id_srv \r\n";
				sql = sql + " INNER JOIN ges_giro_negocio gir ON srv.id_gir=gir.id \r\n";
				sql = sql + " WHERE per.`id_anio`=" +id_anio; 
				if(id_gir!=null) {
					sql = sql + " AND gir.id="+id_gir;
				}
				
				if(id_niv!=null) {
					sql = sql + " AND niv.id="+id_niv;
				}
				
				if(id_tpe!=null) {
					sql = sql + " AND tpe.id="+id_tpe;
				}
				
				if(id_suc!=null) {
					sql = sql + " AND suc.id="+id_suc;
				}
				sql = sql + " ORDER BY gir.nom, tpe.id, cic.fec_ins DESC, suc.nom ASC, niv.nom ASC";
				//sql = sql + " ORDER BY gir.nom, tpe.id, cic.fec_ins, suc.nom ASC, niv.nom ASC";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public List<Row> listarCiclosxGiroNegocio(Integer id_gir, Integer id_anio) {
		
		String sql = "SELECT cct.id ,cic.id id_cic, per.id aux1 , suc.`nom` sucursal, niv.`nom` nivel, tpe.`nom` tipo_periodo, cic.nom ciclo, CONCAT(suc.nom,' ',cic.`nom`,' ', niv.nom,' - ',tur.nom) value, tur.nom turno \r\n" + 
				"FROM `per_periodo` per INNER JOIN `col_ciclo` cic ON per.id=cic.`id_per`\r\n" + 
				"INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" + 
				"INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON per.`id_niv`=niv.`id`\r\n" + 
				"INNER JOIN `cat_tip_periodo` tpe ON per.`id_tpe`=tpe.`id`\r\n" + 
				"INNER JOIN col_ciclo_turno cct ON cic.id=cct.id_cic \r\n"+
				"INNER JOIN col_turno tur ON cct.id_tur=tur.id \r\n"+
				"WHERE ser.`id_gir`="+id_gir+" AND per.id_anio="+id_anio+" AND cct.est='A' "+
				"ORDER BY cic.fec_ins DESC ";
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarCiclosxGiroNegocioxTrabajador(Integer id_gir, Integer id_anio, Integer id_rol) {
		
		String sql = "SELECT cct.id ,cic.id id_cic, per.id aux1 , suc.`nom` sucursal, niv.`nom` nivel, tpe.`nom` tipo_periodo, cic.nom ciclo, CONCAT(suc.nom,' ',cic.`nom`,' ', niv.nom,' - ',tur.nom) value, tur.nom turno \r\n" ;
				sql += "FROM `per_periodo` per INNER JOIN `col_ciclo` cic ON per.id=cic.`id_per`\r\n" ; 
				sql += "INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" ; 
				sql += "INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`\r\n" ; 
				sql += "INNER JOIN `cat_nivel` niv ON per.`id_niv`=niv.`id`\r\n" ; 
				sql += "INNER JOIN `cat_tip_periodo` tpe ON per.`id_tpe`=tpe.`id`\r\n" ; 
				sql += "INNER JOIN col_ciclo_turno cct ON cic.id=cct.id_cic \r\n";
				sql += "INNER JOIN col_turno tur ON cct.id_tur=tur.id \r\n";
				if(id_rol==20) {
					sql +=" INNER JOIN col_administrador_sede ased ON ased.id_suc=suc.id";
				}
				sql += " WHERE ser.`id_gir`="+id_gir+" AND per.id_anio="+id_anio+" AND cct.est='A' ";
				/*if(id_rol==20) {
					sql +=" AND suc.id="+id_suc;
				}*/
				sql += "ORDER BY cic.fec_ins DESC ";
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarCiclosxGiroNegocioxPeriodo(Integer id_gir, Integer id_anio, Integer id_tpe, Integer id_niv) {
		
		String sql = "SELECT cct.id ,cic.id id_cic, per.id aux1 , suc.`nom` sucursal, niv.`nom` nivel, tpe.`nom` tipo_periodo, cic.nom ciclo, CONCAT(suc.nom,' ',cic.`nom`,' ', niv.nom,' - ',tur.nom) value, tur.nom turno \r\n" + 
				"FROM `per_periodo` per INNER JOIN `col_ciclo` cic ON per.id=cic.`id_per`\r\n" + 
				"INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" + 
				"INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON per.`id_niv`=niv.`id`\r\n" + 
				"INNER JOIN `cat_tip_periodo` tpe ON per.`id_tpe`=tpe.`id`\r\n" + 
				"INNER JOIN col_ciclo_turno cct ON cic.id=cct.id_cic \r\n"+
				"INNER JOIN col_turno tur ON cct.id_tur=tur.id \r\n"+
				"WHERE ser.`id_gir`="+id_gir+" AND per.id_anio="+id_anio+" AND tpe.id="+id_tpe+" AND ser.id_niv="+id_niv;
		return sqlUtil.query(sql);

	}

	/*Lista de instrumentos usados*/
	public List<Row> listarCiclosCombo(Integer id_per) {
		String sql = "SELECT cic.id, cic.`nom` AS value, per.id_anio AS aux1, per.id_niv AS aux2, per.id_suc AS aux3 \r\n"  
				+ "	FROM col_ciclo cic INNER JOIN `per_periodo` per ON cic.`id_per`=per.`id`\r\n" 
				+ "	WHERE per.`id`=" +id_per+" AND CURDATE()<cic.`fec_fin`" 
				+ "	ORDER BY cic.nom ASC";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public void desactivarCiclo(Integer id_cic){
		String sql = "update col_ciclo set est='I' where id=?";
		sqlUtil.update(sql,new Object[]{id_cic});		
	}
}
