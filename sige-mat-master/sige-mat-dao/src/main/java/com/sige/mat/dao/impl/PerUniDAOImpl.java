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
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.PerAcaNivel;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Nivel;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PerUniDAO.
 * @author MV
 *
 */
public class PerUniDAOImpl{
	final static Logger logger = Logger.getLogger(PerUniDAOImpl.class);
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
	public int saveOrUpdate(PerUni per_uni) {
		if (per_uni.getId() != null) {
			// update
			String sql = "UPDATE col_per_uni "
						+ "SET id_cpa=?, "
						+ "nump=?, "
						+ "numu_ini=?, "
						+ "numu_fin=?, "
						+ "est=?,usr_act=?,fec_act=?, "
						+ "fec_ini=?, fec_fin=?, fec_ini_ing=?, fec_fin_ing=?, id_anio=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						per_uni.getId_cpa(),
						per_uni.getNump(),
						per_uni.getNumu_ini(),
						per_uni.getNumu_fin(),
						per_uni.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						per_uni.getFec_ini(),
						per_uni.getFec_fin(),
						per_uni.getFec_ini_ing(),
						per_uni.getFec_fin_ing(),
						per_uni.getId_anio(),
						per_uni.getId()); 
			return per_uni.getId();

		} else {
			// insert
			String sql = "insert into col_per_uni ("
						+ "id_cpa, "
						+ "nump, "
						+ "numu_ini, "
						+ "numu_fin, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "fec_ini_ing, "
						+ "fec_fin_ing, "
						+ "id_anio, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ? , ? , ? , ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				per_uni.getId_cpa(),
				per_uni.getNump(),
				per_uni.getNumu_ini(),
				per_uni.getNumu_fin(),
				per_uni.getFec_ini(),
				per_uni.getFec_fin(),
				per_uni.getFec_ini_ing(),
				per_uni.getFec_fin_ing(),
				per_uni.getId_anio(),
				per_uni.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_per_uni where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PerUni> list() {
		String sql = "select * from col_per_uni";
		
		//logger.info(sql);
		
		List<PerUni> listPerUni = jdbcTemplate.query(sql, new RowMapper<PerUni>() {

			@Override
			public PerUni mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPerUni;
	}

	public PerUni get(int id) {
		String sql = "select * from col_per_uni WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUni>() {

			@Override
			public PerUni extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PerUni getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpu.id cpu_id, cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu cpu_numu  ,cpu.est cpu_est ";
		if (aTablas.contains("cat_per_aca_nivel"))
			sql = sql + ", cpan.id cpan_id  , cpan.id_niv cpan_id_niv , cpan.id_cpa cpan_id_cpa  ";
	
		sql = sql + " from col_per_uni cpu "; 
		if (aTablas.contains("cat_per_aca_nivel"))
			sql = sql + " left join cat_per_aca_nivel cpan on cpan.id = cpu.id_cpa ";
		sql = sql + " where cpu.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUni>() {
		
			@Override
			public PerUni extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PerUni peruni= rsToEntity(rs,"cpu_");
					if (aTablas.contains("cat_per_aca_nivel")){
						PerAcaNivel peracanivel = new PerAcaNivel();  
							peracanivel.setId(rs.getInt("cpan_id")) ;  
							peracanivel.setId_niv(rs.getInt("cpan_id_niv")) ;  
							peracanivel.setId_cpa(rs.getInt("cpan_id_cpa")) ;  
							peruni.setPerAcaNivel(peracanivel);
					}
							return peruni;
				}
				
				return null;
			}
			
		});


	}		
	
	public PerUni getByParams(Param param) {

		String sql = "select * from col_per_uni " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PerUni>() {
			@Override
			public PerUni extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PerUni> listByParams(Param param, String[] order) {

		String sql = "select * from col_per_uni " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerUni>() {

			@Override
			public PerUni mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PerUni> listFullByParams(PerUni peruni, String[] order) {
	
		return listFullByParams(Param.toParam("cpu",peruni), order);
	
	}	
	
	public List<PerUni> listFullByParams(Param param, String[] order) {

		String sql = "select cpu.id cpu_id, cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini, cpu.numu_fin cpu_numu_fin ,cpu.est cpu_est, cpu.fec_ini cpu_fec_ini, cpu.fec_fin cpu_fec_fin,  cpu.fec_ini_ing cpu_fec_ini_ing, cpu.fec_fin_ing cpu_fec_fin_ing, cpu.id_anio cpu_id_anio, cpu.id_gir cpu_id_gir ";
		sql = sql + ", cpan.id cpan_id  , cpan.id_niv cpan_id_niv , cpan.id_cpa cpan_id_cpa  ";
		sql = sql + ", niv.id niv_id , niv.nom niv_nom";
		sql = sql + ", cpa.id cpa_id, cpa.nom cpa_nom ";
		sql = sql + ", gir.id gir_id, gir.nom gir_nom ";
		sql = sql + " from col_per_uni cpu";
		sql = sql + " left join cat_per_aca_nivel cpan on cpan.id = cpu.id_cpa ";
		sql = sql + " left join cat_nivel niv on cpan.id_niv=niv.id ";
		sql = sql + " left join cat_periodo_aca cpa on cpa.id=cpan.id_cpa ";
		sql = sql + " left join ges_giro_negocio gir on gir.id=cpan.id_gir ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<PerUni>() {

			@Override
			public PerUni mapRow(ResultSet rs, int rowNum) throws SQLException {
				PerUni peruni= rsToEntity(rs,"cpu_");
				PerAcaNivel peracanivel = new PerAcaNivel();  
				peracanivel.setId(rs.getInt("cpan_id")) ;  
				peracanivel.setId_niv(rs.getInt("cpan_id_niv")) ;  
				peracanivel.setId_cpa(rs.getInt("cpan_id_cpa")) ;  
				Nivel nivel = new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				nivel.setNom(rs.getString("niv_nom"));
				peruni.setNivel(nivel);
				peruni.setPerAcaNivel(peracanivel);
				PeriodoAca periodoaca = new PeriodoAca();
				periodoaca.setId(rs.getInt("cpa_id"));
				periodoaca.setNom(rs.getString("cpa_nom"));
				peruni.setPeriodoAca(periodoaca);
				GiroNegocio giroNegocio = new GiroNegocio();
				giroNegocio.setId(rs.getInt("gir_id"));
				giroNegocio.setNom(rs.getString("gir_nom"));
				peruni.setGiroNegocio(giroNegocio);
				return peruni;
			}

		});

	}	


	public List<CursoUnidad> getListCursoUnidad(Param param, String[] order) {
		String sql = "select * from col_curso_unidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoUnidad curso_unidad = new CursoUnidad();

				curso_unidad.setId(rs.getInt("id"));
				curso_unidad.setId_niv(rs.getInt("id_niv"));
				curso_unidad.setId_gra(rs.getInt("id_gra"));
				curso_unidad.setId_cur(rs.getInt("id_cur"));
				curso_unidad.setId_cpu(rs.getInt("id_cpu"));
				curso_unidad.setNum(rs.getInt("num"));
				curso_unidad.setNom(rs.getString("nom"));
				curso_unidad.setDes(rs.getString("des"));
				//curso_unidad.setNro_sem(rs.getInt("nro_sem"));
				curso_unidad.setProducto(rs.getString("producto"));
				curso_unidad.setEst(rs.getString("est"));
												
				return curso_unidad;
			}

		});	
	}


	// funciones privadas utilitarias para PerUni

	private PerUni rsToEntity(ResultSet rs,String alias) throws SQLException {
		PerUni per_uni = new PerUni();

		per_uni.setId(rs.getInt( alias + "id"));
		per_uni.setId_cpa(rs.getInt( alias + "id_cpa"));
		per_uni.setNump(rs.getInt( alias + "nump"));
		per_uni.setNumu_ini(rs.getInt( alias + "numu_ini"));
		per_uni.setNumu_fin(rs.getInt( alias + "numu_fin"));
		per_uni.setEst(rs.getString( alias + "est"));
		per_uni.setFec_ini(rs.getDate(alias + "fec_ini"));
		per_uni.setFec_fin(rs.getDate(alias + "fec_fin"));
		per_uni.setId_anio(rs.getInt(alias + "id_anio"));
		per_uni.setId_gir(rs.getInt(alias + "id_gir"));
		per_uni.setFec_ini_ing(rs.getDate(alias + "fec_ini_ing"));
		per_uni.setFec_fin_ing(rs.getDate(alias + "fec_fin_ing"));
		return per_uni;

	}
	
}
