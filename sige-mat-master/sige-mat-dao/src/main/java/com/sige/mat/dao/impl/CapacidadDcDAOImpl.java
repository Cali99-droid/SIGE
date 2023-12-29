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
import com.tesla.colegio.model.CapacidadDc;

import com.tesla.colegio.model.CompetenciaDc;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CapacidadDcDAO.
 * @author MV
 *
 */
public class CapacidadDcDAOImpl{
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
	public int saveOrUpdate(CapacidadDc capacidad_dc) {
		if (capacidad_dc.getId() != null) {
			// update
			String sql = "UPDATE aca_capacidad_dc "
						+ "SET id_com=?, "
						+ "nom=?, "
						+ "peso=?, "
						+ "orden=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						capacidad_dc.getId_com(),
						capacidad_dc.getNom(),
						capacidad_dc.getPeso(),
						capacidad_dc.getOrden(),
						capacidad_dc.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						capacidad_dc.getId()); 
			return capacidad_dc.getId();

		} else {
			// insert
			String sql = "insert into aca_capacidad_dc ("
						+ "id_com, "
						+ "nom, "
						+ "peso, "
						+ "orden, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				capacidad_dc.getId_com(),
				capacidad_dc.getNom(),
				capacidad_dc.getPeso(),
				capacidad_dc.getOrden(),
				capacidad_dc.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_capacidad_dc where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CapacidadDc> list() {
		String sql = "select * from aca_capacidad_dc";
		
		System.out.println(sql);
		
		List<CapacidadDc> listCapacidadDc = jdbcTemplate.query(sql, new RowMapper<CapacidadDc>() {

			@Override
			public CapacidadDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCapacidadDc;
	}

	public CapacidadDc get(int id) {
		String sql = "select * from aca_capacidad_dc WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadDc>() {

			@Override
			public CapacidadDc extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CapacidadDc getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select acap.id acap_id, acap.id_com acap_id_com , acap.nom acap_nom , acap.peso acap_peso , acap.orden acap_orden  ,acap.est acap_est ";
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
	
		sql = sql + " from aca_capacidad_dc acap "; 
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + " left join aca_competencia_dc com on com.id = acap.id_com ";
		sql = sql + " where acap.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadDc>() {
		
			@Override
			public CapacidadDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CapacidadDc capacidaddc= rsToEntity(rs,"acap_");
					if (aTablas.contains("aca_competencia_dc")){
						CompetenciaDc competenciadc = new CompetenciaDc();  
							competenciadc.setId(rs.getInt("com_id")) ;  
							competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
							competenciadc.setNom(rs.getString("com_nom")) ;  
							competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
							competenciadc.setOrden(rs.getInt("com_orden")) ;  
							capacidaddc.setCompetenciaDc(competenciadc);
					}
							return capacidaddc;
				}
				
				return null;
			}
			
		});


	}		
	
	public CapacidadDc getByParams(Param param) {

		String sql = "select * from aca_capacidad_dc " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CapacidadDc>() {
			@Override
			public CapacidadDc extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CapacidadDc> listByParams(Param param, String[] order) {

		String sql = "select * from aca_capacidad_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapacidadDc>() {

			@Override
			public CapacidadDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CapacidadDc> listFullByParams(CapacidadDc capacidaddc, String[] order) {
	
		return listFullByParams(Param.toParam("acap",capacidaddc), order);
	
	}	
	
	public List<CapacidadDc> listFullByParams(Param param, String[] order) {

		String sql = "select acap.id acap_id, acap.id_com acap_id_com , acap.nom acap_nom , acap.peso acap_peso , acap.orden acap_orden  ,acap.est acap_est ";
		sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		sql = sql + " from aca_capacidad_dc acap";
		sql = sql + " left join aca_competencia_dc com on com.id = acap.id_com ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CapacidadDc>() {

			@Override
			public CapacidadDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapacidadDc capacidaddc= rsToEntity(rs,"acap_");
				CompetenciaDc competenciadc = new CompetenciaDc();  
				competenciadc.setId(rs.getInt("com_id")) ;  
				competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
				competenciadc.setNom(rs.getString("com_nom")) ;  
				competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
				competenciadc.setOrden(rs.getInt("com_orden")) ;  
				capacidaddc.setCompetenciaDc(competenciadc);
				return capacidaddc;
			}

		});

	}	




	// funciones privadas utilitarias para CapacidadDc

	private CapacidadDc rsToEntity(ResultSet rs,String alias) throws SQLException {
		CapacidadDc capacidad_dc = new CapacidadDc();

		capacidad_dc.setId(rs.getInt( alias + "id"));
		capacidad_dc.setId_com(rs.getInt( alias + "id_com"));
		capacidad_dc.setNom(rs.getString( alias + "nom"));
		capacidad_dc.setPeso(rs.getBigDecimal( alias + "peso"));
		capacidad_dc.setOrden(rs.getInt( alias + "orden"));
		capacidad_dc.setEst(rs.getString( alias + "est"));
								
		return capacidad_dc;

	}
	
}
