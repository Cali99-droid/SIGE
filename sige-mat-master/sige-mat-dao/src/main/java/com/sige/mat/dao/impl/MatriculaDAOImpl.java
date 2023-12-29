package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
import com.tesla.colegio.model.Matricula;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.CondMatricula;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.ColSituacion;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MatriculaDAO.
 * @author MV
 *
 */
public class MatriculaDAOImpl{
	final static Logger logger = Logger.getLogger(MatriculaDAOImpl.class);
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
	public int saveOrUpdate(Matricula matricula) {
		if (matricula.getId() != null) {
			// update
			String sql = "UPDATE mat_matricula "
						+ "SET id_alu=?, "
						+ "id_fam=?, "
						+ "id_enc=?, "
						+ "id_per_res=?, "
						+ "id_fam_res_pag=?, "
						+ "id_fam_res_aca=?, "
						+ "id_con=?, "
						+ "id_cli=?, "
						+ "id_per=?, "
						+ "id_cic=?, "
						+ "id_cct=?, "
						+ "id_au=?, "
						+ "id_gra=?, "
						+ "id_niv=?, "
						+ "tipo=?, "
						+ "tip_mat=?, "
						+ "id_col=?, "
						+ "mat_val=?, "
						+ "fecha=?, "
						+ "car_pod=?, "
						+ "actyc=?, "
						+ "srvint=?, "
						+ "camweb=?, "
						+ "num_cont=?, "
						+ "num_adenda=?, "
						+ "obs=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						matricula.getId_alu(),
						matricula.getId_fam(),
						matricula.getId_enc(),
						matricula.getId_per_res(),
						matricula.getId_fam_res_pag(),
						matricula.getId_fam_res_aca(),
						matricula.getId_con(),
						matricula.getId_cli(),
						matricula.getId_per(),
						matricula.getId_cic(),
						matricula.getId_cct(),
						matricula.getId_au(),
						matricula.getId_gra(),
						matricula.getId_niv(),
						matricula.getTipo(),
						matricula.getTip_mat(),
						matricula.getId_col(),
						matricula.getMat_val(),
						matricula.getFecha(),
						matricula.getCar_pod(),
						matricula.getActyc(),
						matricula.getSrvint(),
						matricula.getCamweb(),
						matricula.getNum_cont(),
						matricula.getNum_adenda(),
						matricula.getObs(),
						matricula.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						matricula.getId()); 
			return matricula.getId();

		} else {
			// insert
			String sql = "insert into mat_matricula ("
						+ "id_alu, "
						+ "id_fam, "
						+ "id_enc, "
						+ "id_per_res, "
						+ "id_fam_res_pag, "
						+ "id_fam_res_aca, "
						+ "id_con, "
						+ "id_cli, "
						+ "id_per, "
						+ "id_cic, "
						+ "id_cct, "
						+ "id_au, "
						+ "id_au_asi, "
						+ "id_suc_con, "
						+ "id_gra, "
						+ "id_niv, "
						+ "tipo, "
						+ "tip_mat, "
						+ "id_col, "
						+ "mat_val, "
						+ "fecha, "
						+ "car_pod, "
						+ "actyc, "
						+ "srvint, "
						+ "camweb, "
						+ "num_cont, "
						+ "num_adenda, "
						+ "obs, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? , ?, ?, ?, ?, ?, ?, ?,?,?,?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				matricula.getId_alu(),
				matricula.getId_fam(),
				matricula.getId_enc(),
				matricula.getId_per_res(),
				matricula.getId_fam_res_pag(),
				matricula.getId_fam_res_aca(),
				matricula.getId_con(),
				matricula.getId_cli(),
				matricula.getId_per(),
				matricula.getId_cic(),
				matricula.getId_cct(),
				matricula.getId_au(),
				matricula.getId_au_asi(),
				matricula.getId_suc_con(),
				matricula.getId_gra(),
				matricula.getId_niv(),
				matricula.getTipo(),
				matricula.getTip_mat(),
				matricula.getId_col(),
				matricula.getMat_val(),
				matricula.getFecha(),
				matricula.getCar_pod(),
				matricula.getActyc(),
				matricula.getSrvint(),
				matricula.getCamweb(),
				matricula.getNum_cont(),
				matricula.getNum_adenda(),
				matricula.getObs(),
				matricula.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_matricula where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Matricula> list() {
		String sql = "select * from mat_matricula";
		
		//logger.info(sql);
		
		List<Matricula> listMatricula = jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			@Override
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMatricula;
	}

	public Matricula get(int id) {
		String sql = "select * from mat_matricula WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Matricula>() {

			@Override
			public Matricula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Matricula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mat.id mat_id, mat.id_alu mat_id_alu , mat.id_col mat_id_col, mat.tipo mat_tipo, mat.camweb mat_camweb,  mat.srvint mat_srvint,  mat.id_fam mat_id_fam, mat.id_per_res mat_id_per_res , mat.id_fam_res_pag mat_id_fam_res_pag, mat.id_fam_res_aca mat_id_fam_res_aca, mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per, mat.id_cic mat_id_cic , mat.id_cct mat_id_cct, mat.actyc mat_actyc, mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont, mat.num_adenda mat_num_adenda , mat.mat_val mat_mat_val, mat.obs mat_obs  ,mat.est mat_est, mat.id_suc_con mat_id_suc_con, mat.id_sit mat_id_sit, mat.id_au_asi mat_id_au_asi, mat.con_val mat_con_val  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("cat_cond_matricula"))
			sql = sql + ", cma.id cma_id  , cma.nom cma_nom , cma.des cma_des  ";
		if (aTablas.contains("cat_cliente"))
			sql = sql + ", cli.id cli_id  , cli.nom cli_nom  ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + ", ccs.id ccs_id  , ccs.cod  ccs_cod , ccs.nom ccs_nom  ";
	
		sql = sql + " from mat_matricula mat "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = mat.id_enc ";
		if (aTablas.contains("cat_cond_matricula"))
			sql = sql + " left join cat_cond_matricula cma on cma.id = mat.id_con ";
		if (aTablas.contains("cat_cliente"))
			sql = sql + " left join cat_cliente cli on cli.id = mat.id_cli ";
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = mat.id_per ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula aula on aula.id = mat.id_au_asi ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad grad on grad.id = mat.id_gra ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel nvl on nvl.id = mat.id_niv ";
		if (aTablas.contains("cat_col_situacion"))
			sql = sql + " left join cat_col_situacion ccs on ccs.id = mat.id_sit ";
		sql = sql + " where mat.id= " + id; 
				
	
		return jdbcTemplate.query(sql, new ResultSetExtractor<Matricula>() {
		
			@Override
			public Matricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Matricula matricula= rsToEntity(rs,"mat_");
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
							alumno.setId_tap(rs.getString("alu_id_tap")) ;  
							alumno.setId_gen(rs.getString("alu_id_gen")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
							alumno.setNom(rs.getString("alu_nom")) ;  
							alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
							alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
							alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							alumno.setFoto(rs.getBytes("alu_foto")) ;  
							matricula.setAlumno(alumno);
					}
					/*
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							matricula.setFamiliar(familiar);
					}*/
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							matricula.setFamiliar(familiar);
					}
					if (aTablas.contains("cat_cond_matricula")){
						CondMatricula condmatricula = new CondMatricula();  
							condmatricula.setId(rs.getInt("cma_id")) ;  
							condmatricula.setNom(rs.getString("cma_nom")) ;  
							condmatricula.setDes(rs.getString("cma_des")) ;  
							matricula.setCondMatricula(condmatricula);
					}
					if (aTablas.contains("cat_cliente")){
						Cliente cliente = new Cliente();  
							cliente.setId(rs.getInt("cli_id")) ;  
							cliente.setNom(rs.getString("cli_nom")) ;  
							matricula.setCliente(cliente);
					}
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							matricula.setPeriodo(periodo);
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
							matricula.setAula(aula);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
							grad.setNom(rs.getString("grad_nom")) ;  
							matricula.setGrad(grad);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("nvl_id")) ;  
							nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
							nivel.setNom(rs.getString("nvl_nom")) ;  
							matricula.setNivel(nivel);
					}
					if (aTablas.contains("cat_col_situacion")){
						ColSituacion colSituacion = new ColSituacion();  
						colSituacion.setId(rs.getInt("ccs_id")) ;  
						colSituacion.setCod(rs.getString("ccs_cod")) ;  
						colSituacion.setNom(rs.getString("ccs_nom")) ;  
						matricula.setColSituacion(colSituacion);
					}
							return matricula;
				}
				
				return null;
			}
			
		});


	}		
	
	public Matricula getByParams(Param param) {

		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Matricula>() {
			@Override
			public Matricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Matricula> listByParams(Param param, String[] order) {

		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			@Override
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	/*public List<Matricula> listFullByParams(Matricula matricula, String[] order) {
	
		return listFullByParams(Param.toParam("mat",matricula), order);
	
	}	*/
	
	public List<Matricula> listFullByParams(Param param, String[] order) {

		String sql = "select mat.id mat_id, mat.id_alu mat_id_alu , mat.id_col mat_id_col, mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.tipo mat_tipo, mat.id_per_res mat_id_per_res, mat.id_fam_res_pag mat_id_fam_res_pag, mat.id_fam_res_aca mat_id_fam_res_aca, mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_cic mat_id_cic, mat.id_cct mat_id_cct, mat.tipo mat_tipo, mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.actyc mat_actyc, mat.srvint mat_srvint, mat.camweb mat_camweb, mat.num_cont mat_num_cont , mat.num_adenda mat_num_adenda, mat.mat_val mat_mat_val,  mat.obs mat_obs  ,mat.est mat_est , mat.id_suc_con mat_id_suc_con,mat.id_sit mat_id_sit,mat.id_au_asi mat_id_au_asi, mat.con_val mat_con_val ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.usuario alu_usuario, alu.pass_educando alu_pass_educando , alu.foto alu_foto, alu.id_classRoom  alu_id_classRoom";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", cma.id cma_id  , cma.nom cma_nom , cma.des cma_des  ";
		sql = sql + ", cli.id cli_id  , cli.nom cli_nom  ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat, pee.id_suc pee_id_suc  ";
		sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
		sql = sql + " from mat_matricula mat";
		sql = sql + " inner join alu_alumno alu on alu.id = mat.id_alu ";
		sql = sql + " left join alu_familiar fam on fam.id = mat.id_fam ";
		sql = sql + " left join cat_cond_matricula cma on cma.id = mat.id_con ";
		sql = sql + " left join cat_cliente cli on cli.id = mat.id_cli ";
		sql = sql + " left join col_aula aula on aula.id = mat.id_au_asi ";
		sql = sql + " inner join per_periodo pee on pee.id = mat.id_per ";
		sql = sql + " inner join cat_grad grad on grad.id = mat.id_gra ";
		sql = sql + " inner join cat_nivel nvl on nvl.id = mat.id_niv ";
		sql = sql + " inner join ges_servicio ser on ser.id = pee.id_srv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			@Override
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula= rsToEntity(rs,"mat_");
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ; 
				alumno.setId_classRoom(rs.getString("alu_id_classRoom"));
				alumno.setFoto(rs.getBytes("alu_foto")) ;
				alumno.setUsuario(rs.getString("alu_usuario"));
				matricula.setAlumno(alumno);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				matricula.setFamiliar(familiar);
				Familiar familiar2 = new Familiar();  
				familiar2.setId(rs.getInt("fam_id")) ;  
				familiar2.setNom(rs.getString("fam_nom")) ;  
				matricula.setFamiliar(familiar2);
				CondMatricula condmatricula = new CondMatricula();  
				condmatricula.setId(rs.getInt("cma_id")) ;  
				condmatricula.setNom(rs.getString("cma_nom")) ;  
				condmatricula.setDes(rs.getString("cma_des")) ;  
				matricula.setCondMatricula(condmatricula);
				Cliente cliente = new Cliente();  
				cliente.setId(rs.getInt("cli_id")) ;  
				cliente.setNom(rs.getString("cli_nom")) ;  
				matricula.setCliente(cliente);
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;
				periodo.setId_suc(rs.getInt("pee_id_suc"));
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				matricula.setPeriodo(periodo);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("aula_id")) ;  
				aula.setId_per(rs.getInt("aula_id_per")) ;  
				aula.setId_grad(rs.getInt("aula_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("aula_id_tur")) ;  
				aula.setSecc(rs.getString("aula_secc")) ;  
				aula.setCap(rs.getInt("aula_cap")) ;  
				matricula.setAula(aula);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				matricula.setGrad(grad);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("nvl_id")) ;  
				nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
				nivel.setNom(rs.getString("nvl_nom")) ;  
				matricula.setNivel(nivel);
				return matricula;
			}

		});

	}	




	// funciones privadas utilitarias para Matricula

	private Matricula rsToEntity(ResultSet rs,String alias) throws SQLException {
		Matricula matricula = new Matricula();

		matricula.setId(rs.getInt( alias + "id"));
		matricula.setId_alu(rs.getInt( alias + "id_alu"));
		matricula.setId_fam(rs.getInt( alias + "id_fam"));
		matricula.setId_enc(rs.getInt( alias + "id_enc"));
		matricula.setId_per_res(rs.getInt( alias + "id_per_res"));
		matricula.setId_fam_res_aca(rs.getInt( alias + "id_fam_res_aca"));
		matricula.setId_fam_res_pag(rs.getInt( alias + "id_fam_res_pag"));
		matricula.setId_con(rs.getInt( alias + "id_con"));
		matricula.setId_cli(rs.getInt( alias + "id_cli"));
		matricula.setId_per(rs.getInt( alias + "id_per"));
		matricula.setId_cic(rs.getInt( alias + "id_cic"));
		matricula.setId_cct(rs.getInt( alias + "id_cct"));
		matricula.setId_sit(rs.getInt( alias + "id_sit"));
		matricula.setId_au(rs.getInt( alias + "id_au"));
		matricula.setId_au_asi(rs.getInt( alias + "id_au_asi"));
		matricula.setId_gra(rs.getInt( alias + "id_gra"));
		matricula.setId_suc_con(rs.getInt( alias + "id_suc_con"));
		matricula.setId_niv(rs.getInt( alias + "id_niv"));
		matricula.setFecha(rs.getDate( alias + "fecha"));
		matricula.setCar_pod(rs.getString( alias + "car_pod"));
		matricula.setActyc(rs.getString( alias + "actyc"));
		matricula.setSrvint(rs.getString( alias + "srvint"));
		matricula.setCamweb(rs.getString( alias + "camweb"));
		matricula.setNum_cont(rs.getString( alias + "num_cont"));
		matricula.setNum_adenda(rs.getString( alias + "num_adenda"));
		matricula.setObs(rs.getString( alias + "obs"));
		matricula.setEst(rs.getString( alias + "est"));
		matricula.setTipo(rs.getString( alias + "tipo"));
		matricula.setId_col(rs.getInt(alias + "id_col"));
		matricula.setMat_val(rs.getString(alias + "mat_val"));
		matricula.setCon_val(rs.getString(alias + "con_val"));
		

		return matricula;

	}
	
	/**
	 * Obtiene los datos mas utilizados de un matricula
	 * @param id_mat
	 * @return
	 */
	public Map<String,Object> getMatriculaDatosPrincipales(Integer id_mat){
		
		String sql = "select concat( p.ape_pat,' ' ,p.ape_mat, ', ' , p.nom) as alumno , srv.id_suc , mat.id, "
				+ " suc.nom as local, srv.nom nivel, g.nom grado, au.secc, per.id_niv, mat.id_fam, mat.id_alu, au.id id_au, alu.usuario, mat.id_per, mat.num_cont, per.id_anio , a.nom anio"
				+ " from mat_matricula mat"
				+ " inner  join alu_alumno alu on alu.id = mat.id_alu"
				+ " inner  join col_persona p on alu.id_per = p.id"
				+ " inner join col_aula au on au.id = mat.id_au_asi"
				//+ " inner join per_periodo per on per.id = au.id_per"
				+ " inner join per_periodo per on per.id = mat.id_per"
				+ " inner join col_anio a ON per.id_anio=a.id"
				+ " inner join cat_grad g on g.id = mat.id_gra"
				+ " inner join ges_servicio srv on srv.id = per.id_srv"
				+ " inner join ges_sucursal suc on suc.id = srv.id_suc"
				+ " where mat.id=?"
				+ " order by alu.ape_pat, alu.ape_mat, alu.nom";
		
		List<Map<String,Object>> lista = jdbcTemplate.queryForList(sql,new Object[]{id_mat});
		
		if (lista.size()==0)
			return null;
		else
			return lista.get(0);
		
	}
}
