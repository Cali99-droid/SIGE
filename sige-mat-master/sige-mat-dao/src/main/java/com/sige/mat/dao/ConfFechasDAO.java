package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.ConfFechasDAOImpl;


/**
 * Define m�todos DAO operations para la entidad conf_fechas.
 * @author MV
 *
 */
@Repository
public class ConfFechasDAO extends ConfFechasDAOImpl{
	final static Logger logger = Logger.getLogger(ConfFechasDAO.class);

	/**
	 * 
	 * @param id_anio
	 * @param tipoAlumno AC: Alumno antiguo con cronograma, AS: antiguo sin cronograma, NC:Nuevo con cronograma, NS: Nuevo sin cronograma 
	 * 
	 * @return
	 */
	public boolean cronogramaVigente(Integer id_anio,String tipoAlumno){
		
		String sql = "select id from mat_conf_fechas where id_anio=? and CURRENT_TIMESTAMP() <=al and CURRENT_TIMESTAMP() >=del and tipo=?"; 
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{id_anio, tipoAlumno});
		
		return list.size()>0;
	}
	
	public boolean cambioSeccionVigente(Integer id_anio,String tipoAlumno){
		
		String sql = "select id from mat_conf_fechas where id_anio=? and CURRENT_TIMESTAMP() <=al_cs and CURRENT_TIMESTAMP() >=del_cs and tipo=?"; 
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{id_anio, tipoAlumno});
		
		return list.size()>0;
	}

}
