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
import com.tesla.colegio.model.NotaCredito;

import com.tesla.colegio.model.Movimiento;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaCreditoDAO.
 * @author MV
 *
 */
public class NotaCreditoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(NotaCredito nota_credito) {
		if (nota_credito.getId() != null) {
			// update
			String sql = "UPDATE fac_nota_credito "
						+ "SET fec_emi=?, "
						+ "id_fmo_nc=?, "
						+ "id_fmo=?, "
						+ "motivo=?, "
						+ "monto=?, "
						+ "ticket=?, "
						+ "id_eiv=?, "
						+ "code=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						nota_credito.getFec_emi(),
						nota_credito.getId_fmo_nc(),
						nota_credito.getId_fmo(),
						nota_credito.getMotivo(),
						nota_credito.getMonto(),
						nota_credito.getTicket(),
						nota_credito.getId_eiv(),
						nota_credito.getCode(),
						nota_credito.getEst(),
						nota_credito.getUsr_act(),
						new java.util.Date(),
						nota_credito.getId()); 
			return nota_credito.getId();

		} else {
			// insert
			String sql = "insert into fac_nota_credito ("
						+ "fec_emi, "
						+ "id_fmo_nc, "
						+ "id_fmo, "
						+ "motivo, "
						+ "monto, "
						+ "ticket, "
						+ "id_eiv, "
						+ "code, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				nota_credito.getFec_emi(),
				nota_credito.getId_fmo_nc(),
				nota_credito.getId_fmo(),
				nota_credito.getMotivo(),
				nota_credito.getMonto(),
				nota_credito.getTicket(),
				nota_credito.getId_eiv(),
				nota_credito.getCode(),
				nota_credito.getEst(),
				nota_credito.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_nota_credito where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<NotaCredito> list() {
		String sql = "select * from fac_nota_credito";
		
		//System.out.println(sql);
		
		List<NotaCredito> listNotaCredito = jdbcTemplate.query(sql, new RowMapper<NotaCredito>() {

			@Override
			public NotaCredito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaCredito;
	}

	public NotaCredito get(int id) {
		String sql = "select * from fac_nota_credito WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCredito>() {

			@Override
			public NotaCredito extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NotaCredito getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select fnc.id fnc_id, fnc.fec_emi fnc_fec_emi , fnc.id_fmo fnc_id_fmo , fnc.motivo fnc_motivo , fnc.monto fnc_monto , fnc.ticket fnc_ticket , fnc.id_eiv fnc_id_eiv , fnc.code fnc_code  ,fnc.est fnc_est ";
		if (aTablas.contains("fac_movimiento"))
			sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs , fmo.cod_res fmo_cod_res  ";
	
		sql = sql + " from fac_nota_credito fnc "; 
		if (aTablas.contains("fac_movimiento"))
			sql = sql + " left join fac_movimiento fmo on fmo.id = fnc.id_fmo ";
		sql = sql + " where fnc.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCredito>() {
		
			@Override
			public NotaCredito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaCredito notacredito= rsToEntity(rs,"fnc_");
					if (aTablas.contains("fac_movimiento")){
						Movimiento movimiento = new Movimiento();  
							movimiento.setId(rs.getInt("fmo_id")) ;  
							movimiento.setTipo(rs.getString("fmo_tipo")) ;  
							movimiento.setFec(rs.getDate("fmo_fec")) ;  
							movimiento.setId_suc(rs.getInt("fmo_id_suc")) ;  
							movimiento.setId_mat(rs.getInt("fmo_id_mat")) ;  
							movimiento.setMonto(rs.getBigDecimal("fmo_monto")) ;  
							movimiento.setDescuento(rs.getBigDecimal("fmo_descuento")) ;  
							movimiento.setMonto_total(rs.getBigDecimal("fmo_monto_total")) ;  
							movimiento.setNro_rec(rs.getString("fmo_nro_rec")) ;  
							movimiento.setObs(rs.getString("fmo_obs")) ;  
							movimiento.setCod_res(rs.getString("fmo_cod_res")) ;  
							notacredito.setMovimiento(movimiento);
					}
							return notacredito;
				}
				
				return null;
			}
			
		});


	}		
	
	public NotaCredito getByParams(Param param) {

		String sql = "select * from fac_nota_credito " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCredito>() {
			@Override
			public NotaCredito extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NotaCredito> listByParams(Param param, String[] order) {

		String sql = "select * from fac_nota_credito " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaCredito>() {

			@Override
			public NotaCredito mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NotaCredito> listFullByParams(NotaCredito notacredito, String[] order) {
	
		return listFullByParams(Param.toParam("fnc",notacredito), order);
	
	}	
	
	public List<NotaCredito> listFullByParams(Param param, String[] order) {

		String sql = "select fnc.id fnc_id, fnc.fec_emi fnc_fec_emi , fnc.id_fmo fnc_id_fmo , fnc.motivo fnc_motivo , fnc.monto fnc_monto , fnc.ticket fnc_ticket , fnc.id_eiv fnc_id_eiv , fnc.code fnc_code  ,fnc.est fnc_est ";
		sql = sql + ", fmo.id fmo_id  , fmo.tipo fmo_tipo , fmo.fec fmo_fec , fmo.id_suc fmo_id_suc , fmo.id_mat fmo_id_mat , fmo.monto fmo_monto , fmo.descuento fmo_descuento , fmo.monto_total fmo_monto_total , fmo.nro_rec fmo_nro_rec , fmo.obs fmo_obs , fmo.cod_res fmo_cod_res  ";
		sql = sql + " from fac_nota_credito fnc";
		sql = sql + " left join fac_movimiento fmo on fmo.id = fnc.id_fmo ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaCredito>() {

			@Override
			public NotaCredito mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaCredito notacredito= rsToEntity(rs,"fnc_");
				Movimiento movimiento = new Movimiento();  
				movimiento.setId(rs.getInt("fmo_id")) ;  
				movimiento.setTipo(rs.getString("fmo_tipo")) ;  
				movimiento.setFec(rs.getDate("fmo_fec")) ;  
				movimiento.setId_suc(rs.getInt("fmo_id_suc")) ;  
				movimiento.setId_mat(rs.getInt("fmo_id_mat")) ;  
				movimiento.setMonto(rs.getBigDecimal("fmo_monto")) ;  
				movimiento.setDescuento(rs.getBigDecimal("fmo_descuento")) ;  
				movimiento.setMonto_total(rs.getBigDecimal("fmo_monto_total")) ;  
				movimiento.setNro_rec(rs.getString("fmo_nro_rec")) ;  
				movimiento.setObs(rs.getString("fmo_obs")) ;  
				movimiento.setCod_res(rs.getString("fmo_cod_res")) ;  
				notacredito.setMovimiento(movimiento);
				return notacredito;
			}

		});

	}	




	// funciones privadas utilitarias para NotaCredito

	private NotaCredito rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaCredito nota_credito = new NotaCredito();

		nota_credito.setId(rs.getInt( alias + "id"));
		nota_credito.setFec_emi(rs.getDate( alias + "fec_emi"));
		nota_credito.setId_fmo(rs.getInt( alias + "id_fmo"));
		nota_credito.setId_fmo_nc(rs.getInt( alias + "id_fmo_nc"));
		nota_credito.setMotivo(rs.getString( alias + "motivo"));
		nota_credito.setMonto(rs.getBigDecimal( alias + "monto"));
		nota_credito.setTicket(rs.getString( alias + "ticket"));
		nota_credito.setId_eiv(rs.getString( alias + "id_eiv"));
		nota_credito.setCode(rs.getString( alias + "code"));
		nota_credito.setEst(rs.getString( alias + "est"));
								
		return nota_credito;

	}
	
}
