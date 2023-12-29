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

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.ModalidadEstudio;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Turno;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfCuotaDAO.
 * @author MV
 *
 */
public class ConfCuotaDAOImpl{
	final static Logger logger = Logger.getLogger(ConfCuotaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfCuota conf_cuota) {
		if (conf_cuota.getId() != null) {
			// update
			String sql = "UPDATE mat_conf_cuota "
						+ "SET id_per=?, "
						+ "id_cct=?, "
						+ "id_cme=?, "
						+ "matricula=?, "
						+ "reserva=?, "
						+ "cuota=?, "
						+ "tip_cuota_ing=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						conf_cuota.getId_per(),
						conf_cuota.getId_cct(),
						conf_cuota.getId_cme(),
						conf_cuota.getMatricula(),
						conf_cuota.getReserva(),
						conf_cuota.getCuota(),
						conf_cuota.getTip_cuota_ing(),
						conf_cuota.getEst(),
						conf_cuota.getUsr_act(),
						new java.util.Date(),
						conf_cuota.getId()); 
			return conf_cuota.getId();

		} else {
			// insert
			String sql = "insert into mat_conf_cuota ("
						+ "id_per, "
						+ "id_cct, "
						+ "id_cme, "
						+ "matricula, "
						+ "reserva, "
						+ "cuota, "
						+ "tip_cuota_ing, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				conf_cuota.getId_per(),
				conf_cuota.getId_cct(),
				conf_cuota.getId_cme(),
				conf_cuota.getMatricula(),
				conf_cuota.getReserva(),
				conf_cuota.getCuota(),
				conf_cuota.getTip_cuota_ing(),
				conf_cuota.getEst(),
				conf_cuota.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_conf_cuota where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfCuota> list() {
		String sql = "select * from mat_conf_cuota";
		
		//logger.info(sql);
		
		List<ConfCuota> listConfCuota = jdbcTemplate.query(sql, new RowMapper<ConfCuota>() {

			@Override
			public ConfCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfCuota;
	}

	public ConfCuota get(int id) {
		String sql = "select * from mat_conf_cuota WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfCuota>() {

			@Override
			public ConfCuota extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfCuota getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select men.id men_id, men.reserva men_reserva, men.id_per men_id_per , men.id_cme men_id_cme , men.matricula men_matricula , men.cuota men_cuota, + men.tip_cuota_ing men_tip_cuota_ing ,men.est men_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_conf_cuota men "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = men.id_per ";
		sql = sql + " where men.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfCuota>() {
		
			@Override
			public ConfCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfCuota confcuota= rsToEntity(rs,"men_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							confcuota.setPeriodo(periodo);
					}
							return confcuota;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfCuota getByParams(Param param) {

		String sql = "select * from mat_conf_cuota " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfCuota>() {
			@Override
			public ConfCuota extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfCuota> listByParams(Param param, String[] order) {

		String sql = "select * from mat_conf_cuota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfCuota>() {

			@Override
			public ConfCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfCuota> listFullByParams(ConfCuota confcuota, String[] order) {
	
		return listFullByParams(Param.toParam("men",confcuota), order);
	
	}	
	
	public List<ConfCuota> listFullByParams(Param param, String[] order) {

		String sql = "select men.id men_id, men.reserva men_reserva, men.id_per men_id_per , men.id_cct men_id_cct, men.id_cme men_id_cme , men.matricula men_matricula , men.cuota men_cuota, men.tip_cuota_ing men_tip_cuota_ing ,men.est men_est ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", ani.nom anio_nom";
		sql = sql + ", cme.nom cme_nom";
		sql = sql + ", s.nom srv_nom ";
		sql = sql + ", pee.id_tpe , s.id_niv ";
		sql = sql + ", suc.nom suc_nom ";
		sql = sql + ", tur.nom tur_nom ";
		sql = sql + " from mat_conf_cuota men";
		sql = sql + " left join per_periodo pee on pee.id = men.id_per ";
		sql = sql + " left join col_anio ani on ani.id = pee.id_anio ";
		sql = sql + " left join ges_servicio s on s.id = pee.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = pee.id_suc ";
		sql = sql + " left join cat_modalidad_estudio cme on cme.id = men.id_cme ";
		sql = sql + " left join col_ciclo_turno cct ON men.id_cct=cct.id ";
		sql = sql + " left join col_turno tur on cct.id_tur=tur.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfCuota>() {

			@Override
			public ConfCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfCuota confcuota= rsToEntity(rs,"men_");
				
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				
				Anio anio = new Anio();
				anio.setNom(rs.getString("anio_nom"));
				periodo.setAnio(anio);
				
				Servicio servicio = new Servicio();
				servicio.setNom(rs.getString("srv_nom"));
				periodo.setServicio(servicio);
				
				Sucursal sucursal = new Sucursal();
				sucursal.setNom(rs.getString("suc_nom"));
				periodo.setSucursal(sucursal);
				
				ModalidadEstudio modalidadEstudio = new ModalidadEstudio();
				modalidadEstudio.setNom(rs.getString("cme_nom"));
				confcuota.setModalidadEstudio(modalidadEstudio);
				
				Turno turno = new Turno();
				turno.setNom(rs.getString("tur_nom"));
				confcuota.setTurno(turno);
				confcuota.setPeriodo(periodo);
				return confcuota;
			}

		});

	}	




	// funciones privadas utilitarias para ConfCuota

	private ConfCuota rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfCuota conf_cuota = new ConfCuota();

		conf_cuota.setId(rs.getInt( alias + "id"));
		conf_cuota.setId_per(rs.getInt( alias + "id_per"));
		conf_cuota.setId_cct(rs.getInt( alias + "id_cct"));
		conf_cuota.setId_cme(rs.getInt( alias + "id_cme"));
		conf_cuota.setMatricula(rs.getBigDecimal( alias + "matricula"));
		conf_cuota.setReserva(rs.getBigDecimal( alias + "reserva"));
		conf_cuota.setCuota(rs.getBigDecimal( alias + "cuota"));
		conf_cuota.setTip_cuota_ing(rs.getString(alias + "tip_cuota_ing"));
		conf_cuota.setEst(rs.getString( alias + "est"));
								
		return conf_cuota;

	}
	
}
