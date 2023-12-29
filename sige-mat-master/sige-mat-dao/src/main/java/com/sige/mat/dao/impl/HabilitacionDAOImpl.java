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
import com.tesla.colegio.model.Habilitacion;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Anio;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HabilitacionDAO.
 * @author MV
 *
 */
public class HabilitacionDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Habilitacion habilitacion) {
		if (habilitacion.getId() != null) {
			// update
			String sql = "UPDATE mat_habilitacion "
						+ "SET id_alu=?, "
						+ "id_anio=?, "
						+ "hab=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						habilitacion.getId_alu(),
						habilitacion.getId_anio(),
						habilitacion.getHab(),
						habilitacion.getFec_fin(),
						habilitacion.getEst(),
						habilitacion.getUsr_act(),
						new java.util.Date(),
						habilitacion.getId()); 
			return habilitacion.getId();

		} else {
			// insert
			String sql = "insert into mat_habilitacion ("
						+ "id_alu, "
						+ "id_anio, "
						+ "hab, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				habilitacion.getId_alu(),
				habilitacion.getId_anio(),
				habilitacion.getHab(),
				habilitacion.getFec_fin(),
				habilitacion.getEst(),
				habilitacion.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_habilitacion where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Habilitacion> list() {
		String sql = "select * from mat_habilitacion";
		
		
		
		List<Habilitacion> listHabilitacion = jdbcTemplate.query(sql, new RowMapper<Habilitacion>() {

			@Override
			public Habilitacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHabilitacion;
	}

	public Habilitacion get(int id) {
		String sql = "select * from mat_habilitacion WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Habilitacion>() {

			@Override
			public Habilitacion extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Habilitacion getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mh.id mh_id, mh.id_alu mh_id_alu , mh.id_anio mh_id_anio , mh.hab mh_hab , mh.fec_fin mh_fec_fin  ,mh.est mh_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
	
		sql = sql + " from mat_habilitacion mh "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = mh.id_alu ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = mh.id_anio ";
		sql = sql + " where mh.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Habilitacion>() {
		
			@Override
			public Habilitacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Habilitacion habilitacion= rsToEntity(rs,"mh_");
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
							habilitacion.setAlumno(alumno);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							habilitacion.setAnio(anio);
					}
							return habilitacion;
				}
				
				return null;
			}
			
		});


	}		
	
	public Habilitacion getByParams(Param param) {

		String sql = "select * from mat_habilitacion " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Habilitacion>() {
			@Override
			public Habilitacion extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Habilitacion> listByParams(Param param, String[] order) {

		String sql = "select * from mat_habilitacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Habilitacion>() {

			@Override
			public Habilitacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Habilitacion> listFullByParams(Habilitacion habilitacion, String[] order) {
	
		return listFullByParams(Param.toParam("mh",habilitacion), order);
	
	}	
	
	public List<Habilitacion> listFullByParams(Param param, String[] order) {

		String sql = "select mh.id mh_id, mh.id_alu mh_id_alu , mh.id_anio mh_id_anio , mh.hab mh_hab , mh.fec_fin mh_fec_fin  ,mh.est mh_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", usr.id, tra.ape_pat tra_ape_pat, tra.ape_mat tra_ape_mat, tra.nom tra_nom";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + " from mat_habilitacion mh";
		sql = sql + " left join alu_alumno alu on alu.id = mh.id_alu ";
		sql = sql + " left join col_anio anio on anio.id = mh.id_anio ";
		sql = sql + " left join seg_usuario usr on mh.usr_ins=usr.id ";
		sql = sql + " left join aeedu_asistencia.ges_trabajador tra on usr.id_tra=tra.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Habilitacion>() {

			@Override
			public Habilitacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				Habilitacion habilitacion= rsToEntity(rs,"mh_");
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
				habilitacion.setAlumno(alumno);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				habilitacion.setAnio(anio);
				Trabajador trabajador = new Trabajador();
				trabajador.setApe_pat(rs.getString("tra_ape_pat"));
				trabajador.setApe_mat(rs.getString("tra_ape_mat"));
				trabajador.setNom(rs.getString("tra_nom"));
				habilitacion.setTrabajador(trabajador);
				return habilitacion;
			}

		});

	}	




	// funciones privadas utilitarias para Habilitacion

	private Habilitacion rsToEntity(ResultSet rs,String alias) throws SQLException {
		Habilitacion habilitacion = new Habilitacion();

		habilitacion.setId(rs.getInt( alias + "id"));
		habilitacion.setId_alu(rs.getInt( alias + "id_alu"));
		habilitacion.setId_anio(rs.getInt( alias + "id_anio"));
		habilitacion.setHab(rs.getString( alias + "hab"));
		habilitacion.setFec_fin(rs.getDate( alias + "fec_fin"));
		habilitacion.setEst(rs.getString( alias + "est"));
								
		return habilitacion;

	}
	
}
