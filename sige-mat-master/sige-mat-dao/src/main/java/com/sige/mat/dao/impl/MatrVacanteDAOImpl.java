package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.MatrVacante;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Colegio;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.CriterioNota;
import com.tesla.colegio.model.MatrVacanteResultado;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.MarcacionNota;

import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MatrVacanteDAO.
 * @author MV
 *
 */
public class MatrVacanteDAOImpl{
	final static Logger logger = Logger.getLogger(MatrVacanteDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MatrVacante matr_vacante) {
		if (matr_vacante.getId() != null) {
			// update
			String sql = "UPDATE eva_matr_vacante "
						+ "SET id_alu=?, "
						+ "id_eva=?, "
						+ "id_gra=?, "
						+ "id_col=?, "
						+ "id_cli=?, "
						+ "num_rec=?, "
						+ "num_cont=?, "
						+ "res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						matr_vacante.getId_alu(),
						matr_vacante.getId_eva(),
						matr_vacante.getId_gra(),
						matr_vacante.getId_col(),
						matr_vacante.getId_cli(),
						matr_vacante.getNum_rec(),
						matr_vacante.getNum_cont(),
						matr_vacante.getRes(),
						matr_vacante.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						matr_vacante.getId()); 
						return matr_vacante.getId();

		} else {
			// insert
			String sql = "insert into eva_matr_vacante ("
						+ "id_alu, "
						+ "id_eva, "
						+ "id_gra, "
						+ "id_col, "
						+ "id_cli, "
						+ "num_rec, "
						+ "num_cont, "
						+ "res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				matr_vacante.getId_alu(),
				matr_vacante.getId_eva(),
				matr_vacante.getId_gra(),
				matr_vacante.getId_col(),
				matr_vacante.getId_cli(),
				matr_vacante.getNum_rec(),
				matr_vacante.getNum_cont(),
				matr_vacante.getRes(),
				matr_vacante.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eva_matr_vacante where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MatrVacante> list() {
		String sql = "select * from eva_matr_vacante";
		
		//logger.info(sql);
		
		List<MatrVacante> listMatrVacante = jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			@Override
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMatrVacante;
	}

	public MatrVacante get(int id) {
		String sql = "select * from eva_matr_vacante WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacante>() {

			@Override
			public MatrVacante extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MatrVacante getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select matr_vac.id matr_vac_id, matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont , matr_vac.res matr_vac_res  ,matr_vac.est matr_vac_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + ", eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio , eva_vac.ptje_apro eva_vac_ptje_apro , eva_vac.fec_ini eva_vac_fec_ini , eva_vac.fec_fin eva_vac_fec_fin  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		if (aTablas.contains("col_colegio"))
			sql = sql + ", col.id col_id  , col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel  ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
	
		sql = sql + " from eva_matr_vacante matr_vac "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = matr_vac.id_alu ";
		if (aTablas.contains("eva_evaluacion_vac"))
			sql = sql + " left join eva_evaluacion_vac eva_vac on eva_vac.id = matr_vac.id_eva ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad grad on grad.id = matr_vac.id_gra ";
		if (aTablas.contains("col_colegio"))
			sql = sql + " left join col_colegio col on col.id = matr_vac.id_col ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = matr_vac.id_cli ";
		sql = sql + " where matr_vac.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacante>() {
		
			@Override
			public MatrVacante extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MatrVacante matrvacante= rsToEntity(rs,"matr_vac_");
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
							alumno.setId_tap(rs.getString("alu_id_tap")) ;  
							alumno.setId_gen(rs.getString("alu_id_gen")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
							alumno.setNom(rs.getString("alu_nom")) ;  
							alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
							alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
							alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							matrvacante.setAlumno(alumno);
					}
					if (aTablas.contains("eva_evaluacion_vac")){
						EvaluacionVac evaluacionvac = new EvaluacionVac();  
							evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
							evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
							evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
							evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
							evaluacionvac.setPtje_apro(rs.getBigDecimal("eva_vac_ptje_apro")) ;  
							evaluacionvac.setFec_ini(rs.getDate("eva_vac_fec_ini")) ;  
							evaluacionvac.setFec_fin(rs.getDate("eva_vac_fec_fin")) ;  
							matrvacante.setEvaluacionVac(evaluacionvac);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
							grad.setNom(rs.getString("grad_nom")) ;  
							matrvacante.setGrad(grad);
					}
					if (aTablas.contains("col_colegio")){
						Colegio colegio = new Colegio();  
							colegio.setId(rs.getInt("col_id")) ;  
							colegio.setId_dist(rs.getInt("col_id_dist")) ;  
							colegio.setCod_mod(rs.getString("col_cod_mod")) ;  
							colegio.setNom_niv(rs.getString("col_nom_niv")) ;  
							colegio.setNom(rs.getString("col_nom")) ;  
							colegio.setEstatal(rs.getString("col_estatal")) ;  
							colegio.setDir(rs.getString("col_dir")) ;  
							colegio.setTel(rs.getString("col_tel")) ;  
							matrvacante.setColegio(colegio);
					}
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							matrvacante.setFamiliar(familiar);
					}
							return matrvacante;
				}
				
