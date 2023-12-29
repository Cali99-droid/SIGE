package com.sige.mat.dao;

import java.util.List;

import com.tesla.colegio.model.Gen;
import com.tesla.frmk.sql.Param;


/**
 * Define m�todos DAO operations para la entidad gen.
 * @author MV
 *
 */
public interface GenDAO {
	
    /**
     * Actualizar o grabar
     * @param gen
     * @return Id 
     */	
	public int saveOrUpdate(Gen gen);

    /**
     * Eliminar gen
     * @param id
     */	
	public void delete(int id);
	
	/**
	 * Obtener gen
	 * @param id
	 * @return gen
	 */	
	public Gen get(int id);

	/**
	 * Lista todos los gen
	 * @return Lista de gen
	 */	
	public List<Gen> list();
	
	/**
	 * Obtiene el primer gen	
	 * @param param
	 * @return gen	
	 */
	public Gen getByParams(Param param);

	/**
	 * Obtiene la lista de gen por condiciones.
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de gen
	 */
	public List<Gen> listByParams(Param param, String[] order);


}
