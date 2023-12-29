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
import com.tesla.colegio.model.Opcion;

import com.tesla.colegio.model.RolOpcion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface OpcionDAO.
 * @author MV
 *
 */
public class OpcionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Opcion opcion) {
		if (opcion.getId() != null) {
			// update
			String sql = "UPDATE seg_opcion "
						+ "SET id_opc=?, "
						+ "nom=?, "
						+ "titulo=?, "
						+ "subtitulo=?, "
						+ "icon=?, "
						+ "url=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						opcion.getId_opc(),
						opcion.getNom(),
						opcion.getTitulo(),
						opcion.getSubtitulo(),
						opcion.getIcon(),
						opcion.getUrl(),
						opcion.getEst(),
						opcion.getUsr_act(),
						new java.util.Date(),
						opcion.getId()); 
			return opcion.getId();

		} else {
			// insert
			String sql = "insert into seg_opcion ("
						+ "id_opc, "
						+ "nom, "
						+ "titulo, "
						+ "subtitulo, "
						+ "icon, "
						+ "url, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				opcion.getId_opc(),
				opcion.getNom(),
				opcion.getTitulo(),
				opcion.getSubtitulo(),
				opcion.getIcon(),
				opcion.getUrl(),
				opcion.getEst(),
				opcion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_opcion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Opcion> list() {
		String sql = "select * from seg_opcion";
		
		
		
		List<Opcion> listOpcion = jdbcTemplate.query(sql, new RowMapper<Opcion>() {

			@Override
			public Opcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listOpcion;
	}

	public Opcion get(int id) {
		String sql = "select * from seg_opcion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Opcion>() {

			@Override
			public Opcion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Opcion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select opc.id opc_id, opc.id_opc opc_id_opc , opc.nom opc_nom , opc.titulo opc_titulo , opc.icon opc_icon , opc.url opc_url  ,opc.est opc_est ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + ", opc.id opc_id  , opc.id_opc opc_id_opc , opc.nom opc_nom , opc.titulo opc_titulo , opc.icon opc_icon , opc.url opc_url  ";
	
		sql = sql + " from seg_opcion opc "; 
		if (aTablas.contains("seg_opcion"))
			sql = sql + " left join seg_opcion opc on opc.id = opc.id_opc ";
		sql = sql + " where opc.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Opcion>() {
		
			@Override
			public Opcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Opcion opcion= rsToEntity(rs,"opc_");
					if (aTablas.contains("seg_opcion")){
						Opcion opcionPadre = new Opcion();  
						opcionPadre.setId(rs.getInt("opc_id")) ;  
						opcionPadre.setId_opc(rs.getInt("opc_id_opc")) ;  
						opcionPadre.setNom(rs.getString("opc_nom")) ;  
						opcionPadre.setTitulo(rs.getString("opc_titulo")) ;  
						opcionPadre.setIcon(rs.getString("opc_icon")) ;  
						opcionPadre.setUrl(rs.getString("opc_url")) ;  
							opcion.setOpcion(opcionPadre);
					}
							return opcion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Opcion getByParams(Param param) {

		String sql = "select * from seg_opcion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Opcion>() {
			@Override
			public Opcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Opcion> listByParams(Param param, String[] order) {

		String sql = "select * from seg_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Opcion>() {

			@Override
			public Opcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Opcion> listFullByParams(Opcion opcion, String[] order) {
	
		return listFullByParams(Param.toParam("opc",opcion), order);
	
	}	
	
	public List<Opcion> listFullByParams(Param param, String[] order) {

		String sql = "select opc.id opc_id, opc.id_opc opc_id_opc , opc.nom opc_nom , opc.titulo opc_titulo ,opc.subtitulo opc_subtitulo , opc.icon opc_icon , opc.url opc_url  ,opc.est opc_est ";
		sql = sql + ", opc_padre.id opc_padre_id  , opc_padre.id_opc opc_padre_id_opc , opc_padre.nom opc_padre_nom , opc_padre.titulo opc_padre_titulo , opc_padre.icon opc_padre_icon , opc_padre.url opc_padre_url  ";
		sql = sql + " from seg_opcion opc";
		sql = sql + " left join seg_opcion opc_padre on opc_padre.id = opc.id_opc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Opcion>() {

			@Override
			public Opcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Opcion opcion= rsToEntity(rs,"opc_");
				Opcion opcionPadre = new Opcion();  
				
				opcionPadre.setId(rs.getObject("opc_padre_id")==null?null:rs.getInt("opc_padre_id"));
				
				opcionPadre.setId_opc(rs.getInt("opc_padre_id_opc")) ;  
				opcionPadre.setNom(rs.getString("opc_padre_nom")) ;  
				opcionPadre.setTitulo(rs.getString("opc_padre_titulo")) ;  
				opcionPadre.setIcon(rs.getString("opc_padre_icon")) ;  
				opcionPadre.setUrl(rs.getString("opc_padre_url")) ;  
				opcion.setOpcion(opcionPadre);
				return opcion;
			}

		});

	}	


	public List<Opcion> getListOpcion(Param param, String[] order) {
		String sql = "select * from seg_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Opcion>() {

			@Override
			public Opcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Opcion opcion = new Opcion();

				opcion.setId(rs.getInt("id"));
				opcion.setId_opc(rs.getInt("id_opc"));
				opcion.setNom(rs.getString("nom"));
				opcion.setTitulo(rs.getString("titulo"));
				opcion.setIcon(rs.getString("icon"));
				opcion.setUrl(rs.getString("url"));
				opcion.setEst(rs.getString("est"));
												
				return opcion;
			}

		});	
	}
	public List<RolOpcion> getListRolOpcion(Param param, String[] order) {
		String sql = "select * from seg_rol_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<RolOpcion>() {

			@Override
			public RolOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				RolOpcion rol_opcion = new RolOpcion();

				rol_opcion.setId(rs.getInt("id"));
				rol_opcion.setId_rol(rs.getInt("id_rol"));
				rol_opcion.setId_opc(rs.getInt("id_opc"));
				rol_opcion.setEst(rs.getString("est"));
												
				return rol_opcion;
			}

		});	
	}


	// funciones privadas utilitarias para Opcion

	protected Opcion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Opcion opcion = new Opcion();

		opcion.setId(rs.getInt( alias + "id"));
		opcion.setId_opc(rs.getObject(alias + "id_opc")==null?null:rs.getInt(alias + "id_opc"));
		opcion.setNom(rs.getString( alias + "nom"));
		opcion.setTitulo(rs.getString( alias + "titulo"));
		opcion.setSubtitulo(rs.getString( alias + "subtitulo"));
		opcion.setIcon(rs.getString( alias + "icon"));
		opcion.setUrl(rs.getString( alias + "url"));
		opcion.setEst(rs.getString( alias + "est"));
								
		return opcion;

	}
	
}
