package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoUnidadDAOImpl;
import com.tesla.colegio.model.AreaCoordinador;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad curso_unidad.
 * 
 * @author MV
 *
 */
@Repository
public class CursoUnidadDAO extends CursoUnidadDAOImpl {
	final static Logger logger = Logger.getLogger(CursoUnidadDAO.class);

	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private AreaCoordinadorDAO areaCoordinadorDAO;
	
	public List<Row> listaUnidades(int id_anio,int id_niv) {

		String sql = "SELECT DISTINCT num id, concat( 'UNIDAD ',num) value FROM col_curso_unidad ccu"
				+ "	INNER JOIN col_per_uni cpu ON cpu.id = ccu.`id_cpu`"
				+ "	WHERE cpu.id_anio=? AND ccu.id_niv=? order by num";

		return sqlUtil.query(sql, new Object[] { id_anio, id_niv });

	}
	
	public Row getByAnioNivelUnidad(int id_anio,int id_niv, int num) {

		String sql = "SELECT DISTINCT cpud.sem_ini sem_ini, cpud.sem_fin"
				+ " FROM col_curso_unidad ccu"
				+ " INNER JOIN col_per_uni cpu ON cpu.id = ccu.`id_cpu`"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id=cpud.id_cpu AND ccu.`num`=cpud.ord"
				+ " WHERE cpu.id_anio=? AND ccu.id_niv=? AND ccu.num=? ";

		List<Row> rows = sqlUtil.query(sql, new Object[] { id_anio, id_niv , num });
		
		return rows.get(0);

	}
	

	
	public List<Row> listaUnidades(int id_niv, int id_gra, int id_cur, int nump) {

		String sql = "SELECT ccu.id, CONCAT('UNIDAD ',ccu.num) as value, cpud.nro_sem FROM col_curso_unidad ccu LEFT JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id = cpud.id_cpu AND cpud.`ord` = ccu.num"
				+ " WHERE ccu.id_niv=? AND ccu.id_gra=? AND (?=0 or ccu.id_cur=?) AND (?=0 or cpu.id=?)";

		return sqlUtil.query(sql, new Object[] { id_niv, id_gra, id_cur, id_cur, nump, nump });

	}
	
	/**
	 * Listar las unidades , campo num, para el reporte de unidades por docente/nivel
	 * @param id_niv
	 * @param id_gra
	 * @param nump
	 * @return
	 */
	public List<Row> listarUnidadesxGrado(int id_niv, int id_gra, int id_cpu) {

		String sql = "SELECT distinct ccu.num id, CONCAT('UNIDAD ',ccu.num) as value, cpud.nro_sem "
				+ " FROM col_curso_unidad ccu LEFT JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id = cpud.id_cpu AND cpud.`ord` = ccu.num"
				+ " WHERE ccu.id_niv=? AND ccu.id_gra=? AND (?=0 or cpu.id=?)";

		return sqlUtil.query(sql, new Object[] { id_niv, id_gra, id_cpu, id_cpu});

	}
	
	public List<Row> listaUnidadesxAnio(int id_niv, int id_gra, int id_cur, int id_anio) {

		String sql = "SELECT ccu.id, ccu.nom as value, cpud.nro_sem FROM col_curso_unidad ccu LEFT JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id = cpud.id_cpu AND cpud.`ord` = ccu.num"
				+ " WHERE ccu.id_niv=? AND ccu.id_gra=? AND ccu.id_cur=? AND cpu.id_anio=?";

		return sqlUtil.query(sql, new Object[] { id_niv, id_gra, id_cur,id_anio});

	}

	public List<Row> listaSubtemas(int id_uni) {

		String sql = "SELECT ccs.id, cs.nom as value FROM col_uni_sub cus LEFT JOIN col_curso_subtema ccs ON cus.id_ccs=ccs.id"
				+ " LEFT JOIN col_subtema cs ON ccs.id_sub=cs.id WHERE cus.id_uni=?";

		return sqlUtil.query(sql, new Object[] { id_uni });

	}

