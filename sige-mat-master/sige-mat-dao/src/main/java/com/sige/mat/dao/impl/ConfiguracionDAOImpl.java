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
import com.tesla.colegio.model.Configuracion;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.ConfTipo;
import com.tesla.colegio.model.EvaResultado;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfiguracionDAO.
 * @author MV
 *
 */
public class ConfiguracionDAOImpl{
	final static Logger logger = Logger.getLogger(ConfiguracionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Configuracion configuracion) {
		if (configuracion.getId() != null) {
			// update
			String sql = "UPDATE eco_configuracion "
						+ "SET id_per=?, "
						+ "condicion=?, "
						+ "tip_resp=?, "
						+ "id_ect=?, "
						+ "valor1=?, "
						+ "valor2=?, "
						+ "ptj=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						configuracion.getId_per(),
						configuracion.getCondicion(),
						configuracion.getTip_resp(),
						configuracion.getId_ect(),
						configuracion.getValor1(),
						configuracion.getValor2(),
						configuracion.getPtj(),
						configuracion.getEst(),
						configuracion.getUsr_act(),
						new java.util.Date(),
						configuracion.getId()); 
			return configuracion.getId();

		} else {
			// insert
			String sql = "insert into eco_configuracion ("
						+ "id_per, "
						+ "condicion, "
						+ "tip_resp, "
						+ "id_ect, "
						+ "valor1, "
						+ "valor2, "
						+ "ptj, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				configuracion.getId_per(),
				configuracion.getCondicion(),
				configuracion.getTip_resp(),
				configuracion.getId_ect(),
				configuracion.getValor1(),
				configuracion.getValor2(),
				configuracion.getPtj(),
				configuracion.getEst(),
				configuracion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from eco_configuracion where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Configuracion> list() {
		String sql = "select * from eco_configuracion";
		
		//logger.info(sql);
		
		List<Configuracion> listConfiguracion = jdbcTemplate.query(sql, new RowMapper<Configuracion>() {

			@Override
			public Configuracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfiguracion;
	}

	public Configuracion get(int id) {
		String sql = "select * from eco_configuracion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Configuracion>() {

			@Override
			public Configuracion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Configuracion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ecc.id ecc_id, ecc.id_per ecc_id_per , ecc.condicion ecc_condicion , ecc.tip_resp ecc_tip_resp , ecc.id_ect ecc_id_ect , ecc.valor1 ecc_valor1 , ecc.valor2 ecc_valor2 , ecc.ptj ecc_ptj  ,ecc.est ecc_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		if (aTablas.contains("eco_conf_tipo"))
			sql = sql + ", ect.id ect_id  , ect.tip ect_tip , ect.nom ect_nom  ";
	
		sql = sql + " from eco_configuracion ecc "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = ecc.id_per ";
		if (aTablas.contains("eco_conf_tipo"))
			sql = sql + " left join eco_conf_tipo ect on ect.id = ecc.id_ect ";
		sql = sql + " where ecc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Configuracion>() {
		
			@Override
			public Configuracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Configuracion configuracion= rsToEntity(rs,"ecc_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							configuracion.setPeriodo(periodo);
					}
					if (aTablas.contains("eco_conf_tipo")){
						ConfTipo conftipo = new ConfTipo();  
							conftipo.setId(rs.getInt("ect_id")) ;  
							conftipo.setTip(rs.getString("ect_tip")) ;  
							conftipo.setNom(rs.getString("ect_nom")) ;  
							configuracion.setConfTipo(conftipo);
					}
							return configuracion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Configuracion getByParams(Param param) {

		String sql = "select * from eco_configuracion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Configuracion>() {
			@Override
			public Configuracion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Configuracion> listByParams(Param param, String[] order) {

		String sql = "select * from eco_configuracion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Configuracion>() {

			@Override
			public Configuracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Configuracion> listFullByParams(Configuracion configuracion, String[] order) {
	
		return listFullByParams(Param.toParam("ecc",configuracion), order);
	
	}	
	
	public List<Configuracion> listFullByParams(Param param, String[] order) {

		String sql = "select ecc.id ecc_id, ecc.id_per ecc_id_per , ecc.condicion ecc_condicion , ecc.tip_resp ecc_tip_resp , ecc.id_ect ecc_id_ect , ecc.valor1 ecc_valor1 , ecc.valor2 ecc_valor2 , ecc.ptj ecc_ptj  ,ecc.est ecc_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", ect.id ect_id  , ect.tip ect_tip , ect.nom ect_nom  ";
		sql = sql + " from eco_configuracion ecc";
		sql = sql + " left join per_periodo pee on pee.id = ecc.id_per ";
		sql = sql + " left join eco_conf_tipo ect on ect.id = ecc.id_ect ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Configuracion>() {

			@Override
			public Configuracion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Configuracion configuracion= rsToEntity(rs,"ecc_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				configuracion.setPeriodo(periodo);
				ConfTipo conftipo = new ConfTipo();  
				conftipo.setId(rs.getInt("ect_id")) ;  
				conftipo.setTip(rs.getString("ect_tip")) ;  
				conftipo.setNom(rs.getString("ect_nom")) ;  
				configuracion.setConfTipo(conftipo);
				return configuracion;
			}

		});

	}	


	public List<EvaResultado> getListEvaResultado(Param param, String[] order) {
		String sql = "select * from eco_eva_resultado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EvaResultado>() {

			@Override
			public EvaResultado mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaResultado eva_resultado = new EvaResultado();

				eva_resultado.setId(rs.getInt("id"));
				eva_resultado.setId_eve(rs.getInt("id_eve"));
				eva_resultado.setId_ecc(rs.getInt("id_ecc"));
				eva_resultado.setVal_resp(rs.getString("val_resp"));
				eva_resultado.setEst(rs.getString("est"));
												
				return eva_resultado;
			}

		});	
	}


	// funciones privadas utilitarias para Configuracion

	private Configuracion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Configuracion configuracion = new Configuracion();

		configuracion.setId(rs.getInt( alias + "id"));
		configuracion.setId_per(rs.getInt( alias + "id_per"));
		configuracion.setCondicion(rs.getString( alias + "condicion"));
		configuracion.setTip_resp(rs.getString( alias + "tip_resp"));
		configuracion.setId_ect(rs.getInt( alias + "id_ect"));
		configuracion.setValor1(rs.getBigDecimal( alias + "valor1"));
		configuracion.setValor2(rs.getBigDecimal( alias + "valor2"));
		configuracion.setPtj(rs.getBigDecimal( alias + "ptj"));
		configuracion.setEst(rs.getString( alias + "est"));
								
		return configuracion;

	}
	
}
