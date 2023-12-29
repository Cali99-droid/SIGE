package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CompetenciaDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad competencia.
 * @author MV
 *
 */
@Repository
public class CompetenciaDAO extends CompetenciaDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;

	// lista para una grilla de competencias con capacidades
	public List<Row> listaCompetenciasCapacidad(Integer id_niv, Integer id_cur) {
		String sql = "SELECT com.nom competencia, cap.nom capacidad"
				+ " FROM col_competencia com, col_capacidad cap"
				+ " WHERE com.id=cap.id_com AND com.id_niv=? AND com.id_cur=? order by com.ord asc, cap.nom asc ";

		return sqlUtil.query(sql, new Object[] { id_niv, id_cur});
	}
	
	/**
	 * Listar las competencias de un curso usados en el presente a�o
	 * @param id_anio
	 * @param id_niv
	 * @param id_cur
	 * @param id_gra
	 * @return
	 */
		public List<Row> listaCompetenciasCursoAnioCatalogo(Integer id_anio,Integer id_niv, Integer id_cur, Integer id_gra) {
	
			String sql ="select com.id, com.nom competencia from col_competencia com  where com.id_cur=? and com.id_niv=? and est='A' " ;

			return sqlUtil.query(sql, new Object[] {  id_cur,id_niv});
		}
		
		/**
		 * Listar las competencias de un curso usados en el presente a�o
		 * SE UTILIZA PARA EL PDF DE PROGRAMACION ANUAL
		 * @param id_anio
		 * @param id_niv
		 * @param id_cur
		 * @param id_gra
		 * @return
		 */
			public List<Row> listaCompetenciasCursoAnio(Integer id_anio,Integer id_niv, Integer id_cur, Integer id_gra) {
				/*
					String sql = "SELECT 	DISTINCT com.id,com.nom competencia, com.id id_com, cap.nom capacidad, cap.id id_cap"
							+ " FROM col_curso_subtema ccs "
							+ " INNER JOIN col_grup_subtema cgs ON cgs.`id_ccs`=ccs.`id`"
							+ " INNER JOIN col_grup_sub_padre cgsp ON cgs.`id_cgsp`= cgsp.`id`"
							+ " INNER JOIN col_grup_capacidad cgp ON cgp.`id_cgsp`=cgp.`id`"
							+ " INNER JOIN col_capacidad cap ON cgp.`id_cap`=cap.`id`"
							+ " INNER JOIN col_competencia com ON cap.`id_com`=com.`id`"
							+ " WHERE ccs.id_anio=? AND ccs.id_niv=? AND ccs.`id_cur`=? AND ccs.`id_gra`=?; ";
					*/
				/*String sql="select distinct com.id, com.nom competencia"
						+ " from col_grup_subtema cgs "
						+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
						+ " inner join col_grup_capacidad cgc on cgc.id_cgsp = cgsp.id"
						+ " inner join col_capacidad cap on cap.id = cgc.id_cap"
						+ " inner join col_competencia com on com.id = cap.id_com"
						+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
						+ " inner join col_subtema sub on sub.id = ccs.id_sub"
						+ " inner join col_tema tem on tem.id = sub.id_tem"
						+ " where cgsp.id_anio=? "
						+ " and  tem.id_niv=? and tem.id_cur=? "
						+ " and ccs.id_gra=? and tem.id_anio= cgsp.id_anio";*/
				String sql ="SELECT com.`id`, com.`nom` competencia FROM `col_competencia` com WHERE com.`id_niv`=? AND com.`id_cur`=?";

				return sqlUtil.query(sql, new Object[] { id_niv, id_cur});
			}
	
			/**
			 * 
			 * @param id_anio
			 * @param id_niv
			 * @param id_cur
			 * @param id_gra
			 * @return
			 */
			public List<Row> listaCapacidadesCursoAnio(Integer id_anio,Integer id_niv, Integer id_cur, Integer id_gra,Integer id_com) {
			 			String sql="select distinct cap.id, cap.nom capacidad"
						+ " from col_grup_subtema cgs "
						+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
						+ " inner join col_grup_capacidad cgc on cgc.id_cgsp = cgsp.id"
						+ " inner join col_capacidad cap on cap.id = cgc.id_cap"
						+ " inner join col_competencia com on com.id = cap.id_com"
						+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
						+ " inner join col_subtema sub on sub.id = ccs.id_sub"
						+ " inner join col_tema tem on tem.id = sub.id_tem"
						+ " where cgsp.id_anio=? "
						+ " and  tem.id_niv=? and tem.id_cur=? "
						+ " and ccs.id_gra=? and tem.id_anio= cgsp.id_anio and com.id=?";

				return sqlUtil.query(sql, new Object[] { id_anio,id_niv, id_cur, id_gra, id_com});
			}
			
}
