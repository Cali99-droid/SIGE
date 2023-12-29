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
import com.tesla.colegio.model.PeriodoAulaVirtual;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PeriodoCurso;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PeriodoAulaVirtualDAO.
 * @author MV
 *
 */
public class PeriodoAulaVirtualDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PeriodoAulaVirtual periodo_aula_virtual) {
		if (periodo_aula_virtual.getId() != null) {
			// update
			String sql = "UPDATE cvi_periodo_aula_virtual "
						+ "SET id_anio=?, "
						+ "id_cpa=?, "
						+ "id_niv=?, "
						+ "abrev=?, "
						+ "nro_per=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						periodo_aula_virtual.getId_anio(),
						periodo_aula_virtual.getId_cpa(),
						periodo_aula_virtual.getId_niv(),
						periodo_aula_virtual.getAbrev(),
						periodo_aula_virtual.getNro_per(),
						periodo_aula_virtual.getEst(),
						periodo_aula_virtual.getUsr_act(),
						new java.util.Date(),
						periodo_aula_virtual.getId()); 
			return periodo_aula_virtual.getId();

		} else {
			// insert
			String sql = "insert into cvi_periodo_aula_virtual ("
						+ "id_anio, "
						+ "id_cpa, "
						+ "id_niv, "
						+ "abrev, "
						+ "nro_per, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				periodo_aula_virtual.getId_anio(),
				periodo_aula_virtual.getId_cpa(),
				periodo_aula_virtual.getId_niv(),
				periodo_aula_virtual.getAbrev(),
				periodo_aula_virtual.getNro_per(),
				periodo_aula_virtual.getEst(),
				periodo_aula_virtual.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_periodo_aula_virtual where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PeriodoAulaVirtual> list() {
		String sql = "select * from cvi_periodo_aula_virtual";
		
		//System.out.println(sql);
		
		List<PeriodoAulaVirtual> listPeriodoAulaVirtual = jdbcTemplate.query(sql, new RowMapper<PeriodoAulaVirtual>() {

			@Override
			public PeriodoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodoAulaVirtual;
	}

	public PeriodoAulaVirtual get(int id) {
		String sql = "select * from cvi_periodo_aula_virtual WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAulaVirtual>() {

			@Override
			public PeriodoAulaVirtual extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PeriodoAulaVirtual getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpv.id cpv_id, cpv.id_anio cpv_id_anio , cpv.id_cpa cpv_id_cpa , cpv.id_niv cpv_id_niv , cpv.abrev cpv_abrev , cpv.nro_per cpv_nro_per  ,cpv.est cpv_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_periodo_aca"))
			sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
	
		sql = sql + " from cvi_periodo_aula_virtual cpv "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cpv.id_anio ";
		if (aTablas.contains("cat_periodo_aca"))
			sql = sql + " left join cat_periodo_aca cpa on cpa.id = cpv.id_cpa ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = cpv.id_niv ";
		sql = sql + " where cpv.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAulaVirtual>() {
		
			@Override
			public PeriodoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PeriodoAulaVirtual periodoaulavirtual= rsToEntity(rs,"cpv_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							periodoaulavirtual.setAnio(anio);
					}
					if (aTablas.contains("cat_periodo_aca")){
						PeriodoAca periodoaca = new PeriodoAca();  
							periodoaca.setId(rs.getInt("cpa_id")) ;  
							periodoaca.setNom(rs.getString("cpa_nom")) ;  
							periodoaulavirtual.setPeriodoAca(periodoaca);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							periodoaulavirtual.setNivel(nivel);
					}
							return periodoaulavirtual;
				}
				
				return null;
			}
			
		});


	}		
	
	public PeriodoAulaVirtual getByParams(Param param) {

		String sql = "select * from cvi_periodo_aula_virtual " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoAulaVirtual>() {
			@Override
			public PeriodoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PeriodoAulaVirtual> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_periodo_aula_virtual " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoAulaVirtual>() {

			@Override
			public PeriodoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PeriodoAulaVirtual> listFullByParams(PeriodoAulaVirtual periodoaulavirtual, String[] order) {
	
		return listFullByParams(Param.toParam("cpv",periodoaulavirtual), order);
	
	}	
	
	public List<PeriodoAulaVirtual> listFullByParams(Param param, String[] order) {

		String sql = "select cpv.id cpv_id, cpv.id_anio cpv_id_anio , cpv.id_cpa cpv_id_cpa , cpv.id_niv cpv_id_niv , cpv.abrev cpv_abrev , cpv.nro_per cpv_nro_per  ,cpv.est cpv_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", cpa.id cpa_id  , cpa.nom cpa_nom  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + " from cvi_periodo_aula_virtual cpv";
		sql = sql + " left join col_anio anio on anio.id = cpv.id_anio ";
		sql = sql + " left join cat_periodo_aca cpa on cpa.id = cpv.id_cpa ";
		sql = sql + " left join cat_nivel niv on niv.id = cpv.id_niv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoAulaVirtual>() {

			@Override
			public PeriodoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoAulaVirtual periodoaulavirtual= rsToEntity(rs,"cpv_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				periodoaulavirtual.setAnio(anio);
				PeriodoAca periodoaca = new PeriodoAca();  
				periodoaca.setId(rs.getInt("cpa_id")) ;  
				periodoaca.setNom(rs.getString("cpa_nom")) ;  
				periodoaulavirtual.setPeriodoAca(periodoaca);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				periodoaulavirtual.setNivel(nivel);
				return periodoaulavirtual;
			}

		});

	}	


	public List<PeriodoCurso> getListPeriodoCurso(Param param, String[] order) {
		String sql = "select * from cvi_periodo_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PeriodoCurso>() {

			@Override
			public PeriodoCurso mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoCurso periodo_curso = new PeriodoCurso();

				periodo_curso.setId(rs.getInt("id"));
				periodo_curso.setId_cpv(rs.getInt("id_cpv"));
				periodo_curso.setId_cau(rs.getInt("id_cau"));
				periodo_curso.setActivo(rs.getInt("activo"));
				periodo_curso.setEst(rs.getString("est"));
												
				return periodo_curso;
			}

		});	
	}


	// funciones privadas utilitarias para PeriodoAulaVirtual

	private PeriodoAulaVirtual rsToEntity(ResultSet rs,String alias) throws SQLException {
		PeriodoAulaVirtual periodo_aula_virtual = new PeriodoAulaVirtual();

		periodo_aula_virtual.setId(rs.getInt( alias + "id"));
		periodo_aula_virtual.setId_anio(rs.getInt( alias + "id_anio"));
		periodo_aula_virtual.setId_cpa(rs.getInt( alias + "id_cpa"));
		periodo_aula_virtual.setId_niv(rs.getInt( alias + "id_niv"));
		periodo_aula_virtual.setAbrev(rs.getString( alias + "abrev"));
		periodo_aula_virtual.setNro_per(rs.getInt( alias + "nro_per"));
		periodo_aula_virtual.setEst(rs.getString( alias + "est"));
								
		return periodo_aula_virtual;

	}
	
}
