package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.ModalidadEstudio;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Servicio;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.Turno;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ConfMensualidadDAO.
 * @author MV
 *
 */
public class ConfMensualidadDAOImpl{
	final static Logger logger = Logger.getLogger(ConfMensualidadDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ConfMensualidad conf_mensualidad) {
		if (conf_mensualidad.getId() != null) {
			// update
			String sql = "UPDATE mat_conf_mensualidad "
						+ "SET id_per=?, "
						+ "id_cct=?, "
						+ "id_cme=?, "
						+ "tipo_fec_ven=?, "
						+ "mes=?, "
						+ "monto=?, "
						+ "descuento=?, "
						+ "desc_banco=?, "
						+ "desc_hermano=?, "
						+ "dia_mora=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						conf_mensualidad.getId_per(),
						conf_mensualidad.getId_cct(),
						conf_mensualidad.getId_cme(),
						conf_mensualidad.getTipo_fec_ven(),
						conf_mensualidad.getMes(),
						conf_mensualidad.getMonto(),
						conf_mensualidad.getDescuento(),
						conf_mensualidad.getDesc_banco(),
						conf_mensualidad.getDesc_hermano(),
						conf_mensualidad.getDia_mora(),
						conf_mensualidad.getEst(),
						conf_mensualidad.getUsr_act(),
						new java.util.Date(),
						conf_mensualidad.getId()); 
			return conf_mensualidad.getId();

		} else {
			// insert
			String sql = "insert into mat_conf_mensualidad ("
						+ "id_per, "
						+ "id_cct, "
						+ "id_cme, "
						+ "tipo_fec_ven, "
						+ "mes, "
						+ "monto, "
						+ "descuento, "
						+ "desc_banco, "
						+ "desc_hermano, "
						+ "dia_mora, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				conf_mensualidad.getId_per(),
				conf_mensualidad.getId_cct(),
				conf_mensualidad.getId_cme(),
				conf_mensualidad.getTipo_fec_ven(),
				conf_mensualidad.getMes(),
				conf_mensualidad.getMonto(),
				conf_mensualidad.getDescuento(),
				conf_mensualidad.getDesc_banco(),
				conf_mensualidad.getDesc_hermano(),
				conf_mensualidad.getDia_mora(),
				conf_mensualidad.getEst(),
				conf_mensualidad.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_conf_mensualidad where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ConfMensualidad> list() {
		String sql = "select * from mat_conf_mensualidad";
		
		//logger.info(sql);
		
		List<ConfMensualidad> listConfMensualidad = jdbcTemplate.query(sql, new RowMapper<ConfMensualidad>() {

			@Override
			public ConfMensualidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfMensualidad;
	}
	
	public List<ConfMensualidad> list(ConfMensualidad confMensualidad) {
		String sql = "select * from mat_conf_mensualidad";
		
		//logger.info(sql);
		
		List<ConfMensualidad> listConfMensualidad = jdbcTemplate.query(sql, new RowMapper<ConfMensualidad>() {

			@Override
			public ConfMensualidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listConfMensualidad;
	}	

	public ConfMensualidad get(int id) {
		String sql = "select * from mat_conf_mensualidad WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfMensualidad>() {

			@Override
			public ConfMensualidad extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ConfMensualidad getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select men.id men_id, men.id_per men_id_per, men.id_cct men_id_cct,  men.id_cme men_id_cme , men.mes men_mes , men.monto men_monto , men.descuento men_descuento , men.desc_banco men_desc_banco, men.desc_hermano men_desc_hermano , men.dia_mora men_dia_mora  ,men.est men_est, men.tipo_fec_ven men_tipo_fec_ven";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
	
		sql = sql + " from mat_conf_mensualidad men "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = men.id_per ";
		sql = sql + " where men.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfMensualidad>() {
		
			@Override
			public ConfMensualidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ConfMensualidad confmensualidad= rsToEntity(rs,"men_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							confmensualidad.setPeriodo(periodo);
					}
							return confmensualidad;
				}
				
				return null;
			}
			
		});


	}		
	
	public ConfMensualidad getByParams(Param param) {

		String sql = "select * from mat_conf_mensualidad " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ConfMensualidad>() {
			@Override
			public ConfMensualidad extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ConfMensualidad> listByParams(Param param, String[] order) {

		String sql = "select * from mat_conf_mensualidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfMensualidad>() {

			@Override
			public ConfMensualidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ConfMensualidad> listFullByParams(ConfMensualidad confmensualidad, String[] order) {
	
		return listFullByParams(Param.toParam("men",confmensualidad), order);
	
	}	
	
	public List<ConfMensualidad> listFullByParams(Param param, String[] order) {

		String sql = "select men.id men_id, men.id_per men_id_per , men.fec_fin_dic men_fec_fin_dic ,  men.id_cct men_id_cct, men.id_cme men_id_cme , men.mes men_mes , men.monto men_monto , men.descuento men_descuento , men.desc_banco men_desc_banco, men.desc_hermano men_desc_hermano, men.dia_mora men_dia_mora  ,men.est men_est,  men.tipo_fec_ven men_tipo_fec_ven";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_niv pee_id_niv, pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", srv.id srv_id  , srv.nom srv_nom  ";
		sql = sql + ", suc.id suc_id  , suc.nom suc_nom  ";
		sql = sql + ", cme.nom cme_nom  ";
		sql = sql + ", tur.nom tur_nom ";
		sql = sql + ", anio.nom anio_nom  ";
		sql = sql + " from mat_conf_mensualidad men";
		sql = sql + " left join per_periodo pee on pee.id = men.id_per ";
		sql = sql + " left join ges_servicio srv on srv.id = pee.id_srv ";
		sql = sql + " left join ges_sucursal suc on suc.id = srv.id_suc ";
		sql = sql + " left join col_anio anio on anio.id = pee.id_anio ";
		sql = sql + " left join cat_modalidad_estudio cme on cme.id = men.id_cme ";
		sql = sql + " left join col_ciclo_turno cct ON men.id_cct=cct.id ";
		sql = sql + " left join col_turno tur on cct.id_tur=tur.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<ConfMensualidad>() {

			@Override
			public ConfMensualidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfMensualidad confmensualidad= rsToEntity(rs,"men_");
				
				Periodo periodo = new Periodo();  
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
				periodo.setId_niv(rs.getInt("pee_id_niv"));
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				confmensualidad.setPeriodo(periodo);
				ModalidadEstudio modalidadEstudio = new ModalidadEstudio();
				modalidadEstudio.setNom(rs.getString("cme_nom"));
				confmensualidad.setModalidadEstudio(modalidadEstudio);
				Turno turno = new Turno();
				turno.setNom(rs.getString("tur_nom"));
				confmensualidad.setTurno(turno);
				return confmensualidad;
			}

		});

	}	




	// funciones privadas utilitarias para ConfMensualidad

	private ConfMensualidad rsToEntity(ResultSet rs,String alias) throws SQLException {
		ConfMensualidad conf_mensualidad = new ConfMensualidad();

		conf_mensualidad.setId(rs.getInt( alias + "id"));
		conf_mensualidad.setId_per(rs.getInt( alias + "id_per"));
		conf_mensualidad.setId_cct(rs.getInt( alias + "id_cct"));
		conf_mensualidad.setId_cme(rs.getInt( alias + "id_cme"));
		conf_mensualidad.setTipo_fec_ven(rs.getString(alias + "tipo_fec_ven"));
		conf_mensualidad.setMes(rs.getInt( alias + "mes"));
		conf_mensualidad.setMonto(rs.getBigDecimal( alias + "monto"));
		conf_mensualidad.setDescuento(rs.getBigDecimal( alias + "descuento"));
		conf_mensualidad.setDesc_banco(rs.getBigDecimal( alias + "desc_banco"));
		conf_mensualidad.setDesc_hermano(rs.getBigDecimal( alias + "desc_hermano"));
		conf_mensualidad.setDia_mora(rs.getInt( alias + "dia_mora"));
		conf_mensualidad.setEst(rs.getString( alias + "est"));
		conf_mensualidad.setFec_fin_dic(rs.getString(alias + "fec_fin_dic"));
								
		return conf_mensualidad;

	}
	
	public boolean existeMensualidad(Integer id_per){
		
		String sql = "SELECT * FROM mat_conf_mensualidad where id_per="+id_per;
	 	
		List<Map<String,Object>> mensualidad = jdbcTemplate.queryForList(sql);
		
		//logger.info(sql);
		
		return (mensualidad.size()!=0);
	}
	
}
