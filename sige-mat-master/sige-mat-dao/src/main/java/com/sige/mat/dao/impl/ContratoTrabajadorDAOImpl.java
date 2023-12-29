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
import com.tesla.colegio.model.ContratoTrabajador;

import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.Anio;
import com.tesla.colegio.model.Empresa;
import com.tesla.colegio.model.GiroNegocio;
import com.tesla.colegio.model.RegimenLaboral;
import com.tesla.colegio.model.ModalidadTrabajo;
import com.tesla.colegio.model.CategoriaOcupacional;
import com.tesla.colegio.model.PuestoTrabajador;
import com.tesla.colegio.model.Nivel;
import com.tesla.colegio.model.PeriodoPrueba;
import com.tesla.colegio.model.LineaCarrera;
import com.tesla.colegio.model.Denominacion;
import com.tesla.colegio.model.Remuneracion;
import com.tesla.colegio.model.TipFrecPago;

import com.sige.web.security.TokenSeguridad;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.SQLFrmkUtil;


/**
 * Implementaci�n de la interface ContratoTrabajadorDAO.
 * @author MV
 *
 */
public class ContratoTrabajadorDAOImpl{
	@Autowired
	private DataSource dataSource;
	protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private TokenSeguridad tokenSeguridad;
	
	@PostConstruct
	private void initialize() {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public int saveOrUpdate(ContratoTrabajador contrato_trabajador) {
		if (contrato_trabajador.getId() != null) {
			// update
			String sql = "UPDATE rhh_contrato_trabajador "
						+ "SET id_tra=?, "
						+ "id_anio_con=?, "
						+ "id_emp=?, "
						+ "id_gir=?, "
						+ "id_reg=?, "
						+ "id_mod=?, "
						+ "id_cat=?, "
						+ "id_pue=?, "
						+ "id_niv_tra=?, "
						+ "id_prue=?, "
						+ "id_lin_carr=?, "
						+ "id_den=?, "
						+ "id_rem_cat=?, "
						+ "id_frec_pag=?, "
						+ "num_con=?, "
						+ "con_indf=?, "
						+ "fec_ini=?, "
						+ "fec_fin=?, "
						+ "fec_fin_prue=?, "
						+ "est=?,usr_act=?,fec_act=? "
						+ "WHERE id=?";
			
			System.out.println(sql);

			jdbcTemplate.update(sql, 
						contrato_trabajador.getId_tra(),
						contrato_trabajador.getId_anio_con(),
						contrato_trabajador.getId_emp(),
						contrato_trabajador.getId_gir(),
						contrato_trabajador.getId_reg(),
						contrato_trabajador.getId_mod(),
						contrato_trabajador.getId_cat(),
						contrato_trabajador.getId_pue(),
						contrato_trabajador.getId_niv_tra(),
						contrato_trabajador.getId_prue(),
						contrato_trabajador.getId_lin_carr(),
						contrato_trabajador.getId_den(),
						contrato_trabajador.getId_rem_cat(),
						contrato_trabajador.getId_frec_pag(),
						contrato_trabajador.getNum_con(),
						contrato_trabajador.getCon_indf(),
						contrato_trabajador.getFec_ini(),
						contrato_trabajador.getFec_fin(),
						contrato_trabajador.getFec_fin_prue(),
						contrato_trabajador.getEst(),
						tokenSeguridad.getId(),
						new java.util.Date(),
						contrato_trabajador.getId()); 
			return contrato_trabajador.getId();

		} else {
			// insert
			String sql = "insert into rhh_contrato_trabajador ("
						+ "id_tra, "
						+ "id_anio_con, "
						+ "id_emp, "
						+ "id_gir, "
						+ "id_reg, "
						+ "id_mod, "
						+ "id_cat, "
						+ "id_pue, "
						+ "id_niv_tra, "
						+ "id_prue, "
						+ "id_lin_carr, "
						+ "id_den, "
						+ "id_rem_cat, "
						+ "id_frec_pag, "
						+ "num_con, "
						+ "con_indf, "
						+ "fec_ini, "
						+ "fec_fin, "
						+ "fec_fin_prue, "
						+ "est, usr_ins, fec_ins) "
						+ "values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

				System.out.println(sql);

				jdbcTemplate.update(sql, 
				contrato_trabajador.getId_tra(),
				contrato_trabajador.getId_anio_con(),
				contrato_trabajador.getId_emp(),
				contrato_trabajador.getId_gir(),
				contrato_trabajador.getId_reg(),
				contrato_trabajador.getId_mod(),
				contrato_trabajador.getId_cat(),
				contrato_trabajador.getId_pue(),
				contrato_trabajador.getId_niv_tra(),
				contrato_trabajador.getId_prue(),
				contrato_trabajador.getId_lin_carr(),
				contrato_trabajador.getId_den(),
				contrato_trabajador.getId_rem_cat(),
				contrato_trabajador.getId_frec_pag(),
				contrato_trabajador.getNum_con(),
				contrato_trabajador.getCon_indf(),
				contrato_trabajador.getFec_ini(),
				contrato_trabajador.getFec_fin(),
				contrato_trabajador.getFec_fin_prue(),
				contrato_trabajador.getEst(),
				tokenSeguridad.getId(),
				new java.util.Date());
				
				return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class );
		}
		
	}

