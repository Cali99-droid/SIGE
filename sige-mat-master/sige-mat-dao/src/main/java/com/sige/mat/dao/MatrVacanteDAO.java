package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.MatrVacanteDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad matr_vacante.
 * @author MV
 *
 */
@Repository
public class MatrVacanteDAO extends MatrVacanteDAOImpl{
	
	@Autowired
    private SQLUtil sqlUtil;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	public List<Map<String,Object>> EvaluacionVacList(String niv) {
		String q_aux="";
		if (niv!= null){
		 q_aux="and ser.nom='"+niv+"'";}
		String sql = "select e.id, e.id_per,e.des,e.precio,ser.nom, suc.nom local from eva_evaluacion_vac e, per_periodo per,"
				+ " ges_servicio ser, ges_sucursal suc where e.id_per=per.id and per.id_srv=ser.id and ser.id_suc=suc.id"
				+ " AND CURDATE()<=e.`fec_fin` and e.est='A'"+q_aux;
		List<Map<String,Object>> evaList = jdbcTemplate.queryForList(sql);	

		return evaList;
	}
	
	public List<Map<String,Object>> EvaluacionVacLista(Integer id_per) {
		String q_aux="";
		if (id_per!= null){
		 q_aux="AND e.`id_per`="+id_per+"";}
		String sql = "SELECT e.id, e.id_per,e.des,e.precio,ser.nom, suc.nom LOCAL FROM eva_evaluacion_vac e, per_periodo per,"
				+ " ges_servicio ser, ges_sucursal suc WHERE e.id_per=per.id AND per.id_srv=ser.id AND ser.id_suc=suc.id"
				+ " AND e.est='A'"+q_aux;
		List<Map<String,Object>> evaLista = jdbcTemplate.queryForList(sql);	

		return evaLista;
	}
	
	public List<Map<String,Object>> Vacante(Integer id_per) {
		String q_aux="";
		if (id_per!= null){
		 q_aux="AND e.`id_per`="+id_per+"";}
		String sql = "SELECT e.id, e.id_per,e.des,e.precio,ser.nom, suc.nom LOCAL FROM eva_evaluacion_vac e, per_periodo per,"
				+ " ges_servicio ser, ges_sucursal suc WHERE e.id_per=per.id AND per.id_srv=ser.id AND ser.id_suc=suc.id"
				+ " AND e.est='A'"+q_aux;
		List<Map<String,Object>> evaLista = jdbcTemplate.queryForList(sql);	
		return evaLista;
	}
	
	public List<Map<String,Object>> Grados_Vacante(Integer id_eva, Integer id_niv) {

		String sql = "SELECT g.nom, g.id,v.*"
				+ " FROM `cat_grad` g LEFT JOIN `eva_vacante` v ON g.id=v.`id_grad` AND( v.`id_eva`="+id_eva+") "
				+ " WHERE g.`id_nvl`="+id_niv;
		List<Map<String,Object>> Grados = jdbcTemplate.queryForList(sql);	

		return Grados;
	}
	
	public List<Map<String,Object>> ListaVacantes(int id_per, int id_eva, int id_grad) {

		String sql = "select post from eva_vacante vac, eva_evaluacion_vac e "
				+ " where vac.id_eva=e.id AND vac.`id_per`=e.`id_per` and e.id_per="+id_per+" and vac.id_grad="+id_grad+" and vac.id_eva="+id_eva;
		List<Map<String,Object>> vacantes = jdbcTemplate.queryForList(sql);	

		return vacantes;
	} 
	
	public List<Map<String,Object>> Matriculados(int id_gra, int id_eva) {

		String sql = "SELECT count(id) matr_vac FROM `eva_matr_vacante` where id_gra="+id_gra+" and id_eva="+id_eva;
		List<Map<String,Object>> matriculados = jdbcTemplate.queryForList(sql);	

		return matriculados;
	}
	
