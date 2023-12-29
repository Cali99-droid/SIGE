package com.sige.mat.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.IndEvaDAOImpl;
import com.tesla.colegio.model.IndEva;


/**
 * Define m�todos DAO operations para la entidad ind_eva.
 * @author MV
 *
 */
@Repository
public class IndEvaDAO extends IndEvaDAOImpl{
	final static Logger logger = Logger.getLogger(IndEvaDAO.class);


	//deshabilitar los id_eva
	public void deshabilitar(List<IndEva> listEva,Integer id_ne){
		
		List<Integer> ids = new ArrayList<>();
		for (IndEva indEva : listEva) {
			ids.add(indEva.getId());
		}
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", ids);
		parameters.addValue("id_ne", id_ne);
		
		namedParameterJdbcTemplate.update("update not_ind_eva set est='I' where id_ne=:id_ne and id not in(:ids)", parameters);
	}
	
}
