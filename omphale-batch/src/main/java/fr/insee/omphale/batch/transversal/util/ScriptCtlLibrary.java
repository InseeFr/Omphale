package fr.insee.omphale.batch.transversal.util;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.insee.omphale.batch.transversal.bean.ScriptCtl;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathInspector;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.IClasspathInputStream;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaException;

public class ScriptCtlLibrary {
    
    private Map<String, ScriptCtl> lesScriptsCtl = new HashMap<String, ScriptCtl>();
    private String nomPackageResourceCtl;
    
    public ScriptCtlLibrary(String nomPackageResourceCtl
            ) throws ClasspathException, GroupeJavaException {
        super();
        
        this.nomPackageResourceCtl = nomPackageResourceCtl;
        
        loadPackageCtl();
    }

    private void loadPackageCtl() throws ClasspathException,
    GroupeJavaException {
ClasspathInspector clInspector;
clInspector = new ClasspathInspector(nomPackageResourceCtl, "*.ctl");
Iterator<Entry<String, IClasspathInputStream>> lesStreamSql = clInspector
        .readers();
while (lesStreamSql.hasNext()) {
    Map.Entry<String, IClasspathInputStream> entry = (Map.Entry<String, IClasspathInputStream>) lesStreamSql
            .next();
    try {
        String nomScript = entry.getKey();
        InputStream is = entry.getValue().getInputStream();
        ScriptCtl script = loadUnStreamCtl(is);
        lesScriptsCtl.put(nomScript, script);
    } catch (IOException e) {
        throw new GroupeJavaException(
                "Erreur lors de la lecture du script " + entry.getKey(), e);
    }
}

}

private ScriptCtl loadUnStreamCtl(InputStream is) throws IOException {

LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
String ligneLue = lr.readLine();
StringBuffer ordreSql = new StringBuffer();
while (ligneLue != null) {
    String sql = ligneLue;
    ordreSql.append(sql+"\r\n");
    ligneLue = lr.readLine();
}
    ScriptCtl scriptCtl;
    scriptCtl = new ScriptCtl();
    scriptCtl.setTexteCtl(ordreSql.toString());
    
return scriptCtl;
}

public String getCtlTexte(String unScript, Map<String, String> listeParametres)
 {
    ScriptCtl op = lesScriptsCtl.get(unScript);
    if (op==null) return null;
    return op.getOrdreCtl(listeParametres);

}

public boolean existsUnScript(String script) {
    return lesScriptsCtl.containsKey(script);
}


}
