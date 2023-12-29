package com.sige.mat.dao;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.AlumnoDescuentoDAOImpl;


/**
 * Define m�todos DAO operations para la entidad alumno_descuento.
 * @author MV
 *
 */
@Repository
public class AlumnoDescuentoDAO extends AlumnoDescuentoDAOImpl{
	final static Logger logger = Logger.getLogger(AlumnoDescuentoDAO.class);
	/**
	 * Todos los descuentos que existen en la bd
	 * @param id_mat
	 * @return
	 */
	public Map<String,Object> descuentosPorMatricula( Integer id_mat) {
		
		String sql = "SELECT g.nom nivel, gr.nom grado, au.secc seccion,  m.id, CONCAT(per.`ape_pat`,' ', per.ape_mat,' ', per.nom) label , mes.monto, "
				+ " mes.descuento descuento_pronto_pago, mes.desc_banco "
				+ " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join col_persona per on a.id_per=per.id"
				+ " inner join per_periodo p on m.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv"
				+ " inner join mat_conf_mensualidad mes on mes.id_per = m.id_per"
				+ " inner join cat_grad gr on gr.id=m.id_gra"
				+ " inner join col_aula au on au.id=m.id_au_asi"
				+ " WHERE  m.id=" + id_mat
				+ " order by per.ape_pat asc";
		
		return jdbcTemplate.queryForMap(sql);

	} 	
	
	public List<Map<String,Object>> listAlumnos( Integer id_anio) {
		
		String sql = "SELECT g.nom nivel, concat(gr.nom,'-', au.secc) as grado, m.id id_mat, d.id, concat(a.`ape_pat`,' ', a.ape_mat,' ', a.nom) as alumno , d.mensualidad,  d.mensualidad_bco, d.motivo,"
				+ " mes.monto, mes.descuento descuento_pronto_pago, mes.desc_banco, d.descuento "
				+ " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join fac_alumno_descuento d on d.id_mat=m.id"
				+ " inner join per_periodo p on m.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv"
				+ " inner join mat_conf_mensualidad mes on mes.id_per = m.id_per"
				+ " inner join cat_grad gr on gr.id=m.id_gra"
				+ " inner join col_aula au on au.id=m.id_au_asi"
				+ " WHERE  p.id_anio=" + id_anio
				+ " order by a.ape_pat asc";
		
		return jdbcTemplate.queryForList(sql);

	} 	
	
	public Map<String,Object> getAlumno(Integer id) {
		
		String sql = "SELECT g.nom nivel, gr.nom as grado, au.secc seccion, m.id id_mat, d.id, concat(a.`ape_pat`,' ', a.ape_mat,' ', a.nom) as alumno , d.mensualidad,d.mensualidad_bco,d.motivo,"
				+ " mes.monto, mes.descuento descuento_pronto_pago, mes.desc_banco, d.descuento "
				+ " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join fac_alumno_descuento d on d.id_mat=m.id"
				+ " inner join per_periodo p on m.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv"
				+ " inner join mat_conf_mensualidad mes on mes.id_per = m.id_per"
				+ " inner join cat_grad gr on gr.id=m.id_gra"
				+ " inner join col_aula au on au.id=m.id_au_asi"
				+ " WHERE  d.id=" + id
				+ " order by a.ape_pat asc";
		
		List< Map<String,Object>> list =jdbcTemplate.queryForList(sql);
		
		if (list.size()==0)
			return null;
		else
			return list.get(0);

	} 	
	
	public Map<String,Object> getAlumnoPorMatricula(Integer id_mat) {
		
		String sql = "SELECT g.nom nivel, gr.nom as grado, au.secc seccion, m.id id_mat, d.id, concat(a.`ape_pat`,' ', a.ape_mat,' ', a.nom) as alumno , d.mensualidad,d.motivo,"
				+ " mes.monto, mes.descuento descuento_pronto_pago, mes.desc_banco, d.descuento "
				+ " FROM mat_matricula m"
				+ " inner join alu_alumno a on m.id_alu=a.id"
				+ " inner join fac_alumno_descuento d on d.id_mat=m.id"
				+ " inner join per_periodo p on m.id_per=p.id"
				+ " inner join ges_servicio g on g.id=p.id_srv"
				+ " inner join mat_conf_mensualidad mes on mes.id_per = m.id_per"
				+ " inner join cat_grad gr on gr.id=m.id_gra"
				+ " inner join col_aula au on au.id=m.id_au_asi"
				+ " WHERE m.id=" + id_mat
				+ " order by a.ape_pat asc";
		
		List< Map<String,Object>> list =jdbcTemplate.queryForList(sql);
		
		if (list.size()==0)
			return null;
		else
			return list.get(0);

	} 	
}
