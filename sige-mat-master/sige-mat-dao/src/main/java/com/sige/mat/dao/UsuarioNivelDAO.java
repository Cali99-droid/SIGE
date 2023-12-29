package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UsuarioNivelDAOImpl;
import com.tesla.colegio.model.Nivel; 
import com.tesla.colegio.model.UsuarioNivel;
import com.tesla.frmk.sql.Row;
 
 

/**
 * Define m�todos DAO operations para la entidad usuario_nivel.
 * @author MV
 *
 */
@Repository
public class UsuarioNivelDAO extends UsuarioNivelDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaxUsuario(Integer id_usr) {

		String sql = "select niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom ";
		sql += ", sun.id sun_id, sun.id_usr sun_id_usr , sun.id_niv sun_id_niv  ,sun.est sun_est ";
		sql = sql + " from cat_nivel niv";
		sql = sql + " left join seg_usuario_nivel sun on sun.id_niv = niv.id and sun.id_usr=" + id_usr;
 
		return sqlUtil.query(sql);

	}	
	
	public List<Row> listarNiveles(Integer id_usr) {

		String sql = "SELECT sgn.`id` id_sgn, niv.`id`, niv.`nom` nivel"
				+ " FROM `seg_usuario_nivel` sgn INNER JOIN `cat_nivel` niv ON sgn.`id_niv`=niv.`id`"
				+ " WHERE sgn.`id_usr`="+id_usr;
 
		return sqlUtil.query(sql);

	}	
	
}
