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
import com.tesla.colegio.model.RemuneracionCat;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.LineaCarrera;
import com.tesla.colegio.model.Denominacion;
import com.tesla.colegio.model.CategoriaOcupacional;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RemuneracionCatDAO.
 * @author MV
 *
 */
public class RemuneracionCatDAOImpl{
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
	public int saveOrUpdate(RemuneracionCat remuneracion_cat) {
		if (remuneracion_cat.getId() != null) {
			// update
			String sql = "UPDATE rhh_remuneracion_cat "
						+ "SET id_anio=?, "
						+ "id_lcarr=?, "
						+ "id_cden=?, "
						+ "id_cocu=?, "
						+ "rem=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						remuneracion_cat.getId_anio(),
						remuneracion_cat.getId_lcarr(),
						remuneracion_cat.getId_cden(),
						remuneracion_cat.getId_cocu(),
						remuneracion_cat.getRem(),
						remuneracion_cat.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						remuneracion_cat.getId()); 
			return remuneracion_cat.getId();

		} else {
			// insert
			String sql = "insert into rhh_remuneracion_cat ("
						+ "id_anio, "
						+ "id_lcarr, "
						+ "id_cden, "
						+ "id_cocu, "
						+ "rem, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				remuneracion_cat.getId_anio(),
				remuneracion_cat.getId_lcarr(),
				remuneracion_cat.getId_cden(),
				remuneracion_cat.getId_cocu(),
				remuneracion_cat.getRem(),
				remuneracion_cat.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from rhh_remuneracion_cat where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<RemuneracionCat> list() {
		String sql = "select * from rhh_remuneracion_cat";
		
		System.out.println(sql);
		
		List<RemuneracionCat> listRemuneracionCat = jdbcTemplate.query(sql, new RowMapper<RemuneracionCat>() {

			@Override
			public RemuneracionCat mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRemuneracionCat;
	}

	public RemuneracionCat get(int id) {
		String sql = "select * from rhh_remuneracion_cat WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RemuneracionCat>() {

			@Override
			public RemuneracionCat extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public RemuneracionCat getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rcat.id rcat_id, rcat.id_anio rcat_id_anio , rcat.id_lcarr rcat_id_lcarr , rcat.id_cden rcat_id_cden , rcat.id_cocu rcat_id_cocu , rcat.rem rcat_rem  ,rcat.est rcat_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_linea_carrera"))
			sql = sql + ", lcarr.id lcarr_id  , lcarr.nom lcarr_nom , lcarr.des lcarr_des  ";
		if (aTablas.contains("cat_denominacion"))
			sql = sql + ", cden.id cden_id  , cden.nom cden_nom , cden.des cden_des  ";
		if (aTablas.contains("cat_categoria_ocupacional"))
			sql = sql + ", cocu.id cocu_id  , cocu.nom cocu_nom , cocu.des cocu_des  ";
	
		sql = sql + " from rhh_remuneracion_cat rcat "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = rcat.id_anio ";
		if (aTablas.contains("cat_linea_carrera"))
			sql = sql + " left join cat_linea_carrera lcarr on lcarr.id = rcat.id_lcarr ";
		if (aTablas.contains("cat_denominacion"))
			sql = sql + " left join cat_denominacion cden on cden.id = rcat.id_cden ";
		if (aTablas.contains("cat_categoria_ocupacional"))
			sql = sql + " left join cat_categoria_ocupacional cocu on cocu.id = rcat.id_cocu ";
		sql = sql + " where rcat.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<RemuneracionCat>() {
		
			@Override
			public RemuneracionCat extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RemuneracionCat remuneracioncat= rsToEntity(rs,"rcat_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							remuneracioncat.setAnio(anio);
					}
					if (aTablas.contains("cat_linea_carrera")){
						LineaCarrera lineacarrera = new LineaCarrera();  
							lineacarrera.setId(rs.getInt("lcarr_id")) ;  
							lineacarrera.setNom(rs.getString("lcarr_nom")) ;  
							lineacarrera.setDes(rs.getString("lcarr_des")) ;  
							remuneracioncat.setLineaCarrera(lineacarrera);
					}
					if (aTablas.contains("cat_denominacion")){
						Denominacion denominacion = new Denominacion();  
							denominacion.setId(rs.getInt("cden_id")) ;  
							denominacion.setNom(rs.getString("cden_nom")) ;  
							denominacion.setDes(rs.getString("cden_des")) ;  
							remuneracioncat.setDenominacion(denominacion);
					}
					if (aTablas.contains("cat_categoria_ocupacional")){
						CategoriaOcupacional categoriaocupacional = new CategoriaOcupacional();  
							categoriaocupacional.setId(rs.getInt("cocu_id")) ;  
							categoriaocupacional.setNom(rs.getString("cocu_nom")) ;  
							categoriaocupacional.setDes(rs.getString("cocu_des")) ;  
							remuneracioncat.setCategoriaOcupacional(categoriaocupacional);
					}
							return remuneracioncat;
				}
				
				return null;
			}
			
		});


	}		
	
	public RemuneracionCat getByParams(Param param) {

		String sql = "select * from rhh_remuneracion_cat " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RemuneracionCat>() {
			@Override
			public RemuneracionCat extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<RemuneracionCat> listByParams(Param param, String[] order) {

		String sql = "select * from rhh_remuneracion_cat " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<RemuneracionCat>() {

			@Override
			public RemuneracionCat mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<RemuneracionCat> listFullByParams(RemuneracionCat remuneracioncat, String[] order) {
	
		return listFullByParams(Param.toParam("rcat",remuneracioncat), order);
	
	}	
	
	public List<RemuneracionCat> listFullByParams(Param param, String[] order) {

		String sql = "select rcat.id rcat_id, rcat.id_anio rcat_id_anio , rcat.id_lcarr rcat_id_lcarr , rcat.id_cden rcat_id_cden , rcat.id_cocu rcat_id_cocu , rcat.rem rcat_rem  ,rcat.est rcat_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", lcarr.id lcarr_id  , lcarr.nom lcarr_nom , lcarr.des lcarr_des  ";
		sql = sql + ", cden.id cden_id  , cden.nom cden_nom , cden.des cden_des  ";
		sql = sql + ", cocu.id cocu_id  , cocu.nom cocu_nom , cocu.des cocu_des  ";
		sql = sql + " from rhh_remuneracion_cat rcat";
		sql = sql + " left join col_anio anio on anio.id = rcat.id_anio ";
		sql = sql + " left join cat_linea_carrera lcarr on lcarr.id = rcat.id_lcarr ";
		sql = sql + " left join cat_denominacion cden on cden.id = rcat.id_cden ";
		sql = sql + " left join cat_categoria_ocupacional cocu on cocu.id = rcat.id_cocu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<RemuneracionCat>() {

			@Override
			public RemuneracionCat mapRow(ResultSet rs, int rowNum) throws SQLException {
				RemuneracionCat remuneracioncat= rsToEntity(rs,"rcat_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				remuneracioncat.setAnio(anio);
				LineaCarrera lineacarrera = new LineaCarrera();  
				lineacarrera.setId(rs.getInt("lcarr_id")) ;  
				lineacarrera.setNom(rs.getString("lcarr_nom")) ;  
				lineacarrera.setDes(rs.getString("lcarr_des")) ;  
				remuneracioncat.setLineaCarrera(lineacarrera);
				Denominacion denominacion = new Denominacion();  
				denominacion.setId(rs.getInt("cden_id")) ;  
				denominacion.setNom(rs.getString("cden_nom")) ;  
				denominacion.setDes(rs.getString("cden_des")) ;  
				remuneracioncat.setDenominacion(denominacion);
				CategoriaOcupacional categoriaocupacional = new CategoriaOcupacional();  
				categoriaocupacional.setId(rs.getInt("cocu_id")) ;  
				categoriaocupacional.setNom(rs.getString("cocu_nom")) ;  
				categoriaocupacional.setDes(rs.getString("cocu_des")) ;  
				remuneracioncat.setCategoriaOcupacional(categoriaocupacional);
				return remuneracioncat;
			}

		});

	}	




	// funciones privadas utilitarias para RemuneracionCat

	private RemuneracionCat rsToEntity(ResultSet rs,String alias) throws SQLException {
		RemuneracionCat remuneracion_cat = new RemuneracionCat();

		remuneracion_cat.setId(rs.getInt( alias + "id"));
		remuneracion_cat.setId_anio(rs.getInt( alias + "id_anio"));
		remuneracion_cat.setId_lcarr(rs.getInt( alias + "id_lcarr"));
		remuneracion_cat.setId_cden(rs.getInt( alias + "id_cden"));
		remuneracion_cat.setId_cocu(rs.getInt( alias + "id_cocu"));
		remuneracion_cat.setRem(rs.getBigDecimal( alias + "rem"));
		remuneracion_cat.setEst(rs.getString( alias + "est"));
								
		return remuneracion_cat;

	}
	
}
