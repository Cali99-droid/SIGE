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
import com.tesla.colegio.model.Trabajador;

import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.EstCivil;
import com.tesla.colegio.model.GradoInstruccion;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.model.Curso;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface TrabajadorDAO.
 * @author MV
 *
 */
public class TrabajadorDAOImpl{
	final static Logger logger = Logger.getLogger(TrabajadorDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Trabajador trabajador) {
		if (trabajador.getId()!=null) {
			// update
			String sql = "UPDATE ges_trabajador "
						+ "SET id_tdc=?, "
						+ "cod=?, "
						+ "id_per=?, "
						+ "email_inst=?, "
						+ "nro_doc=?, "
						+ "ape_pat=?, "
						+ "ape_mat=?, "
						+ "nom=?, "
						+ "fec_nac=?, "
						+ "genero=?, "
						+ "id_eci=?, "
						+ "dir=?, "
						+ "tel=?, "
						+ "cel=?, "
						+ "corr=?, "
					//	+ "corr_val=?, "
						+ "id_gin=?, "
						+ "carrera=?, "
						+ "fot=?, "
						+ "hijos=?, "
						+ "num_hij=?, "
						+ "id_usr=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						trabajador.getId_tdc(),
						trabajador.getCod(),
						trabajador.getId_per(),
						trabajador.getEmail_inst(),
						trabajador.getNro_doc(),
						trabajador.getApe_pat(),
						trabajador.getApe_mat(),
						trabajador.getNom(),
						trabajador.getFec_nac(),
						trabajador.getGenero(),
						trabajador.getId_eci(),
						trabajador.getDir(),
						trabajador.getTel(),
						trabajador.getCel(),
						trabajador.getCorr(),
						//trabajador.getCorr_val(),
						trabajador.getId_gin(),
						trabajador.getCarrera()==null ? trabajador.getCarrera(): trabajador.getCarrera().toUpperCase().trim(),
						trabajador.getFot(),
						trabajador.getHijos(),
						trabajador.getNum_hij(),
						trabajador.getId_usr(),
						trabajador.getEst(),
						trabajador.getUsr_act(),
						new java.util.Date(),
						trabajador.getId()); 

		} else {
			// insert
			String sql = "insert into ges_trabajador ("
						+ "id_tdc, "
						+ "cod, "
						+ "id_per, "
						+ "email_inst, "
						+ "nro_doc, "
						+ "ape_pat, "
						+ "ape_mat, "
						+ "nom, "
						+ "fec_nac, "
						+ "genero, "
						+ "id_eci, "
						+ "dir, "
						+ "tel, "
						+ "cel, "
						+ "corr, "
						//+ "corr_val, "
						+ "id_gin, "
						+ "carrera, "
						+ "fot, "
						+ "hijos, "
						+ "num_hij, "
						+ "id_usr, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				trabajador.getId_tdc(),
				trabajador.getCod(),
				trabajador.getId_per(),
				trabajador.getEmail_inst(),
				trabajador.getNro_doc(),
				trabajador.getApe_pat(),
				trabajador.getApe_mat(),
				trabajador.getNom(),
				trabajador.getFec_nac(),
				trabajador.getGenero(),
				trabajador.getId_eci(),
				trabajador.getDir(),
				trabajador.getTel(),
				trabajador.getCel(),
				trabajador.getCorr(),
				//trabajador.getCorr_val(),
				trabajador.getId_gin(),
				trabajador.getCarrera()==null ? trabajador.getCarrera(): trabajador.getCarrera().toUpperCase().trim(),
				trabajador.getFot(),
				trabajador.getHijos(),
				trabajador.getNum_hij(),
				trabajador.getId_usr(),
				trabajador.getEst(),
				trabajador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from ges_trabajador where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Trabajador> list() {
		String sql = "select * from aeedu_asistencia.ges_trabajador";
		
		//logger.info(sql);
		
		List<Trabajador> listTrabajador = jdbcTemplate.query(sql, new RowMapper<Trabajador>() {

			
			public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTrabajador;
	}

	
	public Trabajador get(int id) {
		String sql = "select * from ges_trabajador WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Trabajador>() {

			
			public Trabajador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Trabajador getFull(int id, String tablas[]) {
		final  List<String> aTablas = Arrays.asList(tablas);
		String sql = "select tra.id tra_id, tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.corr_val tra_corr_val , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ,tra.est tra_est ";
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
	
		sql = sql + " from aeedu_asistencia.ges_trabajador tra "; 
		if (aTablas.contains("cat_tipo_documento"))
			sql = sql + " left join cat_tipo_documento tdc on tdc.id = tra.id_tdc ";
		if (aTablas.contains("cat_est_civil"))
			sql = sql + " left join cat_est_civil eci on eci.id = tra.id_eci ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + " left join cat_grado_instruccion gin on gin.id = tra.id_gin ";
		if (aTablas.contains("seg_usuario"))
			sql = sql + " left join seg_usuario usr on usr.id = tra.id_usr ";
		sql = sql + " where tra.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Trabajador>() {
		
			
			public Trabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Trabajador trabajador= rsToEntity(rs,"tra_");
					if (aTablas.contains("cat_tipo_documento")){
						TipoDocumento tipoDocumento = new TipoDocumento();  
							tipoDocumento.setId(rs.getInt("tdc_id")) ;  
							tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
							trabajador.setTipoDocumento(tipoDocumento);
					}
					if (aTablas.contains("cat_est_civil")){
						EstCivil estCivil = new EstCivil();  
							estCivil.setId(rs.getInt("eci_id")) ;  
							estCivil.setNom(rs.getString("eci_nom")) ;  
							trabajador.setEstCivil(estCivil);
					}
					if (aTablas.contains("cat_grado_instruccion")){
						GradoInstruccion gradoInstruccion = new GradoInstruccion();  
							gradoInstruccion.setId(rs.getInt("gin_id")) ;  
							gradoInstruccion.setNom(rs.getString("gin_nom")) ;  
							trabajador.setGradoInstruccion(gradoInstruccion);
					}
					if (aTablas.contains("seg_usuario")){
						Usuario usuario = new Usuario();  
							usuario.setId(rs.getInt("usr_id")) ;  
							usuario.setId_per(rs.getInt("usr_id_per")) ;  
							usuario.setLogin(rs.getString("usr_login")) ;  
							usuario.setPassword(rs.getString("usr_password")) ;  
							trabajador.setUsuario(usuario);
					}
							return trabajador;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Trabajador getByParams(Param param) {

		String sql = "select * from ges_trabajador " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Trabajador>() {
			
			public Trabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Trabajador> listByParams(Param param, String[] order) {

		String sql = "select * from aeedu_asistencia.ges_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Trabajador>() {

			
			public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Trabajador> listFullByParams(Trabajador trabajador, String[] order) {
	
		return listFullByParams(Param.toParam("tra",trabajador), order);
	
	}	
	
	
	public List<Trabajador> listFullByParams(Param param, String[] order) {

		String sql = "select tra.id tra_id, tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.corr_val tra_corr_val , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ,tra.est tra_est ";
		sql = sql + ", tdc.id tdc_id  , tdc.nom tdc_nom  ";
		sql = sql + ", eci.id eci_id  , eci.nom eci_nom  ";
		sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
		sql = sql + ", usr.id usr_id  , usr.id_per usr_id_per , usr.login usr_login , usr.password usr_password  ";
		sql = sql + " from ges_trabajador tra";
		sql = sql + " left join cat_tipo_documento tdc on tdc.id = tra.id_tdc ";
		sql = sql + " left join cat_est_civil eci on eci.id = tra.id_eci ";
		sql = sql + " left join cat_grado_instruccion gin on gin.id = tra.id_gin ";
		sql = sql + " left join seg_usuario usr on usr.id = tra.id_usr ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Trabajador>() {

			
			public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Trabajador trabajador= rsToEntity(rs,"tra_");
				TipoDocumento tipoDocumento = new TipoDocumento();  
				tipoDocumento.setId(rs.getInt("tdc_id")) ;  
				tipoDocumento.setNom(rs.getString("tdc_nom")) ;  
				trabajador.setTipoDocumento(tipoDocumento);
				EstCivil estCivil = new EstCivil();  
				estCivil.setId(rs.getInt("eci_id")) ;  
				estCivil.setNom(rs.getString("eci_nom")) ;  
				trabajador.setEstCivil(estCivil);
				GradoInstruccion gradoInstruccion = new GradoInstruccion();  
				gradoInstruccion.setId(rs.getInt("gin_id")) ;  
				gradoInstruccion.setNom(rs.getString("gin_nom")) ;  
				trabajador.setGradoInstruccion(gradoInstruccion);
				Usuario usuario = new Usuario();  
				usuario.setId(rs.getInt("usr_id")) ;  
				usuario.setId_per(rs.getInt("usr_id_per")) ;  
				usuario.setLogin(rs.getString("usr_login")) ;  
				usuario.setPassword(rs.getString("usr_password")) ;  
				trabajador.setUsuario(usuario);
				return trabajador;
			}

		});

	}	


	public List<Curso> getListCurso(Param param, String[] order) {
		String sql = "select * from col_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Curso>() {

			
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				Curso curso = new Curso();

				curso.setId(rs.getInt("id"));
				//curso.setId_ar(rs.getString("id_ar"));
				curso.setNom(rs.getString("nom"));
				//curso.setId_nvl(rs.getInt("id_nvl"));
				//curso.setId_au(rs.getInt("id_au"));
				//curso.setId_prof(rs.getInt("id_prof"));
				//curso.setAnio(rs.getString("anio"));
				//curso.setEva(rs.getString("eva"));
				//curso.setCom(rs.getString("com"));
				//curso.setProm(rs.getString("prom"));
				//curso.setSim(rs.getString("sim"));
				curso.setEst(rs.getString("est"));
												
				return curso;
			}

		});	
	}


	// funciones privadas utilitarias para Trabajador

	private Trabajador rsToEntity(ResultSet rs,String alias) throws SQLException {
		Trabajador trabajador = new Trabajador();

		trabajador.setId(rs.getInt( alias + "id"));
		trabajador.setId_tdc(rs.getInt( alias + "id_tdc"));
		trabajador.setId_per(rs.getInt( alias + "id_per"));
		trabajador.setEmail_inst(rs.getString( alias + "email_inst"));
		trabajador.setCod(rs.getString( alias + "cod"));
		trabajador.setNro_doc(rs.getString( alias + "nro_doc"));
		trabajador.setApe_pat(rs.getString( alias + "ape_pat"));
		trabajador.setApe_mat(rs.getString( alias + "ape_mat"));
		trabajador.setNom(rs.getString( alias + "nom"));
		trabajador.setFec_nac(rs.getDate( alias + "fec_nac"));
		trabajador.setGenero(rs.getString( alias + "genero"));
		trabajador.setId_eci(rs.getInt( alias + "id_eci"));
		trabajador.setDir(rs.getString( alias + "dir"));
		trabajador.setTel(rs.getString( alias + "tel"));
		trabajador.setCel(rs.getString( alias + "cel"));
		trabajador.setCorr(rs.getString( alias + "corr"));
		//trabajador.setCorr_val(rs.getString( alias + "corr_val"));
		trabajador.setId_gin(rs.getInt( alias + "id_gin"));
		trabajador.setCarrera(rs.getString( alias + "carrera"));
		trabajador.setFot(rs.getBytes( alias + "fot"));
		trabajador.setHijos(rs.getString( alias + "hijos"));
		trabajador.setNum_hij(rs.getInt( alias + "num_hij"));
		trabajador.setId_usr(rs.getInt( alias + "id_usr"));
		trabajador.setEst(rs.getString( alias + "est"));
								
		return trabajador;

	}
	
}
