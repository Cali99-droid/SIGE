package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TutorAulaDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad tutor_aula.
 * @author MV
 *
 */
@Repository
public class TutorAulaDAO extends TutorAulaDAOImpl{
	final static Logger logger = Logger.getLogger(TutorAulaDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	public List<Row> listaProfesor() {
		
		String sql = "SELECT tra.id, concat(tra.ape_pat,' ',tra.ape_mat,' ',tra.nom) as value ";
				sql = sql +  " FROM aeedu_asistencia.ges_trabajador tra ";
				sql = sql + " inner join seg_usuario usu on usu.id_tra=tra.id";
				sql = sql + " inner join seg_usuario_rol urol on urol.id_usr=usu.id and urol.id_rol=5 ";
				sql = sql +  " where tra.est='A' ";
				sql = sql + " order by tra.ape_pat, tra.ape_mat, tra.nom ";
		logger.info(sql);
		return sqlUtil.query(sql);

	}
	
	public Row getTutorByAula(Integer id_au) {
		
		String sql = "SELECT tra.id, concat(tra.ape_pat,' ',tra.ape_mat,' ',tra.nom) tutor FROM col_tutor_aula cta"
				+ " inner join aeedu_asistencia.ges_trabajador tra on tra.id = cta.id_tra";
				sql = sql + " where cta.id_au=? and tra.est='A' ";
		
		//	logger.info(sql);
		
		List<Row> list =  sqlUtil.query(sql, new Object[]{id_au});
		
		if(list.size()==0)
			return null;
		else 
			return list.get(0);
		
	}
}
