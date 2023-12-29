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
import com.tesla.colegio.model.Seminario;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.SemGrupo;
import com.tesla.colegio.model.SemInscripcion;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SeminarioDAO.
 * @author MV
 *
 */
public class SeminarioDAOImpl{
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
	public int saveOrUpdate(Seminario seminario) {
		if (seminario.getId() != null) {
			// update
			String sql = "UPDATE col_seminario "
						+ "SET id_anio=?, "
						+ "nom=?, "
						+ "corr_envio=?, "
						+ "fec=?, "
						+ "fec_ini_ins=?, "
						+ "fec_fin_ins=?, "
						+ "costo=?, "
						+ "monto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						seminario.getId_anio(),
						seminario.getNom(),
						seminario.getCorr_envio(),
						seminario.getFec(),
						seminario.getFec_ini_ins(),
						seminario.getFec_fin_ins(),
						seminario.getCosto(),
						seminario.getMonto(),
						seminario.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						seminario.getId()); 
			return seminario.getId();

		} else {
			// insert
			String sql = "insert into col_seminario ("
						+ "id_anio, "
						+ "nom, "
						+ "corr_envio, "
						+ "fec, "
						+ "fec_ini_ins, "
						+ "fec_fin_ins, "
						+ "costo, "
						+ "monto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				seminario.getId_anio(),
				seminario.getNom(),
				seminario.getCorr_envio(),
				seminario.getFec(),
				seminario.getFec_ini_ins(),
				seminario.getFec_fin_ins(),
				seminario.getCosto(),
				seminario.getMonto(),
				seminario.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_seminario where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Seminario> list() {
		String sql = "select * from col_seminario";
		
		System.out.println(sql);
		
		List<Seminario> listSeminario = jdbcTemplate.query(sql, new RowMapper<Seminario>() {

			@Override
			public Seminario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSeminario;
	}

	public Seminario get(int id) {
		String sql = "select * from col_seminario WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Seminario>() {

			@Override
			public Seminario extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Seminario getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sem.id sem_id, sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ,sem.est sem_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from col_seminario sem "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = sem.id_anio ";
		sql = sql + " where sem.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Seminario>() {
		
			@Override
			public Seminario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Seminario seminario= rsToEntity(rs,"sem_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							seminario.setAnio(anio);
					}
							return seminario;
				}
				
				return null;
			}
			
		});


	}		
	
	public Seminario getByParams(Param param) {

		String sql = "select * from col_seminario " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Seminario>() {
			@Override
			public Seminario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Seminario> listByParams(Param param, String[] order) {

		String sql = "select * from col_seminario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Seminario>() {

			@Override
			public Seminario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Seminario> listFullByParams(Seminario seminario, String[] order) {
	
		return listFullByParams(Param.toParam("sem",seminario), order);
	
	}	
	
	public List<Seminario> listFullByParams(Param param, String[] order) {

		String sql = "select sem.id sem_id, sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ,sem.est sem_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from col_seminario sem";
		sql = sql + " left join col_anio anio on anio.id = sem.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Seminario>() {

			@Override
			public Seminario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Seminario seminario= rsToEntity(rs,"sem_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				seminario.setAnio(anio);
				return seminario;
			}

		});

	}	


	public List<SemGrupo> getListSemGrupo(Param param, String[] order) {
		String sql = "select * from col_sem_grupo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<SemGrupo>() {

			@Override
			public SemGrupo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemGrupo sem_grupo = new SemGrupo();

				sem_grupo.setId(rs.getInt("id"));
				sem_grupo.setId_sem(rs.getInt("id_sem"));
				sem_grupo.setNom(rs.getString("nom"));
				sem_grupo.setNro(rs.getInt("nro"));
				sem_grupo.setCap(rs.getInt("cap"));
				sem_grupo.setFec(rs.getDate("fec"));
				sem_grupo.setHor_ing(rs.getString("hor_ing"));
				sem_grupo.setEst(rs.getString("est"));
												
				return sem_grupo;
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


	// funciones privadas utilitarias para Seminario

	private Seminario rsToEntity(ResultSet rs,String alias) throws SQLException {
		Seminario seminario = new Seminario();

		seminario.setId(rs.getInt( alias + "id"));
		seminario.setId_anio(rs.getInt( alias + "id_anio"));
		seminario.setNom(rs.getString( alias + "nom"));
		seminario.setCorr_envio(rs.getString( alias + "corr_envio"));
		seminario.setFec(rs.getDate( alias + "fec"));
		seminario.setFec_ini_ins(rs.getDate( alias + "fec_ini_ins"));
		seminario.setFec_fin_ins(rs.getDate( alias + "fec_fin_ins"));
		seminario.setCosto(rs.getString( alias + "costo"));
		seminario.setMonto(rs.getBigDecimal( alias + "monto"));
		seminario.setEst(rs.getString( alias + "est"));
								
		return seminario;

	}
	
}
