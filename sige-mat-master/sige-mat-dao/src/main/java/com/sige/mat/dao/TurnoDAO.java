package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.TurnoDAOImpl;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad turno.
 * @author MV
 *
 */
@Repository
public class TurnoDAO extends TurnoDAOImpl{
	
@Autowired
private SQLUtil sqlUtil;
	
	public List<Row> listaTurnosxServicio(Integer id_per) {
		
		String sql = "SELECT tur.`id`, tur.`nom` value FROM `col_turno` tur INNER JOIN `col_tur_servicio` ser ON tur.`id`=ser.`id_tur`"
				+ " INNER JOIN `per_periodo` per ON ser.`id_srv`=per.`id_srv`"
				+ " WHERE per.`id`=?";
		return sqlUtil.query(sql, new Object[]{id_per});
	}
	
	public List<Row> listaTurnosxCiclo(Integer id_cic) {
		
		String sql = "SELECT tur.*, t.`id_ctu`, t.`hor_ini`, t.`hor_fin` FROM \r\n" ;
		sql +="`col_turno` tur LEFT JOIN ( SELECT ctu.id id_ctu, ctu.hor_ini, ctu.hor_fin, ctu.id_tur FROM col_ciclo_turno ctu WHERE ctu.`id_cic`="+id_cic+" and ctu.est='A' ) t ON tur.`id`=t.id_tur \r\n" ;
		sql +=" ORDER BY tur.id";
		return sqlUtil.query(sql);
	}
	
	public List<Row> listaTurnosExCiclo(Integer id_cic) {
		
		String sql = "SELECT tur.id, tur.nom value , cct.id aux1, cpc.num_cuotas aux2, cct.`hor_ini`, cct.`hor_fin` FROM \r\n" ;
		sql +="`col_turno` tur INNER JOIN col_ciclo_turno cct ON tur.id=cct.id_tur ";
		sql +=" INNER JOIN  fac_conf_pagos_ciclo cpc ON cct.id=cpc.id_cct ";
		sql +=" WHERE cct.id_cic="+id_cic;
		sql +=" ORDER BY tur.id";
		return sqlUtil.query(sql);
	}
}
