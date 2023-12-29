package com.sige.mat.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.sige.mat.dao.OpcionDAO;
import com.sige.mat.dao.RolOpcionDAO;
import com.sige.rest.request.RolOpcionRequest;
import com.tesla.colegio.model.RolOpcion;
import com.tesla.colegio.model.bean.MenuItem;


@RestController
@RequestMapping(value = "/api/rolOpcion")
public class RolOpcionRestController {
	
	@Autowired
	private OpcionDAO opcionDAO;

	
	@Autowired
	private RolOpcionDAO rolOpcionDAO;

 

	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabarRolOpcion(@RequestBody RolOpcionRequest rolOpcion) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			Param param = new Param();
			param.put("id_rol", rolOpcion.getId());//id del rol
			List<RolOpcion> rolOpciones = rolOpcionDAO.listByParams(param,null);
			
			//nuevos
			List<Integer> idRolOpcionNuevos = new ArrayList<Integer>();
			
			for (Integer id : rolOpcion.getOpciones()) {
				boolean encontro = false;
				for (RolOpcion rolOpcion2 : rolOpciones) {
					if (id.equals(rolOpcion2.getId_opc()))
						encontro = true;
				}	
				if (!encontro)
					idRolOpcionNuevos.add(id);
			}
			
			//insertar nuevos
			for (Integer id : idRolOpcionNuevos) {
				RolOpcion rolOpcion1 = new RolOpcion();
				rolOpcion1.setEst("A");
				rolOpcion1.setId_opc(id);
				rolOpcion1.setId_rol(rolOpcion.getId());
				rolOpcionDAO.saveOrUpdate(rolOpcion1);
			}
			
			//ELIMINAR
			for (RolOpcion rolOpcion1 : rolOpciones) {
				boolean encontro = false;
				for (Integer id : rolOpcion.getOpciones()) {
					if (rolOpcion1.getId_opc().equals(id))
						encontro = true;
						
				}
				
				if (!encontro)
					rolOpcionDAO.delete(rolOpcion1.getId());
				
			}
		 
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	

	@RequestMapping( value="/menuTree/{id_rol}", method = RequestMethod.GET)
	public List<MenuItem> menuTree(@PathVariable Integer id_rol ) {

		AjaxResponseBody result = new AjaxResponseBody();
		List<MenuItem> padres = new ArrayList<MenuItem>();
		try {


			List<Row> opciones = opcionDAO.getListMenuRol(id_rol);
			
			//1er nivel
			
			for (Row opcion : opciones ) {
				if (opcion.getInteger("id_opc")==null){
					opcion.put("niv",1);
					//padres.add(opcion);
					MenuItem menuPadre =new MenuItem();
					menuPadre.setTitle(opcion.getString("nom"));
					menuPadre.setKey(opcion.getInteger("id"));
					menuPadre.setExpanded(true);
					
					if(opcion.getInteger("id_rop")!=null)
						menuPadre.setSelected(true);
					padres.add(menuPadre);
				}
					
				 
			}
		
			//2do nivel
			for (Row opcion : opciones ) {
			
				MenuItem opcionPadre = null;
				for (MenuItem padre : padres ) {
					if (opcion.getInteger("id_opc")==padre.getKey())
						opcionPadre = padre;
				}
				if (opcionPadre!=null){ 
					opcion.put("niv",2);
					if(opcionPadre.getChildren()==null)
						opcionPadre.setChildren(new ArrayList<MenuItem>());
					
					MenuItem menuHijo =new MenuItem();
					menuHijo.setTitle(opcion.getString("nom"));
					menuHijo.setKey(opcion.getInteger("id"));
					if(opcion.getInteger("id_rop")!=null)
						menuHijo.setSelected(true);
					opcionPadre.getChildren().add(menuHijo);
				} 
				
			}
		
			//3er nivel
			for (Row opcion : opciones ) {
				if (opcion.get("niv")==null){
			
					for (MenuItem opcion1 : padres ) {
						if (opcion1.getKey().equals(5)){
							// (opcion1.toString());
						}
						if(opcion1.getChildren()!=null)
						for (MenuItem opcion2 : opcion1.getChildren() ) {
							if (opcion2.getKey().equals(opcion.getInteger("id_opc"))){
								opcion.put("niv",3);
								//opcion2.getOpcions().add(opcion);
								
								if(opcion2.getChildren()==null)
									opcion2.setChildren(new ArrayList<MenuItem>());
								
								MenuItem menuHijo =new MenuItem();
								menuHijo.setTitle(opcion.getString("nom"));
								menuHijo.setKey(opcion.getInteger("id"));
								if(opcion.getInteger("id_rop")!=null)
									menuHijo.setSelected(true);
								opcion2.getChildren().add(menuHijo);
							}
						}
					}
				}
			}
				
			
			
		} catch (Exception e) {
			result.setException(e);
		}
		
		return padres;

	}	
}