	public void delete(int id) {
		String sql = "delete from rhh_contrato_trabajador where id=?";
		
		System.out.println(sql);
		
		jdbcTemplate.update(sql, id);
	}

	public List<ContratoTrabajador> list() {
		String sql = "select * from rhh_contrato_trabajador";
		
		System.out.println(sql);
		
		List<ContratoTrabajador> listContratoTrabajador = jdbcTemplate.query(sql, new RowMapper<ContratoTrabajador>() {

			@Override
			public ContratoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}
			
		});
		
		return listContratoTrabajador;
	}

	public ContratoTrabajador get(int id) {
		String sql = "select * from rhh_contrato_trabajador WHERE id=" + id;
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ContratoTrabajador>() {

			@Override
			public ContratoTrabajador extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					return rsToEntity(rs,"");
				}
				
				return null;
			}
			
		});
	}

	public ContratoTrabajador getFull(int id, String tablas[]) {
		final List<String> aTablas = Arrays.asList(tablas);
		String sql = "select ctra.id ctra_id, ctra.id_tra ctra_id_tra , ctra.con_indf ctra_con_indf, ctra.id_anio_con ctra_id_anio_con , ctra.id_emp ctra_id_emp , ctra.id_gir ctra_id_gir , ctra.id_reg ctra_id_reg , ctra.id_mod ctra_id_mod , ctra.id_cat ctra_id_cat , ctra.id_pue ctra_id_pue , ctra.id_niv_tra ctra_id_niv_tra , ctra.id_prue ctra_id_prue , ctra.id_lin_carr ctra_id_lin_carr , ctra.id_den ctra_id_den , ctra.id_rem_cat ctra_id_rem_cat , ctra.id_frec_pag ctra_id_frec_pag , ctra.num_con ctra_num_con , ctra.fec_ini ctra_fec_ini , ctra.fec_fin ctra_fec_fin , ctra.fec_fin_prue ctra_fec_fin_prue  ,ctra.est ctra_est ";
		if (aTablas.contains("ges_trabajador"))
			sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		if (aTablas.contains("col_anio"))
			sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		if (aTablas.contains("ges_empresa"))
			sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.corr emp_corr , emp.dominio emp_dominio , emp.pagina_web emp_pagina_web  ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		if (aTablas.contains("cat_regimen_laboral"))
			sql = sql + ", reg.id reg_id  , reg.nom reg_nom , reg.des reg_des  ";
		if (aTablas.contains("cat_modalidad_trabajo"))
			sql = sql + ", mtrab.id mtrab_id  , mtrab.nom mtrab_nom , mtrab.des mtrab_des  ";
		if (aTablas.contains("cat_categoria_ocupacional"))
			sql = sql + ", cocu.id cocu_id  , cocu.nom cocu_nom , cocu.des cocu_des  ";
		if (aTablas.contains("ges_puesto_trabajador"))
			sql = sql + ", pue.id pue_id  , pue.nom pue_nom , pue.des pue_des  ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		if (aTablas.contains("cat_periodo_prueba"))
			sql = sql + ", pprue.id pprue_id  , pprue.nom pprue_nom , pprue.des pprue_des  ";
		if (aTablas.contains("cat_linea_carrera"))
			sql = sql + ", lcarr.id lcarr_id  , lcarr.nom lcarr_nom , lcarr.des lcarr_des  ";
		if (aTablas.contains("cat_denominacion"))
			sql = sql + ", cden.id cden_id  , cden.nom cden_nom , cden.des cden_des  ";
		if (aTablas.contains("cat_remuneracion"))
			sql = sql + ", rem.id rem_id  , rem.nom rem_nom , rem.des rem_des  ";
		if (aTablas.contains("cat_tip_frec_pago"))
			sql = sql + ", fpag.id fpag_id  , fpag.nom fpag_nom , fpag.des fpag_des  ";
	
		sql = sql + " from rhh_contrato_trabajador ctra "; 
		if (aTablas.contains("ges_trabajador"))
			sql = sql + " left join ges_trabajador tra on tra.id = ctra.id_tra ";
		if (aTablas.contains("col_anio"))
			sql = sql + " left join col_anio anio on anio.id = ctra.id_anio_con ";
		if (aTablas.contains("ges_empresa"))
			sql = sql + " left join ges_empresa emp on emp.id = ctra.id_emp ";
		if (aTablas.contains("ges_giro_negocio"))
			sql = sql + " left join ges_giro_negocio gir on gir.id = ctra.id_gir ";
		if (aTablas.contains("cat_regimen_laboral"))
			sql = sql + " left join cat_regimen_laboral reg on reg.id = ctra.id_reg ";
		if (aTablas.contains("cat_modalidad_trabajo"))
			sql = sql + " left join cat_modalidad_trabajo mtrab on mtrab.id = ctra.id_mod ";
		if (aTablas.contains("cat_categoria_ocupacional"))
			sql = sql + " left join cat_categoria_ocupacional cocu on cocu.id = ctra.id_cat ";
		if (aTablas.contains("ges_puesto_trabajador"))
			sql = sql + " left join ges_puesto_trabajador pue on pue.id = ctra.id_pue ";
		if (aTablas.contains("cat_nivel"))
			sql = sql + " left join cat_nivel niv on niv.id = ctra.id_niv_tra ";
		if (aTablas.contains("cat_periodo_prueba"))
			sql = sql + " left join cat_periodo_prueba pprue on pprue.id = ctra.id_prue ";
		if (aTablas.contains("cat_linea_carrera"))
			sql = sql + " left join cat_linea_carrera lcarr on lcarr.id = ctra.id_lin_carr ";
		if (aTablas.contains("cat_denominacion"))
			sql = sql + " left join cat_denominacion cden on cden.id = ctra.id_den ";
		if (aTablas.contains("cat_remuneracion"))
			sql = sql + " left join cat_remuneracion rem on rem.id = ctra.id_rem_cat ";
		if (aTablas.contains("cat_tip_frec_pago"))
			sql = sql + " left join cat_tip_frec_pago fpag on fpag.id = ctra.id_frec_pag ";
		sql = sql + " where ctra.id= " + id; 
				
		System.out.println(sql);

		return jdbcTemplate.query(sql, new ResultSetExtractor<ContratoTrabajador>() {
		
			@Override
			public ContratoTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					ContratoTrabajador contratotrabajador= rsToEntity(rs,"ctra_");
					if (aTablas.contains("ges_trabajador")){
						Trabajador trabajador = new Trabajador();  
							trabajador.setId(rs.getInt("tra_id")) ;  
							trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
							trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
							trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
							trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
							trabajador.setNom(rs.getString("tra_nom")) ;  
							trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
							trabajador.setGenero(rs.getString("tra_genero")) ;  
							trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
							trabajador.setDir(rs.getString("tra_dir")) ;  
							trabajador.setTel(rs.getString("tra_tel")) ;  
							trabajador.setCel(rs.getString("tra_cel")) ;  
							trabajador.setCorr(rs.getString("tra_corr")) ;  
							trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
							trabajador.setCarrera(rs.getString("tra_carrera")) ;  
							//trabajador.setFot(rs.getString("tra_fot")) ;  
							trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
							trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
							contratotrabajador.setTrabajador(trabajador);
					}
					if (aTablas.contains("col_anio")){
						Anio anio = new Anio();  
							anio.setId(rs.getInt("anio_id")) ;  
							anio.setNom(rs.getString("anio_nom")) ;  
							contratotrabajador.setAnio(anio);
					}
					if (aTablas.contains("ges_empresa")){
						Empresa empresa = new Empresa();  
							empresa.setId(rs.getInt("emp_id")) ;  
							empresa.setNom(rs.getString("emp_nom")) ;  
							empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
							empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
							empresa.setRuc(rs.getString("emp_ruc")) ;  
							empresa.setDir(rs.getString("emp_dir")) ;  
							empresa.setTel(rs.getString("emp_tel")) ;  
							empresa.setCorr(rs.getString("emp_corr")) ;  
							empresa.setDominio(rs.getString("emp_dominio")) ;  
							//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
							contratotrabajador.setEmpresa(empresa);
					}
					if (aTablas.contains("ges_giro_negocio")){
						GiroNegocio gironegocio = new GiroNegocio();  
							gironegocio.setId(rs.getInt("gir_id")) ;  
							gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
							gironegocio.setNom(rs.getString("gir_nom")) ;  
							gironegocio.setDes(rs.getString("gir_des")) ;  
							contratotrabajador.setGiroNegocio(gironegocio);
					}
					if (aTablas.contains("cat_regimen_laboral")){
						RegimenLaboral regimenlaboral = new RegimenLaboral();  
							regimenlaboral.setId(rs.getInt("reg_id")) ;  
							regimenlaboral.setNom(rs.getString("reg_nom")) ;  
							regimenlaboral.setDes(rs.getString("reg_des")) ;  
							contratotrabajador.setRegimenLaboral(regimenlaboral);
					}
					if (aTablas.contains("cat_modalidad_trabajo")){
						ModalidadTrabajo modalidadtrabajo = new ModalidadTrabajo();  
							modalidadtrabajo.setId(rs.getInt("mtrab_id")) ;  
							modalidadtrabajo.setNom(rs.getString("mtrab_nom")) ;  
							modalidadtrabajo.setDes(rs.getString("mtrab_des")) ;  
							contratotrabajador.setModalidadTrabajo(modalidadtrabajo);
					}
					if (aTablas.contains("cat_categoria_ocupacional")){
						CategoriaOcupacional categoriaocupacional = new CategoriaOcupacional();  
							categoriaocupacional.setId(rs.getInt("cocu_id")) ;  
							categoriaocupacional.setNom(rs.getString("cocu_nom")) ;  
							categoriaocupacional.setDes(rs.getString("cocu_des")) ;  
							contratotrabajador.setCategoriaOcupacional(categoriaocupacional);
					}
					if (aTablas.contains("ges_puesto_trabajador")){
						PuestoTrabajador puestotrabajador = new PuestoTrabajador();  
							puestotrabajador.setId(rs.getInt("pue_id")) ;  
							puestotrabajador.setNom(rs.getString("pue_nom")) ;  
							puestotrabajador.setDes(rs.getString("pue_des")) ;  
							contratotrabajador.setPuestoTrabajador(puestotrabajador);
					}
					if (aTablas.contains("cat_nivel")){
						Nivel nivel = new Nivel();  
							nivel.setId(rs.getInt("niv_id")) ;  
							nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
							nivel.setNom(rs.getString("niv_nom")) ;  
							contratotrabajador.setNivel(nivel);
					}
					if (aTablas.contains("cat_periodo_prueba")){
						PeriodoPrueba periodoprueba = new PeriodoPrueba();  
							periodoprueba.setId(rs.getInt("pprue_id")) ;  
							periodoprueba.setNom(rs.getString("pprue_nom")) ;  
							periodoprueba.setDes(rs.getString("pprue_des")) ;  
							contratotrabajador.setPeriodoPrueba(periodoprueba);
					}
					if (aTablas.contains("cat_linea_carrera")){
						LineaCarrera lineacarrera = new LineaCarrera();  
							lineacarrera.setId(rs.getInt("lcarr_id")) ;  
							lineacarrera.setNom(rs.getString("lcarr_nom")) ;  
							lineacarrera.setDes(rs.getString("lcarr_des")) ;  
							contratotrabajador.setLineaCarrera(lineacarrera);
					}
					if (aTablas.contains("cat_denominacion")){
						Denominacion denominacion = new Denominacion();  
							denominacion.setId(rs.getInt("cden_id")) ;  
							denominacion.setNom(rs.getString("cden_nom")) ;  
							denominacion.setDes(rs.getString("cden_des")) ;  
							contratotrabajador.setDenominacion(denominacion);
					}
					if (aTablas.contains("cat_remuneracion")){
						Remuneracion remuneracion = new Remuneracion();  
							remuneracion.setId(rs.getInt("rem_id")) ;  
							remuneracion.setNom(rs.getString("rem_nom")) ;  
							remuneracion.setDes(rs.getString("rem_des")) ;  
							contratotrabajador.setRemuneracion(remuneracion);
					}
					if (aTablas.contains("cat_tip_frec_pago")){
						TipFrecPago tipfrecpago = new TipFrecPago();  
							tipfrecpago.setId(rs.getInt("fpag_id")) ;  
							tipfrecpago.setNom(rs.getString("fpag_nom")) ;  
							tipfrecpago.setDes(rs.getString("fpag_des")) ;  
							contratotrabajador.setTipFrecPago(tipfrecpago);
					}
							return contratotrabajador;
				}
				
				return null;
			}
			
		});


	}		
	
	public ContratoTrabajador getByParams(Param param) {

		String sql = "select * from rhh_contrato_trabajador " + SQLFrmkUtil.getWhere(param);
		
		System.out.println(sql);
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<ContratoTrabajador>() {
			@Override
			public ContratoTrabajador extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return rsToEntity(rs,"");
				return null;
			}

		});
	}

	public List<ContratoTrabajador> listByParams(Param param, String[] order) {

		String sql = "select * from rhh_contrato_trabajador " + SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);
		
		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ContratoTrabajador>() {

			@Override
			public ContratoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rsToEntity(rs,"");
			}

		});

	}	

	
	public List<ContratoTrabajador> listFullByParams(ContratoTrabajador contratotrabajador, String[] order) {
	
		return listFullByParams(Param.toParam("ctra",contratotrabajador), order);
	
	}	
	
	public List<ContratoTrabajador> listFullByParams(Param param, String[] order) {

		String sql = "select ctra.id ctra_id, ctra.id_tra ctra_id_tra , ctra.con_indf ctra_con_indf,  ctra.id_anio_con ctra_id_anio_con , ctra.id_emp ctra_id_emp , ctra.id_gir ctra_id_gir , ctra.id_reg ctra_id_reg , ctra.id_mod ctra_id_mod , ctra.id_cat ctra_id_cat , ctra.id_pue ctra_id_pue , ctra.id_niv_tra ctra_id_niv_tra , ctra.id_prue ctra_id_prue , ctra.id_lin_carr ctra_id_lin_carr , ctra.id_den ctra_id_den , ctra.id_rem_cat ctra_id_rem_cat , ctra.id_frec_pag ctra_id_frec_pag , ctra.num_con ctra_num_con , ctra.fec_ini ctra_fec_ini , ctra.fec_fin ctra_fec_fin , ctra.fec_fin_prue ctra_fec_fin_prue  ,ctra.est ctra_est ";
		sql = sql + ", tra.id tra_id  , tra.id_tdc tra_id_tdc , tra.nro_doc tra_nro_doc , tra.ape_pat tra_ape_pat , tra.ape_mat tra_ape_mat , tra.nom tra_nom , tra.fec_nac tra_fec_nac , tra.genero tra_genero , tra.id_eci tra_id_eci , tra.dir tra_dir , tra.tel tra_tel , tra.cel tra_cel , tra.corr tra_corr , tra.id_gin tra_id_gin , tra.carrera tra_carrera , tra.fot tra_fot , tra.num_hij tra_num_hij , tra.id_usr tra_id_usr  ";
		sql = sql + ", anio.id anio_id  , anio.nom anio_nom  ";
		sql = sql + ", emp.id emp_id  , emp.nom emp_nom , emp.raz_soc emp_raz_soc , emp.rep_leg emp_rep_leg , emp.ruc emp_ruc , emp.dir emp_dir , emp.tel emp_tel , emp.corr emp_corr , emp.dominio emp_dominio , emp.pagina_web emp_pagina_web  ";
		sql = sql + ", gir.id gir_id  , gir.id_emp gir_id_emp , gir.nom gir_nom , gir.des gir_des  ";
		sql = sql + ", reg.id reg_id  , reg.nom reg_nom , reg.des reg_des  ";
		sql = sql + ", mtrab.id mtrab_id  , mtrab.nom mtrab_nom , mtrab.des mtrab_des  ";
		sql = sql + ", cocu.id cocu_id  , cocu.nom cocu_nom , cocu.des cocu_des  ";
		sql = sql + ", pue.id pue_id  , pue.nom pue_nom , pue.des pue_des  ";
		sql = sql + ", niv.id niv_id  , niv.cod_mod niv_cod_mod , niv.nom niv_nom  ";
		sql = sql + ", pprue.id pprue_id  , pprue.nom pprue_nom , pprue.des pprue_des  ";
		sql = sql + ", lcarr.id lcarr_id  , lcarr.nom lcarr_nom , lcarr.des lcarr_des  ";
		sql = sql + ", cden.id cden_id  , cden.nom cden_nom , cden.des cden_des  ";
		sql = sql + ", rem.id rem_id  , rem.nom rem_nom , rem.des rem_des  ";
		sql = sql + ", fpag.id fpag_id  , fpag.nom fpag_nom , fpag.des fpag_des  ";
		sql = sql + " from rhh_contrato_trabajador ctra";
		sql = sql + " left join ges_trabajador tra on tra.id = ctra.id_tra ";
		sql = sql + " left join col_anio anio on anio.id = ctra.id_anio_con ";
		sql = sql + " left join ges_empresa emp on emp.id = ctra.id_emp ";
		sql = sql + " left join ges_giro_negocio gir on gir.id = ctra.id_gir ";
		sql = sql + " left join cat_regimen_laboral reg on reg.id = ctra.id_reg ";
		sql = sql + " left join cat_modalidad_trabajo mtrab on mtrab.id = ctra.id_mod ";
		sql = sql + " left join cat_categoria_ocupacional cocu on cocu.id = ctra.id_cat ";
		sql = sql + " left join ges_puesto_trabajador pue on pue.id = ctra.id_pue ";
		sql = sql + " left join cat_nivel niv on niv.id = ctra.id_niv_tra ";
		sql = sql + " left join cat_periodo_prueba pprue on pprue.id = ctra.id_prue ";
		sql = sql + " left join cat_linea_carrera lcarr on lcarr.id = ctra.id_lin_carr ";
		sql = sql + " left join cat_denominacion cden on cden.id = ctra.id_den ";
		sql = sql + " left join cat_remuneracion rem on rem.id = ctra.id_rem ";
		sql = sql + " left join cat_tip_frec_pago fpag on fpag.id = ctra.id_frec_pag ";

		sql = sql +  SQLFrmkUtil.getWhere(param) + SQLFrmkUtil.getOrder(order);

		System.out.println(sql);

		return jdbcTemplate.query(sql, new RowMapper<ContratoTrabajador>() {

			@Override
			public ContratoTrabajador mapRow(ResultSet rs, int rowNum) throws SQLException {
				ContratoTrabajador contratotrabajador= rsToEntity(rs,"ctra_");
				Trabajador trabajador = new Trabajador();  
				trabajador.setId(rs.getInt("tra_id")) ;  
				trabajador.setId_tdc(rs.getInt("tra_id_tdc")) ;  
				trabajador.setNro_doc(rs.getString("tra_nro_doc")) ;  
				trabajador.setApe_pat(rs.getString("tra_ape_pat")) ;  
				trabajador.setApe_mat(rs.getString("tra_ape_mat")) ;  
				trabajador.setNom(rs.getString("tra_nom")) ;  
				trabajador.setFec_nac(rs.getDate("tra_fec_nac")) ;  
				trabajador.setGenero(rs.getString("tra_genero")) ;  
				trabajador.setId_eci(rs.getInt("tra_id_eci")) ;  
				trabajador.setDir(rs.getString("tra_dir")) ;  
				trabajador.setTel(rs.getString("tra_tel")) ;  
				trabajador.setCel(rs.getString("tra_cel")) ;  
				trabajador.setCorr(rs.getString("tra_corr")) ;  
				trabajador.setId_gin(rs.getInt("tra_id_gin")) ;  
				trabajador.setCarrera(rs.getString("tra_carrera")) ;  
				//trabajador.setFot(rs.getString("tra_fot")) ;  
				trabajador.setNum_hij(rs.getInt("tra_num_hij")) ;  
				trabajador.setId_usr(rs.getInt("tra_id_usr")) ;  
				contratotrabajador.setTrabajador(trabajador);
				Anio anio = new Anio();  
				anio.setId(rs.getInt("anio_id")) ;  
				anio.setNom(rs.getString("anio_nom")) ;  
				contratotrabajador.setAnio(anio);
				Empresa empresa = new Empresa();  
				empresa.setId(rs.getInt("emp_id")) ;  
				empresa.setNom(rs.getString("emp_nom")) ;  
				empresa.setRaz_soc(rs.getString("emp_raz_soc")) ;  
				empresa.setRep_leg(rs.getString("emp_rep_leg")) ;  
				empresa.setRuc(rs.getString("emp_ruc")) ;  
				empresa.setDir(rs.getString("emp_dir")) ;  
				empresa.setTel(rs.getString("emp_tel")) ;  
				empresa.setCorr(rs.getString("emp_corr")) ;  
				empresa.setDominio(rs.getString("emp_dominio")) ;  
				//empresa.setPagina_web(rs.getString("emp_pagina_web")) ;  
				contratotrabajador.setEmpresa(empresa);
				GiroNegocio gironegocio = new GiroNegocio();  
				gironegocio.setId(rs.getInt("gir_id")) ;  
				gironegocio.setId_emp(rs.getInt("gir_id_emp")) ;  
				gironegocio.setNom(rs.getString("gir_nom")) ;  
				gironegocio.setDes(rs.getString("gir_des")) ;  
				contratotrabajador.setGiroNegocio(gironegocio);
				RegimenLaboral regimenlaboral = new RegimenLaboral();  
				regimenlaboral.setId(rs.getInt("reg_id")) ;  
				regimenlaboral.setNom(rs.getString("reg_nom")) ;  
				regimenlaboral.setDes(rs.getString("reg_des")) ;  
				contratotrabajador.setRegimenLaboral(regimenlaboral);
				ModalidadTrabajo modalidadtrabajo = new ModalidadTrabajo();  
				modalidadtrabajo.setId(rs.getInt("mtrab_id")) ;  
				modalidadtrabajo.setNom(rs.getString("mtrab_nom")) ;  
				modalidadtrabajo.setDes(rs.getString("mtrab_des")) ;  
				contratotrabajador.setModalidadTrabajo(modalidadtrabajo);
				CategoriaOcupacional categoriaocupacional = new CategoriaOcupacional();  
				categoriaocupacional.setId(rs.getInt("cocu_id")) ;  
				categoriaocupacional.setNom(rs.getString("cocu_nom")) ;  
				categoriaocupacional.setDes(rs.getString("cocu_des")) ;  
				contratotrabajador.setCategoriaOcupacional(categoriaocupacional);
				PuestoTrabajador puestotrabajador = new PuestoTrabajador();  
				puestotrabajador.setId(rs.getInt("pue_id")) ;  
				puestotrabajador.setNom(rs.getString("pue_nom")) ;  
				puestotrabajador.setDes(rs.getString("pue_des")) ;  
				contratotrabajador.setPuestoTrabajador(puestotrabajador);
				Nivel nivel = new Nivel();  
				nivel.setId(rs.getInt("niv_id")) ;  
				nivel.setCod_mod(rs.getString("niv_cod_mod")) ;  
				nivel.setNom(rs.getString("niv_nom")) ;  
				contratotrabajador.setNivel(nivel);
				PeriodoPrueba periodoprueba = new PeriodoPrueba();  
				periodoprueba.setId(rs.getInt("pprue_id")) ;  
				periodoprueba.setNom(rs.getString("pprue_nom")) ;  
				periodoprueba.setDes(rs.getString("pprue_des")) ;  
				contratotrabajador.setPeriodoPrueba(periodoprueba);
				LineaCarrera lineacarrera = new LineaCarrera();  
				lineacarrera.setId(rs.getInt("lcarr_id")) ;  
				lineacarrera.setNom(rs.getString("lcarr_nom")) ;  
				lineacarrera.setDes(rs.getString("lcarr_des")) ;  
				contratotrabajador.setLineaCarrera(lineacarrera);
				Denominacion denominacion = new Denominacion();  
				denominacion.setId(rs.getInt("cden_id")) ;  
				denominacion.setNom(rs.getString("cden_nom")) ;  
				denominacion.setDes(rs.getString("cden_des")) ;  
				contratotrabajador.setDenominacion(denominacion);
				Remuneracion remuneracion = new Remuneracion();  
				remuneracion.setId(rs.getInt("rem_id")) ;  
				remuneracion.setNom(rs.getString("rem_nom")) ;  
				remuneracion.setDes(rs.getString("rem_des")) ;  
				contratotrabajador.setRemuneracion(remuneracion);
				TipFrecPago tipfrecpago = new TipFrecPago();  
				tipfrecpago.setId(rs.getInt("fpag_id")) ;  
				tipfrecpago.setNom(rs.getString("fpag_nom")) ;  
				tipfrecpago.setDes(rs.getString("fpag_des")) ;  
				contratotrabajador.setTipFrecPago(tipfrecpago);
				return contratotrabajador;
			}

		});

	}	




	// funciones privadas utilitarias para ContratoTrabajador

	private ContratoTrabajador rsToEntity(ResultSet rs,String alias) throws SQLException {
		ContratoTrabajador contrato_trabajador = new ContratoTrabajador();

		contrato_trabajador.setId(rs.getInt( alias + "id"));
		contrato_trabajador.setId_tra(rs.getInt( alias + "id_tra"));
		contrato_trabajador.setId_anio_con(rs.getInt( alias + "id_anio_con"));
		contrato_trabajador.setId_emp(rs.getInt( alias + "id_emp"));
		contrato_trabajador.setId_gir(rs.getInt( alias + "id_gir"));
		contrato_trabajador.setId_reg(rs.getInt( alias + "id_reg"));
		contrato_trabajador.setId_mod(rs.getInt( alias + "id_mod"));
		contrato_trabajador.setId_cat(rs.getInt( alias + "id_cat"));
		contrato_trabajador.setId_pue(rs.getInt( alias + "id_pue"));
		contrato_trabajador.setId_niv_tra(rs.getInt( alias + "id_niv_tra"));
		contrato_trabajador.setId_prue(rs.getInt( alias + "id_prue"));
		contrato_trabajador.setId_lin_carr(rs.getInt( alias + "id_lin_carr"));
		contrato_trabajador.setId_den(rs.getInt( alias + "id_den"));
		contrato_trabajador.setId_rem_cat(rs.getInt( alias + "id_rem_cat"));
		contrato_trabajador.setId_frec_pag(rs.getInt( alias + "id_frec_pag"));
		contrato_trabajador.setNum_con(rs.getString( alias + "num_con"));
		contrato_trabajador.setCon_indf(rs.getString(alias + "con_indf"));
		contrato_trabajador.setFec_ini(rs.getDate( alias + "fec_ini"));
		contrato_trabajador.setFec_fin(rs.getDate( alias + "fec_fin"));
		contrato_trabajador.setFec_fin_prue(rs.getDate( alias + "fec_fin_prue"));
		contrato_trabajador.setEst(rs.getString( alias + "est"));
								
		return contrato_trabajador;

	}
	
}
