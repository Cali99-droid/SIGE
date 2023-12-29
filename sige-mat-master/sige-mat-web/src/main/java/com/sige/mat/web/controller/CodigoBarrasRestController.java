package com.sige.mat.web.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.sige.mat.dao.AlumnoDAO;
import com.sige.mat.dao.AulaDAO;
import com.sige.mat.dao.FamiliarDAO;
import com.sige.mat.dao.GradDAO;
import com.sige.mat.dao.GradoHorarioDAO;
import com.sige.mat.dao.LecturaBarrasDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.NivelDAO;
import com.sige.mat.dao.PersonaDAO;
import com.tesla.colegio.model.Alumno;
import com.tesla.colegio.model.Aula;
import com.tesla.colegio.model.Grad;
import com.tesla.colegio.model.GradoHorario;
import com.tesla.colegio.model.LecturaBarras;
import com.tesla.colegio.model.Matricula;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.Persona;
import com.tesla.frmk.rest.util.AjaxResponseBody;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;
import com.tesla.frmk.util.CodigoBarrasUtil;
import com.tesla.frmk.util.FechaUtil;

@RestController
@RequestMapping(value = "/api/codigoBarras")
public class CodigoBarrasRestController {
	final static Logger logger = Logger.getLogger(CodigoBarrasRestController.class);

	@Autowired
	private MatriculaDAO matriculaDAO;
	
	@Autowired
	private FamiliarDAO familiarDAO;

	@Autowired
	private LecturaBarrasDAO lecturaBarrasDAO;

	@Autowired
	private GradoHorarioDAO gradohorarioDAO;
	
	@Autowired
	private AulaDAO aulaDAO;
	
	@Autowired
	private AlumnoDAO alumnoDAO;
	
	@Autowired
	private NivelDAO nivelDAO;
	
	@Autowired
	private GradDAO gradDAO;
	
	@Autowired
	private PersonaDAO personaDAO;

	/**
	 * Exportar a excel la seccion con su codigo de barras
	 * @param response
	 * @param id_au
	 * @throws Exception
	 */
	@RequestMapping(value = "/seccion/{id_au}")
	@ResponseBody
	public void getLista(HttpServletResponse response, @PathVariable Integer id_au) throws Exception {

		Aula aula = aulaDAO.getFull(id_au, new String[] { Grad.TABLA, Nivel.TABLA });

		String grado = aula.getGrad().getNivel().getNom() + "-" + aula.getGrad().getNom() + "-" + aula.getSecc();

		List<Map<String, Object>> listaAlumnos = matriculaDAO.reporteCodigoBarras(id_au);
		for (Map<String, Object> map : listaAlumnos) {
			String nombre = map.get("nom").toString();
			//nombre = nombre.split(" ")[0];
			map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
			if (nombre.length()>27)
				nombre = nombre.substring(0, 27);
			map.put("nombre", nombre);
			map.put("grado", grado);
			map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
		}


		CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

        float[] _margenes = new float[]{0,5,2,10};
        float _width = 134;
        float _fontSize = 8;
        
		InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

		IOUtils.copy(is, response.getOutputStream());

	}	

	

	/**
	 * Exportar a excel la seccion con su codigo de barras
	 * @param response
	 * @param id_au
	 * @throws Exception
	 */
	@RequestMapping(value = "/alumno/{id_mat}")
	@ResponseBody
	public void getAlumno(HttpServletResponse response, @PathVariable Integer id_mat) throws Exception {



		List<Map<String, Object>> listaAlumnos = matriculaDAO.alumnoCodigoBarras(id_mat);
		for (Map<String, Object> map : listaAlumnos) {
			
			String grado = map.get("nivel") + "-" + map.get("grado") + "-" + map.get("secc");

			String nombre = map.get("nom").toString();
			//nombre = nombre.split(" ")[0];
			map.put("alumno", map.get("ape_pat") + " " + map.get("ape_mat") + ",");
			if (nombre.length()>27)
				nombre = nombre.substring(0, 27);
			map.put("nombre", nombre);
			map.put("grado", grado);
			map.put("cod", map.get("cod") + "-" + String.format("%02d", Integer.parseInt(map.get("periodo").toString())));
		}


		CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();

        float[] _margenes = new float[]{0,5,2,10};
        float _width = 134;
        float _fontSize = 8;
        
		InputStream is = codigoBarras.createPdf(_width, _margenes, _fontSize, listaAlumnos);

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=seccion.pdf");

		IOUtils.copy(is, response.getOutputStream());

	}	
	
