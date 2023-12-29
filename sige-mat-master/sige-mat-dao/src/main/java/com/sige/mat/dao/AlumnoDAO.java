package com.sige.mat.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;








import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumParentesco;
import com.sige.common.enums.EnumSituacionFinal;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AlumnoDAOImpl;
import com.tesla.colegio.model.Alumno;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad alumno.
 * @author MV
 *
 */
@Repository
public class AlumnoDAO extends AlumnoDAOImpl{
	final static Logger logger = Logger.getLogger(AlumnoDAO.class);
    @Autowired
    private SQLUtil sqlUtil;
	
	public List<Map<String,Object>> listAluDoc(Integer id_alu) {// esta bien el query?
		
		String sql = "SELECT a.id, a.nom, m.id_ado FROM `cat_alu_documentos` a left join mat_matricula_doc m on a.id=m.id_ado and id_alu="+id_alu;
		List<Map<String,Object>> listFamiliar = jdbcTemplate.queryForList(sql);			
		return listFamiliar;
	} 
	
	/**
     * Actualizar foto
     * @param idAlu
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int updatePhoto(Integer idAlu, InputStream inputStream)  {
		
		String sql = "update alu_alumno set foto= ? where id=" + idAlu;
		
		//logger.info(sql);
		
        try {
			LobHandler lobHandler = new DefaultLobHandler(); 

			jdbcTemplate.update( sql,
			         new Object[] {new SqlLobValue(inputStream, inputStream.available(), lobHandler)},
			         new int[] {Types.BLOB});
		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return 1;
	}
	
    /**
     * Ver foto
     * @param idAlu
     * @return byte[]
     */	
	public Alumno getPhoto(Integer idAlu) {
		String sql = "select id_gen, foto from alu_alumno where id=" + idAlu;
		
		Alumno alumno = jdbcTemplate.query(sql, new ResultSetExtractor<Alumno>() {

			@Override
			public Alumno extractData(ResultSet rs) throws SQLException,DataAccessException {
                Alumno alumno = new Alumno();
				if (rs.next()) {
					LobHandler lobHandler1 = new DefaultLobHandler();
	                byte[] requestData = lobHandler1.getBlobAsBytes(rs,"foto");
	                alumno.setFoto(requestData);
	                alumno.setId_gen(rs.getString("id_gen"));
	                return alumno;
				}
				
				return alumno;

			}
			
		});
		
		return alumno;
	}
	
	/*Alumnos inscritos al examen Vacante*/
	public List<Map<String,Object>> Alumnos_MatrVacante(Integer id_eva,String apellidosNombres) {
		String q_aux=" ";
		if (apellidosNombres!=null){
			q_aux=" AND CONCAT(a.ape_pat,' ',a.ape_mat, ' ', a.nom) LIKE '%"+apellidosNombres+"%'";
		}
		String sql = "SELECT CONCAT(a.ape_pat,' ',a.ape_mat,' ',a.nom) AS 'Alumno', m.id AS 'id_matri', a.id AS 'id_alu'"
				+ " ,g.nom AS 'grado', n.nom AS 'nivel',m.id_eva FROM eva_matr_vacante m, alu_alumno a, cat_grad g, cat_nivel n,"
				+ "  eva_evaluacion_vac eva WHERE m.id_alu=a.id AND m.id_gra=g.id AND g.id_nvl=n.id AND m.id_eva=eva.id "
				+ " AND m.id_gra<>4 AND m.id_eva="+id_eva+" "+q_aux+" ORDER BY a.ape_pat asc";
		List<Map<String,Object>> AluMat = jdbcTemplate.queryForList(sql);			
		return AluMat;
	}
	
	/*Consulta de alumnos por nombre y apellidos*/
	public List<Map<String,Object>> alumnos_Matricula(String apellidosNombres) {
		String q_aux=" ";
		if (apellidosNombres!=null){
			q_aux=" AND CONCAT(a.nom,' ',a.ape_pat., ' ', a.ape_mat) LIKE '%"+apellidosNombres+"%'";
		}
		String sql = "SELECT CONCAT(a.ape_pat,' ',a.ape_mat,' ',a.nom) AS 'Alumno', m.id AS 'id_matri', a.id AS 'id_alu'"
				+ " ,g.id AS 'grado', n.id AS 'nivel',m.id_eva FROM eva_matr_vacante m, alu_alumno a, cat_grad g, cat_nivel n,"
				+ "  eva_evaluacion_vac eva WHERE m.id_alu=a.id AND m.id_gra=g.id AND g.id_nvl=n.id AND m.id_eva=eva.id and m.res='A'"
				+ q_aux+" ORDER BY a.ape_pat, a.ape_mat asc";
		List<Map<String,Object>> AluMat = jdbcTemplate.queryForList(sql);			
		return AluMat;
	}
	
	public boolean existsAlumno(Integer id, String dni) {
		
		String sql = "select * from col_persona where nro_doc ='" + dni + "'";

		if(id!=null){
			sql = sql + " and id!=" + id;
		}
		//logger.info(sql);

		List<Map<String,Object>> alumnos = jdbcTemplate.queryForList(sql);
		
		return (alumnos.size()>0);

	}
	
	
	
 
	
	public List<Row> listApoderados(Integer id_alu) {
		String sql = "select distinct fam.id, CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) as value from alu_gru_fam_alumno gfa";
		sql += " inner join alu_gru_fam gpf on gpf.id = gfa.id_gpf";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " inner join col_persona per on fam.id_per=per.id ";
		sql += " where gfa.id_alu = ? and (fam.id_par =? or fam.id_par =? )";
		Object[] params = new Object[]{id_alu, EnumParentesco.PARENTESCO_MAMA.getValue(), EnumParentesco.PARENTESCO_PAPA.getValue()};
	
		return sqlUtil.query(sql,params);			 
		
	}
	
	public List<Row> listApoderadosxGrupoFamiliar(Integer id_gpf) {
		String sql = "select distinct per.id, CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) as value, par.par AS aux1, per.cel aux2, per.nro_doc aux3, per.`id_tdc` aux4, per.corr aux5, per.dir aux6, par.id aux7 from alu_gru_fam_alumno gfa";
		sql += " inner join alu_gru_fam gpf on gpf.id = gfa.id_gpf";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " inner join col_persona per on fam.id_per=per.id ";
		sql += " INNER JOIN cat_parentesco par ON fam.id_par=par.id\n";  
		sql += " where gpf.id = ? and (fam.id_par =? or fam.id_par =? )";
		Object[] params = new Object[]{id_gpf, EnumParentesco.PARENTESCO_MAMA.getValue(), EnumParentesco.PARENTESCO_PAPA.getValue()};
	
		return sqlUtil.query(sql,params);			 
		
	}
	
	public List<Row> listTodosFamiliares(Integer id_gpf) {
		String sql = "select DISTINCT fam.id, par.par as value,  CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) as aux1, per.cel aux2, per.nro_doc, par.id id_par, per.ape_pat, per.ape_mat, fam.id_anio_act ";
		sql += "from alu_gru_fam gpf ";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " inner join col_persona per on fam.id_per=per.id";
		sql += " inner join cat_parentesco par on fam.id_par=par.id";
		sql += " where gpf.id=?";
		Object[] params = new Object[]{id_gpf};
	
		return sqlUtil.query(sql,params);			 
		
	}
	
	public List<Row> listTodosFamiliaresxGrupoFam(Integer id_gpf) {
		String sql = "select DISTINCT fam.id, CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) value, par.par aux1,  per.cel aux2, per.nro_doc aux3, par.id id_par, per.ape_pat, per.ape_mat, fam.id_anio_act ";
		sql += "from alu_gru_fam gpf ";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " inner join col_persona per on fam.id_per=per.id";
		sql += " inner join cat_parentesco par on fam.id_par=par.id";
		sql += " where gpf.id=?";
		Object[] params = new Object[]{id_gpf};
	
		return sqlUtil.query(sql,params);			 
		
	}
	
	public List<Row> listTodosIntegrantesFamilia(Integer id_gpf, Integer id_alu) {
		String sql = "SELECT DISTINCT per.id, CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) value, par.par AS aux1, per.cel aux2, per.nro_doc aux3, per.`id_tdc` aux4, per.corr aux5, per.dir aux6, per.ape_pat, per.ape_mat \n" + 
				"		FROM alu_gru_fam gpf \n" + 
				"		INNER JOIN alu_gru_fam_familiar gff ON gff.id_gpf = gpf.id\n" + 
				"		INNER JOIN alu_familiar fam ON fam.id = gff.id_fam\n" + 
				"		INNER JOIN col_persona per ON fam.id_per=per.id\n" + 
				"		INNER JOIN cat_parentesco par ON fam.id_par=par.id\n" + 
				"		WHERE gpf.id=? \n" + 
				"		UNION\n" + 
				"SELECT DISTINCT per.id, CONCAT_WS(' ',per.ape_pat, per.ape_mat, per.nom) value, IF("+id_alu+"=alu.id , 'MISMO ALUMNO','HERMANO') AS aux1, per.cel aux2, per.nro_doc aux3, per.`id_tdc` aux4, per.corr aux5,  per.dir aux6, per.ape_pat, per.ape_mat \n" + 
				"		FROM alu_gru_fam gpf \n" + 
				"		INNER JOIN `alu_gru_fam_alumno` gfa ON gpf.`id`=gfa.id_gpf\n" + 
				"		INNER JOIN `alu_alumno` alu ON alu.id = gfa.id_alu\n" + 
				"		INNER JOIN col_persona per ON alu.id_per=per.id\n" + 
				"		WHERE gpf.id=?";
		Object[] params = new Object[]{id_gpf,id_gpf};
	
		return sqlUtil.query(sql,params);			 
		
	}
	
	public List<Row> listPadres(Integer id_alu) {
		
		//Obtener la ultima matricula
		String sqlMatricula = "select id_fam from mat_matricula where id_alu=" + id_alu + " order by fecha desc limit 1";
		
		Integer id_fam = null;
		try {
			id_fam = sqlUtil.queryForObject(sqlMatricula, Integer.class);
		} catch (Exception e) {
		
		}
		
		
		String sql = "select fam.id, CONCAT(perf.ape_pat,' ',perf.ape_mat,', ', perf.nom) as padre, perf.nro_doc, perf.cel, perf.tlf , cel_val , par.par parentesco ";
		sql += " from alu_gru_fam_alumno gfa";
		sql += " inner join alu_gru_fam gpf on gpf.id = gfa.id_gpf";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " inner join col_persona perf on fam.id_per=perf.id ";
		sql += " inner join cat_parentesco par on par.id = fam.id_par ";
		sql += " where gfa.id_alu = ? and (fam.id_par =? or fam.id_par =? )";
		
		
		Object[] params = new Object[]{id_alu, EnumParentesco.PARENTESCO_MAMA.getValue(), EnumParentesco.PARENTESCO_PAPA.getValue()};

		List<Row> padres = sqlUtil.query(sql,params);		
		
		if(id_fam !=null)
		for (Row row : padres) {
			if(row.getInt("id") == id_fam.intValue())
				row.put("apoderado", "1");
		}	
		
		return padres;
	}
		    
	public List<Row> listOtrosFamiliares(Integer id_alu) {
		String sql = "select fam.* from alu_gru_fam_alumno gfa";
		sql += " inner join alu_gru_fam gpf on gpf.id = gfa.id_gpf";
		sql += " inner join alu_gru_fam_familiar gff on gff.id_gpf = gpf.id";
		sql += " inner join alu_familiar fam on fam.id = gff.id_fam ";
		sql += " where gfa.id_alu = ? and (fam.id_par !=? and fam.id_par !=? )";
		Object[] params = new Object[]{id_alu, EnumParentesco.PARENTESCO_MAMA.getValue(), EnumParentesco.PARENTESCO_PAPA.getValue()};

		return sqlUtil.query(sql,params);	
	}
	
