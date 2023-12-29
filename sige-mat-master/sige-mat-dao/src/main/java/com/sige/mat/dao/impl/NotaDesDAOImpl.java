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
import com.tesla.colegio.model.NotaDes;

import com.tesla.colegio.model.DesempenioAula;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Alumno;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaDesDAO.
 * @author MV
 *
 */
public class NotaDesDAOImpl{
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
	public int saveOrUpdate(NotaDes nota_des) {
		if (nota_des.getId() != null) {
			// update
			String sql = "UPDATE not_nota_des "
						+ "SET id_desau=?, "
						+ "id_tra=?, "
						+ "id_alu=?, "
						+ "fec=?, "
						+ "nota=?, "
						+ "prom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						nota_des.getId_desau(),
						nota_des.getId_tra(),
						nota_des.getId_alu(),
						nota_des.getFec(),
						nota_des.getNota(),
						nota_des.getProm(),
						nota_des.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						nota_des.getId()); 
			return nota_des.getId();

		} else {
			// insert
			String sql = "insert into not_nota_des ("
						+ "id_desau, "
						+ "id_tra, "
						+ "id_alu, "
						+ "fec, "
						+ "nota, "
						+ "prom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				nota_des.getId_desau(),
				nota_des.getId_tra(),
				nota_des.getId_alu(),
				nota_des.getFec(),
				nota_des.getNota(),
				nota_des.getProm(),
				nota_des.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from not_nota_des where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<NotaDes> list() {
		String sql = "select * from not_nota_des";
		
		System.out.println(sql);
		
		List<NotaDes> listNotaDes = jdbcTemplate.query(sql, new RowMapper<NotaDes>() {

			@Override
			public NotaDes mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaDes;
	}

	public NotaDes get(int id) {
		String sql = "select * from not_nota_des WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaDes>() {

			@Override
			public NotaDes extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NotaDes getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select not.id not_id, not.id_desau not_id_desau , not.id_tra not_id_tra , not.id_alu not_id_alu , not.fec not_fec , not.nota not_nota , not.prom not_prom  ,not.est not_est ";
		if (aTablas.contains("aca_desempenio_aula"))
			sql = sql + ", desau.id desau_id  , desau.id_desdc desau_id_desdc , desau.id_cpu desau_id_cpu , desau.id_au desau_id_au  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_usr alu_id_usr , alu.cod alu_cod , alu.num_hij alu_num_hij , alu.email_inst alu_email_inst , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from not_nota_des not "; 
		if (aTablas.contains("aca_desempenio_aula"))
			sql = sql + " left join aca_desempenio_aula desau on desau.id = not.id_desau ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = not.id_tra ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = not.id_alu ";
		sql = sql + " where not.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaDes>() {
		
			@Override
			public NotaDes extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaDes notades= rsToEntity(rs,"not_");
					if (aTablas.contains("aca_desempenio_aula")){
						DesempenioAula desempenioaula = new DesempenioAula();  
							desempenioaula.setId(rs.getInt("desau_id")) ;  
							desempenioaula.setId_desdc(rs.getInt("desau_id_desdc")) ;  
							desempenioaula.setId_cpu(rs.getInt("desau_id_cpu")) ;  
							desempenioaula.setId_au(rs.getInt("desau_id_au")) ;  
							notades.setDesempenioAula(desempenioaula);
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
							notades.setTrabajador(trabajador);
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
							notades.setAlumno(alumno);
					}
							return notades;
				}
				
				return null;
			}
			
		});


	}		
	
	public NotaDes getByParams(Param param) {

		String sql = "select * from not_nota_des " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaDes>() {
			@Override
			public NotaDes extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NotaDes> listByParams(Param param, String[] order) {

		String sql = "select * from not_nota_des " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaDes>() {

			@Override
			public NotaDes mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NotaDes> listFullByParams(NotaDes notades, String[] order) {
	
		return listFullByParams(Param.toParam("not",notades), order);
	
	}	
	
	public List<NotaDes> listFullByParams(Param param, String[] order) {

		String sql = "select not.id not_id, not.id_desau not_id_desau , not.id_tra not_id_tra , not.id_alu not_id_alu , not.fec not_fec , not.nota not_nota , not.prom not_prom  ,not.est not_est ";
		sql = sql + ", desau.id desau_id  , desau.id_desdc desau_id_desdc , desau.id_cpu desau_id_cpu , desau.id_au desau_id_au  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", alu.id alu_id  , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_usr alu_id_usr , alu.cod alu_cod , alu.num_hij alu_num_hij , alu.email_inst alu_email_inst , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + " from not_nota_des not";
		sql = sql + " left join aca_desempenio_aula desau on desau.id = not.id_desau ";
		sql = sql + " left join ges_trabajador tra on tra.id = not.id_tra ";
		sql = sql + " left join alu_alumno alu on alu.id = not.id_alu ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaDes>() {

			@Override
			public NotaDes mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaDes notades= rsToEntity(rs,"not_");
				DesempenioAula desempenioaula = new DesempenioAula();  
				desempenioaula.setId(rs.getInt("desau_id")) ;  
				desempenioaula.setId_desdc(rs.getInt("desau_id_desdc")) ;  
				desempenioaula.setId_cpu(rs.getInt("desau_id_cpu")) ;  
				desempenioaula.setId_au(rs.getInt("desau_id_au")) ;  
				notades.setDesempenioAula(desempenioaula);
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
				notades.setTrabajador(trabajador);
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
				notades.setAlumno(alumno);
				return notades;
			}

		});

	}	




	// funciones privadas utilitarias para NotaDes

	private NotaDes rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaDes nota_des = new NotaDes();

		nota_des.setId(rs.getInt( alias + "id"));
		nota_des.setId_desau(rs.getInt( alias + "id_desau"));
		nota_des.setId_tra(rs.getInt( alias + "id_tra"));
		nota_des.setId_alu(rs.getInt( alias + "id_alu"));
		nota_des.setFec(rs.getDate( alias + "fec"));
		nota_des.setNota(rs.getInt( alias + "nota"));
		nota_des.setProm(rs.getBigDecimal( alias + "prom"));
		nota_des.setEst(rs.getString( alias + "est"));
								
		return nota_des;

	}
	
}
