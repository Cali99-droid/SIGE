package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.SucursalDAOImpl;
import com.tesla.colegio.model.Sucursal;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad sucursal.
 * @author MV
 *
 */
@Repository
public class SucursalDAO extends SucursalDAOImpl{
	
	final static Logger logger = Logger.getLogger(SucursalDAO.class);

	public List<Sucursal> listByParams(Param param, String[] order, String condicion) {

		String sql = "select * from ges_sucursal " + SQLFrmkUtil.getWhere(param) ;
		
		if (sql.indexOf("where")>0)
			sql = sql + " and " + condicion;
		
		sql = sql + SQLFrmkUtil.getOrder(order);
		
		logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Sucursal>() {

			
			public Sucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	
	
	private Sucursal rsToEntity(ResultSet rs,String alias) throws SQLException {
		Sucursal sucursal = new Sucursal();

		sucursal.setId(rs.getInt( alias + "id"));
		sucursal.setNom(rs.getString( alias + "nom"));
		sucursal.setDir(rs.getString( alias + "dir"));
		sucursal.setTel(rs.getString( alias + "tel"));
		sucursal.setCorreo(rs.getString( alias + "correo"));
		sucursal.setEst(rs.getString( alias + "est"));
								
		return sucursal;

	}

	
}