	public List<Row> datosAlumno(Integer id_mat) {
		String sql = "SELECT mat.id, mat.id_fam, alu.cod, CONCAT (per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, per.ape_pat, per.nom, CONCAT('****',SUBSTRING(per.nro_doc,5,8)) nro_doc, concat(niv.`nom`,' - ', gra.`nom`,' - ', au.`secc`) aula, niv.`nom` nivel, gra.`nom` grado, au.`secc` seccion, IF(per.id_gen=0,'FEMENINO','MASCULINO') sexo, perf.cel `celular`,  TIMESTAMPDIFF(YEAR, per.`fec_nac`, CURDATE()) AS edad, alu.id id_alu"
				+ " FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
				+ " INNER JOIN col_persona per ON alu.id_per=per.id"
				+ " INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` "
				+ " INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				//+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				//+ " INNER JOIN col_persona perf ON alu.id_per=perf.id"
				+ " INNER JOIN col_persona perf ON mat.id_per_res=perf.id"
				+ " WHERE mat.`id`=?";
		Object[] params = new Object[]{id_mat};

		return sqlUtil.query(sql,params);	
	}	
	
	/**
	 * Listar Alumnos
	 * @param id_gpf
	 * @return
	 */
	
	public List<Row> listarAlumnos(Integer id_gpf, Integer id_anio){

		String sql = " SELECT alu.`id`, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act, "
				+ " t.nivel, t.grado, t.aula, t.id_mat, IF(t.id_mat IS NULL, 'No','Si') matricula, t.estado "
				+ " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id "
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` "
				+ " LEFT JOIN (SELECT mat.id_alu, mat.id id_mat, niv.nom nivel, gra.nom grado, au.secc aula, IF( peri.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado FROM mat_matricula mat  "
				+ " INNER JOIN col_aula au ON mat.id_au_asi=au.id "
				+ " INNER JOIN per_periodo peri ON au.id_per=peri.id "
				+ " INNER JOIN cat_grad gra ON mat.id_gra=gra.id "
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
				+ " WHERE peri.id_anio="+id_anio+") t ON t.id_alu=alu.id "
				+ " WHERE agfa.`id_gpf`=?";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_gpf}));
	}
	
	public List<Row> listarResponsablesMat(Integer id_mat){

		String sql = " SELECT CONCAT (per_mat.`ape_pat`,' ', per_mat.`ape_mat`,' ', per_mat.`nom`) responsable, per_mat.nro_doc, per_mat.`cel`, per_mat.`corr`, 'res_mat' res\n" + 
				"FROM `mat_matricula` mat \n" + 
				"INNER JOIN `alu_familiar` fam_mat ON mat.`id_fam`=fam_mat.`id`\n" + 
				"INNER JOIN `col_persona` per_mat ON fam_mat.id_per=per_mat.`id`\n" + 
				"WHERE mat.`id`=? \n" + 
				"UNION ALL\n" + 
				"SELECT CONCAT (per_eco.`ape_pat`,' ', per_eco.`ape_mat`,' ', per_eco.`nom`) responsable, per_eco.nro_doc, per_eco.`cel`, per_eco.`corr`, 'res_eco' res\n" + 
				"FROM `mat_matricula` mat \n" + 
				"INNER JOIN `alu_familiar` fam_eco ON mat.`id_fam_res_pag`=fam_eco.`id`\n" + 
				"INNER JOIN `col_persona` per_eco ON fam_eco.`id_per`=per_eco.`id`\n" + 
				"WHERE mat.`id`=? \n" + 
				"UNION ALL\n" + 
				"SELECT CONCAT (per_aca.`ape_pat`,' ', per_aca.`ape_mat`,' ', per_aca.`nom`) responsable,per_aca.nro_doc,  per_aca.`cel`, per_aca.`corr`, 'res_aca' res\n" + 
				"FROM `mat_matricula` mat \n" + 
				"INNER JOIN `alu_familiar` fam_aca ON mat.`id_fam_res_aca`=fam_aca.`id`\n" + 
				"INNER JOIN `col_persona` per_aca ON fam_aca.`id_per`=per_aca.`id`\n" + 
				"WHERE mat.`id`=? ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat, id_mat, id_mat}));
	}
	
	public List<Row> listarAlumnosMatriculaAnterioresAprobadosyRecuperacionNuevos(Integer id_gpf, Integer id_anio_ant, Integer id_anio_act){

		String sql = "SELECT DISTINCT * FROM  (\n" + 
				"				SELECT alu.`id`, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act \n" + 
				"				 FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \n" + 
				"				 INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` \n" + 
				"				 INNER JOIN (SELECT mat.id_alu, mat.id id_mat, niv.nom nivel, gra.nom grado, au.secc aula, IF( peri.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado FROM mat_matricula mat  \n" + 
				"				 INNER JOIN col_aula au ON mat.id_au_asi=au.id \n" + 
				"				 INNER JOIN per_periodo peri ON au.id_per=peri.id \n" + 
				"				 INNER JOIN cat_grad gra ON mat.id_gra=gra.id \n" + 
				"				 INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n" + 
				"				 WHERE peri.id_anio="+id_anio_ant+" AND peri.id_tpe=1 AND mat.id_gra<>'14' AND mat.`id_sit`<>5 )  t ON t.id_alu=alu.id \n" + 
				"				 WHERE agfa.`id_gpf`="+id_gpf+"\n" + 
				"				 UNION ALL\n" + 
				"				 SELECT alu.`id`, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act \n" + 
				"				 FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \n" + 
				"				 INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` \n" + 
				"				 INNER JOIN (SELECT mat.id_alu, mat.id id_mat, niv.nom nivel, gra.nom grado, au.secc aula, IF( peri.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado FROM mat_matricula mat  \n" + 
				"				 INNER JOIN col_aula au ON mat.id_au_asi=au.id \n" + 
				"				 INNER JOIN per_periodo peri ON au.id_per=peri.id \n" + 
				"				 INNER JOIN cat_grad gra ON mat.id_gra=gra.id \n" + 
				"				 INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n" + 
				"				 WHERE peri.id_anio="+id_anio_ant+" AND peri.id_tpe=1 AND mat.id_gra='14' AND mat.`id_sit`=2 )  t ON t.id_alu=alu.id \n" + 
				"				 WHERE agfa.`id_gpf`="+id_gpf+" \n" + 
				"				 UNION ALL\n" + 
				"				 SELECT alu.`id`, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act		\n" + 
				"				 FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id \n" + 
				"				 INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` \n" + 
				"				 INNER JOIN `mat_reserva` res ON alu.`id`=res.id_alu"+
				"				 INNER JOIN `per_periodo` peri ON res.id_per=peri.id"+
				//"				 INNER JOIN  `eva_matr_vacante` eva_matr ON alu.`id`=eva_matr.id_alu\n" + 
				//"				 INNER JOIN `eva_evaluacion_vac` eva ON eva_matr.id_eva=eva.id\n" + 
				//"				 INNER JOIN `per_periodo` per_eva ON eva.`id_per`=per_eva.`id` \n" + 
				"				 WHERE peri.id_anio="+id_anio_act+" AND agfa.`id_gpf`="+id_gpf+" ) l;\n" + 
				"				 ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	
	public List<Row> listarHijosxUsuario(Integer id_usr, Integer id_anio){

		String sql = " SELECT DISTINCT alu.`id` id_alu, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act, "
				+ " t.nivel, t.grado, t.aula, t.sucursal, t.niv_sig, t.gra_sig, t.id_mat, t.id_enc_alu, IF(t.id_mat IS NULL, 'No','Si') matricula, t.estado "
				+ " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id "
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` "
				+ " INNER JOIN alu_gru_fam gf ON agfa.id_gpf=gf.id "
				+ " INNER JOIN seg_usuario usr ON gf.id_usr=usr.id "
				+ " INNER JOIN (SELECT mat.id_alu, mat.id id_mat, enca.id id_enc_alu,  niv.nom nivel, gra.nom grado, suc.nom sucursal,  au.secc aula, IF( peri.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado, "
				+ " (CASE WHEN gra.id=1 THEN 'Inicial' "
				+ " WHEN gra.id=1 THEN 'Inicial' "
				+ " WHEN gra.id=2 THEN 'Inicial' "
				+ " WHEN gra.id=3 THEN 'Primaria' "
				+ " WHEN gra.id=4 THEN 'Primaria' "
				+ " WHEN gra.id=5 THEN 'Primaria' "
				+ " WHEN gra.id=6 THEN 'Primaria' "
				+ " WHEN gra.id=7 THEN 'Primaria' "
				+ " WHEN gra.id=8 THEN 'Primaria' "
				+ " WHEN gra.id=9 THEN 'Secundaria' "
				+ " WHEN gra.id=10 THEN 'Secundaria' "
				+ " WHEN gra.id=11 THEN 'Secundaria' "
				+ " WHEN gra.id=12 THEN 'Secundaria' "
				+ " WHEN gra.id=13 THEN 'Secundaria' "
				+ " ELSE '' END ) niv_sig ,"
				+ " ( CASE WHEN gra.id+1=2 THEN '4 AÑOS' "
				+ " WHEN gra.id+1=3 THEN '5 AÑOS' "
				+ " WHEN gra.id+1=4 THEN 'PRIMERO' "
				+ " WHEN gra.id+1=5 THEN 'SEGUNDO' "
				+ " WHEN gra.id+1=6 THEN 'TERCERO' "
				+ " WHEN gra.id+1=7 THEN 'CUARTO' "
				+ " WHEN gra.id+1=8 THEN 'QUINTO' "
				+ " WHEN gra.id+1=9 THEN 'SEXTO' "
				+ " WHEN gra.id+1=10 THEN 'PRIMERO' "
				+ " WHEN gra.id+1=11 THEN 'SEGUNDO' "
				+ " WHEN gra.id+1=12 THEN 'TERCERO' "
				+ " WHEN gra.id+1=13 THEN 'CUARTO' "
				+ " WHEN gra.id+1=14 THEN 'QUINTO' "
				+ " ELSE '' END ) gra_sig "
				+ " FROM mat_matricula mat  "
				+ " INNER JOIN col_aula au ON mat.id_au_asi=au.id "
				+ " INNER JOIN per_periodo peri ON au.id_per=peri.id "
				+ " INNER JOIN cat_grad gra ON mat.id_gra=gra.id "
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
				+ " INNER JOIN ges_sucursal suc ON peri.id_suc=suc.id "
				+ " LEFT JOIN col_encuesta_alumno enca ON enca.id_mat=mat.id "
				+ " WHERE peri.id_anio="+id_anio+" and gra.id<>'14' AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) ) t ON t.id_alu=alu.id "
				+ " WHERE usr.id=? ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_usr}));
	}
	
	public List<Row> listarHijosxUsuarioRatificacion(Integer id_usr, Integer id_anio){

		String sql = " SELECT DISTINCT alu.`id` id_alu, agfa.id_gpf, alu.cod, per.`nro_doc`, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, alu.id_anio_act, "
				+ " t.nivel, t.grado, t.aula, t.sucursal, t.niv_sig, t.gra_sig, t.id_mat, t.id_rat, t.res , IF(t.id_mat IS NULL, 'No','Si') matricula, t.estado "
				+ " FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id "
				+ " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu` "
				+ " INNER JOIN alu_gru_fam gf ON agfa.id_gpf=gf.id "
				+ " INNER JOIN seg_usuario usr ON gf.id_usr=usr.id "
				+ " INNER JOIN (SELECT mat.id_alu, mat.id id_mat, rat.id id_rat, rat.res,  niv.nom nivel, gra.nom grado, suc.nom sucursal,  au.secc aula, IF( peri.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado, "
				+ " (CASE WHEN gra.id=1 THEN 'Inicial' "
				+ " WHEN gra.id=1 THEN 'Inicial' "
				+ " WHEN gra.id=2 THEN 'Inicial' "
				+ " WHEN gra.id=3 THEN 'Primaria' "
				+ " WHEN gra.id=4 THEN 'Primaria' "
				+ " WHEN gra.id=5 THEN 'Primaria' "
				+ " WHEN gra.id=6 THEN 'Primaria' "
				+ " WHEN gra.id=7 THEN 'Primaria' "
				+ " WHEN gra.id=8 THEN 'Primaria' "
				+ " WHEN gra.id=9 THEN 'Secundaria' "
				+ " WHEN gra.id=10 THEN 'Secundaria' "
				+ " WHEN gra.id=11 THEN 'Secundaria' "
				+ " WHEN gra.id=12 THEN 'Secundaria' "
				+ " WHEN gra.id=13 THEN 'Secundaria' "
				+ " ELSE '' END ) niv_sig ,"
				+ " ( CASE WHEN gra.id+1=2 THEN '4 AÑOS' "
				+ " WHEN gra.id+1=3 THEN '5 AÑOS' "
				+ " WHEN gra.id+1=4 THEN 'PRIMERO' "
				+ " WHEN gra.id+1=5 THEN 'SEGUNDO' "
				+ " WHEN gra.id+1=6 THEN 'TERCERO' "
				+ " WHEN gra.id+1=7 THEN 'CUARTO' "
				+ " WHEN gra.id+1=8 THEN 'QUINTO' "
				+ " WHEN gra.id+1=9 THEN 'SEXTO' "
				+ " WHEN gra.id+1=10 THEN 'PRIMERO' "
				+ " WHEN gra.id+1=11 THEN 'SEGUNDO' "
				+ " WHEN gra.id+1=12 THEN 'TERCERO' "
				+ " WHEN gra.id+1=13 THEN 'CUARTO' "
				+ " WHEN gra.id+1=14 THEN 'QUINTO' "
				+ " ELSE '' END ) gra_sig "
				+ " FROM mat_matricula mat  "
				+ " INNER JOIN col_aula au ON mat.id_au_asi=au.id "
				+ " INNER JOIN per_periodo peri ON au.id_per=peri.id "
				+ " INNER JOIN cat_grad gra ON mat.id_gra=gra.id "
				+ " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id "
				+ " INNER JOIN ges_sucursal suc ON peri.id_suc=suc.id "
				+ " LEFT JOIN mat_ratificacion rat ON rat.id_mat=mat.id "
				+ " WHERE peri.id_anio="+id_anio+" and gra.id<>'14'AND peri.id_tpe=1 AND (mat.id_sit<>'5' OR mat.id_sit IS NULL) ) t ON t.id_alu=alu.id "
				+ " WHERE usr.id=? ";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_usr}));
	}
	
	/**
	 * Alumnos matriculados de Ese a�o
	 * @param id_gpf
	 * @param id_anio
	 * @return
	 */
	public List<Row> listarAlumnosMatriculados(Integer id_fam, Integer id_anio){

		String sql = " SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
				+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
				+ " INNER JOIN `mat_matricula` mat ON alu.`id`=mat.`id_alu`"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`"
				+ " INNER JOIN `alu_familiar` fam ON mat.`id_fam`=fam.`id`"
				+ " WHERE fam.`id`=? and per.id_anio=?";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_fam, id_anio}));
	}
	
	/**
	 * Listar Alumnos Inscritos al Campus Virtual, para un a�o determinado
	 * @param id_gpf
	 * @param is_anio
	 * @return
	 */
	public List<Row> listarAlumnosInsCV(Integer id_fam, Integer id_anio){

		String sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
				+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
				+ " INNER JOIN `mat_matricula` mat ON alu.`id`=mat.`id_alu`"
				+ " INNER JOIN `alu_familiar` fam ON fam.`id`=mat.`id_fam`"
				+ " INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`"
				+ " INNER JOIN `cvi_inscripcion_campus` cvi ON alu.id=cvi.`id_alu` AND cvi.`id_anio`=per.`id_anio` AND mat.`id_fam`=cvi.id_fam"
				+ " WHERE fam.`id`=? and cvi.id_anio=? and cvi.tc_acept=1";
		
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_fam, id_anio}));
	}
	
	
	
	public List<Map<String, Object>> listarHermanosxLocal(String tipoCronogramaVigente,Integer id_gpf,Integer id_anio,Integer id_anio_anterior,Integer id_suc){
		//"AC", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc)
		
		if (tipoCronogramaVigente==null) {
			StringBuilder sql = new StringBuilder("SELECT distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.id_gpf, if(t.id_mat_ant IS NOT NULL and t.id_sit<>'5','A','N') tipo, t.id_fac, t.canc FROM ("
					+ "SELECT alu.`id` id_alu, alu.`nro_doc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`,alu.`id_anio_act`, mat_ant.id id_mat_ant, mat.`id` id_mat,mat.`id_au_asi`, agfa.id_gpf, mat.id_sit , fac.`id` id_fac, fac.`canc`");
					sql.append(" FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.`id_alu`");
					sql.append(" LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=mat_ant.id_alu");
					sql.append(" LEFT JOIN `per_periodo` per_ant ON mat_ant.id_per=per_ant.`id` AND per_ant.`id_anio`="+id_anio_anterior);
					sql.append(" LEFT JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`");
					sql.append(" LEFT JOIN `per_periodo` per ON mat.`id_per`=per.`id` AND per.`id_anio`="+id_anio);
					sql.append(" LEFT JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT'");
					sql.append(" WHERE agfa.`id_gpf`="+id_gpf+")t");
					
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());

					return list;
		} 

		/*String sql=null;
		 
		
		if(cronograma=="AS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
		} else if(cronograma=="ASNS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
					+ " UNION ALL"
		}*/
		
		if (tipoCronogramaVigente.equals("AC")) {

			// alumnos del año anterior
			StringBuilder sql = new StringBuilder(
					"select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val FROM `alu_alumno` alu");
			sql.append(" inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append(" inner join `col_persona` p on p.id=alu.id_per ");
			sql.append(" inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id  ");
			sql.append(" inner join per_periodo per_ant on per_ant.id = mat_ant.id_per ");
			sql.append(" inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv ");
			sql.append(" inner join mat_cronograma cro on  cro.id_anio = ? ");
			sql.append(" left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=? AND per.id_tpe=1) on mat.id_alu = alu.id");//anidado
			sql.append(" left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "); 
			sql.append(" left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio=? ");
			sql.append(" left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sql.append(" left join per_periodo per_sug on per_sug.id = au_sug.id_per ");
			sql.append(" left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sql.append(" where UPPER(SUBSTR(p.ape_pat,1,CHAR_LENGTH(cro.del)))>=UPPER(cro.del) ");
			sql.append(" AND UPPER(SUBSTR(p.ape_pat,1,CHAR_LENGTH(cro.al)))<=UPPER(cro.al) and cro.fec_mat=current_date");
			sql.append(" and gfa.id_gpf="+id_gpf);
			sql.append(" and per_ant.id_anio=? ");
			sql.append(" and ( (mat.id is null && ((srv_sug.id_suc is not null and srv_sug.id_suc=?) or (srv_sug.id_suc is null and srv_ant.id_suc= ?))) || (mat.id is not null &&  per.id_suc=?) )");
			sql.append(" and mat_ant.id_sit in (1,3) and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");

			sql.append(" order by p.ape_pat,p.ape_mat,p.nom");


			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(),
					new Object[] { id_anio, id_anio, id_anio, id_anio_anterior, id_suc, id_suc, id_suc });

			return list;

		}

		if (tipoCronogramaVigente.equals("AS")) {

			// alumnos del a�o anterior CON O sin matricula ( falta probar )
			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD
			sql.append("\n  select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat,mat.id_au_asi, 'A' tipo,  gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val "); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id");  
			sql.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sql.append("\n inner join per_periodo per on per.id = mat.id_per"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " and per_ant.id_tpe=1)  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n gfa.id_gpf="+id_gpf);
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_suc="+id_suc+ " and per.id_tpe=1");
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or=" + id_suc + " )");

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val "); 
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1)  on mat.id_alu = alu.id");
			sqlA.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlA.append("\n where ");
			sqlA.append("\n gfa.id_gpf="+id_gpf);
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior + " and srv_ant.id_suc= " + id_suc);
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or="  + id_suc + " )");
			
