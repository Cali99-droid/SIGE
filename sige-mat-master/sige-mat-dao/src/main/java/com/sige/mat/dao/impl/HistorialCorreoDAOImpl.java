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
import com.tesla.colegio.model.HistorialCorreo;

import com.tesla.colegio.model.Persona;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HistorialCorreoDAO.
 * @author MV
 *
 */
public class HistorialCorreoDAOImpl{
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
	public int saveOrUpdate(HistorialCorreo historial_correo) {
		if (historial_correo.getId() != null) {
			// update
			String sql = "UPDATE col_historial_correo "
						+ "SET id_per=?, "
						+ "corr_antiguo=?, "
						+ "corr_nuevo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						historial_correo.getId_per(),
						historial_correo.getCorr_antiguo(),
						historial_correo.getCorr_nuevo(),
						historial_correo.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						historial_correo.getId()); 
			return historial_correo.getId();

		} else {
			// insert
			String sql = "insert into col_historial_correo ("
						+ "id_per, "
						+ "corr_antiguo, "
						+ "corr_nuevo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				historial_correo.getId_per(),
				historial_correo.getCorr_antiguo(),
				historial_correo.getCorr_nuevo(),
				historial_correo.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_historial_correo where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<HistorialCorreo> list() {
		String sql = "select * from col_historial_correo";
		
		System.out.println(sql);
		
		List<HistorialCorreo> listHistorialCorreo = jdbcTemplate.query(sql, new RowMapper<HistorialCorreo>() {

			@Override
			public HistorialCorreo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHistorialCorreo;
	}

	public HistorialCorreo get(int id) {
		String sql = "select * from col_historial_correo WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialCorreo>() {

			@Override
			public HistorialCorreo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public HistorialCorreo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select hcorr.id hcorr_id, hcorr.id_per hcorr_id_per , hcorr.corr_antiguo hcorr_corr_antiguo , hcorr.corr_nuevo hcorr_corr_nuevo  ,hcorr.est hcorr_est ";
		if (aTablas.contains("col_persona"))
			sql = sql + ", per.id per_id  , per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ";
	
		sql = sql + " from col_historial_correo hcorr "; 
		if (aTablas.contains("col_persona"))
			sql = sql + " left join col_persona per on per.id = hcorr.id_per ";
		sql = sql + " where hcorr.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialCorreo>() {
		
			@Override
			public HistorialCorreo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					HistorialCorreo historialcorreo= rsToEntity(rs,"hcorr_");
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
							historialcorreo.setPersona(persona);
					}
							return historialcorreo;
				}
				
				return null;
			}
			
		});


	}		
	
	public HistorialCorreo getByParams(Param param) {

		String sql = "select * from col_historial_correo " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HistorialCorreo>() {
			@Override
			public HistorialCorreo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<HistorialCorreo> listByParams(Param param, String[] order) {

		String sql = "select * from col_historial_correo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HistorialCorreo>() {

			@Override
			public HistorialCorreo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<HistorialCorreo> listFullByParams(HistorialCorreo historialcorreo, String[] order) {
	
		return listFullByParams(Param.toParam("hcorr",historialcorreo), order);
	
	}	
	
	public List<HistorialCorreo> listFullByParams(Param param, String[] order) {

		String sql = "select hcorr.id hcorr_id, hcorr.id_per hcorr_id_per , hcorr.corr_antiguo hcorr_corr_antiguo , hcorr.corr_nuevo hcorr_corr_nuevo  ,hcorr.est hcorr_est ";
		sql = sql + ", per.id per_id  , per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ";
		sql = sql + " from col_historial_correo hcorr";
		sql = sql + " left join col_persona per on per.id = hcorr.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HistorialCorreo>() {

			@Override
			public HistorialCorreo mapRow(ResultSet rs, int rowNum) throws SQLException {
				HistorialCorreo historialcorreo= rsToEntity(rs,"hcorr_");
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
				historialcorreo.setPersona(persona);
				return historialcorreo;
			}

		});

	}	




	// funciones privadas utilitarias para HistorialCorreo

	private HistorialCorreo rsToEntity(ResultSet rs,String alias) throws SQLException {
		HistorialCorreo historial_correo = new HistorialCorreo();

		historial_correo.setId(rs.getInt( alias + "id"));
		historial_correo.setId_per(rs.getInt( alias + "id_per"));
		historial_correo.setCorr_antiguo(rs.getString( alias + "corr_antiguo"));
		historial_correo.setCorr_nuevo(rs.getString( alias + "corr_nuevo"));
		historial_correo.setEst(rs.getString( alias + "est"));
								
		return historial_correo;

	}
	
}
