package com.sige.mat.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.CursoUnidadControl;

import com.tesla.colegio.model.CursoUnidad;
import com.tesla.colegio.model.Trabajador;
import com.tesla.frmk.common.exceptions.DAOException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;

/**
 * Implementaci�n de la interface CursoUnidadControlDAO.
 * 
 * @author MV
 *
 */
public class CursoUnidadControlDAOImpl {
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;

	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(CursoUnidadControl curso_unidad_control) throws DAOException {

		LobHandler lobHandler = new DefaultLobHandler();
		InputStream inputStream = new ByteArrayInputStream(curso_unidad_control.getPdf());
		try {
			if (curso_unidad_control.getId() != null) {
				// update
				String sql = "UPDATE col_curso_unidad_control " + "SET pdf=?, " + "id_uni=?, " + "id_tra=?, "
						+ "est=?,usr_act=?,fec_act=? " + "WHERE id=?";

				

				jdbcTemplate.update(sql,
						new Object[] { new SqlLobValue(inputStream, inputStream.available(), lobHandler),
								curso_unidad_control.getId_uni(), curso_unidad_control.getId_tra(),
								curso_unidad_control.getEst(), curso_unidad_control.getUsr_act(), new java.util.Date(),
								curso_unidad_control.getId() },
						new int[] { Types.BLOB, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER,
								Types.TIMESTAMP, Types.INTEGER });
				return curso_unidad_control.getId();

			} else {
				// insert

				String sql = "insert into col_curso_unidad_control (" + "pdf, " + "id_uni, " + "id_tra, "
						+ "est, usr_ins, fec_ins) " + "values ( ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql,
						new Object[] { new SqlLobValue(inputStream, inputStream.available(), lobHandler),
								curso_unidad_control.getId_uni(), curso_unidad_control.getId_tra(),
								curso_unidad_control.getEst(), curso_unidad_control.getUsr_ins(),
								new java.util.Date() },
						new int[] { Types.BLOB, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER,
								Types.TIMESTAMP });

				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);

			}

		} catch (Exception e) {
			throw new DAOException("Error al grabar el pdf:" + e.getMessage());
		}

	}

	public void delete(int id) {
		String sql = "delete from col_curso_unidad_control where id=?";

		

		jdbcTemplate.update(sql, id);
	}

	public List<CursoUnidadControl> list() {
		String sql = "select * from col_curso_unidad_control";

		

		List<CursoUnidadControl> listCursoUnidadControl = jdbcTemplate.query(sql, new RowMapper<CursoUnidadControl>() {

			@Override
			public CursoUnidadControl mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs, "");
			}

		});

		return listCursoUnidadControl;
	}

	public CursoUnidadControl get(int id) {
		String sql = "select * from col_curso_unidad_control WHERE id=" + id;

		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidadControl>() {

			@Override
			public CursoUnidadControl extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs, "");
				}

				return null;
			}

		});
	}

	public CursoUnidadControl getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select unic.id unic_id, unic.pdf unic_pdf , unic.id_uni unic_id_uni , unic.id_tra unic_id_tra  ,unic.est unic_est ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql
					+ ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql
					+ ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";

		sql = sql + " from col_curso_unidad_control unic ";
		if (aTablas.contains("col_curso_unidad"))
			sql = sql + " left join col_curso_unidad uni on uni.id = unic.id_uni ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = unic.id_tra ";
		sql = sql + " where unic.id= " + id;

		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidadControl>() {

			@Override
			public CursoUnidadControl extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					CursoUnidadControl cursounidadcontrol = rsToEntity(rs, "unic_");
					if (aTablas.contains("col_curso_unidad")) {
						CursoUnidad cursounidad = new CursoUnidad();
						cursounidad.setId(rs.getInt("uni_id"));
						cursounidad.setId_niv(rs.getInt("uni_id_niv"));
						cursounidad.setId_gra(rs.getInt("uni_id_gra"));
						cursounidad.setId_cur(rs.getInt("uni_id_cur"));
						cursounidad.setId_cpu(rs.getInt("uni_id_cpu"));
						cursounidad.setNum(rs.getInt("uni_num"));
						cursounidad.setNom(rs.getString("uni_nom"));
						cursounidad.setDes(rs.getString("uni_des"));
						cursounidad.setNro_sem(rs.getInt("uni_nro_sem"));
						cursounidad.setProducto(rs.getString("uni_producto"));
						cursounidadcontrol.setCursoUnidad(cursounidad);
					}
					if (aTablas.contains("ges_trabajador")) {
						Trabajador trabajador = new Trabajador();
						trabajador.setId(rs.getInt("tra_id"));
						trabajador.setId_tdc(rs.getInt("tra_id_tdc"));
						trabajador.setNro_doc(rs.getString("tra_nro_doc"));
						trabajador.setApe_pat(rs.getString("tra_ape_pat"));
						trabajador.setApe_mat(rs.getString("tra_ape_mat"));
						trabajador.setNom(rs.getString("tra_nom"));
						trabajador.setFec_nac(rs.getDate("tra_fec_nac"));
						trabajador.setGenero(rs.getString("tra_genero"));
						trabajador.setId_eci(rs.getInt("tra_id_eci"));
						trabajador.setDir(rs.getString("tra_dir"));
						trabajador.setTel(rs.getString("tra_tel"));
						trabajador.setCel(rs.getString("tra_cel"));
						trabajador.setCorr(rs.getString("tra_corr"));
						trabajador.setId_gin(rs.getInt("tra_id_gin"));
						trabajador.setCarrera(rs.getString("tra_carrera"));
						// trabajador.setFot(rs.getString("tra_fot")) ;
						trabajador.setNum_hij(rs.getInt("tra_num_hij"));
						trabajador.setId_usr(rs.getInt("tra_id_usr"));
						cursounidadcontrol.setTrabajador(trabajador);
					}
					return cursounidadcontrol;
				}

				return null;
			}

		});

	}

	public CursoUnidadControl getByParams(Param param) {

		String sql = "select * from col_curso_unidad_control " + SQLFrmkUtil.getWhere(param);

		

		return jdbcTemplate.query(sql, new ResultSetExtractor<CursoUnidadControl>() {
			@Override
			public CursoUnidadControl extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs, "");
				return null;
			}

		});
	}

	public List<CursoUnidadControl> listByParams(Param param, String[] order) {

		String sql = "select * from col_curso_unidad_control " + SQLFrmkUtil.getWhere(param)
				+ SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CursoUnidadControl>() {

			@Override
			public CursoUnidadControl mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs, "");
			}

		});

	}

	public List<CursoUnidadControl> listFullByParams(CursoUnidadControl cursounidadcontrol, String[] order) {

		return listFullByParams(Param.toParam("unic", cursounidadcontrol), order);

	}

	public List<CursoUnidadControl> listFullByParams(Param param, String[] order) {

		String sql = "select unic.id unic_id, unic.pdf unic_pdf , unic.id_uni unic_id_uni , unic.id_tra unic_id_tra  ,unic.est unic_est ";
		sql = sql
				+ ", uni.id uni_id  , uni.id_niv uni_id_niv , uni.id_gra uni_id_gra , uni.id_cur uni_id_cur , uni.id_cpu uni_id_cpu , uni.num uni_num , uni.nom uni_nom , uni.des uni_des , uni.nro_sem uni_nro_sem , uni.producto uni_producto  ";
		sql = sql
				+ ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + " from col_curso_unidad_control unic";
		sql = sql + " left join col_curso_unidad uni on uni.id = unic.id_uni ";
		sql = sql + " left join ges_trabajador tra on tra.id = unic.id_tra ";

		sql = sql + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<CursoUnidadControl>() {

			@Override
			public CursoUnidadControl mapRow(ResultSet rs, int rowNum) throws SQLException {
				CursoUnidadControl cursounidadcontrol = rsToEntity(rs, "unic_");
				CursoUnidad cursounidad = new CursoUnidad();
				cursounidad.setId(rs.getInt("uni_id"));
				cursounidad.setId_niv(rs.getInt("uni_id_niv"));
				cursounidad.setId_gra(rs.getInt("uni_id_gra"));
				cursounidad.setId_cur(rs.getInt("uni_id_cur"));
				cursounidad.setId_cpu(rs.getInt("uni_id_cpu"));
				cursounidad.setNum(rs.getInt("uni_num"));
				cursounidad.setNom(rs.getString("uni_nom"));
				cursounidad.setDes(rs.getString("uni_des"));
				cursounidad.setNro_sem(rs.getInt("uni_nro_sem"));
				cursounidad.setProducto(rs.getString("uni_producto"));
				cursounidadcontrol.setCursoUnidad(cursounidad);
				Trabajador trabajador = new Trabajador();
				trabajador.setId(rs.getInt("tra_id"));
				trabajador.setId_tdc(rs.getInt("tra_id_tdc"));
				trabajador.setNro_doc(rs.getString("tra_nro_doc"));
				trabajador.setApe_pat(rs.getString("tra_ape_pat"));
				trabajador.setApe_mat(rs.getString("tra_ape_mat"));
				trabajador.setNom(rs.getString("tra_nom"));
				trabajador.setFec_nac(rs.getDate("tra_fec_nac"));
				trabajador.setGenero(rs.getString("tra_genero"));
				trabajador.setId_eci(rs.getInt("tra_id_eci"));
				trabajador.setDir(rs.getString("tra_dir"));
				trabajador.setTel(rs.getString("tra_tel"));
				trabajador.setCel(rs.getString("tra_cel"));
				trabajador.setCorr(rs.getString("tra_corr"));
				trabajador.setId_gin(rs.getInt("tra_id_gin"));
				trabajador.setCarrera(rs.getString("tra_carrera"));
				// trabajador.setFot(rs.getString("tra_fot")) ;
				trabajador.setNum_hij(rs.getInt("tra_num_hij"));
				trabajador.setId_usr(rs.getInt("tra_id_usr"));
				cursounidadcontrol.setTrabajador(trabajador);
				return cursounidadcontrol;
			}

		});

	}

	// funciones privadas utilitarias para CursoUnidadControl

	private CursoUnidadControl rsToEntity(ResultSet rs, String alias) throws SQLException {
		CursoUnidadControl curso_unidad_control = new CursoUnidadControl();

		curso_unidad_control.setId(rs.getInt(alias + "id"));
		curso_unidad_control.setPdf(rs.getBytes(alias + "pdf"));
		curso_unidad_control.setId_uni(rs.getInt(alias + "id_uni"));
		curso_unidad_control.setId_tra(rs.getInt(alias + "id_tra"));
		curso_unidad_control.setEst(rs.getString(alias + "est"));

		return curso_unidad_control;

	}

}