//
			
			//matriculados CON SOLICITUD
			StringBuilder sqlSolicitud = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val ");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per and per.id_tpe=1");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1");  
			sqlSolicitud.append("\n where gfa.id_gpf="+id_gpf); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);

			//ANITUGOS NO matriculados CON SOLICITUD
			StringBuilder sqlNoMAtSoli = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlNoMAtSoli.append("\n where gfa.id_gpf="+id_gpf); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior+" AND (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlNoMAtSoli.append("\n and mat.id is null");


			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			StringBuilder sqlReservados = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per and per.id_tpe=1"); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservados.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");

			
			logger.info(sql);
			logger.info(sqlReservados);
			logger.info(sqlSolicitud);

			String sqlFinal = "select distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat,t.id_gpf, t.nom,t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.tipo, t.id_fac, t.canc, t.con_val from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados;
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal );

			return list;

		}

		if (tipoCronogramaVigente.equals("NC")) {
			 
			/*Nuevos con reserva*/
			String sql="select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, null id_mat_ant, mat_act.id id_mat,mat_act.id_au_asi, 'N' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val "; 
			sql += "\n from mat_reserva res ";
			sql += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sql += "\n inner join `col_persona` p on p.id=alu.id_per ";
			sql += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sql += "\n inner join per_periodo per on per.id = res.id_per ";
			sql += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sql += "\n left join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + ") on mat_act.id_alu=alu.id ";  
			sql += "\n left join fac_academico_pago fac on mat_act.id=fac.id_mat and fac.tip='MAT' ";
			sql += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + ") on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sql += "\n where gfa.id_gpf="+id_gpf; 
			sql += "\n and per.id_suc=" + id_suc;
			sql += "\n and per.id_anio=" + id_anio;
			//sql += ("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sql += "\n and mat_ant.id is null";
			/**nuevos con solicitud*/
			String sqlSolicitud = "select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi,  'A' tipo , gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
					+ "\n inner join `col_persona` p on p.id=alu.id_per "
					+ "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ "\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "
					+ "\n  where  gfa.id_gpf="+id_gpf
					+ "  and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";
			/**Reservados nuevos con solicitud**/
			StringBuilder sqlReservadosSolicitud = new StringBuilder("select alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, null id_mat_ant,mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf,fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservadosSolicitud.append("\n from mat_solicitud sol  ");
			sqlReservadosSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservadosSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSolicitud.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_matr_vacante` emv ON alu.`id`=emv.`id_alu` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_evaluacion_vac` eva ON emv.`id_eva`=eva.`id` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `per_periodo` per_eva ON eva.`id_per`=per_eva.`id` ");
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSolicitud.append("\n inner join per_periodo per on per.id = res.id_per AND per.`id_anio`=per_eva.`id_anio`");
			sqlReservadosSolicitud.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservadosSolicitud.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			//sqlReservadosSolicitud.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSolicitud.append("\n WHERE gfa.id_gpf="+id_gpf+" and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservadosSolicitud.append("\n and sol.est='A' ");
			sqlReservadosSolicitud.append("\n and per.id_anio=" + id_anio);
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSolicitud.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO

			String sqlFinal = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi,  t.tipo, t.id_gpf, t.id_fac, t.canc, t.con_val from (\n "
					+ sql.toString();
			sqlFinal = sqlFinal + "\n  union \n " + sqlSolicitud+" \n union \n"+sqlReservadosSolicitud;
			sqlFinal = sqlFinal
					+ " ) t\n  WHERE t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"
					//+ " ) t\n  WHERE t.id_mat is null and t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"
					+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') order by t.ape_pat, t.ape_mat, t.nom";

			// sqlFinal = sqlFinal + " ) t order by t.ape_pat,t.ape_mat,t.nom";

			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);
			return list;

		}

		if (tipoCronogramaVigente.equals("ASNS")) {

			// union ANTIGUO SIN CRONOGRAMA

			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD
			sql.append("\n  select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val"); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id"); 
			sql.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sql.append("\n inner join per_periodo per on per.id = mat.id_per"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n gfa.id_gpf="+id_gpf);
			sql.append("\n and per.id_anio= " + id_anio);
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_des=" + id_suc + " )");

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val"); 
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " )  on mat.id_alu = alu.id");
			sqlA.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlA.append("\n where ");
			sqlA.append("\n gfa.id_gpf="+id_gpf);
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior + " and srv_ant.id_suc= " + id_suc);
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or="  + id_suc + " )");
			
