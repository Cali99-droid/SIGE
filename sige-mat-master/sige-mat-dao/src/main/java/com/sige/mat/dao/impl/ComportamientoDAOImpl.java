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
import com.tesla.colegio.model.Comportamiento;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.PerUni;
import com.tesla.colegio.model.CapCom;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ComportamientoDAO.
 * @author MV
 *
 */
public class ComportamientoDAOImpl{
	final static Logger logger = Logger.getLogger(ComportamientoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Comportamiento comportamiento) {
		if (comportamiento.getId() != null) {
			// update
			String sql = "UPDATE not_comportamiento "
						+ "SET id_tra=?, "
						+ "id_alu=?, "
						+ "id_au=?, "
						+ "id_cpu=?, "
						+ "prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						comportamiento.getId_tra(),
						comportamiento.getId_alu(),
						comportamiento.getId_au(),
						comportamiento.getId_cpu(),
						comportamiento.getProm(),
						comportamiento.getEst(),
						comportamiento.getUsr_act(),
						new java.util.Date(),
						comportamiento.getId()); 
			return comportamiento.getId();

		} else {
			// insert
			String sql = "insert into not_comportamiento ("
						+ "id_tra, "
						+ "id_alu, "
						+ "id_au, "
						+ "id_cpu, "
						+ "prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				comportamiento.getId_tra(),
				comportamiento.getId_alu(),
				comportamiento.getId_au(),
				comportamiento.getId_cpu(),
				comportamiento.getProm(),
				comportamiento.getEst(),
				comportamiento.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id_au, int id_cpu) {
		String sql = "delete from not_comportamiento where id_au=? and id_cpu=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id_au, id_cpu);
	}

