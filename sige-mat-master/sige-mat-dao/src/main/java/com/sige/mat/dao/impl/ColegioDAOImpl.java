package com.sige.mat.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.tesla.colegio.model.Colegio;
import com.tesla.colegio.model.Departamento;
import com.tesla.colegio.model.Distrito;
import com.tesla.colegio.model.MatrVacante;
import com.tesla.colegio.model.Provincia;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ColegioDAO.
 * @author MV
 *
 */
public class ColegioDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(Colegio colegio) {
		if (colegio.getId() != null) {
			// update
			String sql = "UPDATE col_colegio "
						+ "SET id_dist=?, "
						+ "cod_mod=?, "
						+ "nom_niv=?, "
						+ "nom=?, "
						+ "estatal=?, "
						+ "dir=?, "
						+ "tel=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			

			jdbcTemplate.update(sql, 
						colegio.getId_dist(),
						colegio.getCod_mod(),
						colegio.getNom_niv(),
						colegio.getNom(),
						colegio.getEstatal(),
						colegio.getDir(),
						colegio.getTel(),
						colegio.getEst(),
						colegio.getUsr_act(),
						new java.util.Date(),
						colegio.getId()); 
			return colegio.getId();

		} else {
			// insert
			String sql = "insert into col_colegio ("
						+ "id_dist, "
						+ "cod_mod, "
						+ "nom_niv, "
						+ "nom, "
						+ "estatal, "
						+ "dir, "
						+ "tel, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				

				jdbcTemplate.update(sql, 
				colegio.getId_dist(),
				colegio.getCod_mod(),
				colegio.getNom_niv(),
				colegio.getNom(),
				colegio.getEstatal(),
				colegio.getDir(),
				colegio.getTel(),
				colegio.getEst(),
				colegio.getUsr_ins(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from col_colegio where id=?";
		
		
		
		jdbcTemplate.update(sql, id);
	}

	public List<Colegio> list() {
		String sql = "select * from col_colegio";
		
		
		
		List<Colegio> listColegio = jdbcTemplate.query(sql, new RowMapper<Colegio>() {

			@Override
			public Colegio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listColegio;
	}

	public Colegio get(int id) {
		String sql = "select * from col_colegio WHERE id=" + id;
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Colegio>() {

			@Override
			public Colegio extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public Colegio getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select col.id col_id, col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel  ,col.est col_est ";
		if (aTablas.contains("cat_distrito"))
			sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
	
		sql = sql + " from col_colegio col "; 
		if (aTablas.contains("cat_distrito"))
			sql = sql + " left join cat_distrito dist on dist.id = col.id_dist ";
		sql = sql + " where col.id= " + id; 
				
		

		return jdbcTemplate.query(sql, new ResultSetExtractor<Colegio>() {
		
			@Override
			public Colegio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					Colegio colegio= rsToEntity(rs,"col_");
					if (aTablas.contains("cat_distrito")){
						Distrito distrito = new Distrito();  
							distrito.setId(rs.getInt("dist_id")) ;  
							distrito.setNom(rs.getString("dist_nom")) ;  
							//distrito.setCod(rs.getString("dist_cod")) ;  
							distrito.setId_pro(rs.getInt("dist_id_pro")) ;  
							colegio.setDistrito(distrito);
					}
							return colegio;
				}
				
				return null;
			}
			
		});


	}		
	
	public Colegio getByParams(Param param) {

		String sql = "select * from col_colegio " + SQLFrmkUtil.getWhere(param);
		
		
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<Colegio>() {
			@Override
			public Colegio extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<Colegio> listByParams(Param param, String[] order) {

		String sql = "select * from col_colegio " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		

		return jdbcTemplate.query(sql, new RowMapper<Colegio>() {

			@Override
			public Colegio mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<Colegio> listFullByParams(Colegio colegio, String[] order) {
	
		return listFullByParams(Param.toParam("col",colegio), order);
	
	}	
	
	public List<Colegio> listFullByParams(Param param, String[] order) {

		String sql = "select col.id col_id, col.id_dist col_id_dist , col.cod_mod col_cod_mod , col.nom_niv col_nom_niv , col.nom col_nom , col.estatal col_estatal , col.dir col_dir , col.tel col_tel  ,col.est col_est ";
		sql = sql + ", dist.id dist_id  , dist.nom dist_nom , dist.cod dist_cod , dist.id_pro dist_id_pro  ";
		sql = sql + ", prov.id prov_id , prov.nom prov_nom ";
		sql = sql + ", dep.id dep_id , dep.nom dep_nom ";
		sql = sql + " from col_colegio col";
		sql = sql + " left join cat_distrito dist on dist.id = col.id_dist ";
		sql = sql + " left join cat_provincia prov on prov.id=dist.id_pro ";
		sql = sql + " left join cat_departamento dep on dep.id=prov.id_dep ";		

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		

		return jdbcTemplate.query(sql, new RowMapper<Colegio>() {

			@Override
			public Colegio mapRow(ResultSet rs, int rowNum) throws SQLException {
				Colegio colegio= rsToEntity(rs,"col_");
				Distrito distrito = new Distrito();  
				distrito.setId(rs.getInt("dist_id")) ;  
				distrito.setNom(rs.getString("dist_nom")) ;  
				colegio.setDistrito(distrito);
				//distrito.setCod(rs.getString("dist_cod")) ;  
				distrito.setId_pro(rs.getInt("dist_id_pro")) ;
				Provincia provincia = new Provincia();
				provincia.setId(rs.getInt("prov_id"));
				provincia.setNom(rs.getString("prov_nom"));
				colegio.setProvincia(provincia);
				Departamento departamento = new Departamento();
				departamento.setId(rs.getInt("dep_id"));
				departamento.setNom(rs.getString("dep_nom"));
				colegio.setDepartamento(departamento);
				return colegio;
			}

		});

	}	


	public List<MatrVacante> getListMatrVacante(Param param, String[] order) {
		String sql = "select * from eva_matr_vacante " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		
		
		return jdbcTemplate.query(sql, new RowMapper<MatrVacante>() {

			@Override
			public MatrVacante mapRow(ResultSet rs, int rowNum) throws SQLException {
				MatrVacante matr_vacante = new MatrVacante();

				matr_vacante.setId(rs.getInt("id"));
				matr_vacante.setId_alu(rs.getInt("id_alu"));
				matr_vacante.setId_eva(rs.getInt("id_eva"));
				matr_vacante.setId_gra(rs.getInt("id_gra"));
				matr_vacante.setId_col(rs.getInt("id_col"));
				matr_vacante.setId_cli(rs.getInt("id_cli"));
				matr_vacante.setNum_rec(rs.getString("num_rec"));
				matr_vacante.setNum_cont(rs.getString("num_cont"));
				matr_vacante.setRes(rs.getString("res"));
				matr_vacante.setEst(rs.getString("est"));
												
				return matr_vacante;
			}

		});	
	}


	// funciones privadas utilitarias para Colegio

	private Colegio rsToEntity(ResultSet rs,String alias) throws SQLException {
		Colegio colegio = new Colegio();

		colegio.setId(rs.getInt( alias + "id"));
		colegio.setId_dist(rs.getInt( alias + "id_dist"));
		colegio.setCod_mod(rs.getString( alias + "cod_mod"));
		colegio.setNom_niv(rs.getString( alias + "nom_niv"));
		colegio.setNom(rs.getString( alias + "nom"));
		colegio.setEstatal(rs.getString( alias + "estatal"));
		colegio.setDir(rs.getString( alias + "dir"));
		colegio.setTel(rs.getString( alias + "tel"));
		colegio.setEst(rs.getString( alias + "est"));
								
		return colegio;

	}
	
}
