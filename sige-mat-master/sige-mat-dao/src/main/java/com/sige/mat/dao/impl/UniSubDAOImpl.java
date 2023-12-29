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
import com.tesla.colegio.model.UniSub;

import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Subtema;
//import com.tesla.colegio.model.GrupSubPadre;
import com.tesla.colegio.model.Tema;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UniSubDAO.
 * @author MV
 *
 */
public class UniSubDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UniSub uni_sub) {
		if (uni_sub.getId() != null) {
			// update
			String sql = "UPDATE col_uni_sub "
						+ "SET id_uni=?, "
						+ "id_cgsp=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						uni_sub.getId_uni(),
						uni_sub.getId_cgsp(),
						uni_sub.getEst(),
						uni_sub.getUsr_act(),
						new java.util.Date(),
						uni_sub.getId()); 
			return uni_sub.getId();

		} else {
			// insert
			String sql = "insert into col_uni_sub ("
						+ "id_uni, "
						+ "id_cgsp, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				uni_sub.getId_uni(),
				uni_sub.getId_cgsp(),
				uni_sub.getEst(),
				uni_sub.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_uni_sub where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}
	
	public void deletegrupo(int id_cgsp) {
		String sql = "delete from col_uni_sub where id_cgsp=?";
		
		
		
		jdbcTemplate.update(sql, id_cgsp);
	}

	public List<UniSub> list() {
		String sql = "select * from col_uni_sub";
		
		
		
		List<UniSub> listUniSub = jdbcTemplate.query(sql, new RowMapper<UniSub>() {

			@Override
			public UniSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUniSub;
	}

	public UniSub get(int id) {
		String sql = "select * from col_uni_sub WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UniSub>() {

			@Override
			public UniSub extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UniSub getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cus.id cus_id, cus.id_uni cus_id_uni , cus.id_cgsp cus_id_cgsp  ,cus.est cus_est ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.producto uni_producto  ";
		if (aTablas.contains("col_grup_sub_padre"))
			sql = sql + ", cgsp.id cgsp_id  , cgsp.id_anio cgsp_id_anio  ";
	
		sql = sql + " from col_uni_sub cus "; 
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = cus.id_uni ";
		if (aTablas.contains("col_grup_sub_padre"))
			sql = sql + " left join col_grup_sub_padre cgsp on cgsp.id = cus.id_cgsp ";
		sql = sql + " where cus.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<UniSub>() {
		
			@Override
			public UniSub extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UniSub unisub= rsToEntity(rs,"cus_");
					if (aTablas.contains("col_curso_unidad")){
						CursoUnidad cursounidad = new CursoUnidad();  
							cursounidad.setId(rs.getInt("uni_id")) ;  
							cursounidad.setId_niv(rs.getInt("uni_id_niv")) ;  
							cursounidad.setId_gra(rs.getInt("uni_id_gra")) ;  
							cursounidad.setId_cur(rs.getInt("uni_id_cur")) ;  
							cursounidad.setId_cpu(rs.getInt("uni_id_cpu")) ;  
							cursounidad.setNum(rs.getInt("uni_num")) ;  
							cursounidad.setNom(rs.getString("uni_nom")) ;  
							cursounidad.setDes(rs.getString("uni_des")) ;  
							//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
							cursounidad.setProducto(rs.getString("uni_producto")) ;  
							unisub.setCursoUnidad(cursounidad);
					}
					/*if (aTablas.contains("col_grup_sub_padre")){
						GrupSubPadre grupsubpadre = new GrupSubPadre();  
							grupsubpadre.setId(rs.getInt("cgsp_id")) ;  
							grupsubpadre.setId_anio(rs.getInt("cgsp_id_anio")) ;  
							unisub.setGrupSubPadre(grupsubpadre);
					}*/
							return unisub;
				}
				
				return null;
			}
			
		});


	}		
	
	public UniSub getByParams(Param param) {

		String sql = "select * from col_uni_sub " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UniSub>() {
			@Override
			public UniSub extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UniSub> listByParams(Param param, String[] order) {

		String sql = "select * from col_uni_sub " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<UniSub>() {

			@Override
			public UniSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UniSub> listFullByParams(UniSub unisub, String[] order) {
	
		return listFullByParams(Param.toParam("cus",unisub), order);
	
	}	
	
	public List<UniSub> listFullByParams(Param param, String[] order) {

		String sql = "select cus.id cus_id, cus.id_uni cus_id_uni , cus.id_cgsp cus_id_cgsp  ,cus.est cus_est ";
		sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.producto uni_producto  ";
		sql = sql + ", tem.id tem_id, tem.nom tem_nom, sub.id sub_id, sub.nom sub_nom ";
		sql = sql + ", cgsp.id cgsp_id  , cgsp.id_anio cgsp_id_anio  ";
		sql = sql + " from col_uni_sub cus";
		sql = sql + " left join col_curso_unidad uni on uni.id = cus.id_uni ";
		sql = sql + " left join col_grup_sub_padre cgsp on cgsp.id = cus.id_cgsp ";
		sql = sql + " left join col_grup_subtema cgs on cgs.id_cgsp=cgsp.id ";
		sql = sql + " left join col_curso_subtema ccs on cgs.id_ccs= ccs.id ";
		sql = sql + " left join col_subtema sub on ccs.id_sub= sub.id ";
		sql = sql + " left join col_tema tem on sub.id_tem=tem.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<UniSub>() {

			@Override
			public UniSub mapRow(ResultSet rs, int rowNum) throws SQLException {
				UniSub unisub= rsToEntity(rs,"cus_");
				CursoUnidad cursounidad = new CursoUnidad();  
				cursounidad.setId(rs.getInt("uni_id")) ;  
				cursounidad.setId_niv(rs.getInt("uni_id_niv")) ;  
				cursounidad.setId_gra(rs.getInt("uni_id_gra")) ;  
				cursounidad.setId_cur(rs.getInt("uni_id_cur")) ;  
				cursounidad.setId_cpu(rs.getInt("uni_id_cpu")) ;  
				cursounidad.setNum(rs.getInt("uni_num")) ;  
				cursounidad.setNom(rs.getString("uni_nom")) ;  
				cursounidad.setDes(rs.getString("uni_des")) ;  
				//cursounidad.setNro_sem(rs.getInt("uni_nro_sem")) ;  
				cursounidad.setProducto(rs.getString("uni_producto")) ;
				unisub.setCursoUnidad(cursounidad);
				Tema tema = new Tema();
				tema.setId(rs.getInt("tem_id"));
				tema.setNom(rs.getString("tem_nom"));
				unisub.setTema(tema);
				Subtema subtema = new Subtema();
				subtema.setId(rs.getInt("sub_id"));
				subtema.setNom(rs.getString("sub_nom"));
				unisub.setSubtema(subtema);
				/*GrupSubPadre grupsubpadre = new GrupSubPadre();  
				grupsubpadre.setId(rs.getInt("cgsp_id")) ;  
				grupsubpadre.setId_anio(rs.getInt("cgsp_id_anio")) ;  
				unisub.setGrupSubPadre(grupsubpadre);*/
				return unisub;
			}

		});

	}	




	// funciones privadas utilitarias para UniSub

	private UniSub rsToEntity(ResultSet rs,String alias) throws SQLException {
		UniSub uni_sub = new UniSub();

		uni_sub.setId(rs.getInt( alias + "id"));
		uni_sub.setId_uni(rs.getInt( alias + "id_uni"));
		uni_sub.setId_cgsp(rs.getInt( alias + "id_cgsp"));
		uni_sub.setEst(rs.getString( alias + "est"));
								
		return uni_sub;

	}
	
}
