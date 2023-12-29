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
import com.tesla.colegio.model.ReservaCuota;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.Reserva;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ReservaCuotaDAO.
 * @author MV
 *
 */
public class ReservaCuotaDAOImpl{
	final static Logger logger = Logger.getLogger(ReservaCuotaDAOImpl.class);
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
	public int saveOrUpdate(ReservaCuota reserva_cuota) {
		if (reserva_cuota.getId() != null) {
			// update
			String sql = "UPDATE fac_reserva_cuota "
						+ "SET id_res=?, "
						+ "monto=?, "
						+ "nro_recibo=?, "
						+ "id_fmo=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						reserva_cuota.getId_res(),
						reserva_cuota.getMonto(),
						reserva_cuota.getNro_recibo(),
						reserva_cuota.getId_fmo(),
						reserva_cuota.getEst(),
						reserva_cuota.getUsr_act(),
						new java.util.Date(),
						tokenSeguridad.getId()); 
			return reserva_cuota.getId();

		} else {
			// insert
			String sql = "insert into fac_reserva_cuota ("
						+ "id_res, "
						+ "monto, "
						+ "nro_recibo, "
						+ "id_fmo, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				reserva_cuota.getId_res(),
				reserva_cuota.getMonto(),
				reserva_cuota.getNro_recibo(),
				reserva_cuota.getId_fmo(),
				reserva_cuota.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from fac_reserva_cuota where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ReservaCuota> list() {
		String sql = "select * from fac_reserva_cuota";
		
		//logger.info(sql);
		
		List<ReservaCuota> listReservaCuota = jdbcTemplate.query(sql, new RowMapper<ReservaCuota>() {

			@Override
			public ReservaCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listReservaCuota;
	}

	public ReservaCuota get(int id) {
		String sql = "select * from fac_reserva_cuota WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ReservaCuota>() {

			@Override
			public ReservaCuota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ReservaCuota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select rco.id rco_id, rco.id_res rco_id_res , rco.monto rco_monto  , rco.id_fmo rco_id_fmo , rco.nro_recibo rco_nro_recibo  ,rco.est rco_est ";
		if (aTablas.contains("mat_reserva"))
			sql = sql + ", mat_res.id mat_res_id  , mat_res.id_alu mat_res_id_alu , mat_res.id_au mat_res_id_au , mat_res.id_gra mat_res_id_gra , mat_res.id_niv mat_res_id_niv , mat_res.id_con mat_res_id_con , mat_res.id_cli mat_res_id_cli , mat_res.id_per mat_res_id_per , mat_res.id_fam mat_res_id_fam , mat_res.monto mat_res_monto , mat_res.nro_recibo mat_res_nro_recibo , mat_res.fec mat_res_fec , mat_res.fec_lim mat_res_fec_lim  ";
	
		sql = sql + " from fac_reserva_cuota rco "; 
		if (aTablas.contains("mat_reserva"))
			sql = sql + " left join mat_reserva mat_res on mat_res.id = rco.id_res ";
		sql = sql + " where rco.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ReservaCuota>() {
		
			@Override
			public ReservaCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ReservaCuota reservacuota= rsToEntity(rs,"rco_");
					if (aTablas.contains("mat_reserva")){
						Reserva reserva = new Reserva();  
							reserva.setId(rs.getInt("mat_res_id")) ;  
							reserva.setId_alu(rs.getInt("mat_res_id_alu")) ;  
							reserva.setId_au(rs.getInt("mat_res_id_au")) ;  
							reserva.setId_gra(rs.getInt("mat_res_id_gra")) ;  
							reserva.setId_niv(rs.getInt("mat_res_id_niv")) ;  
							reserva.setId_con(rs.getInt("mat_res_id_con")) ;  
							reserva.setId_cli(rs.getInt("mat_res_id_cli")) ;  
							reserva.setId_per(rs.getInt("mat_res_id_per")) ;  
							reserva.setId_fam(rs.getInt("mat_res_id_fam")) ;  
							reserva.setFec(rs.getDate("mat_res_fec")) ;  
							reserva.setFec_lim(rs.getDate("mat_res_fec_lim")) ;  
							reservacuota.setReserva(reserva);
					}
							return reservacuota;
				}
				
				return null;
			}
			
		});


	}		
	
	public ReservaCuota getByParams(Param param) {

		String sql = "select * from fac_reserva_cuota " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ReservaCuota>() {
			@Override
			public ReservaCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ReservaCuota> listByParams(Param param, String[] order) {

		String sql = "select * from fac_reserva_cuota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ReservaCuota>() {

			@Override
			public ReservaCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ReservaCuota> listFullByParams(ReservaCuota reservacuota, String[] order) {
	
		return listFullByParams(Param.toParam("rco",reservacuota), order);
	
	}	
	
	public List<ReservaCuota> listFullByParams(Param param, String[] order) {

		String sql = "select rco.id rco_id, rco.id_res rco_id_res , rco.monto rco_monto , rco.nro_recibo rco_nro_recibo  ,rco.id_fmo rco_id_fmo  ,rco.est rco_est ";
		sql = sql + ", mat_res.id mat_res_id  , mat_res.id_alu mat_res_id_alu , mat_res.id_au mat_res_id_au , mat_res.id_gra mat_res_id_gra , mat_res.id_niv mat_res_id_niv , mat_res.id_con mat_res_id_con , mat_res.id_cli mat_res_id_cli , mat_res.id_per mat_res_id_per , mat_res.id_fam mat_res_id_fam , mat_res.monto mat_res_monto , mat_res.nro_recibo mat_res_nro_recibo , mat_res.fec mat_res_fec , mat_res.fec_lim mat_res_fec_lim  ";
		sql = sql + " from fac_reserva_cuota rco";
		sql = sql + " left join mat_reserva mat_res on mat_res.id = rco.id_res ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ReservaCuota>() {

			@Override
			public ReservaCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReservaCuota reservacuota= rsToEntity(rs,"rco_");
				Reserva reserva = new Reserva();  
				reserva.setId(rs.getInt("mat_res_id")) ;  
				reserva.setId_alu(rs.getInt("mat_res_id_alu")) ;  
				reserva.setId_au(rs.getInt("mat_res_id_au")) ;  
				reserva.setId_gra(rs.getInt("mat_res_id_gra")) ;  
				reserva.setId_niv(rs.getInt("mat_res_id_niv")) ;  
				reserva.setId_con(rs.getInt("mat_res_id_con")) ;  
				reserva.setId_cli(rs.getInt("mat_res_id_cli")) ;  
				reserva.setId_per(rs.getInt("mat_res_id_per")) ;  
				reserva.setId_fam(rs.getInt("mat_res_id_fam")) ;  
				reserva.setFec(rs.getDate("mat_res_fec")) ;  
				reserva.setFec_lim(rs.getDate("mat_res_fec_lim")) ;  
				reservacuota.setReserva(reserva);
				return reservacuota;
			}

		});

	}	




	// funciones privadas utilitarias para ReservaCuota

	private ReservaCuota rsToEntity(ResultSet rs,String alias) throws SQLException {
		ReservaCuota reserva_cuota = new ReservaCuota();

		reserva_cuota.setId(rs.getInt( alias + "id"));
		reserva_cuota.setId_res(rs.getInt( alias + "id_res"));
		reserva_cuota.setMonto(rs.getBigDecimal( alias + "monto"));
		reserva_cuota.setNro_recibo(rs.getString( alias + "nro_recibo"));
		reserva_cuota.setId_fmo(rs.getInt( alias + "id_fmo"));
		reserva_cuota.setEst(rs.getString( alias + "est"));
								
		return reserva_cuota;

	}
	
}
