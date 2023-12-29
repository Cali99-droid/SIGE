package com.sige.mat.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.common.enums.EnumNivel;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.NotaDAOImpl;
import com.sige.rest.request.AreaNotaReq;
import com.sige.rest.request.CompetenciaNotaReq;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.Comportamiento;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.IndEva;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoCalificacion;
import com.tesla.colegio.model.PromedioCom;
import com.tesla.colegio.model.bean.AlumnoMatriculaPromBean;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define metodos DAO operations para la entidad nota.
 * 
 * @author MV
 *
 */
@Repository
public class NotaDAO extends NotaDAOImpl {
	final static Logger logger = Logger.getLogger(NotaDAO.class);

	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private TrabajadorDAO trabajadorDAO;

	@Autowired
	private MatriculaDAO matriculaDAO;

	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private GradDAO gradoDAO;

	@Autowired
	private CronogramaLibretaDAO cronogramaLibretaDAO;

	@Autowired
	private TutorAulaDAO tutorAulaDAO;
	
	@Autowired
	private ComportamientoDAO comportamiendoDAO;
	
	@Autowired
	private EvaluacionDAO evaluacionDAO;
	
	@Autowired
	private CursoExoneracionDAO cursoExoneracionDAO;
	
	@Autowired
	private DcnAreaDAO dcnAreaDAO;
	
	@Autowired
	private CompetenciaDcDAO competenciaDcDAO;
	
	@Autowired
	private PromedioComDAO promedioComDAO;
	
	@Autowired
	private CursoAnioDAO cursoAnioDAO;
	
	@Autowired
	private AreaAnioDAO areaAnioDAO;
	
	@Autowired
	private PerUniDAO perUniDAO;
	
	@Autowired
	private  CronogramaLibretaDAO cronogramaDAO;
	
	@Autowired
	private  PeriodoCalificacionDAO periodoCalificacionDAO;
	
	public List<Row> listarCursosProfesor(int id_tra, int id_anio, Integer id_au) {
		if (id_au != null) {
			String sql = "SELECT distinct cu.id , cu.nom  as value FROM col_curso_aula cca LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
					+ " LEFT JOIN cat_curso cu ON cua.id_cur=cu.id " + " LEFT JOIN per_periodo per ON cua.id_per=per.id"
					+ " WHERE cca.id_tra=? AND per.id_anio=? AND cca.id_au=?";
			return sqlUtil.query(sql, new Object[] { id_tra, id_anio, id_au });
		} else {
			return new ArrayList<Row>();
		}

	}

	public List<Row> listarEvaluaciones(Integer id_au, int nump, int id_anio, Integer id_cur) {
		if (id_au != null) {
			String sql = "SELECT ne.id, CONCAT(IFNULL(DATE_FORMAT(ne.fec_fin,'%d-%m-%Y'),''),' / ','UNIDAD ',uni.num,' / ',UPPER(ne.ins)) as value, ne.fec_fin  aux1"
					+ "\n FROM not_evaluacion ne INNER JOIN not_tip_eva nte ON ne.id_nte=nte.id"
					+ "\n INNER JOIN col_curso_aula cca ON ne.id_cca=cca.id"
					+ "\n INNER JOIN col_curso_anio cua ON cca.id_cua=cua.id"
					+ "\n INNER JOIN per_periodo per ON cua.id_per=per.id"
					+ "\n INNER JOIN `col_sesion_tipo` ses ON ne.`id_ses`=ses.`id`"
					+ "\n INNER JOIN `col_unidad_sesion` uns  ON ses.`id_uns`=uns.`id`"
					+ "\n INNER JOIN `col_curso_unidad` uni ON uns.`id_uni`=uni.`id` "
					+ "\n WHERE cca.id_au=? AND ne.nump=? AND per.id_anio=? AND cua.id_cur=? "
					+ "\n ORDER BY ne.fec_fin DESC";
			return sqlUtil.query(sql, new Object[] { id_au, nump, id_anio, id_cur });
		} else {
			return new ArrayList<Row>();
		}
	}

	public List<Row> listarIndicadores(int id_au, int nump, int id_anio, Integer id_eva) {

		if (id_eva == null)
			id_eva = 0;

		String sql = "SELECT distinct ne.id id_ne, nie.id id, ne.ins evaluacion, ind.nom indicador"
				+ "\n FROM not_evaluacion ne " + "\n inner join not_ind_eva nie ON nie.id_ne=ne.id and nie.est='A' "
				+ "\n inner JOIN col_curso_aula cca ON ne.id_cca=cca.id"
				+ "\n inner JOIN col_curso_anio cua ON cca.id_cua=cua.id"
				+ "\n inner JOIN per_periodo per ON cua.id_per=per.id"
				+ "\n inner join col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ "\n WHERE cca.id_au=? AND ne.nump=? AND per.id_anio=? and (?=0 or ne.id=? ) "
				+ "\n order by ne.ins, ind.nom";
		return sqlUtil.query(sql, new Object[] { id_au, nump, id_anio, id_eva, id_eva });

	}

	public List<Row> listarCompetencias(int id_au, int nump, int id_anio, Integer id_cur) {

		// INDICADORES
		String sql = "select ne.id id_ne, com.id id_com, com.nom competencia,ci.id id_ind, ci.nom indicador, com.peso peso"
				+ "\n from not_evaluacion ne " + "\n inner join not_ind_eva nie ON nie.id_ne=ne.id"
				+ "\n inner join col_curso_aula cca ON ne.id_cca=cca.id"
				+ "\n inner join col_curso_anio cua ON cca.id_cua=cua.id"
				+ "\n inner join per_periodo per ON cua.id_per=per.id"
				+ "\n inner join col_ind_sub cis ON nie.id_cis= cis.id"
				+ "\n inner join col_indicador ci ON cis.id_ind=ci.id"
				+ "\n inner join col_curso_subtema ccs on ccs.id = cis.id_sub" // para llegar a capacidad
				+ "\n inner join col_subtema_capacidad cuc on cuc.id_ccs =  ccs.id" 
				+ "\n inner join col_capacidad cap on cuc.id_cap = cap.id" 
				+ "\n inner join col_competencia com on com.id =  cap.id_com"
				+ "\n where cca.id_au=? AND ne.nump=? AND per.id_anio=? and cua.id_cur=?"
				+ "\n order by com.nom , ci.nom";
		return sqlUtil.query(sql, new Object[] { id_au, nump, id_anio, id_cur });

	}

	public List<Row> listarSucursal(int id_tra, int id_anio) {

		String sql = "SELECT DISTINCT suc.id, suc.nom as value FROM col_curso_aula cca LEFT JOIN col_aula ca ON cca.id_au=ca.id"
				+ " LEFT JOIN per_periodo per ON ca.id_per=per.id" + " LEFT JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " WHERE cca.id_tra=? and per.id_anio=?";

		return sqlUtil.query(sql, new Object[] { id_tra, id_anio });

	}

	public List<Row> listarAulaProfesor(int id_tra, int id_grad, int id_anio, int id_gir) {

		String sql = "SELECT distinct ca.id, ca.secc as value FROM col_curso_aula cca LEFT JOIN col_aula ca ON cca.id_au=ca.id"
				+ " LEFT JOIN per_periodo per ON ca.id_per=per.id"
				+ " LEFT JOIN ges_servicio ser ON per.id_srv=ser.id"
				+ " WHERE cca.id_tra=? AND ca.id_grad=? and per.id_anio=? AND ser.id_gir=?";

		return sqlUtil.query(sql, new Object[] { id_tra, id_grad, id_anio, id_gir });

	}