	public Integer matriculasVacante(int id_eva,int id_gra, int id_per) {
		String sql = "SELECT COUNT(eva_mat.`id`) matriculados FROM `eva_matr_vacante` eva_mat INNER JOIN `eva_evaluacion_vac` eva ON `eva_mat`.`id_eva`=eva.`id` "
				+ " INNER JOIN `eva_matr_vacante_resultado` mat_res ON eva_mat.`id`=`mat_res`.`id_mat_vac`"
				+ " WHERE eva.`id`="+id_eva+" AND mat_res.`res`<>'NO APROBO' AND eva_mat.`id_gra`="+id_gra
				+ " AND `eva_mat`.`id_alu` NOT IN (SELECT res.id_alu FROM `mat_reserva` res WHERE res.id_alu AND res.`id_per`="+id_per+")";
		List<Map<String,Object>> matriculados = jdbcTemplate.queryForList(sql);	

		return Integer.parseInt(matriculados.get(0).get("matriculados").toString());
	}
		
	//Cabecera 6 ultimos
		public List<Map<String,Object>> Cabecera_Inicial(int id_suc, String anio) {
			String sql = "SELECT DISTINCT t.des FROM (SELECT DISTINCT eva.des, eva.`fec_ins` FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id AND a.nom='"+anio+"'"
					+ " AND ser.`nom`='INICIAL'  AND suc.id="+id_suc+" ORDER BY eva.fec_ins DESC LIMIT 6) t ORDER BY t.fec_ins ASC;";
			List<Map<String,Object>> Cabeceraini = jdbcTemplate.queryForList(sql);	

			return Cabeceraini;
		}
		
		public List<Map<String,Object>> Cabecera_Primaria(int id_suc, String anio) {
			String sql = "SELECT DISTINCT t.des FROM (SELECT DISTINCT eva.des, eva.`fec_ins` FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id AND a.nom='"+anio+"'"
					+ " AND ser.`nom`='PRIMARIA'   AND suc.id="+id_suc+" ORDER BY eva.fec_ins DESC LIMIT 6) t ORDER BY t.fec_ins ASC;";
			List<Map<String,Object>> Cabeceraini = jdbcTemplate.queryForList(sql);	

			return Cabeceraini;
		}
		
		public List<Map<String,Object>> Cabecera_Secundaria(int id_suc, String anio) {
			String sql = "SELECT DISTINCT t.des FROM (SELECT DISTINCT eva.des, eva.`fec_ins` FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id AND a.nom='"+anio+"'"
					+ " AND ser.`nom`='SECUNDARIA'   AND suc.id="+id_suc+" ORDER BY eva.fec_ins DESC LIMIT 6) t ORDER BY t.fec_ins ASC;";
			List<Map<String,Object>> Cabeceraini = jdbcTemplate.queryForList(sql);	

			return Cabeceraini;
		}
		
	//Cabecera Todos
		public List<Map<String,Object>> Cabecera_Todos_Inicial(int id_suc, String anio) {
			String sql = "SELECT DISTINCT eva.des FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id "
					+ " AND ser.`nom`='INICIAL' AND a.nom='"+anio+"' AND suc.id="+id_suc;
			List<Map<String,Object>> Cabecera = jdbcTemplate.queryForList(sql);	

			return Cabecera;
		}
		
		public List<Map<String,Object>> Cabecera_Todos_Primaria(int id_suc, String anio) {
			String sql = "SELECT DISTINCT eva.des FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id "
					+ " AND ser.`nom`='PRIMARIA' AND a.nom='"+anio+"' AND suc.id="+id_suc;
			List<Map<String,Object>> Cabecera = jdbcTemplate.queryForList(sql);	

			return Cabecera;
		}
		
		public List<Map<String,Object>> Cabecera_Todos_Secundaria(int id_suc, String anio) {
			String sql = "SELECT DISTINCT eva.des FROM eva_evaluacion_vac eva,per_periodo p, ges_servicio ser, "
					+ " ges_sucursal suc, col_anio a WHERE eva.id_per=p.id AND p.`id_srv`=ser.id AND p.id_anio=a.id AND ser.id_suc=suc.id "
					+ " AND ser.`nom`='SECUNDARIA' AND a.nom='"+anio+"' AND suc.id="+id_suc;
			List<Map<String,Object>> Cabecera = jdbcTemplate.queryForList(sql);	

			return Cabecera;
		}
	//Niveles po Local
		public List<Map<String,Object>> Niveles_Local(int id_suc) {
			String sql = "SELECT ser.nom FROM ges_servicio ser, ges_sucursal suc WHERE ser.id_suc=suc.id AND suc.id="+id_suc;
			List<Map<String,Object>> Niveles = jdbcTemplate.queryForList(sql);	

			return Niveles;
		}
		

