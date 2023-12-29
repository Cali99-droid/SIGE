package com.sige.mat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CicloTurnoDAOImpl;

/**
 * Define m�todos DAO operations para la entidad ciclo_turno.
 * @author MV
 *
 */
@Repository
public class CicloTurnoDAO extends CicloTurnoDAOImpl{
	@Autowired
    private SQLUtil sqlUtil;
	
	public void desactivarTurno(Integer id_ctu, Integer id_tur){
		String sql = "update col_ciclo_turno set est='I' WHERE id="+id_ctu+" AND id_tur="+id_tur;
		sqlUtil.update(sql);
		
	}
	
}
