package com.sige.mat.dao;


import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.SeguimientoDocDAOImpl;
import com.tesla.colegio.model.SeguimientoDoc;
import com.tesla.frmk.sql.Row;


/**
 * Define m�todos DAO operations para la entidad seguimiento_doc.
 * @author MV
 *
 */
@Repository
public class SeguimientoDocDAO extends SeguimientoDocDAOImpl{
	final static Logger logger = Logger.getLogger(SeguimientoDocDAO.class);

	@Autowired
	SQLUtil sqlUtil;
	
	public List<Row> logEntregaLibretas(Integer id_mat){
		
		String sql = "select s.id, nump nro, cpa.nom periodo, concat(fam.ape_pat, ' ' ,fam.ape_mat, ', ' ,fam.nom) as familiar, "
				+ " DATE_FORMAT(s.fec_ins, '%d/%m/%Y') dia,"
				+ " DATE_FORMAT(s.fec_ins, '%h:%i %p') hora"
				+ " from col_seguimiento_doc s"
				+ " inner join col_per_uni cp on cp.id = s.id_cpu"
				+ " inner join cat_per_aca_nivel cpan on cpan.id = cp.id_cpa"
				+ " inner join cat_periodo_aca cpa on cpa.id = cpan.id_cpa"
				+ " inner join alu_familiar fam on fam.id = s.id_fam"
				+ " where s.id_mat=" + id_mat;
		
		return sqlUtil.query(sql); 
		
	}

	//@Transactional(isolation = Isolation.READ_COMMITTED)
	public int actualizarEntrega(SeguimientoDoc seguimiento )  {
		/*
		seguimiento_doc.setId_fam(familiar.getId());
		seguimiento_doc.setId_cpu(id_cpu);
		seguimiento_doc.setId_mat(matricula.getId());
		seguimiento_doc.setEst("A");
		seguimiento_doc.setTip("L");
		*/
		
		//String sql = "insert into col_seguimiento_doc() archivo= ?,  ";
		String sql = "insert into col_seguimiento_doc ("
				+ "id_fam, "
				+ "id_cpu, "
				+ "id_mat, "
				+ "archivo, "
				+ "tip, "
				+ "est, usr_ins, fec_ins) "
				+ "values ( ?, ?, ?, ?, ?, ?, ?, ?)";

		
		//logger.info(sql);
		
        try {
			LobHandler lobHandler = new DefaultLobHandler(); 

			InputStream inputStream = new ByteArrayInputStream(seguimiento.getArchivo());

			jdbcTemplate.update( sql,
			         new Object[] {
			        		 seguimiento.getId_fam(),
			        		 seguimiento.getId_cpu(),
			        		 seguimiento.getId_mat(),
			        		 new SqlLobValue(inputStream, inputStream.available(), lobHandler),
			        		 seguimiento.getTip(),
			        		 "A",
			        		 seguimiento.getUsr_ins(),
							 new java.util.Date()},
			         new int[] {Types.INTEGER,Types.INTEGER,Types.INTEGER,Types.BLOB, Types.VARCHAR, Types.VARCHAR,Types.INTEGER,Types.TIMESTAMP});
		
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
		return 1;
	}
	
	public byte[] getArchivo(Integer id) {
		return jdbcTemplate.queryForObject("select archivo from col_seguimiento_doc where id=" + id,(rs, rowNum) -> rs.getBytes(1));
	}
	
	public List<Row> reporteEntregaLibretas(Integer id_niv, Integer id_cpu, Integer id_anio){
		String sql = "select "
				+ " concat(alu.ape_pat, ' ',alu.ape_mat,', ', alu.nom) alumno, DATE_FORMAT(s.fec_ins, '%d/%m/%Y') fecha"
				+ " , gra.nom grado, au.secc,"
				+ " concat(f.ape_pat,' ' ,f.ape_mat,', ' , f.nom) familiar,"
				+ " gf.id_par, p.par parentesco"
				+ " from"
				+ " col_seguimiento_doc  s"
				+ " inner join mat_matricula  m on m.id = s.id_mat"
				+ " inner join alu_alumno  alu on alu.id = m.id_alu"
				+ " inner join col_aula au on au.id = m.id_au"
				+ " inner join per_periodo per on per.id = au.id_per"
				+ " inner join cat_grad gra on gra.id = au.id_grad"
				+ " inner join alu_familiar f on f.id= s.id_fam"
				+ " inner join alu_gru_fam_familiar gf on gf.id_fam= f.id"
				+ " inner join alu_gru_fam_alumno ga on ga.id_gpf= gf.id_gpf and ga.id_alu = m.id_alu"
				+ " inner join cat_parentesco p on p.id=gf.id_par"
				+ " where per.id_niv=? and s.id_cpu=? and per.id_anio=?";
		
		return sqlUtil.query(sql, new Object[]{id_niv, id_cpu, id_anio});
	} 
}