		//Reporte de inscritos
	public List<Map<String,Object>> Reporte_Ins(int id_suc, String anio, List<Map<String,Object>>  cabeceraList) {
			String sql="SELECT t1.GRADO,";
			for (Map<String, Object> map : cabeceraList) {
				sql= sql + " SUM(t1.`"+map.get("des")+"`) as '"+map.get("des")+"',";
			}	
			sql = sql + "2 FROM (";
			sql = sql + "SELECT t.grado, t.anio,t.id_gra, t.sucursal,";
			for (Map<String, Object> map : cabeceraList) {
				sql= sql + " CASE t.des";
				sql= sql + " WHEN '"+map.get("des")+"' THEN t.cant ";
				sql= sql + " ELSE 0";
				sql= sql + " END AS '"+ map.get("des") +"',";
			}
					sql = sql + "1 FROM (";
					sql = sql + " SELECT n.nom nivel, eva.des des, g.nom grado,g.id id_gra, suc.nom sucursal, COUNT(m.id_gra) cant, a.nom anio";
					sql = sql + " FROM cat_grad g ";
					sql = sql + " LEFT JOIN eva_matr_vacante m  ON (m.id_gra=g.id)";
					sql = sql + " LEFT JOIN cat_nivel n ON (g.id_nvl=n.id) ";
					sql = sql + " LEFT JOIN eva_evaluacion_vac eva ON ( m.id_eva=eva.id) ";
					sql = sql + " LEFT JOIN per_periodo p ON (eva.id_per=p.id)";
					sql = sql + " LEFT JOIN col_anio a ON (p.id_anio=a.id)";
					sql = sql + " LEFT JOIN ges_servicio ser ON (p.id_srv=ser.id AND n.nom=ser.nom)";
					sql = sql + " LEFT JOIN ges_sucursal suc ON (ser.id_suc=suc.id)";
					sql = sql + " WHERE suc.id="+id_suc+" AND a.nom='"+anio+"' AND n.id='1' GROUP BY g.id, eva.id";
					sql = sql + " )t)t1 GROUP BY t1.grado order by t1.id_gra asc";
			List<Map<String,Object>> Inscritos = jdbcTemplate.queryForList(sql);	

			return Inscritos;
		}
		
	public List<Map<String,Object>> Reporte_InsPrim(int id_suc, String anio, List<Map<String,Object>>  cabeceraList) {
		String sql="SELECT t1.GRADO,t1.id_gra,";
		for (Map<String, Object> map : cabeceraList) {
			sql= sql + " SUM(t1.`"+map.get("des")+"`) as '"+map.get("des")+"',";
		}	
		sql = sql + "2 FROM (";
		sql = sql + "SELECT t.grado, t.id_gra,t.anio, t.sucursal,";
		for (Map<String, Object> map : cabeceraList) {
			sql= sql + " CASE t.des";
			sql= sql + " WHEN '"+map.get("des")+"' THEN t.cant ";
			sql= sql + " ELSE 0";
			sql= sql + " END AS '"+ map.get("des") +"',";
		}
				sql = sql + "1 FROM (";
				sql = sql + " SELECT n.nom nivel, eva.des des, g.nom grado, g.id id_gra, suc.nom sucursal, COUNT(m.id_gra) cant, a.nom anio";
				sql = sql + " FROM cat_grad g ";
				sql = sql + " LEFT JOIN eva_matr_vacante m  ON (m.id_gra=g.id)";
				sql = sql + " LEFT JOIN cat_nivel n ON (g.id_nvl=n.id) ";
				sql = sql + " LEFT JOIN eva_evaluacion_vac eva ON ( m.id_eva=eva.id) ";
				sql = sql + " LEFT JOIN per_periodo p ON (eva.id_per=p.id)";
				sql = sql + " LEFT JOIN col_anio a ON (p.id_anio=a.id)";
				sql = sql + " LEFT JOIN ges_servicio ser ON (p.id_srv=ser.id AND n.nom=ser.nom)";
				sql = sql + " LEFT JOIN ges_sucursal suc ON (ser.id_suc=suc.id)";
				sql = sql + " WHERE suc.id="+id_suc+" AND a.nom='"+anio+"' AND n.id='2' GROUP BY g.id, eva.id ";
				sql = sql + " )t)t1 GROUP BY t1.grado order by t1.id_gra asc";
		List<Map<String,Object>> Inscritos = jdbcTemplate.queryForList(sql);	

		return Inscritos;
	}
	