	/**
	 * Exportar a excel la seccion con su codigo de barras
	 * @param response
	 * @param id_au
	 * @throws Exception
	 */
	@RequestMapping(value = "/accesoIntranet/{id_mat}")
	@ResponseBody
	public void getFamiliarAccesoIntranet(HttpServletResponse response, @PathVariable Integer id_mat) throws Exception {



		List<Row> listaFamiliares = familiarDAO.claveIntranet(id_mat);
		
		for (Row map : listaFamiliares) {
			String nombre = map.get("nom").toString();
			String apellidos = map.get("ape_pat") + " " + map.get("ape_mat");
			if (apellidos.length()>24)
				apellidos = apellidos.substring(0, 24);
			if (nombre.length()>24)
				nombre = nombre.substring(0, 24);
			map.put("familiar",apellidos);
			map.put("nombre", nombre);
			map.put("nro_doc",map.get("nro_doc"));
			map.put("grado", map.get("nivel") + "-" + map.getString("grado") + "-" + map.getString("secc"));
		}


		CodigoBarrasUtil codigoBarras = new CodigoBarrasUtil();
        float[] _margenes = new float[]{0,5,0,10};
        float _width = 134;
        float _fontSize = 7;

		InputStream is = codigoBarras.createPdfApoderado(_width, _margenes, _fontSize, listaFamiliares);

		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "attachment; filename=familiar.pdf");

