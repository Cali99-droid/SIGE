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
import com.tesla.colegio.model.DcnCompTrans;
import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.DcnArea;
import com.tesla.colegio.model.CompetenciaTrans;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DcnCompTransDAO.
 * @author MV
 *
 */
public class DcnCompTransDAOImpl{
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
	public int saveOrUpdate(DcnCompTrans dcn_comp_trans) {
		if (dcn_comp_trans.getId() != null) {
			// update
			String sql = "UPDATE aca_dcn_comp_trans "
						+ "SET id_dcare=?, "
						+ "id_ctran=?, "
						+ "id_dcniv=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						dcn_comp_trans.getId_dcare(),
						dcn_comp_trans.getId_ctran(),
						dcn_comp_trans.getId_dcniv(),
						dcn_comp_trans.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						dcn_comp_trans.getId()); 
			return dcn_comp_trans.getId();

		} else {
			// insert
			String sql = "insert into aca_dcn_comp_trans ("
						+ "id_dcare, "
						+ "id_dcniv, "
						+ "id_ctran, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				dcn_comp_trans.getId_dcare(),
				dcn_comp_trans.getId_dcniv(),
				dcn_comp_trans.getId_ctran(),
				dcn_comp_trans.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_dcn_comp_trans where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DcnCompTrans> list() {
		String sql = "select * from aca_dcn_comp_trans";
		
		System.out.println(sql);
		
		List<DcnCompTrans> listDcnCompTrans = jdbcTemplate.query(sql, new RowMapper<DcnCompTrans>() {

			@Override
			public DcnCompTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDcnCompTrans;
	}

	public DcnCompTrans get(int id) {
		String sql = "select * from aca_dcn_comp_trans WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnCompTrans>() {

			@Override
			public DcnCompTrans extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DcnCompTrans getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ctra.id ctra_id, ctra.id_dcare ctra_id_dcare , ctra.id_ctran ctra_id_ctran  ,ctra.est ctra_est ";
		if (aTablas.contains("aca_dcn_area"))
			sql = sql + ", dcare.id dcare_id  , dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ";
		if (aTablas.contains("cat_competencia_trans"))
			sql = sql + ", ctran.id ctran_id  , ctran.nom ctran_nom  ";
	
		sql = sql + " from aca_dcn_comp_trans ctra "; 
		if (aTablas.contains("aca_dcn_area"))
			sql = sql + " left join aca_dcn_area dcare on dcare.id = ctra.id_dcare ";
		if (aTablas.contains("cat_competencia_trans"))
			sql = sql + " left join cat_competencia_trans ctran on ctran.id = ctra.id_ctran ";
		sql = sql + " where ctra.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnCompTrans>() {
		
			@Override
			public DcnCompTrans extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DcnCompTrans dcncomptrans= rsToEntity(rs,"ctra_");
					if (aTablas.contains("aca_dcn_area")){
						DcnArea dcnarea = new DcnArea();  
							dcnarea.setId(rs.getInt("dcare_id")) ;  
							dcnarea.setId_dcniv(rs.getInt("dcare_id_dcniv")) ;  
							dcnarea.setId_are(rs.getInt("dcare_id_are")) ;  
							dcncomptrans.setDcnArea(dcnarea);
					}
					if (aTablas.contains("cat_competencia_trans")){
						CompetenciaTrans competenciatrans = new CompetenciaTrans();  
							competenciatrans.setId(rs.getInt("ctran_id")) ;  
							competenciatrans.setNom(rs.getString("ctran_nom")) ;  
							dcncomptrans.setCompetenciaTrans(competenciatrans);
					}
							return dcncomptrans;
				}
				
				return null;
			}
			
		});


	}		
	
	public DcnCompTrans getByParams(Param param) {

		String sql = "select * from aca_dcn_comp_trans " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnCompTrans>() {
			@Override
			public DcnCompTrans extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DcnCompTrans> listByParams(Param param, String[] order) {

		String sql = "select * from aca_dcn_comp_trans " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnCompTrans>() {

			@Override
			public DcnCompTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DcnCompTrans> listFullByParams(DcnCompTrans dcncomptrans, String[] order) {
	
		return listFullByParams(Param.toParam("ctra",dcncomptrans), order);
	
	}	
	
	public List<DcnCompTrans> listFullByParams(Param param, String[] order) {

		String sql = "select ctra.id ctra_id, ctra.id_dcniv ctra_id_dcniv, ctra.id_ctran ctra_id_ctran  ,ctra.est ctra_est ";
		//sql = sql + ", dcare.id dcare_id  , dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ";
		sql = sql + ", dcniv.id dcniv_id ";
		sql = sql + ", ctran.id ctran_id  , ctran.nom ctran_nom  ";
		sql = sql + " from aca_dcn_comp_trans ctra";
		//sql = sql + " left join aca_dcn_area dcare on dcare.id = ctra.id_dcare ";
		sql = sql + " left join aca_dcn_nivel dcniv on ctra.id_dcniv=dcniv.id ";
		sql = sql + " left join cat_competencia_trans ctran on ctran.id = ctra.id_ctran ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnCompTrans>() {

			@Override
			public DcnCompTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnCompTrans dcncomptrans= rsToEntity(rs,"ctra_");
				DcnNivel dcNivel = new DcnNivel();
				dcNivel.setId(rs.getInt("dcniv_id"));
				dcncomptrans.setDcnivel(dcNivel);
				CompetenciaTrans competenciatrans = new CompetenciaTrans();  
				competenciatrans.setId(rs.getInt("ctran_id")) ;  
				competenciatrans.setNom(rs.getString("ctran_nom")) ;  
				dcncomptrans.setCompetenciaTrans(competenciatrans);
				return dcncomptrans;
			}

		});

	}	




	// funciones privadas utilitarias para DcnCompTrans

	private DcnCompTrans rsToEntity(ResultSet rs,String alias) throws SQLException {
		DcnCompTrans dcn_comp_trans = new DcnCompTrans();

		dcn_comp_trans.setId(rs.getInt( alias + "id"));
		//dcn_comp_trans.setId_dcare(rs.getInt( alias + "id_dcare"));
		dcn_comp_trans.setId_dcniv(rs.getInt( alias + "id_dcniv"));
		dcn_comp_trans.setId_ctran(rs.getInt( alias + "id_ctran"));
		dcn_comp_trans.setEst(rs.getString( alias + "est"));
								
		return dcn_comp_trans;

	}
	
}
