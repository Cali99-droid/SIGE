package com.sige.mat.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Nota;

import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.bean.AlumnoNotaBean;
import com.tesla.colegio.model.bean.AlumnoNotaCursoBean;
import com.tesla.colegio.model.bean.PromedioBean;
import com.sige.common.enums.EnumNivel;
import com.sige.core.dao.SQLUtil;
import com.sige.core.dao.SQLUtilImpl;
import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AreaAnioDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.AulaEspecialDAO;
import com.sige.mat.dao.CapacidadDAO;
import com.sige.mat.dao.CompetenciaDAO;
import com.sige.mat.dao.CompetenciaDcDAO;
import com.sige.mat.dao.ComportamientoDAO;
import com.sige.mat.dao.ConfAnioAcadDcnDAO;
import com.sige.mat.dao.CursoAnioDAO;
import com.sige.mat.dao.DcnAreaDAO;
import com.sige.mat.dao.DcnNivelDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.rest.request.AreaCompetenciaReq;
import com.sige.rest.request.AreaNotaReq;
import com.sige.rest.request.CompetenciaDCReq;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaEspecial;
import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.Comportamiento;
import com.tesla.colegio.model.ConfAnioAcadDcn;
import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.NotaIndicador;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface NotaDAO.
 * 
 * @author MV
 *
 */
public class NotaDAOImpl {
	final static Logger logger = Logger.getLogger(NotaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private CursoAnioDAO curso_anioDAO;

	@Autowired
	private AreaAnioDAO area_anioDAO;

	@Autowired
	private AulaEspecialDAO aulaEspecialDAO;

	@Autowired
	private AulaDAO aulaDAO;

	@Autowired
	private ComportamientoDAO comportamientoDAO;
	
	@Autowired
	private ConfAnioAcadDcnDAO confAnioAcadDcnDAO; 
	
	@Autowired
	private CompetenciaDcDAO competenciaDcDAO; 
	
	@Autowired
	private DcnNivelDAO dcNivelDAO; 
	
	@Autowired
	private DcnAreaDAO dcnAreaDAO; 
	
	@Autowired
	private MatriculaDAO matriculaDAO; 
	
	@Autowired
	private SQLUtil sqlUtil;
	
	@Lazy
	@Autowired
	private NotaDAO notaDAO; 
	
	@Autowired
	private AlumnoDAO alumnoDAO;

	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Nota nota) {
		if (nota.getId() != null) {
			// update
			String sql = "UPDATE not_nota " + "SET id_ne=?, " + "id_tra=?, " + "id_alu=?, " + "fec=?, " + "prom=?, "
					+ "est=?,usr_act=?,fec_act=? " + "WHERE id=?";

			//logger.info(sql);

			jdbcTemplate.update(sql, nota.getId_ne(), nota.getId_tra(), nota.getId_alu(), nota.getFec(), nota.getProm(),
					nota.getEst(), nota.getUsr_act(), new java.util.Date(), nota.getId());
			return nota.getId();

		} else {
			// insert
			String sql = "insert into not_nota (" + "id_ne, " + "id_tra, " + "id_alu, " + "fec, " + "prom, "
					+ "est, usr_ins, fec_ins) " + "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

			//logger.info(sql);

			jdbcTemplate.update(sql, nota.getId_ne(), nota.getId_tra(), nota.getId_alu(), nota.getFec(), nota.getProm(),
					nota.getEst(), nota.getUsr_ins(), new java.util.Date());

			return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
		}

	}

	public void delete(int id_eva) {
		String sql = "delete from not_nota where id_ne=?";

		//logger.info(sql);

		jdbcTemplate.update(sql, id_eva);
	}

