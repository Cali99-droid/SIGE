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
import com.tesla.colegio.model.Salud;

import com.tesla.colegio.model.Alumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface SaludDAO.
 * @author MV
 *
 */
public class SaludDAOImpl{
	final static Logger logger = Logger.getLogger(SaludDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Salud salud) {
		if (salud.getId() > 0) {
			// update
			String sql = "UPDATE alu_salud "
						+ "SET id_alu=?, "
						+ "peso_nac=?, "
						+ "talla_nac=?, "
						+ "nu_edad_cabe=?, "
						+ "nu_edad_paro=?, "
						+ "nu_edad_cami=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						salud.getId_alu(),
						salud.getPeso_nac(),
						salud.getTalla_nac(),
						salud.getNu_edad_cabe(),
						salud.getNu_edad_paro(),
						salud.getNu_edad_cami(),
						salud.getEst(),
						salud.getUsr_act(),
						new java.util.Date(),
						salud.getId()); 

		} else {
			// insert
			String sql = "insert into alu_salud ("
						+ "id_alu, "
						+ "peso_nac, "
						+ "talla_nac, "
						+ "nu_edad_cabe, "
						+ "nu_edad_paro, "
						+ "nu_edad_cami, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				salud.getId_alu(),
				salud.getPeso_nac(),
				salud.getTalla_nac(),
				salud.getNu_edad_cabe(),
				salud.getNu_edad_paro(),
				salud.getNu_edad_cami(),
				salud.getEst(),
				salud.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_salud where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Salud> list() {
		String sql = "select * from alu_salud";
		
		//logger.info(sql);
		
		List<Salud> listSalud = jdbcTemplate.query(sql, new RowMapper<Salud>() {

			
			public Salud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listSalud;
	}

	
	public Salud get(int id) {
		String sql = "select * from alu_salud WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Salud>() {

			
			public Salud extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Salud getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select sal.id sal_id, sal.id_alu sal_id_alu , sal.peso_nac sal_peso_nac , sal.talla_nac sal_talla_nac , sal.nu_edad_cabe sal_nu_edad_cabe , sal.nu_edad_paro sal_nu_edad_paro , sal.nu_edad_cami sal_nu_edad_cami  ,sal.est sal_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from alu_salud sal "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = sal.id_alu ";
		sql = sql + " where sal.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Salud>() {
		
			
			public Salud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Salud salud= rsToEntity(rs,"sal_");
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
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							salud.setAlumno(alumno);
					}
							return salud;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Salud getByParams(Param param) {

		String sql = "select * from alu_salud " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Salud>() {
			
			public Salud extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Salud> listByParams(Param param, String[] order) {

		String sql = "select * from alu_salud " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Salud>() {

			
			public Salud mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Salud> listFullByParams(Salud salud, String[] order) {
	
		return listFullByParams(Param.toParam("sal",salud), order);
	
	}	
	
	
	public List<Salud> listFullByParams(Param param, String[] order) {

		String sql = "select sal.id sal_id, sal.id_alu sal_id_alu , sal.peso_nac sal_peso_nac , sal.talla_nac sal_talla_nac , sal.nu_edad_cabe sal_nu_edad_cabe , sal.nu_edad_paro sal_nu_edad_paro , sal.nu_edad_cami sal_nu_edad_cami  ,sal.est sal_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from alu_salud sal";
		sql = sql + " left join alu_alumno alu on alu.id = sal.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Salud>() {

			
			public Salud mapRow(ResultSet rs, int rowNum) throws SQLException {
				Salud salud= rsToEntity(rs,"sal_");
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
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				salud.setAlumno(alumno);
				return salud;
			}

		});

	}	




	// funciones privadas utilitarias para Salud

	private Salud rsToEntity(ResultSet rs,String alias) throws SQLException {
		Salud salud = new Salud();

		salud.setId(rs.getInt( alias + "id"));
		salud.setId_alu(rs.getInt( alias + "id_alu"));
		salud.setPeso_nac(rs.getString( alias + "peso_nac"));
		salud.setTalla_nac(rs.getString( alias + "talla_nac"));
		salud.setNu_edad_cabe(rs.getString( alias + "nu_edad_cabe"));
		salud.setNu_edad_paro(rs.getString( alias + "nu_edad_paro"));
		salud.setNu_edad_cami(rs.getString( alias + "nu_edad_cami"));
		salud.setEst(rs.getString( alias + "est"));
								
		return salud;

	}
	
}
