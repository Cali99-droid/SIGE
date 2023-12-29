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
import com.tesla.colegio.model.PerfilOpcion;

import com.tesla.colegio.model.Perfil;
import com.tesla.colegio.model.Opcion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerfilOpcionDAO.
 * @author MV
 *
 */
public class PerfilOpcionDAOImpl{
	final static Logger logger = Logger.getLogger(PerfilOpcionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PerfilOpcion perfil_opcion) {
		if (perfil_opcion.getId() > 0) {
			// update
			String sql = "UPDATE seg_perfil_opcion "
						+ "SET id_per=?, "
						+ "id_opc=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						perfil_opcion.getId_per(),
						perfil_opcion.getId_opc(),
						perfil_opcion.getEst(),
						perfil_opcion.getUsr_act(),
						new java.util.Date(),
						perfil_opcion.getId()); 

		} else {
			// insert
			String sql = "insert into seg_perfil_opcion ("
						+ "id_per, "
						+ "id_opc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				perfil_opcion.getId_per(),
				perfil_opcion.getId_opc(),
				perfil_opcion.getEst(),
				perfil_opcion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from seg_perfil_opcion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<PerfilOpcion> list() {
		String sql = "select * from seg_perfil_opcion";
		
		//logger.info(sql);
		
		List<PerfilOpcion> listPerfilOpcion = jdbcTemplate.query(sql, new RowMapper<PerfilOpcion>() {

			
			public PerfilOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerfilOpcion;
	}

	
	public PerfilOpcion get(int id) {
		String sql = "select * from seg_perfil_opcion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerfilOpcion>() {

			
			public PerfilOpcion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public PerfilOpcion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select pop.id pop_id, pop.id_per pop_id_per , pop.id_opc pop_id_opc  ,pop.est pop_est ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + ", opc.id opc_id  , opc.mdl opc_mdl , opc.nom opc_nom , opc.url opc_url , opc.niv opc_niv  ";
	
		sql = sql + " from seg_perfil_opcion pop "; 
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = pop.id_per ";
		if (aTablas.contains("seg_opcion"))
			sql = sql + " left join seg_opcion opc on opc.id = pop.id_opc ";
		sql = sql + " where pop.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PerfilOpcion>() {
		
			
			public PerfilOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PerfilOpcion perfilOpcion= rsToEntity(rs,"pop_");
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							perfilOpcion.setPerfil(perfil);
					}
					if (aTablas.contains("seg_opcion")){
						Opcion opcion = new Opcion();  
							opcion.setId(rs.getInt("opc_id")) ;  
							//opcion.setMdl(rs.getString("opc_mdl")) ;  
							opcion.setNom(rs.getString("opc_nom")) ;  
							opcion.setUrl(rs.getString("opc_url")) ;  
							//opcion.setNiv(rs.getString("opc_niv")) ;  
							perfilOpcion.setOpcion(opcion);
					}
							return perfilOpcion;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public PerfilOpcion getByParams(Param param) {

		String sql = "select * from seg_perfil_opcion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerfilOpcion>() {
			
			public PerfilOpcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<PerfilOpcion> listByParams(Param param, String[] order) {

		String sql = "select * from seg_perfil_opcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerfilOpcion>() {

			
			public PerfilOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<PerfilOpcion> listFullByParams(PerfilOpcion perfilOpcion, String[] order) {
	
		return listFullByParams(Param.toParam("pop",perfilOpcion), order);
	
	}	
	
	
	public List<PerfilOpcion> listFullByParams(Param param, String[] order) {

		String sql = "select pop.id pop_id, pop.id_per pop_id_per , pop.id_opc pop_id_opc  ,pop.est pop_est ";
		sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + ", opc.id opc_id  , opc.mdl opc_mdl , opc.nom opc_nom , opc.url opc_url , opc.niv opc_niv  ";
		sql = sql + " from seg_perfil_opcion pop";
		sql = sql + " left join seg_perfil per on per.id = pop.id_per ";
		sql = sql + " left join seg_opcion opc on opc.id = pop.id_opc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerfilOpcion>() {

			
			public PerfilOpcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerfilOpcion perfilOpcion= rsToEntity(rs,"pop_");
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;  
				perfil.setNom(rs.getString("per_nom")) ;  
				perfil.setDes(rs.getString("per_des")) ;  
				perfilOpcion.setPerfil(perfil);
				Opcion opcion = new Opcion();  
				opcion.setId(rs.getInt("opc_id")) ;  
				//opcion.setMdl(rs.getString("opc_mdl")) ;  
				opcion.setNom(rs.getString("opc_nom")) ;  
				opcion.setUrl(rs.getString("opc_url")) ;  
				//opcion.setNiv(rs.getString("opc_niv")) ;  
				perfilOpcion.setOpcion(opcion);
				return perfilOpcion;
			}

		});

	}	




	// funciones privadas utilitarias para PerfilOpcion

	private PerfilOpcion rsToEntity(ResultSet rs,String alias) throws SQLException {
		PerfilOpcion perfil_opcion = new PerfilOpcion();

		perfil_opcion.setId(rs.getInt( alias + "id"));
		perfil_opcion.setId_per(rs.getInt( alias + "id_per"));
		perfil_opcion.setId_opc(rs.getInt( alias + "id_opc"));
		perfil_opcion.setEst(rs.getString( alias + "est"));
								
		return perfil_opcion;

	}
	
}
