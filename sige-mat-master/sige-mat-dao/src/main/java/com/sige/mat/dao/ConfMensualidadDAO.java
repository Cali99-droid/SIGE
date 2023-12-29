package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ConfMensualidadDAOImpl;
import com.tesla.colegio.model.ConfMensualidad;

/**
 * Define m�todos DAO operations para la entidad conf_mensualidad.
 * 
 * @author MV
 *
 */
@Repository
public class ConfMensualidadDAO extends ConfMensualidadDAOImpl {

	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Lista toda la configuraci�n por a�o
	 * @param id_anio
	 * @return
	 */
	public List<ConfMensualidad> listxAnio(Integer id_anio) {
		String sql = "select c.* from mat_conf_mensualidad c inner join per_periodo p on p.id=c.id_per where p.id_anio=?";
		return sqlUtil.query(sql, new Object[] { id_anio }, ConfMensualidad.class);
	}
}
