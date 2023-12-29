package com.sige.mat.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.ConfFechasDAO;
import com.tesla.colegio.model.ConfFechas;
import com.tesla.colegio.model.bean.ConfFechasBean;


@RestController
@RequestMapping(value = "/api/confFechas")
public class ConfFechasRestController {
	
	@Autowired
	private ConfFechasDAO conf_fechasDAO;

	
	
	@RequestMapping(value = "/{id_anio}", method = RequestMethod.GET)
	public AjaxResponseBody editar(@PathVariable Integer id_anio) {
		
		AjaxResponseBody result = new AjaxResponseBody();
		
		Param param = new Param();
		param.put("id_anio", id_anio);
		List<ConfFechas> conf_fechas = conf_fechasDAO.listByParams(param, null);
		
		ConfFechasBean confFechasBean = new ConfFechasBean();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

		
		for (ConfFechas confFechas : conf_fechas) {
			
			if(confFechas.getTipo().equals("AC")){
				confFechasBean.setAc_id(confFechas.getId());
				confFechasBean.setAc_del(dateFormat.format(confFechas.getDel()));
				confFechasBean.setAc_al(dateFormat.format(confFechas.getAl()));
				if(confFechas.getAl_cs()!=null)
					confFechasBean.setAc_al_cs(dateFormat.format(confFechas.getAl_cs()));
				if(confFechas.getDel_cs()!=null)
					confFechasBean.setAc_del_cs(dateFormat.format(confFechas.getDel_cs()));
			
			}
			
			if(confFechas.getTipo().equals("AS")){
				confFechasBean.setAs_id(confFechas.getId());
				confFechasBean.setAs_del(dateFormat.format(confFechas.getDel()));
				confFechasBean.setAs_al(dateFormat.format(confFechas.getAl()));
				if(confFechas.getAl_cs()!=null)
					confFechasBean.setAs_al_cs(dateFormat.format(confFechas.getAl_cs()));
				if(confFechas.getDel_cs()!=null)
					confFechasBean.setAs_del_cs(dateFormat.format(confFechas.getDel_cs()));
			}

			if(confFechas.getTipo().equals("NC")){
				confFechasBean.setNc_id(confFechas.getId());
				confFechasBean.setNc_del(dateFormat.format(confFechas.getDel()));
				confFechasBean.setNc_al(dateFormat.format(confFechas.getAl()));
				if(confFechas.getAl_cs()!=null)
					confFechasBean.setNc_al_cs(dateFormat.format(confFechas.getAl_cs()));
				if(confFechas.getDel_cs()!=null)
					confFechasBean.setNc_del_cs(dateFormat.format(confFechas.getDel_cs()));
			}
			
			if(confFechas.getTipo().equals("NS")){
				confFechasBean.setNs_id(confFechas.getId());
				confFechasBean.setNs_del(dateFormat.format(confFechas.getDel()));
				confFechasBean.setNs_al(dateFormat.format(confFechas.getAl()));
				if(confFechas.getAl_cs()!=null)
					confFechasBean.setNs_al_cs(dateFormat.format(confFechas.getAl_cs()));
				if(confFechas.getDel_cs()!=null)
					confFechasBean.setNs_del_cs(dateFormat.format(confFechas.getDel_cs()));
			}
			
		 
		}
		result.setResult(confFechasBean);
				
		return result;
	}

