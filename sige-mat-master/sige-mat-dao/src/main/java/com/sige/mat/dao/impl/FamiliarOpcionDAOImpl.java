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
import com.tesla.colegio.model.FamiliarOpcion;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Opcion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface FamiliarOpcionDAO.
 * @author MV
 *
 */
public class FamiliarOpcionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(FamiliarOpcion familiar_opcion) {
		if (familiar_opcion.getId() != null) {
			// update
			String sql = "UPDATE seg_familiar_opcion "
						+ "SET id_fam=?, "
						+ "id_opc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						familiar_opcion.getId_fam(),
						familiar_opcion.getId_opc(),
						familiar_opcion.getEst(),
						familiar_opcion.getUsr_act(),
						new java.util.Date(),
						familiar_opcion.getId()); 
			return familiar_opcion.getId();

		} else {
			// insert
			String sql = "insert into seg_familiar_opcion ("
						+ "id_fam, "
						+ "id_opc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				familiar_opcion.getId_fam(),
				familiar_opcion.getId_opc(),
				familiar_opcion.getEst(),
				familiar_opcion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_familiar_opcion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<FamiliarOpcion> list() {
		String sql = "select * from seg_familiar_opcion";
		
		
		
		List<FamiliarOpcion> listFamiliarOpcion = jdbcTemplate.query(sql, new RowMapper<FamiliarOpcion>() {

			@Override
			public FamiliarOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listFamiliarOpcion;
	}

	public FamiliarOpcion get(int id) {
		String sql = "select * from seg_familiar_opcion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<FamiliarOpcion>() {

			@Override
			public FamiliarOpcion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public FamiliarOpcion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sfo.id sfo_id, sfo.id_fam sfo_id_fam , sfo.id_opc sfo_id_opc  ,sfo.est sfo_est ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + ", opc.id opc_id  , opc.mdl opc_mdl , opc.nom opc_nom , opc.url opc_url , opc.niv opc_niv  ";
	
		sql = sql + " from seg_familiar_opcion sfo "; 
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = sfo.id_fam ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + " left join seg_opcion opc on opc.id = sfo.id_opc ";
		sql = sql + " where sfo.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<FamiliarOpcion>() {
		
			@Override
			public FamiliarOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					FamiliarOpcion familiaropcion= rsToEntity(rs,"sfo_");
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							familiaropcion.setFamiliar(familiar);
					}
					if (aTablas.contains("seg_opcion")){
						Opcion opcion = new Opcion();  
							opcion.setId(rs.getInt("opc_id")) ;  
							//opcion.setMdl(rs.getString("opc_mdl")) ;  
							opcion.setNom(rs.getString("opc_nom")) ;  
							opcion.setUrl(rs.getString("opc_url")) ;  
							//opcion.setNiv(rs.getString("opc_niv")) ;  
							familiaropcion.setOpcion(opcion);
					}
							return familiaropcion;
				}
				
				return null;
			}
			
		});


	}		
	
	public FamiliarOpcion getByParams(Param param) {

		String sql = "select * from seg_familiar_opcion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<FamiliarOpcion>() {
			@Override
			public FamiliarOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<FamiliarOpcion> listByParams(Param param, String[] order) {

		String sql = "select * from seg_familiar_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<FamiliarOpcion>() {

			@Override
			public FamiliarOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<FamiliarOpcion> listFullByParams(FamiliarOpcion familiaropcion, String[] order) {
	
		return listFullByParams(Param.toParam("sfo",familiaropcion), order);
	
	}	
	
	public List<FamiliarOpcion> listFullByParams(Param param, String[] order) {

		String sql = "select sfo.id sfo_id, sfo.id_fam sfo_id_fam , sfo.id_opc sfo_id_opc  ,sfo.est sfo_est ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", opc.id opc_id  , opc.mdl opc_mdl , opc.nom opc_nom , opc.url opc_url , opc.niv opc_niv  ";
		sql = sql + " from seg_familiar_opcion sfo";
		sql = sql + " left join alu_familiar fam on fam.id = sfo.id_fam ";
		sql = sql + " left join seg_opcion opc on opc.id = sfo.id_opc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<FamiliarOpcion>() {

			@Override
			public FamiliarOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				FamiliarOpcion familiaropcion= rsToEntity(rs,"sfo_");
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				familiaropcion.setFamiliar(familiar);
				Opcion opcion = new Opcion();  
				opcion.setId(rs.getInt("opc_id")) ;  
				//opcion.setMdl(rs.getString("opc_mdl")) ;  
				opcion.setNom(rs.getString("opc_nom")) ;  
				opcion.setUrl(rs.getString("opc_url")) ;  
				//opcion.setNiv(rs.getString("opc_niv")) ;  
				familiaropcion.setOpcion(opcion);
				return familiaropcion;
			}

		});

	}	




	// funciones privadas utilitarias para FamiliarOpcion

	private FamiliarOpcion rsToEntity(ResultSet rs,String alias) throws SQLException {
		FamiliarOpcion familiar_opcion = new FamiliarOpcion();

		familiar_opcion.setId(rs.getInt( alias + "id"));
		familiar_opcion.setId_fam(rs.getInt( alias + "id_fam"));
		familiar_opcion.setId_opc(rs.getInt( alias + "id_opc"));
		familiar_opcion.setEst(rs.getString( alias + "est"));
								
		return familiar_opcion;

	}
	
}
