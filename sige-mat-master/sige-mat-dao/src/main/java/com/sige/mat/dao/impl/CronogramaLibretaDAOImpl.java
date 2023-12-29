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
import com.tesla.colegio.model.CronogramaLibreta;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CronogramaLibretaDAO.
 * @author MV
 *
 */
public class CronogramaLibretaDAOImpl{
	final static Logger logger = Logger.getLogger(CronogramaLibretaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CronogramaLibreta cronograma_libreta) {
		if (cronograma_libreta.getId() != null) {
			// update
			String sql = "UPDATE not_cronograma_libreta "
						+ "SET id_anio=?, "
						+ "id_niv=?, "
						+ "id_cpu=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						cronograma_libreta.getId_anio(),
						cronograma_libreta.getId_niv(),
						cronograma_libreta.getId_cpu(),
						cronograma_libreta.getFec_ini(),
						cronograma_libreta.getFec_fin(),
						cronograma_libreta.getEst(),
						cronograma_libreta.getUsr_act(),
						new java.util.Date(),
						cronograma_libreta.getId()); 
			return cronograma_libreta.getId();

		} else {
			// insert
			String sql = "insert into not_cronograma_libreta ("
						+ "id_anio, "
						+ "id_niv, "
						+ "id_cpu, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				cronograma_libreta.getId_anio(),
				cronograma_libreta.getId_niv(),
				cronograma_libreta.getId_cpu(),
				cronograma_libreta.getFec_ini(),
				cronograma_libreta.getFec_fin(),
				cronograma_libreta.getEst(),
				cronograma_libreta.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_cronograma_libreta where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CronogramaLibreta> list() {
		String sql = "select * from not_cronograma_libreta";
		
		//logger.info(sql);
		
		List<CronogramaLibreta> listCronogramaLibreta = jdbcTemplate.query(sql, new RowMapper<CronogramaLibreta>() {

			@Override
			public CronogramaLibreta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCronogramaLibreta;
	}

	public CronogramaLibreta get(int id) {
		String sql = "select * from not_cronograma_libreta WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaLibreta>() {

			@Override
			public CronogramaLibreta extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CronogramaLibreta getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ncl.id ncl_id, ncl.id_anio ncl_id_anio , ncl.id_niv ncl_id_niv , ncl.fec_ini ncl_fec_ini , ncl.fec_fin ncl_fec_fin  ,ncl.est ncl_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
	
		sql = sql + " from not_cronograma_libreta ncl "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = ncl.id_anio ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = ncl.id_niv ";
		sql = sql + " where ncl.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaLibreta>() {
		
			@Override
			public CronogramaLibreta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CronogramaLibreta cronogramalibreta= rsToEntity(rs,"ncl_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cronogramalibreta.setAnio(anio);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							cronogramalibreta.setNivel(nivel);
					}
					
							return cronogramalibreta;
				}
				
				return null;
			}
			
		});


	}		
	
	public CronogramaLibreta getByParams(Param param) {

		String sql = "select * from not_cronograma_libreta " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaLibreta>() {
			@Override
			public CronogramaLibreta extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CronogramaLibreta> listByParams(Param param, String[] order) {

		String sql = "select * from not_cronograma_libreta " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CronogramaLibreta>() {

			@Override
			public CronogramaLibreta mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CronogramaLibreta> listFullByParams(CronogramaLibreta cronogramalibreta, String[] order) {
	
		return listFullByParams(Param.toParam("ncl",cronogramalibreta), order);
	
	}	
	
	public List<CronogramaLibreta> listFullByParams(Param param, String[] order) {

		String sql = "select ncl.id ncl_id, ncl.id_anio ncl_id_anio , ncl.id_niv ncl_id_niv , ncl.id_cpu ncl_id_cpu , ncl.fec_ini ncl_fec_ini , ncl.fec_fin ncl_fec_fin  ,ncl.est ncl_est,ncl.id_cpu ncl_id_cpu";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", cpa.nom cpa_nom ,cpu.nump cpu_nump";
		sql = sql + " from not_cronograma_libreta ncl";
		sql = sql + " left join col_anio anio on anio.id = ncl.id_anio ";
		sql = sql + " left join cat_nivel niv on niv.id = ncl.id_niv ";
		sql = sql + " left join col_per_uni cpu on cpu.id = ncl.id_cpu ";
		sql = sql + " left join cat_per_aca_nivel cpan on cpan.id = cpu.id_cpa ";
		sql = sql + " left join cat_periodo_aca cpa on cpa.id = cpan.id_cpa ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CronogramaLibreta>() {

			@Override
			public CronogramaLibreta mapRow(ResultSet rs, int rowNum) throws SQLException {
				CronogramaLibreta cronogramalibreta= rsToEntity(rs,"ncl_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;
				anio.setNom(rs.getString("anio_nom")) ;
				cronogramalibreta.setAnio(anio);
				
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;
				nivel.setNom(rs.getString("niv_nom")) ;
				cronogramalibreta.setNivel(nivel);
				
				PerUni per_Uni = new PerUni();
				per_Uni.setNump(rs.getInt("cpu_nump")) ;
				
				PeriodoAca periodoAca = new PeriodoAca();
				periodoAca.setNom(rs.getString("cpa_nom")) ;
				
				per_Uni.setPeriodoAca(periodoAca);
			
				cronogramalibreta.setPerUni(per_Uni);
				
				

				return cronogramalibreta;
			}

		});

	}	



	// funciones privadas utilitarias para CronogramaLibreta

	private CronogramaLibreta rsToEntity(ResultSet rs,String alias) throws SQLException {
		CronogramaLibreta cronograma_libreta = new CronogramaLibreta();

		cronograma_libreta.setId(rs.getInt( alias + "id"));
		cronograma_libreta.setId_anio(rs.getInt( alias + "id_anio"));
		cronograma_libreta.setId_niv(rs.getInt( alias + "id_niv"));
		cronograma_libreta.setId_cpu(rs.getInt(alias + "id_cpu"));
		cronograma_libreta.setFec_ini(rs.getDate( alias + "fec_ini"));
		cronograma_libreta.setFec_fin(rs.getDate( alias + "fec_fin"));
		cronograma_libreta.setEst(rs.getString( alias + "est"));
								
		return cronograma_libreta;

	}
	
}
