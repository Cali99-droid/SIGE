package com.sige.mat.dao;


import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UsuarioTokenDAOImpl;
import com.tesla.colegio.model.UsuarioToken;


/**
 * Define m�todos DAO operations para la entidad usuario_token.
 * @author MV
 *
 */
@Repository
public class UsuarioTokenDAO extends UsuarioTokenDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioTokenDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public UsuarioToken getByToken(String token, Date fecha){
		
		String sql = "select * from seg_usuario_token where token=? and fecha=CURDATE()";
		List<UsuarioToken> list = sqlUtil.query(sql, new Object[]{token}, UsuarioToken.class);
		
		if (list.size()==0)
			return null;
		else 
			return list.get(0);
	}
	
}
