package com.sige.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sige.common.enums.EnumTipoException;
import com.sige.mat.dao.AnioDAO;
import com.sige.mat.dao.CapacidadDAO;
import com.sige.mat.dao.CompetenciaDAO;
import com.sige.mat.dao.CursoDAO;
import com.sige.mat.dao.CursoUnidadDAO;
import com.sige.mat.dao.PerUniDAO;
import com.sige.mat.dao.SubtemaDAO;
import com.sige.mat.dao.TemaDAO;
import com.sige.mat.dao.TrabajadorDAO;
import com.sige.mat.dao.UniSubDAO;
import com.tesla.colegio.model.Capacidad;
import com.tesla.colegio.model.Trabajador;
import com.tesla.colegio.model.UniSub;
import com.tesla.colegio.util.Constante;
import com.tesla.frmk.common.exceptions.ServiceException;
import com.tesla.frmk.sql.Param;
import com.tesla.frmk.sql.Row; 
import com.tesla.frmk.util.IntegerUtil;

@Service
public class ProgramacionAnualService {

	@Autowired
	private CursoDAO cursoDAO;

	@Autowired
	private PerUniDAO perUniDAO;

	@Autowired
	private CompetenciaDAO competenciaDAO;

	@Autowired
	private CapacidadDAO capacidadDAO;

	@Autowired
	private UniSubDAO uni_subDAO;

	@Autowired
	private TemaDAO temaDAO;

	@Autowired
	private SubtemaDAO subtemaDAO;

	@Autowired
	private CursoUnidadDAO cursoUnidadDAO;

	@Autowired
	private TrabajadorDAO trabajadorDAO;

	@Autowired
	private AnioDAO anioDAO;

	/**
	 * 
	 * @param id_cca
	 *            Id del curso_aula
	 * @return
	 * @throws ServiceException
	 */
	public Row obtenerCabecera(Integer id_tra, Integer id_cur, Integer id_gra, Integer id_anio, Integer id_niv)
			throws ServiceException {
		List<Row> cursos = cursoDAO.listarDatosCurso(id_cur, id_gra, id_anio, id_niv);
		Row cabecera = cursos.get(0);
		Trabajador trabajador = trabajadorDAO.get(id_tra);
		cabecera.put("trabajador",
				trabajador.getApe_pat() + " " + trabajador.getApe_mat() + ", " + trabajador.getNom());
		cabecera.put("anio", anioDAO.get(id_anio).getNom());

		Date date = new Date(); // your date
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Lima"));
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String fecha = day + " de " + Constante.MES[month] + " de " + year;
		cabecera.put("fecha", fecha);

		return cabecera;

	}

	public List<Row> obtenerCalendarizacion(Integer id_anio, Integer id_niv) throws ServiceException {
		List<Row> calendarizacion = perUniDAO.datosPeriodoxNivel(id_anio, id_niv);
		return calendarizacion;
	}

	public List<Row> obtenerCompetenciasCapacidades(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra)
			throws ServiceException {
		List<Row> competencias = competenciaDAO.listaCompetenciasCursoAnio(id_anio, id_niv, id_cur, id_gra);
		int c = 0;
		for (Row row : competencias) {
			row.put("nro", ++c);
			row.put("id", row.getInteger("id"));
			row.put("competencia", row.getString("competencia"));

			Param param = new Param();
			param.put("id_com", row.getInteger("id"));
			List<Capacidad> capacidades = capacidadDAO.listByParams(param, new String[] { "nom asc" });// competenciaDAO.l
																										// ..listaCapacidadesCursoAnio(id_anio,
																										// id_niv,
																										// id_cur,
																										// id_gra,row.getInteger("id"));
			List<Row> rowCapacidades = new ArrayList<Row>();

			int i = 0;
			for (Capacidad capacidad : capacidades) {
				Row rowCap = new Row();
				rowCap.put("nro", c + "." + ++i);
				rowCap.put("id", capacidad.getId());
				rowCap.put("capacidad", capacidad.getNom());
				rowCapacidades.add(rowCap);
			}

			row.put("capacidades", rowCapacidades);
			// aca se tiene q obtener los subtemas x cada competencia
		}
		return competencias;
	}

