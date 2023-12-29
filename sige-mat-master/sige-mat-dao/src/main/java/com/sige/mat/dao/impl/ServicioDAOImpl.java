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
import com.tesla.colegio.model.Servicio;

import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Periodo;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ServicioDAO.
 * @author MV
 *
 */
public class ServicioDAOImpl{
	final static Logger logger = Logger.getLogger(ServicioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Servicio servicio) {
		if (servicio.getId() != null) {
			// update
			String sql = "UPDATE ges_servicio "
						+ "SET id_suc=?, "
						+ "id_niv=?, "
						+ "id_gir=?, "
						+ "des=?, "
						+ "nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						servicio.getId_suc(),
						servicio.getId_niv(),
						servicio.getId_gir(),
						servicio.getDes(),
						servicio.getNom(),
						servicio.getEst(),
						servicio.getUsr_act(),
						new java.util.Date(),
						servicio.getId()); 

		} else {
			// insert
			String sql = "insert into ges_servicio ("
						+ "id_suc, "
						+ "id_niv, "
						+ "id_gir, "
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				servicio.getId_suc(),
				servicio.getId_niv(),
				servicio.getId_gir(),
				servicio.getNom(),
				servicio.getDes(),
				servicio.getEst(),
				servicio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_servicio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Servicio> list() {
		String sql = "select * from ges_servicio";
		
		//logger.info(sql);
		
		List<Servicio> listServicio = jdbcTemplate.query(sql, new RowMapper<Servicio>() {

			
			public Servicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listServicio;
	}

	
	public Servicio get(int id) {
		String sql = "select * from ges_servicio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Servicio>() {

			
			public Servicio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Servicio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select srv.id srv_id, srv.id_suc srv_id_suc , srv.nom srv_nom  ,srv.est srv_est ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from ges_servicio srv "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";
		sql = sql + " where srv.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Servicio>() {
		
			
			public Servicio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Servicio servicio= rsToEntity(rs,"srv_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							servicio.setSucursal(sucursal);
					}
							return servicio;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Servicio getByParams(Param param) {

		String sql = "select * from ges_servicio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Servicio>() {
			
			public Servicio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Servicio> listByParams(Param param, String[] order) {

		String sql = "select * from ges_servicio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Servicio>() {

			
			public Servicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Servicio> listFullByParams(Servicio servicio, String[] order) {
	
		return listFullByParams(Param.toParam("srv",servicio), order);
	
	}	
	
	
	public List<Servicio> listFullByParams(Param param, String[] order) {

		String sql = "select srv.id srv_id, srv.id_suc srv_id_suc, srv.id_gir srv_id_gir , srv.nom srv_nom, srv.des srv_des,srv.est srv_est, srv.id_niv srv_id_niv ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + " from ges_servicio srv";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Servicio>() {

			
			public Servicio mapRow(ResultSet rs, int rowNum) throws SQLException {
				Servicio servicio= rsToEntity(rs,"srv_");
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				servicio.setSucursal(sucursal);
				return servicio;
			}

		});

	}	


	public List<Periodo> getListPeriodo(Param param, String[] order) {
		String sql = "select * from per_periodo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Periodo periodo = new Periodo();

				periodo.setId(rs.getInt("id"));
				periodo.setId_anio(rs.getInt("id_anio"));
				periodo.setId_srv(rs.getInt("id_srv"));
				periodo.setId_tpe(rs.getInt("id_tpe"));
				periodo.setFec_ini(rs.getDate("fec_ini"));
				periodo.setFec_fin(rs.getDate("fec_fin"));
				periodo.setFec_cie_mat(rs.getDate("fec_cie_mat"));
				periodo.setEst(rs.getString("est"));
												
				return periodo;
			}

		});	
	}


	// funciones privadas utilitarias para Servicio

	private Servicio rsToEntity(ResultSet rs,String alias) throws SQLException {
		Servicio servicio = new Servicio();

		servicio.setId(rs.getInt( alias + "id"));
		servicio.setId_suc(rs.getInt( alias + "id_suc"));
		servicio.setId_niv(rs.getInt(alias + "id_niv"));
		servicio.setId_gir(rs.getInt(alias + "id_gir"));
		servicio.setNom(rs.getString( alias + "nom"));
		servicio.setDes(rs.getString( alias + "des"));
		servicio.setEst(rs.getString( alias + "est"));
								
		return servicio;

	}
	
}
