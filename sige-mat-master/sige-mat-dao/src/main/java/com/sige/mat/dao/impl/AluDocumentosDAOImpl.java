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
import com.tesla.colegio.model.AluDocumentos;

import com.tesla.colegio.model.MatriculaDoc;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AluDocumentosDAO.
 * @author MV
 *
 */
public class AluDocumentosDAOImpl{
	
	final static Logger logger = Logger.getLogger(AluDocumentosDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AluDocumentos alu_documentos) {
		if (alu_documentos.getId()!=null) {
			// update
			String sql = "UPDATE cat_alu_documentos "
						+ "SET nom=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						alu_documentos.getNom(),
						alu_documentos.getEst(),
						alu_documentos.getUsr_act(),
						new java.util.Date(),
						alu_documentos.getId()); 

		} else {
			// insert
			String sql = "insert into cat_alu_documentos ("
						+ "nom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				alu_documentos.getNom(),
				alu_documentos.getEst(),
				alu_documentos.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from cat_alu_documentos where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<AluDocumentos> list() {
		String sql = "select * from cat_alu_documentos";
		
		//logger.info(sql);
		
		List<AluDocumentos> listAluDocumentos = jdbcTemplate.query(sql, new RowMapper<AluDocumentos>() {

			
			public AluDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAluDocumentos;
	}

	
	public AluDocumentos get(int id) {
		String sql = "select * from cat_alu_documentos WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AluDocumentos>() {

			
			public AluDocumentos extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public AluDocumentos getFull(int id, String tablas[]) {
		String sql = "select ado.id ado_id, ado.nom ado_nom  ,ado.est ado_est ";
	
		sql = sql + " from cat_alu_documentos ado "; 
		sql = sql + " where ado.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AluDocumentos>() {
		
			
			public AluDocumentos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AluDocumentos aluDocumentos= rsToEntity(rs,"ado_");
							return aluDocumentos;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public AluDocumentos getByParams(Param param) {

		String sql = "select * from cat_alu_documentos " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AluDocumentos>() {
			
			public AluDocumentos extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<AluDocumentos> listByParams(Param param, String[] order) {

		String sql = "select * from cat_alu_documentos " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AluDocumentos>() {

			
			public AluDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<AluDocumentos> listFullByParams(AluDocumentos aluDocumentos, String[] order) {
	
		return listFullByParams(Param.toParam("ado",aluDocumentos), order);
	
	}	
	
	
	public List<AluDocumentos> listFullByParams(Param param, String[] order) {

		String sql = "select ado.id ado_id, ado.nom ado_nom  ,ado.est ado_est ";
		sql = sql + " from cat_alu_documentos ado";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AluDocumentos>() {

			
			public AluDocumentos mapRow(ResultSet rs, int rowNum) throws SQLException {
				AluDocumentos aluDocumentos= rsToEntity(rs,"ado_");
				return aluDocumentos;
			}

		});

	}	


	public List<MatriculaDoc> getListMatriculaDoc(Param param, String[] order) {
		String sql = "select * from mat_matricula_doc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatriculaDoc>() {

			
			public MatriculaDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatriculaDoc matricula_doc = new MatriculaDoc();

				matricula_doc.setId(rs.getInt("id"));
				matricula_doc.setId_ado(rs.getInt("id_ado"));
				matricula_doc.setId_alu(rs.getInt("id_alu"));
				matricula_doc.setEst(rs.getString("est"));
												
				return matricula_doc;
			}

		});	
	}


	// funciones privadas utilitarias para AluDocumentos

	private AluDocumentos rsToEntity(ResultSet rs,String alias) throws SQLException {
		AluDocumentos alu_documentos = new AluDocumentos();

		alu_documentos.setId(rs.getInt( alias + "id"));
		alu_documentos.setNom(rs.getString( alias + "nom"));
		alu_documentos.setEst(rs.getString( alias + "est"));
								
		return alu_documentos;

	}
	
}
