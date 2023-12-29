package com.sige.spring.service;

import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.IndicadorDAO;
import com.sige.mat.dao.NotaDAO;
import com.sige.mat.dao.NotaDesDAO;
import com.sige.mat.dao.NotaIndicadorDAO;
import com.sige.mat.dao.NotaUpdDAO;
import com.sige.mat.dao.PermisoDocenteDAO;
import com.sige.mat.dao.PermisosDAO;
import com.sige.mat.dao.PromedioComDAO;
import com.sige.rest.request.EvaluacionReq;
import com.sige.rest.request.NotaAlumnoReq;
import com.sige.rest.request.NotaAlumnoUpdateReq;
import com.sige.rest.request.NotaAulaReq;
import com.tesla.colegio.model.Nota;
import com.tesla.colegio.model.NotaDes;
import com.tesla.colegio.model.NotaIndicador;
import com.tesla.colegio.model.NotaUpd;
import com.tesla.colegio.model.PromedioCom;
import com.tesla.colegio.model.bean.PromedioBean;
import com.tesla.frmk.common.exceptions.ServiceException; 
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

@Service
public class NotaService {

	final static Logger logger = Logger.getLogger(NotaService.class);
	
	@Autowired
	private NotaIndicadorDAO notaIndicadorDAO;
	
	@Autowired
	private NotaDAO notaDAO;
	
	@Autowired
	private IndicadorDAO indicadorDAO;
	
	@Autowired
	private PermisoDocenteDAO permisoDocenteDAO;
	
	@Autowired
	private NotaUpdDAO notaUpdDAO;
	
	@Autowired
	private NotaDesDAO notaDesDAO;
	
	@Autowired
	private PromedioComDAO promedioComDAO;
	