	public List<Map<String,Object>> Reporte_InSec(int id_suc, String anio, List<Map<String,Object>>  cabeceraList) {
		String sql="SELECT t1.GRADO,";
		for (Map<String, Object> map : cabeceraList) {
			sql= sql + " SUM(t1.`"+map.get("des")+"`) as '"+map.get("des")+"',";
		}	
		sql = sql + "2 FROM (";
		sql = sql + "SELECT t.grado, t.anio,t.id_gra, t.sucursal,";
		for (Map<String, Object> map : cabeceraList) {
			sql= sql + " CASE t.des";
			sql= sql + " WHEN '"+map.get("des")+"' THEN t.cant ";
			sql= sql + " ELSE 0";
			sql= sql + " END AS '"+ map.get("des") +"',";
		}
				sql = sql + "1 FROM (";
				sql = sql + " SELECT n.nom nivel, eva.des des, g.nom grado, g.id id_gra,suc.nom sucursal, COUNT(m.id_gra) cant, a.nom anio";
				sql = sql + " FROM cat_grad g ";
				sql = sql + " LEFT JOIN eva_matr_vacante m  ON (m.id_gra=g.id)";
				sql = sql + " LEFT JOIN cat_nivel n ON (g.id_nvl=n.id) ";
				sql = sql + " LEFT JOIN eva_evaluacion_vac eva ON ( m.id_eva=eva.id) ";
				sql = sql + " LEFT JOIN per_periodo p ON (eva.id_per=p.id)";
				sql = sql + " LEFT JOIN col_anio a ON (p.id_anio=a.id)";
				sql = sql + " LEFT JOIN ges_servicio ser ON (p.id_srv=ser.id AND n.nom=ser.nom)";
				sql = sql + " LEFT JOIN ges_sucursal suc ON (ser.id_suc=suc.id)";
				sql = sql + " WHERE suc.id="+id_suc+" AND a.nom='"+anio+"' AND n.id='3' GROUP BY g.id, eva.id";
				sql = sql + " )t)t1 GROUP BY t1.grado order by t1.id_gra asc";
		List<Map<String,Object>> Inscritos = jdbcTemplate.queryForList(sql);	

		return Inscritos;
	}
	//Lista de niveles filtrados x evaluacion

	public boolean estaMatriculado(Integer id_alu, Integer id_mat, Integer id_eva){
		
		String sql = "SELECT * FROM `eva_matr_vacante` where id_alu="+id_alu+" and id_eva="+id_eva;
		
		if(id_mat!=null)
			sql = sql + " and id!=" + id_mat;
	 	
		List<Map<String,Object>> matriculados = jdbcTemplate.queryForList(sql);
		
		
		return (matriculados.size()!=0);
	}
	
	/*public List<Map<String,Object>> estaDesaprobadoPisc(Integer id_alu, Integer id_per) {
		String sql = "SELECT DISTINCT cri_not.apto FROM `eva_criterio_nota` cri_not, `eva_exa_conf_criterio` cri,"
				+ " `eva_evaluacion_vac_examen` exa, `eva_evaluacion_vac` eva, `eva_matr_vacante` matr"
				+ " WHERE `cri_not`.`id_ex_cri`=cri.id AND cri.`id_eva_ex`=exa.`id` AND exa.`id_eva`=eva.`id` "
				+ " AND matr.`id_eva`=eva.`id` AND `cri_not`.`id_mat_vac`=matr.id AND  matr.id_alu="+id_alu
				+ " AND eva.id_per="+id_per;
		List<Map<String,Object>> Desaprobado = jdbcTemplate.queryForList(sql);	

		return Desaprobado;
	}*/
	
