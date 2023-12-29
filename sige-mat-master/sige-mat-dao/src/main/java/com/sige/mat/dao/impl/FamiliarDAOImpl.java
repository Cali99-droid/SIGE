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
import com.tesla.colegio.model.Familiar;

import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.Parentesco;
import com.tesla.colegio.model.EstCivil;
import com.tesla.colegio.model.GradoInstruccion;
import com.tesla.colegio.model.Religion;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.Ocupacion;
import com.tesla.colegio.model.GruFamFamiliar;
import com.tesla.colegio.model.Permisos;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface FamiliarDAO.
 * @author MV
 *
 */
public class FamiliarDAOImpl{
	final static Logger logger = Logger.getLogger(FamiliarDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public int saveOrUpdate(Familiar familiar) {
		if (familiar.getId() !=null) {
			// update
			String sql = "UPDATE alu_familiar "
						+ "SET id_per=?, "
						+ "id_par=?, "
						/*+ "id_tap=?, "
						+ "id_gen=?, "
						+ "id_eci=?, "*/
						+ "id_gin=?, "
						/*+ "id_rel=?, "
						+ "id_dist=?, "
						+ "id_pais=?, "
						+ "id_ocu=?, "
						+ "nro_doc=?, "
						+ "fec_emi_dni=?, "
						+ "ubigeo=?, "
						+ "nom=?, "
						+ "ape_pat=?, "
						+ "ape_mat=?, "
						+ "hue=?, "
						+ "fec_nac=?, "
						+ "fec_def=?, "
						+ "viv=?, "*/
						+ "viv_alu=?, "
						/*+ "dir=?, "
						+ "ref=?, "
						+ "tlf=?, "
						+ "corr=?, "
						+ "cel=?, "
						+ "cel_val=?, "
						+ "pass=?, "*/
						+ "prof=?, "
						+ "ocu=?, "
						+ "cto_tra=?, "
						+ "dir_tra=?, "
						+ "tlf_tra=?, "
						+ "cel_tra=?, "
						+ "email_tra=?, "
						+ "email_inst=?, "
						+ "flag_alu_ex=?, "
						+ "ini=?,corr_val=?,est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			 jdbcTemplate.update(sql, 
						familiar.getId_per(),
						familiar.getId_par(),
						/*familiar.getId_tap(),
						familiar.getId_gen(),
						familiar.getId_eci(),*/
						familiar.getId_gin(),
						/*familiar.getId_rel(),
						familiar.getId_dist(),
						familiar.getId_pais(),
						familiar.getId_ocu(),
						familiar.getNro_doc(),
						familiar.getFec_emi_dni(),
						familiar.getUbigeo(),
						familiar.getNom().toUpperCase(),
						familiar.getApe_pat().toUpperCase(),
						familiar.getApe_mat().toUpperCase(),
						familiar.getHue(),
						familiar.getFec_nac(),
						familiar.getFec_def(),
						familiar.getViv(),*/
						familiar.getViv_alu(),
						/*familiar.getDir()==null ? familiar.getDir(): familiar.getDir().toUpperCase(),
						familiar.getRef()==null ? familiar.getRef(): familiar.getRef().toUpperCase(),
						familiar.getTlf(),
						familiar.getCorr()==null ? familiar.getCorr(): familiar.getCorr().toUpperCase(),
						familiar.getCel(),
						familiar.getCel_val(),
						familiar.getPass(),*/
						familiar.getProf()==null ? familiar.getProf(): familiar.getProf().toUpperCase(),
						familiar.getOcu()==null ? familiar.getOcu(): familiar.getOcu().toUpperCase(),
						familiar.getCto_tra()==null ? familiar.getCto_tra(): familiar.getCto_tra().toUpperCase(),
						familiar.getDir_tra()== null ? familiar.getDir_tra(): familiar.getDir_tra().toUpperCase(),
						familiar.getTlf_tra(),
						familiar.getCel_tra(),
						familiar.getEmail_tra()==null? familiar.getEmail_tra(): familiar.getEmail_tra().toUpperCase(),
						familiar.getEmail_inst()==null? familiar.getEmail_inst(): familiar.getEmail_inst().toUpperCase(),
						familiar.getFlag_alu_ex(),
						familiar.getIni(),
						familiar.getCorr_val(),
						familiar.getEst(),
						familiar.getUsr_act(),
						new java.util.Date(),
						familiar.getId()); 
			 return familiar.getId();
		} else {
			// insert
			String sql = "insert into alu_familiar ("
						+ "id_per, "
						//+ "id_tdc, "
						+ "id_par, "
						/*+ "id_tap, "
						+ "id_gen, "
						+ "id_eci, "*/
						+ "id_gin, "
						/*+ "id_rel, "
						+ "id_dist, "
						+ "id_pais, "
						+ "id_ocu, "
						+ "nro_doc, "
						+ "fec_emi_dni, "
						+ "ubigeo, "
						+ "nom, "
						+ "ape_pat, "
						+ "ape_mat, "
						+ "hue, "
						+ "fec_nac, "
						+ "fec_def, "
						+ "viv, "*/
						+ "viv_alu, "
						/*+ "dir, "
						+ "ref, "
						+ "tlf, "
						+ "corr, "
						+ "cel, "
						+ "pass, "*/
						+ "prof, "
						+ "ocu, "
						+ "cto_tra, "
						+ "dir_tra, "
						+ "tlf_tra, "
						+ "cel_tra, "
						+ "email_tra, "
						+ "email_inst, "
						+ "flag_alu_ex, "
						+ "ini,"
						+ "corr_val,"
						+ "est,"
						+ " usr_ins,"
						+ " fec_ins) "
						/*+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
						+ "         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
						+ "         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";*/
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
						+ "         ?, ?, ?)";


				//logger.info(sql);
				jdbcTemplate.update(sql, 
				familiar.getId_per(),
				//familiar.getId_tdc(),
				familiar.getId_par(),
				/*familiar.getId_tap(),
				familiar.getId_gen(),
				familiar.getId_eci(),*/
				familiar.getId_gin(),
				/*familiar.getId_rel(),
				familiar.getId_dist(),
				familiar.getId_pais(),
				familiar.getId_ocu(),
				familiar.getNro_doc(),
				familiar.getFec_emi_dni(),
				familiar.getUbigeo(),
				familiar.getNom()==null ? familiar.getNom(): familiar.getNom().toUpperCase(),
				familiar.getApe_pat()==null ? familiar.getApe_pat(): familiar.getApe_pat().toUpperCase(),
				familiar.getApe_mat()==null ? familiar.getApe_mat(): familiar.getApe_mat().toUpperCase(),
				familiar.getHue(),
				familiar.getFec_nac(),
				familiar.getFec_def(),
				familiar.getViv(),*/
				familiar.getViv_alu(),
				/*familiar.getDir()==null ? familiar.getDir(): familiar.getDir().toUpperCase(),
				familiar.getRef()==null ? familiar.getRef(): familiar.getRef().toUpperCase(),
				familiar.getTlf(),
				familiar.getCorr()==null ? familiar.getCorr(): familiar.getCorr().toUpperCase(), 
				familiar.getCel(),
				familiar.getPass(),*/
				familiar.getProf()==null ? familiar.getProf(): familiar.getProf().toUpperCase(),
				familiar.getOcu()==null ? familiar.getOcu(): familiar.getOcu().toUpperCase(),
				familiar.getCto_tra()==null ? familiar.getCto_tra(): familiar.getCto_tra().toUpperCase(),
				familiar.getDir_tra()== null ? familiar.getDir_tra(): familiar.getDir_tra().toUpperCase(),
				familiar.getTlf_tra(),
				familiar.getCel_tra(),
				familiar.getEmail_tra()==null? familiar.getEmail_tra(): familiar.getEmail_tra().toUpperCase(),
				familiar.getEmail_inst()==null? familiar.getEmail_inst(): familiar.getEmail_inst().toUpperCase(),
				familiar.getFlag_alu_ex(),
				familiar.getIni(),
				familiar.getCorr_val(),
				familiar.getEst(),
				familiar.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public int update(Familiar familiar) {
			// update
			String sql = "UPDATE alu_familiar "
						+ "SET "
						+ "tlf=?, "
						+ "cel=?, "
						+ "usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						familiar.getTlf(),
						familiar.getCel(),
						familiar.getUsr_act(),
						new java.util.Date(),
						familiar.getId()); 

		} 
	
	
	/**
	 * Actualizar datos personales, SIN CELULAR NI CORREO
	 * @param familiar
	 * @return
	 */
	public int actualizarDatos(Familiar familiar) {
		// update
		String sql = "UPDATE alu_familiar "
					+ "SET "
					+ "ini=?, "
					+ "nom=?, "
					+ "ape_pat=?, "
					+ "ape_mat=?, "
					+ "corr=?, "
					+ "tlf=?, "
					+ "cel=?, "
					+ "id_gin=?, "
					+ "ocu=?, "
					+ "cto_tra=?, "
					+ "id_eci=?, "
					+ "id_gen=?, "
					+ "fec_nac=?, "
					+ "id_rel=?, "
					+ "dir=?, "
					+ "id_dist=?, "
					+ "viv_alu=?, "
					+ "usr_act=?,fec_act=? "
				+ "WHERE id=?";
		
		//logger.info(sql);

		return jdbcTemplate.update(sql, 
					familiar.getIni(),
					familiar.getNom()==null ? familiar.getNom() : familiar.getNom().toUpperCase().toUpperCase(),
					familiar.getApe_pat()==null ? familiar.getApe_pat()  : familiar.getApe_pat().toUpperCase(),
					familiar.getApe_mat()==null ? familiar.getApe_mat() : familiar.getApe_mat().toUpperCase(),
					familiar.getCorr()== null ? familiar.getCorr() : familiar.getCorr().toUpperCase(),
					familiar.getTlf(),
					familiar.getCel(),
					familiar.getId_gin(),
					familiar.getOcu()==null ? familiar.getOcu() : familiar.getOcu().toUpperCase(),
					familiar.getCto_tra()==null ? familiar.getCto_tra(): familiar.getCto_tra().toUpperCase(),
					familiar.getId_eci(),
					familiar.getId_gen(),
					familiar.getFec_nac(),
					familiar.getId_rel(),
					familiar.getDir()==null ? familiar.getDir() : familiar.getDir().toUpperCase(),
					familiar.getId_dist(),
					familiar.getViv_alu(),
					familiar.getUsr_act(),
					new java.util.Date(),
					familiar.getId()); 

	} 
	
	public void delete(int id) {
		String sql = "delete from alu_familiar where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Familiar> list() {
		String sql = "select * from alu_familiar";
		
		//logger.info(sql);
		
		List<Familiar> listFamiliar = jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listFamiliar;
	}

	
	public Familiar get(int id) {
		String sql = "select * from alu_familiar WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Familiar>() {

			
			public Familiar extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Familiar getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fam.id fam_id, fam.id_tdc fam_id_tdc ,fam.cel_val fam_cel_val, fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist, fam.id_pais fam_id_pais , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.fec_emi_dni fam_fec_emi_dni, fam.ubigeo fam_ubigeo, fam.nom fam_nom , fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass ,fam.ocu fam_ocu, fam.prof fam_prof, fam.cto_tra fam_cto_tra  ,fam.foto fam_foto ,fam.huella fam_huella,fam.est fam_est, fam.ini fam_ini, fam.corr_val fam_corr_val  ";
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		if (aTablas.contains("cat_parentesco"))
			sql = sql + ", pare.id pare_id  , pare.par pare_par  ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
		if (aTablas.contains("cat_religion"))
			sql = sql + ", reli.id reli_id  , reli.nom reli_nom  ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.id_pro dist_id_pro  ";
		if (aTablas.contains("cat_provincia"))
			sql = sql + ", pro.id pro_id  , pro.nom pro_nom , pro.id_dep pro_id_dep  ";
		if (aTablas.contains("cat_departamento"))
			sql = sql + ", dep.id dep_id  , dep.nom dep_nom  ";		
		if (aTablas.contains("cat_ocupacion"))
			sql = sql + ", ocu.id ocu_id  , ocu.nom ocu_nom  ";

		// TODO esto debe hacerlo el generaor cuando es 1-1
		if (aTablas.contains("alu_permisos"))
			sql = sql + ", prm.id prm_id, prm.rec_lib prm_rec_lib  , prm.ped_inf prm_ped_inf ";
	
		sql = sql + " from alu_familiar fam "; 
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc ";
		if (aTablas.contains("cat_parentesco"))
			sql = sql + " left join cat_parentesco pare on pare.id = fam.id_par ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + " left join cat_est_civil eci on eci.id = fam.id_eci ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + " left join cat_grado_instruccion gin on gin.id = fam.id_gin ";
		if (aTablas.contains("cat_religion"))
			sql = sql + " left join cat_religion reli on reli.id = fam.id_rel ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + " left join cat_distrito dist on dist.id = fam.id_dist ";
		if (aTablas.contains("cat_provincia"))
			sql = sql + " left join cat_provincia pro on pro.id = dist.id_pro ";
		if (aTablas.contains("cat_departamento"))
			sql = sql + " left join cat_departamento dep on dep.id = pro.id_dep ";
		if (aTablas.contains("cat_ocupacion"))
			sql = sql + " left join cat_ocupacion ocu on ocu.id = fam.id_ocu ";
		if (aTablas.contains("alu_permisos"))
			sql = sql + " left join alu_permisos prm on prm.id_fam = fam.id ";
		sql = sql + " where fam.id =" + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Familiar>() {
		
			
			public Familiar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Familiar familiar= rsToEntity(rs,"fam_");
					if (aTablas.contains("cat_tipo_documento")){
						TipoDocumento tipoDocumento = new TipoDocumento();  
							tipoDocumento.setId(rs.getInt("tdc_id")) ;  
							tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
							familiar.setTipoDocumento(tipoDocumento);
					}
					if (aTablas.contains("cat_parentesco")){
						Parentesco parentesco = new Parentesco();  
							parentesco.setId(rs.getInt("pare_id")) ;  
							parentesco.setPar(rs.getString("pare_par")) ;  
							familiar.setParentesco(parentesco);
					}
					if (aTablas.contains("cat_est_civil")){
						EstCivil estCivil = new EstCivil();  
							estCivil.setId(rs.getInt("eci_id")) ;  
							estCivil.setNom(rs.getString("eci_nom")) ;  
							familiar.setEstCivil(estCivil);
					}
					if (aTablas.contains("cat_grado_instruccion")){
						GradoInstruccion gradoInstruccion = new GradoInstruccion();  
							gradoInstruccion.setId(rs.getInt("gin_id")) ;  
							gradoInstruccion.setNom(rs.getString("gin_nom")) ;  
							familiar.setGradoInstruccion(gradoInstruccion);
					}
					if (aTablas.contains("cat_religion")){
						Religion religion = new Religion();  
							religion.setId(rs.getInt("reli_id")) ;  
							religion.setNom(rs.getString("reli_nom")) ;  
							familiar.setReligion(religion);
					}
					if (aTablas.contains("cat_distrito")){
						Distrito distrito = new Distrito();  
						distrito.setId(rs.getInt("dist_id")) ;  
						distrito.setNom(rs.getString("dist_nom")) ;  
						distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
						familiar.setDistrito(distrito);
						if (aTablas.contains("cat_provincia")){
							Provincia provincia = new Provincia();  
							provincia.setId(rs.getInt("pro_id")) ;  
							provincia.setNom(rs.getString("pro_nom")) ;  
							provincia.setId_dep(rs.getInt("pro_id_dep")) ;  
							distrito.setProvincia(provincia);
							if (aTablas.contains("cat_departamento")){
								Departamento departamento = new Departamento();  
								departamento.setId(rs.getInt("dep_id")) ;  
								departamento.setNom(rs.getString("dep_nom")) ;  
								provincia.setDepartamento(departamento);
							}
						}
					}
					if (aTablas.contains("cat_ocupacion")){
						Ocupacion ocupacion = new Ocupacion();  
							ocupacion.setId(rs.getInt("ocu_id")) ;  
							ocupacion.setNom(rs.getString("ocu_nom")) ;  
							familiar.setOcupacion(ocupacion);
					}
					
					//TODO esto debe hacerlo el generador para 1 to 1
					if(aTablas.contains("cat_per")) {
						Permisos permisos = new Permisos();
						permisos.setId(rs.getInt("prm_id"));
						permisos.setPed_inf(rs.getString("prm_ped_inf"));
						permisos.setRec_lib(rs.getString("prm_rec_libs"));
						familiar.setPermisos(permisos);
					}
					
							return familiar;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Familiar getByParams(Param param) {

		String sql = "select * from alu_familiar " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Familiar>() {
			
			public Familiar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Familiar> listByParams(Param param, String[] order) {

		String sql = "select * from alu_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Familiar> listFullByParams(Familiar familiar, String[] order) {
	
		return listFullByParams(Param.toParam("fam",familiar), order);
	
	}	
	
	
	public List<Familiar> listFullByParams(Param param, String[] order) {

		String sql = "select fam.id fam_id, fam.id_per fam_id_per, fam.id_tdc fam_id_tdc ,fam.cel_val fam_cel_val, fam.id_par fam_id_par , fam.id_tap fam_id_tap , fam.id_gen fam_id_gen , fam.id_eci fam_id_eci , fam.id_gin fam_id_gin , fam.id_rel fam_id_rel , fam.id_dist fam_id_dist, fam.id_pais fam_id_pais , fam.id_ocu fam_id_ocu , fam.nro_doc fam_nro_doc , fam.fec_emi_dni fam_fec_emi_dni, fam.ubigeo fam_ubigeo, fam.nom fam_nom ,  fam.ape_pat fam_ape_pat , fam.ape_mat fam_ape_mat , fam.hue fam_hue , fam.fec_nac fam_fec_nac , fam.viv fam_viv , fam.viv_alu fam_viv_alu , fam.dir fam_dir , fam.tlf fam_tlf , fam.corr fam_corr , fam.cel fam_cel , fam.pass fam_pass ,fam.ocu ocu_fam, fam.cto_tra fam_cto_tra  ,fam.foto fam_foto ,fam.huella fam_huella,fam.est fam_est, fam.ini fam_ini, fam.corr_val fam_corr_val ";
		sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		sql = sql + ", pare.id pare_id  , pare.par pare_par  ";
		sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
		sql = sql + ", reli.id reli_id  , reli.nom reli_nom  ";
		sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.id_pro dist_id_pro  ";
		sql = sql + ", ocu.id ocu_id  , ocu.nom ocu_nom  ";
		sql = sql + " from alu_familiar fam";
		sql = sql + " left join cat_tipo_documento tdc on tdc.id = fam.id_tdc ";
		sql = sql + " left join cat_parentesco pare on pare.id = fam.id_par ";
		sql = sql + " left join cat_est_civil eci on eci.id = fam.id_eci ";
		sql = sql + " left join cat_grado_instruccion gin on gin.id = fam.id_gin ";
		sql = sql + " left join cat_religion reli on reli.id = fam.id_rel ";
		sql = sql + " left join cat_distrito dist on dist.id = fam.id_dist ";
		sql = sql + " left join cat_ocupacion ocu on ocu.id = fam.id_ocu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Familiar>() {

			
			public Familiar mapRow(ResultSet rs, int rowNum) throws SQLException {
				Familiar familiar= rsToEntity(rs,"fam_");
				TipoDocumento tipoDocumento = new TipoDocumento();  
				tipoDocumento.setId(rs.getInt("tdc_id")) ;  
				tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
				familiar.setTipoDocumento(tipoDocumento);
				Parentesco parentesco = new Parentesco();  
				parentesco.setId(rs.getInt("pare_id")) ;  
				parentesco.setPar(rs.getString("pare_par")) ;  
				familiar.setParentesco(parentesco);
				EstCivil estCivil = new EstCivil();  
				estCivil.setId(rs.getInt("eci_id")) ;  
				estCivil.setNom(rs.getString("eci_nom")) ;  
				familiar.setEstCivil(estCivil);
				GradoInstruccion gradoInstruccion = new GradoInstruccion();  
				gradoInstruccion.setId(rs.getInt("gin_id")) ;  
				gradoInstruccion.setNom(rs.getString("gin_nom")) ;  
				familiar.setGradoInstruccion(gradoInstruccion);
				Religion religion = new Religion();  
				religion.setId(rs.getInt("reli_id")) ;  
				religion.setNom(rs.getString("reli_nom")) ;  
				familiar.setReligion(religion);
				Distrito distrito = new Distrito();  
				distrito.setId(rs.getInt("dist_id")) ;  
				distrito.setNom(rs.getString("dist_nom")) ;  
				distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
				familiar.setDistrito(distrito);
				Ocupacion ocupacion = new Ocupacion();  
				ocupacion.setId(rs.getInt("ocu_id")) ;  
				ocupacion.setNom(rs.getString("ocu_nom")) ;  
				familiar.setOcupacion(ocupacion);
				return familiar;
			}

		});

	}	


	public List<GruFamFamiliar> getListGruFamFamiliar(Param param, String[] order) {
		String sql = "select * from alu_gru_fam_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFamFamiliar>() {

			
			public GruFamFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamFamiliar gru_fam_familiar = new GruFamFamiliar();

				gru_fam_familiar.setId(rs.getInt("id"));
				gru_fam_familiar.setId_gpf(rs.getInt("id_gpf"));
				gru_fam_familiar.setId_fam(rs.getInt("id_fam"));
				gru_fam_familiar.setEst(rs.getString("est"));
												
				return gru_fam_familiar;
			}

		});	
	}
	public List<Permisos> getListPermisos(Param param, String[] order) {
		String sql = "select * from alu_permisos " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Permisos>() {

			
			public Permisos mapRow(ResultSet rs, int rowNum) throws SQLException {
				Permisos permisos = new Permisos();

				permisos.setId(rs.getInt("id"));
				permisos.setId_fam(rs.getInt("id_fam"));
				permisos.setRec_lib(rs.getString("rec_lib"));
				permisos.setPed_inf(rs.getString("ped_inf"));
				permisos.setEst(rs.getString("est"));
												
				return permisos;
			}

		});	
	}
	public List<Matricula> getListMatricula(Param param, String[] order) {
		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();

				matricula.setId(rs.getInt("id"));
				matricula.setId_alu(rs.getInt("id_alu"));
				matricula.setId_fam(rs.getInt("id_fam"));
				matricula.setId_enc(rs.getInt("id_enc"));
				matricula.setId_con(rs.getInt("id_con"));
				matricula.setId_cli(rs.getInt("id_cli"));
				matricula.setId_per(rs.getInt("id_per"));
				matricula.setId_au(rs.getInt("id_au"));
				matricula.setId_gra(rs.getInt("id_gra"));
				matricula.setId_niv(rs.getInt("id_niv"));
				matricula.setFecha(rs.getDate("fecha"));
				matricula.setCar_pod(rs.getString("car_pod"));
				matricula.setEst(rs.getString("est"));
												
				return matricula;
			}

		});	
	}


	// funciones privadas utilitarias para Familiar

	private Familiar rsToEntity(ResultSet rs,String alias) throws SQLException {
		Familiar familiar = new Familiar();

		familiar.setId(rs.getInt( alias + "id"));
		familiar.setId_per(rs.getInt( alias + "id_per"));
		familiar.setId_tdc(rs.getInt( alias + "id_tdc"));
		familiar.setId_par(rs.getInt( alias + "id_par"));
		familiar.setId_tap(rs.getString( alias + "id_tap"));
		familiar.setId_gen(rs.getString( alias + "id_gen"));
		familiar.setId_eci(rs.getInt( alias + "id_eci"));
		familiar.setId_gin((rs.getInt( alias + "id_gin")==0)?null:rs.getInt( alias + "id_gin"));
		familiar.setId_rel((rs.getInt( alias + "id_rel")==0)?null:rs.getInt( alias + "id_rel"));
		familiar.setId_dist((rs.getInt( alias + "id_dist")==0)?null:rs.getInt( alias + "id_dist"));
		familiar.setId_pais((rs.getInt( alias + "id_pais")==0)?null:rs.getInt( alias + "id_pais"));
		familiar.setId_ocu((rs.getInt( alias + "id_ocu")==0)?null:rs.getInt( alias + "id_ocu"));
		familiar.setNro_doc(rs.getString( alias + "nro_doc"));
		familiar.setFec_emi_dni(rs.getDate(alias + "fec_emi_dni"));
		familiar.setNom(rs.getString( alias + "nom"));
		familiar.setApe_pat(rs.getString( alias + "ape_pat"));
		familiar.setApe_mat(rs.getString( alias + "ape_mat"));
		familiar.setHue(rs.getString( alias + "hue"));
		familiar.setFec_nac(rs.getDate( alias + "fec_nac"));
		familiar.setViv(rs.getString( alias + "viv"));
		familiar.setViv_alu(rs.getString( alias + "viv_alu"));
		familiar.setDir(rs.getString( alias + "dir"));
		familiar.setTlf(rs.getString( alias + "tlf"));
		familiar.setCorr(rs.getString( alias + "corr"));
		familiar.setCel(rs.getString( alias + "cel"));
		familiar.setCel_val(rs.getString( alias + "cel_val"));
		familiar.setPass(rs.getString( alias + "pass"));
		familiar.setProf(rs.getString( alias + "prof"));
		familiar.setOcu(rs.getString( alias + "ocu"));
		familiar.setCto_tra(rs.getString( alias + "cto_tra"));
		familiar.setEst(rs.getString( alias + "est"));
		familiar.setHuella(rs.getBytes(alias + "huella"));
		familiar.setFoto(rs.getBytes(alias + "foto"));
		familiar.setIni(rs.getString(alias + "ini"));
		familiar.setCorr_val(rs.getString(alias + "corr_val"));

		return familiar;

	}
	
}
