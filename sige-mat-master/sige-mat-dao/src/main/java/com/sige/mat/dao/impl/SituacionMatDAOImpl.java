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
import com.tesla.colegio.model.SituacionMat;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.Grad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SituacionMatDAO.
 * @author MV
 *
 */
public class SituacionMatDAOImpl{
	final static Logger logger = Logger.getLogger(SituacionMatDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(SituacionMat situacion_mat) {
		if (situacion_mat.getId() != null) {
			// update
			String sql = "UPDATE col_situacion_mat "
						+ "SET id_mat=?, "
						+ "id_sit=?, "
						+ "mot=?, "
						+ "fec=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						situacion_mat.getId_mat(),
						situacion_mat.getId_sit(),
						situacion_mat.getMot(),
						situacion_mat.getFec(),
						situacion_mat.getEst(),
						situacion_mat.getUsr_act(),
						new java.util.Date(),
						situacion_mat.getId()); 
			return situacion_mat.getId();

		} else {
			// insert
			String sql = "insert into col_situacion_mat ("
						+ "id_mat, "
						+ "id_sit, "
						+ "mot, "
						+ "fec, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				situacion_mat.getId_mat(),
				situacion_mat.getId_sit(),
				situacion_mat.getMot(),
				situacion_mat.getFec(),
				situacion_mat.getEst(),
				situacion_mat.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_situacion_mat where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SituacionMat> list() {
		String sql = "select * from col_situacion_mat";
		
		//logger.info(sql);
		
		List<SituacionMat> listSituacionMat = jdbcTemplate.query(sql, new RowMapper<SituacionMat>() {

			@Override
			public SituacionMat mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSituacionMat;
	}

	public SituacionMat get(int id) {
		String sql = "select * from col_situacion_mat WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SituacionMat>() {

			@Override
			public SituacionMat extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SituacionMat getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csm.id csm_id, csm.id_mat csm_id_mat , csm.id_sit csm_id_sit , csm.mot csm_mot , csm.fec csm_fec  ,csm.est csm_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.nom niv_nom ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.nom gra_nom ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.secc au_secc ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
	
		sql = sql + " from col_situacion_mat csm "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = csm.id_mat ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + " left join cat_col_situacion cma on cma.id = csm.id_sit ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = mat.id_niv ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = mat.id_gra ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = mat.id_au ";
		sql = sql + " where csm.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SituacionMat>() {
		
			@Override
			public SituacionMat extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SituacionMat situacionmat= rsToEntity(rs,"csm_");
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
							if (aTablas.contains("alu_alumno")){
								Alumno alumno= new Alumno();
								alumno.setApe_pat(rs.getString("alu_ape_pat"));
								alumno.setApe_mat(rs.getString("alu_ape_mat"));
								alumno.setNom(rs.getString("alu_nom"));
								alumno.setId(rs.getInt("alu_id"));
								matricula.setAlumno(alumno);
							}
							if (aTablas.contains("cat_nivel")){
								Nivel nivel= new Nivel();
								nivel.setId(rs.getInt("niv_id"));
								nivel.setNom(rs.getString("niv_nom"));
								matricula.setNivel(nivel);
							}
							if (aTablas.contains("cat_grad")){
								Grad grad=new Grad();
								grad.setId(rs.getInt("gra_id"));
								grad.setNom(rs.getString("gra_nom"));
								matricula.setGrad(grad);
							}
							if (aTablas.contains("col_aula")){
								Aula aula=new Aula();
								aula.setId(rs.getInt("au_id"));
								aula.setSecc(rs.getString("au_secc"));
								matricula.setAula(aula);
							}
							situacionmat.setMatricula(matricula);
					}
					if (aTablas.contains("cat_col_situacion")){
						ColSituacion colsituacion = new ColSituacion();  
							colsituacion.setId(rs.getInt("cma_id")) ;  
							colsituacion.setCod(rs.getString("cma_cod")) ;  
							colsituacion.setNom(rs.getString("cma_nom")) ;  
							colsituacion.setDes(rs.getString("cma_des")) ;  
							situacionmat.setColSituacion(colsituacion);
					}
							return situacionmat;
				}
				
				return null;
			}
			
		});


	}		
	
	public SituacionMat getByParams(Param param) {

		String sql = "select * from mat_traslado_detalle " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SituacionMat>() {
			@Override
			public SituacionMat extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SituacionMat> listByParams(Param param, String[] order) {

		String sql = "select * from col_situacion_mat " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SituacionMat>() {

			@Override
			public SituacionMat mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SituacionMat> listFullByParams(SituacionMat situacionmat, String[] order) {
	
		return listFullByParams(Param.toParam("csm",situacionmat), order);
	
	}	
	
	public List<SituacionMat> listFullByParams(Param param, String[] order) {

		String sql = "select csm.id csm_id, csm.id_mat csm_id_mat , csm.id_sit csm_id_sit , csm.mot csm_mot , csm.fec csm_fec  ,csm.est csm_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", alu.id alu_id, alu.nom alu_nom, alu.ape_pat alu_ape_pat, alu.ape_mat alu_ape_mat";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom ";
		sql = sql + ", gra.id gra_id, gra.nom gra_nom ";
		sql = sql + ", au.id au_id, au.secc au_secc ";
		sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
		sql = sql + " from col_situacion_mat csm";
		sql = sql + " left join mat_matricula mat on mat.id = csm.id_mat ";
		sql = sql + " left join cat_col_situacion cma on cma.id = csm.id_sit ";
		sql = sql + " left join alu_alumno alu on alu.id=mat.id_alu ";
		sql = sql + " left join cat_nivel niv on niv.id=mat.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id=mat.id_gra ";
		sql = sql + " left join col_aula au on au.id=mat.id_au ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<SituacionMat>() {

			@Override
			public SituacionMat mapRow(ResultSet rs, int rowNum) throws SQLException {
				SituacionMat situacionmat= rsToEntity(rs,"csm_");
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
				Alumno alumno = new Alumno();
				alumno.setId(rs.getInt("alu_id"));
				alumno.setNom(rs.getString("alu_nom"));
				alumno.setApe_pat(rs.getString("alu_ape_pat"));
				alumno.setApe_mat(rs.getString("alu_ape_mat"));
				matricula.setAlumno(alumno);
				Nivel nivel = new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				nivel.setNom(rs.getString("niv_nom"));
				matricula.setNivel(nivel);
				Grad grad= new Grad();
				grad.setId(rs.getInt("gra_id"));
				grad.setNom(rs.getString("gra_nom"));
				matricula.setGrad(grad);
				Aula aula= new Aula();
				aula.setId(rs.getInt("au_id"));
				aula.setSecc(rs.getString("au_secc"));
				matricula.setAula(aula);
				situacionmat.setMatricula(matricula);
				ColSituacion colsituacion = new ColSituacion();  
				colsituacion.setId(rs.getInt("cma_id")) ;  
				colsituacion.setCod(rs.getString("cma_cod")) ;  
				colsituacion.setNom(rs.getString("cma_nom")) ;  
				colsituacion.setDes(rs.getString("cma_des")) ;  
				situacionmat.setColSituacion(colsituacion);
				return situacionmat;
			}

		});

	}	




	// funciones privadas utilitarias para SituacionMat

	private SituacionMat rsToEntity(ResultSet rs,String alias) throws SQLException {
		SituacionMat situacion_mat = new SituacionMat();

		situacion_mat.setId(rs.getInt( alias + "id"));
		situacion_mat.setId_mat(rs.getInt( alias + "id_mat"));
		situacion_mat.setId_sit(rs.getInt( alias + "id_sit"));
		situacion_mat.setMot(rs.getString( alias + "mot"));
		situacion_mat.setFec(rs.getDate( alias + "fec"));
		situacion_mat.setEst(rs.getString( alias + "est"));
								
		return situacion_mat;

	}
	
}
