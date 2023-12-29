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
import com.tesla.colegio.model.RecuperacionArea;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Area;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface RecuperacionAreaDAO.
 * @author MV
 *
 */
public class RecuperacionAreaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(RecuperacionArea recuperacion_area) {
		if (recuperacion_area.getId() != null) {
			// update
			String sql = "UPDATE mat_recuperacion_area "
						+ "SET id_mat=?, "
						+ "id_area=?, "
						+ "prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						recuperacion_area.getId_mat(),
						recuperacion_area.getId_area(),
						recuperacion_area.getProm(),
						recuperacion_area.getEst(),
						recuperacion_area.getUsr_act(),
						new java.util.Date(),
						recuperacion_area.getId()); 
			return recuperacion_area.getId();

		} else {
			// insert
			String sql = "insert into mat_recuperacion_area ("
						+ "id_mat, "
						+ "id_area, "
						+ "prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				recuperacion_area.getId_mat(),
				recuperacion_area.getId_area(),
				recuperacion_area.getProm(),
				recuperacion_area.getEst(),
				recuperacion_area.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_recuperacion_area where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<RecuperacionArea> list() {
		String sql = "select * from mat_recuperacion_area";
		
		
		
		List<RecuperacionArea> listRecuperacionArea = jdbcTemplate.query(sql, new RowMapper<RecuperacionArea>() {

			@Override
			public RecuperacionArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listRecuperacionArea;
	}

	public RecuperacionArea get(int id) {
		String sql = "select * from mat_recuperacion_area WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RecuperacionArea>() {

			@Override
			public RecuperacionArea extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public RecuperacionArea getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mra.id mra_id, mra.id_mat mra_id_mat , mra.id_area mra_id_area , mra.prom mra_prom  ,mra.est mra_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("cat_area"))
			sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
	
		sql = sql + " from mat_recuperacion_area mra "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = mra.id_mat ";
		if (aTablas.contains("cat_area"))
			sql = sql + " left join cat_area area on area.id = mra.id_area ";
		sql = sql + " where mra.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<RecuperacionArea>() {
		
			@Override
			public RecuperacionArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					RecuperacionArea recuperacionarea= rsToEntity(rs,"mra_");
					if (aTablas.contains("mat_matricula")){
						Matricula matricula = new Matricula();  
							matricula.setId(rs.getInt("mat_id")) ;  
							matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
							matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
							matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
							matricula.setId_con(rs.getInt("mat_id_con")) ;  
							matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
							matricula.setId_per(rs.getInt("mat_id_per")) ;  
							matricula.setId_au(rs.getInt("mat_id_au")) ;  
							matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
							matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
							matricula.setFecha(rs.getDate("mat_fecha")) ;  
							matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
							matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
							matricula.setObs(rs.getString("mat_obs")) ;  
							recuperacionarea.setMatricula(matricula);
					}
					if (aTablas.contains("cat_area")){
						Area area = new Area();  
							area.setId(rs.getInt("area_id")) ;  
							area.setNom(rs.getString("area_nom")) ;  
							area.setDes(rs.getString("area_des")) ;  
							recuperacionarea.setArea(area);
					}
							return recuperacionarea;
				}
				
				return null;
			}
			
		});


	}		
	
	public RecuperacionArea getByParams(Param param) {

		String sql = "select * from mat_recuperacion_area " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<RecuperacionArea>() {
			@Override
			public RecuperacionArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<RecuperacionArea> listByParams(Param param, String[] order) {

		String sql = "select * from mat_recuperacion_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<RecuperacionArea>() {

			@Override
			public RecuperacionArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<RecuperacionArea> listFullByParams(RecuperacionArea recuperacionarea, String[] order) {
	
		return listFullByParams(Param.toParam("mra",recuperacionarea), order);
	
	}	
	
	public List<RecuperacionArea> listFullByParams(Param param, String[] order) {

		String sql = "select mra.id mra_id, mra.id_mat mra_id_mat , mra.id_area mra_id_area , mra.prom mra_prom  ,mra.est mra_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		sql = sql + " from mat_recuperacion_area mra";
		sql = sql + " left join mat_matricula mat on mat.id = mra.id_mat ";
		sql = sql + " left join cat_area area on area.id = mra.id_area ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<RecuperacionArea>() {

			@Override
			public RecuperacionArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				RecuperacionArea recuperacionarea= rsToEntity(rs,"mra_");
				Matricula matricula = new Matricula();  
				matricula.setId(rs.getInt("mat_id")) ;  
				matricula.setId_alu(rs.getInt("mat_id_alu")) ;  
				matricula.setId_fam(rs.getInt("mat_id_fam")) ;  
				matricula.setId_enc(rs.getInt("mat_id_enc")) ;  
				matricula.setId_con(rs.getInt("mat_id_con")) ;  
				matricula.setId_cli(rs.getInt("mat_id_cli")) ;  
				matricula.setId_per(rs.getInt("mat_id_per")) ;  
				matricula.setId_au(rs.getInt("mat_id_au")) ;  
				matricula.setId_gra(rs.getInt("mat_id_gra")) ;  
				matricula.setId_niv(rs.getInt("mat_id_niv")) ;  
				matricula.setFecha(rs.getDate("mat_fecha")) ;  
				matricula.setCar_pod(rs.getString("mat_car_pod")) ;  
				matricula.setNum_cont(rs.getString("mat_num_cont")) ;  
				matricula.setObs(rs.getString("mat_obs")) ;  
				recuperacionarea.setMatricula(matricula);
				Area area = new Area();  
				area.setId(rs.getInt("area_id")) ;  
				area.setNom(rs.getString("area_nom")) ;  
				area.setDes(rs.getString("area_des")) ;  
				recuperacionarea.setArea(area);
				return recuperacionarea;
			}

		});

	}	




	// funciones privadas utilitarias para RecuperacionArea

	private RecuperacionArea rsToEntity(ResultSet rs,String alias) throws SQLException {
		RecuperacionArea recuperacion_area = new RecuperacionArea();

		recuperacion_area.setId(rs.getInt( alias + "id"));
		recuperacion_area.setId_mat(rs.getInt( alias + "id_mat"));
		recuperacion_area.setId_area(rs.getInt( alias + "id_area"));
		recuperacion_area.setProm(rs.getInt( alias + "prom"));
		recuperacion_area.setEst(rs.getString( alias + "est"));
								
		return recuperacion_area;

	}
	
}
