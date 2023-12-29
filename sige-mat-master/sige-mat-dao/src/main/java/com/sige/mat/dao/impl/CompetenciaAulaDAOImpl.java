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
import com.tesla.colegio.model.CompetenciaAula;

import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.PromedioCom;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CompetenciaAulaDAO.
 * @author MV
 *
 */
public class CompetenciaAulaDAOImpl{
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
	public int saveOrUpdate(CompetenciaAula competencia_aula) {
		if (competencia_aula.getId() != null) {
			// update
			String sql = "UPDATE aca_competencia_aula "
						+ "SET id_com=?, "
						+ "id_cpu=?, "
						+ "id_au=?, "
						+ "id_cua=?, "
						+ "conf_curso=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						competencia_aula.getId_com(),
						competencia_aula.getId_cpu(),
						competencia_aula.getId_au(),
						competencia_aula.getId_cua(),
						competencia_aula.getConf_curso(),
						competencia_aula.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						competencia_aula.getId()); 
			return competencia_aula.getId();

		} else {
			// insert
			String sql = "insert into aca_competencia_aula ("
						+ "id_com, "
						+ "id_cpu, "
						+ "id_au, "
						+ "id_cua, "
						+ "conf_curso, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				competencia_aula.getId_com(),
				competencia_aula.getId_cpu(),
				competencia_aula.getId_au(),
				competencia_aula.getId_cua(),
				competencia_aula.getConf_curso(),
				competencia_aula.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_competencia_aula where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CompetenciaAula> list() {
		String sql = "select * from aca_competencia_aula";
		
		System.out.println(sql);
		
		List<CompetenciaAula> listCompetenciaAula = jdbcTemplate.query(sql, new RowMapper<CompetenciaAula>() {

			@Override
			public CompetenciaAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCompetenciaAula;
	}

	public CompetenciaAula get(int id) {
		String sql = "select * from aca_competencia_aula WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaAula>() {

			@Override
			public CompetenciaAula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CompetenciaAula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select comau.id comau_id, comau.id_com comau_id_com , comau.id_cpu comau_id_cpu , comau.id_au comau_id_au , comau.id_cua comau_id_cua , comau.conf_curso comau_conf_curso  ,comau.est comau_est ";
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("col_curso_anio"))
			sql = sql + ", cua.id cua_id  , cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ";
	
		sql = sql + " from aca_competencia_aula comau "; 
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + " left join aca_competencia_dc com on com.id = comau.id_com ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = comau.id_cpu ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = comau.id_au ";
		if (aTablas.contains("col_curso_anio"))
			sql = sql + " left join col_curso_anio cua on cua.id = comau.id_cua ";
		sql = sql + " where comau.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaAula>() {
		
			@Override
			public CompetenciaAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CompetenciaAula competenciaaula= rsToEntity(rs,"comau_");
					if (aTablas.contains("aca_competencia_dc")){
						CompetenciaDc competenciadc = new CompetenciaDc();  
							competenciadc.setId(rs.getInt("com_id")) ;  
							competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
							competenciadc.setNom(rs.getString("com_nom")) ;  
							competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
							competenciadc.setOrden(rs.getInt("com_orden")) ;  
							competenciaaula.setCompetenciaDc(competenciadc);
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
							competenciaaula.setPerUni(peruni);
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
							competenciaaula.setAula(aula);
					}
					if (aTablas.contains("col_curso_anio")){
						CursoAnio cursoanio = new CursoAnio();  
							cursoanio.setId(rs.getInt("cua_id")) ;  
							cursoanio.setId_per(rs.getInt("cua_id_per")) ;  
							cursoanio.setId_gra(rs.getInt("cua_id_gra")) ;  
							cursoanio.setId_caa(rs.getInt("cua_id_caa")) ;  
							cursoanio.setId_cur(rs.getInt("cua_id_cur")) ;  
							cursoanio.setPeso(rs.getInt("cua_peso")) ;  
							cursoanio.setOrden(rs.getInt("cua_orden")) ;  
							cursoanio.setFlg_prom(rs.getString("cua_flg_prom")) ;  
							competenciaaula.setCursoAnio(cursoanio);
					}
							return competenciaaula;
				}
				
				return null;
			}
			
		});


	}		
	
	public CompetenciaAula getByParams(Param param) {

		String sql = "select * from aca_competencia_aula " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CompetenciaAula>() {
			@Override
			public CompetenciaAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CompetenciaAula> listByParams(Param param, String[] order) {

		String sql = "select * from aca_competencia_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaAula>() {

			@Override
			public CompetenciaAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CompetenciaAula> listFullByParams(CompetenciaAula competenciaaula, String[] order) {
	
		return listFullByParams(Param.toParam("comau",competenciaaula), order);
	
	}	
	
	public List<CompetenciaAula> listFullByParams(Param param, String[] order) {

		String sql = "select comau.id comau_id, comau.id_com comau_id_com , comau.id_cpu comau_id_cpu , comau.id_au comau_id_au , comau.id_cua comau_id_cua , comau.conf_curso comau_conf_curso  ,comau.est comau_est ";
		sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", cua.id cua_id  , cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ";
		sql = sql + " from aca_competencia_aula comau";
		sql = sql + " left join aca_competencia_dc com on com.id = comau.id_com ";
		sql = sql + " left join col_per_uni cpu on cpu.id = comau.id_cpu ";
		sql = sql + " left join col_aula au on au.id = comau.id_au ";
		sql = sql + " left join col_curso_anio cua on cua.id = comau.id_cua ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<CompetenciaAula>() {

			@Override
			public CompetenciaAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				CompetenciaAula competenciaaula= rsToEntity(rs,"comau_");
				CompetenciaDc competenciadc = new CompetenciaDc();  
				competenciadc.setId(rs.getInt("com_id")) ;  
				competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
				competenciadc.setNom(rs.getString("com_nom")) ;  
				competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
				competenciadc.setOrden(rs.getInt("com_orden")) ;  
				competenciaaula.setCompetenciaDc(competenciadc);
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
				competenciaaula.setPerUni(peruni);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				competenciaaula.setAula(aula);
				CursoAnio cursoanio = new CursoAnio();  
				cursoanio.setId(rs.getInt("cua_id")) ;  
				cursoanio.setId_per(rs.getInt("cua_id_per")) ;  
				cursoanio.setId_gra(rs.getInt("cua_id_gra")) ;  
				cursoanio.setId_caa(rs.getInt("cua_id_caa")) ;  
				cursoanio.setId_cur(rs.getInt("cua_id_cur")) ;  
				cursoanio.setPeso(rs.getInt("cua_peso")) ;  
				cursoanio.setOrden(rs.getInt("cua_orden")) ;  
				cursoanio.setFlg_prom(rs.getString("cua_flg_prom")) ;  
				competenciaaula.setCursoAnio(cursoanio);
				return competenciaaula;
			}

		});

	}	


	public List<PromedioCom> getListPromedioCom(Param param, String[] order) {
		String sql = "select * from not_promedio_com " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<PromedioCom>() {

			@Override
			public PromedioCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				PromedioCom promedio_com = new PromedioCom();

				promedio_com.setId(rs.getInt("id"));
				//promedio_com.setId_comau(rs.getInt("id_comau"));
				promedio_com.setId_au(rs.getInt("id_au"));
				promedio_com.setId_tra(rs.getInt("id_tra"));
				promedio_com.setId_alu(rs.getInt("id_alu"));
				promedio_com.setId_cpu(rs.getInt("id_cpu"));
				promedio_com.setFec(rs.getDate("fec"));
				promedio_com.setProm(rs.getBigDecimal("prom"));
				promedio_com.setEst(rs.getString("est"));
												
				return promedio_com;
			}

		});	
	}


	// funciones privadas utilitarias para CompetenciaAula

	private CompetenciaAula rsToEntity(ResultSet rs,String alias) throws SQLException {
		CompetenciaAula competencia_aula = new CompetenciaAula();

		competencia_aula.setId(rs.getInt( alias + "id"));
		competencia_aula.setId_com(rs.getInt( alias + "id_com"));
		competencia_aula.setId_cpu(rs.getInt( alias + "id_cpu"));
		competencia_aula.setId_au(rs.getInt( alias + "id_au"));
		competencia_aula.setId_cua(rs.getInt( alias + "id_cua"));
		competencia_aula.setConf_curso(rs.getString( alias + "conf_curso"));
		competencia_aula.setEst(rs.getString( alias + "est"));
								
		return competencia_aula;

	}
	
}
