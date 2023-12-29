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
import com.tesla.colegio.model.DescuentoConf;

import com.tesla.colegio.model.Ciclo;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DescuentoConfDAO.
 * @author MV
 *
 */
public class DescuentoConfDAOImpl{
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
	public int saveOrUpdate(DescuentoConf descuento_conf) {
		if (descuento_conf.getId() != null) {
			// update
			String sql = "UPDATE fac_descuento_conf "
						+ "SET id_cct=?, "
						+ "id_des=?, "
						+ "nom=?, "
						+ "monto=?, "
						+ "nro_cuota_max=?, "
						+ "venc=?, "
						+ "fec_venc=?, "
						+ "acu=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						descuento_conf.getId_cct(),
						descuento_conf.getId_des(),
						descuento_conf.getNom(),
						descuento_conf.getMonto(),
						descuento_conf.getNro_cuota_max(),
						descuento_conf.getVenc(),
						descuento_conf.getFec_venc(),
						descuento_conf.getAcu(),
						descuento_conf.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						descuento_conf.getId()); 
			return descuento_conf.getId();

		} else {
			// insert
			String sql = "insert into fac_descuento_conf ("
						+ "id_cct, "
						+ "id_des, "
						+ "nom, "
						+ "monto, "
						+ "nro_cuota_max, "
						+ "venc, "
						+ "fec_venc, "
						+ "acu, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				descuento_conf.getId_cct(),
				descuento_conf.getId_des(),
				descuento_conf.getNom(),
				descuento_conf.getMonto(),
				descuento_conf.getNro_cuota_max(),
				descuento_conf.getVenc(),
				descuento_conf.getFec_venc(),
				descuento_conf.getAcu(),
				descuento_conf.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_descuento_conf where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DescuentoConf> list() {
		String sql = "select * from fac_descuento_conf";
		
		System.out.println(sql);
		
		List<DescuentoConf> listDescuentoConf = jdbcTemplate.query(sql, new RowMapper<DescuentoConf>() {

			@Override
			public DescuentoConf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDescuentoConf;
	}

	public DescuentoConf get(int id) {
		String sql = "select * from fac_descuento_conf WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoConf>() {

			@Override
			public DescuentoConf extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DescuentoConf getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fdes.id fdes_id, fdes.id_cic fdes_id_cic , fdes.nom fdes_nom , fdes.venc fdes_venc , fdes.fec_venc fdes_fec_venc , fdes.acu fdes_acu ,fdes.est fdes_est ";
		if (aTablas.contains("col_ciclo"))
			sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
	
		sql = sql + " from fac_descuento_conf fdes "; 
		if (aTablas.contains("col_ciclo"))
			sql = sql + " left join col_ciclo cic on cic.id = fdes.id_cic ";
		sql = sql + " where fdes.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoConf>() {
		
			@Override
			public DescuentoConf extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DescuentoConf descuentoconf= rsToEntity(rs,"fdes_");
					if (aTablas.contains("col_ciclo")){
						Ciclo ciclo = new Ciclo();  
							ciclo.setId(rs.getInt("cic_id")) ;  
							ciclo.setId_per(rs.getInt("cic_id_per")) ;  
							ciclo.setNom(rs.getString("cic_nom")) ;  
							ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
							ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
							descuentoconf.setCiclo(ciclo);
					}
							return descuentoconf;
				}
				
				return null;
			}
			
		});


	}		
	
	public DescuentoConf getByParams(Param param) {

		String sql = "select * from fac_descuento_conf " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DescuentoConf>() {
			@Override
			public DescuentoConf extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DescuentoConf> listByParams(Param param, String[] order) {

		String sql = "select * from fac_descuento_conf " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescuentoConf>() {

			@Override
			public DescuentoConf mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DescuentoConf> listFullByParams(DescuentoConf descuentoconf, String[] order) {
	
		return listFullByParams(Param.toParam("fdes",descuentoconf), order);
	
	}	
	
	public List<DescuentoConf> listFullByParams(Param param, String[] order) {

		String sql = "select fdes.id fdes_id, fdes.id_cct fdes_id_cct, fdes.id_des fdes_id_des , fdes.nom fdes_nom , fdes.monto fdes_monto, fdes.nro_cuota_max fdes_nro_cuota_max, fdes.venc fdes_venc , fdes.fec_venc fdes_fec_venc , fdes.acu fdes_acu ,fdes.est fdes_est ";
		sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
		sql = sql + " from fac_descuento_conf fdes";
		sql = sql + " left join col_ciclo cic on cic.id = fdes.id_cic ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DescuentoConf>() {

			@Override
			public DescuentoConf mapRow(ResultSet rs, int rowNum) throws SQLException {
				DescuentoConf descuentoconf= rsToEntity(rs,"fdes_");
				Ciclo ciclo = new Ciclo();  
				ciclo.setId(rs.getInt("cic_id")) ;  
				ciclo.setId_per(rs.getInt("cic_id_per")) ;  
				ciclo.setNom(rs.getString("cic_nom")) ;  
				ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
				ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
				descuentoconf.setCiclo(ciclo);
				return descuentoconf;
			}

		});

	}	




	// funciones privadas utilitarias para DescuentoConf

	private DescuentoConf rsToEntity(ResultSet rs,String alias) throws SQLException {
		DescuentoConf descuento_conf = new DescuentoConf();

		descuento_conf.setId(rs.getInt( alias + "id"));
		descuento_conf.setId_cct(rs.getInt( alias + "id_cct"));
		descuento_conf.setId_des(rs.getInt( alias + "id_des"));
		descuento_conf.setNom(rs.getString( alias + "nom"));
		descuento_conf.setMonto(rs.getString(alias + "monto"));
		descuento_conf.setNro_cuota_max(rs.getInt(alias + "nro_cuota_max"));
		descuento_conf.setVenc(rs.getString( alias + "venc"));
		descuento_conf.setFec_venc(rs.getDate( alias + "fec_venc"));
		descuento_conf.setAcu(rs.getString( alias + "acu"));
		descuento_conf.setEst(rs.getString( alias + "est"));
								
		return descuento_conf;

	}
	
}
