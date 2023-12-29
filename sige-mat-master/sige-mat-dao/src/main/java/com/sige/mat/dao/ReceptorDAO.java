package com.sige.mat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.ReceptorDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad receptor.
 * @author MV
 *
 */
@Repository
public class ReceptorDAO extends ReceptorDAOImpl{
	
	/**
	 * Actualizar el estado del mensaje
	 * @param id_msj
	 * @param id_rec
	 * @return
	 */
	public int actualizarEstadoMensaje(Integer id_msj, Integer id_rec, Integer id_est) {
		
		String sql = "UPDATE msj_receptor SET id_est=? WHERE id=? AND id_msj=?";
		return jdbcTemplate.update(sql,id_est,id_msj, id_rec);
	}
}
