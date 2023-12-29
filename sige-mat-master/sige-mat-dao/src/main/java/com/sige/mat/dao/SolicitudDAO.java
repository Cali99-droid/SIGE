package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.common.enums.EnumSituacionFinal;
import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SolicitudDAOImpl;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Solicitud;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad solicitud.
 * @author MV
 *
 */
@Repository
public class SolicitudDAO extends SolicitudDAOImpl{
	final static Logger logger = Logger.getLogger(SolicitudDAO.class);

	@Autowired
	private AnioDAO anioDAO;

	@Autowired
	private SQLUtil sqlUtil;

	/*Consulta de alumnos por nombre y apellidos*/
	public List<Row> consultarAlumnos(String alumno, Integer id_anio, Integer id_suc_ori, Integer id_suc_des,String tipo) {

		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt(anio.getNom()) -1 );
		Anio anioAnterior = anioDAO.getByParams(param);
		int id_anio_anterior = anioAnterior.getId();
		 
	
		//matriculados 
		List<Row> list = null;
		StringBuilder sql = new StringBuilder("");
		if (!tipo.equals("NC")){
			sql.append("select n.nom nivel, g.nom grado, alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, m.id id_mat,null id_res,'M' tipo");
			sql.append("\n from mat_matricula m ");
			sql.append("\n inner join per_periodo per on per.id=m.id_per and per.id_tpe=1");
			sql.append("\n inner join cat_nivel n on n.id=m.id_niv");
			sql.append("\n inner join cat_grad g on g.id = m.id_gra");
			sql.append("\n inner join alu_alumno alu on m.id_alu=alu.id");
			sql.append("\n inner join col_persona p on p.id=alu.id_per");
			sql.append("\n where per.id_anio=:id_anio and per.id_suc=:id_suc ");
			sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%' ");
			
			sql.append("\n union");
		}
		
	/*	if (tipo.equals("NCAS")){
			sql.append("select n.nom nivel, g.nom grado, alu.id id_alu, alu.nro_doc, alu.ape_pat, alu.ape_mat, alu.nom, m.id id_mat,null id_res,'M' tipo");
			sql.append("\n from mat_matricula m ");
			sql.append("\n inner join per_periodo per on per.id=m.id_per");
			sql.append("\n inner join cat_nivel n on n.id=m.id_niv");
			sql.append("\n inner join cat_grad g on g.id = m.id_gra");
			sql.append("\n inner join alu_alumno alu on m.id_alu=alu.id");
			sql.append("\n where per.id_anio=:id_anio and per.id_suc=:id_suc ");
			sql.append("\n and upper(CONCAT(alu.ape_pat,' ',alu.ape_mat, ' ', alu.nom)) LIKE '%" + alumno +"%' ");
			
			sql.append("\n union");
		}*/
		
		//reservados q aun no se matriculan
		sql.append("\n select n.nom nivel, g.nom grado, alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, null id_mat , r.id id_res, 'R' tipo");
		sql.append("\n from mat_reserva r ");
		sql.append("\n inner join per_periodo per on per.id=r.id_per");
		sql.append("\n inner join cat_nivel n on n.id=r.id_niv");
		sql.append("\n inner join cat_grad g on g.id = r.id_gra");
		sql.append("\n inner join alu_alumno alu on r.id_alu=alu.id");
		sql.append("\n inner join col_persona p on p.id=alu.id_per");
		sql.append("\n where per.id_anio=:id_anio and per.id_suc=:id_suc ");
		sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%' ");
		sql.append("\n and r.id_alu not in (select m.id_alu from mat_matricula m ");
		sql.append("\n inner join per_periodo peri on peri.id= m.id_per ");
		sql.append("\n where peri.id_anio=:id_anio and peri.id_tpe=1)");
		
		if(tipo.equals("AS")){
			sql.append("\n and r.id_alu in (select ma.id_alu from mat_matricula ma ");
			sql.append("\n inner join col_aula aa on aa.id= ma.id_au_asi ");
			sql.append("\n inner join per_periodo pa on pa.id= aa.id_per ");
			sql.append("\n where pa.id_anio=:id_anio_anterior and pa.id_tpe=1) ");
		}
		
