package com.sige.mat.dao;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.NotaCriterioDAOImpl;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad nota_criterio.
 * @author MV
 *
 */
@Repository
public class NotaCriterioDAO extends NotaCriterioDAOImpl{
	
	@Autowired
    private SQLUtil sqlUtil;
	
}
