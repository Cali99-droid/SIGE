package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AreaAnioDAOImpl;
import com.tesla.colegio.model.Anio;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Define m�todos DAO operations para la entidad area_anio.
 * @author MV
 *
 */
@Repository
public class AreaAnioDAO extends AreaAnioDAOImpl{
	
	@Autowired
	private AnioDAO anioDAO;
	
	@Autowired
	private SQLUtil sqlUtil;
	
	@Transactional
	public Integer clonarAnio(Integer id_anio) throws Exception{
		
		//consultar A�o anterior
		Anio anio = anioDAO.get(id_anio);
		Param param = new Param();
		param.put("nom", Integer.parseInt(anio.getNom()) -1 );
		
		Anio anioAnterior = anioDAO.getByParams(param);
		
		if (anioAnterior==null)
			throw new Exception("A�o anterior no existe");
		
		String sql = "INSERT INTO col_area_anio(id_anio,id_niv,id_area,ORD,est,usr_ins,fec_ins)";
		sql = sql + "\n select ?, id_niv,id_area,ORD, 'A',1, now() from col_area_anio aa";
		sql = sql + "\n where aa.id_anio=? and aa.est='A'";
		
		Integer registrosClonados = sqlUtil.update(sql, new Object[]{id_anio, anioAnterior.getId()});
		
		return registrosClonados;

	}
	
	/**
	 * Obtengo el id de curso area anio, a partir del curso
	 * @param id_gra
	 * @param id_cur
	 * @param id_anio
	 * @return
	 */
	public List<Row> obtenerDatos(Integer id_gra, Integer id_cur, Integer id_anio){
		
		String sql = "SELECT DISTINCT caa.id FROM `col_area_anio` caa INNER JOIN `cat_area` a ON caa.`id_area`=a.`id` INNER JOIN `col_curso_anio` cca ON cca.id_caa=caa.`id` WHERE cca.`id_gra`=? AND cca.`id_cur`=? AND caa.`id_anio`=?";
		
		List<Row> area_anio = sqlUtil.query(sql, new Object[]{ id_gra, id_cur,id_anio});
		
		return area_anio;

	}
	
	public List<Row> listarAreasDCN(Integer id_gra, Integer id_anio, Integer id_gir){
		
		String sql = "SELECT are.nom area, are.id, adc.id id_adc, caa.id id_area_anio, caa.est, are.id id_area, caa.id_tca, caa.id_pro_per, caa.id_pro_anu \n" + 
				"FROM `aca_dcn_area` adc INNER JOIN `aca_dcn_nivel` dcniv ON adc.`id_dcniv`=dcniv.`id`\n" + 
				"INNER JOIN col_conf_anio_acad_dcn adcn ON dcniv.`id_dcn`=adcn.`id_dcn`\n" + 
				"INNER JOIN cat_area are ON adc.`id_are`=are.id\n" + 
				"INNER JOIN cat_nivel niv ON dcniv.`id_niv`=niv.id\n" + 
				"INNER JOIN cat_grad gra ON gra.id_nvl=niv.id\n" + 
				"LEFT JOIN `col_area_anio` caa ON adc.`id_are`=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` \n" + 
				"AND caa.`id_anio`=adcn.id_anio AND caa.`id_adc`=adc.`id` AND caa.`id_gra`="+id_gra+" AND caa.id_gir="+id_gir+"\n" + 
				"WHERE adcn.`id_anio`=? AND gra.`id`=?";
		
		List<Row> areas = sqlUtil.query(sql, new Object[]{ id_anio, id_gra});
		
		return areas;

	}
	
