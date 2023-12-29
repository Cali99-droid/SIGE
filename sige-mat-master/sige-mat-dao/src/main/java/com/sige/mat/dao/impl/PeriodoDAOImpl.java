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
import com.tesla.colegio.model.Periodo;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.TipPeriodo;
import com.tesla.colegio.model.EvaluacionVac;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PagoProgramacion;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PeriodoDAO.
 * @author MV
 *
 */
public class PeriodoDAOImpl{
	final static Logger logger = Logger.getLogger(PeriodoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Periodo periodo) {
		if (periodo.getId() != null) {
			// update
			String sql = "UPDATE per_periodo "
						+ "SET id_anio=?, "
						+ "id_srv=?, "
						+ "id_niv=?, "
						+ "id_suc=?, "
						+ "flag_sit=?, "
						+ "id_tpe=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "fec_cie_mat=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			 jdbcTemplate.update(sql, 
						periodo.getId_anio(),
						periodo.getId_srv(),
						periodo.getId_niv(),
						periodo.getId_suc(),
						periodo.getFlag_sit(),
						periodo.getId_tpe(),
						periodo.getFec_ini(),
						periodo.getFec_fin(),
						periodo.getFec_cie_mat(),
						periodo.getEst(),
						periodo.getUsr_act(),
						new java.util.Date(),
						periodo.getId()); 
			return periodo.getId();

		} else {
			// insert
			String sql = "insert into per_periodo ("
						+ "id_anio, "
						+ "id_srv, "
						+ "id_niv, "
						+ "id_suc, "
						+ "id_tpe, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "fec_cie_mat, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				periodo.getId_anio(),
				periodo.getId_srv(),
				periodo.getId_niv(),
				periodo.getId_suc(),
				periodo.getId_tpe(),
				periodo.getFec_ini(),
				periodo.getFec_fin(),
				periodo.getFec_cie_mat(),
				periodo.getEst(),
				periodo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from per_periodo where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Periodo> list() {
		String sql = "select * from per_periodo";
		
		//logger.info(sql);
		
		List<Periodo> listPeriodo = jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPeriodo;
	}

	
	public Periodo get(int id) {
		String sql = "select p.*, a.nom as anio_nom from per_periodo p inner join col_anio a on a.id=p.id_anio WHERE p.id=" + id + "";
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Periodo>() {

			
			public Periodo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					
					Periodo periodo = rsToEntity(rs,"");
					Anio anio = new Anio();  
					anio.setNom(rs.getString("anio_nom")) ;  
					periodo.setAnio(anio);
					
					return periodo;
				}
				
				return null;
			}
			
		});
	}

	
	public Periodo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select pee.id pee_id, pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_niv pee_id_niv, pee.id_suc pee_id_suc , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ,pee.est pee_est ,pee.flag_sit pee_flag_sit, pee.flag_usu_alu pee_flag_usu_alu ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("ges_servicio"))
			sql = sql + ", srv.id srv_id  , srv.id_suc srv_id_suc , srv.nom srv_nom  ";
		if (aTablas.contains("cat_tip_periodo"))
			sql = sql + ", tpe.id tpe_id  , tpe.nom tpe_nom , tpe.des tpe_des  ";
	
		sql = sql + " from per_periodo pee "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = pee.id_anio ";
		if (aTablas.contains("ges_servicio"))
			sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		if (aTablas.contains("cat_tip_periodo"))
			sql = sql + " left join cat_tip_periodo tpe on tpe.id = pee.id_tpe ";
		sql = sql + " where pee.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Periodo>() {
		
			
			public Periodo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Periodo periodo= rsToEntity(rs,"pee_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							periodo.setAnio(anio);
					}
					if (aTablas.contains("ges_servicio")){
						Servicio servicio = new Servicio();  
							servicio.setId(rs.getInt("srv_id")) ;  
							servicio.setId_suc(rs.getInt("srv_id_suc")) ;  
							servicio.setNom(rs.getString("srv_nom")) ;  
							periodo.setServicio(servicio);
					}
					if (aTablas.contains("cat_tip_periodo")){
						TipPeriodo tipperiodo = new TipPeriodo();  
							tipperiodo.setId(rs.getInt("tpe_id")) ;  
							tipperiodo.setNom(rs.getString("tpe_nom")) ;  
							tipperiodo.setDes(rs.getString("tpe_des")) ;  
							periodo.setTipPeriodo(tipperiodo);
					}
							return periodo;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Periodo getByParams(Param param) {

		String sql = "select * from per_periodo " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Periodo>() {
			
			public Periodo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Periodo> listByParams(Param param, String[] order) {

		String sql = "select * from per_periodo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<Periodo> listFullByParams(Periodo periodo, String[] order) {
	
		return listFullByParams(Param.toParam("pee",periodo), order);
	
	}	
	
	
	public List<Periodo> listFullByParams(Param param, String[] order) {

		String sql = "select pee.id pee_id, pee.id_anio pee_id_anio , pee.id_srv pee_id_srv  , pee.id_niv pee_id_niv , CONCAT(tpe.nom,' ',suc.nom,' ', niv.nom) pee_value, pee.id_suc pee_id_suc , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ,pee.est pee_est  ,pee.flag_sit pee_flag_sit, pee.flag_usu_alu pee_flag_usu_alu ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", srv.id srv_id  , srv.id_suc srv_id_suc , srv.nom srv_nom, srv.id_niv  ";
		sql = sql + ", ggn.id ggn_id , ggn.nom ggn_nom ";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + ", tpe.id tpe_id  , tpe.nom tpe_nom , tpe.des tpe_des  ";
		sql = sql + " from per_periodo pee";
		sql = sql + " left join col_anio anio on anio.id = pee.id_anio ";
		sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		sql = sql + " left join cat_nivel niv on pee.id_niv=niv.id ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";
		sql = sql + " left join cat_tip_periodo tpe on tpe.id = pee.id_tpe ";
		sql = sql + " left join ges_giro_negocio ggn on srv.id_gir=ggn.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Periodo>() {

			
			public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Periodo periodo= rsToEntityCombo(rs,"pee_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				periodo.setAnio(anio);
				Servicio servicio = new Servicio();  
				servicio.setId(rs.getInt("srv_id")) ;  
				servicio.setId_suc(rs.getInt("srv_id_suc")) ;  
				servicio.setNom(rs.getString("srv_nom")) ;
				
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id"));
				sucursal.setNom(rs.getString("suc_nom"));
				servicio.setSucursal(sucursal);
				periodo.setServicio(servicio);
				
				Nivel nivel = new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				nivel.setNom(rs.getString("niv_nom"));
				periodo.setNivel(nivel);	
				
				GiroNegocio giroNegocio = new GiroNegocio();
				giroNegocio.setId(rs.getInt("ggn_id"));
				giroNegocio.setNom(rs.getString("ggn_nom"));
				periodo.setGiroNegocio(giroNegocio);
				
				TipPeriodo tipperiodo = new TipPeriodo();  
				tipperiodo.setId(rs.getInt("tpe_id")) ;  
				tipperiodo.setNom(rs.getString("tpe_nom")) ;  
				tipperiodo.setDes(rs.getString("tpe_des")) ;  
				periodo.setTipPeriodo(tipperiodo);
				return periodo;
			}

		});

	}	


	public List<EvaluacionVac> getListEvaluacionVac(Param param, String[] order) {
		String sql = "select * from eva_evaluacion_vac " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<EvaluacionVac>() {

			
			public EvaluacionVac mapRow(ResultSet rs, int rowNum) throws SQLException {
				EvaluacionVac evaluacion_vac = new EvaluacionVac();

				evaluacion_vac.setId(rs.getInt("id"));
				evaluacion_vac.setId_per(rs.getInt("id_per"));
				evaluacion_vac.setDes(rs.getString("des"));
				evaluacion_vac.setPrecio(rs.getBigDecimal("precio"));
				evaluacion_vac.setEst(rs.getString("est"));
												
				return evaluacion_vac;
			}

		});	
	}
	public List<Aula> getListAula(Param param, String[] order) {
		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Aula aula = new Aula();

				aula.setId(rs.getInt("id"));
				aula.setId_per(rs.getInt("id_per"));
				aula.setId_grad(rs.getInt("id_grad"));
				aula.setId_secc_ant(rs.getInt("id_secc_ant"));
				aula.setSecc(rs.getString("secc"));
				aula.setCap(rs.getInt("cap"));
				//aula.setTur(rs.getString("tur"));
				aula.setEst(rs.getString("est"));
												
				return aula;
			}

		});	
	}
	public List<Reserva> getListReserva(Param param, String[] order) {
		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			
			public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				Reserva reserva = new Reserva();

				reserva.setId(rs.getInt("id"));
				reserva.setId_alu(rs.getInt("id_alu"));
				reserva.setId_au(rs.getInt("id_au"));
				reserva.setId_gra(rs.getInt("id_gra"));
				reserva.setId_niv(rs.getInt("id_niv"));
				reserva.setId_con(rs.getInt("id_con"));
				reserva.setId_cli(rs.getInt("id_cli"));
				reserva.setId_per(rs.getInt("id_per"));
				reserva.setId_fam(rs.getInt("id_fam"));
				reserva.setFec(rs.getDate("fec"));
				reserva.setFec_lim(rs.getDate("fec_lim"));
				reserva.setEst(rs.getString("est"));
												
				return reserva;
			}

		});	
	}
	public List<Matricula> getListMatricula(Param param, String[] order) {
		String sql = "select * from mat_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			
			public Matricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Matricula matricula = new Matricula();

				matricula.setId(rs.getInt("id"));
				matricula.setId_alu(rs.getInt("id_alu"));
				matricula.setId_fam(rs.getInt("id_fam"));
				matricula.setId_enc(rs.getInt("id_enc"));
				matricula.setId_con(rs.getInt("id_con"));
				matricula.setId_cli(rs.getInt("id_cli"));
				matricula.setId_per(rs.getInt("id_per"));
				matricula.setId_au(rs.getInt("id_au"));
				matricula.setId_gra(rs.getInt("id_gra"));
				matricula.setId_niv(rs.getInt("id_niv"));
				matricula.setFecha(rs.getDate("fecha"));
				matricula.setCar_pod(rs.getString("car_pod"));
				matricula.setNum_cont(rs.getString("num_cont"));
				matricula.setObs(rs.getString("obs"));
				matricula.setEst(rs.getString("est"));
												
				return matricula;
			}

		});	
	}
	public List<PagoProgramacion> getListPagoProgramacion(Param param, String[] order) {
		String sql = "select * from pag_pago_programacion " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PagoProgramacion>() {

			
			public PagoProgramacion mapRow(ResultSet rs, int rowNum) throws SQLException {
				PagoProgramacion pago_programacion = new PagoProgramacion();

				pago_programacion.setId(rs.getInt("id"));
				pago_programacion.setId_cpa(rs.getInt("id_cpa"));
				pago_programacion.setId_per(rs.getInt("id_per"));
				pago_programacion.setMonto(rs.getBigDecimal("monto"));
				pago_programacion.setMes(rs.getString("mes"));
				pago_programacion.setFec(rs.getDate("fec"));
				pago_programacion.setEst(rs.getString("est"));
												
				return pago_programacion;
			}

		});	
	}


	// funciones privadas utilitarias para Periodo

	private Periodo rsToEntity(ResultSet rs,String alias) throws SQLException {
		Periodo periodo = new Periodo();

		periodo.setId(rs.getInt( alias + "id"));
		periodo.setId_anio(rs.getInt( alias + "id_anio"));
		periodo.setId_srv(rs.getInt( alias + "id_srv"));
		periodo.setId_niv(rs.getInt( alias + "id_niv"));
		periodo.setId_suc(rs.getInt( alias + "id_suc"));
		periodo.setFlag_sit(rs.getString( alias + "flag_sit"));
		periodo.setFlag_usu_alu(rs.getString( alias + "flag_usu_alu"));
		periodo.setId_tpe(rs.getInt( alias + "id_tpe"));
		periodo.setFec_ini(rs.getDate( alias + "fec_ini"));
		periodo.setFec_fin(rs.getDate( alias + "fec_fin"));
		periodo.setFec_cie_mat(rs.getDate( alias + "fec_cie_mat"));
		periodo.setEst(rs.getString( alias + "est"));
								
		return periodo;

	}
	
	private Periodo rsToEntityCombo(ResultSet rs,String alias) throws SQLException {
		Periodo periodo = new Periodo();

		periodo.setId(rs.getInt( alias + "id"));
		periodo.setId_anio(rs.getInt( alias + "id_anio"));
		periodo.setId_srv(rs.getInt( alias + "id_srv"));
		periodo.setId_niv(rs.getInt( alias + "id_niv"));
		periodo.setId_suc(rs.getInt( alias + "id_suc"));
		periodo.setFlag_sit(rs.getString( alias + "flag_sit"));
		periodo.setFlag_usu_alu(rs.getString( alias + "flag_usu_alu"));
		periodo.setId_tpe(rs.getInt( alias + "id_tpe"));
		periodo.setFec_ini(rs.getDate( alias + "fec_ini"));
		periodo.setFec_fin(rs.getDate( alias + "fec_fin"));
		periodo.setFec_cie_mat(rs.getDate( alias + "fec_cie_mat"));
		periodo.setEst(rs.getString( alias + "est"));
		if(!rs.getString( alias + "value").equals(""))
			periodo.setValue(rs.getString( alias + "value"));
								
		return periodo;

	}
	
}
