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
import com.tesla.colegio.model.AlumnoDescuento;

import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AlumnoDescuentoDAO.
 * @author MV
 *
 */
public class AlumnoDescuentoDAOImpl{
	
	final static Logger logger = Logger.getLogger(AlumnoDescuentoDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AlumnoDescuento alumno_descuento) {
		if (alumno_descuento.getId() != null) {
			// update
			String sql = "UPDATE fac_alumno_descuento "
						+ "SET id_mat=?, "
						+ "id_fdes=?, "
						+ "descuento=?, "
						+ "mensualidad=?, "
						+ "mensualidad_bco=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						alumno_descuento.getId_mat(),
						alumno_descuento.getId_fdes(),
						alumno_descuento.getDescuento(),
						alumno_descuento.getMensualidad(),
						alumno_descuento.getMensualidad_bco(),
						alumno_descuento.getMotivo(),
						alumno_descuento.getEst(),
						alumno_descuento.getUsr_act(),
						new java.util.Date(),
						alumno_descuento.getId()); 
			return alumno_descuento.getId();

		} else {
			// insert
			String sql = "insert into fac_alumno_descuento ("
						+ "id_mat, "
						+ "id_fdes, "
						+ "descuento, "
						+ "mensualidad, "
						+ "mensualidad_bco, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				alumno_descuento.getId_mat(),
				alumno_descuento.getId_fdes(),
				alumno_descuento.getDescuento(),
				alumno_descuento.getMensualidad(),
				alumno_descuento.getMensualidad_bco(),
				alumno_descuento.getMotivo(),
				alumno_descuento.getEst(),
				alumno_descuento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_alumno_descuento where id_mat=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AlumnoDescuento> list() {
		String sql = "select * from fac_alumno_descuento";
		
		//logger.info(sql);
		
		List<AlumnoDescuento> listAlumnoDescuento = jdbcTemplate.query(sql, new RowMapper<AlumnoDescuento>() {

			@Override
			public AlumnoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAlumnoDescuento;
	}

	public AlumnoDescuento get(int id) {
		String sql = "select * from fac_alumno_descuento WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AlumnoDescuento>() {

			@Override
			public AlumnoDescuento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AlumnoDescuento getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fda.id fda_id, fda.id_mat fda_id_mat , fda.descuento fda_descuento  ,fda.est fda_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
	
		sql = sql + " from fac_alumno_descuento fda "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = fda.id_mat ";
		sql = sql + " where fda.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AlumnoDescuento>() {
		
			@Override
			public AlumnoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AlumnoDescuento alumnodescuento= rsToEntity(rs,"fda_");
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
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							alumnodescuento.setMatricula(matricula);
					}
							return alumnodescuento;
				}
				
				return null;
			}
			
		});


	}		
	
	public AlumnoDescuento getByParams(Param param) {

		String sql = "select * from fac_alumno_descuento " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AlumnoDescuento>() {
			@Override
			public AlumnoDescuento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AlumnoDescuento> listByParams(Param param, String[] order) {

		String sql = "select * from fac_alumno_descuento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AlumnoDescuento>() {

			@Override
			public AlumnoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AlumnoDescuento> listFullByParams(AlumnoDescuento alumnodescuento, String[] order) {
	
		return listFullByParams(Param.toParam("fda",alumnodescuento), order);
	
	}	
	
	public List<AlumnoDescuento> listFullByParams(Param param, String[] order) {

		String sql = "select fda.id fda_id, fda.id_mat fda_id_mat ,fda.id_fdes fda_id_fdes,  fda.descuento fda_descuento  ,fda.est fda_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from fac_alumno_descuento fda";
		sql = sql + " left join mat_matricula mat on mat.id = fda.id_mat ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AlumnoDescuento>() {

			@Override
			public AlumnoDescuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				AlumnoDescuento alumnodescuento= rsToEntity(rs,"fda_");
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
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				alumnodescuento.setMatricula(matricula);
				return alumnodescuento;
			}

		});

	}	




	// funciones privadas utilitarias para AlumnoDescuento

	private AlumnoDescuento rsToEntity(ResultSet rs,String alias) throws SQLException {
		AlumnoDescuento alumno_descuento = new AlumnoDescuento();

		alumno_descuento.setId(rs.getInt( alias + "id"));
		alumno_descuento.setId_mat(rs.getInt( alias + "id_mat"));
		alumno_descuento.setId_fdes(rs.getInt( alias + "id_fdes"));
		alumno_descuento.setMensualidad(rs.getBigDecimal( alias + "mensualidad"));
		alumno_descuento.setMensualidad_bco(rs.getBigDecimal( alias + "mensualidad_bco"));
		alumno_descuento.setDescuento(rs.getBigDecimal( alias + "descuento"));
		alumno_descuento.setEst(rs.getString( alias + "est"));
								
		return alumno_descuento;

	}
	
}
