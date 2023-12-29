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
import com.tesla.colegio.model.Alumno;

import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.Idioma;
import com.tesla.colegio.model.EstCivil;
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Salud;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.MatriculaDoc;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementación de la interface AlumnoDAO.
 * @author MV
 *
 */
public class AlumnoDAOImpl{
	
	final static Logger logger = Logger.getLogger(AlumnoDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Alumno alumno) {
		if (alumno.getId() !=null ) {
			// update
			String sql = "UPDATE alu_alumno "
						//+ "SET id_tdc=?, "
						+ "SET id_per=?, "
						+ "id_idio1=?, "
						+ "id_idio2=?, "
						/*+ "id_eci=?, "
						+ "id_tap=?, "
						+ "id_gen=?, "*/
						+ "id_anio_act=?, "
						+ "cod=?, "
						/*+ "nro_doc=?, "
						+ "nom=?, "
						+ "ape_pat=?, "
						+ "ape_mat=?, "
						+ "fec_nac=?, "
						+ "id_pais_nac=?, "
						+ "id_dist_nac=?, "
						+ "id_nac=?,"
						+ "id_dist_viv=?, "*/
						+ "num_hij=?, "
						/*+ "direccion=?, "
						+ "ref=?, "
						+ "telf=?, "
						+ "celular=?, "
						+ "corr=?, "*/
						+ "pass_educando=?, "
						+ "pass_google=?, "
						+ "email_inst=?, "
						//+ "foto=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						//alumno.getId_tdc(),
						alumno.getId_per(),
						alumno.getId_idio1(),
						alumno.getId_idio2(),
						/*alumno.getId_eci(),
						alumno.getId_tap(),
						alumno.getId_gen(),*/
						alumno.getId_anio_act(),
						alumno.getCod(),
						/*alumno.getNro_doc(),
						alumno.getNom()==null ? alumno.getNom() : alumno.getNom().toUpperCase(), //== null ? familiar.getCorr() : familiar.getCorr().toUpperCase(),
						alumno.getApe_pat()== null ? alumno.getApe_pat() : alumno.getApe_pat().toUpperCase(),
						alumno.getApe_mat()== null ? alumno.getApe_mat() : alumno.getApe_mat().toUpperCase(),
						alumno.getFec_nac(),
						alumno.getId_pais_nac(),
						alumno.getId_dist_nac(),
						alumno.getId_nac(),
						alumno.getId_dist_viv(),*/
						alumno.getNum_hij(),
						/*alumno.getDireccion() == null ? alumno.getDireccion() : alumno.getDireccion().toUpperCase(),
						alumno.getRef() == null ? alumno.getDireccion(): alumno.getDireccion().toUpperCase(),
						alumno.getTelf(),
						alumno.getCelular(),
						alumno.getCorr() == null ? alumno.getCorr() : alumno.getCorr().toUpperCase(),*/
						alumno.getPass_educando(),
						alumno.getPass_google(),
						alumno.getEmail_inst(),
						//alumno.getFoto(),
						alumno.getEst(),
						alumno.getUsr_act(),
						new java.util.Date(),
						alumno.getId()); 
			return alumno.getId();

		} else {
			// insert
			String sql = "insert into alu_alumno ("
						+ "id_per, "
						//+ "id_tdc, "
						+ "id_idio1, "
						+ "id_idio2, "
						/*+ "id_eci, "
						+ "id_tap, "
						+ "id_gen, "*/
						+ "id_anio_act, "
						+ "cod, "
						/*+ "nro_doc, "
						+ "nom, "
						+ "ape_pat, "
						+ "ape_mat, "
						+ "fec_nac, "
						+ "id_pais_nac, "
						+ "id_dist_nac, "
						+ "id_nac, "
						+ "id_dist_viv, "*/
						+ "num_hij, "
						/*+ "direccion, "
						+ "ref, "
						+ "telf, "
						+ "celular, "
						+ "corr, "*/
						+ "pass_educando, "
						+ "pass_google, "
						+ "email_inst, "
						//+ "foto, "
						+ "est, usr_ins, fec_ins) "
						//+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
				//logger.info(sql);

				jdbcTemplate.update(sql, 
				//alumno.getId_tdc(),
				alumno.getId_per(),
				alumno.getId_idio1(),
				alumno.getId_idio2(),
				/*alumno.getId_eci(),
				alumno.getId_tap(),
				alumno.getId_gen(),*/
				alumno.getId_anio_act(),
				alumno.getCod(),
				/*alumno.getNro_doc(),
				alumno.getNom() == null ? alumno.getNom() : alumno.getNom().toUpperCase(),
				alumno.getApe_pat() == null ? alumno.getApe_pat() : alumno.getApe_pat().toUpperCase(),
				alumno.getApe_mat() == null ? alumno.getApe_mat() : alumno.getApe_mat().toUpperCase(),
				alumno.getFec_nac(),
				alumno.getId_pais_nac(),
				alumno.getId_dist_nac(),
				alumno.getId_nac(),
				alumno.getId_dist_viv(),*/
				alumno.getNum_hij(),
				/*alumno.getDireccion() == null ? alumno.getDireccion() : alumno.getDireccion().toUpperCase(),
				alumno.getRef() == null ? alumno.getRef() : alumno.getRef().toUpperCase(),
				alumno.getTelf(),
				alumno.getCelular(),
				alumno.getCorr() == null ? alumno.getCorr() : alumno.getCorr().toUpperCase(),*/
				alumno.getPass_educando(),
				alumno.getPass_google(),
				alumno.getEmail_inst(),
				//alumno.getFoto(),
				alumno.getEst(),
				alumno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_alumno where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Alumno> list() {
		String sql = "select * from alu_alumno";
		
		//logger.info(sql);
		
		List<Alumno> listAlumno = jdbcTemplate.query(sql, new RowMapper<Alumno>() {

			
			public Alumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAlumno;
	}

	
	public Alumno get(int id) {
		String sql = "select * from alu_alumno WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Alumno>() {

			
			public Alumno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Alumno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select alu.id alu_id, alu.id_tdc alu_id_tdc , alu.id_per alu_id_per, alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando ,alu.est alu_est ";
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		if (aTablas.contains("cat_idioma"))
			sql = sql + ", idio.id idio_id  , idio.nom idio_nom  ";
		if (aTablas.contains("cat_idioma"))
			sql = sql + ", idio.id idio_id  , idio.nom idio_nom  ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";	
		sql = sql + " from alu_alumno alu "; 
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = alu.id_tdc ";
		if (aTablas.contains("cat_idioma"))
			sql = sql + " left join cat_idioma idio on idio.id = alu.id_idio1 ";
		if (aTablas.contains("cat_idioma"))
			sql = sql + " left join cat_idioma idio on idio.id = alu.id_idio2 ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + " left join cat_est_civil eci on eci.id = alu.id_eci ";
		sql = sql + " where alu.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Alumno>() {
		
			
			public Alumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Alumno alumno= rsToEntity(rs,"alu_");
					if (aTablas.contains("cat_tipo_documento")){
						TipoDocumento tipoDocumento = new TipoDocumento();  
							tipoDocumento.setId(rs.getInt("tdc_id")) ;  
							tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
							alumno.setTipoDocumento(tipoDocumento);
					}
					if (aTablas.contains("cat_idioma")){
						Idioma idioma = new Idioma();  
							idioma.setId(rs.getInt("idio_id")) ;  
							idioma.setNom(rs.getString("idio_nom")) ;  
							alumno.setIdioma1(idioma);
					}
					if (aTablas.contains("cat_idioma")){
						Idioma idioma = new Idioma();  
							idioma.setId(rs.getInt("idio_id")) ;  
							idioma.setNom(rs.getString("idio_nom")) ;  
							alumno.setIdioma2(idioma);
					}
					if (aTablas.contains("cat_est_civil")){
						EstCivil estCivil = new EstCivil();  
							estCivil.setId(rs.getInt("eci_id")) ;  
							estCivil.setNom(rs.getString("eci_nom")) ;  
							alumno.setEstCivil(estCivil);
					}
							return alumno;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Alumno getByParams(Param param) {

		String sql = "select * from alu_alumno " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Alumno>() {
			
			public Alumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Alumno> listByParams(Param param, String[] order) {

		String sql = "select * from alu_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Alumno>() {

			
			public Alumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Alumno> listFullByParams(Alumno alumno, String[] order) {
	
		return listFullByParams(Param.toParam("alu",alumno), order);
	
	}	
	
	
	public List<Alumno> listFullByParams(Param param, String[] order) {

		String sql = "select alu.id alu_id, agfa.id_gpf agfa_id_gpf, alu.id_per alu_id_per, alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.pass_google alu_pass_google, alu.usuario alu_usuario, alu.est alu_est, alu.id_pais_nac alu_id_pais_nac, alu.id_dist_nac alu_id_dist_nac, alu.id_nac alu_id_nac, alu.id_dist_viv alu_id_dist_viv, alu.ref, alu.corr alu_corr, alu.id_classRoom alu_id_classRoom, alu.ref alu_ref ";
		sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		sql = sql + ", idio1.id idio1_id  , idio1.nom idio1_nom  ";
		sql = sql + ", idio2.id idio2_id  , idio2.nom idio2_nom  ";
		sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		sql = sql + " from alu_alumno alu";
		sql = sql + " left join cat_tipo_documento tdc on tdc.id = alu.id_tdc ";
		sql = sql + " left join cat_idioma idio1 on idio1.id = alu.id_idio1 ";
		sql = sql + " left join cat_idioma idio2 on idio2.id = alu.id_idio2 ";
		sql = sql + " left join cat_est_civil eci on eci.id = alu.id_eci ";
		sql = sql + " left join col_persona per on alu.id_per=per.id";
		sql = sql + " inner join alu_gru_fam_alumno agfa on alu.id=agfa.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Alumno>() {

			
			public Alumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				Alumno alumno= rsToEntity(rs,"alu_");
				TipoDocumento tipoDocumento = new TipoDocumento();  
				tipoDocumento.setId(rs.getInt("tdc_id")) ;  
				tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
				alumno.setTipoDocumento(tipoDocumento);
				GruFamAlumno gruFamAlumno = new GruFamAlumno();
				gruFamAlumno.setId_gpf(rs.getInt("agfa_id_gpf"));
				alumno.setGruFamAlumno(gruFamAlumno);
				/*Idioma idioma = new Idioma();  
				idioma.setId(rs.getInt("idio_id")) ;  
				idioma.setNom(rs.getString("idio_nom")) ;  
				alumno.setIdioma1(idioma);
				idioma = new Idioma();  
				idioma.setId(rs.getInt("idio_id")) ;  
				idioma.setNom(rs.getString("idio_nom")) ;  
				alumno.setIdioma2(idioma);*/
				EstCivil estCivil = new EstCivil();  
				estCivil.setId(rs.getInt("eci_id")) ;  
				estCivil.setNom(rs.getString("eci_nom")) ;  
				alumno.setEstCivil(estCivil);
				return alumno;
			}

		});

	}	


	public List<GruFamAlumno> getListGruFamAlumno(Param param, String[] order) {
		String sql = "select * from alu_gru_fam_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GruFamAlumno>() {

			
			public GruFamAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamAlumno gru_fam_alumno = new GruFamAlumno();

				gru_fam_alumno.setId(rs.getInt("id"));
				gru_fam_alumno.setId_gpf(rs.getInt("id_gpf"));
				gru_fam_alumno.setId_alu(rs.getInt("id_alu"));
				gru_fam_alumno.setEst(rs.getString("est"));
												
				return gru_fam_alumno;
			}

		});	
	}
	public List<Salud> getListSalud(Param param, String[] order) {
		String sql = "select * from alu_salud " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Salud>() {

			
			public Salud mapRow(ResultSet rs, int rowNum) throws SQLException {
				Salud salud = new Salud();

				salud.setId(rs.getInt("id"));
				salud.setId_alu(rs.getInt("id_alu"));
				salud.setPeso_nac(rs.getString("peso_nac"));
				salud.setTalla_nac(rs.getString("talla_nac"));
				salud.setNu_edad_cabe(rs.getString("nu_edad_cabe"));
				salud.setNu_edad_paro(rs.getString("nu_edad_paro"));
				salud.setNu_edad_cami(rs.getString("nu_edad_cami"));
				salud.setEst(rs.getString("est"));
												
				return salud;
			}

		});	
	}
	public List<MatrVacante> getListMatrVacante(Param param, String[] order) {
		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacante matr_vacante = new MatrVacante();

				matr_vacante.setId(rs.getInt("id"));
				matr_vacante.setId_alu(rs.getInt("id_alu"));
				//matr_vacante.setId_exa(rs.getInt("id_exa"));
				matr_vacante.setId_gra(rs.getInt("id_gra"));
				matr_vacante.setId_col(rs.getInt("id_col"));
				matr_vacante.setNum_rec(rs.getString("num_rec"));
				matr_vacante.setNum_cont(rs.getString("num_cont"));
				matr_vacante.setEst(rs.getString("est"));
												
				return matr_vacante;
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
	public List<MatriculaDoc> getListMatriculaDoc(Param param, String[] order) {
		String sql = "select * from mat_matricula_doc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<MatriculaDoc>() {

			
			public MatriculaDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatriculaDoc matricula_doc = new MatriculaDoc();

				matricula_doc.setId(rs.getInt("id"));
				matricula_doc.setId_ado(rs.getInt("id_ado"));
				matricula_doc.setId_alu(rs.getInt("id_alu"));
				matricula_doc.setEst(rs.getString("est"));
												
				return matricula_doc;
			}

		});	
	}
	/*public List<Descuento> getListDescuento(Param param, String[] order) {
		String sql = "select * from pag_descuento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Descuento>() {

			
			public Descuento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Descuento descuento = new Descuento();

				descuento.setId(rs.getInt("id"));
				descuento.setId_alu(rs.getInt("id_alu"));
				descuento.setDes(rs.getString("des"));
				descuento.setMot(rs.getString("mot"));
				descuento.setEst(rs.getString("est"));
												
				return descuento;
			}

		});	
	}*/


	// funciones privadas utilitarias para Alumno

	private Alumno rsToEntity(ResultSet rs,String alias) throws SQLException {
		Alumno alumno = new Alumno();

		alumno.setId(rs.getInt( alias + "id"));
		alumno.setId_tdc(rs.getInt( alias + "id_tdc"));
		alumno.setId_per(rs.getInt( alias + "id_per"));
		alumno.setId_idio1(rs.getInt( alias + "id_idio1"));
		alumno.setId_idio2(rs.getInt( alias + "id_idio2"));
		alumno.setId_eci(rs.getInt( alias + "id_eci"));
		alumno.setId_tap(rs.getString( alias + "id_tap"));
		alumno.setId_gen(rs.getString( alias + "id_gen"));
		alumno.setCod(rs.getString( alias + "cod"));
		alumno.setNro_doc(rs.getString( alias + "nro_doc"));
		alumno.setNom(rs.getString( alias + "nom"));
		alumno.setApe_pat(rs.getString( alias + "ape_pat"));
		alumno.setApe_mat(rs.getString( alias + "ape_mat"));
		alumno.setFec_nac(rs.getDate( alias + "fec_nac"));
		alumno.setId_pais_nac(rs.getInt(alias + "id_pais_nac"));
		alumno.setId_dist_nac(rs.getInt(alias + "id_dist_nac"));
		alumno.setId_classRoom(rs.getString(alias + "id_classRoom"));
		alumno.setId_nac(rs.getInt(alias + "id_nac"));
		alumno.setId_dist_viv(rs.getInt(alias + "id_dist_viv"));
		alumno.setNum_hij(rs.getInt( alias + "num_hij"));
		alumno.setDireccion(rs.getString( alias + "direccion"));
		alumno.setRef(rs.getString(alias + "ref"));
		alumno.setTelf(rs.getString( alias + "telf"));
		alumno.setCelular(rs.getString( alias + "celular"));
		alumno.setCorr(rs.getString(alias + "corr"));
		alumno.setUsuario(rs.getString(alias + "usuario"));
		alumno.setPass_educando(rs.getString( alias + "pass_educando"));
		alumno.setPass_google(rs.getString(alias + "pass_google"));
		//alumno.setFoto(rs.getString( alias + "foto"));
		alumno.setEst(rs.getString( alias + "est"));
								
		return alumno;

	}

	protected JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}
}
