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
import com.tesla.colegio.model.CondMatricula;

import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.Matricula;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CondMatriculaDAO.
 * @author MV
 *
 */
public class CondMatriculaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CondMatricula cond_matricula) {
		if (cond_matricula.getId() != null) {
			// update
			String sql = "UPDATE cat_cond_matricula "
						+ "SET nom=?, "
						+ "des=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						cond_matricula.getNom(),
						cond_matricula.getDes(),
						cond_matricula.getEst(),
						cond_matricula.getUsr_act(),
						new java.util.Date(),
						cond_matricula.getId()); 
			return cond_matricula.getId();

		} else {
			// insert
			String sql = "insert into cat_cond_matricula ("
						+ "nom, "
						+ "des, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				cond_matricula.getNom(),
				cond_matricula.getDes(),
				cond_matricula.getEst(),
				cond_matricula.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from cat_cond_matricula where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<CondMatricula> list() {
		String sql = "select * from cat_cond_matricula";
		
		
		
		List<CondMatricula> listCondMatricula = jdbcTemplate.query(sql, new RowMapper<CondMatricula>() {

			@Override
			public CondMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCondMatricula;
	}

	public CondMatricula get(int id) {
		String sql = "select * from cat_cond_matricula WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondMatricula>() {

			@Override
			public CondMatricula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CondMatricula getFull(int id, String tablas[]) {
		String sql = "select cma.id cma_id, cma.nom cma_nom , cma.des cma_des  ,cma.est cma_est ";
	
		sql = sql + " from cat_cond_matricula cma "; 
		sql = sql + " where cma.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CondMatricula>() {
		
			@Override
			public CondMatricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CondMatricula condmatricula= rsToEntity(rs,"cma_");
							return condmatricula;
				}
				
				return null;
			}
			
		});


	}		
	
	public CondMatricula getByParams(Param param) {

		String sql = "select * from cat_cond_matricula " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CondMatricula>() {
			@Override
			public CondMatricula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CondMatricula> listByParams(Param param, String[] order) {

		String sql = "select * from cat_cond_matricula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<CondMatricula>() {

			@Override
			public CondMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CondMatricula> listFullByParams(CondMatricula condmatricula, String[] order) {
	
		return listFullByParams(Param.toParam("cma",condmatricula), order);
	
	}	
	
	public List<CondMatricula> listFullByParams(Param param, String[] order) {

		String sql = "select cma.id cma_id, cma.nom cma_nom , cma.des cma_des  ,cma.est cma_est ";
		sql = sql + " from cat_cond_matricula cma";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CondMatricula>() {

			@Override
			public CondMatricula mapRow(ResultSet rs, int rowNum) throws SQLException {
				CondMatricula condmatricula= rsToEntity(rs,"cma_");
				return condmatricula;
			}

		});

	}	


	public List<Reserva> getListReserva(Param param, String[] order) {
		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			@Override
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

		
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			@Override
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


	// funciones privadas utilitarias para CondMatricula

	private CondMatricula rsToEntity(ResultSet rs,String alias) throws SQLException {
		CondMatricula cond_matricula = new CondMatricula();

		cond_matricula.setId(rs.getInt( alias + "id"));
		cond_matricula.setNom(rs.getString( alias + "nom"));
		cond_matricula.setDes(rs.getString( alias + "des"));
		cond_matricula.setEst(rs.getString( alias + "est"));
								
		return cond_matricula;

	}
	
}
