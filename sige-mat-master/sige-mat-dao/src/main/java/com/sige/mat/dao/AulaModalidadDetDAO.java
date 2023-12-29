package com.sige.mat.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.sige.mat.dao.impl.AulaModalidadDetDAOImpl;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Define m�todos DAO operations para la entidad aula_modalidad_det.
 * @author MV
 *
 */
@Repository
public class AulaModalidadDetDAO extends AulaModalidadDetDAOImpl{
	
	/*Lista de instrumentos usados*/
	public List<Row> listarModalidadesxAulaxMes(Integer id_au) {
		String sql = "SELECT aucmd.id, au.`id` id_au, cme.`nom` modalidad, aucmd.`mes` \r\n" + 
				"FROM col_aula au INNER JOIN `col_aula_modalidad_det` aucmd ON au.`id`=aucmd.`id_au`\r\n" + 
				"INNER JOIN `cat_modalidad_estudio` cme ON aucmd.`id_cme`=cme.`id`\r\n" + 
				"WHERE au.id=? \r\n" + 
				"ORDER BY aucmd.`mes`;";
		return SQLFrmkUtil.listToRows(jdbcTemplate.queryForList(sql, new Object[] {id_au}));
	}
	
}
