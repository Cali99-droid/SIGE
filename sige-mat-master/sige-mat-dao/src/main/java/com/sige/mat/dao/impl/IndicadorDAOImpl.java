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
import com.tesla.colegio.model.Indicador;

import com.tesla.colegio.model.SesionDesempenio;
import com.tesla.colegio.model.SesionIndicador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface IndicadorDAO.
 * @author MV
 *
 */
public class IndicadorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Indicador indicador) {
		if (indicador.getId() != null) {
			// update
			String sql = "UPDATE col_indicador "
						+ "SET nom=?, "
						+ "id_csd=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						indicador.getNom(),
						indicador.getId_csd(),
						indicador.getEst(),
						indicador.getUsr_act(),
						new java.util.Date(),
						indicador.getId()); 
			return indicador.getId();

		} else {
			// insert
			String sql = "insert into col_indicador ("
						+ "nom, "
						+ "id_csd, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				indicador.getNom(),
				indicador.getId_csd(),
				indicador.getEst(),
				indicador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_indicador where id_csd=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}
	
	public void deleteIndicador(int id) {
		String sql = "delete from col_indicador where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Indicador> list() {
		String sql = "select * from col_indicador";
		
		
		
		List<Indicador> listIndicador = jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listIndicador;
	}

	public Indicador get(int id) {
		String sql = "select * from col_indicador WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Indicador>() {

			@Override
			public Indicador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Indicador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ind.id ind_id, ind.nom ind_nom , ind.id_csd ind_id_csd  ,ind.est ind_est ";
		if (aTablas.contains("col_sesion_desempenio"))
			sql = sql + ", csd.id csd_id  , csd.id_cde csd_id_cde , csd.id_ses csd_id_ses  ";
	
		sql = sql + " from col_indicador ind "; 
		if (aTablas.contains("col_sesion_desempenio"))
			sql = sql + " left join col_sesion_desempenio csd on csd.id = ind.id_csd ";
		sql = sql + " where ind.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Indicador>() {
		
			@Override
			public Indicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Indicador indicador= rsToEntity(rs,"ind_");
					if (aTablas.contains("col_sesion_desempenio")){
						SesionDesempenio sesiondesempenio = new SesionDesempenio();  
							sesiondesempenio.setId(rs.getInt("csd_id")) ;  
							sesiondesempenio.setId_cde(rs.getInt("csd_id_cde")) ;  
							sesiondesempenio.setId_ses(rs.getInt("csd_id_ses")) ;  
							indicador.setSesionDesempenio(sesiondesempenio);
					}
							return indicador;
				}
				
				return null;
			}
			
		});


	}		
	
	public Indicador getByParams(Param param) {

		String sql = "select * from col_indicador " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Indicador>() {
			@Override
			public Indicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Indicador> listByParams(Param param, String[] order) {

		String sql = "select * from col_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Indicador> listFullByParams(Indicador indicador, String[] order) {
	
		return listFullByParams(Param.toParam("ind",indicador), order);
	
	}	
	
	public List<Indicador> listFullByParams(Param param, String[] order) {

		String sql = "select ind.id ind_id, ind.nom ind_nom , ind.id_csd ind_id_csd  ,ind.est ind_est ";
		sql = sql + ", csd.id csd_id  , csd.id_cde csd_id_cde , csd.id_ses csd_id_ses  ";
		sql = sql + " from col_indicador ind";
		sql = sql + " left join col_sesion_desempenio csd on csd.id = ind.id_csd ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Indicador>() {

			@Override
			public Indicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Indicador indicador= rsToEntity(rs,"ind_");
				SesionDesempenio sesiondesempenio = new SesionDesempenio();  
				sesiondesempenio.setId(rs.getInt("csd_id")) ;  
				sesiondesempenio.setId_cde(rs.getInt("csd_id_cde")) ;  
				sesiondesempenio.setId_ses(rs.getInt("csd_id_ses")) ;  
				indicador.setSesionDesempenio(sesiondesempenio);
				return indicador;
			}

		});

	}	


	public List<SesionIndicador> getListSesionIndicador(Param param, String[] order) {
		String sql = "select * from col_sesion_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<SesionIndicador>() {

			@Override
			public SesionIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				SesionIndicador sesion_indicador = new SesionIndicador();

				sesion_indicador.setId(rs.getInt("id"));
				sesion_indicador.setId_ses(rs.getInt("id_ses"));
				sesion_indicador.setId_ind(rs.getInt("id_ind"));
				sesion_indicador.setEst(rs.getString("est"));
												
				return sesion_indicador;
			}

		});	
	}


	// funciones privadas utilitarias para Indicador

	private Indicador rsToEntity(ResultSet rs,String alias) throws SQLException {
		Indicador indicador = new Indicador();

		indicador.setId(rs.getInt( alias + "id"));
		indicador.setNom(rs.getString( alias + "nom"));
		indicador.setId_csd(rs.getInt( alias + "id_csd"));
		indicador.setEst(rs.getString( alias + "est"));
								
		return indicador;

	}
	
}
