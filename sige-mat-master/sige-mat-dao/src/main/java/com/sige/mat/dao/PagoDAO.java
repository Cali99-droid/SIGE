package com.sige.mat.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tesla.colegio.model.Pago;
import com.tesla.frmk.sql.Param;


/**
 * Define m�todos DAO operations para la entidad pago.
 * @author MV
 *
 */

public interface PagoDAO {
	
    /**
     * Actualizar o grabar
     * @param pago
     * @return Id 
     */	
	public int saveOrUpdate(Pago pago);

    /**
     * Eliminar pago
     * @param id
     */	
	public void delete(int id);
	
	/**
	 * Obtener pago
	 * @param id
	 * @return pago
	 */	
	public Pago get(int id);

	/**
	 * Obtener pago
	 * @param id
	 * @param tablas Tablas que se agrega con inner join
	 * @return pago
	 */	
	public Pago getFull(int id, String tablas[]);

	/**
	 * Lista todos los pago
	 * @return Lista de pago
	 */	
	public List<Pago> list();
	
	/**
	 * Obtiene el primer pago	
	 * @param param
	 * @return pago	
	 */
	public Pago getByParams(Param param);

	/**
	 * Obtiene la lista de pago por condiciones.
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de pago
	 */
	public List<Pago> listByParams(Param param, String[] order);

	/**
	 * Obtiene la lista de pago por condiciones, pero con sus FK llenos
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de pago
	 */
	public List<Pago> listFullByParams(Param param, String[] order);

	/**
	 * Obtiene la lista de pago por condiciones, pero con sus FK llenos
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de pago
	 */
	public List<Pago> listFullByParams(Pago pago, String[] order);
	
	
}
