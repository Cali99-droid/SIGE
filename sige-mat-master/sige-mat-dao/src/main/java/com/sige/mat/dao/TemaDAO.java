package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TemaDAOImpl;
import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad tema.
 * @author MV
 *
 */
@Repository
public class TemaDAO extends TemaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;

	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	// lista para temas y subtemas
	public List<Row> listaTemaSubtema(Integer id_niv, Integer id_cur, Integer id_anio) {
		String sql = "SELECT distinct t.nom tema, s.nom subtema"
				+ " FROM col_tema t, col_subtema s "
				+ " WHERE t.id=s.id_tem AND id_niv=? AND id_cur=? and id_anio=? order by t.ord asc, s.ord asc";

		return sqlUtil.query(sql, new Object[] { id_niv, id_cur, id_anio});
	}
	
	// lista subtemas
		public List<Row> listaSubtema(Integer id_tema) {
			String sql = "SELECT distinct s.id, s.nom subtema "
					+ " FROM col_subtema s inner join col_grup_subtema cgs ON s.id"
					+ " WHERE s.id_tem =? order by s.ord asc";

			return sqlUtil.query(sql, new Object[] { id_tema});
		}
	
	/**
     * Clonar un la configuraci�n de Unidades por Periodo
     * @param idAlu
     * @return Id 
     */	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int insertarSubtemaxTema(Integer id_tem_nue, Integer id_tem )  {
			
		String sql = " INSERT INTO col_subtema (id_tem, nom, ORD, obs, est, usr_ins, fec_ins)"
					+ " SELECT "+id_tem_nue+",nom, ORD, obs, est,"+ tokenSeguridad.getId() +", NOW() FROM `col_subtema` WHERE id_tem="+id_tem;
        try {
			LobHandler lobHandler = new DefaultLobHandler(); 
			jdbcTemplate.update( sql);	
		} catch (DataAccessException e) {
			e.printStackTrace();
		} 
		return 1;
	}
	
	/**
	 * Listar Temas x Peridodos
	 * @param id_cpu
	 * @param id_niv
	 * @param id_gra
	 * @param id_cur
	 * @return
	 */
	// lista para temas y subtemas
	public List<Row> listaTemaxPeriodo(Integer id_cpu, Integer id_niv, Integer id_gra, Integer id_cur) {
		String sql = "SELECT distinct tem.`id`, tem.`nom` tema"
				+ "\n FROM `col_uni_sub` cus INNER JOIN `col_curso_unidad` ccu ON cus.`id_uni`=ccu.`id`"
				+ "\n INNER JOIN `col_grup_sub_padre` cgsp ON cus.`id_cgsp`=cgsp.`id`"
				+ "\n INNER JOIN `col_grup_subtema` cgc ON cgsp.`id`=cgc.`id_cgsp`"
				+ "\n INNER JOIN `col_curso_subtema` ccs ON cgc.`id_ccs`=ccs.`id`"
				+ "\n INNER JOIN `col_subtema` sub ON ccs.id_sub=sub.`id`"
				+ "\n INNER JOIN `col_tema` tem ON sub.`id_tem`=tem.`id`"
				+ "\n WHERE ccu.`id_cpu`=? AND ccs.`id_niv`=? AND ccs.`id_gra`=? AND ccs.`id_cur`=?;";

		return sqlUtil.query(sql, new Object[] {id_cpu, id_niv, id_gra, id_cur});
	}
}
