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
import com.tesla.colegio.model.PermisoDocente;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.PeriodoAca;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PermisoDocenteDAO.
 * @author MV
 *
 */
public class PermisoDocenteDAOImpl{
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
	public int saveOrUpdate(PermisoDocente permiso_docente) {
		if (permiso_docente.getId() != null) {
			// update
			String sql = "UPDATE col_permiso_docente "
						+ "SET id_prof=?, "
						+ "id_cpu=?, "
						+ "id_au=?, "
						+ "dias=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						permiso_docente.getId_prof(),
						permiso_docente.getId_cpu(),
						permiso_docente.getId_au(),
						permiso_docente.getDias(),
						permiso_docente.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						permiso_docente.getId()); 
			return permiso_docente.getId();

		} else {
			// insert
			String sql = "insert into col_permiso_docente ("
						+ "id_prof, "
						+ "id_cpu, "
						+ "id_au, "
						+ "dias, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				permiso_docente.getId_prof(),
				permiso_docente.getId_cpu(),
				permiso_docente.getId_au(),
				permiso_docente.getDias(),
				permiso_docente.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_permiso_docente where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PermisoDocente> list() {
		String sql = "select * from col_permiso_docente";
		
		//System.out.println(sql);
		
		List<PermisoDocente> listPermisoDocente = jdbcTemplate.query(sql, new RowMapper<PermisoDocente>() {

			@Override
			public PermisoDocente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPermisoDocente;
	}

	public PermisoDocente get(int id) {
		String sql = "select * from col_permiso_docente WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PermisoDocente>() {

			@Override
			public PermisoDocente extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PermisoDocente getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cpd.id cpd_id, cpd.id_prof cpd_id_prof , cpd.id_cpu cpd_id_cpu , cpd.id_au cpd_id_au , cpd.dias cpd_dias  ,cpd.est cpd_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
	
		sql = sql + " from col_permiso_docente cpd "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = cpd.id_prof ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = cpd.id_cpu ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = cpd.id_au ";
		sql = sql + " where cpd.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PermisoDocente>() {
		
			@Override
			public PermisoDocente extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PermisoDocente permisodocente= rsToEntity(rs,"cpd_");
					if (aTablas.contains("ges_trabajador")){
						Trabajador trabajador = new Trabajador();  
							trabajador.setId(rs.getInt("tra_id")) ;  
							trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
							trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
							trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
							trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
							trabajador.setNom(rs.getString("tra_nom")) ;  
							trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
							trabajador.setGenero(rs.getString("tra_genero")) ;  
							trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
							trabajador.setDir(rs.getString("tra_dir")) ;  
							trabajador.setTel(rs.getString("tra_tel")) ;  
							trabajador.setCel(rs.getString("tra_cel")) ;  
							trabajador.setCorr(rs.getString("tra_corr")) ;  
							trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
							trabajador.setCarrera(rs.getString("tra_carrera")) ;  
							//trabajador.setFot(rs.getString("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							permisodocente.setTrabajador(trabajador);
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
							permisodocente.setPerUni(peruni);
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
							permisodocente.setAula(aula);
					}
							return permisodocente;
				}
				
				return null;
			}
			
		});


	}		
	
	public PermisoDocente getByParams(Param param) {

		String sql = "select * from col_permiso_docente " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PermisoDocente>() {
			@Override
			public PermisoDocente extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PermisoDocente> listByParams(Param param, String[] order) {

		String sql = "select * from col_permiso_docente " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PermisoDocente>() {

			@Override
			public PermisoDocente mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PermisoDocente> listFullByParams(PermisoDocente permisodocente, String[] order) {
	
		return listFullByParams(Param.toParam("cpd",permisodocente), order);
	
	}	
	
	public List<PermisoDocente> listFullByParams(Param param, String[] order) {

		String sql = "select cpd.id cpd_id, cpd.id_prof cpd_id_prof , cpd.id_cpu cpd_id_cpu , cpd.id_au cpd_id_au , cpd.dias cpd_dias  ,cpd.est cpd_est, cpd.fec_ins cpd_fec_ins";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		//sql = sql + ", per.id per_id  , per.id_tdc per_id_tdc , per.nro_doc per_nro_doc , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.nom per_nom , per.fec_nac per_fec_nac , per.genero per_genero , per.id_eci per_id_eci , per.dir per_dir , per.tel per_tel , per.cel per_cel , per.corr per_corr , per.id_gin per_id_gin   ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", cp.nom cp_nom, cg.id cg_id, cg.nom cg_nom, niv.id niv_id ";
		sql = sql + ", gir.nom gir_nom, gir.id gir_id ";
		sql = sql + " from col_permiso_docente cpd";
		sql = sql + " left join ges_trabajador tra on tra.id = cpd.id_prof ";
		//sql = sql + " left join col_persona per on tra.id_per = per.id ";
		sql = sql + " left join col_per_uni cpu on cpu.id = cpd.id_cpu ";
		sql = sql + " left join col_aula au on au.id = cpd.id_au ";
		sql = sql + " left join cat_per_aca_nivel cpa on cpu.id_cpa=cpa.id  ";
		sql = sql + " left join cat_periodo_aca cp on cpa.id_cpa=cp.id ";
		sql = sql + " left join cat_grad cg on au.id_grad=cg.id ";
		sql = sql + " left join cat_nivel niv on cg.id_nvl=niv.id ";
		sql = sql + " left join ges_giro_negocio gir on cpa.id_gir=gir.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PermisoDocente>() {

			@Override
			public PermisoDocente mapRow(ResultSet rs, int rowNum) throws SQLException {
				PermisoDocente permisodocente= rsToEntity(rs,"cpd_");
				Trabajador trabajador = new Trabajador();  
				trabajador.setId(rs.getInt("tra_id")) ;  
				trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
				trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
				trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
				trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
				trabajador.setNom(rs.getString("tra_nom")) ;  
				trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
				trabajador.setGenero(rs.getString("tra_genero")) ;  
				trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
				trabajador.setDir(rs.getString("tra_dir")) ;  
				trabajador.setTel(rs.getString("tra_tel")) ;  
				trabajador.setCel(rs.getString("tra_cel")) ;  
				trabajador.setCorr(rs.getString("tra_corr")) ;  
				trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
				trabajador.setCarrera(rs.getString("tra_carrera")) ;  
				//trabajador.setFot(rs.getString("tra_fot")) ;  
				trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
				trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
				permisodocente.setTrabajador(trabajador);
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
				permisodocente.setPerUni(peruni);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				permisodocente.setAula(aula);
				PeriodoAca periodo= new PeriodoAca();
				periodo.setNom(rs.getString("cp_nom"));
				permisodocente.setPeriodoaca(periodo);
				Grad grado = new Grad();
				grado.setId(rs.getInt("cg_id"));
				grado.setNom(rs.getString("cg_nom"));
				permisodocente.setGrad(grado);
				Nivel nivel = new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				permisodocente.setNivel(nivel);
				GiroNegocio giro = new GiroNegocio();
				giro.setId(rs.getInt("gir_id"));
				permisodocente.setGiro(giro);
				return permisodocente;
			}

		});

	}	




	// funciones privadas utilitarias para PermisoDocente

	private PermisoDocente rsToEntity(ResultSet rs,String alias) throws SQLException {
		PermisoDocente permiso_docente = new PermisoDocente();

		permiso_docente.setId(rs.getInt( alias + "id"));
		permiso_docente.setId_prof(rs.getInt( alias + "id_prof"));
		permiso_docente.setId_cpu(rs.getInt( alias + "id_cpu"));
		permiso_docente.setId_au(rs.getInt( alias + "id_au"));
		permiso_docente.setDias(rs.getInt( alias + "dias"));
		permiso_docente.setEst(rs.getString( alias + "est"));
		permiso_docente.setFec_ins(rs.getDate(alias + "fec_ins"));					
		return permiso_docente;

	}
	
}