	public List<Comportamiento> list() {
		String sql = "select * from not_comportamiento";
		
		//logger.info(sql);
		
		List<Comportamiento> listComportamiento = jdbcTemplate.query(sql, new RowMapper<Comportamiento>() {

			@Override
			public Comportamiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listComportamiento;
	}

	public Comportamiento get(int id) {
		String sql = "select * from not_comportamiento WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Comportamiento>() {

			@Override
			public Comportamiento extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Comportamiento getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select nc.id nc_id, nc.id_tra nc_id_tra , nc.id_alu nc_id_alu , nc.id_au nc_id_au , nc.id_cpu nc_id_cpu , nc.prom nc_prom  ,nc.est nc_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin  ";
	
		sql = sql + " from not_comportamiento nc "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = nc.id_tra ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = nc.id_alu ";
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = nc.id_au ";
		if (aTablas.contains("col_per_uni"))
			sql = sql + " left join col_per_uni cpu on cpu.id = nc.id_cpu ";
		sql = sql + " where nc.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Comportamiento>() {
		
			@Override
			public Comportamiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Comportamiento comportamiento= rsToEntity(rs,"nc_");
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
							comportamiento.setTrabajador(trabajador);
					}
					if (aTablas.contains("alu_alumno")){
						Alumno alumno = new Alumno();  
							alumno.setId(rs.getInt("alu_id")) ;  
							alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
							alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
							alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
							alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
							alumno.setId_tap(rs.getString("alu_id_tap")) ;  
							alumno.setId_gen(rs.getString("alu_id_gen")) ;  
							alumno.setCod(rs.getString("alu_cod")) ;  
							alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
							alumno.setNom(rs.getString("alu_nom")) ;  
							alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
							alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
							//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							comportamiento.setAlumno(alumno);
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
							comportamiento.setAula(aula);
					}
					if (aTablas.contains("col_per_uni")){
						PerUni peruni = new PerUni();  
							peruni.setId(rs.getInt("cpu_id")) ;  
							peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
							peruni.setNump(rs.getInt("cpu_nump")) ;  
							peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
							peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
							comportamiento.setPerUni(peruni);
					}
							return comportamiento;
				}
				
				return null;
			}
			
		});


	}		
	
	public Comportamiento getByParams(Param param) {

		String sql = "select * from not_comportamiento " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Comportamiento>() {
			@Override
			public Comportamiento extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Comportamiento> listByParams(Param param, String[] order) {

		String sql = "select * from not_comportamiento " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		////logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Comportamiento>() {

			@Override
			public Comportamiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Comportamiento> listFullByParams(Comportamiento comportamiento, String[] order) {
	
		return listFullByParams(Param.toParam("nc",comportamiento), order);
	
	}	
	
	public List<Comportamiento> listFullByParams(Param param, String[] order) {

		String sql = "select nc.id nc_id, nc.id_tra nc_id_tra , nc.id_alu nc_id_alu , nc.id_au nc_id_au , nc.id_cpu nc_id_cpu , nc.prom nc_prom  ,nc.est nc_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", cpu.id cpu_id  , cpu.id_cpa cpu_id_cpa , cpu.nump cpu_nump , cpu.numu_ini cpu_numu_ini , cpu.numu_fin cpu_numu_fin  ";
		sql = sql + " from not_comportamiento nc";
		sql = sql + " left join ges_trabajador tra on tra.id = nc.id_tra ";
		sql = sql + " left join alu_alumno alu on alu.id = nc.id_alu ";
		sql = sql + " left join col_aula au on au.id = nc.id_au ";
		sql = sql + " left join col_per_uni cpu on cpu.id = nc.id_cpu ";
		sql = sql + " left join per_periodo per on au.id_per=per.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Comportamiento>() {

			@Override
			public Comportamiento mapRow(ResultSet rs, int rowNum) throws SQLException {
				Comportamiento comportamiento= rsToEntity(rs,"nc_");
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
				comportamiento.setTrabajador(trabajador);
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ;  
				alumno.setCod(rs.getString("alu_cod")) ;  
				alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				//alumno.setFec_nac(rs.getString("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				comportamiento.setAlumno(alumno);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				comportamiento.setAula(aula);
				PerUni peruni = new PerUni();  
				peruni.setId(rs.getInt("cpu_id")) ;  
				peruni.setId_cpa(rs.getInt("cpu_id_cpa")) ;  
				peruni.setNump(rs.getInt("cpu_nump")) ;  
				peruni.setNumu_ini(rs.getInt("cpu_numu_ini")) ;  
				peruni.setNumu_fin(rs.getInt("cpu_numu_fin")) ;  
				comportamiento.setPerUni(peruni);
				return comportamiento;
			}

		});

	}	


	public List<CapCom> getListCapCom(Param param, String[] order) {
		String sql = "select * from not_cap_com " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CapCom>() {

			@Override
			public CapCom mapRow(ResultSet rs, int rowNum) throws SQLException {
				CapCom cap_com = new CapCom();

				cap_com.setId(rs.getInt("id"));
				cap_com.setId_nc(rs.getInt("id_nc"));
				cap_com.setId_cap(rs.getInt("id_cap"));
				cap_com.setNota(rs.getInt("nota"));
				cap_com.setFec(rs.getDate("fec"));
				cap_com.setEst(rs.getString("est"));
												
				return cap_com;
			}

		});	
	}


	// funciones privadas utilitarias para Comportamiento

	private Comportamiento rsToEntity(ResultSet rs,String alias) throws SQLException {
		Comportamiento comportamiento = new Comportamiento();

		comportamiento.setId(rs.getInt( alias + "id"));
		comportamiento.setId_tra(rs.getInt( alias + "id_tra"));
		comportamiento.setId_alu(rs.getInt( alias + "id_alu"));
		comportamiento.setId_au(rs.getInt( alias + "id_au"));
		comportamiento.setId_cpu(rs.getInt( alias + "id_cpu"));
		comportamiento.setProm(rs.getBigDecimal( alias + "prom"));
		comportamiento.setEst(rs.getString( alias + "est"));
								
		return comportamiento;

	}
	
}
