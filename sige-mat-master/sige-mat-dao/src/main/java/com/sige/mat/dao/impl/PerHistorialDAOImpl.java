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
import com.tesla.colegio.model.PerHistorial;

import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.EstCivil;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerHistorialDAO.
 * @author MV
 *
 */
public class PerHistorialDAOImpl{
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
	public int saveOrUpdate(PerHistorial per_historial) {
		if (per_historial.getId() != null) {
			// update
			String sql = "UPDATE col_per_historial "
						+ "SET id_per=?, "
						+ "id_anio=?, "
						+ "id_eci=?, "
						+ "corr_actual=?, "
						+ "corr_antiguo=?, "
						+ "cel_actual=?, "
						+ "cel_antiguo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						per_historial.getId_per(),
						per_historial.getId_anio(),
						per_historial.getId_eci(),
						per_historial.getCorr_actual(),
						per_historial.getCorr_antiguo(),
						per_historial.getCel_actual(),
						per_historial.getCel_antiguo(),
						per_historial.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						per_historial.getId()); 
			return per_historial.getId();

		} else {
			// insert
			String sql = "insert into col_per_historial ("
						+ "id_per, "
						+ "id_anio, "
						+ "id_eci, "
						+ "corr_actual, "
						+ "corr_antiguo, "
						+ "cel_actual, "
						+ "cel_antiguo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				per_historial.getId_per(),
				per_historial.getId_anio(),
				per_historial.getId_eci(),
				per_historial.getCorr_actual(),
				per_historial.getCorr_antiguo(),
				per_historial.getCel_actual(),
				per_historial.getCel_antiguo(),
				per_historial.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_per_historial where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PerHistorial> list() {
		String sql = "select * from col_per_historial";
		
		System.out.println(sql);
		
		List<PerHistorial> listPerHistorial = jdbcTemplate.query(sql, new RowMapper<PerHistorial>() {

			@Override
			public PerHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerHistorial;
	}

	public PerHistorial get(int id) {
		String sql = "select * from col_per_historial WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerHistorial>() {

			@Override
			public PerHistorial extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PerHistorial getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select per_hist.id per_hist_id, per_hist.id_per per_hist_id_per , per_hist.id_anio per_hist_id_anio , per_hist.id_eci per_hist_id_eci , per_hist.corr_actual per_hist_corr_actual , per_hist.corr_antiguo per_hist_corr_antiguo , per_hist.cel_actual per_hist_cel_actual , per_hist.cel_antiguo per_hist_cel_antiguo  ,per_hist.est per_hist_est ";
		if (aTablas.contains("col_persona"))
			sql = sql + ", per.id per_id  , per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
	
		sql = sql + " from col_per_historial per_hist "; 
		if (aTablas.contains("col_persona"))
			sql = sql + " left join col_persona per on per.id = per_hist.id_per ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = per_hist.id_anio ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + " left join cat_est_civil eci on eci.id = per_hist.id_eci ";
		sql = sql + " where per_hist.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PerHistorial>() {
		
			@Override
			public PerHistorial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PerHistorial perhistorial= rsToEntity(rs,"per_hist_");
					if (aTablas.contains("col_persona")){
						Persona persona = new Persona();  
							persona.setId(rs.getInt("per_id")) ;  
							//persona.setId_tdc(rs.getInt("per_id_tdc")) ;  
							persona.setId_tap(rs.getString("per_id_tap")) ;  
							persona.setId_gen(rs.getString("per_id_gen")) ;  
							persona.setId_eci(rs.getInt("per_id_eci")) ;  
							persona.setId_rel(rs.getInt("per_id_rel")) ;  
							persona.setId_dist_viv(rs.getInt("per_id_dist_viv")) ;  
							persona.setNro_doc(rs.getString("per_nro_doc")) ;  
							persona.setFec_emi(rs.getDate("per_fec_emi")) ;  
							persona.setUbigeo(rs.getString("per_ubigeo")) ;  
							persona.setNom(rs.getString("per_nom")) ;  
							persona.setApe_pat(rs.getString("per_ape_pat")) ;  
							persona.setApe_mat(rs.getString("per_ape_mat")) ;  
							persona.setFoto(rs.getString("per_foto")) ;  
							persona.setHue(rs.getString("per_hue")) ;  
							persona.setFec_nac(rs.getDate("per_fec_nac")) ;  
							persona.setFec_def(rs.getDate("per_fec_def")) ;  
							persona.setId_pais_nac(rs.getInt("per_id_pais_nac")) ;  
							persona.setId_dist_nac(rs.getInt("per_id_dist_nac")) ;  
							persona.setId_nac(rs.getInt("per_id_nac")) ;  
							persona.setTlf(rs.getString("per_tlf")) ;  
							persona.setCorr(rs.getString("per_corr")) ;  
							persona.setCel(rs.getString("per_cel")) ;  
							persona.setViv(rs.getString("per_viv")) ;  
							persona.setDir(rs.getString("per_dir")) ;  
							persona.setTrab(rs.getString("per_trab")) ;  
							persona.setId_cond(rs.getInt("per_id_cond")) ;  
							perhistorial.setPersona(persona);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							perhistorial.setAnio(anio);
					}
					if (aTablas.contains("cat_est_civil")){
						EstCivil estcivil = new EstCivil();  
							estcivil.setId(rs.getInt("eci_id")) ;  
							estcivil.setNom(rs.getString("eci_nom")) ;  
							perhistorial.setEstCivil(estcivil);
					}
							return perhistorial;
				}
				
				return null;
			}
			
		});


	}		
	
	public PerHistorial getByParams(Param param) {

		String sql = "select * from col_per_historial " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerHistorial>() {
			@Override
			public PerHistorial extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PerHistorial> listByParams(Param param, String[] order) {

		String sql = "select * from col_per_historial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerHistorial>() {

			@Override
			public PerHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PerHistorial> listFullByParams(PerHistorial perhistorial, String[] order) {
	
		return listFullByParams(Param.toParam("per_hist",perhistorial), order);
	
	}	
	
	public List<PerHistorial> listFullByParams(Param param, String[] order) {

		String sql = "select per_hist.id per_hist_id, per_hist.id_per per_hist_id_per , per_hist.id_anio per_hist_id_anio , per_hist.id_eci per_hist_id_eci , per_hist.corr_actual per_hist_corr_actual , per_hist.corr_antiguo per_hist_corr_antiguo , per_hist.cel_actual per_hist_cel_actual , per_hist.cel_antiguo per_hist_cel_antiguo  ,per_hist.est per_hist_est ";
		sql = sql + ", per.id per_id  , per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		sql = sql + " from col_per_historial per_hist";
		sql = sql + " left join col_persona per on per.id = per_hist.id_per ";
		sql = sql + " left join col_anio anio on anio.id = per_hist.id_anio ";
		sql = sql + " left join cat_est_civil eci on eci.id = per_hist.id_eci ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerHistorial>() {

			@Override
			public PerHistorial mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerHistorial perhistorial= rsToEntity(rs,"per_hist_");
				Persona persona = new Persona();  
				persona.setId(rs.getInt("per_id")) ;  
				//persona.setId_tdc(rs.getInt("per_id_tdc")) ;  
				persona.setId_tap(rs.getString("per_id_tap")) ;  
				persona.setId_gen(rs.getString("per_id_gen")) ;  
				persona.setId_eci(rs.getInt("per_id_eci")) ;  
				persona.setId_rel(rs.getInt("per_id_rel")) ;  
				persona.setId_dist_viv(rs.getInt("per_id_dist_viv")) ;  
				persona.setNro_doc(rs.getString("per_nro_doc")) ;  
				persona.setFec_emi(rs.getDate("per_fec_emi")) ;  
				persona.setUbigeo(rs.getString("per_ubigeo")) ;  
				persona.setNom(rs.getString("per_nom")) ;  
				persona.setApe_pat(rs.getString("per_ape_pat")) ;  
				persona.setApe_mat(rs.getString("per_ape_mat")) ;  
				persona.setFoto(rs.getString("per_foto")) ;  
				persona.setHue(rs.getString("per_hue")) ;  
				persona.setFec_nac(rs.getDate("per_fec_nac")) ;  
				persona.setFec_def(rs.getDate("per_fec_def")) ;  
				persona.setId_pais_nac(rs.getInt("per_id_pais_nac")) ;  
				persona.setId_dist_nac(rs.getInt("per_id_dist_nac")) ;  
				persona.setId_nac(rs.getInt("per_id_nac")) ;  
				persona.setTlf(rs.getString("per_tlf")) ;  
				persona.setCorr(rs.getString("per_corr")) ;  
				persona.setCel(rs.getString("per_cel")) ;  
				persona.setViv(rs.getString("per_viv")) ;  
				persona.setDir(rs.getString("per_dir")) ;  
				persona.setTrab(rs.getString("per_trab")) ;  
				persona.setId_cond(rs.getInt("per_id_cond")) ;  
				perhistorial.setPersona(persona);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				perhistorial.setAnio(anio);
				EstCivil estcivil = new EstCivil();  
				estcivil.setId(rs.getInt("eci_id")) ;  
				estcivil.setNom(rs.getString("eci_nom")) ;  
				perhistorial.setEstCivil(estcivil);
				return perhistorial;
			}

		});

	}	




	// funciones privadas utilitarias para PerHistorial

	private PerHistorial rsToEntity(ResultSet rs,String alias) throws SQLException {
		PerHistorial per_historial = new PerHistorial();

		per_historial.setId(rs.getInt( alias + "id"));
		per_historial.setId_per(rs.getInt( alias + "id_per"));
		per_historial.setId_anio(rs.getInt( alias + "id_anio"));
		per_historial.setId_eci(rs.getInt( alias + "id_eci"));
		per_historial.setCorr_actual(rs.getString( alias + "corr_actual"));
		per_historial.setCorr_antiguo(rs.getString( alias + "corr_antiguo"));
		per_historial.setCel_actual(rs.getString( alias + "cel_actual"));
		per_historial.setCel_antiguo(rs.getString( alias + "cel_antiguo"));
		per_historial.setEst(rs.getString( alias + "est"));
								
		return per_historial;

	}
	
}