	public List<Row> listaDesempenios(int id_cpu, int id_cur, int id_anio, int num, Integer id_gra, Integer id_nep, Integer id_uni) {
		if (id_nep == null) {
			//Funcionalidad Antigua para el 2018
			/*String sql = "SELECT distinct cis.id, ci.nom indicador, sub.nom subtema, tem.nom tema "
					+ " FROM col_ind_sub cis " + " INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
					+ " INNER JOIN col_curso_subtema ccs ON cis.id_sub=ccs.id "
					+ " INNER JOIN col_subtema sub ON sub.id=ccs.id_sub"
					+ " INNER JOIN col_tema tem ON tem.id=sub.id_tem"
					+ " INNER JOIN col_curso_unidad ccu ON ccs.id_cur=ccu.id_cur AND ccs.id_niv=ccu.id_niv AND ccs.id_gra=ccu.id_gra"
					+ " INNER JOIN col_uni_sub cus ON cus.id_uni=ccu.id AND cus.id_ccs=ccs.id"
					+ " INNER JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
					+ " WHERE ccu.id_cur=? AND ccs.id_anio=? AND cpu.nump=? AND ccs.id_gra=?";*/
			
			String sql = "SELECT ind.`id`, ind.`nom` indicador"
					+ "\n FROM `col_indicador` ind INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
					+ "\n INNER JOIN `col_sesion_tipo` ses ON csd.`id_ses`=ses.`id`"
					+ "\n INNER JOIN `col_unidad_sesion` cus ON ses.`id_uns`=cus.`id`"
					+ "\n INNER JOIN `col_curso_unidad` ccu ON cus.`id_uni`=ccu.`id`"
					+ "\n INNER JOIN `col_per_uni` cpu ON ccu.`id_cpu`=cpu.`id`"
					+ "\n WHERE ccu.`id_cpu`=? AND ccu.`id_cur`=? AND ccu.`id_gra`=? AND cpu.`id_anio`=? and ccu.id=?";

			return sqlUtil.query(sql, new Object[] { id_cpu, id_cur, id_gra, id_anio, id_uni});
		} else {
			/*
			 * String sql =
			 * "SELECT distinct cis.id, ci.nom as value , IF(nie.`id` IS NOT NULL ,cis.id,'0') nie_id, nie.`est`,cis.`id_sub`"
			 * +
			 * " FROM col_ind_sub cis INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
			 * + " RIGHT JOIN `not_evaluacion` ne ON  ne.`id_nep`=?" +
			 * " LEFT JOIN not_ind_eva nie ON cis.id=nie.`id_cis` AND nie.`id_ne`= ne.id AND cis.`est`='A' "
			 * + " WHERE cis.`id_sub`=?";
			 */
			//Funcionalidad Antigua para el 2018
			/*String sql = "SELECT distinct cis.id, ci.nom indicador, sub.nom subtema, tem.nom tema, cpu.id cpu_id,  "
					+ " IF(nie.id IS NOT NULL ,cis.id,'0') nie_id, nie.est,cis.id_sub"
					+ " FROM col_ind_sub cis LEFT JOIN col_indicador ci ON cis.id_ind=ci.id"
					+ " INNER join col_curso_subtema ccs on cis.id_sub=ccs.id "
					+ " INNER join col_subtema sub on sub.id=ccs.id_sub"
					+ " INNER JOIN col_tema tem ON tem.id=sub.id_tem"
					+ " INNER join col_curso_unidad ccu on ccs.id_cur=ccu.id_cur and ccs.id_niv=ccu.id_niv and ccs.id_gra=ccu.id_gra"
					+ " INNER join col_uni_sub cus on cus.id_uni=ccu.id and cus.id_ccs=ccs.id"
					+ " INNER JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
					+ " RIGHT JOIN not_evaluacion ne ON  ne.id_nep=?"
					+ " LEFT JOIN not_ind_eva nie ON cis.id=nie.id_cis AND nie.id_ne= ne.id AND cis.est='A' "
					+ " where ccu.id_cur=? and ccs.id_anio=? AND cpu.nump=? AND ccs.id_gra=?";*/
			// logger.info(sql);
			String sql = "SELECT DISTINCT ind.id, ind.nom indicador, cpu.id cpu_id,  "
					+ " IF(nie.id IS NOT NULL ,ind.id,'0') nie_id, nie.est"
					+ " FROM col_indicador ind INNER JOIN  col_sesion_desempenio csd ON ind.`id_csd`=csd.`id`"
					+ " INNER JOIN `col_sesion_tipo` ses ON csd.`id_ses`=ses.`id`"
					+ " INNER JOIN `col_unidad_sesion` cuses ON ses.`id_uns`=cuses.`id`"
					+ " INNER JOIN `col_curso_unidad` ccu ON cuses.`id_uni`=ccu.`id`"
					+ " INNER JOIN `col_per_uni` cpu ON ccu.`id_cpu`=cpu.`id`"
					+ " RIGHT JOIN not_evaluacion ne ON  ne.id_nep=?"
					+ " LEFT JOIN not_ind_eva nie ON ind.id=nie.id_ind AND nie.id_ne= ne.id AND ind.est='A' "
					+ " WHERE ccu.id_cur=? AND cpu.id_anio=? AND cpu.nump=? AND ccu.id_gra=? and ccu.id=? ";
			return sqlUtil.query(sql, new Object[] { id_nep, id_cur, id_anio, num, id_gra, id_uni});
		}

	}
	
