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
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.ReservaCuota;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.CondMatricula;
import com.tesla.colegio.model.Cliente;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Familiar;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ReservaDAO.
 * @author MV
 *
 */
public class ReservaDAOImpl{
	final static Logger logger = Logger.getLogger(ReservaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Reserva reserva) {
		if (reserva.getId() != null) {
			// update
			String sql = "UPDATE mat_reserva "
						+ "SET id_alu=?, "
						+ "id_au=?, "
						+ "id_gra=?, "
						+ "id_niv=?, "
						+ "id_con=?, "
						+ "id_cli=?, "
						+ "id_per=?, "
						+ "id_fam=?, "
						+ "fec=?, "
						+ "fec_lim=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						reserva.getId_alu(),
						reserva.getId_au(),
						reserva.getId_gra(),
						reserva.getId_niv(),
						reserva.getId_con(),
						reserva.getId_cli(),
						reserva.getId_per(),
						reserva.getId_fam(),
						reserva.getFec(),
						reserva.getFec_lim(),
						reserva.getEst(),
						reserva.getUsr_act(),
						new java.util.Date(),
						reserva.getId()); 
			return reserva.getId();

		} else {
			// insert
			String sql = "insert into mat_reserva ("
						+ "id_alu, "
						+ "id_au, "
						+ "id_gra, "
						+ "id_niv, "
						+ "id_con, "
						+ "id_cli, "
						+ "id_per, "
						+ "id_fam, "
						+ "fec, "
						+ "fec_lim, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				reserva.getId_alu(),
				reserva.getId_au(),
				reserva.getId_gra(),
				reserva.getId_niv(),
				reserva.getId_con(),
				reserva.getId_cli(),
				reserva.getId_per(),
				reserva.getId_fam(),
				reserva.getFec(),
				reserva.getFec_lim(),
				reserva.getEst(),
				reserva.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_reserva where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Reserva> list() {
		String sql = "select * from mat_reserva";
		
		//logger.info(sql);
		
		List<Reserva> listReserva = jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			@Override
			public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listReserva;
	}

	public Reserva get(int id) {
		String sql = "select * from mat_reserva WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Reserva>() {

			@Override
			public Reserva extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Reserva getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mat_res.id mat_res_id, mat_res.id_alu mat_res_id_alu , mat_res.id_au mat_res_id_au , mat_res.id_gra mat_res_id_gra , mat_res.id_niv mat_res_id_niv , mat_res.id_con mat_res_id_con , mat_res.id_cli mat_res_id_cli , mat_res.id_per mat_res_id_per , mat_res.id_fam mat_res_id_fam , mat_res.fec mat_res_fec , mat_res.fec_lim mat_res_fec_lim  ,mat_res.est mat_res_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("col_persona_a"))
			sql = sql + ", pa.id pa_id  , pa.id_tdc pa_id_tdc , pa.id_eci pa_id_eci , pa.id_gen pa_id_gen, pa.nro_doc pa_nro_doc , pa.nom pa_nom , pa.ape_pat pa_ape_pat , pa.ape_mat pa_ape_mat , pa.fec_nac pa_fec_nac , pa.tlf pa_tlf , pa.cel pa_cel  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
		if (aTablas.contains("cat_cond_matricula"))
			sql = sql + ", cma.id cma_id  , cma.nom cma_nom , cma.des cma_des  ";
		if (aTablas.contains("cat_cliente"))
			sql = sql + ", cli.id cli_id  , cli.nom cli_nom  ";
		if (aTablas.contains("per_periodo")){
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
			sql = sql + ", ani.nom ani_nom, ani.id ani_id ";
		}if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat  ";
		if (aTablas.contains("col_persona_f"))
			sql = sql + ", pf.id pf_id  , pf.nom pf_nom , pf.ape_pat pf_ape_pat , pf.ape_mat pf_ape_mat  ";
		if (aTablas.contains("fac_reserva_cuota"))
			sql = sql + ", fre.id fre_id  , fre.monto fre_monto, fre.nro_recibo fre_nro_recibo, fre.id_fmo fre_id_fmo  ";

		
		sql = sql + " from mat_reserva mat_res "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " inner join alu_alumno alu on alu.id = mat_res.id_alu ";
		if (aTablas.contains("col_persona_a"))
			sql = sql + " inner join col_persona pa on alu.id_per = pa.id ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " inner join cat_grad grad on grad.id = mat_res.id_gra ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " inner join cat_nivel nvl on nvl.id = mat_res.id_niv ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula aula on aula.id = mat_res.id_au ";
		if (aTablas.contains("cat_cond_matricula"))
			sql = sql + " left join cat_cond_matricula cma on cma.id = mat_res.id_con ";
		if (aTablas.contains("cat_cliente"))
			sql = sql + " left join cat_cliente cli on cli.id = mat_res.id_cli ";
		if (aTablas.contains("per_periodo")){
			sql = sql + " left join per_periodo pee on pee.id = mat_res.id_per ";
			sql = sql + " left join col_anio ani on ani.id = pee.id_anio ";
		}if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = mat_res.id_fam ";
		if (aTablas.contains("col_persona_f"))
		sql = sql + " left join col_persona pf on pf.id = fam.id_per ";
		if (aTablas.contains("fac_reserva_cuota"))
			sql = sql + " left join fac_reserva_cuota fre on fre.id_res = mat_res.id ";
		sql = sql + " where mat_res.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Reserva>() {
		
			@Override
			public Reserva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Reserva reserva= rsToEntity(rs,"mat_res_");
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
							reserva.setAlumno(alumno);
					}
					if (aTablas.contains("col_persona_a")){
							Persona persona_alu = new Persona();  
							persona_alu.setId(rs.getInt("pa_id")) ;  
							persona_alu.setId_tdc(rs.getString("pa_id_tdc")) ;   
							persona_alu.setId_eci(rs.getInt("pa_id_eci")) ;  
							persona_alu.setId_gen(rs.getString("pa_id_gen")) ;   
							persona_alu.setNro_doc(rs.getString("pa_nro_doc")) ;  
							persona_alu.setNom(rs.getString("pa_nom")) ;  
							persona_alu.setApe_pat(rs.getString("pa_ape_pat")) ;  
							persona_alu.setApe_mat(rs.getString("pa_ape_mat")) ;  
							persona_alu.setFec_nac(rs.getDate("pa_fec_nac")) ;     
							persona_alu.setTlf(rs.getString("pa_tlf")) ;  
							persona_alu.setCel(rs.getString("pa_cel")) ;  
							reserva.setPersona_alu(persona_alu);
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
							reserva.setAula(aula);
					}
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
							grad.setNom(rs.getString("grad_nom")) ;  
							reserva.setGrad(grad);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("nvl_id")) ;  
							nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
							nivel.setNom(rs.getString("nvl_nom")) ;  
							reserva.setNivel(nivel);
					}
					if (aTablas.contains("cat_cond_matricula")){
						CondMatricula condmatricula = new CondMatricula();  
							condmatricula.setId(rs.getInt("cma_id")) ;  
							condmatricula.setNom(rs.getString("cma_nom")) ;  
							condmatricula.setDes(rs.getString("cma_des")) ;  
							reserva.setCondMatricula(condmatricula);
					}
					if (aTablas.contains("cat_cliente")){
						Cliente cliente = new Cliente();  
							cliente.setId(rs.getInt("cli_id")) ;  
							cliente.setNom(rs.getString("cli_nom")) ;  
							reserva.setCliente(cliente);
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
							
							Anio anio = new Anio();
							anio.setId(rs.getInt("ani_id")) ;
							anio.setNom(rs.getString("ani_nom")) ;
							periodo.setAnio(anio);
							reserva.setPeriodo(periodo);
					}
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;
							familiar.setApe_pat(rs.getString("fam_ape_pat")) ;
							familiar.setApe_mat(rs.getString("fam_ape_mat")) ;
							reserva.setFamiliar(familiar);
					}
					if (aTablas.contains("col_persona_f")){
						Persona persona_fam= new Persona();  
						persona_fam.setId(rs.getInt("pf_id")) ;  
						persona_fam.setNom(rs.getString("pf_nom")) ;  
						persona_fam.setApe_pat(rs.getString("pf_ape_pat")) ;  
						persona_fam.setApe_mat(rs.getString("pf_ape_mat")) ;    
						reserva.setPersona_fam(persona_fam);
					}
					if (aTablas.contains("fac_reserva_cuota")){
						ReservaCuota reservacuota = new ReservaCuota();  
						reservacuota.setId(rs.getInt("fre_id")) ;  
						reservacuota.setNro_recibo(rs.getString("fre_nro_recibo")) ;  
						reservacuota.setId_fmo(rs.getInt("fre_id_fmo")) ;  
						reservacuota.setMonto(rs.getBigDecimal("fre_monto"));
						reserva.setReservaCuota(reservacuota);
					}
							return reserva;
				}
				
				return null;
			}
			
		});


	}		
	
	public Reserva getByParams(Param param) {

		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Reserva>() {
			@Override
			public Reserva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Reserva> listByParams(Param param, String[] order) {

		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			@Override
			public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Reserva> listFullByParams(Reserva reserva, String[] order) {
	
		return listFullByParams(Param.toParam("mat_res",reserva), order);
	
	}	
	
	public List<Reserva> listFullByParams(Param param, String[] order) {

		String sql = "select mat_res.id mat_res_id, mat_res.id_alu mat_res_id_alu , mat_res.id_au mat_res_id_au , mat_res.id_gra mat_res_id_gra , mat_res.id_niv mat_res_id_niv , mat_res.id_con mat_res_id_con , mat_res.id_cli mat_res_id_cli , mat_res.id_per mat_res_id_per , mat_res.id_fam mat_res_id_fam ,  mat_res.fec mat_res_fec , mat_res.fec_lim mat_res_fec_lim  ,mat_res.est mat_res_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + ", nvl.id nvl_id  , nvl.cod_mod nvl_cod_mod , nvl.nom nvl_nom  ";
		sql = sql + ", cma.id cma_id  , cma.nom cma_nom , cma.des cma_des  ";
		sql = sql + ", cli.id cli_id  , cli.nom cli_nom  ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", fre.id fre_id  , fre.monto fre_monto, fre.nro_recibo fre_nro_recibo  ";
		sql = sql + " from mat_reserva mat_res";
		sql = sql + " left join alu_alumno alu on alu.id = mat_res.id_alu ";
		sql = sql + " left join fac_reserva_cuota fre on fre.id_res = mat_res.id ";
		sql = sql + " left join col_aula aula on aula.id = mat_res.id_au ";
		sql = sql + " left join cat_grad grad on grad.id = mat_res.id_gra ";
		sql = sql + " left join cat_nivel nvl on nvl.id = mat_res.id_niv ";
		sql = sql + " left join cat_cond_matricula cma on cma.id = mat_res.id_con ";
		sql = sql + " left join cat_cliente cli on cli.id = mat_res.id_cli ";
		sql = sql + " left join per_periodo pee on pee.id = mat_res.id_per ";
		sql = sql + " left join alu_familiar fam on fam.id = mat_res.id_fam ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			@Override
			public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				Reserva reserva= rsToEntity(rs,"mat_res_");
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
				reserva.setAlumno(alumno);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("aula_id")) ;  
				aula.setId_per(rs.getInt("aula_id_per")) ;  
				aula.setId_grad(rs.getInt("aula_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("aula_id_tur")) ;  
				aula.setSecc(rs.getString("aula_secc")) ;  
				aula.setCap(rs.getInt("aula_cap")) ;  
				reserva.setAula(aula);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				reserva.setGrad(grad);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("nvl_id")) ;  
				nivel.setCod_mod(rs.getString("nvl_cod_mod")) ;  
				nivel.setNom(rs.getString("nvl_nom")) ;  
				reserva.setNivel(nivel);
				CondMatricula condmatricula = new CondMatricula();  
				condmatricula.setId(rs.getInt("cma_id")) ;  
				condmatricula.setNom(rs.getString("cma_nom")) ;  
				condmatricula.setDes(rs.getString("cma_des")) ;  
				reserva.setCondMatricula(condmatricula);
				Cliente cliente = new Cliente();  
				cliente.setId(rs.getInt("cli_id")) ;  
				cliente.setNom(rs.getString("cli_nom")) ;  
				reserva.setCliente(cliente);
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				reserva.setPeriodo(periodo);
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				reserva.setFamiliar(familiar);
				
				ReservaCuota reservaCuota = new ReservaCuota();
				reservaCuota.setMonto(rs.getBigDecimal("fre_monto"));
				reservaCuota.setNro_recibo(rs.getString("fre_nro_recibo"));
				reserva.setReservaCuota(reservaCuota);
				return reserva;
			}

		});

	}	




	// funciones privadas utilitarias para Reserva

	private Reserva rsToEntity(ResultSet rs,String alias) throws SQLException {
		Reserva reserva = new Reserva();

		reserva.setId(rs.getInt( alias + "id"));
		reserva.setId_alu(rs.getInt( alias + "id_alu"));
		reserva.setId_au(rs.getInt( alias + "id_au"));
		reserva.setId_gra(rs.getInt( alias + "id_gra"));
		reserva.setId_niv(rs.getInt( alias + "id_niv"));
		reserva.setId_con(rs.getInt( alias + "id_con"));
		reserva.setId_cli(rs.getInt( alias + "id_cli"));
		reserva.setId_per(rs.getInt( alias + "id_per"));
		reserva.setId_fam(rs.getInt( alias + "id_fam"));
		reserva.setFec(rs.getDate( alias + "fec"));
		reserva.setFec_lim(rs.getDate( alias + "fec_lim"));
		reserva.setEst(rs.getString( alias + "est"));

		return reserva;

	}
	
}
