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
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.GruFamAlumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci&oacute;n de la interface GruFamDAO.
 * @author MV
 *
 */
public class GruFamDAOImpl{
	final static Logger logger = Logger.getLogger(GruFamDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GruFam gru_fam) {
		if (gru_fam.getId() !=null ) {
			// update
			String sql = "UPDATE alu_gru_fam "
						+ "SET cod=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "id_dist=?, "
						+ "id_seg=?, "
						+ "id_csal=?, "
						+ "id_usr=?, "
						+ "cod_aseg=?, "
						+ "direccion=?, "
						+ "referencia=?, "
						+ "latitud=?, "
						+ "longitud=?, "
						+ "zoom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						gru_fam.getCod(),
						gru_fam.getNom()==null ? gru_fam.getNom(): gru_fam.getNom().toUpperCase(),
						gru_fam.getDes(),
						gru_fam.getId_dist(),
						gru_fam.getId_seg(),
						gru_fam.getId_csal(),
						gru_fam.getId_usr(),
						gru_fam.getCod_aseg(),
						gru_fam.getDireccion()==null ? gru_fam.getDireccion(): gru_fam.getDireccion().toUpperCase(),
						gru_fam.getReferencia()==null ? gru_fam.getReferencia(): gru_fam.getReferencia().toUpperCase(),
						gru_fam.getLatitud(),
						gru_fam.getLongitud(),
						gru_fam.getZoom(),
						gru_fam.getEst(),
						gru_fam.getUsr_act(),
						new java.util.Date(),
						gru_fam.getId()); 
			return gru_fam.getId();

		} else {
			// insert
			String sql = "insert into alu_gru_fam ("
						+ "cod, "
						+ "nom, "
						+ "des, "
						+ "id_dist, "
						+ "id_seg, "
						+ "id_csal, "
						+ "id_usr, "
						+ "cod_aseg, "
						+ "direccion, referencia,"
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				gru_fam.getCod(),
				gru_fam.getNom()==null ? gru_fam.getNom(): gru_fam.getNom().toUpperCase(),
				gru_fam.getDes(),
				gru_fam.getId_dist(),
				gru_fam.getId_seg(),
				gru_fam.getId_csal(),
				gru_fam.getId_usr(),
				gru_fam.getCod_aseg(),
				gru_fam.getDireccion()==null ? gru_fam.getDireccion(): gru_fam.getDireccion().toUpperCase(),
				gru_fam.getReferencia()==null ? gru_fam.getReferencia(): gru_fam.getReferencia().toUpperCase(),
				gru_fam.getEst(),
				gru_fam.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_gru_fam where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GruFam> list() {
		String sql = "select * from alu_gru_fam";
		
		//logger.info(sql);
		
		List<GruFam> listGruFam = jdbcTemplate.query(sql, new RowMapper<GruFam>() {

			
			public GruFam mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGruFam;
	}

	
	public GruFam get(int id) {
		String sql = "select * from alu_gru_fam WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFam>() {

			
			public GruFam extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GruFam getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gpf.id gpf_id, gpf.cod gpf_cod , gpf.des gpf_des , gpf.id_dist gpf_id_dist , gpf.direccion gpf_direccion  , gpf.referencia gpf_referencia  , gpf.latitud gpf_latitud ,gpf.longitud gpf_longitud,gpf.zoom gpf_zoom,gpf.est gpf_est ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.id_pro dist_id_pro  ";
		if (aTablas.contains("cat_provincia"))
			sql = sql + ", pro.id pro_id  , pro.nom pro_nom , pro.id_dep pro_id_dep  ";
		if (aTablas.contains("cat_departamento"))
			sql = sql + ", dep.id dep_id  , dep.nom dep_nom  ";	
		
		sql = sql + " from alu_gru_fam gpf "; 
		if (aTablas.contains("cat_distrito"))
			sql = sql + " left join cat_distrito dist on dist.id = gpf.id_dist ";
		if (aTablas.contains("cat_provincia"))
			sql = sql + " left join cat_provincia pro on pro.id = dist.id_pro ";
		if (aTablas.contains("cat_departamento"))
			sql = sql + " left join cat_departamento dep on dep.id = pro.id_dep ";
		
		
		sql = sql + " where gpf.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFam>() {
		
			
			public GruFam extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GruFam gruFam= rsToEntity(rs,"gpf_");
					if (aTablas.contains("cat_distrito")){
						Distrito distrito = new Distrito();  
						distrito.setId(rs.getInt("dist_id")) ;  
						distrito.setNom(rs.getString("dist_nom")) ;  
						distrito.setId_pro(rs.getInt("dist_id_pro")) ;  

						if (aTablas.contains("cat_provincia")){
							Provincia provincia = new Provincia();  
							provincia.setId(rs.getInt("pro_id")) ;  
							provincia.setNom(rs.getString("pro_nom")) ;  
							provincia.setId_dep(rs.getInt("pro_id_dep")) ;  
							if (aTablas.contains("cat_departamento")){
								Departamento departamento = new Departamento();  
								departamento.setId(rs.getInt("dep_id")) ;  
								departamento.setNom(rs.getString("dep_nom")) ;  
								provincia.setDepartamento(departamento);
							}
							distrito.setProvincia(provincia);

						}						
						gruFam.setDistrito(distrito);
						
					}
							return gruFam;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GruFam getByParams(Param param) {

		String sql = "select * from alu_gru_fam " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFam>() {
			
			public GruFam extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GruFam> listByParams(Param param, String[] order) {

		String sql = "select * from alu_gru_fam " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFam>() {

			
			public GruFam mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GruFam> listFullByParams(GruFam gruFam, String[] order) {
	
		return listFullByParams(Param.toParam("gpf",gruFam), order);
	
	}	
	
	
	public List<GruFam> listFullByParams(Param param, String[] order) {

		String sql = "select gpf.id gpf_id, gpf.id_usr gpf_id_usr, gpf.id_csal gpf_id_csal, gpf.cod gpf_cod , gpf.des gpf_des , gpf.id_dist gpf_id_dist, gpf.nom gpf_nom , gpf.direccion gpf_direccion  ,gpf.referencia gpf_referencia  ,gpf.est gpf_est ";
		sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.id_pro dist_id_pro  ";
		sql = sql + " from alu_gru_fam gpf";
		sql = sql + " left join cat_distrito dist on dist.id = gpf.id_dist ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFam>() {

			
			public GruFam mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFam gruFam= rsToEntity(rs,"gpf_");
				Distrito distrito = new Distrito();  
				distrito.setId(rs.getInt("dist_id")) ;  
				distrito.setNom(rs.getString("dist_nom")) ;  
				distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
				gruFam.setDistrito(distrito);
				return gruFam;
			}

		});

	}	


	public List<GruFamFamiliar> getListGruFamFamiliar(Param param, String[] order) {
		String sql = "select * from alu_gru_fam_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFamFamiliar>() {

			
			public GruFamFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();

				gru_fam_familiar.setId(rs.getInt("id"));
				gru_fam_familiar.setId_gpf(rs.getInt("id_gpf"));
				gru_fam_familiar.setId_fam(rs.getInt("id_fam"));
				gru_fam_familiar.setEst(rs.getString("est"));
												
				return gru_fam_familiar;
			}

		});	
	}
	public List<GruFamAlumno> getListGruFamAlumno(Param param, String[] order) {
		String sql = "select * from alu_gru_fam_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFamAlumno>() {

			
			public GruFamAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamAlumno gru_fam_alumno = new GruFamAlumno();

				gru_fam_alumno.setId(rs.getInt("id"));
				gru_fam_alumno.setId_gpf(rs.getInt("id_gpf"));
				gru_fam_alumno.setId_alu(rs.getInt("id_alu"));
				gru_fam_alumno.setEst(rs.getString("est"));
												
				return gru_fam_alumno;
			}

		});	
	}


	// funciones privadas utilitarias para GruFam

	private GruFam rsToEntity(ResultSet rs,String alias) throws SQLException {
		GruFam gru_fam = new GruFam();

		gru_fam.setId(rs.getInt( alias + "id"));
		gru_fam.setCod(rs.getString( alias + "cod"));
		gru_fam.setNom(rs.getString( alias + "nom"));
		gru_fam.setDes(rs.getString( alias + "des"));
		gru_fam.setId_dist(rs.getInt( alias + "id_dist"));
		gru_fam.setId_usr(rs.getInt( alias + "id_usr"));
		gru_fam.setId_csal(rs.getInt( alias + "id_csal"));
		gru_fam.setDireccion(rs.getString( alias + "direccion"));
		gru_fam.setReferencia(rs.getString( alias + "referencia"));
		gru_fam.setLatitud(rs.getBigDecimal( alias + "latitud"));
		gru_fam.setLongitud(rs.getBigDecimal( alias + "longitud"));
		gru_fam.setZoom(rs.getInt( alias + "zoom"));
		
		gru_fam.setEst(rs.getString( alias + "est"));
								
		return gru_fam;

	}
	
}
