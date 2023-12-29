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
import com.tesla.colegio.model.UnidadTrabajador;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.CursoUnidad;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface UnidadTrabajadorDAO.
 * @author MV
 *
 */
public class UnidadTrabajadorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(UnidadTrabajador unidad_trabajador) {
		if (unidad_trabajador.getId() != null) {
			// update
			String sql = "UPDATE con_unidad_trabajador "
						+ "SET id_tra=?, "
						+ "id_uni=?, "
						+ "flg_descarga=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						unidad_trabajador.getId_tra(),
						unidad_trabajador.getId_uni(),
						unidad_trabajador.getFlg_descarga(),
						unidad_trabajador.getEst(),
						unidad_trabajador.getUsr_act(),
						new java.util.Date(),
						unidad_trabajador.getId()); 
			return unidad_trabajador.getId();

		} else {
			// insert
			String sql = "insert into con_unidad_trabajador ("
						+ "id_tra, "
						+ "id_uni, "
						+ "flg_descarga, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				unidad_trabajador.getId_tra(),
				unidad_trabajador.getId_uni(),
				unidad_trabajador.getFlg_descarga(),
				unidad_trabajador.getEst(),
				unidad_trabajador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from con_unidad_trabajador where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<UnidadTrabajador> list() {
		String sql = "select * from con_unidad_trabajador";
		
		
		
		List<UnidadTrabajador> listUnidadTrabajador = jdbcTemplate.query(sql, new RowMapper<UnidadTrabajador>() {

			@Override
			public UnidadTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listUnidadTrabajador;
	}

	public UnidadTrabajador get(int id) {
		String sql = "select * from con_unidad_trabajador WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTrabajador>() {

			@Override
			public UnidadTrabajador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public UnidadTrabajador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ccu.id ccu_id, ccu.id_tra ccu_id_tra , ccu.id_uni ccu_id_uni , ccu.flg_descarga ccu_flg_descarga  ,ccu.est ccu_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
	
		sql = sql + " from con_unidad_trabajador ccu "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = ccu.id_tra ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = ccu.id_uni ";
		sql = sql + " where ccu.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTrabajador>() {
		
			@Override
			public UnidadTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					UnidadTrabajador unidadtrabajador= rsToEntity(rs,"ccu_");
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
							unidadtrabajador.setTrabajador(trabajador);
					}
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
							unidadtrabajador.setCursoUnidad(cursounidad);
					}
							return unidadtrabajador;
				}
				
				return null;
			}
			
		});


	}		
	
	public UnidadTrabajador getByParams(Param param) {

		String sql = "select * from con_unidad_trabajador " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<UnidadTrabajador>() {
			@Override
			public UnidadTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<UnidadTrabajador> listByParams(Param param, String[] order) {

		String sql = "select * from con_unidad_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<UnidadTrabajador>() {

			@Override
			public UnidadTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<UnidadTrabajador> listFullByParams(UnidadTrabajador unidadtrabajador, String[] order) {
	
		return listFullByParams(Param.toParam("ccu",unidadtrabajador), order);
	
	}	
	
	public List<UnidadTrabajador> listFullByParams(Param param, String[] order) {

		String sql = "select ccu.id ccu_id, ccu.id_tra ccu_id_tra , ccu.id_uni ccu_id_uni , ccu.flg_descarga ccu_flg_descarga  ,ccu.est ccu_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		sql = sql + " from con_unidad_trabajador ccu";
		sql = sql + " left join ges_trabajador tra on tra.id = ccu.id_tra ";
		sql = sql + " left join col_curso_unidad uni on uni.id = ccu.id_uni ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<UnidadTrabajador>() {

			@Override
			public UnidadTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				UnidadTrabajador unidadtrabajador= rsToEntity(rs,"ccu_");
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
				unidadtrabajador.setTrabajador(trabajador);
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
				unidadtrabajador.setCursoUnidad(cursounidad);
				return unidadtrabajador;
			}

		});

	}	




	// funciones privadas utilitarias para UnidadTrabajador

	private UnidadTrabajador rsToEntity(ResultSet rs,String alias) throws SQLException {
		UnidadTrabajador unidad_trabajador = new UnidadTrabajador();

		unidad_trabajador.setId(rs.getInt( alias + "id"));
		unidad_trabajador.setId_tra(rs.getInt( alias + "id_tra"));
		unidad_trabajador.setId_uni(rs.getInt( alias + "id_uni"));
		unidad_trabajador.setFlg_descarga(rs.getString( alias + "flg_descarga"));
		unidad_trabajador.setEst(rs.getString( alias + "est"));
								
		return unidad_trabajador;

	}
	
}
