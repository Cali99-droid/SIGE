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
import com.tesla.colegio.model.DcnNivel;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.DisenioCurricular;
import com.tesla.colegio.model.DcnArea;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DcnNivelDAO.
 * @author MV
 *
 */
public class DcnNivelDAOImpl{
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
	public int saveOrUpdate(DcnNivel dcn_nivel) {
		if (dcn_nivel.getId() != null) {
			// update
			String sql = "UPDATE aca_dcn_nivel "
						+ "SET id_niv=?, "
						+ "id_dcn=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						dcn_nivel.getId_niv(),
						dcn_nivel.getId_dcn(),
						dcn_nivel.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						dcn_nivel.getId()); 
			return dcn_nivel.getId();

		} else {
			// insert
			String sql = "insert into aca_dcn_nivel ("
						+ "id_niv, "
						+ "id_dcn, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				dcn_nivel.getId_niv(),
				dcn_nivel.getId_dcn(),
				dcn_nivel.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_dcn_nivel where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DcnNivel> list() {
		String sql = "select * from aca_dcn_nivel";
		
		System.out.println(sql);
		
		List<DcnNivel> listDcnNivel = jdbcTemplate.query(sql, new RowMapper<DcnNivel>() {

			@Override
			public DcnNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDcnNivel;
	}

	public DcnNivel get(int id) {
		String sql = "select * from aca_dcn_nivel WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnNivel>() {

			@Override
			public DcnNivel extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DcnNivel getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dcniv.id dcniv_id, dcniv.id_niv dcniv_id_niv , dcniv.id_dcn dcniv_id_dcn  ,dcniv.est dcniv_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("col_disenio_curricular"))
			sql = sql + ", cdc.id cdc_id  , cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ";
	
		sql = sql + " from aca_dcn_nivel dcniv "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = dcniv.id_niv ";
		if (aTablas.contains("col_disenio_curricular"))
			sql = sql + " left join col_disenio_curricular cdc on cdc.id = dcniv.id_dcn ";
		sql = sql + " where dcniv.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnNivel>() {
		
			@Override
			public DcnNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DcnNivel dcnnivel= rsToEntity(rs,"dcniv_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							dcnnivel.setNivel(nivel);
					}
					if (aTablas.contains("col_disenio_curricular")){
						DisenioCurricular diseniocurricular = new DisenioCurricular();  
							diseniocurricular.setId(rs.getInt("cdc_id")) ;  
							diseniocurricular.setId_anio(rs.getInt("cdc_id_anio")) ;  
							diseniocurricular.setNom(rs.getString("cdc_nom")) ;  
							dcnnivel.setDisenioCurricular(diseniocurricular);
					}
							return dcnnivel;
				}
				
				return null;
			}
			
		});


	}		
	
	public DcnNivel getByParams(Param param) {

		String sql = "select * from aca_dcn_nivel " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnNivel>() {
			@Override
			public DcnNivel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DcnNivel> listByParams(Param param, String[] order) {

		String sql = "select * from aca_dcn_nivel " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnNivel>() {

			@Override
			public DcnNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DcnNivel> listFullByParams(DcnNivel dcnnivel, String[] order) {
	
		return listFullByParams(Param.toParam("dcniv",dcnnivel), order);
	
	}	
	
	public List<DcnNivel> listFullByParams(Param param, String[] order) {

		String sql = "select dcniv.id dcniv_id, dcniv.id_niv dcniv_id_niv , dcniv.id_dcn dcniv_id_dcn  ,dcniv.est dcniv_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", cdc.id cdc_id  , cdc.id_anio cdc_id_anio , cdc.nom cdc_nom  ";
		sql = sql + " from aca_dcn_nivel dcniv";
		sql = sql + " left join cat_nivel niv on niv.id = dcniv.id_niv ";
		sql = sql + " left join col_disenio_curricular cdc on cdc.id = dcniv.id_dcn ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnNivel>() {

			@Override
			public DcnNivel mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnNivel dcnnivel= rsToEntity(rs,"dcniv_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				dcnnivel.setNivel(nivel);
				DisenioCurricular diseniocurricular = new DisenioCurricular();  
				diseniocurricular.setId(rs.getInt("cdc_id")) ;  
				diseniocurricular.setId_anio(rs.getInt("cdc_id_anio")) ;  
				diseniocurricular.setNom(rs.getString("cdc_nom")) ;  
				dcnnivel.setDisenioCurricular(diseniocurricular);
				return dcnnivel;
			}

		});

	}	


	public List<DcnArea> getListDcnArea(Param param, String[] order) {
		String sql = "select * from aca_dcn_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<DcnArea>() {

			@Override
			public DcnArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnArea dcn_area = new DcnArea();

				dcn_area.setId(rs.getInt("id"));
				dcn_area.setId_dcniv(rs.getInt("id_dcniv"));
				dcn_area.setId_are(rs.getInt("id_are"));
				dcn_area.setEst(rs.getString("est"));
												
				return dcn_area;
			}

		});	
	}


	// funciones privadas utilitarias para DcnNivel

	private DcnNivel rsToEntity(ResultSet rs,String alias) throws SQLException {
		DcnNivel dcn_nivel = new DcnNivel();

		dcn_nivel.setId(rs.getInt( alias + "id"));
		dcn_nivel.setId_niv(rs.getInt( alias + "id_niv"));
		dcn_nivel.setId_dcn(rs.getInt( alias + "id_dcn"));
		dcn_nivel.setEst(rs.getString( alias + "est"));
								
		return dcn_nivel;

	}
	
}