		IOUtils.copy(is, response.getOutputStream());

	}	

	
	// @PostMapping("/txt/upload")
	@RequestMapping(method = RequestMethod.POST, value = "/txt/upload/{id_anio}")
	public AjaxResponseBody uploadFile(@RequestParam("file") MultipartFile uploadfile,@RequestParam("fecha") String fecha, @PathVariable Integer id_anio) {

		AjaxResponseBody result = new AjaxResponseBody();

		if (uploadfile.isEmpty()) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo es vacio");
			return result;
		}
		int total = 0;

		try {

			//formato de la fecha del archivo
			String formato = "h:m:s a M/dd/yy";
			SimpleDateFormat formatter = new SimpleDateFormat(formato);
			//recorremos la linea desde el archivo
			InputStream is = uploadfile.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line = null;
			
			while ((line = br.readLine()) != null) {
				String arr[] = line.split(",");
				String arrCodigoBarras[] =  arr[0].split("-");
				String codigo =  arrCodigoBarras[0];
				String periodo =  arrCodigoBarras[1];
				//logger.debug(line);
				//if (isNumeric(codigo) == true && isNumeric(periodo)){//Esto no funciona en el 2022
					total ++;
					//String arr[] = line.split(",");
					//String codigo = arr[0];

					//String periodo  = arr[1];

					String fecha_ori  = arr[2];
					//String fecha_lectura=arr[3];
					//String codigo = line.substring(0, 8);
					
					//String periodo = line.substring(9, 11);
					//String fecha_ori = line.substring(20).trim();
					Date date1= FechaUtil.toDate(fecha);
					
					List<Map<String, Object>> asistencia= lecturaBarrasDAO.getAsistencia(codigo, date1);
					
					//LecturaBarras lectura = lecturaBarrasDAO.getByParams(param);//El codigo y fecha ya existe en bd
					LecturaBarras lectura= new LecturaBarras();
					if(asistencia.size()>0 && asistencia.get(0).get("fecha_ori").equals("00:00:00")){
						lectura.setId(Integer.parseInt(asistencia.get(0).get("id").toString()));
						lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_ori).getTime()));
						//lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_lectura).getTime()));
						lectura.setFecha_ori(fecha_ori);
						lecturaBarrasDAO.saveOrUpdate(lectura);
					}  else if (asistencia.size()==0){
						lectura= new LecturaBarras();
						lectura.setCodigo(codigo);
						lectura.setId_per(Integer.parseInt(periodo));
						lectura.setFecha_ori(fecha_ori);
						lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_ori).getTime()));
						//lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_lectura).getTime()));
						lectura.setEst("A");
						
						lecturaBarrasDAO.saveOrUpdate(lectura);
					}
					
				//}
			}

			br.close();

			//actualiar a tardanza o puntual
			//String fec="2018-03-09";
			Date date1= FechaUtil.toDate(fecha);
			actualizarTardanzaAsistencia(date1,id_anio);
			//actualizarTardanzaAsistencia(date1);
			
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("archivo con errores");
			e.printStackTrace();
			return result;
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", total);
		result.setResult(map);
		return result;
	}
	
	@Transactional
	@RequestMapping(method = RequestMethod.POST, value = "/registrarAsistencia")
	public AjaxResponseBody registrarAsistencia(@RequestParam String lect_ing) {

		AjaxResponseBody result = new AjaxResponseBody();

		
		try {

			
				String arr[] = lect_ing.trim().split("-");
				String codigo =  arr[0];
				String periodo =  arr[1];
				//Buscamos si existe en la BD del colegio
				Alumno alu= alumnoDAO.getByParams(new Param("cod",codigo));
				Persona per = personaDAO.getByParams(new Param("id", alu.getId_per()));
				
				if(alu==null) {
					result.setCode("ARCHIVO");
					result.setMsg("El alumno no pertenece al colegio");
					return result;
				}	
				String alumno = per.getApe_pat().toUpperCase()+" "+per.getApe_mat().toUpperCase()+" "+per.getNom();
				String nivel="";
				String grado="";
				String aula ="";
				String reg_asis ="";
				String hora_mar="";
				String flag_tard="";
				//logger.debug(line);
				//if (isNumeric(codigo) == true && isNumeric(periodo)){//Esto no funciona en el 2022
				
					//String arr[] = line.split(",");
					//String codigo = arr[0];

					//String periodo  = arr[1];

					//String fecha_ori  = arr[2];
					//String fecha_lectura=arr[3];
					//String codigo = line.substring(0, 8);
					
					//String periodo = line.substring(9, 11);
					//String fecha_ori = line.substring(20).trim();
					//Date date1= FechaUtil.toDate(fecha);
				//formato de la fecha del archivo
				String formato = "h:m:s a M/dd/yy";
				SimpleDateFormat formatter = new SimpleDateFormat(formato);
				 SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy");
				 SimpleDateFormat dtf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				 
				// Date d=new Date(new Date().getTime()+28800000); 
				 
					Date fec_actual=new Date(); 
					String fec_act_fr=dtf.format(fec_actual);
					//String fec=fec_actual.toString();
					String fecha_ori=formatter.format(fec_actual);
					
					List<Map<String, Object>> asistencia= lecturaBarrasDAO.getAsistencia(codigo, FechaUtil.toDate(fec_act_fr));
					
					//LecturaBarras lectura = lecturaBarrasDAO.getByParams(param);//El codigo y fecha ya existe en bd
					LecturaBarras lectura= new LecturaBarras();
					if(asistencia.size()>0){
						//result.setCode("ARCHIVO");
						//result.setMsg("Ya se encuentra registrado su ingreso");
						//return result;
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("existe", 1);
						map.put("mensaje", "Ya se encuentra registrado su ingreso");
						result.setResult(map);		
						return result;
					}  else if (asistencia.size()==0){
						
						
						//Insertamos tardanza o Asistencia
						Param param = new Param();
						param.put("alu.cod", codigo);
						param.put("mat.id_per", periodo);
						List<Matricula> matriculas =matriculaDAO.listFullByParams(param, null);
						
						if(matriculas.size()>0){

							int id_aula= matriculas.get(0).getId_au_asi();
							Nivel niv = nivelDAO.get(matriculas.get(0).getId_niv());
							nivel=niv.getNom();
							Grad gra= gradDAO.get(matriculas.get(0).getId_gra());
							grado=gra.getNom();
							Aula au = aulaDAO.get(matriculas.get(0).getId_au_asi());
							aula=au.getSecc();
							GradoHorario gradoHorario=gradohorarioDAO.getByParams(new Param("id_au", id_aula));

							if (gradoHorario!=null && gradoHorario.getHora_ini()!=null){
								lectura= new LecturaBarras();
								lectura.setCodigo(codigo);
								lectura.setId_per(Integer.parseInt(periodo));
								lectura.setFecha_ori(fecha_ori.toString());
								lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_ori.toString()).getTime()));
								//lectura.setFecha( new java.sql.Timestamp(formatter.parse(fecha_lectura).getTime()));
								lectura.setEst("A");
								
								Integer id_lec=lecturaBarrasDAO.saveOrUpdate(lectura);
								
				            	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
					            Date horaIni;
					            Date horaMar;
					            horaIni = dateFormat.parse(gradoHorario.getHora_ini());
					            horaMar = dateFormat.parse(dateFormat.format(lectura.getFecha()));
					           // hora_mar=horaMar.getTime().toString();
				            	 if (horaMar.before(horaIni) || horaMar.equals(horaIni)) {
				                 	lecturaBarrasDAO.actualizarAsistencia("A", id_lec);
				                 	reg_asis="ASISTENCIA";
				                 	flag_tard="0";
				                 } else{
				                 	lecturaBarrasDAO.actualizarAsistencia("T", id_lec);
				                 	reg_asis ="TARDANZA";
				                 	flag_tard="1";
				                 }
				            
							} else {
								result.setCode("ARCHIVO");
								result.setMsg("No existe configurado el horario para el aula al que pertenece el alumno ");
								return result;
							}
							
						}else{
							System.err.println("No se proceso:" + codigo);
						}
					}
					
				//}
			

			//actualiar a tardanza o puntual
			//String fec="2018-03-09";
			//actualizarTardanzaAsistencia(date1);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("alumno", alumno.toUpperCase());
			map.put("aula", nivel.toUpperCase()+" "+grado.toUpperCase()+" "+aula.toUpperCase());
			map.put("registro_asistencia", reg_asis);
			map.put("hora", dtf2.format(lectura.getFecha()));
			map.put("existe", 0);
			map.put("tardanza", flag_tard);
			result.setResult(map);		
			return result;
		} catch (Exception e) {
			result.setCode("ARCHIVO");
			result.setMsg("No se reconoce el código");
			e.printStackTrace();
			return result;
		}

		
	}
	
	
	private boolean isNumeric(String cadena) {
		 boolean resultado;
		  try {
	            Integer.parseInt(cadena);
	            resultado = true;
	        } catch (NumberFormatException excepcion) {
	            resultado = false;
	        }

	        return resultado;
	}

	private Map<String,Object> actualizarTardanzaAsistencia(Date fec, Integer id_anio) throws Exception{

		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			
			List<LecturaBarras> lista_alumnos=lecturaBarrasDAO.alumnosPorFecha(fec);
			int cant_asistencia=0;
			int cant_tardanzas=0;
			for (int i = 0; i < lista_alumnos.size(); i++) {
				
	            if(lista_alumnos.get(i).getCodigo()!=null ){
	            	
					String codigo_alu=lista_alumnos.get(i).getCodigo();
					int id_per=lista_alumnos.get(i).getId_per();
					Param param = new Param();
					param.put("alu.cod", codigo_alu);
					param.put("mat.id_per", id_per);
					List<Matricula> matriculas =matriculaDAO.listFullByParams(param, null);
					
					if(matriculas.size()>0){

						int id_aula= matriculas.get(0).getId_au_asi();
						GradoHorario gradoHorario=gradohorarioDAO.getByParams(new Param("id_au", id_aula));

						if (gradoHorario!=null && gradoHorario.getHora_ini()!=null && !lista_alumnos.get(i).getFecha_ori().equals("00:00:00")){

			            	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				            Date horaIni;
				            Date horaMar;
				            horaIni = dateFormat.parse(gradoHorario.getHora_ini());
				            horaMar = dateFormat.parse(lista_alumnos.get(i).getFecha_ori());
			            	 if (horaMar.before(horaIni) || horaMar.equals(horaIni)) {
			                 	lecturaBarrasDAO.actualizarAsistencia("A", lista_alumnos.get(i).getId());
			                 	cant_asistencia++;
			                 } else{
			                 	lecturaBarrasDAO.actualizarAsistencia("T", lista_alumnos.get(i).getId());
			                 	cant_tardanzas++;
			                 }
						}
						
					}else{
						System.err.println("No se proceso:" + codigo_alu);
					}
					
	            }    
			}
			
			insertar_falta( fec,id_anio);
			map.put("asistencias_procesadas",cant_asistencia);
			map.put("tardanzas_procesadas",cant_tardanzas);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Fallo al actualizar las puntualidades y  tardanzas");
		}
		return map;

	}	
	
	
	public AjaxResponseBody  insertar_falta(Date fec,Integer id_anio){
		//fec="2018-03-13";
		//id_suc=2;
		//id_niv=1;
		AjaxResponseBody result = new AjaxResponseBody();
		try{
		List<Map<String, Object>> matriculados=matriculaDAO.matriculadosNivLocal(id_anio);
		for (Map<String, Object> map : matriculados) {
			String codigo = map.get("codigo").toString();
			Integer id_aula= Integer.parseInt(map.get("id_au_asi").toString());
			//GradoHorario gradoHorario=gradohorarioDAO.getByParams(new Param("id_au", id_aula));
			
	        //String date = new SimpleDateFormat("yyyy-MM-dd").format(date1);
			//String formato_date = "yyyy-MM-dd HH:mm:ss ";
			//SimpleDateFormat formatter = new SimpleDateFormat(formato);
			//SimpleDateFormat formatter_date = new SimpleDateFormat(formato_date);
	        //Date date = formatter.parse(fec);
	      //  Date fecha= formatter_date.parse(fec);
			List<Map<String, Object>> asistencia=lecturaBarrasDAO.getAsistencia(codigo, fec);
			String fecha_ori="00:00:00";
			if(asistencia.size()==0 ){
				LecturaBarras lectura_barras=new LecturaBarras();
				lectura_barras.setCodigo(codigo);
				lectura_barras.setId_per( Integer.parseInt(map.get("id_per").toString()));
				lectura_barras.setAsistencia("F");
				lectura_barras.setFecha_ori(fecha_ori);
				lectura_barras.setFecha(fec);
				lectura_barras.setEst("A");
				lectura_barras.setFec_ins(new Date());
				lectura_barras.setUsr_ins(new Integer(1));
				result.setResult(lecturaBarrasDAO.save(lectura_barras));
			}
			
		} 
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;	
	}
	
	
	public static void main (String ars[]) throws Exception{
		String fecha="4:50:24 PM  3/06/18";
		String formato = "h:m:s a M/dd/yy";
		//String fecha="3/06/18";
		//String formato = "M/dd/yy";
		 SimpleDateFormat formatter = new SimpleDateFormat(formato);
         Date date = formatter.parse(fecha);

         logger.debug(date);
	}

}
