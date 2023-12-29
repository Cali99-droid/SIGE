package com.sige.core.dao.cache;

public enum CacheEnum {

	GES_SUCURSAL("ges_sucursal"),
	GES_USUARIO("ges_usuario"),
	FAC_CONCEPTO("fac_concepto"),
	FAC_BANCO("fac_banco"),
	MOD_PARAMETRO("mod_parametro"),
	AREA_ANIO("col_area_anio"),
	GES_SERVICIO("ges_servicio"),
	GES_SUCURSAL_SEC("ges_sucursal_sec"),
	CAT_GRAD("cat_grad"),
	CAT_GRAD_TODOS("cat_grad_todos"),
	COL_AULA_ESPECIAL("col_aula_especial"),
	COL_AULA("col_aula"),
	COL_AULA_ANT("col_aula_ant"),
	COL_AULA_LOCAL("col_aula_local"),
	COL_AULA_LOCAL_NUEVO("col_aula_local_nuevo"),
	COL_CURSO_ANIO("col_curso_anio"),
	COL_CURSO_ANIO_NIVEL("col_curso_anio_nivel"),
	COL_SUBTEMA("col_subtema"),
	CAT_PARENTESCO("cat_parentesco"),
	GES_TRABAJADOR("ges_trabajador"),
	SEG_USUARIO("seg_usuario"),
	PER_PERIODO("per_periodo"),
	CAT_CURSO("cat_curso"),
	CAT_PER_NIVEL("cat_per_nivel"),
	CAT_PER_ACA_NIVEL("cat_per_aca_nivel"),
	CAT_PROVINCIA("cat_provincia"),
	CAT_DISTRITO("cat_distrito"),
	CAT_COND_MATRICULA("cat_cond_matricula"),
	CAT_CENTRO_SALUD("cat_centro_salud");
	private String catalogo;

	CacheEnum(String catalogo) {
		this.catalogo = catalogo;
	}

	public String catalogo() {
		return catalogo;
	}
}
