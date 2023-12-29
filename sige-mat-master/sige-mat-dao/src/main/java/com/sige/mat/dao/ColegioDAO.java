package com.sige.mat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.ColegioDAOImpl;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad colegio.
 * @author MV
 *
 */
@Repository
public class ColegioDAO extends ColegioDAOImpl{
	 @Autowired
	 private SQLUtil sqlUtil;
	
	public List<Row> listColegios() {
		String sql = "SELECT col.id, CONCAT (col.nom_niv,' - ' , col.nom, ' - ', col.cod_mod) value, dis.nom aux1, pro.nom aux2, dep.nom aux3, col.estatal FROM `col_colegio` col "
				+ " INNER JOIN `cat_distrito` dis ON col.`id_dist`=dis.id"
				+ " INNER JOIN `cat_provincia` pro ON dis.id_pro=pro.id"
				+ " INNER JOIN `cat_departamento` dep ON pro.id_dep=dep.id";
			return sqlUtil.query(sql);			 
			
	}	
	
	public List<Row> listColegiosConcurso(String nivel) {
		String sql = "SELECT col.id, CONCAT (col.nom_niv,' - ' , col.nom, ' - ', col.cod_mod) value, dis.nom aux1, pro.nom aux2, dep.nom aux3, col.estatal FROM `col_colegio` col "
				+ " INNER JOIN `cat_distrito` dis ON col.`id_dist`=dis.id"
				+ " INNER JOIN `cat_provincia` pro ON dis.id_pro=pro.id"
				+ " INNER JOIN `cat_departamento` dep ON pro.id_dep=dep.id"
				+ " WHERE col.nom_niv=?";
			return sqlUtil.query( sql,new Object[]{ nivel});			 
			
	}
	
	public List<Row> listColegiosDistrito(String nivel) {
		String sql = "SELECT col.id, CONCAT (col.nom_niv,' - ' , col.nom, ' - ', col.cod_mod,' - ', dis.nom) value, dis.nom aux1, pro.nom aux2, dep.nom aux3, col.estatal FROM `col_colegio` col "
				+ " INNER JOIN `cat_distrito` dis ON col.`id_dist`=dis.id"
				+ " INNER JOIN `cat_provincia` pro ON dis.id_pro=pro.id"
				+ " INNER JOIN `cat_departamento` dep ON pro.id_dep=dep.id"
				+ " WHERE col.nom_niv=?";
			return sqlUtil.query( sql,new Object[]{ nivel});			 
			
	}
		
/*	SELECT col.`id`, col.`nom`, dis.`nom` distrito, pro.`nom` provincia, dep.`nom` departamento
	FROM `col_colegio` col INNER JOIN `cat_distrito` dis ON col.`id_dist`=dis.`id`
	INNER JOIN `cat_provincia` pro ON dis.`id_pro`=pro.`id`
	INNER JOIN `cat_departamento` dep ON pro.`id_dep`=dep.`id`
	WHERE mon_niv=;*/
}