		if(tipo.equals("NSAS")){
			sql.append("\n union");
			sql.append("\n select n.nom nivel, g.nom grado, alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, null id_mat , r.id id_res, 'R' tipo");
			sql.append("\n from mat_reserva r ");
			sql.append("\n inner join per_periodo per on per.id=r.id_per");
			sql.append("\n inner join cat_nivel n on n.id=r.id_niv");
			sql.append("\n inner join cat_grad g on g.id = r.id_gra");
			sql.append("\n inner join alu_alumno alu on r.id_alu=alu.id");
			sql.append("\n inner join col_persona p on p.id=alu.id_per");
			sql.append("\n where per.id_anio=:id_anio and per.id_suc=:id_suc ");
			sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%' ");
			sql.append("\n and r.id_alu not in (select m.id_alu from mat_matricula m ");
			sql.append("\n inner join per_periodo p on p.id= m.id_per ");
			sql.append("\n where p.id_anio=:id_anio and p.id_tpe=1)");
			sql.append("\n and r.id_alu in (select ma.id_alu from mat_matricula ma ");
			sql.append("\n inner join col_aula aa on aa.id= ma.id_au_asi ");
			sql.append("\n inner join per_periodo pa on pa.id= aa.id_per and pa.id_tpe=1");
			sql.append("\n where pa.id_anio=:id_anio_anterior) ");
		}
		
		sql.append("\n union");

		//antiguos que todavia no se matriculan ni tampoco hacen reserva
		sql.append("\n select n.nom nivel,g.nom grado, alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, m.id id_mat,null id_res,'A' tipo");
		sql.append("\n from mat_matricula m ");
		sql.append("\n inner join col_aula au on au.id = m.id_au_asi");
		sql.append("\n inner join cat_nivel n on n.id=m.id_niv");
		sql.append("\n inner join per_periodo per on per.id=au.id_per");
		sql.append("\n inner join cat_grad g on g.id = m.id_gra");
		sql.append("\n inner join alu_alumno alu on m.id_alu=alu.id");
		sql.append("\n inner join col_persona p on p.id=alu.id_per");
		sql.append("\n where per.id_anio=:id_anio_anterior and per.id_suc=:id_suc and per.id_tpe=1");
		sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%' ");
		sql.append("\n and m.id_alu not in (select alu.id ");
		sql.append("\n from mat_matricula m ");
		sql.append("\n inner join per_periodo per on per.id=m.id_per");
		sql.append("\n inner join alu_alumno alu on m.id_alu=alu.id");
		sql.append("\n where per.id_anio=:id_anio and per.id_tpe=1");
		sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%') ");
		sql.append("\n and m.id_sit in (:id_sitAP,:id_sitRR,:id_sitDE) ");
		sql.append("\n and m.id_alu not in (select alu.id ");
		sql.append("\n from mat_reserva r ");
		sql.append("\n inner join per_periodo per on per.id=r.id_per");
		sql.append("\n inner join alu_alumno alu on r.id_alu=alu.id");
		sql.append("\n where per.id_anio=:id_anio ");
		sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%') ");
		
		sql.append("\n union");
		
		//tienen solicitud pero aun no han cambiado su matricula o (reserva)
		sql.append("\n select '' nivel, '' grado, alu.id id_alu, p.nro_doc, p.ape_pat, p.ape_mat, p.nom, null id_mat , null id_res, 'S' tipo");
		sql.append("\n from mat_solicitud s ");
		sql.append("\n inner join alu_alumno alu on s.id_alu=alu.id");
		sql.append("\n inner join col_persona p on p.id=alu.id_per");
		sql.append("\n and upper(CONCAT(p.ape_pat,' ',p.ape_mat, ' ', p.nom)) LIKE '%" + alumno +"%' ");
		sql.append("\n where s.id_anio=:id_anio and s.id_suc_des=:id_suc and s.est='A'");
		
		param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_suc", id_suc_ori);
		param.put("id_anio_anterior", id_anio_anterior);
		param.put("id_sitAP", EnumSituacionFinal.APROBADO.getValue());
		param.put("id_sitRR", EnumSituacionFinal.REQUIERE_RECUPERACION_PEDAGOGICA.getValue());
		param.put("id_sitDE", EnumSituacionFinal.DESAPROBADO.getValue());
		
		//QUE NO TENGA SOLICITUD EN EL LOCAL QUE BUSCA
		String sql_final = "select t.* from (" + sql.toString() + ")t "
				+ "\n where t.id_alu not in (select sol.id_alu from mat_solicitud sol where sol.id_suc_or=" + id_suc_ori +" and sol.est='A' and sol.id_anio=:id_anio ) "
				+ "\n order by t.ape_pat, t.ape_mat, t.nom";
		
		list = sqlUtil.query(sql_final,param);
		
