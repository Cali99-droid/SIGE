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
import com.tesla.colegio.model.NotaUpd;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.NotaIndicador;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface NotaUpdDAO.
 * @author MV
 *
 */
public class NotaUpdDAOImpl{
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
	public int saveOrUpdate(NotaUpd nota_upd) {
		if (nota_upd.getId() != null) {
			// update
			String sql = "UPDATE aud_nota_upd "
						+ "SET id_tra=?, "
						+ "id_nni=?, "
						+ "nota_ant=?, "
						+ "nota_act=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			//System.out.println(sql);

			jdbcTemplate.update(sql, 
						nota_upd.getId_tra(),
						nota_upd.getId_nni(),
						nota_upd.getNota_ant(),
						nota_upd.getNota_act(),
						nota_upd.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						nota_upd.getId()); 
			return nota_upd.getId();

		} else {
			// insert
			String sql = "insert into aud_nota_upd ("
						+ "id_tra, "
						+ "id_nni, "
						+ "nota_ant, "
						+ "nota_act, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?)";

				//System.out.println(sql);

				jdbcTemplate.update(sql, 
				nota_upd.getId_tra(),
				nota_upd.getId_nni(),
				nota_upd.getNota_ant(),
				nota_upd.getNota_act(),
				nota_upd.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from aud_nota_upd where id=?";
		
		//System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<NotaUpd> list() {
		String sql = "select * from aud_nota_upd";
		
		//System.out.println(sql);
		
		List<NotaUpd> listNotaUpd = jdbcTemplate.query(sql, new RowMapper<NotaUpd>() {

			@Override
			public NotaUpd mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listNotaUpd;
	}

	public NotaUpd get(int id) {
		String sql = "select * from aud_nota_upd WHERE id=" + id;
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaUpd>() {

			@Override
			public NotaUpd extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public NotaUpd getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select audn.id audn_id, audn.id_tra audn_id_tra , audn.id_nni audn_id_nni , audn.nota_ant audn_nota_ant , audn.nota_act audn_nota_act  ,audn.est audn_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("not_nota_indicador"))
			sql = sql + ", nni.id nni_id  , nni.id_not nni_id_not , nni.id_nie nni_id_nie , nni.nota nni_nota  ";
	
		sql = sql + " from aud_nota_upd audn "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = audn.id_tra ";
		if (aTablas.contains("not_nota_indicador"))
			sql = sql + " left join not_nota_indicador nni on nni.id = audn.id_nni ";
		sql = sql + " where audn.id= " + id; 
				
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaUpd>() {
		
			@Override
			public NotaUpd extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					NotaUpd notaupd= rsToEntity(rs,"audn_");
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
							notaupd.setTrabajador(trabajador);
					}
					if (aTablas.contains("not_nota_indicador")){
						NotaIndicador notaindicador = new NotaIndicador();  
							notaindicador.setId(rs.getInt("nni_id")) ;  
							notaindicador.setId_not(rs.getInt("nni_id_not")) ;  
							notaindicador.setId_nie(rs.getInt("nni_id_nie")) ;  
							notaindicador.setNota(rs.getInt("nni_nota")) ;  
							notaupd.setNotaIndicador(notaindicador);
					}
							return notaupd;
				}
				
				return null;
			}
			
		});


	}		
	
	public NotaUpd getByParams(Param param) {

		String sql = "select * from aud_nota_upd " + SQLFrmkUtil.getWhere(param);
		
		//System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<NotaUpd>() {
			@Override
			public NotaUpd extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<NotaUpd> listByParams(Param param, String[] order) {

		String sql = "select * from aud_nota_upd " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaUpd>() {

			@Override
			public NotaUpd mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<NotaUpd> listFullByParams(NotaUpd notaupd, String[] order) {
	
		return listFullByParams(Param.toParam("audn",notaupd), order);
	
	}	
	
	public List<NotaUpd> listFullByParams(Param param, String[] order) {

		String sql = "select audn.id audn_id, audn.id_tra audn_id_tra , audn.id_nni audn_id_nni , audn.nota_ant audn_nota_ant , audn.nota_act audn_nota_act  ,audn.est audn_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", nni.id nni_id  , nni.id_not nni_id_not , nni.id_nie nni_id_nie , nni.nota nni_nota  ";
		sql = sql + " from aud_nota_upd audn";
		sql = sql + " left join ges_trabajador tra on tra.id = audn.id_tra ";
		sql = sql + " left join not_nota_indicador nni on nni.id = audn.id_nni ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<NotaUpd>() {

			@Override
			public NotaUpd mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotaUpd notaupd= rsToEntity(rs,"audn_");
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
				notaupd.setTrabajador(trabajador);
				NotaIndicador notaindicador = new NotaIndicador();  
				notaindicador.setId(rs.getInt("nni_id")) ;  
				notaindicador.setId_not(rs.getInt("nni_id_not")) ;  
				notaindicador.setId_nie(rs.getInt("nni_id_nie")) ;  
				notaindicador.setNota(rs.getInt("nni_nota")) ;  
				notaupd.setNotaIndicador(notaindicador);
				return notaupd;
			}

		});

	}	




	// funciones privadas utilitarias para NotaUpd

	private NotaUpd rsToEntity(ResultSet rs,String alias) throws SQLException {
		NotaUpd nota_upd = new NotaUpd();

		nota_upd.setId(rs.getInt( alias + "id"));
		nota_upd.setId_tra(rs.getInt( alias + "id_tra"));
		nota_upd.setId_nni(rs.getInt( alias + "id_nni"));
		nota_upd.setNota_ant(rs.getInt( alias + "nota_ant"));
		nota_upd.setNota_act(rs.getInt( alias + "nota_act"));
		nota_upd.setEst(rs.getString( alias + "est"));
								
		return nota_upd;

	}
	
}
