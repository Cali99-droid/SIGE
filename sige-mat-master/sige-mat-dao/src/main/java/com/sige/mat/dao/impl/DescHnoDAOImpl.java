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
import com.tesla.colegio.model.DescHno;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescHnoDAO.
 * @author MV
 *
 */
public class DescHnoDAOImpl{
	final static Logger logger = Logger.getLogger(DescHnoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(DescHno desc_hno) {
		if (desc_hno.getId() != null) {
			// update
			String sql = "UPDATE mat_desc_hno "
						+ "SET id_per=?, "
						+ "monto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						desc_hno.getId_per(),
						desc_hno.getMonto(),
						desc_hno.getEst(),
						desc_hno.getUsr_act(),
						new java.util.Date(),
						desc_hno.getId()); 
			return desc_hno.getId();

		} else {
			// insert
			String sql = "insert into mat_desc_hno ("
						+ "id_per, "
						+ "monto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?,  ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				desc_hno.getId_per(),
				desc_hno.getMonto(),
				desc_hno.getEst(),
				desc_hno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_desc_hno where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescHno> list() {
		String sql = "select * from mat_desc_hno";
		
		//logger.info(sql);
		
		List<DescHno> listDescHno = jdbcTemplate.query(sql, new RowMapper<DescHno>() {

			@Override
			public DescHno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescHno;
	}

	public DescHno get(int id) {
		String sql = "select * from mat_desc_hno WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescHno>() {

			@Override
			public DescHno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescHno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.monto dxh_monto  ,dxh.est dxh_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_desc_hno dxh "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";
		sql = sql + " where dxh.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescHno>() {
		
			@Override
			public DescHno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescHno deschno= rsToEntity(rs,"dxh_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							deschno.setPeriodo(periodo);
					}
							return deschno;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescHno getByParams(Param param) {

		String sql = "select * from mat_desc_hno " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescHno>() {
			@Override
			public DescHno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescHno> listByParams(Param param, String[] order) {

		String sql = "select * from mat_desc_hno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescHno>() {

			@Override
			public DescHno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescHno> listFullByParams(DescHno deschno, String[] order) {
	
		return listFullByParams(Param.toParam("dxh",deschno), order);
	
	}	
	
	public List<DescHno> listFullByParams(Param param, String[] order) {

		String sql = "select dxh.id dxh_id, dxh.id_per dxh_id_per , dxh.monto dxh_monto  ,dxh.est dxh_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", srv.id srv_id  , srv.nom srv_nom";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom";
		sql = sql + " from mat_desc_hno dxh";
		sql = sql + " left join per_periodo pee on pee.id = dxh.id_per ";
		sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescHno>() {

			@Override
			public DescHno mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescHno deschno= rsToEntity(rs,"dxh_");
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id"));
				sucursal.setNom(rs.getString("suc_nom"));
				Servicio servicio = new Servicio();
				servicio.setId(rs.getInt("srv_id"));
				servicio.setNom(rs.getString("srv_nom"));
				servicio.setSucursal(sucursal);
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;
				periodo.setServicio(servicio);
				deschno.setPeriodo(periodo);
				return deschno;
			}

		});

	}	




	// funciones privadas utilitarias para DescHno

	private DescHno rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescHno desc_hno = new DescHno();

		desc_hno.setId(rs.getInt( alias + "id"));
		desc_hno.setId_per(rs.getInt( alias + "id_per"));
		desc_hno.setMonto(rs.getBigDecimal( alias + "monto"));
		desc_hno.setEst(rs.getString( alias + "est"));
								
		return desc_hno;

	}
	
}
