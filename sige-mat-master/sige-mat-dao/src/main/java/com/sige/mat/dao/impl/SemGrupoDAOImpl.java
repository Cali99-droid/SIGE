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
import com.tesla.colegio.model.SemGrupo;

import com.tesla.colegio.model.Seminario;
import com.tesla.colegio.model.SemInscripcion;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SemGrupoDAO.
 * @author MV
 *
 */
public class SemGrupoDAOImpl{
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
	public int saveOrUpdate(SemGrupo sem_grupo) {
		if (sem_grupo.getId() != null) {
			// update
			String sql = "UPDATE col_sem_grupo "
						+ "SET id_sem=?, "
						+ "nom=?, "
						+ "nro=?, "
						+ "cap=?, "
						+ "fec=?, "
						+ "hor_ing=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						sem_grupo.getId_sem(),
						sem_grupo.getNom(),
						sem_grupo.getNro(),
						sem_grupo.getCap(),
						sem_grupo.getFec(),
						sem_grupo.getHor_ing(),
						sem_grupo.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						sem_grupo.getId()); 
			return sem_grupo.getId();

		} else {
			// insert
			String sql = "insert into col_sem_grupo ("
						+ "id_sem, "
						+ "nom, "
						+ "nro, "
						+ "cap, "
						+ "fec, "
						+ "hor_ing, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				sem_grupo.getId_sem(),
				sem_grupo.getNom(),
				sem_grupo.getNro(),
				sem_grupo.getCap(),
				sem_grupo.getFec(),
				sem_grupo.getHor_ing(),
				sem_grupo.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sem_grupo where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SemGrupo> list() {
		String sql = "select * from col_sem_grupo";
		
		System.out.println(sql);
		
		List<SemGrupo> listSemGrupo = jdbcTemplate.query(sql, new RowMapper<SemGrupo>() {

			@Override
			public SemGrupo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSemGrupo;
	}

	public SemGrupo get(int id) {
		String sql = "select * from col_sem_grupo WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemGrupo>() {

			@Override
			public SemGrupo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SemGrupo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gru.id gru_id, gru.id_sem gru_id_sem , gru.nom gru_nom , gru.nro gru_nro , gru.cap gru_cap , gru.fec gru_fec , gru.hor_ing gru_hor_ing  ,gru.est gru_est ";
		if (aTablas.contains("col_seminario"))
			sql = sql + ", sem.id sem_id  , sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ";
	
		sql = sql + " from col_sem_grupo gru "; 
		if (aTablas.contains("col_seminario"))
			sql = sql + " left join col_seminario sem on sem.id = gru.id_sem ";
		sql = sql + " where gru.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SemGrupo>() {
		
			@Override
			public SemGrupo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SemGrupo semgrupo= rsToEntity(rs,"gru_");
					if (aTablas.contains("col_seminario")){
						Seminario seminario = new Seminario();  
							seminario.setId(rs.getInt("sem_id")) ;  
							seminario.setId_anio(rs.getInt("sem_id_anio")) ;  
							seminario.setNom(rs.getString("sem_nom")) ;  
							seminario.setCorr_envio(rs.getString("sem_corr_envio")) ;  
							seminario.setFec(rs.getDate("sem_fec")) ;  
							seminario.setFec_ini_ins(rs.getDate("sem_fec_ini_ins")) ;  
							seminario.setFec_fin_ins(rs.getDate("sem_fec_fin_ins")) ;  
							seminario.setCosto(rs.getString("sem_costo")) ;  
							seminario.setMonto(rs.getBigDecimal("sem_monto")) ;  
							semgrupo.setSeminario(seminario);
					}
							return semgrupo;
				}
				
				return null;
			}
			
		});


	}		
	
	public SemGrupo getByParams(Param param) {

		String sql = "select * from col_sem_grupo " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemGrupo>() {
			@Override
			public SemGrupo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SemGrupo> listByParams(Param param, String[] order) {

		String sql = "select * from col_sem_grupo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemGrupo>() {

			@Override
			public SemGrupo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SemGrupo> listFullByParams(SemGrupo semgrupo, String[] order) {
	
		return listFullByParams(Param.toParam("gru",semgrupo), order);
	
	}	
	
	public List<SemGrupo> listFullByParams(Param param, String[] order) {

		String sql = "select gru.id gru_id, gru.id_sem gru_id_sem , gru.nom gru_nom , gru.nro gru_nro , gru.cap gru_cap , gru.fec gru_fec , gru.hor_ing gru_hor_ing  ,gru.est gru_est ";
		sql = sql + ", sem.id sem_id  , sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ";
		sql = sql + " from col_sem_grupo gru";
		sql = sql + " left join col_seminario sem on sem.id = gru.id_sem ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemGrupo>() {

			@Override
			public SemGrupo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemGrupo semgrupo= rsToEntity(rs,"gru_");
				Seminario seminario = new Seminario();  
				seminario.setId(rs.getInt("sem_id")) ;  
				seminario.setId_anio(rs.getInt("sem_id_anio")) ;  
				seminario.setNom(rs.getString("sem_nom")) ;  
				seminario.setCorr_envio(rs.getString("sem_corr_envio")) ;  
				seminario.setFec(rs.getDate("sem_fec")) ;  
				seminario.setFec_ini_ins(rs.getDate("sem_fec_ini_ins")) ;  
				seminario.setFec_fin_ins(rs.getDate("sem_fec_fin_ins")) ;  
				seminario.setCosto(rs.getString("sem_costo")) ;  
				seminario.setMonto(rs.getBigDecimal("sem_monto")) ;  
				semgrupo.setSeminario(seminario);
				return semgrupo;
			}

		});

	}	


	public List<SemInscripcion> getListSemInscripcion(Param param, String[] order) {
		String sql = "select * from col_sem_inscripcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<SemInscripcion>() {

			@Override
			public SemInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemInscripcion sem_inscripcion = new SemInscripcion();

				sem_inscripcion.setId(rs.getInt("id"));
				sem_inscripcion.setId_sem(rs.getInt("id_sem"));
				sem_inscripcion.setId_gru(rs.getInt("id_gru"));
				sem_inscripcion.setId_dist(rs.getInt("id_dist"));
				sem_inscripcion.setNro_dni(rs.getInt("nro_dni"));
				sem_inscripcion.setApe_pat(rs.getString("ape_pat"));
				sem_inscripcion.setApe_mat(rs.getString("ape_mat"));
				sem_inscripcion.setNom(rs.getString("nom"));
				sem_inscripcion.setCorr(rs.getString("corr"));
				sem_inscripcion.setCol(rs.getString("col"));
				sem_inscripcion.setFlag_pago(rs.getString("flag_pago"));
				sem_inscripcion.setEst(rs.getString("est"));
												
				return sem_inscripcion;
			}

		});	
	}


	// funciones privadas utilitarias para SemGrupo

	private SemGrupo rsToEntity(ResultSet rs,String alias) throws SQLException {
		SemGrupo sem_grupo = new SemGrupo();

		sem_grupo.setId(rs.getInt( alias + "id"));
		sem_grupo.setId_sem(rs.getInt( alias + "id_sem"));
		sem_grupo.setNom(rs.getString( alias + "nom"));
		sem_grupo.setNro(rs.getInt( alias + "nro"));
		sem_grupo.setCap(rs.getInt( alias + "cap"));
		sem_grupo.setFec(rs.getDate( alias + "fec"));
		sem_grupo.setHor_ing(rs.getString( alias + "hor_ing"));
		sem_grupo.setEst(rs.getString( alias + "est"));
								
		return sem_grupo;

	}
	
}
