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
import com.tesla.colegio.model.Emisor;

import com.tesla.colegio.model.Perfil;
import com.tesla.colegio.model.Mensaje;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EmisorDAO.
 * @author MV
 *
 */
public class EmisorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Emisor emisor) {
		if (emisor.getId() != null) {
			// update
			String sql = "UPDATE msj_emisor "
						+ "SET id_usr=?, "
						+ "id_per=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						emisor.getId_usr(),
						emisor.getId_per(),
						emisor.getEst(),
						emisor.getUsr_act(),
						new java.util.Date(),
						emisor.getId()); 
			return emisor.getId();

		} else {
			// insert
			String sql = "insert into msj_emisor ("
						+ "id_usr, "
						+ "id_per, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				emisor.getId_usr(),
				emisor.getId_per(),
				emisor.getEst(),
				emisor.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_emisor where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Emisor> list() {
		String sql = "select * from msj_emisor";
		
		//System.out.println(sql);
		
		List<Emisor> listEmisor = jdbcTemplate.query(sql, new RowMapper<Emisor>() {

			@Override
			public Emisor mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEmisor;
	}

	public Emisor get(int id) {
		String sql = "select * from msj_emisor WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Emisor>() {

			@Override
			public Emisor extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Emisor getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select emi.id emi_id, emi.id_usr emi_id_usr , emi.id_per emi_id_per  ,emi.est emi_est ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
	
		sql = sql + " from msj_emisor emi "; 
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = emi.id_per ";
		sql = sql + " where emi.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Emisor>() {
		
			@Override
			public Emisor extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Emisor emisor= rsToEntity(rs,"emi_");
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							emisor.setPerfil(perfil);
					}
							return emisor;
				}
				
				return null;
			}
			
		});


	}		
	
	public Emisor getByParams(Param param) {

		String sql = "select * from msj_emisor " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Emisor>() {
			@Override
			public Emisor extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Emisor> listByParams(Param param, String[] order) {

		String sql = "select * from msj_emisor " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Emisor>() {

			@Override
			public Emisor mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Emisor> listFullByParams(Emisor emisor, String[] order) {
	
		return listFullByParams(Param.toParam("emi",emisor), order);
	
	}	
	
	public List<Emisor> listFullByParams(Param param, String[] order) {

		String sql = "select emi.id emi_id, emi.id_usr emi_id_usr , emi.id_per emi_id_per  ,emi.est emi_est ";
		sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + " from msj_emisor emi";
		sql = sql + " left join seg_perfil per on per.id = emi.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Emisor>() {

			@Override
			public Emisor mapRow(ResultSet rs, int rowNum) throws SQLException {
				Emisor emisor= rsToEntity(rs,"emi_");
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;  
				perfil.setNom(rs.getString("per_nom")) ;  
				perfil.setDes(rs.getString("per_des")) ;  
				emisor.setPerfil(perfil);
				return emisor;
			}

		});

	}	


	public List<Mensaje> getListMensaje(Param param, String[] order) {
		String sql = "select * from msj_mensaje " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Mensaje>() {

			@Override
			public Mensaje mapRow(ResultSet rs, int rowNum) throws SQLException {
				Mensaje mensaje = new Mensaje();

				mensaje.setId(rs.getInt("id"));
				mensaje.setId_emi(rs.getInt("id_emi"));
				mensaje.setId_adj(rs.getInt("id_adj"));
				mensaje.setDes(rs.getString("des"));
				mensaje.setFec_envio(rs.getDate("fec_envio"));
				mensaje.setEst(rs.getString("est"));
												
				return mensaje;
			}

		});	
	}


	// funciones privadas utilitarias para Emisor

	private Emisor rsToEntity(ResultSet rs,String alias) throws SQLException {
		Emisor emisor = new Emisor();

		emisor.setId(rs.getInt( alias + "id"));
		emisor.setId_usr(rs.getInt( alias + "id_usr"));
		emisor.setId_per(rs.getInt( alias + "id_per"));
		emisor.setEst(rs.getString( alias + "est"));
								
		return emisor;

	}
	
}
