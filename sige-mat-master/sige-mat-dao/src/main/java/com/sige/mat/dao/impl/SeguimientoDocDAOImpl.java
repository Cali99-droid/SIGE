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
import com.tesla.colegio.model.SeguimientoDoc;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.PerUni;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SeguimientoDocDAO.
 * @author MV
 *
 */
public class SeguimientoDocDAOImpl{
	final static Logger logger = Logger.getLogger(SeguimientoDocDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SeguimientoDoc seguimiento_doc) {
		if (seguimiento_doc.getId() != null) {
			// update
			String sql = "UPDATE col_seguimiento_doc "
						+ "SET id_fam=?, "
						+ "id_cpu=?, "
						+ "id_mat=?, "
						+ "tip=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						seguimiento_doc.getId_fam(),
						seguimiento_doc.getId_cpu(),
						seguimiento_doc.getId_mat(),
						seguimiento_doc.getTip(),
						seguimiento_doc.getEst(),
						seguimiento_doc.getUsr_act(),
						new java.util.Date(),
						seguimiento_doc.getId()); 
			return seguimiento_doc.getId();

		} else {
			// insert
			String sql = "insert into col_seguimiento_doc ("
						+ "id_fam, "
						+ "id_cpu, "
						+ "id_mat, "
						+ "tip, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				seguimiento_doc.getId_fam(),
				seguimiento_doc.getId_cpu(),
				seguimiento_doc.getId_mat(),
				seguimiento_doc.getTip(),
				seguimiento_doc.getEst(),
				seguimiento_doc.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_seguimiento_doc where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SeguimientoDoc> list() {
		String sql = "select * from col_seguimiento_doc";
		
		//logger.info(sql);
		
		List<SeguimientoDoc> listSeguimientoDoc = jdbcTemplate.query(sql, new RowMapper<SeguimientoDoc>() {

			@Override
			public SeguimientoDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSeguimientoDoc;
	}

	public SeguimientoDoc get(int id) {
		String sql = "select * from col_seguimiento_doc WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SeguimientoDoc>() {

			@Override
			public SeguimientoDoc extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SeguimientoDoc getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csd.id csd_id, csd.id_fam csd_id_fam ,  csd.id_mat csd_id_mat , csd.id_cpu csd_id_cpu , csd.tip csd_tip  ,csd.est csd_est ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin  ";
	
		sql = sql + " from col_seguimiento_doc csd "; 
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = csd.id_fam ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = csd.id_cpu ";
		sql = sql + " where csd.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SeguimientoDoc>() {
		
			@Override
			public SeguimientoDoc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SeguimientoDoc seguimientodoc= rsToEntity(rs,"csd_");
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							seguimientodoc.setFamiliar(familiar);
					}
					if (aTablas.contains("col_per_uni")){
						PerUni peruni = new PerUni();  
							peruni.setId(rs.getInt("cpu_id")) ;  
							peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
							//peruni.setId_anio(rs.getString("cpu_id_anio")) ;  
							peruni.setNump(rs.getInt("cpu_nump")) ;  
							peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
							peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
							peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
							peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
							seguimientodoc.setPerUni(peruni);
					}
							return seguimientodoc;
				}
				
				return null;
			}
			
		});


	}		
	
	public SeguimientoDoc getByParams(Param param) {

		String sql = "select * from col_seguimiento_doc " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SeguimientoDoc>() {
			@Override
			public SeguimientoDoc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SeguimientoDoc> listByParams(Param param, String[] order) {

		String sql = "select * from col_seguimiento_doc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SeguimientoDoc>() {

			@Override
			public SeguimientoDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SeguimientoDoc> listFullByParams(SeguimientoDoc seguimientodoc, String[] order) {
	
		return listFullByParams(Param.toParam("csd",seguimientodoc), order);
	
	}	
	
	public List<SeguimientoDoc> listFullByParams(Param param, String[] order) {

		String sql = "select csd.id csd_id, csd.id_fam csd_id_fam ,  csd.id_mat csd_id_mat , csd.id_cpu csd_id_cpu , csd.tip csd_tip  ,csd.est csd_est ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin  ";
		sql = sql + " from col_seguimiento_doc csd";
		sql = sql + " left join alu_familiar fam on fam.id = csd.id_fam ";
		sql = sql + " left join col_per_uni cpu on cpu.id = csd.id_cpu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SeguimientoDoc>() {

			@Override
			public SeguimientoDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				SeguimientoDoc seguimientodoc= rsToEntity(rs,"csd_");
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				seguimientodoc.setFamiliar(familiar);
				PerUni peruni = new PerUni();  
				peruni.setId(rs.getInt("cpu_id")) ;  
				peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
				//peruni.setId_anio(rs.getString("cpu_id_anio")) ;  
				peruni.setNump(rs.getInt("cpu_nump")) ;  
				peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
				peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
				peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
				peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
				seguimientodoc.setPerUni(peruni);
				return seguimientodoc;
			}

		});

	}	




	// funciones privadas utilitarias para SeguimientoDoc

	private SeguimientoDoc rsToEntity(ResultSet rs,String alias) throws SQLException {
		SeguimientoDoc seguimiento_doc = new SeguimientoDoc();

		seguimiento_doc.setId(rs.getInt( alias + "id"));
		seguimiento_doc.setId_fam(rs.getInt( alias + "id_fam"));
		seguimiento_doc.setId_cpu(rs.getInt( alias + "id_cpu"));
		seguimiento_doc.setId_mat(rs.getInt( alias + "id_mat"));
		seguimiento_doc.setTip(rs.getString( alias + "tip"));
		seguimiento_doc.setEst(rs.getString( alias + "est"));
								
		return seguimiento_doc;

	}
	
}
