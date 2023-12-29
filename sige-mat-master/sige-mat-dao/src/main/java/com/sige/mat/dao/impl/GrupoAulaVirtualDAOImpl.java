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
import com.tesla.colegio.model.GrupoAulaVirtual;

import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GrupoConfig;
import com.tesla.colegio.model.GrupoAlumno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GrupoAulaVirtualDAO.
 * @author MV
 *
 */
public class GrupoAulaVirtualDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GrupoAulaVirtual grupo_aula_virtual) {
		if (grupo_aula_virtual.getId() != null) {
			// update
			String sql = "UPDATE cvi_grupo_aula_virtual "
						+ "SET id_gra=?, "
						+ "id_cgc=?, "
						+ "id_anio=?, "
						+ "des=?, "
						+ "nro=?, "
						+ "lleno=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						grupo_aula_virtual.getId_gra(),
						grupo_aula_virtual.getId_cgc(),
						grupo_aula_virtual.getId_anio(),
						grupo_aula_virtual.getDes(),
						grupo_aula_virtual.getNro(),
						grupo_aula_virtual.getLleno(),
						grupo_aula_virtual.getEst(),
						grupo_aula_virtual.getUsr_act(),
						new java.util.Date(),
						grupo_aula_virtual.getId()); 
			return grupo_aula_virtual.getId();

		} else {
			// insert
			String sql = "insert into cvi_grupo_aula_virtual ("
						+ "id_gra, "
						+ "id_cgc, "
						+ "id_anio, "
						+ "des, "
						+ "nro, "
						+ "lleno, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				grupo_aula_virtual.getId_gra(),
				grupo_aula_virtual.getId_cgc(),
				grupo_aula_virtual.getId_anio(),
				grupo_aula_virtual.getDes(),
				grupo_aula_virtual.getNro(),
				grupo_aula_virtual.getLleno(),
				grupo_aula_virtual.getEst(),
				grupo_aula_virtual.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cvi_grupo_aula_virtual where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<GrupoAulaVirtual> list() {
		String sql = "select * from cvi_grupo_aula_virtual";
		
		//System.out.println(sql);
		
		List<GrupoAulaVirtual> listGrupoAulaVirtual = jdbcTemplate.query(sql, new RowMapper<GrupoAulaVirtual>() {

			@Override
			public GrupoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGrupoAulaVirtual;
	}

	public GrupoAulaVirtual get(int id) {
		String sql = "select * from cvi_grupo_aula_virtual WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAulaVirtual>() {

			@Override
			public GrupoAulaVirtual extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public GrupoAulaVirtual getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cgr.id cgr_id, cgr.id_gra cgr_id_gra , cgr.id_cgc cgr_id_cgc , cgr.id_anio cgr_id_anio, cgr.des cgr_des , cgr.nro cgr_nro , cgr.lleno cgr_lleno  ,cgr.est cgr_est ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		if (aTablas.contains("cvi_grupo_config"))
			sql = sql + ", cgc.id cgc_id  , cgc.cap cgc_cap , cgc.des cgc_des  ";
	
		sql = sql + " from cvi_grupo_aula_virtual cgr "; 
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad gra on gra.id = cgr.id_gra ";
		if (aTablas.contains("cvi_grupo_config"))
			sql = sql + " left join cvi_grupo_config cgc on cgc.id = cgr.id_cgc ";
		sql = sql + " where cgr.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAulaVirtual>() {
		
			@Override
			public GrupoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GrupoAulaVirtual grupoaulavirtual= rsToEntity(rs,"cgr_");
					if (aTablas.contains("cat_grad")){
						Grad grad = new Grad();  
							grad.setId(rs.getInt("gra_id")) ;  
							grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
							grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
							grad.setNom(rs.getString("gra_nom")) ;  
							grad.setTipo(rs.getString("gra_tipo")) ;  
							grupoaulavirtual.setGrad(grad);
					}
					if (aTablas.contains("cvi_grupo_config")){
						GrupoConfig grupoconfig = new GrupoConfig();  
							grupoconfig.setId(rs.getInt("cgc_id")) ;  
							grupoconfig.setCap(rs.getString("cgc_cap")) ;  
							grupoconfig.setDes(rs.getString("cgc_des")) ;  
							grupoaulavirtual.setGrupoConfig(grupoconfig);
					}
							return grupoaulavirtual;
				}
				
				return null;
			}
			
		});


	}		
	
	public GrupoAulaVirtual getByParams(Param param) {

		String sql = "select * from cvi_grupo_aula_virtual " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GrupoAulaVirtual>() {
			@Override
			public GrupoAulaVirtual extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<GrupoAulaVirtual> listByParams(Param param, String[] order) {

		String sql = "select * from cvi_grupo_aula_virtual " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoAulaVirtual>() {

			@Override
			public GrupoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<GrupoAulaVirtual> listFullByParams(GrupoAulaVirtual grupoaulavirtual, String[] order) {
	
		return listFullByParams(Param.toParam("cgr",grupoaulavirtual), order);
	
	}	
	
	public List<GrupoAulaVirtual> listFullByParams(Param param, String[] order) {

		String sql = "select cgr.id cgr_id, cgr.id_gra cgr_id_gra , cgr.id_anio cgr_id_anio, cgr.id_cgc cgr_id_cgc , cgr.des cgr_des , cgr.nro cgr_nro , cgr.lleno cgr_lleno  ,cgr.est cgr_est ";
		sql = sql + ", gra.id gra_id  , gra.id_nvl gra_id_nvl , gra.id_gra_ant gra_id_gra_ant , gra.nom gra_nom , gra.tipo gra_tipo  ";
		sql = sql + ", cgc.id cgc_id  , cgc.cap cgc_cap , cgc.des cgc_des  ";
		sql = sql + " from cvi_grupo_aula_virtual cgr";
		sql = sql + " left join cat_grad gra on gra.id = cgr.id_gra ";
		sql = sql + " left join cvi_grupo_config cgc on cgc.id = cgr.id_cgc ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<GrupoAulaVirtual>() {

			@Override
			public GrupoAulaVirtual mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoAulaVirtual grupoaulavirtual= rsToEntity(rs,"cgr_");
				Grad grad = new Grad();  
				grad.setId(rs.getInt("gra_id")) ;  
				grad.setId_nvl(rs.getInt("gra_id_nvl")) ;  
				grad.setId_gra_ant(rs.getInt("gra_id_gra_ant")) ;  
				grad.setNom(rs.getString("gra_nom")) ;  
				grad.setTipo(rs.getString("gra_tipo")) ;  
				grupoaulavirtual.setGrad(grad);
				GrupoConfig grupoconfig = new GrupoConfig();  
				grupoconfig.setId(rs.getInt("cgc_id")) ;  
				grupoconfig.setCap(rs.getString("cgc_cap")) ;  
				grupoconfig.setDes(rs.getString("cgc_des")) ;  
				grupoaulavirtual.setGrupoConfig(grupoconfig);
				return grupoaulavirtual;
			}

		});

	}	


	public List<GrupoAlumno> getListGrupoAlumno(Param param, String[] order) {
		String sql = "select * from cvi_grupo_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<GrupoAlumno>() {

			@Override
			public GrupoAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				GrupoAlumno grupo_alumno = new GrupoAlumno();

				grupo_alumno.setId(rs.getInt("id"));
				grupo_alumno.setId_cgr(rs.getInt("id_cgr"));
				grupo_alumno.setId_alu(rs.getInt("id_alu"));
				grupo_alumno.setEst(rs.getString("est"));
												
				return grupo_alumno;
			}

		});	
	}


	// funciones privadas utilitarias para GrupoAulaVirtual

	private GrupoAulaVirtual rsToEntity(ResultSet rs,String alias) throws SQLException {
		GrupoAulaVirtual grupo_aula_virtual = new GrupoAulaVirtual();

		grupo_aula_virtual.setId(rs.getInt( alias + "id"));
		grupo_aula_virtual.setId_gra(rs.getInt( alias + "id_gra"));
		grupo_aula_virtual.setId_cgc(rs.getInt( alias + "id_cgc"));
		grupo_aula_virtual.setId_anio(rs.getInt( alias + "id_anio"));
		grupo_aula_virtual.setDes(rs.getString( alias + "des"));
		grupo_aula_virtual.setNro(rs.getInt( alias + "nro"));
		grupo_aula_virtual.setLleno(rs.getString( alias + "lleno"));
		grupo_aula_virtual.setEst(rs.getString( alias + "est"));
		grupo_aula_virtual.setId_grupoclass(rs.getString(alias + "id_grupoclass"));					
		return grupo_aula_virtual;

	}
	
}
