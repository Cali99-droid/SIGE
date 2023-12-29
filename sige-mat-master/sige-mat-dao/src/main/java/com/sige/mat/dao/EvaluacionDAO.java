package com.sige.mat.dao;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.EvaluacionDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad evaluacion.
 * @author MV
 *
 */
@Repository
public class EvaluacionDAO extends EvaluacionDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaNiveles(int id_tra, int id_anio, int id_gir) {
		
		String sql = "SELECT DISTINCT niv.`id`, niv.`nom` as value FROM `cat_nivel` niv INNER JOIN `per_periodo` per ON niv.`id`=per.`id_niv`"
				+ " INNER JOIN `col_aula` ca ON per.`id`=ca.`id_per`"
				+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`"
				+ " INNER JOIN `col_curso_aula` cca ON ca.`id`=cca.`id_au`"
				+ " WHERE (id_tra=? or ?=0) AND per.`id_anio`=? AND ser.id_gir=?;";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra,id_tra, id_anio, id_gir });

	}
	
	public List<Row> listaGrados(int id_tra, int id_anio, int id_niv) {
		
		String sql = "SELECT distinct g.id, g.nom as value FROM col_curso_aula cca "
				+ " INNER JOIN `col_area_anio` caa ON caa.`id`=cca.`id_caa` "
				//+ "JOIN col_curso_anio cua ON cca.id_cua=cua.id"
				//+ " INNER JOIN cat_grad g ON cua.id_gra=g.id "
				+ " INNER JOIN col_aula au ON cca.id_au=au.id "
				+ " INNER JOIN cat_grad g ON au.id_grad=g.id " 
				+ " INNER JOIN per_periodo p ON au.id_per=p.id"
				+ " WHERE cca.id_tra=? AND p.id_anio=? AND p.id_niv=? order by g.id";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_tra, id_anio, id_niv });

	}
	
	public List<Row> listarAulas(int id_tra, int id_cur, int id_gra, int id_anio, int id_niv, Integer id_nep) {
		logger.info("listarAulas->");
		String sql=" ";
		if (id_nep ==null){
			sql = "SELECT  DISTINCT cca.id, ca.secc,null eva_id,  null fec_ini, null fec_fin , null est  FROM `col_curso_aula` cca LEFT JOIN col_aula ca ON cca.id_au=ca.id "
					+ "\n LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
					+ "\n LEFT JOIN per_periodo per ON cua.id_per=per.id "
					+ "\n WHERE cca.id_tra=? AND  cua.id_cur=? AND cua.id_gra=? "		
					+ " AND per.id_anio=? AND per.id_niv=?"
					+ " order by ca.secc";
			logger.info(sql);
			return sqlUtil.query(sql, new Object[] {id_tra, id_cur, id_gra, id_anio, id_niv});	
		} else{
			sql = "SELECT cca.id, ca.secc,eva.id eva_id, eva.fec_ini, eva.fec_fin , eva.est  FROM `col_curso_aula` cca LEFT JOIN col_aula ca ON cca.id_au=ca.id "
					+ "\n LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
					+ "\n LEFT JOIN per_periodo per ON cua.id_per=per.id"
					//+ "\n LEFT JOIN not_evaluacion eva on eva.id_cca= cca.id "
					//+ "\n RIGHT JOIN not_eva_padre ep on eva.id_nep=ep.id and ep.id=?"
					+ "\nLEFT JOIN not_evaluacion eva ON eva.id_cca= cca.id AND eva.id IN "
					+ "\n(SELECT eva1.id FROM not_evaluacion eva1 INNER JOIN not_eva_padre ep ON eva1.id_nep=ep.id AND ep.id=?)"
					+ "\n WHERE cca.id_tra=? AND  cua.id_cur=? AND cua.id_gra=? "		
					+ " AND per.id_anio=? AND per.id_niv=?"
					+ " order by ca.secc";
			
			//logger.info(sql);
			return sqlUtil.query(sql, new Object[] {id_nep,id_tra, id_cur, id_gra, id_anio, id_niv});	
		}
		//creo q asi lo q pasa q a la primera vez no hay id_nep es vacio entonces 
		

	}
	
	public List<Row> listarAulasProfesor(Integer id_tra, Integer id_gra, Integer id_anio, Integer id_niv) {
		logger.info("listarAulas->");
		String sql=" ";
			sql = "SELECT  DISTINCT ca.id, ca.secc as value,null eva_id,  null fec_ini, null fec_fin , null est  FROM `col_curso_aula` cca LEFT JOIN col_aula ca ON cca.id_au=ca.id ";
			sql += "\n LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id ";
			sql += "\n LEFT JOIN per_periodo per ON cua.id_per=per.id ";
			sql += "\n WHERE cca.id_tra=? AND per.id_anio=?";
			if(id_gra!=null)
			sql += " AND cua.id_gra="+id_gra;
			if(id_niv!=null)
			sql += "  AND per.id_niv="+id_niv;
			sql += " order by ca.secc";
			logger.info(sql);
			return sqlUtil.query(sql, new Object[] {id_tra, id_anio});	
	}
	
	public List<Row> listarCursos(int id_tra, int id_anio, int id_niv, int id_gra) {
		
		String sql = "SELECT distinct cu.id , cu.nom  as value FROM col_curso_aula cca LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
				+ " LEFT JOIN cat_curso cu ON cua.id_cur=cu.id "
				+ " LEFT JOIN per_periodo per ON cua.id_per=per.id"
				+ " WHERE cca.id_tra=? AND per.id_anio=? AND per.id_niv=? and cua.id_gra=? order by cu.nom";
		//logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio, id_niv, id_gra});

	}
	
public List<Row> listarSubtemas(int id_uni) {
		
		String sql = "SELECT cus.id, cs.nom FROM col_uni_sub cus LEFT JOIN col_curso_subtema ccs ON cus.id_ccs=ccs.id"
				+ " LEFT JOIN col_subtema cs ON ccs.id_sub=cs.id WHERE cus.id_uni=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_uni});

}


public List<Row> validarFechaFin(Date fec_fin) throws Exception{
	
	String sql = "select count(id) cantidad from not_evaluacion where fec_fin=?";
	logger.info(sql);
	//Date fecha = FechaUtil.toDate(fec_fin);
	return sqlUtil.query(sql, new Object[] {fec_fin});

}

public Integer getCurso(Integer id_eva){
	
	String sql = "select cua.id_cur from not_evaluacion ne "
			+ " inner join  col_curso_aula cca on cca.id = ne.id_cca  "
			+ " inner join  col_curso_anio cua on cua.id = cca.id_cua  "
			+ "	 where ne.id=" +id_eva;

	List<Row> cursos = sqlUtil.query(sql);
	
	if (cursos.size()==0)
		return  null;
	else
		return cursos.get(0).getInteger("id_cur");
	
	
}


	public void deshabilitar(List<Integer> listaPorDesactivar){
		/*
		for (Integer id_eva: listaPorDesactivar) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("id", id_eva.getInteger("id_eva"));
			NamedParameterJdbcTemplate.update("update not_evaluacion set est='I' where id=:id ", parameters);
		}
		*/
		/*Michael
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", listaPorDesactivar);
		
		//si manejo estados, en el htmlo modal, tambien tengo q manejar estados
		//namedParameterJdbcTemplate.update("update not_evaluacion set est='I' where id in(:ids)", parameters);
		namedParameterJdbcTemplate.update("delete not_evaluacion where id in (:ids)", parameters);
		*/
		//Lina, asdiii rtampoco funciona , sale nulo
		for (Integer id_eva: listaPorDesactivar) {
			//elimina indicador aca
			jdbcTemplate.update("delete from not_ind_eva where id_ne = " + id_eva);
			jdbcTemplate.update("delete from not_evaluacion where id = " + id_eva);//asi es uno x uno
			//michael pero IGUAL SALDRA ERROR ANTES SE DEBE ELIMINAR EL INDICADOR RELACIONADO O COMO LO HICISTE??
			/*
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("ids",id_eva.getInteger("id"));//esto debe ser un arreglo, preo es un integer.. tipo de dato
			namedParameterJdbcTemplate.update("delete not_evaluacion where id = :ids", parameters);
			*/
		}
		
	}
	
	public int upFecEva (Integer id_ses, Date fec_ini, Date fec_fin, Integer id_cca) {

		// update
		String sql = "UPDATE not_evaluacion SET fec_ini=?, fec_fin=? " + "WHERE id_ses=? and id_cca=?";

		logger.info("id_ses:" + id_ses);
		logger.info("id_cca:" + id_cca);
		return jdbcTemplate.update(sql,fec_ini, fec_fin, id_ses, id_cca);

	}
	
	public List<Row> obtenerListaEvaluaciones(int id_cua) {
		
		String sql = "SELECT ne.* FROM `not_evaluacion` ne INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id`"
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " WHERE cua.`id`=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cua});

	}
}