	public List<Nota> list() {
		String sql = "select * from not_nota";

		//logger.info(sql);

		List<Nota> listNota = jdbcTemplate.query(sql, new RowMapper<Nota>() {

			@Override
			public Nota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs, "");
			}

		});

		return listNota;
	}

	public Nota get(int id) {
		String sql = "select * from not_nota WHERE id=" + id;

		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Nota>() {

			@Override
			public Nota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs, "");
				}

				return null;
			}

		});
	}

	public Nota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select not.id not_id, not.id_ne not_id_ne , not.id_tra not_id_tra , not.id_alu not_id_alu , not.fec not_fec , not.prom not_prom  ,not.est not_est ";
		if (aTablas.contains("not_evaluacion"))
			sql = sql
					+ ", ne.id ne_id  , ne.id_nep ne_id_nep , ne.id_cca ne_id_cca , ne.id_nte ne_id_nte , ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql
					+ ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql
					+ ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";

		sql = sql + " from not_nota not ";
		if (aTablas.contains("not_evaluacion"))
			sql = sql + " left join not_evaluacion ne on ne.id = not.id_ne ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = not.id_tra ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = not.id_alu ";
		sql = sql + " where not.id= " + id;

		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Nota>() {

			@Override
			public Nota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Nota nota = rsToEntity(rs, "not_");
					if (aTablas.contains("not_evaluacion")) {
						Evaluacion evaluacion = new Evaluacion();
						evaluacion.setId(rs.getInt("ne_id"));
						evaluacion.setId_nep(rs.getInt("ne_id_nep"));
						evaluacion.setId_cca(rs.getInt("ne_id_cca"));
						evaluacion.setId_nte(rs.getInt("ne_id_nte"));
						evaluacion.setIns(rs.getString("ne_ins"));
						evaluacion.setEvi(rs.getString("ne_evi"));
						evaluacion.setFec_ini(rs.getDate("ne_fec_ini"));
						evaluacion.setFec_fin(rs.getDate("ne_fec_fin"));
						nota.setEvaluacion(evaluacion);
					}
					if (aTablas.contains("ges_trabajador")) {
						Trabajador trabajador = new Trabajador();
						trabajador.setId(rs.getInt("tra_id"));
						trabajador.setId_tdc(rs.getInt("tra_id_tdc"));
						trabajador.setNro_doc(rs.getString("tra_nro_doc"));
						trabajador.setApe_pat(rs.getString("tra_ape_pat"));
						trabajador.setApe_mat(rs.getString("tra_ape_mat"));
						trabajador.setNom(rs.getString("tra_nom"));
						trabajador.setFec_nac(rs.getDate("tra_fec_nac"));
						trabajador.setGenero(rs.getString("tra_genero"));
						trabajador.setId_eci(rs.getInt("tra_id_eci"));
						trabajador.setDir(rs.getString("tra_dir"));
						trabajador.setTel(rs.getString("tra_tel"));
						trabajador.setCel(rs.getString("tra_cel"));
						trabajador.setCorr(rs.getString("tra_corr"));
						trabajador.setId_gin(rs.getInt("tra_id_gin"));
						trabajador.setCarrera(rs.getString("tra_carrera"));
						// trabajador.setFot(rs.getString("tra_fot")) ;
						trabajador.setNum_hij(rs.getInt("tra_num_hij"));
						trabajador.setId_usr(rs.getInt("tra_id_usr"));
						nota.setTrabajador(trabajador);
					}
					if (aTablas.contains("alu_alumno")) {
						Alumno alumno = new Alumno();
						alumno.setId(rs.getInt("alu_id"));
						alumno.setId_tdc(rs.getInt("alu_id_tdc"));
						alumno.setId_idio1(rs.getInt("alu_id_idio1"));
						alumno.setId_idio2(rs.getInt("alu_id_idio2"));
						alumno.setId_eci(rs.getInt("alu_id_eci"));
						alumno.setId_tap(rs.getString("alu_id_tap"));
						alumno.setId_gen(rs.getString("alu_id_gen"));
						alumno.setCod(rs.getString("alu_cod"));
						alumno.setNro_doc(rs.getString("alu_nro_doc"));
						alumno.setNom(rs.getString("alu_nom"));
						alumno.setApe_pat(rs.getString("alu_ape_pat"));
						alumno.setApe_mat(rs.getString("alu_ape_mat"));
						// alumno.setFec_nac(rs.getString("alu_fec_nac")) ;
						alumno.setNum_hij(rs.getInt("alu_num_hij"));
						alumno.setDireccion(rs.getString("alu_direccion"));
						alumno.setTelf(rs.getString("alu_telf"));
						alumno.setCelular(rs.getString("alu_celular"));
						alumno.setPass_educando(rs.getString("alu_pass_educando"));
						// alumno.setFoto(rs.getString("alu_foto")) ;
						nota.setAlumno(alumno);
					}
					return nota;
				}

				return null;
			}

		});

	}

	public Nota getByParams(Param param) {

		String sql = "select * from not_nota " + SQLFrmkUtil.getWhere(param);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Nota>() {
			@Override
			public Nota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs, "");
				return null;
			}

		});
	}

	public List<Nota> listByParams(Param param, String[] order) {

		String sql = "select * from not_nota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nota>() {

			@Override
			public Nota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs, "");
			}

		});

	}

	public List<Nota> listFullByParams(Nota nota, String[] order) {

		return listFullByParams(Param.toParam("not", nota), order);

	}

	public List<Nota> listFullByParams(Param param, String[] order) {

		String sql = "select nota.id not_id, nota.id_ne not_id_ne , nota.id_tra not_id_tra , nota.id_alu not_id_alu , nota.fec not_fec , nota.prom not_prom  ,nota.est not_est ";
		sql = sql
				+ ", ne.id ne_id  , ne.id_nep ne_id_nep , ne.id_cca ne_id_cca , ne.id_nte ne_id_nte , ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ";
		sql = sql
				+ ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql
				+ ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from not_nota nota";
		sql = sql + " left join not_evaluacion ne on ne.id = nota.id_ne ";
		sql = sql + " left join ges_trabajador tra on tra.id = nota.id_tra ";
		sql = sql + " left join alu_alumno alu on alu.id = nota.id_alu ";

		sql = sql + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Nota>() {

			@Override
			public Nota mapRow(ResultSet rs, int rowNum) throws SQLException {
				Nota nota = rsToEntity(rs, "not_");
				Evaluacion evaluacion = new Evaluacion();
				evaluacion.setId(rs.getInt("ne_id"));
				evaluacion.setId_nep(rs.getInt("ne_id_nep"));
				evaluacion.setId_cca(rs.getInt("ne_id_cca"));
				evaluacion.setId_nte(rs.getInt("ne_id_nte"));
				evaluacion.setIns(rs.getString("ne_ins"));
				evaluacion.setEvi(rs.getString("ne_evi"));
				evaluacion.setFec_ini(rs.getDate("ne_fec_ini"));
				evaluacion.setFec_fin(rs.getDate("ne_fec_fin"));
				nota.setEvaluacion(evaluacion);
				Trabajador trabajador = new Trabajador();
				trabajador.setId(rs.getInt("tra_id"));
				trabajador.setId_tdc(rs.getInt("tra_id_tdc"));
				trabajador.setNro_doc(rs.getString("tra_nro_doc"));
				trabajador.setApe_pat(rs.getString("tra_ape_pat"));
				trabajador.setApe_mat(rs.getString("tra_ape_mat"));
				trabajador.setNom(rs.getString("tra_nom"));
				trabajador.setFec_nac(rs.getDate("tra_fec_nac"));
				trabajador.setGenero(rs.getString("tra_genero"));
				trabajador.setId_eci(rs.getInt("tra_id_eci"));
				trabajador.setDir(rs.getString("tra_dir"));
				trabajador.setTel(rs.getString("tra_tel"));
				trabajador.setCel(rs.getString("tra_cel"));
				trabajador.setCorr(rs.getString("tra_corr"));
				trabajador.setId_gin(rs.getInt("tra_id_gin"));
				trabajador.setCarrera(rs.getString("tra_carrera"));
				// trabajador.setFot(rs.getString("tra_fot")) ;
				trabajador.setNum_hij(rs.getInt("tra_num_hij"));
				trabajador.setId_usr(rs.getInt("tra_id_usr"));
				nota.setTrabajador(trabajador);
				Alumno alumno = new Alumno();
				alumno.setId(rs.getInt("alu_id"));
				alumno.setId_tdc(rs.getInt("alu_id_tdc"));
				alumno.setId_idio1(rs.getInt("alu_id_idio1"));
				alumno.setId_idio2(rs.getInt("alu_id_idio2"));
				alumno.setId_eci(rs.getInt("alu_id_eci"));
				alumno.setId_tap(rs.getString("alu_id_tap"));
				alumno.setId_gen(rs.getString("alu_id_gen"));
				alumno.setCod(rs.getString("alu_cod"));
				alumno.setNro_doc(rs.getString("alu_nro_doc"));
				alumno.setNom(rs.getString("alu_nom"));
				alumno.setApe_pat(rs.getString("alu_ape_pat"));
				alumno.setApe_mat(rs.getString("alu_ape_mat"));
				// alumno.setFec_nac(rs.getString("alu_fec_nac")) ;
				alumno.setNum_hij(rs.getInt("alu_num_hij"));
				alumno.setDireccion(rs.getString("alu_direccion"));
				alumno.setTelf(rs.getString("alu_telf"));
				alumno.setCelular(rs.getString("alu_celular"));
				alumno.setPass_educando(rs.getString("alu_pass_educando"));
				// alumno.setFoto(rs.getString("alu_foto")) ;
				nota.setAlumno(alumno);
				return nota;
			}

		});

	}

	public List<NotaIndicador> getListNotaIndicador(Param param, String[] order) {
		String sql = "select * from not_nota_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaIndicador>() {

			@Override
			public NotaIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaIndicador nota_indicador = new NotaIndicador();

				nota_indicador.setId(rs.getInt("id"));
				nota_indicador.setId_not(rs.getInt("id_not"));
				nota_indicador.setId_nie(rs.getInt("id_nie"));
				nota_indicador.setEst(rs.getString("est"));

				return nota_indicador;
			}

		});
	}

	// funciones privadas utilitarias para Nota

	private Nota rsToEntity(ResultSet rs, String alias) throws SQLException {
		Nota nota = new Nota();

		nota.setId(rs.getInt(alias + "id"));
		nota.setId_ne(rs.getInt(alias + "id_ne"));
		nota.setId_tra(rs.getInt(alias + "id_tra"));
		nota.setId_alu(rs.getInt(alias + "id_alu"));
		nota.setFec(rs.getDate(alias + "fec"));
		nota.setProm(rs.getBigDecimal(alias + "prom"));
		nota.setEst(rs.getString(alias + "est"));

		return nota;

	}

	public Map<String, Object> listNotaAula(Integer id_anio, Integer id_cur, Integer id_niv, Integer id_au,
			Integer nump) {

		// competencias
		/*String sqlCompetencias = "SELECT DISTINCT com.id com_id, com.nom, COUNT(com.id) count "
				+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
				+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN `col_curso_subtema` ccs  ON ccs.`id_cur`=cua.`id_cur`"
				+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis` AND ccs.id=cis.`id_sub`"
				+ " INNER JOIN col_subtema_capacidad csc ON csc.`id_ccs`=ccs.`id`"
				+ " INNER JOIN  col_capacidad cap ON csc.`id_cap`=cap.`id`"
				+ " INNER JOIN col_competencia com ON com.id = cap.id_com AND ccs.`id_cur`=com.`id_cur`"
				+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.id AND ind.`id_cap`=cap.`id`"
				+ " WHERE cua.`id_cur`=?  AND cca.`id_au`=? AND ne.`nump`=? AND nie.`est`='A' "
				+ " GROUP BY com.id, com.nom " + " ORDER BY com.id ";*/
		String sqlCompetencias ="SELECT DISTINCT com.`id` com_id, com.`nom`, COUNT(com.id) count"
				+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
				+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.`id`"
				+ " INNER JOIN `col_competencia` com ON cap.`id_com`=com.`id`"
				+ " WHERE cua.`id_cur`=?  AND cca.`id_au`=? AND ne.`nump`=? AND nie.`est`='A' "
				+ " GROUP BY com.id, com.nom "
				+ " ORDER BY com.id";
		//logger.info(sqlCompetencias);
		List<Map<String, Object>> competencias = jdbcTemplate.queryForList(sqlCompetencias,
				new Object[] { id_cur, id_au, nump });

		/*String sqlCapacidad = "SELECT  com.id com_id, com.nom com_nom,cap.id, cap.nom"
				+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
				+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN `col_curso_subtema` ccs  ON ccs.`id_cur`=cua.`id_cur`"
				+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis` AND ccs.id=cis.`id_sub`"
				+ " INNER JOIN col_subtema_capacidad csc ON csc.`id_ccs`=ccs.`id`"
				+ " INNER JOIN  col_capacidad cap ON csc.`id_cap`=cap.`id`"
				+ " INNER JOIN col_competencia com ON com.id = cap.id_com AND ccs.`id_cur`=com.`id_cur`"
				+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.id AND ind.`id_cap`=cap.`id`"
				+ " WHERE cua.`id_cur`=?  AND cca.`id_au`=? AND ne.`nump`=? AND nie.`est`='A'" + " ORDER BY cap.id";*/
		String sqlCapacidad="SELECT cap.`id_com` com_id, com.nom com_nom, cap.`id`, cap.`nom`  "
				+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
				+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
				+ " INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.`id`"
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " WHERE cua.`id_cur`=?  AND cca.`id_au`=? AND ne.`nump`=? AND nie.`est`='A' "
				+ " ORDER BY cap.id";
		// competencias
				//logger.info(sqlCapacidad);
		List<Map<String, Object>> capacidades = jdbcTemplate.queryForList(sqlCapacidad,
				new Object[] { id_cur, id_au, nump });

		List<Map<String, Object>> alumnos = new ArrayList<Map<String, Object>>();

		String sqlAlumnos = "SELECT alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
				+ " WHERE mat.`id_au_asi`=?  "// alumno
																														// garcia
																														// pinachi
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		//logger.info(sqlAlumnos);
		alumnos = jdbcTemplate.queryForList(sqlAlumnos, new Object[] { id_au });

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();// aca
																		// ira
																		// el
																		// detalle
																		// de
																		// cada
																		// alumno
			detalle.put("alumno", map.get("alumno"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nota", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		/*String sqlNotas = "SELECT alu.id id_alu, nni.nota,  nni.id id_not, ind.`id_cap`, cap.id_com, com.peso "
				+ " FROM `col_curso_aula` cca INNER JOIN  `mat_matricula` mat  ON mat.`id_au_asi`=cca.`id_au` "
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`  INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id` "
				+ " INNER JOIN `not_evaluacion` ne ON  ne.`id_cca`=cca.`id`  "
				+ " INNER JOIN `not_nota` nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_ind_eva` nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_nota_indicador` nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis`"
				+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.`id`"
				+ " INNER JOIN col_capacidad cap ON cap.id=ind.id_cap "
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " WHERE  cca.`id_au`=? AND cua.`id_cur`=? AND ne.`nump`=?  AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom, ind.id_cap ";*/
		String sqlNotas="SELECT alu.id id_alu, nni.nota,  nni.id id_not, cap.id id_cap, cap.id_com, com.peso "
				+ " FROM `col_curso_aula` cca INNER JOIN  `mat_matricula` mat  ON mat.`id_au_asi`=cca.`id_au` AND (mat.id_sit IS NULL OR mat.id_sit<>5)"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`  "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id` "
				+ " INNER JOIN `not_evaluacion` ne ON  ne.`id_cca`=cca.`id`  "
				+ " INNER JOIN `not_nota` nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_ind_eva` nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_nota_indicador` nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ " INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.`id`"
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " WHERE  cca.`id_au`=? AND cua.`id_cur`=? AND ne.`nump`=?  "
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom, cap.`id`";
		//logger.info(sqlNotas);
		notas = jdbcTemplate.queryForList(sqlNotas, new Object[] { id_au, id_cur, nump });

		for (Map<String, Object> map : notas) {

			Map<String, Object> alumno = linkalumnos.get(map.get("id_alu").toString());
			// notas en ese momento
			Object notaObject = alumno.get("nota");

			if (notaObject == null) { //
				List<AlumnoNotaBean> notaAlumno = new ArrayList<AlumnoNotaBean>();
				List<PromedioBean> prom = new ArrayList<PromedioBean>();
				AlumnoNotaBean nota = new AlumnoNotaBean();
				nota.setId_alu((Integer) map.get("id_alu"));
				nota.setId_not((Integer) map.get("id_not"));
				nota.setNota((Integer) map.get("nota"));
				nota.setId_cap((Integer) map.get("id_cap"));
				nota.setId_com((Integer) map.get("id_com"));
				BigDecimal peso = new BigDecimal((Double) map.get("peso"));
				nota.setPeso(peso);
				notaAlumno.add(nota);
				alumno.put("nota", notaAlumno);
			} else {
				List<AlumnoNotaBean> notaAlumno = (List<AlumnoNotaBean>) notaObject;
				List<PromedioBean> prom = (List<PromedioBean>) notaObject;
				AlumnoNotaBean nota = new AlumnoNotaBean();
				nota.setId_alu((Integer) map.get("id_alu"));
				nota.setId_not((Integer) map.get("id_not"));
				nota.setNota((Integer) map.get("nota"));
				nota.setId_cap((Integer) map.get("id_cap"));
				nota.setId_com((Integer) map.get("id_com"));
				BigDecimal peso = new BigDecimal((Double) map.get("peso"));
				nota.setPeso(peso);
				notaAlumno.add(nota);
				alumno.put("nota", notaAlumno);
			}

		}

		// itera los alumnos
		LinkedHashMap<String, Map<String, Object>> linkalumnos1 = new LinkedHashMap<String, Map<String, Object>>();

		for (Map.Entry<String, Map<String, Object>> entry : linkalumnos.entrySet()) {
			String key = entry.getKey();// id del alumno
			Map<String, Object> notaMap = entry.getValue();
			List<AlumnoNotaBean> notasAlumno = (List<AlumnoNotaBean>) notaMap.get("nota");// aca
																							// tenemos
																							// las
																							// notas
																							// del
																							// alumno
			Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
			Map<Integer, BigDecimal> competenciaCont = new HashMap<Integer, BigDecimal>();
			// aca tenemos las 5 notas
			if (notasAlumno != null) {
				for (AlumnoNotaBean alumnoNotaBean : notasAlumno) {
					int id_cap = alumnoNotaBean.getId_cap();
					int id_com = alumnoNotaBean.getId_com();
					BigDecimal nota = new BigDecimal(alumnoNotaBean.getNota());

					
					Object suma = competenciaSuma.get(id_com);

					BigDecimal sumaTotal = new BigDecimal(0);
					BigDecimal promedio = new BigDecimal(0);
					BigDecimal sumaPromedio = new BigDecimal(0);
					int cant_notas = 1;
					if (suma != null) {
						sumaTotal = (BigDecimal) competenciaSuma.get(id_com);
						competenciaCont.put(id_com, competenciaCont.get(id_com).add(new BigDecimal(1)));
						cant_notas = cant_notas + 1;
						
					} else
						competenciaCont.put(id_com, new BigDecimal(1));

					sumaTotal = sumaTotal.add(nota);
					promedio = sumaTotal.divide(new BigDecimal(cant_notas));
					sumaPromedio = sumaPromedio.add(promedio);

					competenciaSuma.put(id_com, sumaTotal);

				}
			}

			for (Integer id_com : competenciaSuma.keySet()) {

				competenciaSuma.put(id_com,
						competenciaSuma.get(id_com).divide(competenciaCont.get(id_com), RoundingMode.HALF_UP));
			}

			notaMap.put("competenciaSuma", competenciaSuma);

			// notaMap.put("competenciaSuma", competenciaSuma.GET);
			// notaMap.put("promediogeneral", promedioGeneral);//purebalo por
			// faovr quiero ver el json final

		} // linkalumnos

		List<Map<String, Object>> listAlumnos = new ArrayList<>();

		for (Map.Entry<String, Map<String, Object>> entry : linkalumnos.entrySet()) {
			String key = entry.getKey();// id del alumno
			Map<String, Object> notaMap = entry.getValue();

			Map<Integer, BigDecimal> promediosCom = (Map<Integer, BigDecimal>) notaMap.get("competenciaSuma");// aca
																												// tenemos
																												// las
																												// notas
																												// del
																												// alumno,
																												// ok?
																												// oki
			Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
			// aca tenemos las 5 notas
			BigDecimal promedioGeneral = new BigDecimal(0);
			BigDecimal sumaPromedio = new BigDecimal(0);
			if (promediosCom != null) {
				for (Map<String, Object> competencia : competencias) {
					Integer id_com = (Integer) competencia.get("com_id");
					if (promediosCom.get(id_com) != null)
						sumaPromedio = sumaPromedio.add(promediosCom.get(id_com));

				}
			}
			
			promedioGeneral = sumaPromedio.divide(new BigDecimal(competencias.size()), RoundingMode.HALF_UP);
			notaMap.put("promedioGeneral", promedioGeneral);

			listAlumnos.add(notaMap);
		}

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("competencias", competencias);
		map.put("capacidades", capacidades);
		map.put("alumnos", listAlumnos);
		return map;
	}
	
	public Map<String, Object> listNotasAulaAreaoCurso(Integer id_anio, String id_cur, Integer id_niv, Integer id_au, Integer id_dcare, Integer id_gra, Integer id_cpu) {

		// competencias
		String sqlCompetencias ="SELECT DISTINCT com.`id` com_id, SUBSTRING(com.`nom`,1,25) nom, COUNT(com.id) count \n" ; 
				sqlCompetencias +="				 FROM aca_competencia_dc com \n" ; 
				sqlCompetencias +="				 INNER JOIN `aca_capacidad_dc` cap ON com.`id`=cap.`id_com`";
				sqlCompetencias +="				 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id\n" ; 
				//"				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au+""+
				sqlCompetencias +="				 INNER JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au+"";
				sqlCompetencias +="				 WHERE com.`id_dcare`=? AND des.id_gra=? ";
				if(!id_cur.equals("T")) {
					if(!id_cur.equals("")) {
						sqlCompetencias +=" AND desau.id_cua="+id_cur;
					}	
				}			
				sqlCompetencias +="				 GROUP  BY com.id "	;	
		//logger.info(sqlCompetencias);
		List<Map<String, Object>> competencias = jdbcTemplate.queryForList(sqlCompetencias,new Object[] { id_dcare, id_gra});

		String sqlCapacidad="SELECT DISTINCT cap.`id_com` com_id, com.nom com_nom, cap.`id`, cap.orden,  SUBSTRING(cap.`nom`,1,25) nom, cap.nom capacidad, COUNT(cap.id) count_cap  \n" ; 
				sqlCapacidad +="				 FROM aca_capacidad_dc cap INNER JOIN aca_competencia_dc com ON cap.`id_com`=com.`id`\n" ;
				sqlCapacidad +="				 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id\n" ;
				//"				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au+
				sqlCapacidad +="				 INNER JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au;
				sqlCapacidad +="				 WHERE com.`id_dcare`=? AND des.id_gra=?";
				if(!id_cur.equals("T")) {
					if(!id_cur.equals("")) {
					sqlCapacidad +=" AND desau.id_cua="+id_cur;
					}	
				}	
				sqlCapacidad +="				 GROUP  BY cap.id "	;	
		
		// competencias
				//logger.info(sqlCapacidad);
		List<Map<String, Object>> capacidades = jdbcTemplate.queryForList(sqlCapacidad,new Object[] {id_dcare, id_gra});
		
		String sqlDesempenios="SELECT DISTINCT des.`id` id_des, des.nom des_nom, des.orden, cap.`id` id_cap, com.nom com_nom  \n" ; 
		sqlDesempenios +="								 FROM aca_capacidad_dc cap INNER JOIN aca_competencia_dc com ON cap.`id_com`=com.`id`\n" ; 
		sqlDesempenios +="								 INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id \n" ; 
				//"				 				 LEFT JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au+
		sqlDesempenios +="				 		 INNER JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id AND desau.id_cpu="+id_cpu+" AND desau.`id_au`=\n" + id_au; 
		sqlDesempenios +="								 WHERE com.`id_dcare`=? AND des.id_gra=?";
		if(!id_cur.equals("T")) {
			if(!id_cur.equals("")) {
			sqlDesempenios +=" AND desau.id_cua="+id_cur;
			}
		}	
		//sqlDesempenios +="		ORDER BY des.orden";
		
		List<Map<String, Object>> desempenios = jdbcTemplate.queryForList(sqlDesempenios,new Object[] {id_dcare, id_gra});
		List<Map<String, Object>> cursos = new ArrayList<>();
		if(id_cur.equals("T")) {
			String sqlCursos="SELECT DISTINCT com.`id` id_com, cur.nom curso, cua.`id` id_cua\n" + 
					"FROM `aca_competencia_dc` com INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.id\n" + 
					"INNER JOIN `aca_capacidad_dc` cap ON cap.`id_com`=com.id\n" + 
					"INNER JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`est`='A' AND desau.id_cap=cap.id \n" + 
					"INNER JOIN `col_curso_anio` cua ON desau.`id_cua`=cua.`id`\n" + 
					"INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id\n" + 
					"WHERE desau.id_cpu=? AND desau.`id_au`=? AND com.id_dcare=?";	
			
			cursos = jdbcTemplate.queryForList(sqlCursos,new Object[] {id_cpu, id_au, id_dcare});
		}

		List<Map<String, Object>> alumnos = new ArrayList<Map<String, Object>>();

		String sqlAlumnos = "SELECT alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,' ',per.nom) alumno, per.nro_doc nro_doc, mat.id_sit "
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
				+ " INNER JOIN col_persona per ON per.id=alu.id_per"
				+ " WHERE mat.`id_au_asi`=?  "// alumno and alu.id=2714
																														// garcia
																														// pinachi
				+ " ORDER BY per.ape_pat, per.ape_mat, per.nom";
		//logger.info(sqlAlumnos);
		alumnos = jdbcTemplate.queryForList(sqlAlumnos, new Object[] { id_au });

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();// aca
																		// ira
																		// el
																		// detalle
																		// de
																		// cada
																		// alumno
			detalle.put("alumno", map.get("alumno"));
			detalle.put("id_sit", map.get("id_sit"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nota", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		List<Map<String, Object>> notas = new ArrayList<Map<String, Object>>();
		
		String sqlNotas="SELECT DISTINCT alu.id id_alu, nn.nota, mat.id_sit,nn.id id_not, desau.id id_desau, cap.`id` id_cap , cap.`id_com`, com.`peso`, prom_com.id id_prom_com, prom_com.`prom` prom_com, prom_com.id_cua, prom_per.`id` id_prom_per, prom_per.`prom` prom_periodo \n" ;
				sqlNotas += "FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n"; 
				sqlNotas += "INNER JOIN `col_curso_aula` cca ON mat.`id_au_asi`=cca.`id_au`\n"; 
				sqlNotas += "INNER JOIN `col_area_anio` caa ON cca.`id_caa`=caa.`id`\n";
				sqlNotas += "INNER JOIN `aca_dcn_area` dcare ON dcare.`id`=caa.`id_adc`\n"; 
				sqlNotas += "INNER JOIN `aca_dcn_nivel` dcniv ON dcare.`id_dcniv`=dcniv.`id`\n"; 
				sqlNotas += "INNER JOIN `col_conf_anio_acad_dcn` adcn ON adcn.`id_anio`=caa.`id_anio`\n"; 
				sqlNotas += "INNER JOIN `aca_competencia_dc` com ON com.`id_dcare`=dcare.`id`\n"; 
				sqlNotas += "INNER JOIN `aca_capacidad_dc` cap ON com.`id`=cap.`id_com`\n"; 
				sqlNotas += "INNER JOIN `aca_desempenio_dc` des ON des.`id_com`=com.`id` \n"; 
				sqlNotas += "INNER JOIN `aca_desempenio_aula` desau ON desau.`id_desdc`=des.`id` AND desau.`id_au`=mat.`id_au_asi` AND desau.`id_cap`=cap.id AND desau.est='A' \n"; 
				sqlNotas += "LEFT JOIN not_nota_des nn ON nn.id_alu=alu.id AND nn.id_desau=desau.id\n"; 
				sqlNotas += "LEFT JOIN not_promedio_com prom_com ON prom_com.`id_alu`=alu.`id` AND prom_com.`id_com`=com.`id` AND prom_com.`id_cpu`=desau.`id_cpu` \n";
				if(!id_cur.equals("T")) {
					if(!id_cur.equals("")) {
					sqlNotas += " AND prom_com.id_cua=desau.id_cua \n";
					}
				} else if(id_cur.equals("T"))	{
					sqlNotas += " AND prom_com.id_cua=desau.id_cua \n";
				}
				sqlNotas += "LEFT JOIN `not_promedio_per` prom_per ON prom_per.`id_alu`=alu.id AND prom_per.`id_cpu`=desau.`id_cpu` AND prom_per.`id_dcare`=dcare.`id` AND prom_per.`id_au`=mat.`id_au_asi` \n";
				if(!id_cur.equals("T")) {
					if(!id_cur.equals("")) {
					sqlNotas += " AND prom_per.`id_cua`=desau.`id_cua`  \n";
					}
				}
				sqlNotas += "WHERE adcn.`id_anio`=? AND desau.`id_au`=? AND desau.`id_cpu`=? AND dcare.`id`=? AND (mat.id_sit NOT IN(5) OR mat.id_sit IS NULL) \n"; // and alu.id=2714 // and alu.id=1470
				if(!id_cur.equals("T")) {
					if(!id_cur.equals("")) {
					sqlNotas += " AND desau.`id_cua`="+id_cur; // AND prom_com.id_cua="+id_cur
					}
				}
		/*String sqlNotas="SELECT alu.id id_alu, nni.nota,  nni.id id_not, cap.id id_cap, cap.id_com, com.peso "
				+ " FROM `col_curso_aula` cca INNER JOIN  `mat_matricula` mat  ON mat.`id_au_asi`=cca.`id_au` AND (mat.id_sit IS NULL OR mat.id_sit<>5)"
				+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`  "
				+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id` "
				+ " INNER JOIN `not_evaluacion` ne ON  ne.`id_cca`=cca.`id`  "
				+ " INNER JOIN `not_nota` nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_ind_eva` nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id`  "
				+ " INNER JOIN `not_nota_indicador` nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
				+ " INNER JOIN `col_indicador` ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ " INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.`id`"
				+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
				+ " WHERE  cca.`id_au`=? AND cua.`id_cur`=? AND ne.`nump`=?  "
				+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom, cap.`id`";*/
		//logger.info(sqlNotas);
		//notas = jdbcTemplate.queryForList(sqlNotas, new Object[] { id_au, id_cur, 1 });
		notas = jdbcTemplate.queryForList(sqlNotas, new Object[] { id_anio,id_au, id_cpu, id_dcare});

		for (Map<String, Object> map : notas) {

			Map<String, Object> alumno = linkalumnos.get(map.get("id_alu").toString());
			// notas en ese momento
			Object notaObject = alumno.get("nota");

			if (notaObject == null) { //
				List<AlumnoNotaBean> notaAlumno = new ArrayList<AlumnoNotaBean>();
				List<PromedioBean> prom = new ArrayList<PromedioBean>();
				AlumnoNotaBean nota = new AlumnoNotaBean();
				nota.setId_alu((Integer) map.get("id_alu"));
				nota.setId_not((Integer) map.get("id_not"));
				nota.setNota((Integer) map.get("nota"));
				nota.setId_cap((Integer) map.get("id_cap"));
				nota.setId_com((Integer) map.get("id_com"));
				nota.setId_desau((Integer) map.get("id_desau"));
				nota.setProm_com((BigDecimal)map.get("prom_com"));
				nota.setProm_per((BigDecimal)map.get("prom_periodo"));
				nota.setId_cua((Integer) map.get("id_cua"));
				//BigDecimal peso = new BigDecimal((Double) map.get("peso"));
				BigDecimal peso = (BigDecimal) map.get("peso");
				nota.setPeso(peso);
				notaAlumno.add(nota);
				alumno.put("nota", notaAlumno);
			} else {
				List<AlumnoNotaBean> notaAlumno = (List<AlumnoNotaBean>) notaObject;
				List<PromedioBean> prom = (List<PromedioBean>) notaObject;
				AlumnoNotaBean nota = new AlumnoNotaBean();
				nota.setId_alu((Integer) map.get("id_alu"));
				nota.setId_not((Integer) map.get("id_not"));
				nota.setNota((Integer) map.get("nota"));
				nota.setId_cap((Integer) map.get("id_cap"));
				nota.setId_com((Integer) map.get("id_com"));
				nota.setId_desau((Integer) map.get("id_desau"));
				nota.setProm_com((BigDecimal)map.get("prom_com"));
				nota.setProm_per((BigDecimal)map.get("prom_periodo"));
				nota.setId_cua((Integer) map.get("id_cua"));
				//BigDecimal peso = new BigDecimal((Double) map.get("peso"));
				BigDecimal peso = (BigDecimal) map.get("peso");
				nota.setPeso(peso);
				notaAlumno.add(nota);
				alumno.put("nota", notaAlumno);
			}

		}

		// itera los alumnos
		LinkedHashMap<String, Map<String, Object>> linkalumnos1 = new LinkedHashMap<String, Map<String, Object>>();
		List<Map<String, Object>> listAlumnos = new ArrayList<>();
		for (Map.Entry<String, Map<String, Object>> entry : linkalumnos.entrySet()) {
			String key = entry.getKey();// id del alumno
			Map<String, Object> notaMap = entry.getValue();
			List<AlumnoNotaBean> notasAlumno = (List<AlumnoNotaBean>) notaMap.get("nota");// aca
																							// tenemos
																							// las
																							// notas
																							// del
																							// alumno
			Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
			Map<Integer, BigDecimal> notasCursos = new HashMap<Integer, BigDecimal>();
			Map<Integer, BigDecimal> competenciaCont = new HashMap<Integer, BigDecimal>();
			BigDecimal promedioGeneral = new BigDecimal(0);
			List<AlumnoNotaCursoBean> notas_cursos=new ArrayList<AlumnoNotaCursoBean>();
			// aca tenemos las 5 notas
			if(id_cur.equals("T")) {
				if (notasAlumno != null) {	
					for (Map<String, Object> map3 : competencias) {
						Integer id_competencia=Integer.parseInt(map3.get("com_id").toString());
						BigDecimal nota_tot_com= new BigDecimal(0);
						BigDecimal nota_comp= new BigDecimal(0);;
						//BigDecimal nota_tot_curso= new BigDecimal(0);
						BigDecimal nota_prom_cur_com= new BigDecimal(0);
						Integer cant_notas=0;
						Integer cant_notas_com=0;
						//Integer cant_notas_cursos=0;
						for (Map<String, Object> map2 : cursos) {
							System.out.println("map->>"+map2);
							Integer id_cua=Integer.parseInt(map2.get("id_cua").toString());
							Integer id_com=Integer.parseInt(map2.get("id_com").toString());
							BigDecimal nota_tot_curso= new BigDecimal(0);
							Integer cant_notas_cursos=0;
							if(id_com.equals(id_competencia)) {
								for (AlumnoNotaBean alumnoNotaBean : notasAlumno) {
									Integer id_com_nota = alumnoNotaBean.getId_com();
									Integer id_cua_nota=alumnoNotaBean.getId_cua();
									BigDecimal promedio_com = alumnoNotaBean.getProm_com();
									System.out.println("id_alu->>"+alumnoNotaBean.getId_alu());
									System.out.println("id_com_nota->>"+id_com_nota);
									System.out.println("id_cua_nota->>"+id_cua_nota);
									if(id_cua_nota!=null) {
										if(id_cua.equals(id_cua_nota) && id_com.equals(id_com_nota)) {
											nota_prom_cur_com=nota_prom_cur_com.add(promedio_com);
											cant_notas = cant_notas + 1;
											//esto deberia estar aqui
											nota_tot_curso=nota_tot_curso.add(promedio_com);
											cant_notas_cursos = cant_notas_cursos + 1;
											
										}
									}
									
									System.out.println("entro 1.1->>");
									/*if(promedio_com!=null) {
										if(id_com.equals(id_com_nota)) {
											nota_comp=nota_comp.add(promedio_com);
											cant_notas_com=cant_notas_com+1;
										}
									}*/
									
									
									//competenciaCont.put(id_com, competenciaCont.get(id_com).add(new BigDecimal(1)));
									
								}	
								/*if(!nota_prom_cur_com.equals(new BigDecimal(0))) {
									System.out.println("entro 1.1_1->>");
									nota_tot_curso=nota_prom_cur_com.divide(new BigDecimal(cant_notas));
									
									System.out.println("entro 1.2->>");
								}*/
								if(!nota_tot_curso.equals(new BigDecimal(0))) {
									System.out.println("entro 1.1_1->>");
									nota_tot_curso=nota_tot_curso.divide(new BigDecimal(cant_notas_cursos));
									
									System.out.println("entro 1.2->>");
								}	
									AlumnoNotaCursoBean alumnoNotaCursoBean = new AlumnoNotaCursoBean();
									alumnoNotaCursoBean.setId_com(id_com);
									alumnoNotaCursoBean.setId_cua(id_cua);
									alumnoNotaCursoBean.setNota(nota_tot_curso);
									notas_cursos.add(alumnoNotaCursoBean);
									if(!nota_tot_curso.equals(new BigDecimal(0))) {
											nota_comp=nota_comp.add(new BigDecimal(nota_tot_curso.setScale(0, RoundingMode.HALF_UP).intValue()));
											cant_notas_com=cant_notas_com+1;
									}
								
							}
							
						}
						if(!nota_comp.equals(new BigDecimal(0))) {
							nota_tot_com=nota_comp.divide(new BigDecimal(cant_notas_com));
						}
						
						competenciaSuma.put(id_competencia, nota_tot_com);
					}
					
					/*for (AlumnoNotaBean alumnoNotaBean : notasAlumno) {
						int id_cap = alumnoNotaBean.getId_cap();
						int id_com = alumnoNotaBean.getId_com();
						BigDecimal nota = null;
						if(alumnoNotaBean.getNota()!=null) {
							nota = new BigDecimal(alumnoNotaBean.getNota());
						}
						
						Object suma = competenciaSuma.get(id_com);

						BigDecimal sumaTotal = new BigDecimal(0);
						BigDecimal promedio = new BigDecimal(0);
						BigDecimal sumaPromedio = new BigDecimal(0);
						int cant_notas = 1;
						if (suma != null) {
							sumaTotal = (BigDecimal) competenciaSuma.get(id_com);
							competenciaCont.put(id_com, competenciaCont.get(id_com).add(new BigDecimal(1)));
							cant_notas = cant_notas + 1;
							
						} else
							competenciaCont.put(id_com, new BigDecimal(1));
						if(nota!=null) {
							sumaTotal = sumaTotal.add(nota);
							promedio = sumaTotal.divide(new BigDecimal(cant_notas));
							sumaPromedio = sumaPromedio.add(promedio);
						}
						
						competenciaSuma.put(id_com, sumaTotal);

					}*/
				}

			} else {
				if (notasAlumno != null) {
					for (AlumnoNotaBean alumnoNotaBean : notasAlumno) {
						Integer id_com = alumnoNotaBean.getId_com();
						/*Integer cant=0;
						for (Map<String, Object> map3 : competencias) {
								Integer id_com_conf=Integer.parseInt(map3.get("com_id").toString());
								if(id_com.equals(id_com_conf)){
									
								}
						}*/
						
						BigDecimal promedio_com = alumnoNotaBean.getProm_com();
						//competenciaCont.put(id_com, competenciaCont.get(id_com).add(new BigDecimal(1)));
						competenciaSuma.put(id_com, promedio_com);
					}
					
				}
			}

			//System.out.println("entro 1.3->>");
			for (Integer id_com : competenciaSuma.keySet()) {

				/*competenciaSuma.put(id_com,
						competenciaSuma.get(id_com).divide(competenciaCont.get(id_com), RoundingMode.HALF_UP));*/
				competenciaSuma.put(id_com,
						competenciaSuma.get(id_com));
			}

			notaMap.put("competenciaSuma", competenciaSuma);
			notaMap.put("notasCursos", notas_cursos);
			if(notasAlumno!=null) {
				notaMap.put("promedioGeneral", notasAlumno.get(0).getProm_per());
			} else {
				notaMap.put("promedioGeneral", null);
			}
			
			listAlumnos.add(notaMap);
			// notaMap.put("competenciaSuma", competenciaSuma.GET);
			// notaMap.put("promediogeneral", promedioGeneral);//purebalo por
			// faovr quiero ver el json final

		} // linkalumnos

		/*List<Map<String, Object>> listAlumnos = new ArrayList<>();

		for (Map.Entry<String, Map<String, Object>> entry : linkalumnos.entrySet()) {
			String key = entry.getKey();// id del alumno
			Map<String, Object> notaMap = entry.getValue();
			BigDecimal promedio_com = alumnoNotaBean.getProm_com();
			Map<Integer, BigDecimal> promediosCom = (Map<Integer, BigDecimal>) notaMap.get("competenciaSuma");// aca
																												// tenemos
																												// las
																												// notas
																												// del
																												// alumno,
																												// ok?
																												// oki
			Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
			// aca tenemos las 5 notas
			BigDecimal promedioGeneral = new BigDecimal(0);
			BigDecimal sumaPromedio = new BigDecimal(0);
			if (promediosCom != null) {
				for (Map<String, Object> competencia : competencias) {
					Integer id_com = (Integer) competencia.get("com_id");
					if (promediosCom.get(id_com) != null)
						sumaPromedio = sumaPromedio.add(promediosCom.get(id_com));

				}
			}
			
			promedioGeneral = sumaPromedio.divide(new BigDecimal(competencias.size()), RoundingMode.HALF_UP);
			notaMap.put("promedioGeneral", promedioGeneral);

			listAlumnos.add(notaMap);
		}*/

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("competencias", competencias);
		map.put("capacidades", capacidades);
		map.put("desempenios", desempenios);
		map.put("cursos", cursos);
		map.put("alumnos", listAlumnos);
		return map;
	}
	
	
	public Map<String, Object> listNotasAlumnoPeriodo(Integer id_gir,Integer id_anio,Integer id_per,Integer id_cpu) {

		//Obtener las áreas
		Alumno alumno = alumnoDAO.getByParams(new Param("id_per",id_per));
		Param param1 = new Param();
		param1.put("mat.id_alu", alumno.getId());
		param1.put("pee.id_anio", id_anio);
		param1.put("ser.id_gir", id_gir);
		Matricula matricula = matriculaDAO.listFullByParams(param1, null).get(0);
		
		  ConfAnioAcadDcn confAnioAcadDcn = confAnioAcadDcnDAO.getByParams(new Param("id_anio",id_anio));
		  Param param = new Param();
	      param.put("id_niv", matricula.getId_niv());
	      param.put("id_dcn", confAnioAcadDcn.getId_dcn());
	      DcnNivel dcnNivel= dcNivelDAO.getByParams(param);
	      List<AreaCompetenciaReq> areas_competencia= new ArrayList<AreaCompetenciaReq>();
	      //Obtengo la lista de Areas
	      List<Row> areas= dcnAreaDAO.listarAreasComboAnio(dcnNivel.getId(),id_anio, matricula.getId_gra(), id_gir);
	       for (Row row : areas) {
				List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),new String[]{"orden asc"});
				List<CompetenciaDCReq> competenciaDCReqs = new ArrayList<CompetenciaDCReq>();
				for (CompetenciaDc competenciaDC : competenciasDc) {
					CompetenciaDCReq competenciaDCReq = new CompetenciaDCReq();
					competenciaDCReq.setId(competenciaDC.getId());
					competenciaDCReq.setNom(competenciaDC.getNom());
					competenciaDCReq.setId_area(competenciaDC.getId_dcare());
					//poner la nota final por competencia
					
					competenciaDCReqs.add(competenciaDCReq);
				}
				AreaCompetenciaReq area_com= new AreaCompetenciaReq();
				area_com.setId(row.getInteger("id"));
				area_com.setNom_area(row.getString("value"));
				area_com.setCompetencias(competenciaDCReqs);
				areas_competencia.add(area_com);
	       }	

	       List<AreaNotaReq> areas_nota=notaDAO.promediosAreasMatricula(matricula.getId(), matricula.getId_alu(), matricula.getId_gra(), matricula.getId_au_asi(),id_cpu, id_anio, dcnNivel.getId(), id_gir);
	
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("areas_competencia", areas_competencia);
		map.put("areas_nota", areas_nota);
		return map;
	}
	
	

	public Map<String, Object> listarNotasCurso_old(Integer id_anio, Integer id_gra, Integer id_niv, Integer id_au,
			Integer nump) {

		// alumnos
		List<Row> alumnos = new ArrayList<Row>();
		if (id_au != -1) {
			String sqlAlumnos = "SELECT gr.orden grado, au.secc, au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad " + " WHERE mat.id_au_asi=? "
					+ " AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5) "// alumno
																									// garcia
																									// pinachi
					+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
			alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_au });

		} else {
			String sqlAlumnos = "SELECT gr.orden grado, au.secc, au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " WHERE per.`id_anio`=? AND au.`id_grad`=? "
					+ " AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+ " ORDER BY au.secc, alu.ape_pat, alu.ape_mat, alu.nom";
			alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra });

		}

		// alumnos = jdbcTemplate.queryForList(sqlAlumnos, new Object[]{id_au});

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();// aca
																		// ira
																		// el
																		// detalle
																		// de
																		// cada
																		// alumno
			detalle.put("alumno", map.get("alumno"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nota", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		// obtener cursos x seccion
		List<Row> cursos;
		if (id_au != -1)
			cursos = curso_anioDAO.listaCursosAula(id_anio, id_au);
		else
			cursos = curso_anioDAO.listaCursosGrado(id_anio, id_gra);

		// iteramos por alumno todos sus cursos
		for (Row alumno : alumnos) {

			Integer id_alu = alumno.getInteger("id");

			logger.debug("alumno::::::::::::::::::::::::::");
			logger.debug(alumno);

			List<Map<String, Object>> cursoAlumno = new ArrayList<Map<String, Object>>();
			// iteracion por curso
			for (Row curso : cursos) {

				logger.debug("::::::::::::::::::::::::::::curso::::::::::::::::::::::::::");
				logger.debug(curso);

				Integer id_cur = curso.getInteger("id");
				// String curso = row.getString("value");

				// competencias
				String sqlCompetencias = "SELECT DISTINCT com.id com_id, com.nom, COUNT(com.id) count "
						+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
						+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
						+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
						+ " INNER JOIN `col_curso_subtema` ccs  ON ccs.`id_cur`=cua.`id_cur`"
						+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis` AND ccs.id=cis.`id_sub`"
						+ " INNER JOIN col_subtema_capacidad csc ON csc.`id_ccs`=ccs.`id`"
						+ " INNER JOIN  col_capacidad cap ON csc.`id_cap`=cap.`id`"
						+ " INNER JOIN col_competencia com ON com.id = cap.id_com AND ccs.`id_cur`=com.`id_cur`"
						+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.id AND ind.`id_cap`=cap.`id`"
						+ " WHERE cua.`id_cur`=:id_cur  AND cca.`id_au`=:id_au AND (:nump=0 or ne.nump=:nump) AND nie.`est`='A' "
						+ " GROUP BY com.id, com.nom " + " ORDER BY com.id ";

				// List<Row> competencias = sqlUtil.query(sqlCompetencias, new
				// Object[]{id_cur, alumno.get("id_au"), nump});
				Param param = new Param();
				param.put("id_cur", id_cur);
				param.put("id_au", alumno.get("id_au"));
				param.put("nump", nump);

				List<Row> competencias = sqlUtil.query(sqlCompetencias, param);
				curso.put("competencias", competencias);

				// capacidades
				String sqlCapacidad = "SELECT  com.id com_id, com.nom com_nom,cap.id, cap.nom"
						+ " FROM `not_evaluacion` ne INNER JOIN not_ind_eva nie ON nie.`id_ne`=ne.`id` "
						+ " INNER JOIN `col_curso_aula` cca ON ne.`id_cca`=cca.`id` "
						+ " INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id`"
						+ " INNER JOIN `col_curso_subtema` ccs  ON ccs.`id_cur`=cua.`id_cur`"
						+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis` AND ccs.id=cis.`id_sub`"
						+ " INNER JOIN col_subtema_capacidad csc ON csc.`id_ccs`=ccs.`id`"
						+ " INNER JOIN  col_capacidad cap ON csc.`id_cap`=cap.`id`"
						+ " INNER JOIN col_competencia com ON com.id = cap.id_com AND ccs.`id_cur`=com.`id_cur`"
						+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.id AND ind.`id_cap`=cap.`id`"
						+ " WHERE cua.`id_cur`=:id_cur  AND cca.`id_au`=:id_au AND  (:nump=0 or ne.nump=:nump)  AND nie.`est`='A'"
						+ " ORDER BY cap.id";
				// //logger.info(sqlCapacidad);
				List<Row> capacidades = sqlUtil.query(sqlCapacidad, param);

				curso.put("capacidades", capacidades);

				// notas
				List<Row> notas = new ArrayList<Row>();
				String sqlNotas = "SELECT alu.id id_alu, nni.nota,  nni.id id_not, ind.`id_cap`, cap.id_com, com.peso "
						+ " FROM `col_curso_aula` cca INNER JOIN  `mat_matricula` mat  ON mat.`id_au_asi`=cca.`id_au` "
						+ " INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`  INNER JOIN `col_curso_anio` cua ON cca.`id_cua`=cua.`id` "
						+ " INNER JOIN `not_evaluacion` ne ON  ne.`id_cca`=cca.`id`  "
						+ " INNER JOIN `not_nota` nn ON nn.`id_alu`=alu.`id` AND nn.`id_ne`=ne.`id`  "
						+ " INNER JOIN `not_ind_eva` nie ON  nie.`est`='A' AND nie.`id_ne`=ne.`id`  "
						+ " INNER JOIN `not_nota_indicador` nni ON nn.`id`=nni.`id_not`  AND nni.`id_nie`=nie.`id` "
						+ " INNER JOIN `col_ind_sub` cis ON cis.id=nie.`id_cis`"
						+ " INNER JOIN `col_indicador` ind ON cis.`id_ind`=ind.`id`"
						+ " INNER JOIN col_capacidad cap ON cap.id=ind.id_cap "
						+ " INNER JOIN col_competencia com ON cap.id_com=com.id "
						+ " WHERE  cca.`id_au`=:id_au AND cua.`id_cur`=:id_cur AND (:nump=0 or ne.nump=:nump)  AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
						+ " AND alu.id=:id_alu" // este es nuevo por alumno
						+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom, ind.id_cap ";
				//logger.info(param);
				param.put("id_alu", id_alu);
				notas = sqlUtil.query(sqlNotas, param);

				Map<String, Object> notasCursoAlumno = new HashMap<String, Object>();
				notasCursoAlumno.put("id_cur", id_cur);

				//logger.info("===========notas============================================");
				//logger.info(notas);

				for (Map<String, Object> map : notas) {// notas por alumno y x
														// curso

					// Map<String, Object> alumno
					// =linkalumnos.get(map.get("id_alu").toString());
					// notas en ese momento
					Object notaObject = notasCursoAlumno.get("nota");

					if (notaObject == null) { //
						List<AlumnoNotaBean> notaAlumno = new ArrayList<AlumnoNotaBean>();
						List<PromedioBean> prom = new ArrayList<PromedioBean>();
						AlumnoNotaBean nota = new AlumnoNotaBean();
						nota.setId_alu((Integer) map.get("id_alu"));
						nota.setId_not((Integer) map.get("id_not"));
						nota.setNota((Integer) map.get("nota"));
						nota.setId_cap((Integer) map.get("id_cap"));
						nota.setId_com((Integer) map.get("id_com"));
						BigDecimal peso = new BigDecimal((Double) map.get("peso"));
						nota.setPeso(peso);
						notaAlumno.add(nota);
						notasCursoAlumno.put("nota", notaAlumno);
					} else {
						List<AlumnoNotaBean> notaAlumno = (List<AlumnoNotaBean>) notaObject;
						List<PromedioBean> prom = (List<PromedioBean>) notaObject;
						AlumnoNotaBean nota = new AlumnoNotaBean();
						nota.setId_alu((Integer) map.get("id_alu"));
						nota.setId_not((Integer) map.get("id_not"));
						nota.setNota((Integer) map.get("nota"));
						nota.setId_cap((Integer) map.get("id_cap"));
						nota.setId_com((Integer) map.get("id_com"));
						BigDecimal peso = new BigDecimal((Double) map.get("peso"));
						nota.setPeso(peso);
						notaAlumno.add(nota);
						notasCursoAlumno.put("nota", notaAlumno);
					}

				}

				List<AlumnoNotaBean> notasAlumno = (List<AlumnoNotaBean>) notasCursoAlumno.get("nota");// aca
																										// tenemos
																										// las
																										// notas
																										// del
																										// alumno
																										// de
																										// un
																										// curso
				Map<Integer, BigDecimal> competenciaSuma = new HashMap<Integer, BigDecimal>();
				Map<Integer, BigDecimal> competenciaCont = new HashMap<Integer, BigDecimal>();
				// aca tenemos las 5 notas

				if (notasAlumno == null) {// todavia no ingresan notas

				} else {

					for (AlumnoNotaBean alumnoNotaBean : notasAlumno) {
						int id_cap = alumnoNotaBean.getId_cap();
						int id_com = alumnoNotaBean.getId_com();
						BigDecimal nota = new BigDecimal(alumnoNotaBean.getNota());

						Object suma = competenciaSuma.get(id_com);

						BigDecimal sumaTotal = new BigDecimal(0);
						BigDecimal promedio = new BigDecimal(0);
						BigDecimal sumaPromedio = new BigDecimal(0);
						int cant_notas = 1;
						if (suma != null) {
							sumaTotal = (BigDecimal) competenciaSuma.get(id_com);
							competenciaCont.put(id_com, competenciaCont.get(id_com).add(new BigDecimal(1)));
							cant_notas = cant_notas + 1;
						} else
							competenciaCont.put(id_com, new BigDecimal(1));

						sumaTotal = sumaTotal.add(nota);
						promedio = sumaTotal.divide(new BigDecimal(cant_notas));
						sumaPromedio = sumaPromedio.add(promedio);

						competenciaSuma.put(id_com, sumaTotal);

					}

					for (Integer id_com : competenciaSuma.keySet()) {

						competenciaSuma.put(id_com,
								competenciaSuma.get(id_com).divide(competenciaCont.get(id_com), RoundingMode.HALF_UP));
					}

					logger.debug("competenciaSuma:" + competenciaSuma);
					notasCursoAlumno.put("competenciaSuma", competenciaSuma);

					// calculo de promedio por curso
					Map<Integer, BigDecimal> promediosCom = (Map<Integer, BigDecimal>) notasCursoAlumno
							.get("competenciaSuma");// aca tenemos las notas del
													// alumno, ok? oki
					competenciaSuma = new HashMap<Integer, BigDecimal>();

					// aca tenemos las 5 notas
					BigDecimal promedioGeneral = new BigDecimal(0);
					BigDecimal sumaPromedio = new BigDecimal(0);
					for (Map<String, Object> competencia : competencias) {
						Integer id_com = (Integer) competencia.get("com_id");
						sumaPromedio = sumaPromedio.add(promediosCom.get(id_com));

					}
					logger.debug("sumaPromedio:" + sumaPromedio);

					promedioGeneral = sumaPromedio.divide(new BigDecimal(competencias.size()), RoundingMode.HALF_UP);
					notasCursoAlumno.put("promedioGeneral", promedioGeneral);

					logger.debug("notasCursoAlumno------>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					logger.debug(notasCursoAlumno);

					notasCursoAlumno.remove("nota");
					notasCursoAlumno.remove("competenciaSuma");

					cursoAlumno.add(notasCursoAlumno);

				}

			} // iteracion por cursos

			alumno.put("cursos", cursoAlumno);

		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cursos", cursos);
		map.put("alumnos", alumnos);
		return map;
	}

	public Map<String, Object> listarNotasCurso(Integer id_anio, Integer id_gra, Integer id_niv, Integer id_au,
			Integer nump) {

		// alumnos
		List<Row> alumnos = new ArrayList<Row>();
		Aula aulaEspecial= null;
		if (id_au != -1) {
			
			Param param = new Param();
			param.put("id_au", id_au);
			
			List<AulaEspecial> aulaEspeciales = aulaEspecialDAO.listByParams(param,null ); 
			

			if (!aulaEspeciales.isEmpty()){
				
				param = new Param();
				param.put("id_grad",aulaEspeciales.get(0).getId_gra());
				
				aulaEspecial = aulaDAO.getByParams(param);
				
			}
			
			String sqlAlumnos = "SELECT mat.id id_mat,";
			//gr.orden grado,
			if (aulaEspecial!=null)
				sqlAlumnos += " gr_esp.abrv grado, ";
			else
				sqlAlumnos += " gr.abrv grado, ";
			
			sqlAlumnos += "au.secc, au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad " 
					+ " LEFT JOIN `col_aula` esp ON esp.id = mat.id_au " //cuando existe aula especial
					+ " LEFT JOIN cat_grad gr_esp ON gr_esp.id=esp.id_grad " 
					+ " WHERE ";
			
			if (aulaEspecial!=null)
				sqlAlumnos += " (mat.id_au=? and mat.id_au_asi=" + aulaEspecial.getId() + ") ";
			else
				sqlAlumnos += " mat.id_au_asi=? ";
			
			sqlAlumnos += "  AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5) "// alumno
																									// garcia
																									// pinachi
					+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
			alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_au });

		} else {
			String sqlAlumnos = "SELECT mat.id id_mat, gr.orden grado, au.secc, au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " WHERE per.`id_anio`=? AND au.`id_grad`=? "
					+ " AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+ " ORDER BY au.secc, alu.ape_pat, alu.ape_mat, alu.nom";
			alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra });

		}

		// alumnos = jdbcTemplate.queryForList(sqlAlumnos, new Object[]{id_au});

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();// aca
																		// ira
																		// el
																		// detalle
																		// de
																		// cada
																		// alumno
			detalle.put("alumno", map.get("alumno"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nota", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		// obtener cursos x seccion
		List<Row> cursos;
		if (id_au != -1){
			if (aulaEspecial!=null)
				cursos = curso_anioDAO.listaCursosAula(id_anio, aulaEspecial.getId());
			else
				cursos = curso_anioDAO.listaCursosAula(id_anio, id_au);
		}else
			cursos = curso_anioDAO.listaCursosGrado(id_anio, id_gra);

		// iteramos por alumno todos sus cursos
		for (Row alumno : alumnos) {

			Integer id_alu = alumno.getInteger("id");
			Integer id_mat = alumno.getInteger("id_mat");

			logger.debug("alumno::::::::::::::::::::::::::");
			logger.debug(alumno);

			Map<Integer, Object> cursoAlumno = new HashMap<Integer, Object>();

			// iteracion por curso
			for (Row curso : cursos) {

				logger.debug("::::::::::::::::::::::::::::curso::::::::::::::::::::::::::");
				logger.debug(curso);

				Integer id_cur = curso.getInteger("id");

				// notas
				List<Row> notas = new ArrayList<Row>();
				
				String sqlNotas = "SELECT ARE.id_cur, ROUND(AVG(ARE.promedio))  promedio"
						+"\n 	FROM"
						+"\n 	(	"
						+"\n 		SELECT PER.nump, PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
						+"\n 		FROM"
						+"\n 		(	"
						+"\n 			SELECT CURSO.nump, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
						+"\n 			FROM ("
						/*+"\n 				SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
						+"\n 				FROM not_evaluacion ne"
						+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu" 
						+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
						+"\n 				INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`"
						+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
						+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
						+"\n 				INNER JOIN col_indicador ind ON ind.id=nie.id_ind " //aqui me quede
						+"\n 				INNER JOIN col_ind_sub cis ON cis.id = nie.id_cis"
						+"\n 				INNER JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
						+"\n 				INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
						+"\n 				INNER JOIN col_tema tem ON tem.id = sub.id_tem"
						+"\n 				INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
						+"\n 				INNER JOIN col_capacidad cap ON cap.id = ci.id_cap"
						+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
						+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
						+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca "
						+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";*/
						+"\n 				SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
						+"\n 				FROM not_evaluacion ne"
						+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu"
						+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
						+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
						+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
						+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
						+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
						+"\n 				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
						+"\n 				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
						+"\n 				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
						+"\n 				INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
						+"\n 				INNER JOIN col_capacidad cap ON cap.id = cgc.`id_cap`"
						+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
						+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
						+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca" 
						+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
			if(nump==0)
				sqlNotas += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
				
				sqlNotas += "\n 				INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
						+"\n 				WHERE (:nump=0 or ne.nump=:nump) AND "
						+"\n 				per.id_anio=:id_anio"
						+"\n 				AND nie.est='A'"
						+"\n 				AND mat.ID=:id_mat"
						+"\n				AND cua.id_cur=:id_cur"
						+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
						+"\n 			) CURSO"
						+"\n 			GROUP BY 	"
						+"\n 			CURSO.nump, CURSO.id_cur, CURSO.com_id"
						+"\n 		) PER "
						+"\n 		GROUP BY PER.nump, PER.id_cur"
						+"\n 	)ARE "
						+"\n 	GROUP BY ARE.id_cur";

				Param param = new Param();
				param.put("id_cur", id_cur);
				param.put("id_alu", id_alu);
				param.put("id_anio", id_anio);
				param.put("id_mat", id_mat);
				param.put("id_cur", id_cur);
				param.put("nump", nump);
				//logger.info(param);

				notas = sqlUtil.query(sqlNotas, param);
				if (notas.size() == 0)
					cursoAlumno.put(id_cur, 0);
				else{
					cursoAlumno.put(id_cur, notas.get(0).getInteger("promedio"));
					

				}
				alumno.put("cursos", cursoAlumno);

				
							
			}
			
			
			if (id_niv.equals(EnumNivel.PRIMARIA.getValue()) || id_niv.equals(EnumNivel.SECUNDARIA.getValue()) ){
				//OBTENER EL PUNTEJA TOTAL
				String sqlPuntaje	="SELECT SUM(ARE.promedio)  puntaje"
						+"\n 	FROM "
						+"\n 	(	SELECT PER.nump, PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
						+"\n 		FROM"
						+"\n 		(	"
						+"\n 			SELECT CURSO.nump, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
						+"\n 			FROM ("
/*						+"\n 				SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
						+"\n 				FROM not_evaluacion ne"
						+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu" 
						+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
						+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
						+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
						+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
						+"\n				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
						+"\n 				INNER JOIN col_ind_sub cis ON cis.id = nie.id_cis"
						+"\n 				INNER JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
						+"\n 				INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
						+"\n 				INNER JOIN col_tema tem ON tem.id = sub.id_tem"
						+"\n 				INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
						+"\n 				INNER JOIN col_capacidad cap ON cap.id = ci.id_cap"
						+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
						+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
						+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca "
						+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";*/
						+"\n 				SELECT  ne.nump, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
						+"\n 				FROM not_evaluacion ne"
						+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu"
						+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
						+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
						+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
						+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
						+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
						+"\n 				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
						+"\n 				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
						+"\n 				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
						+"\n 				INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
						+"\n 				INNER JOIN col_capacidad cap ON cap.id = cgc.`id_cap`"
						+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
						+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
						+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca" 
						+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
			if(nump==0)
				sqlPuntaje += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
				
			sqlPuntaje += "\n 				INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
						+"\n 				WHERE (:nump=0 or ne.nump=:nump) AND "
						+"\n 				per.id_anio=:id_anio"
						+"\n 				AND nie.est='A'"
						+"\n 				AND mat.ID=:id_mat"
						//+"\n				AND cua.id_cur=:id_cur"
						+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
						+"\n 			) CURSO"
						+"\n 			GROUP BY 	"
						+"\n 			CURSO.nump, CURSO.id_cur, CURSO.com_id"
						+"\n 		) PER "
						+"\n 		GROUP BY PER.nump, PER.id_cur"
						+"\n 	)ARE "
						+"\n 	";
				
						Param param = new Param();
						param.put("id_alu", id_alu);
						param.put("id_anio", id_anio);
						param.put("id_mat", id_mat);
			 			param.put("nump", nump);
						
						List<Row> puntaje = sqlUtil.query(sqlPuntaje, param);
						
						alumno.put("puntaje", puntaje.get(0).getInteger("puntaje"));
			}
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cursos", cursos);
		map.put("alumnos", alumnos);
		return map;

	}

	public Map<String, Object> listarNotasArea(Integer id_anio, Integer id_gra, Integer id_niv, Integer id_au,
			Integer nump) {

		// alumnos
		List<Row> alumnos = new ArrayList<Row>();
		
		if (id_au != -1) {
			
			Param param = new Param();
			param.put("id_au", id_au);
			
			List<AulaEspecial> aulaEspeciales = aulaEspecialDAO.listByParams(param,null ); 
			
			Aula aulaEspecial= null;
			if (!aulaEspeciales.isEmpty()){
				
				param = new Param();
				param.put("aula.id_grad",aulaEspeciales.get(0).getId_gra());
				param.put("pee.id_anio",id_anio);
				
				aulaEspecial = aulaDAO.listFullByParams(param, null).get(0);
				
			}
				
			String sqlAlumnos = "SELECT mat.id id_mat,alu.nro_doc, ";
			if (aulaEspecial!=null)
				sqlAlumnos += " gr_esp.abrv grado, ";
			else
				sqlAlumnos += " gr.abrv grado, ";
			
			sqlAlumnos +=  " au.secc, au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` "
					+ " mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad " 
					+ " LEFT JOIN `col_aula` esp ON esp.id = mat.id_au " //cuando existe aula especial
					+ " LEFT JOIN cat_grad gr_esp ON gr_esp.id=esp.id_grad " 
					+ " WHERE ";
			
			if (aulaEspecial!=null){
				sqlAlumnos += " (mat.id_au_asi=" + aulaEspecial.getId() + ") ";
			}else
				sqlAlumnos += " mat.id_au_asi=" + id_au;
			
				
				//sqlAlumnos += " AND (mat.id_sit<>5 OR mat.id_sit IS NULL) ";// alumno

																									// garcia
																									// pinachi
			sqlAlumnos += " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
			
			alumnos = sqlUtil.query(sqlAlumnos );

		} else {
			String sqlAlumnos = "SELECT mat.id id_mat,alu.nro_doc, "
					+ " gr.abrv grado, au.secc, "
					+ " au.id id_au, alu.`id`, CONCAT(alu.ape_pat,' ',alu.ape_mat,' ',alu.nom) alumno, alu.nro_doc nro_doc "
					+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
					+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
					+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
					+ " WHERE per.`id_anio`=? AND au.`id_grad`=? "
					+ " AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+ " ORDER BY au.secc, alu.ape_pat, alu.ape_mat, alu.nom";
			alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra });

		}

		LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
		for (Map<String, Object> map : alumnos) {
			Map<String, Object> detalle = new HashMap<String, Object>();// aca
																		// ira
																		// el
																		// detalle
																		// de
																		// cada
																		// alumno
			detalle.put("alumno", map.get("alumno"));
			detalle.put("nro_doc", map.get("nro_doc"));
			detalle.put("id_alu", map.get("id"));
			detalle.put("nota", null);
			detalle.put("promedio", null);

			linkalumnos.put(map.get("id").toString(), detalle);

		}

		// obtener cursos x seccion
		Param param = new Param();
		param.put("caa.id_anio", id_anio);
		param.put("caa.id_niv", id_niv);
		List<AreaAnio> areas = area_anioDAO.listFullByParams(param, new String[] { "caa.ORD" });

		// iteramos por alumno todos sus cursos

		for (Row alumno : alumnos) {

			Integer id_alu = alumno.getInteger("id");
			Integer id_mat = alumno.getInteger("id_mat");

			// PROMEDIO GENERAL DEL ALUMNO
			BigDecimal promedioGeneral = getPromedioGeneral(id_alu, id_au,id_mat,id_anio, nump);

 
			Integer promedioGeneralInteger = null;
			if (promedioGeneral!=null)
				promedioGeneralInteger = promedioGeneral.intValue();

			if (promedioGeneralInteger!=null && promedioGeneralInteger < 11)
				promedioGeneralInteger = 11;// ESTO REEMPLAZARA A LAS AREAS QUE
											// NO EXISTE EVUALIACION DEL COLEGIO

			logger.debug("alumno::::::::::::::::::::::::::");
			logger.debug(alumno);

			Map<Integer, Object> areaAlumno = new HashMap<Integer, Object>();
			Map<Integer, Integer> mapNotasAreas = new HashMap<Integer, Integer>();

			Integer sumaTotal = 0;
			Integer numAreasTotal = 0; 

			// notas por cada area
			List<Row> notasArea = new ArrayList<Row>();
			String sqlNotas = "SELECT FIN.id_area, ROUND(AVG(FIN.promedio))  promedio"
					+"\n FROM"
					+"\n ("
					+"\n 	SELECT ARE.id_area, ARE.id_cur, ROUND(AVG(ARE.promedio))  promedio"
					+"\n 	FROM"
					+"\n 	(	"
					+"\n 		SELECT PER.nump, PER.id_area,PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
					+"\n 		FROM"
					+"\n 		(	"
					+"\n 			SELECT CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
					+"\n 			FROM ("
					/*+"\n 				SELECT  ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
					+"\n 				FROM not_evaluacion ne"
					+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu" 
					+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
					+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
					+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
					+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
					+"\n 				INNER JOIN col_ind_sub cis ON cis.id = nie.id_cis"
					+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
					+"\n 				INNER JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
					+"\n 				INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
					+"\n 				INNER JOIN col_tema tem ON tem.id = sub.id_tem"
					+"\n 				INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
					+"\n 				INNER JOIN col_capacidad cap ON cap.id = ci.id_cap"
					+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
					+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
					+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca "
					+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";*/					
					+"\n 				SELECT  ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
					+"\n 				FROM not_evaluacion ne"
					+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu"
					+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
					+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
					+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
					+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
					+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
					+"\n 				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
					+"\n 				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
					+"\n 				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
					+"\n 				INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
					+"\n 				INNER JOIN col_capacidad cap ON cap.id = cgc.`id_cap`"
					+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
					+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
					+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca" 
					+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
		if(nump==0)
			sqlNotas += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
			
			sqlNotas += "\n 				INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
					+"\n 				WHERE (:nump=0 or ne.nump=:nump) AND "
					+"\n 				per.id_anio=:id_anio"
					+"\n 				AND nie.est='A'"
					//+"\n 				AND caa.`id_area`=14"
					+"\n 				AND mat.ID=:id_mat"
					+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
					+"\n 			) CURSO"
					+"\n 			GROUP BY 	"
					+"\n 			CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id"
					+"\n 		) PER "
					+"\n 		GROUP BY PER.nump, PER.id_area,PER.id_cur"
					+"\n 	)ARE "
					+"\n 	GROUP BY ARE.id_area, ARE.id_cur"
					+"\n ) FIN GROUP BY FIN.id_area";

			param = new Param();
			param.put("id_alu", id_alu);
			//param.put("id_au", id_au);
			param.put("id_anio", id_anio);
			param.put("id_mat", id_mat);
			param.put("nump", nump);
			//logger.info(param);
			////logger.info(sqlNotas);

			notasArea = sqlUtil.query(sqlNotas, param);
			for (Row row : notasArea) {
				if (promedioGeneral!=null)
					mapNotasAreas.put(row.getInteger("id_area"), row.getBigDecimal("promedio").intValue());
				else
					mapNotasAreas.put(row.getInteger("id_area"), null);
			}

			// iteracion por areas
			for (AreaAnio area : areas) {

				logger.debug("::::::::::::::::::::::::::::area::::::::::::::::::::::::::");
				logger.debug(area);

				Integer id_area = area.getArea().getId();

				Integer nota = mapNotasAreas.get(id_area);

				if (nota == null)
					areaAlumno.put(id_area, promedioGeneralInteger);
				else
					areaAlumno.put(id_area, nota);

				alumno.put("areas", areaAlumno);

				if (nota != null) {
					sumaTotal = sumaTotal + nota;
					numAreasTotal++;
				}

			}

			if (numAreasTotal!=0)
				alumno.put("promedio", new BigDecimal(sumaTotal).divide(new BigDecimal(numAreasTotal), 2, RoundingMode.HALF_UP));
			else 
				alumno.put("promedio",null);
			
			if(nump.intValue()==0)
				alumno.put("comportamiento",comportamiento2Letra(comportamientoDAO.promedioAnual(id_alu, id_au)));
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("areas", areas);
		map.put("alumnos", alumnos);
		return map;

	}
	
	public Map<String, Object> listarNotasAreaCompetencias(Integer id_anio, Integer id_gra, Integer id_niv, Integer id_au,
			Integer nump, Integer id_gir, Integer rep_com) throws Exception {

		// alumnos
		List<Row> alumnos = new ArrayList<Row>();	
		 //Obtengo la lista de Areas
	     List<AreaCompetenciaReq> areas_competencia= new ArrayList<AreaCompetenciaReq>();
		try {
			if(id_au!=null) {
				if (id_au != -1) {
					
					Param param = new Param();
					param.put("id_au", id_au);
					
					List<AulaEspecial> aulaEspeciales = aulaEspecialDAO.listByParams(param,null ); 
					
					Aula aulaEspecial= null;
					if (!aulaEspeciales.isEmpty()){
						
						param = new Param();
						param.put("aula.id_grad",aulaEspeciales.get(0).getId_gra());
						param.put("pee.id_anio",id_anio);
						
						aulaEspecial = aulaDAO.listFullByParams(param, null).get(0);
						
					}
						
					String sqlAlumnos = "SELECT mat.id id_mat, mat.id_sit, alu.nro_doc, ";
					if (aulaEspecial!=null)
						sqlAlumnos += " gr_esp.abrv grado, ";
					else
						sqlAlumnos += " gr.abrv grado, ";
					
					if (rep_com.equals(1)) {
						sqlAlumnos +=  " au.secc, au.id id_au, alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,', ',per.nom) alumno, per.nro_doc nro_doc "
								+ " FROM `mat_matricula` "
								+ " mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN `col_persona` per ON alu.`id_per`=per.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad " 
								+ " LEFT JOIN `col_aula` esp ON esp.id = mat.id_au " //cuando existe aula especial
								+ " LEFT JOIN cat_grad gr_esp ON gr_esp.id=esp.id_grad " 
								+ " WHERE   (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL)";
					} else {
						sqlAlumnos +=  " au.secc, au.id id_au, alu.`id`, CONCAT(per.ape_pat,' ',per.ape_mat,' ',per.nom) alumno, per.nro_doc nro_doc "
								+ " FROM `mat_matricula` "
								+ " mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN `col_persona` per ON alu.`id_per`=per.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad " 
								+ " LEFT JOIN `col_aula` esp ON esp.id = mat.id_au " //cuando existe aula especial
								+ " LEFT JOIN cat_grad gr_esp ON gr_esp.id=esp.id_grad " 
								+ " WHERE   (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL) AND gr.id="+id_gra;
					}
					
					
					if(id_au!=null) {
						if (aulaEspecial!=null){
							sqlAlumnos += " AND (mat.id_au_asi=" + aulaEspecial.getId() + ") ";
						}else
							sqlAlumnos += " AND mat.id_au_asi=" + id_au;
					}

						//sqlAlumnos += " AND (mat.id_sit<>5 OR mat.id_sit IS NULL) ";// alumno

																											// garcia
																											// pinachi
					sqlAlumnos += " ORDER BY per.ape_pat, per.ape_mat, per.nom";
					
					alumnos = sqlUtil.query(sqlAlumnos );

				} else {
					if (rep_com.equals(1)) {
						String sqlAlumnos = "SELECT mat.id id_mat, mat.id_sit, alu.nro_doc, "
								+ " gr.abrv grado, au.secc, "
								+ " au.id id_au, alu.`id`, CONCAT(pera.ape_pat,' ',pera.ape_mat,', ',pera.nom) alumno, pera.nro_doc nro_doc "
								+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN col_persona pera ON alu.id_per=pera.id"
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
								+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
								+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id` "
								+ " WHERE per.`id_anio`=? AND au.`id_grad`=?  AND ser.id_gir=? "
								+ " AND (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL) ";
								if(id_au!=null) {
									sqlAlumnos += " AND mat.id_au_asi=" + id_au;
								}
								sqlAlumnos += " ORDER BY au.secc, pera.ape_pat, pera.ape_mat, pera.nom";
						alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra, id_gir });
					} else {
						String sqlAlumnos = "SELECT mat.id id_mat, mat.id_sit, alu.nro_doc, "
								+ " gr.abrv grado, au.secc, "
								+ " au.id id_au, alu.`id`, CONCAT(pera.ape_pat,' ',pera.ape_mat,' ',pera.nom) alumno, pera.nro_doc nro_doc "
								+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN col_persona pera ON alu.id_per=pera.id"
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
								+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
								+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id` "
								+ " WHERE per.`id_anio`=? AND au.`id_grad`=?  AND ser.id_gir=? "
								+ " AND (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL) ";
								if(id_au!=null) {
									sqlAlumnos += " AND mat.id_au_asi=" + id_au;
								}
								sqlAlumnos += " ORDER BY au.secc, pera.ape_pat, pera.ape_mat, pera.nom";
						alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra, id_gir });
					}
					

				}
			} else {

					if (rep_com.equals(1)) {
						String sqlAlumnos = "SELECT mat.id id_mat, mat.id_sit, alu.nro_doc, "
								+ " gr.abrv grado, au.secc, "
								+ " au.id id_au, alu.`id`, CONCAT(pera.ape_pat,' ',pera.ape_mat,', ',pera.nom) alumno, pera.nro_doc nro_doc "
								+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN col_persona pera ON alu.id_per=pera.id"
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
								+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
								+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id` "
								+ " WHERE per.`id_anio`=? AND au.`id_grad`=? AND ser.id_gir=? "
								+ " AND (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL) ";
								if(id_au!=null) {
									sqlAlumnos += " AND mat.id_au_asi=" + id_au;
								}
								sqlAlumnos += " ORDER BY au.secc, pera.ape_pat, pera.ape_mat, pera.nom";
						alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra, id_gir });
					} else {
						String sqlAlumnos = "SELECT mat.id id_mat, mat.id_sit, alu.nro_doc, "
								+ " gr.abrv grado, au.secc, "
								+ " au.id id_au, alu.`id`, CONCAT(pera.ape_pat,' ',pera.ape_mat,' ',pera.nom) alumno, pera.nro_doc nro_doc "
								+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON  mat.`id_alu`=alu.`id` "
								+ " INNER JOIN col_persona pera ON alu.id_per=pera.id"
								+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
								+ " INNER JOIN cat_grad gr ON gr.id=au.id_grad "
								+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id` "
								+ " INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id` "
								+ " WHERE per.`id_anio`=? AND au.`id_grad`=? AND ser.id_gir=? "
								+ " AND (mat.id_sit NOT IN (5,4) OR mat.id_sit IS NULL) ";
								if(id_au!=null) {
									sqlAlumnos += " AND mat.id_au_asi=" + id_au;
								}
						       sqlAlumnos += " ORDER BY au.secc, pera.ape_pat, pera.ape_mat, pera.nom";
						alumnos = sqlUtil.query(sqlAlumnos, new Object[] { id_anio, id_gra, id_gir });
					}

			}


			LinkedHashMap<String, Map<String, Object>> linkalumnos = new LinkedHashMap<String, Map<String, Object>>();
			for (Map<String, Object> map : alumnos) {
				Map<String, Object> detalle = new HashMap<String, Object>();// aca
																			// ira
																			// el
																			// detalle
																			// de
																			// cada
																			// alumno
				detalle.put("alumno", map.get("alumno"));
				detalle.put("nro_doc", map.get("nro_doc"));
				detalle.put("id_alu", map.get("id"));
				detalle.put("nota", null);
				detalle.put("promedio", null);

				linkalumnos.put(map.get("id").toString(), detalle);

			}

			// obtener cursos x seccion
			/*Param param = new Param();
			param.put("caa.id_anio", id_anio);
			param.put("caa.id_niv", id_niv);
			List<AreaAnio> areas = area_anioDAO.listFullByParams(param, new String[] { "caa.ORD" });*/
			 ConfAnioAcadDcn confAnioAcadDcn = confAnioAcadDcnDAO.getByParams(new Param("id_anio",id_anio));
		      //  DetalleNotaReq detalle_nota = new DetalleNotaReq();
		        Param param = new Param();
		        param.put("id_niv", id_niv);
		        param.put("id_dcn", confAnioAcadDcn.getId_dcn());
		        DcnNivel dcnNivel= dcNivelDAO.getByParams(param);
		
		     List<Row> areas= dcnAreaDAO.listarAreasComboAnio(dcnNivel.getId(),id_anio, id_gra, id_gir);
		    // Map<String, Object> areaCompetencias = new HashMap<String, Object>();
		     for (Row row : areas) {
		    	 List<CompetenciaDc> competenciasDc= competenciaDcDAO.listByParams(new Param("id_dcare",row.getInteger("id")),new String[]{"orden asc"});
					List<CompetenciaDCReq> competenciaDCReqs = new ArrayList<CompetenciaDCReq>();
					for (CompetenciaDc competenciaDC : competenciasDc) {
						CompetenciaDCReq competenciaDCReq = new CompetenciaDCReq();
						competenciaDCReq.setId(competenciaDC.getId());
						competenciaDCReq.setNom(competenciaDC.getNom());
						competenciaDCReq.setId_area(competenciaDC.getId_dcare());
						//poner la nota final por competencia
						competenciaDCReqs.add(competenciaDCReq);
					}
					AreaCompetenciaReq area_com= new AreaCompetenciaReq();
					area_com.setId(row.getInteger("id"));
					area_com.setNom_area(row.getString("value"));
					area_com.setCompetencias(competenciaDCReqs);
					areas_competencia.add(area_com);
			}
			

			// iteramos por alumno todos sus cursos

			for (Row alumno : alumnos) {

				Integer id_alu = alumno.getInteger("id");
				Integer id_mat = alumno.getInteger("id_mat");

				// PROMEDIO GENERAL DEL ALUMNO
			//	BigDecimal promedioGeneral = getPromedioGeneral(id_alu, id_au,id_mat,id_anio, nump);

	 
				Integer promedioGeneralInteger = null;
			/*	if (promedioGeneral!=null)
					promedioGeneralInteger = promedioGeneral.intValue();

				if (promedioGeneralInteger!=null && promedioGeneralInteger < 11)
					promedioGeneralInteger = 11;// ESTO REEMPLAZARA A LAS AREAS QUE
												// NO EXISTE EVUALIACION DEL COLEGIO

				logger.debug("alumno::::::::::::::::::::::::::");
				logger.debug(alumno);*/

				Map<Integer, Object> areaAlumno = new HashMap<Integer, Object>();
				Map<Integer, Integer> mapNotasAreas = new HashMap<Integer, Integer>();

				Integer sumaTotal = 0;
				Integer numAreasTotal = 0; 
				
				Matricula matricula = matriculaDAO.get(id_mat);
				
				//AREAS NOTA
				 List<AreaNotaReq> areas_nota=notaDAO.promediosAreasMatriculaxPeriodo(id_mat, matricula.getId_alu(), matricula.getId_gra(), matricula.getId_au_asi(),nump, id_anio, dcnNivel.getId(), id_gir);
				 alumno.put("notas", areas_nota);
				// notas por cada area
				/*List<Row> notasArea = new ArrayList<Row>();
				String sqlNotas = "SELECT FIN.id_area, ROUND(AVG(FIN.promedio))  promedio"
						+"\n FROM"
						+"\n ("
						+"\n 	SELECT ARE.id_area, ARE.id_cur, ROUND(AVG(ARE.promedio))  promedio"
						+"\n 	FROM"
						+"\n 	(	"
						+"\n 		SELECT PER.nump, PER.id_area,PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
						+"\n 		FROM"
						+"\n 		(	"
						+"\n 			SELECT CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id, ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
						+"\n 			FROM ("					
						+"\n 				SELECT  ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
						+"\n 				FROM not_evaluacion ne"
						+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu"
						+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
						+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
						+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
						+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
						+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
						+"\n 				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
						+"\n 				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
						+"\n 				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
						+"\n 				INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
						+"\n 				INNER JOIN col_capacidad cap ON cap.id = cgc.`id_cap`"
						+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
						+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
						+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca" 
						+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
			if(nump==0)
				sqlNotas += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
				
				sqlNotas += "\n 				INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
						+"\n 				WHERE (:nump=0 or ne.nump=:nump) AND "
						+"\n 				per.id_anio=:id_anio"
						+"\n 				AND nie.est='A'"
						//+"\n 				AND caa.`id_area`=14"
						+"\n 				AND mat.ID=:id_mat"
						+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
						+"\n 			) CURSO"
						+"\n 			GROUP BY 	"
						+"\n 			CURSO.nump, CURSO.id_area, CURSO.id_cur, CURSO.com_id"
						+"\n 		) PER "
						+"\n 		GROUP BY PER.nump, PER.id_area,PER.id_cur"
						+"\n 	)ARE "
						+"\n 	GROUP BY ARE.id_area, ARE.id_cur"
						+"\n ) FIN GROUP BY FIN.id_area";

				param = new Param();
				param.put("id_alu", id_alu);
				//param.put("id_au", id_au);
				param.put("id_anio", id_anio);
				param.put("id_mat", id_mat);
				param.put("nump", nump);
				//logger.info(param);
				////logger.info(sqlNotas);

				notasArea = sqlUtil.query(sqlNotas, param);*/
				/*for (Row row : notasArea) {
					if (promedioGeneral!=null)
						mapNotasAreas.put(row.getInteger("id_area"), row.getBigDecimal("promedio").intValue());
					else
						mapNotasAreas.put(row.getInteger("id_area"), null);
				}*/

				// iteracion por areas
				/*for (AreaAnio area : areas) {

					logger.debug("::::::::::::::::::::::::::::area::::::::::::::::::::::::::");
					logger.debug(area);

					Integer id_area = area.getArea().getId();

					Integer nota = mapNotasAreas.get(id_area);

					if (nota == null)
						areaAlumno.put(id_area, promedioGeneralInteger);
					else
						areaAlumno.put(id_area, nota);

					alumno.put("areas", areaAlumno);

					if (nota != null) {
						sumaTotal = sumaTotal + nota;
						numAreasTotal++;
					}

				}

				if (numAreasTotal!=0)
					alumno.put("promedio", new BigDecimal(sumaTotal).divide(new BigDecimal(numAreasTotal), 2, RoundingMode.HALF_UP));
				else 
					alumno.put("promedio",null);
				
				if(nump.intValue()==0)
					alumno.put("comportamiento",comportamiento2Letra(comportamientoDAO.promedioAnual(id_alu, id_au)));*/
				
			}
			
		} catch (Exception e) {
			System.out.print((e.getMessage().toString()));
			// TODO: handle exception
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("areas", areas);
		map.put("areaCompetencias", areas_competencia);
		
		map.put("alumnos", alumnos);
		return map;

	}


	public BigDecimal getPromedioGeneral(Integer id_alu, Integer id_au, Integer id_mat,Integer id_anio, Integer nump) {

		String sqlNotas = "SELECT ROUND(AVG(FINAL.promedio)) promedio from (SELECT  FIN.id_area, ROUND(AVG(FIN.promedio))  promedio"
				+"\n FROM"
				+"\n ("
				+"\n 	SELECT ARE.id_area, ARE.id_cur, ROUND(AVG(ARE.promedio))  promedio"
				+"\n 	FROM"
				+"\n 	(	"
				+"\n 		SELECT PER.nump, PER.id_area,PER.id_cur, ROUND(AVG(PER.nota_curso))  promedio"
				+"\n 		FROM"
				+"\n 		(	"
				+"\n 			SELECT CURSO.nump, CURSO.id_area, CURSO.id_cur,CURSO.com_id,  ROUND(CAST(SUM(CURSO.nota_ind*CURSO.peso) AS DECIMAL(10,2))/CAST(SUM(CURSO.peso) AS DECIMAL(10,2)))  nota_curso"
				+"\n 			FROM ("
				/*+"\n 				SELECT  ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+"\n 				FROM not_evaluacion ne"
				+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu" 
				+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
				+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
				+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
				+"\n 				INNER JOIN col_ind_sub cis ON cis.id = nie.id_cis"
				+"\n 				INNER JOIN col_curso_subtema ccs ON ccs.id = cis.id_sub"
				+"\n 				INNER JOIN col_subtema sub ON sub.id = ccs.id_sub"
				+"\n 				INNER JOIN col_tema tem ON tem.id = sub.id_tem"
				+"\n 				INNER JOIN col_indicador ci ON cis.id_ind=ci.id"
				+"\n 				INNER JOIN col_capacidad cap ON cap.id = ci.id_cap"
				+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
				+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
				+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca "
				+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";*/
				+"\n 				SELECT  ne.nump, caa.id_area, cua.id_cur, com.id com_id, com.peso,cap.id cap_id, nni.nota  nota_ind"
				+"\n 				FROM not_evaluacion ne"
				+"\n 				LEFT JOIN not_nota nn ON nn.id_ne = ne.id AND nn.id_alu=:id_alu"
				+"\n 				INNER JOIN `mat_matricula` mat ON nn.`id_alu`=mat.`id_alu`"
				+"\n 				INNER JOIN `col_aula` au ON mat.`id_au`=au.`id`"
				+"\n 				INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+"\n 				INNER JOIN not_ind_eva nie ON nie.id_ne = ne.id"
				+"\n 				INNER JOIN col_indicador ind ON nie.id_ind=ind.id"
				+"\n 				INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+"\n 				INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+"\n 				INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+"\n 				INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
				+"\n 				INNER JOIN col_capacidad cap ON cap.id = cgc.`id_cap`"
				+"\n 				INNER JOIN col_competencia com ON com.id = cap.id_com"
				+"\n 				INNER JOIN not_nota_indicador nni ON nni.id_nie= nie.id AND nni.id_not= nn.id"
				+"\n 				INNER JOIN col_curso_aula cca ON cca.id = ne.id_cca" 
				+"\n 				INNER JOIN col_curso_anio cua ON cua.id = cca.id_cua AND  cua.id_cur ";
				if(nump==0)
			sqlNotas += "\n				IN (SELECT cua1.id_cur FROM col_curso_anio cua1, col_curso_aula cca1 WHERE cua1.id = cca1.id_cua AND cca1.id_au = mat.id_au_asi )";
		
		sqlNotas += "\n 				INNER JOIN `col_area_anio` caa ON caa.`id`=cua.`id_caa`"
				+"\n 				WHERE (:nump=0 or ne.nump=:nump) AND "
				+"\n 				per.id_anio=:id_anio"
				+"\n 				AND nie.est='A'"
				//+"\n 				AND caa.`id_area`=14"
				+"\n 				AND mat.ID=:id_mat"
				+"\n 				AND mat.id NOT IN (SELECT id_mat FROM `col_situacion_mat` WHERE id_sit=5)"
				+"\n 			) CURSO"
				+"\n 			GROUP BY 	"
				+"\n 			CURSO.nump, CURSO.id_area, CURSO.id_cur,CURSO.com_id"
				+"\n 		) PER "
				+"\n 		GROUP BY PER.nump, PER.id_area,PER.id_cur"
				+"\n 	)ARE "
				+"\n 	GROUP BY ARE.id_area, ARE.id_cur"
				+"\n ) FIN GROUP BY  FIN.id_area "
				+"\n) FINAL ";

		Param param = new Param();
		param.put("id_alu", id_alu);
 		param.put("id_anio", id_anio);
		param.put("id_mat", id_mat);
		param.put("nump", nump);
		//logger.info(param);
		//logger.info(sqlNotas);

		return sqlUtil.queryForObject(sqlNotas, param, BigDecimal.class);

	}


	private String comportamiento2Letra(Integer notaComportamiento){
		if (notaComportamiento>=17)
			return "AD";
		else if (notaComportamiento>=13)
			return  "A";
		else if (notaComportamiento>=11)
			return  "B";
		else
			return  "C";
	}

}
