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
import com.tesla.colegio.model.NotaIndicador;

import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.IndEva;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaIndicadorDAO.
 * @author MV
 *
 */
public class NotaIndicadorDAOImpl{
	final static Logger logger = Logger.getLogger(NotaIndicadorDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(NotaIndicador nota_indicador) {
		if (nota_indicador.getId() != null) {
			// update
			String sql = "UPDATE not_nota_indicador "
						+ "SET id_not=?, "
						+ "id_nie=?, "
						+ "nota=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						nota_indicador.getId_not(),
						nota_indicador.getId_nie(),
						nota_indicador.getNota(),
						nota_indicador.getEst(),
						nota_indicador.getUsr_act(),
						new java.util.Date(),
						nota_indicador.getId()); 
			return nota_indicador.getId();

		} else {
			// insert
			String sql = "insert into not_nota_indicador ("
						+ "id_not, "
						+ "id_nie, "
						+ "nota, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				nota_indicador.getId_not(),
				nota_indicador.getId_nie(),
				nota_indicador.getNota(),
				nota_indicador.getEst(),
				nota_indicador.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id_eva) {
		String sql = "delete from not_nota_indicador  WHERE id_nie IN ( SELECT id FROM not_ind_eva nie WHERE nie.id_ne=?);";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id_eva);
	}

	public List<NotaIndicador> list() {
		String sql = "select * from not_nota_indicador";
		
		//logger.info(sql);
		
		List<NotaIndicador> listNotaIndicador = jdbcTemplate.query(sql, new RowMapper<NotaIndicador>() {

			@Override
			public NotaIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaIndicador;
	}

	public NotaIndicador get(int id) {
		String sql = "select * from not_nota_indicador WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaIndicador>() {

			@Override
			public NotaIndicador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NotaIndicador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nni.id nni_id, nni.id_not nni_id_not , nni.id_nie nni_id_nie , nni.nota nni_nota  ,nni.est nni_est ";
		if (aTablas.contains("not_nota"))
			sql = sql + ", not.id not_id  , not.id_ne not_id_ne , not.id_tra not_id_tra , not.id_alu not_id_alu , not.fec not_fec , not.prom not_prom  ";
		if (aTablas.contains("not_ind_eva"))
			sql = sql + ", nie.id nie_id  , nie.id_ne nie_id_ne , nie.id_cis nie_id_cis  ";
	
		sql = sql + " from not_nota_indicador nni "; 
		if (aTablas.contains("not_nota"))
			sql = sql + " left join not_nota not on not.id = nni.id_not ";
		if (aTablas.contains("not_ind_eva"))
			sql = sql + " left join not_ind_eva nie on nie.id = nni.id_nie ";
		sql = sql + " where nni.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaIndicador>() {
		
			@Override
			public NotaIndicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaIndicador notaindicador= rsToEntity(rs,"nni_");
					if (aTablas.contains("not_nota")){
						Nota nota = new Nota();  
							nota.setId(rs.getInt("not_id")) ;  
							nota.setId_ne(rs.getInt("not_id_ne")) ;  
							nota.setId_tra(rs.getInt("not_id_tra")) ;  
							nota.setId_alu(rs.getInt("not_id_alu")) ;  
							nota.setFec(rs.getDate("not_fec")) ;  
							nota.setProm(rs.getBigDecimal("not_prom")) ;  
							notaindicador.setNotaObj(nota);
					}
					if (aTablas.contains("not_ind_eva")){
						IndEva indeva = new IndEva();  
							indeva.setId(rs.getInt("nie_id")) ;  
							indeva.setId_ne(rs.getInt("nie_id_ne")) ;  
							////////////////indeva.setId_cis(rs.getInt("nie_id_cis")) ;  
							notaindicador.setIndEva(indeva);
					}
							return notaindicador;
				}
				
				return null;
			}
			
		});


	}		
	
	public NotaIndicador getByParams(Param param) {

		String sql = "select * from not_nota_indicador " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaIndicador>() {
			@Override
			public NotaIndicador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NotaIndicador> listByParams(Param param, String[] order) {

		String sql = "select * from not_nota_indicador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaIndicador>() {

			@Override
			public NotaIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NotaIndicador> listFullByParams(NotaIndicador notaindicador, String[] order) {
	
		return listFullByParams(Param.toParam("nni",notaindicador), order);
	
	}	
	
	public List<NotaIndicador> listFullByParams(Param param, String[] order) {

		String sql = "select nni.id nni_id, nni.id_not nni_id_not , nni.id_nie nni_id_nie , nni.nota nni_nota  ,nni.est nni_est ";
		sql = sql + ", not.id not_id  , not.id_ne not_id_ne , not.id_tra not_id_tra , not.id_alu not_id_alu , not.fec not_fec , not.prom not_prom  ";
		sql = sql + ", nie.id nie_id  , nie.id_ne nie_id_ne , nie.id_cis nie_id_cis  ";
		sql = sql + " from not_nota_indicador nni";
		sql = sql + " left join not_nota not on not.id = nni.id_not ";
		sql = sql + " left join not_ind_eva nie on nie.id = nni.id_nie ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaIndicador>() {

			@Override
			public NotaIndicador mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaIndicador notaindicador= rsToEntity(rs,"nni_");
				Nota nota = new Nota();  
				nota.setId(rs.getInt("not_id")) ;  
				nota.setId_ne(rs.getInt("not_id_ne")) ;  
				nota.setId_tra(rs.getInt("not_id_tra")) ;  
				nota.setId_alu(rs.getInt("not_id_alu")) ;  
				nota.setFec(rs.getDate("not_fec")) ;  
				nota.setProm(rs.getBigDecimal("not_prom")) ;  
				notaindicador.setNotaObj(nota);
				IndEva indeva = new IndEva();  
				indeva.setId(rs.getInt("nie_id")) ;  
				indeva.setId_ne(rs.getInt("nie_id_ne")) ;  
				//////////indeva.setId_cis(rs.getInt("nie_id_cis")) ;  
				notaindicador.setIndEva(indeva);
				return notaindicador;
			}

		});

	}	




	// funciones privadas utilitarias para NotaIndicador

	private NotaIndicador rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaIndicador nota_indicador = new NotaIndicador();

		nota_indicador.setId(rs.getInt( alias + "id"));
		nota_indicador.setId_not(rs.getInt( alias + "id_not"));
		nota_indicador.setId_nie(rs.getInt( alias + "id_nie"));
		nota_indicador.setNota(rs.getInt( alias + "nota"));
		nota_indicador.setEst(rs.getString( alias + "est"));
								
		return nota_indicador;

	}
	
}
