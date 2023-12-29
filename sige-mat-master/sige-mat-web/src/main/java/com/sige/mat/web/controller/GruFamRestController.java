package com.sige.mat.web.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.frmk.common.exceptions.ControllerException;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.JsonUtil;
import com.tesla.frmk.util.StringUtil;
import com.tesla.colegio.model.Familiar;
import com.tesla.colegio.model.GruFam;
import com.tesla.colegio.model.Usuario;
import com.tesla.colegio.util.Constante;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GruFamDAO;
import com.sige.mat.dao.UsuarioDAO;
import com.sige.rest.request.FamiliaReq;
import com.sige.rest.request.FamiliarReq;


@RestController
@RequestMapping(value = "/api/gruFam")
public class GruFamRestController {
	
	@Autowired
	private GruFamDAO gru_famDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;

	@RequestMapping(value = "/listar")
	public AjaxResponseBody getLista(GruFam gru_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		result.setResult(gru_famDAO.listFullByParams( gru_fam, new String[]{"gpf.id"}) );
		
		return result;
	}

	@Transactional
	@RequestMapping( method = RequestMethod.POST)
	public AjaxResponseBody grabar(GruFam gru_fam) {

		AjaxResponseBody result = new AjaxResponseBody();
		 Integer id_gpf=null;
		try {
			if(gru_fam.getId()==null && gru_fam.getCod().equals("")) {
				//Creamos Codigo de familia
				String codigo = generarCodigoFamilia();
				/*//Verificar que no exista el codigo de familia
				GruFam grupo_familia=gru_famDAO.getByParams(new Param("cod",codigo));
				if(grupo_familia!=null) {
					codigo=generarCodigoFamilia();
				}*/
				gru_fam.setCod(codigo);
				//Creamos su usuario y contraseña de la familia
				String pass=StringUtil.randomInt(6);
				Usuario usuario = new Usuario();
				usuario.setId_per(Constante.PERFIL_FAMILIAR);
				usuario.setLogin(codigo);
				usuario.setPassword(pass);
				usuario.setEst("A");
				Integer id_usr=usuarioDAO.saveOrUpdate(usuario);
				//Actualizamos el id_usr al gru fam
				gru_fam.setId_usr(id_usr);
				id_gpf= gru_famDAO.saveOrUpdate(gru_fam);
			} else {
				id_gpf= gru_famDAO.saveOrUpdate(gru_fam);
			}
			
			//Obtenemos datos del usuario
			Usuario usuario = usuarioDAO.getByParams(new Param("id",gru_fam.getId_usr()));
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("usuario", usuario.getLogin());
			map.put("pass",usuario.getPassword());
			map.put("id_gpf",id_gpf);
			//Buscamos el codigo 
			GruFam gruFam = gru_famDAO.get(id_gpf);
			map.put("cod",gruFam.getCod());
			result.setResult(map);
			
			result.setResult(map);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/verficarFamilia/{nom}", method = RequestMethod.GET)
	public AjaxResponseBody verificarExisteFamilia(@PathVariable String nom ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			GruFam gruFam = gru_famDAO.getByParams(new Param("nom",nom.trim()));
			if(gruFam!=null) {
				result.setResult(1);
			} else {
				result.setResult(0);
			}
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@Transactional
	@RequestMapping(value = "/FamiliaGrabarReq", method = RequestMethod.POST)
	public void grabarFamiliarReq(@RequestBody  FamiliaReq familiaReq, HttpServletResponse response) throws IOException {
		
		Map<String, Object> result= new HashMap<String,Object>();
		
		try{
			//Obtenemos datos de la familia
			GruFam gruFam=gru_famDAO.get(familiaReq.getId());
			gruFam.setId_dist(familiaReq.getId_dist());
			gruFam.setDireccion(familiaReq.getDireccion());
			gruFam.setReferencia(familiaReq.getReferencia());
			gruFam.setId_csal(familiaReq.getId_csal());
			gruFam.setId_seg(familiaReq.getId_seg());
			gruFam.setCod_aseg(familiaReq.getCod_aseg());
			
			Integer id_gpf=gru_famDAO.saveOrUpdate(gruFam);
			
			
			Map<String, Object> map = new HashMap<String,Object>();
			//map.put("error", error);
			map.put("id_gpf", id_gpf);
			
			//System.out.println(map);
			response.setContentType("application/json");
	        response.getWriter().write(JsonUtil.toJson(map));
	        
		}catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.DELETE)
	public AjaxResponseBody eliminar(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			gru_famDAO.delete(id);
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	@RequestMapping( value="/{id}", method = RequestMethod.GET)
	public AjaxResponseBody detalle(@PathVariable Integer id ) {

		AjaxResponseBody result = new AjaxResponseBody();
		 
		try {
			result.setResult( gru_famDAO.get(id) );
		} catch (Exception e) {
			result.setException(e);
		}
		
		return result;

	}	
	
	private String generarCodigoFamilia() {
		//List<Row> Cantidad = familiarDAO.Cantidad_Fam();
		Row ultimo_codigo=familiarDAO.obtenerUltimoCodigodeFamilia();
		String num_ultimo=ultimo_codigo.getString("codigo").toString().replace("F","");
		int correlativo = Integer.parseInt(num_ultimo) + 1;

		/*Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year =Integer.toString(cal.get(Calendar.YEAR)).substring(2);*/

		DecimalFormat myFormatter = new DecimalFormat("00000");
		String correlativoString = myFormatter.format(correlativo);

		String codigo ="F" + correlativoString;

		return codigo;
	}

}
