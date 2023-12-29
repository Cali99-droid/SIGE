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
import com.tesla.colegio.model.Ciclo;

import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.CicloTurno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CicloDAO.
 * @author MV
 *
 */
public class CicloDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Ciclo ciclo) {
		if (ciclo.getId() != null) {
			// update
			String sql = "UPDATE col_ciclo "
						+ "SET id_per=?, "
						+ "nom=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						ciclo.getId_per(),
						ciclo.getNom(),
						ciclo.getFec_ini(),
						ciclo.getFec_fin(),
						ciclo.getEst(),
						ciclo.getUsr_act(),
						new java.util.Date(),
						ciclo.getId()); 
			return ciclo.getId();

		} else {
			// insert
			String sql = "insert into col_ciclo ("
						+ "id_per, "
						+ "nom, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				ciclo.getId_per(),
				ciclo.getNom(),
				ciclo.getFec_ini(),
				ciclo.getFec_fin(),
				ciclo.getEst(),
				ciclo.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_ciclo where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<Ciclo> list() {
		String sql = "select * from col_ciclo";
		
		//System.out.println(sql);
		
		List<Ciclo> listCiclo = jdbcTemplate.query(sql, new RowMapper<Ciclo>() {

			@Override
			public Ciclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCiclo;
	}

	public Ciclo get(int id) {
		String sql = "select * from col_ciclo WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ciclo>() {

			@Override
			public Ciclo extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Ciclo getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cic.id cic_id, cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ,cic.est cic_est ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat , per.flag_sit per_flag_sit  ";
	
		sql = sql + " from col_ciclo cic "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo per on per.id = cic.id_per ";
		sql = sql + " where cic.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Ciclo>() {
		
			@Override
			public Ciclo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Ciclo ciclo= rsToEntity(rs,"cic_");
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
							ciclo.setPeriodo(periodo);
					}
							return ciclo;
				}
				
				return null;
			}
			
		});


	}		
	
	public Ciclo getByParams(Param param) {

		String sql = "select * from col_ciclo " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Ciclo>() {
			@Override
			public Ciclo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Ciclo> listByParams(Param param, String[] order) {

		String sql = "select * from col_ciclo " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ciclo>() {

			@Override
			public Ciclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Ciclo> listFullByParams(Ciclo ciclo, String[] order) {
	
		return listFullByParams(Param.toParam("cic",ciclo), order);
	
	}	
	
	public List<Ciclo> listFullByParams(Param param, String[] order) {

		String sql = "select cic.id cic_id, cic.id_per cic_id_per , cic.nom cic_nom , cic.fec_ini cic_fec_ini , cic.fec_fin cic_fec_fin  ,cic.est cic_est ";
		sql = sql + ", per.id per_id  , per.id_anio per_id_anio , per.id_suc per_id_suc , per.id_niv per_id_niv , per.id_srv per_id_srv , per.id_tpe per_id_tpe , per.fec_ini per_fec_ini , per.fec_fin per_fec_fin , per.fec_cie_mat per_fec_cie_mat , per.flag_sit per_flag_sit  ";
		sql = sql + " from col_ciclo cic";
		sql = sql + " left join per_periodo per on per.id = cic.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<Ciclo>() {

			@Override
			public Ciclo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Ciclo ciclo= rsToEntity(rs,"cic_");
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
				ciclo.setPeriodo(periodo);
				return ciclo;
			}

		});

	}	


	public List<CicloTurno> getListCicloTurno(Param param, String[] order) {
		String sql = "select * from col_ciclo_turno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CicloTurno>() {

			@Override
			public CicloTurno mapRow(ResultSet rs, int rowNum) throws SQLException {
				CicloTurno ciclo_turno = new CicloTurno();

				ciclo_turno.setId(rs.getInt("id"));
				ciclo_turno.setId_cic(rs.getInt("id_cic"));
				ciclo_turno.setId_tur(rs.getInt("id_tur"));
				ciclo_turno.setHor_ini(rs.getString("hor_ini"));
				ciclo_turno.setHor_fin(rs.getString("hor_fin"));
				ciclo_turno.setEst(rs.getString("est"));
												
				return ciclo_turno;
			}

		});	
	}


	// funciones privadas utilitarias para Ciclo

	private Ciclo rsToEntity(ResultSet rs,String alias) throws SQLException {
		Ciclo ciclo = new Ciclo();

		ciclo.setId(rs.getInt( alias + "id"));
		ciclo.setId_per(rs.getInt( alias + "id_per"));
		ciclo.setNom(rs.getString( alias + "nom"));
		ciclo.setFec_ini(rs.getDate( alias + "fec_ini"));
		ciclo.setFec_fin(rs.getDate( alias + "fec_fin"));
		ciclo.setEst(rs.getString( alias + "est"));
								
		return ciclo;

	}
	
}