	/**
	 * Cantidad de unidades creadas por nivel, grado, curso y anio, usada para validaci�n
	 * @param id_niv
	 * @param id_gra
	 * @param id_cur
	 * @param id_anio
	 * @return
	 */
	public Integer cantidadUnidadesCreadas(Integer id_niv, Integer id_gra, Integer id_cur, Integer id_anio) {

		String sql = "SELECT COUNT(ccu.id) cant_uni_cre"
				+ " FROM col_curso_unidad ccu INNER JOIN col_per_uni cpu ON ccu.id_cpu=cpu.id"
				+ " WHERE ccu.id_niv=? AND ccu.id_gra=? AND ccu.id_cur=? AND cpu.id_anio=?";

		return sqlUtil.queryForObject(sql, new Object[] {id_niv, id_gra, id_cur, id_anio}, Integer.class);

	}
	
	/**
	 * Cantidad de unidades configuradas para el nivel y a�o definido, usada para valiadaci�n
	 * @param id_niv
	 * @param id_anio
	 * @return
	 */
	public Integer cantidadUnidadesConfiguradas(Integer id_niv, Integer id_anio) {

		String sql = "SELECT COUNT(cpud.id) cant_uni_conf"
				+ " FROM col_per_uni cpu INNER JOIN cat_per_aca_nivel cpa ON cpu.id_cpa=cpa.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id=cpud.`id_cpu`"
				+ " WHERE cpa.id_niv=? AND cpu.id_anio=?";

		return sqlUtil.queryForObject(sql, new Object[] {id_niv,id_anio}, Integer.class);

	}


	/**
	 * Lista las competencias por unidad (col_curso_unidad)
	 * @param id
	 * @return
	 */
	public List<Row> listaCompetenciasxCursoUnidad(Integer id_anio,Integer id) {

		String sql="select distinct com.id, com.nom competencia"
				+ " from col_grup_subtema cgs "
				+ " inner join col_grup_sub_padre cgsp on cgsp.id= cgs.id_cgsp"
				+ " inner join col_grup_capacidad cgc on cgc.id_cgsp = cgsp.id"
				+ " inner join col_capacidad cap on cap.id = cgc.id_cap"
				+ " inner join col_competencia com on com.id = cap.id_com"
				+ " inner join col_curso_subtema ccs on ccs.id = cgs.id_ccs"
				+ " inner join col_subtema sub on sub.id = ccs.id_sub"
				+ " inner join col_tema tem on tem.id = sub.id_tem"
				+ " INNER JOIN col_curso_unidad cuu ON cuu.`id_cur` = tem.id_cur AND  cuu.`id_niv` = tem.`id_niv` AND cuu.`id_gra` = ccs.`id_gra`"
				+ " WHERE "
				+ " cuu.id=?"
				+ " AND cgsp.id_anio=?"
				+ " AND tem.id_anio= cgsp.id_anio";

		return sqlUtil.query(sql, new Object[] { id,id_anio});
		
	}
	
	/**
	 * Lista las competencias por unidad (col_curso_unidad)
	 * @param id
	 * @return
	 */
	public List<Row> listaTemasxCursoUnidad(Integer id_anio,Integer id) {

		String sql="SELECT  tem.id , tem.`nom` tema"
				+ " FROM col_curso_unidad  uni"
				+ "	INNER JOIN col_tema tem ON  tem.`id_cur`= uni.`id_cur` AND tem.`id_niv`= uni.`id_niv`"
				+ " WHERE uni.id=? AND tem.`id_anio`=?"
				+ " ORDER BY tem.`ord`";

		return sqlUtil.query(sql, new Object[] { id,id_anio});
		
	}
	

