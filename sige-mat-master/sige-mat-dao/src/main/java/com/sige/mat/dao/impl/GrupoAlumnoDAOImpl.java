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
import com.tesla.colegio.model.GrupoAlumno;

import com.tesla.colegio.model.GrupoAulaVirtual;
import com.tesla.colegio.model.Alumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GrupoAlumnoDAO.
 * @author MV
 *
 */
public class GrupoAlumnoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GrupoAlumno grupo_alumno) {
		if (grupo_alumno.getId() != null) {
			// update
			String sql = "UPDATE cvi_grupo_alumno "
						+ "SET id_cgr=?, "
						+ "id_alu=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						grupo_alumno.getId_cgr(),
						grupo_alumno.getId_alu(),
						grupo_alumno.getEst(),
						grupo_alumno.getUsr_act(),
						new java.util.Date(),
						grupo_alumno.getId()); 
			return grupo_alumno.getId();

		} else {
			// insert
			String sql = "insert into cvi_grupo_alumno ("
						+ "id_cgr, "
						+ "id_alu, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				grupo_alumno.getId_cgr(),
				grupo_alumno.getId_alu(),
				grupo_alumno.getEst(),
				grupo_alumno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_grupo_alumno where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<GrupoAlumno> list() {
		String sql = "select * from cvi_grupo_alumno";
		
		//System.out.println(sql);
		
		List<GrupoAlumno> listGrupoAlumno = jdbcTemplate.query(sql, new RowMapper<GrupoAlumno>() {

			@Override
			public GrupoAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGrupoAlumno;
	}

	public GrupoAlumno get(int id) {
		String sql = "select * from cvi_grupo_alumno WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAlumno>() {

			@Override
			public GrupoAlumno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public GrupoAlumno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cga.id cga_id, cga.id_cgr cga_id_cgr , cga.id_alu cga_id_alu  ,cga.est cga_est ";
		if (aTablas.contains("cvi_grupo_aula_virtual"))
			sql = sql + ", cgr.id cgr_id  , cgr.id_gra cgr_id_gra , cgr.id_cgc cgr_id_cgc  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from cvi_grupo_alumno cga "; 
		if (aTablas.contains("cvi_grupo_aula_virtual"))
			sql = sql + " left join cvi_grupo_aula_virtual cgr on cgr.id = cga.id_cgr ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = cga.id_alu ";
		sql = sql + " where cga.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAlumno>() {
		
			@Override
			public GrupoAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GrupoAlumno grupoalumno= rsToEntity(rs,"cga_");
					if (aTablas.contains("cvi_grupo_aula_virtual")){
						GrupoAulaVirtual grupoaulavirtual = new GrupoAulaVirtual();  
							grupoaulavirtual.setId(rs.getInt("cgr_id")) ;  
							grupoaulavirtual.setId_gra(rs.getInt("cgr_id_gra")) ;  
							grupoaulavirtual.setId_cgc(rs.getInt("cgr_id_cgc")) ;  
							grupoalumno.setGrupoAulaVirtual(grupoaulavirtual);
					}
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
							grupoalumno.setAlumno(alumno);
					}
							return grupoalumno;
				}
				
				return null;
			}
			
		});


	}		
	
	public GrupoAlumno getByParams(Param param) {

		String sql = "select * from cvi_grupo_alumno " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAlumno>() {
			@Override
			public GrupoAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<GrupoAlumno> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_grupo_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoAlumno>() {

			@Override
			public GrupoAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<GrupoAlumno> listFullByParams(GrupoAlumno grupoalumno, String[] order) {
	
		return listFullByParams(Param.toParam("cga",grupoalumno), order);
	
	}	
	
	public List<GrupoAlumno> listFullByParams(Param param, String[] order) {

		String sql = "select cga.id cga_id, cga.id_cgr cga_id_cgr , cga.id_alu cga_id_alu  ,cga.est cga_est ";
		sql = sql + ", cgr.id cgr_id  , cgr.id_gra cgr_id_gra , cgr.id_cgc cgr_id_cgc  ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from cvi_grupo_alumno cga";
		sql = sql + " left join cvi_grupo_aula_virtual cgr on cgr.id = cga.id_cgr ";
		sql = sql + " left join alu_alumno alu on alu.id = cga.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoAlumno>() {

			@Override
			public GrupoAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoAlumno grupoalumno= rsToEntity(rs,"cga_");
				GrupoAulaVirtual grupoaulavirtual = new GrupoAulaVirtual();  
				grupoaulavirtual.setId(rs.getInt("cgr_id")) ;  
				grupoaulavirtual.setId_gra(rs.getInt("cgr_id_gra")) ;  
				grupoaulavirtual.setId_cgc(rs.getInt("cgr_id_cgc")) ;  
				grupoalumno.setGrupoAulaVirtual(grupoaulavirtual);
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
				grupoalumno.setAlumno(alumno);
				return grupoalumno;
			}

		});

	}	




	// funciones privadas utilitarias para GrupoAlumno

	private GrupoAlumno rsToEntity(ResultSet rs,String alias) throws SQLException {
		GrupoAlumno grupo_alumno = new GrupoAlumno();

		grupo_alumno.setId(rs.getInt( alias + "id"));
		grupo_alumno.setId_cgr(rs.getInt( alias + "id_cgr"));
		grupo_alumno.setId_alu(rs.getInt( alias + "id_alu"));
		grupo_alumno.setEst(rs.getString( alias + "est"));
								
		return grupo_alumno;

	}
	
}