	public List<Row> listarAreasDCNxGrado(Integer id_gra, Integer id_anio, Integer id_gir){
		
		String sql = "SELECT are.nom area, are.id, adc.id id_adc, caa.id id_area_anio, caa.est, are.id id_area, pro_per.nom pro_per, pro_anu.nom pro_anu, tca.nom cal, adc.ord, gra.nom grado \n" + 
				"FROM `aca_dcn_area` adc INNER JOIN `aca_dcn_nivel` dcniv ON adc.`id_dcniv`=dcniv.`id`\n" + 
				"INNER JOIN col_conf_anio_acad_dcn adcn ON dcniv.`id_dcn`=adcn.`id_dcn`\n" + 
				"INNER JOIN cat_area are ON adc.`id_are`=are.id\n" + 
				"INNER JOIN cat_nivel niv ON dcniv.`id_niv`=niv.id\n" + 
				"INNER JOIN cat_grad gra ON gra.id_nvl=niv.id\n" + 
				"INNER JOIN `col_area_anio` caa ON adc.`id_are`=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` \n" + 
				"AND caa.`id_anio`=adcn.id_anio AND caa.`id_adc`=adc.`id` AND caa.`id_gra`="+id_gra+"\n" + 
				"INNER JOIN cat_tipo_calificacion tca ON caa.id_tca=tca.id "+
				"INNER JOIN cat_tipo_promedio_aca pro_per ON caa.id_pro_per=pro_per.id "+
				"INNER JOIN cat_tipo_promedio_aca pro_anu ON caa.id_pro_anu=pro_anu.id "+
				"WHERE adcn.`id_anio`=? AND gra.`id`=? and caa.est='A' AND caa.id_gir=?  ORDER BY adc.ord ";
		
		List<Row> areas = sqlUtil.query(sql, new Object[]{ id_anio, id_gra, id_gir});
		
		return areas;

	}
	
	public List<Row> listarAreasDCNxGradoCombo(Integer id_gra, Integer id_anio, Integer id_tra, Integer id_gir){
		
		String sql = "SELECT DISTINCT caa.id, are.nom value, adc.id aux1, caa.id id_area_anio, caa.est, are.id id_area \n"; 
				sql += "FROM `aca_dcn_area` adc INNER JOIN `aca_dcn_nivel` dcniv ON adc.`id_dcniv`=dcniv.`id`\n" ; 
				sql += "INNER JOIN col_conf_anio_acad_dcn adcn ON dcniv.`id_dcn`=adcn.`id_dcn`\n" ; 
				sql += "INNER JOIN cat_area are ON adc.`id_are`=are.id\n" ;
				sql += "INNER JOIN cat_nivel niv ON dcniv.`id_niv`=niv.id\n" ; 
				sql += "INNER JOIN cat_grad gra ON gra.id_nvl=niv.id\n" ;
				sql += "INNER JOIN `col_area_anio` caa ON adc.`id_are`=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` \n"; 
				sql += " AND caa.`id_anio`=adcn.id_anio AND caa.`id_adc`=adc.`id` AND caa.`id_gra`="+id_gra+"\n" ;
				sql += "INNER JOIN col_curso_aula cua ON cua.id_caa=caa.id ";
				sql += "WHERE adcn.`id_anio`=? AND gra.`id`=? AND caa.id_gir=? and caa.est='A' ";
				if(id_tra!=null) {
					sql += " AND cua.id_tra="+id_tra;
				}
				sql += " ORDER BY adc.ord ";
		
		List<Row> areas = sqlUtil.query(sql, new Object[]{ id_anio, id_gra,id_gir});
		
		return areas;

	}
	
