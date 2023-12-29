package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoHorarioSesDAOImpl;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad curso_horario_ses.
 * @author MV
 *
 */
@Repository
public class CursoHorarioSesDAO extends CursoHorarioSesDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAO.class);
	
	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Lista los cursos que lleva el alumno segun su matricula, y mes
	 * @param id_mat
	 * @param anio
	 * @param mes
	 * @return
	 */
	public List<Row> listaAgenda(Integer id_mat,Integer mes) {
		
		String sql = "SELECT t.* FROM ("
				+ "\n SELECT cchs.id extendedProps, CONCAT( DATE_FORMAT(cchs.`fec`,'%Y-%m-%d'),'T', DATE_FORMAT(cchs.`fec`,'%H:%i:%s') ) start,"
				+ "\n CONCAT(DATE_FORMAT(cchs.`fec`,'%Y-%m-%d'),'T',cch.`hora_fin`) end, CONCAT(cur.nom,'-',cts.nom) title, IF(cts.id=1,'#58D68D','#6CBBF5') color"
				+ "\n ,cchp.fec_ini_vig,cchp.fec_fin_vig"
				+ "\n FROM `col_curso_horario_ses` cchs "
				+ "\n INNER JOIN `col_curso_horario` cch ON cchs.`id_cch`=cch.`id`"
				+ "\n INNER JOIN `col_curso_horario_pad` cchp ON cch.id_cchp=cchp.id"
				+ "\n INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`"
				+ "\n INNER JOIN `col_curso_anio` cua ON cua.id=cca.`id_cua`"
				+ "\n INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id"
				+ "\n INNER JOIN mat_matricula mat ON mat.id_au_asi=cca.`id_au`"
				//+ " INNER JOIN alu_alumno alu ON mat.id_alu=alu.id"
				+ "\n INNER JOIN `col_unidad_sesion` cus ON cchs.`id_uns`=cus.`id` "
				+ "\n INNER JOIN `col_sesion_tipo` ses ON cus.`id`=ses.`id_uns`"
				+ "\n INNER JOIN cat_tipo_sesion cts ON ses.`id_cts`=cts.id"
				+ "\n WHERE mat.id=? AND  month(cchs.`fec`)=" + mes
				+ "\n )t WHERE t.start BETWEEN t.fec_ini_vig AND IFNULL(t.fec_fin_vig,'9999-12-31')";
		
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_mat});

	}
	
	public List<Row> validarHorarioSesionxSemana(Integer id_cca,Integer nro_sem) {
		
		String sql = "SELECT cchs.* "
				+ "\n FROM `col_curso_horario_ses` cchs "
				+ "\n INNER JOIN `col_curso_horario` cch ON cchs.`id_cch`=cch.`id`"
				+ "\n INNER JOIN `col_conf_semanas` ccf ON cchs.`id_ccs`=ccf.`id`"
				+ "\n INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`"
				+ "\n WHERE cca.id=? AND ccf.`nro_sem`=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cca, nro_sem});

	}
	
	/**
	 * Devuelve la lista de sesiones que ya han sido vinculadas con el horario
	 * Modulo de sesiones:Coordinador
	 * Estas sesiones seran 'bloqueadas'
	 * 
	 * @param id_uni
	 * @return
	 * @throws ServiceException
	 */
	public List<Row> listarSesionesVinculadas(Integer id_uni, Integer id_gra) {
		
		String sql = "SELECT distinct ses.`id` FROM col_unidad_sesion uns INNER JOIN `col_curso_horario_ses` cchs ON uns.`id`=cchs.`id_uns`"
				+ " INNER JOIN `col_sesion_tipo` ses ON uns.id=ses.`id_uns`"
				+ " INNER JOIN col_curso_unidad uni on uns.id_uni=uni.id"
				+ " WHERE id_uni=? and uni.id_gra=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_uni, id_gra});

	}
	
	/**
	 * Lista los subtemas x curso sesion horario
	 * @param id_cchs
	 * @return
	 */
	public List<Row> listarTemasxCursoSesion(Integer id_cchs) {
		
		String sql = "SELECT DISTINCT tem.id, tem.nom tema "
				+ " FROM col_curso_horario_ses cchs INNER JOIN col_unidad_sesion uns ON cchs.id_uns=uns.id "
				+ " INNER JOIN col_sesion_tipo ses ON uns.id=ses.id_uns "
				+ " INNER JOIN col_sesion_desempenio csd ON csd.id_ses=ses.id "
				+ " INNER JOIN col_desempenio cde ON csd.id_cde=cde.id "
				+ " INNER JOIN col_grup_capacidad cgc ON cde.id_cgc=cgc.id "
				+ " INNER JOIN col_grup_sub_padre cgsp ON cgc.id_cgsp=cgsp.id "
				+ " INNER JOIN col_grup_subtema cgs ON cgsp.id=cgs.id_cgsp "
				+ " INNER JOIN col_curso_subtema ccs ON cgs.id_ccs=ccs.id "
				+ " INNER JOIN col_subtema sub ON ccs.id_sub=sub.id "
				+ " INNER JOIN col_tema tem ON sub.id_tem=tem.id "
				+ " WHERE cchs.`id`=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cchs});

	}
	
	/**
	 * Listar Temas x curso sesion
	 * @param id_cchs
	 * @return
	 */
	public List<Row> listarSubtemasxCursoSesion(Integer id_cchs, Integer id_tem) {
		
		/*String sql = "SELECT DISTINCT sub.`id`, sub.`nom` subtema"
				+ " FROM `col_curso_horario_ses` cchs INNER JOIN `col_unidad_sesion` uns ON cchs.`id_uns`=uns.`id`"
				+ " INNER JOIN `col_curso_unidad` uni ON uns.`id_uni`=uni.`id`"
				+ " INNER JOIN `col_uni_sub` cus ON uni.`id`=cus.`id_uni`"
				+ " INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_grup_subtema` cgc ON cgsp.`id`=cgc.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgc.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.id_sub=sub.`id`"
				+ " WHERE cchs.`id`=? and id_tem=?";*/
		String sql = "SELECT DISTINCT sub.`id`, sub.`nom` subtema "
				+ " FROM `col_curso_horario_ses` cchs INNER JOIN `col_unidad_sesion` uns ON cchs.`id_uns`=uns.`id` "
				+ " INNER JOIN `col_sesion_tipo` ses ON uns.`id`=ses.`id_uns`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON csd.`id_ses`=ses.`id`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id` "
				+ " INNER JOIN `col_grup_subtema` cgs ON cgsp.`id`=cgs.`id_cgsp` "
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id` "
				+ " INNER JOIN `col_subtema` sub ON ccs.id_sub=sub.`id` "
				+ " INNER JOIN `col_tema` tem ON sub.`id_tem`=tem.`id` "
				+ " WHERE cchs.`id`=? and id_tem=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cchs,id_tem});

	}
	
	/**
	 * Lista los indicadores por curso sesion horario y subtema, usado para el detalle de la agenda
	 * @param id_cchs
	 * @param id_sub
	 * @return
	 */
	public List<Row> listarIndicadoresxSubtema(Integer id_cchs, Integer id_sub) {
		
		String sql = "SELECT ind.`id`, ind.`nom` indicador"
				+ " FROM `col_curso_horario_ses` cchs INNER JOIN `col_unidad_sesion` uns ON cchs.`id_uns`=uns.`id`"
				+ " INNER JOIN `col_sesion_tipo` ses ON uns.`id`=ses.`id_uns`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON ses.`id`=csd.`id_ses`"
				+ " INNER JOIN `col_indicador` ind ON csd.`id`=ind.`id_csd`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=`cgsp`.`id`"
				+ " INNER JOIN `col_grup_subtema` cgs ON cgsp.`id`=cgs.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ " WHERE cchs.`id`=? AND sub.`id`=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cchs,id_sub});

	}
}
