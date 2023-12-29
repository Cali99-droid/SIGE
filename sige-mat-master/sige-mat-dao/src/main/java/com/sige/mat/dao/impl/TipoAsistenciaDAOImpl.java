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
import com.tesla.colegio.model.TipoAsistencia;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TipoAsistenciaDAO.
 * @author MV
 *
 */
public class TipoAsistenciaDAOImpl{
	final static Logger logger = Logger.getLogger(TipoAsistenciaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TipoAsistencia tipo_asistencia) {
		if (tipo_asistencia.getId() > 0) {
			// update
			String sql = "UPDATE asi_tipo_asistencia "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						tipo_asistencia.getNom(),
						tipo_asistencia.getDes(),
						tipo_asistencia.getEst(),
						tipo_asistencia.getUsr_act(),
						new java.util.Date(),
						tipo_asistencia.getId()); 

		} else {
			// insert
			String sql = "insert into asi_tipo_asistencia ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tipo_asistencia.getNom(),
				tipo_asistencia.getDes(),
				tipo_asistencia.getEst(),
				tipo_asistencia.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from asi_tipo_asistencia where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<TipoAsistencia> list() {
		String sql = "select * from asi_tipo_asistencia";
		
		//logger.info(sql);
		
		List<TipoAsistencia> listTipoAsistencia = jdbcTemplate.query(sql, new RowMapper<TipoAsistencia>() {

			
			public TipoAsistencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTipoAsistencia;
	}

	
	public TipoAsistencia get(int id) {
		String sql = "select * from asi_tipo_asistencia WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoAsistencia>() {

			
			public TipoAsistencia extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public TipoAsistencia getFull(int id, String tablas[]) {
		String sql = "select tas.id tas_id, tas.nom tas_nom , tas.des tas_des  ,tas.est tas_est ";
	
		sql = sql + " from asi_tipo_asistencia tas "; 
		sql = sql + " where tas.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoAsistencia>() {
		
			
			public TipoAsistencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TipoAsistencia tipoAsistencia= rsToEntity(rs,"tas_");
							return tipoAsistencia;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public TipoAsistencia getByParams(Param param) {

		String sql = "select * from asi_tipo_asistencia " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TipoAsistencia>() {
			
			public TipoAsistencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<TipoAsistencia> listByParams(Param param, String[] order) {

		String sql = "select * from asi_tipo_asistencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoAsistencia>() {

			
			public TipoAsistencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<TipoAsistencia> listFullByParams(TipoAsistencia tipoAsistencia, String[] order) {
	
		return listFullByParams(Param.toParam("tas",tipoAsistencia), order);
	
	}	
	
	
	public List<TipoAsistencia> listFullByParams(Param param, String[] order) {

		String sql = "select tas.id tas_id, tas.nom tas_nom , tas.des tas_des  ,tas.est tas_est ";
		sql = sql + " from asi_tipo_asistencia tas";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TipoAsistencia>() {

			
			public TipoAsistencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				TipoAsistencia tipoAsistencia= rsToEntity(rs,"tas_");
				return tipoAsistencia;
			}

		});

	}	




	// funciones privadas utilitarias para TipoAsistencia

	private TipoAsistencia rsToEntity(ResultSet rs,String alias) throws SQLException {
		TipoAsistencia tipo_asistencia = new TipoAsistencia();

		tipo_asistencia.setId(rs.getInt( alias + "id"));
		tipo_asistencia.setNom(rs.getString( alias + "nom"));
		tipo_asistencia.setDes(rs.getString( alias + "des"));
		tipo_asistencia.setEst(rs.getString( alias + "est"));
								
		return tipo_asistencia;

	}
	
}