	public List<Row> obtenerCompetenciasCapacidadesIndicadores(Integer id_anio, Integer id_niv, Integer id_cur,
			Integer id_gra) throws ServiceException {
		List<Row> competencias = competenciaDAO.listaCompetenciasCursoAnio(id_anio, id_niv, id_cur, id_gra);
		int c = 0;
		for (Row row : competencias) {
			row.put("nro", ++c);
			row.put("id", row.getInteger("id"));
			row.put("competencia", row.getString("competencia"));

			Param param = new Param();
			param.put("id_com", row.getInteger("id"));
			List<Row> capacidades = competenciaDAO.listaCapacidadesCursoAnio(id_anio, id_niv, id_cur, id_gra,
					row.getInteger("id"));

			int i = 0;
			for (Row capacidad : capacidades) {
				capacidad.put("nro", c + "." + ++i);
			}

			row.put("capacidades", capacidades);
			// aca se tiene q obtener los subtemas x cada competencia
		}
		return competencias;
	}

	public List<Row> listPeriodosSubtemas(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra)
			throws ServiceException {

		List<Row> unidades = new ArrayList<Row>();

		/**
		 * query para listar los periodos a partir de la unidad, curso, nivel,
		 * etc
		 */
		List<Row> periodos = perUniDAO.datosPeriodoxNivel(id_anio, id_niv);
		// List<Row> periuodos = periodoDAO.list(anio, nivel, curso, grado);
		// for (int u=1;u<=4;u++){

		for (Row rowPer : periodos) {

			Integer id_cpu = rowPer.getInteger("id");
			// llenamos cada unidad
			rowPer.put("id", rowPer.getString("nump") + rowPer.getString("periodo"));

			// listamos temas por unidad
			List<Row> arrayTemas = new ArrayList<Row>();

			/**
			 * query para obtener los temas a partir del periodo, curso, nivel,
			 * etc
			 */
			List<Row> temas = temaDAO.listaTemaxPeriodo(id_cpu, id_niv, id_gra, id_cur);
			int nro_grupos = 0;
			for (Row rowTema : temas) {

				Integer id_tem = rowTema.getInteger("id");
				// listamos grupo de subtemas

				/**
				 * Query de grupos de subtemas por tema
				 */
				List<Row> grupos = subtemaDAO.listarGruposxTema(id_anio, id_niv, id_gra, id_cur, id_tem, id_cpu);

				for (Row rowGrupo : grupos) {

					Integer id_cgsp = rowGrupo.getInteger("id");

					rowGrupo.put("nro_uni", rowGrupo.get("num")); // TODO falta
																	// nro de
																	// unidades
					rowGrupo.put("nro_semanas", rowGrupo.get("nro_sem")); // TODO
																			// falta
																			// nro
																			// de
																			// semanas

					/**
					 * query para obtener los subtemas por grupo
					 */
					List<Row> subtemas = subtemaDAO.listarSubtemasxGrupo(id_cgsp);

					rowGrupo.put("subtemas", subtemas);

				}

				nro_grupos += grupos.size();

				rowTema.put("grupos", grupos);
				rowTema.put("nro_grupos", grupos.size());

				arrayTemas.add(rowTema);
			}
			rowPer.put("temas", arrayTemas);
			rowPer.put("nro_grupos", nro_grupos);
			unidades.add(rowPer);
		}


		return unidades;
	}

	public Row obtenerUnidadesCapacidades(Integer id_anio, Integer id_niv, Integer id_cur, Integer id_gra)
			throws ServiceException {

		/**
		 * Query lista de competencias
		 */
		List<Row> competencias = new ArrayList<>();
		int nro_capacidades = 0;
		for (int c = 1; c <= 3; c++) {
			Row rCompetencia = new Row();
			rCompetencia.put("id", c);
			rCompetencia.put("competencia", "competencia" + c);

			/**
			 * Lista de capacidades
			 */
			List<Row> capacidades = new ArrayList<>();
			for (int c1 = 1; c1 <= IntegerUtil.getRandom(3); c1++) {
				Row rCapacidad = new Row();
				rCapacidad.put("id", c);
				rCapacidad.put("capacidad", "capacidad" + c1);

				capacidades.add(rCapacidad);
				nro_capacidades++;

			}
			rCompetencia.put("capacidades", capacidades);
			competencias.add(rCompetencia);

		}

		Row row = new Row();
		row.put("competencias", competencias);
		row.put("nro_capacidades", nro_capacidades);

		/**
		 * Query lista de unidades
		 */
		return row;
	}

