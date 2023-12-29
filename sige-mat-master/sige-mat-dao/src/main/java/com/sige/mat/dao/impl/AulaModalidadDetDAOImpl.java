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
import com.tesla.colegio.model.AulaModalidadDet;

import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.ModalidadEstudio;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AulaModalidadDetDAO.
 * @author MV
 *
 */
public class AulaModalidadDetDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AulaModalidadDet aula_modalidad_det) {
		if (aula_modalidad_det.getId() != null) {
			// update
			String sql = "UPDATE col_aula_modalidad_det "
						+ "SET id_au=?, "
						+ "id_cme=?, "
						+ "mes=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						aula_modalidad_det.getId_au(),
						aula_modalidad_det.getId_cme(),
						aula_modalidad_det.getMes(),
						aula_modalidad_det.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						aula_modalidad_det.getId()); 
			return aula_modalidad_det.getId();

		} else {
			// insert
			String sql = "insert into col_aula_modalidad_det ("
						+ "id_au, "
						+ "id_cme, "
						+ "mes, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				aula_modalidad_det.getId_au(),
				aula_modalidad_det.getId_cme(),
				aula_modalidad_det.getMes(),
				aula_modalidad_det.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_aula_modalidad_det where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AulaModalidadDet> list() {
		String sql = "select * from col_aula_modalidad_det";
		
		System.out.println(sql);
		
		List<AulaModalidadDet> listAulaModalidadDet = jdbcTemplate.query(sql, new RowMapper<AulaModalidadDet>() {

			@Override
			public AulaModalidadDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAulaModalidadDet;
	}

	public AulaModalidadDet get(int id) {
		String sql = "select * from col_aula_modalidad_det WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaModalidadDet>() {

			@Override
			public AulaModalidadDet extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AulaModalidadDet getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select amd.id amd_id, amd.id_au amd_id_au , amd.id_cme amd_id_cme , amd.mes amd_mes  ,amd.est amd_est ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.id_cme au_id_cme , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("cat_modalidad_estudio"))
			sql = sql + ", cme.id cme_id  , cme.nom cme_nom , cme.des cme_des  ";
	
		sql = sql + " from col_aula_modalidad_det amd "; 
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = amd.id_au ";
		if (aTablas.contains("cat_modalidad_estudio"))
			sql = sql + " left join cat_modalidad_estudio cme on cme.id = amd.id_cme ";
		sql = sql + " where amd.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaModalidadDet>() {
		
			@Override
			public AulaModalidadDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AulaModalidadDet aulamodalidaddet= rsToEntity(rs,"amd_");
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("au_id")) ;  
							aula.setId_per(rs.getInt("au_id_per")) ;  
							aula.setId_grad(rs.getInt("au_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("au_id_tur")) ;  
							//aula.setId_cme(rs.getInt("au_id_cme")) ;  
							aula.setSecc(rs.getString("au_secc")) ;  
							aula.setCap(rs.getInt("au_cap")) ;  
							aulamodalidaddet.setAula(aula);
					}
					if (aTablas.contains("cat_modalidad_estudio")){
						ModalidadEstudio modalidadestudio = new ModalidadEstudio();  
							modalidadestudio.setId(rs.getInt("cme_id")) ;  
							modalidadestudio.setNom(rs.getString("cme_nom")) ;  
							modalidadestudio.setDes(rs.getString("cme_des")) ;  
							aulamodalidaddet.setModalidadEstudio(modalidadestudio);
					}
							return aulamodalidaddet;
				}
				
				return null;
			}
			
		});


	}		
	
	public AulaModalidadDet getByParams(Param param) {

		String sql = "select * from col_aula_modalidad_det " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AulaModalidadDet>() {
			@Override
			public AulaModalidadDet extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AulaModalidadDet> listByParams(Param param, String[] order) {

		String sql = "select * from col_aula_modalidad_det " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AulaModalidadDet>() {

			@Override
			public AulaModalidadDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AulaModalidadDet> listFullByParams(AulaModalidadDet aulamodalidaddet, String[] order) {
	
		return listFullByParams(Param.toParam("amd",aulamodalidaddet), order);
	
	}	
	
	public List<AulaModalidadDet> listFullByParams(Param param, String[] order) {

		String sql = "select amd.id amd_id, amd.id_au amd_id_au , amd.id_cme amd_id_cme , amd.mes amd_mes  ,amd.est amd_est ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.id_cme au_id_cme , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", cme.id cme_id  , cme.nom cme_nom , cme.des cme_des  ";
		sql = sql + " from col_aula_modalidad_det amd";
		sql = sql + " left join col_aula au on au.id = amd.id_au ";
		sql = sql + " left join cat_modalidad_estudio cme on cme.id = amd.id_cme ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AulaModalidadDet>() {

			@Override
			public AulaModalidadDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				AulaModalidadDet aulamodalidaddet= rsToEntity(rs,"amd_");
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				//aula.setId_cme(rs.getInt("au_id_cme")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				aulamodalidaddet.setAula(aula);
				ModalidadEstudio modalidadestudio = new ModalidadEstudio();  
				modalidadestudio.setId(rs.getInt("cme_id")) ;  
				modalidadestudio.setNom(rs.getString("cme_nom")) ;  
				modalidadestudio.setDes(rs.getString("cme_des")) ;  
				aulamodalidaddet.setModalidadEstudio(modalidadestudio);
				return aulamodalidaddet;
			}

		});

	}	




	// funciones privadas utilitarias para AulaModalidadDet

	private AulaModalidadDet rsToEntity(ResultSet rs,String alias) throws SQLException {
		AulaModalidadDet aula_modalidad_det = new AulaModalidadDet();

		aula_modalidad_det.setId(rs.getInt( alias + "id"));
		aula_modalidad_det.setId_au(rs.getInt( alias + "id_au"));
		aula_modalidad_det.setId_cme(rs.getInt( alias + "id_cme"));
		aula_modalidad_det.setMes(rs.getInt( alias + "mes"));
		aula_modalidad_det.setEst(rs.getString( alias + "est"));
								
		return aula_modalidad_det;

	}
	
}
