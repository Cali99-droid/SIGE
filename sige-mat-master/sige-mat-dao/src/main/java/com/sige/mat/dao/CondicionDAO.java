package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CondicionDAOImpl;
import com.tesla.frmk.sql.Row; 

/**
 * Define m�todos DAO operations para la entidad condicion.
 * @author MV
 *
 */
@Repository
public class CondicionDAO extends CondicionDAOImpl{
	@Autowired 
	SQLUtil sqlUtil ;

	public Row obtenerBloqueo(Integer id_mat,String tip) {
		List<Row> bloqueo =null;
		if(tip.equals("E")){
			String sql = "SELECT cond.id, cond.id_mat, cond.mat_blo, obs_blo FROM `mat_condicion` cond WHERE id_mat="+id_mat+" and cond.tip_blo='E'";
			 bloqueo = sqlUtil.query(sql);
		}else if(tip.equals("C")){
			String sql = "SELECT cond.id, cond.id_mat, cond.mat_blo, obs_blo FROM `mat_condicion` cond WHERE id_mat="+id_mat+" and cond.tip_blo='C'";
			 bloqueo = sqlUtil.query(sql);
		} 		
		
		if(bloqueo.size()>0)	
			return bloqueo.get(0);
		else
			return null;
	}
}
