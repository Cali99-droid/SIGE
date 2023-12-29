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
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.Parentesco;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;




/**
 * Implementaci�n de la interface GruFamFamiliarDAO.
 * @author MV
 *
 */
public class GruFamFamiliarDAOImpl{
	final static Logger logger = Logger.getLogger(GruFamFamiliarDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GruFamFamiliar gru_fam_familiar) {
		if (gru_fam_familiar.getId() != null) {
			// update
			String sql = "UPDATE alu_gru_fam_familiar "
						+ "SET id_gpf=?, "
						+ "id_fam=?, "
						+ "id_par=?, "
						+ "flag_permisos=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						gru_fam_familiar.getId_gpf(),
						gru_fam_familiar.getId_fam(),
						gru_fam_familiar.getId_par(),
						gru_fam_familiar.getFlag_permisos(),
						gru_fam_familiar.getEst(),
						gru_fam_familiar.getUsr_act(),
						new java.util.Date(),
						gru_fam_familiar.getId()); 

		} else {
			// insert
			String sql = "insert into alu_gru_fam_familiar ("
						+ "id_gpf, "
						+ "id_fam, "
						+ "id_par, "
						+ "flag_permisos, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?,?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				gru_fam_familiar.getId_gpf(),
				gru_fam_familiar.getId_fam(),
				gru_fam_familiar.getId_par(),
				gru_fam_familiar.getFlag_permisos(),
				gru_fam_familiar.getEst(),
				gru_fam_familiar.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_gru_fam_familiar where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GruFamFamiliar> list() {
		String sql = "select * from alu_gru_fam_familiar";
		
		//logger.info(sql);
		
		List<GruFamFamiliar> listGruFamFamiliar = jdbcTemplate.query(sql, new RowMapper<GruFamFamiliar>() {

			
			public GruFamFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGruFamFamiliar;
	}

	
	public GruFamFamiliar get(int id) {
		String sql = "select * from alu_gru_fam_familiar WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamFamiliar>() {

			
			public GruFamFamiliar extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GruFamFamiliar getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gfr.id gfr_id, gfr.id_gpf gfr_id_gpf , gfr.id_par gfr_id_par , gfr.id_fam gfr_id_fam , gfr.flag_permisos gfr_flag_permisos , gfr.est gfr_est ";
		if (aTablas.contains("alu_gru_fam"))
			sql = sql + ", gpf.id gpf_id  , gpf.cod gpf_cod , gpf.des gpf_des  ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass , fam.cto_tra fam_cto_tra  ";
	
		sql = sql + " from alu_gru_fam_familiar gfr "; 
		if (aTablas.contains("alu_gru_fam"))
			sql = sql + " left join alu_gru_fam gpf on gpf.id = gfr.id_gpf ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = gfr.id_fam ";
		sql = sql + " where gfr.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamFamiliar>() {
		
			
			public GruFamFamiliar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GruFamFamiliar gruFamFamiliar= rsToEntity(rs,"gfr_");
					if (aTablas.contains("alu_gru_fam")){
						GruFam gruFam = new GruFam();  
							gruFam.setId(rs.getInt("gpf_id")) ;  
							gruFam.setCod(rs.getString("gpf_cod")) ;  
							gruFam.setDes(rs.getString("gpf_des")) ;  
							gruFamFamiliar.setGruFam(gruFam);
					}
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setId_tdc(rs.getInt("fam_id_tdc")) ;  
							familiar.setId_par(rs.getInt("fam_id_par")) ;  
							familiar.setId_tap(rs.getString("fam_id_tap")) ;  
							familiar.setId_gen(rs.getString("fam_id_gen")) ;  
							familiar.setId_eci(rs.getInt("fam_id_eci")) ;  
							familiar.setId_gin(rs.getInt("fam_id_gin")) ;  
							familiar.setId_rel(rs.getInt("fam_id_rel")) ;  
							familiar.setId_dist(rs.getInt("fam_id_dist")) ;  
							familiar.setId_ocu(rs.getInt("fam_id_ocu")) ;  
							familiar.setNro_doc(rs.getString("fam_nro_doc")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							familiar.setApe_pat(rs.getString("fam_ape_pat")) ;  
							familiar.setApe_mat(rs.getString("fam_ape_mat")) ;  
							familiar.setHue(rs.getString("fam_hue")) ;  
							familiar.setFec_nac(rs.getDate("fam_fec_nac")) ;  
							familiar.setViv(rs.getString("fam_viv")) ;  
							familiar.setViv_alu(rs.getString("fam_viv_alu")) ;  
							familiar.setDir(rs.getString("fam_dir")) ;  
							familiar.setTlf(rs.getString("fam_tlf")) ;  
							familiar.setCorr(rs.getString("fam_corr")) ;  
							familiar.setCel(rs.getString("fam_cel")) ;  
							familiar.setPass(rs.getString("fam_pass")) ;  
							familiar.setCto_tra(rs.getString("fam_cto_tra")) ;  
							gruFamFamiliar.setFamiliar(familiar);
					}
							return gruFamFamiliar;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GruFamFamiliar getByParams(Param param) {

		String sql = "select * from alu_gru_fam_familiar " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamFamiliar>() {
			
			public GruFamFamiliar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GruFamFamiliar> listByParams(Param param, String[] order) {

		String sql = "select * from alu_gru_fam_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFamFamiliar>() {

			
			public GruFamFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GruFamFamiliar> listFullByParams(GruFamFamiliar gruFamFamiliar, String[] order) {
	
		return listFullByParams(Param.toParam("gfr",gruFamFamiliar), order);
	
	}	
	
	
	public List<GruFamFamiliar> listFullByParams(Param param, String[] order) {
		////logger.info(new DATE());
		String sql = "select gfr.id gfr_id, gfr.id_gpf gfr_id_gpf , gfr.id_fam gfr_id_fam,gfr.id_par gfr_id_par, gfr.flag_permisos gfr_flag_permisos  ,gfr.est gfr_est ";
		sql = sql + ", gpf.id gpf_id  , gpf.cod gpf_cod , gpf.des gpf_des  ";
		sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass , fam.cto_tra fam_cto_tra  ";
		sql = sql + ", par.id par_id, par.par par_par  ";
		sql = sql + " from alu_gru_fam_familiar gfr";
		sql = sql + " left join alu_gru_fam gpf on gpf.id = gfr.id_gpf ";
		sql = sql + " left join alu_familiar fam on fam.id = gfr.id_fam ";
		sql = sql + " left join cat_parentesco par on par.id = fam.id_par ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFamFamiliar>() {

			
			public GruFamFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamFamiliar gruFamFamiliar= rsToEntity(rs,"gfr_");
				GruFam gruFam = new GruFam();  
				gruFam.setId(rs.getInt("gpf_id")) ;  
				gruFam.setCod(rs.getString("gpf_cod")) ;  
				gruFam.setDes(rs.getString("gpf_des")) ;  
				gruFamFamiliar.setGruFam(gruFam);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setId_tdc(rs.getInt("fam_id_tdc")) ;  
				familiar.setId_par(rs.getInt("fam_id_par")) ;  
				familiar.setId_tap(rs.getString("fam_id_tap")) ;  
				familiar.setId_gen(rs.getString("fam_id_gen")) ;  
				familiar.setId_eci(rs.getInt("fam_id_eci")) ;  
				familiar.setId_gin(rs.getInt("fam_id_gin")) ;  
				familiar.setId_rel(rs.getInt("fam_id_rel")) ;  
				familiar.setId_dist(rs.getInt("fam_id_dist")) ;  
				familiar.setId_ocu(rs.getInt("fam_id_ocu")) ;  
				familiar.setNro_doc(rs.getString("fam_nro_doc")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				familiar.setApe_pat(rs.getString("fam_ape_pat")) ;  
				familiar.setApe_mat(rs.getString("fam_ape_mat")) ;  
				familiar.setHue(rs.getString("fam_hue")) ;  
				familiar.setFec_nac(rs.getDate("fam_fec_nac")) ;  
				familiar.setViv(rs.getString("fam_viv")) ;  
				familiar.setViv_alu(rs.getString("fam_viv_alu")) ;  
				familiar.setDir(rs.getString("fam_dir")) ;  
				familiar.setTlf(rs.getString("fam_tlf")) ;  
				familiar.setCorr(rs.getString("fam_corr")) ;  
				familiar.setCel(rs.getString("fam_cel")) ;  
				familiar.setPass(rs.getString("fam_pass")) ;  
				familiar.setCto_tra(rs.getString("fam_cto_tra")) ;  
				
				Parentesco parentesco = new Parentesco();
				parentesco.setPar(rs.getString("par_par"));
				familiar.setParentesco(parentesco);
				
				gruFamFamiliar.setFamiliar(familiar);
				return gruFamFamiliar;
			}

		});

	}	




	// funciones privadas utilitarias para GruFamFamiliar

	private GruFamFamiliar rsToEntity(ResultSet rs,String alias) throws SQLException {
		GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();

		gru_fam_familiar.setId(rs.getInt( alias + "id"));
		gru_fam_familiar.setId_gpf(rs.getInt( alias + "id_gpf"));
		gru_fam_familiar.setId_fam(rs.getInt( alias + "id_fam"));
		gru_fam_familiar.setId_par(rs.getObject( alias + "id_par")==null?null:rs.getInt( alias + "id_par"));
		gru_fam_familiar.setFlag_permisos(rs.getString( alias + "flag_permisos"));
		gru_fam_familiar.setEst(rs.getString( alias + "est"));
								
		return gru_fam_familiar;

	}
	
}