	public List<Map<String,Object>> estaDesaprobadoPisc(String nro_doc, Integer id_anio) {
		String sql = "SELECT DISTINCT cri_not.apto "
				+ " FROM `eva_criterio_nota` cri_not INNER JOIN `eva_exa_conf_criterio` cri ON `cri_not`.`id_ex_cri`=cri.id"
				+ " INNER JOIN `eva_evaluacion_vac_examen` exa ON cri.`id_eva_ex`=exa.`id` "
				+ " INNER JOIN `eva_evaluacion_vac` eva ON exa.`id_eva`=eva.`id`"
				+ " INNER JOIN `eva_matr_vacante` matr ON matr.`id_eva`=eva.`id` AND `cri_not`.`id_mat_vac`=matr.id"
				+ " INNER JOIN `per_periodo` per ON eva.`id_per`=per.`id`"
				+ " INNER JOIN `alu_alumno` alu ON matr.`id_alu`=alu.`id`"
				+ " WHERE alu.`nro_doc`="+nro_doc+" AND `per`.`id_anio`="+id_anio+" AND cri_not.apto='N'";
		List<Map<String,Object>> Desaprobado = jdbcTemplate.queryForList(sql);	

		return Desaprobado;
	}

	//Departamento/Provincia/Distrito
	public List<Map<String,Object>> Procedencia(int id_col) {
		String sql = "SELECT d.nom dep, p.nom pro, di.`nom` dis, c.`nom`"
					+ " FROM col_colegio c, cat_departamento d, cat_provincia p, `cat_distrito` di WHERE c.`id_dist`=di.`id` AND di.`id_pro`=p.`id` AND p.`id_dep`=d.`id` AND c.id="+id_col;
		List<Map<String,Object>> Procedencia = jdbcTemplate.queryForList(sql);	

		return Procedencia;
	}
	
