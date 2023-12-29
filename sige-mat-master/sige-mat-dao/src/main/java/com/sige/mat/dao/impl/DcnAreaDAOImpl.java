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
import com.tesla.colegio.model.DcnArea;

import com.tesla.colegio.model.DcnNivel;
import com.tesla.colegio.model.Area;
import com.tesla.colegio.model.DcnCompTrans;
import com.tesla.colegio.model.CompetenciaDc;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface DcnAreaDAO.
 * @author MV
 *
 */
public class DcnAreaDAOImpl{
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
	public int saveOrUpdate(DcnArea dcn_area) {
		if (dcn_area.getId() != null) {
			// update
			String sql = "UPDATE aca_dcn_area "
						+ "SET id_dcniv=?, "
						+ "id_are=?, "
						+ "ord=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						dcn_area.getId_dcniv(),
						dcn_area.getId_are(),
						dcn_area.getOrd(),
						dcn_area.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						dcn_area.getId()); 
			return dcn_area.getId();

		} else {
			// insert
			String sql = "insert into aca_dcn_area ("
						+ "id_dcniv, "
						+ "id_are, "
						+ "ord, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				dcn_area.getId_dcniv(),
				dcn_area.getId_are(),
				dcn_area.getEst(),
				dcn_area.getOrd(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aca_dcn_area where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<DcnArea> list() {
		String sql = "select * from aca_dcn_area";
		
		System.out.println(sql);
		
		List<DcnArea> listDcnArea = jdbcTemplate.query(sql, new RowMapper<DcnArea>() {

			@Override
			public DcnArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listDcnArea;
	}

	public DcnArea get(int id) {
		String sql = "select * from aca_dcn_area WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnArea>() {

			@Override
			public DcnArea extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public DcnArea getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select dcare.id dcare_id, dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ,dcare.est dcare_est ";
		if (aTablas.contains("aca_dcn_nivel"))
			sql = sql + ", dcniv.id dcniv_id  , dcniv.id_niv dcniv_id_niv , dcniv.id_dcn dcniv_id_dcn  ";
		if (aTablas.contains("cat_area"))
			sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
	
		sql = sql + " from aca_dcn_area dcare "; 
		if (aTablas.contains("aca_dcn_nivel"))
			sql = sql + " left join aca_dcn_nivel dcniv on dcniv.id = dcare.id_dcniv ";
		if (aTablas.contains("cat_area"))
			sql = sql + " left join cat_area area on area.id = dcare.id_are ";
		sql = sql + " where dcare.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnArea>() {
		
			@Override
			public DcnArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					DcnArea dcnarea= rsToEntity(rs,"dcare_");
					if (aTablas.contains("aca_dcn_nivel")){
						DcnNivel dcnnivel = new DcnNivel();  
							dcnnivel.setId(rs.getInt("dcniv_id")) ;  
							dcnnivel.setId_niv(rs.getInt("dcniv_id_niv")) ;  
							dcnnivel.setId_dcn(rs.getInt("dcniv_id_dcn")) ;  
							dcnarea.setDcnNivel(dcnnivel);
					}
					if (aTablas.contains("cat_area")){
						Area area = new Area();  
							area.setId(rs.getInt("area_id")) ;  
							area.setNom(rs.getString("area_nom")) ;  
							area.setDes(rs.getString("area_des")) ;  
							dcnarea.setArea(area);
					}
							return dcnarea;
				}
				
				return null;
			}
			
		});


	}		
	
	public DcnArea getByParams(Param param) {

		String sql = "select * from aca_dcn_area " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<DcnArea>() {
			@Override
			public DcnArea extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<DcnArea> listByParams(Param param, String[] order) {

		String sql = "select * from aca_dcn_area " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnArea>() {

			@Override
			public DcnArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<DcnArea> listFullByParams(DcnArea dcnarea, String[] order) {
	
		return listFullByParams(Param.toParam("dcare",dcnarea), order);
	
	}	
	
	public List<DcnArea> listFullByParams(Param param, String[] order) {

		String sql = "select dcare.id dcare_id, dcare.id_dcniv dcare_id_dcniv , dcare.id_are dcare_id_are  ,dcare.est dcare_est, dcare.ord dcare_ord ";
		sql = sql + ", dcniv.id dcniv_id  , dcniv.id_niv dcniv_id_niv , dcniv.id_dcn dcniv_id_dcn  ";
		sql = sql + ", area.id area_id  , area.nom area_nom , area.des area_des  ";
		sql = sql + " from aca_dcn_area dcare";
		sql = sql + " left join aca_dcn_nivel dcniv on dcniv.id = dcare.id_dcniv ";
		sql = sql + " left join cat_area area on area.id = dcare.id_are ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<DcnArea>() {

			@Override
			public DcnArea mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnArea dcnarea= rsToEntity(rs,"dcare_");
				DcnNivel dcnnivel = new DcnNivel();  
				dcnnivel.setId(rs.getInt("dcniv_id")) ;  
				dcnnivel.setId_niv(rs.getInt("dcniv_id_niv")) ;  
				dcnnivel.setId_dcn(rs.getInt("dcniv_id_dcn")) ;  
				dcnarea.setDcnNivel(dcnnivel);
				Area area = new Area();  
				area.setId(rs.getInt("area_id")) ;  
				area.setNom(rs.getString("area_nom")) ;  
				area.setDes(rs.getString("area_des")) ;  
				dcnarea.setArea(area);
				return dcnarea;
			}

		});

	}	


	public List<DcnCompTrans> getListDcnCompTrans(Param param, String[] order) {
		String sql = "select * from aca_dcn_comp_trans " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<DcnCompTrans>() {

			@Override
			public DcnCompTrans mapRow(ResultSet rs, int rowNum) throws SQLException {
				DcnCompTrans dcn_comp_trans = new DcnCompTrans();

				dcn_comp_trans.setId(rs.getInt("id"));
				dcn_comp_trans.setId_dcare(rs.getInt("id_dcare"));
				dcn_comp_trans.setId_ctran(rs.getInt("id_ctran"));
				dcn_comp_trans.setEst(rs.getString("est"));
												
				return dcn_comp_trans;
			}

		});	
	}
	public List<CompetenciaDc> getListCompetenciaDc(Param param, String[] order) {
		String sql = "select * from aca_competencia_dc " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new RowMapper<CompetenciaDc>() {

			@Override
			public CompetenciaDc mapRow(ResultSet rs, int rowNum) throws SQLException {
				CompetenciaDc competencia_dc = new CompetenciaDc();

				competencia_dc.setId(rs.getInt("id"));
				competencia_dc.setId_dcare(rs.getInt("id_dcare"));
				competencia_dc.setNom(rs.getString("nom"));
				competencia_dc.setPeso(rs.getBigDecimal("peso"));
				competencia_dc.setOrden(rs.getInt("orden"));
				competencia_dc.setEst(rs.getString("est"));
												
				return competencia_dc;
			}

		});	
	}


	// funciones privadas utilitarias para DcnArea

	private DcnArea rsToEntity(ResultSet rs,String alias) throws SQLException {
		DcnArea dcn_area = new DcnArea();

		dcn_area.setId(rs.getInt( alias + "id"));
		dcn_area.setId_dcniv(rs.getInt( alias + "id_dcniv"));
		dcn_area.setId_are(rs.getInt( alias + "id_are"));
		dcn_area.setOrd(rs.getInt( alias + "ord"));
		dcn_area.setEst(rs.getString( alias + "est"));
								
		return dcn_area;

	}
	
}
