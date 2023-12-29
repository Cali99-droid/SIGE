package com.sige.mat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.impl.CursoSubtemaDAOImpl;
import com.sige.web.security.TokenSeguridad;


/**
 * Define m�todos DAO operations para la entidad curso_subtema.
 * @author MV
 *
 */
@Repository
public class CursoSubtemaDAO extends CursoSubtemaDAOImpl{
	@Autowired
	private TokenSeguridad tokenSeguridad;

	/**
     * Clonar un la configuraci�n de Unidades por Periodo
     * @param idAlu
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int clonarConfiguraciones(Integer id_anio,  Integer id_anio_ant)  {
			
		String sql = "INSERT INTO `col_curso_subtema` (id_anio, id_niv, id_cur, id_sub, id_gra, dur, est, usr_ins, fec_ins)"
				+ " SELECT "+id_anio+", id_niv, id_cur, id_sub, id_gra, dur, est,"+tokenSeguridad.getId()+", NOW() FROM `col_curso_subtema`WHERE id_anio="+id_anio_ant;
		
        try {

			jdbcTemplate.update( sql);		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return 1;
	}
	
}
