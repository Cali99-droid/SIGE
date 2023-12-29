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
import com.tesla.colegio.model.ProgAnual;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Curso;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ProgAnualDAO.
 * @author MV
 *
 */
public class ProgAnualDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ProgAnual prog_anual) {
		if (prog_anual.getId() != null) {
			// update
			String sql = "UPDATE con_prog_anual "
						+ "SET id_tra=?, "
						+ "id_niv=?, "
						+ "id_gra=?, "
						+ "id_cur=?, "
						+ "flg_descarga=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						prog_anual.getId_tra(),
						prog_anual.getId_niv(),
						prog_anual.getId_gra(),
						prog_anual.getId_cur(),
						prog_anual.getFlg_descarga(),
						prog_anual.getEst(),
						prog_anual.getUsr_act(),
						new java.util.Date(),
						prog_anual.getId()); 
			return prog_anual.getId();

		} else {
			// insert
			String sql = "insert into con_prog_anual ("
						+ "id_tra, "
						+ "id_niv, "
						+ "id_gra, "
						+ "id_cur, "
						+ "flg_descarga, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				prog_anual.getId_tra(),
				prog_anual.getId_niv(),
				prog_anual.getId_gra(),
				prog_anual.getId_cur(),
				prog_anual.getFlg_descarga(),
				prog_anual.getEst(),
				prog_anual.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from con_prog_anual where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<ProgAnual> list() {
		String sql = "select * from con_prog_anual";
		
		
		
		List<ProgAnual> listProgAnual = jdbcTemplate.query(sql, new RowMapper<ProgAnual>() {

			@Override
			public ProgAnual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listProgAnual;
	}

	public ProgAnual get(int id) {
		String sql = "select * from con_prog_anual WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ProgAnual>() {

			@Override
			public ProgAnual extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ProgAnual getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpg.id cpg_id, cpg.id_tra cpg_id_tra , cpg.id_niv cpg_id_niv , cpg.id_gra cpg_id_gra , cpg.id_cur cpg_id_cur , cpg.flg_descarga cpg_flg_descarga  ,cpg.est cpg_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
	
		sql = sql + " from con_prog_anual cpg "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = cpg.id_niv ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = cpg.id_gra ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = cpg.id_cur ";
		sql = sql + " where cpg.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<ProgAnual>() {
		
			@Override
			public ProgAnual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ProgAnual proganual= rsToEntity(rs,"cpg_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							proganual.setNivel(nivel);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							proganual.setGrad(grad);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							proganual.setCurso(curso);
					}
							return proganual;
				}
				
				return null;
			}
			
		});


	}		
	
	public ProgAnual getByParams(Param param) {

		String sql = "select * from con_prog_anual " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ProgAnual>() {
			@Override
			public ProgAnual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ProgAnual> listByParams(Param param, String[] order) {

		String sql = "select * from con_prog_anual " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<ProgAnual>() {

			@Override
			public ProgAnual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ProgAnual> listFullByParams(ProgAnual proganual, String[] order) {
	
		return listFullByParams(Param.toParam("cpg",proganual), order);
	
	}	
	
	public List<ProgAnual> listFullByParams(Param param, String[] order) {

		String sql = "select cpg.id cpg_id, cpg.id_tra cpg_id_tra , cpg.id_niv cpg_id_niv , cpg.id_gra cpg_id_gra , cpg.id_cur cpg_id_cur , cpg.flg_descarga cpg_flg_descarga  ,cpg.est cpg_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + " from con_prog_anual cpg";
		sql = sql + " left join cat_nivel niv on niv.id = cpg.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id = cpg.id_gra ";
		sql = sql + " left join cat_curso cur on cur.id = cpg.id_cur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<ProgAnual>() {

			@Override
			public ProgAnual mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProgAnual proganual= rsToEntity(rs,"cpg_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				proganual.setNivel(nivel);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				proganual.setGrad(grad);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				proganual.setCurso(curso);
				return proganual;
			}

		});

	}	




	// funciones privadas utilitarias para ProgAnual

	private ProgAnual rsToEntity(ResultSet rs,String alias) throws SQLException {
		ProgAnual prog_anual = new ProgAnual();

		prog_anual.setId(rs.getInt( alias + "id"));
		prog_anual.setId_tra(rs.getInt( alias + "id_tra"));
		prog_anual.setId_niv(rs.getInt( alias + "id_niv"));
		prog_anual.setId_gra(rs.getInt( alias + "id_gra"));
		prog_anual.setId_cur(rs.getInt( alias + "id_cur"));
		prog_anual.setFlg_descarga(rs.getString( alias + "flg_descarga"));
		prog_anual.setEst(rs.getString( alias + "est"));
								
		return prog_anual;

	}
	
}