//
			
			//matriculados CON SOLICITUD
			StringBuilder sqlSolicitud = new StringBuilder("select distinct distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat,mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per ");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where gfa.id_gpf="+id_gpf); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);

			//ANITUGOS NO matriculados CON SOLICITUD
			StringBuilder sqlNoMAtSoli = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi,'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlNoMAtSoli.append("\n where gfa.id_gpf="+id_gpf); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlNoMAtSoli.append("\n and mat.id is null");



			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			StringBuilder sqlReservados = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat,mat.id_au_asi,'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservados.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");

			
			StringBuilder sqlReservadosSS = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat,mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservadosSS.append("\n from mat_reserva res ");
			sqlReservadosSS.append("\n inner join alu_alumno alu on res.id_alu=alu.id ");
			sqlReservadosSS.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSS.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSS.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = alu.id"); 
			sqlReservadosSS.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservadosSS.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSS.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservadosSS.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = alu.id");
			sqlReservadosSS.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservadosSS.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservadosSS.append("\n and per.id_anio=" + id_anio);
			sqlReservadosSS.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservadosSS.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSS.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservadosSS.append("\n and mat.id is null");
			sqlReservadosSS.append("\n and alu.id not in"); 
			sqlReservadosSS.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or="  + id_suc + " )");
			logger.info(sql);
			logger.info(sqlReservados);
			logger.info(sqlSolicitud);	
			
			//NUEVO SIN CRONOGRAMA
			String sqlNS="select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, null id_mat_ant, mat_act.id id_mat,mat_act.id_au_asi, 'N' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat_act.con_val "; 
			sqlNS += "\n from mat_reserva res ";
			sqlNS += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sqlNS += "\n inner join `col_persona` p on p.id=alu.id_per ";
			sqlNS += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sqlNS += "\n inner join per_periodo per on per.id = res.id_per ";
			sqlNS += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sqlNS += "\n left  join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + ") on mat_act.id_alu=alu.id ";  
			sqlNS += "\n left join fac_academico_pago fac on mat_act.id=fac.id_mat and fac.tip='MAT' ";
			sqlNS += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + ") on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sqlNS += "\n  where gfa.id_gpf="+id_gpf;
			sqlNS += "\n and per.id_suc=" + id_suc;
			sqlNS += "\n and per.id_anio=" + id_anio;
			//sqlNS += "\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")";
			sqlNS += "\n and (mat_ant.id is null OR mat_ant.id_sit in (4,5) )";
			/**nuevos con solicitud*/
			String sqlSolicitudNS = "select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc,mat.con_val "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
					+ "\n inner join `col_persona` p on p.id=alu.id_per "
					+ "\n  inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ "\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "
					+ "\n  where gfa.id_gpf="+id_gpf
					+ "\n and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";
			/**Reservados nuevos con solicitud**/
			StringBuilder sqlReservadosSolicitud = new StringBuilder("select alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act,null id_mat_ant,null id_mat, null id_au_asi, 'A' tipo, gfa.id_gpf,null id_fac, null canc, null con_val");   
			sqlReservadosSolicitud.append("\n from mat_solicitud sol  ");
			sqlReservadosSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservadosSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu and gfa.id_gpf="+id_gpf);
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_matr_vacante` emv ON alu.`id`=emv.`id_alu` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_evaluacion_vac` eva ON emv.`id_eva`=eva.`id` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `per_periodo` per_eva ON eva.`id_per`=per_eva.`id` ");
			sqlReservadosSolicitud.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSolicitud.append("\n inner join per_periodo per on per.id = res.id_per and per.id_anio=" + id_anio+" AND per.`id_anio`=per_eva.`id_anio`"); 
			//sqlReservadosSolicitud.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			//sqlReservadosSolicitud.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			//sqlReservadosSolicitud.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservadosSolicitud.append("\n and sol.est='A' ");
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSolicitud.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO

			String sqlFinalNS = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant,t.id_mat,t.id_au_asi, t.tipo, t.id_gpf, t.id_fac, t.canc, t.con_val from (\n "
					+ sqlNS.toString();
			sqlFinalNS = sqlFinalNS + "\n  union \n " + sqlSolicitudNS+"\n union \n"+sqlReservadosSolicitud;
			sqlFinalNS = sqlFinalNS	+ " ) t\n  WHERE t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"	+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') ";
			
			String sqlFinal = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.tipo, t.id_gpf, t.id_fac, t.canc, t.con_val from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados + "\n  union \n" + sqlReservadosSS;//AS
			sqlFinal = sqlFinal + "\n union \n" + sqlFinalNS;//NS
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);

			return list;

		}

		return new ArrayList<Map<String, Object>>();

	}
	
	public List<Map<String, Object>> listarTodosHermanosxGruFam(String tipoCronogramaVigente,Integer id_gpf,Integer id_anio,Integer id_anio_anterior,Integer id_suc){
		//"AC", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc)
		
		if (tipoCronogramaVigente==null) {
			StringBuilder sql = new StringBuilder("SELECT distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.id_gpf, if(t.id_mat_ant IS NOT NULL and t.id_sit<>'5','A','N') tipo, t.id_fac, t.canc FROM ("
					+ "\n SELECT alu.`id` id_alu, alu.`nro_doc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`,alu.`id_anio_act`, mat_ant.id id_mat_ant, mat.`id` id_mat,mat.`id_au_asi`, agfa.id_gpf, mat.id_sit , fac.`id` id_fac, fac.`canc`");
					sql.append("\n FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.`id_alu`");
					sql.append("\n LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=mat_ant.id_alu");
					sql.append("\n LEFT JOIN `per_periodo` per_ant ON mat_ant.id_per=per_ant.`id` AND per_ant.`id_anio`="+id_anio_anterior);
					sql.append("\n LEFT JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`");
					sql.append("\n LEFT JOIN `per_periodo` per ON mat.`id_per`=per.`id` AND per.`id_anio`="+id_anio+ "AND per.id_tpe=1");
					sql.append("\n LEFT JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT'");
					sql.append("\n WHERE agfa.`id_gpf`="+id_gpf+")t");
					
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());

					return list;
		} 

		/*String sql=null;
		 
		
		if(cronograma=="AS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
		} else if(cronograma=="ASNS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
					+ " UNION ALL"
		}*/
		
		if (tipoCronogramaVigente.equals("AC")) {

			// alumnos del a�o anterior
			StringBuilder sql = new StringBuilder(
					"select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per_sug.id_suc id_suc_sug, suc_sug.nom sucursal, niv_sug.nom nivel, gra_sug.nom grado  FROM `alu_alumno` alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id  ");
			sql.append("\n inner join per_periodo per_ant on per_ant.id = mat_ant.id_per ");
			sql.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv ");
			sql.append("\n inner join cvi_inscripcion_campus ins on mat_ant.id_alu=ins.id_alu and per_ant.id_anio=ins.id_anio");
			sql.append("\n inner join mat_cronograma cro on  cro.id_anio = ? ");
			sql.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=? AND per.id_tpe=1) on mat.id_alu = alu.id");//anidado
			sql.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "); 
			sql.append("\n left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio=? ");
			sql.append("\n left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sql.append("\n LEFT JOIN col_ciclo cic_sug ON cic_sug.id = au_sug.id_cic ");
			sql.append("\n LEFT JOIN per_periodo per_sug ON per_sug.id = cic_sug.id_per  ");
			sql.append("\n left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sql.append("\n left join ges_sucursal suc_sug on suc_sug.id = per_sug.id_suc ");
			sql.append("\n left join cat_nivel niv_sug on niv_sug.id = per_sug.id_niv ");
			sql.append("\n left join cat_grad gra_sug on gra_sug.id = au_sug.id_grad ");
			//sql.append("\n where UPPER(SUBSTR(alu.ape_pat,1,CHAR_LENGTH(cro.del)))>=UPPER(cro.del) ");
			//sql.append("\n AND UPPER(SUBSTR(alu.ape_pat,1,CHAR_LENGTH(cro.al)))<=UPPER(cro.al) and cro.fec_mat=current_date");
			sql.append("\n where gfa.id_gpf="+id_gpf);
			sql.append("\n and per_ant.id_anio=? ");
			//sql.append(" and ( (mat.id is null && ((srv_sug.id_suc is not null and srv_sug.id_suc=?) or (srv_sug.id_suc is null and srv_ant.id_suc= ?))) || (mat.id is not null &&  per.id_suc=?) )");
			sql.append("\n and ( (mat.id is null) || (mat.id is not null) )");
			sql.append("\n and mat_ant.id_sit in (1,3) and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");

			sql.append("\n order by alu.ape_pat,alu.ape_mat,alu.nom");


			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(),
					//new Object[] { id_anio, id_anio, id_anio, id_anio_anterior, id_suc, id_suc, id_suc });
					new Object[] { id_anio, id_anio, id_anio, id_anio_anterior});

			return list;

		}

		if (tipoCronogramaVigente.equals("AS")) {

			// alumnos del a�o anterior CON O sin matricula ( falta probar )
			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD Comentado para 2021
			sql.append("\n  select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat,mat.id_au_asi, 'A' tipo,  gfa.id_gpf, fac.id id_fac, fac.canc "); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id");  
			sql.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sql.append("\n inner join per_periodo per on per.id = mat.id_per"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n gfa.id_gpf="+id_gpf);
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_suc="+id_suc);
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or=" + id_suc + " )");

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc "); 
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1)  on mat.id_alu = alu.id");
			sqlA.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlA.append("\n where ");
			sqlA.append("\n gfa.id_gpf="+id_gpf);
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior ); //and srv_ant.id_suc= " + id_suc
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " )"); // and s.id_suc_or="  + id_suc + " 
			
