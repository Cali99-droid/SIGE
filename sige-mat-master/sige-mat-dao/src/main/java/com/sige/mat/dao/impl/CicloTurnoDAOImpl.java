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
import com.tesla.colegio.model.CicloTurno;

import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.Turno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CicloTurnoDAO.
 * @author MV
 *
 */
public class CicloTurnoDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CicloTurno ciclo_turno) {
		if (ciclo_turno.getId() != null) {
			// update
			String sql = "UPDATE col_ciclo_turno "
						+ "SET id_cic=?, "
						+ "id_tur=?, "
						+ "hor_ini=?, "
						+ "hor_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						ciclo_turno.getId_cic(),
						ciclo_turno.getId_tur(),
						ciclo_turno.getHor_ini(),
						ciclo_turno.getHor_fin(),
						ciclo_turno.getEst(),
						ciclo_turno.getUsr_act(),
						new java.util.Date(),
						ciclo_turno.getId()); 
			return ciclo_turno.getId();

		} else {
			// insert
			String sql = "insert into col_ciclo_turno ("
						+ "id_cic, "
						+ "id_tur, "
						+ "hor_ini, "
						+ "hor_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				ciclo_turno.getId_cic(),
				ciclo_turno.getId_tur(),
				ciclo_turno.getHor_ini(),
				ciclo_turno.getHor_fin(),
				ciclo_turno.getEst(),
				ciclo_turno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_ciclo_turno where id_cic=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CicloTurno> list() {
		String sql = "select * from col_ciclo_turno";
		
		//System.out.println(sql);
		
		List<CicloTurno> listCicloTurno = jdbcTemplate.query(sql, new RowMapper<CicloTurno>() {

			@Override
			public CicloTurno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCicloTurno;
	}

	public CicloTurno get(int id) {
		String sql = "select * from col_ciclo_turno WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CicloTurno>() {

			@Override
			public CicloTurno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CicloTurno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cit.id cit_id, cit.id_cic cit_id_cic , cit.id_tur cit_id_tur , cit.hor_ini cit_hor_ini , cit.hor_fin cit_hor_fin  ,cit.est cit_est ";
		if (aTablas.contains("col_ciclo"))
			sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
		if (aTablas.contains("col_turno"))
			sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
	
		sql = sql + " from col_ciclo_turno cit "; 
		if (aTablas.contains("col_ciclo"))
			sql = sql + " left join col_ciclo cic on cic.id = cit.id_cic ";
		if (aTablas.contains("col_turno"))
			sql = sql + " left join col_turno turno on turno.id = cit.id_tur ";
		sql = sql + " where cit.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CicloTurno>() {
		
			@Override
			public CicloTurno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CicloTurno cicloturno= rsToEntity(rs,"cit_");
					if (aTablas.contains("col_ciclo")){
						Ciclo ciclo = new Ciclo();  
							ciclo.setId(rs.getInt("cic_id")) ;  
							ciclo.setId_per(rs.getInt("cic_id_per")) ;  
							ciclo.setNom(rs.getString("cic_nom")) ;  
							ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
							ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
							cicloturno.setCiclo(ciclo);
					}
					if (aTablas.contains("col_turno")){
						Turno turno = new Turno();  
							turno.setId(rs.getInt("turno_id")) ;  
							turno.setNom(rs.getString("turno_nom")) ;  
							turno.setCod(rs.getString("turno_cod")) ;  
							cicloturno.setTurno(turno);
					}
							return cicloturno;
				}
				
				return null;
			}
			
		});


	}		
	
	public CicloTurno getByParams(Param param) {

		String sql = "select * from col_ciclo_turno " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CicloTurno>() {
			@Override
			public CicloTurno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CicloTurno> listByParams(Param param, String[] order) {

		String sql = "select * from col_ciclo_turno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CicloTurno>() {

			@Override
			public CicloTurno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CicloTurno> listFullByParams(CicloTurno cicloturno, String[] order) {
	
		return listFullByParams(Param.toParam("cit",cicloturno), order);
	
	}	
	
	public List<CicloTurno> listFullByParams(Param param, String[] order) {

		String sql = "select cit.id cit_id, cit.id_cic cit_id_cic , cit.id_tur cit_id_tur , cit.hor_ini cit_hor_ini , cit.hor_fin cit_hor_fin  ,cit.est cit_est ";
		sql = sql + ", cic.id cic_id  , cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ";
		sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
		sql = sql + " from col_ciclo_turno cit";
		sql = sql + " left join col_ciclo cic on cic.id = cit.id_cic ";
		sql = sql + " left join col_turno turno on turno.id = cit.id_tur ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CicloTurno>() {

			@Override
			public CicloTurno mapRow(ResultSet rs, int rowNum) throws SQLException {
				CicloTurno cicloturno= rsToEntity(rs,"cit_");
				Ciclo ciclo = new Ciclo();  
				ciclo.setId(rs.getInt("cic_id")) ;  
				ciclo.setId_per(rs.getInt("cic_id_per")) ;  
				ciclo.setNom(rs.getString("cic_nom")) ;  
				ciclo.setFec_ini(rs.getDate("cic_fec_ini")) ;  
				ciclo.setFec_fin(rs.getDate("cic_fec_fin")) ;  
				cicloturno.setCiclo(ciclo);
				Turno turno = new Turno();  
				turno.setId(rs.getInt("turno_id")) ;  
				turno.setNom(rs.getString("turno_nom")) ;  
				turno.setCod(rs.getString("turno_cod")) ;  
				cicloturno.setTurno(turno);
				return cicloturno;
			}

		});

	}	




	// funciones privadas utilitarias para CicloTurno

	private CicloTurno rsToEntity(ResultSet rs,String alias) throws SQLException {
		CicloTurno ciclo_turno = new CicloTurno();

		ciclo_turno.setId(rs.getInt( alias + "id"));
		ciclo_turno.setId_cic(rs.getInt( alias + "id_cic"));
		ciclo_turno.setId_tur(rs.getInt( alias + "id_tur"));
		ciclo_turno.setHor_ini(rs.getString( alias + "hor_ini"));
		ciclo_turno.setHor_fin(rs.getString( alias + "hor_fin"));
		ciclo_turno.setEst(rs.getString( alias + "est"));
								
		return ciclo_turno;

	}
	
}
