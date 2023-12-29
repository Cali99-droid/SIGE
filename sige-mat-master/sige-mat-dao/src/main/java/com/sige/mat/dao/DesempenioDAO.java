package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.DesempenioDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad desempenio.
 * @author MV
 *
 */
@Repository
public class DesempenioDAO extends DesempenioDAOImpl{
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaDesempenios(Integer id_cap, Integer id_cgsp, Integer id_ses) {
		String sql = "SELECT  DISTINCT des.id ,des.`nom` desempenio"
				+ " FROM `col_uni_sub` cus INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=`cgsp`.`id`"
				+ " INNER JOIN `col_grup_subtema` cgs ON cgsp.`id`=cgs.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ " INNER JOIN `col_grup_capacidad` cgc ON cgc.`id_cgsp`=cgsp.`id`"
				+ " INNER JOIN `col_desempenio` des ON des.`id_cgc`=cgc.`id`"
				+ " WHERE cgc.id_cap=? and cgsp.id=? and des.id not in (select id_cde from col_sesion_desempenio where id_ses=?)";
		Object[] params = new Object[]{id_cap,id_cgsp, id_ses};

		return sqlUtil.query(sql,params);	
	}
	
	/**
	 * Listar Desempenios x Grupo
	 * @param id_cgsp
	 * @return
	 */
	public List<Row> listaDesempeniosxGrupo(Integer id_cgsp, Integer id_cap, Integer id_ses) {
		String sql = "SELECT distinct csd.`id`, cde.`nom` desempenio"
				+ " FROM col_sesion_desempenio csd"
				+ " INNER JOIN `col_desempenio` cde ON csd.id_cde=cde.id"
				+ " INNER JOIN `col_grup_capacidad` cgc ON  cde.`id_cgc`=cgc.`id`"
				+ " INNER JOIN `col_grup_sub_padre` cgsp ON cgc.`id_cgsp`=cgsp.`id`"
				+ " WHERE cgsp.`id`=? and cgc.id_cap=? and id_ses=?";
		Object[] params = new Object[]{id_cgsp, id_cap, id_ses};

		return sqlUtil.query(sql,params);	
	}
	
	/**
	 * Listar desempenios por grupo cuando es tipo examen
	 * @param id_cgsp
	 * @param id_cap
	 * @param id_ses
	 * @return
	 */
	public List<Row> listaDesempeniosxGrupoparaExamen(Integer id_cgsp, Integer id_cap, Integer id_ses) {
		String sql = "SELECT DISTINCT csd.`id`, cde.`nom` desempenio"
				+ "\n FROM `not_ind_eva` nie INNER JOIN `not_evaluacion` ne ON nie.`id_ne`=ne.`id`"
				+ "\n INNER JOIN col_indicador ind ON nie.`id_ind`=ind.`id`"
				+ "\n INNER JOIN col_sesion_desempenio csd ON ind.id_csd=csd.id "
				+ "\n INNER JOIN col_desempenio cde ON csd.id_cde=cde.id "
				+ "\n INNER JOIN col_grup_capacidad cgc ON cde.id_cgc=cgc.id "
				+ "\n INNER JOIN col_capacidad cap ON cgc.`id_cap`=cap.id"
				+ "\n WHERE ne.id_ses=? AND cgc.`id_cgsp`=? AND cgc.`id_cap`=?";
		Object[] params = new Object[]{id_ses,id_cgsp, id_cap};

		return sqlUtil.query(sql,params);	
	}
	
}
