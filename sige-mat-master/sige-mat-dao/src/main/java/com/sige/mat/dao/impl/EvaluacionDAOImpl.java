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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Evaluacion;
import com.tesla.colegio.model.Grad;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.TipEva;
import com.tesla.colegio.model.IndEva;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Sucursal;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface EvaluacionDAO.
 * @author MV
 *
 */
public class EvaluacionDAOImpl{
	final static Logger logger = Logger.getLogger(EvaluacionDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	protected  NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Evaluacion evaluacion) {
		if (evaluacion.getId() != null) {
			// update
			String sql = "UPDATE not_evaluacion "
						+ "SET id_cca=?, "
						+ "id_nep=?, "
						+ "id_nte=?, "
						+ "id_ses=?, "
						+ "ins=?, "
						+ "evi=?, "
						+ "nump=?,"
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						evaluacion.getId_cca(),
						evaluacion.getId_nep(),
						evaluacion.getId_nte(),
						evaluacion.getId_ses(),
						evaluacion.getIns(),
						evaluacion.getEvi(),
						evaluacion.getNump(),
						evaluacion.getFec_ini(),
						evaluacion.getFec_fin(),
						evaluacion.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						evaluacion.getId()); 
			return evaluacion.getId();

		} else {
			// insert 
			String sql = "insert into not_evaluacion ("
						+ "id_cca, "
						+ "id_nep, "
						+ "id_nte, "
						+ "id_ses, "
						+ "ins, "
						+ "evi, "
						+ "nump, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				evaluacion.getId_cca(),
				evaluacion.getId_nep(),
				evaluacion.getId_nte(),
				evaluacion.getId_ses(),
				evaluacion.getIns(),
				evaluacion.getEvi(),
				evaluacion.getNump(),
				evaluacion.getFec_ini(),
				evaluacion.getFec_fin(),
				evaluacion.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int updateEvas(Evaluacion evaluacion) {{
		String sql = "UPDATE not_evaluacion "
				+ "SET id_cca=?, "
				+ "id_nte=?, "
				+ "ins=?, "
				+ "evi=?, "
				+ "est=?,usr_act=?,fec_act=? "
				+ "WHERE id=?";
	
		//logger.info(sql);

		jdbcTemplate.update(sql, 
				evaluacion.getId_cca(),
				evaluacion.getId_nte(),
				evaluacion.getIns(),
				evaluacion.getEvi(),
				evaluacion.getEst(),
				evaluacion.getUsr_act(),
				new java.util.Date(),
				evaluacion.getId()); 
				return evaluacion.getId();

		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_evaluacion where id_ses=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Evaluacion> list() {
		String sql = "select * from not_evaluacion";
		
		//logger.info(sql);
		
		List<Evaluacion> listEvaluacion = jdbcTemplate.query(sql, new RowMapper<Evaluacion>() {

			@Override
			public Evaluacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listEvaluacion;
	}

	public Evaluacion get(int id) {
		String sql = "select * from not_evaluacion WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Evaluacion>() {

			@Override
			public Evaluacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Evaluacion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ne.id ne_id, ne.id_cca ne_id_cca , ne.id_ses ne_id_ses, ne.nump ne_nump, ne.id_nte ne_id_nte , ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ,ne.est ne_est, ne.id_nep ne_id_nep ";
		if (aTablas.contains("col_curso_aula")){
			sql = sql + ", cca.id cca_id  , cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra  ";
			sql = sql + ", cua.id cua_id  , cua.id_gra cua_id_gra, cua.id_cur cua_id_cur  ";
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl  ";
		}if (aTablas.contains("not_tip_eva"))
			sql = sql + ", nte.id nte_id  , nte.nom nte_nom  ";
	
		sql = sql + " from not_evaluacion ne "; 
		if (aTablas.contains("col_curso_aula")){
			sql = sql + " left join col_curso_aula cca on cca.id = ne.id_cca ";
			sql = sql + " left join col_curso_anio cua on cua.id = cca.id_cua ";
			sql = sql + " left join cat_grad gra on gra.id = cua.id_gra ";
		}if (aTablas.contains("not_tip_eva"))
			sql = sql + " left join not_tip_eva nte on nte.id = ne.id_nte ";
		sql = sql + " where ne.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Evaluacion>() {
		
			@Override
			public Evaluacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Evaluacion evaluacion= rsToEntity(rs,"ne_");
					if (aTablas.contains("col_curso_aula")){
						CursoAula cursoaula = new CursoAula();  
							cursoaula.setId(rs.getInt("cca_id")) ;  
							cursoaula.setId_cua(rs.getInt("cca_id_cua")) ;  
							cursoaula.setId_au(rs.getInt("cca_id_au")) ;  
							cursoaula.setId_tra(rs.getInt("cca_id_tra")) ; 
							CursoAnio cursoAnio = new CursoAnio();
							cursoAnio.setId_gra(rs.getInt("cua_id_gra")) ;
							cursoAnio.setId_cur(rs.getInt("cua_id_cur"));
							Grad grado = new Grad();
							grado.setId_nvl(rs.getInt("gra_id_nvl"));
							cursoAnio.setGrad(grado);
							cursoaula.setCursoAnio(cursoAnio);
							evaluacion.setCursoAula(cursoaula);
					}
					if (aTablas.contains("not_tip_eva")){
						TipEva tipeva = new TipEva();  
							tipeva.setId(rs.getInt("nte_id")) ;  
							tipeva.setNom(rs.getString("nte_nom")) ;  
							evaluacion.setTipEva(tipeva);
					}
							return evaluacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Evaluacion getByParams(Param param) {

		String sql = "select * from not_evaluacion " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Evaluacion>() {
			@Override
			public Evaluacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Evaluacion> listByParams(Param param, String[] order) {

		String sql = "select * from not_evaluacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Evaluacion>() {

			@Override
			public Evaluacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Evaluacion> listFullByParams(Evaluacion evaluacion, String[] order) {
	
		return listFullByParams(Param.toParam("ne",evaluacion), order);
	
	}	
	
	public List<Evaluacion> listFullByParams(Param param, String[] order) {

		String sql = "select ne.id ne_id, ne.id_cca ne_id_cca ,ne.id_ses ne_id_ses , ne.id_nte ne_id_nte , ne.nump ne_nump, ne.ins ne_ins , ne.evi ne_evi , ne.fec_ini ne_fec_ini , ne.fec_fin ne_fec_fin  ,ne.est ne_est , ne.id_nep ne_id_nep";
		sql = sql + ", cca.id cca_id  , cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra  ";
		sql = sql + ", cur.nom cur_nom, ca.secc ca_secc, gra.nom gra_nom";
		sql = sql + ", cpu.id, cpu.nump, ca.id ";
		sql = sql + ", nte.id nte_id  , nte.nom nte_nom, niv.nom niv_nom, suc.nom suc_nom  ";
		sql = sql + " from not_evaluacion ne";
		sql = sql + " inner join not_eva_padre nep on nep.id = ne.id_nep";
		sql = sql + " left join col_curso_aula cca on cca.id = ne.id_cca ";
		sql = sql + " left join col_curso_anio cua on cua.id=cca.id_cua ";
		sql = sql + " left join cat_curso cur on cua.id_cur=cur.id";
		sql = sql + " left join col_aula ca on ca.id=cca.id_au";
		sql = sql + " left join cat_grad gra on cua.id_gra=gra.id";
		sql = sql + " left join not_tip_eva nte on nte.id = ne.id_nte ";
		sql = sql + " left join cat_nivel niv on gra.id_nvl=niv.id ";
		sql = sql + " left join cat_per_aca_nivel cpa on cpa.id_niv=niv.id ";
		sql = sql + " left join col_per_uni cpu on cpa.id=cpu.id_cpa and cpu.id=ne.nump";
		sql = sql + " left join per_periodo per on cua.id_per=per.id ";
		sql = sql + " left join ges_sucursal suc on per.id_suc=suc.id";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Evaluacion>() {

			@Override
			public Evaluacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Evaluacion evaluacion= rsToEntity(rs,"ne_");
				CursoAula cursoaula = new CursoAula();  
				cursoaula.setId(rs.getInt("cca_id")) ;  
				cursoaula.setId_cua(rs.getInt("cca_id_cua")) ;  
				cursoaula.setId_au(rs.getInt("cca_id_au")) ;  
				cursoaula.setId_tra(rs.getInt("cca_id_tra")) ;  
				evaluacion.setCursoAula(cursoaula);
				TipEva tipeva = new TipEva();  
				tipeva.setId(rs.getInt("nte_id")) ;  
				tipeva.setNom(rs.getString("nte_nom")) ;  
				evaluacion.setTipEva(tipeva);
				Curso curso= new Curso();
				curso.setNom(rs.getString("cur_nom"));
				evaluacion.setCurso(curso);
				Aula aula = new Aula();
				aula.setSecc(rs.getString("ca_secc"));
				evaluacion.setAula(aula);
				Grad grad = new Grad();
				grad.setNom(rs.getString("gra_nom"));
				evaluacion.setGrad(grad);
				Nivel nivel= new Nivel();
				nivel.setNom(rs.getString("niv_nom"));
				evaluacion.setNivel(nivel);
				Sucursal sucursal = new Sucursal();
				sucursal.setNom(rs.getString("suc_nom"));
				evaluacion.setSucursal(sucursal);
				return evaluacion;
			}

		});

	}	


	public List<IndEva> getListIndEva(Param param, String[] order) {
		String sql = "select * from not_ind_eva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<IndEva>() {

			@Override
			public IndEva mapRow(ResultSet rs, int rowNum) throws SQLException {
				IndEva ind_eva = new IndEva();

				ind_eva.setId(rs.getInt("id"));
				ind_eva.setId_ne(rs.getInt("id_ne"));
				ind_eva.setId_ind(rs.getInt("id_ind"));
				ind_eva.setEst(rs.getString("est"));
												
				return ind_eva;
			}

		});	
	}


	// funciones privadas utilitarias para Evaluacion

	private Evaluacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Evaluacion evaluacion = new Evaluacion();

		evaluacion.setId(rs.getInt( alias + "id"));
		evaluacion.setId_cca(rs.getInt( alias + "id_cca"));
		evaluacion.setId_nte(rs.getInt( alias + "id_nte"));
		evaluacion.setId_nep(rs.getInt( alias + "id_nep"));
		evaluacion.setId_ses(rs.getInt( alias + "id_ses"));
		evaluacion.setIns(rs.getString( alias + "ins"));
		evaluacion.setEvi(rs.getString( alias + "evi"));
		evaluacion.setNump(rs.getInt(alias + "nump"));
		evaluacion.setFec_ini(rs.getDate( alias + "fec_ini"));
		evaluacion.setFec_fin(rs.getDate( alias + "fec_fin"));
		evaluacion.setEst(rs.getString( alias + "est"));
								
		return evaluacion;

	}
	
}