	public List<Row> listarAreasDCNxCoordinadorxGradoCombo(Integer id_gra, Integer id_anio, Integer id_tra, Integer id_gir){
		
		String sql = "SELECT DISTINCT caa.id, are.nom value, adc.id aux1, caa.id id_area_anio, caa.est, are.id id_area \n"; 
				sql += "FROM `aca_dcn_area` adc INNER JOIN `aca_dcn_nivel` dcniv ON adc.`id_dcniv`=dcniv.`id`\n" ; 
				sql += "INNER JOIN col_conf_anio_acad_dcn adcn ON dcniv.`id_dcn`=adcn.`id_dcn`\n" ; 
				sql += "INNER JOIN cat_area are ON adc.`id_are`=are.id\n" ;
				sql += "INNER JOIN cat_nivel niv ON dcniv.`id_niv`=niv.id\n" ; 
				sql += "INNER JOIN cat_grad gra ON gra.id_nvl=niv.id\n" ;
				sql += "INNER JOIN `col_area_anio` caa ON adc.`id_are`=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` \n"; 
				sql += " AND caa.`id_anio`=adcn.id_anio AND caa.`id_adc`=adc.`id` AND caa.`id_gra`="+id_gra+"\n" ;
				sql += "INNER JOIN col_curso_aula cua ON cua.id_caa=caa.id ";
				sql += " INNER JOIN col_aula au ON cua.id_au=au.id ";
				sql += " INNER JOIN per_periodo per ON au.id_per=per.id";
				sql += " INNER JOIN ges_servicio srv ON per.id_srv=srv.id ";
				sql += " INNER JOIN col_nivel_coordinador cniv ON niv.id=cniv.id_niv AND srv.id_gir=cniv.id_gir ";
				sql += "WHERE adcn.`id_anio`=? AND gra.`id`=? AND srv.id_gir=? and caa.est='A' ";
				if(id_tra!=null) {
					sql += " AND cniv.id_tra="+id_tra;
				}
				sql += " ORDER BY adc.ord ";
		
		List<Row> areas = sqlUtil.query(sql, new Object[]{ id_anio, id_gra, id_gir});
		
		return areas;

	}
	
	
	public List<Row> listarAreasDCNxCoordinadorAreaxGradoCombo(Integer id_gra, Integer id_anio, Integer id_tra, Integer id_gir){
		
		String sql = "SELECT DISTINCT caa.id, are.nom value, adc.id aux1, caa.id id_area_anio, caa.est, are.id id_area \n"; 
				sql += "FROM `aca_dcn_area` adc INNER JOIN `aca_dcn_nivel` dcniv ON adc.`id_dcniv`=dcniv.`id`\n" ; 
				sql += "INNER JOIN col_conf_anio_acad_dcn adcn ON dcniv.`id_dcn`=adcn.`id_dcn`\n" ; 
				sql += "INNER JOIN cat_area are ON adc.`id_are`=are.id\n" ;
				sql += "INNER JOIN cat_nivel niv ON dcniv.`id_niv`=niv.id\n" ; 
				sql += "INNER JOIN cat_grad gra ON gra.id_nvl=niv.id\n" ;
				sql += "INNER JOIN `col_area_anio` caa ON adc.`id_are`=caa.`id_area` AND caa.`id_niv`=`dcniv`.`id_niv` \n"; 
				sql += " AND caa.`id_anio`=adcn.id_anio AND caa.`id_adc`=adc.`id` AND caa.`id_gra`="+id_gra+"\n" ;
				sql += "INNER JOIN col_curso_aula cua ON cua.id_caa=caa.id ";
				sql += " INNER JOIN col_aula au ON cua.id_au=au.id ";
				sql += " INNER JOIN per_periodo per ON au.id_per=per.id";
				sql += " INNER JOIN ges_servicio srv ON per.id_srv=srv.id ";
				sql += " INNER JOIN col_area_coordinador cac ON niv.id=cac.id_niv AND srv.id_gir=cac.id_gir AND cac.id_anio=per.id_anio AND cac.id_area=are.id";
				sql += " WHERE adcn.`id_anio`=? AND gra.`id`=? AND srv.id_gir=? and caa.est='A' ";
				if(id_tra!=null) {
					sql += " AND cac.id_tra="+id_tra;
				}
				sql += " ORDER BY adc.ord ";
		
		List<Row> areas = sqlUtil.query(sql, new Object[]{ id_anio, id_gra, id_gir});
		
		return areas;

	}
	
	public List<Row> listarTiposCalificacion(){
		
		String sql = "SELECT id, nom value, cod aux1 FROM cat_tipo_calificacion";
		
		List<Row> tipos_calificacion = sqlUtil.query(sql);
		
		return tipos_calificacion;

	}
	
	public List<Row> listarTiposPromedio(){
		
		String sql = "SELECT id, nom value FROM cat_tipo_promedio_aca";
		
		List<Row> tipos_promedio = sqlUtil.query(sql);
		
		return tipos_promedio;

	}
	
	public void desactivarAreaAnio(Integer id_caa){
		String sql = "update col_area_anio set est='I' WHERE id="+id_caa; //and id_adc="+id_adc
		sqlUtil.update(sql);
		
	}
	
	public Row obtenerConfiguracionxArea(Integer id_adc, Integer id_anio, Integer id_gra) {
		String sql = "SELECT caa.id, prom_per.`cod` tip_pro_per, prom_anu.`cod` tip_pro_anu, cal.cod  tip_cali \n" + 
				"FROM `col_area_anio` caa INNER JOIN `cat_tipo_calificacion` cal ON caa.`id_tca`=cal.`id`\n" + 
				"INNER JOIN `cat_tipo_promedio_aca` prom_per ON caa.`id_pro_per`=prom_per.`id`\n" + 
				"INNER JOIN `cat_tipo_promedio_aca` prom_anu ON caa.`id_pro_anu`=prom_anu.`id`\n" + 
				"WHERE caa.`id_adc`=? AND caa.`id_anio`=? AND caa.`id_gra`=?;";

		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{ id_adc,id_anio, id_gra});
		
		return SQLFrmkUtil.listToRows(list).get(0);	
	}
	
}
