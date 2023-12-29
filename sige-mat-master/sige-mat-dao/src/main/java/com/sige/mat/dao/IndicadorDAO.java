package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.IndicadorDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad indicador.
 * @author MV
 *
 */
@Repository
public class IndicadorDAO extends IndicadorDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	/**
	 * Eliminar indicadores por Sesi�n
	 * @param id_csd
	 */
	public void deletexSesion(int id_csd) {
		String sql = "delete from col_indicador where id_csd=?";
		
		jdbcTemplate.update(sql, id_csd);
	}
	
	/**
	 * Lista de Indicadores por Capacidad y Grado
	 * @param id_cap
	 * @param id_cgsp
	 * @return
	 */
	public List<Row> listaIndicadoresxCapcidadyGrupo(Integer id_cap, Integer id_cgsp) {
		String sql = "SELECT ind.`id`"
				+ "\n FROM `col_sesion_desempenio` csd INNER JOIN `col_indicador` ind ON ind.`id_csd`=ind.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n WHERE cgc.`id_cap`=? AND cgc.`id_cgsp`=?";
		Object[] params = new Object[]{id_cap,id_cgsp};

		return sqlUtil.query(sql,params);	
	}
	
	/**
	 * Lista de Indicadores 
	 * @param id_cgc
	 * @return
	 */
	public List<Row> listaIndicadoresxGrupoCapacidad(Integer id_cgc) {
		String sql = "SELECT ind.`id`"
				+ "\n FROM `col_sesion_desempenio` csd INNER JOIN `col_indicador` ind ON ind.`id_csd`=ind.`id`"
				+ "\n INNER JOIN col_desempenio cde ON csd.id_cde=cde.id"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n WHERE cgc.id=?";
		Object[] params = new Object[]{id_cgc};

		return sqlUtil.query(sql,params);	
	}
	
	public List<Row> obtenerPeso(Integer id_ind){
		String sql = "SELECT com.peso"
				+ "\n FROM not_ind_eva nie INNER JOIN `col_indicador` ind ON nie.id_ind=ind.id"
				+ "\n INNER JOIN `col_sesion_desempenio` csd ON ind.`id_csd`=csd.`id`"
				+ "\n INNER JOIN `col_desempenio` cde ON csd.`id_cde`=cde.`id`"
				+ "\n INNER JOIN `col_grup_capacidad` cgc ON cde.`id_cgc`=cgc.`id`"
				+ "\n INNER JOIN `col_capacidad` cap ON cgc.`id_cap`=cap.id"
				+ "\n INNER JOIN col_competencia com ON cap.id_com=com.id"
				+ "\n WHERE nie.id=?";
		
		return sqlUtil.query(sql, new Object[]{id_ind});
	}
	
	public List<Row> obtenerIndicador(Integer id_not_ind){
		String sql = "SELECT nie.id  FROM `not_nota_indicador` nni INNER JOIN `not_ind_eva` nie ON nni.`id_nie`=nie.`id`"
				+ " INNER JOIN `col_indicador` ind ON nie.id_ind=ind.id "
				+ " WHERE nni.`id`=?";
		
		return sqlUtil.query(sql, new Object[]{id_not_ind});
	}
}
