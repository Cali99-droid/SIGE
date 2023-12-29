package com.sige.mat.dao;

import java.util.List;

import com.tesla.colegio.model.Rec;
import com.tesla.frmk.sql.Param;


/**
 * Define m�todos DAO operations para la entidad rec.
 * @author MV
 *
 */
public interface RecDAO {
	
    /**
     * Actualizar o grabar
     * @param rec
     * @return Id 
     */	
	public int saveOrUpdate(Rec rec);

    /**
     * Eliminar rec
     * @param id
     */	
	public void delete(int id);
	
	/**
	 * Obtener rec
	 * @param id
	 * @return rec
	 */	
	public Rec get(int id);

	/**
	 * Obtener rec
	 * @param id
	 * @param tablas Tablas que se agrega con inner join
	 * @return rec
	 */	
	public Rec getFull(int id, String tablas[]);

	/**
	 * Lista todos los rec
	 * @return Lista de rec
	 */	
	public List<Rec> list();
	
	/**
	 * Obtiene el primer rec	
	 * @param param
	 * @return rec	
	 */
	public Rec getByParams(Param param);

	/**
	 * Obtiene la lista de rec por condiciones.
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de rec
	 */
	public List<Rec> listByParams(Param param, String[] order);

	/**
	 * Obtiene la lista de rec por condiciones, pero con sus FK llenos
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de rec
	 */
	public List<Rec> listFullByParams(Param param, String[] order);

	/**
	 * Obtiene la lista de rec por condiciones, pero con sus FK llenos
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de rec
	 */
	public List<Rec> listFullByParams(Rec rec, String[] order);
	
	
}
