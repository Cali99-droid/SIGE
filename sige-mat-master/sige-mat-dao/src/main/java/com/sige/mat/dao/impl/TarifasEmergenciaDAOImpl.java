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
import com.tesla.colegio.model.TarifasEmergencia;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TarifasEmergenciaDAO.
 * @author MV
 *
 */
public class TarifasEmergenciaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TarifasEmergencia tarifas_emergencia) {
		if (tarifas_emergencia.getId() != null) {
			// update
			String sql = "UPDATE mat_tarifas_emergencia "
						+ "SET id_per=?, "
						+ "exonerado=?, "
						+ "monto=?, "
						+ "descuento=?, "
						+ "des_hermano=?, "
						+ "des_banco=?, "
						+ "mes=?, "
						+ "fec_ven=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						tarifas_emergencia.getId_per(),
						tarifas_emergencia.getExonerado(),
						tarifas_emergencia.getMonto(),
						tarifas_emergencia.getDescuento(),
						tarifas_emergencia.getDes_hermano(),
						tarifas_emergencia.getDes_banco(),
						tarifas_emergencia.getMes(),
						tarifas_emergencia.getFec_ven(),
						tarifas_emergencia.getEst(),
						tarifas_emergencia.getUsr_act(),
						new java.util.Date(),
						tarifas_emergencia.getId()); 
			return tarifas_emergencia.getId();

		} else {
			// insert
			String sql = "insert into mat_tarifas_emergencia ("
						+ "id_per, "
						+ "exonerado, "
						+ "monto, "
						+ "descuento, "
						+ "des_hermano, "
						+ "des_banco, "
						+ "mes, "
						+ "fec_ven, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				tarifas_emergencia.getId_per(),
				tarifas_emergencia.getExonerado(),
				tarifas_emergencia.getMonto(),
				tarifas_emergencia.getDescuento(),
				tarifas_emergencia.getDes_hermano(),
				tarifas_emergencia.getDes_banco(),
				tarifas_emergencia.getMes(),
				tarifas_emergencia.getFec_ven(),
				tarifas_emergencia.getEst(),
				tarifas_emergencia.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_tarifas_emergencia where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TarifasEmergencia> list() {
		String sql = "select * from mat_tarifas_emergencia";
		
		//System.out.println(sql);
		
		List<TarifasEmergencia> listTarifasEmergencia = jdbcTemplate.query(sql, new RowMapper<TarifasEmergencia>() {

			@Override
			public TarifasEmergencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTarifasEmergencia;
	}

	public TarifasEmergencia get(int id) {
		String sql = "select * from mat_tarifas_emergencia WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TarifasEmergencia>() {

			@Override
			public TarifasEmergencia extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TarifasEmergencia getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mte.id mte_id, mte.id_per mte_id_per , mte.exonerado mte_exonerado, mte.monto mte_monto , mte.descuento mte_descuento , mte.des_hermano mte_des_hermano , mte.des_banco mte_des_banco , mte.mes mte_mes, mte.fec_ven mte_fec_ven, mte.procesado mte_procesado  ,mte.est mte_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat , per.flag_sit per_flag_sit  ";
	
		sql = sql + " from mat_tarifas_emergencia mte "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo per on per.id = mte.id_per ";
		sql = sql + " where mte.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TarifasEmergencia>() {
		
			@Override
			public TarifasEmergencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TarifasEmergencia tarifasemergencia= rsToEntity(rs,"mte_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("per_id")) ;  
							periodo.setId_anio(rs.getInt("per_id_anio")) ;  
							periodo.setId_suc(rs.getInt("per_id_suc")) ;  
							periodo.setId_niv(rs.getInt("per_id_niv")) ;  
							periodo.setId_srv(rs.getInt("per_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("per_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("per_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("per_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("per_fec_cie_mat")) ;  
							periodo.setFlag_sit(rs.getString("per_flag_sit")) ;  
							tarifasemergencia.setPeriodo(periodo);
					}
							return tarifasemergencia;
				}
				
				return null;
			}
			
		});


	}		
	
	public TarifasEmergencia getByParams(Param param) {

		String sql = "select * from mat_tarifas_emergencia " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TarifasEmergencia>() {
			@Override
			public TarifasEmergencia extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TarifasEmergencia> listByParams(Param param, String[] order) {

		String sql = "select * from mat_tarifas_emergencia " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TarifasEmergencia>() {

			@Override
			public TarifasEmergencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TarifasEmergencia> listFullByParams(TarifasEmergencia tarifasemergencia, String[] order) {
	
		return listFullByParams(Param.toParam("mte",tarifasemergencia), order);
	
	}	
	
	public List<TarifasEmergencia> listFullByParams(Param param, String[] order) {

		String sql = "select mte.id mte_id, mte.id_per mte_id_per , mte.exonerado mte_exonerado, mte.monto mte_monto , mte.descuento mte_descuento , mte.des_hermano mte_des_hermano , mte.des_banco mte_des_banco , mte.mes mte_mes, mte.fec_ven mte_fec_ven, mte.procesado mte_procesado ,mte.est mte_est ";
		sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat , per.flag_sit per_flag_sit  ";
		sql = sql + ", srv.id srv_id  , srv.nom srv_nom  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + ", anio.nom anio_nom  ";
		sql = sql + " from mat_tarifas_emergencia mte";
		sql = sql + " left join per_periodo per on per.id = mte.id_per ";
		sql = sql + " left join ges_servicio srv on srv.id = per.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";
		sql = sql + " left join col_anio anio on anio.id = per.id_anio ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TarifasEmergencia>() {

			@Override
			public TarifasEmergencia mapRow(ResultSet rs, int rowNum) throws SQLException {
				TarifasEmergencia tarifasemergencia= rsToEntity(rs,"mte_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("per_id")) ;  
				periodo.setId_anio(rs.getInt("per_id_anio")) ;  
				periodo.setId_suc(rs.getInt("per_id_suc")) ;  
				periodo.setId_niv(rs.getInt("per_id_niv")) ;  
				Servicio servicio = new Servicio();
				servicio.setId(rs.getInt("srv_id")) ;
				servicio.setNom(rs.getString("srv_nom"));
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id")) ;
				sucursal.setNom(rs.getString("suc_nom"));
				servicio.setSucursal(sucursal);
				Anio anio = new Anio();
				anio.setNom(rs.getString("anio_nom"));
				periodo.setAnio(anio);
				periodo.setServicio(servicio);
				periodo.setId_srv(rs.getInt("per_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("per_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("per_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("per_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("per_fec_cie_mat")) ;  
				periodo.setFlag_sit(rs.getString("per_flag_sit")) ;  
				/*Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;
				Servicio servicio = new Servicio();
				servicio.setId(rs.getInt("srv_id")) ;
				servicio.setNom(rs.getString("srv_nom"));
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id")) ;
				sucursal.setNom(rs.getString("suc_nom"));
				servicio.setSucursal(sucursal);
				Anio anio = new Anio();
				anio.setNom(rs.getString("anio_nom"));
				periodo.setAnio(anio);
				periodo.setServicio(servicio);
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ; */
				tarifasemergencia.setPeriodo(periodo);
				return tarifasemergencia;
			}

		});

	}	




	// funciones privadas utilitarias para TarifasEmergencia

	private TarifasEmergencia rsToEntity(ResultSet rs,String alias) throws SQLException {
		TarifasEmergencia tarifas_emergencia = new TarifasEmergencia();

		tarifas_emergencia.setId(rs.getInt( alias + "id"));
		tarifas_emergencia.setId_per(rs.getInt( alias + "id_per"));
		tarifas_emergencia.setExonerado(rs.getString( alias + "exonerado"));
		tarifas_emergencia.setMonto(rs.getBigDecimal( alias + "monto"));
		tarifas_emergencia.setDescuento(rs.getBigDecimal( alias + "descuento"));
		tarifas_emergencia.setDes_hermano(rs.getBigDecimal( alias + "des_hermano"));
		tarifas_emergencia.setDes_banco(rs.getBigDecimal( alias + "des_banco"));
		tarifas_emergencia.setMes(rs.getInt( alias + "mes"));
		tarifas_emergencia.setFec_ven(rs.getDate(alias + "fec_ven"));
		tarifas_emergencia.setProcesado(rs.getString( alias + "procesado"));
		tarifas_emergencia.setEst(rs.getString( alias + "est"));
								
		return tarifas_emergencia;

	}
	
}
