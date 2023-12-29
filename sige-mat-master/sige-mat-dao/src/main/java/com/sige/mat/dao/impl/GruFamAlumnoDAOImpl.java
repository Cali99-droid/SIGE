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
import com.tesla.colegio.model.GruFamAlumno;
import com.tesla.colegio.model.Persona;
import com.tesla.colegio.model.Provincia;
import com.tesla.colegio.model.TipoDocumento;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Departamento;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface GruFamAlumnoDAO.
 * @author MV
 *
 */
public class GruFamAlumnoDAOImpl{
	final static Logger logger = Logger.getLogger(GruFamAlumnoDAOImpl.class);
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(GruFamAlumno gru_fam_alumno) {
		if (gru_fam_alumno.getId() !=null) {
			// update
			String sql = "UPDATE alu_gru_fam_alumno "
						+ "SET id_gpf=?, "
						+ "id_alu=?, "
						+ "est=?,usr_act=?,fec_act=? "
					+ "WHERE id=?";
			
			//logger.info(sql);

			return jdbcTemplate.update(sql, 
						gru_fam_alumno.getId_gpf(),
						gru_fam_alumno.getId_alu(),
						gru_fam_alumno.getEst(),
						gru_fam_alumno.getUsr_act(),
						new java.util.Date(),
						gru_fam_alumno.getId()); 

		} else {
			// insert
			String sql = "insert into alu_gru_fam_alumno ("
						+ "id_gpf, "
						+ "id_alu, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?)";

				//logger.info(sql);

				jdbcTemplate.update(sql, 
				gru_fam_alumno.getId_gpf(),
				gru_fam_alumno.getId_alu(),
				gru_fam_alumno.getEst(),
				gru_fam_alumno.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	
	public void delete(int id) {
		String sql = "delete from alu_gru_fam_alumno where id=?";
		
		//logger.info(sql);
		
		jdbcTemplate.update(sql, id);
	}

	
	public List<GruFamAlumno> list() {
		String sql = "select * from alu_gru_fam_alumno";
		
		//logger.info(sql);
		
		List<GruFamAlumno> listGruFamAlumno = jdbcTemplate.query(sql, new RowMapper<GruFamAlumno>() {

			
			public GruFamAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listGruFamAlumno;
	}

	
	public GruFamAlumno get(int id) {
		String sql = "select * from alu_gru_fam_alumno WHERE id=" + id;
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamAlumno>() {

			
			public GruFamAlumno extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	
	public GruFamAlumno getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select gfa.id gfa_id, gfa.id_gpf gfa_id_gpf , gfa.id_alu gfa_id_alu  ,gfa.est gfa_est ";
		if (aTablas.contains("alu_gru_fam"))
			sql = sql + ", gpf.id gpf_id  , gpf.cod gpf_cod , gpf.des gpf_des  ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + ", alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen , alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , alu.pass_educando alu_pass_educando , alu.foto alu_foto  ";
	
		sql = sql + " from alu_gru_fam_alumno gfa "; 
		if (aTablas.contains("alu_gru_fam"))
			sql = sql + " left join alu_gru_fam gpf on gpf.id = gfa.id_gpf ";
		if (aTablas.contains("alu_alumno"))
			sql = sql + " left join alu_alumno alu on alu.id = gfa.id_alu ";
		sql = sql + " where gfa.id= " + id; 
				
		//logger.info(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamAlumno>() {
		
			
			public GruFamAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					GruFamAlumno gruFamAlumno= rsToEntity(rs,"gfa_");
					if (aTablas.contains("alu_gru_fam")){
						GruFam gruFam = new GruFam();  
							gruFam.setId(rs.getInt("gpf_id")) ;  
							gruFam.setCod(rs.getString("gpf_cod")) ;  
							gruFam.setDes(rs.getString("gpf_des")) ;  
							gruFamAlumno.setGruFam(gruFam);
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
							alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  
							alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
							alumno.setDireccion(rs.getString("alu_direccion")) ;  
							alumno.setTelf(rs.getString("alu_telf")) ;  
							alumno.setCelular(rs.getString("alu_celular")) ;  
							alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
							//alumno.setFoto(rs.getString("alu_foto")) ;  
							gruFamAlumno.setAlumno(alumno);
					}
							return gruFamAlumno;
				}
				
				return null;
			}
			
		});


	}		
	
	
	public GruFamAlumno getByParams(Param param) {

		String sql = "select * from alu_gru_fam_alumno " + SQLFrmkUtil.getWhere(param);
		
		//logger.info(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<GruFamAlumno>() {
			
			public GruFamAlumno extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	
	public List<GruFamAlumno> listByParams(Param param, String[] order) {

		String sql = "select * from alu_gru_fam_alumno " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFamAlumno>() {

			
			public GruFamAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	
	public List<GruFamAlumno> listFullByParams(GruFamAlumno gruFamAlumno, String[] order) {
	
		return listFullByParams(Param.toParam("gfa",gruFamAlumno), order);
	
	}	
	
	
	public List<GruFamAlumno> listFullByParams(Param param, String[] order) {

		String sql = "select gfa.id gfa_id, gfa.id_gpf gfa_id_gpf , gfa.id_alu gfa_id_alu  ,gfa.est gfa_est ";
		sql = sql + "\n, gpf.id gpf_id  , gpf.cod gpf_cod , gpf.des gpf_des  ";
		sql = sql + "\n, d.id id_dep_nac, pro.id id_pro_nac";
		sql = sql + "\n, dv.id id_dep_viv, pv.id id_pro_viv";
		sql = sql + "\n, alu.id alu_id  , alu.id_tdc alu_id_tdc , alu.id_idio1 alu_id_idio1 , alu.id_idio2 alu_id_idio2 , alu.id_eci alu_id_eci , alu.id_tap alu_id_tap , alu.id_gen alu_id_gen ,alu.id_pais_nac alu_id_pais_nac, alu.id_dist_nac alu_id_dist_nac, alu.id_nac alu_id_nac, alu.id_dist_viv alu_id_dist_viv, alu.cod alu_cod , alu.nro_doc alu_nro_doc , alu.nom alu_nom , alu.ape_pat alu_ape_pat , alu.ape_mat alu_ape_mat , alu.fec_nac alu_fec_nac , alu.num_hij alu_num_hij , alu.direccion alu_direccion , alu.telf alu_telf , alu.celular alu_celular , tdc.nom alu_tdc_nom, alu.ref alu_ref, alu.corr alu_corr, alu.id_anio_act alu_id_anio_act ";
		sql = sql + "\n, per.id per_id  , per.id_tdc per_id_tdc , per.id_eci per_id_eci  , per.id_gen per_id_gen ,per.id_pais_nac per_id_pais_nac, per.id_dist_nac per_id_dist_nac, per.id_dist_viv per_id_dist_viv , per.nro_doc per_nro_doc , per.nom per_nom , per.ape_pat per_ape_pat , per.ape_mat per_ape_mat , per.fec_nac per_fec_nac , per.tlf per_tlf , per.cel per_cel , per.corr per_corr ";
		sql = sql + "\n from alu_gru_fam_alumno gfa";
		sql = sql + "\n left join alu_gru_fam gpf on gpf.id = gfa.id_gpf ";
		sql = sql + "\n left join alu_alumno alu on alu.id = gfa.id_alu ";
		sql = sql + "\n left join col_persona per on alu.id_per = per.id ";
		sql = sql + "\n left join cat_tipo_documento tdc on tdc.id = alu.id_tdc ";
		sql = sql + "\n LEFT JOIN cat_distrito dis ON per.`id_dist_nac`=dis.id ";
		sql = sql + "\n LEFT JOIN cat_provincia pro ON dis.id_pro=pro.id ";
		sql = sql + "\n LEFT JOIN cat_departamento d ON pro.id_dep=d.id ";
		sql = sql + "\n LEFT JOIN cat_pais p ON d.id_pais=p.id AND alu.id_pais_nac=p.id ";
		sql = sql + "\n LEFT JOIN cat_distrito disv ON disv.id=alu.id_dist_viv ";
		sql = sql + "\n LEFT JOIN cat_provincia pv ON pv.id=disv.id_pro ";
		sql = sql + "\n LEFT JOIN cat_departamento dv ON dv.id=pv.id_dep ";
		
		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		//logger.info(sql);

		return jdbcTemplate.query(sql, new RowMapper<GruFamAlumno>() {

			
			public GruFamAlumno mapRow(ResultSet rs, int rowNum) throws SQLException {
				GruFamAlumno gruFamAlumno= rsToEntity(rs,"gfa_");
				GruFam gruFam = new GruFam();  
				gruFam.setId(rs.getInt("gpf_id")) ;  
				gruFam.setCod(rs.getString("gpf_cod")) ;  
				gruFam.setDes(rs.getString("gpf_des")) ;  
				gruFamAlumno.setGruFam(gruFam);
				Departamento departamento_nac =  new Departamento();
				departamento_nac.setId(rs.getInt("id_dep_nac"));
				Provincia provincia_nac = new Provincia();
				provincia_nac.setId(rs.getInt("id_pro_nac"));
				Departamento departamento_viv =  new Departamento();
				departamento_viv.setId(rs.getInt("id_dep_viv"));
				Provincia provincia_viv = new Provincia();
				provincia_viv.setId(rs.getInt("id_pro_viv"));
				Persona persona = new Persona();  
				persona.setId(rs.getInt("per_id")) ;  
				persona.setId_tdc(rs.getString("per_id_tdc")) ;  
				persona.setId_eci(rs.getInt("per_id_eci")) ;    
				persona.setId_gen(rs.getString("per_id_gen")) ; 
				persona.setId_pais_nac(rs.getInt("per_id_pais_nac"));
				persona.setId_dist_nac(rs.getInt("per_id_dist_nac"));
				persona.setId_dist_viv(rs.getInt("per_id_dist_viv"));;  
				persona.setNro_doc(rs.getString("per_nro_doc")) ;  
				persona.setNom(rs.getString("per_nom")) ;  
				persona.setApe_pat(rs.getString("per_ape_pat")) ;  
				persona.setApe_mat(rs.getString("per_ape_mat")) ;  
				persona.setFec_nac(rs.getDate("per_fec_nac")) ;   
				persona.setCorr(rs.getString("per_corr")); 
				persona.setCel(rs.getString("per_cel")) ;  
				persona.setDepartamento(departamento_nac);
				persona.setProvincia_nac(provincia_nac);
				Alumno alumno = new Alumno();  
				alumno.setId(rs.getInt("alu_id")) ;  
				//alumno.setId_tdc(rs.getInt("alu_id_tdc")) ;  
				alumno.setId_idio1(rs.getInt("alu_id_idio1")) ;  
				alumno.setId_idio2(rs.getInt("alu_id_idio2")) ;  
				/*alumno.setId_eci(rs.getInt("alu_id_eci")) ;  
				alumno.setId_tap(rs.getString("alu_id_tap")) ;  
				alumno.setId_gen(rs.getString("alu_id_gen")) ; 
				alumno.setId_pais_nac(rs.getInt("alu_id_pais_nac"));
				alumno.setId_dist_nac(rs.getInt("alu_id_dist_nac"));
				alumno.setId_nac(rs.getInt("alu_id_nac"));
				alumno.setId_dist_viv(rs.getInt("alu_id_dist_viv"));
				alumno.setId_anio_act(rs.getInt("alu_id_anio_act"));*/
				alumno.setCod(rs.getString("alu_cod")) ;  
				/*alumno.setNro_doc(rs.getString("alu_nro_doc")) ;  
				alumno.setNom(rs.getString("alu_nom")) ;  
				alumno.setApe_pat(rs.getString("alu_ape_pat")) ;  
				alumno.setApe_mat(rs.getString("alu_ape_mat")) ;  
				alumno.setFec_nac(rs.getDate("alu_fec_nac")) ;  */
				alumno.setNum_hij(rs.getInt("alu_num_hij")) ;  
				/*alumno.setDireccion(rs.getString("alu_direccion")) ;  
				alumno.setRef(rs.getString("alu_ref"));
				alumno.setCorr(rs.getString("alu_corr"));
				alumno.setTelf(rs.getString("alu_telf")) ;  
				alumno.setCelular(rs.getString("alu_celular")) ;  
				alumno.setDepartamento_nac(departamento_nac);
				alumno.setProvincia_nac(provincia_nac);
				alumno.setDepartamento_viv(departamento_viv);
				alumno.setProvincia_viv(provincia_viv);*/
				//alumno.setPass_educando(rs.getString("alu_pass_educando")) ;  
				//alumno.setFoto(rs.getString("alu_foto")) ;
				
				TipoDocumento tipoDocumento = new TipoDocumento();
				tipoDocumento.setNom(rs.getString("alu_tdc_nom"));
				alumno.setTipoDocumento(tipoDocumento);
				gruFamAlumno.setAlumno(alumno);
				gruFamAlumno.setPersona(persona);
				return gruFamAlumno;
			}

		});

	}	




	// funciones privadas utilitarias para GruFamAlumno

	private GruFamAlumno rsToEntity(ResultSet rs,String alias) throws SQLException {
		GruFamAlumno gru_fam_alumno = new GruFamAlumno();

		gru_fam_alumno.setId(rs.getInt( alias + "id"));
		gru_fam_alumno.setId_gpf(rs.getInt( alias + "id_gpf"));
		gru_fam_alumno.setId_alu(rs.getInt( alias + "id_alu"));
		gru_fam_alumno.setEst(rs.getString( alias + "est"));
								
		return gru_fam_alumno;

	}
	
	private Departamento rsToEntityDep(ResultSet rs,String alias) throws SQLException {
		Departamento departamento = new Departamento();

		departamento.setId(rs.getInt( alias + "id_dep"));								
		return departamento;

	}
	
	private Provincia rsToEntityPro(ResultSet rs,String alias) throws SQLException {
		Provincia provincia = new Provincia();

		provincia.setId(rs.getInt( alias + "id_pro"));								
		return provincia;

	}
	
}
