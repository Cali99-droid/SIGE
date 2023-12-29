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
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Periodo;
import com.tesla.colegio.model.CursoAnio;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Curso;
import com.tesla.colegio.model.Trabajador;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface CursoAulaDAO.
 * @author MV
 *
 */
public class CursoAulaDAOImpl{
	final static Logger logger = Logger.getLogger(CursoAulaDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoAula curso_aula) {
		if (curso_aula.getId() != null) {
			// update
			String sql = "UPDATE col_curso_aula "
						+ "SET id_cua=?, "
					    + "id_caa=?, "
						+ "id_au=?, "
						+ "id_tra=?, "
						+ "cod_classroom=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			jdbcTemplate.update(sql, 
						curso_aula.getId_cua(),
						curso_aula.getId_caa(),
						curso_aula.getId_au(),
						curso_aula.getId_tra(),
						curso_aula.getCod_classroom(),
						curso_aula.getEst(),
						curso_aula.getUsr_act(),
						new java.util.Date(),
						curso_aula.getId()); 
			return curso_aula.getId();

		} else {
			// insert
			String sql = "insert into col_curso_aula ("
						+ "id_cua, "
						+ "id_caa, "
						+ "id_au, "
						+ "id_tra, "
						+ "cod_classroom, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				curso_aula.getId_cua(),
				curso_aula.getId_caa(),
				curso_aula.getId_au(),
				curso_aula.getId_tra(),
				curso_aula.getCod_classroom(),
				curso_aula.getEst(),
				curso_aula.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_curso_aula where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<CursoAula> list() {
		String sql = "select * from col_curso_aula";
		
		//logger.info(sql);
		
		List<CursoAula> listCursoAula = jdbcTemplate.query(sql, new RowMapper<CursoAula>() {

			@Override
			public CursoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listCursoAula;
	}

	public CursoAula get(int id) {
		String sql = "select * from col_curso_aula WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAula>() {

			@Override
			public CursoAula extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public CursoAula getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select cca.id cca_id, cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra  ,cca.est cca_est ";
		
		if (aTablas.contains("col_curso_anio")){
			sql = sql + ", cua.id cua_id  , cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ";
			sql = sql + ", per.id_anio per_id_anio,per.id_niv per_id_niv ";
		}
		
		if (aTablas.contains("col_aula"))
			sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
	
		sql = sql + " from col_curso_aula cca "; 
		if (aTablas.contains("col_curso_anio")){
			sql = sql + " left join col_curso_anio cua on cua.id = cca.id_cua ";
			sql = sql + " left join per_periodo per on per.id = cua.id_per ";
		}if (aTablas.contains("col_aula"))
			sql = sql + " left join col_aula au on au.id = cca.id_au ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = cca.id_tra ";
		sql = sql + " where cca.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAula>() {
		
			@Override
			public CursoAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoAula cursoaula= rsToEntity(rs,"cca_");
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
							
							Periodo periodo = new Periodo();
							periodo.setId_anio(rs.getInt("per_id_anio")) ;
							periodo.setId_niv(rs.getInt("per_id_niv")) ;
							cursoanio.setPeriodo(periodo);
							cursoaula.setCursoAnio(cursoanio);
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
							cursoaula.setAula(aula);
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
							cursoaula.setTrabajador(trabajador);
					}
							return cursoaula;
				}
				
				return null;
			}
			
		});


	}		
	
	public CursoAula getByParams(Param param) {

		String sql = "select * from col_curso_aula " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoAula>() {
			@Override
			public CursoAula extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<CursoAula> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_aula " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAula>() {

			@Override
			public CursoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<CursoAula> listFullByParams(CursoAula cursoaula, String[] order) {
	
		return listFullByParams(Param.toParam("cca",cursoaula), order);
	
	}	
	
	public List<CursoAula> listFullByParams(Param param, String[] order) {

		String sql = "select cca.id cca_id, cca.id_caa cca_id_caa, cca.id_cua cca_id_cua , cca.id_au cca_id_au , cca.id_tra cca_id_tra  ,cca.est cca_est, cca.cod_classroom cca_cod_classroom ";
		sql = sql + ", cua.id cua_id  , cua.id_per cua_id_per , cua.id_gra cua_id_gra , cua.id_caa cua_id_caa , cua.id_cur cua_id_cur , cua.peso cua_peso , cua.orden cua_orden , cua.flg_prom cua_flg_prom  ";
		sql = sql + ", cur.id cur_id, cur.nom cur_nom ";
		sql = sql + ", per.id per_id, per.id_anio per_id_anio, per.id_suc per_id_suc ";
		sql = sql + ", gra.id gra_id, gra.nom gra_nom ";
		sql = sql + ", niv.id niv_id, niv.nom niv_nom ";
		sql = sql + ", au.id au_id  , au.id_per au_id_per , au.id_grad au_id_grad , au.id_secc_ant au_id_secc_ant , au.id_tur au_id_tur , au.secc au_secc , au.cap au_cap  ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_curso_aula cca";
		sql = sql + " left join col_curso_anio cua on cua.id = cca.id_cua ";
		sql = sql + " left join col_aula au on au.id = cca.id_au ";
		sql = sql + " left join aeedu_asistencia.ges_trabajador tra on tra.id = cca.id_tra ";
		sql = sql + " left join cat_curso cur on cua.id_cur=cur.id ";
		sql = sql + " left join per_periodo per on cua.id_per=per.id ";
		sql = sql + " left join cat_grad gra on cua.id_gra=gra.id and gra.id=au.id_grad ";
		sql = sql + " left join col_area_anio area_anio on area_anio.id=cua.id_caa ";
		//sql = sql + " left join cat_nivel niv on area_anio.id_niv=niv.id ";
		sql = sql + " left join cat_nivel niv on per.id_niv=niv.id ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<CursoAula>() {

			@Override
			public CursoAula mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoAula cursoaula= rsToEntity(rs,"cca_");
				CursoAnio cursoanio = new CursoAnio();  
				cursoanio.setId(rs.getInt("cua_id")) ;  
				cursoanio.setId_per(rs.getInt("cua_id_per")) ;  
				cursoanio.setId_gra(rs.getInt("cua_id_gra")) ;  
				cursoanio.setId_caa(rs.getInt("cua_id_caa")) ;  
				cursoanio.setId_cur(rs.getInt("cua_id_cur")) ;  
				cursoanio.setPeso(rs.getInt("cua_peso")) ;  
				cursoanio.setOrden(rs.getInt("cua_orden")) ;  
				cursoanio.setFlg_prom(rs.getString("cua_flg_prom")) ;  
				cursoaula.setCursoAnio(cursoanio);
				Aula aula = new Aula();  
				aula.setId(rs.getInt("au_id")) ;  
				aula.setId_per(rs.getInt("au_id_per")) ;  
				aula.setId_grad(rs.getInt("au_id_grad")) ;  
				aula.setId_secc_ant(rs.getInt("au_id_secc_ant")) ;  
				aula.setId_tur(rs.getInt("au_id_tur")) ;  
				aula.setSecc(rs.getString("au_secc")) ;  
				aula.setCap(rs.getInt("au_cap")) ;  
				cursoaula.setAula(aula);
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
				cursoaula.setTrabajador(trabajador);
				Periodo periodo= new Periodo();
				periodo.setId(rs.getInt("per_id"));
				periodo.setId_anio(rs.getInt("per_id_anio"));
				periodo.setId_suc(rs.getInt("per_id_suc"));
				cursoaula.setPeriodo(periodo);
				Grad grad= new Grad();
				grad.setId(rs.getInt("gra_id"));
				grad.setNom(rs.getString("gra_nom"));
				cursoaula.setGrad(grad);
				Curso curso= new Curso();
				curso.setId(rs.getInt("cur_id"));
				curso.setNom(rs.getString("cur_nom"));
				cursoaula.setCurso(curso);
				Nivel niv= new Nivel();
				niv.setId(rs.getInt("niv_id"));
				niv.setNom(rs.getString("niv_nom"));
				cursoaula.setNiv(niv);
				return cursoaula;
			}

		});

	}	




	// funciones privadas utilitarias para CursoAula

	private CursoAula rsToEntity(ResultSet rs,String alias) throws SQLException {
		CursoAula curso_aula = new CursoAula();
		curso_aula.setId(rs.getInt( alias + "id"));
		curso_aula.setId_caa(rs.getInt(alias+ "id_caa"));
		curso_aula.setId_cua(rs.getInt( alias + "id_cua"));
		curso_aula.setId_au(rs.getInt( alias + "id_au"));
		curso_aula.setId_tra(rs.getInt( alias + "id_tra"));
		curso_aula.setCod_classroom(rs.getString( alias + "cod_classroom"));
		curso_aula.setEst(rs.getString( alias + "est"));				
		return curso_aula;

	}
	
}