	@Transactional
	public void grabarNota(EvaluacionReq evaluacionReq) throws ServiceException {


		Map<Integer,BigDecimal> peso = new HashMap<Integer,BigDecimal>();
		BigDecimal tota_peso = new BigDecimal(0);
		 
		for(int i=0; i<evaluacionReq.getId_ind().length; i++){
			Integer id_ind =evaluacionReq.getId_ind()[i];
			List<Row> pesos = indicadorDAO.obtenerPeso(id_ind);
			
			if(pesos==null || pesos.size()!=1){
				throw new ServiceException("El indicador con codigo:" + id_ind + " no tiene un peso correspondiente");
			}
			
			String peso_string = pesos.get(0).get("peso").toString();
			BigDecimal peso_ind = new BigDecimal(peso_string);
			tota_peso = tota_peso.add( peso_ind);
			peso.put(id_ind, peso_ind);
		}
		
		//try {
			
			for (NotaAlumnoReq notaAlumnoReq : evaluacionReq.getNotaAlumno()) {
				Nota nota = new Nota();
				nota.setId_ne(evaluacionReq.getId_eva());
				nota.setId_tra(evaluacionReq.getId_tra());
				nota.setId_alu(notaAlumnoReq.getId_alu());
				nota.setFec(new Date());
				nota.setEst("A");
				//aun ver como sera el promedio
				nota.setProm(new BigDecimal(0));
				Param param = new Param();
				param.put("id_alu", notaAlumnoReq.getId_alu());
				param.put("id_ne", evaluacionReq.getId_eva());
				if( notaDAO.getByParams(param)!=null){
					Integer id_nota=notaDAO.getByParams(param).getId();
					nota.setId(id_nota);
				}
				Integer id_not=notaDAO.saveOrUpdate(nota);
				nota.setId(id_not);
				BigDecimal total = new BigDecimal(0);
				
				for(int i=0; i<evaluacionReq.getId_ind().length; i++){
					Integer notaIndicador_nota = notaAlumnoReq.getNotas()[i];
					if(notaIndicador_nota==null)
						notaIndicador_nota  = 0;
					total =  total.add( new BigDecimal( notaIndicador_nota).multiply(peso.get(evaluacionReq.getId_ind()[i])));
					NotaIndicador notaIndicador= new NotaIndicador();
					notaIndicador.setId_not(id_not);
					notaIndicador.setId_nie(evaluacionReq.getId_ind()[i]);
					notaIndicador.setNota(notaIndicador_nota);
					notaIndicador.setEst("A");
					
					notaIndicadorDAO.saveOrUpdate(notaIndicador);
				}
				
				nota.setProm(total.divide(tota_peso,2));
				
				notaDAO.actualizaPromedio(nota);
				
			}			
			//result.setResult(1);
		
		//return result;

	}	
	
	
	@Transactional
	public void grabarNotaDesempeniosCom(NotaAulaReq notaAulaReq) throws ServiceException {


		for(int i=0; i<notaAulaReq.getNotaDesempenioReq().length; i++){
			Integer id_not_des=notaAulaReq.getNotaDesempenioReq()[i].getId_not_des();
			Integer id_alu=notaAulaReq.getNotaDesempenioReq()[i].getId_alu();
			Integer id_tra=notaAulaReq.getNotaDesempenioReq()[i].getId_tra();
			Integer id_desau=notaAulaReq.getNotaDesempenioReq()[i].getId_desau();
			Integer nota=notaAulaReq.getNotaDesempenioReq()[i].getNota();
			if(nota==null && id_not_des!=null) {
				//elimino la nota
				notaDesDAO.delete(id_not_des);
			} else {
				NotaDes notaDes = new NotaDes();
				if(id_not_des!=null) {
					notaDes.setId(id_not_des);
				}
				notaDes.setId_alu(id_alu);
				notaDes.setId_desau(id_desau);
				notaDes.setId_tra(id_tra);
				notaDes.setFec(new Date());
				notaDes.setNota(nota);
				notaDes.setEst("A");
				notaDesDAO.saveOrUpdate(notaDes);
			}
						
		}
		
		for(int i=0; i<notaAulaReq.getNotaPromCompetenciaReq().length; i++){
			//Integer id_not_prom=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_not_com();
			Integer id=notaAulaReq.getNotaPromCompetenciaReq()[i].getId();
			Integer id_com=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_com();
			Integer id_cpu=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_cpu();
			Integer id_alu=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_alu();
			Integer id_tra=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_tra();
			Integer id_au=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_au();
			Integer id_cua=notaAulaReq.getNotaPromCompetenciaReq()[i].getId_cua();
			BigDecimal prom=notaAulaReq.getNotaPromCompetenciaReq()[i].getProm();
			if(id_cua==null) {
				id_cua=0;
			}
			if(id!=null && prom==null) {
				promedioComDAO.deletePromedioComxCompetencia(id_au, id_cpu, id_cua, id_com,id_alu);
			} else {
				//Busco si ha existido para el periodo
				Param param = new Param();
				param.put("id_com", id_com);
				param.put("id_au", id_au);
				param.put("id_alu", id_alu);
				param.put("id_cpu", id_cpu);
				if(id_cua!=null) {
					param.put("id_cua", id_cua);
				}
				PromedioCom promExiste=promedioComDAO.getByParams(param);
				PromedioCom promedioCom = new PromedioCom();
				if(promExiste!=null) {
					promedioCom.setId(promExiste.getId());
				}
				promedioCom.setId_alu(id_alu);
				promedioCom.setId_com(id_com);
				promedioCom.setId_tra(id_tra);
				promedioCom.setId_alu(id_alu);
				promedioCom.setId_cpu(id_cpu);
				promedioCom.setId_au(id_au);
				promedioCom.setFec(new Date());
				promedioCom.setProm(prom);
				promedioCom.setEst("A");
				if(id_cua!=null) {
					promedioCom.setId_cua(id_cua);
				}
				promedioComDAO.saveOrUpdate(promedioCom);
			}
						
		}
		 		
			//result.setResult(1);
		
		//return result;

	}
	