	/**
	 * Lista de indicadores x subtema - unidad (PDF de unidad de aprendizaje)
	 * @param id_uni
	 * @param id_sub
	 * @return
	 */
	public List<Row> listaIndicadoresxSubtemaUnidad(Integer id_uni,Integer id_sub) {
		String sql = "SELECT uns.id, uns.nro,ind.nom  indicador"
				+ " FROM col_unidad_sesion uns"
				+ " INNER JOIN col_curso_unidad uni ON uni.id = uns.`id_uni`"
				+ " INNER JOIN col_sesion_tipo ses ON ses.`id_uns` = uns.id"
				+ " INNER JOIN col_sesion_desempenio csd ON csd.`id_ses` = ses.id"
				+ " INNER JOIN col_desempenio cde ON cde.id = csd.`id_cde` "
				+ " INNER JOIN col_grup_capacidad cgc ON cgc.id = cde.`id_cgc`"
				+ " INNER JOIN `col_grup_subtema` cgs ON cgs.`id_cgsp`=cgc.`id_cgsp` "
				+ " INNER JOIN `col_curso_subtema` ccs ON ccs.`id`=cgs.`id_ccs`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id` AND sub.`id`=?"
				+ " INNER JOIN col_indicador ind ON ind.`id_csd` = csd.`id`"
				+ " WHERE uns.id_uni =?";
		
		return sqlUtil.query(sql, new Object[] { id_sub, id_uni});
	}
	
	
	/**
	 * Listar las capaciades de una competencia 
	 * @param id_anio
	 * @param id_cuu
	 * @param id_com
	 * @return
	 */
	public List<Row> listaCapacidadesxCursoUnidad(Integer id_anio,Integer id_cuu,Integer id_com) {
			String sql="SELECT DISTINCT cap.id, cap.nom capacidad"
					+ " FROM col_grup_subtema cgs "
					+ " INNER JOIN col_grup_sub_padre cgsp ON cgsp.id= cgs.id_cgsp"
					+ " INNER JOIN col_grup_capacidad cgc ON cgc.id_cgsp = cgsp.id"
					+ " INNER JOIN col_capacidad cap ON cap.id = cgc.id_cap"
					+ " INNER JOIN col_competencia com ON com.id = cap.id_com"
					+ " INNER JOIN col_curso_subtema ccs ON ccs.id = cgs.id_ccs"
					+ " INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
					+ " INNER JOIN col_tema tem ON tem.id = sub.id_tem"
					+ " INNER JOIN col_desempenio cde ON cgc.id=cde.id_cgc"
					+ " INNER JOIN col_sesion_desempenio csd ON csd.id_cde=cde.id"
					+ " INNER JOIN `col_sesion_tipo` ses ON csd.`id_ses`=ses.`id`"
					+ " INNER JOIN `col_unidad_sesion` uns ON ses.`id_uns`=uns.`id`"
					+ " INNER JOIN col_curso_unidad cuu ON cuu.`id_cur` = tem.id_cur AND  cuu.`id_niv` = tem.`id_niv` AND cuu.`id_gra` = ccs.`id_gra` AND cuu.id=uns.`id_uni`"
					+ " WHERE cgsp.id_anio=? "
					+ " AND tem.id_anio= cgsp.id_anio AND cuu.id=? AND com.id=? ";
/*
 * + " FROM `col_capacidad` cap "
				+ " INNER JOIN `col_grup_capacidad` cgc ON cap.`id`=cgc.`id_cap`  "
				+ " INNER JOIN col_desempenio cde ON cgc.id=cde.id_cgc"
				+ " inner join col_sesion_desempenio csd on csd.id_cde=cde.id"
				+ " WHERE csd.id_ses=? and cgc.id_cgsp=?";
 * */	 			
		return sqlUtil.query(sql, new Object[] { id_anio, id_cuu, id_com});
	}
	
	
	public List<Row> listaIndicadoresXCapacidad(Integer id_uni, Integer id_cap){
		
		String sql ="SELECT uns.id, uns.nro,ind.nom  indicador"
				+ " FROM col_unidad_sesion uns"
				+ " INNER JOIN col_curso_unidad uni ON uni.id = uns.`id_uni`"
				+ " INNER JOIN col_sesion_tipo ses ON ses.`id_uns` = uns.id"
				+ " INNER JOIN col_sesion_desempenio csd ON csd.`id_ses` = ses.id"
				+ " INNER JOIN col_desempenio cde ON cde.id = csd.`id_cde` "
				+ " INNER JOIN col_grup_capacidad cgc ON cgc.id = cde.`id_cgc` AND cgc.`id_cap`=?"
				+ " INNER JOIN col_indicador ind ON ind.`id_csd` = csd.`id`"
				+ " WHERE uns.id_uni =?";
		
		return sqlUtil.query(sql, new Object[] { id_cap, id_uni});
		
	}
	
	/**
	 * Liste de sesiones por unidad (PDF unidad de aprendizaje)
	 * @param id_uni
	 * @return
	 */
	public List<Row> listaSesionesxUnidad(Integer id_uni){
		
		String sql ="SELECT uns.`id`, uns.`nro`, ses.id id_ses, cts.`nom` tipo"
				+ " FROM `col_unidad_sesion` uns INNER JOIN `col_sesion_tipo` ses ON uns.`id`=ses.`id_uns`"
				+ " INNER JOIN `cat_tipo_sesion` cts ON ses.`id_cts`=cts.`id`"
				+ " WHERE uns.`id_uni`=?";
		
		return sqlUtil.query(sql, new Object[] {id_uni});
		
	}
	
	/**
	 * indicadores por sesion (PDF unidad de aprendizaje)
	 * @param id_ses
	 * @return
	 */
	public List<Row> listaIndicadoresxSesionClase(Integer id_ses){
		
		String sql ="SELECT DISTINCT ind.`id`, ind.`nom` indicador, ses.`id`, ses.`id_uns`"
				+ " FROM `col_indicador` ind "
				+ " INNER JOIN `col_sesion_desempenio` csd on ind.`id_csd`=csd.`id`"
				+ " INNER JOIN `col_sesion_tipo` ses ON csd.`id_ses`=ses.`id`"
				+ " WHERE ses.id=?";
		
		return sqlUtil.query(sql, new Object[] {id_ses});
		
	}
	
