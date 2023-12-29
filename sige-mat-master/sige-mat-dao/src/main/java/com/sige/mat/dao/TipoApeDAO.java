package com.sige.mat.dao;

import java.util.List;

import com.tesla.colegio.model.TipoApe;
import com.tesla.frmk.sql.Param;


/**
 * Define m�todos DAO operations para la entidad tipo_ape.
 * @author MV
 *
 */
public interface TipoApeDAO {
	
    /**
     * Actualizar o grabar
     * @param tipo_ape
     * @return Id 
     */	
	public int saveOrUpdate(TipoApe tipo_ape);

    /**
     * Eliminar tipo_ape
     * @param id
     */	
	public void delete(int id);
	
	/**
	 * Obtener tipo_ape
	 * @param id
	 * @return tipo_ape
	 */	
	public TipoApe get(int id);

	/**
	 * Lista todos los tipo_ape
	 * @return Lista de tipo_ape
	 */	
	public List<TipoApe> list();
	
	/**
	 * Obtiene el primer tipo_ape	
	 * @param param
	 * @return tipo_ape	
	 */
	public TipoApe getByParams(Param param);

	/**
	 * Obtiene la lista de tipo_ape por condiciones.
	 * 
	 * @param param
	 *            Par�metros para los WHERE
	 * @param order
	 *            Par�metros para los ORDER
	 * @return Lista de tipo_ape
	 */
	public List<TipoApe> listByParams(Param param, String[] order);


}
