package com.sige.mat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AreaCoordinadorDAOImpl;
import com.tesla.colegio.model.AreaCoordinador;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad area_coordinador.
 * 
 * @author MV
 *
 */
@Repository
public class AreaCoordinadorDAO extends AreaCoordinadorDAOImpl {
	final static Logger logger = Logger.getLogger(AreaCoordinadorDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	

	// lista para una grilla de coordinadores por area
	public List<Row> listaAreaxNivel(Integer id_anio, Integer id_gir, Integer id_niv) {
		//String sql = "select a.id id_area, a.nom, ac1.id  'inicial', ac2.id  'primaria', ac3.id  'secundaria'"
		String sql = "select DISTINCT a.id id_area, a.nom, ac1.id  coor_area"
				+ "\n,concat(tra1.ape_pat,' ',tra1.ape_mat,' ',tra1.nom) nom1 " 
				//+ "\n,concat(tra2.ape_pat,' ',tra2.nom) nom2 "
				//+ "\n,concat(tra3.ape_pat,' ',tra3.nom) nom3 " 
			//	+ "\n,aa1.id area_nivel1 " 
				//+ "\n,aa2.id area_nivel2 "
				//+ "\n,aa3.id area_nivel3 " 
				+ "\nfrom cat_area a "
				+ "\ninner join aca_dcn_area dca ON dca.id_are=a.id"
				+ "\ninner join aca_dcn_nivel dcniv ON dcniv.id=dca.id_dcniv"
				+ "\ninner join col_disenio_curricular dcu ON dcu.id=dcniv.id_dcn"
				+ "\ninner join col_conf_anio_acad_dcn conf ON dcu.id=conf.id_dcn"
				+ "\ninner join col_area_anio aa1 on aa1.id_anio=? and aa1.id_niv=? and aa1.id_gir=? and aa1.id_area = a.id and aa1.id_adc=dca.id "
				+ "\nleft join (col_area_coordinador ac1 inner join ges_trabajador tra1 ON ac1.id_tra= tra1.id AND tra1.est='A' AND ac1.id_anio=? AND ac1.id_gir=? AND ac1.id_niv=?) on ac1.id_area = a.id  "
				
				/*+ "\nleft join (col_area_coordinador ac2 left join ges_trabajador tra2 on ac2.id_tra= tra2.id and tra2.est='A') on ac2.id_area = a.id and ac2.id_niv=2 "
				+ "\nleft join col_area_anio aa2 on aa2.id_anio=? and aa2.id_niv=2 and aa2.id_area = a.id "
				+ "\nleft join (col_area_coordinador ac3 left join ges_trabajador tra3 on ac3.id_tra= tra3.id and tra3.est='A') on ac3.id_area = a.id and ac3.id_niv=3 "
				+ "\nleft join col_area_anio aa3 on aa3.id_anio=? and aa3.id_niv=3 and aa3.id_area = a.id "*/
				+ "\nwhere a.est='A'  AND conf.id_anio=? " + "\nORDER BY a.nom";

		return sqlUtil.query(sql, new Object[] { id_anio, id_niv, id_gir,id_anio, id_gir, id_niv,id_anio });
	}
	
		public List<Row> listCursosCoordinadores(int id_anio) {
			List<Row> cursos=listaCursoxNivel(id_anio); //32 cursos del 2019
			List<AreaCoordinador> coordinadores = listFullByParams(new Param("cac.id_anio", id_anio),new String[] { "tra.ape_pat, tra.ape_mat" });
			for (Row rowCurso : cursos) {
				for (AreaCoordinador areaCoordinador : coordinadores) {
					
					if( areaCoordinador.getId_cur()==rowCurso.getInt("id_cur")){
						
						Object objCoordinadoresxCurso = rowCurso.get("coordinadores");
						
						if(objCoordinadoresxCurso == null){
							List<AreaCoordinador> coordinadoresXcurso = new ArrayList<AreaCoordinador>();
							coordinadoresXcurso.add(areaCoordinador);
							rowCurso.put("coordinadores", coordinadoresXcurso);
						}else{
							List<AreaCoordinador> coordinadoresXcurso = (List<AreaCoordinador>)objCoordinadoresxCurso;
							coordinadoresXcurso.add(areaCoordinador);
							rowCurso.put("coordinadores", coordinadoresXcurso);
							
						}
						
						//rowCurso.put("coordinadores", value);
					}
				}
			}
			
			return cursos;
		
		}
		
	// lista para una grilla de coordinadores por curso
		public List<Row> listaCursoxNivel(int id_anio) {
			 
			String sql ="SELECT DISTINCT cur.`id` id_cur, cur.`nom` curso, caa.`id_area`, a.`nom` nom_area ,"
					+ " (SELECT COUNT(1) FROM `col_curso_anio` cca1  INNER JOIN `per_periodo` p1 ON cca1.`id_per`=p1.`id` AND p1.`id_anio`=3 AND p1.`id_niv`=1 WHERE cca1.id_cur=cur.id and cca1.id_caa = caa.id) inicial,"
					+ " (SELECT COUNT(1) FROM `col_curso_anio` cca2  INNER JOIN `per_periodo` p2 ON cca2.`id_per`=p2.`id` AND p2.`id_anio`=3 AND p2.`id_niv`=2 WHERE cca2.id_cur=cur.id and cca2.id_caa = caa.id) primaria,"
					+ " (SELECT COUNT(1) FROM `col_curso_anio` cca3  INNER JOIN `per_periodo` p3 ON cca3.`id_per`=p3.`id` AND p3.`id_anio`=3 AND p3.`id_niv`=3 WHERE cca3.id_cur=cur.id and cca3.id_caa = caa.id) secundaria"
					+ " FROM `cat_curso` cur "
					+ " INNER JOIN `col_curso_anio` cca ON cca.`id_cur`=cur.id"
					+ " INNER JOIN `col_area_anio` caa ON cca.`id_caa`=caa.id "
					+ " INNER JOIN `cat_area` a ON caa.`id_area`=a.`id`"
					+ " WHERE caa.`id_anio`=:id_anio ORDER BY a.`nom`, cur.`nom`";
			Param param = new Param();
			param.put("id_anio", id_anio);
			
			

			return sqlUtil.query(sql, param);
		}
	
	

	public List<Row> listaCursos(int id_anio, int id_tra, int id_niv, int id_gra, int id_cur) {
		
		String sql = "SELECT distinct ca.id_cur id, cc.nom value, c.id_niv as aux1 "
				+ " FROM col_area_coordinador c  "
				+ "\ninner join col_area_anio aa on aa.id_area = c.id_area and aa.id_anio=? AND aa.id_niv= c.id_niv "
				+ "\ninner join per_periodo per on per.id_anio = aa.id_anio and c.id_anio=per.id_anio  "
				+ "\ninner join col_curso_anio ca on ca.id_per = per.id and ca.id_caa = aa.id "
				+ "\ninner join cat_curso cc on cc.id = ca.id_cur and c.id_cur=cc.id " 
				+ "\nWHERE per.id_anio=? and id_tra=? and (?=0 or c.id_niv=?) and ca.id_gra=? and (?=0 or ca.id_cur=?)"
				+ "\norder by cc.nom";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_anio, id_anio, id_tra, id_niv, id_niv, id_gra, id_cur,id_cur });

	}
	
	public List<Row> listaCoordinadores() {
		
		String sql = "SELECT distinct  tra.id id,  u.`login` aux1, CONCAT(per.`ape_pat`,' ', per.ape_mat,', ', per.nom) value "
				+ "\nFROM `seg_usuario` u "
				+ "\ninner join `seg_usuario_rol` ur on ur.id_usr = u.id "
				+ "\nINNER JOIN ges_trabajador tra ON u.`id_tra`=tra.id "
				+ "\nINNER JOIN col_persona per ON tra.`id_per`=per.id "
				+ "\nwhere u.est='A' and tra.est='A' and ur.id_rol=?";
		
		return sqlUtil.query(sql, new Object[]{Constante.ROL_COORDINADOR_AREA});

	}
	

	/**
	 * Lisdta de usuarios que tienen perfil coordinador y que ademas ense�an
	 * @return
	 */
	public List<Row> coordinadoresDisponibles(Integer id_anio,Integer id_niv,Integer id_cur) {
		
		String sql = "SELECT distinct  tra.id id,  u.`login` aux1, CONCAT(tra.`ape_pat`,' ', tra.ape_mat,', ', tra.nom) value from col_curso_anio cua ";
		sql +="\n inner join per_periodo p on p.id = cua.id_per";
		sql +="\n inner join col_curso_aula cca on cca.id_cua = cua.id";
		sql +="\n inner JOIN aeedu_asistencia.ges_trabajador tra ON cca.`id_tra`=tra.id ";
		sql +="\n inner join `seg_usuario` u on u.id_tra = tra.id ";
		sql +="\n inner join `seg_usuario_rol` ur on ur.id_usr = u.id ";
		sql +="\n where p.id_anio=:id_anio and cua.id_cur=:id_cur and p.id_niv=:id_niv and u.est='A' and tra.est='A' and ur.id_rol=:id_rol";
		
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_niv", id_niv);
		param.put("id_cur", id_cur);
		param.put("id_rol", Constante.ROL_COORDINADOR_AREA);
		
		return sqlUtil.query(sql, param);

	}
	
	public List<Row> listaNiveles(Integer id_tra) {
		
		String sql = "SELECT distinct ac.id_niv id, n.nom value "
				+ "\nFROM col_area_coordinador ac "
				+ "\ninner join cat_nivel n on n.id = ac.id_niv"
				+ "\nwhere id_tra=?";
		
		return sqlUtil.query(sql, new Object[]{id_tra});

	}

	public List<Row> listaGrados(Integer id_anio, Integer id_tra,Integer id_niv) {
		
		String sql = "SELECT  distinct id_gra id, gr.nom value FROM col_curso_anio ca "
				+ "\ninner join per_periodo per on per.id_anio= ? and per.id= ca.id_per "
				+ "\ninner join cat_grad gr on gr.id = ca.id_gra "
				+ "\ninner join col_area_anio caa  on caa.id = ca.id_caa "
				//+ "\nwhere caa.id_area  in  (SELECT ac.id_area FROM col_area_coordinador ac inner join cat_nivel n on n.id = ac.id_niv where id_tra=? ) "
				+ "\nwhere ca.id_cur  in  (SELECT ac.id_cur FROM col_area_coordinador ac inner join cat_nivel n on n.id = ac.id_niv where id_tra=? ) "
				+ "\nand gr.id_nvl=? order by id_gra";
		
		return sqlUtil.query(sql, new Object[]{id_anio, id_tra, id_niv});

	}

	
	public List<Row> listaAreas(Integer id_tra) {
		
		String sql = "SELECT distinct ac.id_niv , n.nom "
				+ "\nFROM col_area_coordinador ac "
				+ "\ninner join cat_nivel n on n.id = ac.id_niv"
				+ "\nwhere id_tra=?";
		
		return sqlUtil.query(sql, new Object[]{id_tra});

	}
	
	
}
