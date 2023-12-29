package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.MarcacionNotaDAOImpl;


/**
 * Define m�todos DAO operations para la entidad marcacion_nota.
 * @author MV
 *
 */
@Repository
public class MarcacionNotaDAO extends MarcacionNotaDAOImpl{
	final static Logger logger = Logger.getLogger(MarcacionNotaDAO.class);

	public List<Map<String,Object>> Reporte_Notas(Integer id_suc, Integer id_niv,String anio,Integer id_gra ) {
		String sql = "SELECT CONCAT(a.`ape_pat`,' ',a.`ape_mat`,' ',a.`nom`) AS 'Alumno', res.`notafinal`, res.res, g.nom grado, an.nom anio"
				+ " FROM alu_alumno a, eva_matr_vacante m,  eva_matr_vacante_resultado res, eva_evaluacion_vac eva,"
				+ " per_periodo p, ges_servicio ser, ges_sucursal suc, cat_nivel n, cat_grad g,col_anio an"
				+ " WHERE a.id=m.id_alu AND m.id=res.id_mat_vac AND m.id_eva=eva.id AND eva.id_per=p.id AND p.id_srv=ser.id AND ser.id_suc=suc.`id`"
				+ " AND n.nom=ser.nom AND g.id_nvl=n.id AND m.id_gra=g.id AND p.id_anio=an.id AND suc.id="+id_suc+" AND n.id="+id_niv+" "
				+ "AND an.nom='"+anio+"' AND g.id="+id_gra;
		List<Map<String,Object>> RepNota = jdbcTemplate.queryForList(sql);			
		return RepNota;
	}
	
	public List<Map<String,Object>> Nivel_Local(Integer id_suc) {
		String sql = "SELECT * FROM ges_servicio ser, `cat_nivel` n WHERE ser.nom=n.nom AND ser.`id_suc`="+id_suc;
		List<Map<String,Object>> Nivel = jdbcTemplate.queryForList(sql);			
		return Nivel;
	}
	

	
	
}
