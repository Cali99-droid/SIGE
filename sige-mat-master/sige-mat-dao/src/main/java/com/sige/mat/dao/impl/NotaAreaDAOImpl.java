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
import com.tesla.colegio.model.NotaArea;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Area;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaAreaDAO.
 * @author MV
 *
 */
public class NotaAreaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(NotaArea nota_area) {
		if (nota_area.getId() != null) {
			// update
			String sql = "UPDATE not_nota_area "
						+ "SET id_mat=?, "
						+ "id_au=?, "
						+ "id_area=?, "
						+ "prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						nota_area.getId_mat(),
						nota_area.getId_au(),
						nota_area.getId_area(),
						nota_area.getProm(),
						nota_area.getEst(),
						nota_area.getUsr_act(),
						new java.util.Date(),
						nota_area.getId()); 
			return nota_area.getId();

		} else {
			// insert
			String sql = "insert into not_nota_area ("
						+ "id_mat, "
						+ "id_au, "
						+ "id_area, "
						+ "prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				nota_area.getId_mat(),
				nota_area.getId_au(),
				nota_area.getId_area(),
				nota_area.getProm(),
				nota_area.getEst(),
				nota_area.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_nota_area where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<NotaArea> list() {
		String sql = "select * from not_nota_area";
		
		
		
		List<NotaArea> listNotaArea = jdbcTemplate.query(sql, new RowMapper<NotaArea>() {

			@Override
			public NotaArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaArea;
	}

	public NotaArea get(int id) {
		String sql = "select * from not_nota_area WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaArea>() {

			@Override
			public NotaArea extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NotaArea getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nna.id nna_id, nna.id_mat nna_id_mat , nna.id_au nna_id_au , nna.id_area nna_id_area , nna.prom nna_prom  ,nna.est nna_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("cat_area"))
			sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
	
		sql = sql + " from not_nota_area nna "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = nna.id_mat ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = nna.id_au ";
		if (aTablas.contains("cat_area"))
			sql = sql + " left join cat_area area on area.id = nna.id_area ";
		sql = sql + " where nna.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaArea>() {
		
			@Override
			public NotaArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaArea notaarea= rsToEntity(rs,"nna_");
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
							notaarea.setMatricula(matricula);
					}
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("au_id")) ;  
							aula.setId_per(rs.getInt("au_id_per")) ;  
							aula.setId_grad(rs.getInt("au_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("au_id_tur")) ;  
							aula.setSecc(rs.getString("au_secc")) ;  
							aula.setCap(rs.getInt("au_cap")) ;  
							notaarea.setAula(aula);
					}
					if (aTablas.contains("cat_area")){
						Area area = new Area();  
							area.setId(rs.getInt("area_id")) ;  
							area.setNom(rs.getString("area_nom")) ;  
							area.setDes(rs.getString("area_des")) ;  
							notaarea.setArea(area);
					}
							return notaarea;
				}
				
				return null;
			}
			
		});


	}		
	
	public NotaArea getByParams(Param param) {

		String sql = "select * from not_nota_area " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaArea>() {
			@Override
			public NotaArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NotaArea> listByParams(Param param, String[] order) {

		String sql = "select * from not_nota_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<NotaArea>() {

			@Override
			public NotaArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NotaArea> listFullByParams(NotaArea notaarea, String[] order) {
	
		return listFullByParams(Param.toParam("nna",notaarea), order);
	
	}	
	
	public List<NotaArea> listFullByParams(Param param, String[] order) {

		String sql = "select nna.id nna_id, nna.id_mat nna_id_mat , nna.id_au nna_id_au , nna.id_area nna_id_area , nna.prom nna_prom  ,nna.est nna_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		sql = sql + " from not_nota_area nna";
		sql = sql + " left join mat_matricula mat on mat.id = nna.id_mat ";
		sql = sql + " left join col_aula au on au.id = nna.id_au ";
		sql = sql + " left join cat_area area on area.id = nna.id_area ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<NotaArea>() {

			@Override
			public NotaArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaArea notaarea= rsToEntity(rs,"nna_");
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
				notaarea.setMatricula(matricula);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				notaarea.setAula(aula);
				Area area = new Area();  
				area.setId(rs.getInt("area_id")) ;  
				area.setNom(rs.getString("area_nom")) ;  
				area.setDes(rs.getString("area_des")) ;  
				notaarea.setArea(area);
				return notaarea;
			}

		});

	}	




	// funciones privadas utilitarias para NotaArea

	private NotaArea rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaArea nota_area = new NotaArea();

		nota_area.setId(rs.getInt( alias + "id"));
		nota_area.setId_mat(rs.getInt( alias + "id_mat"));
		nota_area.setId_au(rs.getInt( alias + "id_au"));
		nota_area.setId_area(rs.getInt( alias + "id_area"));
		nota_area.setProm(rs.getInt( alias + "prom"));
		nota_area.setEst(rs.getString( alias + "est"));
								
		return nota_area;

	}
	
}
