package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.UsuarioCampus;

import com.tesla.colegio.model.InscripcionCampus;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioCampusDAO.
 * @author MV
 *
 */
public class UsuarioCampusDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UsuarioCampus usuario_campus) {
		if (usuario_campus.getId() != null) {
			// update
			String sql = "UPDATE cvi_usuario_campus "
						+ "SET id_cvic=?, "
						+ "usr=?, "
						+ "psw=?, "
						+ "id_error=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						usuario_campus.getId_cvic(),
						usuario_campus.getUsr(),
						usuario_campus.getPsw(),
						usuario_campus.getId_error(),
						usuario_campus.getEst(),
						usuario_campus.getUsr_act(),
						new java.util.Date(),
						usuario_campus.getId()); 
			return usuario_campus.getId();

		} else {
			// insert
			String sql = "insert into cvi_usuario_campus ("
						+ "id_cvic, "
						+ "usr, "
						+ "psw, "
						+ "id_error, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				usuario_campus.getId_cvic(),
				usuario_campus.getUsr(),
				usuario_campus.getPsw(),
				usuario_campus.getId_error(),
				usuario_campus.getEst(),
				usuario_campus.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_usuario_campus where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<UsuarioCampus> list() {
		String sql = "select * from cvi_usuario_campus";
		
		//System.out.println(sql);
		
		List<UsuarioCampus> listUsuarioCampus = jdbcTemplate.query(sql, new RowMapper<UsuarioCampus>() {

			@Override
			public UsuarioCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuarioCampus;
	}

	public UsuarioCampus get(int id) {
		String sql = "select * from cvi_usuario_campus WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioCampus>() {

			@Override
			public UsuarioCampus extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UsuarioCampus getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cviu.id cviu_id, cviu.id_cvic cviu_id_cvic , cviu.usr cviu_usr , cviu.psw cviu_psw , cviu.id_error cviu_id_error  ,cviu.est cviu_est ";
		if (aTablas.contains("cvi_inscripcion_campus"))
			sql = sql + ", cvic.id cvic_id  , cvic.id_alu cvic_id_alu , cvic.id_fam cvic_id_fam , cvic.id_anio cvic_id_anio , cvic.tc_acept cvic_tc_acept , cvic.tc_not_acept_mot cvic_tc_not_acept_mot , cvic.id_error cvic_id_error  ";
	
		sql = sql + " from cvi_usuario_campus cviu "; 
		if (aTablas.contains("cvi_inscripcion_campus"))
			sql = sql + " left join cvi_inscripcion_campus cvic on cvic.id = cviu.id_cvic ";
		sql = sql + " where cviu.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioCampus>() {
		
			@Override
			public UsuarioCampus extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UsuarioCampus usuariocampus= rsToEntity(rs,"cviu_");
					if (aTablas.contains("cvi_inscripcion_campus")){
						InscripcionCampus inscripcioncampus = new InscripcionCampus();  
							inscripcioncampus.setId(rs.getInt("cvic_id")) ;  
							inscripcioncampus.setId_alu(rs.getInt("cvic_id_alu")) ;  
							inscripcioncampus.setId_fam(rs.getInt("cvic_id_fam")) ;  
							inscripcioncampus.setId_anio(rs.getInt("cvic_id_anio")) ;  
							inscripcioncampus.setTc_acept(rs.getString("cvic_tc_acept")) ;  
							inscripcioncampus.setTc_not_acept_mot(rs.getString("cvic_tc_not_acept_mot")) ;  
							inscripcioncampus.setId_error(rs.getInt("cvic_id_error")) ;  
							usuariocampus.setInscripcionCampus(inscripcioncampus);
					}
							return usuariocampus;
				}
				
				return null;
			}
			
		});


	}		
	
	public UsuarioCampus getByParams(Param param) {

		String sql = "select * from cvi_usuario_campus " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UsuarioCampus>() {
			@Override
			public UsuarioCampus extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UsuarioCampus> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_usuario_campus " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioCampus>() {

			@Override
			public UsuarioCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UsuarioCampus> listFullByParams(UsuarioCampus usuariocampus, String[] order) {
	
		return listFullByParams(Param.toParam("cviu",usuariocampus), order);
	
	}	
	
	public List<UsuarioCampus> listFullByParams(Param param, String[] order) {

		String sql = "select cviu.id cviu_id, cviu.id_cvic cviu_id_cvic , cviu.usr cviu_usr , cviu.psw cviu_psw , cviu.id_error cviu_id_error  ,cviu.est cviu_est ";
		sql = sql + ", cvic.id cvic_id  , cvic.id_alu cvic_id_alu , cvic.id_fam cvic_id_fam , cvic.id_anio cvic_id_anio , cvic.tc_acept cvic_tc_acept , cvic.tc_not_acept_mot cvic_tc_not_acept_mot , cvic.id_error cvic_id_error  ";
		sql = sql + " from cvi_usuario_campus cviu";
		sql = sql + " left join cvi_inscripcion_campus cvic on cvic.id = cviu.id_cvic ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<UsuarioCampus>() {

			@Override
			public UsuarioCampus mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioCampus usuariocampus= rsToEntity(rs,"cviu_");
				InscripcionCampus inscripcioncampus = new InscripcionCampus();  
				inscripcioncampus.setId(rs.getInt("cvic_id")) ;  
				inscripcioncampus.setId_alu(rs.getInt("cvic_id_alu")) ;  
				inscripcioncampus.setId_fam(rs.getInt("cvic_id_fam")) ;  
				inscripcioncampus.setId_anio(rs.getInt("cvic_id_anio")) ;  
				inscripcioncampus.setTc_acept(rs.getString("cvic_tc_acept")) ;  
				inscripcioncampus.setTc_not_acept_mot(rs.getString("cvic_tc_not_acept_mot")) ;  
				inscripcioncampus.setId_error(rs.getInt("cvic_id_error")) ;  
				usuariocampus.setInscripcionCampus(inscripcioncampus);
				return usuariocampus;
			}

		});

	}	




	// funciones privadas utilitarias para UsuarioCampus

	private UsuarioCampus rsToEntity(ResultSet rs,String alias) throws SQLException {
		UsuarioCampus usuario_campus = new UsuarioCampus();

		usuario_campus.setId(rs.getInt( alias + "id"));
		usuario_campus.setId_cvic(rs.getInt( alias + "id_cvic"));
		usuario_campus.setUsr(rs.getString( alias + "usr"));
		usuario_campus.setPsw(rs.getString( alias + "psw"));
		usuario_campus.setId_error(rs.getInt( alias + "id_error"));
		usuario_campus.setEst(rs.getString( alias + "est"));
								
		return usuario_campus;

	}
	
}
