package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.CursoExoneracionDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad curso_exoneracion.
 * @author MV
 *
 */
@Repository
public class CursoExoneracionDAO extends CursoExoneracionDAOImpl{
	final static Logger logger = Logger.getLogger(CursoExoneracionDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaExonerados(Integer id_au, Integer id_cur){
		
		String sql = "SELECT mat.id"
				+ " FROM `mat_matricula` mat"
				+ " inner join col_curso_aula cca  on mat.id_au = cca.id_au "
				+ " inner join col_curso_anio cua on cua.id = cca.id_cua "
				+ " inner JOIN `alu_alumno` alu ON mat.`id_alu`=alu.id"
				+ " inner join not_curso_exoneracion nce on nce.id_mat=mat.id and nce.id_cur= cua.id_cur"
				+ " WHERE mat.id_au= ? and cua.id_cur = ?"
				+ " ORDER BY mat.`id_alu` ASC";
		
		
		return sqlUtil.query(sql,new Object[]{id_au, id_cur});
	}
	
public List<Row> listaPorAnio(Integer id_anio){
		
		String sql = "SELECT  e.fecha, a.nro_doc , c.nom curso, e.motivo, e.resol, CONCAT(a.ape_pat, ' ', a.ape_mat , ', ' , a.nom) alumno,  e.* FROM not_curso_exoneracion e"
				+ " INNER JOIN mat_matricula m ON m.id= e.id_mat"
				+ " INNER JOIN alu_alumno a ON a.id= m.id_alu"
				+ " INNER JOIN cat_curso c ON c.id = e.id_cur"
				+ " INNER JOIN per_periodo p ON p.id = m.id_per"
				+ " WHERE p.id_anio=" + id_anio;

		
		
		return sqlUtil.query(sql);
	}
	
}
