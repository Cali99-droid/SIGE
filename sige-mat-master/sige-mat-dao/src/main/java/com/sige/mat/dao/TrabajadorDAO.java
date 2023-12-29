package com.sige.mat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TrabajadorDAOImpl;
import com.tesla.colegio.model.AulaEspecial;
import com.tesla.colegio.model.Trabajador;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad trabajador.
 * @author MV
 *
 */
@Repository
public class TrabajadorDAO extends TrabajadorDAOImpl{
	final static Logger logger = Logger.getLogger(TrabajadorDAO.class);

    @Autowired
    private SQLUtil sqlUtil;

    @Autowired
    private AulaEspecialDAO aulaEspecialDAO;

    
	//Listar profesores por matricula del alumno
	public List<Row> listarProfesoresPorMatricula(Integer id_mat){
		Param param = new Param();
		param.put("id_mat", id_mat);
		AulaEspecial aulaEspecial = aulaEspecialDAO.getByParams(param);
		boolean flag_aulaEspecial = false;
		if(aulaEspecial!=null && aulaEspecial.getEst().equals("A")){
			flag_aulaEspecial  = true;
		}
		
		if(!flag_aulaEspecial){
			String sql = "select "
					+ " cur.id cur_id, cur.nom curso, cca.id_au au_id, "
					+ " tra.id tra_id, concat(tra.ape_pat,' ',tra.ape_mat,', ',tra.nom) trabajador,"
					+ " tra.nro_doc, tra.cel, tra.corr, nce.id exonerado  "
					+ " from col_curso_aula cca"
					+ " inner join mat_matricula mat on mat.id_au_asi = cca.id_au"
					+ " inner join aeedu_asistencia.ges_trabajador tra on tra.id= cca.id_tra"
					+ " inner join col_curso_anio cua on cua.id = cca.id_cua"
					+ " inner join cat_curso cur on cur.id = cua.id_cur"
					+ " left join not_curso_exoneracion nce on nce.id_mat = mat.id and nce.id_cur = cur.id"
					+ " where mat.id=? and tra.est='A' "
					+ " order by curso";
		
			return sqlUtil.query(sql, new Integer[]{id_mat});
		}else{
			String sql = "select cur.id cur_id, cur.nom curso, cca.id_au au_id,  tra.id tra_id, concat(tra.ape_pat,' ',tra.ape_mat,', ',tra.nom) trabajador, tra.nro_doc, tra.cel, tra.corr, null exonerado  "
					+ " from col_curso_aula cca "
					//+ " inner join col_aula_especial mat on mat.id_au = cca.id_au "
					+ " inner join aeedu_asistencia.ges_trabajador tra on tra.id= cca.id_tra "
					+ " inner join col_curso_anio cua on cua.id = cca.id_cua "
					+ " inner join cat_curso cur on cur.id = cua.id_cur "
					+ " where cca.id_au=173 and tra.est='A' order by curso";
			return sqlUtil.query(sql);

		}
		

	}
	
    /**
     * Ver foto
     * @param idAlu
     * @return byte[]
     */	
	public Trabajador getPhoto(Integer idAlu) {
		String sql = "select genero, fot from aeedu_asistencia.ges_trabajador where id=" + idAlu;
		
		Trabajador trabajador = jdbcTemplate.query(sql, new ResultSetExtractor<Trabajador>() {

			@Override
			public Trabajador extractData(ResultSet rs) throws SQLException,DataAccessException {
				Trabajador trabajador = new Trabajador();
				if (rs.next()) {
					LobHandler lobHandler1 = new DefaultLobHandler();
	                byte[] requestData = lobHandler1.getBlobAsBytes(rs,"fot");
	                trabajador.setFot(requestData);
	                trabajador.setGenero(rs.getString("genero"));
	                return trabajador;
				}
				
				return trabajador;

			}
			
		});
		
		return trabajador;
	}
	