	public Map<String, Object> listarAlumnoIndicadores(int id_au, int nump, int id_anio, Integer id_eva) {

		List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
		
		Aula aula = aulaDAO.get(id_au);
		Grad grado = gradoDAO.get(aula.getId_grad());
		
		String tipo = grado.getTipo();// N: NORMAL, F: FICTICIO
				
		List<Row> indicadores = listarIndicadores(id_au, nump, id_anio, id_eva);
		for (Row row : indicadores) {

			Integer id_ind = row.getInteger("id");
			Integer id_ne = row.getInteger("id_ne");
			Evaluacion evaluacion = null;
			for (Evaluacion evaluacion1 : evaluaciones) {
				if (evaluacion1.getId().equals(id_ne)) {
					evaluacion = evaluacion1;
					evaluacion1.getIndEvas().add(new IndEva(id_ind, id_ne));
				}
			}

			if (evaluacion == null) {
				evaluacion = new Evaluacion();
				evaluacion.setId(id_ne);
				evaluacion.getIndEvas().add(new IndEva(id_ind, id_ne));
				evaluaciones.add(evaluacion);
			}

		}

		String sql = "SELECT m.id mat_id, alu.id, alu.nom, alu.ape_pat, alu.ape_mat, ccs.cod ";
		for (Evaluacion evaluacion : evaluaciones) {// PROMEDIO POR NOTA (EVALUACION)
			sql = sql + ", nn" + evaluacion.getId() + ".prom as prom_" + evaluacion.getId();
			for (IndEva indEva : evaluacion.getIndEvas()) {// NOTA POR INDICADOR
				sql = sql + ", nni" + indEva.getId() + ".id as nni" + indEva.getId() + "_id , nni" + indEva.getId()
						+ ".nota as nota_" + indEva.getId();
			}
		}

		sql = sql + "\n FROM `mat_matricula` m" + "\n inner join alu_alumno alu on alu.id = m.id_alu";
		
		if(tipo.equals("F")){
			sql = sql + "\n inner join col_aula_especial ae on ae.id_mat = m.id";
		}
		
		sql = sql + "\n inner join per_periodo per on per.id = m.id_per";

		for (Evaluacion evaluacion : evaluaciones) {// PROMEDIO POR NOTA (EVALUACION)

			sql = sql + "\n left join not_nota nn" + evaluacion.getId() + " on nn" + evaluacion.getId() + ".id_alu = alu.id";

			if (!id_eva.equals(new Integer(0)))
				sql = sql + "\n and nn" + evaluacion.getId() + ".id_ne = " + id_eva;

			sql = sql + "\n left join not_evaluacion ne" + evaluacion.getId() + " on nn" + evaluacion.getId() + ".id_ne = ne" + evaluacion.getId() + ".id";

			for (IndEva indEva : evaluacion.getIndEvas()) {// NOTA POR INDICADOR
				sql = sql + "\n left join not_ind_eva nie" + indEva.getId() + " on nie" + indEva.getId() + ".id = " + indEva.getId() + " and nie" + indEva.getId() + ".est='A' ";
				sql = sql + "\n left join not_nota_indicador nni" + indEva.getId() + " on nni" + indEva.getId() + ".id_nie= nie" + indEva.getId() + ".id and nni" + indEva.getId() + ".id_not= nn" + evaluacion.getId() + ".id";
			}

		}
		
		sql = sql + "\n left join col_situacion_mat csm ON m.id=csm.id_mat";
		sql = sql + "\n left join cat_col_situacion ccs ON csm.id_sit=ccs.id";

		if(tipo.equals("N"))
			sql = sql + "\n where m.id_au_asi=" + id_au;

		if(sql.indexOf("where")>0)
			sql = sql + "\n and per.id_anio=" + id_anio+ " and (m.id_sit<>'5' OR m.id_sit IS NULL)";
		else
			sql = sql + "\n where per.id_anio=" + id_anio+ " and (m.id_sit<>'5' OR m.id_sit IS NULL)";

		sql = sql + "\n order by alu.ape_pat, alu.ape_mat, alu.nom";

		Map<String, Object> map = new HashMap<String, Object>();

		//lISTA DE EXONERADOS POR AULA
		Integer id_cur = evaluacionDAO.getCurso(id_eva);
		//OBTENER ALUMNOS EXONERADOS
		List<Row> listaExonerados = cursoExoneracionDAO.listaExonerados(id_au, id_cur);
		
		List<Row> alumnosIndicadores = sqlUtil.query(sql);
		
		for (Row row : alumnosIndicadores) {
			Integer mat_id = row.getInteger("mat_id");
			
			for (Row rowExonerados : listaExonerados) {
				if (rowExonerados.getInteger("id").equals(mat_id))
					row.put("exonerado", "1");
			}
		}
		map.put("alumnosIndicadores", alumnosIndicadores);
		map.put("indicadores", indicadores);
		map.put("evaluaciones", evaluaciones);

		return map;
	}

	public Map<String, Object> listarPromedioxCurso(int id_au, int nump, int id_anio, Integer id_cur) {

		// EVALUACIONES
		List<Row> evaluaciones = listarEvaluaciones(id_au, nump, id_anio, id_cur);

		// COMPETENCIAS
		List<Row> competencias = listarCompetencias(id_au, nump, id_anio, id_cur);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("evaluaciones", evaluaciones);
		map.put("competencias", competencias);

		return map;

	}

	public void actualizaPromedio(Nota nota) {
		String sql = "update not_nota set prom=? where id=?";
		sqlUtil.update(sql, new Object[] { nota.getProm(), nota.getId() });
	}

	public void actualizaNotaIndicador(Integer id, Integer nota, Integer usr_act) {
		String sql = "update not_nota_indicador set nota=?, usr_act=?, fec_act=?  where id=?";
		sqlUtil.update(sql, new Object[] { nota, usr_act, new Date(), id });

	}

	public List<Row> listarNotasXAlumnoCurso(Integer id_anio,Integer id_alu, Integer id_cur, Integer nump) {
 
		String sql ="SELECT com.id id_com, com.peso, ne.`id`, ne.`fec_fin`, ne.`ins`, nni.`nota`, ne.`id_nte` tipo, ci.id id_ind, ci.nom indicador,"
				+ "\n CASE  "
				+ " WHEN nni.nota =4 THEN 'AD' "
				+ " WHEN nni.nota =3 THEN 'A' "
				+ " WHEN nni.nota =2 THEN 'B' "
				+ " ELSE 'C' END AS cualitativo"
				+ "\n FROM not_evaluacion ne"
				+ "\n LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=?"
				+ "\n INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
				+ "\n INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
				+ "\n INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ "\n INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
				+ "\n INNER JOIN col_indicador ci ON nie.id_ind=ci.id"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ci.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.id"
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id"
				+ "\n LEFT JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
				+ "\n INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca"
				+ "\n INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua"
				+ "\n WHERE ne.nump=?"
				+ "\n AND per.id_anio=?"
				+ "\n AND cua.id_cur=?"
				+ "\n AND nie.est='A'"
				+ "\n ORDER BY ne.fec_fin";
		
		return sqlUtil.query(sql, new Object[]{id_alu, nump, id_anio, id_cur});
	}

	public List<Row> listarNotasXAulaCurso(Integer id_cur, Integer id_au, Integer nump) {
		String sql = "select alu.id id_alu,alu.ape_pat, alu.ape_mat, alu.nom,"
				+ "\n com.id id_com, com.peso, com.nom competencia, ci.id id_ind, ci.nom indicador, "
				+ "\n sub.nom subtema, tem.nom tema," + "\n nni.nota,ne.fec_fin, ne.id_nte tipo from not_evaluacion ne "
				+ "\n inner join not_ind_eva nie on nie.id_ne = ne.id"
				+ "\n inner join col_ind_sub cis on cis.id = nie.id_cis"
				+ "\n inner join col_curso_subtema ccs on ccs.id = cis.id_sub"
				+ "\n inner join col_subtema sub on sub.id = ccs.id_sub"
				+ "\n inner join col_tema tem on tem.id = sub.id_tem"
				+ "\n inner join col_indicador ci ON cis.id_ind=ci.id"
				+ "\n inner join col_capacidad cap on cap.id = ci.id_cap"
				+ "\n inner join col_competencia com on com.id = cap.id_com"
				+ "\n inner join col_curso_aula cca on cca.id = ne.id_cca"
				+ "\n inner join col_curso_anio cua on cua.id = cca.id_cua"
				+ "\n inner join mat_matricula mat on mat.id_au_asi = cca.id_au"
				+ "\n inner join alu_alumno alu on alu.id = mat.id_alu"
				+ "\n left join not_nota_indicador nni on nni.id_nie= nie.id and nni.id_not= nn.id"
				+ "\n left join not_nota nn on nn.id_ne = ne.id and nn.id_alu=mat.id_alu"// 2296
				+ "\n where ne.nump=" + nump // 7
				+ "\n and cca.id_au=" + id_au// 167
				+ "\n and cua.id_cur=" + id_cur // 10
				+ "\n and nie.est='A'" + "\n order by com.ORD, ne.fec_fin,ci.nom";

		return sqlUtil.query(sql);
	}

