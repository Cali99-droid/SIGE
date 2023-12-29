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
import com.tesla.colegio.model.TutorAula;

import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Trabajador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface TutorAulaDAO.
 * @author MV
 *
 */
public class TutorAulaDAOImpl{
	final static Logger logger = Logger.getLogger(TutorAulaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(TutorAula tutor_aula) {
		if (tutor_aula.getId() != null) {
			// update
			String sql = "UPDATE col_tutor_aula "
						+ "SET id_au=?, "
						+ "id_tra=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						tutor_aula.getId_au(),
						tutor_aula.getId_tra(),
						tutor_aula.getEst(),
						tutor_aula.getUsr_act(),
						new java.util.Date(),
						tutor_aula.getId()); 
			return tutor_aula.getId();

		} else {
			// insert
			String sql = "insert into col_tutor_aula ("
						+ "id_au, "
						+ "id_tra, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				tutor_aula.getId_au(),
				tutor_aula.getId_tra(),
				tutor_aula.getEst(),
				tutor_aula.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_tutor_aula where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<TutorAula> list() {
		String sql = "select * from col_tutor_aula";
		
		//logger.info(sql);
		
		List<TutorAula> listTutorAula = jdbcTemplate.query(sql, new RowMapper<TutorAula>() {

			@Override
			public TutorAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listTutorAula;
	}

	public TutorAula get(int id) {
		String sql = "select * from col_tutor_aula WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TutorAula>() {

			@Override
			public TutorAula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public TutorAula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cta.id cta_id, cta.id_au cta_id_au , cta.id_tra cta_id_tra  ,cta.est cta_est ";
		if (aTablas.contains("col_aula"))
			sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from col_tutor_aula cta "; 
		if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula aula on aula.id = cta.id_au ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = cta.id_tra ";
		sql = sql + " where cta.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<TutorAula>() {
		
			@Override
			public TutorAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TutorAula tutoraula= rsToEntity(rs,"cta_");
					if (aTablas.contains("col_aula")){
						Aula aula = new Aula();  
							aula.setId(rs.getInt("aula_id")) ;  
							aula.setId_per(rs.getInt("aula_id_per")) ;  
							aula.setId_grad(rs.getInt("aula_id_grad")) ;  
							aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
							aula.setId_tur(rs.getInt("aula_id_tur")) ;  
							aula.setSecc(rs.getString("aula_secc")) ;  
							aula.setCap(rs.getInt("aula_cap")) ;  
							tutoraula.setAula(aula);
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
							tutoraula.setTrabajador(trabajador);
					}
							return tutoraula;
				}
				
				return null;
			}
			
		});


	}		
	
	public TutorAula getByParams(Param param) {

		String sql = "select * from col_tutor_aula " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<TutorAula>() {
			@Override
			public TutorAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<TutorAula> listByParams(Param param, String[] order) {

		String sql = "select * from col_tutor_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TutorAula>() {

			@Override
			public TutorAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	/*public List<TutorAula> listFullByParams(TutorAula tutoraula, String[] order) {
	
		return listFullByParams(Param.toParam("cta",tutoraula), order);
	
	}	*/
	
	public List<TutorAula> listFullByParams(Param param, String[] order) {

		String sql = "select cta.id cta_id, cta.id_au cta_id_au , cta.id_tra cta_id_tra  ,cta.est cta_est ";
		sql = sql + ", aula.id aula_id  , aula.id_per aula_id_per , aula.id_grad aula_id_grad , aula.id_secc_ant aula_id_secc_ant , aula.id_tur aula_id_tur , aula.secc aula_secc , aula.cap aula_cap  ";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom, gra.nom gra_nom ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_tutor_aula cta";
		sql = sql + " left join col_aula aula on aula.id = cta.id_au ";
		sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = cta.id_tra ";
		sql = sql + " left join cat_grad gra on aula.id_grad=gra.id ";
		sql = sql + " left join cat_nivel niv on niv.id=gra.id_nvl ";
		sql = sql + " left join per_periodo per on aula.id_per=per.id ";
		
		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<TutorAula>() {

			@Override
			public TutorAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				TutorAula tutoraula= rsToEntity(rs,"cta_");
				Aula aula = new Aula();  
				aula.setId(rs.getInt("aula_id")) ;  
				aula.setId_per(rs.getInt("aula_id_per")) ;  
				aula.setId_grad(rs.getInt("aula_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("aula_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("aula_id_tur")) ;  
				aula.setSecc(rs.getString("aula_secc")) ;  
				aula.setCap(rs.getInt("aula_cap")) ;  
				tutoraula.setAula(aula);
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
				tutoraula.setTrabajador(trabajador);
				Nivel nivel= new Nivel();
				nivel.setId(rs.getInt("niv_id"));
				nivel.setNom(rs.getString("niv_nom"));
				tutoraula.setNivel(nivel);
				Grad grad = new Grad();
				grad.setNom(rs.getString("gra_nom"));
				tutoraula.setGrad(grad);
				return tutoraula;
			}

		});

	}	




	// funciones privadas utilitarias para TutorAula

	private TutorAula rsToEntity(ResultSet rs,String alias) throws SQLException {
		TutorAula tutor_aula = new TutorAula();

		tutor_aula.setId(rs.getInt( alias + "id"));
		tutor_aula.setId_au(rs.getInt( alias + "id_au"));
		tutor_aula.setId_tra(rs.getInt( alias + "id_tra"));
		tutor_aula.setEst(rs.getString( alias + "est"));
								
		return tutor_aula;

	}
	
}
