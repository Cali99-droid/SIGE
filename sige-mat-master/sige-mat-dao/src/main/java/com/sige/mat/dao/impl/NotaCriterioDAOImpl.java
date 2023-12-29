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
import com.tesla.colegio.model.NotaCriterio;

import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Examen;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaCriterioDAO.
 * @author MV
 *
 */
public class NotaCriterioDAOImpl{
	final static Logger logger = Logger.getLogger(NotaCriterioDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(NotaCriterio nota_criterio) {
		if (nota_criterio.getId() != null) {
			// update
			String sql = "UPDATE eva_nota_criterio "
						+ "SET id_alu=?, "
						+ "id_exa=?, "
						+ "num=?, "
						+ "instr_tecn1=?, "
						+ "instr_tecn2=?, "
						+ "instr_tecn3=?, "
						+ "instr_tecn4=?, "
						+ "resultado=?, "
						+ "apto=?, "
						+ "concl_recom1=?, "
						+ "concl_recom2=?, "
						+ "concl_recom3=?, "
						+ "concl_recom4=?, "
						+ "concl_recom5=?, "
						+ "concl_recom6=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						nota_criterio.getId_alu(),
						nota_criterio.getId_exa(),
						nota_criterio.getNum(),
						nota_criterio.getInstr_tecn1(),
						nota_criterio.getInstr_tecn2(),
						nota_criterio.getInstr_tecn3(),
						nota_criterio.getInstr_tecn4(),
						nota_criterio.getResultado(),
						nota_criterio.getApto(),
						nota_criterio.getConcl_recom1(),
						nota_criterio.getConcl_recom2(),
						nota_criterio.getConcl_recom3(),
						nota_criterio.getConcl_recom4(),
						nota_criterio.getConcl_recom5(),
						nota_criterio.getConcl_recom6(),
						nota_criterio.getEst(),
						nota_criterio.getUsr_act(),
						new java.util.Date(),
						nota_criterio.getId()); 

		} else {
			// insert
			String sql = "insert into eva_nota_criterio ("
						+ "id_alu, "
						+ "id_exa, "
						+ "num, "
						+ "instr_tecn1, "
						+ "instr_tecn2, "
						+ "instr_tecn3, "
						+ "instr_tecn4, "
						+ "resultado, "
						+ "apto, "
						+ "concl_recom1, "
						+ "concl_recom2, "
						+ "concl_recom3, "
						+ "concl_recom4, "
						+ "concl_recom5, "
						+ "concl_recom6, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				nota_criterio.getId_alu(),
				nota_criterio.getId_exa(),
				nota_criterio.getNum(),
				nota_criterio.getInstr_tecn1(),
				nota_criterio.getInstr_tecn2(),
				nota_criterio.getInstr_tecn3(),
				nota_criterio.getInstr_tecn4(),
				nota_criterio.getResultado(),
				nota_criterio.getApto(),
				nota_criterio.getConcl_recom1(),
				nota_criterio.getConcl_recom2(),
				nota_criterio.getConcl_recom3(),
				nota_criterio.getConcl_recom4(),
				nota_criterio.getConcl_recom5(),
				nota_criterio.getConcl_recom6(),
				nota_criterio.getEst(),
				nota_criterio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from eva_nota_criterio where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<NotaCriterio> list() {
		String sql = "select * from eva_nota_criterio";
		
		//logger.info(sql);
		
		List<NotaCriterio> listNotaCriterio = jdbcTemplate.query(sql, new RowMapper<NotaCriterio>() {

			
			public NotaCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaCriterio;
	}

	
	public NotaCriterio get(int id) {
		String sql = "select * from eva_nota_criterio WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCriterio>() {

			
			public NotaCriterio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public NotaCriterio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select eva_exa.id eva_exa_id, eva_exa.id_alu eva_exa_id_alu , eva_exa.id_exa eva_exa_id_exa , eva_exa.num eva_exa_num , eva_exa.instr_tecn1 eva_exa_instr_tecn1 , eva_exa.instr_tecn2 eva_exa_instr_tecn2 , eva_exa.instr_tecn3 eva_exa_instr_tecn3 , eva_exa.instr_tecn4 eva_exa_instr_tecn4 , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto , eva_exa.concl_recom1 eva_exa_concl_recom1 , eva_exa.concl_recom2 eva_exa_concl_recom2 , eva_exa.concl_recom3 eva_exa_concl_recom3 , eva_exa.concl_recom4 eva_exa_concl_recom4 , eva_exa.concl_recom5 eva_exa_concl_recom5 , eva_exa.concl_recom6 eva_exa_concl_recom6  ,eva_exa.est eva_exa_est ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		if (aTablas.contains("eva_examen"))
			sql = sql + ", ex_vac.id ex_vac_id  , ex_vac.id_eva ex_vac_id_eva , ex_vac.id_eae ex_vac_id_eae , ex_vac.id_tae ex_vac_id_tae , ex_vac.fec_exa ex_vac_fec_exa , ex_vac.fec_not ex_vac_fec_not , ex_vac.precio ex_vac_precio , ex_vac.pje_pre_cor ex_vac_pje_pre_cor , ex_vac.pje_pre_inc ex_vac_pje_pre_inc  ";
	
		sql = sql + " from eva_nota_criterio eva_exa "; 
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = eva_exa.id_alu ";
		if (aTablas.contains("eva_examen"))
			sql = sql + " left join eva_examen ex_vac on ex_vac.id = eva_exa.id_exa ";
		sql = sql + " where eva_exa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCriterio>() {
		
			
			public NotaCriterio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaCriterio notacriterio= rsToEntity(rs,"eva_exa_");
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
							alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							notacriterio.setAlumno(alumno);
					}
					if (aTablas.contains("eva_examen")){
						Examen examen = new Examen();  
							examen.setId(rs.getInt("ex_vac_id")) ;  
							examen.setId_eva(rs.getInt("ex_vac_id_eva")) ;  
							examen.setId_eae(rs.getInt("ex_vac_id_eae")) ;  
							examen.setId_tae(rs.getInt("ex_vac_id_tae")) ;  
							examen.setFec_exa(rs.getDate("ex_vac_fec_exa")) ;  
							examen.setFec_not(rs.getDate("ex_vac_fec_not")) ;  
							examen.setPrecio(rs.getBigDecimal("ex_vac_precio")) ;  
							examen.setPje_pre_cor(rs.getBigDecimal("ex_vac_pje_pre_cor")) ;  
							examen.setPje_pre_inc(rs.getBigDecimal("ex_vac_pje_pre_inc")) ;  
							notacriterio.setExamen(examen);
					}
							return notacriterio;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public NotaCriterio getByParams(Param param) {

		String sql = "select * from eva_nota_criterio " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaCriterio>() {
			
			public NotaCriterio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<NotaCriterio> listByParams(Param param, String[] order) {

		String sql = "select * from eva_nota_criterio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaCriterio>() {

			
			public NotaCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<NotaCriterio> listFullByParams(NotaCriterio notacriterio, String[] order) {
	
		return listFullByParams(Param.toParam("eva_exa",notacriterio), order);
	
	}	
	
	
	public List<NotaCriterio> listFullByParams(Param param, String[] order) {

		String sql = "select eva_exa.id eva_exa_id, eva_exa.id_alu eva_exa_id_alu , eva_exa.id_exa eva_exa_id_exa , eva_exa.num eva_exa_num , eva_exa.instr_tecn1 eva_exa_instr_tecn1 , eva_exa.instr_tecn2 eva_exa_instr_tecn2 , eva_exa.instr_tecn3 eva_exa_instr_tecn3 , eva_exa.instr_tecn4 eva_exa_instr_tecn4 , eva_exa.resultado eva_exa_resultado , eva_exa.apto eva_exa_apto , eva_exa.concl_recom1 eva_exa_concl_recom1 , eva_exa.concl_recom2 eva_exa_concl_recom2 , eva_exa.concl_recom3 eva_exa_concl_recom3 , eva_exa.concl_recom4 eva_exa_concl_recom4 , eva_exa.concl_recom5 eva_exa_concl_recom5 , eva_exa.concl_recom6 eva_exa_concl_recom6  ,eva_exa.est eva_exa_est ";
		sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
		sql = sql + ", ex_vac.id ex_vac_id  , ex_vac.id_eva ex_vac_id_eva , ex_vac.id_eae ex_vac_id_eae , ex_vac.id_tae ex_vac_id_tae , ex_vac.fec_exa ex_vac_fec_exa , ex_vac.fec_not ex_vac_fec_not , ex_vac.precio ex_vac_precio , ex_vac.pje_pre_cor ex_vac_pje_pre_cor , ex_vac.pje_pre_inc ex_vac_pje_pre_inc  ";
		sql = sql + " from eva_nota_criterio eva_exa";
		sql = sql + " left join alu_alumno alu on alu.id = eva_exa.id_alu ";
		sql = sql + " left join eva_examen ex_vac on ex_vac.id = eva_exa.id_exa ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaCriterio>() {

			
			public NotaCriterio mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaCriterio notacriterio= rsToEntity(rs,"eva_exa_");
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
				alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;  
				notacriterio.setAlumno(alumno);
				Examen examen = new Examen();  
				examen.setId(rs.getInt("ex_vac_id")) ;  
				examen.setId_eva(rs.getInt("ex_vac_id_eva")) ;  
				examen.setId_eae(rs.getInt("ex_vac_id_eae")) ;  
				examen.setId_tae(rs.getInt("ex_vac_id_tae")) ;  
				examen.setFec_exa(rs.getDate("ex_vac_fec_exa")) ;  
				examen.setFec_not(rs.getDate("ex_vac_fec_not")) ;  
				examen.setPrecio(rs.getBigDecimal("ex_vac_precio")) ;  
				examen.setPje_pre_cor(rs.getBigDecimal("ex_vac_pje_pre_cor")) ;  
				examen.setPje_pre_inc(rs.getBigDecimal("ex_vac_pje_pre_inc")) ;  
				notacriterio.setExamen(examen);
				return notacriterio;
			}

		});

	}	




	// funciones privadas utilitarias para NotaCriterio

	private NotaCriterio rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaCriterio nota_criterio = new NotaCriterio();

		nota_criterio.setId(rs.getInt( alias + "id"));
		nota_criterio.setId_alu(rs.getInt( alias + "id_alu"));
		nota_criterio.setId_exa(rs.getInt( alias + "id_exa"));
		nota_criterio.setNum(rs.getInt( alias + "num"));
		nota_criterio.setInstr_tecn1(rs.getString( alias + "instr_tecn1"));
		nota_criterio.setInstr_tecn2(rs.getString( alias + "instr_tecn2"));
		nota_criterio.setInstr_tecn3(rs.getString( alias + "instr_tecn3"));
		nota_criterio.setInstr_tecn4(rs.getString( alias + "instr_tecn4"));
		nota_criterio.setResultado(rs.getString( alias + "resultado"));
		nota_criterio.setApto(rs.getString( alias + "apto"));
		nota_criterio.setConcl_recom1(rs.getString( alias + "concl_recom1"));
		nota_criterio.setConcl_recom2(rs.getString( alias + "concl_recom2"));
		nota_criterio.setConcl_recom3(rs.getString( alias + "concl_recom3"));
		nota_criterio.setConcl_recom4(rs.getString( alias + "concl_recom4"));
		nota_criterio.setConcl_recom5(rs.getString( alias + "concl_recom5"));
		nota_criterio.setConcl_recom6(rs.getString( alias + "concl_recom6"));
		nota_criterio.setEst(rs.getString( alias + "est"));
								
		return nota_criterio;

	}
	
}
