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

import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.EvaluacionVac;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.EvaluacionVacExamen;
import com.tesla.colegio.model.MatrVacante;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaluacionVacDAO.
 * @author MV
 *
 */
public class EvaluacionVacDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionVacDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EvaluacionVac evaluacion_vac) {
		if (evaluacion_vac.getId() != null) {
			// update
			String sql = "UPDATE eva_evaluacion_vac "
						+ "SET id_per=?, "
						+ "des=?, "
						+ "precio=?, "
						+ "ptje_apro=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "fec_vig_vac=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			//return 
			jdbcTemplate.update(sql, 
						evaluacion_vac.getId_per(),
						evaluacion_vac.getDes(),
						evaluacion_vac.getPrecio(),
						evaluacion_vac.getPtje_apro(),
						evaluacion_vac.getFec_ini(),
						evaluacion_vac.getFec_fin(),
						evaluacion_vac.getFec_vig_vac(),
						evaluacion_vac.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						evaluacion_vac.getId());
			
			return evaluacion_vac.getId();

		} else {
			// insert
			String sql = "insert into eva_evaluacion_vac ("
						+ "id_per, "
						+ "des, "
						+ "precio, "
						+ "ptje_apro, fec_ini, fec_fin, fec_vig_vac, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?,?,?,?,?,?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				evaluacion_vac.getId_per(),
				evaluacion_vac.getDes(),
				evaluacion_vac.getPrecio(),
				evaluacion_vac.getPtje_apro(),
				evaluacion_vac.getFec_ini(),
				evaluacion_vac.getFec_fin(),
				evaluacion_vac.getFec_vig_vac(),
				evaluacion_vac.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_evaluacion_vac where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<EvaluacionVac> list() {
		String sql = "select * from eva_evaluacion_vac";
		
		//logger.info(sql);
		
		List<EvaluacionVac> listEvaluacionVac = jdbcTemplate.query(sql, new RowMapper<EvaluacionVac>() {

			
			public EvaluacionVac mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaluacionVac;
	}

	
	public EvaluacionVac get(int id) {
		String sql = "select * from eva_evaluacion_vac WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVac>() {

			
			public EvaluacionVac extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public EvaluacionVac getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eva_vac.id eva_vac_id, eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio  ,eva_vac.est eva_vac_est, eva_vac.fec_ini eva_vac_fec_ini,eva_vac.fec_fin eva_vac_fec_fin ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from eva_evaluacion_vac eva_vac "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = eva_vac.id_per ";
		sql = sql + " where eva_vac.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVac>() {
		
			
			public EvaluacionVac extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EvaluacionVac evaluacionvac= rsToEntity(rs,"eva_vac_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							evaluacionvac.setPeriodo(periodo);
					}
							return evaluacionvac;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public EvaluacionVac getByParams(Param param) {

		String sql = "select * from eva_evaluacion_vac " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaluacionVac>() {
			
			public EvaluacionVac extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<EvaluacionVac> listByParams(Param param, String[] order) {

		String sql = "select * from eva_evaluacion_vac " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVac>() {

			
			public EvaluacionVac mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<EvaluacionVac> listFullByParams(EvaluacionVac evaluacionvac, String[] order) {
	
		return listFullByParams(Param.toParam("eva_vac",evaluacionvac), order);
	
	}	
	
	
	public List<EvaluacionVac> listFullByParams(Param param, String[] order) {

		String sql = "select eva_vac.id eva_vac_id, eva_vac.id_per eva_vac_id_per , eva_vac.des eva_vac_des , eva_vac.precio eva_vac_precio,eva_vac.ptje_apro eva_vac_ptje_apro  ,eva_vac.est eva_vac_est, eva_vac.fec_ini eva_vac_fec_ini, eva_vac.fec_fin eva_vac_fec_fin , eva_vac.fec_vig_vac eva_vac_fec_vig_vac";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		//nuevo
		sql = sql + ", ani.nom ani_nom";
		sql = sql + ", srv.nom srv_nom";
		sql = sql + ", suc.nom suc_nom";
		sql = sql + " from eva_evaluacion_vac eva_vac";
		sql = sql + " left join per_periodo pee on pee.id = eva_vac.id_per ";
		//nuevo 
		sql = sql + " left join col_anio ani on pee.id_anio = ani.id ";
		sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVac>() {

			
			public EvaluacionVac mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaluacionVac evaluacionvac= rsToEntity(rs,"eva_vac_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;
				//nuevo
				Anio anio = new Anio();
				anio.setNom(rs.getString("ani_nom"));
				periodo.setAnio(anio);
				//servicio
				Servicio servicio = new Servicio();
				//local
				Sucursal suc = new Sucursal();
				suc.setNom(rs.getString("suc_nom"));
				servicio.setSucursal(suc);
				servicio.setNom(rs.getString("srv_nom"));
				periodo.setServicio(servicio);


				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				evaluacionvac.setPeriodo(periodo);
				return evaluacionvac;
			}

		});

	}	


	public List<EvaluacionVacExamen> getListEvaluacionVacExamen(Param param, String[] order) {
		String sql = "select * from eva_evaluacion_vac_examen " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVacExamen>() {

			
			public EvaluacionVacExamen mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaluacionVacExamen evaluacion_vac_examen = new EvaluacionVacExamen();

				evaluacion_vac_examen.setId(rs.getInt("id"));
				evaluacion_vac_examen.setId_eva(rs.getInt("id_eva"));
				evaluacion_vac_examen.setId_eae(rs.getInt("id_eae"));
				evaluacion_vac_examen.setId_tae(rs.getInt("id_tae"));
				evaluacion_vac_examen.setFec_exa(rs.getDate("fec_exa"));
				evaluacion_vac_examen.setEst(rs.getString("est"));
												
				return evaluacion_vac_examen;
			}

		});	
	}
	public List<MatrVacante> getListMatrVacante(Param param, String[] order) {
		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacante matr_vacante = new MatrVacante();

				matr_vacante.setId(rs.getInt("id"));
				matr_vacante.setId_alu(rs.getInt("id_alu"));
				matr_vacante.setId_eva(rs.getInt("id_eva"));
				matr_vacante.setId_gra(rs.getInt("id_gra"));
				matr_vacante.setId_col(rs.getInt("id_col"));
				matr_vacante.setNum_rec(rs.getString("num_rec"));
				matr_vacante.setNum_cont(rs.getString("num_cont"));
				matr_vacante.setEst(rs.getString("est"));
												
				return matr_vacante;
			}

		});	
	}


	// funciones privadas utilitarias para EvaluacionVac

	private EvaluacionVac rsToEntity(ResultSet rs,String alias) throws SQLException {
		EvaluacionVac evaluacion_vac = new EvaluacionVac();

		evaluacion_vac.setId(rs.getInt( alias + "id"));
		evaluacion_vac.setId_per(rs.getInt( alias + "id_per"));
		evaluacion_vac.setDes(rs.getString( alias + "des"));
		evaluacion_vac.setPrecio(rs.getBigDecimal( alias + "precio"));
		evaluacion_vac.setPtje_apro(rs.getBigDecimal( alias + "ptje_apro"));
		evaluacion_vac.setFec_ini(rs.getDate( alias + "fec_ini"));
		evaluacion_vac.setFec_fin(rs.getDate( alias + "fec_fin"));
		evaluacion_vac.setFec_vig_vac(rs.getDate(alias + "fec_vig_vac"));
		evaluacion_vac.setEst(rs.getString( alias + "est"));
								
		return evaluacion_vac;

	}
	
}
