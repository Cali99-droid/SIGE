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

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Ciclo;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Turno;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Reserva;
import com.tesla.colegio.model.Sucursal;
import com.tesla.colegio.model.TipPeriodo;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.ModalidadEstudio;
import com.tesla.colegio.model.Nivel;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface AulaDAO.
 * @author MV
 *
 */
public class AulaDAOImpl{
	
	final static Logger logger = Logger.getLogger(AulaDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Aula aula) {
		if (aula.getId() != null) {
			// update
			String sql = "UPDATE col_aula "
						+ "SET id_per=?, "
						+ "id_cme=?, "
						+ "id_cic=?, "
						+ "id_grad=?, "
						+ "id_secc_ant=?, "
						+ "id_tur=?, "
						+ "secc=?, "
						+ "cap=?, "
						+ "des_classroom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			 jdbcTemplate.update(sql, 
						aula.getId_per(),
						aula.getId_cme(),
						aula.getId_cic(),
						aula.getId_grad(),
						aula.getId_secc_ant(),
						aula.getId_tur(),
						aula.getSecc(),
						aula.getCap(),
						aula.getDes_classroom(),
						aula.getEst(),
						aula.getUsr_act(),
						new java.util.Date(),
						aula.getId()); 
			 return aula.getId();

		} else {
			// insert
			String sql = "insert into col_aula ("
						+ "id_per, "
						+ "id_cme, "
						+ "id_cic, "
						+ "id_grad, "
						+ "id_secc_ant, "
						+ "id_tur, "
						+ "secc, "
						+ "cap, "
						+ "des_classroom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				aula.getId_per(),
				aula.getId_cme(),
				aula.getId_cic(),
				aula.getId_grad(),
				aula.getId_secc_ant(),
				aula.getId_tur(),
				aula.getSecc(),
				aula.getCap(),
				aula.getDes_classroom(),
				aula.getEst(),
				aula.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from col_aula where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<Aula> list() {
		String sql = "select * from col_aula";
		
		//logger.info(sql);
		
		List<Aula> listAula = jdbcTemplate.query(sql, new RowMapper<Aula>() {

			
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listAula;
	}

	
	public Aula get(int id) {
		String sql = "select * from col_aula WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Aula>() {

			
			public Aula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public Aula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select aula.id aula_id, aula.id_per aula_id_per, aula.id_cme aula_id_cme, aula.id_cic aula_id_cic , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ,aula.est aula_est, aula.des_classroom aula_des_classroom, aula.id_classroom aula_id_classroom ";
		if (aTablas.contains("per_periodo"))
			sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat, pee.id_suc pee_id_suc  ";
		if (aTablas.contains("cat_grad"))
			sql = sql + ", grad.id grad_id  , grad.id_gra_ant grad_id_gra_ant, grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		if (aTablas.contains("col_turno"))
			sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.nom  niv_nom  ";
	
		sql = sql + " from col_aula aula "; 
		if (aTablas.contains("per_periodo"))
			sql = sql + " left join per_periodo pee on pee.id = aula.id_per ";
		if (aTablas.contains("cat_grad"))
			sql = sql + " left join cat_grad grad on grad.id = aula.id_grad ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = grad.id_nvl ";
		if (aTablas.contains("col_turno"))
			sql = sql + " left join col_turno turno on turno.id = aula.id_tur ";
		sql = sql + " where aula.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<Aula>() {
		
			
			public Aula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Aula aula= rsToEntity(rs,"aula_");
					if (aTablas.contains("per_periodo")){
						Periodo periodo = new Periodo();  
							periodo.setId(rs.getInt("pee_id")) ;  
							periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
							periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
							periodo.setId_suc(rs.getInt("pee_id_suc")) ;
							periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
							periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
							periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
							periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
							aula.setPeriodo(periodo);
					}
					if (aTablas.contains("cat_grad")){
								
						Grad grad = new Grad();  
							grad.setId(rs.getInt("grad_id")) ;  
							grad.setId_nvl(rs.getInt("grad_id_nvl")) ;
							grad.setId_gra_ant(rs.getInt("grad_id_gra_ant"));
							grad.setNom(rs.getString("grad_nom")) ;
							
							if (aTablas.contains("cat_nivel")){
								
								Nivel nivel = new Nivel();
								nivel.setNom(rs.getString("niv_nom"));
								grad.setNivel(nivel);
							}

								
							aula.setGrad(grad);
					}
					if (aTablas.contains("col_turno")){
						Turno turno = new Turno();  
							turno.setId(rs.getInt("turno_id")) ;  
							turno.setNom(rs.getString("turno_nom")) ;  
							turno.setCod(rs.getString("turno_cod")) ;  
							aula.setTurno(turno);
					}
							return aula;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public Aula getByParams(Param param) {

		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Aula>() {
			
			public Aula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<Aula> listByParams(Param param, String[] order) {

		String sql = "select * from col_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	/*public List<Aula> listFullByParams(Aula aula, String[] order) {
	
		return listFullByParams(Param.toParam("aula",aula), order);
	
	}*/	
	
	
	public List<Aula> listFullByParams(Param param, String[] order) {

		String sql = "select aula.id aula_id, aula.id_per aula_id_per, aula.id_cme aula_id_cme, aula.id_cme aula_id_cme , aula.id_cic aula_id_cic, aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ,aula.est aula_est, aula.des_classroom aula_des_classroom, aula.id_classroom aula_id_classroom  ";
		sql = sql + ", pee.id pee_id  , pee.id_anio pee_id_anio , pee.id_srv pee_id_srv , pee.id_tpe pee_id_tpe , pee.fec_ini pee_fec_ini , pee.fec_fin pee_fec_fin , pee.fec_cie_mat pee_fec_cie_mat  ";
		sql = sql + ", suc.id suc_id, suc.nom suc_nom ";
		sql = sql + ", cic.id cic_id, cic.nom cic_nom";
		sql = sql + ", cme.nom cme_nom";
		sql = sql + ", ctp.id ctp_id, ctp.nom ctp_nom";
		sql = sql + ", grad.id grad_id  , grad.id_nvl grad_id_nvl , grad.nom grad_nom  ";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom";
		sql = sql + ", turno.id turno_id  , turno.nom turno_nom , turno.cod turno_cod  ";
		sql = sql + " from col_aula aula";
		sql = sql + " inner join col_ciclo cic ON aula.id_cic=cic.id";
		sql = sql + " inner join per_periodo pee on pee.id = cic.id_per ";
		sql = sql + " inner join ges_sucursal suc on pee.id_suc=suc.id ";
		sql = sql + " inner join cat_grad grad on grad.id = aula.id_grad ";
		sql = sql + " inner join cat_nivel niv on grad.id_nvl=niv.id ";
		sql = sql + " INNER JOIN `col_ciclo_turno` cit ON cic.`id`=cit.`id_cic` ";
		sql = sql + " INNER JOIN `col_turno_aula` cta ON cta.`id_au`=aula.id AND cta.`id_cit`=cit.`id`";
		sql = sql + " inner join col_turno turno on turno.id = cit.id_tur ";
		sql = sql + " inner join cat_tip_periodo ctp on pee.id_tpe=ctp.id ";
		sql = sql + " LEFT join cat_modalidad_estudio cme on aula.id_cme=cme.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<Aula>() {

			
			public Aula mapRow(ResultSet rs, int rowNum) throws SQLException {
				Aula aula= rsToEntity(rs,"aula_");
				Periodo periodo = new Periodo();  
				periodo.setId(rs.getInt("pee_id")) ;  
				periodo.setId_anio(rs.getInt("pee_id_anio")) ;  
				periodo.setId_srv(rs.getInt("pee_id_srv")) ;  
				periodo.setId_tpe(rs.getInt("pee_id_tpe")) ;  
				periodo.setFec_ini(rs.getDate("pee_fec_ini")) ;  
				periodo.setFec_fin(rs.getDate("pee_fec_fin")) ;  
				periodo.setFec_cie_mat(rs.getDate("pee_fec_cie_mat")) ;  
				TipPeriodo tipPeriodo = new TipPeriodo();
				tipPeriodo.setId(rs.getInt("ctp_id"));
				tipPeriodo.setNom(rs.getString("ctp_nom"));
				periodo.setTipPeriodo(tipPeriodo);
				Sucursal sucursal = new Sucursal();
				sucursal.setId(rs.getInt("suc_id"));
				sucursal.setNom(rs.getString("suc_nom"));
				periodo.setSucursal(sucursal);
				aula.setPeriodo(periodo);
				Ciclo ciclo = new Ciclo();
				ciclo.setId(rs.getInt("cic_id"));
				ciclo.setNom(rs.getString("cic_nom"));
				aula.setCiclo(ciclo);
				Grad grad = new Grad();  
				grad.setId(rs.getInt("grad_id")) ;  
				grad.setId_nvl(rs.getInt("grad_id_nvl")) ;  
				grad.setNom(rs.getString("grad_nom")) ;  
				Nivel nivel = new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				nivel.setNom(rs.getString("niv_nom"));
				grad.setNivel(nivel);
				aula.setGrad(grad);
				Turno turno = new Turno();  
				turno.setId(rs.getInt("turno_id")) ;  
				turno.setNom(rs.getString("turno_nom")) ;  
				turno.setCod(rs.getString("turno_cod")) ;  
				aula.setTurno(turno);
				ModalidadEstudio modalidadEstudio = new ModalidadEstudio();
				modalidadEstudio.setNom(rs.getString("cme_nom"));
				aula.setModalidadEstudio(modalidadEstudio);
				return aula;
			}

		});

	}	


	public List<Curso> getListCurso(Param param, String[] order) {
		String sql = "select * from col_curso " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Curso>() {

			
			public Curso mapRow(ResultSet rs, int rowNum) throws SQLException {
				Curso curso = new Curso();

				curso.setId(rs.getInt("id"));
				//curso.setId_ar(rs.getInt("id_ar"));
				curso.setNom(rs.getString("nom"));
				//curso.setId_nvl(rs.getInt("id_nvl"));
				//curso.setId_au(rs.getInt("id_au"));
				//curso.setId_prof(rs.getInt("id_prof"));
				//curso.setAnio(rs.getInt("anio"));
				//curso.setEva(rs.getString("eva"));
				//curso.setCom(rs.getString("com"));
				//curso.setProm(rs.getString("prom"));
				//curso.setSim(rs.getString("sim"));
				curso.setEst(rs.getString("est"));
												
				return curso;
			}

		});	
	}
	public List<Reserva> getListReserva(Param param, String[] order) {
		String sql = "select * from mat_reserva " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Reserva>() {

			
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

		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<Matricula>() {

			
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


	// funciones privadas utilitarias para Aula

	private Aula rsToEntity(ResultSet rs,String alias) throws SQLException {
		Aula aula = new Aula();

		aula.setId(rs.getInt( alias + "id"));
		aula.setId_per(rs.getInt( alias + "id_per"));
		aula.setId_cme(rs.getInt( alias + "id_cme"));
		aula.setId_cic(rs.getInt( alias + "id_cic"));
		aula.setId_grad(rs.getInt( alias + "id_grad"));
		aula.setId_secc_ant(rs.getInt( alias + "id_secc_ant"));
		aula.setId_tur(rs.getInt( alias + "id_tur"));
		aula.setSecc(rs.getString( alias + "secc"));
		aula.setCap(rs.getInt( alias + "cap"));
		aula.setEst(rs.getString( alias + "est"));
		aula.setDes_classroom(rs.getString( alias + "des_classroom"));		
		aula.setId_classroom(rs.getString( alias + "id_classroom"));
		return aula;

	}
	
}
