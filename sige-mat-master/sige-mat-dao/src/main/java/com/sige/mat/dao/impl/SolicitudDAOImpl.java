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
import com.tesla.colegio.model.Solicitud;

import com.tesla.colegio.model.Matricula;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Sucursal;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SolicitudDAO.
 * @author MV
 *
 */
public class SolicitudDAOImpl{
	final static Logger logger = Logger.getLogger(SolicitudDAOImpl.class);
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
	public int saveOrUpdate(Solicitud solicitud) {
		if (solicitud.getId() != null) {
			// update
			String sql = "UPDATE mat_solicitud "
						+ "SET id_alu=?, id_anio=?, "
						+ "tipo=?, "
						+ "id_fam=?, "
						+ "id_fam=?, "
						+ "id_suc_or=?, "
						+ "id_suc_des=?, "
						+ "nro_exp=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						solicitud.getId_alu(),
						solicitud.getId_anio(),
						solicitud.getTipo(),
						solicitud.getId_fam(),
						solicitud.getId_suc_or(),
						solicitud.getId_suc_des(),
						solicitud.getNro_exp(),
						solicitud.getMotivo(),
						solicitud.getEst(),
						solicitud.getUsr_act(),
						new java.util.Date(),
						tokenSeguridad.getId()); 
			return solicitud.getId();

		} else {
			// insert
			String sql = "insert into mat_solicitud ("
						+ "id_alu, "
						+ "id_anio, "
						+ "tipo, "
						+ "id_fam, "
						+ "id_suc_or, "
						+ "id_suc_des, "
						+ "nro_exp, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
						solicitud.getId_alu(),
						solicitud.getId_anio(),
						solicitud.getTipo(),
						solicitud.getId_fam(),
						solicitud.getId_suc_or(),
						solicitud.getId_suc_des(),
						solicitud.getNro_exp(),
						solicitud.getMotivo(),
						solicitud.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_solicitud where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Solicitud> list() {
		String sql = "select * from mat_solicitud";
		
		//logger.info(sql);
		
		List<Solicitud> listSolicitud = jdbcTemplate.query(sql, new RowMapper<Solicitud>() {

			@Override
			public Solicitud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSolicitud;
	}

	public Solicitud get(int id) {
		String sql = "select * from mat_solicitud WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Solicitud>() {

			@Override
			public Solicitud extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Solicitud getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mat_sol.id mat_sol_id, mat_sol.id_mat mat_sol_id_mat , mat_sol.tipo mat_sol_tipo , mat_sol.id_fam mat_sol_id_fam , mat_sol.id_suc_or mat_sol_id_suc_or , mat_sol.id_suc_des mat_sol_id_suc_des , mat_sol.nro_exp mat_sol_nro_exp , mat_sol.motivo mat_sol_motivo  ,mat_sol.est mat_sol_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from mat_solicitud mat_sol "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = mat_sol.id_mat ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = mat_sol.id_fam ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = mat_sol.id_suc_or ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = mat_sol.id_suc_des ";
		sql = sql + " where mat_sol.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Solicitud>() {
		
			@Override
			public Solicitud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Solicitud solicitud= rsToEntity(rs,"mat_sol_");
					if (aTablas.contains("mat_matricula")){
						Matricula matricula = new Matricula();  
							matricula.setId(rs.getInt("mat_id")) ;  
							matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
							matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
							matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
							matricula.setId_con(rs.getInt("mat_id_con")) ;  
							matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
							matricula.setId_per(rs.getInt("mat_id_per")) ;  
							matricula.setId_au(rs.getInt("mat_id_au")) ;  
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							solicitud.setMatricula(matricula);
					}
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							solicitud.setFamiliar(familiar);
					}
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							solicitud.setSucursal(sucursal);
					}
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							solicitud.setSucursal(sucursal);
					}
							return solicitud;
				}
				
				return null;
			}
			
		});


	}		
	
	public Solicitud getByParams(Param param) {

		String sql = "select * from mat_solicitud " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Solicitud>() {
			@Override
			public Solicitud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Solicitud> listByParams(Param param, String[] order) {

		String sql = "select * from mat_solicitud " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Solicitud>() {

			@Override
			public Solicitud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Solicitud> listFullByParams(Solicitud solicitud, String[] order) {
	
		return listFullByParams(Param.toParam("mat_sol",solicitud), order);
	
	}	
	
	public List<Solicitud> listFullByParams(Param param, String[] order) {

		String sql = "select mat_sol.id mat_sol_id, mat_sol.id_mat mat_sol_id_mat , mat_sol.tipo mat_sol_tipo , mat_sol.id_fam mat_sol_id_fam , mat_sol.id_suc_or mat_sol_id_suc_or , mat_sol.id_suc_des mat_sol_id_suc_des , mat_sol.nro_exp mat_sol_nro_exp , mat_sol.motivo mat_sol_motivo  ,mat_sol.est mat_sol_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + " from mat_solicitud mat_sol";
		sql = sql + " left join mat_matricula mat on mat.id = mat_sol.id_mat ";
		sql = sql + " left join alu_familiar fam on fam.id = mat_sol.id_fam ";
		sql = sql + " left join ges_sucursal suc on suc.id = mat_sol.id_suc_or ";
		sql = sql + " left join ges_sucursal suc on suc.id = mat_sol.id_suc_des ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Solicitud>() {

			@Override
			public Solicitud mapRow(ResultSet rs, int rowNum) throws SQLException {
				Solicitud solicitud= rsToEntity(rs,"mat_sol_");
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				solicitud.setMatricula(matricula);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				solicitud.setFamiliar(familiar);
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				solicitud.setSucursal(sucursal);
				
				return solicitud;
			}

		});

	}	




	// funciones privadas utilitarias para Solicitud

	private Solicitud rsToEntity(ResultSet rs,String alias) throws SQLException {
		Solicitud solicitud = new Solicitud();

		solicitud.setId(rs.getInt( alias + "id"));
		solicitud.setId_mat(rs.getInt( alias + "id_mat"));
		solicitud.setTipo(rs.getString( alias + "tipo"));
		solicitud.setId_fam(rs.getInt( alias + "id_fam"));
		solicitud.setId_suc_or(rs.getInt( alias + "id_suc_or"));
		solicitud.setId_suc_des(rs.getInt( alias + "id_suc_des"));
		solicitud.setNro_exp(rs.getString( alias + "nro_exp"));
		solicitud.setMotivo(rs.getString( alias + "motivo"));
		solicitud.setEst(rs.getString( alias + "est"));
								
		return solicitud;

	}
	
}
