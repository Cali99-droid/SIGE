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
import com.tesla.colegio.model.EvaPadre;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Evaluacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaPadreDAO.
 * @author MV
 *
 */
public class EvaPadreDAOImpl{
	final static Logger logger = Logger.getLogger(EvaPadreDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(EvaPadre eva_padre) {
		if (eva_padre.getId() != null) {
			// update
			String sql = "UPDATE not_eva_padre "
						+ "SET id_tra=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						eva_padre.getId_tra(),
						eva_padre.getEst(),
						eva_padre.getUsr_act(),
						new java.util.Date(),
						eva_padre.getId()); 
			return eva_padre.getId();

		} else {
			// insert
			String sql = "insert into not_eva_padre ("
						+ "id_tra, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				eva_padre.getId_tra(),
				eva_padre.getEst(),
				eva_padre.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_eva_padre where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<EvaPadre> list() {
		String sql = "select * from not_eva_padre";
		
		//logger.info(sql);
		
		List<EvaPadre> listEvaPadre = jdbcTemplate.query(sql, new RowMapper<EvaPadre>() {

			@Override
			public EvaPadre mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaPadre;
	}

	public EvaPadre get(int id) {
		String sql = "select * from not_eva_padre WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaPadre>() {

			@Override
			public EvaPadre extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public EvaPadre getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nep.id nep_id, nep.id_tra nep_id_tra  ,nep.est nep_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from not_eva_padre nep "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = nep.id_tra ";
		sql = sql + " where nep.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaPadre>() {
		
			@Override
			public EvaPadre extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					EvaPadre evapadre= rsToEntity(rs,"nep_");
					if (aTablas.contains("ges_trabajador")){
						Trabajador trabajador = new Trabajador();  
							trabajador.setId(rs.getInt("tra_id")) ;  
							trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
							trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
							trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
							trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
							trabajador.setNom(rs.getString("tra_nom")) ;  
							trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
							trabajador.setGenero(rs.getString("tra_genero")) ;  
							trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
							trabajador.setDir(rs.getString("tra_dir")) ;  
							trabajador.setTel(rs.getString("tra_tel")) ;  
							trabajador.setCel(rs.getString("tra_cel")) ;  
							trabajador.setCorr(rs.getString("tra_corr")) ;  
							trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
							trabajador.setCarrera(rs.getString("tra_carrera")) ;  
							trabajador.setFot(rs.getBytes("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							evapadre.setTrabajador(trabajador);
					}
							return evapadre;
				}
				
				return null;
			}
			
		});


	}		
	
	public EvaPadre getByParams(Param param) {

		String sql = "select * from not_eva_padre " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<EvaPadre>() {
			@Override
			public EvaPadre extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<EvaPadre> listByParams(Param param, String[] order) {

		String sql = "select * from not_eva_padre " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaPadre>() {

			@Override
			public EvaPadre mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<EvaPadre> listFullByParams(EvaPadre evapadre, String[] order) {
	
		return listFullByParams(Param.toParam("nep",evapadre), order);
	
	}	
	
	public List<EvaPadre> listFullByParams(Param param, String[] order) {

		String sql = "select nep.id nep_id, nep.id_tra nep_id_tra  ,nep.est nep_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from not_eva_padre nep";
		sql = sql + " left join ges_trabajador tra on tra.id = nep.id_tra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<EvaPadre>() {

			@Override
			public EvaPadre mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaPadre evapadre= rsToEntity(rs,"nep_");
				Trabajador trabajador = new Trabajador();  
				trabajador.setId(rs.getInt("tra_id")) ;  
				trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
				trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
				trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
				trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
				trabajador.setNom(rs.getString("tra_nom")) ;  
				trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
				trabajador.setGenero(rs.getString("tra_genero")) ;  
				trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
				trabajador.setDir(rs.getString("tra_dir")) ;  
				trabajador.setTel(rs.getString("tra_tel")) ;  
				trabajador.setCel(rs.getString("tra_cel")) ;  
				trabajador.setCorr(rs.getString("tra_corr")) ;  
				trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
				trabajador.setCarrera(rs.getString("tra_carrera")) ;  
				trabajador.setFot(rs.getBytes("tra_fot")) ;  
				trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
				trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
				evapadre.setTrabajador(trabajador);
				return evapadre;
			}

		});

	}	


	public List<Evaluacion> getListEvaluacion(Param param, String[] order) {
		String sql = "select * from not_evaluacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Evaluacion>() {

			@Override
			public Evaluacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Evaluacion evaluacion = new Evaluacion();

				evaluacion.setId(rs.getInt("id"));
				evaluacion.setId_nep(rs.getInt("id_nep"));
				evaluacion.setId_cca(rs.getInt("id_cca"));
				evaluacion.setId_nte(rs.getInt("id_nte"));
				evaluacion.setIns(rs.getString("ins"));
				evaluacion.setEvi(rs.getString("evi"));
				evaluacion.setFec_ini(rs.getDate("fec_ini"));
				evaluacion.setFec_fin(rs.getDate("fec_fin"));
				evaluacion.setEst(rs.getString("est"));
												
				return evaluacion;
			}

		});	
	}


	// funciones privadas utilitarias para EvaPadre

	private EvaPadre rsToEntity(ResultSet rs,String alias) throws SQLException {
		EvaPadre eva_padre = new EvaPadre();

		eva_padre.setId(rs.getInt( alias + "id"));
		eva_padre.setId_tra(rs.getInt( alias + "id_tra"));
		eva_padre.setEst(rs.getString( alias + "est"));
								
		return eva_padre;

	}
	
}