   public List<Row> listaIndicadoresxSesionExamen(Integer id_ses){
		
		String sql ="SELECT DISTINCT ind.`id`, ind.`nom` indicador "
				+ " FROM `not_ind_eva` nie INNER JOIN `not_evaluacion` ne ON nie.`id_ne`=ne.`id`"
				+ " INNER JOIN col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN col_sesion_desempenio csd ON ind.id_csd=csd.id "
				+ " WHERE ne.`id_ses`=?";
		
		return sqlUtil.query(sql, new Object[] {id_ses});
		
	}
	
	public List<Row> listaSubtemasxSesionClase(Integer id_ses){
		String sql = "SELECT DISTINCT CONCAT (tem.`nom`,' ', sub.`nom`) campo_tematico,  csd.`id_ses`"
				+ " FROM `col_sesion_desempenio` csd INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_grup_subtema` cgs ON cgc.`id_cgsp`=cgs.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ " INNER JOIN `col_tema` tem ON sub.`id_tem`=tem.`id`"
				+ " WHERE csd.`id_ses`=?";
		
		return sqlUtil.query(sql, new Object[] {id_ses});
	}
	
	public List<Row> listaSubtemasxSesionExamen(Integer id_ses){
		String sql = "SELECT DISTINCT CONCAT (tem.`nom`,' ', sub.`nom`) campo_tematico,  ne.id_ses "
				+ " FROM `not_ind_eva` nie INNER JOIN `not_evaluacion` ne ON nie.`id_ne`=ne.`id`"
				+ " INNER JOIN col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN col_sesion_desempenio csd ON ind.id_csd=csd.id "
				+ " INNER JOIN col_desempenio cde ON csd.id_cde=cde.id "
				+ " INNER JOIN col_grup_capacidad cgc ON cde.id_cgc=cgc.id "
				+ " INNER JOIN `col_grup_subtema` cgs ON cgc.`id_cgsp`=cgs.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ " INNER JOIN `col_tema` tem ON sub.`id_tem`=tem.`id`"
				+ " WHERE ne.`id_ses`=?";
		
		return sqlUtil.query(sql, new Object[] {id_ses});
	}
	
	public List<Row> listaUnidadesTrabajador(Integer id_tra, Integer id_gra, Integer id_anio, Integer id_cur){
		String sql = " SELECT DISTINCT uni.`id` , uni.`nom`, niv.`nom` nivel, gra.`nom` grado, cur.`nom` curso, uni.`producto`, uni.num  "
				+ " FROM col_curso_unidad uni"
				+ " INNER JOIN cat_nivel niv ON niv.id = uni.id_niv "
				+ " INNER JOIN cat_grad gra ON gra.id = uni.id_gra AND gra.id_nvl=niv.id"
				+ " INNER JOIN cat_curso cur ON cur.id = uni.id_cur "
				+ " INNER JOIN col_per_uni cpu ON cpu.id = uni.id_cpu "
				+ " INNER JOIN cat_per_aca_nivel per_niv ON cpu.id_cpa=per_niv.id"
				+ " INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ " INNER JOIN col_curso_anio ca ON ca.id_cur=uni.id_cur AND ca.id_gra=gra.id "
				+ " INNER JOIN col_area_anio caa ON ca.id_caa=caa.id AND caa.id_niv=niv.id"
				+ " INNER JOIN col_area_coordinador are_cor ON are_cor.id_cur=ca.id_cur AND are_cor.id_niv=niv.id"
				+ " INNER JOIN col_unidad_sesion cus ON cus.id_uni=uni.id "
				+ " WHERE id_tra=? AND  gra.id=? AND cpu.id_anio=? AND cur.id=?"
				+ " ORDER BY niv.nom asc, gra.id asc, cur.nom asc, uni.num asc";
		
		return sqlUtil.query(sql, new Object[] {id_tra, id_gra, id_anio, id_cur});
	}
	
