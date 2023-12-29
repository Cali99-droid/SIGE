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
import com.tesla.colegio.model.Mensaje;

import com.tesla.colegio.model.Emisor;
import com.tesla.colegio.model.Adjunto;
import com.tesla.colegio.model.Receptor;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MensajeDAO.
 * @author MV
 *
 */
public class MensajeDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Mensaje mensaje) {
		if (mensaje.getId() != null) {
			// update
			String sql = "UPDATE msj_mensaje "
						+ "SET id_emi=?, "
						+ "id_adj=?, "
						+ "asu=?, "
						+ "des=?, "
						+ "fec_envio=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						mensaje.getId_emi(),
						mensaje.getId_adj(),
						mensaje.getAsu(),
						mensaje.getDes(),
						mensaje.getFec_envio(),
						mensaje.getEst(),
						mensaje.getUsr_act(),
						new java.util.Date(),
						mensaje.getId()); 
			return mensaje.getId();

		} else {
			// insert
			String sql = "insert into msj_mensaje ("
						+ "id_emi, "
						+ "id_adj, "
						+ "asu, "
						+ "des, "
						+ "fec_envio, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				mensaje.getId_emi(),
				mensaje.getId_adj(),
				mensaje.getAsu(),
				mensaje.getDes(),
				mensaje.getFec_envio(),
				mensaje.getEst(),
				mensaje.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_mensaje where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Mensaje> list() {
		String sql = "select * from msj_mensaje";
		
		//System.out.println(sql);
		
		List<Mensaje> listMensaje = jdbcTemplate.query(sql, new RowMapper<Mensaje>() {

			@Override
			public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMensaje;
	}

	public Mensaje get(int id) {
		String sql = "select * from msj_mensaje WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Mensaje>() {

			@Override
			public Mensaje extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Mensaje getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select msj.id msj_id, msj.id_emi msj_id_emi , msj.id_adj msj_id_adj , msj.asu msj_asu , msj.des msj_des , msj.fec_envio msj_fec_envio  ,msj.est msj_est ";
		if (aTablas.contains("msj_emisor"))
			sql = sql + ", emi.id emi_id  , emi.id_usr emi_id_usr , emi.id_per emi_id_per  ";
		if (aTablas.contains("msj_adjunto"))
			sql = sql + ", adj.id adj_id  , adj.archivo adj_archivo  ";
	
		sql = sql + " from msj_mensaje msj "; 
		if (aTablas.contains("msj_emisor"))
			sql = sql + " left join msj_emisor emi on emi.id = msj.id_emi ";
		if (aTablas.contains("msj_adjunto"))
			sql = sql + " left join msj_adjunto adj on adj.id = msj.id_adj ";
		sql = sql + " where msj.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Mensaje>() {
		
			@Override
			public Mensaje extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Mensaje mensaje= rsToEntity(rs,"msj_");
					if (aTablas.contains("msj_emisor")){
						Emisor emisor = new Emisor();  
							emisor.setId(rs.getInt("emi_id")) ;  
							emisor.setId_usr(rs.getInt("emi_id_usr")) ;  
							emisor.setId_per(rs.getInt("emi_id_per")) ;  
							mensaje.setEmisor(emisor);
					}
					if (aTablas.contains("msj_adjunto")){
						Adjunto adjunto = new Adjunto();  
							adjunto.setId(rs.getInt("adj_id")) ;  
							adjunto.setArchivo(rs.getString("adj_archivo")) ;  
							mensaje.setAdjunto(adjunto);
					}
							return mensaje;
				}
				
				return null;
			}
			
		});


	}		
	
	public Mensaje getByParams(Param param) {

		String sql = "select * from msj_mensaje " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Mensaje>() {
			@Override
			public Mensaje extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Mensaje> listByParams(Param param, String[] order) {

		String sql = "select * from msj_mensaje " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Mensaje>() {

			@Override
			public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Mensaje> listFullByParams(Mensaje mensaje, String[] order) {
	
		return listFullByParams(Param.toParam("msj",mensaje), order);
	
	}	
	
	public List<Mensaje> listFullByParams(Param param, String[] order) {

		String sql = "select msj.id msj_id, msj.id_emi msj_id_emi , msj.id_adj msj_id_adj , msj.asu msj_asu , msj.des msj_des , msj.fec_envio msj_fec_envio  ,msj.est msj_est ";
		sql = sql + ", emi.id emi_id  , emi.id_usr emi_id_usr , emi.id_per emi_id_per  ";
		sql = sql + ", adj.id adj_id  , adj.archivo adj_archivo  ";
		sql = sql + " from msj_mensaje msj";
		sql = sql + " left join msj_emisor emi on emi.id = msj.id_emi ";
		sql = sql + " left join msj_adjunto adj on adj.id = msj.id_adj ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Mensaje>() {

			@Override
			public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
				Mensaje mensaje= rsToEntity(rs,"msj_");
				Emisor emisor = new Emisor();  
				emisor.setId(rs.getInt("emi_id")) ;  
				emisor.setId_usr(rs.getInt("emi_id_usr")) ;  
				emisor.setId_per(rs.getInt("emi_id_per")) ;  
				mensaje.setEmisor(emisor);
				Adjunto adjunto = new Adjunto();  
				adjunto.setId(rs.getInt("adj_id")) ;  
				adjunto.setArchivo(rs.getString("adj_archivo")) ;  
				mensaje.setAdjunto(adjunto);
				return mensaje;
			}

		});

	}	


	public List<Receptor> getListReceptor(Param param, String[] order) {
		String sql = "select * from msj_receptor " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Receptor>() {

			@Override
			public Receptor mapRow(ResultSet rs, int rowNum) throws SQLException {
				Receptor receptor = new Receptor();

				receptor.setId(rs.getInt("id"));
				receptor.setId_usr(rs.getInt("id_usr"));
				receptor.setId_per(rs.getInt("id_per"));
				receptor.setId_msj(rs.getInt("id_msj"));
				receptor.setId_est(rs.getInt("id_est"));
				receptor.setEst(rs.getString("est"));
												
				return receptor;
			}

		});	
	}


	// funciones privadas utilitarias para Mensaje

	private Mensaje rsToEntity(ResultSet rs,String alias) throws SQLException {
		Mensaje mensaje = new Mensaje();

		mensaje.setId(rs.getInt( alias + "id"));
		mensaje.setId_emi(rs.getInt( alias + "id_emi"));
		mensaje.setId_adj(rs.getInt( alias + "id_adj"));
		mensaje.setAsu(rs.getString( alias + "asu"));
		mensaje.setDes(rs.getString( alias + "des"));
		mensaje.setFec_envio(rs.getDate( alias + "fec_envio"));
		mensaje.setEst(rs.getString( alias + "est"));
								
		return mensaje;

	}
	
}
