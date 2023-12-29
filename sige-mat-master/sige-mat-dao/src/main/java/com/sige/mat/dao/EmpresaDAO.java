package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.EmpresaDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad empresa.
 * @author MV
 *
 */
@Repository
public class EmpresaDAO extends EmpresaDAOImpl{
	@Autowired
	SQLUtil sqlUtil;
	
	/**
	 * Obtener los datos del colegio o giro de negocio
	 */
	public Row datosGiroNegocio(Integer id_giro){
		String sql = "SELECT emp.`nom` empresa, emp.`ruc`, emp.`dir`, emp.`tel`, emp.corr, emp.`insignia`, gir.`nom` giro_negocio, emp.dominio "
				+ " FROM `ges_empresa` emp INNER JOIN `ges_giro_negocio` gir ON emp.`id`=gir.`id_emp`"
				+ " WHERE gir.`id`=?";
		List<Row> giro= sqlUtil.query(sql, new Object[] {id_giro});
		if(giro.size()>0)
			return giro.get(0);
		else
			return null;
	
	}	
}
