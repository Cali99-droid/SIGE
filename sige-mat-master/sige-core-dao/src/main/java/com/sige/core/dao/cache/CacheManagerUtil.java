package com.sige.core.dao.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sige.core.dao.SQLUtil;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Component
public class CacheManagerUtil {

	@Autowired
	private SQLUtil sqlUtil;

	private static final Map<String, List<Row>> catalogos = new HashMap<String, List<Row>>();

	public List<Row> getComboBox(String catalogo, String args[]) {
		List<Row> listaCombo = catalogos.get(catalogo);
		if (listaCombo == null) {
			String sql = "select * from " + catalogo + " where est='A'";
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					sql = sql + " and " + args[i];
				}
			}
			listaCombo = sqlUtil.query(sql);
			catalogos.put(catalogo, listaCombo);
		}

		return listaCombo;
	}

	public void update(String catalogo) {
		String sql = "select * from " + catalogo + " where est='A'";
		List<Row> listaCombo = sqlUtil.query(sql);
		catalogos.put(catalogo, listaCombo);
	}

	public List<Row> getList(String tabla,Param param) {
		List<Row> listBD = catalogos.get(tabla);

		if (listBD == null) {
			String sql = "select * from " + tabla + " where est='A'";
			
			listBD = sqlUtil.query(sql);
			catalogos.put(tabla, listBD);
		}
		
		List<Row> list = new ArrayList<Row>();
		
		for (Row row : listBD) {
			boolean encontro=false;
			for (Map.Entry<String, Object> entry : param.entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();
			    Object valueBD = row.get(key);
			    if(valueBD.toString().equals(value.toString()))
			    	encontro=true;
			    else
			    	encontro=false;
			}
			
			if (encontro)
				list.add(row);

		}
		
		return list;
		
	}
	
	public Row getParametro(String parametro){
		List<Row> parametros= catalogos.get("mod_parametro");

		if (parametros == null) {
			String sql = "select * from mod_parametro where est='A'";
			parametros = sqlUtil.query(sql);
			catalogos.put("mod_parametro", parametros);
		}

		
		for (Row row : parametros) {
			if (row.getString("nom").equals(parametro))
				return row; 
		}
		
		return null;
	}
	

	public Row getParametro(int id_niv, String parametro){
		List<Row> parametros= catalogos.get("mod_parametro_nivel");

		if (parametros == null) {
			String sql = "SELECT par.id, md.nom nom_param, par.id_par, par.id_niv, par.nom, par.des, par.val "
					+ " FROM mod_parametro_nivel par INNER JOIN mod_parametro md ON md.id = par.id_par WHERE par.est='A'";
			parametros = sqlUtil.query(sql);
			catalogos.put("mod_parametro", parametros);
		}

		
		for (Row row : parametros) {
			if (row.getString("nom_param").equals(parametro) && id_niv == row.getInt("id_niv") )
				return row; 
		}
		
		return null;
	}
	
	public List<Row> getListDepartamentos(int id_pais){
		List<Row> departamentos= catalogos.get("cat_departamento");

		if (departamentos == null) {
			String sql = "select id, nom, id_pais from cat_departamento where est='A' order by nom";
			departamentos = sqlUtil.query(sql);
			catalogos.put("cat_departamento", departamentos);
		}

		
		List<Row> list = new ArrayList<Row>();
		
		for (Row row : departamentos) {
			if (row.getInt("id_pais")== id_pais)
				list.add(row); 
		}
		
		return list;
	}
	
	public List<Row> getListProvincias(int id_dep){
		List<Row> provincias= catalogos.get("cat_provincia");

		if (provincias == null) {
			String sql = "select id, nom, id_dep from cat_provincia where est='A' order by nom";
			provincias = sqlUtil.query(sql);
			catalogos.put("cat_provincia", provincias);
		}

		
		List<Row> list = new ArrayList<Row>();
		
		for (Row row : provincias) {
			if (row.getInt("id_dep")== id_dep)
				list.add(row); 
		}
		
		return list;
	}

	public List<Row> getListDistritos(int id_pro){
		List<Row> distritos= catalogos.get("cat_distrito");

		if (distritos == null) {
			String sql = "select id, nom, id_pro from cat_distrito where est='A' order by nom";
			distritos = sqlUtil.query(sql);
			catalogos.put("cat_distrito", distritos);
		}

		
		List<Row> list = new ArrayList<Row>();
		
		for (Row row : distritos) {
			if (row.getInt("id_pro")== id_pro)
				list.add(row); 
		}
		
		return list;
	}

}
