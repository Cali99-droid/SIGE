package com.sige.mat.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CorrelativoDAOImpl;
import com.tesla.colegio.model.Matricula;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad correlativo.
 * @author MV
 *
 */
@Repository
public class CorrelativoDAO extends CorrelativoDAOImpl{
	
	@Autowired
    private SQLUtil sqlUtil;
	
	/**
	 * Obtener el numero de adenda x local y anio
	 * @param id_suc
	 * @param id_anio
	 * @return
	 */
	public Integer obtenerCorrelativoAdenda(Integer id_suc,Integer id_anio) {
		
		String sql = "SELECT numero FROM col_correlativo WHERE id_anio="+id_anio+" and id_suc="+id_suc;
		Integer num_adenda=sqlUtil.queryForObject(sql, Integer.class);
		return num_adenda;
	}
	
	public int updateCorrelativo(Integer numero,Integer id_suc, Integer id_anio) {

		String sql = "UPDATE col_correlativo "+ "SET numero=? " + "WHERE id_suc=? and id_anio=?";

		return jdbcTemplate.update(sql,numero, id_suc, id_anio);

	}
}
