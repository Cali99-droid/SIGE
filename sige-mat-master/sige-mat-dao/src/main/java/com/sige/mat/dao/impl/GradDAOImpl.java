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
import com.tesla.colegio.model.Grad;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Vacante;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.CapacidadSetup;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.Indicador;
import com.tesla.colegio.model.CursoSubtema;
import com.tesla.colegio.model.CursoUnidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GradDAO.
 * @author MV
 *
 */
public class GradDAOImpl{
	final static Logger logger = Logger.getLogger(GradDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Grad grad) {
		if (grad.getId() != null) {
			// update
			String sql = "UPDATE cat_grad "
						+ "SET id_nvl=?, "
						+ "id_gra_ant=?, "
						+ "nom=?, "
						+ "tipo=?, "
						+ "abrv=?, "
						+ "abrv_classroom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						grad.getId_nvl(),
						grad.getId_gra_ant(),
						grad.getNom(),
						grad.getTipo(),
						grad.getAbrv(),
						grad.getAbrv_classroom(),
						grad.getEst(),
						grad.getUsr_act(),
						new java.util.Date(),
						grad.getId()); 
			return grad.getId();

		} else {
			// insert
			String sql = "insert into cat_grad ("
						+ "id_nvl, "
						+ "id_gra_ant, "
						+ "nom, "
						+ "tipo, "
						+ "abrv, "
						+ "abrv_classroom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				grad.getId_nvl(),
				grad.getId_gra_ant(),
				grad.getNom(),
				grad.getTipo(),
				grad.getAbrv(),
				grad.getAbrv_classroom(),
				grad.getEst(),
				grad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_grad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Grad> list() {
		String sql = "select * from cat_grad";
		
		//logger.info(sql);
		
		List<Grad> listGrad = jdbcTemplate.query(sql, new RowMapper<Grad>() {

			@Override
			public Grad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGrad;
	}

	public Grad get(int id) {
		String sql = "select * from cat_grad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Grad>() {

			@Override
			public Grad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Grad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gra.id gra_id, gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ,gra.est gra_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
	
		sql = sql + " from cat_grad gra "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = gra.id_nvl ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = gra.id_gra_ant ";
		sql = sql + " where gra.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Grad>() {
		
			@Override
			public Grad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Grad grad= rsToEntity(rs,"gra_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							grad.setNivel(nivel);
					}
					if (aTablas.contains("cat_grad")){
						Grad gradAnt = new Grad();  
						gradAnt.setId(rs.getInt("gra_id")) ;  
						gradAnt.setId_nvl(rs.getInt("gra_id_nvl")) ;  
						gradAnt.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
						gradAnt.setNom(rs.getString("gra_nom")) ;  
						gradAnt.setTipo(rs.getString("gra_tipo")) ;  
							grad.setGrad(gradAnt);
					}
							return grad;
				}
				
				return null;
			}
			
		});


	}		
	
	public Grad getByParams(Param param) {

		String sql = "select * from cat_grad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Grad>() {
			@Override
			public Grad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Grad> listByParams(Param param, String[] order) {

		String sql = "select * from cat_grad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Grad>() {

			@Override
			public Grad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Grad> listFullByParams(Grad grad, String[] order) {
	
		return listFullByParams(Param.toParam("gra",grad), order);
	
	}	
	
	public List<Grad> listFullByParams(Param param, String[] order) {

		String sql = "select gra.id gra_id, gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ,gra.est gra_est, gra.abrv gra_abrv, gra.abrv_classroom gra_abrv_classroom ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", gra_ant.id gra_ant_id  , gra_ant.id_nvl gra_ant_id_nvl , gra_ant.id_gra_ant gra_ant_id_gra_ant , gra_ant.nom gra_ant_nom , gra_ant.tipo gra_ant_tipo  ";
		sql = sql + " from cat_grad gra";
		sql = sql + " left join cat_nivel niv on niv.id = gra.id_nvl ";
		sql = sql + " left join cat_grad gra_ant on gra_ant.id = gra.id_gra_ant ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Grad>() {

			@Override
			public Grad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Grad grad= rsToEntity(rs,"gra_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				grad.setNivel(nivel);
				Grad gradAnt = new Grad();  
				gradAnt.setId(rs.getInt("gra_ant_id")) ;  
				gradAnt.setId_nvl(rs.getInt("gra_ant_id_nvl")) ;  
				gradAnt.setId_gra_ant(rs.getInt("gra_ant_id_gra_ant")) ;  
				gradAnt.setNom(rs.getString("gra_ant_nom")) ;  
				gradAnt.setTipo(rs.getString("gra_ant_tipo")) ;  
				grad.setGrad(gradAnt);
				return grad;
			}

		});

	}	


	public List<Grad> getListGrad(Param param, String[] order) {
		String sql = "select * from cat_grad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Grad>() {

			@Override
			public Grad mapRow(ResultSet rs, int rowNum) throws SQLException {
				Grad grad = new Grad();

				grad.setId(rs.getInt("id"));
				grad.setId_nvl(rs.getInt("id_nvl"));
				grad.setId_gra_ant(rs.getInt("id_gra_ant"));
				grad.setNom(rs.getString("nom"));
				grad.setTipo(rs.getString("tipo"));
				grad.setEst(rs.getString("est"));
												
				return grad;
			}

		});	
	}
	
	public List<Vacante> getListVacante(Param param, String[] order) {
		String sql = "select * from eva_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Vacante>() {

			@Override
			public Vacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				Vacante vacante = new Vacante();

				vacante.setId(rs.getInt("id"));
				vacante.setId_per(rs.getInt("id_per"));
				vacante.setId_eva(rs.getInt("id_eva"));
				vacante.setId_grad(rs.getInt("id_grad"));
				vacante.setNro_vac(rs.getInt("nro_vac"));
				vacante.setVac_ofe(rs.getInt("vac_ofe"));
				vacante.setPost(rs.getInt("post"));
				vacante.setEst(rs.getString("est"));
												
				return vacante;
			}

		});	
	}
	public List<MatrVacante> getListMatrVacante(Param param, String[] order) {
		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			@Override
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacante matr_vacante = new MatrVacante();

				matr_vacante.setId(rs.getInt("id"));
				matr_vacante.setId_alu(rs.getInt("id_alu"));
				matr_vacante.setId_eva(rs.getInt("id_eva"));
				matr_vacante.setId_gra(rs.getInt("id_gra"));
				matr_vacante.setId_col(rs.getInt("id_col"));
				matr_vacante.setId_cli(rs.getInt("id_cli"));
				matr_vacante.setNum_rec(rs.getString("num_rec"));
				matr_vacante.setNum_cont(rs.getString("num_cont"));
				matr_vacante.setRes(rs.getString("res"));
				matr_vacante.setEst(rs.getString("est"));
												
				return matr_vacante;
			}

		});	
	}
	public List<CapacidadSetup> getListCapacidadSetup(Param param, String[] order) {
		String sql = "select * from col_capacidad_setup " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CapacidadSetup>() {

			@Override
			public CapacidadSetup mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapacidadSetup capacidad_setup = new CapacidadSetup();

				capacidad_setup.setId(rs.getInt("id"));
				capacidad_setup.setId_per(rs.getInt("id_per"));
				capacidad_setup.setId_grad(rs.getInt("id_grad"));
				capacidad_setup.setCant(rs.getInt("cant"));
				capacidad_setup.setEst(rs.getString("est"));
												
				return capacidad_setup;
			}

		});	
	}
	public List<Reserva> getListReserva(Param param, String[] order) {
		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			@Override
			public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				Reserva reserva = new Reserva();

				reserva.setId(rs.getInt("id"));
				reserva.setId_alu(rs.getInt("id_alu"));
				reserva.setId_au(rs.getInt("id_au"));
				reserva.setId_gra(rs.getInt("id_gra"));
				reserva.setId_niv(rs.getInt("id_niv"));
				reserva.setId_con(rs.getInt("id_con"));
				reserva.setId_cli(rs.getInt("id_cli"));
				reserva.setId_per(rs.getInt("id_per"));
				reserva.setId_fam(rs.getInt("id_fam"));
				reserva.setFec(rs.getDate("fec"));
				reserva.setFec_lim(rs.getDate("fec_lim"));
				reserva.setEst(rs.getString("est"));
												
				return reserva;
			}

		});	
	}
	public List<Matricula> getListMatricula(Param param, String[] order) {
		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			@Override
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();

				matricula.setId(rs.getInt("id"));
				matricula.setId_alu(rs.getInt("id_alu"));
				matricula.setId_fam(rs.getInt("id_fam"));
				matricula.setId_enc(rs.getInt("id_enc"));
				matricula.setId_con(rs.getInt("id_con"));
				matricula.setId_cli(rs.getInt("id_cli"));
				matricula.setId_per(rs.getInt("id_per"));
				matricula.setId_au(rs.getInt("id_au"));
				matricula.setId_gra(rs.getInt("id_gra"));
				matricula.setId_niv(rs.getInt("id_niv"));
				matricula.setFecha(rs.getDate("fecha"));
				matricula.setCar_pod(rs.getString("car_pod"));
				matricula.setNum_cont(rs.getString("num_cont"));
				matricula.setObs(rs.getString("obs"));
				matricula.setEst(rs.getString("est"));
												
				return matricula;
			}

		});	
	}
	public List<Aula> getListAula(Param param, String[] order) {
		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			@Override
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Aula aula = new Aula();

				aula.setId(rs.getInt("id"));
				aula.setId_per(rs.getInt("id_per"));
				aula.setId_grad(rs.getInt("id_grad"));
				aula.setId_secc_ant(rs.getInt("id_secc_ant"));
				aula.setId_tur(rs.getInt("id_tur"));
				aula.setSecc(rs.getString("secc"));
				aula.setCap(rs.getInt("cap"));
				aula.setEst(rs.getString("est"));
												
				return aula;
			}

		});	
	}
	public List<CursoAnio> getListCursoAnio(Param param, String[] order) {
		String sql = "select * from col_curso_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAnio curso_anio = new CursoAnio();

				curso_anio.setId(rs.getInt("id"));
				curso_anio.setId_per(rs.getInt("id_per"));
				curso_anio.setId_gra(rs.getInt("id_gra"));
				curso_anio.setId_caa(rs.getInt("id_caa"));
				curso_anio.setId_cur(rs.getInt("id_cur"));
				curso_anio.setPeso(rs.getInt("peso"));
				curso_anio.setOrden(rs.getInt("orden"));
				curso_anio.setFlg_prom(rs.getString("flg_prom"));
				curso_anio.setEst(rs.getString("est"));
												
				return curso_anio;
			}

		});	
	}
	public List<Indicador> getListIndicador(Param param, String[] order) {
		String sql = "select * from col_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Indicador indicador = new Indicador();

				indicador.setId(rs.getInt("id"));
				indicador.setNom(rs.getString("nom"));
				indicador.setEst(rs.getString("est"));
												
				return indicador;
			}

		});	
	}
	public List<CursoSubtema> getListCursoSubtema(Param param, String[] order) {
		String sql = "select * from col_curso_subtema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoSubtema>() {

			@Override
			public CursoSubtema mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoSubtema curso_subtema = new CursoSubtema();

				curso_subtema.setId(rs.getInt("id"));
				curso_subtema.setId_anio(rs.getInt("id_anio"));
				curso_subtema.setId_niv(rs.getInt("id_niv"));
				curso_subtema.setId_gra(rs.getInt("id_gra"));
				curso_subtema.setId_cur(rs.getInt("id_cur"));
				curso_subtema.setId_sub(rs.getInt("id_sub"));
				curso_subtema.setDur(rs.getBigDecimal("dur"));
				curso_subtema.setEst(rs.getString("est"));
												
				return curso_subtema;
			}

		});	
	}
	public List<CursoUnidad> getListCursoUnidad(Param param, String[] order) {
		String sql = "select * from col_curso_unidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoUnidad curso_unidad = new CursoUnidad();

				curso_unidad.setId(rs.getInt("id"));
				curso_unidad.setId_niv(rs.getInt("id_niv"));
				curso_unidad.setId_gra(rs.getInt("id_gra"));
				curso_unidad.setId_cur(rs.getInt("id_cur"));
				curso_unidad.setId_cpu(rs.getInt("id_cpu"));
				curso_unidad.setNum(rs.getInt("num"));
				curso_unidad.setNom(rs.getString("nom"));
				curso_unidad.setDes(rs.getString("des"));
				//curso_unidad.setNro_sem(rs.getInt("nro_sem"));
				curso_unidad.setProducto(rs.getString("producto"));
				curso_unidad.setEst(rs.getString("est"));
												
				return curso_unidad;
			}

		});	
	}


	// funciones privadas utilitarias para Grad

	private Grad rsToEntity(ResultSet rs,String alias) throws SQLException {
		Grad grad = new Grad();

		grad.setId(rs.getInt( alias + "id"));
		grad.setId_nvl(rs.getInt( alias + "id_nvl"));
		grad.setId_gra_ant(rs.getInt( alias + "id_gra_ant"));
		grad.setNom(rs.getString( alias + "nom"));
		grad.setTipo(rs.getString( alias + "tipo"));
		grad.setAbrv(rs.getString( alias + "abrv"));
		grad.setAbrv_classroom(rs.getString(alias + "abrv_classroom"));
		grad.setEst(rs.getString( alias + "est"));
								
		return grad;

	}
	
}
