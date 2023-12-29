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
import com.tesla.colegio.model.AreaCoordinador;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.Trabajador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AreaCoordinadorDAO.
 * @author MV
 *
 */
public class AreaCoordinadorDAOImpl{
	
	final static Logger logger = Logger.getLogger(AreaCoordinadorDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(AreaCoordinador area_coordinador) {
		if (area_coordinador.getId() != null) {
			// update
			String sql = "UPDATE col_area_coordinador "
						+ "SET id_anio=?, id_niv=?, "
						+ "id_area=?, "
						+ "id_tra=?, "
						+ "id_cur=?, "
						+ "id_gir=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						area_coordinador.getId_anio(),
						area_coordinador.getId_niv(),
						area_coordinador.getId_area(),
						area_coordinador.getId_tra(),
						area_coordinador.getId_cur(),
						area_coordinador.getId_gir(),
						area_coordinador.getEst(),
						area_coordinador.getUsr_act(),
						new java.util.Date(),
						area_coordinador.getId()); 
			return area_coordinador.getId();

		} else {
			// insert
			String sql = "insert into col_area_coordinador ("
						+ "id_anio, "
						+ "id_niv, "
						+ "id_area, "
						+ "id_tra, "
						+ "id_cur, "
						+ "id_gir, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?,?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				area_coordinador.getId_anio(),
				area_coordinador.getId_niv(),
				area_coordinador.getId_area(),
				area_coordinador.getId_tra(),
				area_coordinador.getId_cur(),
				area_coordinador.getId_gir(),
				area_coordinador.getEst(),
				area_coordinador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_area_coordinador where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AreaCoordinador> list() {
		String sql = "select * from col_area_coordinador";
		
		//logger.info(sql);
		
		List<AreaCoordinador> listAreaCoordinador = jdbcTemplate.query(sql, new RowMapper<AreaCoordinador>() {

			@Override
			public AreaCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAreaCoordinador;
	}

	public AreaCoordinador get(int id) {
		String sql = "select * from col_area_coordinador WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaCoordinador>() {

			@Override
			public AreaCoordinador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AreaCoordinador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cac.id cac_id, cac.id_niv cac_id_niv , cac.id_area cac_id_area , cac.id_tra cac_id_tra  ,cac.id_cur cac_id_cur, cac.est cac_est, cac.id_anio cac_id_anio, cac.id_gir cac_id_gir ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_area"))
			sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from col_area_coordinador cac "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = cac.id_niv ";
		if (aTablas.contains("cat_area"))
			sql = sql + " left join cat_area area on area.id = cac.id_area ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = cac.id_tra ";
		sql = sql + " where cac.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaCoordinador>() {
		
			@Override
			public AreaCoordinador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AreaCoordinador areacoordinador= rsToEntity(rs,"cac_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							areacoordinador.setNivel(nivel);
					}
					if (aTablas.contains("cat_area")){
						Area area = new Area();  
							area.setId(rs.getInt("area_id")) ;  
							area.setNom(rs.getString("area_nom")) ;  
							area.setDes(rs.getString("area_des")) ;  
							areacoordinador.setArea(area);
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
							trabajador.setFot(rs.getBytes("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							areacoordinador.setTrabajador(trabajador);
					}
							return areacoordinador;
				}
				
				return null;
			}
			
		});


	}		
	
	public AreaCoordinador getByParams(Param param) {

		String sql = "select * from col_area_coordinador " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AreaCoordinador>() {
			@Override
			public AreaCoordinador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AreaCoordinador> listByParams(Param param, String[] order) {

		String sql = "select * from col_area_coordinador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaCoordinador>() {

			@Override
			public AreaCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AreaCoordinador> listFullByParams(AreaCoordinador areacoordinador, String[] order) {
	
		return listFullByParams(Param.toParam("cac",areacoordinador), order);
	
	}	
	
	public List<AreaCoordinador> listFullByParams(Param param, String[] order) {

		String sql = "select cac.id cac_id, cac.id_niv cac_id_niv , cac.id_area cac_id_area , cac.id_tra cac_id_tra,cac.id_cur cac_id_cur  ,cac.est cac_est ,cac.id_anio cac_id_anio ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_area_coordinador cac";
		sql = sql + " left join cat_nivel niv on niv.id = cac.id_niv ";
		sql = sql + " left join cat_area area on area.id = cac.id_area ";
		sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = cac.id_tra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<AreaCoordinador>() {

			@Override
			public AreaCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				AreaCoordinador areacoordinador= rsToEntity(rs,"cac_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				areacoordinador.setNivel(nivel);
				Area area = new Area();  
				area.setId(rs.getInt("area_id")) ;  
				area.setNom(rs.getString("area_nom")) ;  
				area.setDes(rs.getString("area_des")) ;  
				areacoordinador.setArea(area);
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
				trabajador.setFot(rs.getBytes("tra_fot")) ;  
				trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
				trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
				areacoordinador.setTrabajador(trabajador);
				return areacoordinador;
			}

		});

	}	




	// funciones privadas utilitarias para AreaCoordinador

	private AreaCoordinador rsToEntity(ResultSet rs,String alias) throws SQLException {
		AreaCoordinador area_coordinador = new AreaCoordinador();

		area_coordinador.setId(rs.getInt( alias + "id"));
		area_coordinador.setId_anio(rs.getInt( alias + "id_anio"));
		area_coordinador.setId_niv(rs.getInt( alias + "id_niv"));
		area_coordinador.setId_area(rs.getInt( alias + "id_area"));
		area_coordinador.setId_tra(rs.getInt( alias + "id_tra"));
		area_coordinador.setId_cur(rs.getInt( alias + "id_cur"));
		area_coordinador.setId_cur(rs.getInt( alias + "id_gir"));
		area_coordinador.setEst(rs.getString( alias + "est"));
								
		return area_coordinador;

	}
	
}
