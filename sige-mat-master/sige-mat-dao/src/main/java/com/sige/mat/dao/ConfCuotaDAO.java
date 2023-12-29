package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.ConfCuotaDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad conf_cuota.
 * @author MV
 *
 */
@Repository
public class ConfCuotaDAO extends ConfCuotaDAOImpl{
	
	public Row datosCuotaxId(Integer id_cuo) {
		String sql = "SELECT cuo.*, per.`id_niv` \n" + 
				"FROM `mat_conf_cuota` cuo INNER JOIN `per_periodo` per ON cuo.`id_per`=per.`id`\n" + 
				"WHERE cuo.`id`="+id_cuo;

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
}
