package com.sige.mat.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PerUniDAOImpl;
import com.sige.mat.dao.impl.PeriodoCalificacionDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.PerUni;
import com.tesla.frmk.sql.Row; 

/**
 * Define m�todos DAO operations para la entidad per_uni.
 * @author MV
 *
 */
@Repository
public class PeriodoCalificacionDAO extends PeriodoCalificacionDAOImpl{
	
	@Autowired
	private TokenSeguridad tokenSeguridad;

	@Autowired
	private SQLUtil sqlUtil;
	
	public void desactivarPeriodoCalificacion(Integer id_gra, Integer id_cpu, Integer id_anio, Integer id_tca, String letra){
		String sql = "update col_periodo_calificacion set est='I' WHERE id_gra="+id_gra+" AND id_cpu="+id_cpu+" AND id_anio="+id_anio+" AND id_tca="+id_tca+" AND letra='"+letra+"'"; 
		sqlUtil.update(sql);
		
	}
	
	public void activarPeriodoCalificacion(Integer id_gra, Integer id_cpu, Integer id_anio, Integer id_tca, String letra){
		String sql = "update col_periodo_calificacion set est='A' WHERE id_gra="+id_gra+" AND id_cpu="+id_cpu+" AND id_anio="+id_anio+" AND id_tca="+id_tca+" AND letra='"+letra+"'";  
		sqlUtil.update(sql);
		
	}
	
	public List<Row> listarPeriodosCalificacionxGrado(Integer id_gra, Integer id_cpu, Integer id_anio) {
		String sql="SELECT * from col_periodo_calificacion where id_gra=? and id_cpu=? and id_anio=? and est='A'"; 
		return sqlUtil.query(sql, new Object[]{ id_gra,id_cpu,id_anio});
	}
	
	public void deletePeriodoCalificacion(Integer id_gra, Integer id_cpu, Integer id_anio, Integer id_tca) {
		String sql = "delete from col_periodo_calificacion where id_gra=? and id_cpu=? and id_anio=? and id_tca=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, new Object[]{id_gra, id_cpu, id_anio, id_tca});
	}
	
}