	/**
	 * Consultar las unidades de un curso , por nivel y grado
	 * @param id_tra
	 * @param id_anio
	 * @param id_niv
	 * @param id_gra
	 * @param id_cur
	 * @return
	 */
	public List<Row> consultarUnidades(Integer id_tra, Integer id_anio, Integer id_niv, Integer id_gra, Integer id_cur){ //id_tra, id_anio,  curso_unidad.getId_niv(), curso_unidad.getId_gra(), id_uni
		
		Param param = new Param();
		param.put("id_tra", id_tra);
		param.put("id_anio", id_anio);
		param.put("id_niv", id_niv);
		//param.put("id_gra", id_gra);
		
		List<AreaCoordinador> areaCoordinadores = areaCoordinadorDAO.listByParams(param, null);
		
		if(areaCoordinadores.size()>0){
			String sql =  "SELECT DISTINCT * FROM (";
			   sql = sql + " SELECT DISTINCT uni.`id` , uni.`nom`, niv.`nom` nivel, gra.`nom` grado, gra.id id_gra, cur.`nom` curso, uni.`producto`, uni.num, per_aca.nom per_aca_nom, cpu.nump cpu_nump  ";
			   sql = sql + "\n FROM col_curso_unidad uni";
			   sql = sql + "\n INNER JOIN cat_nivel niv ON niv.id = uni.id_niv ";
			   sql = sql + "\n INNER JOIN cat_grad gra ON gra.id = uni.id_gra AND gra.id_nvl=niv.id";
			   sql = sql + "\n INNER JOIN cat_curso cur ON cur.id = uni.id_cur ";
			   sql = sql + "\n INNER JOIN col_per_uni cpu ON cpu.id = uni.id_cpu ";
			   sql = sql + "\n INNER JOIN cat_per_aca_nivel per_niv ON cpu.id_cpa=per_niv.id";
			   sql = sql + "\n INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa";
			   sql = sql + "\n INNER JOIN col_curso_anio ca ON ca.id_cur=uni.id_cur AND ca.id_gra=gra.id ";
			   sql = sql + "\n INNER JOIN col_area_anio caa ON ca.id_caa=caa.id AND caa.id_niv=niv.id";
			   sql = sql + "\n LEFT JOIN col_area_coordinador are_cor ON are_cor.id_cur=ca.id_cur AND are_cor.id_niv=niv.id";
			   sql = sql + "\n LEFT JOIN col_curso_aula cca ON cca.`id_cua`=ca.`id` and are_cor.id_tra=cca.id_tra";
			   sql = sql + "\n INNER JOIN col_unidad_sesion cus ON cus.id_uni=uni.id ";
			   sql = sql + "\n INNER JOIN col_sesion_tipo cst ON cus.id=cst.id_uns ";
			   sql = sql + "\n WHERE are_cor.id_tra=? AND cpu.id_anio=? AND niv.id=? AND  gra.id=? ";
			  if(id_cur!=null)
				  sql = sql + " AND uni.id_cur="+id_cur;
			  sql = sql + " UNION ALL";
			  sql = sql + " SELECT DISTINCT uni.`id` , uni.`nom`, niv.`nom` nivel, gra.`nom` grado, gra.id id_gra, cur.`nom` curso, uni.`producto`, uni.num, per_aca.nom per_aca_nom, cpu.nump cpu_nump  ";
			   sql = sql + "\n FROM col_curso_unidad uni";
			   sql = sql + "\n INNER JOIN cat_nivel niv ON niv.id = uni.id_niv ";
			   sql = sql + "\n INNER JOIN cat_grad gra ON gra.id = uni.id_gra AND gra.id_nvl=niv.id";
			   sql = sql + "\n INNER JOIN cat_curso cur ON cur.id = uni.id_cur ";
			   sql = sql + "\n INNER JOIN col_per_uni cpu ON cpu.id = uni.id_cpu ";
			   sql = sql + "\n INNER JOIN cat_per_aca_nivel per_niv ON cpu.id_cpa=per_niv.id";
			   sql = sql + "\n INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa";
			   sql = sql + "\n INNER JOIN col_curso_anio ca ON ca.id_cur=uni.id_cur AND ca.id_gra=gra.id ";
			   sql = sql + "\n INNER JOIN col_area_anio caa ON ca.id_caa=caa.id AND caa.id_niv=niv.id";
			   sql = sql + "\n INNER JOIN col_curso_aula cca ON cca.`id_cua`=ca.`id`";
			   sql = sql + "\n INNER JOIN col_unidad_sesion cus ON cus.id_uni=uni.id ";
			   sql = sql + "\n INNER JOIN col_sesion_tipo cst ON cus.id=cst.id_uns ";
			   sql = sql + "\n WHERE id_tra=? AND cpu.id_anio=? AND niv.id=? AND  gra.id=? ";
			  if(id_cur!=null)
				  sql = sql + " AND uni.id_cur="+id_cur;
			   sql = sql + ")t";
		       sql = sql + " ORDER BY t.nivel asc, t.id_gra asc, t.curso asc, t.num asc";
		
		       return sqlUtil.query(sql, new Object[] {id_tra,id_anio,id_niv,id_gra,id_tra,id_anio,id_niv,id_gra});

		}else{

			String sql = " SELECT DISTINCT uni.`id` , uni.`nom`, niv.`nom` nivel, gra.`nom` grado, cur.`nom` curso, uni.`producto`, uni.num, per_aca.nom per_aca_nom, cpu.nump cpu_nump  ";
			   sql = sql + "\n FROM col_curso_unidad uni";
			   sql = sql + "\n INNER JOIN cat_nivel niv ON niv.id = uni.id_niv ";
			   sql = sql + "\n INNER JOIN cat_grad gra ON gra.id = uni.id_gra AND gra.id_nvl=niv.id";
			   sql = sql + "\n INNER JOIN cat_curso cur ON cur.id = uni.id_cur ";
			   sql = sql + "\n INNER JOIN col_per_uni cpu ON cpu.id = uni.id_cpu ";
			   sql = sql + "\n INNER JOIN cat_per_aca_nivel per_niv ON cpu.id_cpa=per_niv.id";
			   sql = sql + "\n INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa";
			   sql = sql + "\n INNER JOIN col_curso_anio ca ON ca.id_cur=uni.id_cur AND ca.id_gra=gra.id ";
			   sql = sql + "\n INNER JOIN col_area_anio caa ON ca.id_caa=caa.id AND caa.id_niv=niv.id";
			   sql = sql + "\n INNER JOIN col_curso_aula cca ON cca.`id_cua`=ca.`id`";
			   sql = sql + "\n INNER JOIN col_unidad_sesion cus ON cus.id_uni=uni.id ";
			   sql = sql + "\n INNER JOIN col_sesion_tipo cst ON cus.id=cst.id_uns ";
			   sql = sql + "\n WHERE id_tra=? AND cpu.id_anio=? AND niv.id=? AND  gra.id=? ";
			  if(id_cur!=null)
				  sql = sql + " AND uni.id_cur="+id_cur;
		       sql = sql + " ORDER BY niv.nom asc, gra.id asc, cur.nom asc, uni.num asc";
		
		       return sqlUtil.query(sql, new Object[] {id_tra,id_anio,id_niv,id_gra});

			
		}
		
	}
	
