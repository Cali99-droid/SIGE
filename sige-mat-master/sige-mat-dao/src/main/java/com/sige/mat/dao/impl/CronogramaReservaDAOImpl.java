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
import com.tesla.colegio.model.CronogramaReserva;

import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Nivel;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CronogramaReservaDAO.
 * @author MV
 *
 */
public class CronogramaReservaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CronogramaReserva cronograma_reserva) {
		if (cronograma_reserva.getId() != null) {
			// update
			String sql = "UPDATE mat_cronograma_reserva "
						+ "SET id_anio=?, "
						+ "id_niv=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						cronograma_reserva.getId_anio(),
						cronograma_reserva.getId_niv(),
						cronograma_reserva.getFec_ini(),
						cronograma_reserva.getFec_fin(),
						cronograma_reserva.getEst(),
						cronograma_reserva.getUsr_act(),
						new java.util.Date(),
						cronograma_reserva.getId()); 
			return cronograma_reserva.getId();

		} else {
			// insert
			String sql = "insert into mat_cronograma_reserva ("
						+ "id_anio, "
						+ "id_niv, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				cronograma_reserva.getId_anio(),
				cronograma_reserva.getId_niv(),
				cronograma_reserva.getFec_ini(),
				cronograma_reserva.getFec_fin(),
				cronograma_reserva.getEst(),
				cronograma_reserva.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from mat_cronograma_reserva where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CronogramaReserva> list() {
		String sql = "select * from mat_cronograma_reserva";
		
		
		
		List<CronogramaReserva> listCronogramaReserva = jdbcTemplate.query(sql, new RowMapper<CronogramaReserva>() {

			@Override
			public CronogramaReserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCronogramaReserva;
	}

	public CronogramaReserva get(int id) {
		String sql = "select * from mat_cronograma_reserva WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaReserva>() {

			@Override
			public CronogramaReserva extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CronogramaReserva getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select mcr.id mcr_id, mcr.id_anio mcr_id_anio , mcr.id_niv mcr_id_niv , mcr.fec_ini mcr_fec_ini , mcr.fec_fin mcr_fec_fin  ,mcr.est mcr_est ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
	
		sql = sql + " from mat_cronograma_reserva mcr "; 
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = mcr.id_anio ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = mcr.id_niv ";
		sql = sql + " where mcr.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaReserva>() {
		
			@Override
			public CronogramaReserva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CronogramaReserva cronogramareserva= rsToEntity(rs,"mcr_");
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							cronogramareserva.setAnio(anio);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							cronogramareserva.setNivel(nivel);
					}
							return cronogramareserva;
				}
				
				return null;
			}
			
		});


	}		
	
	public CronogramaReserva getByParams(Param param) {

		String sql = "select * from mat_cronograma_reserva " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CronogramaReserva>() {
			@Override
			public CronogramaReserva extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CronogramaReserva> listByParams(Param param, String[] order) {

		String sql = "select * from mat_cronograma_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CronogramaReserva>() {

			@Override
			public CronogramaReserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CronogramaReserva> listFullByParams(CronogramaReserva cronogramareserva, String[] order) {
	
		return listFullByParams(Param.toParam("mcr",cronogramareserva), order);
	
	}	
	
	public List<CronogramaReserva> listFullByParams(Param param, String[] order) {

		String sql = "select mcr.id mcr_id, mcr.id_anio mcr_id_anio , mcr.id_niv mcr_id_niv , mcr.fec_ini mcr_fec_ini , mcr.fec_fin mcr_fec_fin  ,mcr.est mcr_est ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + " from mat_cronograma_reserva mcr";
		sql = sql + " left join col_anio anio on anio.id = mcr.id_anio ";
		sql = sql + " left join cat_nivel niv on niv.id = mcr.id_niv ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CronogramaReserva>() {

			@Override
			public CronogramaReserva mapRow(ResultSet rs, int rowNum) throws SQLException {
				CronogramaReserva cronogramareserva= rsToEntity(rs,"mcr_");
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				cronogramareserva.setAnio(anio);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				cronogramareserva.setNivel(nivel);
				return cronogramareserva;
			}

		});

	}	




	// funciones privadas utilitarias para CronogramaReserva

	private CronogramaReserva rsToEntity(ResultSet rs,String alias) throws SQLException {
		CronogramaReserva cronograma_reserva = new CronogramaReserva();

		cronograma_reserva.setId(rs.getInt( alias + "id"));
		cronograma_reserva.setId_anio(rs.getInt( alias + "id_anio"));
		cronograma_reserva.setId_niv(rs.getInt( alias + "id_niv"));
		cronograma_reserva.setFec_ini(rs.getDate( alias + "fec_ini"));
		cronograma_reserva.setFec_fin(rs.getDate( alias + "fec_fin"));
		cronograma_reserva.setEst(rs.getString( alias + "est"));
								
		return cronograma_reserva;

	}
	
}
