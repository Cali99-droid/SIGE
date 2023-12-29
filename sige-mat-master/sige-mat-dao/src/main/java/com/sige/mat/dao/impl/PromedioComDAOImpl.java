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
import com.tesla.colegio.model.PromedioCom;

import com.tesla.colegio.model.CompetenciaDc;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.PerUni;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface PromedioComDAO.
 * @author MV
 *
 */
public class PromedioComDAOImpl{
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
	public int saveOrUpdate(PromedioCom promedio_com) {
		if (promedio_com.getId() != null) {
			// update
			String sql = "UPDATE not_promedio_com "
						+ "SET id_com=?, "
						+ "id_cua=?, "
						+ "id_au=?, "
						+ "id_tra=?, "
						+ "id_alu=?, "
						+ "id_cpu=?, "
						+ "fec=?, "
						+ "prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						promedio_com.getId_com(),
						promedio_com.getId_cua(),
						promedio_com.getId_au(),
						promedio_com.getId_tra(),
						promedio_com.getId_alu(),
						promedio_com.getId_cpu(),
						promedio_com.getFec(),
						promedio_com.getProm(),
						promedio_com.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						promedio_com.getId()); 
			return promedio_com.getId();

		} else {
			// insert
			String sql = "insert into not_promedio_com ("
						+ "id_com, "
						+ "id_cua, "
						+ "id_au, "
						+ "id_tra, "
						+ "id_alu, "
						+ "id_cpu, "
						+ "fec, "
						+ "prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				promedio_com.getId_com(),
				promedio_com.getId_cua(),
				promedio_com.getId_au(),
				promedio_com.getId_tra(),
				promedio_com.getId_alu(),
				promedio_com.getId_cpu(),
				promedio_com.getFec(),
				promedio_com.getProm(),
				promedio_com.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_promedio_com where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<PromedioCom> list() {
		String sql = "select * from not_promedio_com";
		
		System.out.println(sql);
		
		List<PromedioCom> listPromedioCom = jdbcTemplate.query(sql, new RowMapper<PromedioCom>() {

			@Override
			public PromedioCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listPromedioCom;
	}

	public PromedioCom get(int id) {
		String sql = "select * from not_promedio_com WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PromedioCom>() {

			@Override
			public PromedioCom extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public PromedioCom getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select not.id not_id, not.id_com not_id_com , not.id_tra not_id_tra , not.id_alu not_id_alu , not.id_cpu not_id_cpu , not.fec not_fec , not.prom not_prom  ,not.est not_est ";
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_usr alu_id_usr , alu.cod alu_cod , alu.num_hij alu_num_hij , alu.email_inst alu_email_inst , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
	
		sql = sql + " from not_promedio_com not "; 
		if (aTablas.contains("aca_competencia_dc"))
			sql = sql + " left join aca_competencia_dc com on com.id = not.id_com ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = not.id_tra ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = not.id_alu ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = not.id_cpu ";
		sql = sql + " where not.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<PromedioCom>() {
		
			@Override
			public PromedioCom extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PromedioCom promediocom= rsToEntity(rs,"not_");
					if (aTablas.contains("aca_competencia_dc")){
						CompetenciaDc competenciadc = new CompetenciaDc();  
							competenciadc.setId(rs.getInt("com_id")) ;  
							competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
							competenciadc.setNom(rs.getString("com_nom")) ;  
							competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
							competenciadc.setOrden(rs.getInt("com_orden")) ;  
							promediocom.setCompetenciaDc(competenciadc);
					}
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
							promediocom.setTrabajador(trabajador);
					}
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							//alumno.setId_usr(rs.getInt("alu_id_usr")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setEmail_inst(rs.getString("alu_email_inst")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							promediocom.setAlumno(alumno);
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
							promediocom.setPerUni(peruni);
					}
							return promediocom;
				}
				
				return null;
			}
			
		});


	}		
	
	public PromedioCom getByParams(Param param) {

		String sql = "select * from not_promedio_com " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<PromedioCom>() {
			@Override
			public PromedioCom extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<PromedioCom> listByParams(Param param, String[] order) {

		String sql = "select * from not_promedio_com " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PromedioCom>() {

			@Override
			public PromedioCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<PromedioCom> listFullByParams(PromedioCom promediocom, String[] order) {
	
		return listFullByParams(Param.toParam("not",promediocom), order);
	
	}	
	
	public List<PromedioCom> listFullByParams(Param param, String[] order) {

		String sql = "select not.id not_id, not.id_com not_id_com , not.id_au not_id_au , not.id_tra not_id_tra , not.id_alu not_id_alu , not.id_cpu not_id_cpu , not.fec not_fec , not.prom not_prom  ,not.est not_est ";
		sql = sql + ", com.id com_id  , com.id_dcare com_id_dcare , com.nom com_nom , com.peso com_peso , com.orden com_orden  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", alu.id alu_id  , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_usr alu_id_usr , alu.cod alu_cod , alu.num_hij alu_num_hij , alu.email_inst alu_email_inst , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.id_anio cpu_id_anio , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin , cpu.fec_ini cpu_fec_ini , cpu.fec_fin cpu_fec_fin , cpu.fec_ini_ing cpu_fec_ini_ing , cpu.fec_fin_ing cpu_fec_fin_ing  ";
		sql = sql + " from not_promedio_com not";
		sql = sql + " left join aca_competencia_dc com on com.id = not.id_com ";
		sql = sql + " left join ges_trabajador tra on tra.id = not.id_tra ";
		sql = sql + " left join alu_alumno alu on alu.id = not.id_alu ";
		sql = sql + " left join col_per_uni cpu on cpu.id = not.id_cpu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<PromedioCom>() {

			@Override
			public PromedioCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				PromedioCom promediocom= rsToEntity(rs,"not_");
				CompetenciaDc competenciadc = new CompetenciaDc();  
				competenciadc.setId(rs.getInt("com_id")) ;  
				competenciadc.setId_dcare(rs.getInt("com_id_dcare")) ;  
				competenciadc.setNom(rs.getString("com_nom")) ;  
				competenciadc.setPeso(rs.getBigDecimal("com_peso")) ;  
				competenciadc.setOrden(rs.getInt("com_orden")) ;  
				promediocom.setCompetenciaDc(competenciadc);
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
				promediocom.setTrabajador(trabajador);
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				//alumno.setId_usr(rs.getInt("alu_id_usr")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setEmail_inst(rs.getString("alu_email_inst")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				promediocom.setAlumno(alumno);
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
				promediocom.setPerUni(peruni);
				return promediocom;
			}

		});

	}	




	// funciones privadas utilitarias para PromedioCom

	private PromedioCom rsToEntity(ResultSet rs,String alias) throws SQLException {
		PromedioCom promedio_com = new PromedioCom();

		promedio_com.setId(rs.getInt( alias + "id"));
		promedio_com.setId_com(rs.getInt( alias + "id_com"));
		promedio_com.setId_au(rs.getInt( alias + "id_au"));
		promedio_com.setId_tra(rs.getInt( alias + "id_tra"));
		promedio_com.setId_alu(rs.getInt( alias + "id_alu"));
		promedio_com.setId_cpu(rs.getInt( alias + "id_cpu"));
		promedio_com.setFec(rs.getDate( alias + "fec"));
		promedio_com.setProm(rs.getBigDecimal( alias + "prom"));
		promedio_com.setEst(rs.getString( alias + "est"));
								
		return promedio_com;

	}
	
}
