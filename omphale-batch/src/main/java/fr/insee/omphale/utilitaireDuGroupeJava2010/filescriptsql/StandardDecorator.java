package fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql;

public class StandardDecorator implements INomsScriptsDecorator {
	public static final String PREFIX_CRE_TABLE = "CRE_TABLE_";

	public static final String PREFIX_CRE_VUE = "CRE_VUE_";

	public static final String PREFIX_CRE_INDEX = "CRE_INDEX_";
	public static final String PREFIX_CRE_SEQUENCE = "CRE_SEQUENCE_";


	public String getNomScriptTable(String nomTable) {
		return PREFIX_CRE_TABLE + nomTable + ".sql";
	}


	public String getNomScriptIndex(String nomIndex) {
		return PREFIX_CRE_INDEX + nomIndex + ".sql";
	}


	public String getNomScriptVue(String nomVue) {
		return PREFIX_CRE_VUE + nomVue + ".sql";
	}


	public String getNomScriptSequence(String nomSequence) {
		return PREFIX_CRE_SEQUENCE + nomSequence + ".sql";
	}
}