	@SuppressWarnings("unchecked")
	public Row listarPromedioCursos(  int id_mat, int _id_cpu, int id_gir) {

		Map<String, Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(id_mat);

		Integer id_au = (Integer) matricula.get("id_au");
		Integer id_alu = (Integer) matricula.get("id_alu");
		Integer id_niv = (Integer) matricula.get("id_niv");
		Integer id_anio = (Integer) matricula.get("id_anio");
		
		List<Row> periodos = cronogramaLibretaDAO.listarPeriodosX_Nivel(id_niv, id_anio, id_gir);
		
		logger.debug(matricula);
		//profesores
		
		List<Row> listaProfesoresCursos = trabajadorDAO.listarProfesoresPorMatricula(id_mat);

		logger.debug("listaProfesoresCursos.size():" + listaProfesoresCursos.size());
		
		Map<Integer, Row> competencias = null;

		for (Row row : listaProfesoresCursos) {

				Integer id_cur = row.getInteger("cur_id");
				id_au = row.getInteger("au_id");
				List<Row> periodosNuevos = new ArrayList<Row>();

				for (Row rowPeriodo : periodos) {
					int id_cpu = rowPeriodo.getInt("id");
					
					if (_id_cpu==0 || id_cpu<=_id_cpu){//SE EVALUA HASTA EL PERIODO ELEGIDO DE LA LIBRETA
						Row curso = new Row();
						curso.put("id_cur", id_cur);
						
						if (row.get("exonerado") != null){
							logger.debug(id_mat + " ES EXONERADO");
							Row rowPeriodoNuevo = new Row();
							rowPeriodoNuevo.put("value", rowPeriodo.getString("value"));
							rowPeriodoNuevo.put("aux1", rowPeriodo.getString("aux1"));
							rowPeriodoNuevo.put("id_cpu", rowPeriodo.getInteger("id"));
							curso.put("competenciasPromedio", "EXO");
	
							rowPeriodoNuevo.put("cursos", curso);
							periodosNuevos.add(rowPeriodoNuevo);
							
						}else{
							List<Row> notasXPeriodo = listarNotasXAlumnoCurso(id_anio,id_alu, id_cur, rowPeriodo.getInteger("id"));
							
							if (notasXPeriodo.size() > 0) {
								// CLASIFICAR POR COMPETENCIAS
								competencias = listarCompetenciasXPeriodo(notasXPeriodo);
	
								logger.info("competencias.size():" + competencias.size());
								
								BigDecimal sumaCompetencias = new BigDecimal(0);
								BigDecimal totalCompetencias = new BigDecimal(0);
	
								for (Integer id_com : competencias.keySet()) {
									Row competencia = competencias.get(id_com);
									List<Row> indicadores = (List<Row>) competencia.get("indicadores");
									BigDecimal indicadoresPromedio = null;
									BigDecimal indicadoresSumaNotas = null;
									BigDecimal indicadoresCantidadNotas = new BigDecimal(0);
									BigDecimal peso = new BigDecimal(0);
	
									for (Row row2 : indicadores) {
	
										Integer inota = row2.getInteger("nota");
										if (inota != null) {
											BigDecimal nota = new BigDecimal(inota);
											if (indicadoresSumaNotas == null)
												indicadoresSumaNotas = new BigDecimal(0);
											indicadoresSumaNotas = indicadoresSumaNotas.add(nota);
											indicadoresCantidadNotas = indicadoresCantidadNotas.add(new BigDecimal(1));
										}
	
									}
	
									if (indicadores.size() > 0)
										peso = new BigDecimal(indicadores.get(0).getDouble("peso"));
	
									if (indicadores.size() > 0 && !indicadoresCantidadNotas.equals(new BigDecimal(0))) {
										// peso = new
										sumaCompetencias = sumaCompetencias.add(indicadoresSumaNotas);
										indicadoresPromedio = indicadoresSumaNotas.divide(indicadoresCantidadNotas,	RoundingMode.HALF_UP);
									}
									
									competencia.put("indicadoresPromedio", indicadoresPromedio);
									if (id_niv.intValue()==EnumNivel.INICIAL.getValue()){
										logger.debug("indicadoresPromedio:" + indicadoresPromedio);
										competencia.put("indicadoresPromedio_cualitativo", inicial_cualitativo(indicadoresPromedio));	
									}
									competencia.put("indicadoresSumaNotas", indicadoresSumaNotas);
									competencia.put("indicadoresCantidadNotas", indicadoresCantidadNotas);
									competencia.put("peso", peso);
								}
								curso.put("tieneNotas", notasXPeriodo.size() > 0);
								curso.put("competencias", competencias);
								curso.put("competenciasSuma", sumaCompetencias);
								if (notasXPeriodo.size() > 0) {
									BigDecimal competenciasSuma = new BigDecimal(0);
									for (Integer id_com : competencias.keySet()) {
										
										if (competencias.get(id_com).getBigDecimal("indicadoresPromedio") != null) {
											competenciasSuma = competenciasSuma.add(competencias.get(id_com).getBigDecimal("indicadoresPromedio").multiply(competencias.get(id_com).getBigDecimal("peso")));
											totalCompetencias = totalCompetencias.add(competencias.get(id_com).getBigDecimal("peso"));
										}
	
									}
	
									if (!totalCompetencias.equals(new BigDecimal(0))) {
										BigDecimal competenciasPromedio =competenciasSuma.divide(totalCompetencias, RoundingMode.HALF_UP); 
										curso.put("competenciasPromedio",competenciasPromedio);//sin
										if (id_niv.intValue()==EnumNivel.INICIAL.getValue()){
											curso.put("competenciasPromedio_cualitativo",inicial_cualitativo(competenciasPromedio));//sin
										}
										
									} else {
										curso.put("competenciasPromedio", null);
									}
								} else
									curso.put("competenciasPromedio", null);
								// TODAVIA NO LE INGRESAN EVALUACIONES
	
								rowPeriodo.put("cursos", curso);
	
								Row rowPeriodoNuevo = new Row();
								rowPeriodoNuevo.put("value", rowPeriodo.getString("value"));
								rowPeriodoNuevo.put("aux1", rowPeriodo.getString("aux1"));
								rowPeriodoNuevo.put("id_cpu", rowPeriodo.getInteger("id"));
								
								rowPeriodoNuevo.put("cursos", curso);
								periodosNuevos.add(rowPeriodoNuevo);
	
							} else {
								Row rowPeriodoNuevo = new Row();
								rowPeriodoNuevo.put("value", rowPeriodo.getString("value"));
								rowPeriodoNuevo.put("id_cpu", rowPeriodo.getInteger("id"));
								rowPeriodoNuevo.put("aux1", rowPeriodo.getString("aux1"));
								rowPeriodoNuevo.put("cursos", null);
								periodosNuevos.add(rowPeriodoNuevo);
	
							}
							
						}
				
					}
				}

				row.put("periodos", periodosNuevos);
 
		}

		//tutor
		Row notas = new Row();
		notas.put("listaProfesoresCursos", listaProfesoresCursos);
		
		Row tutor = tutorAulaDAO.getTutorByAula(id_au);

		if (tutor==null){
			return notas;
		}
		 Param param = new Param();
		 param.put("alu.id", id_alu);
		 param.put("per.id_anio", Integer.valueOf(id_anio));
		//param.put("", value);
		
		List<Comportamiento> comportamiento = comportamiendoDAO.listFullByParams(param, new String[] { "id_cpu" });//ordenados por periodo

		List<Row> periodosNuevos = new ArrayList<Row>();

		for (Row periodo : periodos) {
			Integer id_cpu = periodo.getInteger("id");
			if (_id_cpu==0 || id_cpu<=_id_cpu){//SE EVALUA HASTA EL PERIODO ELEGIDO DE LA LIBRETA
				Integer id_comportamiento = null;
				
				Row rowPeriodo = new Row();
				rowPeriodo.put("id_cpu", id_cpu);
				rowPeriodo.put("aux1", periodo.getString("aux1"));
				rowPeriodo.put("value", periodo.getString("value"));
	
				//obtener el id del comportamiento que le corresponde para el periodo
				for (Comportamiento comportamiento2 : comportamiento) {
					if (comportamiento2.getId_cpu().equals(id_cpu))
						id_comportamiento = comportamiento2.getId();
				}	
				
				//lista de sus capacidades
				BigDecimal promedioComportamiento = null;
				BigDecimal sumaComportamiento = new  BigDecimal(0);
				logger.debug("id_comportamiento:" + id_comportamiento);
				List<Row> capacidades = comportamiendoDAO.listNotasCapacidades(id_comportamiento);
				for (Row row : capacidades) {
					BigDecimal nota = new BigDecimal(row.getInteger("nota"));
					//String cap = row.getString("cap");
					sumaComportamiento =sumaComportamiento.add(nota);
				}
				
				if (capacidades.size()>0)
					promedioComportamiento = sumaComportamiento.divide(new BigDecimal(capacidades.size()),RoundingMode.HALF_UP);
				
				rowPeriodo.put("promedioComportamiento", promedioComportamiento);
				rowPeriodo.put("capacidades", capacidades);
				periodosNuevos.add(rowPeriodo);
			}
		}
		
		tutor.put("periodos", periodosNuevos);
		

		notas.put("tutor", tutor);
		
		
		return notas;
	}

