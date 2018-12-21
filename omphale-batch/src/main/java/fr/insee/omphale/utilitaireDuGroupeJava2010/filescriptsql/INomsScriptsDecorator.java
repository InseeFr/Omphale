package fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql;

public interface INomsScriptsDecorator {

	public abstract String getNomScriptTable(String nomTable);

	public abstract String getNomScriptIndex(String nomIndex);

	public abstract String getNomScriptVue(String nomVue);

	public abstract String getNomScriptSequence(String nomSequence);

}