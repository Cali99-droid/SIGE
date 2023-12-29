package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.CapacidadSetupDAOImpl;


/**
 * Define m�todos DAO operations para la entidad capacidad_setup.
 * @author MV
 *
 */
@Repository
public class CapacidadSetupDAO extends CapacidadSetupDAOImpl{
	final static Logger logger = Logger.getLogger(CapacidadSetupDAO.class);

	public List<Map<String,Object>> listConfiguracion() {// esta bien el query?
	
		String sql = "SELECT co.id id_conf,g.nom grado, n.nom nivel,a.nom,suc.nom sucursal, co.cant, g.id"
				+ " FROM col_capacidad_setup co, cat_grad g,cat_nivel n, per_periodo p, ges_servicio ser, ges_sucursal suc, col_anio a"
				+ " where co.id_per=p.id and co.id_grad=g.id and g.id_nvl=n.id and p.id_srv=ser.id and ser.id_suc=suc.id"
				+ " and p.id_anio=a.id and ser.nom=n.nom order by g.id asc, suc.nom desc";
		List<Map<String,Object>> listConfiguracion = jdbcTemplate.queryForList(sql);			
		return listConfiguracion;
	} 
	
	public List<Map<String,Object>> ExisCapacidad(String id_grad,String id_per) {// esta bien el query?
	
		String sql = "select * from col_capacidad_setup where id_grad="+id_grad+" and id_per="+id_per;
		List<Map<String,Object>> listExisCap = jdbcTemplate.queryForList(sql);			
		return listExisCap;
	} 
	
	
}