//
			
			//matriculados CON SOLICITUD
			StringBuilder sqlSolicitud = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc ");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per ");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where gfa.id_gpf="+id_gpf); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);

			//ANITUGOS NO matriculados CON SOLICITUD
			StringBuilder sqlNoMAtSoli = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlNoMAtSoli.append("\n where gfa.id_gpf="+id_gpf); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior+" AND (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlNoMAtSoli.append("\n and mat.id is null");


			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			StringBuilder sqlReservados = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservados.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");

			
			logger.info(sql);
			logger.info(sqlReservados);
			logger.info(sqlSolicitud);

			String sqlFinal = "select distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat,t.id_gpf, t.nom,t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.tipo, t.id_fac, t.canc from (";
			sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados;
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal );

			return list;

		}
		return new ArrayList<Map<String, Object>>();
	}
	
	public List<Map<String, Object>> listarTodosHermanosxGruFamparaWeb(String tipoCronogramaVigente,Integer id_gpf,Integer id_anio,Integer id_anio_anterior){
		//"AC", apellidosNombres.trim(), id_anio, anioAnterior.getId(), id_suc)
		
		if (tipoCronogramaVigente==null) {
			StringBuilder sql = new StringBuilder("SELECT distinct t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.id_gpf, if(t.id_mat_ant IS NOT NULL and t.id_sit<>'5','A','N') tipo, t.id_fac, t.canc FROM ("
					+ "\n SELECT alu.`id` id_alu, alu.`nro_doc`, alu.`ape_pat`, alu.`ape_mat`, alu.`nom`,alu.`id_anio_act`, mat_ant.id id_mat_ant, mat.`id` id_mat,mat.`id_au_asi`, agfa.id_gpf, mat.id_sit , fac.`id` id_fac, fac.`canc`");
					sql.append("\n FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.`id_alu`");
					sql.append("\n LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=mat_ant.id_alu");
					sql.append("\n LEFT JOIN `per_periodo` per_ant ON mat_ant.id_per=per_ant.`id` AND per_ant.`id_anio`="+id_anio_anterior);
					sql.append("\n LEFT JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`");
					sql.append("\n LEFT JOIN `per_periodo` per ON mat.`id_per`=per.`id` AND per.`id_anio`="+id_anio+ "AND per.id_tpe=1");
					sql.append("\n LEFT JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat` AND fac.`tip`='MAT'");
					sql.append("\n WHERE agfa.`id_gpf`="+id_gpf+")t");
					
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());

					return list;
		} 

		/*String sql=null;
		 
		
		if(cronograma=="AS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
		} else if(cronograma=="ASNS"){
			sql = "SELECT alu.`id`, agfa.id_gpf, alu.`nro_doc`, CONCAT(alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno"
					+ " FROM `alu_alumno` alu INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`"
					+ " LEFT JOIN `mat_matricula` mat_ant ON alu.`id`=`mat_ant`.`id_alu`"
					+ " LEFT JOIN `col_aula` au_ant ON mat_ant.`id_au_asi`=au_ant.`id`"
					+ " LEFT JOIN `per_periodo` per_ant ON `mat_ant`.id_per=per_ant.`id`"
					+ " LEFT JOIN `ges_sucursal` suc ON per_ant.`id_suc`=suc.`id`"
					+ " WHERE agfa.`id_gpf`="+id_gpf+" AND suc.`id`="+id_suc;
					+ " UNION ALL"
		}*/
		
		if (tipoCronogramaVigente.equals("AC")) {

			// alumnos del a�o anterior
			StringBuilder sql = new StringBuilder(
					"select distinct alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per_sug.id_suc id_suc_sug, suc_act.nom sucursal, cme.nom modalidad, niv_act.nom nivel, gra_sug.nom grado , ");
			sql.append("CASE \n"); 
			sql.append("WHEN mat_ant.id_sit=1 THEN mat_ant.id_gra+1 \n");
			sql.append("WHEN mat_ant.id_sit=3 THEN mat_ant.id_gra\n" ); 
			sql.append("ELSE ''\n");
			sql.append("END \n");
			sql.append("gra_sig ");
			sql.append(" FROM `alu_alumno` alu");
			sql.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id  ");
			sql.append("\n inner join mat_ratificacion mat_rat on  mat_rat.id_mat = mat_ant.id AND mat_rat.res=1 ");
			sql.append("\n inner join per_periodo per_ant on per_ant.id = mat_ant.id_per ");
			sql.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv ");
			//sql.append("\n inner join cvi_inscripcion_campus ins on mat_ant.id_alu=ins.id_alu and per_ant.id_anio=ins.id_anio");
			sql.append("\n inner join mat_cronograma cro on  cro.id_anio = ? ");
			sql.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=? AND per.id_tpe=1) on mat.id_alu = alu.id");//anidado
			sql.append("\n left join col_aula auact ON mat.id_au_asi=auact.id ");
			sql.append("\n left join cat_modalidad_estudio cme ON auact.id_cme=cme.id "); 
			sql.append("\n left join per_periodo peract ON auact.id_per=peract.id "); 
			sql.append("\n left join ges_servicio srv_act on srv_act.id = peract.id_srv ");
			sql.append("\n left join ges_sucursal suc_act on suc_act.id = peract.id_suc ");
			sql.append("\n left join cat_nivel niv_act on niv_act.id = peract.id_niv ");
			sql.append("\n left join cat_grad gra_act on gra_act.id_nvl = niv_act.id ");
			sql.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "); 
			sql.append("\n left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio=? ");
			sql.append("\n left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sql.append("\n LEFT JOIN col_ciclo cic_sug ON cic_sug.id = au_sug.id_cic ");
			sql.append("\n LEFT JOIN per_periodo per_sug ON per_sug.id = cic_sug.id_per  ");
			sql.append("\n left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sql.append("\n left join ges_sucursal suc_sug on suc_sug.id = per_sug.id_suc ");
			sql.append("\n left join cat_nivel niv_sug on niv_sug.id = per_sug.id_niv ");
			sql.append("\n left join cat_grad gra_sug on gra_sug.id = au_sug.id_grad ");
			//sql.append("\n where UPPER(SUBSTR(alu.ape_pat,1,CHAR_LENGTH(cro.del)))>=UPPER(cro.del) ");
			//sql.append("\n AND UPPER(SUBSTR(alu.ape_pat,1,CHAR_LENGTH(cro.al)))<=UPPER(cro.al) and cro.fec_mat=current_date");
			sql.append("\n where gfa.id_gpf="+id_gpf);
			sql.append("\n and per_ant.id_anio=? ");
			//sql.append(" and ( (mat.id is null && ((srv_sug.id_suc is not null and srv_sug.id_suc=?) or (srv_sug.id_suc is null and srv_ant.id_suc= ?))) || (mat.id is not null &&  per.id_suc=?) )");
			sql.append("\n and ( (mat.id is null) || (mat.id is not null) )");
			sql.append("\n and mat_ant.id_sit in (1,3) and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");

			sql.append("\n order by pera.ape_pat,pera.ape_mat,pera.nom");


			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(),
					//new Object[] { id_anio, id_anio, id_anio, id_anio_anterior, id_suc, id_suc, id_suc });
					new Object[] { id_anio, id_anio, id_anio, id_anio_anterior});

			return list;

		}

		if (tipoCronogramaVigente.equals("AS")) {

			// alumnos del a�o anterior CON O sin matricula ( falta probar )
			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD Comentado para 2021
			/*sql.append("\n  select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat,mat.id_au_asi, 'A' tipo,  gfa.id_gpf, fac.id id_fac, fac.canc "); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id");  
			sql.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sql.append("\n inner join per_periodo per on per.id = mat.id_per"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n gfa.id_gpf="+id_gpf);
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_suc="+id_suc);
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or=" + id_suc + " )");*/

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, pera.nro_doc, pera.ape_pat, pera.ape_mat, pera.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per_sug.id_suc id_suc_sug, suc_act.nom sucursal, cme.nom modalidad, niv_act.nom nivel, gra_sug.nom grado, "); 
			sqlA.append("CASE \n"); 
			sqlA.append("WHEN mat_ant.id_sit=1 THEN mat_ant.id_gra+1 \n");
			sqlA.append("WHEN mat_ant.id_sit=3 THEN mat_ant.id_gra\n" ); 
			sqlA.append("ELSE ''\n");
			sqlA.append("END \n");
			sqlA.append("gra_sig ");
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `col_persona` pera on alu.id_per=pera.id ");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join mat_ratificacion mat_rat on  mat_rat.id_mat = mat_ant.id AND mat_rat.res=1 ");
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1)  on mat.id_alu = alu.id");
			sqlA.append("\n left join col_aula auact ON mat.id_au_asi=auact.id ");
			sqlA.append("\n left join cat_modalidad_estudio cme ON auact.id_cme=cme.id "); 
			sqlA.append("\n left join per_periodo peract ON auact.id_per=peract.id "); 
			sqlA.append("\n left join ges_servicio srv_act on srv_act.id = peract.id_srv ");
			sqlA.append("\n left join ges_sucursal suc_act on suc_act.id = peract.id_suc ");
			sqlA.append("\n left join cat_nivel niv_act on niv_act.id = peract.id_niv ");
			sqlA.append("\n left join cat_grad gra_act on gra_act.id_nvl = niv_act.id ");
			sqlA.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlA.append("\n left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio="+id_anio);
			sqlA.append("\n left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sqlA.append("\n LEFT JOIN col_ciclo cic_sug ON cic_sug.id = au_sug.id_cic ");
			sqlA.append("\n LEFT JOIN per_periodo per_sug ON per_sug.id = cic_sug.id_per  ");
			sqlA.append("\n left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sqlA.append("\n left join ges_sucursal suc_sug on suc_sug.id = per_sug.id_suc ");
			sqlA.append("\n left join cat_nivel niv_sug on niv_sug.id = per_sug.id_niv ");
			sqlA.append("\n left join cat_grad gra_sug on gra_sug.id = au_sug.id_grad ");
			sqlA.append("\n where ");
			sqlA.append("\n gfa.id_gpf="+id_gpf);
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior ); //and srv_ant.id_suc= " + id_suc
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " )"); // and s.id_suc_or="  + id_suc + " 
			
