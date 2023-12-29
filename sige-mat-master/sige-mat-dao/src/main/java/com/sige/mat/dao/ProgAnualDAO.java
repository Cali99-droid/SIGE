package com.sige.mat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.ProgAnualDAOImpl;
import com.tesla.colegio.model.Nota;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad prog_anual.
 * @author MV
 *
 */
@Repository
public class ProgAnualDAO extends ProgAnualDAOImpl{
	
	/*public List<Row> listControlProgAnual(Integer id_anio, Integer id_niv, Integer id_gra, Integer id_cur) {
		String sql="SELECT DISTINCT suc.nom sucursal, niv.nom nivel, gra.nom grado, cur.nom , cpa.`flg_descarga`  , cpa.`fec_ins` fec_des";
			   sql += " FROM `col_curso_anio` cua INNER JOIN per_periodo per ON cua.`id_per`=per.id ";
			   sql += " INNER JOIN cat_curso cur ON cua.`id_cur`=cur.id";
			   sql += " INNER JOIN cat_nivel niv ON per.id_niv=niv.id";
				+ " INNER JOIN cat_grad gra ON gra.id_nvl=niv.id"
				+ " INNER JOIN `col_curso_aula` cca ON cca.id_cua=cua.`id`"
				+ " INNER JOIN `col_aula` au ON cca.id_au=au.`id`AND au.`id_per`=per.id AND gra.id=au.id_grad"
				+ " INNER JOIN ges_sucursal suc ON per.id_suc=suc.id"
				+ " LEFT JOIN `con_prog_anual` cpa ON cur.id=cpa.`id_cur` AND cpa.`id_gra`=gra.id AND cpa.`id_niv`=niv.id"
				+ " WHERE  per.id_anio="+id_anio
				if(id_niv!=null)
					+ "AND niv.id=:id_niv"
				+ " ORDER BY suc.id,niv.id, gra.id, cur.nom;"; 
	}*/
}
