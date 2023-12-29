package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.GradoHorario;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GradoHorarioDAO.
 * @author MV
 *
 */
public class GradoHorarioDAOImpl{
	final static Logger logger = Logger.getLogger(GradoHorarioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GradoHorario grado_horario) {
		if (grado_horario.getId() != null) {
			// update
			String sql = "UPDATE asi_grado_horario "
						+ "SET id_anio=?, "
						+ "id_au=?, "
						+ "id_gir=?, "
						+ "hora_ini=?, "
						+ "hora_fin=?, "
						+ "hora_ini_aux=?, "
						+ "hora_fin_aux=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						grado_horario.getId_anio(),
						grado_horario.getId_au(),
						grado_horario.getId_gir(),
						grado_horario.getHora_ini(),
						grado_horario.getHora_fin(),
						grado_horario.getHora_ini_aux(),
						grado_horario.getHora_fin_aux(),
						grado_horario.getEst(),
						grado_horario.getUsr_act(),
						new java.util.Date(),
						grado_horario.getId()); 
			return grado_horario.getId();

		} else {
			// insert
			String sql = "insert into asi_grado_horario ("
						+ "id_anio, "
						+ "id_au, "
						+ "id_gir, "
						+ "hora_ini, "
						+ "hora_fin, "
						+ "hora_ini_aux, "
						+ "hora_fin_aux, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				grado_horario.getId_anio(),
				grado_horario.getId_au(),
				grado_horario.getId_gir(),
				grado_horario.getHora_ini(),
				grado_horario.getHora_fin(),
				grado_horario.getHora_ini_aux(),
				grado_horario.getHora_fin_aux(),
				grado_horario.getEst(),
				grado_horario.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from asi_grado_horario where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<GradoHorario> list() {
		String sql = "select * from asi_grado_horario";
		
		//logger.info(sql);
		
		List<GradoHorario> listGradoHorario = jdbcTemplate.query(sql, new RowMapper<GradoHorario>() {

			@Override
			public GradoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGradoHorario;
	}

	public GradoHorario get(int id) {
		String sql = "select agh.*, au.id_grad id_gra, gra.id_nvl id_niv, cic.id id_cic, tpe.id id_tpe from asi_grado_horario agh" 
			    	+ " LEFT JOIN col_aula au ON  agh.id_au=au.id"
			    	+ " LEFT JOIN cat_grad gra ON au.id_grad=gra.id"
			    	+ " LEFT JOIN col_ciclo cic ON au.id_cic=cic.id"
			    	+ " LEFT JOIN per_periodo per ON au.id_per=per.id"
			    	+ " LEFT JOIN cat_tip_periodo tpe ON per.id_tpe=tpe.id"
			    	+ " where agh.id=" + id;
		
//		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoHorario>() {

			@Override
			public GradoHorario extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public GradoHorario getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select agh.id agh_id, agh.id_anio agh_id_anio, agh.id_gir agh_id_gir , agh.id_au agh_id_au , agh.hora_ini agh_hora_ini , agh.hora_fin agh_hora_fin , agh.hora_ini_aux agh_hora_ini_aux , agh.hora_fin_aux agh_hora_fin_aux  ,agh.est agh_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
	
		sql = sql + " from asi_grado_horario agh "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = agh.id_anio ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula aula on aula.id = agh.id_au ";
		sql = sql + " where agh.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoHorario>() {
		
			@Override
			public GradoHorario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GradoHorario gradohorario= rsToEntity(rs,"agh_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							gradohorario.setAnio(anio);
					}
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("aula_id")) ;  
							aula.setId_per(rs.getInt("aula_id_per")) ;  
							aula.setId_grad(rs.getInt("aula_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("aula_id_tur")) ;  
							aula.setSecc(rs.getString("aula_secc")) ;  
							aula.setCap(rs.getInt("aula_cap")) ;  
							gradohorario.setAula(aula);
					}
							return gradohorario;
				}
				
				return null;
			}
			
		});


	}		
	
	public GradoHorario getByParams(Param param) {

		String sql = "select g.*, aula.id_grad id_gra, gr.id_nvl id_niv, cic.id id_cic, tpe.id id_tpe from asi_grado_horario g inner join col_aula aula on aula.id = g.id_au INNER JOIN col_ciclo cic ON aula.id_cic=cic.id INNER JOIN per_periodo per ON aula.id_per=per.id INNER JOIN cat_tip_periodo tpe ON per.id_tpe=tpe.id inner join cat_grad gr on gr.id= aula.id_grad  " + SQLFrmkUtil.getWhere(param);
		
	//	//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GradoHorario>() {
			@Override
			public GradoHorario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<GradoHorario> listByParams(Param param, String[] order) {

		String sql = "select * from asi_grado_horario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		////logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GradoHorario>() {

			@Override
			public GradoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<GradoHorario> listFullByParams(GradoHorario gradohorario, String[] order) {
	
		return listFullByParams(Param.toParam("agh",gradohorario), order);
	
	}	
	
	public List<GradoHorario> listFullByParams(Param param, String[] order) {

		String sql = "select agh.id agh_id, agh.id_anio agh_id_anio , agh.id_gir agh_id_gir,  agh.id_au agh_id_au , agh.hora_ini agh_hora_ini , agh.hora_fin agh_hora_fin , agh.hora_ini_aux agh_hora_ini_aux , agh.hora_fin_aux agh_hora_fin_aux  ,agh.est agh_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		sql = sql + " from asi_grado_horario agh";
		sql = sql + " left join col_anio anio on anio.id = agh.id_anio ";
		sql = sql + " left join col_aula aula on aula.id = agh.id_au ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		////logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GradoHorario>() {

			@Override
			public GradoHorario mapRow(ResultSet rs, int rowNum) throws SQLException {
				GradoHorario gradohorario= rsToEntity(rs,"agh_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				gradohorario.setAnio(anio);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("aula_id")) ;  
				aula.setId_per(rs.getInt("aula_id_per")) ;  
				aula.setId_grad(rs.getInt("aula_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("aula_id_tur")) ;  
				aula.setSecc(rs.getString("aula_secc")) ;  
				aula.setCap(rs.getInt("aula_cap")) ;  
				gradohorario.setAula(aula);
				return gradohorario;
			}

		});

	}	




	// funciones privadas utilitarias para GradoHorario

	private GradoHorario rsToEntity(ResultSet rs,String alias) throws SQLException {
		GradoHorario grado_horario = new GradoHorario();

		grado_horario.setId(rs.getInt( alias + "id"));
		grado_horario.setId_anio(rs.getInt( alias + "id_anio"));
		grado_horario.setId_au(rs.getInt( alias + "id_au"));
		grado_horario.setId_gir(rs.getInt( alias + "id_gir"));
		grado_horario.setHora_ini(rs.getString( alias + "hora_ini"));
		grado_horario.setHora_fin(rs.getString( alias + "hora_fin"));
		grado_horario.setHora_ini_aux(rs.getString( alias + "hora_ini_aux"));
		grado_horario.setHora_fin_aux(rs.getString( alias + "hora_fin_aux"));
		grado_horario.setEst(rs.getString( alias + "est"));
		grado_horario.setId_gra(rs.getInt(alias + "id_gra")); //parametro agregado
		grado_horario.setId_niv(rs.getInt(alias + "id_niv")); //parametro agregado		
		grado_horario.setId_cic(rs.getInt(alias + "id_cic"));
		grado_horario.setId_tpe(rs.getInt(alias + "id_tpe"));
		return grado_horario;

	}
	
}
