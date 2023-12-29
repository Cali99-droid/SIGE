package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sige.mat.dao.impl.InstrumentoDAOImpl;

/**
 * Define m�todos DAO operations para la entidad instrumento.
 * @author MV
 *
 */
@Repository
public class InstrumentoDAO extends InstrumentoDAOImpl{
	
	public List<Map<String,Object>> listIns(Integer id_exa) {
		
		String sql ="select distinct ins.id, ins.nom, ex_ins.id_ins from eva_instrumento ins "
				+ "left join "
				+ "(eva_ins_exa_cri ex_ins "
				+ "right join eva_exa_conf_criterio ex_cri on (ex_ins.id_excri= ex_cri.id and ex_cri.id_eva_ex=" + id_exa + ")"
				+ ") on (ins.id=ex_ins.id_ins and ex_ins.est='A')";
		
		List<Map<String,Object>> listInstrumentos = jdbcTemplate.queryForList(sql);	

		return listInstrumentos;
	} 
}