//
			
			//matriculados CON SOLICITUD
			/*StringBuilder sqlSolicitud = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc ");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per ");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where gfa.id_gpf="+id_gpf); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);*/

			//ANITUGOS NO matriculados CON SOLICITUD
			/*StringBuilder sqlNoMAtSoli = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlNoMAtSoli.append("\n where gfa.id_gpf="+id_gpf); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior+" AND (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlNoMAtSoli.append("\n and mat.id is null");*/


			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			/*StringBuilder sqlReservados = new StringBuilder("select distinct alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom,alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservados.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");*/

			
			logger.info(sql);
			//logger.info(sqlReservados);
			//logger.info(sqlSolicitud);

			String sqlFinal = "select distinct t.id_alu, t.gra_sig, t.nro_doc, t.ape_pat, t.ape_mat,t.id_gpf, t.nom,t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.tipo, t.id_fac, t.canc, t.sucursal, t.modalidad, t.nivel, t.grado  from (";
			//sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados;
			//sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA +" ";
			sqlFinal = sqlFinal + " " + sqlA +" ";
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal );

			return list;

		}
		if (tipoCronogramaVigente.equals("NC")) {
			 
			/*Nuevos con reserva*/
			String sql="select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, null id_mat_ant, mat_act.id id_mat,mat_act.id_au_asi, 'N' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per.id_suc id_suc_sug, suc.nom sucursal, niv.nom nivel, gra.nom grado "; 
			sql += "\n from mat_reserva res ";
			sql += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sql += "\n inner join `col_persona` p on p.id=alu.id_per ";
			sql += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sql += "\n inner join per_periodo per on per.id = res.id_per ";
			sql += "\n inner join ges_sucursal suc on per.id_suc = suc.id ";
			sql += "\n inner join col_aula au on au.id = res.id_au ";
			sql += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sql += "\n inner join cat_nivel niv on niv.id = gra.id_nvl ";
			sql += "\n left join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + ") on mat_act.id_alu=alu.id ";  
			sql += "\n left join fac_academico_pago fac on mat_act.id=fac.id_mat and fac.tip='MAT' ";
			sql += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + ") on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sql += "\n where gfa.id_gpf="+id_gpf; 
			//sql += "\n and per.id_suc=" + id_suc;
			sql += "\n and per.id_anio=" + id_anio;
			//sql += ("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sql += "\n and mat_ant.id is null";
			/**nuevos con solicitud*/
			/*String sqlSolicitud = "select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi,  'A' tipo , gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
					+ "\n inner join `col_persona` p on p.id=alu.id_per "
					+ "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ "\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "
					+ "\n  where  gfa.id_gpf="+id_gpf
					+ "  and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";*/
			/**Reservados nuevos con solicitud**/
			/*StringBuilder sqlReservadosSolicitud = new StringBuilder("select alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, null id_mat_ant,mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf,fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservadosSolicitud.append("\n from mat_solicitud sol  ");
			sqlReservadosSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservadosSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSolicitud.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_matr_vacante` emv ON alu.`id`=emv.`id_alu` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_evaluacion_vac` eva ON emv.`id_eva`=eva.`id` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `per_periodo` per_eva ON eva.`id_per`=per_eva.`id` ");
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSolicitud.append("\n inner join per_periodo per on per.id = res.id_per AND per.`id_anio`=per_eva.`id_anio`");
			sqlReservadosSolicitud.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservadosSolicitud.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			//sqlReservadosSolicitud.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSolicitud.append("\n WHERE gfa.id_gpf="+id_gpf+" and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservadosSolicitud.append("\n and sol.est='A' ");
			sqlReservadosSolicitud.append("\n and per.id_anio=" + id_anio);
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSolicitud.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");*/
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO

			String sqlFinal = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi,  t.tipo, t.id_gpf, t.id_fac, t.canc, t.con_val from (\n "
					+ sql.toString();
			//sqlFinal = sqlFinal + "\n  union \n " + sqlSolicitud+" \n union \n"+sqlReservadosSolicitud;
			sqlFinal = sqlFinal
					+ " ) t\n "
					//+ " ) t\n  WHERE t.id_mat is null and t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"
					//+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') order by t.ape_pat, t.ape_mat, t.nom";
					+ " order by t.ape_pat, t.ape_mat, t.nom";
			// sqlFinal = sqlFinal + " ) t order by t.ape_pat,t.ape_mat,t.nom";

			logger.info(sql);

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);
			return list;

		}
		
		if (tipoCronogramaVigente.equals("ASNS")) {

			// union ANTIGUO SIN CRONOGRAMA

			StringBuilder sql = new StringBuilder();
			//MATRICULADOS SIN SOLICITUD
			/*sql.append("\n  select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val"); 
			sql.append("\n FROM alu_alumno alu");
			sql.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sql.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sql.append("\n inner join mat_matricula mat on  mat.id_alu = alu.id"); 
			sql.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sql.append("\n inner join per_periodo per on per.id = mat.id_per"); 
			sql.append("\n left join ( mat_matricula mat_ant inner join col_aula au_ant on au_ant.id= mat_ant.id_au_asi inner join per_periodo per_ant  on au_ant.id_per= per_ant.id and per_ant.id_anio=" + id_anio_anterior + " )  on mat_ant.id_alu = alu.id");
			sql.append("\n where"); 
			sql.append("\n gfa.id_gpf="+id_gpf);
			sql.append("\n and per.id_anio= " + id_anio+" and per.id_tpe=1");
			sql.append("\n and alu.id not in"); 
			sql.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " )"); // and s.id_suc_des=" + id_suc + "*/

			//ANTOGIO NO MATRICULADO SIN SOLICITUD
			StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per_sug.id_suc id_suc_sug, suc_act.nom sucursal, cme.nom modalidad, niv_act.nom nivel, niv_sug.id id_niv_sug, gra_sug.nom grado, gra_sug.id id_gra_sug,  "); 
			sqlA.append("CASE \n"); 
			sqlA.append("WHEN mat_ant.id_sit=1 THEN mat_ant.id_gra+1 \n");
			sqlA.append("WHEN mat_ant.id_sit=3 THEN mat_ant.id_gra\n" ); 
			sqlA.append("ELSE ''\n");
			sqlA.append("END \n");
			sqlA.append("gra_sig ");
			sqlA.append("\n FROM alu_alumno alu");
			sqlA.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlA.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlA.append("\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id");  
			sqlA.append("\n inner join mat_ratificacion mat_rat on  mat_rat.id_mat = mat_ant.id AND mat_rat.res=1 ");
			sqlA.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlA.append("\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per and per_ant.id_tpe=1");
			sqlA.append("\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv");
			sqlA.append("\n left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio=" + id_anio + " and per.id_tpe=1)  on mat.id_alu = alu.id");
			sqlA.append("\n left join col_aula auact ON mat.id_au_asi=auact.id ");
			sqlA.append("\n left join cat_modalidad_estudio cme ON auact.id_cme=cme.id "); 
			sqlA.append("\n left join per_periodo peract ON auact.id_per=peract.id "); 
			sqlA.append("\n left join ges_servicio srv_act on srv_act.id = peract.id_srv ");
			sqlA.append("\n left join ges_sucursal suc_act on suc_act.id = peract.id_suc ");
			sqlA.append("\n left join cat_nivel niv_act on niv_act.id = peract.id_niv ");
			sqlA.append("\n left join cat_grad gra_act on gra_act.id_nvl = niv_act.id ");
			sqlA.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlA.append("\n left join mat_seccion_sugerida sug on  sug.id_mat = mat_ant.id and sug.id_anio="+id_anio);
			sqlA.append("\n left join col_aula au_sug on au_sug.id = sug.id_au_nue ");
			sqlA.append("\n LEFT JOIN col_ciclo cic_sug ON cic_sug.id = au_sug.id_cic ");
			sqlA.append("\n LEFT JOIN per_periodo per_sug ON per_sug.id = cic_sug.id_per  ");
			sqlA.append("\n left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ");
			sqlA.append("\n left join ges_sucursal suc_sug on suc_sug.id = per_sug.id_suc ");
			sqlA.append("\n left join cat_nivel niv_sug on niv_sug.id = per_sug.id_niv ");
			sqlA.append("\n left join cat_grad gra_sug on gra_sug.id = au_sug.id_grad ");
			sqlA.append("\n where ");
			sqlA.append("\n gfa.id_gpf="+id_gpf);
			sqlA.append("\n and per_ant.id_anio=" + id_anio_anterior ); // and srv_ant.id_suc= " + id_suc
			sqlA.append("\n and mat_ant.id_sit in (1,3)");
			sqlA.append("\n and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1))");
			sqlA.append("\n and mat.id is null");
			sqlA.append("\n and alu.id not in"); 
			sqlA.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " )"); // and s.id_suc_or="  + id_suc + "
			
