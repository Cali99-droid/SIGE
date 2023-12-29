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
import com.tesla.colegio.model.HisGradoIns;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.GradoInstruccion;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface HisGradoInsDAO.
 * @author MV
 *
 */
public class HisGradoInsDAOImpl{
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
	public int saveOrUpdate(HisGradoIns his_grado_ins) {
		if (his_grado_ins.getId() != null) {
			// update
			String sql = "UPDATE ges_his_grado_ins "
						+ "SET id_tra=?, "
						+ "id_gin=?, "
						+ "carrera=?, "
						+ "fec_egre=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						his_grado_ins.getId_tra(),
						his_grado_ins.getId_gin(),
						his_grado_ins.getCarrera(),
						his_grado_ins.getFec_egre(),
						his_grado_ins.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						his_grado_ins.getId()); 
			return his_grado_ins.getId();

		} else {
			// insert
			String sql = "insert into ges_his_grado_ins ("
						+ "id_tra, "
						+ "id_gin, "
						+ "carrera, "
						+ "fec_egre, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				his_grado_ins.getId_tra(),
				his_grado_ins.getId_gin(),
				his_grado_ins.getCarrera(),
				his_grado_ins.getFec_egre(),
				his_grado_ins.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from ges_his_grado_ins where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<HisGradoIns> list() {
		String sql = "select * from ges_his_grado_ins";
		
		System.out.println(sql);
		
		List<HisGradoIns> listHisGradoIns = jdbcTemplate.query(sql, new RowMapper<HisGradoIns>() {

			@Override
			public HisGradoIns mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listHisGradoIns;
	}

	public HisGradoIns get(int id) {
		String sql = "select * from ges_his_grado_ins WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HisGradoIns>() {

			@Override
			public HisGradoIns extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public HisGradoIns getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gins.id gins_id, gins.id_tra gins_id_tra , gins.id_gin gins_id_gin , gins.carrera gins_carrera , gins.fec_egre gins_fec_egre  ,gins.est gins_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
	
		sql = sql + " from ges_his_grado_ins gins "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = gins.id_tra ";
		if (aTablas.contains("cat_grado_instruccion"))
			sql = sql + " left join cat_grado_instruccion gin on gin.id = gins.id_gin ";
		sql = sql + " where gins.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<HisGradoIns>() {
		
			@Override
			public HisGradoIns extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					HisGradoIns hisgradoins= rsToEntity(rs,"gins_");
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
							hisgradoins.setTrabajador(trabajador);
					}
					if (aTablas.contains("cat_grado_instruccion")){
						GradoInstruccion gradoinstruccion = new GradoInstruccion();  
							gradoinstruccion.setId(rs.getInt("gin_id")) ;  
							gradoinstruccion.setNom(rs.getString("gin_nom")) ;  
							hisgradoins.setGradoInstruccion(gradoinstruccion);
					}
							return hisgradoins;
				}
				
				return null;
			}
			
		});


	}		
	
	public HisGradoIns getByParams(Param param) {

		String sql = "select * from ges_his_grado_ins " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<HisGradoIns>() {
			@Override
			public HisGradoIns extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<HisGradoIns> listByParams(Param param, String[] order) {

		String sql = "select * from ges_his_grado_ins " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HisGradoIns>() {

			@Override
			public HisGradoIns mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<HisGradoIns> listFullByParams(HisGradoIns hisgradoins, String[] order) {
	
		return listFullByParams(Param.toParam("gins",hisgradoins), order);
	
	}	
	
	public List<HisGradoIns> listFullByParams(Param param, String[] order) {

		String sql = "select gins.id gins_id, gins.id_tra gins_id_tra , gins.id_gin gins_id_gin , gins.carrera gins_carrera , gins.fec_egre gins_fec_egre  ,gins.est gins_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", gin.id gin_id  , gin.nom gin_nom  ";
		sql = sql + " from ges_his_grado_ins gins";
		sql = sql + " left join ges_trabajador tra on tra.id = gins.id_tra ";
		sql = sql + " left join cat_grado_instruccion gin on gin.id = gins.id_gin ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<HisGradoIns>() {

			@Override
			public HisGradoIns mapRow(ResultSet rs, int rowNum) throws SQLException {
				HisGradoIns hisgradoins= rsToEntity(rs,"gins_");
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
				hisgradoins.setTrabajador(trabajador);
				GradoInstruccion gradoinstruccion = new GradoInstruccion();  
				gradoinstruccion.setId(rs.getInt("gin_id")) ;  
				gradoinstruccion.setNom(rs.getString("gin_nom")) ;  
				hisgradoins.setGradoInstruccion(gradoinstruccion);
				return hisgradoins;
			}

		});

	}	




	// funciones privadas utilitarias para HisGradoIns

	private HisGradoIns rsToEntity(ResultSet rs,String alias) throws SQLException {
		HisGradoIns his_grado_ins = new HisGradoIns();

		his_grado_ins.setId(rs.getInt( alias + "id"));
		his_grado_ins.setId_tra(rs.getInt( alias + "id_tra"));
		his_grado_ins.setId_gin(rs.getInt( alias + "id_gin"));
		his_grado_ins.setCarrera(rs.getString( alias + "carrera"));
		his_grado_ins.setFec_egre(rs.getDate( alias + "fec_egre"));
		his_grado_ins.setEst(rs.getString( alias + "est"));
								
		return his_grado_ins;

	}
	
}
