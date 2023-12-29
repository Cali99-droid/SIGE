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
import com.tesla.colegio.model.SemInscripcion;

import com.tesla.colegio.model.Seminario;
import com.tesla.colegio.model.SemGrupo;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.SemPago;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SemInscripcionDAO.
 * @author MV
 *
 */
public class SemInscripcionDAOImpl{
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
	public int saveOrUpdate(SemInscripcion sem_inscripcion) {
		if (sem_inscripcion.getId() != null) {
			// update
			String sql = "UPDATE col_sem_inscripcion "
						+ "SET id_sem=?, "
						+ "id_gru=?, "
						+ "id_dist=?, "
						+ "nro_dni=?, "
						+ "ape_pat=?, "
						+ "ape_mat=?, "
						+ "nom=?, "
						+ "corr=?, "
						+ "col=?, "
						+ "flag_pago=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						sem_inscripcion.getId_sem(),
						sem_inscripcion.getId_gru(),
						sem_inscripcion.getId_dist(),
						sem_inscripcion.getNro_dni(),
						sem_inscripcion.getApe_pat(),
						sem_inscripcion.getApe_mat(),
						sem_inscripcion.getNom(),
						sem_inscripcion.getCorr(),
						sem_inscripcion.getCol(),
						sem_inscripcion.getFlag_pago(),
						sem_inscripcion.getEst(),
						1,
						new java.util.Date(),
						sem_inscripcion.getId()); 
			return sem_inscripcion.getId();

		} else {
			// insert
			String sql = "insert into col_sem_inscripcion ("
						+ "id_sem, "
						+ "id_gru, "
						+ "id_dist, "
						+ "nro_dni, "
						+ "ape_pat, "
						+ "ape_mat, "
						+ "nom, "
						+ "corr, "
						+ "col, "
						+ "flag_pago, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				sem_inscripcion.getId_sem(),
				sem_inscripcion.getId_gru(),
				sem_inscripcion.getId_dist(),
				sem_inscripcion.getNro_dni(),
				sem_inscripcion.getApe_pat(),
				sem_inscripcion.getApe_mat(),
				sem_inscripcion.getNom(),
				sem_inscripcion.getCorr(),
				sem_inscripcion.getCol(),
				sem_inscripcion.getFlag_pago(),
				sem_inscripcion.getEst(),
				1,
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_sem_inscripcion where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SemInscripcion> list() {
		String sql = "select * from col_sem_inscripcion";
		
		System.out.println(sql);
		
		List<SemInscripcion> listSemInscripcion = jdbcTemplate.query(sql, new RowMapper<SemInscripcion>() {

			@Override
			public SemInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSemInscripcion;
	}

	public SemInscripcion get(int id) {
		String sql = "select * from col_sem_inscripcion WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemInscripcion>() {

			@Override
			public SemInscripcion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SemInscripcion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select semins.id semins_id, semins.id_sem semins_id_sem , semins.id_gru semins_id_gru , semins.id_dist semins_id_dist , semins.nro_dni semins_nro_dni , semins.ape_pat semins_ape_pat , semins.ape_mat semins_ape_mat , semins.nom semins_nom , semins.corr semins_corr , semins.col semins_col , semins.flag_pago semins_flag_pago  ,semins.est semins_est ";
		if (aTablas.contains("col_seminario"))
			sql = sql + ", sem.id sem_id  , sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ";
		if (aTablas.contains("col_sem_grupo"))
			sql = sql + ", gru.id gru_id  , gru.id_sem gru_id_sem , gru.nom gru_nom , gru.nro gru_nro , gru.cap gru_cap , gru.fec gru_fec , gru.hor_ing gru_hor_ing  ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
	
		sql = sql + " from col_sem_inscripcion semins "; 
		if (aTablas.contains("col_seminario"))
			sql = sql + " left join col_seminario sem on sem.id = semins.id_sem ";
		if (aTablas.contains("col_sem_grupo"))
			sql = sql + " left join col_sem_grupo gru on gru.id = semins.id_gru ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + " left join cat_distrito dist on dist.id = semins.id_dist ";
		sql = sql + " where semins.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SemInscripcion>() {
		
			@Override
			public SemInscripcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SemInscripcion seminscripcion= rsToEntity(rs,"semins_");
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
							seminscripcion.setSeminario(seminario);
					}
					if (aTablas.contains("col_sem_grupo")){
						SemGrupo semgrupo = new SemGrupo();  
							semgrupo.setId(rs.getInt("gru_id")) ;  
							semgrupo.setId_sem(rs.getInt("gru_id_sem")) ;  
							semgrupo.setNom(rs.getString("gru_nom")) ;  
							semgrupo.setNro(rs.getInt("gru_nro")) ;  
							semgrupo.setCap(rs.getInt("gru_cap")) ;  
							semgrupo.setFec(rs.getDate("gru_fec")) ;  
							semgrupo.setHor_ing(rs.getString("gru_hor_ing")) ;  
							seminscripcion.setSemGrupo(semgrupo);
					}
					if (aTablas.contains("cat_distrito")){
						Distrito distrito = new Distrito();  
							distrito.setId(rs.getInt("dist_id")) ;  
							distrito.setNom(rs.getString("dist_nom")) ;  
							//distrito.setCod(rs.getString("dist_cod")) ;  
							distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
							seminscripcion.setDistrito(distrito);
					}
							return seminscripcion;
				}
				
				return null;
			}
			
		});


	}		
	
	public SemInscripcion getByParams(Param param) {

		String sql = "select * from col_sem_inscripcion " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SemInscripcion>() {
			@Override
			public SemInscripcion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SemInscripcion> listByParams(Param param, String[] order) {

		String sql = "select * from col_sem_inscripcion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemInscripcion>() {

			@Override
			public SemInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SemInscripcion> listFullByParams(SemInscripcion seminscripcion, String[] order) {
	
		return listFullByParams(Param.toParam("semins",seminscripcion), order);
	
	}	
	
	public List<SemInscripcion> listFullByParams(Param param, String[] order) {

		String sql = "select semins.id semins_id, semins.id_sem semins_id_sem , semins.id_gru semins_id_gru , semins.id_dist semins_id_dist , semins.nro_dni semins_nro_dni , semins.ape_pat semins_ape_pat , semins.ape_mat semins_ape_mat , semins.nom semins_nom , semins.corr semins_corr , semins.col semins_col , semins.flag_pago semins_flag_pago  ,semins.est semins_est ";
		sql = sql + ", sem.id sem_id  , sem.id_anio sem_id_anio , sem.nom sem_nom , sem.corr_envio sem_corr_envio , sem.fec sem_fec , sem.fec_ini_ins sem_fec_ini_ins , sem.fec_fin_ins sem_fec_fin_ins , sem.costo sem_costo , sem.monto sem_monto  ";
		sql = sql + ", gru.id gru_id  , gru.id_sem gru_id_sem , gru.nom gru_nom , gru.nro gru_nro , gru.cap gru_cap , gru.fec gru_fec , gru.hor_ing gru_hor_ing  ";
		sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
		sql = sql + " from col_sem_inscripcion semins";
		sql = sql + " left join col_seminario sem on sem.id = semins.id_sem ";
		sql = sql + " left join col_sem_grupo gru on gru.id = semins.id_gru ";
		sql = sql + " left join cat_distrito dist on dist.id = semins.id_dist ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SemInscripcion>() {

			@Override
			public SemInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemInscripcion seminscripcion= rsToEntity(rs,"semins_");
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
				seminscripcion.setSeminario(seminario);
				SemGrupo semgrupo = new SemGrupo();  
				semgrupo.setId(rs.getInt("gru_id")) ;  
				semgrupo.setId_sem(rs.getInt("gru_id_sem")) ;  
				semgrupo.setNom(rs.getString("gru_nom")) ;  
				semgrupo.setNro(rs.getInt("gru_nro")) ;  
				semgrupo.setCap(rs.getInt("gru_cap")) ;  
				semgrupo.setFec(rs.getDate("gru_fec")) ;  
				semgrupo.setHor_ing(rs.getString("gru_hor_ing")) ;  
				seminscripcion.setSemGrupo(semgrupo);
				Distrito distrito = new Distrito();  
				distrito.setId(rs.getInt("dist_id")) ;  
				distrito.setNom(rs.getString("dist_nom")) ;  
				//distrito.setCod(rs.getString("dist_cod")) ;  
				distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
				seminscripcion.setDistrito(distrito);
				return seminscripcion;
			}

		});

	}	


	public List<SemPago> getListSemPago(Param param, String[] order) {
		String sql = "select * from col_sem_pago " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<SemPago>() {

			@Override
			public SemPago mapRow(ResultSet rs, int rowNum) throws SQLException {
				SemPago sem_pago = new SemPago();

				sem_pago.setId(rs.getInt("id"));
				sem_pago.setId_semins(rs.getInt("id_semins"));
				sem_pago.setMonto(rs.getBigDecimal("monto"));
				sem_pago.setEst(rs.getString("est"));
												
				return sem_pago;
			}

		});	
	}


	// funciones privadas utilitarias para SemInscripcion

	private SemInscripcion rsToEntity(ResultSet rs,String alias) throws SQLException {
		SemInscripcion sem_inscripcion = new SemInscripcion();

		sem_inscripcion.setId(rs.getInt( alias + "id"));
		sem_inscripcion.setId_sem(rs.getInt( alias + "id_sem"));
		sem_inscripcion.setId_gru(rs.getInt( alias + "id_gru"));
		sem_inscripcion.setId_dist(rs.getInt( alias + "id_dist"));
		sem_inscripcion.setNro_dni(rs.getInt( alias + "nro_dni"));
		sem_inscripcion.setApe_pat(rs.getString( alias + "ape_pat"));
		sem_inscripcion.setApe_mat(rs.getString( alias + "ape_mat"));
		sem_inscripcion.setNom(rs.getString( alias + "nom"));
		sem_inscripcion.setCorr(rs.getString( alias + "corr"));
		sem_inscripcion.setCol(rs.getString( alias + "col"));
		sem_inscripcion.setFlag_pago(rs.getString( alias + "flag_pago"));
		sem_inscripcion.setEst(rs.getString( alias + "est"));
								
		return sem_inscripcion;

	}
	
}
