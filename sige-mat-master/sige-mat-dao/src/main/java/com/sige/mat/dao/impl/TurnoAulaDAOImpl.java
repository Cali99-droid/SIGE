package  com.sige.mat.dao.impl;

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
import com.tesla.colegio.model.TurnoAula;

import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CicloTurno;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TurnoAulaDAO.
 * @author MV
 *
 */
public class TurnoAulaDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TurnoAula turno_aula) {
		if (turno_aula.getId() != null) {
			// update
			String sql = "UPDATE col_turno_aula "
						+ "SET id_au=?, "
						+ "id_cit=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						turno_aula.getId_au(),
						turno_aula.getId_cit(),
						turno_aula.getEst(),
						turno_aula.getUsr_act(),
						new java.util.Date(),
						turno_aula.getId()); 
			return turno_aula.getId();

		} else {
			// insert
			String sql = "insert into col_turno_aula ("
						+ "id_au, "
						+ "id_cit, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				turno_aula.getId_au(),
				turno_aula.getId_cit(),
				turno_aula.getEst(),
				turno_aula.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_turno_aula where id_au=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TurnoAula> list() {
		String sql = "select * from col_turno_aula";
		
		//System.out.println(sql);
		
		List<TurnoAula> listTurnoAula = jdbcTemplate.query(sql, new RowMapper<TurnoAula>() {

			@Override
			public TurnoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTurnoAula;
	}

	public TurnoAula get(int id) {
		String sql = "select * from col_turno_aula WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TurnoAula>() {

			@Override
			public TurnoAula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TurnoAula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select atur.id atur_id, atur.id_au atur_id_au , atur.id_cit atur_id_cit  ,atur.est atur_est ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("col_ciclo_turno"))
			sql = sql + ", cit.id cit_id  , cit.id_cic cit_id_cic , cit.id_tur cit_id_tur , cit.hor_ini cit_hor_ini , cit.hor_fin cit_hor_fin  ";
	
		sql = sql + " from col_turno_aula atur "; 
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = atur.id_au ";
		if (aTablas.contains("col_ciclo_turno"))
			sql = sql + " left join col_ciclo_turno cit on cit.id = atur.id_cit ";
		sql = sql + " where atur.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TurnoAula>() {
		
			@Override
			public TurnoAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TurnoAula turnoaula= rsToEntity(rs,"atur_");
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("au_id")) ;  
							aula.setId_per(rs.getInt("au_id_per")) ;  
							aula.setId_grad(rs.getInt("au_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("au_id_tur")) ;  
							aula.setSecc(rs.getString("au_secc")) ;  
							aula.setCap(rs.getInt("au_cap")) ;  
							turnoaula.setAula(aula);
					}
					if (aTablas.contains("col_ciclo_turno")){
						CicloTurno cicloturno = new CicloTurno();  
							cicloturno.setId(rs.getInt("cit_id")) ;  
							cicloturno.setId_cic(rs.getInt("cit_id_cic")) ;  
							cicloturno.setId_tur(rs.getInt("cit_id_tur")) ;  
							cicloturno.setHor_ini(rs.getString("cit_hor_ini")) ;  
							cicloturno.setHor_fin(rs.getString("cit_hor_fin")) ;  
							turnoaula.setCicloTurno(cicloturno);
					}
							return turnoaula;
				}
				
				return null;
			}
			
		});


	}		
	
	public TurnoAula getByParams(Param param) {

		String sql = "select * from col_turno_aula " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TurnoAula>() {
			@Override
			public TurnoAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TurnoAula> listByParams(Param param, String[] order) {

		String sql = "select * from col_turno_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TurnoAula>() {

			@Override
			public TurnoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<TurnoAula> listFullByParams(TurnoAula turnoaula, String[] order) {
	
		return listFullByParams(Param.toParam("atur",turnoaula), order);
	
	}	
	
	public List<TurnoAula> listFullByParams(Param param, String[] order) {

		String sql = "select atur.id atur_id, atur.id_au atur_id_au , atur.id_cit atur_id_cit  ,atur.est atur_est ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", cit.id cit_id  , cit.id_cic cit_id_cic , cit.id_tur cit_id_tur , cit.hor_ini cit_hor_ini , cit.hor_fin cit_hor_fin  ";
		sql = sql + " from col_turno_aula atur";
		sql = sql + " left join col_aula au on au.id = atur.id_au ";
		sql = sql + " left join col_ciclo_turno cit on cit.id = atur.id_cit ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<TurnoAula>() {

			@Override
			public TurnoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				TurnoAula turnoaula= rsToEntity(rs,"atur_");
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				turnoaula.setAula(aula);
				CicloTurno cicloturno = new CicloTurno();  
				cicloturno.setId(rs.getInt("cit_id")) ;  
				cicloturno.setId_cic(rs.getInt("cit_id_cic")) ;  
				cicloturno.setId_tur(rs.getInt("cit_id_tur")) ;  
				cicloturno.setHor_ini(rs.getString("cit_hor_ini")) ;  
				cicloturno.setHor_fin(rs.getString("cit_hor_fin")) ;  
				turnoaula.setCicloTurno(cicloturno);
				return turnoaula;
			}

		});

	}	




	// funciones privadas utilitarias para TurnoAula

	private TurnoAula rsToEntity(ResultSet rs,String alias) throws SQLException {
		TurnoAula turno_aula = new TurnoAula();

		turno_aula.setId(rs.getInt( alias + "id"));
		turno_aula.setId_au(rs.getInt( alias + "id_au"));
		turno_aula.setId_cit(rs.getInt( alias + "id_cit"));
		turno_aula.setEst(rs.getString( alias + "est"));
								
		return turno_aula;

	}
	
}
