package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SesionDesempenioDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad sesion_desempenio.
 * @author MV
 *
 */
@Repository
public class SesionDesempenioDAO extends SesionDesempenioDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	/**
	 *  Eliminar desempe�os x capacidad y grupo usado en la pantalla unidad sesi�n que usan los coordinadores
	 * @param id_cap
	 * @param id_cgsp
	 */
	public void deleteDesempeniosxCapacidadyGrupo(int id_cap, int id_cgsp) {
		String sql = "DELETE FROM `col_sesion_desempenio` WHERE id IN ( SELECT csd.`id` "
				+ " FROM `col_sesion_desempenio` csd INNER JOIN col_desempenio cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " WHERE cgc.`id_cap`=? AND cgc.`id_cgsp`=?)";
		
		Object[] params = new Object[]{id_cap,id_cgsp};
		
		jdbcTemplate.update(sql, params);
	}
	
	/**
	 * Lista de Sesiones Desempenio por Grupo Capacidad
	 * @param id_cgc
	 * @return
	 */
	public List<Row> listaSesionesxGrupoCapacidad(Integer id_cgc) {
		String sql = " SELECT cus.* FROM col_uni_sub cus INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cgsp.`id`=cgc.`id_cgsp`"
				+ " WHERE cgc.`id`=?";
		Object[] params = new Object[]{id_cgc};

		return sqlUtil.query(sql,params);	
	}
	
}
