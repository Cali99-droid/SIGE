package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.HistorialEco;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.CentralRiesgo;
import com.tesla.colegio.model.CentralRiesgo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HistorialEcoDAO.
 * @author MV
 *
 */
public class HistorialEcoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(HistorialEco historial_eco) {
		if (historial_eco.getId() != null) {
			// update
			String sql = "UPDATE col_historial_eco "
						+ "SET id_mat=?, "
						+ "id_ccr_pad=?, "
						+ "id_ccr_mad=?, "
						+ "ult_men=?, "
						+ "nro_mens=?, "
						+ "ing_fam=?, "
						+ "puntaje=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						historial_eco.getId_mat(),
						historial_eco.getId_ccr_pad(),
						historial_eco.getId_ccr_mad(),
						historial_eco.getUlt_men(),
						historial_eco.getNro_mens(),
						historial_eco.getIng_fam(),
						historial_eco.getPuntaje(),
						historial_eco.getEst(),
						historial_eco.getUsr_act(),
						new java.util.Date(),
						historial_eco.getId()); 
			return historial_eco.getId();

		} else {
			// insert
			String sql = "insert into col_historial_eco ("
						+ "id_mat, "
						+ "id_ccr_pad, "
						+ "id_ccr_mad, "
						+ "ult_men, "
						+ "nro_mens, "
						+ "ing_fam, "
						+ "puntaje, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				historial_eco.getId_mat(),
				historial_eco.getId_ccr_pad(),
				historial_eco.getId_ccr_mad(),
				historial_eco.getUlt_men(),
				historial_eco.getNro_mens(),
				historial_eco.getIng_fam(),
				historial_eco.getPuntaje(),
				historial_eco.getEst(),
				historial_eco.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_historial_eco where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<HistorialEco> list() {
		String sql = "select * from col_historial_eco";
		
		
		
		List<HistorialEco> listHistorialEco = jdbcTemplate.query(sql, new RowMapper<HistorialEco>() {

			@Override
			public HistorialEco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHistorialEco;
	}

	public HistorialEco get(int id) {
		String sql = "select * from col_historial_eco WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialEco>() {

			@Override
			public HistorialEco extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public HistorialEco getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csh.id csh_id, csh.id_mat csh_id_mat , csh.id_ccr_pad csh_id_ccr_pad , csh.id_ccr_mad csh_id_ccr_mad , csh.ult_men csh_ult_men , csh.nro_mens csh_nro_mens , csh.ing_fam csh_ing_fam , csh.puntaje csh_puntaje  ,csh.est csh_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("cat_central_riesgo"))
			sql = sql + ", ccr.id ccr_id  , ccr.nom ccr_nom  ";
		if (aTablas.contains("cat_central_riesgo"))
			sql = sql + ", ccr.id ccr_id  , ccr.nom ccr_nom  ";
	
		sql = sql + " from col_historial_eco csh "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = csh.id_mat ";
		if (aTablas.contains("cat_central_riesgo"))
			sql = sql + " left join cat_central_riesgo ccr on ccr.id = csh.id_ccr_pad ";
		if (aTablas.contains("cat_central_riesgo"))
			sql = sql + " left join cat_central_riesgo ccr on ccr.id = csh.id_ccr_mad ";
		sql = sql + " where csh.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialEco>() {
		
			@Override
			public HistorialEco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					HistorialEco historialeco= rsToEntity(rs,"csh_");
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
							historialeco.setMatricula(matricula);
					}
					if (aTablas.contains("cat_central_riesgo")){
						CentralRiesgo centralriesgo = new CentralRiesgo();  
							centralriesgo.setId(rs.getInt("ccr_id")) ;  
							centralriesgo.setNom(rs.getString("ccr_nom")) ;  
							historialeco.setCentralRiesgo(centralriesgo);
					}
					if (aTablas.contains("cat_central_riesgo")){
						CentralRiesgo centralriesgo = new CentralRiesgo();  
							centralriesgo.setId(rs.getInt("ccr_id")) ;  
							centralriesgo.setNom(rs.getString("ccr_nom")) ;  
							historialeco.setCentralRiesgo(centralriesgo);
					}
							return historialeco;
				}
				
				return null;
			}
			
		});


	}		
	
	public HistorialEco getByParams(Param param) {

		String sql = "select * from col_historial_eco " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialEco>() {
			@Override
			public HistorialEco extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<HistorialEco> listByParams(Param param, String[] order) {

		String sql = "select * from col_historial_eco " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<HistorialEco>() {

			@Override
			public HistorialEco mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<HistorialEco> listFullByParams(HistorialEco historialeco, String[] order) {
	
		return listFullByParams(Param.toParam("csh",historialeco), order);
	
	}	
	
	public List<HistorialEco> listFullByParams(Param param, String[] order) {

		String sql = "select csh.id csh_id, csh.id_mat csh_id_mat , csh.id_ccr_pad csh_id_ccr_pad , csh.id_ccr_mad csh_id_ccr_mad , csh.ult_men csh_ult_men , csh.nro_mens csh_nro_mens , csh.ing_fam csh_ing_fam , csh.puntaje csh_puntaje  ,csh.est csh_est, csh.fec_ins csh_fec_ins ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", ccr_pad.id ccr_id_pad  , ccr_pad.nom ccr_nom_pad  ";
		sql = sql + ", ccr_mad.id ccr_id_mad  , ccr_mad.nom ccr_nom_mad  ";
		sql = sql + " from col_historial_eco csh";
		sql = sql + " left join mat_matricula mat on mat.id = csh.id_mat ";
		sql = sql + " left join cat_central_riesgo ccr_pad on ccr_pad.id = csh.id_ccr_pad ";
		sql = sql + " left join cat_central_riesgo ccr_mad on ccr_mad.id = csh.id_ccr_mad ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<HistorialEco>() {

			@Override
			public HistorialEco mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistorialEco historialeco= rsToEntity(rs,"csh_");
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
				historialeco.setMatricula(matricula);
				CentralRiesgo centralriesgo = new CentralRiesgo();  
				centralriesgo.setId_pad(rs.getInt("ccr_id_pad")) ;  
				centralriesgo.setNom_pad(rs.getString("ccr_nom_pad")) ;  
				centralriesgo.setId_mad(rs.getInt("ccr_id_mad")) ;  
				centralriesgo.setNom_mad(rs.getString("ccr_nom_mad")) ;  
				historialeco.setCentralRiesgo(centralriesgo);
				return historialeco;
			}

		});

	}	




	// funciones privadas utilitarias para HistorialEco

	private HistorialEco rsToEntity(ResultSet rs,String alias) throws SQLException {
		HistorialEco historial_eco = new HistorialEco();

		historial_eco.setId(rs.getInt( alias + "id"));
		historial_eco.setId_mat(rs.getInt( alias + "id_mat"));
		historial_eco.setId_ccr_pad(rs.getInt( alias + "id_ccr_pad"));
		historial_eco.setId_ccr_mad(rs.getInt( alias + "id_ccr_mad"));
		historial_eco.setUlt_men(rs.getString( alias + "ult_men"));
		historial_eco.setNro_mens(rs.getInt( alias + "nro_mens"));
		historial_eco.setIng_fam(rs.getBigDecimal( alias + "ing_fam"));
		historial_eco.setPuntaje(rs.getBigDecimal( alias + "puntaje"));
		historial_eco.setEst(rs.getString( alias + "est"));
		historial_eco.setFec_ins(rs.getDate(alias + "fec_ins"));						
		return historial_eco;

	}
	
}
