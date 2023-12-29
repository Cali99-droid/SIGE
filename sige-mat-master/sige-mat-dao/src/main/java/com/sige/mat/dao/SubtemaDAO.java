package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SubtemaDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad subtema.
 * 
 * @author MV
 *
 */
@Repository
public class SubtemaDAO extends SubtemaDAOImpl {
	@Autowired
	private SQLUtil sqlUtil;

	// lista para temas y subtemas
	public List<Row> listaSubtemaxTema(Integer id_tem) {
		String sql = "SELECT id, nom value FROM `col_subtema` WHERE id_tem=?";

		return sqlUtil.query(sql, new Object[] { id_tem });
	}

	//listar todos los subtemas por a�o y curso
	public List<Row> listarSubtemasxAnioCurso(Integer id_anio, Integer id_niv,Integer id_cur) {
		String sql = "SELECT s.id, concat(t.nom,' - ', s.nom) value FROM col_subtema s inner join col_tema t on t.id= s.id_tem WHERE t.id_anio=? and t.id_niv=? and t.id_cur=? order by t.nom, s.nom";

		return sqlUtil.query(sql, new Object[] { id_anio,id_niv ,id_cur});
	}
	
	/**
	 * Listar todos los col_curso_subtema, para la pantalla col_subtema_capacidad
	 * @param id_anio A�o escolar
	 * @param id_tem Tema
	 * @return
	 */
	public List<Row> listarCursoSubtemas(Integer id_anio, Integer id_niv,Integer id_gra, Integer id_cur,Integer id_tem) {
		String sql = "select ccs.id, sub.nom value from col_curso_subtema ccs"
				+ " inner join col_subtema sub on sub.id = ccs.id_sub"
				+ " where ccs.id_anio=? and ccs.id_niv=? and ccs.id_gra=? and ccs.id_cur=? and sub.id_tem=?";

		return sqlUtil.query(sql, new Object[] { id_anio,id_niv,id_gra, id_cur, id_tem });
	}
	
	/**
	 * Listar subtemas x grupo
	 * @param id_cgsp
	 * @return
	 */
	public List<Row> listarSubtemasxGrupo(Integer id_cgsp) { //
		String sql = "SELECT sub.`id`, CONCAT(tem.nom,' - ',sub.`nom`) subtema "
				+ " FROM `col_grup_sub_padre` cgsp INNER JOIN `col_grup_subtema` cgs ON cgsp.`id`=cgs.`id_cgsp`"
				+ " INNER JOIN `col_curso_subtema` ccs ON cgs.`id_ccs`=ccs.`id`"
				+ " INNER JOIN `col_subtema` sub ON ccs.`id_sub`=sub.`id`"
				+ " INNER JOIN col_tema tem ON tem.id=sub.id_tem"
				+ " WHERE cgsp.`id`=?";

		return sqlUtil.query(sql, new Object[] {id_cgsp});
	}
	
	/**
	 * Listar los grupos x tema
	 * @param id_anio
	 * @param id_niv
	 * @param id_gra
	 * @param id_cur
	 * @param id_tem
	 * @return
	 */
	public List<Row> listarGruposxTema(Integer id_anio, Integer id_niv, Integer id_gra, Integer id_cur, Integer id_tem, Integer id_cpu) {
		String sql = "SELECT DISTINCT cgsp.id, ccu.num, cpud.nro_sem"
				+ "\n FROM `col_grup_sub_padre` cgsp"
				+ "\n INNER JOIN `col_grup_subtema` cgc ON cgsp.`id`=cgc.`id_cgsp`"
				+ "\n INNER JOIN `col_curso_subtema` ccs ON cgc.`id_ccs`=ccs.`id`"
				+ "\n INNER JOIN `col_subtema` sub ON ccs.id_sub=sub.`id`"
				+ "\n inner join `col_uni_sub` cus ON cus.`id_cgsp`=cgsp.`id`"
				+ "\n inner join `col_curso_unidad` ccu on cus.id_uni=ccu.id "
				+ "\n inner join col_per_uni cpu ON ccu.id_cpu=cpu.id  "
				+ "\n inner join col_per_uni_det cpud ON cpu.id=cpud.id_cpu AND cpud.`ord` = ccu.num"
				+ "\n WHERE ccs.id_anio=? AND ccs.id_niv=? AND ccs.id_gra=? AND ccs.id_cur=? AND sub.id_tem=?"
				+ "\n and ccu.`id_cpu`=? ";

		return sqlUtil.query(sql, new Object[] {id_anio, id_niv, id_gra, id_cur, id_tem, id_cpu});
	}
	
}
