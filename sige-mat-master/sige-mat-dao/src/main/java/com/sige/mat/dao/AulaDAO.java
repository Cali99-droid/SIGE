package com.sige.mat.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AulaDAOImpl;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.CursoAula;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Row;

import java.util.List;
import java.util.Map; 

/**
 * Define m�todos DAO operations para la entidad aula.
 * @author MV
 *
 */
@Repository
public class AulaDAO extends AulaDAOImpl{
	final static Logger logger = Logger.getLogger(AulaDAO.class);

	@Autowired
	SQLUtil sqlUtil;
	
	public List<Map<String,Object>> listAula() {
	
		String sql = "SELECT au.id id,tper.nom tper,n.nom niv,g.nom gra,au.cap cap, t.nom tur,au.secc secc,au.est, a.nom anio,suc.nom LOCAL,"
				+ " (SELECT CONCAT(n.nom,' ',g.nom,' ',aula.secc)  FROM col_aula aula, cat_grad g, cat_nivel n WHERE aula.id=au.id_secc_ant "
				+ " AND aula.id_grad=g.id AND g.id_nvl=n.id) secc_ant FROM col_aula au,per_periodo per,cat_tip_periodo tper,col_turno t, "
				+ " cat_grad g, cat_nivel n, col_anio a, ges_servicio s, ges_sucursal suc WHERE au.id_per=per.id "
				+ " AND per.id_tpe=tper.id AND au.id_grad=g.id AND g.id_nvl=n.id AND per.id_anio=a.id AND s.id_suc=suc.id "
				+ " AND per.id_srv=s.id AND au.id_tur=t.id"
				+ " ORDER BY a.nom desc, g.nom asc, au.secc asc;";
		List<Map<String,Object>> listAula = jdbcTemplate.queryForList(sql);			
		return listAula;
	} 
	
	public List<Map<String,Object>> lisPeriodo() {
	
		String sql = "select p.id,tp.nom tper,a.nom anio, ser.nom ser, suc.nom suc, suc.id id_suc from per_periodo p, cat_tip_periodo tp,"
				+ " ges_servicio ser,ges_sucursal suc, col_anio a where p.id_tpe=tp.id and p.id_srv=ser.id and "
				+ " ser.id_suc=suc.id and p.id_anio=a.id and tp.id="+Constante.TIPO_PERIODOES+" and p.est='"+Constante.ESTADO+"'";
		List<Map<String,Object>> listAula = jdbcTemplate.queryForList(sql);			
		return listAula;
	} 
	
	public List<Map<String,Object>> lisPeriodoVac() {
	
		String sql = "select p.id,tp.nom tper,a.nom anio,n.id id_niv, ser.nom ser, suc.nom suc, suc.id id_suc from per_periodo p, cat_tip_periodo tp,"
				+ " ges_servicio ser,cat_nivel n, ges_sucursal suc, col_anio a where p.id_tpe=tp.id and ser.nom=n.nom and p.id_srv=ser.id and "
				+ " ser.id_suc=suc.id and p.id_anio=a.id and tp.id="+Constante.TIPO_PERIODOVAC+" and p.est='"+Constante.ESTADO+"'";
		List<Map<String,Object>> listAula = jdbcTemplate.queryForList(sql);			
		return listAula;
	} 
	
	public List<Map<String,Object>> VerifAula(String id_per, String id_grad, String secc) {

		String sql = "select * from col_aula where id_per="+id_per+" and id_grad="+id_grad+" and secc='"+secc+"'";
		List<Map<String,Object>> listAula = jdbcTemplate.queryForList(sql);	
		//logger.info(sql);
		return listAula;
	} 
	
	//Filtrar los grados
	public List<Map<String,Object>> GradporNiv(int id_per) {

		String sql = "select g.* from cat_grad g, cat_nivel n, per_periodo p, ges_servicio s"
				+ " where g.id_nvl=n.id and s.nom=n.nom and p.id_srv=s.id and p.id="+id_per;
		List<Map<String,Object>> listGrado = jdbcTemplate.queryForList(sql);	
		//logger.info(sql);
		return listGrado;
	}
	
