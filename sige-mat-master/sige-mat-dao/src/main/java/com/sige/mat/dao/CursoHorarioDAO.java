package com.sige.mat.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoSesion;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoHorarioDAOImpl;
import com.tesla.colegio.model.CursoHorario;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.FechaUtil;

/**
 * Define m�todos DAO operations para la entidad curso_horario.
 * @author MV
 *
 */
@Repository
public class CursoHorarioDAO extends CursoHorarioDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	@Autowired
	private SesionTipoDAO sesionTipoDAO;
	
	@Autowired
	private SubtemaDAO subtemaDAO;
	
	// lista para una grilla de coordinadores por area
		public List<Row> listaCursos(int id_au) {
			String sql = "SELECT cur.nom curso, cua.id, ccs.nro_ses, concat( tra.nom,' ',tra.ape_pat) docente"
				+ " FROM col_curso_aula cua "
				+ " INNER JOIN aeedu_asistencia.ges_trabajador tra ON tra.id= cua.id_tra"
				+ " INNER JOIN col_aula au ON cua.id_au=au.id"
				+ " INNER JOIN col_curso_anio cur_anio ON cua.id_cua=cur_anio.id"
				+ " INNER JOIN cat_curso cur ON cur_anio.id_cur=cur.id "
				+ " INNER JOIN `col_area_anio` caa ON cur_anio.`id_caa`=caa.`id`"
				+ " INNER JOIN `col_curso_sesion` ccs ON caa.id=ccs.`id_caa` AND ccs.`id_cur`=cur.id AND caa.`id_niv`=ccs.`id_niv` AND cur_anio.`id_gra`=ccs.`id_gra`"
				+ " WHERE cua.id_au=? ORDER BY cur.nom";

		return sqlUtil.query(sql, new Object[] { id_au });
		}
		
	
		
		public List<Row> listarHorariosxAula(int id_au, Integer anio_act){
			
			String sql="SELECT concat(cch.id,'-', cch.id_cca) as 'extendedProps', CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T',  hora_ini) `start`," 
					+ " CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T', hora_fin) `end`, concat(cur.nom,'\n(',concat( tra.nom,' ',tra.ape_pat),')') title"
					+ " FROM col_curso_horario cch "
					+ " INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`"
					+ " INNER JOIN aeedu_asistencia.ges_trabajador tra ON tra.id= cca.id_tra"
					+ " INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
					+ " INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id"
					+ " WHERE  cca.id_au=? ";


			return sqlUtil.query(sql, new Object[] { id_au });
		}

		/**
		 * TODO col_curso_horario_pad
		 * @param id_au
		 * @param id_cchp
		 * @return
		 */
		public List<Row> listarHorariosxAula(int id_au, int id_cchp, Integer anio_act){
			
			String sql="SELECT concat(cch.id,'-', cch.id_cca) as 'extendedProps', CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T',  hora_ini) `start`," 
					+ " CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T', hora_fin) `end`, concat(cur.nom,'\n(',concat( tra.nom,' ',tra.ape_pat),')') title"
					+ " FROM col_curso_horario cch "
					+ " INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`"
					+ " INNER JOIN aeedu_asistencia.ges_trabajador tra ON tra.id= cca.id_tra"
					+ " INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`"
					+ " INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id"
					+ " WHERE  cca.id_au=? and cch.id_cchp=?";


			return sqlUtil.query(sql, new Object[] { id_au,id_cchp });
		}

		/*
		 * Listar sesiones por curso horario y semana
		 */
		public List<Row> listarSesiones(Integer id_cch, Integer id_sem){

			/*
			String sql ="SELECT  uns.id, uns.`nro`,ccu.`nom` unidad,ccu.`num`, cch.`id` id_cch, cchs.`id` id_cchs"
					+ "\n FROM `col_unidad_sesion` uns "
					+ "\n INNER JOIN `col_curso_unidad` ccu ON uns.`id_uni`=ccu.`id`"
					+ "\n INNER JOIN `col_area_anio` caa ON ccu.`id_niv`=caa.`id_niv` "
					+ "\n INNER JOIN `col_curso_anio` cua ON cua.`id_caa`=caa.`id` AND cua.`id_cur`=ccu.`id_cur` AND cua.`id_gra`=ccu.`id_gra`"
					+ "\n INNER JOIN `col_curso_aula` cca ON cca.`id_cua`=cua.id "
					+ "\n INNER JOIN `col_curso_horario` cch ON cch.`id_cca`=cca.`id` "
					+ "\n INNER JOIN `col_per_uni` cpu ON ccu.`id_cpu`=cpu.`id`  "
					+ "\n INNER JOIN `col_per_uni_det` cpud ON cpud.`id_cpu`=cpu.`id`  AND cpud.`ord` = ccu.`num`  "
					+ "\n LEFT JOIN `col_curso_horario_ses` cchs ON cchs.`id_uns`=uns.`id`  AND cchs.id_cch = cch.id"
					+ "\n WHERE cch.id=? AND ? BETWEEN cpud.`sem_ini` AND cpud.`sem_fin` "
					//+ "\n AND (uns.id NOT IN (SELECT cchs1.id_uns FROM col_curso_horario_ses cchs1 INNER JOIN col_curso_horario cch1 ON cch1.id=cchs1.id_cch WHERE cchs1.id_ccs=? AND cch1.id_cca=cch.id_cca ) "//no mostrar las sesiones utilizadas en la semana y para el curso
					//+ "\n OR  cchs.`id` IS NOT NULL "
					//+ "\n )"; 
					+ "AND  (cchs.`id` IS NULL  OR (cchs.id_uns = uns.id && cchs.id_ccs=?  ) )";
			*/
			String id_cchSS = "";
			CursoHorario cursoHorario = get(id_cch);
			
			String sql = "select id from col_curso_horario where id_cca=" + cursoHorario.getId_cca();
			List<Row> idcchList= sqlUtil.query(sql);
			for (Row id : idcchList) {
				id_cchSS =id_cchSS + "," + id.getInteger("id") ;
			}
			
			id_cchSS = id_cchSS.substring(1);
			
			sql = "SELECT X.* FROM ("
					 + "\n SELECT  t.id, t.`nro`,t.unidad,t.`num`, null id_cchs" 
					 + "\n FROM ("
					 + "\n SELECT  uns.id, uns.`nro`,ccu.`nom` unidad,ccu.`num`, SUM( IFNULL( cchs.`id`,0)) id_cchs"
					 + "\n FROM `col_unidad_sesion` uns "
					 + "\n INNER JOIN `col_curso_unidad` ccu ON uns.`id_uni`=ccu.`id`"
					 + "\n INNER JOIN `col_area_anio` caa ON ccu.`id_niv`=caa.`id_niv` "
					 + "\n INNER JOIN `col_curso_anio` cua ON cua.`id_caa`=caa.`id` AND cua.`id_cur`=ccu.`id_cur` AND cua.`id_gra`=ccu.`id_gra`"
					 + "\n INNER JOIN `col_curso_aula` cca ON cca.`id_cua`=cua.id "
					 + "\n INNER JOIN `col_curso_horario` cch ON cch.`id_cca`=cca.`id` " 
					 + "\n INNER JOIN `col_per_uni` cpu ON ccu.`id_cpu`=cpu.`id`  "
					 + "\n INNER JOIN `col_per_uni_det` cpud ON cpud.`id_cpu`=cpu.`id`  AND cpud.`ord` = ccu.`num` "  
					 + "\n LEFT JOIN `col_curso_horario_ses` cchs ON cchs.`id_uns`=uns.`id`   AND cchs.id_cch = cch.id "
					 + "\n WHERE cch.id IN (" + id_cchSS +") AND  :id_sem BETWEEN cpud.`sem_ini` AND cpud.`sem_fin` "
					 + "\n GROUP BY 1,2,3,4 "
					 + "\n ) t WHERE t.id_cchs=0 "
					 + "\n UNION "
					 + "\n SELECT  uns.id, uns.`nro`,ccu.`nom` unidad,ccu.`num`, cchs.`id` id_cchs "
					 + "\n FROM `col_unidad_sesion` uns  "
					 + "\n INNER JOIN `col_curso_unidad` ccu ON uns.`id_uni`=ccu.`id` "
					 + "\n INNER JOIN `col_area_anio` caa ON ccu.`id_niv`=caa.`id_niv`  "
					 + "\n INNER JOIN `col_curso_anio` cua ON cua.`id_caa`=caa.`id` AND cua.`id_cur`=ccu.`id_cur` AND cua.`id_gra`=ccu.`id_gra` "
					 + "\n INNER JOIN `col_curso_aula` cca ON cca.`id_cua`=cua.id  "
					 + "\n INNER JOIN `col_curso_horario` cch ON cch.`id_cca`=cca.`id` " 
					 + "\n INNER JOIN `col_per_uni` cpu ON ccu.`id_cpu`=cpu.`id`   "
					 + "\n INNER JOIN `col_per_uni_det` cpud ON cpud.`id_cpu`=cpu.`id`  AND cpud.`ord` = ccu.`num` "  
					 + "\n LEFT JOIN `col_curso_horario_ses` cchs ON cchs.`id_uns`=uns.`id`  AND cchs.id_cch = cch.id "
					 + "\n WHERE cch.id =:id_cch AND :id_sem BETWEEN cpud.`sem_ini` AND cpud.`sem_fin`  AND  (cchs.`id` IS NOT NULL  AND (cchs.id_uns = uns.id && cchs.id_ccs=:id_sem ) ) "
					 + "\n ) X ORDER BY X.nro"; 
			//esto
			
			Param param = new Param();
			param.put("id_cch", id_cch);
			param.put("id_sem", id_sem);
			
			List<Row> sesiones = sqlUtil.query(sql, param);
			
			for (Row row : sesiones) {
				Integer id_uns = row.getInteger("id");
				String sql2 = "SELECT cst.id id_cst, cts.nom , cst.id_cts FROM col_sesion_tipo cst INNER JOIN cat_tipo_sesion cts ON cts.id= cst.`id_cts` where cst.id_uns = " + id_uns;
				List<Row> tipoSesionList = sqlUtil.query(sql2);
				
				for (Row row2 : tipoSesionList) {
					//lista de subtemas
					
					int id_cts=row2.getInt("id_cts");//ID DEL TIPO DE SESION EXAMEN, CLASE, REPASO
					
					List<Row> grupo_subtema = new ArrayList<>();
					if(EnumTipoSesion.CLASE.getValue()==id_cts || EnumTipoSesion.REPASO.getValue()==id_cts){
						  grupo_subtema = sesionTipoDAO.listarGrupoSubtemaxClase(row2.getInteger("id_cst"));	
					} else if(EnumTipoSesion.EXAMEN.getValue()==id_cts){
						 grupo_subtema = sesionTipoDAO.listarGrupoSubtemaxExamen(row2.getInteger("id_cst"));
					}
					
					
					List<Row> subtemas = new ArrayList<Row>();
					for (Row grupo : grupo_subtema) {

						//lista de subtemas
						List<Row> subtemas_ = subtemaDAO.listarSubtemasxGrupo(grupo.getInteger("id_cgsp"));
						subtemas.addAll(subtemas_);
					
					}
					row2.put("subtemas", subtemas);
				}
				
				row.put("tipoSesionList", tipoSesionList);
				
			}
			
			return sesiones;
			
		}
		
		/**
		 * Horarios por semana para el docente (horario semana)
		 * @param yyyymm
		 * @param dd
		 * @param id_tra
		 * @param id_suc
		 * @param id_niv
		 * @param id_sem
		 * @return
		 */
		public List<Row> listarHorariosxSemana(String yyyymm, String anio_mes_sig, String dd, Integer id_tra, Integer id_suc,Integer id_niv,Integer id_sem,Integer primer_dia, Integer primer_dia_mes_siguiente, Integer ultimo_dia_mes_actual){
			
			Integer dia = Integer.parseInt(dd) - 1;
			
			String sql="SELECT t.extendedProps, t.`start`, t.`end`, t.title, IF(t.sesiones>0,'#81C784' , '#F44336') color,t.fec_ini_vig,t.fec_fin_vig "
					+ "\n FROM ("
					+ "\n SELECT CONCAT(cch.id,'-', cch.id_cca,'-', ccu.id ) AS 'extendedProps',cchp.fec_ini_vig,cchp.fec_fin_vig,"
					
					//+ "\n CONCAT(DATE_FORMAT(CONCAT('" + yyyymm + "-' , " + "IF((dia<" + primer_dia +" AND " + primer_dia + ">1) ,7+" + dia + "," + dia + ")" + "+ dia) , '%Y-%m-%d'),'T',  hora_ini) `start`, " 
					//+ "\n CONCAT(DATE_FORMAT(CONCAT('" + yyyymm + "-' , " + "IF((dia<" + primer_dia +" AND " + primer_dia + ">1) ,7+" + dia + "," + dia + ")"  + "+ dia) , '%Y-%m-%d'),'T', hora_fin) `end`,"
					
					+ "\n CASE WHEN dia + " + dia+"<="+ultimo_dia_mes_actual+" THEN "
					+ "\n CONCAT(DATE_FORMAT(CONCAT('" + yyyymm + "-' , " + "IF((dia<" + primer_dia +" AND " + primer_dia + ">1) ,7+" + dia + "," + dia + ")" + "+ dia) , '%Y-%m-%d'),'T',  hora_ini)  "
					+ "\n ELSE "
					+ "\n CONCAT(DATE_FORMAT(CONCAT('" + anio_mes_sig + "-' ," + (primer_dia - primer_dia_mes_siguiente) +" + dia) , '%Y-%m-%d'),'T',  hora_ini) end `start`, "
					+ "\n CASE WHEN dia + " + dia+"<="+ultimo_dia_mes_actual+" THEN "
					+ "\n CONCAT(DATE_FORMAT(CONCAT('" + yyyymm + "-' , " + "IF((dia<" + primer_dia +" AND " + primer_dia + ">1) ,7+" + dia + "," + dia + ")"  + "+ dia) , '%Y-%m-%d'),'T', hora_fin) "
					+ "\n ELSE "
					+ "\n CONCAT(DATE_FORMAT(CONCAT('" + anio_mes_sig + "-' ," + (primer_dia - primer_dia_mes_siguiente) +" + dia) , '%Y-%m-%d'),'T',  hora_fin) end `end`, "
					+ "\n CONCAT(cur.nom,'\n(', grad.nom,'-', au.secc ,')') title, "
					+ "\n (SELECT COUNT(1) FROM col_curso_horario_ses cchs  WHERE cchs.id_cch = cch.id AND cchs.id_ccs=:id_sem and cch.id_cca=cca.id) sesiones"
					+ "\n FROM col_curso_horario cch  "
					+ "\n INNER JOIN col_curso_horario_pad cchp ON cchp.id = cch.id_cchp "
					+ "\n INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id` "
					+ "\n INNER JOIN `col_aula` au ON au.id=cca.`id_au` "
					+ "\n INNER JOIN `per_periodo` per ON per.id=au.id_per "
					+ "\n INNER JOIN `cat_grad` grad ON grad.id=au.`id_grad` "
					+ "\n INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id` "
					+ "\n INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id "
					+ "\n INNER JOIN col_curso_unidad ccu ON ccu.id_niv =per.id_niv  AND ccu.id_gra= au.id_grad AND ccu.`id_cur` = cua.id_cur " //-- and ccu.id_cpu=1
					+ "\n INNER JOIN col_per_uni cpu ON cpu.id = ccu.id_cpu AND cpu.id_anio=per.id_anio " // -- and cpu.`id_cpa` = cpan.id 
					+ "\n INNER JOIN col_per_uni_det cpud ON cpud.id_cpu = cpu.id AND :id_sem BETWEEN cpud.sem_ini AND cpud.sem_fin  AND ccu.`num`=cpud.`ord`" 
					+ "\n WHERE cca.id_tra =:id_tra AND (:id_suc=0 or per.id_suc=:id_suc) AND per.id_niv=:id_niv  ) t "
					+ "\n WHERE t.start BETWEEN t.fec_ini_vig AND IFNULL(t.fec_fin_vig,'9999-12-31')" ;
			
			Param param = new Param();
			param.put("id_sem", id_sem);
			param.put("id_tra", id_tra);
			param.put("id_suc", id_suc);
			param.put("id_niv", id_niv);
				
			return sqlUtil.query(sql, param);
		}
		
		public List<Row> listarHorariosxProfesor(Integer id_tra,Integer id_anio, Integer id_suc, Integer id_niv, Integer id_gra, Integer anio_act){
			
			String sql="SELECT concat(cch.id,'-', cch.id_cca) as 'extendedProps', CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T',  hora_ini) `start`,"; 
				   sql += " CONCAT(DATE_FORMAT(CONCAT('"+anio_act+"-01-' , 6+ dia) , '%Y-%m-%d'),'T', hora_fin) `end`, concat(cur.nom,'\n(',concat( tra.nom,' ',tra.ape_pat),')') title";
				   sql += " FROM col_curso_horario cch ";
				   sql += " INNER JOIN `col_curso_aula` cca ON cch.`id_cca`=cca.`id`";
				   sql += " INNER JOIN aeedu_asistencia.ges_trabajador tra ON tra.id= cca.id_tra";
				   sql += " INNER JOIN col_curso_anio cua ON cca.`id_cua`=cua.`id`";
				   sql += " INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id";
				   sql += " INNER JOIN per_periodo per ON cua.id_per=per.id";
				   sql += " WHERE  tra.id=? and per.id_anio=?";
					
			if(id_suc!=null)
				   sql += " and per.id_suc="+id_suc;
			if(id_niv!=null)
				   sql += " and per.id_niv="+id_niv;
			if(id_gra!=null)
				   sql += " and cua.id_gra="+id_gra;

			return sqlUtil.query(sql, new Object[] { id_tra, id_anio});
		}
		
		//proceso q se uso una sola vez
		@Transactional
		public void actualizarPadres() throws Exception{
			String sql = "SELECT DISTINCT per.id_anio, cca.id_au FROM col_curso_horario cch"
					+ " INNER JOIN col_curso_aula cca ON cch.id_cca = cca.id"
					+ " INNER JOIN col_curso_anio cua ON cca.id_cua = cua.id"
					+ " INNER JOIN per_periodo per ON per.id = cua.id_per where per.id_anio=3";
			
			List<Row> list = sqlUtil.query(sql);
			
			for (Row row : list) {
				int id_anio = row.getInt("id_anio");
				int id_au = row.getInt("id_au");
				
				String sqlPadre = "insert into col_curso_horario_pad(id_anio, id_au, fec_ini_vig, est, usr_ins, fec_ins) values(?,?,?,?,?,?)";
				jdbcTemplate.update(sqlPadre, 
						id_anio,
						id_au,
					//	FechaUtil.toDate("15/02/2019", "dd/MM/yyyy"),
						"a",
						1,
						new java.util.Date());
						
				int id_pad = jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
				
				/**
				 * hijos
				 */
				
				sql ="update col_curso_horario cch "
						+ "\n inner join col_curso_aula cca ON cch.id_cca = cca.id"
						+ "\n INNER JOIN col_curso_anio cua ON cca.id_cua = cua.id"
						+ "\n INNER JOIN per_periodo per ON per.id = cua.id_per "
						+ "\n set cch.id_cchp=? where cch.id_anio=? and cca.id_au=? ";
				
				sqlUtil.update(sql, new Object[]{id_pad,id_anio, id_au});
				
			}
			
		}		
}