	/**
	 * Lista de alumnos aptos para reserva 
	 * @param id_anio
	 * @param nomApeAlumno
	 * @return
	 */
		public List<Map<String,Object>> listAptosParaReserva(Integer id_anio,Integer id_anio_ant,String nomApeAlumno, String id_suc, String tipoCronogramaVigente) {
			/*String sql = "select alu.id id_alu, alu.ape_pat, alu.ape_mat, alu.nom, vac.id id_vac, vac.id_gra, srv.nom servicio, gra.nom grado, res.id id_res ";
			sql += " from eva_matr_vacante vac";
			sql += " inner join alu_alumno alu on alu.id = vac.id_alu";
			sql += " inner join eva_evaluacion_vac eva on eva.id = vac.id_eva"; 
			sql += " inner join per_periodo per on per.id = eva.id_per"; 
			sql += " inner join ges_servicio srv on srv.id = per.id_srv"; 
			sql +=" INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
			sql += " inner join cat_grad gra on gra.id = vac.id_gra"; 
			sql += " left join mat_reserva res on (res.id_alu=alu.id ) "; //@TODO falta probar cuando el alumno postula en dos a�os 
			sql += " where res='A' and per.id_anio=" + id_anio;*/
			
			String sql="";
			if ("NC".equals(tipoCronogramaVigente)){
				/**
				 * -- ALUMNOS QUE HAN RESERVADO MISMO LOCAL
				 */
				sql += "\n Select distinct alu.id id_alu, p.ape_pat, p.ape_mat, p.nom, vac.id id_vac, vac.id_gra, srv.nom servicio, gra.nom grado, res.id id_res"; 
				sql += "\n from eva_matr_vacante vac";
				sql += "\n inner join alu_alumno alu on alu.id = vac.id_alu";
				sql += "\n INNER JOIN col_persona p ON p.id = alu.id_per";
				sql += "\n inner join eva_evaluacion_vac eva on eva.id = vac.id_eva -- and CURDATE()<=eva.fec_vig_vac"; 
				sql += "\n inner join per_periodo per on per.id = eva.id_per and per.id_anio=" + id_anio;
				sql += "\n inner join ges_servicio srv on srv.id = per.id_srv";
				sql += "\n INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
				sql += "\n inner join cat_grad gra on gra.id = vac.id_gra";
				sql += "\n INNER join mat_reserva res on (res.id_alu=alu.id ) and curdate() <=fec_lim";
				sql += "\n inner join col_aula au on res.id_au=au.id";
				sql += "\n inner join per_periodo per_res on au.id_per=per_res.id and per_res.id_anio=" + id_anio;
				sql += "\n INNER JOIN mat_cronograma_reserva cro ON per.id_niv=cro.id_niv AND per.id_anio=cro.id_anio ";
				sql += "\n where  and per_res.id_suc=" + id_suc  ; //por emergencia cometado vac.res='A'
				
				sql += "\n union ";
				/**
				 *ALUMNOS QUE HAN SOLICITADO CAMBIO 
				 */
				 
				sql += "\n select distinct alu.id id_alu,  pers.ape_pat, pers.ape_mat, pers.nom,";
				sql += "\n mat.id id_vac, m.id_gra,";
				sql += "\n n.nom servicio, gra.nom grado,"; 
				sql += "\n r.id id_res";
				sql += "\n from mat_solicitud sol";
				sql += "\n inner join mat_reserva r on r.id_alu = sol.id_alu ";
				sql += "\n inner join cat_grad gra on gra.id = r.id_gra";
				sql += "\n inner join cat_nivel n on n.id = r.id_niv";
				sql += "\n inner join per_periodo p on p.id = r.id_per";
				sql += "\n inner join alu_alumno alu on alu.id = sol.id_alu";
				sql += "\n INNER JOIN col_persona pers ON pers.id = alu.id_per";
				sql += "\n left join ( mat_matricula m inner join per_periodo per on per.id = m.id_per    )";
				sql += "\n on m.id_alu = alu.id  and per.id_anio=" + id_anio;
				sql += "\n inner join eva_matr_vacante mat on  mat.id_alu = alu.id "; //por emergencia comentado and mat.res='A'
				sql += "\n where p.id_anio=" + id_anio+ " and sol.id_anio=p.id_anio and sol.est='A' and sol.id_suc_des=" + id_suc;

				
			}else{
				 sql="SELECT alu.id id_alu, p.ape_pat, p.ape_mat, p.nom, vac.id id_vac, vac.id_gra, srv.nom servicio, gra.nom grado, l.id id_res ";
				   sql +="\n FROM eva_matr_vacante vac ";
				   sql += "\n INNER JOIN alu_alumno alu ON alu.id = vac.id_alu";
				   sql += "\n INNER JOIN col_persona p ON p.id = alu.id_per";
				   sql += "\n INNER JOIN eva_evaluacion_vac eva ON eva.id = vac.id_eva ";
				   sql += "\n INNER JOIN per_periodo per ON per.id = eva.id_per";
				   sql += "\n INNER JOIN ges_servicio srv ON srv.id = per.id_srv";
				   sql += "\n INNER JOIN `ges_sucursal` suc ON srv.`id_suc`=suc.id";
				   sql += "\n INNER JOIN cat_grad gra ON gra.id = vac.id_gra";
				   sql += "\n INNER JOIN mat_cronograma_reserva cro ON per.id_niv=cro.id_niv AND per.id_anio=cro.id_anio";
				  // sql += "\n LEFT JOIN mat_reserva res ON (res.id_alu=alu.id ) AND res.`id_gra`=vac.`id_gra`";
				   sql += "\n LEFT JOIN (SELECT res.id, res.`id_alu`, res.`id_gra` FROM mat_reserva res ";
				   sql += "\n INNER JOIN `per_periodo` per_res ON res.`id_per`=per_res.`id`  AND per_res.`id_anio`=7)l ON l.id_alu=alu.id  AND l.`id_gra`=vac.`id_gra`";
				   //sql += "\n LEFT JOIN `per_periodo` per_res ON res.`id_per`=per_res.`id`  AND per_res.`id_anio`="+id_anio;
				   sql += "\n WHERE  CURDATE()<=eva.fec_vig_vac AND (CURDATE() BETWEEN cro.fec_ini AND cro.fec_fin) AND per.id_anio="+id_anio; //por emergencia comentado vac.res='A' AND
				   if(id_suc!=null)
					   sql += " and suc.id="+id_suc;

					   sql += "\n UNION ALL";
					   sql += "\n select distinct alu.id id_alu, p.ape_pat, p.ape_mat, p.nom, m.id id_vac, m.id_gra, ser.nom servicio, gra.nom grado, res.id id_res";
					   sql += "\n from `mat_matricula` m";
					   sql += "\n inner join `alu_alumno` alu on alu.`id`=m.`id_alu`";
					   sql += "\n INNER JOIN col_persona p ON p.id = alu.id_per";
					   sql += "\n LEFT JOIN `mat_reserva` res ON (alu.`id`=res.`id_alu` AND res.`id_per` IN (SELECT pp.id FROM per_periodo pp  WHERE pp.`id_tpe`=1 AND pp.id_anio=" + id_anio + "))";
					   sql += "\n inner join `per_periodo` per on m.`id_per`=per.`id`";
					   sql += "\n inner join `ges_servicio` ser on ser.`id`=per.`id_srv`";
					   sql += "\n inner join `ges_sucursal` suc on suc.`id`=ser.`id_suc`";
					   sql += "\n inner join cat_grad gra on gra.id = m.id_gra";
					   sql += "\n left join mat_seccion_sugerida sug on  sug.id_mat = m.id and sug.id_anio= "+ id_anio;
					   sql += "\n left join col_aula au_sug on au_sug.id = sug.id_au_nue ";
					   sql += "\n left join per_periodo per_sug on per_sug.id = au_sug.id_per ";
					   sql += "\n left join ges_servicio srv_sug on srv_sug.id = per_sug.id_srv ";

					   if(tipoCronogramaVigente.equals("AC")){
						   sql += "\n inner join mat_cronograma cro on  cro.id_anio ="+id_anio;
					   }
	 				   sql += "\n where m.`id_sit`='2'  and m.id_gra<>'14' and per.`id_anio`="+id_anio_ant;
					   if(tipoCronogramaVigente.equals("AC")){
						   sql += "\n and UPPER(SUBSTR(p.ape_pat,1,LENGTH(cro.del)))>=cro.del AND UPPER(SUBSTR(p.ape_pat,1,LENGTH(cro.al)))<=cro.al and cro.fec_mat=current_date";  
					   }
					   if(id_suc!=null){
	 					   sql += "\n and ((srv_sug.id_suc is not null and srv_sug.id_suc=" + id_suc + ") or (srv_sug.id_suc is null and suc.id= " +id_suc  + "))";
					   }

				   //}

					
				   sql += "\n UNION ALL \n";
				   //SOLICITUD PARA ALUMNOS ANTIUGOS
				   String sqlSolicitud = "select alu.id id_alu,  pers.ape_pat, pers.ape_mat, pers.nom,";
				   sqlSolicitud += "\n mat_ant.id id_vac, m.id_gra,"; 
				   sqlSolicitud += "\n srv_ant.nom servicio, gra.nom grado, ";
				   sqlSolicitud += "\n r.id id_res";
				   sqlSolicitud += "\n from mat_solicitud sol"; 
				   sqlSolicitud += "\n inner join mat_reserva r on r.id_alu = sol.id_alu";
				   sqlSolicitud += "\n inner join cat_grad gra on gra.id = r.id_gra";
				   sqlSolicitud += "\n inner join per_periodo p on p.id = r.id_per";
				   sqlSolicitud += "\n inner join alu_alumno alu on alu.id = sol.id_alu";
				   sqlSolicitud += "\n INNER JOIN col_persona pers ON pers.id = alu.id_per";
				   sqlSolicitud += "\n left join ( mat_matricula m inner join per_periodo per on per.id = m.id_per    )"; 
				   sqlSolicitud += "\n on m.id_alu = alu.id  and per.id_anio=3";
				   sqlSolicitud += "\n inner join mat_matricula mat_ant on  mat_ant.id_alu = alu.id";   
				   sqlSolicitud += "\n inner join col_aula au_ant on au_ant.id = mat_ant.id_au_asi";
				   sqlSolicitud += "\n inner join per_periodo per_ant on per_ant.id = au_ant.id_per";  
				   sqlSolicitud += "\n inner join ges_servicio srv_ant on srv_ant.id = per_ant.id_srv";  
				   sqlSolicitud += "\n where per_ant.id_anio=" + id_anio_ant +" and p.id_anio=" + id_anio + " and sol.id_anio=p.id_anio and sol.est='A'";

				   sql += sqlSolicitud;
				   sql = "select t.id_alu, t.ape_pat, t.ape_mat, t.nom, t.id_vac,t.id_gra, t.servicio, t.grado, t.id_res from (" + sql + ")t "
				   		+ " where t.id_alu not in (select m.id_alu from mat_matricula m inner join per_periodo p on m.id_per = p.id where p.id_anio=" + id_anio + " AND p.id_tpe=1 AND (m.id_sit<>'5' OR m.id_sit IS NULL))"
				   		+ " order by t.ape_pat asc";
				   
			}
			
			System.out.println(sql);
			List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);	

