package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.NivelDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad nivel.
 * @author MV
 *
 */
@Repository
public class NivelDAO extends NivelDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarNivelesxGiroNegocio(Integer id_gir, Integer id_suc){

		String sql = "SELECT distinct niv.`id`, niv.`nom` value \n" ;
				sql += "FROM `ges_servicio` srv INNER JOIN `cat_nivel` niv ON srv.`id_niv`=niv.`id`\n" ;
				sql += "WHERE srv.`id_gir`=? ";
				if(id_suc!=null) {
					sql += "AND srv.`id_suc`="+id_suc;
				}
				
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{id_gir}));
	}	
	
	
}
