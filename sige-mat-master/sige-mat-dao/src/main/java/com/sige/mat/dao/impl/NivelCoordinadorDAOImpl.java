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
import com.tesla.colegio.model.NivelCoordinador;

import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Trabajador;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NivelCoordinadorDAO.
 * @author MV
 *
 */
public class NivelCoordinadorDAOImpl{
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
	public int saveOrUpdate(NivelCoordinador nivel_coordinador) {
		if (nivel_coordinador.getId() != null) {
			// update
			String sql = "UPDATE col_nivel_coordinador "
						+ "SET id_niv=?, "
						+ "id_anio=?, "
						+ "id_gir=?, "
						+ "id_tra=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						nivel_coordinador.getId_niv(),
						nivel_coordinador.getId_anio(),
						nivel_coordinador.getId_gir(),
						nivel_coordinador.getId_tra(),
						nivel_coordinador.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						nivel_coordinador.getId()); 
			return nivel_coordinador.getId();

		} else {
			// insert
			String sql = "insert into col_nivel_coordinador ("
						+ "id_niv, "
						+ "id_anio, "
						+ "id_gir, "
						+ "id_tra, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				nivel_coordinador.getId_niv(),
				nivel_coordinador.getId_anio(),
				nivel_coordinador.getId_gir(),
				nivel_coordinador.getId_tra(),
				nivel_coordinador.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_nivel_coordinador where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<NivelCoordinador> list() {
		String sql = "select * from col_nivel_coordinador";
		
		System.out.println(sql);
		
		List<NivelCoordinador> listNivelCoordinador = jdbcTemplate.query(sql, new RowMapper<NivelCoordinador>() {

			@Override
			public NivelCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNivelCoordinador;
	}

	public NivelCoordinador get(int id) {
		String sql = "select * from col_nivel_coordinador WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NivelCoordinador>() {

			@Override
			public NivelCoordinador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NivelCoordinador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cn.id cn_id, cn.id_niv cn_id_niv , cn.id_anio cn_id_anio , cn.id_gir cn_id_gir , cn.id_tra cn_id_tra  ,cn.est cn_est ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from col_nivel_coordinador cn "; 
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = cn.id_niv ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = cn.id_anio ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + " left join ges_giro_negocio gir on gir.id = cn.id_gir ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = cn.id_tra ";
		sql = sql + " where cn.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NivelCoordinador>() {
		
			@Override
			public NivelCoordinador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NivelCoordinador nivelcoordinador= rsToEntity(rs,"cn_");
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							nivelcoordinador.setNivel(nivel);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							nivelcoordinador.setAnio(anio);
					}
					if (aTablas.contains("ges_giro_negocio")){
						GiroNegocio gironegocio = new GiroNegocio();  
							gironegocio.setId(rs.getInt("gir_id")) ;  
							gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
							gironegocio.setNom(rs.getString("gir_nom")) ;  
							gironegocio.setDes(rs.getString("gir_des")) ;  
							nivelcoordinador.setGiroNegocio(gironegocio);
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
							nivelcoordinador.setTrabajador(trabajador);
					}
							return nivelcoordinador;
				}
				
				return null;
			}
			
		});


	}		
	
	public NivelCoordinador getByParams(Param param) {

		String sql = "select * from col_nivel_coordinador " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NivelCoordinador>() {
			@Override
			public NivelCoordinador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NivelCoordinador> listByParams(Param param, String[] order) {

		String sql = "select * from col_nivel_coordinador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NivelCoordinador>() {

			@Override
			public NivelCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NivelCoordinador> listFullByParams(NivelCoordinador nivelcoordinador, String[] order) {
	
		return listFullByParams(Param.toParam("cn",nivelcoordinador), order);
	
	}	
	
	public List<NivelCoordinador> listFullByParams(Param param, String[] order) {

		String sql = "select cn.id cn_id, cn.id_niv cn_id_niv , cn.id_anio cn_id_anio , cn.id_gir cn_id_gir , cn.id_tra cn_id_tra  ,cn.est cn_est ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_nivel_coordinador cn";
		sql = sql + " left join cat_nivel niv on niv.id = cn.id_niv ";
		sql = sql + " left join col_anio anio on anio.id = cn.id_anio ";
		sql = sql + " left join ges_giro_negocio gir on gir.id = cn.id_gir ";
		sql = sql + " left join ges_trabajador tra on tra.id = cn.id_tra ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NivelCoordinador>() {

			@Override
			public NivelCoordinador mapRow(ResultSet rs, int rowNum) throws SQLException {
				NivelCoordinador nivelcoordinador= rsToEntity(rs,"cn_");
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				nivelcoordinador.setNivel(nivel);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				nivelcoordinador.setAnio(anio);
				GiroNegocio gironegocio = new GiroNegocio();  
				gironegocio.setId(rs.getInt("gir_id")) ;  
				gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
				gironegocio.setNom(rs.getString("gir_nom")) ;  
				gironegocio.setDes(rs.getString("gir_des")) ;  
				nivelcoordinador.setGiroNegocio(gironegocio);
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
				nivelcoordinador.setTrabajador(trabajador);
				return nivelcoordinador;
			}

		});

	}	




	// funciones privadas utilitarias para NivelCoordinador

	private NivelCoordinador rsToEntity(ResultSet rs,String alias) throws SQLException {
		NivelCoordinador nivel_coordinador = new NivelCoordinador();

		nivel_coordinador.setId(rs.getInt( alias + "id"));
		nivel_coordinador.setId_niv(rs.getInt( alias + "id_niv"));
		nivel_coordinador.setId_anio(rs.getInt( alias + "id_anio"));
		nivel_coordinador.setId_gir(rs.getInt( alias + "id_gir"));
		nivel_coordinador.setId_tra(rs.getInt( alias + "id_tra"));
		nivel_coordinador.setEst(rs.getString( alias + "est"));
								
		return nivel_coordinador;

	}
	
}
