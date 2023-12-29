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
import com.tesla.colegio.model.Persona;

import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.EstCivil;
import com.tesla.colegio.model.Religion;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PersonaDAO.
 * @author MV
 *
 */
public class PersonaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Persona persona) {
		if (persona.getId() != null) {
			// update
			String sql = "UPDATE col_persona "
						+ "SET id_tdc=?, "
						+ "id_tap=?, "
						+ "id_gen=?, "
						+ "id_eci=?, "
						+ "id_rel=?, "
						+ "id_dist_viv=?, "
						+ "nro_doc=?, "
						+ "fec_emi=?, "
						+ "ubigeo=?, "
						+ "nom=?, "
						+ "ape_pat=?, "
						+ "ape_mat=?, "
						+ "foto=?, "
						+ "hue=?, "
						+ "fec_nac=?, "
						+ "fec_def=?, "
						+ "id_pais_nac=?, "
						+ "id_dist_nac=?, "
						+ "id_nac=?, "
						+ "tlf=?, "
						+ "corr=?, "
						+ "cel=?, "
						+ "viv=?, "
						+ "dir=?, "
						+ "trab=?, "
						+ "id_cond=?, "
						+ "face=?, "
						+ "tik_tok=?, "
						+ "istrg=?, "
						+ "twitter=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						persona.getId_tdc(),
						persona.getId_tap(),
						persona.getId_gen(),
						persona.getId_eci(),
						persona.getId_rel(),
						persona.getId_dist_viv(),
						persona.getNro_doc(),
						persona.getFec_emi(),
						persona.getUbigeo(),
						persona.getNom()==null ? persona.getNom(): persona.getNom().toUpperCase().trim(),
						persona.getApe_pat()==null ? persona.getApe_pat(): persona.getApe_pat().toUpperCase().trim(),
						persona.getApe_mat()==null ? persona.getApe_mat(): persona.getApe_mat().toUpperCase().trim(),
						persona.getFoto(),
						persona.getHue(),
						persona.getFec_nac(),
						persona.getFec_def(),
						persona.getId_pais_nac(),
						persona.getId_dist_nac(),
						persona.getId_nac(),
						persona.getTlf(),
						persona.getCorr()==null ? persona.getCorr(): persona.getCorr().toUpperCase().trim(),
						persona.getCel(),
						persona.getViv(),
						persona.getDir()==null ? persona.getDir(): persona.getDir().toUpperCase().trim(),
						persona.getTrab(),
						persona.getId_cond(),
						persona.getFace(),
						persona.getTik_tok(),
						persona.getIstrg(),
						persona.getTwitter(),
						persona.getEst(),
						persona.getUsr_act(),
						new java.util.Date(),
						persona.getId()); 
			return persona.getId();

		} else {
			// insert
			String sql = "insert into col_persona ("
						+ "id_tdc, "
						+ "id_tap, "
						+ "id_gen, "
						+ "id_eci, "
						+ "id_rel, "
						+ "id_dist_viv, "
						+ "nro_doc, "
						+ "fec_emi, "
						+ "ubigeo, "
						+ "nom, "
						+ "ape_pat, "
						+ "ape_mat, "
						+ "foto, "
						+ "hue, "
						+ "fec_nac, "
						+ "fec_def, "
						+ "id_pais_nac, "
						+ "id_dist_nac, "
						+ "id_nac, "
						+ "tlf, "
						+ "corr, "
						+ "cel, "
						+ "viv, "
						+ "dir, "
						+ "trab, "
						+ "id_cond, "
						+ "face, "
						+ "tik_tok, "
						+ "istrg, "
						+ "twitter, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				persona.getId_tdc(),
				persona.getId_tap(),
				persona.getId_gen(),
				persona.getId_eci(),
				persona.getId_rel(),
				persona.getId_dist_viv(),
				persona.getNro_doc(),
				persona.getFec_emi(),
				persona.getUbigeo(),
				persona.getNom()==null ? persona.getNom(): persona.getNom().toUpperCase().trim(),
				persona.getApe_pat()==null ? persona.getApe_pat(): persona.getApe_pat().toUpperCase().trim(),
				persona.getApe_mat()==null ? persona.getApe_mat(): persona.getApe_mat().toUpperCase().trim(),
				persona.getFoto(),
				persona.getHue(),
				persona.getFec_nac(),
				persona.getFec_def(),
				persona.getId_pais_nac(),
				persona.getId_dist_nac(),
				persona.getId_nac(),
				persona.getTlf(),
				persona.getCorr()==null ? persona.getCorr(): persona.getCorr().toUpperCase().trim(),
				persona.getCel(),
				persona.getViv(),
				persona.getDir()==null ? persona.getDir(): persona.getDir().toUpperCase().trim(),
				persona.getTrab(),
				persona.getId_cond(),
				persona.getFace(),
				persona.getTik_tok(),
				persona.getIstrg(),
				persona.getTwitter(),
				persona.getEst(),
				persona.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_persona where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Persona> list() {
		String sql = "select * from col_persona";
		
		//System.out.println(sql);
		
		List<Persona> listPersona = jdbcTemplate.query(sql, new RowMapper<Persona>() {

			@Override
			public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPersona;
	}

	public Persona get(int id) {
		String sql = "select * from col_persona WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Persona>() {

			@Override
			public Persona extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Persona getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select per.id per_id, per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ,per.est per_est ";
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		if (aTablas.contains("cat_religion"))
			sql = sql + ", reli.id reli_id  , reli.nom reli_nom  ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
	
		sql = sql + " from col_persona per "; 
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = per.id_tdc ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + " left join cat_est_civil eci on eci.id = per.id_eci ";
		if (aTablas.contains("cat_religion"))
			sql = sql + " left join cat_religion reli on reli.id = per.id_rel ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + " left join cat_distrito dist on dist.id = per.id_dist_viv ";
		sql = sql + " where per.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Persona>() {
		
			@Override
			public Persona extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Persona persona= rsToEntity(rs,"per_");
					if (aTablas.contains("cat_tipo_documento")){
						TipoDocumento tipodocumento = new TipoDocumento();  
							tipodocumento.setId(rs.getInt("tdc_id")) ;  
							tipodocumento.setNom(rs.getString("tdc_nom")) ;  
							persona.setTipoDocumento(tipodocumento);
					}
					if (aTablas.contains("cat_est_civil")){
						EstCivil estcivil = new EstCivil();  
							estcivil.setId(rs.getInt("eci_id")) ;  
							estcivil.setNom(rs.getString("eci_nom")) ;  
							persona.setEstCivil(estcivil);
					}
					if (aTablas.contains("cat_religion")){
						Religion religion = new Religion();  
							religion.setId(rs.getInt("reli_id")) ;  
							religion.setNom(rs.getString("reli_nom")) ;  
							persona.setReligion(religion);
					}
					if (aTablas.contains("cat_distrito")){
						Distrito distrito = new Distrito();  
							distrito.setId(rs.getInt("dist_id")) ;  
							distrito.setNom(rs.getString("dist_nom")) ;  
							//distrito.setCod(rs.getString("dist_cod")) ;  
							distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
							persona.setDistrito(distrito);
					}
							return persona;
				}
				
				return null;
			}
			
		});


	}		
	
	public Persona getByParams(Param param) {

		String sql = "select * from col_persona " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Persona>() {
			@Override
			public Persona extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Persona> listByParams(Param param, String[] order) {

		String sql = "select * from col_persona " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Persona>() {

			@Override
			public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Persona> listFullByParams(Persona persona, String[] order) {
	
		return listFullByParams(Param.toParam("per",persona), order);
	
	}	
	
	public List<Persona> listFullByParams(Param param, String[] order) {

		String sql = "select per.id per_id, per.id_tdc per_id_tdc , per.id_tap per_id_tap , per.id_gen per_id_gen , per.id_eci per_id_eci , per.id_rel per_id_rel , per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.fec_emi per_fec_emi , per.ubigeo per_ubigeo , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.foto per_foto , per.hue per_hue , per.fec_nac per_fec_nac , per.fec_def per_fec_def , per.id_pais_nac per_id_pais_nac , per.id_dist_nac per_id_dist_nac , per.id_nac per_id_nac , per.tlf per_tlf , per.corr per_corr , per.cel per_cel , per.viv per_viv , per.dir per_dir , per.trab per_trab , per.id_cond per_id_cond  ,per.est per_est ";
		sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		sql = sql + ", reli.id reli_id  , reli.nom reli_nom  ";
		sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
		sql = sql + " from col_persona per";
		sql = sql + " left join cat_tipo_documento tdc on tdc.id = per.id_tdc ";
		sql = sql + " left join cat_est_civil eci on eci.id = per.id_eci ";
		sql = sql + " left join cat_religion reli on reli.id = per.id_rel ";
		sql = sql + " left join cat_distrito dist on dist.id = per.id_dist_viv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Persona>() {

			@Override
			public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
				Persona persona= rsToEntity(rs,"per_");
				TipoDocumento tipodocumento = new TipoDocumento();  
				tipodocumento.setId(rs.getInt("tdc_id")) ;  
				tipodocumento.setNom(rs.getString("tdc_nom")) ;  
				persona.setTipoDocumento(tipodocumento);
				EstCivil estcivil = new EstCivil();  
				estcivil.setId(rs.getInt("eci_id")) ;  
				estcivil.setNom(rs.getString("eci_nom")) ;  
				persona.setEstCivil(estcivil);
				Religion religion = new Religion();  
				religion.setId(rs.getInt("reli_id")) ;  
				religion.setNom(rs.getString("reli_nom")) ;  
				persona.setReligion(religion);
				Distrito distrito = new Distrito();  
				distrito.setId(rs.getInt("dist_id")) ;  
				distrito.setNom(rs.getString("dist_nom")) ;  
				//distrito.setCod(rs.getString("dist_cod")) ;  
				distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
				persona.setDistrito(distrito);
				return persona;
			}

		});

	}	


	public List<Familiar> getListFamiliar(Param param, String[] order) {
		String sql = "select * from alu_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			@Override
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				Familiar familiar = new Familiar();

				familiar.setId(rs.getInt("id"));
				familiar.setId_par(rs.getInt("id_par"));
				familiar.setId_gin(rs.getInt("id_gin"));
				familiar.setId_per(rs.getInt("id_per"));
				familiar.setId_usr(rs.getInt("id_usr"));
				familiar.setViv_alu(rs.getString("viv_alu"));
				familiar.setProf(rs.getString("prof"));
				familiar.setOcu(rs.getString("ocu"));
				familiar.setCto_tra(rs.getString("cto_tra"));
				familiar.setDir_tra(rs.getString("dir_tra"));
				familiar.setTlf_tra(rs.getString("tlf_tra"));
				familiar.setEmail_tra(rs.getString("email_tra"));
				//familiar.setIni(rs.getInt("ini"));
				familiar.setEst(rs.getString("est"));
												
				return familiar;
			}

		});	
	}


	// funciones privadas utilitarias para Persona

	private Persona rsToEntity(ResultSet rs,String alias) throws SQLException {
		Persona persona = new Persona();

		persona.setId(rs.getInt( alias + "id"));
		persona.setId_tdc(rs.getString( alias + "id_tdc"));
		persona.setId_tap(rs.getString( alias + "id_tap"));
		persona.setId_gen(rs.getString( alias + "id_gen"));
		persona.setId_eci(rs.getInt( alias + "id_eci"));
		persona.setId_rel(rs.getInt( alias + "id_rel"));
		persona.setId_dist_viv(rs.getInt( alias + "id_dist_viv"));
		persona.setNro_doc(rs.getString( alias + "nro_doc"));
		persona.setFec_emi(rs.getDate( alias + "fec_emi"));
		persona.setUbigeo(rs.getString( alias + "ubigeo"));
		persona.setNom(rs.getString( alias + "nom"));
		persona.setApe_pat(rs.getString( alias + "ape_pat"));
		persona.setApe_mat(rs.getString( alias + "ape_mat"));
		persona.setFoto(rs.getString( alias + "foto"));
		persona.setHue(rs.getString( alias + "hue"));
		persona.setFec_nac(rs.getDate( alias + "fec_nac"));
		persona.setFec_def(rs.getDate( alias + "fec_def"));
		persona.setId_pais_nac(rs.getInt( alias + "id_pais_nac"));
		persona.setId_dist_nac(rs.getInt( alias + "id_dist_nac"));
		persona.setId_nac(rs.getInt( alias + "id_nac"));
		persona.setTlf(rs.getString( alias + "tlf"));
		persona.setCorr(rs.getString( alias + "corr"));
		persona.setCel(rs.getString( alias + "cel"));
		persona.setViv(rs.getString( alias + "viv"));
		persona.setDir(rs.getString( alias + "dir"));
		persona.setTrab(rs.getString( alias + "trab"));
		persona.setId_cond(rs.getInt( alias + "id_cond"));
		persona.setEst(rs.getString( alias + "est"));
								
		return persona;

	}
	
}
