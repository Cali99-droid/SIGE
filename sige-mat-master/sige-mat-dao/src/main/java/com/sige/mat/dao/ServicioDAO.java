package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ServicioDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad servicio.
 * @author MV
 *
 */
@Repository
public class ServicioDAO extends ServicioDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaServicios() {
		
		String sql = "SELECT  ser.id, CONCAT(suc.nom,' - ',niv.nom,' - ',ggn.nom) value, niv.id aux2, suc.id aux3 "
				+ " FROM ges_servicio ser INNER JOIN ges_sucursal suc ON ser.id_suc=suc.id"
				+ " INNER JOIN cat_nivel niv ON ser.id_niv=niv.id"
				+ " INNER JOIN ges_giro_negocio ggn ON ser.id_gir=ggn.id"
				+ " ORDER BY suc.nom";
		//logger.info(sql);
		return sqlUtil.query(sql);

	}
}
