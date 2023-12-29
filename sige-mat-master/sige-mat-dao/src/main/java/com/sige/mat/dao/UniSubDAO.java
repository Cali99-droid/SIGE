package com.sige.mat.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.UniSubDAOImpl;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad uni_sub.
 * @author MV
 *
 */
@Repository
public class UniSubDAO extends UniSubDAOImpl{
	final static Logger logger = Logger.getLogger(UniSubDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	/*FUNCIONA PARA EL 2018*/
	/*
	public List<Row> listaSubtemas(Integer id_cur, Integer id_niv, Integer id_gra) {//este query si ya estaba bien xq lo usabamos para la liosta de cursos
		
		String sql = "SELECT ccs.id id, concat(t.nom,' - ',sub.nom)  as value"
				+ " FROM col_curso_subtema ccs LEFT JOIN col_subtema sub ON ccs.id_sub=sub.id LEFT JOIN col_tema t on t.id=sub.id_tem"
				+ " WHERE ccs.id_cur=? AND ccs.id_niv=? AND ccs.id_gra=?";
		
		return sqlUtil.query(sql, new Object[] { id_cur, id_niv, id_gra});

	}
	*/
	
	public List<Row> listaSubtemas(Integer id_anio, Integer id_cur, Integer id_niv, Integer id_gra, String tip, Integer id_uni){
		
		
		/**
		 * Todos los subtemas disponibles
		 */
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_cur", id_cur);
		param.put("id_niv", id_niv);
		param.put("id_gra", id_gra);
		param.put("id_uni", id_uni);
		String sql="";
		if(tip.equals("E")){
			sql = "select cgsp.id id_cgsp, count(*) cant, cus.id id_cus "
					+ " from col_grup_subtema cgs "
					+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
					+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
					+ " inner join col_subtema sub on sub.id = ccs.id_sub"
					+ " left join col_uni_sub cus on cus.id_cgsp=cgsp.id and cus.est='A' "
					+ " where cgsp.id_anio=:id_anio "
					+ " and ccs.id_niv = :id_niv and ccs.id_cur = :id_cur and ccs.id_gra = :id_gra "
					+ " group by 1";
		} else if(tip.equals("N")) {
			sql = "select cgsp.id id_cgsp, count(*) cant, null id_cus "
					+ " from col_grup_subtema cgs "
					+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
					+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
					+ " inner join col_subtema sub on sub.id = ccs.id_sub"
					+ " inner join col_grup_capacidad cgc on cgc.id_cgsp=cgsp.id"//se aumento estas dos lineas xq jose dijo solo los q tienen desempe�o
					+ " inner join col_desempenio cd on cd.id_cgc=cgc.id"//esto
					+ " where cgsp.id_anio=:id_anio "
					+ " and ccs.id_niv = :id_niv and ccs.id_cur = :id_cur and ccs.id_gra = :id_gra "
					+ " and cgsp.id not in (select id_cgsp from col_uni_sub where est='A' and id_uni=:id_uni )"
					+ " group by 1";			
		}  else if(tip.equals("L")) {
			sql = "select cgsp.id id_cgsp, count(*) cant, cus.id id_cus "
					+ " from col_grup_subtema cgs "
					+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
					+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
					+ " inner join col_subtema sub on sub.id = ccs.id_sub"
					+ " inner join col_uni_sub cus on cus.id_cgsp=cgsp.id and cus.est='A' "
					+ " where cgsp.id_anio=:id_anio "
					+ " and ccs.id_niv = :id_niv and ccs.id_cur = :id_cur and ccs.id_gra = :id_gra  and cus.id_uni= :id_uni" //
					+ " group by 1";
		}
		
		
		
		
		List<Row> agrupador_subtemas = sqlUtil.query(sql,param);
		for (Row row : agrupador_subtemas) {
			Integer id_cgsp = row.getInteger("id_cgsp");	
			/**
			 * SUBTEMAS
			 */
			sql = "select gs.id_ccs, concat(tem.nom, ' - ', sub.nom) subtema from  col_grup_subtema gs "
					+ " inner join col_curso_subtema ccs on ccs.id = gs.id_ccs"
					+ " inner join col_subtema sub on ccs.id_sub = sub.id"
					+ " inner join col_tema tem on sub.id_tem= tem.id"
					+ " where gs.id_cgsp=:id_cgsp";
			param = new Param();
			param.put("id_cgsp", id_cgsp);
			List<Row> listSubtemas = sqlUtil.query(sql,param);
			
			row.put("subtemas", listSubtemas);
			
		}
		
		return agrupador_subtemas;
		 
		
	}
	
	/**
	 * Temas y subtemas del tipo L
	 * mostradas en forma jerarquizada
	 * @param id_anio
	 * @param id_cur
	 * @param id_niv
	 * @param id_gra
	 * @param id_uni
	 * @return
	 */
	public List<Row> listaTemasSubtemas(Integer id_anio, Integer id_cur, Integer id_niv, Integer id_gra,  Integer id_uni){
		
		
		/**
		 * Todos los subtemas disponibles
		 */
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_cur", id_cur);
		param.put("id_niv", id_niv);
		param.put("id_gra", id_gra);
		param.put("id_uni", id_uni);
		String sql="";
 
		sql = "select cgsp.id id_cgsp, count(*) cant, cus.id id_cus "
				+ "\n from col_grup_subtema cgs "
				+ "\n inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
				+ "\n inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
				+ "\n inner join col_subtema sub on sub.id = ccs.id_sub"
				+ "\n inner join col_uni_sub cus on cus.id_cgsp=cgsp.id and cus.est='A' "
				+ "\n where cgsp.id_anio=:id_anio "
				+ "\n and ccs.id_niv = :id_niv and ccs.id_cur = :id_cur and ccs.id_gra = :id_gra  and cus.id_uni= :id_uni" //
				+ "\n group by 1";
 
		
		
		
		
		List<Row> agrupador_subtemas = sqlUtil.query(sql,param);
		
		List<Row> temasFinales = new ArrayList<Row>();
		
		for (Row row : agrupador_subtemas) {
			Integer id_cgsp = row.getInteger("id_cgsp");	
			
			/**
			 * TEMAS
			 */
			sql = "select distinct tem.id,tem.nom tema from  col_grup_subtema gs "
					+ " inner join col_curso_subtema ccs on ccs.id = gs.id_ccs"
					+ " inner join col_subtema sub on ccs.id_sub = sub.id"
					+ " inner join col_tema tem on sub.id_tem= tem.id"
					+ " where gs.id_cgsp=" + id_cgsp;
			
			List<Row> temas= sqlUtil.query(sql);
			for (Row tem : temas) {

				/**
				 * SUBTEMAS
				 */
				sql = "select sub.id, sub.nom subtema from  col_grup_subtema gs "
						+ " inner join col_curso_subtema ccs on ccs.id = gs.id_ccs"
						+ " inner join col_subtema sub on ccs.id_sub = sub.id"
						+ " inner join col_tema tem on sub.id_tem= tem.id"
						+ " where gs.id_cgsp=:id_cgsp and sub.id_tem=:id_tem";
				param = new Param();
				param.put("id_cgsp", id_cgsp);
				param.put("id_tem", tem.getInteger("id"));
				List<Row> listSubtemas = sqlUtil.query(sql,param);
				
				tem.put("subtemas", listSubtemas);

			
				temasFinales.add(tem);
			}
			
			
			
		}
		
		return temasFinales;
		 
		
	}

	/**
     * Actualizar foto
     * @param idAlu
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int updateEstado(String estado,Integer id_uni,Integer id_cgsp, InputStream inputStream)  {
		
		String sql = "update col_uni_sub set estado= '"+estado+"' where id=_uni"+id_uni+" and " +id_cgsp;
		
		//logger.info(sql);
		
        try {
			LobHandler lobHandler = new DefaultLobHandler(); 

			jdbcTemplate.update( sql);
		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return 1;
	}
		
	public List<Row> listaSubtemasxUnidad(Integer id_anio,Integer id_niv, Integer id_gra, Integer id_cur, Integer id_uni) {
		String sql = "SELECT  DISTINCT ccs.id id , CONCAT(tem.nom,' - ',sub.`nom`) as value"
				+ "\n FROM `col_uni_sub` cus INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=`cgsp`.`id`"
				+ "\n INNER JOIN `col_grup_subtema` cgs ON cgsp.`id`=cgs.`id_cgsp`"
				+ "\n INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ "\n INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ "\n INNER JOIN col_tema tem ON sub.id_tem=tem.id"
				+ "\n WHERE ccs.`id_anio`=? AND ccs.`id_niv`=? AND ccs.`id_gra`=? AND ccs.`id_cur`=? AND cus.id_uni=?";
				//+ "\n AND des.id NOT IN (SELECT id_cde FROM col_sesion_desempenio );";
		Object[] params = new Object[]{id_anio, id_niv, id_gra, id_cur, id_uni};

		return sqlUtil.query(sql,params);	
	}
	
}