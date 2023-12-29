package com.sige.mat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.sige.mat.dao.impl.DescuentoConfDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad descuento_conf.
 * @author MV
 *
 */
@Repository
public class DescuentoConfDAO extends DescuentoConfDAOImpl{
	
	public List<Row> listarDescuentos(Integer id_cct) {
		String sql = "SELECT fdc.`id`, des.`nom`, IF(fdc.`venc`='1','SI','NO') venc, fdc.`fec_venc`, IF(fdc.`acu`='1','SI','NO') acu, fdc.monto \r\n" + 
				"FROM `fac_descuento_conf` fdc INNER JOIN `col_ciclo_turno` cct ON fdc.`id_cct`=cct.`id`\r\n" + 
				"INNER JOIN cat_descuento des ON fdc.id_des=des.id "+
				//" INNER JOIN  fac_conf_pagos_ciclo fcpc ON cic.id=fcpc.id_cic \r\n"+
				"WHERE cct.`id`="+id_cct+" AND fdc.`est`='A'";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
}
