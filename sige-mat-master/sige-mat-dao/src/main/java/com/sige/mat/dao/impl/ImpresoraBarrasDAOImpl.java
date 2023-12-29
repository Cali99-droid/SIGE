package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.tesla.colegio.model.ImpresoraBarras;



import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ImpresoraBarrasDAO.
 * @author MV
 *
 */
public class ImpresoraBarrasDAOImpl{
	final static Logger logger = Logger.getLogger(ImpresoraBarrasDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ImpresoraBarras impresora_barras) {
		if (impresora_barras.getId() != null) {
			// update
			String sql = "UPDATE asi_impresora_barras "
						+ "SET tipo=?, "
						+ "ancho=?, "
						+ "tam_letra=?, "
						+ "mar_izq=?, "
						+ "mar_der=?, "
						+ "mar_sup=?, "
						+ "mar_inf=?, "
						+ "texto_relleno=?, "
						+ "barra_relleno=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						impresora_barras.getTipo(),
						impresora_barras.getAncho(),
						impresora_barras.getTam_letra(),
						impresora_barras.getMar_izq(),
						impresora_barras.getMar_der(),
						impresora_barras.getMar_sup(),
						impresora_barras.getMar_inf(),
						impresora_barras.getTexto_relleno(),
						impresora_barras.getBarra_relleno(),
						impresora_barras.getEst(),
						impresora_barras.getUsr_act(),
						new java.util.Date(),
						impresora_barras.getId()); 
			return impresora_barras.getId();

		} else {
			// insert
			String sql = "insert into asi_impresora_barras ("
						+ "tipo, "
						+ "ancho, "
						+ "tam_letra, "
						+ "mar_izq, "
						+ "mar_der, "
						+ "mar_sup, "
						+ "mar_inf, "
						+ "texto_relleno, "
						+ "barra_relleno, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				impresora_barras.getTipo(),
				impresora_barras.getAncho(),
				impresora_barras.getTam_letra(),
				impresora_barras.getMar_izq(),
				impresora_barras.getMar_der(),
				impresora_barras.getMar_sup(),
				impresora_barras.getMar_inf(),
				impresora_barras.getTexto_relleno(),
				impresora_barras.getBarra_relleno(),
				impresora_barras.getEst(),
				impresora_barras.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from asi_impresora_barras where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ImpresoraBarras> list() {
		String sql = "select * from asi_impresora_barras";
		
		//logger.info(sql);
		
		List<ImpresoraBarras> listImpresoraBarras = jdbcTemplate.query(sql, new RowMapper<ImpresoraBarras>() {

			@Override
			public ImpresoraBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listImpresoraBarras;
	}

	public ImpresoraBarras get(int id) {
		String sql = "select * from asi_impresora_barras WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ImpresoraBarras>() {

			@Override
			public ImpresoraBarras extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ImpresoraBarras getFull(int id, String tablas[]) {
		String sql = "select aib.id aib_id, aib.tipo aib_tipo , aib.ancho aib_ancho , aib.tam_letra aib_tam_letra , aib.mar_izq aib_mar_izq , aib.mar_der aib_mar_der , aib.mar_sup aib_mar_sup , aib.mar_inf aib_mar_inf , aib.texto_relleno aib_texto_relleno , aib.barra_relleno aib_barra_relleno  ,aib.est aib_est ";
	
		sql = sql + " from asi_impresora_barras aib "; 
		sql = sql + " where aib.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ImpresoraBarras>() {
		
			@Override
			public ImpresoraBarras extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ImpresoraBarras impresorabarras= rsToEntity(rs,"aib_");
							return impresorabarras;
				}
				
				return null;
			}
			
		});


	}		
	
	public ImpresoraBarras getByParams(Param param) {

		String sql = "select * from asi_impresora_barras " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ImpresoraBarras>() {
			@Override
			public ImpresoraBarras extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ImpresoraBarras> listByParams(Param param, String[] order) {

		String sql = "select * from asi_impresora_barras " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ImpresoraBarras>() {

			@Override
			public ImpresoraBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ImpresoraBarras> listFullByParams(ImpresoraBarras impresorabarras, String[] order) {
	
		return listFullByParams(Param.toParam("aib",impresorabarras), order);
	
	}	
	
	public List<ImpresoraBarras> listFullByParams(Param param, String[] order) {

		String sql = "select aib.id aib_id, aib.tipo aib_tipo , aib.ancho aib_ancho , aib.tam_letra aib_tam_letra , aib.mar_izq aib_mar_izq , aib.mar_der aib_mar_der , aib.mar_sup aib_mar_sup , aib.mar_inf aib_mar_inf , aib.texto_relleno aib_texto_relleno , aib.barra_relleno aib_barra_relleno  ,aib.est aib_est ";
		sql = sql + " from asi_impresora_barras aib";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ImpresoraBarras>() {

			@Override
			public ImpresoraBarras mapRow(ResultSet rs, int rowNum) throws SQLException {
				ImpresoraBarras impresorabarras= rsToEntity(rs,"aib_");
				return impresorabarras;
			}

		});

	}	




	// funciones privadas utilitarias para ImpresoraBarras

	private ImpresoraBarras rsToEntity(ResultSet rs,String alias) throws SQLException {
		ImpresoraBarras impresora_barras = new ImpresoraBarras();

		impresora_barras.setId(rs.getInt( alias + "id"));
		impresora_barras.setTipo(rs.getString( alias + "tipo"));
		impresora_barras.setAncho(rs.getBigDecimal( alias + "ancho"));
		impresora_barras.setTam_letra(rs.getBigDecimal( alias + "tam_letra"));
		impresora_barras.setMar_izq(rs.getBigDecimal( alias + "mar_izq"));
		impresora_barras.setMar_der(rs.getBigDecimal( alias + "mar_der"));
		impresora_barras.setMar_sup(rs.getBigDecimal( alias + "mar_sup"));
		impresora_barras.setMar_inf(rs.getBigDecimal( alias + "mar_inf"));
		impresora_barras.setTexto_relleno(rs.getBigDecimal( alias + "texto_relleno"));
		impresora_barras.setBarra_relleno(rs.getBigDecimal( alias + "barra_relleno"));
		impresora_barras.setEst(rs.getString( alias + "est"));
								
		return impresora_barras;

	}
	
}
