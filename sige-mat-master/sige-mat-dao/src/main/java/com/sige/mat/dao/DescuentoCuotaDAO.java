package com.sige.mat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.DescuentoCuotaDAOImpl;

/**
 * Define m�todos DAO operations para la entidad descuento_cuota.
 * @author MV
 *
 */
@Repository
public class DescuentoCuotaDAO extends DescuentoCuotaDAOImpl{

	@Autowired
	private SQLUtil sqlUtil;
	
	public void desactivarCuota(Integer id_fdc, Integer nro_cuota){
		String sql = "update fac_descuento_cuota set est='I' WHERE id="+id_fdc+" AND nro_cuota="+nro_cuota;
		sqlUtil.update(sql);
		
	}
}
