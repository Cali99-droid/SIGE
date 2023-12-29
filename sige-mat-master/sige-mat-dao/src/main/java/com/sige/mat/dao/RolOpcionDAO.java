package com.sige.mat.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.RolOpcionDAOImpl;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad rol_opcion.
 * @author MV
 *
 */
@Repository
public class RolOpcionDAO extends RolOpcionDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> getNuevosRolOpcion(Integer id_rol, List<Integer> id_opcions){
		String sql = "SELECT ro.*  FROM seg_rol_opcion ro "
				+ " WHERE ro.id and ro.id_rol = :id_rol IN ro.id_opc in (:id_opcions)  ";

		Param param = new Param();
		param.put("id_rol", id_rol); 
		param.put("id_opcions", Arrays.asList(id_opcions));
		
		return sqlUtil.query(sql,param);
	}
}