	//Filtrar los turnos
		public List<Map<String,Object>> TurporSer(int id_per) {

			String sql = "SELECT tur.id, tur.nom FROM col_turno tur, col_tur_servicio tur_ser, per_periodo per, ges_servicio ser"
					+ " where per.id_srv=ser.id and tur_ser.id_srv=ser.id and tur.id=tur_ser.id_tur and per.id="+id_per;
			List<Map<String,Object>> listTurno = jdbcTemplate.queryForList(sql);	
			//logger.info(sql);
			return listTurno;
		}
	
	//Filtrar las secciones anteriores
		public List<Map<String,Object>> seccAnt (int id_anio_ant, int id_grad, int id_suc) {

			String sql = "SELECT au.id, CONCAT(g.nom,'',au.secc) value FROM col_aula au, cat_grad g, per_periodo p, col_anio a, ges_servicio ser, ges_sucursal suc"
					+ " WHERE au.id_grad=g.id AND p.id_anio=a.id AND au.id_per=p.id AND p.id_anio='"+id_anio_ant+"' AND au.id_grad="+id_grad+" and p.id_srv=ser.id and ser.id_suc=suc.id and suc.id="+id_suc;
			List<Map<String,Object>> listSeccAnt = jdbcTemplate.queryForList(sql);	
			//logger.info(sql);
			return listSeccAnt;
			}
	
		//alumnos por aula
		
		public List<Row> listMatriculados(int id_au, String tipo) {

			if (tipo.equals("N")){
				String sql = "select m.id, concat(ape_pat, ' ', a.nom ) nombres from mat_matricula m inner join alu_alumno a on a.id= m.id_alu where m.id_au_asi= " + id_au;
				//return jdbcTemplate.queryForList(sql,Integer.class);

				logger.debug(sql);
				return sqlUtil.query(sql);	
			}else{
				String sql = "select ae.id_mat id ,  concat(ape_pat, ' ', a.nom ) nombres from col_aula_especial ae inner join mat_matricula m on m.id= ae.id_mat inner join alu_alumno a on a.id= m.id_alu where ae.id_au= " + id_au;
				//return jdbcTemplate.queryForList(sql,Integer.class);
				return sqlUtil.query(sql);
			}
			
		}

		public Row getSucursal(Integer id_au){
			String sql = "select suc.* "
					+ " from  mat_matricula m "
					+ " inner join col_aula au on au.id = m.id_au_asi"
					+ " inner join cat_grad g on au.id_grad = g.id"
					+ " left join col_ciclo cic on au.id_cic=cic.id"
					+ " left join per_periodo p on p.id = au.id_per"
					+ " inner join ges_servicio s on s.id = p.id_srv"
					+ " inner join ges_sucursal suc on suc.id = s.id_suc"
					+ " where id_au_asi=? ";
			
			logger.debug(sql);

			return sqlUtil.query(sql,new Object[]{id_au}).get(0);
		
		}
		
		public List<Row> listAulasXLocal(Integer id_anio, Integer id_suc, Integer id_grad){
			String sql = "select a.* from col_aula a "
					+ " inner join per_periodo p on p.id = a.id_per"
					+ " where p.id_anio=? and p.id_suc=? and id_grad=?";
			
			return sqlUtil.query(sql,new Object[]{id_anio, id_suc,id_grad});
			
		}
		
		public List<Row> listAulasXCicloTurnoGrado(Integer id_cic, Integer id_grad,Integer id_tur, Integer id_au){
			String sql = "SELECT au.`id`, au.`secc` value, gra.nom grado, niv.nom nivel \n"; 
					sql += "FROM `col_aula` au INNER JOIN `col_turno_aula` cta ON au.`id`=cta.id_au\n"; 
					sql += " INNER JOIN col_ciclo_turno cit ON cta.id_cit=cit.id\n";
					sql += " INNER JOIN cat_grad gra ON au.id_grad=gra.id\n";
					sql += " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n";
					sql += "WHERE au.`id_cic`=? AND au.`id_grad`=? ";
					if(id_tur!=null) {
						sql += " AND cit.id_tur="+id_tur;
					}
					if(id_au!=null) {
						sql += " AND au.id="+id_au;
					}
			
			return sqlUtil.query(sql,new Object[]{id_cic, id_grad});
			
		}
		