	public Integer cantidadSesionesVinculadas(Integer id_uni, Integer id_tra) {

		String sql = "SELECT COUNT(cchs.id) cantidad FROM `col_curso_horario_ses` cchs INNER JOIN col_curso_horario cch ON cchs.`id_cch`=cch.id"
				+ " INNER JOIN `col_unidad_sesion` cus ON cchs.`id_uns`=cus.id"
				+ " INNER JOIN `col_curso_unidad` uni ON cus.`id_uni`=uni.`id`INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`"
				+ " WHERE uni.`id`=? AND cca.`id_tra`=?";

		return sqlUtil.queryForObject(sql, new Object[] {id_uni, id_tra}, Integer.class);

	}
	
	public Integer cantidadSesionesConfiguradas(Integer id_uni, Integer id_tra, Integer id_anio) {

		String sql = "SELECT COUNT(cus.`id`) FROM `col_curso_aula` cca INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN `col_curso_unidad` uni ON cua.`id_gra`=uni.`id_gra` AND cua.`id_cur`=uni.`id_cur`"
				+ " INNER JOIN `col_unidad_sesion` cus ON uni.`id`=cus.`id_uni`"
				+ " INNER JOIN per_periodo per ON cua.id_per=per.id"
				+ " WHERE cus.`id_uni`=? AND cca.`id_tra`=? AND per.id_anio=?";

		return sqlUtil.queryForObject(sql, new Object[] {id_uni, id_tra, id_anio}, Integer.class);

	}
	
	public List<Row> obtenerSemanaInicioFin(Integer id_cus){
		String sql = "SELECT cpud.`sem_ini`, cpud.`sem_fin` FROM `col_unidad_sesion` cus INNER JOIN `col_curso_unidad` uni ON cus.`id_uni`=uni.`id`"
				+ " INNER JOIN col_per_uni cpu ON uni.`id_cpu`=cpu.id"
				+ " INNER JOIN col_per_uni_det cpud ON cpu.id=cpud.id_cpu AND uni.`num`=cpud.ord"
				+ " WHERE cus.`id`=?";
		
		return sqlUtil.query(sql, new Object[] {id_cus});
	}

	public List<Row> listarCursos(Integer id_niv, Integer id_gra,Integer id_cpu,Integer num) {
		String sql="SELECT cur.id, cur.nom FROM col_curso_unidad ccu "
				+ " INNER JOIN cat_curso cur ON cur.id=ccu.`id_cur`"
				+ " WHERE ccu.id_niv=? AND ccu.id_gra=? AND ccu.id_cpu=? AND ccu.num=?"
				+ " ORDER BY nom";

		return sqlUtil.query(sql, new Object[] {id_niv, id_gra,id_cpu,num});
		
	}
	
	public List<Row> listarCursosAnio(Integer id_niv, Integer id_gra, Integer id_anio) {
		String sql="SELECT DISTINCT cc.id, cc.nom FROM col_area_anio aa"
				+ " INNER JOIN per_periodo per ON per.id_anio = aa.id_anio "
				+ " INNER JOIN col_curso_anio ca ON ca.id_per = per.id AND ca.id_caa = aa.id "
				+ " INNER JOIN cat_curso cc ON cc.id = ca.id_cur "
				+ " WHERE per.id_anio=? AND per.id_niv=? AND ca.id_gra=? "
				+ " ORDER BY cc.nom";

		return sqlUtil.query(sql, new Object[] {id_anio, id_niv, id_gra});
		
	}
	
