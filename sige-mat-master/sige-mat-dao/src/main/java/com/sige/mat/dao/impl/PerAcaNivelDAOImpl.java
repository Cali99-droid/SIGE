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
import com.tesla.colegio.model.PerAcaNivel;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.PerUni;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerAcaNivelDAO.
 * @author MV
 *
 */
public class PerAcaNivelDAOImpl{
	final static Logger logger = Logger.getLogger(PerAcaNivelDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PerAcaNivel per_aca_nivel) {
		if (per_aca_nivel.getId() != null) {
			// update
			String sql = "UPDATE cat_per_aca_nivel "
						+ "SET id_niv=?, "
						+ "id_anio=?, "
						+ "id_gir=?, "
						+ "id_cpa=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						per_aca_nivel.getId_niv(),
						per_aca_nivel.getId_anio(),
						per_aca_nivel.getId_gir(),
						per_aca_nivel.getId_cpa(),
						per_aca_nivel.getEst(),
						per_aca_nivel.getUsr_act(),
						new java.util.Date(),
						per_aca_nivel.getId()); 
			return per_aca_nivel.getId();

		} else {
			// insert
			String sql = "insert into cat_per_aca_nivel ("
						+ "id_niv, "
						+ "id_anio, "
						+ "id_gir, "
						+ "id_cpa, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				per_aca_nivel.getId_niv(),
				per_aca_nivel.getId_anio(),
				per_aca_nivel.getId_gir(),
				per_aca_nivel.getId_cpa(),
				per_aca_nivel.getEst(),
				per_aca_nivel.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_per_aca_nivel where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PerAcaNivel> list() {
		String sql = "select * from cat_per_aca_nivel";
		
		//logger.info(sql);
		
		List<PerAcaNivel> listPerAcaNivel = jdbcTemplate.query(sql, new RowMapper<PerAcaNivel>() {

			@Override
			public PerAcaNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerAcaNivel;
	}

	public PerAcaNivel get(int id) {
		String sql = "select * from cat_per_aca_nivel WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerAcaNivel>() {

			@Override
			public PerAcaNivel extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PerAcaNivel getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpan.id cpan_id, cpan.id_niv cpan_id_niv , cpan.id_cpa cpan_id_cpa  ,cpan.est cpan_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_periodo_aca"))
			sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
	
		sql = sql + " from cat_per_aca_nivel cpan "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = cpan.id_niv ";
		if (aTablas.contains("cat_periodo_aca"))
			sql = sql + " left join cat_periodo_aca cpa on cpa.id = cpan.id_cpa ";
		sql = sql + " where cpan.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PerAcaNivel>() {
		
			@Override
			public PerAcaNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PerAcaNivel peracanivel= rsToEntity(rs,"cpan_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							peracanivel.setNivel(nivel);
					}
					if (aTablas.contains("cat_periodo_aca")){
						PeriodoAca periodoaca = new PeriodoAca();  
							periodoaca.setId(rs.getInt("cpa_id")) ;  
							periodoaca.setNom(rs.getString("cpa_nom")) ;  
							peracanivel.setPeriodoAca(periodoaca);
					}
							return peracanivel;
				}
				
				return null;
			}
			
		});


	}		
	
	public PerAcaNivel getByParams(Param param) {

		String sql = "select * from cat_per_aca_nivel " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerAcaNivel>() {
			@Override
			public PerAcaNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PerAcaNivel> listByParams(Param param, String[] order) {

		String sql = "select * from cat_per_aca_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerAcaNivel>() {

			@Override
			public PerAcaNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PerAcaNivel> listFullByParams(PerAcaNivel peracanivel, String[] order) {
	
		return listFullByParams(Param.toParam("cpan",peracanivel), order);
	
	}	
	
	public List<PerAcaNivel> listFullByParams(Param param, String[] order) {

		String sql = "select cpan.id cpan_id, cpan.id_niv cpan_id_niv , cpan.id_cpa cpan_id_cpa  ,cpan.est cpan_est, cpan.id_anio cpan_id_anio, cpan.id_gir cpan_id_gir ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
		sql = sql + ", gir.id gir_id  , gir.nom gir_nom  ";
		sql = sql + " from cat_per_aca_nivel cpan";
		sql = sql + " left join cat_nivel niv on niv.id = cpan.id_niv ";
		sql = sql + " left join cat_periodo_aca cpa on cpa.id = cpan.id_cpa ";
		sql = sql + " left join ges_giro_negocio gir on gir.id = cpan.id_gir ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerAcaNivel>() {

			@Override
			public PerAcaNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerAcaNivel peracanivel= rsToEntity(rs,"cpan_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				peracanivel.setNivel(nivel);
				PeriodoAca periodoaca = new PeriodoAca();  
				periodoaca.setId(rs.getInt("cpa_id")) ;  
				periodoaca.setNom(rs.getString("cpa_nom")) ;  
				peracanivel.setPeriodoAca(periodoaca);
				GiroNegocio giroNegocio = new GiroNegocio();
				giroNegocio.setId(rs.getInt("gir_id"));
				giroNegocio.setNom(rs.getString("gir_nom"));
				peracanivel.setGiroNegocio(giroNegocio);
				return peracanivel;
			}

		});

	}	


	public List<PerUni> getListPerUni(Param param, String[] order) {
		String sql = "select * from col_per_uni " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PerUni>() {

			@Override
			public PerUni mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerUni per_uni = new PerUni();

				per_uni.setId(rs.getInt("id"));
				per_uni.setId_cpa(rs.getInt("id_cpan"));
				per_uni.setNump(rs.getInt("nump"));
				//per_uni.setNumu(rs.getInt("numu"));
				per_uni.setEst(rs.getString("est"));
												
				return per_uni;
			}

		});	
	}


	// funciones privadas utilitarias para PerAcaNivel

	private PerAcaNivel rsToEntity(ResultSet rs,String alias) throws SQLException {
		PerAcaNivel per_aca_nivel = new PerAcaNivel();

		per_aca_nivel.setId(rs.getInt( alias + "id"));
		per_aca_nivel.setId_niv(rs.getInt( alias + "id_niv"));
		per_aca_nivel.setId_anio(rs.getInt( alias + "id_anio"));
		per_aca_nivel.setId_gir(rs.getInt( alias + "id_gir"));
		per_aca_nivel.setId_cpa(rs.getInt( alias + "id_cpa"));
		per_aca_nivel.setEst(rs.getString( alias + "est"));
								
		return per_aca_nivel;

	}
	
}
