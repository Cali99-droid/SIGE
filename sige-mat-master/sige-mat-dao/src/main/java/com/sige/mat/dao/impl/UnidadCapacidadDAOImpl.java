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
import com.tesla.colegio.model.UnidadCapacidad;

import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Capacidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UnidadCapacidadDAO.
 * @author MV
 *
 */
public class UnidadCapacidadDAOImpl{
	final static Logger logger = Logger.getLogger(UnidadCapacidadDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UnidadCapacidad unidad_capacidad) {
		if (unidad_capacidad.getId() != null) {
			// update
			String sql = "UPDATE col_unidad_capacidad "
						+ "SET id_uni=?, "
						+ "id_cap=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						unidad_capacidad.getId_uni(),
						unidad_capacidad.getId_cap(),
						unidad_capacidad.getEst(),
						unidad_capacidad.getUsr_act(),
						new java.util.Date(),
						unidad_capacidad.getId()); 
			return unidad_capacidad.getId();

		} else {
			// insert
			String sql = "insert into col_unidad_capacidad ("
						+ "id_uni, "
						+ "id_cap, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				unidad_capacidad.getId_uni(),
				unidad_capacidad.getId_cap(),
				unidad_capacidad.getEst(),
				unidad_capacidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_unidad_capacidad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<UnidadCapacidad> list() {
		String sql = "select * from col_unidad_capacidad";
		
		//logger.info(sql);
		
		List<UnidadCapacidad> listUnidadCapacidad = jdbcTemplate.query(sql, new RowMapper<UnidadCapacidad>() {

			@Override
			public UnidadCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUnidadCapacidad;
	}

	public UnidadCapacidad get(int id) {
		String sql = "select * from col_unidad_capacidad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadCapacidad>() {

			@Override
			public UnidadCapacidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UnidadCapacidad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cuc.id cuc_id, cuc.id_uni cuc_id_uni , cuc.id_cap cuc_id_cap  ,cuc.est cuc_est ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + ", uni.id uni_id  , uni.id_cua uni_id_cua , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_ses uni_nro_ses , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
	
		sql = sql + " from col_unidad_capacidad cuc "; 
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = cuc.id_uni ";
		if (aTablas.contains("col_capacidad"))
			sql = sql + " left join col_capacidad cap on cap.id = cuc.id_cap ";
		sql = sql + " where cuc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadCapacidad>() {
		
			@Override
			public UnidadCapacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UnidadCapacidad unidadcapacidad= rsToEntity(rs,"cuc_");
					if (aTablas.contains("col_curso_unidad")){
						CursoUnidad cursounidad = new CursoUnidad();  
							cursounidad.setId(rs.getInt("uni_id")) ;  
							cursounidad.setNum(rs.getInt("uni_num")) ;  
							cursounidad.setNom(rs.getString("uni_nom")) ;  
							cursounidad.setDes(rs.getString("uni_des")) ;  
							//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
							cursounidad.setProducto(rs.getString("uni_producto")) ;  
							unidadcapacidad.setCursoUnidad(cursounidad);
					}
					if (aTablas.contains("col_capacidad")){
						Capacidad capacidad = new Capacidad();  
							capacidad.setId(rs.getInt("cap_id")) ;  
							capacidad.setId_com(rs.getInt("cap_id_com")) ;  
							capacidad.setNom(rs.getString("cap_nom")) ;  
							unidadcapacidad.setCapacidad(capacidad);
					}
							return unidadcapacidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public UnidadCapacidad getByParams(Param param) {

		String sql = "select * from col_unidad_capacidad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadCapacidad>() {
			@Override
			public UnidadCapacidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UnidadCapacidad> listByParams(Param param, String[] order) {

		String sql = "select * from col_unidad_capacidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UnidadCapacidad>() {

			@Override
			public UnidadCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UnidadCapacidad> listFullByParams(UnidadCapacidad unidadcapacidad, String[] order) {
	
		return listFullByParams(Param.toParam("cuc",unidadcapacidad), order);
	
	}	
	
	public List<UnidadCapacidad> listFullByParams(Param param, String[] order) {

		String sql = "select cuc.id cuc_id, cuc.id_uni cuc_id_uni , cuc.id_cap cuc_id_cap  ,cuc.est cuc_est ";
		sql = sql + ", uni.id uni_id  , uni.id_cua uni_id_cua , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_ses uni_nro_ses , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		sql = sql + ", cap.id cap_id  , cap.id_com cap_id_com , cap.nom cap_nom  ";
		sql = sql + " from col_unidad_capacidad cuc";
		sql = sql + " left join col_curso_unidad uni on uni.id = cuc.id_uni ";
		sql = sql + " left join col_capacidad cap on cap.id = cuc.id_cap ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<UnidadCapacidad>() {

			@Override
			public UnidadCapacidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadCapacidad unidadcapacidad= rsToEntity(rs,"cuc_");
				CursoUnidad cursounidad = new CursoUnidad();  
				cursounidad.setId(rs.getInt("uni_id")) ;  
				cursounidad.setNum(rs.getInt("uni_num")) ;  
				cursounidad.setNom(rs.getString("uni_nom")) ;  
				cursounidad.setDes(rs.getString("uni_des")) ;   
				//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
				cursounidad.setProducto(rs.getString("uni_producto")) ;  
				unidadcapacidad.setCursoUnidad(cursounidad);
				Capacidad capacidad = new Capacidad();  
				capacidad.setId(rs.getInt("cap_id")) ;  
				capacidad.setId_com(rs.getInt("cap_id_com")) ;  
				capacidad.setNom(rs.getString("cap_nom")) ;  
				unidadcapacidad.setCapacidad(capacidad);
				return unidadcapacidad;
			}

		});

	}	




	// funciones privadas utilitarias para UnidadCapacidad

	private UnidadCapacidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		UnidadCapacidad unidad_capacidad = new UnidadCapacidad();

		unidad_capacidad.setId(rs.getInt( alias + "id"));
		unidad_capacidad.setId_uni(rs.getInt( alias + "id_uni"));
		unidad_capacidad.setId_cap(rs.getInt( alias + "id_cap"));
		unidad_capacidad.setEst(rs.getString( alias + "est"));
								
		return unidad_capacidad;

	}
	
}
