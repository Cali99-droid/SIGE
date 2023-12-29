package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ConfSemanasDAOImpl;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad conf_semanas.
 * @author MV
 *
 */
@Repository
public class ConfSemanasDAO extends ConfSemanasDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Listar por la fecha de la unidad seleccionada
	 * @param id_anio
	 * @param fec_ini
	 * @param fec_fin
	 * @return
	 */
	public List<Row> listarxUnidad(Integer id_anio, Integer sem_ini, Integer sem_fin){
		
		String sql = "SELECT ccs.* FROM col_conf_semanas ccs "
				+ " INNER JOIN col_conf_anio_escolar cca"
				+ " WHERE cca.id_anio=:id_anio and ccs.id between :sem_ini and :sem_fin ";
		
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("sem_ini", sem_ini);
		param.put("sem_fin", sem_fin);
		
		return sqlUtil.query(sql,param);
	}
}
