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
import com.tesla.colegio.model.SemPago;

import com.tesla.colegio.model.SemInscripcion;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SemPagoDAO.
 * @author MV
 *
 */
public class SemPagoDAOImpl{
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
	public int saveOrUpdate(SemPago sem_pago) {
		if (sem_pago.getId() != null) {
			// update
			String sql = "UPDATE col_sem_pago "
						+ "SET id_semins=?, "
						+ "monto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						sem_pago.getId_semins(),
						sem_pago.getMonto(),
						sem_pago.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						sem_pago.getId()); 
			return sem_pago.getId();

		} else {
			// insert
			String sql = "insert into col_sem_pago ("
						+ "id_semins, "
						+ "monto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				sem_pago.getId_semins(),
				sem_pago.getMonto(),
				sem_pago.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sem_pago where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SemPago> list() {
		String sql = "select * from col_sem_pago";
		
		System.out.println(sql);
		
		List<SemPago> listSemPago = jdbcTemplate.query(sql, new RowMapper<SemPago>() {

			@Override
			public SemPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSemPago;
	}

	public SemPago get(int id) {
		String sql = "select * from col_sem_pago WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemPago>() {

			@Override
			public SemPago extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SemPago getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sempag.id sempag_id, sempag.id_semins sempag_id_semins , sempag.monto sempag_monto  ,sempag.est sempag_est ";
		if (aTablas.contains("col_sem_inscripcion"))
			sql = sql + ", semins.id semins_id  , semins.id_sem semins_id_sem , semins.id_gru semins_id_gru , semins.id_dist semins_id_dist , semins.nro_dni semins_nro_dni , semins.ape_pat semins_ape_pat , semins.ape_mat semins_ape_mat , semins.nom semins_nom , semins.corr semins_corr , semins.col semins_col , semins.flag_pago semins_flag_pago  ";
	
		sql = sql + " from col_sem_pago sempag "; 
		if (aTablas.contains("col_sem_inscripcion"))
			sql = sql + " left join col_sem_inscripcion semins on semins.id = sempag.id_semins ";
		sql = sql + " where sempag.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SemPago>() {
		
			@Override
			public SemPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SemPago sempago= rsToEntity(rs,"sempag_");
					if (aTablas.contains("col_sem_inscripcion")){
						SemInscripcion seminscripcion = new SemInscripcion();  
							seminscripcion.setId(rs.getInt("semins_id")) ;  
							seminscripcion.setId_sem(rs.getInt("semins_id_sem")) ;  
							seminscripcion.setId_gru(rs.getInt("semins_id_gru")) ;  
							seminscripcion.setId_dist(rs.getInt("semins_id_dist")) ;  
							seminscripcion.setNro_dni(rs.getInt("semins_nro_dni")) ;  
							seminscripcion.setApe_pat(rs.getString("semins_ape_pat")) ;  
							seminscripcion.setApe_mat(rs.getString("semins_ape_mat")) ;  
							seminscripcion.setNom(rs.getString("semins_nom")) ;  
							seminscripcion.setCorr(rs.getString("semins_corr")) ;  
							seminscripcion.setCol(rs.getString("semins_col")) ;  
							seminscripcion.setFlag_pago(rs.getString("semins_flag_pago")) ;  
							sempago.setSemInscripcion(seminscripcion);
					}
							return sempago;
				}
				
				return null;
			}
			
		});


	}		
	
	public SemPago getByParams(Param param) {

		String sql = "select * from col_sem_pago " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemPago>() {
			@Override
			public SemPago extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SemPago> listByParams(Param param, String[] order) {

		String sql = "select * from col_sem_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemPago>() {

			@Override
			public SemPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SemPago> listFullByParams(SemPago sempago, String[] order) {
	
		return listFullByParams(Param.toParam("sempag",sempago), order);
	
	}	
	
	public List<SemPago> listFullByParams(Param param, String[] order) {

		String sql = "select sempag.id sempag_id, sempag.id_semins sempag_id_semins , sempag.monto sempag_monto  ,sempag.est sempag_est ";
		sql = sql + ", semins.id semins_id  , semins.id_sem semins_id_sem , semins.id_gru semins_id_gru , semins.id_dist semins_id_dist , semins.nro_dni semins_nro_dni , semins.ape_pat semins_ape_pat , semins.ape_mat semins_ape_mat , semins.nom semins_nom , semins.corr semins_corr , semins.col semins_col , semins.flag_pago semins_flag_pago  ";
		sql = sql + " from col_sem_pago sempag";
		sql = sql + " left join col_sem_inscripcion semins on semins.id = sempag.id_semins ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemPago>() {

			@Override
			public SemPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemPago sempago= rsToEntity(rs,"sempag_");
				SemInscripcion seminscripcion = new SemInscripcion();  
				seminscripcion.setId(rs.getInt("semins_id")) ;  
				seminscripcion.setId_sem(rs.getInt("semins_id_sem")) ;  
				seminscripcion.setId_gru(rs.getInt("semins_id_gru")) ;  
				seminscripcion.setId_dist(rs.getInt("semins_id_dist")) ;  
				seminscripcion.setNro_dni(rs.getInt("semins_nro_dni")) ;  
				seminscripcion.setApe_pat(rs.getString("semins_ape_pat")) ;  
				seminscripcion.setApe_mat(rs.getString("semins_ape_mat")) ;  
				seminscripcion.setNom(rs.getString("semins_nom")) ;  
				seminscripcion.setCorr(rs.getString("semins_corr")) ;  
				seminscripcion.setCol(rs.getString("semins_col")) ;  
				seminscripcion.setFlag_pago(rs.getString("semins_flag_pago")) ;  
				sempago.setSemInscripcion(seminscripcion);
				return sempago;
			}

		});

	}	




	// funciones privadas utilitarias para SemPago

	private SemPago rsToEntity(ResultSet rs,String alias) throws SQLException {
		SemPago sem_pago = new SemPago();

		sem_pago.setId(rs.getInt( alias + "id"));
		sem_pago.setId_semins(rs.getInt( alias + "id_semins"));
		sem_pago.setMonto(rs.getBigDecimal( alias + "monto"));
		sem_pago.setEst(rs.getString( alias + "est"));
								
		return sem_pago;

	}
	
}
