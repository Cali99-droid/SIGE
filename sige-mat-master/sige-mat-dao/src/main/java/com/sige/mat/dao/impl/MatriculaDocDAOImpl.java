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
import com.tesla.colegio.model.MatriculaDoc;

import com.tesla.colegio.model.AluDocumentos;
import com.tesla.colegio.model.Alumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MatriculaDocDAO.
 * @author MV
 *
 */
public class MatriculaDocDAOImpl{
	final static Logger logger = Logger.getLogger(MatriculaDocDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MatriculaDoc matricula_doc) {
		if (matricula_doc.getId() != null) {
			// update
			String sql = "UPDATE mat_matricula_doc "
						+ "SET id_ado=?, "
						+ "id_alu=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						matricula_doc.getId_ado(),
						matricula_doc.getId_alu(),
						matricula_doc.getEst(),
						matricula_doc.getUsr_act(),
						new java.util.Date(),
						matricula_doc.getId()); 

		} else {
			// insert
			String sql = "insert into mat_matricula_doc ("
						+ "id_ado, "
						+ "id_alu, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				matricula_doc.getId_ado(),
				matricula_doc.getId_alu(),
				matricula_doc.getEst(),
				matricula_doc.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from mat_matricula_doc where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	
	public void delete(int id_alu, int id_ado) {
		String sql = "delete from mat_matricula_doc where id_alu=" + id_alu + " and id_ado=" + id_ado;
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql);
	}

	
	public List<MatriculaDoc> list() {
		String sql = "select * from mat_matricula_doc";
		
		//logger.info(sql);
		
		List<MatriculaDoc> listMatriculaDoc = jdbcTemplate.query(sql, new RowMapper<MatriculaDoc>() {

			
			public MatriculaDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMatriculaDoc;
	}

	
	public MatriculaDoc get(int id) {
		String sql = "select * from mat_matricula_doc WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatriculaDoc>() {

			
			public MatriculaDoc extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public MatriculaDoc getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mad.id mad_id, mad.id_ado mad_id_ado , mad.id_alu mad_id_alu  ,mad.est mad_est ";
		if (aTablas.contains("cat_alu_documentos"))
			sql = sql + ", ado.id ado_id  , ado.nom ado_nom  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from mat_matricula_doc mad "; 
		if (aTablas.contains("cat_alu_documentos"))
			sql = sql + " left join cat_alu_documentos ado on ado.id = mad.id_ado ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = mad.id_alu ";
		sql = sql + " where mad.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<MatriculaDoc>() {
		
			
			public MatriculaDoc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MatriculaDoc matriculaDoc= rsToEntity(rs,"mad_");
					if (aTablas.contains("cat_alu_documentos")){
						AluDocumentos aluDocumentos = new AluDocumentos();  
							aluDocumentos.setId(rs.getInt("ado_id")) ;  
							aluDocumentos.setNom(rs.getString("ado_nom")) ;  
							matriculaDoc.setAluDocumentos(aluDocumentos);
					}
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
							alumno.setId_tap(rs.getString("alu_id_tap")) ;  
							alumno.setId_gen(rs.getString("alu_id_gen")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
							alumno.setNom(rs.getString("alu_nom")) ;  
							alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
							alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
							alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							matriculaDoc.setAlumno(alumno);
					}
							return matriculaDoc;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public MatriculaDoc getByParams(Param param) {

		String sql = "select * from mat_matricula_doc " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MatriculaDoc>() {
			
			public MatriculaDoc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<MatriculaDoc> listByParams(Param param, String[] order) {

		String sql = "select * from mat_matricula_doc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatriculaDoc>() {

			
			public MatriculaDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<MatriculaDoc> listFullByParams(MatriculaDoc matriculaDoc, String[] order) {
	
		return listFullByParams(Param.toParam("mad",matriculaDoc), order);
	
	}	
	
	
	public List<MatriculaDoc> listFullByParams(Param param, String[] order) {

		String sql = "select mad.id mad_id, mad.id_ado mad_id_ado , mad.id_alu mad_id_alu  ,mad.est mad_est ";
		sql = sql + ", ado.id ado_id  , ado.nom ado_nom  ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from mat_matricula_doc mad";
		sql = sql + " left join cat_alu_documentos ado on ado.id = mad.id_ado ";
		sql = sql + " left join alu_alumno alu on alu.id = mad.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<MatriculaDoc>() {

			
			public MatriculaDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatriculaDoc matriculaDoc= rsToEntity(rs,"mad_");
				AluDocumentos aluDocumentos = new AluDocumentos();  
				aluDocumentos.setId(rs.getInt("ado_id")) ;  
				aluDocumentos.setNom(rs.getString("ado_nom")) ;  
				matriculaDoc.setAluDocumentos(aluDocumentos);
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				matriculaDoc.setAlumno(alumno);
				return matriculaDoc;
			}

		});

	}	




	// funciones privadas utilitarias para MatriculaDoc

	private MatriculaDoc rsToEntity(ResultSet rs,String alias) throws SQLException {
		MatriculaDoc matricula_doc = new MatriculaDoc();

		matricula_doc.setId(rs.getInt( alias + "id"));
		matricula_doc.setId_ado(rs.getInt( alias + "id_ado"));
		matricula_doc.setId_alu(rs.getInt( alias + "id_alu"));
		matricula_doc.setEst(rs.getString( alias + "est"));
								
		return matricula_doc;

	}
	
}
