package com.sige.spring.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.mat.dao.AcademicoPagoDAO;
import com.sige.mat.dao.AlumnoDescuentoDAO;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.ConfMensualidadDAO;
import com.sige.mat.dao.DescHnoDAO;
import com.sige.mat.dao.MatriculaDAO;
import com.sige.mat.dao.SituacionMatDAO;
import com.sige.mat.dao.SolicitudDAO;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Matricula;
import com.tesla.frmk.sql.Param;

@Service
public class FamiliarService {


	@Transactional
	public void actualizarVafificacionFamiliar(){
		
	}

}
