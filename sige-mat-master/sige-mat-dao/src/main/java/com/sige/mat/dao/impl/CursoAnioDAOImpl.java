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
import com.tesla.colegio.model.CursoAnio;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.sige.core.dao.SQLUtil;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.AreaAnio;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.Competencia;
import com.tesla.colegio.model.CursoUnidad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoAnioDAO.
 * @author MV
 *
 */
public class CursoAnioDAOImpl{
	final static Logger logger = Logger.getLogger(CursoAnioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private SQLUtil sqlUtil;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoAnio curso_anio) {
		if (curso_anio.getId() != null) {
			// update
			String sql = "UPDATE col_curso_anio "
						+ "SET id_per=?, "
					    + "id_cic=?, "
						+ "id_gra=?, "
						+ "id_caa=?, "
						+ "id_cur=?, "
						+ "peso=?, "
						+ "orden=?, "
						+ "cod_classroom=?, "
						+ "flg_prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso_anio.getId_per(),
						curso_anio.getId_cic(),
						curso_anio.getId_gra(),
						curso_anio.getId_caa(),
						curso_anio.getId_cur(),
						curso_anio.getPeso(),
						curso_anio.getOrden(),
						curso_anio.getCod_classroom(),
						curso_anio.getFlg_prom(),
						curso_anio.getEst(),
						curso_anio.getUsr_act(),
						new java.util.Date(),
						curso_anio.getId()); 
			return curso_anio.getId();

		} else {
			// insert
			String sql = "insert into col_curso_anio ("
						+ "id_per, "
					    + "id_cic, "
						+ "id_gra, "
						+ "id_caa, "
						+ "id_cur, "
						+ "peso, "
						+ "orden, "
						+ "cod_classroom, "
						+ "flg_prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso_anio.getId_per(),
				curso_anio.getId_cic(),
				curso_anio.getId_gra(),
				curso_anio.getId_caa(),
				curso_anio.getId_cur(),
				curso_anio.getPeso(),
				curso_anio.getOrden(),
				curso_anio.getCod_classroom(),
				curso_anio.getFlg_prom(),
				curso_anio.getEst(),
				curso_anio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_anio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoAnio> list() {
		String sql = "select * from col_curso_anio";
		
		//logger.info(sql);
		
		List<CursoAnio> listCursoAnio = jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoAnio;
	}

	public CursoAnio get(int id) {
		String sql = "select * from col_curso_anio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAnio>() {

			@Override
			public CursoAnio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoAnio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cua.id cua_id, cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ,cua.est cua_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.nom gra_nom  ";
		if (aTablas.contains("col_area_anio"))
			sql = sql + ", caa.id caa_id  , caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
	
		sql = sql + " from col_curso_anio cua "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo per on per.id = cua.id_per ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = cua.id_gra ";
		if (aTablas.contains("col_area_anio"))
			sql = sql + " left join col_area_anio caa on caa.id = cua.id_caa ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = cua.id_cur ";
		sql = sql + " where cua.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAnio>() {
		
			@Override
			public CursoAnio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoAnio cursoanio= rsToEntity(rs,"cua_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("per_id")) ;  
							periodo.setId_anio(rs.getInt("per_id_anio")) ;  
							periodo.setId_suc(rs.getInt("per_id_suc")) ;  
							periodo.setId_niv(rs.getInt("per_id_niv")) ;  
							periodo.setId_srv(rs.getInt("per_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("per_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("per_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("per_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("per_fec_cie_mat")) ;  
							cursoanio.setPeriodo(periodo);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							cursoanio.setGrad(grad);
					}
					if (aTablas.contains("col_area_anio")){
						AreaAnio areaanio = new AreaAnio();  
							areaanio.setId(rs.getInt("caa_id")) ;  
							areaanio.setId_anio(rs.getInt("caa_id_anio")) ;  
							areaanio.setId_niv(rs.getInt("caa_id_niv")) ;  
							areaanio.setId_area(rs.getInt("caa_id_area")) ;  
							areaanio.setOrd(rs.getInt("caa_ord")) ;  
							cursoanio.setAreaAnio(areaanio);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							cursoanio.setCurso(curso);
					}
							return cursoanio;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoAnio getByParams(Param param) {

		String sql = "select * from col_curso_anio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAnio>() {
			@Override
			public CursoAnio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoAnio> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_anio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoAnio> listFullByParams(CursoAnio cursoanio, String[] order) {
	
		return listFullByParams(Param.toParam("cua",cursoanio), order);
	
	}	
	
	public List<CursoAnio> listFullByParams(Param param, String[] order) {

		String sql = "select cua.id cua_id, cua.id_per cua_id_per, cua.id_cic cua_id_cic, cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.cod_classroom cua_cod_classroom, cua.flg_prom cua_flg_prom  ,cua.est cua_est ";
		sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat  ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl, gra.nom gra_nom ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + ", niv.id niv_id  , niv.nom niv_nom  ";
		sql = sql + ", a.id id_a, a.nom a_nom ";
		sql = sql + ", caa.id caa_id  , caa.id_anio caa_id_anio , caa.id_niv caa_id_niv , caa.id_area caa_id_area , caa.ord caa_ord  ";
		sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		sql = sql + " from col_curso_anio cua";
		sql = sql + " left join per_periodo per on per.id = cua.id_per ";
		sql = sql + " left join ges_sucursal suc on per.id_suc=suc.id ";
		sql = sql + " left join cat_nivel niv on niv.id=per.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id = cua.id_gra ";
		sql = sql + " left join col_area_anio caa on caa.id = cua.id_caa ";
		sql = sql + " left join cat_area a on a.id=caa.id_area ";
		sql = sql + " left join cat_curso cur on cur.id = cua.id_cur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAnio>() {

			@Override
			public CursoAnio mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAnio cursoanio= rsToEntity(rs,"cua_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("per_id")) ;  
				periodo.setId_anio(rs.getInt("per_id_anio")) ;  
				periodo.setId_suc(rs.getInt("per_id_suc")) ;  
				periodo.setId_niv(rs.getInt("per_id_niv")) ;  
				periodo.setId_srv(rs.getInt("per_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("per_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("per_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("per_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("per_fec_cie_mat")) ;  
				cursoanio.setPeriodo(periodo);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				cursoanio.setGrad(grad);
				AreaAnio areaanio = new AreaAnio();  
				areaanio.setId(rs.getInt("caa_id")) ;  
				areaanio.setId_anio(rs.getInt("caa_id_anio")) ;  
				areaanio.setId_niv(rs.getInt("caa_id_niv")) ;  
				areaanio.setId_area(rs.getInt("caa_id_area")) ;  
				areaanio.setOrd(rs.getInt("caa_ord")) ;  
				cursoanio.setAreaAnio(areaanio);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				cursoanio.setCurso(curso);
				Sucursal sucursal= new Sucursal();
				sucursal.setNom(rs.getString("suc_nom"));
				cursoanio.setSucursal(sucursal);
				Nivel nivel = new Nivel();
				nivel.setNom(rs.getString("niv_nom"));
				cursoanio.setNivel(nivel);
				Area area = new Area();
				area.setNom(rs.getString("a_nom"));
				cursoanio.setArea(area);
				return cursoanio;
			}

		});

	}	


	public List<CursoAula> getListCursoAula(Param param, String[] order) {
		String sql = "select * from col_curso_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoAula>() {

			@Override
			public CursoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAula curso_aula = new CursoAula();

				curso_aula.setId(rs.getInt("id"));
				curso_aula.setId_cua(rs.getInt("id_cua"));
				curso_aula.setId_au(rs.getInt("id_au"));
				//curso_aula.setId_prof(rs.getInt("id_prof"));
				curso_aula.setEst(rs.getString("est"));
												
				return curso_aula;
			}

		});	
	}
	public List<Competencia> getListCompetencia(Param param, String[] order) {
		String sql = "select * from col_competencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Competencia>() {

			@Override
			public Competencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				Competencia competencia = new Competencia();

				competencia.setId(rs.getInt("id"));
				//competencia.setId_cua(rs.getInt("id_cua"));
				competencia.setNom(rs.getString("nom"));
				competencia.setPeso(rs.getString("peso"));
				competencia.setEst(rs.getString("est"));
												
				return competencia;
			}

		});	
	}
	public List<CursoUnidad> getListCursoUnidad(Param param, String[] order) {
		String sql = "select * from col_curso_unidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoUnidad curso_unidad = new CursoUnidad();

				curso_unidad.setId(rs.getInt("id"));
				//curso_unidad.setId_cua(rs.getInt("id_cua"));
				curso_unidad.setNum(rs.getInt("num"));
				curso_unidad.setNom(rs.getString("nom"));
				curso_unidad.setDes(rs.getString("des"));
				//curso_unidad.setNro_ses(rs.getInt("nro_ses"));
				//curso_unidad.setNro_sem(rs.getInt("nro_sem"));
				curso_unidad.setProducto(rs.getString("producto"));
				curso_unidad.setEst(rs.getString("est"));
												
				return curso_unidad;
			}

		});	
	}


	// funciones privadas utilitarias para CursoAnio

	private CursoAnio rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoAnio curso_anio = new CursoAnio();

		curso_anio.setId(rs.getInt( alias + "id"));
		curso_anio.setId_per(rs.getInt( alias + "id_per"));
		curso_anio.setId_cic(rs.getInt( alias + "id_cic"));
		curso_anio.setId_gra(rs.getInt( alias + "id_gra"));
		curso_anio.setId_caa(rs.getInt( alias + "id_caa"));
		curso_anio.setId_cur(rs.getInt( alias + "id_cur"));
		curso_anio.setPeso(rs.getInt( alias + "peso"));
		curso_anio.setOrden(rs.getInt( alias + "orden"));
		curso_anio.setCod_classroom(rs.getString(alias + "cod_classroom"));
		curso_anio.setFlg_prom(rs.getString( alias + "flg_prom"));
		curso_anio.setEst(rs.getString( alias + "est"));
								
		return curso_anio;

	}
	
	public List<Row> listaCursosAula(int id_anio, int id_au) {

		String sql = "SELECT DISTINCT cu.id , cu.nom  AS value, cca.id id_cca, cu.abrv abrv_curso FROM col_curso_aula cca LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
				+ " LEFT JOIN cat_curso cu ON cua.id_cur=cu.id "
				+ " LEFT JOIN col_ciclo cic ON cua.id_cic=cic.id"
				+ " LEFT JOIN per_periodo per ON cic.id_per=per.id"
				+ " WHERE per.id_anio=? AND cca.id_au=? AND cca.cod_classroom IS NULL order by cu.nom";
		// //logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_anio, id_au });

	}
	
	
	public List<Row> listaCursosGrado(int id_anio, int id_gra) {

		String sql = "SELECT DISTINCT cu.id , cu.nom  AS value "
				+ " FROM col_curso_aula cca "
				+ " LEFT JOIN col_curso_anio cua ON cca.id_cua=cua.id "
				+ " LEFT JOIN cat_curso cu ON cua.id_cur=cu.id "
				+ " LEFT JOIN per_periodo per ON cua.id_per=per.id"
				+ " WHERE per.id_anio=? AND cua.id_gra=? order by cu.nom";
		// //logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_anio,id_gra });

	}
}
