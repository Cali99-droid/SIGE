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
import com.tesla.colegio.model.Receptor;

import com.tesla.colegio.model.Perfil;
import com.tesla.colegio.model.Mensaje;
import com.tesla.colegio.model.EstadoMensajeria;
import com.tesla.colegio.model.Historial;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ReceptorDAO.
 * @author MV
 *
 */
public class ReceptorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Receptor receptor) {
		if (receptor.getId() != null) {
			// update
			String sql = "UPDATE msj_receptor "
						+ "SET id_usr=?, "
						+ "id_per=?, "
						+ "id_msj=?, "
						+ "id_est=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						receptor.getId_usr(),
						receptor.getId_per(),
						receptor.getId_msj(),
						receptor.getId_est(),
						receptor.getEst(),
						receptor.getUsr_act(),
						new java.util.Date(),
						receptor.getId()); 
			return receptor.getId();

		} else {
			// insert
			String sql = "insert into msj_receptor ("
						+ "id_usr, "
						+ "id_per, "
						+ "id_msj, "
						+ "id_est, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				receptor.getId_usr(),
				receptor.getId_per(),
				receptor.getId_msj(),
				receptor.getId_est(),
				receptor.getEst(),
				receptor.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_receptor where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Receptor> list() {
		String sql = "select * from msj_receptor";
		
		//System.out.println(sql);
		
		List<Receptor> listReceptor = jdbcTemplate.query(sql, new RowMapper<Receptor>() {

			@Override
			public Receptor mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listReceptor;
	}

	public Receptor get(int id) {
		String sql = "select * from msj_receptor WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Receptor>() {

			@Override
			public Receptor extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Receptor getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rec.id rec_id, rec.id_usr rec_id_usr , rec.id_per rec_id_per , rec.id_msj rec_id_msj , rec.id_est rec_id_est  ,rec.est rec_est ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		if (aTablas.contains("msj_mensaje"))
			sql = sql + ", msj.id msj_id  , msj.id_emi msj_id_emi , msj.id_adj msj_id_adj , msj.des msj_des , msj.fec_envio msj_fec_envio  ";
		if (aTablas.contains("msj_estado_mensajeria"))
			sql = sql + ", est.id est_id  , est.des est_des , est.cod est_cod  ";
	
		sql = sql + " from msj_receptor rec "; 
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = rec.id_per ";
		if (aTablas.contains("msj_mensaje"))
			sql = sql + " left join msj_mensaje msj on msj.id = rec.id_msj ";
		if (aTablas.contains("msj_estado_mensajeria"))
			sql = sql + " left join msj_estado_mensajeria est on est.id = rec.id_est ";
		sql = sql + " where rec.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Receptor>() {
		
			@Override
			public Receptor extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Receptor receptor= rsToEntity(rs,"rec_");
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							receptor.setPerfil(perfil);
					}
					if (aTablas.contains("msj_mensaje")){
						Mensaje mensaje = new Mensaje();  
							mensaje.setId(rs.getInt("msj_id")) ;  
							mensaje.setId_emi(rs.getInt("msj_id_emi")) ;  
							mensaje.setId_adj(rs.getInt("msj_id_adj")) ;  
							mensaje.setDes(rs.getString("msj_des")) ;  
							mensaje.setFec_envio(rs.getDate("msj_fec_envio")) ;  
							receptor.setMensaje(mensaje);
					}
					if (aTablas.contains("msj_estado_mensajeria")){
						EstadoMensajeria estadomensajeria = new EstadoMensajeria();  
							estadomensajeria.setId(rs.getInt("est_id")) ;  
							estadomensajeria.setDes(rs.getString("est_des")) ;  
							estadomensajeria.setCod(rs.getString("est_cod")) ;  
							receptor.setEstadoMensajeria(estadomensajeria);
					}
							return receptor;
				}
				
				return null;
			}
			
		});


	}		
	
	public Receptor getByParams(Param param) {

		String sql = "select * from msj_receptor " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Receptor>() {
			@Override
			public Receptor extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Receptor> listByParams(Param param, String[] order) {

		String sql = "select * from msj_receptor " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Receptor>() {

			@Override
			public Receptor mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Receptor> listFullByParams(Receptor receptor, String[] order) {
	
		return listFullByParams(Param.toParam("rec",receptor), order);
	
	}	
	
	public List<Receptor> listFullByParams(Param param, String[] order) {

		String sql = "select rec.id rec_id, rec.id_usr rec_id_usr , rec.id_per rec_id_per , rec.id_msj rec_id_msj , rec.id_est rec_id_est  ,rec.est rec_est ";
		sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + ", msj.id msj_id  , msj.id_emi msj_id_emi , msj.id_adj msj_id_adj , msj.des msj_des , msj.fec_envio msj_fec_envio  ";
		sql = sql + ", est.id est_id  , est.des est_des , est.cod est_cod  ";
		sql = sql + " from msj_receptor rec";
		sql = sql + " left join seg_perfil per on per.id = rec.id_per ";
		sql = sql + " left join msj_mensaje msj on msj.id = rec.id_msj ";
		sql = sql + " left join msj_estado_mensajeria est on est.id = rec.id_est ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Receptor>() {

			@Override
			public Receptor mapRow(ResultSet rs, int rowNum) throws SQLException {
				Receptor receptor= rsToEntity(rs,"rec_");
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;  
				perfil.setNom(rs.getString("per_nom")) ;  
				perfil.setDes(rs.getString("per_des")) ;  
				receptor.setPerfil(perfil);
				Mensaje mensaje = new Mensaje();  
				mensaje.setId(rs.getInt("msj_id")) ;  
				mensaje.setId_emi(rs.getInt("msj_id_emi")) ;  
				mensaje.setId_adj(rs.getInt("msj_id_adj")) ;  
				mensaje.setDes(rs.getString("msj_des")) ;  
				mensaje.setFec_envio(rs.getDate("msj_fec_envio")) ;  
				receptor.setMensaje(mensaje);
				EstadoMensajeria estadomensajeria = new EstadoMensajeria();  
				estadomensajeria.setId(rs.getInt("est_id")) ;  
				estadomensajeria.setDes(rs.getString("est_des")) ;  
				estadomensajeria.setCod(rs.getString("est_cod")) ;  
				receptor.setEstadoMensajeria(estadomensajeria);
				return receptor;
			}

		});

	}	


	public List<Historial> getListHistorial(Param param, String[] order) {
		String sql = "select * from msj_historial " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Historial>() {

			@Override
			public Historial mapRow(ResultSet rs, int rowNum) throws SQLException {
				Historial historial = new Historial();

				historial.setId(rs.getInt("id"));
				historial.setId_est(rs.getInt("id_est"));
				historial.setId_rec(rs.getInt("id_rec"));
				historial.setEst(rs.getString("est"));
												
				return historial;
			}

		});	
	}


	// funciones privadas utilitarias para Receptor

	private Receptor rsToEntity(ResultSet rs,String alias) throws SQLException {
		Receptor receptor = new Receptor();

		receptor.setId(rs.getInt( alias + "id"));
		receptor.setId_usr(rs.getInt( alias + "id_usr"));
		receptor.setId_per(rs.getInt( alias + "id_per"));
		receptor.setId_msj(rs.getInt( alias + "id_msj"));
		receptor.setId_est(rs.getInt( alias + "id_est"));
		receptor.setEst(rs.getString( alias + "est"));
								
		return receptor;

	}
	
}
