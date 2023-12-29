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
import com.tesla.colegio.model.TrasladoDetalle;

import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.Colegio;
import com.tesla.colegio.model.Condicion;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.Grad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TrasladoDetalleDAO.
 * @author MV
 *
 */
public class TrasladoDetalleDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TrasladoDetalle traslado_detalle) {
		if (traslado_detalle.getId() != null) {
			// update
			String sql = "UPDATE mat_traslado_detalle "
						+ "SET id_mat=?, "
						+ "id_sit=?, "
						+ "id_col=?, "
						+ "fec=?, "
						+ "mot=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						traslado_detalle.getId_mat(),
						traslado_detalle.getId_sit(),
						traslado_detalle.getId_col(),
						traslado_detalle.getFec(),
						traslado_detalle.getMot(),
						traslado_detalle.getEst(),
						traslado_detalle.getUsr_act(),
						new java.util.Date(),
						traslado_detalle.getId()); 
			return traslado_detalle.getId();

		} else {
			// insert
			String sql = "insert into mat_traslado_detalle ("
						+ "id_mat, "
						+ "id_sit, "
						+ "id_col, "
						+ "fec, "
						+ "mot, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				traslado_detalle.getId_mat(),
				traslado_detalle.getId_sit(),
				traslado_detalle.getId_col(),
				traslado_detalle.getFec(),
				traslado_detalle.getMot(),
				traslado_detalle.getEst(),
				traslado_detalle.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_traslado_detalle where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<TrasladoDetalle> list() {
		String sql = "select * from mat_traslado_detalle";
		
		
		
		List<TrasladoDetalle> listTrasladoDetalle = jdbcTemplate.query(sql, new RowMapper<TrasladoDetalle>() {

			@Override
			public TrasladoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTrasladoDetalle;
	}

	public TrasladoDetalle get(int id) {
		String sql = "select * from mat_traslado_detalle WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TrasladoDetalle>() {

			@Override
			public TrasladoDetalle extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TrasladoDetalle getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mtd.id mtd_id, mtd.id_mat mtd_id_mat , mtd.id_sit mtd_id_sit , mtd.id_col mtd_id_col , mtd.fec mtd_fec , mtd.mot mtd_mot  ,mtd.est mtd_est ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
		if (aTablas.contains("col_colegio"))
			sql = sql + ", col.id col_id  , col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel  ";
	
		sql = sql + " from mat_traslado_detalle mtd "; 
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = mtd.id_mat ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + " left join cat_col_situacion cma on cma.id = mtd.id_sit ";
		if (aTablas.contains("col_colegio"))
			sql = sql + " left join col_colegio col on col.id = mtd.id_col ";
		sql = sql + " where mtd.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<TrasladoDetalle>() {
		
			@Override
			public TrasladoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TrasladoDetalle trasladodetalle= rsToEntity(rs,"mtd_");
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
							trasladodetalle.setMatricula(matricula);
					}
					if (aTablas.contains("cat_col_situacion")){
						ColSituacion colsituacion = new ColSituacion();  
							colsituacion.setId(rs.getInt("cma_id")) ;  
							colsituacion.setCod(rs.getString("cma_cod")) ;  
							colsituacion.setNom(rs.getString("cma_nom")) ;  
							colsituacion.setDes(rs.getString("cma_des")) ;  
							trasladodetalle.setColSituacion(colsituacion);
					}
					if (aTablas.contains("col_colegio")){
						Colegio colegio = new Colegio();  
							colegio.setId(rs.getInt("col_id")) ;  
							colegio.setId_dist(rs.getInt("col_id_dist")) ;  
							colegio.setCod_mod(rs.getString("col_cod_mod")) ;  
							colegio.setNom_niv(rs.getString("col_nom_niv")) ;  
							colegio.setNom(rs.getString("col_nom")) ;  
							colegio.setEstatal(rs.getString("col_estatal")) ;  
							colegio.setDir(rs.getString("col_dir")) ;  
							colegio.setTel(rs.getString("col_tel")) ;  
							trasladodetalle.setColegio(colegio);
					}
							return trasladodetalle;
				}
				
				return null;
			}
			
		});


	}		
	
	public TrasladoDetalle getByParams(Param param) {

		String sql = "select * from mat_traslado_detalle " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TrasladoDetalle>() {
			@Override
			public TrasladoDetalle extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TrasladoDetalle> listByParams(Param param, String[] order) {

		String sql = "select * from mat_traslado_detalle " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<TrasladoDetalle>() {

			@Override
			public TrasladoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	/*public List<TrasladoDetalle> listFullByParams(TrasladoDetalle trasladodetalle, String[] order) {
	
		return listFullByParams(Param.toParam("mtd",trasladodetalle), order);
	
	}	*/
	
	public List<TrasladoDetalle> listFullByParams(Param param, String[] order) {

		String sql = "select mtd.id mtd_id, mtd.id_mat mtd_id_mat , mtd.id_sit mtd_id_sit , mtd.id_col mtd_id_col , mtd.fec mtd_fec , mtd.mot mtd_mot  ,mtd.est mtd_est ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + ", alu.id alu_id, p.nom alu_nom, p.ape_pat alu_ape_pat, p.ape_mat alu_ape_mat";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom ";
		sql = sql + ", gra.id gra_id, gra.nom gra_nom ";
		sql = sql + ", au.id au_id, au.secc au_secc ";
		sql = sql + ", cma.id cma_id  , cma.cod cma_cod , cma.nom cma_nom , cma.des cma_des  ";
		sql = sql + ", col.id col_id  , col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel ";
		sql = sql + ", dis.nom dis_nom, pro.nom pro_nom, dep.nom dep_nom";
		sql = sql + ", (select concat(mc.des,' - CONDICIÓN ',ctc.nom) from mat_condicion mc inner join cat_cond_alumno cca on mc.id_cond=cca.id inner join cat_tip_cond ctc on cca.id_ctc=ctc.id where mc.id_mat=mat.id limit 1) as des_cond";
		sql = sql + " from mat_traslado_detalle mtd";
		sql = sql + " left join mat_matricula mat on mat.id = mtd.id_mat ";
		sql = sql + " left join cat_col_situacion cma on cma.id = mtd.id_sit ";
		sql = sql + " left join col_colegio col on col.id = mtd.id_col ";
		sql = sql + " left join cat_distrito dis on col.id_dist=dis.id ";
		sql = sql + " left join cat_provincia pro on dis.id_pro=pro.id ";
		sql = sql + " left join cat_departamento dep on pro.id_dep=dep.id ";
		sql = sql + " left join alu_alumno alu on alu.id=mat.id_alu ";
		sql = sql + " left join col_persona p on alu.id_per=p.id ";
		sql = sql + " left join cat_nivel niv on niv.id=mat.id_niv ";
		sql = sql + " left join cat_grad gra on gra.id=mat.id_gra ";
		sql = sql + " left join col_aula au on au.id=mat.id_au_asi ";
		sql = sql + " left join per_periodo per on per.id=mat.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<TrasladoDetalle>() {

			@Override
			public TrasladoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				TrasladoDetalle trasladodetalle= rsToEntity(rs,"mtd_");
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
				trasladodetalle.setMatricula(matricula);
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
				trasladodetalle.setMatricula(matricula);
				ColSituacion colsituacion = new ColSituacion();  
				colsituacion.setId(rs.getInt("cma_id")) ;  
				colsituacion.setCod(rs.getString("cma_cod")) ;  
				colsituacion.setNom(rs.getString("cma_nom")) ;  
				colsituacion.setDes(rs.getString("cma_des")) ;  
				trasladodetalle.setColSituacion(colsituacion);
				Colegio colegio = new Colegio();  
				colegio.setId(rs.getInt("col_id")) ;  
				colegio.setId_dist(rs.getInt("col_id_dist")) ;  
				colegio.setCod_mod(rs.getString("col_cod_mod")) ;  
				colegio.setNom_niv(rs.getString("col_nom_niv")) ;  
				colegio.setNom(rs.getString("col_nom")) ;  
				colegio.setEstatal(rs.getString("col_estatal")) ;  
				colegio.setDir(rs.getString("col_dir")) ;  
				colegio.setTel(rs.getString("col_tel")) ;
				Distrito distrito = new Distrito();
				distrito.setNom(rs.getString("dis_nom"));
				colegio.setDistrito(distrito);
				Provincia provincia = new Provincia();
				provincia.setNom(rs.getString("pro_nom"));
				colegio.setProvincia(provincia);
				Departamento departamento = new Departamento();
				departamento.setNom(rs.getString("dep_nom"));
				colegio.setDepartamento(departamento);
				trasladodetalle.setColegio(colegio);
				Condicion condicion = new Condicion();
				condicion.setDes(rs.getString("des_cond"));
				trasladodetalle.setCondicion(condicion);
				return trasladodetalle;
			}

		});

	}	




	// funciones privadas utilitarias para TrasladoDetalle

	private TrasladoDetalle rsToEntity(ResultSet rs,String alias) throws SQLException {
		TrasladoDetalle traslado_detalle = new TrasladoDetalle();

		traslado_detalle.setId(rs.getInt( alias + "id"));
		traslado_detalle.setId_mat(rs.getInt( alias + "id_mat"));
		traslado_detalle.setId_sit(rs.getInt( alias + "id_sit"));
		traslado_detalle.setId_col(rs.getInt( alias + "id_col"));
		traslado_detalle.setFec(rs.getDate( alias + "fec"));
		traslado_detalle.setMot(rs.getString( alias + "mot"));
		traslado_detalle.setEst(rs.getString( alias + "est"));
								
		return traslado_detalle;

	}
	
}