	public List<Row> listarLibreta(int id_mat) {

		Map<String, Object> matricula = matriculaDAO.getMatriculaDatosPrincipales(id_mat);

		Integer id_alu = (Integer) matricula.get("id_alu");
		Integer id_niv = (Integer) matricula.get("id_niv");
		Integer id_anio= (Integer) matricula.get("id_anio");

		List<Row> periodos = cronogramaLibretaDAO.listaPeriodos(id_niv, id_anio); 

		List<Row> listaProfesoresCursos = trabajadorDAO.listarProfesoresPorMatricula(id_mat);

		Map<Integer, Row> competencias = null;

		for (Row row : listaProfesoresCursos) {

			Integer id_cur = row.getInteger("cur_id");
			List<Row> periodosNuevos = new ArrayList<Row>();

			for (Row rowPeriodo : periodos) {
				Row curso = new Row();
				curso.put("id_cur", id_cur);
				
				List<Row> notasXPeriodo = listarNotasXAlumnoCurso(id_anio,id_alu, id_cur, rowPeriodo.getInteger("id"));

				if (notasXPeriodo.size() > 0) {
					// CLASIFICAR POR COMPETENCIAS
					competencias = listarCompetenciasXPeriodo(notasXPeriodo);

					BigDecimal sumaCompetencias = new BigDecimal(0);
					BigDecimal totalCompetencias = new BigDecimal(0);

				
					curso.put("tieneNotas", notasXPeriodo.size() > 0);
					curso.put("competencias", competencias);
					curso.put("competenciasSuma", sumaCompetencias);
					if (notasXPeriodo.size() > 0) {
						BigDecimal competenciasSuma = new BigDecimal(0);
						for (Integer id_com : competencias.keySet()) {
				
							if (competencias.get(id_com).getBigDecimal("indicadoresPromedio") != null) {
								competenciasSuma = competenciasSuma
										.add(competencias.get(id_com).getBigDecimal("indicadoresPromedio")
												.multiply(competencias.get(id_com).getBigDecimal("peso")));
								totalCompetencias = totalCompetencias
										.add(competencias.get(id_com).getBigDecimal("peso"));
							}

						}

						if (!totalCompetencias.equals(new BigDecimal(0))) {
							curso.put("competenciasPromedio",
									competenciasSuma.divide(totalCompetencias, RoundingMode.HALF_UP));
						} else {
							curso.put("competenciasPromedio", null);// tiene nota null

						}
					} else
						curso.put("competenciasPromedio", null);
					// TODAVIA NO LE INGRESAN EVALUACIONES
					rowPeriodo.put("cursos", curso);

					Row rowPeriodoNuevo = new Row();
					rowPeriodoNuevo.put("value", rowPeriodo.getString("value"));
					rowPeriodoNuevo.put("aux1", rowPeriodo.getString("aux1"));
					rowPeriodoNuevo.put("id_cpu", rowPeriodo.getInteger("id"));
					rowPeriodoNuevo.put("cursos", curso);
					periodosNuevos.add(rowPeriodoNuevo);

				} else {
					Row rowPeriodoNuevo = new Row();
					rowPeriodoNuevo.put("value", rowPeriodo.getString("value"));
					rowPeriodoNuevo.put("aux1", rowPeriodo.getString("aux1"));
					rowPeriodoNuevo.put("id_cpu", rowPeriodo.getInteger("id"));
					rowPeriodoNuevo.put("cursos", null);
					periodosNuevos.add(rowPeriodoNuevo);
				}
			}

			row.put("periodos", periodosNuevos);
		}

		return listaProfesoresCursos;
	}

	

	@SuppressWarnings("unchecked")
	private Map<Integer, Row> listarCompetenciasXPeriodo(List<Row> notasXPeriodo) {

		Map<Integer, Row> competencias = new HashMap<Integer, Row>();

		for (Row row : notasXPeriodo) {
			Row indicadores = competencias.get(row.getInteger("id_com"));
			if (indicadores == null) {
				Row indicaroresCom = new Row();
				List<Row> listIndicadores = new ArrayList<Row>();
				listIndicadores.add(row);
				indicaroresCom.put("indicadores", listIndicadores);
				competencias.put(row.getInteger("id_com"), indicaroresCom);
			} else {
				List<Row> listaIndicadores = (List<Row>) indicadores.get("indicadores");
				listaIndicadores.add(row);
				indicadores.put("indicadores", listaIndicadores);
				competencias.put(row.getInteger("id_com"), indicadores);
			}

		}

		return competencias;
	}

