package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UnidadSesionDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad unidad_sesion.
 * @author MV
 *
 */
@Repository
public class UnidadSesionDAO extends UnidadSesionDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> tiposSesionDisponible(Integer id_uns){
		String sql = "select id, nom value from  cat_tipo_sesion where id not in(SELECT cst.id_cts "
				+ " FROM `col_unidad_sesion` cus INNER JOIN `col_sesion_tipo` cst ON cus.`id`=cst.`id_uns`"
				+ " WHERE cus.`id`=?) ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_uns});
	
	}
	
	public List<Row> obtenerDatos(Integer id_uni){
		String sql = "SELECT cpud.`sem_ini`, cpud.`sem_fin`,cus.id, cus.nro, cus.nom,"
				+ " (SELECT DATE_FORMAT(fec_ini,'%d-%m-%Y') FROM col_conf_semanas ccf WHERE ccf.id=cpud.sem_ini) fec_ini, (SELECT DATE_FORMAT(fec_fin,'%d-%m-%Y') FROM col_conf_semanas ccf WHERE ccf.id=cpud.sem_fin) fec_fin"
				+ " FROM `col_unidad_sesion` cus INNER JOIN `col_curso_unidad` uni ON cus.`id_uni`=uni.`id`"
				+ " INNER JOIN col_per_uni cpu ON uni.`id_cpu`=cpu.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id=cpud.id_cpu AND uni.`num`=cpud.ord"
				+ " WHERE cus.`id_uni`=? AND cus.est='A'";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_uni});
	
	}
}
