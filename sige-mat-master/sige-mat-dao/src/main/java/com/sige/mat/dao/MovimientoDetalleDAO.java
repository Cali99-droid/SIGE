package com.sige.mat.dao;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.MovimientoDetalleDAOImpl;
import com.sige.rest.request.MovimientoDetalleReq;


/**
 * Define m�todos DAO operations para la entidad movimiento_detalle.
 * @author MV
 *
 */
@Repository
public class MovimientoDetalleDAO extends MovimientoDetalleDAOImpl{
	final static Logger logger = Logger.getLogger(MovimientoDetalleDAO.class);

    @Autowired
    private SQLUtil sqlUtil;
    
	public List<MovimientoDetalleReq> listReq(Integer id_fmo){
		
		String sql = "select d.id , d.id_fco, d.obs, d.monto, c.nom concepto from fac_movimiento_detalle d "
				+ " inner join  fac_concepto c on c.id= d.id_fco and d.id_fmo=" + id_fmo;
	
		return sqlUtil.query(sql, MovimientoDetalleReq.class);		
		
	
	}
	
}
