package com.sige.mat.dao;

import java.util.List; 

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tesla.frmk.sql.Row;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PeriodoAcaDAOImpl;
import com.tesla.colegio.util.Constante;


/**
 * Define m�todos DAO operations para la entidad periodo_aca.
 * @author MV
 *
 */
@Repository
public class PeriodoAcaDAO extends PeriodoAcaDAOImpl{
final static Logger logger = Logger.getLogger(PeriodoAcaDAO.class);

@Autowired
private SQLUtil sqlUtil;
	public List<Row> listarPeriodo(int id_anio,int id_usr, int id_rol) {
		
		if(id_rol==6){
			String sql = "SELECT DISTINCT per.`id`, CONCAT('AE-',suc.`nom`,'-',ser.`nom`) value, per.id_niv aux1"
					+ " FROM `per_periodo` per "
					+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`  AND ser.`id_suc`=per.`id_suc` "
					+ " INNER JOIN `col_curso_anio` cua ON   cua.`id_per`=per.`id`"
					+ " INNER JOIN `col_curso_aula` cca ON cca.`id_cua`=cua.`id`"
					+ " INNER JOIN seg_usuario usr ON cca.`id_tra`=usr.`id_tra`"
					+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id` AND ser.`id_suc`=suc.`id`"
					+ " WHERE  per.`id_anio`=? AND usr.`id`=? AND per.`id_tpe`=1 ;";
			logger.info(sql);
			return sqlUtil.query(sql, new Object[] { id_anio, id_usr });
		} else if(id_rol==5){
			String sql = "SELECT DISTINCT per.`id`, CONCAT('AE-',suc.`nom`,'-',ser.`nom`) value, per.id_niv aux1"
					+ " FROM `per_periodo` per "
					+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`  AND ser.`id_suc`=per.`id_suc` "
					+ " INNER JOIN `col_aula` au ON   au.id_per=per.id"
					+ " INNER JOIN col_tutor_aula cta ON au.`id`=cta.`id_au`"
					+ " INNER JOIN seg_usuario usr ON cta.`id_tra`=usr.`id_tra`"
					+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id` AND ser.`id_suc`=suc.`id`"
					+ " WHERE  per.`id_anio`=? AND usr.`id`=? AND per.`id_tpe`=1 ;";
			logger.info(sql);
			return sqlUtil.query(sql, new Object[] { id_anio, id_usr });
		} else {
			String sql = "SELECT DISTINCT per.`id`, CONCAT('AE-',suc.`nom`,'-',ser.`nom`) value, per.id_niv aux1"
					+ " FROM `per_periodo` per INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`  AND ser.`id_suc`=per.`id_suc` "
					+ " INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id` AND ser.`id_suc`=suc.`id`"
					+ " WHERE  per.`id_anio`=? AND per.`id_tpe`=1 ;";
			logger.info(sql);
			return sqlUtil.query(sql, new Object[] { id_anio});
		}
		
	}
	
	public List<Row> listarPeriodoVacante(Integer id_anio) {
		String sql = "select p.id,concat(tp.nom,' ',a.nom,' ',ser.nom,' ',suc.nom) as value, p.id_niv aux1 from per_periodo p, cat_tip_periodo tp,"
				+ " ges_servicio ser,ges_sucursal suc, col_anio a where p.id_tpe=tp.id and p.id_srv=ser.id and "
				+ " ser.id_suc=suc.id and p.id_anio=a.id and tp.id="+Constante.TIPO_PERIODOVAC+" and p.est='"+Constante.ESTADO+"' and p.id_anio=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_anio});	
	}
	
	public List<Row> listarPeriodoxLocalNivel(Integer id_anio, Integer id_suc, Integer id_niv) {
		String sql = "SELECT per.`id`, CONCAT(suc.nom,' ', niv.`nom`) value , per.id_niv aux1 FROM `per_periodo` per INNER JOIN `ges_sucursal` suc ON per.`id_suc`=suc.`id`"
				+ " INNER JOIN `cat_nivel` niv ON per.`id_niv`=niv.`id` WHERE per.id_anio="+id_anio+" AND per.id_suc="+id_suc+" AND per.id_niv="+id_niv+" AND per.`id_tpe`="+Constante.TIPO_PERIODOES+" AND per.est='"+Constante.ESTADO + "'";
		logger.info(sql);
		return sqlUtil.query(sql);	
	}
	
}