		public List<Row> listAulasXCicloTurnoGradoDocente(Integer id_cic, Integer id_grad,Integer id_tur, Integer id_au, Integer id_tra){
			String sql = "SELECT DISTINCT au.`id`, au.`secc` value, gra.nom grado, niv.nom nivel \n"; 
					sql += "FROM `col_aula` au INNER JOIN `col_turno_aula` cta ON au.`id`=cta.id_au\n"; 
					sql += " INNER JOIN col_ciclo_turno cit ON cta.id_cit=cit.id\n";
					sql += " INNER JOIN col_curso_aula cua ON cua.id_au=au.id ";
					sql += " INNER JOIN cat_grad gra ON au.id_grad=gra.id\n";
					sql += " INNER JOIN cat_nivel niv ON gra.id_nvl=niv.id \n";
					sql += "WHERE au.`id_cic`=? AND au.`id_grad`=? AND cua.id_tra=? ";
					if(id_tur!=null) {
						sql += " AND cit.id_tur="+id_tur;
					}
					if(id_au!=null) {
						sql += " AND au.id="+id_au;
					}
			
			return sqlUtil.query(sql,new Object[]{id_cic, id_grad, id_tra});
			
		}
		
		public List<Row> listGradosxCicloTurno(Integer id_cic,Integer id_tur){
			String sql = "SELECT DISTINCT gra.`id`, gra.`nom` value\n" + 
					"FROM `col_aula` au INNER JOIN `col_turno_aula` cta ON au.`id`=cta.id_au\n" + 
					" INNER JOIN col_ciclo_turno cit ON cta.id_cit=cit.id\n"+
					" INNER JOIN cat_grad gra ON au.id_grad=gra.id"+
					" WHERE au.`id_cic`=?  AND cit.id_tur=?";
			
			return sqlUtil.query(sql,new Object[]{id_cic ,id_tur});
			
		}
		
		public List<Row> listAulasTodos(Integer id_anio, Integer id_grad){
			String sql = "select a.* from col_aula a "
					+ " inner join col_ciclo cic on cic.id = a.id_cic"
					+ " inner join per_periodo p on p.id = cic.id_per"
					+ " where p.id_anio=? and id_grad=? and p.id_tpe=1"
					+ " order by a.secc ";
			
			return sqlUtil.query(sql,new Object[]{id_anio,id_grad});
			
		}
		
		public List<Row> listAulasxGradoLocal(Integer id_anio, Integer id_grad, Integer id_suc, Integer id_gir){
			String sql = "select a.id, a.secc value from col_aula a ";
					sql += " inner join col_ciclo cic on a.id_cic=cic.id";
					sql += " inner join per_periodo p on p.id = cic.id_per";
					sql += " inner join ges_servicio srv on p.id_srv = srv.id";
					sql += " where p.id_anio=? and a.id_grad=? and srv.id_gir=?";
					if (id_suc!=null) {
						sql += " and p.id_suc="+id_suc;
					}
					sql += " order by a.secc ";
			
			return sqlUtil.query(sql,new Object[]{id_anio,id_grad,id_gir});
			
		}
		
