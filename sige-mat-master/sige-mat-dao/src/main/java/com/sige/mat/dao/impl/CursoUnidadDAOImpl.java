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
import com.tesla.colegio.model.CursoUnidad;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.UnidadTema;
import com.tesla.colegio.model.UnidadSesion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoUnidadDAO.
 * @author MV
 *
 */
public class CursoUnidadDAOImpl{
	final static Logger logger = Logger.getLogger(CursoUnidadDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoUnidad curso_unidad) {
		if (curso_unidad.getId() != null) {
			// update
			String sql = "UPDATE col_curso_unidad "
						+ "SET id_niv=?, "
						+ "id_gra=?, "
						+ "id_cur=?, "
						+ "id_cpu=?, "
						+ "num=?, "
						+ "nom=?, "
						+ "des=?, "
						+ "producto=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso_unidad.getId_niv(),
						curso_unidad.getId_gra(),
						curso_unidad.getId_cur(),
						curso_unidad.getId_cpu(),
						curso_unidad.getNum(),
						curso_unidad.getNom(),
						curso_unidad.getDes(),
						curso_unidad.getProducto(),
						curso_unidad.getEst(),
						curso_unidad.getUsr_act(),
						new java.util.Date(),
						curso_unidad.getId()); 
			return curso_unidad.getId();

		} else {
			// insert
			String sql = "insert into col_curso_unidad ("
						+ "id_niv, "
						+ "id_gra, "
						+ "id_cur, "
						+ "id_cpu, "
						+ "num, "
						+ "nom, "
						+ "des, "
						+ "producto, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso_unidad.getId_niv(),
				curso_unidad.getId_gra(),
				curso_unidad.getId_cur(),
				curso_unidad.getId_cpu(),
				curso_unidad.getNum(),
				curso_unidad.getNom(),
				curso_unidad.getDes(),
				curso_unidad.getProducto(),
				curso_unidad.getEst(),
				curso_unidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_unidad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoUnidad> list() {
		String sql = "select * from col_curso_unidad";
		
		//logger.info(sql);
		
		List<CursoUnidad> listCursoUnidad = jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoUnidad;
	}

	public CursoUnidad get(int id) {
		String sql = "select * from col_curso_unidad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidad>() {

			@Override
			public CursoUnidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoUnidad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select uni.id uni_id, uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.producto uni_producto  ,uni.est uni_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.nom gra_nom  ";
		if (aTablas.contains("cat_curso"))
			sql = sql + ", cur.id cur_id  , cur.nom cur_nom  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini, cpu.numu_fin cpu_numu_fin  ";
	
		sql = sql + " from col_curso_unidad uni "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = uni.id_niv ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = uni.id_gra ";
		if (aTablas.contains("cat_curso"))
			sql = sql + " left join cat_curso cur on cur.id = uni.id_cur ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = uni.id_cpu ";
		sql = sql + " where uni.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidad>() {
		
			@Override
			public CursoUnidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoUnidad cursounidad= rsToEntity(rs,"uni_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							cursounidad.setNivel(nivel);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							cursounidad.setGrad(grad);
					}
					if (aTablas.contains("cat_curso")){
						Curso curso = new Curso();  
							curso.setId(rs.getInt("cur_id")) ;  
							curso.setNom(rs.getString("cur_nom")) ;  
							cursounidad.setCurso(curso);
					}
					if (aTablas.contains("col_per_uni")){
						PerUni peruni = new PerUni();  
							peruni.setId(rs.getInt("cpu_id")) ;  
							peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
							peruni.setNump(rs.getInt("cpu_nump")) ;  
							peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
							peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
							cursounidad.setPerUni(peruni);
					}
							return cursounidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoUnidad getByParams(Param param) {

		String sql = "select * from col_curso_unidad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidad>() {
			@Override
			public CursoUnidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoUnidad> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_unidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoUnidad> listFullByParams(CursoUnidad cursounidad, String[] order) {
	
		return listFullByParams(Param.toParam("uni",cursounidad), order);
	
	}	
	
	public List<CursoUnidad> listFullByParams(Param param, String[] order) {

		String sql = "SELECT DISTINCT uni.id uni_id, uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , "
				+ "\n uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.producto uni_producto  ,uni.est uni_est "
				+ "\n , niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  "
				+ "\n , gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.nom gra_nom  "
				+ "\n , cur.id cur_id  , cur.nom cur_nom  "
				+ "\n , per_aca.id per_aca_id, per_aca.nom per_aca_nom "
				+ "\n , cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini, cpu.numu_fin cpu_numu_fin  "
				+ "\n FROM col_curso_unidad uni"
				+ "\n INNER JOIN cat_nivel niv ON niv.id = uni.id_niv "
				+ "\n INNER JOIN cat_grad gra ON gra.id = uni.id_gra AND gra.id_nvl=niv.id"
				+ "\n INNER JOIN cat_curso cur ON cur.id = uni.id_cur "
				+ "\n INNER JOIN col_per_uni cpu ON cpu.id = uni.id_cpu "
				+ "\n INNER JOIN cat_per_aca_nivel per_niv ON cpu.id_cpa=per_niv.id"
				+ "\n INNER JOIN cat_periodo_aca per_aca ON per_aca.id=per_niv.id_cpa"
				+ "\n inner JOIN col_curso_anio ca ON ca.id_cur=uni.id_cur AND ca.id_gra=gra.id "
				+ "\n inner JOIN col_area_anio caa ON ca.id_caa=caa.id AND caa.id_niv=niv.id"
				+ "\n inner JOIN col_area_coordinador are_cor ON are_cor.id_cur=ca.id_cur AND are_cor.id_niv=niv.id"
				+ "\n inner join col_unidad_sesion cus ON cus.id_uni=uni.id ";
				//+ "\n inner join col_sesion_tipo cst ON cus.id=cst.id_uns ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoUnidad>() {

			@Override
			public CursoUnidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoUnidad cursounidad= rsToEntity(rs,"uni_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				cursounidad.setNivel(nivel);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				cursounidad.setGrad(grad);
				Curso curso = new Curso();  
				curso.setId(rs.getInt("cur_id")) ;  
				curso.setNom(rs.getString("cur_nom")) ;  
				cursounidad.setCurso(curso);
				PerUni peruni = new PerUni();  
				peruni.setId(rs.getInt("cpu_id")) ;  
				peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
				peruni.setNump(rs.getInt("cpu_nump")) ;  
				peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
				peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
				cursounidad.setPerUni(peruni);
				PeriodoAca periodoAca= new PeriodoAca();
				periodoAca.setId(rs.getInt("per_aca_id"));
				periodoAca.setNom(rs.getString("per_aca_nom"));
				cursounidad.setPeriodoAca(periodoAca);
				return cursounidad;
			}

		});

	}	


	public List<UnidadTema> getListUnidadTema(Param param, String[] order) {
		String sql = "select * from col_unidad_tema " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UnidadTema>() {

			@Override
			public UnidadTema mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadTema unidad_tema = new UnidadTema();

				unidad_tema.setId(rs.getInt("id"));
				unidad_tema.setId_uni(rs.getInt("id_uni"));
				unidad_tema.setId_ccs(rs.getInt("id_ccs"));
				unidad_tema.setEst(rs.getString("est"));
												
				return unidad_tema;
			}

		});	
	}
	public List<UnidadSesion> getListUnidadSesion(Param param, String[] order) {
		String sql = "select * from col_unidad_sesion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UnidadSesion>() {

			@Override
			public UnidadSesion mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadSesion unidad_sesion = new UnidadSesion();

				unidad_sesion.setId(rs.getInt("id"));
				unidad_sesion.setId_uni(rs.getInt("id_uni"));
				//unidad_sesion.setTit(rs.getString("tit"));
				//unidad_sesion.setDur(rs.getBigDecimal("dur"));
				//unidad_sesion.setTipo(rs.getString("tipo"));
				unidad_sesion.setEst(rs.getString("est"));
												
				return unidad_sesion;
			}

		});	
	}


	// funciones privadas utilitarias para CursoUnidad

	private CursoUnidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoUnidad curso_unidad = new CursoUnidad();

		curso_unidad.setId(rs.getInt( alias + "id"));
		curso_unidad.setId_niv(rs.getInt( alias + "id_niv"));
		curso_unidad.setId_gra(rs.getInt( alias + "id_gra"));
		curso_unidad.setId_cur(rs.getInt( alias + "id_cur"));
		curso_unidad.setId_cpu(rs.getInt( alias + "id_cpu"));
		curso_unidad.setNum(rs.getInt( alias + "num"));
		curso_unidad.setNom(rs.getString( alias + "nom"));
		curso_unidad.setDes(rs.getString( alias + "des"));
		curso_unidad.setProducto(rs.getString( alias + "producto"));
		curso_unidad.setEst(rs.getString( alias + "est"));
								
		return curso_unidad;

	}
	
}
