package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Classe basique de lecture d'un fichier de scripts sql. Le fichier de scripts
 * doit être préalablement compatible avec le chargement de l'opération SQL. Ces
 * instructions sont supposées être sous la forme :<br>
 * <code>insert into <i>nomTable</i> values (<i>nomOperation</i>,<i>no_sql</i>,<i>no_ligne</i>,<i>type_sql</i>,<i>texte_sql</i>);</code>
 *
 */
public class BasicSqlReader implements IReaderScriptSql {
	/**
	 * le lecteur de fichiers texte
	 */
	private LineNumberReader lr;

	/**
	 * la ligne lue.
	 */
	private String ligneLue;

	/**
	 * Constructeur.
	 *
	 * @param is
	 *            l'inputStream de lecture du fichier.
	 */
	public BasicSqlReader(InputStream is) {
		super();
		lr = new LineNumberReader(new InputStreamReader(is));
	}

	/**
	 * Constructeur.
	 *
	 * @param lr
	 *            le lecteur du fichier.
	 */
	public BasicSqlReader(LineNumberReader lr) {
		super();
		this.lr = lr;
	}


	public String getInstructionSql() throws IOException {
		return ligneLue;
	}


	public boolean hasNext() {
		try {
			ligneLue = lr.readLine();
		} catch (IOException e) {
			ligneLue = null;
		}
		return ligneLue != null;
	}

}