	public List<Row> listarNotas(int id_cpu, int id_anio, int id_niv, String id_suc, Integer id_tra, String est, Integer id_gir) {
		String sql_suc = " ";
		String sql_fec = " ";
		String sql_tra = " ";
		String sql_est = " ";
		if (id_suc != "") {
			sql_suc = " and per.id_suc=" + id_suc;
		}
		if (id_tra!=null){
			sql_tra = " and tra.id="+id_tra;
		}
		if (est!=""){
			sql_est = " and usu.est='"+est+"'";
		}
		
		/*String sql ="SELECT DISTINCT CONCAT( persa.`ape_pat`, ' ',persa.`ape_mat`, ' ',persa.`nom`) AS docente, tra.cel, niv.`nom` nivel, gra.`nom` grado, au.`secc`, cur.`nom` curso, usu.est, DATE_FORMAT(eva_not.`fec_fin`, '%d/%m/%y') AS evaluacion, tra.`id`, eva_not.cant_notas AS cantidad"
				+ " FROM  `col_curso_aula` cca "
				+ " LEFT JOIN `ges_trabajador` tra ON cca.`id_tra`=tra.`id` "
				+ " LEFT JOIN `col_persona` persa ON persa.`id`=tra.`id_per` "
				+ " LEFT JOIN `seg_usuario` usu ON usu.`id_tra`=tra.`id`"
				+ " LEFT JOIN `seg_usuario_rol` urol ON usu.`id`=urol.`id_rol` AND urol.`id_rol`=6"
				+ " LEFT JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " LEFT JOIN `cat_curso` cur ON cua.`id_cur`=cur.`id`"
				+ " LEFT JOIN col_aula au ON cca.`id_au`=au.`id`"
				+ " LEFT JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
				+ " LEFT JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " LEFT JOIN `per_periodo` per ON au.`id_per`=per.id AND per.`id_niv`=niv.id "
				+ " LEFT JOIN (SELECT DISTINCT"
				+ " au.`id_grad` AS `gra_id`,"
				+ " `ne`.`nump` AS `nump`,"
				+ " `ne`.`id` AS `id`,"
				+ " `cca`.`id_tra` AS `id_tra`,"
				+ " `cca`.`id_au` AS `id_au`,"
				+ " `ne`.`fec_fin` AS `fec_fin`,"
				+ " `cca`.`id` AS `cca_id`, COUNT(`nni`.`nota`) AS `cant_notas`"
				+ " FROM `not_evaluacion` `ne`"
				+ " JOIN `not_ind_eva` `nie` ON `nie`.`id_ne` = `ne`.`id`"
				+ " JOIN `col_curso_aula` `cca` ON `ne`.`id_cca` = `cca`.`id`"
				+ " JOIN `col_curso_anio` `cua` ON `cca`.`id_cua` = `cua`.`id`"
				+ " JOIN `col_aula` `au` ON `cca`.`id_au` = `au`.`id`"
				+ " JOIN `per_periodo` `per` ON `cua`.`id_per` = `per`.`id`"
				+ " LEFT JOIN `not_nota` `nn` ON `nn`.`id_ne` = `ne`.`id`"
				+ " LEFT JOIN `not_nota_indicador` `nni` ON `nni`.`id_nie` = `nie`.`id` AND `nni`.`id_not` = `nn`.`id`"
				+ " GROUP BY `ne`.`id`) eva_not ON  eva_not.id_au = cca.id_au  AND eva_not.id_tra=tra.id AND eva_not.gra_id=cua.id_gra AND eva_not.cca_id=cca.id AND eva_not.nump="+id_cpu+" WHERE  per.`id_anio`="+id_anio+" AND per.`id_niv`="+id_niv
				+ " "+ sql_suc + " "+ sql_est+" "+sql_tra
				+ " ORDER BY tra.`ape_pat`, tra.`ape_mat`, tra.nom, niv.`id`,gra.`id`, au.`secc`, cur.nom, eva_not.fec_fin";*/
		String sql ="SELECT DISTINCT CONCAT( persa.`ape_pat`, ' ',persa.`ape_mat`, ' ',persa.`nom`) AS docente, persa.cel\n" + 
				", niv.`nom` nivel, gra.`nom` grado, au.`secc`\n" + 
				", are.`nom` curso, usu.est\n" + 
				",eva_not.nom evaluacion, tra.`id`, eva_not.cant_notas AS cantidad \n" + 
				"FROM `col_curso_aula` cca \n" + 
				"INNER JOIN `col_area_anio` caa  ON caa.`id`=cca.`id_caa`\n" + 
				"INNER JOIN `ges_trabajador` tra ON cca.`id_tra`=tra.`id`  \n" + 
				"INNER JOIN `col_persona` persa ON persa.`id`=tra.`id_per`  \n" + 
				"INNER JOIN `seg_usuario` usu ON usu.`id_tra`=tra.`id` \n" + 
				"INNER JOIN `seg_usuario_rol` urol ON usu.`id`=urol.`id_usr` AND urol.`id_rol`=6 \n" + 
				"INNER JOIN col_aula au ON cca.`id_au`=au.`id` \n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`  \n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id` \n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.id AND per.`id_niv`=niv.id  \n" + 
				"INNER JOIN `ges_servicio` serv ON per.`id_srv`=serv.`id`\n" + 
				"INNER JOIN `aca_dcn_area` ardc  ON caa.`id_adc`=ardc.`id`\n" + 
				"INNER JOIN `cat_area` are ON ardc.`id_are`=are.`id` \n" + 
				"LEFT JOIN (SELECT DISTINCT au.`id_grad` AS `gra_id`,des.`id_cpu`, des.id, desdc.`nom`, `ne`.`id_tra` AS `id_tra`, `des`.`id_au` AS `id_au`, com.id_dcare , cca.id id_cca, COUNT(`ne`.`nota`) AS `cant_notas` \n" + 
				"FROM not_nota_des ne INNER JOIN `aca_desempenio_aula` des ON ne.`id_desau`=des.`id`\n" + 
				"INNER JOIN `aca_desempenio_dc` desdc ON des.`id_desdc`=desdc.`id`\n" + 
				"INNER JOIN `col_aula` `au` ON `des`.`id_au` = `au`.`id` \n" + 
				"INNER JOIN `per_periodo` `per` ON `au`.`id_per` = `per`.`id` \n" + 
				"INNER JOIN `ges_servicio` serv ON per.`id_srv`=serv.`id`\n" + 
				"INNER JOIN `aca_competencia_dc` com ON desdc.`id_com`=com.`id`\n" + 
				"INNER JOIN `aca_dcn_area` dcare ON com.`id_dcare`=dcare.`id`\n" + 
				"INNER JOIN `cat_area` are ON dcare.`id_are`=are.`id`\n" + 
				"INNER JOIN `col_area_anio` caa ON caa.`id_area`=are.`id` AND caa.`id_anio`=per.`id_anio` AND caa.`id_gra`=au.`id_grad` AND caa.`id_gir`=serv.`id_gir` AND caa.`id_adc`=dcare.`id`\n" + 
				"INNER JOIN `col_curso_aula` cca ON cca.`id_caa`=caa.`id` AND cca.`id_tra`=ne.`id_tra`\n" + 
				"LEFT JOIN `col_curso_anio` `cua` ON `des`.`id_cua` = `cua`.`id` \n" + 
				"WHERE per.`id_anio`="+id_anio+" AND serv.`id_gir`="+id_gir+" AND des.`id_cpu`="+id_cpu+"\n" + 
				"GROUP BY des.`id`) eva_not ON  eva_not.id_au = cca.id_au  AND eva_not.id_tra=tra.id AND eva_not.gra_id=gra.id AND eva_not.id_cca=cca.id AND eva_not.id_cpu=54 \n" + 
				"WHERE  per.`id_anio`="+id_anio+" AND per.`id_niv`="+id_niv+" AND serv.id_gir="+id_gir+"      \n"+  
				 " "+ sql_suc + " "+ sql_est+" "+sql_tra+" "+
				"ORDER BY persa.`ape_pat`, persa.`ape_mat`, persa.nom, niv.`id`,gra.`id`, au.`secc`, are.nom, eva_not.nom";
		/*String sql = " SELECT * FROM ("
				+ "\n SELECT DISTINCT concat( tra.`ape_pat`, ' ',tra.`ape_mat`, ' ',tra.`nom`) as docente, tra.cel, niv.`nom` nivel, gra.`nom` grado, au.`secc`, cur.`nom` curso, usu.est, eva_not.`nep_id`,"
				+ " \n DATE_FORMAT(eva_not.`fec_fin`, '%d/%m/%y') AS evaluacion, tra.`id`, eva_not.cant_notas AS cantidad, eva_not.tipo as tipo  "
				+ " \n FROM  `col_curso_aula` cca "
				+ " \n LEFT JOIN `aeedu_asistencia`.`ges_trabajador` tra ON cca.`id_tra`=tra.`id` "
				+ " \n LEFT JOIN `seg_usuario` usu ON usu.`id_tra`=tra.`id`"
				+ " \n LEFT JOIN `seg_usuario_rol` urol ON usu.`id`=urol.`id_rol` AND urol.`id_rol`=6"
				+ " \n LEFT JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " \n LEFT JOIN `cat_curso` cur ON cua.`id_cur`=cur.`id`"
				+ " \n LEFT JOIN col_aula au ON cca.`id_au`=au.`id`"
				+ " \n LEFT JOIN `cat_grad` gra ON au.`id_grad`=gra.`id` "
				+ " \n LEFT JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " \n LEFT JOIN `per_periodo` per ON au.`id_per`=per.id AND per.`id_niv`=niv.id "
				+ " \n LEFT JOIN evaluacion_nota eva_not ON  eva_not.id_au = cca.id_au AND eva_not.id_tra=tra.id and eva_not.gra_id=cua.id_gra and eva_not.cca_id=cca.id AND eva_not.nump="
				+ id_cpu + " \n WHERE  per.`id_anio`=" + id_anio + " " + sql_suc + " "+ sql_est+" \n AND per.`id_niv`=" + id_niv
				+ " " + sql_fec+" "+sql_tra
				+ " \n ORDER BY tra.`ape_pat`, tra.`ape_mat`, tra.nom, niv.`id`,gra.`id`, au.`secc`, cur.nom, eva_not.fec_fin) y"
				+ " \n WHERE y.cantidad is null OR  cantidad=0";*/

		return sqlUtil.query(sql);

	}
	
