package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.TrasladoDetalleDAOImpl;

/**
 * Define m�todos DAO operations para la entidad traslado_detalle.
 * @author MV
 *
 */
@Repository
public class TrasladoDetalleDAO extends TrasladoDetalleDAOImpl{
	
	public boolean validarUltimoAnioEstudios(Integer id_mat, Integer id_alu) {
		
		String sql = "SELECT mat.id FROM `mat_matricula` mat INNER JOIN per_periodo per ON mat.id_per=per.id AND per.id_tpe=1 WHERE id_alu="+id_alu+" ORDER BY mat.id DESC limit 1;";

		
		List<Map<String,Object>> estudios = jdbcTemplate.queryForList(sql);
		
		if(id_mat.equals(estudios.get(0).get("id")))
			return true;
		else
			return false;

	}
}