	public List<Row> obtenerCapacidadesxUnidad(Integer id_niv, Integer id_gra, Integer id_cur, Integer nump)
			throws ServiceException {
		List<Row> unidades = cursoUnidadDAO.listaUnidades(id_niv, id_gra, id_cur, nump);
		int orden = 0;
		for (Row row : unidades) {
			Integer[] capacidades = capacidadDAO.listaCapacidadxUnidad(row.getInteger("id"));
			row.put("capacidades_arr", capacidades);
			row.put("orden", ++orden);
		}
		return unidades;
	}

	@Transactional
	public int grabarSubtemasporUnidad(UniSub uni_sub, Integer[] id_subtemas) throws Exception {

		List<UniSub> grupSubtemas = uni_subDAO.listByParams(new Param("id_uni", uni_sub.getId_uni()), null);
		Integer[] subtemas_bd = new Integer[grupSubtemas.size()];
		int i = 0;
		for (UniSub subtemas : grupSubtemas) {
			subtemas_bd[i] = subtemas.getId_cgsp();
			i++;
		}

		if (id_subtemas != null) {
			for (i = 0; i < id_subtemas.length; i++) {
				uni_sub.setId_cgsp(id_subtemas[i]);
				uni_sub.setId_uni(uni_sub.getId_uni());
				uni_sub.setEst("A");
				uni_subDAO.saveOrUpdate(uni_sub);

			}
			/*
			 * for ( i = 0; i < id_subtemas.length; i++) { //Si lo q viene en el
			 * formulario no existe en bd if
			 * (!Arrays.stream(subtemas_bd).anyMatch(id_subtemas[i]::equals)){
			 * uni_sub.setId_cgsp(id_subtemas[i]);
			 * uni_sub.setId_uni(uni_sub.getId_uni()); uni_sub.setEst("A");
			 * uni_subDAO.saveOrUpdate(uni_sub);
			 * 
			 * } else { //si esta en el formulario y esta en la bd pero con
			 * inactivo, entonces lo activo Param param = new Param();
			 * param.put("id_uni", uni_sub.getId_uni()); param.put("id_cgsp",
			 * id_subtemas[i]); UniSub uniSubtema =
			 * uni_subDAO.getByParams(param); uniSubtema.setEst("A");
			 * uni_subDAO.saveOrUpdate(uniSubtema);
			 * 
			 * } }
			 * 
			 * for (UniSub uniSub : grupSubtemas) { //Busco del la BD al
			 * formulario, si esta en la bd y no en formulario entonces lo
			 * activo if
			 * (!Arrays.stream(id_subtemas).anyMatch(uniSub.getId_cgsp()::equals
			 * )){ Param param = new Param(); param.put("id_uni",
			 * uni_sub.getId_uni()); param.put("id_cgsp", uniSub.getId_cgsp());
			 * UniSub uniSubtema = uni_subDAO.getByParams(param);
			 * uniSubtema.setEst("I"); uni_subDAO.saveOrUpdate(uniSubtema);
			 * 
			 * } }
			 */
		} else {
			throw new ServiceException(
					"Al menos debe selecionar un grupo de subtemas, si desea eliminar vaya a la opci�n eliminar.",
					EnumTipoException.WARNING);
		}

		return 1;

	}

	public boolean validarUnidadesCompeltas(Integer id_niv, Integer id_gra, Integer id_cur, Integer id_anio)
			throws Exception {
		boolean resultado = false;
		Integer nro_uni_cre = cursoUnidadDAO.cantidadUnidadesCreadas(id_niv, id_gra, id_cur, id_anio);
		Integer nro_uni_conf = cursoUnidadDAO.cantidadUnidadesConfiguradas(id_niv, id_anio);
		if (nro_uni_cre.equals(nro_uni_conf)) {
			List<Row> unidades = cursoUnidadDAO.listaUnidadesxAnio(id_niv, id_gra, id_cur, id_anio);
			for (Row row : unidades) {
				List<Row> subtemas = uni_subDAO.listaSubtemasxUnidad(id_anio, id_niv, id_gra, id_cur,
						row.getInteger("id"));
				if (subtemas.size() <= 0)
					throw new ServiceException(
							"No se puede generar la programaci�n anual, si no ha configurado subtemas para todas la unidades, porfavor proceda a configurar!!",
							EnumTipoException.WARNING);
			}
			resultado = true;
		} else {
			throw new ServiceException(
					"El n�mero de unidades creadas no es igual al n�mero de unidades configuradas que es "
							+ nro_uni_conf,
					EnumTipoException.WARNING);
		}
		return resultado;
	}
}
