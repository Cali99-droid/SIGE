package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad curso.
 * @author MV
 *
 */
@Repository
public class CursoDAO extends CursoDAOImpl{
	final static Logger logger = Logger.getLogger(CursoDAO.class);

    @Autowired
    private SQLUtil sqlUtil;


	//Listar profesores por matricula del alumno
		public List<Row> listarCursos(Integer id_mat){
			String sql = "select  cur.id , cur.nom value"
					+ " from col_curso_aula cca"
					+ " inner join mat_matricula mat on mat.id_au_asi = cca.id_au"
					+ " inner join aeedu_asistencia.ges_trabajador tra on tra.id= cca.id_tra"
					+ " inner join col_curso_anio cua on cua.id = cca.id_cua"
					+ " inner join cat_curso cur on cur.id = cua.id_cur"
					+ " where mat.id=? and tra.est='A' "
					+ " order by cur.nom";
		
			return sqlUtil.query(sql, new Integer[]{id_mat});
		}
		
		public List<Row> listarCursosxNivel(Integer id_niv, Integer id_anio){
			String sql = "SELECT DISTINCT cur.`id`, cur.`nom` FROM `col_curso_anio` cua "
					+ " INNER JOIN `cat_curso` cur ON cua.`id_cur`=cur.`id`"
					+ " INNER JOIN `per_periodo` per ON cua.`id_per`=per.id"
					+ " WHERE per.id_niv=? AND per.id_anio=? AND per.`id_tpe`=1;";
		
			return sqlUtil.query(sql, new Integer[]{id_niv, id_anio});
		}
		
		/**
		 * Lista los datos del curso necesarios para la programacion anual
		 * @param id_cur
		 * @param id_gra
		 * @param id_anio
		 * @param id_niv
		 * @return
		 */
		public List<Row> listarDatosCurso(Integer id_cur, Integer id_gra, Integer id_anio, Integer id_niv){
			String sql = "SELECT DISTINCT gra.`nom` grado, niv.`nom` nivel, UPPER(ca.nom) area, gra.id id_gra, niv.id id_niv, cur.id id_cur, ccs.nro_ses, "
					+ " UPPER(cur.nom) curso, caa.id id_caa, ccs.`nro_ses` , tra.id id_tra, CONCAT( tra.`ape_pat`,' ', tra.`ape_mat`,' ', tra.`nom`) trabajador,au.secc"
					+ " FROM `col_curso_anio` cca "
					+ " INNER JOIN `col_area_anio` caa ON cca.`id_caa`=caa.`id`"
					+ " INNER JOIN `cat_area` ca ON caa.`id_area`=ca.`id`"
					+ " INNER JOIN `cat_curso` cur ON cca.`id_cur`=cur.`id`"
					+ " INNER JOIN `cat_grad` gra ON cca.`id_gra`= gra.`id`"
					+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
					+ " left JOIN `col_curso_sesion` ccs ON ccs.`id_cur`=cur.`id` AND niv.`id`=ccs.`id_niv` AND ccs.`id_gra`=gra.`id`  AND ccs.`id_caa`=caa.`id`"
					+ " INNER JOIN col_curso_aula cur_au ON cur_au.`id_cua`=cca.`id`"
					+ " INNER JOIN col_aula au ON au.`id`=cur_au.id_au"
					+ " INNER JOIN aeedu_asistencia.`ges_trabajador` tra ON cur_au.`id_tra`=tra.`id`"
					+ " WHERE tra.est='A' and cur.`id`=? AND gra.`id`=? AND caa.`id_anio`=? AND niv.`id`=?";
					//+ " WHERE  cur_au.id=?";
		
			return sqlUtil.query(sql, new Integer[]{id_cur, id_gra, id_anio, id_niv});
		}
		
}
