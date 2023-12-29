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
import com.tesla.colegio.model.SigeUsuarios;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Alumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SigeUsuariosDAO.
 * @author MV
 *
 */
public class SigeUsuariosDAOImpl{
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
	public int saveOrUpdate(SigeUsuarios sige_usuarios) {
		if (sige_usuarios.getId() != null) {
			// update
			String sql = "UPDATE cvi_sige_usuarios "
						+ "SET id_alu=?, "
						+ "Nombres=?, "
						+ "Apellidos=?, "
						+ "Correo=?, "
						+ "Clave=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						sige_usuarios.getId_alu(),
						sige_usuarios.getNombres(),
						sige_usuarios.getApellidos(),
						sige_usuarios.getCorreo(),
						sige_usuarios.getClave(),
						sige_usuarios.getEst(),
						sige_usuarios.getUsr_act(),
						new java.util.Date(),
						sige_usuarios.getId()); 
			return sige_usuarios.getId();

		} else {
			// insert
			String sql = "insert into cvi_sige_usuarios ("
						+ "id_alu, "
						+ "Nombres, "
						+ "Apellidos, "
						+ "Correo, "
						+ "Clave, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				sige_usuarios.getId_alu(),
				sige_usuarios.getNombres(),
				sige_usuarios.getApellidos(),
				sige_usuarios.getCorreo(),
				sige_usuarios.getClave(),
				sige_usuarios.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_sige_usuarios where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<SigeUsuarios> list() {
		String sql = "select * from cvi_sige_usuarios";
		
		//System.out.println(sql);
		
		List<SigeUsuarios> listSigeUsuarios = jdbcTemplate.query(sql, new RowMapper<SigeUsuarios>() {

			@Override
			public SigeUsuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSigeUsuarios;
	}

	public SigeUsuarios get(int id) {
		String sql = "select * from cvi_sige_usuarios WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SigeUsuarios>() {

			@Override
			public SigeUsuarios extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public SigeUsuarios getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select csu.id csu_id, csu.id_alu csu_id_alu , csu.Nombres csu_Nombres , csu.Apellidos csu_Apellidos , csu.Correo csu_Correo , csu.Clave csu_Clave  ,csu.est csu_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from cvi_sige_usuarios csu "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = csu.id_alu ";
		sql = sql + " where csu.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<SigeUsuarios>() {
		
			@Override
			public SigeUsuarios extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					SigeUsuarios sigeusuarios= rsToEntity(rs,"csu_");
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
							//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							sigeusuarios.setAlumno(alumno);
					}
							return sigeusuarios;
				}
				
				return null;
			}
			
		});


	}		
	
	public SigeUsuarios getByParams(Param param) {

		String sql = "select * from cvi_sige_usuarios " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<SigeUsuarios>() {
			@Override
			public SigeUsuarios extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<SigeUsuarios> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_sige_usuarios " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SigeUsuarios>() {

			@Override
			public SigeUsuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<SigeUsuarios> listFullByParams(SigeUsuarios sigeusuarios, String[] order) {
	
		return listFullByParams(Param.toParam("csu",sigeusuarios), order);
	
	}	
	
	public List<SigeUsuarios> listFullByParams(Param param, String[] order) {

		String sql = "select csu.id csu_id, csu.id_alu csu_id_alu , csu.Nombres csu_Nombres , csu.Apellidos csu_Apellidos , csu.Correo csu_Correo , csu.Clave csu_Clave  ,csu.est csu_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from cvi_sige_usuarios csu";
		sql = sql + " left join alu_alumno alu on alu.id = csu.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<SigeUsuarios>() {

			@Override
			public SigeUsuarios mapRow(ResultSet rs, int rowNum) throws SQLException {
				SigeUsuarios sigeusuarios= rsToEntity(rs,"csu_");
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
				//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				sigeusuarios.setAlumno(alumno);
				return sigeusuarios;
			}

		});

	}	




	// funciones privadas utilitarias para SigeUsuarios

	private SigeUsuarios rsToEntity(ResultSet rs,String alias) throws SQLException {
		SigeUsuarios sige_usuarios = new SigeUsuarios();

		sige_usuarios.setId(rs.getInt( alias + "id"));
		sige_usuarios.setId_alu(rs.getInt( alias + "id_alu"));
		sige_usuarios.setNombres(rs.getString( alias + "Nombres"));
		sige_usuarios.setApellidos(rs.getString( alias + "Apellidos"));
		sige_usuarios.setCorreo(rs.getString( alias + "Correo"));
		sige_usuarios.setClave(rs.getString( alias + "Clave"));
		sige_usuarios.setEst(rs.getString( alias + "est"));
								
		return sige_usuarios;

	}
	
}
