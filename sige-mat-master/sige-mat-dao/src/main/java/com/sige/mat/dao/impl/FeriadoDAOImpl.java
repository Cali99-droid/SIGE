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
import com.tesla.colegio.model.Feriado;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface FeriadoDAO.
 * @author MV
 *
 */
public class FeriadoDAOImpl{
	final static Logger logger = Logger.getLogger(FeriadoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Feriado feriado) {
		if (feriado.getId() != null) {
			// update
			String sql = "UPDATE aca_feriado "
						+ "SET nom=?, "
						+ "dia=?, "
						+ "motivo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						feriado.getNom(),
						feriado.getDia(),
						feriado.getMotivo(),
						feriado.getEst(),
						feriado.getUsr_act(),
						new java.util.Date(),
						feriado.getId()); 
			return feriado.getId();

		} else {
			// insert
			String sql = "insert into aca_feriado ("
						+ "nom, "
						+ "dia, "
						+ "motivo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				feriado.getNom(),
				feriado.getDia(),
				feriado.getMotivo(),
				feriado.getEst(),
				feriado.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_feriado where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Feriado> list() {
		String sql = "select * from aca_feriado";
		
		//logger.info(sql);
		
		List<Feriado> listFeriado = jdbcTemplate.query(sql, new RowMapper<Feriado>() {

			@Override
			public Feriado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listFeriado;
	}

	public Feriado get(int id) {
		String sql = "select * from aca_feriado WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Feriado>() {

			@Override
			public Feriado extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Feriado getFull(int id, String tablas[]) {
		String sql = "select afe.id afe_id, afe.nom afe_nom , afe.dia afe_dia , afe.motivo afe_motivo  ,afe.est afe_est ";
	
		sql = sql + " from aca_feriado afe "; 
		sql = sql + " where afe.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Feriado>() {
		
			@Override
			public Feriado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Feriado feriado= rsToEntity(rs,"afe_");
							return feriado;
				}
				
				return null;
			}
			
		});


	}		
	
	public Feriado getByParams(Param param) {

		String sql = "select * from aca_feriado " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Feriado>() {
			@Override
			public Feriado extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Feriado> listByParams(Param param, String[] order) {

		String sql = "select * from aca_feriado " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Feriado>() {

			@Override
			public Feriado mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Feriado> listFullByParams(Feriado feriado, String[] order) {
	
		return listFullByParams(Param.toParam("afe",feriado), order);
	
	}	
	
	public List<Feriado> listFullByParams(Param param, String[] order) {

		String sql = "select afe.id afe_id, afe.nom afe_nom , afe.dia afe_dia , afe.motivo afe_motivo  ,afe.est afe_est ";
		sql = sql + " from aca_feriado afe";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Feriado>() {

			@Override
			public Feriado mapRow(ResultSet rs, int rowNum) throws SQLException {
				Feriado feriado= rsToEntity(rs,"afe_");
				return feriado;
			}

		});

	}	




	// funciones privadas utilitarias para Feriado

	private Feriado rsToEntity(ResultSet rs,String alias) throws SQLException {
		Feriado feriado = new Feriado();

		feriado.setId(rs.getInt( alias + "id"));
		feriado.setNom(rs.getString( alias + "nom"));
		feriado.setDia(rs.getDate( alias + "dia"));
		feriado.setMotivo(rs.getString( alias + "motivo"));
		feriado.setEst(rs.getString( alias + "est"));
								
		return feriado;

	}
	
}
