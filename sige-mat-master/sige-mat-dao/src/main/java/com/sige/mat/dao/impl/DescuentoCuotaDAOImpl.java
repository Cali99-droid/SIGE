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
import com.tesla.colegio.model.DescuentoCuota;

import com.tesla.colegio.model.DescuentoConf;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescuentoCuotaDAO.
 * @author MV
 *
 */
public class DescuentoCuotaDAOImpl{
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
	public int saveOrUpdate(DescuentoCuota descuento_cuota) {
		if (descuento_cuota.getId() != null) {
			// update
			String sql = "UPDATE fac_descuento_cuota "
						+ "SET id_fdes=?, "
						+ "id_fcuo=?, "
						+ "nro_cuota=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						descuento_cuota.getId_fdes(),
						descuento_cuota.getId_fcuo(),
						descuento_cuota.getNro_cuota(),
						descuento_cuota.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						descuento_cuota.getId()); 
			return descuento_cuota.getId();

		} else {
			// insert
			String sql = "insert into fac_descuento_cuota ("
						+ "id_fdes, "
						+ "id_fcuo, "
						+ "nro_cuota, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				descuento_cuota.getId_fdes(),
				descuento_cuota.getId_fcuo(),
				descuento_cuota.getNro_cuota(),
				descuento_cuota.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_descuento_cuota where id_fdes=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescuentoCuota> list() {
		String sql = "select * from fac_descuento_cuota";
		
		System.out.println(sql);
		
		List<DescuentoCuota> listDescuentoCuota = jdbcTemplate.query(sql, new RowMapper<DescuentoCuota>() {

			@Override
			public DescuentoCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescuentoCuota;
	}

	public DescuentoCuota get(int id) {
		String sql = "select * from fac_descuento_cuota WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoCuota>() {

			@Override
			public DescuentoCuota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescuentoCuota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fdcu.id fdcu_id, fdcu.id_fdes fdcu_id_fdes , fdcu.nro_cuota fdcu_nro_cuota  ,fdcu.est fdcu_est ";
		if (aTablas.contains("fac_descuento_conf"))
			sql = sql + ", fdes.id fdes_id  , fdes.id_cic fdes_id_cic , fdes.nom fdes_nom , fdes.monto fdes_monto , fdes.venc fdes_venc , fdes.fec_venc fdes_fec_venc , fdes.acu fdes_acu  ";
	
		sql = sql + " from fac_descuento_cuota fdcu "; 
		if (aTablas.contains("fac_descuento_conf"))
			sql = sql + " left join fac_descuento_conf fdes on fdes.id = fdcu.id_fdes ";
		sql = sql + " where fdcu.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoCuota>() {
		
			@Override
			public DescuentoCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescuentoCuota descuentocuota= rsToEntity(rs,"fdcu_");
					if (aTablas.contains("fac_descuento_conf")){
						DescuentoConf descuentoconf = new DescuentoConf();  
							descuentoconf.setId(rs.getInt("fdes_id")) ;  
							descuentoconf.setId_cct(rs.getInt("fdes_id_cct")) ;  
							descuentoconf.setNom(rs.getString("fdes_nom")) ;  
							descuentoconf.setMonto(rs.getString("fdes_monto")) ;  
							descuentoconf.setVenc(rs.getString("fdes_venc")) ;  
							descuentoconf.setFec_venc(rs.getDate("fdes_fec_venc")) ;  
							descuentoconf.setAcu(rs.getString("fdes_acu")) ;  
							descuentocuota.setDescuentoConf(descuentoconf);
					}
							return descuentocuota;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescuentoCuota getByParams(Param param) {

		String sql = "select * from fac_descuento_cuota " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoCuota>() {
			@Override
			public DescuentoCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescuentoCuota> listByParams(Param param, String[] order) {

		String sql = "select * from fac_descuento_cuota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescuentoCuota>() {

			@Override
			public DescuentoCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescuentoCuota> listFullByParams(DescuentoCuota descuentocuota, String[] order) {
	
		return listFullByParams(Param.toParam("fdcu",descuentocuota), order);
	
	}	
	
	public List<DescuentoCuota> listFullByParams(Param param, String[] order) {

		String sql = "select fdcu.id fdcu_id, fdcu.id_fdes fdcu_id_fdes, fdcu.id_fcuo fdcu_id_fcuo , fdcu.nro_cuota fdcu_nro_cuota  ,fdcu.est fdcu_est ";
		sql = sql + ", fdes.id fdes_id  , fdes.id_cic fdes_id_cic , fdes.nom fdes_nom , fdes.monto fdes_monto , fdes.venc fdes_venc , fdes.fec_venc fdes_fec_venc , fdes.acu fdes_acu  ";
		sql = sql + " from fac_descuento_cuota fdcu";
		sql = sql + " left join fac_descuento_conf fdes on fdes.id = fdcu.id_fdes ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescuentoCuota>() {

			@Override
			public DescuentoCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescuentoCuota descuentocuota= rsToEntity(rs,"fdcu_");
				DescuentoConf descuentoconf = new DescuentoConf();  
				descuentoconf.setId(rs.getInt("fdes_id")) ;  
				descuentoconf.setId_cct(rs.getInt("fdes_id_cct")) ;  
				descuentoconf.setNom(rs.getString("fdes_nom")) ;  
				descuentoconf.setMonto(rs.getString("fdes_monto")) ;  
				descuentoconf.setVenc(rs.getString("fdes_venc")) ;  
				descuentoconf.setFec_venc(rs.getDate("fdes_fec_venc")) ;  
				descuentoconf.setAcu(rs.getString("fdes_acu")) ;  
				descuentocuota.setDescuentoConf(descuentoconf);
				return descuentocuota;
			}

		});

	}	




	// funciones privadas utilitarias para DescuentoCuota

	private DescuentoCuota rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescuentoCuota descuento_cuota = new DescuentoCuota();

		descuento_cuota.setId(rs.getInt( alias + "id"));
		descuento_cuota.setId_fdes(rs.getInt( alias + "id_fdes"));
		descuento_cuota.setId_fcuo(rs.getInt( alias + "id_fcuo"));
		descuento_cuota.setNro_cuota(rs.getInt( alias + "nro_cuota"));
		descuento_cuota.setEst(rs.getString( alias + "est"));
								
		return descuento_cuota;

	}
	
}
