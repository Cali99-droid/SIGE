package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.SitHistorialDAOImpl;

/**
 * Define m�todos DAO operations para la entidad sit_historial.
 * @author MV
 *
 */
@Repository
public class SitHistorialDAO extends SitHistorialDAOImpl{
	
	public boolean existsHistorial(Integer id_mat, Integer id_sit) {
		
		String sql = "select * from mat_sit_historial where id_mat =" + id_mat + " and id_sit="+id_sit;

			//logger.info(sql);

		List<Map<String,Object>> historial = jdbcTemplate.queryForList(sql);
		
		return (historial.size()>0);

	}
}
