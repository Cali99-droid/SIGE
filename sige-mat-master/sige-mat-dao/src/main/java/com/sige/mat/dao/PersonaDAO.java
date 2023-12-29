package com.sige.mat.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.PersonaDAOImpl;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad persona.
 * @author MV
 *
 */
@Repository
public class PersonaDAO extends PersonaDAOImpl{
	
	@Autowired
	private SQLUtil sqlUtil;

/*	public Row obtenerDatosPersona(int id_pag) {
		
		String sql = "SELECT fam.nro_doc, td.nom tip_doc, CONCAT(fam.ape_pat,' ', fam.ape_mat,' ', fam.nom) nombres, fam.dir direccion"
				+ " FROM fac_academico_pago fac INNER JOIN mat_matricula mat ON fac.id_mat =mat.id"
				+ " INNER JOIN alu_familiar fam ON mat.id_fam=fam.id"
				+ " INNER JOIN cat_tipo_documento td ON td.id =fam.id_tdc"
				+ " WHERE fac.id=?";
		logger.info(sql);
		List<Row> list = sqlUtil.query(sql, new Object[] { id_pag });
		if(list.size()==0)
			return null;
		else
			return list.get(0);

	}*/
	
	public List<Row> listarPersonas() {

		String sql ="SELECT per.`id`, per.nro_doc, CONCAT(per.`ape_pat`,' ', per.`ape_mat`, ' ', per.`nom`) value "
				+ " FROM col_persona per"
				+ " ORDER BY per.`ape_pat`, per.`ape_mat`, per.`nom`";
		return sqlUtil.query(sql);

	}
	
	public Row datosPersona(Integer id_per) {
		String sql = "SELECT per.*,pro.id id_pro, dep.id id_dep, fam.id id_fam, fam.prof, fam.ocu, fam.id_par \r\n" ; 
				sql+= "FROM `col_persona` per \r\n"; 
				//sql+= " INNER JOIN cat_tipo_documento tdc ON per.id_tdc=tdc.id"; 
				sql+= " LEFT JOIN cat_distrito dist ON per.id_dist_viv=dist.id"; 
				sql+= " LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id";
				sql+= " LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id";
				sql+= " LEFT JOIN alu_familiar fam ON fam.id_per=per.id ";
				sql += " WHERE per.`id`="+id_per;
		List<Row> datosPersona =sqlUtil.query(sql);
		if(datosPersona.size()>0)
			return datosPersona.get(0);
		else 
			return null;
	}
	
	public Row datosPersonaxNro_doc(String nro_doc) {
		String sql = "SELECT per.*,pro.id id_pro, dep.id id_dep, fam.id id_fam, fam.prof, fam.ocu, fam.id_par \r\n" ; 
				sql+= "FROM `col_persona` per \r\n"; 
				//sql+= " INNER JOIN cat_tipo_documento tdc ON per.id_tdc=tdc.id"; 
				sql+= " LEFT JOIN cat_distrito dist ON per.id_dist_viv=dist.id"; 
				sql+= " LEFT JOIN cat_provincia pro ON dist.id_pro=pro.id";
				sql+= " LEFT JOIN cat_departamento dep ON pro.id_dep=dep.id";
				sql+= " LEFT JOIN alu_familiar fam ON fam.id_per=per.id ";
				sql += " WHERE per.`nro_doc`="+nro_doc;
		List<Row> datosPersona =sqlUtil.query(sql);
		if(datosPersona.size()>0)
			return datosPersona.get(0);
		else 
			return null;
	}
	
}