//
			
			//matriculados CON SOLICITUD
			/*StringBuilder sqlSolicitud = new StringBuilder("select distinct distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat,mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlSolicitud.append("\n from mat_solicitud sol  ");
			sqlSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlSolicitud.append("\n inner join mat_matricula mat on mat.id_alu = sol.id_alu"); 
			sqlSolicitud.append("\n inner join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlSolicitud.append("\n inner join per_periodo per on per.id = mat.id_per ");
			sqlSolicitud.append("\n left join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu");
			sqlSolicitud.append("\n left join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlSolicitud.append("\n left join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlSolicitud.append("\n where gfa.id_gpf="+id_gpf); 
			sqlSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlSolicitud.append("\n and sol.est='A' ");
			sqlSolicitud.append("\n and per.id_anio=" + id_anio);
			sqlSolicitud.append("\n and per_ant.id_anio=" + id_anio_anterior);*/

			//ANITUGOS NO matriculados CON SOLICITUD
			/*StringBuilder sqlNoMAtSoli = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi,'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlNoMAtSoli.append("\n from mat_solicitud sol  ");
			sqlNoMAtSoli.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlNoMAtSoli.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlNoMAtSoli.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlNoMAtSoli.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlNoMAtSoli.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi");
			sqlNoMAtSoli.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per");  
			sqlNoMAtSoli.append("\n left join (mat_matricula mat inner join per_periodo per on per.id = mat.id_per and per.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlNoMAtSoli.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlNoMAtSoli.append("\n where gfa.id_gpf="+id_gpf); 
			sqlNoMAtSoli.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlNoMAtSoli.append("\n and sol.est='A' ");
			sqlNoMAtSoli.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlNoMAtSoli.append("\n and mat.id is null");*/



			//RESERVADOS CON SOLICITUD - ADEMAS APROBADOS (TODAVIA NO SE MATRICULAN )
			//si todavia no aprueba.. no debe aparecer
			/*StringBuilder sqlReservados = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat,mat.id_au_asi,'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservados.append("\n from mat_solicitud sol  ");
			sqlReservados.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservados.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservados.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservados.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservados.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservados.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			sqlReservados.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservados.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservados.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservados.append("\n and sol.est='A' ");
			sqlReservados.append("\n and per.id_anio=" + id_anio);
			sqlReservados.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservados.append("\n and mat.id is null");*/

			
			/*StringBuilder sqlReservadosSS = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant,mat.id id_mat,mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, mat.con_val");   
			sqlReservadosSS.append("\n from mat_reserva res ");
			sqlReservadosSS.append("\n inner join alu_alumno alu on res.id_alu=alu.id ");
			sqlReservadosSS.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSS.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ");
			sqlReservadosSS.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = alu.id"); 
			sqlReservadosSS.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			sqlReservadosSS.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSS.append("\n inner join per_periodo per on per.id = res.id_per "); 
			sqlReservadosSS.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = alu.id");
			sqlReservadosSS.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			sqlReservadosSS.append("\n where gfa.id_gpf="+id_gpf); 
			sqlReservadosSS.append("\n and per.id_anio=" + id_anio);
			sqlReservadosSS.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");
			sqlReservadosSS.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSS.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO
			sqlReservadosSS.append("\n and mat.id is null");
			sqlReservadosSS.append("\n and alu.id not in"); 
			sqlReservadosSS.append("\n (select s.id_alu from mat_solicitud s where s.est='A' and s.id_anio=" + id_anio + " and s.id_suc_or="  + id_suc + " )");*/
			logger.info(sql);
			//logger.info(sqlReservados);
			//logger.info(sqlSolicitud);	
			/*StringBuilder sqlA = new StringBuilder("select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per_sug.id_suc id_suc_sug, suc_act.nom sucursal, cme.nom modalidad, niv_act.nom nivel, niv_sug.id id_niv_sug, gra_sug.nom grado, gra_sug.id id_gra_sug,  "); 
			sqlA.append("CASE \n"); 
			sqlA.append("WHEN mat_ant.id_sit=1 THEN mat_ant.id_gra+1 \n");
			sqlA.append("WHEN mat_ant.id_sit=3 THEN mat_ant.id_gra\n" ); 
			sqlA.append("ELSE ''\n");
			sqlA.append("END \n");
			sqlA.append("gra_sig ");*/
			
			//NUEVO SIN CRONOGRAMA
			String sqlNS="select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, null id_mat_ant, mat_act.id id_mat,mat_act.id_au_asi, 'N' tipo, gfa.id_gpf, fac.id id_fac, fac.canc, per.id_suc id_suc_sug, suc_act.nom sucursal, cme.nom modalidad, niv.nom nivel, niv.id id_niv_sug, gra.nom grado, gra.id id_gra_sug, "; 
			sqlNS += "\n res.id_gra gra_sig ";
			sqlNS += "\n from mat_reserva res ";
			sqlNS += "\n inner join alu_alumno alu on alu.id = res.id_alu ";
			sqlNS += "\n inner join `col_persona` p on p.id=alu.id_per ";
			sqlNS += "\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu ";
			sqlNS += "\n inner join per_periodo per on per.id = res.id_per ";
			sqlNS += "\n inner join ges_sucursal suc on suc.id = per.id_suc ";
			sqlNS += "\n inner join cat_grad gra on gra.id = res.id_gra ";
			sqlNS += "\n inner join cat_nivel niv on gra.id_nvl = niv.id ";
			sqlNS += "\n left  join ( mat_matricula mat_act inner join  per_periodo per_act on per_act.id= mat_act.id_per and per_act.id_anio=" + id_anio + " and per_act.id_tpe=1 and (mat_act.id_sit IS NULL OR mat_act.id_sit<>'5') ) on mat_act.id_alu=alu.id ";  
			sqlNS += "\n left join fac_academico_pago fac on mat_act.id=fac.id_mat and fac.tip='MAT' ";
			sqlNS += "\n left join ges_sucursal suc_act ON per_act.id_suc=suc_act.id "; 
			sqlNS += "\n left join col_aula au_act ON mat_act.id_au_asi=au_act.id "; 
			sqlNS += "\n left join cat_modalidad_estudio cme ON au_act.id_cme=cme.id "; 
			sqlNS += "\n left  join ( mat_matricula mat_ant inner join  per_periodo per_ant on per_ant.id= mat_ant.id_per and per_ant.id_anio=" + id_anio_anterior + " and per_ant.id_tpe=1) on mat_ant.id_alu=alu.id and mat_ant.id_sit!=5  ";
			sqlNS += "\n  where gfa.id_gpf="+id_gpf;
			//sqlNS += "\n and per.id_suc=" + id_suc;
			sqlNS += "\n and per.id_anio=" + id_anio;
			//sqlNS += "\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")";
		//	sqlNS += "\n and (mat_ant.id is null OR mat_ant.id_sit in (4,5) )";
			/**nuevos con solicitud*/
			/*String sqlSolicitudNS = "select distinct alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act, mat_ant.id id_mat_ant, mat.id id_mat, mat.id_au_asi, 'A' tipo, gfa.id_gpf, fac.id id_fac, fac.canc,mat.con_val "
					+ "\n  from mat_solicitud sol " + " inner join mat_matricula m on m.id = sol.id_mat"
					+ "\n  inner join alu_alumno alu on alu.id = m.id_alu"
					+ "\n inner join `col_persona` p on p.id=alu.id_per "
					+ "\n  inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu "
					+ "\n  inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id   "
					+ "\n  inner join per_periodo per_ant on per_ant.id = mat_ant.id_per  "
					+ "\n  inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv  "
					+ "\n  left join ( mat_matricula mat inner join per_periodo per  on mat.id_per= per.id and per.id_anio="
					+ id_anio + " ) on mat.id_alu = alu.id "
					+ "\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' "
					+ "\n  where gfa.id_gpf="+id_gpf
					+ "\n and per_ant.id_anio=" + id_anio_anterior
					+ "\n  and (mat_ant.id_gra!=14 OR (mat_ant.id_gra=14 and mat_ant.id_sit!=1)) "
					+ "\n  and sol.id_suc_des =" + id_suc +  " and sol.id_anio=" + id_anio + " and sol.est='A' ";*/
			/**Reservados nuevos con solicitud**/
			/*StringBuilder sqlReservadosSolicitud = new StringBuilder("select alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom,alu.id_anio_act,null id_mat_ant,null id_mat, null id_au_asi, 'A' tipo, gfa.id_gpf,null id_fac, null canc, mat.con_val");   
			sqlReservadosSolicitud.append("\n from mat_solicitud sol  ");
			sqlReservadosSolicitud.append("\n inner join alu_alumno alu on alu.id = sol.id_alu ");
			sqlReservadosSolicitud.append("\n inner join `col_persona` p on p.id=alu.id_per ");
			sqlReservadosSolicitud.append("\n inner join `alu_gru_fam_alumno` gfa on alu.id=gfa.id_alu and gfa.id_gpf="+id_gpf);
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_matr_vacante` emv ON alu.`id`=emv.`id_alu` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `eva_evaluacion_vac` eva ON emv.`id_eva`=eva.`id` ");
			sqlReservadosSolicitud.append("\n INNER JOIN `per_periodo` per_eva ON eva.`id_per`=per_eva.`id` ");
			sqlReservadosSolicitud.append("\n inner join mat_reserva res on res.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join mat_matricula mat_ant on mat_ant.id_alu = sol.id_alu"); 
			//sqlReservados.append("\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi"); 
			//sqlReservados.append("\n inner join per_periodo  per_ant on per_ant.id = au_ant.id_per"); 
			sqlReservadosSolicitud.append("\n inner join per_periodo per on per.id = res.id_per and per.id_anio=" + id_anio+" AND per.`id_anio`=per_eva.`id_anio`"); 
			//sqlReservadosSolicitud.append("\n left join (mat_matricula mat inner join per_periodo per1 on per1.id = mat.id_per and per1.id_anio=" + id_anio + ") on mat.id_alu = sol.id_alu");
			//sqlReservadosSolicitud.append("\n left join fac_academico_pago fac on mat.id=fac.id_mat and fac.tip='MAT' ");
			//sqlReservadosSolicitud.append("\n where  upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom))  LIKE '%" + alumno.toUpperCase() + "%'"); 
			sqlReservadosSolicitud.append("\n and sol.id_suc_des =" + id_suc+" and sol.id_anio="+id_anio);
			sqlReservadosSolicitud.append("\n and sol.est='A' ");
			//sqlReservados.append("\n and per_ant.id_anio=" + id_anio_anterior);
			sqlReservadosSolicitud.append("\n and NOT EXISTS ( SELECT m.id_alu FROM mat_matricula m INNER JOIN col_aula au ON m.id_au_asi=au.id INNER JOIN per_periodo p ON au.id_per=p.id WHERE m.id_alu = res.id_alu AND p.id_anio="+id_anio+")");*/
			//sqlReservados.append("\n and mat_ant.id_sit=" + EnumSituacionFinal.APROBADO.getValue());//APROBADO

			/*String sqlFinalNS = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant,t.id_mat,t.id_au_asi, t.tipo, t.id_gpf, t.id_fac, t.canc from (\n "
					+ sqlNS.toString();*/
			String sqlFinalNS =  sqlNS.toString();
			//sqlFinalNS = sqlFinalNS + "\n  union \n " + sqlSolicitudNS+"\n union \n"+sqlReservadosSolicitud;
			//sqlFinalNS = sqlFinalNS	+ " ) t\n  WHERE t.id_alu not in (select ss.id_alu from mat_solicitud ss where ss.id_suc_or='"	+ id_suc + "' and ss.id_anio=" + id_anio + " and ss.est='A') ";
			//sqlFinalNS = sqlFinalNS	+ " ) t\n  ";
			String sqlFinal = "select distinct  t.id_alu, t.nro_doc, t.ape_pat, t.ape_mat, t.nom, t.id_anio_act, t.id_mat_ant, t.id_mat, t.id_au_asi, t.tipo, t.id_gpf, t.id_fac, t.canc, t.gra_sig, t.id_suc_sug, t.sucursal, t.modalidad, t.nivel, t.grado, t.id_gra_sug, t.id_niv_sug from (";
			//sqlFinal = sqlFinal + sql  + "\n union \n" + sqlA + "\n union \n"  + sqlSolicitud  + "\n union \n" + sqlNoMAtSoli + "\n  union \n" + sqlReservados + "\n  union \n" + sqlReservadosSS;//AS
			sqlFinal = sqlFinal  + " " + sqlA + " union \n" + sqlFinalNS;//NS
			sqlFinal = sqlFinal + " ) t  order by t.ape_pat,t.ape_mat,t.nom";

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlFinal);

			return list;

		}
		return new ArrayList<Map<String, Object>>();
	}
	
	/** Listar todos los alumnos*/
	public List<Row> listarTodosAlumnos(String apellidosNombres) {		
		String sql = "SELECT alu.id, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, agfa.id_gpf FROM `alu_alumno` alu INNER JOIN col_persona per ON alu.id_per=per.id  INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu"
				+ " WHERE CONCAT(per.ape_pat,' ',per.ape_mat, ' ', per.nom) LIKE '%"+apellidosNombres+"%'";
		//return sqlUtil.query(sql);	
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	/**
	 * Actualizar el anio de actualizacion
	 * @param id_anio
	 * @param id_alu
	 */
	public void actualizarAnioAlumnoActualizacion(Integer id_anio, Integer id_alu){
		String sql = "update alu_alumno set id_anio_act="+id_anio+" WHERE id_per="+id_alu;
		sqlUtil.update(sql);
		
	}
	
	/**
	 * Verificar si existe el usuario en algun alumno
	 * @param usuario
	 * @return
	 */
	public boolean existeUsuario(String usuario) {
		
		String sql = "select * from alu_alumno where usuario ='" + usuario + "'";

		//logger.info(sql);

		List<Map<String,Object>> alumnos = jdbcTemplate.queryForList(sql);
		
		return (alumnos.size()>0);

	}
	
	/**
	 * Actualizar el usuario
	 * @param id_alu
	 * @param usuario
	 */
	public void actualizarUsuarioAlumno(Integer id_alu, String usuario, String pass_google){
		String sql = "update alu_alumno set usuario=?, pass_google=? WHERE id=?";
		sqlUtil.update(sql,new Object[]{ usuario,pass_google,id_alu});		
	}
	
	/**
	 * Actualizar el psw del alumno
	 * @param usuario
	 * @param psw
	 */
	public void actualizarPswAlumno(String usuario, String psw){
		String sql = "update alu_alumno set pass_educando=? WHERE usuario=?";
		sqlUtil.update(sql,new Object[]{ psw,usuario});		
	}
	
	public void actualizarPswAlumnoxId(Integer id_alu, String psw){
		String sql = "update alu_alumno set pass_educando=? WHERE id=?";
		sqlUtil.update(sql,new Object[]{ psw,id_alu});		
	}
	
	public void actualizarIdClasroomAlumno(String id_classRoom, String usuario){
		String sql = "update alu_alumno set id_classRoom=? WHERE usuario=?";
		sqlUtil.update(sql,new Object[]{id_classRoom,usuario});		
	}
	
	/** Listar alumnos segun tipo*/
	public List<Row> listarAlumnosSegunTipo(String tipBusqueda, Integer id_anio, Integer id_gir, Integer id_suc, Integer id_niv) {
		String sql="";
		if(tipBusqueda.equals("L")) {
			sql += "SELECT alu.id, alu.cod, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, agfa.id_gpf ";
			sql += " FROM col_persona per INNER JOIN `alu_alumno` alu ON per.id=alu.id_per";
			sql	+= " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu";
			sql += " ORDER BY per.ape_pat, per.ape_mat, per.nom";
		} else if(tipBusqueda.equals("A")) {
			sql += "SELECT DISTINCT alu.id, alu.cod, CONCAT(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) alumno, agfa.id_gpf \r\n" ;
			sql += " FROM col_persona pers INNER JOIN `alu_alumno` alu ON pers.id=alu.id_per \r\n" ;
			sql += " INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu \r\n" ;
			//sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu \r\n" ;
			sql +=" INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id` \r\n" ; 
			sql +=" INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" ;
			sql += " WHERE per.`id_anio`=" +id_anio;
			if(id_suc!=null && id_suc!=0) {
				sql += " AND per.id_suc="+id_suc;	
			}
			if(id_niv!=null) {
				sql += " AND per.id_niv="+id_niv;	
			}
			sql +=" ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		} else if(tipBusqueda.equals("R")) {
			sql += "SELECT alu.id, alu.cod, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, agfa.id_gpf \r\n" ;
			sql += " FROM col_persona per INNER JOIN `alu_alumno` alu ON per.id=alu.id_per \r\n" ;
			sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu\r\n" ;
			sql +="INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`\r\n" ;
			sql +="INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" ; 
			sql +="WHERE per.`id_anio`=" +id_anio+" and mat.id_sit=5";
			if(id_suc!=null) {
				sql += " AND per.id_suc="+id_suc;	
			}
			if(id_niv!=null) {
				sql += " AND per.id_niv="+id_niv;	
			}
			sql +=" ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		} else if(tipBusqueda.equals("E")) {
			sql += "SELECT alu.id, alu.cod, CONCAT(per.ape_pat,' ', per.ape_mat,' ', per.nom) alumno, agfa.id_gpf \r\n" ;
			sql += " FROM col_persona per INNER JOIN `alu_alumno` alu ON per.id=alu.id_per \r\n" ;
			sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON alu.id=agfa.id_alu\r\n" ;
			sql +="INNER JOIN `mat_matricula` mat ON mat.`id_alu`=alu.`id`\r\n" ;
			sql +="INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" ; 
			sql +="WHERE per.`id_anio` and id_sit='1' AND id_gra='14' ";
			if(id_suc!=null) {
				sql += " AND per.id_suc="+id_suc;	
			}
			sql +=" ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
		}
		
		//return sqlUtil.query(sql);	
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public Row datosAlumnoxCodigo(String cod, Integer id_anio) {
		String sql = "SELECT per.*, alu.id id_alu, alu.`cod`,pro.id id_pro_nac, dep.id id_dep_nac, t.id_fam_emer, alu.`id_idio1`, alu.usuario, alu.pass_educando, alu.`id_idio2`, alu.`email_inst`, agfa.id_gpf\r\n" + 
				"FROM `col_persona` per INNER JOIN `alu_alumno` alu ON per.`id`=alu.`id_per`\r\n" + 
				" INNER JOIN alu_gru_fam_alumno agfa ON alu.id=agfa.id_alu "+
				" LEFT JOIN cat_distrito dist ON per.id_dist_nac=dist.id"+ 
				" LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id"+
				" LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id"+
				" LEFT JOIN mat_matricula mat ON mat.id_alu=alu.id"+
				" LEFT JOIN (SELECT mat.id_fam_emer , mat.id_alu FROM mat_matricula mat"+ 
				" INNER JOIN per_periodo peri ON mat.id_per=peri.id  AND peri.id_anio="+id_anio+") t ON  t.id_alu=alu.id"+
				" WHERE alu.`cod`='"+cod+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
	public Row datosAlumnoxId(Integer id_alu) {
		String sql = "SELECT per.*, alu.`id` id_alu , alu.`cod`\n" + 
				"FROM `col_persona` per INNER JOIN `alu_alumno` alu ON per.`id`=alu.`id_per`\n" + 
				"WHERE alu.`id`="+id_alu;

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
	/** Listar familias*/
	public List<Row> listarFamilias() {
		String sql="SELECT id, nom FROM alu_gru_fam ORDER BY nom ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	/** Listar familias*/
	public List<Row> listarMatriculas(Integer id_alu) {
		String sql="SELECT a.`nom` anio,  SUBSTRING(niv.`nom`,1,1) niv, gra.`abrv` grado, au.`secc`, CONCAT(niv.`nom`,' ', gra.`nom`) salon ,gir.`nom` servicio, \r\n" + 
				"IF( per.`fec_fin`>=CURDATE(),'Vigente','Inactivo') estado \r\n" + 
				"FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\r\n" + 
				"INNER JOIN `per_periodo` per ON mat.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id` AND au.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				"INNER JOIN `col_anio` a ON per.`id_anio`=a.`id`\r\n" + 
				"INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" + 
				"INNER JOIN `ges_giro_negocio` gir ON ser.`id_gir`=gir.`id`\r\n" + 
				"WHERE mat.`id_alu`="+id_alu;

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql));
	}
	
	public void desactivarCuentaGoogle(Integer id_alu){
		String sql = "update alu_alumno set est_cta_google='I' where id=?";
		sqlUtil.update(sql,new Object[]{id_alu});		
	}
	
	public void activarCuentaGoogle(Integer id_alu){
		String sql = "update alu_alumno set est_cta_google='A' where id=?";
		sqlUtil.update(sql,new Object[]{id_alu});		
	}
	
	public List<Row> listaPagosxAlumno(Integer id_anio, Integer id_per){
		String sql ="SELECT DISTINCT * FROM (SELECT DISTINCT a.id, p.nro_rec, p.fec_pago, p.id_mat , p.canc, p.fec_venc, p.`monto_total` , cic.nom, p.desc_beca, mov.id id_fmo, p.mens,\n" + 
				"				 CASE \n" + 
				"				 WHEN p.tip='MAT' AND gir.id=1  THEN CONCAT('MATRICULA COLEGIO',' ', an.nom) \n" + 
				"				 WHEN p.tip='MAT' AND gir.id=2  THEN CONCAT('MATRICULA ACADEMIA',' ', an.nom) \n" + 
				"				 WHEN p.tip='MAT' AND gir.id=3  THEN CONCAT('MATRICULA VACACIONES ÚTILES',' ', an.nom) \n" + 
				"				 WHEN p.tip='MEN' THEN CONCAT('MENSUALIDAD',' - ',mes.nom)\n" + 
				"				 WHEN p.tip='ING' THEN 'CUOTA DE INGRESO'\n" + 
				"				 ELSE '' END AS 'concepto',\n" + 
				"				 CASE \n" + 
				"				 WHEN emer.id IS NULL THEN p.monto \n" + 
				"				 WHEN emer.id IS NOT NULL THEN emer.monto \n" + 
				"				 ELSE '' END AS 'monto'\n" + 
				"				 FROM fac_academico_pago p\n" + 
				"				 INNER JOIN mat_matricula m ON m.id= p.id_mat\n" + 
				"				 INNER JOIN per_periodo per ON per.id= m.id_per\n" + 
				"				 INNER JOIN `col_ciclo` cic ON per.id=cic.id_per AND m.id_cic=cic.id\n" + 
				"				 INNER JOIN alu_alumno a ON a.id = m.id_alu\n" + 
				"				 INNER JOIN `ges_servicio` srv ON per.id_srv=srv.id\n" + 
				"				 INNER JOIN `ges_giro_negocio` gir ON srv.id_gir=gir.id\n" + 
				"				 INNER JOIN col_anio an ON per.id_anio=an.id\n" + 
				"				 LEFT JOIN `cat_mes` mes ON p.mens=mes.id\n" + 
				"				 LEFT JOIN `mat_tarifas_emergencia` emer ON emer.id_per=per.id AND p.mens=emer.mes\n" + 
				"				 LEFT JOIN fac_movimiento mov ON p.nro_rec=mov.nro_rec \n" + 
				"				 WHERE per.id_anio="+id_anio+" AND a.id_per="+id_per+" AND p.canc=1 AND p.est='A' AND p.monto<>'0.00'\n" + 
				"UNION ALL\n" + 
				"SELECT DISTINCT alu.id, fmo.nro_rec, fmo.fec fec_pago, res.id id_mat , 1 canc, NULL fec_venc, fmo.`monto_total` , cic.nom, NULL desc_beca, fmo.id id_fmo, NULL mens,\n" + 
				"				 'RESERVA' concepto, fmo.monto\n" + 
				"				FROM `fac_movimiento` fmo INNER JOIN `fac_reserva_cuota` rcuo ON rcuo.nro_recibo=fmo.nro_rec \n" + 
				"				INNER JOIN `mat_reserva` res ON rcuo.`id_res`=res.`id` \n" + 
				"				INNER JOIN `alu_alumno` alu ON res.`id_alu`=alu.`id` \n" + 
				"				INNER JOIN `per_periodo` per ON res.`id_per`=per.`id` \n" + 
				"				INNER JOIN `col_ciclo` cic ON per.id=cic.id_per\n" + 
				"				INNER JOIN `col_anio` a ON per.`id_anio`=a.`id`\n" + 
				"				INNER JOIN col_persona pera ON alu.id_per=pera.id \n" + 
				"				WHERE per.id_anio="+id_anio+" AND alu.id_per="+id_per+") t\n" + 
				"ORDER BY t.mens";
		
		return sqlUtil.query(sql);
	}
	
	
	/** Listar familias*/
	public List<Row> listarMatriculadosxFamilia(Integer id_gpf, Integer id_anio) {
		String sql="SELECT DISTINCT mat.`id` id_mat, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) alumno, niv.nom nivel, gra.nom grado, cme.nom modalidad, mat.id_fam \n" + 
				"FROM `mat_matricula` mat \n" + 
				"INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n" + 
				"INNER JOIN `col_persona` per ON alu.id_per=per.`id`\n" + 
				"INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" +
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\n" + 
				"INNER JOIN `cat_modalidad_estudio` cme ON au.`id_cme`=cme.`id`\n" + 
				"INNER JOIN per_periodo peri ON au.id_per=peri.`id`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON agfa.`id_alu`=alu.`id`\n" + 
				"INNER JOIN `alu_gru_fam` agf ON agfa.`id_gpf`=agf.`id`\n" + 
				"WHERE peri.id_anio=? AND agf.`id`=? AND peri.id_tpe=1 and (mat.id_sit is null or mat.id_sit<>'5')";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql,new Object[]{id_anio, id_gpf}));
	}
	
	public List<Row> listarDeudasxFamilia(Integer id_gpf) {
		String sql="SELECT fac.* \n" + 
				"FROM `mat_matricula` mat INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`\n" + 
				"INNER JOIN `alu_gru_fam_alumno` agfa ON alu.`id`=agfa.`id_alu`\n" + 
				"INNER JOIN `fac_academico_pago` fac ON mat.`id`=fac.`id_mat`\n" + 
				"WHERE fac.`canc`='0' AND fac.`est`='A' AND agfa.`id_gpf`=? AND (CURDATE()>=fec_venc OR fec_venc IS NULL) AND (mat.id_sit NOT IN(5) or mat.id_sit IS NULL) ";

		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql,new Object[]{id_gpf}));
	}
	
	public void actualizarPswAlumnoGoogle(String usuario, String psw){
		String sql = "update alu_alumno set pass_google=? WHERE usuario=?";
		sqlUtil.update(sql,new Object[]{ psw,usuario});		
	}
	
	
}
