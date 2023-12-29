
package com.sige.core.dao.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sige.core.dao.SQLUtil;
import com.tesla.colegio.model.Sucursal;

@Component
public class CacheManager {
	
	@Autowired
	private SQLUtil sqlUtil;
	
	private static final Map<String, List<ComboBox>> catalogos = new HashMap<String, List<ComboBox>>();

	public  List<ComboBox> getComboBox(String catalogo, String[] args) {

		List<ComboBox> listaCombo = catalogos.get(catalogo);
		if (listaCombo == null) {
			if (CacheEnum.GES_SUCURSAL.catalogo().equals(catalogo)) {
				List<Sucursal> lista = sqlUtil.query("select * from ges_sucursal",Sucursal.class);
				List<ComboBox> list = new ArrayList<ComboBox>();
				for (Sucursal sucursal : lista) {
					ComboBox combo = new ComboBox();
					combo.setId(sucursal.getId());
					combo.setValue(sucursal.getNom());
					list.add(combo);
				}

				catalogos.put(catalogo, list);
				listaCombo = list;
			}else if (CacheEnum.FAC_CONCEPTO.catalogo().equals(catalogo)) {
					listaCombo = sqlUtil.query("select id, nom as value, monto as aux1, tip as aux2, flag_edit as aux3 from " +  catalogo + " where est='A'", ComboBox.class);
					catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.GES_SERVICIO.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id_niv id, nom as value, id_suc as aux1 from " +  catalogo + " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.GES_SUCURSAL_SEC.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id as aux1 from ges_sucursal where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.SEG_USUARIO.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, login as value from seg_usuario where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_GRAD.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from " +  catalogo + " where est='A' and tipo!='F'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_GRAD_TODOS.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from cat_grad where est='A' ", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.COL_AULA_ESPECIAL.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from cat_grad where est='A' and tipo='F'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_PROVINCIA.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id_dep as aux1 from "+ catalogo+ " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_DISTRITO.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, id_pro as aux1 from "+ catalogo+ " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_PARENTESCO.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, par as value from " +  catalogo + " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.COL_AULA.catalogo().equals(catalogo)) {
				listaCombo= sqlUtil.query("select a.id, a.secc as value, a.id_grad as aux1, p.id_anio as aux2, p.id_suc as aux3 from col_aula a "
						    + " inner join col_ciclo cic on a.id_cic = cic.id "
						    + " inner join per_periodo p on p.id = cic.id_per "
							+ " where a.est='A' order by a.secc asc", ComboBox.class);
				catalogos.put(catalogo, listaCombo);

			}else if (CacheEnum.COL_AULA_ANT.catalogo().equals(catalogo)) {
				listaCombo= sqlUtil.query("select a.id, a.secc as value, a.id_grad as aux1, p.id_anio as aux2, p.id_suc as aux3 from col_aula a "
					    + " inner join per_periodo p on p.id = a.id_per and p.id_tpe=1 "
						+ " where a.est='A' order by a.secc asc", ComboBox.class);
			catalogos.put(catalogo, listaCombo);

			}else if (CacheEnum.COL_AULA_LOCAL.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("select a.id, a.secc as value, a.id_grad as aux1, p.id_anio as aux2, p.id_suc as aux3 from col_aula a "
							//+ " inner join col_ciclo c on c.id = a.id_cic "
							//+ " inner join per_periodo p on p.id = c.id_per and p.id_tpe=1" // para el 2021 descomentar
							+ " inner join per_periodo p on p.id = a.id_per and p.id_tpe=1"
							+ " where a.est='A' order by a.secc asc", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			} else if (CacheEnum.COL_AULA_LOCAL_NUEVO.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("select a.id, a.secc as value, a.id_grad as aux1, p.id_anio as aux2, p.id_suc as aux3 from col_aula a "
							+ " inner join col_ciclo c on c.id = a.id_cic "
							+ " inner join per_periodo p on p.id = c.id_per and p.id_tpe=1" 
							//+ " inner join per_periodo p on p.id = a.id_per and p.id_tpe=1"
							+ " where a.est='A' order by a.secc asc", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			} else if (CacheEnum.CAT_PER_NIVEL.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("SELECT cp.id , ca.nom as value , cp.id_niv as aux1, ca.id as aux2 "
						+ " FROM cat_per_aca_nivel cp INNER JOIN cat_periodo_aca ca ON cp.id_cpa=ca.id", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			}else if (CacheEnum.COL_CURSO_ANIO.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("select a.id, c.nom as value, a.id_cur as aux1 from col_curso_anio a "
							+ " inner join cat_curso c on a.id_cur = c.id "
							//+ " inner join per_periodo p on p.id_anio = ? "
							+ " where a.est='A' order by c.nom desc", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			} else if (CacheEnum.COL_CURSO_ANIO_NIVEL.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("select a.id, c.nom as value, p.id_niv as aux1, a.id_gra as aux2, p.id_anio as aux3, p.id_suc as aux4 from col_curso_anio a "
							+ " inner join cat_curso c on a.id_cur = c.id "
							+ " inner join per_periodo p on a.id_per=p.id "
							+ " where a.est='A' order by c.nom desc", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			} else if (CacheEnum.COL_SUBTEMA.catalogo().equals(catalogo)) {

				listaCombo= sqlUtil.query("select s.id, concat(t.nom,' - ',s.nom)  as value,  t.id_cur as aux1,  t.id_niv as aux2 from col_subtema s "
							+ " inner join col_tema t on s.id_tem = t.id "
							+ " where s.est='A' order by t.ord asc, s.ord asc", ComboBox.class);

				catalogos.put(catalogo, listaCombo);

			}else if (CacheEnum.MOD_PARAMETRO.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, val as aux1 from " +  catalogo + " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if (CacheEnum.CAT_CENTRO_SALUD.catalogo().equals(catalogo)) {
				listaCombo = sqlUtil.query("select id, nom as value, direccion as aux1 from " +  catalogo + " where est='A'", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
			}else if(CacheEnum.AREA_ANIO.catalogo().equals(catalogo)){
				
				listaCombo = sqlUtil.query("SELECT area_anio.id, area.nom as value, area_anio.id_niv as aux1 FROM "+ catalogo+" area_anio INNER JOIN cat_area area ON area_anio.id_area=area.id", ComboBox.class);
				
				catalogos.put(catalogo, listaCombo);

			}else if(CacheEnum.GES_USUARIO.catalogo().equals(catalogo)){
				
				List<ComboBox> list = sqlUtil.query("SELECT id, login as value FROM "+ catalogo+" where est='A'", ComboBox.class);
				catalogos.put(catalogo, list);
				return list;
			}else if(CacheEnum.PER_PERIODO.catalogo().equals(catalogo)){
				
				listaCombo= sqlUtil.query("SELECT per.id, concat(suc.nom,' - ',niv.nom) as value, per.id_anio as aux1, per.id_niv as aux2, per.id_suc as aux3 "
						+ " FROM "+ catalogo+" per "
						+ " left join cat_nivel niv on per.id_niv=niv.id "
						+ " left join ges_sucursal suc on per.id_suc=suc.id "
						+ " where id_tpe=1 " 
						+ " order by suc.nom asc, niv.nom asc", ComboBox.class);
				catalogos.put(catalogo, listaCombo);
				//return list;
			}else if(CacheEnum.GES_TRABAJADOR.catalogo().equals(catalogo)){
				
				List<ComboBox> list = sqlUtil.query("SELECT id, concat(ape_pat,' ',ape_mat,' ',nom) as value from aeedu_asistencia.ges_trabajador order by ape_pat asc, ape_mat asc", ComboBox.class);
				catalogos.put(catalogo, list);
				return list;
			}else {
				List<ComboBox> list = sqlUtil.query("select id, nom as value from " +  catalogo + " where est='A' order by nom asc", ComboBox.class); 
				catalogos.put(catalogo, list);
				return list;
			}
			
		} 
		
		//aplicar filtros
		
		if (CacheEnum.FAC_CONCEPTO.catalogo().equals(catalogo)) {
		
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals(comboBox.getAux2()))
					list2.add(comboBox);
			}
			
			return list2;
		}else if(CacheEnum.GES_SERVICIO.catalogo().equals(catalogo)){
				List<ComboBox> list2 = new ArrayList<ComboBox>();
				for (ComboBox comboBox : listaCombo) {
					if (args[0].equals("A") || args[0].equals(comboBox.getAux1()) )
						list2.add(comboBox);
				}
				
				return list2;	
		}else if(CacheEnum.GES_SUCURSAL_SEC.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()) )
					list2.add(comboBox);
			}
			
			return list2;	
		}else if(CacheEnum.CAT_GRAD.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			
			return list2;
		}else if(CacheEnum.CAT_GRAD_TODOS.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			
			return list2;

		}else if(CacheEnum.COL_AULA_ESPECIAL.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			
			return list2;

		}else if(CacheEnum.CAT_PROVINCIA.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			return list2;
			
		}else if(CacheEnum.CAT_DISTRITO.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			return list2;
			
		}else if(CacheEnum.CAT_PER_NIVEL.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			
			return list2;

		}else if(CacheEnum.COL_AULA.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux2()) && args.length>1 && args[1].equals(comboBox.getAux1())  )
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_AULA_ANT.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux2()) && args.length>1 && args[1].equals(comboBox.getAux1())  )
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_AULA_LOCAL.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux2()) && args.length>1 && args[1].equals(comboBox.getAux1()) && args[2].equals(comboBox.getAux3()) )
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_AULA_LOCAL_NUEVO.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux2()) && args.length>1 && args[1].equals(comboBox.getAux1()) && args[2].equals(comboBox.getAux3()) )
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_CURSO_ANIO.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0]==null || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_CURSO_ANIO_NIVEL.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()) && args[1].equals(comboBox.getAux2()) && args[2].equals(comboBox.getAux3()) && args[3].equals(comboBox.getAux4()))
					list2.add(comboBox);
			}
			return list2;

		} else if(CacheEnum.COL_SUBTEMA.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()) && args.length>1 && args[1].equals(comboBox.getAux2()))
					list2.add(comboBox);
			}
			return list2;

		} else if (CacheEnum.MOD_PARAMETRO.catalogo().equals(catalogo)) {

			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getValue()))
					list2.add(comboBox);
			}
			
			return list2;
		} else if (CacheEnum.CAT_CENTRO_SALUD.catalogo().equals(catalogo)) {

			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getValue()))
					list2.add(comboBox);
			}
			
			return list2;
		} else if(CacheEnum.AREA_ANIO.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {
				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
				
			}
			
			return list2;
		}else if(CacheEnum.PER_PERIODO.catalogo().equals(catalogo)){
			List<ComboBox> list2 = new ArrayList<ComboBox>();
			for (ComboBox comboBox : listaCombo) {

				if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
					list2.add(comboBox);
				
			}
			
			return list2;
		}else if(CacheEnum.CAT_PARENTESCO.catalogo().equals(catalogo)){
		List<ComboBox> list2 = new ArrayList<ComboBox>();
		for (ComboBox comboBox : listaCombo) {

			if (args[0].equals("A") || args[0].equals(comboBox.getAux1()))
				list2.add(comboBox);
			
		}
		
		return list2;
	}
	
		
		return listaCombo;
		
		
	}
	
	public void update(String catalogo){
		List<ComboBox> listaCombo = new ArrayList<ComboBox>();
		
		//actualizar el cataloo
		if (CacheEnum.FAC_CONCEPTO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, nom as value, monto as aux1, tip as aux2, flag_edit as aux3 from " +  catalogo + " where est='A'", ComboBox.class);
		}else if (CacheEnum.GES_SERVICIO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id_niv id, nom as value, id_suc as aux1 from " +  catalogo + " where est='A'", ComboBox.class);
		}else if (CacheEnum.CAT_GRAD.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from " +  catalogo + " where est='A' and tipo='N'", ComboBox.class);
		}else if (CacheEnum.CAT_GRAD_TODOS.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from cat_grad where est='A' ", ComboBox.class);
		}else if (CacheEnum.COL_AULA_ESPECIAL.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, nom as value, id_nvl as aux1 from cat_grad where est='A' and tipo='F'", ComboBox.class);
		}else if (CacheEnum.COL_AULA.catalogo().equals(catalogo)) {
			listaCombo= sqlUtil.query("select a.id, a.secc as value, a.id_grad as aux1, p.id_anio as aux2 from col_aula a "
					+ " inner join per_periodo p on p.id = a.id_per "
					+ " where a.est='A'", ComboBox.class); 
		}else if (CacheEnum.MOD_PARAMETRO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, nom as value, val as aux1 from " +  catalogo + " where est='A'", ComboBox.class);
		}else if (CacheEnum.AREA_ANIO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("SELECT area_anio.id, area_anio.nom as value, area_anio.id_niv as aux1 FROM "+ catalogo+" area_anio INNER JOIN cat_area AREA ON area_anio.id_area=area.id", ComboBox.class);
		}else if (CacheEnum.GES_USUARIO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("SELECT id, login as value FROM "+ catalogo+" where est='A'", ComboBox.class);
		}else if (CacheEnum.COL_CURSO_ANIO.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select a.id, c.nom as value, a.id_cur as aux1 from col_curso_anio a "
					+ " inner join cat_curso c on a.id_cur = c.id  "
					//+ " inner join per_periodo p on p.id_anio = ? "
					+ " where a.est='A' order by c.nom desc", ComboBox.class);

		catalogos.put(catalogo, listaCombo);	
		}else if (CacheEnum.CAT_PER_ACA_NIVEL.catalogo().equals(catalogo)) {
			listaCombo = sqlUtil.query("select id, id_cpa as value from "+catalogo+ " where est='A'", ComboBox.class);
		}else if (CacheEnum.COL_CURSO_ANIO_NIVEL.catalogo().equals(catalogo)) {

			listaCombo= sqlUtil.query("select a.id, c.nom as value, p.id_niv as aux1, a.id_gra as aux2, p.id_anio as aux3, p.id_suc as aux4 from col_curso_anio a "
						+ " inner join cat_curso c on a.id_cur = c.id "
						+ " inner join per_periodo p on a.id_per=p.id "
						+ " where a.est='A' order by c.nom desc", ComboBox.class);

			catalogos.put(catalogo, listaCombo);

		}else if (CacheEnum.COL_SUBTEMA.catalogo().equals(catalogo)) {

			listaCombo= sqlUtil.query("select s.id, concat(t.nom,' - ',s.nom)  as value,  t.id_cur as aux1,  t.id_niv as aux2 from col_subtema s "
						+ " inner join col_tema t on s.id_tem = t.id "
						+ " where s.est='A' order by t.ord asc, s.ord asc", ComboBox.class);

			catalogos.put(catalogo, listaCombo);

		}else 
			listaCombo = sqlUtil.query("select id, nom as value from " +  catalogo + " where est='A'", ComboBox.class);
		
		catalogos.put(catalogo, listaCombo);

		
	}

}
