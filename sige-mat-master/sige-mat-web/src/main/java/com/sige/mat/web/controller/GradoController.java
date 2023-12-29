package com.sige.mat.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.NivelDAO;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.Nivel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Este controllador es llamado desde los jsp 
 * @author Institiucion educativa y ciencias
 *
 */
@RestController
@RequestMapping(value = "/api/grado") 
public class GradoController{

	@Autowired
	private GradDAO gradDAO;


	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private HttpSession httpSession;

	@RequestMapping(value="/GradList")
	public ModelAndView listar(ModelAndView model) throws IOException{
		List<Grad> gradList = gradDAO.listFullByParams(new Param(), null);
		model.addObject("gradList", gradList);
		model.setViewName("grad_list");
		
		return model;
	}
	
	@RequestMapping(value="/GradMant")
	public ModelAndView mant(ModelAndView model) throws IOException{
		List<Grad> gradList = gradDAO.list();
		model.addObject("gradList", gradList);
		model.setViewName("grad_mant");
		
		return model;
	}
	
	@RequestMapping(value = "/GradNuevo", method = RequestMethod.GET)
	public ModelAndView nuevo(ModelAndView model) {
		Grad nuevo = new Grad();
		model.addObject("grad", nuevo);
		cargarListas(model);
		model.setViewName("grad_form");
		
		return model;
	}
	
	@RequestMapping(value = "/GradGrabar", method = RequestMethod.POST)
	public ModelAndView grabar(@ModelAttribute Grad grad) {
		grad.setUsuario(httpSession.getAttribute("usuario"));
		gradDAO.saveOrUpdate(grad);		

		return new ModelAndView("redirect:/GradList");
	}
	
	@RequestMapping(value = "/GradEliminar", method = RequestMethod.GET)
	public ModelAndView eliminar(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		gradDAO.delete(id);
		return new ModelAndView("redirect:/GradList");
	}
	
	@RequestMapping(value = "/GradEditar", method = RequestMethod.GET)
	public ModelAndView editar(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		Grad grad = gradDAO.get(id);
		ModelAndView model = new ModelAndView("grad_form");
		cargarListas(model);
		model.addObject("grad", grad);
		
		return model;
	}
	
	private void cargarListas(ModelAndView model){	
		List<Nivel> nivelList = nivelDAO.listByParams(new Param("est","A"),new String[]{"cod_mod asc"});
		model.addObject("nivelList", nivelList );

	}
	
	@RequestMapping( value="/listarTodosGrados", method = RequestMethod.GET)
	public AjaxResponseBody listarTodosGrados( Integer id_niv) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( gradDAO.listaTodosGrados(id_niv));
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}
}