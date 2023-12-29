package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.ModalidadEstudio;

import com.tesla.colegio.model.ConfMensualidad;
import com.tesla.colegio.model.ConfCuota;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.AulaModalidadDet;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ModalidadEstudioDAO.
 * @author MV
 *
 */
public class ModalidadEstudioDAOImpl{
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
	public int saveOrUpdate(ModalidadEstudio modalidad_estudio) {
		if (modalidad_estudio.getId() != null) {
			// update
			String sql = "UPDATE cat_modalidad_estudio "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						modalidad_estudio.getNom(),
						modalidad_estudio.getDes(),
						modalidad_estudio.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						modalidad_estudio.getId()); 
			return modalidad_estudio.getId();

		} else {
			// insert
			String sql = "insert into cat_modalidad_estudio ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				modalidad_estudio.getNom(),
				modalidad_estudio.getDes(),
				modalidad_estudio.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_modalidad_estudio where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ModalidadEstudio> list() {
		String sql = "select * from cat_modalidad_estudio";
		
		System.out.println(sql);
		
		List<ModalidadEstudio> listModalidadEstudio = jdbcTemplate.query(sql, new RowMapper<ModalidadEstudio>() {

			@Override
			public ModalidadEstudio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listModalidadEstudio;
	}

	public ModalidadEstudio get(int id) {
		String sql = "select * from cat_modalidad_estudio WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadEstudio>() {

			@Override
			public ModalidadEstudio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ModalidadEstudio getFull(int id, String tablas[]) {
		String sql = "select cme.id cme_id, cme.nom cme_nom , cme.des cme_des  ,cme.est cme_est ";
	
		sql = sql + " from cat_modalidad_estudio cme "; 
		sql = sql + " where cme.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadEstudio>() {
		
			@Override
			public ModalidadEstudio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ModalidadEstudio modalidadestudio= rsToEntity(rs,"cme_");
							return modalidadestudio;
				}
				
				return null;
			}
			
		});


	}		
	
	public ModalidadEstudio getByParams(Param param) {

		String sql = "select * from cat_modalidad_estudio " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ModalidadEstudio>() {
			@Override
			public ModalidadEstudio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ModalidadEstudio> listByParams(Param param, String[] order) {

		String sql = "select * from cat_modalidad_estudio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ModalidadEstudio>() {

			@Override
			public ModalidadEstudio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ModalidadEstudio> listFullByParams(ModalidadEstudio modalidadestudio, String[] order) {
	
		return listFullByParams(Param.toParam("cme",modalidadestudio), order);
	
	}	
	
	public List<ModalidadEstudio> listFullByParams(Param param, String[] order) {

		String sql = "select cme.id cme_id, cme.nom cme_nom , cme.des cme_des  ,cme.est cme_est ";
		sql = sql + " from cat_modalidad_estudio cme";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ModalidadEstudio>() {

			@Override
			public ModalidadEstudio mapRow(ResultSet rs, int rowNum) throws SQLException {
				ModalidadEstudio modalidadestudio= rsToEntity(rs,"cme_");
				return modalidadestudio;
			}

		});

	}	


	public List<ConfMensualidad> getListConfMensualidad(Param param, String[] order) {
		String sql = "select * from mat_conf_mensualidad " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<ConfMensualidad>() {

			@Override
			public ConfMensualidad mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfMensualidad conf_mensualidad = new ConfMensualidad();

				conf_mensualidad.setId(rs.getInt("id"));
				conf_mensualidad.setId_per(rs.getInt("id_per"));
				//conf_mensualidad.setId_cme(rs.getInt("id_cme"));
				conf_mensualidad.setTipo_fec_ven(rs.getString("tipo_fec_ven"));
				conf_mensualidad.setMes(rs.getInt("mes"));
				conf_mensualidad.setMonto(rs.getBigDecimal("monto"));
				conf_mensualidad.setDescuento(rs.getBigDecimal("descuento"));
				//conf_mensualidad.setDes_hermano(rs.getBigDecimal("des_hermano"));
				//conf_mensualidad.setDes_banco(rs.getBigDecimal("des_banco"));
				conf_mensualidad.setDia_mora(rs.getInt("dia_mora"));
				conf_mensualidad.setEst(rs.getString("est"));
												
				return conf_mensualidad;
			}

		});	
	}
	public List<ConfCuota> getListConfCuota(Param param, String[] order) {
		String sql = "select * from mat_conf_cuota " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<ConfCuota>() {

			@Override
			public ConfCuota mapRow(ResultSet rs, int rowNum) throws SQLException {
				ConfCuota conf_cuota = new ConfCuota();

				conf_cuota.setId(rs.getInt("id"));
				conf_cuota.setId_per(rs.getInt("id_per"));
				//conf_cuota.setId_cme(rs.getInt("id_cme"));
				conf_cuota.setMatricula(rs.getBigDecimal("matricula"));
				conf_cuota.setCuota(rs.getBigDecimal("cuota"));
				conf_cuota.setReserva(rs.getBigDecimal("reserva"));
				conf_cuota.setEst(rs.getString("est"));
												
				return conf_cuota;
			}

		});	
	}
	public List<Aula> getListAula(Param param, String[] order) {
		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			@Override
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Aula aula = new Aula();

				aula.setId(rs.getInt("id"));
				aula.setId_per(rs.getInt("id_per"));
				aula.setId_grad(rs.getInt("id_grad"));
				aula.setId_secc_ant(rs.getInt("id_secc_ant"));
				aula.setId_tur(rs.getInt("id_tur"));
				//aula.setId_cme(rs.getInt("id_cme"));
				aula.setSecc(rs.getString("secc"));
				aula.setCap(rs.getInt("cap"));
				aula.setEst(rs.getString("est"));
												
				return aula;
			}

		});	
	}
	public List<AulaModalidadDet> getListAulaModalidadDet(Param param, String[] order) {
		String sql = "select * from col_aula_modalidad_det " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<AulaModalidadDet>() {

			@Override
			public AulaModalidadDet mapRow(ResultSet rs, int rowNum) throws SQLException {
				AulaModalidadDet aula_modalidad_det = new AulaModalidadDet();

				aula_modalidad_det.setId(rs.getInt("id"));
				aula_modalidad_det.setId_au(rs.getInt("id_au"));
				aula_modalidad_det.setId_cme(rs.getInt("id_cme"));
				aula_modalidad_det.setMes(rs.getInt("mes"));
				aula_modalidad_det.setEst(rs.getString("est"));
												
				return aula_modalidad_det;
			}

		});	
	}


	// funciones privadas utilitarias para ModalidadEstudio

	private ModalidadEstudio rsToEntity(ResultSet rs,String alias) throws SQLException {
		ModalidadEstudio modalidad_estudio = new ModalidadEstudio();

		modalidad_estudio.setId(rs.getInt( alias + "id"));
		modalidad_estudio.setNom(rs.getString( alias + "nom"));
		modalidad_estudio.setDes(rs.getString( alias + "des"));
		modalidad_estudio.setEst(rs.getString( alias + "est"));
								
		return modalidad_estudio;

	}
	
}
