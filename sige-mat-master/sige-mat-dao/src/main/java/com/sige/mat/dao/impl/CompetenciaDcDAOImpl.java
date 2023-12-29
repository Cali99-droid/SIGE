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
import com.tesla.colegio.model.CompetenciaDc;

import com.tesla.colegio.model.DcnArea;
import com.tesla.colegio.model.CapacidadDc;
import com.tesla.colegio.model.DesempenioDc;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CompetenciaDcDAO.
 * @author MV
 *
 */
public class CompetenciaDcDAOImpl{
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
	public int saveOrUpdate(CompetenciaDc competencia_dc) {
		if (competencia_dc.getId() != null) {
			// update
			String sql = "UPDATE aca_competencia_dc "
						+ "SET id_dcare=?, "
						+ "nom=?, "
						+ "peso=?, "
						+ "orden=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						competencia_dc.getId_dcare(),
						competencia_dc.getNom(),
						competencia_dc.getPeso(),
						competencia_dc.getOrden(),
						competencia_dc.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						competencia_dc.getId()); 
			return competencia_dc.getId();

		} else {
			// insert
			String sql = "insert into aca_competencia_dc ("
						+ "id_dcare, "
						+ "nom, "
						+ "peso, "
						+ "orden, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				competencia_dc.getId_dcare(),
				competencia_dc.getNom(),
				competencia_dc.getPeso(),
				competencia_dc.getOrden(),
				competencia_dc.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_competencia_dc where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CompetenciaDc> list() {
		String sql = "select * from aca_competencia_dc";
		
		System.out.println(sql);
		
		List<CompetenciaDc> listCompetenciaDc = jdbcTemplate.query(sql, new RowMapper<CompetenciaDc>() {

			@Override
			public CompetenciaDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCompetenciaDc;
	}

	public CompetenciaDc get(int id) {
		String sql = "select * from aca_competencia_dc WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaDc>() {

			@Override
			public CompetenciaDc extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CompetenciaDc getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select com.id com_id, com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ,com.est com_est ";
		if (aTablas.contains("aca_dcn_area"))
			sql = sql + ", dcare.id dcare_id  , dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ";
	
		sql = sql + " from aca_competencia_dc com "; 
		if (aTablas.contains("aca_dcn_area"))
			sql = sql + " left join aca_dcn_area dcare on dcare.id = com.id_dcare ";
		sql = sql + " where com.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaDc>() {
		
			@Override
			public CompetenciaDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CompetenciaDc competenciadc= rsToEntity(rs,"com_");
					if (aTablas.contains("aca_dcn_area")){
						DcnArea dcnarea = new DcnArea();  
							dcnarea.setId(rs.getInt("dcare_id")) ;  
							dcnarea.setId_dcniv(rs.getInt("dcare_id_dcniv")) ;  
							dcnarea.setId_are(rs.getInt("dcare_id_are")) ;  
							competenciadc.setDcnArea(dcnarea);
					}
							return competenciadc;
				}
				
				return null;
			}
			
		});


	}		
	
	public CompetenciaDc getByParams(Param param) {

		String sql = "select * from aca_competencia_dc " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaDc>() {
			@Override
			public CompetenciaDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CompetenciaDc> listByParams(Param param, String[] order) {

		String sql = "select * from aca_competencia_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaDc>() {

			@Override
			public CompetenciaDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CompetenciaDc> listFullByParams(CompetenciaDc competenciadc, String[] order) {
	
		return listFullByParams(Param.toParam("com",competenciadc), order);
	
	}	
	
	public List<CompetenciaDc> listFullByParams(Param param, String[] order) {

		String sql = "select com.id com_id, com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ,com.est com_est ";
		sql = sql + ", dcare.id dcare_id  , dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ";
		sql = sql + " from aca_competencia_dc com";
		sql = sql + " left join aca_dcn_area dcare on dcare.id = com.id_dcare ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaDc>() {

			@Override
			public CompetenciaDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				CompetenciaDc competenciadc= rsToEntity(rs,"com_");
				DcnArea dcnarea = new DcnArea();  
				dcnarea.setId(rs.getInt("dcare_id")) ;  
				dcnarea.setId_dcniv(rs.getInt("dcare_id_dcniv")) ;  
				dcnarea.setId_are(rs.getInt("dcare_id_are")) ;  
				competenciadc.setDcnArea(dcnarea);
				return competenciadc;
			}

		});

	}	


	public List<CapacidadDc> getListCapacidadDc(Param param, String[] order) {
		String sql = "select * from aca_capacidad_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CapacidadDc>() {

			@Override
			public CapacidadDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapacidadDc capacidad_dc = new CapacidadDc();

				capacidad_dc.setId(rs.getInt("id"));
				capacidad_dc.setId_com(rs.getInt("id_com"));
				capacidad_dc.setNom(rs.getString("nom"));
				capacidad_dc.setPeso(rs.getBigDecimal("peso"));
				capacidad_dc.setOrden(rs.getInt("orden"));
				capacidad_dc.setEst(rs.getString("est"));
												
				return capacidad_dc;
			}

		});	
	}
	public List<DesempenioDc> getListDesempenioDc(Param param, String[] order) {
		String sql = "select * from aca_desempenio_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<DesempenioDc>() {

			@Override
			public DesempenioDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				DesempenioDc desempenio_dc = new DesempenioDc();

				desempenio_dc.setId(rs.getInt("id"));
				desempenio_dc.setId_com(rs.getInt("id_com"));
				desempenio_dc.setId_gra(rs.getInt("id_gra"));
				desempenio_dc.setNom(rs.getString("nom"));
				desempenio_dc.setPeso(rs.getBigDecimal("peso"));
				desempenio_dc.setOrden(rs.getInt("orden"));
				desempenio_dc.setEst(rs.getString("est"));
												
				return desempenio_dc;
			}

		});	
	}


	// funciones privadas utilitarias para CompetenciaDc

	private CompetenciaDc rsToEntity(ResultSet rs,String alias) throws SQLException {
		CompetenciaDc competencia_dc = new CompetenciaDc();

		competencia_dc.setId(rs.getInt( alias + "id"));
		competencia_dc.setId_dcare(rs.getInt( alias + "id_dcare"));
		competencia_dc.setNom(rs.getString( alias + "nom"));
		competencia_dc.setPeso(rs.getBigDecimal( alias + "peso"));
		competencia_dc.setOrden(rs.getInt( alias + "orden"));
		competencia_dc.setEst(rs.getString( alias + "est"));
								
		return competencia_dc;

	}
	
}
