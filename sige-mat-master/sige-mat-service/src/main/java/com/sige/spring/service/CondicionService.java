package com.sige.spring.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sige.core.dao.SQLUtil;
import com.tesla.colegio.model.bean.CondicionBean;
import com.tesla.frmk.sql.Row;

@Service
public class CondicionService {

	Logger logger;
	
	@Autowired
	private SQLUtil sqlUtil;
	

	public List<CondicionBean> mensajeCondicionalumno(Integer id_alu){
		
		String sql = "select c.*, cat.id cat_id, cat.id_ctc   from mat_condicion c "
				+ " inner join mat_matricula m on c.id_mat=m.id "
				+ " left join cat_cond_alumno cat on cat.id= c.id_cond"
				+ " where m.id_alu=? and c.est='A'";
		
		List<Row> rows = sqlUtil.query(sql, new Object[]{id_alu});
		
		List<CondicionBean>  condiciones= new ArrayList<CondicionBean>();
		
		if (rows.size()==0)
			return null;
		
		for (Row row : rows) {
			String mat_blo = row.getString("mat_blo");
			
			if (mat_blo==null || mat_blo.trim().equals("")){
				Integer id_ctc = row.getInteger("id_ctc");
				
				if(id_ctc!=null) {
					CondicionBean condicion = new CondicionBean();
					condicion.setId_ctc(id_ctc);
					condicion.setObs(row.getString("des"));
					
					if (row.getInteger("cat_id").intValue() == 1 || row.getInteger("cat_id").intValue() == 4 )
						condicion.setTipo("M");//MATRICULA CONDICIONADA

					if (row.getInteger("cat_id").intValue() == 2 || row.getInteger("cat_id").intValue() == 5 )
						condicion.setTipo("V");//PIERDE VACANTE
					
					condiciones.add(condicion);
				}
					
					
			}else{//bloqueo
				CondicionBean condicion = new CondicionBean();
				condicion.setObs(row.getString("obs_blo"));
				condiciones.add(condicion);

				condicion.setTipo("B");//BLOQUEO
				condiciones.add(condicion);
				
			}
			
		}
		
		return condiciones;
	}
}
