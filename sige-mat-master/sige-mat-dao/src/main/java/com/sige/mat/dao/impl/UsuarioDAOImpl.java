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
import com.tesla.colegio.model.Usuario;

import com.tesla.colegio.model.Perfil;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.UsuarioRol;
import com.tesla.colegio.model.UsuarioSucursal;

import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UsuarioDAO.
 * @author MV
 *
 */
public class UsuarioDAOImpl{
	final static Logger logger = Logger.getLogger(UsuarioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	 
	public int saveOrUpdate(Usuario usuario) {
		if (usuario.getId() != null) {
			// update
			String sql = "UPDATE seg_usuario "
						+ "SET id_per=?, "
						+ "id_tra=?, "
						+ "login=?, "
						+ "password=?, "
						+ "id_suc=?, "
						+ "ini=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						usuario.getId_per(),
						usuario.getId_tra(),
						usuario.getLogin(),
						usuario.getPassword(),
						usuario.getId_suc(),
						usuario.getIni(),
						usuario.getEst(),
						usuario.getUsr_act(),
						new java.util.Date(),
						usuario.getId()); 
			
			
			return usuario.getId();

		} else {
			// insert
			String sql = "insert into seg_usuario ("
						+ "id_per, "
						+ "id_tra, "
						+ "tipo, "
						+ "login, "
						+ "password, "
						+ "id_suc, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				usuario.getId_per(),
				usuario.getId_tra(),
				usuario.getTipo(),
				usuario.getLogin(),
				usuario.getPassword(),
				usuario.getId_suc(),
				usuario.getEst(),
				usuario.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from seg_usuario where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Usuario> list() {
		String sql = "select * from seg_usuario";
		
		//logger.info(sql);
		
		List<Usuario> listUsuario = jdbcTemplate.query(sql, new RowMapper<Usuario>() {

			@Override
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUsuario;
	}

	public Usuario get(int id) {
		String sql = "select * from seg_usuario WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Usuario>() {

			@Override
			public Usuario extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Usuario getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select usr.id usr_id, usr.ini usr_ini,usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc  ,usr.est usr_est ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
	
		sql = sql + " from seg_usuario usr "; 
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = usr.id_per ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = usr.id_tra ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = usr.id_suc ";
		sql = sql + " where usr.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Usuario>() {
		
			@Override
			public Usuario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Usuario usuario= rsToEntity(rs,"usr_");
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							usuario.setPerfil(perfil);
					}
					if (aTablas.contains("ges_trabajador")){
						Trabajador trabajador = new Trabajador();  
							trabajador.setId(rs.getInt("tra_id")) ;  
							trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
							trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
							trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
							trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
							trabajador.setNom(rs.getString("tra_nom")) ;  
							trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
							trabajador.setGenero(rs.getString("tra_genero")) ;  
							trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
							trabajador.setDir(rs.getString("tra_dir")) ;  
							trabajador.setTel(rs.getString("tra_tel")) ;  
							trabajador.setCel(rs.getString("tra_cel")) ;  
							trabajador.setCorr(rs.getString("tra_corr")) ;  
							trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
							trabajador.setCarrera(rs.getString("tra_carrera")) ;  
							//trabajador.setFot(rs.getString("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							usuario.setTrabajador(trabajador);
					}
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							usuario.setSucursal(sucursal);
					}
							return usuario;
				}
				
				return null;
			}
			
		});


	}		
	
	public Usuario getByParams(Param param) {

		String sql = "select * from seg_usuario " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Usuario>() {
			@Override
			public Usuario extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Usuario> listByParams(Param param, String[] order) {

		String sql = "select * from seg_usuario " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Usuario>() {

			@Override
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Usuario> listFullByParams(Usuario usuario, String[] order) {
	
		return listFullByParams(Param.toParam("usr",usuario), order);
	
	}	
	
	public List<Usuario> listFullByParams(Param param, String[] order) {

		String sql = "select usr.id usr_id, usr.ini usr_ini,usr.id_per usr_id_per , usr.id_tra usr_id_tra , usr.login usr_login , usr.password usr_password , usr.id_suc usr_id_suc  ,usr.est usr_est ";
		sql = sql + "\n , per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + "\n , tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + "\n , pers.id pers_id  , pers.id_tdc pers_id_tdc , pers.nro_doc pers_nro_doc , pers.ape_pat pers_ape_pat , pers.ape_mat pers_ape_mat , pers.nom pers_nom , pers.fec_nac pers_fec_nac , pers.id_gen pers_id_gen , pers.id_eci pers_id_eci , pers.dir pers_dir , pers.tlf pers_tlf , pers.cel pers_cel , pers.corr pers_corr  ";
		sql = sql + "\n , suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + "\n from seg_usuario usr";
		sql = sql + "\n inner join seg_perfil per on per.id = usr.id_per ";
		sql = sql + "\n left join ges_trabajador tra on tra.id = usr.id_tra";
		sql = sql + "\n left join col_persona pers on tra.id_per = pers.id";
		//sql = sql + "\n  AND (usr.tipo='E' OR EXISTS(SELECT con.id FROM rhh_contrato_trabajador con WHERE con.id_tra= usr.id_tra AND usr.id_per=per.id AND CURRENT_DATE() BETWEEN con.fec_ini AND  ADDDATE(con.fec_fin, INTERVAL per.`dias_adi_login` DAY)))";
		sql = sql + "\n LEFT join ges_sucursal suc on suc.id = usr.id_suc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Usuario>() {

			@Override
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				Usuario usuario= rsToEntity(rs,"usr_");
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;  
				perfil.setNom(rs.getString("per_nom")) ;  
				perfil.setDes(rs.getString("per_des")) ;  
				usuario.setPerfil(perfil);
				Trabajador trabajador = new Trabajador();  
				trabajador.setId(rs.getInt("tra_id")) ;  
				trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
				trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
				trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
				trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
				trabajador.setNom(rs.getString("tra_nom")) ;  
				trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
				trabajador.setGenero(rs.getString("tra_genero")) ;  
				trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
				trabajador.setDir(rs.getString("tra_dir")) ;  
				trabajador.setTel(rs.getString("tra_tel")) ;  
				trabajador.setCel(rs.getString("tra_cel")) ;  
				trabajador.setCorr(rs.getString("tra_corr")) ;  
				trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
				trabajador.setCarrera(rs.getString("tra_carrera")) ;  
				//trabajador.setFot(rs.getString("tra_fot")) ;  
				trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
				trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
				usuario.setTrabajador(trabajador);
				Persona persona = new Persona();
				persona.setId(rs.getInt("pers_id"));
				persona.setId_tdc(rs.getString("pers_id_tdc")) ;  
				persona.setNro_doc(rs.getString("pers_nro_doc")) ;  
				persona.setApe_pat(rs.getString("pers_ape_pat")) ;  
				persona.setApe_mat(rs.getString("pers_ape_mat")) ;  
				persona.setNom(rs.getString("pers_nom")) ;  
				persona.setFec_nac(rs.getDate("pers_fec_nac")) ;  
				persona.setId_gen(rs.getString("pers_id_gen")) ;  
				persona.setId_eci(rs.getInt("pers_id_eci")) ;  
				persona.setDir(rs.getString("pers_dir")) ;  
				persona.setTlf(rs.getString("pers_tlf")) ;  
				persona.setCel(rs.getString("pers_cel")) ;  
				persona.setCorr(rs.getString("pers_corr")) ;
				usuario.setPersona(persona);
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				usuario.setSucursal(sucursal);
				return usuario;
			}

		});

	}	


	public List<UsuarioRol> getListUsuarioRol(Param param, String[] order) {
		String sql = "select * from seg_usuario_rol " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UsuarioRol>() {

			@Override
			public UsuarioRol mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioRol usuario_rol = new UsuarioRol();

				usuario_rol.setId(rs.getInt("id"));
				usuario_rol.setId_usr(rs.getInt("id_usr"));
				usuario_rol.setId_rol(rs.getInt("id_rol"));
				usuario_rol.setEst(rs.getString("est"));
												
				return usuario_rol;
			}

		});	
	}
	public List<UsuarioSucursal> getListUsuarioSucursal(Param param, String[] order) {
		String sql = "select * from seg_usuario_sucursal " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<UsuarioSucursal>() {

			@Override
			public UsuarioSucursal mapRow(ResultSet rs, int rowNum) throws SQLException {
				UsuarioSucursal usuario_sucursal = new UsuarioSucursal();

				usuario_sucursal.setId(rs.getInt("id"));
				usuario_sucursal.setId_usr(rs.getInt("id_usr"));
				usuario_sucursal.setId_suc(rs.getInt("id_suc"));
				usuario_sucursal.setEst(rs.getString("est"));
												
				return usuario_sucursal;
			}

		});	
	}
	public List<Trabajador> getListTrabajador(Param param, String[] order) {
		String sql = "select * from aeedu_asistencia.ges_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Trabajador>() {

			@Override
			public Trabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Trabajador trabajador = new Trabajador();

				trabajador.setId(rs.getInt("id"));
				trabajador.setId_tdc(rs.getInt("id_tdc"));
				trabajador.setNro_doc(rs.getString("nro_doc"));
				trabajador.setApe_pat(rs.getString("ape_pat"));
				trabajador.setApe_mat(rs.getString("ape_mat"));
				trabajador.setNom(rs.getString("nom"));
				trabajador.setFec_nac(rs.getDate("fec_nac"));
				trabajador.setGenero(rs.getString("genero"));
				trabajador.setId_eci(rs.getInt("id_eci"));
				trabajador.setDir(rs.getString("dir"));
				trabajador.setTel(rs.getString("tel"));
				trabajador.setCel(rs.getString("cel"));
				trabajador.setCorr(rs.getString("corr"));
				trabajador.setId_gin(rs.getInt("id_gin"));
				trabajador.setCarrera(rs.getString("carrera"));
				//trabajador.setFot(rs.getString("fot"));
				trabajador.setNum_hij(rs.getInt("num_hij"));
				trabajador.setId_usr(rs.getInt("id_usr"));
				trabajador.setEst(rs.getString("est"));
												
				return trabajador;
			}

		});	
	}


	// funciones privadas utilitarias para Usuario

	private Usuario rsToEntity(ResultSet rs,String alias) throws SQLException {
		Usuario usuario = new Usuario();

		usuario.setId(rs.getInt( alias + "id"));
		usuario.setId_per(rs.getInt( alias + "id_per"));
		usuario.setId_tra(rs.getInt( alias + "id_tra"));
		usuario.setLogin(rs.getString( alias + "login"));
		usuario.setPassword(rs.getString( alias + "password"));
		usuario.setId_suc(rs.getInt( alias + "id_suc"));
		usuario.setEst(rs.getString( alias + "est"));
		usuario.setIni(rs.getString( alias + "ini"));
	
		return usuario;

	}
	
}
