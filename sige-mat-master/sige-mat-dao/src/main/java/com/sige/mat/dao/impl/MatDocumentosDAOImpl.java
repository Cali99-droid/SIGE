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
import com.tesla.colegio.model.MatDocumentos;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MatDocumentosDAO.
 * @author MV
 *
 */
public class MatDocumentosDAOImpl{
	final static Logger logger = Logger.getLogger(MatDocumentosDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MatDocumentos mat_documentos) {
		if (mat_documentos.getId() != null) {
			// update
			String sql = "UPDATE cat_mat_documentos "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						mat_documentos.getNom(),
						mat_documentos.getEst(),
						mat_documentos.getUsr_act(),
						new java.util.Date(),
						mat_documentos.getId()); 
			return mat_documentos.getId();

		} else {
			// insert
			String sql = "insert into cat_mat_documentos ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				mat_documentos.getNom(),
				mat_documentos.getEst(),
				mat_documentos.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_mat_documentos where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<MatDocumentos> list() {
		String sql = "select * from cat_mat_documentos";
		
		//logger.info(sql);
		
		List<MatDocumentos> listMatDocumentos = jdbcTemplate.query(sql, new RowMapper<MatDocumentos>() {

			@Override
			public MatDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMatDocumentos;
	}

	public MatDocumentos get(int id) {
		String sql = "select * from cat_mat_documentos WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatDocumentos>() {

			@Override
			public MatDocumentos extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MatDocumentos getFull(int id, String tablas[]) {
		String sql = "select mdo.id mdo_id, mdo.nom mdo_nom  ,mdo.est mdo_est ";
	
		sql = sql + " from cat_mat_documentos mdo "; 
		sql = sql + " where mdo.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MatDocumentos>() {
		
			@Override
			public MatDocumentos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MatDocumentos matdocumentos= rsToEntity(rs,"mdo_");
							return matdocumentos;
				}
				
				return null;
			}
			
		});


	}		
	
	public MatDocumentos getByParams(Param param) {

		String sql = "select * from cat_mat_documentos " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatDocumentos>() {
			@Override
			public MatDocumentos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MatDocumentos> listByParams(Param param, String[] order) {

		String sql = "select * from cat_mat_documentos " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatDocumentos>() {

			@Override
			public MatDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MatDocumentos> listFullByParams(MatDocumentos matdocumentos, String[] order) {
	
		return listFullByParams(Param.toParam("mdo",matdocumentos), order);
	
	}	
	
	public List<MatDocumentos> listFullByParams(Param param, String[] order) {

		String sql = "select mdo.id mdo_id, mdo.nom mdo_nom  ,mdo.est mdo_est ";
		sql = sql + " from cat_mat_documentos mdo";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatDocumentos>() {

			@Override
			public MatDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatDocumentos matdocumentos= rsToEntity(rs,"mdo_");
				return matdocumentos;
			}

		});

	}	




	// funciones privadas utilitarias para MatDocumentos

	private MatDocumentos rsToEntity(ResultSet rs,String alias) throws SQLException {
		MatDocumentos mat_documentos = new MatDocumentos();

		mat_documentos.setId(rs.getInt( alias + "id"));
		mat_documentos.setNom(rs.getString( alias + "nom"));
		mat_documentos.setEst(rs.getString( alias + "est"));
								
		return mat_documentos;

	}
	
}
