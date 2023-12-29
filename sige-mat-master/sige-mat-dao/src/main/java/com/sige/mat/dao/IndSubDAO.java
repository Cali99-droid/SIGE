package com.sige.mat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.IndSubDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad ind_sub.
 * @author MV
 *
 */
@Repository
public class IndSubDAO extends IndSubDAOImpl{
	final static Logger logger = Logger.getLogger(IndSubDAO.class);

	@Autowired
	private SQLUtil sqlUtil;
	
	/**
	 * Lista de indicadores
	 * @param id_anio
	 * @param id_gra
	 * @param id_cap
	 * @param id_sub
	 * @return
	 */
	public List<Row> listaIndicadores(Integer id_anio,Integer id_gra,Integer id_cap,Integer id_sub){
		
		String sql = "select i.*,cis.id cis_id  from col_indicador i inner join col_ind_sub cis where cis.id_ind = i.id and id_anio=? and id_gra=? and id_cap=? and cis.id_sub=?";
		
		return sqlUtil.query(sql, new Object[]{id_anio,id_gra,id_cap,id_sub});
		
	}
	
	public void deleteByIdIndicador(Integer id_ind){
		String sql = "delete from col_ind_sub where id_ind=" + id_ind;
		sqlUtil.update(sql);
	}
}
