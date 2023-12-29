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
import com.tesla.colegio.model.Permisos;

import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PermisosDAO.
 * @author MV
 *
 */
public class PermisosDAOImpl{
	final static Logger logger = Logger.getLogger(PermisosDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Permisos permisos) {
		if (permisos.getId() != null) {
			// update
			String sql = "UPDATE alu_permisos "
						+ "SET id_alu=?,id_fam=?, "
						+ "rec_lib=?, "
						+ "ped_inf=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						permisos.getId_alu(),
						permisos.getId_fam(),
						permisos.getRec_lib(),
						permisos.getPed_inf(),
						permisos.getEst(),
						permisos.getUsr_act(),
						new java.util.Date(),
						permisos.getId()); 
			return permisos.getId();
		} else {
			// insert
			String sql = "insert into alu_permisos ("
						+ "id_alu,id_fam, "
						+ "rec_lib, "
						+ "ped_inf, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql,
				permisos.getId_alu(),
				permisos.getId_fam(),
				permisos.getRec_lib(),
				permisos.getPed_inf(),
				permisos.getEst(),
				permisos.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_permisos where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Permisos> list() {
		String sql = "select * from alu_permisos";
		
		//logger.info(sql);
		
		List<Permisos> listPermisos = jdbcTemplate.query(sql, new RowMapper<Permisos>() {

			
			public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPermisos;
	}

	
	public Permisos get(int id) {
		String sql = "select * from alu_permisos WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Permisos>() {

			
			public Permisos extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Permisos getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select per.id per_id, per.id_alu per_id_alu , per.id_fam per_id_fam , per.rec_lib per_rec_lib , per.ped_inf per_ped_inf  ,per.est per_est ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass , fam.cto_tra fam_cto_tra  ";
	
		sql = sql + " from alu_permisos per "; 
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = per.id_fam ";
		sql = sql + " where per.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Permisos>() {
		
			
			public Permisos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Permisos permisos= rsToEntity(rs,"per_");
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
							permisos.setFamiliar(familiar);
					}
							return permisos;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Permisos getByParams(Param param) {

		String sql = "select * from alu_permisos " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Permisos>() {
			
			public Permisos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Permisos> listByParams(Param param, String[] order) {

		String sql = "select * from alu_permisos " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Permisos>() {

			
			public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Permisos> listFullByParams(Permisos permisos, String[] order) {
	
		return listFullByParams(Param.toParam("per",permisos), order);
	
	}	
	
	
	public List<Permisos> listFullByParams(Param param, String[] order) {

		String sql = "select per.id per_id, per.id_alu per_id_alu, per.id_fam per_id_fam , per.rec_lib per_rec_lib , per.ped_inf per_ped_inf  ,per.est per_est ";
		sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass , fam.cto_tra fam_cto_tra  ";
		sql = sql + " from alu_permisos per";
		sql = sql + " left join alu_familiar fam on fam.id = per.id_fam ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Permisos>() {

			
			public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
				Permisos permisos= rsToEntity(rs,"per_");
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
				permisos.setFamiliar(familiar);
				return permisos;
			}

		});

	}	




	// funciones privadas utilitarias para Permisos

	private Permisos rsToEntity(ResultSet rs,String alias) throws SQLException {
		Permisos permisos = new Permisos();

		permisos.setId(rs.getInt( alias + "id"));
		permisos.setId_alu(rs.getInt( alias + "id_alu"));
		permisos.setId_fam(rs.getInt( alias + "id_fam"));
		permisos.setRec_lib(rs.getString( alias + "rec_lib"));
		permisos.setPed_inf(rs.getString( alias + "ped_inf"));
		permisos.setEst(rs.getString( alias + "est"));
								
		return permisos;

	}
	
}
