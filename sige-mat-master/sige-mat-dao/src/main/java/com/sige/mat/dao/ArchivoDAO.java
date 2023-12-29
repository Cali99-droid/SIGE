package com.sige.mat.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

import com.sige.core.dao.SQLUtil;
import com.sige.mat.dao.impl.AcademicoPagoDAOImpl;
import com.tesla.colegio.model.AcademicoPago;
import com.tesla.colegio.model.Archivo;
import com.tesla.colegio.model.Familiar;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row;

/**
 * Define m�todos DAO operations para la entidad academico_pago.
 * 
 * @author MV
 *
 */
@Repository
public class ArchivoDAO  {

	final static Logger logger = Logger.getLogger(ArchivoDAO.class);
	@Autowired
	private DataSource dataSource;

	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	@Autowired
	private SQLUtil sqlUtil;

	/**
	 * Actualizacion por codigo
	 * @param codigo
	 * @param inputStream
	 * @return
	 */
	public int saveOrUpadte(Archivo archivo, InputStream inputStream) throws DAOException{

		String sql = "select count(1) total from col_archivo where codigo=?";
		
		Integer total = sqlUtil.queryForObject(sql, new Object[] {archivo.getCodigo()},Integer.class);
		
		if(total.intValue()==0)
			sql = "insert into col_archivo(codigo,archivo, est, fec_ins,usr_ins)values(?,?,?,?,?)";
		else
			sql = "update col_archivo set archivo=? , fec_act=?, usr_act=? where codigo=?";
			
		try {
			LobHandler lobHandler = new DefaultLobHandler();

			if(total==0) {
				jdbcTemplate.update(sql, new Object[] { archivo.getCodigo(),
						new SqlLobValue(inputStream, inputStream.available(), lobHandler),
						"A",
						new Date(),
						archivo.getUsr_ins()},
						new int[] { Types.VARCHAR, Types.BLOB,Types.CHAR, Types.DATE, Types.INTEGER });
				
			}else {
				jdbcTemplate.update(sql, new Object[] { 
						new SqlLobValue(inputStream, inputStream.available(), lobHandler),
						new Date(),
						archivo.getUsr_act(),
						archivo.getCodigo()},
						new int[] { Types.BLOB, Types.DATE, Types.INTEGER, Types.VARCHAR });
				
			}

		} catch (DataAccessException e) {
			new DAOException("Error de acceso al archivo",e);
		} catch (FileNotFoundException e) {
			new DAOException("Archivo no encontrado",e);
		} catch (IOException ie) {
			new DAOException("Error de escritura",ie);
		}
		return 1;
	}
	
	
	/**
	 * Actualizacion por codigo
	 * @param codigo
	 * @param inputStream
	 * @return
	 */
	public int update(String codigo, InputStream inputStream) throws DAOException{

		String sql = "update col_archivo set archivo=? where codigo=?";


		try {
			LobHandler lobHandler = new DefaultLobHandler();

			jdbcTemplate.update(sql, new Object[] { new SqlLobValue(inputStream, inputStream.available(), lobHandler),codigo },
					new int[] { Types.BLOB,Types.VARCHAR });

		} catch (DataAccessException e) {
			new DAOException("Error de acceso al archivo",e);
		} catch (FileNotFoundException e) {
			new DAOException("Archivo no encontrado",e);
		} catch (IOException ie) {
			new DAOException("Error de escritura",ie);
		}
		return 1;
	}
	
	
	/**
     * Ver foto
     * @param familiar
     * @return Id 
     */	
	public Archivo getBycodigo(String codigo) {
		String sql = "select archivo, mimetype from col_archivo where codigo='" + codigo + "'";
		
		Archivo archivo = jdbcTemplate.query(sql, new ResultSetExtractor<Archivo>() {

			@Override
			public Archivo extractData(ResultSet rs) throws SQLException,DataAccessException {
				Archivo fam = new Archivo();
				if (rs.next()) {
					LobHandler lobHandler1 = new DefaultLobHandler();
	                byte[] requestData = lobHandler1.getBlobAsBytes(rs,"archivo");
	                fam.setArchivo(requestData);
	                fam.setMimetype(rs.getString("mimetype"));
	                return fam;
				}
				
				return fam;

			}
			
		});
		
		return archivo;
	}
	
}
