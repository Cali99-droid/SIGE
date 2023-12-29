package com.sige.mat.dao;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.ResDiarioBoletaDAOImpl;

/**
 * Define m�todos DAO operations para la entidad res_diario_boleta.
 * @author MV
 *
 */
@Repository
public class ResDiarioBoletaDAO extends ResDiarioBoletaDAOImpl{
	
	public void updateResumen(Integer id_res, Integer cant){
		jdbcTemplate.update("update fac_res_diario_boleta set estado='F', cant_reg="+cant+" where id= " + id_res);
	}
}
