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
import com.tesla.colegio.model.PeriodoCurso;

import com.tesla.colegio.model.PeriodoAulaVirtual;
import com.tesla.colegio.model.CursoAulaVirtual;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PeriodoCursoDAO.
 * @author MV
 *
 */
public class PeriodoCursoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(PeriodoCurso periodo_curso) {
		if (periodo_curso.getId() != null) {
			// update
			String sql = "UPDATE cvi_periodo_curso "
						+ "SET id_cpv=?, "
						+ "id_cau=?, "
						+ "activo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						periodo_curso.getId_cpv(),
						periodo_curso.getId_cau(),
						periodo_curso.getActivo(),
						periodo_curso.getEst(),
						periodo_curso.getUsr_act(),
						new java.util.Date(),
						periodo_curso.getId()); 
			return periodo_curso.getId();

		} else {
			// insert
			String sql = "insert into cvi_periodo_curso ("
						+ "id_cpv, "
						+ "id_cau, "
						+ "activo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				periodo_curso.getId_cpv(),
				periodo_curso.getId_cau(),
				periodo_curso.getActivo(),
				periodo_curso.getEst(),
				periodo_curso.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_periodo_curso where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PeriodoCurso> list() {
		String sql = "select * from cvi_periodo_curso";
		
		//System.out.println(sql);
		
		List<PeriodoCurso> listPeriodoCurso = jdbcTemplate.query(sql, new RowMapper<PeriodoCurso>() {

			@Override
			public PeriodoCurso mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodoCurso;
	}

	public PeriodoCurso get(int id) {
		String sql = "select * from cvi_periodo_curso WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoCurso>() {

			@Override
			public PeriodoCurso extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PeriodoCurso getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpc.id cpc_id, cpc.id_cpv cpc_id_cpv , cpc.id_cau cpc_id_cau , cpc.activo cpc_activo  ,cpc.est cpc_est ";
		if (aTablas.contains("cvi_periodo_aula_virtual"))
			sql = sql + ", cpv.id cpv_id  , cpv.id_anio cpv_id_anio , cpv.id_cpa cpv_id_cpa , cpv.id_niv cpv_id_niv , cpv.abrev cpv_abrev , cpv.nro_per cpv_nro_per  ";
		if (aTablas.contains("cvi_curso_aula_virtual"))
			sql = sql + ", cau.id cau_id  , cau.id_anio cau_id_anio , cau.id_gra cau_id_gra , cau.nom cau_nom , cau.abrev cau_abrev  ";
	
		sql = sql + " from cvi_periodo_curso cpc "; 
		if (aTablas.contains("cvi_periodo_aula_virtual"))
			sql = sql + " left join cvi_periodo_aula_virtual cpv on cpv.id = cpc.id_cpv ";
		if (aTablas.contains("cvi_curso_aula_virtual"))
			sql = sql + " left join cvi_curso_aula_virtual cau on cau.id = cpc.id_cau ";
		sql = sql + " where cpc.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoCurso>() {
		
			@Override
			public PeriodoCurso extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PeriodoCurso periodocurso= rsToEntity(rs,"cpc_");
					if (aTablas.contains("cvi_periodo_aula_virtual")){
						PeriodoAulaVirtual periodoaulavirtual = new PeriodoAulaVirtual();  
							periodoaulavirtual.setId(rs.getInt("cpv_id")) ;  
							periodoaulavirtual.setId_anio(rs.getInt("cpv_id_anio")) ;  
							periodoaulavirtual.setId_cpa(rs.getInt("cpv_id_cpa")) ;  
							periodoaulavirtual.setId_niv(rs.getInt("cpv_id_niv")) ;  
							periodoaulavirtual.setAbrev(rs.getString("cpv_abrev")) ;  
							periodoaulavirtual.setNro_per(rs.getInt("cpv_nro_per")) ;  
							periodocurso.setPeriodoAulaVirtual(periodoaulavirtual);
					}
					if (aTablas.contains("cvi_curso_aula_virtual")){
						CursoAulaVirtual cursoaulavirtual = new CursoAulaVirtual();  
							cursoaulavirtual.setId(rs.getInt("cau_id")) ;  
							cursoaulavirtual.setId_anio(rs.getInt("cau_id_anio")) ;  
							cursoaulavirtual.setId_gra(rs.getInt("cau_id_gra")) ;  
							cursoaulavirtual.setNom(rs.getString("cau_nom")) ;  
							cursoaulavirtual.setAbrev(rs.getString("cau_abrev")) ;  
							periodocurso.setCursoAulaVirtual(cursoaulavirtual);
					}
							return periodocurso;
				}
				
				return null;
			}
			
		});


	}		
	
	public PeriodoCurso getByParams(Param param) {

		String sql = "select * from cvi_periodo_curso " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PeriodoCurso>() {
			@Override
			public PeriodoCurso extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PeriodoCurso> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_periodo_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoCurso>() {

			@Override
			public PeriodoCurso mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PeriodoCurso> listFullByParams(PeriodoCurso periodocurso, String[] order) {
	
		return listFullByParams(Param.toParam("cpc",periodocurso), order);
	
	}	
	
	public List<PeriodoCurso> listFullByParams(Param param, String[] order) {

		String sql = "select cpc.id cpc_id, cpc.id_cpv cpc_id_cpv , cpc.id_cau cpc_id_cau , cpc.activo cpc_activo  ,cpc.est cpc_est ";
		sql = sql + ", cpv.id cpv_id  , cpv.id_anio cpv_id_anio , cpv.id_cpa cpv_id_cpa , cpv.id_niv cpv_id_niv , cpv.abrev cpv_abrev , cpv.nro_per cpv_nro_per  ";
		sql = sql + ", cau.id cau_id  , cau.id_anio cau_id_anio , cau.id_gra cau_id_gra , cau.nom cau_nom , cau.abrev cau_abrev  ";
		sql = sql + " from cvi_periodo_curso cpc";
		sql = sql + " left join cvi_periodo_aula_virtual cpv on cpv.id = cpc.id_cpv ";
		sql = sql + " left join cvi_curso_aula_virtual cau on cau.id = cpc.id_cau ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PeriodoCurso>() {

			@Override
			public PeriodoCurso mapRow(ResultSet rs, int rowNum) throws SQLException {
				PeriodoCurso periodocurso= rsToEntity(rs,"cpc_");
				PeriodoAulaVirtual periodoaulavirtual = new PeriodoAulaVirtual();  
				periodoaulavirtual.setId(rs.getInt("cpv_id")) ;  
				periodoaulavirtual.setId_anio(rs.getInt("cpv_id_anio")) ;  
				periodoaulavirtual.setId_cpa(rs.getInt("cpv_id_cpa")) ;  
				periodoaulavirtual.setId_niv(rs.getInt("cpv_id_niv")) ;  
				periodoaulavirtual.setAbrev(rs.getString("cpv_abrev")) ;  
				periodoaulavirtual.setNro_per(rs.getInt("cpv_nro_per")) ;  
				periodocurso.setPeriodoAulaVirtual(periodoaulavirtual);
				CursoAulaVirtual cursoaulavirtual = new CursoAulaVirtual();  
				cursoaulavirtual.setId(rs.getInt("cau_id")) ;  
				cursoaulavirtual.setId_anio(rs.getInt("cau_id_anio")) ;  
				cursoaulavirtual.setId_gra(rs.getInt("cau_id_gra")) ;  
				cursoaulavirtual.setNom(rs.getString("cau_nom")) ;  
				cursoaulavirtual.setAbrev(rs.getString("cau_abrev")) ;  
				periodocurso.setCursoAulaVirtual(cursoaulavirtual);
				return periodocurso;
			}

		});

	}	




	// funciones privadas utilitarias para PeriodoCurso

	private PeriodoCurso rsToEntity(ResultSet rs,String alias) throws SQLException {
		PeriodoCurso periodo_curso = new PeriodoCurso();

		periodo_curso.setId(rs.getInt( alias + "id"));
		periodo_curso.setId_cpv(rs.getInt( alias + "id_cpv"));
		periodo_curso.setId_cau(rs.getInt( alias + "id_cau"));
		periodo_curso.setActivo(rs.getInt( alias + "activo"));
		periodo_curso.setEst(rs.getString( alias + "est"));
								
		return periodo_curso;

	}
	
}
