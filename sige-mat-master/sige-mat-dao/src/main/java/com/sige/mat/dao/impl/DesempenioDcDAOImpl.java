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
import com.tesla.colegio.model.DesempenioDc;

import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.Grad;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DesempenioDcDAO.
 * @author MV
 *
 */
public class DesempenioDcDAOImpl{
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
	public int saveOrUpdate(DesempenioDc desempenio_dc) {
		if (desempenio_dc.getId() != null) {
			// update
			String sql = "UPDATE aca_desempenio_dc "
						+ "SET id_com=?, "
						+ "id_gra=?, "
						+ "nom=?, "
						+ "peso=?, "
						+ "orden=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						desempenio_dc.getId_com(),
						desempenio_dc.getId_gra(),
						desempenio_dc.getNom(),
						desempenio_dc.getPeso(),
						desempenio_dc.getOrden(),
						desempenio_dc.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						desempenio_dc.getId()); 
			return desempenio_dc.getId();

		} else {
			// insert
			String sql = "insert into aca_desempenio_dc ("
						+ "id_com, "
						+ "id_gra, "
						+ "nom, "
						+ "peso, "
						+ "orden, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				desempenio_dc.getId_com(),
				desempenio_dc.getId_gra(),
				desempenio_dc.getNom(),
				desempenio_dc.getPeso(),
				desempenio_dc.getOrden(),
				desempenio_dc.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_desempenio_dc where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DesempenioDc> list() {
		String sql = "select * from aca_desempenio_dc";
		
		System.out.println(sql);
		
		List<DesempenioDc> listDesempenioDc = jdbcTemplate.query(sql, new RowMapper<DesempenioDc>() {

			@Override
			public DesempenioDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDesempenioDc;
	}

	public DesempenioDc get(int id) {
		String sql = "select * from aca_desempenio_dc WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioDc>() {

			@Override
			public DesempenioDc extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DesempenioDc getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select des.id des_id, des.id_com des_id_com , des.id_gra des_id_gra , des.nom des_nom , des.peso des_peso , des.orden des_orden  ,des.est des_est ";
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
	
		sql = sql + " from aca_desempenio_dc des "; 
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + " left join aca_competencia_dc com on com.id = des.id_com ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = des.id_gra ";
		sql = sql + " where des.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioDc>() {
		
			@Override
			public DesempenioDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DesempenioDc desempeniodc= rsToEntity(rs,"des_");
					if (aTablas.contains("aca_competencia_dc")){
						CompetenciaDc competenciadc = new CompetenciaDc();  
							competenciadc.setId(rs.getInt("com_id")) ;  
							competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
							competenciadc.setNom(rs.getString("com_nom")) ;  
							competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
							competenciadc.setOrden(rs.getInt("com_orden")) ;  
							desempeniodc.setCompetenciaDc(competenciadc);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							desempeniodc.setGrad(grad);
					}
							return desempeniodc;
				}
				
				return null;
			}
			
		});


	}		
	
	public DesempenioDc getByParams(Param param) {

		String sql = "select * from aca_desempenio_dc " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioDc>() {
			@Override
			public DesempenioDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DesempenioDc> listByParams(Param param, String[] order) {

		String sql = "select * from aca_desempenio_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DesempenioDc>() {

			@Override
			public DesempenioDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DesempenioDc> listFullByParams(DesempenioDc desempeniodc, String[] order) {
	
		return listFullByParams(Param.toParam("des",desempeniodc), order);
	
	}	
	
	public List<DesempenioDc> listFullByParams(Param param, String[] order) {

		String sql = "select des.id des_id, des.id_com des_id_com , des.id_gra des_id_gra , des.nom des_nom , des.peso des_peso , des.orden des_orden  ,des.est des_est ";
		sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + " from aca_desempenio_dc des";
		sql = sql + " left join aca_competencia_dc com on com.id = des.id_com ";
		sql = sql + " left join cat_grad gra on gra.id = des.id_gra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DesempenioDc>() {

			@Override
			public DesempenioDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				DesempenioDc desempeniodc= rsToEntity(rs,"des_");
				CompetenciaDc competenciadc = new CompetenciaDc();  
				competenciadc.setId(rs.getInt("com_id")) ;  
				competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
				competenciadc.setNom(rs.getString("com_nom")) ;  
				competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
				competenciadc.setOrden(rs.getInt("com_orden")) ;  
				desempeniodc.setCompetenciaDc(competenciadc);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				desempeniodc.setGrad(grad);
				return desempeniodc;
			}

		});

	}	




	// funciones privadas utilitarias para DesempenioDc

	private DesempenioDc rsToEntity(ResultSet rs,String alias) throws SQLException {
		DesempenioDc desempenio_dc = new DesempenioDc();

		desempenio_dc.setId(rs.getInt( alias + "id"));
		desempenio_dc.setId_com(rs.getInt( alias + "id_com"));
		desempenio_dc.setId_gra(rs.getInt( alias + "id_gra"));
		desempenio_dc.setNom(rs.getString( alias + "nom"));
		desempenio_dc.setPeso(rs.getBigDecimal( alias + "peso"));
		desempenio_dc.setOrden(rs.getInt( alias + "orden"));
		desempenio_dc.setEst(rs.getString( alias + "est"));
								
		return desempenio_dc;

	}
	
}