	/**
	 * 
	 * @param id_au
	 * @param id_cup
	 * @param tipo N normal o F: Ficticio
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AlumnoMatriculaPromBean>  promediosPorAula(Integer id_au,Integer id_cpu, String tipo, Integer id_gir){
	
		List<Row> id_mats = aulaDAO.listMatriculados(id_au, tipo);
		
		List<AlumnoMatriculaPromBean> promediosAlumno = new ArrayList<AlumnoMatriculaPromBean>();
		List<AlumnoMatriculaPromBean> promediosAlumnoDinamico = new ArrayList<AlumnoMatriculaPromBean>();
		
		
		for (Row id_mat : id_mats) {
			
			Row promedios = listarPromedioCursos(id_mat.getInteger("id"),id_cpu, id_gir);	
			List<Row> cursos = (List<Row>)promedios.get("listaProfesoresCursos");
			Integer cantidadCursos = 0;

			BigDecimal promedio ;
			Integer sumaPromedio = 0;

			for (Row row : cursos) {
				List<Row> periodos = (List<Row>)row.get("periodos");

				Row periodo = null;
				for (Row row2 : periodos) {
					if (row2.getInteger("id_cpu").equals(id_cpu))
						periodo = row2;
				}

				Row curso1 = (Row)periodo.get("cursos");
				Integer promedioInt = null;
				if (curso1!=null && !"EXO".equals(curso1.getString("competenciasPromedio"))){
						BigDecimal promedioCurso = curso1.getBigDecimal("competenciasPromedio");
						if(promedioCurso!=null){
							cantidadCursos++;
							promedioInt = promedioCurso.intValue();
							sumaPromedio = sumaPromedio + promedioInt;
						}
					}
				}
			if (cantidadCursos==0)
				logger.info("promedio de " + id_mat + "NO TIENE CURSOS");
			else{
				promedio = new BigDecimal(sumaPromedio).divide(new BigDecimal(cantidadCursos),2, RoundingMode.HALF_UP);
				
				logger.info("promedio de " + id_mat + ":es:" + promedio);
				
				if (promediosAlumnoDinamico.size()==0){
					promediosAlumnoDinamico.add(new AlumnoMatriculaPromBean(id_mat.getInteger("id"), id_mat.getString("nombres"), promedio));
	
				}else{
					//ordenar
					int i = 0;
					boolean menor  = false;
	
					for (AlumnoMatriculaPromBean promedio_alumno : promediosAlumno) {	
						
						if (promedio_alumno.getPromedio().compareTo(promedio)>0 && !menor){
							promediosAlumnoDinamico.add(i,new AlumnoMatriculaPromBean(id_mat.getInteger("id"),id_mat.getString("nombres"), promedio));
							menor  = true;
						}
						i++;
					}
					
					if (!menor)
						promediosAlumnoDinamico.add(new AlumnoMatriculaPromBean(id_mat.getInteger("id"),id_mat.getString("nombres"), promedio));
				}
				promediosAlumno = new ArrayList<AlumnoMatriculaPromBean>(promediosAlumnoDinamico.size());
				
				promediosAlumno.addAll(promediosAlumnoDinamico);
			}
		}
		
		//asignar puestos
		int puesto = 1;
		for (int i = promediosAlumno.size()-1; i > 0; i--) {
			if (promediosAlumno.get(i).getPromedio().compareTo(promediosAlumno.get(i-1).getPromedio())==0)
				promediosAlumno.get(i).setPuesto(puesto);
			else{
				promediosAlumno.get(i).setPuesto(puesto++);
			}
			
			logger.info(promediosAlumno.get(i));
		}
		logger.info("puesto:" + puesto);
		//puesto para el ultimo
		if(promediosAlumno.size()>0){
			promediosAlumno.get(0).setPuesto(puesto);
			logger.info(promediosAlumno.get(0));
		}
		
		
		return promediosAlumno;
	}
	
	/**
	 * 
	 * @param id_au
	 * @param id_cup
	 * @param tipo N normal o F: Ficticio
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AlumnoMatriculaPromBean>  promediosAnualAula(Integer id_au,String tipo ,int id_cpu, Integer id_gir){
	
		List<Row> id_mats = aulaDAO.listMatriculados(id_au, tipo);
		
		List<AlumnoMatriculaPromBean> promediosAlumno = new ArrayList<AlumnoMatriculaPromBean>();
		List<AlumnoMatriculaPromBean> promediosAlumnoDinamico = new ArrayList<AlumnoMatriculaPromBean>();
		
		for (Row id_mat : id_mats) {
			
			Row promedios = listarPromedioCursos(id_mat.getInteger("id"),id_cpu, id_gir);
			List<Row> cursos = (List<Row>)promedios.get("listaProfesoresCursos");
			Integer cantidadCursos = 0;
 
			BigDecimal promedio ;
			Integer sumaPromedio = 0;

			for (Row row : cursos) {

				List<Row> periodos = (List<Row>)row.get("periodos");
				
				Row curso1 = (Row)periodos.get(0).get("cursos");
				Row curso2 = periodos.size()>1 ? (Row)periodos.get(1).get("cursos"):null;
				Row curso3 = periodos.size()>2 ? (Row)periodos.get(2).get("cursos"):null;
				Row curso4 = periodos.size()>3 ? (Row)periodos.get(3).get("cursos"):null;
				
				Integer promedioInt = null;
				
				Integer cant_periodos = 0;
				Integer suma_periodo = 0;
				
				if (curso1!=null && !"EXO".equals(curso1.getString("competenciasPromedio"))){
						BigDecimal promedioCurso = curso1.getBigDecimal("competenciasPromedio");
						if(promedioCurso!=null){
							cant_periodos ++;
							promedioInt = promedioCurso.setScale(0, RoundingMode.HALF_UP).intValue();
							suma_periodo = suma_periodo + promedioInt;
						}
				}
				
				if (curso2!=null && !"EXO".equals(curso2.getString("competenciasPromedio"))){
					BigDecimal promedioCurso = curso2.getBigDecimal("competenciasPromedio");
					if(promedioCurso!=null){
						cant_periodos ++;
						promedioInt = promedioCurso.setScale(0, RoundingMode.HALF_UP).intValue();
						suma_periodo = suma_periodo + promedioInt;
					}
				}

				if (curso3!=null && !"EXO".equals(curso3.getString("competenciasPromedio"))){
					BigDecimal promedioCurso = curso3.getBigDecimal("competenciasPromedio");
					if(promedioCurso!=null){
						cant_periodos ++;
						promedioInt = promedioCurso.setScale(0, RoundingMode.HALF_UP).intValue();
						suma_periodo = suma_periodo + promedioInt;
					}
				}

				if (curso4!=null && !"EXO".equals(curso4.getString("competenciasPromedio"))){
					BigDecimal promedioCurso = curso4.getBigDecimal("competenciasPromedio");
					if(promedioCurso!=null){
						cant_periodos ++;
						promedioInt = promedioCurso.setScale(0, RoundingMode.HALF_UP).intValue();
						suma_periodo = suma_periodo + promedioInt;
					}
				}
				
				if (cant_periodos>0){

					cantidadCursos++;
					sumaPromedio = sumaPromedio + suma_periodo;
				}
				
				
			}
			if (cantidadCursos==0)
				logger.info("promedio de " + id_mat + "NO TIENE CURSOS");
			else{
				promedio = new BigDecimal(sumaPromedio);
				
				logger.info("promedio de " + id_mat + ":es:" + promedio);
				
				if (promediosAlumnoDinamico.size()==0){
					promediosAlumnoDinamico.add(new AlumnoMatriculaPromBean(id_mat.getInteger("id"), id_mat.getString("nombres"), promedio));
	
				}else{
					//ordenar
					int i = 0;
					boolean menor  = false;
	
					for (AlumnoMatriculaPromBean promedio_alumno : promediosAlumno) {	
						
						if (promedio_alumno.getPromedio().compareTo(promedio)>0 && !menor){
							promediosAlumnoDinamico.add(i,new AlumnoMatriculaPromBean(id_mat.getInteger("id"),id_mat.getString("nombres"), promedio));
							menor  = true;
						}
						i++;
					}
					
					if (!menor)
						promediosAlumnoDinamico.add(new AlumnoMatriculaPromBean(id_mat.getInteger("id"),id_mat.getString("nombres"), promedio));
					
				}
				promediosAlumno = new ArrayList<AlumnoMatriculaPromBean>(promediosAlumnoDinamico.size());
				
				promediosAlumno.addAll(promediosAlumnoDinamico);
			}
		}
		
		//asignar puestos
		int puesto = 1;
		for (int i = promediosAlumno.size()-1; i > 0; i--) {
			if (promediosAlumno.get(i).getPromedio().compareTo(promediosAlumno.get(i-1).getPromedio())==0)
				promediosAlumno.get(i).setPuesto(puesto);
			else{
				promediosAlumno.get(i).setPuesto(puesto++);
			}
			
		}
		logger.info("puesto:" + puesto);
		//puesto para el ultimo
		promediosAlumno.get(0).setPuesto(puesto);
		
		logger.info(promediosAlumno.get(0));
		return promediosAlumno;
	}
	
	/**
	 * 
	 * @param id_au
	 * @param id_cup
	 * @param tipo N normal o F: Ficticio
	 * @return
	 */
	@SuppressWarnings("unchecked") //aquiii
	public List<AreaNotaReq>  promediosAreasMatricula(Integer id_mat, Integer id_alu,Integer id_gra, Integer id_au ,Integer id_cpu, Integer id_anio, Integer id_dcniv, Integer id_gir){
		Matricula matricula = matriculaDAO.get(id_mat);
		PerUni perUni=perUniDAO.get(id_cpu);
        Integer nro_per=perUni.getNump();
		List<AreaNotaReq> areas_nota= new ArrayList<AreaNotaReq>();
		//Obtengo la lista de Areas
		List<Row> areas= dcnAreaDAO.listarAreasComboAnio(id_dcniv,id_anio, id_gra, id_gir);
		//Lista de Competencias
		List<Row> competencias = new ArrayList<>();
		for (Row row : areas) {
			List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),new String[]{"orden asc"});
			AreaNotaReq area_nota= new AreaNotaReq();
			area_nota.setId(row.getInteger("id"));
			area_nota.setNom_area(row.getString("value"));
			//area_nota.setCompetencias(competenciasDc);
			//Buscamos cursos
			List<Row> cursos_anio=cursoAnioDAO.listarCursosporAreaDc(row.getInteger("id"), matricula.getId_gra(), id_gir,id_anio);
			//Buscamos el promedio
			List<CompetenciaNotaReq> competencias_nota = new ArrayList<CompetenciaNotaReq>();
			/*if(id_cpu==0) {
				//Buscamos el tipo de promedio del área
				Param param2 = new Param();
				param2.put("id_anio", id_anio);
				param2.put("id_gra", id_gra);
				param2.put("id_adc", row.getInteger("id"));
				param2.put("id_gir", 1);
				AreaAnio areaAnio = areaAnioDAO.getByParams(param2);
				Integer id_pro_anu=areaAnio.getId_pro_anu();
			}	*/
			List<Row> listaPeriodos = cronogramaDAO.listarPeriodosX_Nivel(matricula.getId_niv(), id_anio, id_gir);
			BigDecimal nota_periodo_com=new BigDecimal(0);
			//BigDecimal sum_notas_periodo=new BigDecimal(0);
		    for (Row row4 : listaPeriodos) {
		    	if(row4.getInteger("aux1")<= nro_per) {
		            Param param2 = new Param();
		            param2.put("id_gra", id_gra);
		            param2.put("id_cpu", row4.getInteger("id"));
		            param2.put("id_anio", id_anio);
		            PeriodoCalificacion periodoCalificacion = periodoCalificacionDAO.getByParams(param2);
		            Integer id_tca=0;
		            if(periodoCalificacion!=null)
		             id_tca=periodoCalificacion.getId_tca();
		    		for (CompetenciaDc competenciaDc : competenciasDc) {
						CompetenciaNotaReq competenciaNotaReq = new CompetenciaNotaReq();
						if(cursos_anio.size()>0) {
							//Tengo q promediar las notas de todas las competencias de todos los cursos del area
							BigDecimal nota_prom=new BigDecimal(0);
							BigDecimal sum_notas=new BigDecimal(0);
							Integer cant_notas=0;
							for (Row row2 : cursos_anio) {
								System.out.println("id_cua->>"+row2.getInteger("id_cua"));
								/*Param param = new Param();
								param.put("id_cua", row2.getInteger("id_cua"));
								param.put("id_com", competenciaDc.getId());
								param.put("id_cpu",row4.getInteger("id"));
								param.put("id_alu", id_alu);*/
								//PromedioCom promedio = promedioComDAO.getByParams(param);
								Row promedio= promedioComDAO.notaPromedioCom(row2.getInteger("id_cua"), competenciaDc.getId(), row4.getInteger("id"), id_alu);
								if(promedio!=null) {
									Integer promedio_nota=promedio.getBigDecimal("prom").setScale(0, RoundingMode.HALF_UP).intValue();
									sum_notas=sum_notas.add(new BigDecimal(promedio_nota));
									cant_notas = cant_notas + 1;
								}
								
							}
							if(cant_notas>0) {
								//nota_prom=sum_notas.divide(new BigDecimal(cant_notas));
								nota_prom=(sum_notas.divide(new BigDecimal(cant_notas),2,RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP); 
							}
							
							competenciaNotaReq.setId(competenciaDc.getId());
							competenciaNotaReq.setPeriodo(row4.getString("nro_per_rom"));
							competenciaNotaReq.setId_dcare(row.getInteger("id"));
							competenciaNotaReq.setNombre(competenciaDc.getNom());
							if(!nota_prom.equals(new BigDecimal(0))) {
								Integer nota_promedio=nota_prom.intValue();
								String nota_equi="";
								if(id_tca.equals(Constante.TIPO_CALIFICACION_CUALITATIVA)) {
									if(nota_promedio.equals(4)) {
										nota_equi="AD";
									} else if(nota_promedio.equals(3)) {
										nota_equi="A";
									} else if(nota_promedio.equals(2)) {
										nota_equi="B";
									} else if(nota_promedio.equals(1)) {
										nota_equi="C";
									}
								} else if(id_tca.equals(Constante.TIPO_CALIFICACION_CUANTITATIVA)) {
									nota_equi=nota_promedio.toString();
								}
								competenciaNotaReq.setNota(nota_equi);
								/*if(id_gra.equals(13) || id_gra.equals(14)) {
									nota_periodo_com= nota_periodo_com.add(new BigDecimal(nota_promedio));
								}*/
							} else {
								competenciaNotaReq.setNota("");
							}
							
							competencias_nota.add(competenciaNotaReq);
						} else {
							Param param = new Param();
							param.put("id_com", competenciaDc.getId());
							param.put("id_cpu",row4.getInteger("id"));
							param.put("id_alu", id_alu);
							PromedioCom promedio = promedioComDAO.getByParams(param);
							//area_nota.setProm_area(Integer.parseInt(promedio.getProm().toString()));
							competenciaNotaReq.setId(competenciaDc.getId());
							competenciaNotaReq.setPeriodo(row4.getString("nro_per_rom"));
							competenciaNotaReq.setId_dcare(row.getInteger("id"));
							competenciaNotaReq.setNombre(competenciaDc.getNom());
							if(promedio!=null) {
								Integer nota_promedio=promedio.getProm().intValue();
								String nota_equi="";
								if(id_tca.equals(Constante.TIPO_CALIFICACION_CUALITATIVA)) {
									if(nota_promedio.equals(4)) {
										nota_equi="AD";
									} else if(nota_promedio.equals(3)) {
										nota_equi="A";
									} else if(nota_promedio.equals(2)) {
										nota_equi="B";
									} else if(nota_promedio.equals(1)) {
										nota_equi="C";
									}
								} else if(id_tca.equals(Constante.TIPO_CALIFICACION_CUANTITATIVA)) {
									nota_equi=nota_promedio.toString();
								}
								competenciaNotaReq.setNota(nota_equi);
								/*if(id_gra.equals(13) || id_gra.equals(14)) {
									nota_periodo_com= nota_periodo_com.add(new BigDecimal(nota_promedio));
								}*/
							} else {
								competenciaNotaReq.setNota("");
							}
							
							competencias_nota.add(competenciaNotaReq);
						}		
					}
					area_nota.setCompetencia_notas(competencias_nota);
					
		    	}
		    	
		    }
		    areas_nota.add(area_nota);
		    
		}
		
