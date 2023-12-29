package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AulaEspecialDAOImpl;
import com.tesla.colegio.model.AulaEspecial;


/**
 * Define m�todos DAO operations para la entidad aula_especial.
 * @author MV
 *
 */
@Repository
public class AulaEspecialDAO extends AulaEspecialDAOImpl{
	final static Logger logger = Logger.getLogger(AulaEspecialDAO.class);

	@Autowired
	SQLUtil sqlUtil;
	
	public AulaEspecial getByMatricula(Integer id_mat){
		List<AulaEspecial> list = sqlUtil.query("select * from col_aula_especial where id_mat=" + id_mat, AulaEspecial.class);
		if (list.size()==0)
			return null;
		else 
			return list.get(0);
	}
	
}
