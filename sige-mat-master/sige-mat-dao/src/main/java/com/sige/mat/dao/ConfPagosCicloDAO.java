package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ConfPagosCicloDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad conf_pagos_ciclo.
 * @author MV
 *
 */
@Repository
public class ConfPagosCicloDAO extends ConfPagosCicloDAOImpl{
	
@Autowired
private SQLUtil sqlUtil;
	
public List<Row> datosDescuentoCuota(Integer id_cct) {
	String sql = "SELECT fdes.`id`, fdes.`monto`, fdes.`fec_venc`,des.nom,  fdes.`venc`, fdes.nro_cuota_max, fdes.acu \r\n" + 
			"FROM `fac_descuento_conf` fdes \r\n" + 
			"INNER JOIN cat_descuento des ON fdes.id_des=des.id "+
			"WHERE fdes.id_cct="+id_cct;
	
	/*String sql = "SELECT fdes.`id`, fdes.`monto`, fdes.`fec_venc`,fdes.nom,  fdes.`venc`, dcuo.`nro_cuota`\r\n" + 
			"FROM `fac_descuento_cuota` dcuo INNER JOIN `fac_descuento_conf` fdes ON dcuo.`id_fdes`=fdes.`id`\r\n" + 
			"WHERE fdes.id_cct="+id_cct+" AND dcuo.`nro_cuota`="+nro_cuo;*/

	return sqlUtil.query(sql);
	/*List<Row> list =  sqlUtil.query(sql);
	
	if(list.size()==0)
		return null;
	else 
		return list.get(0);*/
}
}