	@Transactional
	@RequestMapping(value = "/grabar", method = RequestMethod.POST)
	public AjaxResponseBody grabar(ConfFechasBean confFechas,Integer id_anio) throws IOException{
		
		AjaxResponseBody result = new AjaxResponseBody();

 		try {
		 
			//validacion de fechas
			
			
			ConfFechas confFechasAC = new ConfFechas();
			confFechasAC.setId(confFechas.getAc_id());
			confFechasAC.setTipo("AC");
			confFechasAC.setId_anio(id_anio);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

			
			confFechasAC.setDel((Date)dateFormat.parse(confFechas.getAc_del()));
			confFechasAC.setAl((Date)dateFormat.parse(confFechas.getAc_al()));
			if(confFechas.getAc_del_cs()!= null && !confFechas.getAc_del_cs().equals(""))
			confFechasAC.setDel_cs((Date)dateFormat.parse(confFechas.getAc_del_cs()));
			if(confFechas.getAc_al_cs()!= null && !confFechas.getAc_al_cs().equals(""))
			confFechasAC.setAl_cs((Date)dateFormat.parse(confFechas.getAc_al_cs()));
			confFechasAC.setEst("A");

			ConfFechas confFechasAS = new ConfFechas();
			confFechasAS.setId(confFechas.getAs_id());
			confFechasAS.setTipo("AS");
			confFechasAS.setId_anio(id_anio);
			confFechasAS.setDel((Date)dateFormat.parse(confFechas.getAs_del()));
			confFechasAS.setAl((Date)dateFormat.parse(confFechas.getAs_al()));
			if(confFechas.getAs_del_cs()!= null && !confFechas.getAs_del_cs().equals(""))
			confFechasAS.setDel_cs((Date)dateFormat.parse(confFechas.getAs_del_cs()));
			if(confFechas.getAs_al_cs()!= null && !confFechas.getAs_al_cs().equals(""))
			confFechasAS.setAl_cs((Date)dateFormat.parse(confFechas.getAs_al_cs()));
			confFechasAS.setEst("A");

			if (!confFechasAS.getDel().after(confFechasAC.getAl())){
				result.setCode("201");
				result.setMsg("La fecha de inicio de la matricula de alumnos antiguos sin cronograma debe ser posterior a la fecha fin de la matricula con cronograma");
				return result;
			}

			ConfFechas confFechasNC = new ConfFechas();
			confFechasNC.setId(confFechas.getNc_id());
			confFechasNC.setTipo("NC");
			confFechasNC.setId_anio(id_anio);
			
			if(confFechas.getNc_del() != null && !confFechas.getNc_del().equals("") )
				confFechasNC.setDel((Date)dateFormat.parse(confFechas.getNc_del()));
			else
				confFechasNC.setDel(null);
			
			if(confFechas.getNc_al() != null && !confFechas.getNc_al().equals("") )
				confFechasNC.setAl((Date)dateFormat.parse(confFechas.getNc_al()));
			else
			confFechasNC.setAl((Date)dateFormat.parse(confFechas.getNc_al()));
			
			if(confFechas.getNc_del_cs()!= null && !confFechas.getNc_del_cs().equals(""))
				confFechasNC.setDel_cs((Date)dateFormat.parse(confFechas.getNc_del_cs()));
			else
				confFechasNC.setDel_cs(null);
			
			if(confFechas.getNc_al_cs()!= null && !confFechas.getNc_al_cs().equals(""))
				confFechasNC.setAl_cs((Date)dateFormat.parse(confFechas.getNc_al_cs()));
			else
				confFechasNC.setAl_cs(null);
			
			confFechasNC.setEst("A");
			

			ConfFechas confFechasNS = new ConfFechas();
			confFechasNS.setId(confFechas.getNs_id());
			confFechasNS.setTipo("NS");
			confFechasNS.setId_anio(id_anio);
			if(confFechas.getNs_del() != null && !confFechas.getNs_del().equals("") )
				confFechasNS.setDel((Date)dateFormat.parse(confFechas.getNs_del()));
			else
				confFechasNS.setDel(null);
			if(confFechas.getNs_al() != null &&  !confFechas.getNs_al().equals("") )
				confFechasNS.setAl((Date)dateFormat.parse(confFechas.getNs_al()));
			else
				confFechasNS.setAl(null);
			
			if(confFechas.getNs_del_cs()!= null && !confFechas.getNs_del_cs().equals(""))
				confFechasNS.setDel_cs((Date)dateFormat.parse(confFechas.getNs_del_cs()));
			else
				confFechasNS.setDel_cs(null);
			
			if(confFechas.getNs_al_cs()!= null && !confFechas.getNs_al_cs().equals(""))
				confFechasNS.setAl_cs((Date)dateFormat.parse(confFechas.getNs_al_cs()));
			else
				confFechasNS.setAl_cs(null);
			
			if (!(confFechasNC.getAl()!=null && confFechasNS.getDel().after(confFechasNC.getAl()))){
				result.setCode("201");
				result.setMsg("La fecha de inicio de la matricula de alumnos nuevos sin cronograma debe ser posterior a la fecha fin de la matricula con cronograma");
				return result;
			}
			
			confFechasNS.setEst("A");
			
			//System.out.print(confFechasAC.getDel_cs());
			//System.out.print(confFechasAC.getAl_cs());
			
			Integer ac_id = conf_fechasDAO.saveOrUpdate(confFechasAC);
			confFechas.setAc_id(ac_id);
			//System.out.print(confFechasAS.getDel_cs());
			//System.out.print(confFechasAS.getAl_cs());
			Integer as_id = conf_fechasDAO.saveOrUpdate(confFechasAS);
			confFechas.setAs_id(as_id);
			//System.out.print(confFechasNC.getDel_cs());
			//System.out.print(confFechasNC.getAl_cs());
			Integer nc_id = conf_fechasDAO.saveOrUpdate(confFechasNC);
			confFechas.setNc_id(nc_id);
			//System.out.print(confFechasNS.getDel_cs());
			//System.out.print(confFechasNS.getAl_cs());
			Integer ns_id = conf_fechasDAO.saveOrUpdate(confFechasNS);
			confFechas.setNs_id(ns_id);
			
			result.setResult(confFechas);
			
		} catch (NumberFormatException e) {
			result.setCode("201");
			result.setMsg("Formato del numero");
			return result;
		} catch (ParseException e) {
			result.setCode("201");
			result.setMsg("Formato de la fecha invalido");
		} 
		

		return result;

	}
}
