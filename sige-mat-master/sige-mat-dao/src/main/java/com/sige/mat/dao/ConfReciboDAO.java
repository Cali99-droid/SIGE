package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ConfReciboDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad conf_recibo.
 * @author MV
 *
 */
@Repository
public class ConfReciboDAO extends ConfReciboDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;

	
	public List<Row> listConfBanco(Param param, String[] order) {

		String sql = "select * from fac_conf_recibo_banco " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		return sqlUtil.query(sql);

	}	

	/**
	 * Obtiene la configuracion por numero de serie desde  fac_conf_recibo y fac_conf_recibo_banco
	 * @param serie
	 * @return
	 */
	public Row getConfPorSerie(String serie) {

		Param param = new Param();
		param.put("serie", serie);
		String sql = "select * from fac_conf_recibo" + SQLFrmkUtil.getWhere(param);
		
		List<Row> rows = sqlUtil.query(sql);
		if (rows.size()>0)
			return rows.get(0);
		else{
			sql = "select * from fac_conf_recibo_banco " + SQLFrmkUtil.getWhere(param);
			rows = sqlUtil.query(sql);
			if (rows.size()>0)
				return rows.get(0);
			else
				return null;
		}

	}	
	
	public int saveOrUpdateBanco(Row row) {

	String sql = "UPDATE fac_conf_recibo_banco "
						+ "SET " 
						+ "numero=?, "
						+ "usr_act=?,fec_act=? "
						+ "WHERE id=?";

			jdbcTemplate.update(sql, 
						row.getInteger("numero"),
						//tokenSeguridad.getId(),
						1,
						new java.util.Date(),
						row.getInteger("id")
						); 
			return row.getInteger("numero");

		
		
	}
}