		public List<Aula> listAulasxTutor(Integer id_anio, Integer id_tra, Integer id_rol) {
			String sql="";
			if(id_rol.equals(5)) {
				sql = "SELECT au.* FROM `col_aula` au INNER JOIN col_aula_detalle cad ON au.`id`=cad.`id_au`";
				sql += " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`";
				sql += " WHERE per.id_anio="+id_anio+" AND cad.`id_aux`="+id_tra;
			} else if(id_rol.equals(20)) {
				sql = "SELECT au.* FROM `col_aula` au ";
				sql += " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`";
				sql += " INNER JOIN seg_usuario usr ON usr.id_suc=per.id_suc ";
				sql += " WHERE per.id_anio="+id_anio+" AND usr.`id_tra`="+id_tra;
			} else if(id_rol.equals(1)) {
				sql = "SELECT au.* FROM `col_aula` au ";
				sql += " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`";
				sql += " WHERE per.id_anio="+id_anio+" AND per.id_tpe=1";
			}
			return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Aula.class));

		}
		
		
		
	/**
	 * Lista de aulas por grado, a�o y nivel
	 * @param id_anio
	 * @param id_niv
	 * @param id_gra
	 * @return
	 */
		public List<CursoAula> listAulasxGrado(Integer id_anio, Integer id_cur,Integer id_gra) {
			
			/*String sql = "SELECT au.* FROM col_aula au "
					+ " INNER JOIN per_periodo per ON au.id_per=per.id"
					+ " WHERE per.id_anio=? and per.id_niv=? and au.id_grad=? " ;*/
			String sql =" SELECT cca.* FROM col_curso_aula cca INNER JOIN col_curso_anio cua ON cca.id_cua=cua.id"
					+ " INNER JOIN per_periodo per ON cua.id_per=per.id"
					+ " WHERE per.id_anio=? AND cua.id_cur=? AND cua.id_gra=?";

			return sqlUtil.query(sql, new Object[]{id_anio, id_cur,id_gra},CursoAula.class);

		}
		
		public List<Row> listAulasxTutoryLocal(Integer id_suc, Integer id_tra, Integer id_anio, Integer id_grad) {
			
			String sql = "SELECT au.`id`, au.`secc` value "
					+ " FROM `col_tutor_aula` cta INNER JOIN col_aula au ON cta.`id_au`=au.`id`"
					+ " INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`"
					+ " WHERE per.`id_suc`=? AND cta.`id_tra`=? and per.id_anio=? and au.id_grad=?";

			return sqlUtil.query(sql,new Object[]{id_suc,id_tra, id_anio, id_grad});

		}
		
		public List<Row> listarAulasxNivelGrado(Integer id_anio, Integer id_niv, Integer id_gra) {
			
			String sql = "SELECT au.`id`, au.`secc` value ";
					sql += " FROM col_aula au INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`";
					sql += " WHERE per.id_anio="+id_anio;
					sql += " AND per.id_niv="+id_niv;
					sql += " AND au.id_grad="+id_gra;
					sql += " ORDER BY au.secc  ";

			return sqlUtil.query(sql);

		}
		
		public List<Row> listarAulasxNivelGradoLocal(Integer id_anio, Integer id_niv, Integer id_gra, Integer id_suc) {
			
			String sql = "SELECT au.`id`, au.`secc` value ";
					sql += " FROM col_aula au INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`";
					sql += " WHERE per.id_anio="+id_anio;
					sql += " AND per.id_niv="+id_niv;
					sql += " AND au.id_grad="+id_gra;
					sql += " AND per.id_suc="+id_suc;
					sql += " ORDER BY au.secc  ";

			return sqlUtil.query(sql);

		}
		
		public List<Row> listAulasxCiclo(Integer id_cic){
			String sql = "SELECT au.`id`, au.id_grad, au.`secc`, cic.nom ciclo, gra.`nom` grado, gra.abrv, gra.abrv_classroom, niv.`nom` nivel, gra.`orden`, au.des_classroom, au.id_classroom \n" + 
					"FROM `col_aula` au INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" + 
					"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\n" + 
					"INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\n" + 
					"WHERE cic.`id`=? ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_cic});
			
		}
		
		public Row generarDescripcionAula(Integer id_au){

			String sql = "SELECT CONCAT(t.nivel,' - ', t.grado,' ', t.secc) descripcion FROM (SELECT\n" + 
					"CASE WHEN gra.id=1 THEN '3 ANIOS'\n" + 
					"WHEN gra.id=2 THEN '4 ANIOS'\n" + 
					"WHEN gra.id=3 THEN '5 ANIOS'\n" + 
					"ELSE gra.nom\n" + 
					"END grado,\n" + 
					"niv.nom nivel, au.secc\n" + 
					"FROM \n" + 
					"`col_aula` au INNER JOIN `col_ciclo` cic ON au.`id_cic`=cic.`id`\n" + 
					"INNER JOIN `per_periodo` per ON cic.`id_per`=per.id\n" + 
					"INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" + 
					"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\n" + 
					"INNER JOIN `cat_nivel` niv ON gra.id_nvl=niv.id \n" + 
					"WHERE au.id="+id_au+")t";
					//+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
			
			List<Row> list = sqlUtil.query(sql);
			return list.get(0);
			
			//return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
		}
		
		public void actualizarIdClassroom(String id_classroom,Integer id_au){
			String sql = "update col_aula set id_classroom=? where id=?";
			sqlUtil.update(sql,new Object[]{id_classroom, id_au});		
		}
		
		public Row obtenerDatosAula(Integer id_au){

			String sql = "SELECT COUNT(mat.id) matriculados, gra.nom grado, nvl.nom nivel, tur.nom turno, au.secc, \n" + 
					"(SELECT COUNT(mat.id) trasladados FROM mat_matricula mat INNER JOIN `col_aula` au ON mat.id_au_asi=au.id \n" + 
					"WHERE au.id="+id_au+" AND mat.id_sit=5) trasladados\n" + 
					"FROM `mat_matricula` mat INNER JOIN `col_aula` au ON mat.`id_au_asi`=au.`id`\n" + 
					"INNER JOIN cat_grad gra ON au.`id_grad`=gra.id\n" + 
					"INNER JOIN cat_nivel nvl ON gra.id_nvl=nvl.id\n" + 
					"INNER JOIN `col_ciclo_turno` cit ON cit.`id_cic`=au.`id_cic`\n" + 
					"INNER JOIN `col_turno_aula` cta ON au.id=cta.`id_au` AND cit.`id`=cta.`id_cit`\n" + 
					"INNER JOIN col_turno tur ON cit.`id_tur`=tur.id\n" + 
					"WHERE au.id="+id_au+" AND (mat.`id_sit` NOT IN (5,6) OR mat.id_sit IS NULL);";
					//+ " ORDER BY alu.ape_pat, alu.ape_mat, alu.nom";
			
			List<Row> list = sqlUtil.query(sql);
			return list.get(0);
			
			//return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[]{ id_mat}));
		}
		
		public List<Row> listAulasxDocente(Integer id_anio, Integer id_tra, Integer id_gir){
			String sql = "SELECT  DISTINCT ca.id, CONCAT(niv.nom,' - ', gra.`nom`,' ', ca.secc) value, gra.id aux1, niv.id aux2 \n" + 
					"FROM `col_curso_aula` cca \n" + 
					"INNER JOIN col_aula ca ON cca.id_au=ca.id \n" + 
					"INNER JOIN `cat_grad` gra ON ca.`id_grad`=gra.`id`\n" + 
					"INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.id\n" + 
					"INNER JOIN `col_area_anio` caa ON caa.id=cca.`id_caa` \n" + 
					"INNER JOIN per_periodo per ON ca.id_per=per.id "+
					"INNER JOIN ges_servicio srv ON per.id_srv=srv.id "+
					"WHERE cca.id_tra=? AND caa.id_anio=? AND srv.id_gir=? \n" + 
					"ORDER BY gra.id, ca.secc ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_tra, id_anio, id_gir});
			
		}
		
		public List<Row> listAulasxCoordinadorNivel(Integer id_anio, Integer id_tra, Integer id_gir){
			String sql = "SELECT  DISTINCT ca.id, CONCAT(niv.nom,' - ', gra.`nom`,' ', ca.secc) value, gra.id aux1, niv.id aux2 \n" + 
					"FROM `col_curso_aula` cca \n" + 
					"INNER JOIN col_aula ca ON cca.id_au=ca.id \n" + 
					"INNER JOIN `cat_grad` gra ON ca.`id_grad`=gra.`id`\n" + 
					"INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.id\n" + 
					"INNER JOIN `col_area_anio` caa ON caa.id=cca.`id_caa` \n" + 
					"INNER JOIN per_periodo per ON ca.id_per=per.id "+
					"INNER JOIN ges_servicio srv ON per.id_srv=srv.id "+
					"INNER JOIN col_nivel_coordinador cniv ON cniv.id_anio=caa.id_anio AND cniv.id_niv=niv.id AND cniv.id_gir=srv.id_gir "+
					"WHERE cniv.id_tra=? AND caa.id_anio=? AND srv.id_gir=? \n" + 
					"ORDER BY gra.id, ca.secc ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_tra, id_anio, id_gir});
			
		}
		
		public List<Row> listAulasxCoordinadorArea(Integer id_anio, Integer id_tra, Integer id_gir){
			String sql = "SELECT  DISTINCT ca.id, CONCAT(niv.nom,' - ', gra.`nom`,' ', ca.secc) value, gra.id aux1, niv.id aux2 \n" + 
					"FROM `col_curso_aula` cca \n" + 
					"INNER JOIN col_aula ca ON cca.id_au=ca.id \n" + 
					"INNER JOIN `cat_grad` gra ON ca.`id_grad`=gra.`id`\n" + 
					"INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.id\n" + 
					"INNER JOIN `col_area_anio` caa ON caa.id=cca.`id_caa` \n" + 
					"INNER JOIN per_periodo per ON ca.id_per=per.id "+
					"INNER JOIN ges_servicio srv ON per.id_srv=srv.id "+
					"INNER JOIN col_area_coordinador cac ON cac.id_anio=caa.id_anio AND cac.id_niv=niv.id AND cac.id_gir=srv.id_gir AND cac.id_area=caa.id_area"+
					" WHERE cac.id_tra=? AND caa.id_anio=? AND srv.id_gir=? \n" + 
					"ORDER BY gra.id, ca.secc ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_tra, id_anio, id_gir});
			
		}
		
		public List<Row> listAulasxCoordinadorxGiro(Integer id_anio, Integer id_tra, Integer id_gir, Integer id_gra){
			String sql = "SELECT  DISTINCT ca.id, CONCAT(niv.nom,' - ', gra.`nom`,' ', ca.secc) value, gra.id aux1, niv.id aux2 \n" ;
					sql += "FROM `col_curso_aula` cca \n" ; 
					sql += "INNER JOIN col_aula ca ON cca.id_au=ca.id \n" ; 
					sql += "INNER JOIN `cat_grad` gra ON ca.`id_grad`=gra.`id`\n" ; 
					sql += "INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.id\n" ; 
					sql += "INNER JOIN `col_area_anio` caa ON caa.id=cca.`id_caa` \n" ; 
					sql += "INNER JOIN per_periodo per ON ca.id_per=per.id ";
					sql += "INNER JOIN ges_servicio srv ON per.id_srv=srv.id ";
					sql += "INNER JOIN col_nivel_coordinador cniv ON cniv.id_anio=caa.id_anio AND cniv.id_niv=niv.id AND cniv.id_gir=srv.id_gir ";
					sql += "WHERE caa.id_anio=? AND srv.id_gir=? \n"; 
					if(!id_tra.equals(0)) {
						sql += " AND cniv.id_tra="+id_tra;
					}
					if(id_gra!=null) {
						sql += " AND gra.id="+id_gra;
					}
					sql += " ORDER BY gra.id, ca.secc ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_anio, id_gir});
			
		}
		
		public List<Row> listAulasxTutorxAnio(Integer id_tut,Integer id_anio, Integer id_gir, Integer id_gra){
			String sql = "SELECT au.`id`, CONCAT(niv.`nom`,' - ', gra.`nom`,' ', au.`secc`) VALUE\n" ;
					sql += "FROM col_aula au INNER JOIN `col_aula_detalle` aud ON au.`id`=aud.`id_au`\n" ; 
					sql += "INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" ; 
					sql += "INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" ; 
					sql += "INNER JOIN `ges_giro_negocio` gir ON srv.`id_gir`=gir.`id`\n" ; 
					sql += "INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`\n" ; 
					sql += "INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`\n" ; 
					sql += "WHERE aud.`id_tut`=? AND per.`id_anio`=? AND gir.id=? ";
			if(id_gra!=null) {
				sql += " AND gra.id="+id_gra;
			}
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_tut, id_anio});
			
		}
		
		public List<Row> listarAulasxGiroNivelGrado(Integer id_anio, Integer id_gra, Integer id_gir){
			String sql = "SELECT  DISTINCT ca.id, CONCAT(niv.nom,' - ', gra.`nom`,' ', ca.secc) value, gra.id aux1, niv.id aux2 \n" ;
					sql += "FROM col_aula ca  \n" ; 
					sql += "INNER JOIN `cat_grad` gra ON ca.`id_grad`=gra.`id`\n" ; 
					sql += "INNER JOIN cat_nivel niv ON gra.`id_nvl`=niv.id\n" ; 
					sql += "INNER JOIN per_periodo per ON ca.id_per=per.id ";
					sql += "INNER JOIN ges_servicio srv ON per.id_srv=srv.id ";
					sql += "WHERE per.id_anio=? AND srv.id_gir=? ";
					if(id_gra!=null) {
						sql += "AND ca.id_grad=\n"+id_gra; 
					}							
					sql += " ORDER BY gra.id, ca.secc ";
			//and au.id IN (371,372)
			
			return sqlUtil.query(sql,new Object[]{id_anio, id_gir});
			
		}
		
		public List<Row> listarModalidadesxLocalyGrado(Integer id_anio, Integer id_suc, Integer id_gra){
			String sql = "SELECT DISTINCT cme.`id`, CONCAT(cme.`nom`,' - ', tur.nom) value, cct.id aux1 FROM `col_aula` au INNER JOIN `cat_modalidad_estudio` cme ON au.`id_cme`=cme.`id`\n" + 
					"INNER JOIN `per_periodo` per ON au.id_per=per.`id`\n" + 
					"INNER JOIN `col_ciclo` cic ON per.`id`=cic.`id_per`\n" + 
					"INNER JOIN `col_ciclo_turno` cct ON cic.`id`=cct.`id_cic`\n" + 
					"INNER JOIN `col_turno` tur ON cct.`id_tur`=tur.id \n" + 
					"INNER JOIN col_turno_aula tau ON cct.`id`=tau.`id_cit` AND tau.`id_au`=au.id \n" + 
					"WHERE per.`id_anio`=? AND per.`id_suc`=? AND au.`id_grad`=? AND per.id_tpe=1 \n"; 
			
			return sqlUtil.query(sql,new Object[]{id_anio, id_suc, id_gra});
			
		}
		
		public List<Row> listarLocalesxNivel(Integer id_niv , Integer id_anio){
			String sql = "SELECT DISTINCT suc.id, suc.nom value \n" + 
					"FROM per_periodo per INNER JOIN ges_sucursal suc ON per.id_suc=suc.id\n" + 
					"WHERE per.id_niv=? AND per.id_anio=? AND per.id_tpe=1 "; 
			
			return sqlUtil.query(sql,new Object[]{id_niv, id_anio});
			
		}
		
		public List<Row> listarLocalesxAnio(Integer id_anio){
			String sql = "SELECT DISTINCT suc.id, suc.nom value \n" + 
					"FROM per_periodo per INNER JOIN ges_sucursal suc ON per.id_suc=suc.id\n" + 
					"WHERE per.id_anio=? AND per.id_tpe=1 "; 
			
			return sqlUtil.query(sql,new Object[]{id_anio});
			
		}
}
