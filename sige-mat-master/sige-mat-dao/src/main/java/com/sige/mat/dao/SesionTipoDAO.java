package com.sige.mat.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SesionTipoDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad sesion_tipo.
 * @author MV
 *
 */
@Repository
public class SesionTipoDAO extends SesionTipoDAOImpl{
	@Autowired
    private SQLUtil sqlUtil;
	
	/**
	 * Listar los tipos de las sesiones
	 * @param id_uns
	 * @return
	 */
	public List<Row> listarSesionesTipo(Integer id_uns){
		
		String sql = "SELECT distinct cst.`id`, cts.`nom` tipo, cst.id_uns, cts.id id_cts, ne.`ins` "
				+ " FROM `col_sesion_tipo` cst "
				+ " INNER JOIN `cat_tipo_sesion` cts ON cst.`id_cts`=cts.`id` "
				+ " left JOIN not_evaluacion ne ON ne.`id_ses` = cst.`id`"
				+ " WHERE cst.`id_uns`=?";
		
		List<Row> tipo_sesion = sqlUtil.query(sql, new Object[]{ id_uns});
		
		return tipo_sesion;

	}
	
	/**
	 * Listar Grupo Subtemas por clase o repaso
	 * @param id_cst
	 * @return
	 */
	public List<Row> listarGrupoSubtemaxClase(Integer id_cst){
		
		String sql = "select distinct cgc.id_cgsp "
				+ " from col_sesion_desempenio csd inner join col_sesion_tipo ses on csd.id_ses=ses.id"
				+ " inner join col_desempenio cde on csd.id_cde=cde.id"
				+ " inner join col_grup_capacidad cgc on cde.id_cgc=cgc.id"
				+ " where ses.id=?";
		
		List<Row> grupos = sqlUtil.query(sql, new Object[]{ id_cst});
		
		return grupos;

	}
	
	/**
	 * Listar Grupo Subtemas cuando es tipo examen, (Los indicadores pertenecen a sesiones anteriores por eso se tiene q ir desde la evaluacion para llegar al grupo de subtemas)
	 * @param id_cst
	 * @return
	 */
	public List<Row> listarGrupoSubtemaxExamen(Integer id_cst){
		
		String sql = "SELECT DISTINCT cgc.id_cgsp "
				+ " FROM `not_ind_eva` nie INNER JOIN `not_evaluacion` ne ON nie.`id_ne`=ne.`id`"
				+ " INNER JOIN col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN col_sesion_desempenio csd ON ind.id_csd=csd.id "
				+ " INNER JOIN col_desempenio cde ON csd.id_cde=cde.id "
				+ " INNER JOIN col_grup_capacidad cgc ON cde.id_cgc=cgc.id "
				+ " WHERE ne.id_ses=?";
		
		List<Row> grupos = sqlUtil.query(sql, new Object[]{ id_cst});
		
		return grupos;

	}
	
	public int updateTipoSesion(Integer id_ses)  {
		String sql = "update col_sesion_tipo set id_cts=null where id=" + id_ses;
        try {
			jdbcTemplate.update(sql);
		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return 1;
	}
}
