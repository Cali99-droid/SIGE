package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.PagoProgramacionDAOImpl;


/**
 * Define m�todos DAO operations para la entidad pago_programacion.
 * @author MV
 *
 */
@Repository
public class PagoProgramacionDAO extends PagoProgramacionDAOImpl{
	final static Logger logger = Logger.getLogger(PagoProgramacionDAO.class);

	public List<Map<String,Object>> listPeriodoSuc() {
		
		String sql = "SELECT p.*, ser.nom as 'servicio', suc.nom as 'local' FROM `per_periodo` p,ges_servicio ser, ges_sucursal suc "
				+ " where p.id_srv=ser.id and ser.id_suc=suc.id";
		List<Map<String,Object>> listPeriodoSuc = jdbcTemplate.queryForList(sql);			

		
		return listPeriodoSuc;
	} 
	
}
