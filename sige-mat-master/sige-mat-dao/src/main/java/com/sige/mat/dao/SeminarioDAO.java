package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SeminarioDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad seminario.
 * @author MV
 *
 */
@Repository
public class SeminarioDAO extends SeminarioDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listarSeminarios(Integer id_anio) {
		String sql = "SELECT  DISTINCT sem.id, sem.`nom` , CASE WHEN fec_ini_ins <= NOW() AND NOW() <= fec_fin_ins THEN '1' ELSE '0' END vigente, sem.fec " // CASE WHEN fec_ini_ins <= NOW() AND NOW() <= fec_fin_ins THEN '1' ELSE '0' END
				+ " FROM col_seminario sem WHERE sem.id_anio="+id_anio
				+ " ORDER BY sem.fec DESC";
			return sqlUtil.query(sql);			 
			
	}	
	
	public boolean existsInscripcion(String dni, Integer id_sem) {
		
		String sql = "SELECT * FROM `col_sem_inscripcion` WHERE nro_dni="+dni+" AND id_sem="+id_sem;

		List<Map<String,Object>> postulante = jdbcTemplate.queryForList(sql);
		
		return (postulante.size()>0);

	}
	
	public List<Row> obtenerDatosInscripcion(Integer id_ins) {
		String sql= " SELECT ins.id, LOWER(sem.`nom`) seminario, dis.`nom` distrito, sem.id id_sem, ins.corr, "
				+ " pro.nom provincia, "
				+ "ins.`ape_pat`, ins.`ape_mat`, ins.`nom`, ins.`nro_dni`, gru.nro, gru.hor_ing "
				+ " FROM `col_seminario` sem INNER JOIN `col_sem_inscripcion` ins ON sem.id=ins.`id_sem` "
				+ " INNER JOIN `cat_distrito` dis ON ins.`id_dist`=dis.`id`"
				+ " INNER JOIN `cat_provincia` pro ON dis.id_pro=pro.id"
				+ " INNER JOIN `col_sem_grupo` gru ON ins.`id_gru`=gru.`id`"
				//+ " WHERE cos.id_ti in (1,2) and ins.`id`="+id_ins;
				+ " WHERE ins.`id`="+id_ins;
		return sqlUtil.query(sql);		
	}

}
