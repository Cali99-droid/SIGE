package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PermisoDocenteDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad permiso_docente.
 * @author MV
 *
 */
@Repository
public class PermisoDocenteDAO extends PermisoDocenteDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	/**
	 * Obtener la vigencia para ingreso de notas x trabajador y aula
	 * @param id_tra
	 * @param id_au
	 * @return
	 */
	public Row obtenerVigencia(Integer id_tra, Integer id_au, Integer id_cpu) {
		
		String sql = "SELECT IF(CURDATE()<=DATE_ADD(fec_ins, INTERVAL 7 DAY),1,0) vig FROM `col_permiso_docente` WHERE id_prof=? AND id_au=? and id_cpu=?";
		List<Row> vigencia=sqlUtil.query(sql, new Object[] {id_tra, id_au, id_cpu});
		
		if(vigencia.size()>0)
			return vigencia.get(0);
		else
			return null;

	}
	
	public List<Row> listarPermisoDocente(Integer id_anio) {
		
		String sql = "SELECT cpd.id, gir.`nom` giro, CONCAT(pers.ape_pat,' ', pers.ape_mat,' ', pers.nom) persona, niv.`nom` nivel, gra.`nom` grado, au.`secc`\n" + 
				", pa.`nom`, cpu.nump, DATE(cpd.`fec_ins`) fec_ins, cpd.`dias`\n" + 
				"FROM `col_permiso_docente` cpd INNER JOIN `ges_trabajador` tra ON cpd.`id_prof`=tra.`id`\n" + 
				"INNER JOIN `col_persona` pers ON tra.`id_per`=pers.`id`\n" + 
				"INNER JOIN `col_aula` au ON cpd.`id_au`=au.`id`\n" + 
				"INNER JOIN `cat_grad` gra ON au.`id_grad`=gra.id\n" + 
				"INNER JOIN `per_periodo` per ON au.`id_per`=per.`id`\n" + 
				"INNER JOIN `cat_nivel` niv ON per.`id_niv`=niv.`id` AND niv.`id`=gra.`id_nvl` \n" + 
				"INNER JOIN `ges_servicio` srv ON per.`id_srv`=srv.`id`\n" + 
				"INNER JOIN `ges_giro_negocio` gir ON srv.`id_gir`=gir.`id`\n" + 
				"INNER JOIN `col_per_uni` cpu ON cpd.`id_cpu`=cpu.`id`\n" + 
				"INNER JOIN `cat_per_aca_nivel` cpa ON cpu.`id_cpa`=cpa.`id` AND cpa.`id_gir`=gir.`id`\n" + 
				"INNER JOIN `cat_periodo_aca` pa ON cpa.`id_cpa`=pa.`id`\n" + 
				"WHERE per.`id_anio`=? \n" + 
				"ORDER BY cpd.`fec_ins` DESC";
		return sqlUtil.query(sql, new Object[] { id_anio });

	}
}