		return list;
	}	
	
	public Solicitud detalle(Integer id_alu, Integer id_suc,Integer id_anio) throws Exception{
		
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_alu", id_alu);
		param.put("id_suc_des", id_suc);
		param.put("est", "A");
		
		Solicitud solicitud = getByParams(param);
		
		if (solicitud!=null && ( solicitud.getTipo()==null || solicitud.getTipo().equals("")))
			throw new Exception ("Solicitud no tiene un estado valido");

		return solicitud;
		
		/*
		if (solicitud.getTipo().equals("M")){
			String sql = "select sol.* "
					+ " from mat_solicitud sol"
					+ " inner join mat_matricula mat on mat.id= sol.id_alu"
					+ " where sol.id_suc_des=:id_suc ";
			
		}else  if (solicitud.getTipo().equals("R")){
			
		}else  if (solicitud.getTipo().equals("A")){
			
		}else
			throw new Exception ("Solicitud no tiene un estado valido");
		
	*/

	
	}

	public List<Row> datosAlumno(Integer id_alu, Integer id_anio) {
		
		Param param = new Param();
		param.put("id_anio", id_anio);
		param.put("id_alu", id_alu);
		param.put("est", "A");
		
		Solicitud solicitud = getByParams(param);
		
		if (solicitud.getTipo().equals("M") || solicitud.getTipo().equals("A")){

			if (solicitud.getTipo().equals("A")){
				Anio anio = anioDAO.get(id_anio);
				param = new Param();
				param.put("nom", Integer.parseInt(anio.getNom()) -1 );
				Anio anioAnterior = anioDAO.getByParams(param);
				id_anio = anioAnterior.getId();
			}
			
			
			String sql = "SELECT mat.id, mat.id_fam,CONCAT (alu.`ape_pat`,' ', alu.`ape_mat`,' ', alu.`nom`) alumno, alu.nro_doc, "
					+ "\n concat(niv.`nom`,' - ', gra.`nom`,' - ', au.`secc`) aula, niv.`nom` nivel, gra.`nom` grado, "
					+ "\n au.`secc` seccion, IF(id_gen=0,'FEMENINO','MASCULINO') sexo, alu.`celular`,  "
					+ "\n TIMESTAMPDIFF(YEAR, alu.`fec_nac`, CURDATE()) AS edad, alu.id id_alu"
					+ "\n FROM mat_solicitud s "
					+ "\n INNER JOIN mat_matricula mat ON mat.`id_alu`=s.id_alu"
					+ "\n INNER JOIN `col_aula` au ON mat.`id_au`=au.`id` "
					+ "\n INNER JOIN `per_periodo` p ON p.id=au.id_per "
					+ "\n INNER JOIN `alu_alumno` alu ON mat.`id_alu`=alu.`id`"
					+ "\n INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.`id`"
					+ "\n INNER JOIN `cat_nivel` niv ON gra.`id_nvl`=niv.`id`"
					+ "\n WHERE p.id_anio=? and s.id_alu=?";
			
			Object[] params = new Object[]{id_anio, id_alu};
 			return sqlUtil.query(sql,params);	

		}
		
		return null;
	}

	public List<Row> resumenSolicitudes(Integer id_anio){
		
		String sql="SELECT "
				+ "\n a.id id_alu, "
				+ "\n concat(a.ape_pat, ' ',a.ape_pat, ', ',a.nom) alumno, "
				+ "\n s.fec_ins,  m.id, p.id , "
				+ "\n n.nom nivel, "
				+ "\n g.nom grado, "
				+ "\n au.secc seccion, "
				+ "\n ori.nom local_origen, "
				+ "\n des.nom local_destino "
				+ "\n from mat_solicitud s "
				+ "\n inner join  alu_alumno a on s.id_alu=a.id "
				+ "\n inner join  mat_matricula m on s.id_alu=m.id_alu " 
				+ "\n inner join  col_aula au on au.id =m.id_au "
				+ "\n inner join  cat_grad g on g.id =au.id_grad "
				+ "\n inner join per_periodo p on p.id = m.id_per "
				+ "\n inner join cat_nivel n on n.id =p.id_niv "
				+ "\n inner join ges_sucursal des on des.id = s.id_suc_des "
				+ "\n inner join ges_sucursal ori on ori.id = s.id_suc_or "
				+ "\n where s.id_anio=? and p.id_anio=s.id_anio order by s.fec_ins";
						
		return sqlUtil.query(sql,new Object[]{id_anio});
	}
}
