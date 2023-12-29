package com.sige.mat.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SubtemaCapacidadDAOImpl;
import com.tesla.colegio.model.CursoSubtema;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad subtema_capacidad.
 * @author MV
 *
 */
@Repository
public class SubtemaCapacidadDAO extends SubtemaCapacidadDAOImpl{
	final static Logger logger = Logger.getLogger(SubtemaCapacidadDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public Map<String,Object> listCursoSubTema(CursoSubtema curso_subtema){

		//competencias
		String sqlCompetencias = "select com.id com_id, com.nom, count(*) count from col_capacidad cap "
				+ "\n inner join col_competencia com on com.id = cap.id_com "
				+ "\n where com.id_cur=? and com.id_niv=? "
				+ "\n group by com.id, com.nom"
				+ "\n order by com.nom";
		List<Map<String,Object>> competencias = jdbcTemplate.queryForList(sqlCompetencias, new Object[]{curso_subtema.getId_cur(), curso_subtema.getId_niv()});	

		
		//capacidades
		String sqlCapacidad = "select com.id com_id, com.nom com_nom,cap.id, cap.nom from col_capacidad cap"
				+ "\n inner join col_competencia com on com.id = cap.id_com"
				+ "\n where com.id_cur=?  and com.id_niv=? "
				+ "\n order by com.nom, cap.nom";
		
		List<Map<String,Object>> capacidades = jdbcTemplate.queryForList(sqlCapacidad, new Object[]{curso_subtema.getId_cur(), curso_subtema.getId_niv()});	

		String sqlSelectCapacidades = "";
		String leftJoinCapacidaes = "";
		for (Map<String, Object> map : capacidades) {
			Integer capacidad_id = (Integer)map.get("id");
			String capacidad_nombre = map.get("nom").toString();
			sqlSelectCapacidades = sqlSelectCapacidades + ",csc" + capacidad_id +".id as 'cap" + capacidad_id + "' ";
			leftJoinCapacidaes = leftJoinCapacidaes + "\n left join col_subtema_capacidad csc" + capacidad_id + " on csc" + capacidad_id + ".id_ccs = ccs.id and csc" + capacidad_id + ".id_cap =" +  capacidad_id;

		}
		
		//query por subtema
		List<Map<String,Object>>  list = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>>  listIndicadores = new ArrayList<Map<String,Object>>();

		if(capacidades.size()>0){
			String sql ="select ccs.id, tem.nom tema,sub.nom " + sqlSelectCapacidades
					+ "\n from  `col_curso_subtema` ccs "
					+ "\n inner join col_subtema sub on sub.id = ccs.id_sub"
					+ "\n inner join col_tema tem on tem.id = sub.id_tem and ccs.id_niv = tem.id_niv"
					+ leftJoinCapacidaes
					+ "\n where ccs.id_anio=? and ccs.id_niv=? and ccs.id_cur=? and ccs.id_gra=? and tem.id=?"
					+ "\n and ccs.est='A' and sub.est='A'"
					+ "\n order by tem.nom, sub.nom";
			
			list = jdbcTemplate.queryForList(sql, new Object[]{curso_subtema.getId_anio(), curso_subtema.getId_niv(), curso_subtema.getId_cur(), curso_subtema.getId_gra(), curso_subtema.getId_tem()});
			
			//indicadores
			String sqlIndicadores = "select cap.id cap_id, i.id, i.nom, ccs.id id_ccs from col_indicador i"
					+ "\n inner join col_capacidad cap  on cap.id = i.id_cap"
					+ "\n inner join col_competencia com on com.id = cap.id_com"
					+ "\n inner join col_curso_subtema ccs  on ccs.id_anio=i.id_anio and ccs.id_niv= com.id_niv and ccs.id_cur=com.id_cur and ccs.id_gra= i.id_gra  "
					+ "\n inner join col_subtema_capacidad csc  on csc.id_cap = cap.id  and  csc.id_ccs = ccs.id "
					+ "\n inner join col_ind_sub ins on ins.id_ind = i.id  and ccs.id = ins.id_sub "
					+ "\n where i.id_anio=? and com.id_niv=? and com.id_cur=? and i.id_gra=?"
					+ "\n order by com.nom";
			listIndicadores = jdbcTemplate.queryForList(sqlIndicadores, new Object[]{curso_subtema.getId_anio(), curso_subtema.getId_niv(), curso_subtema.getId_cur(), curso_subtema.getId_gra()});

		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("competencias", competencias);
		map.put("capacidades", capacidades);
		map.put("indicadores", listIndicadores);
		map.put("list", list);
		
		return map;
		
		
	}
	
	public List<Row> listarTemas(int id_anio, int id_niv, int id_cur, int id_gra) {

		String sql ="select distinct tem.id, tem.nom  as value " 
				+ "\n from  `col_curso_subtema` ccs "
				+ "\n inner join col_subtema sub on sub.id = ccs.id_sub"
				+ "\n inner join col_tema tem on tem.id = sub.id_tem and ccs.id_niv = tem.id_niv"	
				+ "\n where ccs.id_anio=? and ccs.id_niv=? and ccs.id_cur=? and ccs.id_gra=?"
				+ "\n and ccs.est='A' and sub.est='A'"
				+ "\n order by tem.nom";
		logger.info(sql);
		return sqlUtil.query(sql, new Object[] { id_anio, id_niv, id_cur, id_gra });

	}

	
}
