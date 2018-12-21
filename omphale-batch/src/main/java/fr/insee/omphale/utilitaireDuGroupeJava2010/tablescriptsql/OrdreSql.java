package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

/**
 * Bean associé à un ordre SQL.
 *
 */
public class OrdreSql extends ScriptSql {
	@Override
	public void setTexteSql(String texteSql) {
		// suppression des espaces
		String instruction = texteSql.trim();
		// élimination du ; à la fin
		int pos;
		if ((pos = isSeTerminePar(instruction, ';')) != -1) {
			instruction = instruction.substring(0, pos);
		}
		this.texteSql =instruction;
	}

	@Override
	public boolean isSql() {
		return true;
	}
}
