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
import com.tesla.colegio.model.Movimiento;

import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Usuario;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.MovimientoDetalle;
import com.tesla.colegio.model.Persona;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MovimientoDAO.
 * @author MV
 *
 */
public class MovimientoDAOImpl{
	final static Logger logger = Logger.getLogger(MovimientoDAOImpl.class);
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
	public int saveOrUpdate(Movimiento movimiento) {
	
		Integer id_usr_ins=null;
		if(movimiento.getId_fpa().equals(2)) {
			id_usr_ins=1;
		} else {
			id_usr_ins=tokenSeguridad.getId();
		}
		
		if (movimiento.getId() != null) {
			// update
			String sql = "UPDATE fac_movimiento "
						+ "SET tipo=?, "
						+ "fec=?, "
						+ "id_suc=?, "
						+ "id_mat=?, "
						+ "id_fam=?, "
						+ "id_per=?, "
						+ "id_fpa=?, "
						+ "monto=?, "
						+ "descuento=?, "
						+ "monto_total=?, "
						+ "nro_rec=?, "
						+ "obs=?, "
						+ "cod_res=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						movimiento.getTipo(),
						movimiento.getFec(),
						movimiento.getId_suc(),
						movimiento.getId_mat(),
						movimiento.getId_fam(),
						movimiento.getId_per(),
						movimiento.getId_fpa(),
						movimiento.getMonto(),
						movimiento.getDescuento(),
						movimiento.getMonto_total(),
						movimiento.getNro_rec(),
						movimiento.getObs(),
						movimiento.getCod_res(),
						movimiento.getEst(),
						movimiento.getUsrId(),
						new java.util.Date(),
						tokenSeguridad.getId()); 
			return movimiento.getId();

		} else {
			// insert
			String sql = "insert into fac_movimiento ("
						+ "tipo, "
						+ "fec, "
						+ "id_suc, "
						+ "id_mat, "
						+ "id_fam, "
						+ "id_per, "
						+ "id_fpa, "
						+ "monto, "
						+ "descuento, "
						+ "monto_total, "
						+ "nro_rec, "
						+ "obs, "
						+ "cod_res, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				movimiento.getTipo(),
				movimiento.getFec(),
				movimiento.getId_suc(),
				movimiento.getId_mat(),
				movimiento.getId_fam(),
				movimiento.getId_per(),
				movimiento.getId_fpa(),
				movimiento.getMonto(),
				movimiento.getDescuento(),
				movimiento.getMonto_total(),
				movimiento.getNro_rec(),
				movimiento.getObs(),
				movimiento.getCod_res(),
				movimiento.getEst(),
				id_usr_ins,
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_movimiento where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Movimiento> list() {
		String sql = "select * from fac_movimiento";
		
		//logger.info(sql);
		
		List<Movimiento> listMovimiento = jdbcTemplate.query(sql, new RowMapper<Movimiento>() {

			@Override
			public Movimiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMovimiento;
	}

	public Movimiento get(int id) {
		String sql = "select * from fac_movimiento WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Movimiento>() {

			@Override
			public Movimiento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Movimiento getFullByNroRec(String nro_rec, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fmo.id fmo_id, fmo.tipo fmo_tipo , fmo.id_fpa fmo_id_fpa, fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ,fmo.cod_res fmo_cod_res  ,fmo.est fmo_est, fmo.fec_ins fmo_fec_ins ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id, alu.ape_pat alu_ape_pat,  alu.ape_mat alu_ape_mat, alu.nom alu_nom ";
		if (aTablas.contains("col_persona_a"))
			sql = sql + ", pa.ape_pat pa_ape_pat,  pa.ape_mat pa_ape_mat, pa.nom pa_nom ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.nro_doc fam_nro_doc, fam.ape_pat fam_ape_pat,  fam.ape_mat fam_ape_mat, fam.nom fam_nom ";
			//sql = sql + ", tdc.nom tdc_nom ";
		}
		if (aTablas.contains("col_persona_f")){
			sql = sql + ", pf.id pf_id  , pf.id_tdc pf_id_tdc , pf.nro_doc pf_nro_doc, pf.ape_pat pf_ape_pat,  pf.ape_mat pf_ape_mat, pf.nom pf_nom ";
			sql = sql + ", tdc.nom tdc_nom ";
		}
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", tra.id tra_id  , tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat , tra.nom tra_nom ";
	
		sql = sql + " from fac_movimiento fmo "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = fmo.id_suc ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = fmo.id_mat ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + " left join alu_familiar fam on fam.id = fmo.id_fam ";
			//sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc";
		}
		if (aTablas.contains("col_persona_f")){
			sql = sql + " left join col_persona pf on pf.id = fam.id_per ";
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = pf.id_tdc";
		}
		if (aTablas.contains("alu_alumno")){
			if (sql.indexOf("mat_matricula")==-1)
				sql = sql + " left join mat_matricula mat on mat.id = fmo.id_mat ";
			sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu";
			}
		if (aTablas.contains("col_persona_a")){
			//if (sql.indexOf("mat_matricula")==-1)
				sql = sql + " left join col_persona pa on pa.id = alu.id_per ";
			}
		if (aTablas.contains("seg_usuario")){
			sql = sql + " left join seg_usuario usu on usu.id = fmo.usr_ins ";
			sql = sql + " left join ges_trabajador tra on tra.id = usu.id_tra ";
		}
			
		sql = sql + " where fmo.nro_rec= '" + nro_rec + "'"; 
				
		logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Movimiento>() {
		
			@Override
			public Movimiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Movimiento movimiento= rsToEntity(rs,"fmo_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							movimiento.setSucursal(sucursal);
					}
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
							movimiento.setMatricula(matricula);
					}
					
					if (aTablas.contains("alu_familiar")){
							Familiar familiar= new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setId_tdc(rs.getInt("fam_id_tdc")) ;
							familiar.setApe_mat(rs.getString("fam_ape_mat")) ;
							familiar.setApe_pat(rs.getString("fam_ape_pat")) ;
							familiar.setNro_doc(rs.getString("fam_nro_doc")) ;

							familiar.setNom(rs.getString("fam_nom")) ;
							
							//TipoDocumento tipoDocumento = new TipoDocumento();
							//tipoDocumento.setNom(rs.getString("tdc_nom"));
							//familiar.setTipoDocumento(tipoDocumento);
							movimiento.setFamiliar(familiar);
					}
					
					if (aTablas.contains("col_persona_f")){
						Persona persona_fam= new Persona();  
						persona_fam.setId(rs.getInt("pf_id")) ;  
						persona_fam.setId_tdc(rs.getString("pf_id_tdc")) ;
						persona_fam.setApe_mat(rs.getString("pf_ape_mat")) ;
						persona_fam.setApe_pat(rs.getString("pf_ape_pat")) ;
						persona_fam.setNro_doc(rs.getString("pf_nro_doc")) ;
						persona_fam.setNom(rs.getString("pf_nom")) ;
						
						TipoDocumento tipoDocumento = new TipoDocumento();
						tipoDocumento.setNom(rs.getString("tdc_nom"));
						persona_fam.setTipoDocumento(tipoDocumento);
						movimiento.setPersona_fam(persona_fam);
				}
					
				if (aTablas.contains("alu_alumno")){
						Alumno alumno= new Alumno();
						alumno.setApe_mat(rs.getString("alu_ape_mat"));
						alumno.setApe_pat(rs.getString("alu_ape_pat"));
						alumno.setNom(rs.getString("alu_nom"));
						alumno.setId(rs.getInt("alu_id"));
						Matricula matricula = movimiento.getMatricula();
						if (matricula==null)
							matricula = new Matricula();
						
						matricula.setAlumno(alumno);
						movimiento.setMatricula(matricula);
				}
				
				if (aTablas.contains("col_persona_a")){
					Persona persona_alu= new Persona();
					persona_alu.setApe_mat(rs.getString("pa_ape_mat"));
					persona_alu.setApe_pat(rs.getString("pa_ape_pat"));
					persona_alu.setNom(rs.getString("pa_nom"));
					
					Matricula matricula = movimiento.getMatricula();
					if (matricula==null)
						matricula = new Matricula();
					Alumno alumno = new Alumno();
					alumno.setPersona(persona_alu);
					matricula.setAlumno(alumno);
					movimiento.setPersona_alu(persona_alu);
				}
					
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();
						Trabajador trabajador = new Trabajador();
						trabajador.setApe_pat(rs.getString("tra_ape_pat"));
						trabajador.setApe_mat(rs.getString("tra_ape_mat"));
						trabajador.setNom(rs.getString("tra_nom"));
						usuario.setTrabajador(trabajador);
						movimiento.setUsuario(usuario);
				}
							return movimiento;
				}
				
				return null;
			}
			
		});


	}		
	
	public Movimiento getFullById(Integer id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fmo.id fmo_id, fmo.tipo fmo_tipo , fmo.id_fpa fmo_id_fpa,fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ,fmo.cod_res fmo_cod_res  ,fmo.est fmo_est, fmo.fec_ins fmo_fec_ins ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.ape_pat alu_ape_pat,  alu.ape_mat alu_ape_mat, alu.nom alu_nom ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + ", fam.id fam_id  , fam.id_tdc fam_id_tdc , fam.nro_doc fam_nro_doc, fam.ape_pat fam_ape_pat,  fam.ape_mat fam_ape_mat, fam.nom fam_nom ";
			sql = sql + ", tdc.nom tdc_nom ";
		}
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", tra.id tra_id  , tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat , tra.nom tra_nom ";
	
		sql = sql + " from fac_movimiento fmo "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = fmo.id_suc ";
		 if (aTablas.contains("alu_familiar")){
			sql = sql + " left join alu_familiar fam on fam.id = fmo.id_fam ";
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc";
			}
		if (aTablas.contains("alu_alumno")){
			if (sql.indexOf("mat_matricula")==-1)
				sql = sql + " left join mat_matricula mat on mat.id = fmo.id_mat ";
			sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu";
			}
		if (aTablas.contains("seg_usuario")){
			sql = sql + " left join seg_usuario usu on usu.id = fmo.usr_ins ";
			sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = usu.id_tra ";
		}
			
		sql = sql + " where fmo.id= '" + id + "'"; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Movimiento>() {
		
			@Override
			public Movimiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Movimiento movimiento= rsToEntity(rs,"fmo_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							movimiento.setSucursal(sucursal);
					}
					
					if (aTablas.contains("alu_familiar")){
							Familiar familiar= new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setId_tdc(rs.getInt("fam_id_tdc")) ;
							familiar.setApe_mat(rs.getString("fam_ape_mat")) ;
							familiar.setApe_pat(rs.getString("fam_ape_pat")) ;
							familiar.setNro_doc(rs.getString("fam_nro_doc")) ;

							familiar.setNom(rs.getString("fam_nom")) ;
							
							TipoDocumento tipoDocumento = new TipoDocumento();
							tipoDocumento.setNom(rs.getString("tdc_nom"));
							familiar.setTipoDocumento(tipoDocumento);
							movimiento.setFamiliar(familiar);
					}
					
					if (aTablas.contains("alu_alumno")){
						Alumno alumno= new Alumno();
						alumno.setApe_mat(rs.getString("alu_ape_mat"));
						alumno.setApe_pat(rs.getString("alu_ape_pat"));
						alumno.setNom(rs.getString("alu_nom"));
						
						Matricula matricula = movimiento.getMatricula();
						if (matricula==null)
							matricula = new Matricula();
						
						matricula.setAlumno(alumno);
						movimiento.setMatricula(matricula);
				}
					
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();
						Trabajador trabajador = new Trabajador();
						trabajador.setApe_pat(rs.getString("tra_ape_pat"));
						trabajador.setApe_mat(rs.getString("tra_ape_mat"));
						trabajador.setNom(rs.getString("tra_nom"));
						usuario.setTrabajador(trabajador);
						movimiento.setUsuario(usuario);
				}
							return movimiento;
				}
				
				return null;
			}
			
		});


	}		
	public Movimiento getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fmo.id fmo_id, fmo.tipo fmo_tipo , fmo.id_fpa fmo_id_fpa, fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs, fmo.cod_res fmo_cod_res ,fmo.est fmo_est ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + ", mat.id fam_id  , fam.id_tdc fam.id_tdc , fam.nro_doc fam_nro_doc, fam.ape_pat fam_ape_pat,  fam.ape_mat fam_ape_mat, fam.nom fam_nom ";
			sql = sql + ", tdc.nom tdc_nom ";
		}
		if (aTablas.contains("seg_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat , tra.nom tra_nom ";
	
		sql = sql + " from fac_movimiento fmo "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = fmo.id_suc ";
		if (aTablas.contains("mat_matricula"))
			sql = sql + " left join mat_matricula mat on mat.id = fmo.id_mat ";
		if (aTablas.contains("alu_familiar")){
			sql = sql + " left join alu_familiar fam on fam.id = fmo.id_fam ";
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc";
			}
		if (aTablas.contains("seg_trabajador")){
			sql = sql + " left join seg_usuario usu on usu.id = fmo.usr_ins ";
			sql = sql + " left join ges_trabajador tra on tra.id = usu.id_tra ";
		}
			
		sql = sql + " where fmo.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Movimiento>() {
		
			@Override
			public Movimiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Movimiento movimiento= rsToEntity(rs,"fmo_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							movimiento.setSucursal(sucursal);
					}
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
							movimiento.setMatricula(matricula);
					}
					
					if (aTablas.contains("alu_familiar")){
							Familiar familiar= new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setId_tdc(rs.getInt("mat_id_tdc")) ;
							familiar.setApe_mat(rs.getString("mat_ape_nat")) ;
							familiar.setNom(rs.getString("mat_nom")) ;
							
							TipoDocumento tipoDocumento = new TipoDocumento();
							tipoDocumento.setNom(rs.getString("tdc_nom"));
							familiar.setTipoDocumento(tipoDocumento);
							movimiento.setFamiliar(familiar);
					}
					
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();
						Trabajador trabajador = new Trabajador();
						trabajador.setApe_pat(rs.getString("tra_ape_pat"));
						trabajador.setApe_mat(rs.getString("tra_ape_mat"));
						trabajador.setNom(rs.getString("tra_nom"));
						usuario.setTrabajador(trabajador);
						movimiento.setUsuario(usuario);
				}
							return movimiento;
				}
				
				return null;
			}
			
		});


	}		
	public Movimiento getByParams(Param param) {

		String sql = "select * from fac_movimiento " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Movimiento>() {
			@Override
			public Movimiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Movimiento> listByParams(Param param, String[] order) {

		String sql = "select * from fac_movimiento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Movimiento>() {

			@Override
			public Movimiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Movimiento> listFullByParams(Movimiento movimiento, String[] order) {
	
		return listFullByParams(Param.toParam("fmo",movimiento), order);
	
	}	
	
	public List<Movimiento> listFullByParams(Param param, String[] order) {

		String sql = "select fmo.id fmo_id, fmo.tipo fmo_tipo , fmo.id_fpa fmo_id_fpa, fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs  ,fmo.cod_res fmo_cod_res ,fmo.est fmo_est, alu.ape_pat alu_ape_pat, alu.ape_mat alu_ape_mat, alu.nom alu_nom ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + ", mat.id mat_id  , mat.id_alu mat_id_alu , mat.id_fam mat_id_fam , mat.id_enc mat_id_enc , mat.id_con mat_id_con , mat.id_cli mat_id_cli , mat.id_per mat_id_per , mat.id_au mat_id_au , mat.id_gra mat_id_gra , mat.id_niv mat_id_niv , mat.fecha mat_fecha , mat.car_pod mat_car_pod , mat.num_cont mat_num_cont , mat.obs mat_obs  ";
		sql = sql + " from fac_movimiento fmo";
		sql = sql + " left join ges_sucursal suc on suc.id = fmo.id_suc ";
		sql = sql + " left join mat_matricula mat on mat.id = fmo.id_mat ";
		sql = sql + " left join alu_alumno alu on alu.id = mat.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Movimiento>() {

			@Override
			public Movimiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Movimiento movimiento= rsToEntity(rs,"fmo_");
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				movimiento.setSucursal(sucursal);
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
				alumno.setApe_mat(rs.getString("alu_ape_mat"));
				alumno.setApe_pat(rs.getString("alu_ape_pat"));
				alumno.setNom(rs.getString("alu_nom"));
				matricula.setAlumno(alumno);
				
				movimiento.setMatricula(matricula);
				return movimiento;
			}

		});

	}	


	public List<MovimientoDetalle> getListMovimientoDetalle(Param param, String[] order) {
		String sql = "select * from fac_movimiento_detalle " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MovimientoDetalle>() {

			@Override
			public MovimientoDetalle mapRow(ResultSet rs, int rowNum) throws SQLException {
				MovimientoDetalle movimiento_detalle = new MovimientoDetalle();

				movimiento_detalle.setId(rs.getInt("id"));
				movimiento_detalle.setId_fmo(rs.getInt("id_fmo"));
				movimiento_detalle.setId_fco(rs.getInt("id_fco"));
				movimiento_detalle.setMonto(rs.getBigDecimal("monto"));
				movimiento_detalle.setDescuento(rs.getBigDecimal("descuento"));
				movimiento_detalle.setMonto_total(rs.getBigDecimal("monto_total"));
				movimiento_detalle.setObs(rs.getString("obs"));
				movimiento_detalle.setEst(rs.getString("est"));
												
				return movimiento_detalle;
			}

		});	
	}


	// funciones privadas utilitarias para Movimiento

	private Movimiento rsToEntity(ResultSet rs,String alias) throws SQLException {
		Movimiento movimiento = new Movimiento();

		movimiento.setId(rs.getInt( alias + "id"));
		movimiento.setTipo(rs.getString( alias + "tipo"));
		movimiento.setFec(rs.getDate( alias + "fec"));
		movimiento.setFec_ins(rs.getTimestamp( alias + "fec_ins"));
		movimiento.setId_suc(rs.getInt( alias + "id_suc"));
		movimiento.setId_mat(rs.getInt( alias + "id_mat"));
		movimiento.setMonto(rs.getBigDecimal( alias + "monto"));
		movimiento.setDescuento(rs.getBigDecimal( alias + "descuento"));
		movimiento.setMonto_total(rs.getBigDecimal( alias + "monto_total"));
		movimiento.setNro_rec(rs.getString( alias + "nro_rec"));
		movimiento.setObs(rs.getString( alias + "obs"));
		movimiento.setCod_res(rs.getString( alias + "cod_res"));
		movimiento.setId_fpa(rs.getInt( alias + "id_fpa"));
		movimiento.setEst(rs.getString( alias + "est"));
								
		return movimiento;

	}
	
}