	public List<Row> listarprofesoresxNivel(int id_anio, int id_niv) {

		String sql ="SELECT DISTINCT tra.`id`, CONCAT(tra.`ape_pat`,' ', tra.`ape_mat`, ' ', tra.`nom`) value"
				+ " FROM `aeedu_asistencia`.`ges_trabajador` tra "
				+ " INNER JOIN col_curso_aula cca ON tra.`id`=cca.id_tra"
				+ " INNER JOIN `col_curso_anio` cua ON cca.id_cua=cua.`id`"
				+ " INNER JOIN `per_periodo` per ON cua.`id_per`=per.`id`"
				+ " WHERE tra.est='A' and per.`id_niv`=? AND per.`id_anio`=? "
				+ " ORDER BY tra.`ape_pat`, tra.`ape_mat`, tra.`nom`";
		
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_niv,id_anio});

	}
	
	public List<Row> listartrabajadores(String apellidosNombres) {

		String sql ="SELECT DISTINCT tra.`id`, tra.nro_doc, CONCAT(tra.`ape_pat`,' ', tra.`ape_mat`, ' ', tra.`nom`) label, pue.nom cargo"
				+ " FROM `aeedu_asistencia`.`ges_trabajador` tra "
				+ " INNER JOIN `aeedu_asistencia`.asi_contrato_trabajador con ON tra.id=con.id_tra "
				+ " INNER JOIN `aeedu_asistencia`.`ges_puesto_trabajador` pue ON con.id_pue=pue.id"
				+ " WHERE tra.est='A' AND ( upper(CONCAT(tra.ape_pat,' ',tra.ape_mat, ' ', tra.nom)) LIKE '%" + apellidosNombres.toUpperCase() + "%'  ) "
				+ " ORDER BY tra.`ape_pat`, tra.`ape_mat`, tra.`nom`";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarTodosTrabajadores() {

		String sql ="SELECT DISTINCT tra.`id`, tra.cod, CONCAT(per.`ape_pat`,' ', per.`ape_mat`, ' ', per.`nom`) label, CONCAT(per.`ape_pat`,' ', per.`ape_mat`, ' ', per.`nom`) value\n" + 
				"				 FROM ges_trabajador tra \n" + 
				"				 INNER JOIN `col_persona` per ON tra.`id_per`=per.`id` \n" + 
				"				 WHERE tra.est='A' \n" + 
				"				 ORDER BY per.`ape_pat`, per.`ape_mat`, per.`nom`";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarTodosTrabajadoresDocTut() {

		String sql ="SELECT DISTINCT tra.`id`, tra.cod, CONCAT(per.`ape_pat`,' ', per.`ape_mat`, ' ', per.`nom`) label, CONCAT(per.`ape_pat`,' ', per.`ape_mat`, ' ', per.`nom`) value\n" + 
				"				 FROM ges_trabajador tra \n" + 
				"				 INNER JOIN `col_persona` per ON tra.`id_per`=per.`id` \n" + 
				"INNER JOIN `seg_usuario` usr ON tra.`id`=usr.id_tra \r\n" + 
				"INNER JOIN `seg_usuario_rol` urol ON usr.id=urol.`id_usr`\r\n" + 
				"INNER JOIN seg_rol rol ON urol.`id_rol`=rol.id\r\n" + 
				"INNER JOIN seg_usuario_nivel univ ON univ.`id_usr`=usr.id\r\n" + 
				"INNER JOIN `rhh_contrato_trabajador` con ON tra.`id`=con.`id_tra`\r\n" + 
				"WHERE rol.`id`=6 AND (CURDATE()>=con.`fec_ini` AND (CURDATE()<=con.`fec_fin` OR con.con_indf=1)) AND tra.est='A' "+
				"				 ORDER BY per.`ape_pat`, per.`ape_mat`, per.`nom`";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarAuxiliaresConVigente(Integer id_niv) {

		String sql ="SELECT DISTINCT tra.`id`, tra.cod, CONCAT(per.`ape_pat`,' ', per.`ape_mat`,' ', per.`nom`) value \r\n" + 
				"FROM `ges_trabajador` tra INNER JOIN col_persona per ON tra.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `seg_usuario` usr ON tra.`id`=usr.id_tra \r\n" + 
				"INNER JOIN `seg_usuario_rol` urol ON usr.id=urol.`id_usr`\r\n" + 
				"INNER JOIN seg_rol rol ON urol.`id_rol`=rol.id\r\n" + 
				"INNER JOIN seg_usuario_nivel univ ON univ.`id_usr`=usr.id\r\n" + 
				"INNER JOIN `rhh_contrato_trabajador` con ON tra.`id`=con.`id_tra`\r\n" + 
				"WHERE rol.`id`=5 AND (CURDATE()>=con.`fec_ini` AND (CURDATE()<=con.`fec_fin` OR con.con_indf=1)) AND univ.id_niv="+id_niv;
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarGirosNegocioxTrabajadorCoordinador(Integer id_tra, Integer id_anio) {

		String sql ="SELECT gir.id, gir.nom value \n" + 
				"FROM `col_nivel_coordinador` coo INNER JOIN ges_giro_negocio gir ON coo.`id_gir`=gir.id\n" + 
				"WHERE coo.`id_tra`=? AND coo.`id_anio`=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listarGirosNegocioxAuxiliar(Integer id_tra, Integer id_anio) {

		String sql ="SELECT gir.id, gir.nom value \n" + 
				"FROM `col_aula` au INNER JOIN col_aula_detalle aud ON au.id=aud.id_au "+
				" INNER JOIN col_ciclo cic ON au.id_cic=cic.id "+
				" INNER JOIN per_periodo per ON cic.id_per=per.id"+
				" INNER JOIN ges_servicio srv on per.id_srv=srv.id"+
				" INNER JOIN ges_giro_negocio gir ON srv.`id_gir`=gir.id\n" + 
				"WHERE aud.`id_aux`=? AND per.`id_anio`=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listarGirosNegocioxCoordinadorArea(Integer id_tra, Integer id_anio) {

		String sql ="SELECT DISTINCT gir.id, gir.nom value \n" + 
				"FROM `col_area_coordinador` coo INNER JOIN ges_giro_negocio gir ON coo.`id_gir`=gir.id\n" + 
				"WHERE coo.`id_tra`=? AND coo.`id_anio`=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listarGirosNegocioxTrabajadorDocente(Integer id_tra, Integer id_anio) {

		String sql ="SELECT DISTINCT gir.id, gir.`nom` value\r\n" + 
				"FROM `col_curso_aula` cua INNER JOIN `col_aula` au ON cua.`id_au`=au.`id`\r\n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" + 
				"INNER JOIN ges_giro_negocio gir ON ser.id_gir=gir.id "+
				"WHERE cua.`id_tra`=? AND per.id_anio=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listarGirosNegocioxTutor(Integer id_tut, Integer id_anio) {

		String sql ="SELECT gir.`id`, gir.`nom` VALUE \n" + 
				"FROM col_aula au INNER JOIN `col_aula_detalle` aud ON au.`id`=aud.`id_au`\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" + 
				"INNER JOIN `ges_giro_negocio` gir ON srv.`id_gir`=gir.`id`\n" + 
				"WHERE aud.`id_tut`=? AND per.`id_anio`=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tut, id_anio});

	}
	
	public List<Row> listarNivelesxTrabajadorCoordinador(Integer id_tra, Integer id_anio, Integer id_gir) {

		String sql ="SELECT niv.id, niv.nom value \n" + 
				"FROM `col_nivel_coordinador` coo INNER JOIN cat_nivel niv ON coo.`id_niv`=niv.id\n" + 
				"WHERE coo.`id_tra`=? AND coo.`id_anio`=? AND coo.id_gir=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio, id_gir});

	}
	
	public List<Row> listarGirosxTrabajadorCoordinador(Integer id_tra, Integer id_anio) {

		String sql ="SELECT gir.id, gir.nom value \n" + 
				"FROM `col_nivel_coordinador` coo INNER JOIN ges_giro gir ON coo.`id_gir`=gir.id\n" + 
				"WHERE coo.`id_tra`=? AND coo.`id_anio`=? AND coo.id_gir=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_tra, id_anio});

	}
	
	public List<Row> listarNivelesxAuxiliar(Integer id_aux) {

		String sql ="SELECT niv.id, niv.nom value \n" + 
				"FROM col_aula au INNER JOIN col_aula_detalle aud ON au.id=aud.id_au "+
				"INNER JOIN cat_grad gra ON au.id_grad=gra.id "+
				"INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id  "+
				" WHERE aud.id_aux=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_aux});

	}
	
	public List<Row> listarNivelesxTrabajadorDocente(Integer id_tra, Integer id_anio, Integer id_gir) {

		String sql ="SELECT DISTINCT niv.id, niv.`nom` value\r\n" + 
				"FROM `col_curso_aula` cua INNER JOIN `col_aula` au ON cua.`id_au`=au.`id`\r\n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\r\n" + 
				"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\r\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\r\n" + 
				"INNER JOIN `ges_servicio` ser ON per.`id_srv`=ser.`id`\r\n" + 
				"WHERE ser.`id_gir`=? AND cua.`id_tra`=? AND per.id_anio=? ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_gir, id_tra, id_anio});

	}
	
	public List<Row> listarCoordinadorNivelxAnio( Integer id_anio) {

		String sql ="SELECT cniv.id,  CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) trabajador, gir.`nom` giro, niv.`nom` nivel\n" + 
				"FROM col_nivel_coordinador cniv INNER JOIN `ges_trabajador` tra ON cniv.`id_tra`=tra.`id`\n" + 
				"INNER JOIN `ges_giro_negocio` gir ON cniv.`id_gir`=gir.`id`\n" + 
				"INNER JOIN `cat_nivel` niv ON cniv.`id_niv`=niv.`id`\n" + 
				"INNER JOIN `col_persona` pers ON tra.`id_per`=pers.`id`\n" + 
				"WHERE cniv.`id_anio`=?";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio});

	}
	
	public List<Row> listarCoordinadorNivelUsuarioxAnio( Integer id_anio) {

		String sql ="SELECT tra.`id`, CONCAT(pera.`ape_pat`,' ', pera.`ape_pat`,' ', pera.`nom`) value \r\n" + 
				"FROM `ges_trabajador` tra INNER JOIN `seg_usuario` usr ON tra.`id`=usr.`id_tra`\r\n" + 
				"INNER JOIN `col_persona` pera ON tra.`id_per`=pera.`id`\r\n" + 
				"INNER JOIN `rhh_contrato_trabajador` con ON tra.`id`=con.`id_tra`\r\n" + 
				"INNER JOIN `seg_usuario_rol` urol ON usr.`id`=urol.`id_usr`\r\n" + 
				"WHERE urol.`id_rol`=12 AND ((con.id_anio_con=? AND CURDATE() BETWEEN con.`fec_ini` AND con.`fec_fin`) OR (con.con_indf=1));";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio});

	}
	
	public List<Row> listarAdministradorSedeUsuarioxAnio( Integer id_anio) {

		String sql ="SELECT tra.`id`, CONCAT(pera.`ape_pat`,' ', pera.`ape_pat`,' ', pera.`nom`) value \r\n" + 
				"FROM `ges_trabajador` tra INNER JOIN `seg_usuario` usr ON tra.`id`=usr.`id_tra`\r\n" + 
				"INNER JOIN `col_persona` pera ON tra.`id_per`=pera.`id`\r\n" + 
				"INNER JOIN `rhh_contrato_trabajador` con ON tra.`id`=con.`id_tra`\r\n" + 
				"INNER JOIN `seg_usuario_rol` urol ON usr.`id`=urol.`id_usr`\r\n" + 
				"WHERE urol.`id_rol`=20 AND ((con.id_anio_con=? AND CURDATE() BETWEEN con.`fec_ini` AND con.`fec_fin`) OR (con.con_indf=1));";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio});

	}
	
	public List<Row> listarProfesoresTutores(Integer id_anio) {

		String sql ="SELECT DISTINCT id,  CONCAT(t.`ape_pat`,' ', t.`ape_mat`,' ', t.`nom`) trabajador FROM "
				+ " (SELECT tra.id,tra.`ape_pat`, tra.`ape_mat`, tra.`nom`"
				+ " FROM `aeedu_asistencia`.`ges_trabajador` tra INNER JOIN `col_curso_aula` cca ON tra.`id`=cca.`id_tra`"
				+ " INNER JOIN `col_aula` au ON cca.`id_au`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`="+id_anio
				+ " UNION ALL"
				+ " SELECT tra.id, tra.`ape_pat`, tra.`ape_mat`, tra.`nom`"
				+ " FROM `aeedu_asistencia`.`ges_trabajador` tra INNER JOIN `col_tutor_aula` cta ON tra.`id`=cta.`id_tra`"
				+ " INNER JOIN `col_aula` au ON cta.`id_au`=au.`id`"
				+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
				+ " WHERE per.`id_anio`="+id_anio+") t"
				+ " ORDER BY t.ape_pat, t.ape_mat, t.nom";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public Row obtenerUltimoCodigodeTrabajador(){
		
		String sql = "SELECT MAX(cod) codigo FROM `ges_trabajador`";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);
	}
	
	public Row obtenerDatosTrabajador(String cod){
		
		String sql = "SELECT per.*, tra.id id_tra, tra.email_inst, tra.`cod`, tra.`hijos`, tra.`num_hij`, tra.`id_gin`, tra.carrera, dist.id id_dist, pro.id id_pro, dep.id id_dep, concat(per.ape_pat,' ', per.ape_mat,' ', per.nom) trabajador  \n" + 
				"FROM `ges_trabajador` tra INNER JOIN `col_persona` per ON tra.`id_per`=per.`id`\n" + 
				"LEFT JOIN cat_distrito dist ON per.id_dist_viv=dist.id\n" + 
				"LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id\n" + 
				"LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id\n" + 
				"WHERE tra.`cod`='"+cod+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		return SQLFrmkUtil.listToRows(list).get(0);
	}
	
	public Row obtenerDatosTrabajadorxDNI(String dni){
		
		String sql = "SELECT per.*, dist.id id_dist, pro.id id_pro, dep.id id_dep, concat(per.ape_pat,' ', per.ape_mat,' ', per.nom) trabajador  \n" + 
				"FROM  `col_persona` per \n" + 
				"LEFT JOIN cat_distrito dist ON per.id_dist_viv=dist.id\n" + 
				"LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id\n" + 
				"LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id\n" + 
				"WHERE per.`nro_doc`='"+dni+"'";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		
		if(list.size()>0) {
			return SQLFrmkUtil.listToRows(list).get(0); 
		} else {
			return null;
		}
		
	}
	
	public List<Row> listarRegimenLaboral() {

		String sql ="SELECT id, nom value FROM cat_regimen_laboral ORDER BY id";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarModalidadTrabajo() {

		String sql ="SELECT id, nom value FROM cat_modalidad_trabajo ORDER BY id";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarCategoriaOcupacional() {

		String sql ="SELECT id, nom value FROM cat_categoria_ocupacional ORDER BY id";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarPuesto() {

		String sql ="SELECT id, nom value FROM ges_puesto_trabajador ORDER BY nom";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarPeriodoPrueba() {

		String sql ="SELECT id, nom value FROM cat_periodo_prueba ORDER BY nom";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarLineaCarrera() {

		String sql ="SELECT id, nom value FROM cat_linea_carrera ORDER BY nom";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarDenominacion(Integer id_anio, Integer id_lcarr) {

		String sql ="SELECT den.id, den.nom value FROM cat_denominacion den INNER JOIN rhh_remuneracion_cat rem ON den.id=rem.id_cden WHERE rem.id_anio=? AND rem.id_lcarr=? ORDER BY id";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio, id_lcarr});

	}
	
	public List<Row> listarRemuneracion(Integer id_lcarr, Integer id_cden, Integer id_cocu, Integer id_anio) {

		String sql ="SELECT id, rem value FROM rhh_remuneracion_cat rem WHERE rem.id_anio=? AND rem.id_lcarr=? AND rem.id_cden=? AND rem.id_cocu=? ORDER BY rem";
		logger.info(sql);
		return sqlUtil.query(sql,  new Object[] {id_anio, id_lcarr, id_cden, id_cocu});

	}
	
	public List<Row> listarFrecuenciaPago() {

		String sql ="SELECT id, nom value FROM cat_tip_frec_pago ORDER BY nom";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarAnio() {

		String sql ="SELECT id, nom value FROM col_anio ORDER BY nom desc";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public List<Row> listarGiroNegocio(Integer id_emp) {

		String sql ="SELECT id, nom value FROM ges_giro_negocio where id_emp=? ORDER BY nom desc";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_emp});

	}
	
	public List<Row> listarHijosTrabajadores(Integer id_anio, Integer id_gir, String tip_con, Integer id_suc) {

		String sql ="SELECT DISTINCT t.trabajador, t.nivel, t.grado, t.secc, t.sucursal, t.alumno,t.fec_mat, t.nro_doc, t.monto, t.con_indf FROM (SELECT CONCAT(pert.ape_pat,' ', pert.ape_mat,' ', pert.nom) trabajador , niv.nom nivel, gra.nom grado, mat.fecha fec_mat, au.secc, suc.nom sucursal, CONCAT(pera.ape_pat,' ', pera.ape_mat,' ', pera.nom) alumno, pera.`nro_doc`, men.monto, con.id id_con , con.fec_ini, con.fec_fin, con.id_anio_con, con.con_indf \n" ;
				sql += "FROM `ges_trabajador` tra \n" ; 
				sql += "INNER JOIN `col_persona` pert ON tra.`id_per`=pert.id\n" ; 
				sql += "INNER JOIN `alu_familiar` fam ON pert.id=fam.id_per\n" ; 
				sql += "INNER JOIN `alu_gru_fam_familiar` agff ON fam.id=agff.id_fam\n" ; 
				sql += "INNER JOIN `alu_gru_fam` agf ON agff.id_gpf=agf.id\n" ; 
				sql += "INNER JOIN `alu_gru_fam_alumno` agfa ON agfa.id_gpf=agf.id\n" ; 
				sql += "INNER JOIN `alu_alumno` alu ON agfa.id_alu=alu.id\n" ; 
				sql += "INNER JOIN col_persona pera ON alu.id_per=pera.id\n" ; 
				sql += "INNER JOIN `mat_matricula` mat ON alu.id=mat.id_alu\n" ; 
				sql += "INNER JOIN col_aula au ON mat.id_au_asi=au.id\n" ; 
				sql += "INNER JOIN col_turno_aula cta ON au.id=cta.id_au\n" ; 
				sql += "INNER JOIN `cat_grad` gra ON au.id_grad=gra.id\n" ; 
				sql += "INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id\n" ; 
				sql += "INNER JOIN per_periodo per ON au.id_per=per.id\n" ; 
				sql += "INNER JOIN ges_sucursal suc ON per.id_suc=suc.id\n" ; 
				sql += "INNER JOIN ges_servicio srv ON per.id_srv=srv.id\n" ; 
				 
				sql += "INNER JOIN `mat_conf_mensualidad` men ON men.id_per=per.id and cta.id_cit=men.id_cct and men.id_cme=au.id_cme \n" ; 
				sql += "LEFT JOIN `rhh_contrato_trabajador` con ON tra.id=con.`id_tra`\n" ;
				sql += "WHERE per.id_anio=? AND srv.id_gir=? AND fam.id_par IN (1,2) \n" ; 
				/*if(tip_con!=null) {
					if(tip_con.equals("1")) {
						sql += "AND con.`id_anio_con`="+id_anio+" AND CURDATE() BETWEEN con.fec_fin  AND con.fec_ini\n" ; 
					} else if (tip_con.equals("0")) {
						sql += "AND con.`id_anio_con`<>"+id_anio+" AND CURDATE()>con.fec_fin\n" ; 
					} 
				}*/
				if(id_suc!=null) {
					sql +=" AND per.id_suc="+id_suc;
				}
				
				sql += " ORDER BY pert.ape_pat, pert.ape_mat, pert.nom) t ";
		
				if(tip_con!=null) {
					if(tip_con.equals("1")) {
						sql += "WHERE ((CURDATE() BETWEEN t.fec_ini  AND t.fec_fin) OR t.con_indf=1)\n" ;  //  t.`id_anio_con`="+id_anio+"
					} else if (tip_con.equals("0")) {
						sql += "WHERE ( t.id_con IS NULL OR  (t.`id_anio_con`<>"+id_anio+" AND CURDATE()>t.fec_fin))\n" ; 
					} 
				}
				
				sql += " ORDER BY t.trabajador ";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] {id_anio, id_gir});

	}
	
	public List<Row> listarContratosxAnio(Integer id_anio, String vig, Integer id_gir) {

		String sql ="SELECT  pers.`nro_doc`,	CONCAT(pers.`ape_pat`,' ', pers.`ape_mat`,' ', pers.`nom`) trabajador, CONCAT(con.`num_con`,' - ', a.`nom`) contrato, con.`fec_ini`, con.`fec_fin`, den.`nom` denominacion, pues.`nom` puesto, rem.`rem`, gir.nom giro \n" ; 
				sql += "FROM `ges_trabajador` tra INNER JOIN `col_persona` pers ON tra.`id_per`=pers.`id`\n";
				sql += "INNER JOIN `rhh_contrato_trabajador` con ON tra.id=con.`id_tra`\n" ; 
				sql +="INNER JOIN `cat_denominacion` den ON con.`id_den`=den.`id`\n" ;
				sql +="INNER JOIN `cat_linea_carrera` lin ON con.`id_lin_carr`=lin.`id`\n" ;
				sql += "INNER JOIN `ges_puesto_trabajador` pues ON con.`id_pue`=pues.`id`\n" ;
				sql += "INNER JOIN `col_anio` a ON con.`id_anio_con`=a.`id`\n" ;
				sql += "INNER JOIN `ges_giro_negocio` gir ON con.`id_gir`=gir.`id`\n" ; 
				sql += "INNER JOIN `rhh_remuneracion_cat` rem ON con.`id_rem_cat`=rem.`id`\n" ;
				//sql += "INNER JOIN `ges_giro` gir ON con.`id_gir`=gir.`id`\n" ;
				//sql += "WHERE con.`id_anio_con`=?   \n";
				sql += "WHERE 1=1 \n";
				if(vig!=null) {
					if(vig.equals("1")) {
						sql += "AND ((CURDATE() BETWEEN con.fec_ini  AND con.fec_fin) OR con.con_indf=1 )\n" ; 
					} else if(vig.equals("0")) {
						sql += "AND CURDATE()>=con.fec_fin \n" ; 
					}
				} else {
					sql += "AND CURDATE()>=con.fec_fin \n" ; 
				}
				if(id_gir!=null) {
					sql += "AND gir.id="+id_gir ; 
				}
				sql += " ORDER BY con.`num_con`";
		logger.info(sql);
		///return sqlUtil.query(sql, new Object[] {id_anio});
		return sqlUtil.query(sql);

	}
}
