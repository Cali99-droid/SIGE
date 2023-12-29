package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CapacidadDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad capacidad.
 * @author MV
 *
 */
@Repository
public class CapacidadDAO extends CapacidadDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaCapacidadxSubtema(Integer id_ccs) {
		String sql = "SELECT cap.id, cap.`nom` value, cgsp.id aux1"
				+ " FROM `col_capacidad` cap INNER JOIN `col_grup_capacidad` cgc ON cap.`id`=cgc.`id_cap`"
				+ " INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_grup_subtema` cgs ON cgs.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " WHERE ccs.id=?;";
		Object[] params = new Object[]{id_ccs};

		return sqlUtil.query(sql,params);	
	}	
	
	
	public List<Row> listaCapacidadxGrupo(Integer id_ses,Integer id_cgsp) {
		String sql = "SELECT distinct cap.id id_cap, cap.`nom` capacidad "
				+ " FROM `col_capacidad` cap "
				+ " INNER JOIN `col_grup_capacidad` cgc ON cap.`id`=cgc.`id_cap`  "
				+ " INNER JOIN col_desempenio cde ON cgc.id=cde.id_cgc"
				+ " inner join col_sesion_desempenio csd on csd.id_cde=cde.id"
				+ " WHERE csd.id_ses=? and cgc.id_cgsp=?";
		Object[] params = new Object[]{id_ses,id_cgsp};

		return sqlUtil.query(sql,params);	
	}
	
	/** 
	 * Lista las capacidades por grupo cuando es tipo examen
	 * @param id_ses
	 * @param id_cgsp
	 * @return
	 */
	public List<Row> listaCapacidadxGrupoparaExamen(Integer id_ses,Integer id_cgsp) {
		String sql = "SELECT DISTINCT cap.`id` id_cap, cap.`nom` capacidad"
				+ " FROM `not_ind_eva` nie INNER JOIN `not_evaluacion` ne ON nie.`id_ne`=ne.`id`"
				+ " INNER JOIN col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ " INNER JOIN col_sesion_desempenio csd ON ind.id_csd=csd.id "
				+ " INNER JOIN col_desempenio cde ON csd.id_cde=cde.id "
				+ " INNER JOIN col_grup_capacidad cgc ON cde.id_cgc=cgc.id "
				+ " INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.id"
				+ " WHERE ne.id_ses=? AND cgc.`id_cgsp`=?";
		Object[] params = new Object[]{id_ses,id_cgsp};

		return sqlUtil.query(sql,params);	
	}	
	
	/**
	 * Lista las capacidades por unidad, usado en programacion anual
	 * @param id_uni
	 * @return
	 */
	public Integer[] listaCapacidadxUnidad(Integer id_uni) {
		String sql = "SELECT distinct cap.`id`"
				+ " FROM `col_uni_sub` cus INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cgc.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.`id`"
				+ " WHERE cus.`id_uni`=?";
		Object[] params = new Object[]{id_uni};

		List<Row> list = sqlUtil.query(sql,params);
		
		
		Integer ids[] = new Integer[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			ids[i] = list.get(i).getInteger("id") ;
		}
		
		
		return ids;
		
			
	}
}