				return null;
			}
			
		});


	}		
	
	public MatrVacante getByParams(Param param) {

		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatrVacante>() {
			@Override
			public MatrVacante extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MatrVacante> listByParams(Param param, String[] order) {

		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			@Override
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MatrVacante> listFullByParams(MatrVacante matrvacante, String[] order) {
	
		return listFullByParams(Param.toParam("matr_vac",matrvacante), order);
	
	}	
	
	public List<MatrVacante> listFullByParams(Param param, String[] order) {

		String sql = "select matr_vac.id matr_vac_id, matr_vac.id_alu matr_vac_id_alu , matr_vac.id_eva matr_vac_id_eva , matr_vac.id_gra matr_vac_id_gra , matr_vac.id_col matr_vac_id_col , matr_vac.id_cli matr_vac_id_cli , matr_vac.num_rec matr_vac_num_rec , matr_vac.num_cont matr_vac_num_cont , matr_vac.res matr_vac_res  ,matr_vac.est matr_vac_est ";
		sql = sql + ",\n alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ",\n eva_vac.id eva_vac_id  , eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio , eva_vac.ptje_apro eva_vac_ptje_apro , eva_vac.fec_ini eva_vac_fec_ini , eva_vac.fec_fin eva_vac_fec_fin  ";
		sql = sql + ",\n grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + ",\n col.id col_id  , col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel  ";
		sql = sql + ",\n fam.id fam_id  , fam.nom fam_nom  ";
		//sql = sql + ", niv.id niv_id   ";
		sql = sql + "\n from eva_matr_vacante matr_vac";
		sql = sql + "\n left join alu_alumno alu on alu.id = matr_vac.id_alu ";
		sql = sql + "\n left join eva_evaluacion_vac eva_vac on eva_vac.id = matr_vac.id_eva ";
		sql = sql + "\n left join per_periodo per on eva_vac.id_per=per.id ";
		sql = sql + "\n left join cat_grad grad on grad.id = matr_vac.id_gra ";
		//sql = sql + " left join cat_niv niv on niv.id = grad.id_nvl ";
		sql = sql + "\n left join col_colegio col on col.id = matr_vac.id_col ";
		sql = sql + "\n left join alu_familiar fam on fam.id = matr_vac.id_cli ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			@Override
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacante matrvacante= rsToEntity(rs,"matr_vac_");
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				matrvacante.setAlumno(alumno);
				EvaluacionVac evaluacionvac = new EvaluacionVac();  
				evaluacionvac.setId(rs.getInt("eva_vac_id")) ;  
				evaluacionvac.setId_per(rs.getInt("eva_vac_id_per")) ;  
				evaluacionvac.setDes(rs.getString("eva_vac_des")) ;  
				evaluacionvac.setPrecio(rs.getBigDecimal("eva_vac_precio")) ;  
				evaluacionvac.setPtje_apro(rs.getBigDecimal("eva_vac_ptje_apro")) ;  
				evaluacionvac.setFec_ini(rs.getDate("eva_vac_fec_ini")) ;  
				evaluacionvac.setFec_fin(rs.getDate("eva_vac_fec_fin")) ;  
				matrvacante.setEvaluacionVac(evaluacionvac);
				//Nivel nivel = new Nivel();
				//nivel.setId(rs.getInt("niv_id"));
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				//grad.setNivel(nivel);
				matrvacante.setGrad(grad);
				Colegio colegio = new Colegio();  
				colegio.setId(rs.getInt("col_id")) ;  
				colegio.setId_dist(rs.getInt("col_id_dist")) ;  
				colegio.setCod_mod(rs.getString("col_cod_mod")) ;  
				colegio.setNom_niv(rs.getString("col_nom_niv")) ;  
				colegio.setNom(rs.getString("col_nom")) ;  
				colegio.setEstatal(rs.getString("col_estatal")) ;  
				colegio.setDir(rs.getString("col_dir")) ;  
				colegio.setTel(rs.getString("col_tel")) ;  
				matrvacante.setColegio(colegio);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				matrvacante.setFamiliar(familiar);
				return matrvacante;
			}

		});

	}	


	public List<CriterioNota> getListCriterioNota(Param param, String[] order) {
		String sql = "select * from eva_criterio_nota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CriterioNota>() {

			@Override
			public CriterioNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				CriterioNota criterio_nota = new CriterioNota();

				criterio_nota.setId(rs.getInt("id"));
				criterio_nota.setId_ex_cri(rs.getInt("id_ex_cri"));
				criterio_nota.setId_mat_vac(rs.getInt("id_mat_vac"));
				criterio_nota.setNum(rs.getInt("num"));
				criterio_nota.setPuntaje(rs.getInt("puntaje"));
				criterio_nota.setResultado(rs.getString("resultado"));
				criterio_nota.setApto(rs.getString("apto"));
				criterio_nota.setEst(rs.getString("est"));
												
				return criterio_nota;
			}

		});	
	}
	public List<MatrVacanteResultado> getListMatrVacanteResultado(Param param, String[] order) {
		String sql = "select * from eva_matr_vacante_resultado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatrVacanteResultado>() {

			@Override
			public MatrVacanteResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacanteResultado matr_vacante_resultado = new MatrVacanteResultado();

				matr_vacante_resultado.setId(rs.getInt("id"));
				matr_vacante_resultado.setId_mat_vac(rs.getInt("id_mat_vac"));
				matr_vacante_resultado.setNotafinal(rs.getBigDecimal("notafinal"));
				matr_vacante_resultado.setRes(rs.getString("res"));
				matr_vacante_resultado.setEst(rs.getString("est"));
												
				return matr_vacante_resultado;
			}

		});	
	}
	public List<MarcacionNota> getListMarcacionNota(Param param, String[] order) {
		String sql = "select * from eva_marcacion_nota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MarcacionNota>() {

			@Override
			public MarcacionNota mapRow(ResultSet rs, int rowNum) throws SQLException {
				MarcacionNota marcacion_nota = new MarcacionNota();

				marcacion_nota.setId(rs.getInt("id"));
				marcacion_nota.setId_mat_vac(rs.getInt("id_mat_vac"));
				marcacion_nota.setId_exa_mar(rs.getInt("id_exa_mar"));
				marcacion_nota.setPreg_favor(rs.getInt("preg_favor"));
				marcacion_nota.setPreg_contra(rs.getInt("preg_contra"));
				marcacion_nota.setPtje(rs.getBigDecimal("ptje"));
				marcacion_nota.setEst(rs.getString("est"));
												
				return marcacion_nota;
			}

		});	
	}
	

	// funciones privadas utilitarias para MatrVacante

	private MatrVacante rsToEntity(ResultSet rs,String alias) throws SQLException {
		MatrVacante matr_vacante = new MatrVacante();

		matr_vacante.setId(rs.getInt( alias + "id"));
		matr_vacante.setId_alu(rs.getInt( alias + "id_alu"));
		matr_vacante.setId_eva(rs.getInt( alias + "id_eva"));
		matr_vacante.setId_gra(rs.getInt( alias + "id_gra"));
		matr_vacante.setId_col(rs.getInt( alias + "id_col"));
		matr_vacante.setId_cli(rs.getInt( alias + "id_cli"));
		matr_vacante.setNum_rec(rs.getString( alias + "num_rec"));
		matr_vacante.setNum_cont(rs.getString( alias + "num_cont"));
		matr_vacante.setRes(rs.getString( alias + "res"));
		matr_vacante.setEst(rs.getString( alias + "est"));
								
		return matr_vacante;

	}
	
}
