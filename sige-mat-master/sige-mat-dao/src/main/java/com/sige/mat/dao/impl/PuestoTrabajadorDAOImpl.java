package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
import com.tesla.colegio.model.PuestoTrabajador;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PuestoTrabajadorDAO.
 * @author MV
 *
 */
public class PuestoTrabajadorDAOImpl{
	final static Logger logger = Logger.getLogger(PuestoTrabajadorDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PuestoTrabajador puesto_trabajador) {
		if (puesto_trabajador.getId() > 0) {
			// update
			String sql = "UPDATE ges_puesto_trabajador "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						puesto_trabajador.getNom(),
						puesto_trabajador.getDes(),
						puesto_trabajador.getEst(),
						puesto_trabajador.getUsr_act(),
						new java.util.Date(),
						puesto_trabajador.getId()); 

		} else {
			// insert
			String sql = "insert into ges_puesto_trabajador ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				puesto_trabajador.getNom(),
				puesto_trabajador.getDes(),
				puesto_trabajador.getEst(),
				puesto_trabajador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_puesto_trabajador where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<PuestoTrabajador> list() {
		String sql = "select * from ges_puesto_trabajador";
		
		//logger.info(sql);
		
		List<PuestoTrabajador> listPuestoTrabajador = jdbcTemplate.query(sql, new RowMapper<PuestoTrabajador>() {

			
			public PuestoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPuestoTrabajador;
	}

	
	public PuestoTrabajador get(int id) {
		String sql = "select * from ges_puesto_trabajador WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PuestoTrabajador>() {

			
			public PuestoTrabajador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public PuestoTrabajador getFull(int id, String tablas[]) {
		String sql = "select pue.id pue_id, pue.nom pue_nom , pue.des pue_des  ,pue.est pue_est ";
	
		sql = sql + " from ges_puesto_trabajador pue "; 
		sql = sql + " where pue.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PuestoTrabajador>() {
		
			
			public PuestoTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PuestoTrabajador puestoTrabajador= rsToEntity(rs,"pue_");
							return puestoTrabajador;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public PuestoTrabajador getByParams(Param param) {

		String sql = "select * from ges_puesto_trabajador " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PuestoTrabajador>() {
			
			public PuestoTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<PuestoTrabajador> listByParams(Param param, String[] order) {

		String sql = "select * from ges_puesto_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PuestoTrabajador>() {

			
			public PuestoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<PuestoTrabajador> listFullByParams(PuestoTrabajador puestoTrabajador, String[] order) {
	
		return listFullByParams(Param.toParam("pue",puestoTrabajador), order);
	
	}	
	
	
	public List<PuestoTrabajador> listFullByParams(Param param, String[] order) {

		String sql = "select pue.id pue_id, pue.nom pue_nom , pue.des pue_des  ,pue.est pue_est ";
		sql = sql + " from ges_puesto_trabajador pue";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PuestoTrabajador>() {

			
			public PuestoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				PuestoTrabajador puestoTrabajador= rsToEntity(rs,"pue_");
				return puestoTrabajador;
			}

		});

	}	




	// funciones privadas utilitarias para PuestoTrabajador

	private PuestoTrabajador rsToEntity(ResultSet rs,String alias) throws SQLException {
		PuestoTrabajador puesto_trabajador = new PuestoTrabajador();

		puesto_trabajador.setId(rs.getInt( alias + "id"));
		puesto_trabajador.setNom(rs.getString( alias + "nom"));
		puesto_trabajador.setDes(rs.getString( alias + "des"));
		puesto_trabajador.setEst(rs.getString( alias + "est"));
								
		return puesto_trabajador;

	}
	
}
