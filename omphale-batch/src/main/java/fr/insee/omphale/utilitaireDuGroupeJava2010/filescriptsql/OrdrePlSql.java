package fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql;

/**
 * Bean associé à un ordre PL/SQL.
 *
 */
public class OrdrePlSql extends ScriptSql {

	@Override
	public void setTexteSql(String texteSql) {
		// suppression de espaces
		String instruction = texteSql.trim();
		// élimination du / à la fin
		int pos;
		if ((pos = isSeTerminePar(instruction, '/')) != -1) {
			instruction = instruction.substring(0, pos);
		}
		this.texteSql =instruction;
	}

	@Override
	public boolean isSql() {
		return false;
	}
}
