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
import com.tesla.colegio.model.AdministradorSede;

import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Trabajador;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AdministradorSedeDAO.
 * @author MV
 *
 */
public class AdministradorSedeDAOImpl{
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
	public int saveOrUpdate(AdministradorSede administrador_sede) {
		if (administrador_sede.getId() != null) {
			// update
			String sql = "UPDATE col_administrador_sede "
						+ "SET id_suc=?, "
						+ "id_anio=?, "
						+ "id_tra=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						administrador_sede.getId_suc(),
						administrador_sede.getId_anio(),
						administrador_sede.getId_tra(),
						administrador_sede.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						administrador_sede.getId()); 
			return administrador_sede.getId();

		} else {
			// insert
			String sql = "insert into col_administrador_sede ("
						+ "id_suc, "
						+ "id_anio, "
						+ "id_tra, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				administrador_sede.getId_suc(),
				administrador_sede.getId_anio(),
				administrador_sede.getId_tra(),
				administrador_sede.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_administrador_sede where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<AdministradorSede> list() {
		String sql = "select * from col_administrador_sede";
		
		System.out.println(sql);
		
		List<AdministradorSede> listAdministradorSede = jdbcTemplate.query(sql, new RowMapper<AdministradorSede>() {

			@Override
			public AdministradorSede mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAdministradorSede;
	}

	public AdministradorSede get(int id) {
		String sql = "select * from col_administrador_sede WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AdministradorSede>() {

			@Override
			public AdministradorSede extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public AdministradorSede getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cn.id cn_id, cn.id_suc cn_id_suc , cn.id_anio cn_id_anio , cn.id_tra cn_id_tra  ,cn.est cn_est ";
		if (aTablas.contains("ges_sucursal"))
			sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from col_administrador_sede cn "; 
		if (aTablas.contains("ges_sucursal"))
			sql = sql + " left join ges_sucursal suc on suc.id = cn.id_suc ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cn.id_anio ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = cn.id_tra ";
		sql = sql + " where cn.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<AdministradorSede>() {
		
			@Override
			public AdministradorSede extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					AdministradorSede administradorsede= rsToEntity(rs,"cn_");
					if (aTablas.contains("ges_sucursal")){
						Sucursal sucursal = new Sucursal();  
							sucursal.setId(rs.getInt("suc_id")) ;  
							sucursal.setNom(rs.getString("suc_nom")) ;  
							sucursal.setDir(rs.getString("suc_dir")) ;  
							sucursal.setTel(rs.getString("suc_tel")) ;  
							sucursal.setCorreo(rs.getString("suc_correo")) ;  
							administradorsede.setSucursal(sucursal);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							administradorsede.setAnio(anio);
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
							//trabajador.setFot(rs.getString("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							administradorsede.setTrabajador(trabajador);
					}
							return administradorsede;
				}
				
				return null;
			}
			
		});


	}		
	
	public AdministradorSede getByParams(Param param) {

		String sql = "select * from col_administrador_sede " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<AdministradorSede>() {
			@Override
			public AdministradorSede extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<AdministradorSede> listByParams(Param param, String[] order) {

		String sql = "select * from col_administrador_sede " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AdministradorSede>() {

			@Override
			public AdministradorSede mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<AdministradorSede> listFullByParams(AdministradorSede administradorsede, String[] order) {
	
		return listFullByParams(Param.toParam("cn",administradorsede), order);
	
	}	
	
	public List<AdministradorSede> listFullByParams(Param param, String[] order) {

		String sql = "select cn.id cn_id, cn.id_suc cn_id_suc , cn.id_anio cn_id_anio , cn.id_tra cn_id_tra  ,cn.est cn_est ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom , suc.dir suc_dir , suc.tel suc_tel , suc.correo suc_correo  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_administrador_sede cn";
		sql = sql + " left join ges_sucursal suc on suc.id = cn.id_suc ";
		sql = sql + " left join col_anio anio on anio.id = cn.id_anio ";
		sql = sql + " left join ges_trabajador tra on tra.id = cn.id_tra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<AdministradorSede>() {

			@Override
			public AdministradorSede mapRow(ResultSet rs, int rowNum) throws SQLException {
				AdministradorSede administradorsede= rsToEntity(rs,"cn_");
				Sucursal sucursal = new Sucursal();  
				sucursal.setId(rs.getInt("suc_id")) ;  
				sucursal.setNom(rs.getString("suc_nom")) ;  
				sucursal.setDir(rs.getString("suc_dir")) ;  
				sucursal.setTel(rs.getString("suc_tel")) ;  
				sucursal.setCorreo(rs.getString("suc_correo")) ;  
				administradorsede.setSucursal(sucursal);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				administradorsede.setAnio(anio);
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
				administradorsede.setTrabajador(trabajador);
				return administradorsede;
			}

		});

	}	




	// funciones privadas utilitarias para AdministradorSede

	private AdministradorSede rsToEntity(ResultSet rs,String alias) throws SQLException {
		AdministradorSede administrador_sede = new AdministradorSede();

		administrador_sede.setId(rs.getInt( alias + "id"));
		administrador_sede.setId_suc(rs.getInt( alias + "id_suc"));
		administrador_sede.setId_anio(rs.getInt( alias + "id_anio"));
		administrador_sede.setId_tra(rs.getInt( alias + "id_tra"));
		administrador_sede.setEst(rs.getString( alias + "est"));
								
		return administrador_sede;

	}
	
}