	@Transactional
	public void actualizarNota(NotaAlumnoUpdateReq[] notaAlumnoUpdateReq, Integer per) throws ServiceException{
		 
 
			//Map<Integer,BigDecimal> peso = new HashMap<Integer,BigDecimal>();
			
			Integer id_not=0;
			
			for (NotaAlumnoUpdateReq notaAlumno : notaAlumnoUpdateReq) {
				Integer id_ni = notaAlumno.getId();
				Integer notaInd = notaAlumno.getNota();
				Integer id_tra = notaAlumno.getId_usr();
				Integer id_ne= notaAlumno.getId_ne();
				Integer id_alu= notaAlumno.getId_alu();
				Integer id_ind= notaAlumno.getId_ind();
				Integer nota_ant=notaIndicadorDAO.getByParams(new Param("id",id_ni)).getNota();
				if(id_ni!=null){
					id_not=notaIndicadorDAO.getByParams(new Param("id",id_ni)).getId_not();
					notaDAO.actualizaNotaIndicador(id_ni, notaInd, id_tra);
					//Verificamos si esta dentro de la tabla de permisos y grabamos en auditoria el cambio que hizo
					if(per.equals("1")){
						NotaUpd nota_up = new NotaUpd();
						nota_up.setId_nni(id_ni);
						nota_up.setId_tra(id_tra);
						nota_up.setNota_ant(nota_ant);
						nota_up.setNota_act(notaInd);
						nota_up.setEst("A");
						notaUpdDAO.saveOrUpdate(nota_up);
					}
				}else{
					//es un alumno nuevo a la evaluacion
					//insertar nota
					
					//VER SI TIENE NOTA
					Param param = new Param();
					param.put("id_alu", id_alu);
					param.put("id_ne", id_ne);
					
					Nota nota = notaDAO.getByParams(param);
 					if(nota==null){

						nota = new Nota();
						
						nota.setId_ne(id_ne);
						nota.setId_tra(id_tra);
						nota.setId_alu(id_alu);
						nota.setFec(new Date());
						nota.setEst("A");
						
						//aun ver como sera el promedio
						nota.setProm(new BigDecimal(0));
						id_not  = notaDAO.saveOrUpdate(nota);
						
						nota.setId(id_not);

						NotaIndicador notaIndicador = new NotaIndicador();
						notaIndicador.setEst("A");
						notaIndicador.setId_nie(id_ind);
						notaIndicador.setId_not(id_not);
						notaIndicador.setNota(notaInd);

						notaIndicadorDAO.saveOrUpdate(notaIndicador);
					}else
						id_not = nota.getId();

					
 					BigDecimal total = new BigDecimal(0);
 					BigDecimal tota_peso = new BigDecimal(0);
					
					param = new Param();
					param.put("id_not", id_not);
					
					List<NotaIndicador> notaIndicadores = notaIndicadorDAO.listByParams(param,null);
					
					BigDecimal peso = BigDecimal.ZERO;
					
					if(notaIndicadores.size()>0){
						for (NotaIndicador notaIndicador : notaIndicadores) {
							
							List<Row> pesos = indicadorDAO.obtenerPeso(id_ind);
							
							//logger.debug("ind:" + id_ind);
							
							if(pesos==null || pesos.size()!=1){
								throw new ServiceException("El indicador con codigo:" + id_ind + " no tiene un peso correspondiente");

							}
							
							peso = new BigDecimal(pesos.get(0).getDouble("peso"));
							tota_peso = tota_peso.add(peso);
							
							Integer notaIndicador_nota = notaIndicador.getNota();
							if(notaIndicador_nota==null)
								notaIndicador_nota  = 0;
							
							total =  total.add( new BigDecimal( notaIndicador_nota).multiply(peso));
							 
						}
						
						nota.setProm(total.divide(tota_peso,2));
						
						notaDAO.actualizaPromedio(nota);
					}
					
					
					
					
					  
					//return;
				}
				
			}
			
			for(NotaAlumnoUpdateReq notaAlumno : notaAlumnoUpdateReq){
				Integer id_ni = notaAlumno.getId();
				
				if(id_ni!=null){
					
					id_not=notaIndicadorDAO.getByParams(new Param("id",id_ni)).getId_not();
					List<NotaIndicador> notas_indicadores=notaIndicadorDAO.listByParams(new Param("id_not",id_not), null);
					BigDecimal tota_peso = new BigDecimal(0);
					BigDecimal total = new BigDecimal(0);
					for (int i=0; i<=notas_indicadores.size()-1; i++){
						
						//Integer id_ind=indicadorDAO.obtenerIndicador(notas_indicadores.get(0).getId()).get(0).getInteger("id");
						List<Row> indicadoresList=indicadorDAO.obtenerIndicador(notas_indicadores.get(0).getId());
						//if(indicadoresList.size()>0){
						Integer id_ind=indicadoresList.get(0).getInteger("id");
						List<Row> pesos = indicadorDAO.obtenerPeso(id_ind);
						
						logger.debug("ind:" + id_ind);
						
						if(pesos==null || pesos.size()!=1){
							
							throw new ServiceException("El indicador con codigo:" + id_ind + " no tiene un peso correspondiente");
						}
						String peso_string = pesos.get(0).get("peso").toString();
						BigDecimal peso_ind = new BigDecimal(peso_string);
						logger.debug("peso:" + peso_ind);
						tota_peso = tota_peso.add( peso_ind);
						//peso.put(id_ind, peso_ind);
						total =  total.add( new BigDecimal( notas_indicadores.get(i).getNota()).multiply(peso_ind)); //}
					}
					Nota nota=new Nota();
					nota.setProm(total.divide(tota_peso,2));
					nota.setId(id_not);
					notaDAO.actualizaPromedio(nota);
				}
			}
 
		 
	}
	
}
