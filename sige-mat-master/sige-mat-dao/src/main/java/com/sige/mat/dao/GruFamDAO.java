package com.sige.mat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.GruFamDAOImpl;
import com.tesla.colegio.model.GruFam;


/**
 * Define m�todos DAO operations para la entidad gru_fam.
 * @author MV
 *
 */
@Repository
public class GruFamDAO extends GruFamDAOImpl{
	
	@Autowired
	SQLUtil sqlUtil;
	
	public GruFam getByAlumno(Integer id_alu){
		
		String sql = "select gpf.* from alu_gru_fam gpf "
				+ "\n inner join alu_gru_fam_alumno ga on ga.id_gpf = gpf.id"
				+ "\n where ga.id_alu ="+ id_alu;
		
		return sqlUtil.queryForJavaBean(sql, GruFam.class);
	}
	
	public void actualizoEstadoEnvio(Integer id_gpf){
		String sql = "update alu_gru_fam set env_email=1 where id = ?"; 
		sqlUtil.update(sql, new Object[]{id_gpf});
	}
}
