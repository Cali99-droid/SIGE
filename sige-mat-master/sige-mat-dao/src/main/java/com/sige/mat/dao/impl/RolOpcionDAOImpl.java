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
import com.tesla.colegio.model.RolOpcion;

import com.tesla.colegio.model.Rol;
import com.tesla.colegio.model.Opcion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RolOpcionDAO.
 * @author MV
 *
 */
public class RolOpcionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(RolOpcion rol_opcion) {
		if (rol_opcion.getId() != null) {
			// update
			String sql = "UPDATE seg_rol_opcion "
						+ "SET id_rol=?, "
						+ "id_opc=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						rol_opcion.getId_rol(),
						rol_opcion.getId_opc(),
						rol_opcion.getEst(),
						rol_opcion.getUsr_act(),
						new java.util.Date(),
						rol_opcion.getId()); 
			return rol_opcion.getId();

		} else {
			// insert
			String sql = "insert into seg_rol_opcion ("
						+ "id_rol, "
						+ "id_opc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				rol_opcion.getId_rol(),
				rol_opcion.getId_opc(),
				rol_opcion.getEst(),
				rol_opcion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_rol_opcion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<RolOpcion> list() {
		String sql = "select * from seg_rol_opcion";
		
		
		
		List<RolOpcion> listRolOpcion = jdbcTemplate.query(sql, new RowMapper<RolOpcion>() {

			@Override
			public RolOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRolOpcion;
	}

	public RolOpcion get(int id) {
		String sql = "select * from seg_rol_opcion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RolOpcion>() {

			@Override
			public RolOpcion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public RolOpcion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sro.id sro_id, sro.id_rol sro_id_rol , sro.id_opc sro_id_opc  ,sro.est sro_est ";
		if (aTablas.contains("seg_rol"))
			sql = sql + ", rol.id rol_id  , rol.nom rol_nom  ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + ", opc.id opc_id  , opc.id_opc opc_id_opc , opc.nom opc_nom , opc.titulo opc_titulo , opc.icon opc_icon , opc.url opc_url  ";
	
		sql = sql + " from seg_rol_opcion sro "; 
		if (aTablas.contains("seg_rol"))
			sql = sql + " left join seg_rol rol on rol.id = sro.id_rol ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + " left join seg_opcion opc on opc.id = sro.id_opc ";
		sql = sql + " where sro.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<RolOpcion>() {
		
			@Override
			public RolOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RolOpcion rolopcion= rsToEntity(rs,"sro_");
					if (aTablas.contains("seg_rol")){
						Rol rol = new Rol();  
							rol.setId(rs.getInt("rol_id")) ;  
							rol.setNom(rs.getString("rol_nom")) ;  
							rolopcion.setRol(rol);
					}
					if (aTablas.contains("seg_opcion")){
						Opcion opcion = new Opcion();  
							opcion.setId(rs.getInt("opc_id")) ;  
							opcion.setId_opc(rs.getInt("opc_id_opc")) ;  
							opcion.setNom(rs.getString("opc_nom")) ;  
							opcion.setTitulo(rs.getString("opc_titulo")) ;  
							opcion.setIcon(rs.getString("opc_icon")) ;  
							opcion.setUrl(rs.getString("opc_url")) ;  
							rolopcion.setOpcion(opcion);
					}
							return rolopcion;
				}
				
				return null;
			}
			
		});


	}		
	
	public RolOpcion getByParams(Param param) {

		String sql = "select * from seg_rol_opcion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RolOpcion>() {
			@Override
			public RolOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<RolOpcion> listByParams(Param param, String[] order) {

		String sql = "select * from seg_rol_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<RolOpcion>() {

			@Override
			public RolOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<RolOpcion> listFullByParams(RolOpcion rolopcion, String[] order) {
	
		return listFullByParams(Param.toParam("sro",rolopcion), order);
	
	}	
	
	public List<RolOpcion> listFullByParams(Param param, String[] order) {

		String sql = "select sro.id sro_id, sro.id_rol sro_id_rol , sro.id_opc sro_id_opc  ,sro.est sro_est ";
		sql = sql + ", rol.id rol_id  , rol.nom rol_nom  ";
		sql = sql + ", opc.id opc_id  , opc.id_opc opc_id_opc , opc.nom opc_nom , opc.titulo opc_titulo , opc.icon opc_icon , opc.url opc_url  ";
		sql = sql + " from seg_rol_opcion sro";
		sql = sql + " left join seg_rol rol on rol.id = sro.id_rol ";
		sql = sql + " left join seg_opcion opc on opc.id = sro.id_opc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<RolOpcion>() {

			@Override
			public RolOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				RolOpcion rolopcion= rsToEntity(rs,"sro_");
				Rol rol = new Rol();  
				rol.setId(rs.getInt("rol_id")) ;  
				rol.setNom(rs.getString("rol_nom")) ;  
				rolopcion.setRol(rol);
				Opcion opcion = new Opcion();  
				opcion.setId(rs.getInt("opc_id")) ;  
				opcion.setId_opc(rs.getInt("opc_id_opc")) ;  
				opcion.setNom(rs.getString("opc_nom")) ;  
				opcion.setTitulo(rs.getString("opc_titulo")) ;  
				opcion.setIcon(rs.getString("opc_icon")) ;  
				opcion.setUrl(rs.getString("opc_url")) ;  
				rolopcion.setOpcion(opcion);
				return rolopcion;
			}

		});

	}	




	// funciones privadas utilitarias para RolOpcion

	private RolOpcion rsToEntity(ResultSet rs,String alias) throws SQLException {
		RolOpcion rol_opcion = new RolOpcion();

		rol_opcion.setId(rs.getInt( alias + "id"));
		rol_opcion.setId_rol(rs.getInt( alias + "id_rol"));
		rol_opcion.setId_opc(rs.getInt( alias + "id_opc"));
		rol_opcion.setEst(rs.getString( alias + "est"));
								
		return rol_opcion;

	}
	
}
