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
import com.tesla.colegio.model.DesempenioAula;

import com.tesla.colegio.model.DesempenioDc;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.NotaDes;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DesempenioAulaDAO.
 * @author MV
 *
 */
public class DesempenioAulaDAOImpl{
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
	public int saveOrUpdate(DesempenioAula desempenio_aula) {
		if (desempenio_aula.getId() != null) {
			// update
			String sql = "UPDATE aca_desempenio_aula "
						+ "SET id_desdc=?, "
						+ "id_cpu=?, "
						+ "id_cap=?, "
						+ "id_au=?, "
						+ "id_cua=?, "
						+ "conf_curso=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						desempenio_aula.getId_desdc(),
						desempenio_aula.getId_cpu(),
						desempenio_aula.getId_cap(),
						desempenio_aula.getId_au(),
						desempenio_aula.getId_cua(),
						desempenio_aula.getConf_curso(),
						desempenio_aula.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						desempenio_aula.getId()); 
			return desempenio_aula.getId();

		} else {
			// insert
			String sql = "insert into aca_desempenio_aula ("
						+ "id_desdc, "
						+ "id_cpu, "
						+ "id_cap, "
						+ "id_au, "
						+ "id_cua, "
						+ "conf_curso, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?,?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				desempenio_aula.getId_desdc(),
				desempenio_aula.getId_cpu(),
				desempenio_aula.getId_cap(),
				desempenio_aula.getId_au(),
				desempenio_aula.getId_cua(),
				desempenio_aula.getConf_curso(),
				desempenio_aula.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_desempenio_aula where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DesempenioAula> list() {
		String sql = "select * from aca_desempenio_aula";
		
		System.out.println(sql);
		
		List<DesempenioAula> listDesempenioAula = jdbcTemplate.query(sql, new RowMapper<DesempenioAula>() {

			@Override
			public DesempenioAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDesempenioAula;
	}

	public DesempenioAula get(int id) {
		String sql = "select * from aca_desempenio_aula WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioAula>() {

			@Override
			public DesempenioAula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DesempenioAula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select desau.id desau_id, desau.id_desdc desau_id_desdc , desau.id_cpu desau_id_cpu , desau.id_au desau_id_au  ,desau.est desau_est ";
		if (aTablas.contains("aca_desempenio_dc"))
			sql = sql + ", des.id des_id  , des.id_com des_id_com , des.id_gra des_id_gra , des.nom des_nom , des.peso des_peso , des.orden des_orden  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
	
		sql = sql + " from aca_desempenio_aula desau "; 
		if (aTablas.contains("aca_desempenio_dc"))
			sql = sql + " left join aca_desempenio_dc des on des.id = desau.id_desdc ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = desau.id_cpu ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = desau.id_au ";
		sql = sql + " where desau.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioAula>() {
		
			@Override
			public DesempenioAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DesempenioAula desempenioaula= rsToEntity(rs,"desau_");
					if (aTablas.contains("aca_desempenio_dc")){
						DesempenioDc desempeniodc = new DesempenioDc();  
							desempeniodc.setId(rs.getInt("des_id")) ;  
							desempeniodc.setId_com(rs.getInt("des_id_com")) ;  
							desempeniodc.setId_gra(rs.getInt("des_id_gra")) ;  
							desempeniodc.setNom(rs.getString("des_nom")) ;  
							desempeniodc.setPeso(rs.getBigDecimal("des_peso")) ;  
							desempeniodc.setOrden(rs.getInt("des_orden")) ;  
							desempenioaula.setDesempenioDc(desempeniodc);
					}
					if (aTablas.contains("col_per_uni")){
						PerUni peruni = new PerUni();  
							peruni.setId(rs.getInt("cpu_id")) ;  
							peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
							//peruni.setId_anio(rs.getString("cpu_id_anio")) ;  
							peruni.setNump(rs.getInt("cpu_nump")) ;  
							peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
							peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
							peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
							peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
							peruni.setFec_ini_ing(rs.getDate("cpu_fec_ini_ing")) ;  
							peruni.setFec_fin_ing(rs.getDate("cpu_fec_fin_ing")) ;  
							desempenioaula.setPerUni(peruni);
					}
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("au_id")) ;  
							aula.setId_per(rs.getInt("au_id_per")) ;  
							aula.setId_grad(rs.getInt("au_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("au_id_tur")) ;  
							aula.setSecc(rs.getString("au_secc")) ;  
							aula.setCap(rs.getInt("au_cap")) ;  
							desempenioaula.setAula(aula);
					}
							return desempenioaula;
				}
				
				return null;
			}
			
		});


	}		
	
	public DesempenioAula getByParams(Param param) {

		String sql = "select * from aca_desempenio_aula " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DesempenioAula>() {
			@Override
			public DesempenioAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DesempenioAula> listByParams(Param param, String[] order) {

		String sql = "select * from aca_desempenio_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DesempenioAula>() {

			@Override
			public DesempenioAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DesempenioAula> listFullByParams(DesempenioAula desempenioaula, String[] order) {
	
		return listFullByParams(Param.toParam("desau",desempenioaula), order);
	
	}	
	
	public List<DesempenioAula> listFullByParams(Param param, String[] order) {

		String sql = "select desau.id desau_id, desau.id_desdc desau_id_desdc , desau.id_cpu desau_id_cpu , desau.id_au desau_id_au  ,desau.est desau_est ";
		sql = sql + ", des.id des_id  , des.id_com des_id_com , des.id_gra des_id_gra , des.nom des_nom , des.peso des_peso , des.orden des_orden  ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + " from aca_desempenio_aula desau";
		sql = sql + " left join aca_desempenio_dc des on des.id = desau.id_desdc ";
		sql = sql + " left join col_per_uni cpu on cpu.id = desau.id_cpu ";
		sql = sql + " left join col_aula au on au.id = desau.id_au ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DesempenioAula>() {

			@Override
			public DesempenioAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				DesempenioAula desempenioaula= rsToEntity(rs,"desau_");
				DesempenioDc desempeniodc = new DesempenioDc();  
				desempeniodc.setId(rs.getInt("des_id")) ;  
				desempeniodc.setId_com(rs.getInt("des_id_com")) ;  
				desempeniodc.setId_gra(rs.getInt("des_id_gra")) ;  
				desempeniodc.setNom(rs.getString("des_nom")) ;  
				desempeniodc.setPeso(rs.getBigDecimal("des_peso")) ;  
				desempeniodc.setOrden(rs.getInt("des_orden")) ;  
				desempenioaula.setDesempenioDc(desempeniodc);
				PerUni peruni = new PerUni();  
				peruni.setId(rs.getInt("cpu_id")) ;  
				peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
				//peruni.setId_anio(rs.getString("cpu_id_anio")) ;  
				peruni.setNump(rs.getInt("cpu_nump")) ;  
				peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
				peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
				peruni.setFec_ini(rs.getDate("cpu_fec_ini")) ;  
				peruni.setFec_fin(rs.getDate("cpu_fec_fin")) ;  
				peruni.setFec_ini_ing(rs.getDate("cpu_fec_ini_ing")) ;  
				peruni.setFec_fin_ing(rs.getDate("cpu_fec_fin_ing")) ;  
				desempenioaula.setPerUni(peruni);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				desempenioaula.setAula(aula);
				return desempenioaula;
			}

		});

	}	


	public List<NotaDes> getListNotaDes(Param param, String[] order) {
		String sql = "select * from not_nota_des " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<NotaDes>() {

			@Override
			public NotaDes mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaDes nota_des = new NotaDes();

				nota_des.setId(rs.getInt("id"));
				nota_des.setId_desau(rs.getInt("id_desau"));
				nota_des.setId_tra(rs.getInt("id_tra"));
				nota_des.setId_alu(rs.getInt("id_alu"));
				nota_des.setFec(rs.getDate("fec"));
				nota_des.setNota(rs.getInt("nota"));
				nota_des.setProm(rs.getBigDecimal("prom"));
				nota_des.setEst(rs.getString("est"));
												
				return nota_des;
			}

		});	
	}


	// funciones privadas utilitarias para DesempenioAula

	private DesempenioAula rsToEntity(ResultSet rs,String alias) throws SQLException {
		DesempenioAula desempenio_aula = new DesempenioAula();

		desempenio_aula.setId(rs.getInt( alias + "id"));
		desempenio_aula.setId_desdc(rs.getInt( alias + "id_desdc"));
		desempenio_aula.setId_cpu(rs.getInt( alias + "id_cpu"));
		desempenio_aula.setId_au(rs.getInt( alias + "id_au"));
		desempenio_aula.setId_cua(rs.getInt( alias + "id_cua"));
		desempenio_aula.setEst(rs.getString( alias + "est"));
								
		return desempenio_aula;

	}
	
}
