package com.tesla.colegio.model;

import com.tesla.frmk.model.EntidadBase;
import java.util.List;

/**
 * Clase entidad que representa a la tabla pag_pago_realizado
 * @author MV
 *
 */
public class PagoRealizado extends EntidadBase{

	public final static String TABLA = "pag_pago_realizado";
	private Integer id;
	private String num_rec;
	private java.math.BigDecimal total;
	private Integer id_mat;
	private Matricula matricula;	
	private List<PagoDetalle> pagodetalles;

	public PagoRealizado(){
	}

	/**
	* Obtiene Llave Principal 
	* @return id
	*/
	public Integer getId(){
		return id;
	}	

	/**
	* Llave Principal 
	* @param id
	*/
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	* Obtiene Nmero de Recibo 
	* @return num_rec
	*/
	public String getNum_rec(){
		return num_rec;
	}	

	/**
	* Nmero de Recibo 
	* @param num_rec
	*/
	public void setNum_rec(String num_rec) {
		this.num_rec = num_rec;
	}

	/**
	* Obtiene Total 
	* @return total
	*/
	public java.math.BigDecimal getTotal(){
		return total;
	}	

	/**
	* Total 
	* @param total
	*/
	public void setTotal(java.math.BigDecimal total) {
		this.total = total;
	}

	/**
	* Obtiene Matrcula del alumno 
	* @return id_mat
	*/
	public Integer getId_mat(){
		return id_mat;
	}	

	/**
	* Matrcula del alumno 
	* @param id_mat
	*/
	public void setId_mat(Integer id_mat) {
		this.id_mat = id_mat;
	}

	public Matricula getMatricula(){
		return matricula;
	}	

	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	/**
	* Obtiene lista de Pago detalle 
	*/
	public List<PagoDetalle> getPagoDetalles() {
		return pagodetalles;
	}

	/**
	* Seta Lista de Pago detalle 
	* @param pagodetalles
	*/	
	public void setPagoDetalle(List<PagoDetalle> pagodetalles) {
		this.pagodetalles = pagodetalles;
	}
}