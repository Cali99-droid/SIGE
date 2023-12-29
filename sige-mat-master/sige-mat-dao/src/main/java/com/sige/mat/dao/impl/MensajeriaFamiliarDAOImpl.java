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
import com.tesla.colegio.model.MensajeriaFamiliar;

import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.Perfil;


import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface MensajeriaFamiliarDAO.
 * @author MV
 *
 */
public class MensajeriaFamiliarDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(MensajeriaFamiliar mensajeria_familiar) {
		if (mensajeria_familiar.getId() != null) {
			// update
			String sql = "UPDATE msj_mensajeria_familiar "
						+ "SET id_des=?, "
						+ "id_per=?, "
						+ "id_alu=?, "
						+ "msj=?, "
						+ "flg_en=?, "
						+ "clave=?, "
						+ "usr_rmt=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						mensajeria_familiar.getId_des(),
						mensajeria_familiar.getId_per(),
						mensajeria_familiar.getId_alu(),
						mensajeria_familiar.getMsj(),
						mensajeria_familiar.getFlg_en(),
						mensajeria_familiar.getClave(),
						mensajeria_familiar.getUsr_rmt(),
						mensajeria_familiar.getEst(),
						mensajeria_familiar.getUsr_act(),
						new java.util.Date(),
						mensajeria_familiar.getId()); 
			return mensajeria_familiar.getId();

		} else {
			// insert
			String sql = "insert into msj_mensajeria_familiar ("
						+ "id_des, "
						+ "id_per, "
						+ "id_alu, "
						+ "msj, "
						+ "flg_en, "
						+ "clave, "
						+ "usr_rmt, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				mensajeria_familiar.getId_des(),
				mensajeria_familiar.getId_per(),
				mensajeria_familiar.getId_alu(),
				mensajeria_familiar.getMsj(),
				mensajeria_familiar.getFlg_en(),
				mensajeria_familiar.getClave(),
				mensajeria_familiar.getUsr_rmt(),
				mensajeria_familiar.getEst(),
				mensajeria_familiar.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from msj_mensajeria_familiar where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<MensajeriaFamiliar> list() {
		String sql = "select * from msj_mensajeria_familiar";
		
		
		
		List<MensajeriaFamiliar> listMensajeriaFamiliar = jdbcTemplate.query(sql, new RowMapper<MensajeriaFamiliar>() {

			@Override
			public MensajeriaFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listMensajeriaFamiliar;
	}

	public MensajeriaFamiliar get(int id) {
		String sql = "select * from msj_mensajeria_familiar WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MensajeriaFamiliar>() {

			@Override
			public MensajeriaFamiliar extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public MensajeriaFamiliar getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select msjf.id msjf_id, msjf.id_des msjf_id_des , msjf.id_per msjf_id_per , msjf.msj msjf_msj , msjf.flg_en msjf_flg_en  ,msjf.est msjf_est ";
		if (aTablas.contains("alu_familiar"))
			sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
	
		sql = sql + " from msj_mensajeria_familiar msjf "; 
		if (aTablas.contains("alu_familiar"))
			sql = sql + " left join alu_familiar fam on fam.id = msjf.id_des ";
		if (aTablas.contains("seg_perfil"))
			sql = sql + " left join seg_perfil per on per.id = msjf.id_per ";
		sql = sql + " where msjf.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<MensajeriaFamiliar>() {
		
			@Override
			public MensajeriaFamiliar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					MensajeriaFamiliar mensajeriafamiliar= rsToEntity(rs,"msjf_");
					if (aTablas.contains("alu_familiar")){
						Familiar familiar = new Familiar();  
							familiar.setId(rs.getInt("fam_id")) ;  
							familiar.setNom(rs.getString("fam_nom")) ;  
							mensajeriafamiliar.setFamiliar(familiar);
					}
					if (aTablas.contains("seg_perfil")){
						Perfil perfil = new Perfil();  
							perfil.setId(rs.getInt("per_id")) ;  
							perfil.setNom(rs.getString("per_nom")) ;  
							perfil.setDes(rs.getString("per_des")) ;  
							mensajeriafamiliar.setPerfil(perfil);
					}
							return mensajeriafamiliar;
				}
				
				return null;
			}
			
		});


	}		
	
	public MensajeriaFamiliar getByParams(Param param) {

		String sql = "select * from msj_mensajeria_familiar " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<MensajeriaFamiliar>() {
			@Override
			public MensajeriaFamiliar extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<MensajeriaFamiliar> listByParams(Param param, String[] order) {

		String sql = "select * from msj_mensajeria_familiar " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<MensajeriaFamiliar>() {

			@Override
			public MensajeriaFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<MensajeriaFamiliar> listFullByParams(MensajeriaFamiliar mensajeriafamiliar, String[] order) {
	
		return listFullByParams(Param.toParam("msjf",mensajeriafamiliar), order);
	
	}	
	
	public List<MensajeriaFamiliar> listFullByParams(Param param, String[] order) {

		String sql = "select msjf.id msjf_id, msjf.id_des msjf_id_des , msjf.id_per msjf_id_per, msjf.id_alu msjf_id_alu , msjf.msj msjf_msj , msjf.flg_en msjf_flg_en  ,msjf.est msjf_est ";
		sql = sql + ", fam.id fam_id  , fam.nom fam_nom  ";
		sql = sql + ", per.id per_id  , per.nom per_nom , per.des per_des  ";
		sql = sql + " from msj_mensajeria_familiar msjf";
		sql = sql + " left join alu_familiar fam on fam.id = msjf.id_des ";
		sql = sql + " left join seg_perfil per on per.id = msjf.id_per ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<MensajeriaFamiliar>() {

			@Override
			public MensajeriaFamiliar mapRow(ResultSet rs, int rowNum) throws SQLException {
				MensajeriaFamiliar mensajeriafamiliar= rsToEntity(rs,"msjf_");
				Familiar familiar = new Familiar();  
				familiar.setId(rs.getInt("fam_id")) ;  
				familiar.setNom(rs.getString("fam_nom")) ;  
				mensajeriafamiliar.setFamiliar(familiar);
				Perfil perfil = new Perfil();  
				perfil.setId(rs.getInt("per_id")) ;  
				perfil.setNom(rs.getString("per_nom")) ;  
				perfil.setDes(rs.getString("per_des")) ;  
				mensajeriafamiliar.setPerfil(perfil);
				return mensajeriafamiliar;
			}

		});

	}	




	// funciones privadas utilitarias para MensajeriaFamiliar

	private MensajeriaFamiliar rsToEntity(ResultSet rs,String alias) throws SQLException {
		MensajeriaFamiliar mensajeria_familiar = new MensajeriaFamiliar();

		mensajeria_familiar.setId(rs.getInt( alias + "id"));
		mensajeria_familiar.setId_des(rs.getInt( alias + "id_des"));
		mensajeria_familiar.setId_per(rs.getInt( alias + "id_per"));
		mensajeria_familiar.setId_alu(rs.getInt( alias + "id_alu"));
		mensajeria_familiar.setMsj(rs.getString( alias + "msj"));
		mensajeria_familiar.setFlg_en(rs.getString( alias + "flg_en"));
		mensajeria_familiar.setClave(rs.getString( alias + "clave"));
		mensajeria_familiar.setEst(rs.getString( alias + "est"));
								
		return mensajeria_familiar;

	}
	
}