			return list;
		}
		
	public int CondFinal(MatrVacante matr_vacante) {
		
		// update
		String sql = "UPDATE eva_matr_vacante "
					+ "SET res=? "
					+ "WHERE id=?";
		
		return jdbcTemplate.update(sql, 
				matr_vacante.getRes(),
				matr_vacante.getId()); 

	}
	
	public List<Row> listarMatriculasVancante(Integer id_alu) {
		String sql = "SELECT emv.`id`,  evv.`des`, a.`nom` anio, DATE_FORMAT(evv.fec_fin,'%d/%m/%Y') fec_fin, IF(CURDATE()<=evv.fec_fin,1,0) vigencia" 
				+ " FROM `eva_matr_vacante` emv INNER JOIN `alu_alumno` alu ON emv.`id_alu`=alu.`id` "
				+ " INNER JOIN `eva_evaluacion_vac` evv ON emv.`id_eva`=evv.`id`"
				+ " INNER JOIN `per_periodo` per ON evv.`id_per`=per.`id`"
				+ " INNER JOIN `col_anio` a ON per.`id_anio`=a.`id`"
				+ " WHERE emv.`id_alu`=?";
		Object[] params = new Object[]{id_alu};

		return sqlUtil.query(sql,params);	
	}	
	
	public List<Row> listarColegioProcedencia(Integer id_anio) {
		String sql = "SELECT col.`id`, col.`cod_mod`,col.`nom`, niv.`nom` nivel, dist.`nom` distrito, pro.`nom` provincia, dep.`nom` departamento, COUNT(matr.`id`) inscritos\n" + 
				"FROM `eva_matr_vacante` matr INNER JOIN `alu_alumno` alu ON matr.`id_alu`=alu.`id`\n" + 
				"INNER JOIN `col_colegio` col ON matr.`id_col`=col.`id`\n" + 
				"INNER JOIN `eva_evaluacion_vac` eva ON matr.`id_eva`=eva.`id`\n" + 
				"INNER JOIN `per_periodo` per ON eva.`id_per`=per.`id`\n" + 
				"INNER JOIN `cat_nivel` niv ON col.`nom_niv`=niv.`nom`\n" + 
				"INNER JOIN `cat_distrito` dist ON col.`id_dist`=dist.`id`\n" + 
				"INNER JOIN `cat_provincia` pro ON dist.`id_pro`=pro.`id`\n" + 
				"INNER JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`\n" + 
				"WHERE per.`id_anio`=?\n" + 
				"GROUP BY col.id\n" + 
				"ORDER BY col.nom;";
		Object[] params = new Object[]{id_anio};

		return sqlUtil.query(sql,params);	
	}	
	
	public Row obtenerDatosInscripcion(Integer id_ins) {
		
		String sql = "SELECT emv.*, niv.`id` id_niv, eva.`fec_fin`"
				+ " FROM `eva_matr_vacante` emv INNER JOIN `cat_grad` gra ON emv.`id_gra`=gra.`id`"
				+ " INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
				+ " INNER JOIN `eva_evaluacion_vac` eva ON emv.`id_eva`=eva.`id`"
				+ " WHERE emv.id="+id_ins;
				
		List<Row> inscripcion =sqlUtil.query(sql);
		if(inscripcion.size()>0)
			return inscripcion.get(0);
		else 
			return null;

	}
	
	public int condFinal(String res, Integer id_mat) {
		
		// update
		String sql = "UPDATE eva_matr_vacante "
					+ "SET res=?, fec_act=?, usr_act=? "
					+ "WHERE id=?";
		
		//System.out.println(sql);
		return jdbcTemplate.update(sql, 
				res, new java.util.Date(),tokenSeguridad.getId(),
				id_mat); 

	}
	

}