	/**
	 * Listar todos los docentes con la relacion de unidades generadas.
	 * 
	 * @param id_niv
	 * @param id_gra
	 * @param id_cpu
	 * @param num
	 * @return
	 */
	public List<Row> listarDocentesUnidades(Integer id_niv, Integer id_gra,Integer id_cpu,Integer num, Integer id_anio) {
		
	
	/*String sql="SELECT DISTINCT tra.id , tra.ape_pat,tra.ape_mat, tra.nom , ccu.id id_ccu, ccu.num, cua.`id_cur` , ccuc.id id_ccuc "
			+ "\n	FROM col_curso_aula cca"
			+ "\n	INNER JOIN col_curso_anio cua ON cua.id = cca.`id_cua` "
			+ "\n	INNER JOIN col_aula au ON au.id = cca.id_au AND cca.`id_au`= au.id "
			+ "\n	INNER JOIN per_periodo per ON per.id=au.`id_per` AND cua.id_per = per.id "
			+ "\n	INNER JOIN aeedu_asistencia.`ges_trabajador` tra ON tra.id = cca.id_tra "
			+ "\n	INNER JOIN col_curso_unidad ccu ON ccu.`id_gra` = au.`id_grad` AND ccu.`id_niv`=? AND ccu.`id_cur`= cua.`id_cur`"
			+ "\n	INNER JOIN col_per_uni cpu ON cpu.id = ccu.`id_cpu` "
			+ "\n	LEFT JOIN col_curso_unidad_control ccuc ON ccuc.id_uni= ccu.`id` AND ccuc.id_tra= tra.id "
			+ "\n	WHERE au.id_grad = ? AND cpu.id=? AND ccu.num=? AND cpu.`id_anio`= per.`id_anio` AND cua.id_gra= au.id_grad"
			+ "\n	ORDER BY tra.ape_pat,tra.ape_mat, tra.nom";*/
/*	String sql ="SELECT DISTINCT tra.id , tra.ape_pat,tra.ape_mat, tra.nom , ccu.id id_ccu, ccu.num, cua.`id_cur` , ccuc.id id_ccuc "
			+ " FROM col_curso_aula cca"
			+ " INNER JOIN col_curso_anio cua ON cua.id = cca.`id_cua` "
			+ " INNER JOIN col_aula au ON au.id = cca.id_au AND cca.`id_au`= au.id "
			+ " INNER JOIN per_periodo per ON per.id=au.`id_per` AND cua.id_per = per.id "
			+ " INNER JOIN aeedu_asistencia.`ges_trabajador` tra ON tra.id = cca.id_tra "
			+ " LEFT JOIN col_curso_unidad ccu ON ccu.`id_gra` = au.`id_grad` AND ccu.`id_niv`=per.`id_niv` AND ccu.`id_cur`= cua.`id_cur` AND ccu.num=?"
			+ " LEFT JOIN col_per_uni cpu ON cpu.id = ccu.`id_cpu` AND cpu.id=? AND cpu.`id_anio`= per.`id_anio`"
			+ " LEFT JOIN col_curso_unidad_control ccuc ON ccuc.id_uni= ccu.`id` AND ccuc.id_tra= tra.id "
			+ " WHERE au.id_grad = ? AND cua.id_gra= au.id_grad AND per.`id_niv`=? AND per.`id_anio`=?"
			+ " ORDER BY tra.ape_pat,tra.ape_mat, tra.nom" ;*/
	String sql ="SELECT DISTINCT tra.id , tra.ape_pat,tra.ape_mat, tra.nom , ccu.id id_ccu, ccu.num, cua.`id_cur` , ccuc.id id_ccuc "
			+ " FROM col_curso_aula cca"
			+ " INNER JOIN col_curso_anio cua ON cua.id = cca.`id_cua` "
			+ " INNER JOIN col_aula au ON au.id = cca.id_au AND cca.`id_au`= au.id "
			+ " INNER JOIN per_periodo per ON per.id=au.`id_per` AND cua.id_per = per.id "
			+ " INNER JOIN aeedu_asistencia.`ges_trabajador` tra ON tra.id = cca.id_tra "
			+ " INNER  JOIN col_per_uni cpu ON cpu.`id_anio`= per.`id_anio` "
			+ " LEFT JOIN col_curso_unidad ccu ON ccu.`id_gra` = au.`id_grad` AND ccu.`id_niv`=per.`id_niv` AND ccu.`id_cur`= cua.`id_cur` AND cpu.id = ccu.`id_cpu` AND ccu.num=?"
			+ " LEFT JOIN col_curso_unidad_control ccuc ON ccuc.id_uni= ccu.`id` AND ccuc.id_tra= tra.id "
			+ " WHERE au.id_grad =? AND cpu.id=?  AND cua.id_gra= au.id_grad AND per.`id_niv`=? AND per.`id_anio`=?"
			+ " ORDER BY tra.ape_pat,tra.ape_mat, tra.nom"; 
	
	//return sqlUtil.query(sql, new Object[] {id_niv, id_gra,id_cpu,num});
			return sqlUtil.query(sql, new Object[] {num,id_gra,id_cpu,id_niv,id_anio});
	}
}