		return areas_nota;
	}
	
	@SuppressWarnings("unchecked") //aquiii
	public List<AreaNotaReq>  promediosAreasMatriculaxPeriodo(Integer id_mat, Integer id_alu,Integer id_gra, Integer id_au ,Integer id_cpu, Integer id_anio, Integer id_dcniv, Integer id_gir){
		Matricula matricula = matriculaDAO.get(id_mat);
		PerUni perUni=perUniDAO.get(id_cpu);
        Integer nro_per=perUni.getNump();
		List<AreaNotaReq> areas_nota= new ArrayList<AreaNotaReq>();
		//Obtengo la lista de Areas
		List<Row> areas= dcnAreaDAO.listarAreasComboAnio(id_dcniv,id_anio, id_gra, id_gir);
		//Lista de Competencias
		List<Row> competencias = new ArrayList<>();
		for (Row row : areas) {
			List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),new String[]{"orden asc"});
			AreaNotaReq area_nota= new AreaNotaReq();
			area_nota.setId(row.getInteger("id"));
			area_nota.setNom_area(row.getString("value"));
			//area_nota.setCompetencias(competenciasDc);
			//Buscamos cursos
			List<Row> cursos_anio=cursoAnioDAO.listarCursosporAreaDc(row.getInteger("id"), matricula.getId_gra(), id_gir, id_anio);
			//Buscamos el promedio
			List<CompetenciaNotaReq> competencias_nota = new ArrayList<CompetenciaNotaReq>();
			/*if(id_cpu==0) {
				//Buscamos el tipo de promedio del área
				Param param2 = new Param();
				param2.put("id_anio", id_anio);
				param2.put("id_gra", id_gra);
				param2.put("id_adc", row.getInteger("id"));
				param2.put("id_gir", 1);
				AreaAnio areaAnio = areaAnioDAO.getByParams(param2);
				Integer id_pro_anu=areaAnio.getId_pro_anu();
			}	*/
			Row periodo_aca = cronogramaDAO.datosPeriodo(id_cpu, id_anio);
		   // for (Row row4 : listaPeriodos) {
		    //	if(row4.getInteger("aux1")<= nro_per) {
		            Param param2 = new Param();
		            param2.put("id_gra", id_gra);
		            param2.put("id_cpu", id_cpu);
		            param2.put("id_anio", id_anio);
		            PeriodoCalificacion periodoCalificacion = periodoCalificacionDAO.getByParams(param2);
		            Integer id_tca=0;
		            if(periodoCalificacion!=null)
		             id_tca=periodoCalificacion.getId_tca();
		    		for (CompetenciaDc competenciaDc : competenciasDc) {
						CompetenciaNotaReq competenciaNotaReq = new CompetenciaNotaReq();
						if(cursos_anio.size()>0) {
							//Tengo q promediar las notas de todas las competencias de todos los cursos del area
							BigDecimal nota_prom=new BigDecimal(0);
							BigDecimal sum_notas=new BigDecimal(0);
							Integer cant_notas=0;
							for (Row row2 : cursos_anio) {
								System.out.println("id_cua->>"+row2.getInteger("id_cua"));
								/*Param param = new Param();
								param.put("id_cua", row2.getInteger("id_cua"));
								param.put("id_com", competenciaDc.getId());
								param.put("id_cpu",row4.getInteger("id"));
								param.put("id_alu", id_alu);*/
								//PromedioCom promedio = promedioComDAO.getByParams(param);
								Row promedio= promedioComDAO.notaPromedioCom(row2.getInteger("id_cua"), competenciaDc.getId(), periodo_aca.getInteger("id"), id_alu);
								if(promedio!=null) {
									Integer promedio_nota=promedio.getBigDecimal("prom").setScale(0, RoundingMode.HALF_UP).intValue();
									sum_notas=sum_notas.add(new BigDecimal(promedio_nota));
									cant_notas = cant_notas + 1;
								}
								
							}
							if(cant_notas>0) {
								//nota_prom=sum_notas.divide(new BigDecimal(cant_notas));
								nota_prom=(sum_notas.divide(new BigDecimal(cant_notas),2,RoundingMode.HALF_UP)).setScale(0, RoundingMode.HALF_UP); 
							}
							
							competenciaNotaReq.setId(competenciaDc.getId());
							competenciaNotaReq.setPeriodo(periodo_aca.getString("nro_per_rom"));
							competenciaNotaReq.setId_dcare(row.getInteger("id"));
							competenciaNotaReq.setNombre(competenciaDc.getNom());
							if(!nota_prom.equals(new BigDecimal(0))) {
								Integer nota_promedio=nota_prom.intValue();
								String nota_equi="";
								if(id_tca.equals(Constante.TIPO_CALIFICACION_CUALITATIVA)) {
									if(nota_promedio.equals(4)) {
										nota_equi="AD";
									} else if(nota_promedio.equals(3)) {
										nota_equi="A";
									} else if(nota_promedio.equals(2)) {
										nota_equi="B";
									} else if(nota_promedio.equals(1)) {
										nota_equi="C";
									}
								} else if(id_tca.equals(Constante.TIPO_CALIFICACION_CUANTITATIVA)) {
									nota_equi=nota_promedio.toString();
								}
								competenciaNotaReq.setNota(nota_equi);
							} else {
								competenciaNotaReq.setNota("");
							}
							
							competencias_nota.add(competenciaNotaReq);
						} else {
							Param param = new Param();
							param.put("id_com", competenciaDc.getId());
							param.put("id_cpu",periodo_aca.getInteger("id"));
							param.put("id_alu", id_alu);
							PromedioCom promedio = promedioComDAO.getByParams(param);
							//area_nota.setProm_area(Integer.parseInt(promedio.getProm().toString()));
							competenciaNotaReq.setId(competenciaDc.getId());
							competenciaNotaReq.setPeriodo(periodo_aca.getString("nro_per_rom"));
							competenciaNotaReq.setId_dcare(row.getInteger("id"));
							competenciaNotaReq.setNombre(competenciaDc.getNom());
							if(promedio!=null) {
								Integer nota_promedio=promedio.getProm().intValue();
								String nota_equi="";
								if(id_tca.equals(Constante.TIPO_CALIFICACION_CUALITATIVA)) {
									if(nota_promedio.equals(4)) {
										nota_equi="AD";
									} else if(nota_promedio.equals(3)) {
										nota_equi="A";
									} else if(nota_promedio.equals(2)) {
										nota_equi="B";
									} else if(nota_promedio.equals(1)) {
										nota_equi="C";
									}
								} else if(id_tca.equals(Constante.TIPO_CALIFICACION_CUANTITATIVA)) {
									nota_equi=nota_promedio.toString();
								}
								competenciaNotaReq.setNota(nota_equi);
							} else {
								competenciaNotaReq.setNota("");
							}
							
							competencias_nota.add(competenciaNotaReq);
						}		
					}
					area_nota.setCompetencia_notas(competencias_nota);
					
		    	//}
		    	
		   // }
		    areas_nota.add(area_nota);
		}
		
		return areas_nota;
	}


	
	/**
	 * Promedio de notas por periodo x alumno
	 * @param id_mat
	 * @param nump
	 * @return
	 */
	public List<Row> promerdioNotasPorPeriodo(Integer id_mat, Integer nump){
		
		String sql ="SELECT ARE.id_cur, cur.nom curso, ROUND(AVG(ARE.promedio))  promedio"
					+ "\n	 	FROM"
					+ "\n	 	(	"
					+ "\n	 		SELECT PER.nump, PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
					+ "\n	 		FROM"
					+ "\n	 		(	"
					+ "\n	 			SELECT CURSO.nump, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
					+ "\n	 			FROM ("
					+ "\n						SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
                    + "\n                        from mat_matricula mat"
					+ "\n							inner join col_curso_aula cca on cca.id_au = mat.id_au_asi"
					+ "\n							inner JOIN col_curso_anio cua ON cua.id = cca.id_cua  "
					+ "\n							inner JOIN col_aula au ON mat.id_au_asi=au.id"
					+ "\n							inner JOIN per_periodo per ON au.id_per=per.id"
					+ "\n							inner JOIN not_evaluacion ne ON (ne.nump=:nump and cca.id = ne.id_cca )" 
					+ "\n							left JOIN not_nota nn ON (nn.id_alu=mat.id_alu and nn.id_ne = ne.id )"
					+ "\n							left JOIN not_nota_indicador nni ON nni.id_not= nn.id -- AND nie.est='A'"
					+ "\n							left JOIN not_ind_eva nie ON nie.id_ne = ne.id and nni.id_nie= nie.id "
					+ "\n							left JOIN col_ind_sub cis ON cis.id = nie.id_cis"
					+ "\n							left JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
					+ "\n							left JOIN col_subtema sub ON sub.id = ccs.id_sub"
					+ "\n							left JOIN col_tema tem ON tem.id = sub.id_tem"
					+ "\n							left JOIN col_indicador ci ON cis.id_ind=ci.id"
					+ "\n							left JOIN col_capacidad cap ON cap.id = ci.id_cap"
					+ "\n							left JOIN col_competencia com ON com.id = cap.id_com"
					+ "\n						WHERE  "
					+ "\n						 mat.id=:id_mat" //-- nd mat.id_alu= 
					+ "\n						AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+ "\n					) CURSO"
					+ "\n					GROUP BY 	"
					+ "\n					CURSO.nump, CURSO.id_cur, CURSO.com_id"
					+ "\n	 		) PER "
					+ "\n	 		GROUP BY PER.nump, PER.id_cur"
					+ "\n	 	) ARE INNER JOIN cat_curso cur on cur.id= ARE.id_cur"
					+ "\n	 	GROUP BY ARE.id_cur order by ARE.id_cur";
		Param param = new Param();
		param.put("id_mat", id_mat);
		param.put("nump", nump);
		
		return sqlUtil.query(sql,param);
	}
	
	public List<Row> listarNotasxUnidadSesion(int id_cca, int id_uni) {

		String sql = "SELECT nni.*"
		+ " FROM `col_unidad_sesion` uns INNER JOIN `col_sesion_tipo` ses ON uns.id=ses.`id_uns`"
		+ " INNER JOIN `not_evaluacion` ne ON ses.`id`=ne.`id_ses`"
		+ " INNER JOIN `not_nota` nn ON ne.`id`=nn.`id_ne`"
		+ " INNER JOIN `not_nota_indicador` nni ON nn.`id`=nni.`id_not`"
		+ " INNER JOIN col_curso_horario_ses hor on hor.id_uns=uns.id "
		+ " WHERE ne.`id_cca`=? and uns.id_uni=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_cca, id_uni});

	}
	
	public List<Row> listarAlumnosCambioAula(int id_anio) {
		
		String sql = "SELECT a.id FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au`=au.`id` "
				+ " INNER JOIN `alu_alumno` a ON mat.`id_alu`=a.id"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`=? AND mat.`id_au`<> mat.`id_au_asi`";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio});

	}

	public String inicial_cualitativo(BigDecimal nota){
		if(nota==null)
			return "C";
		int not_dec = nota.setScale(0,RoundingMode.HALF_UP).intValue();
		if (not_dec==4)
			return "AD";
		else if (not_dec>=3)
			return "A";
		else if (not_dec>=2)
			return "B";
		else
			return "C";
	}
	
	public static void main(String args[]){

	

		
	}
}
