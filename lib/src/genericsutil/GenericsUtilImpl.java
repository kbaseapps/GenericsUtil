package genericsutil;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import java.net.URL;

import us.kbase.auth.AuthService;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.*;
import us.kbase.workspace.*;

import us.kbase.common.utils.CorrectProcess;
import us.kbase.workspace.ObjectData;

import kbasereport.*;
import us.kbase.kbasegenerics.*;

import org.strbio.IO;
import org.strbio.io.*;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

/**
   This class implements methods (currently just importing)
   required to deal with KBase Generics
*/
public class GenericsUtilImpl {
    protected static java.io.File tempDir = new java.io.File("/kb/module/work/tmp/");

    /**
       creates a workspace client; if token is null, client can
       only read public workspaces.
    */
    public static WorkspaceClient createWsClient(String wsURL,
                                                 AuthToken token) throws Exception {
        WorkspaceClient rv = null;

        if (token==null)
            rv = new WorkspaceClient(new URL(wsURL));
        else
            rv = new WorkspaceClient(new URL(wsURL),token);
        rv.setAuthAllowedForHttp(true);
        return rv;
    }

    /**
       Make and save Report object, returning its name and reference
    */
    public static String[] makeReport(AuthToken token,
                                      String ws,
                                      String reportText,
                                      List<String> warnings,
                                      List<WorkspaceObject> objects) throws Exception {
        Report report = new Report()
            .withTextMessage(reportText)
            .withWarnings(warnings)
            .withObjectsCreated(objects);

        KBaseReportClient rc = new KBaseReportClient(new URL(System.getenv("SDK_CALLBACK_URL")),token);
        rc.setIsInsecureHttpConnectionAllowed(true);
        ReportInfo ri = rc.create(new CreateParams()
                                  .withReport(report)
                                  .withWorkspaceName(ws));

        return new String[] { ri.getName(), ri.getRef() };
    }

    private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
        return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
    }

    /**
       Imports a DataMatrix object from CSV file
    */
    public static ImportDataMatrixResult importDataMatrixCSV(String wsURL,
                                                             String shockURL,
                                                             String serviceWizardURL,
                                                             AuthToken token,
                                                             ImportDataMatrixCSV params) throws Exception {
        WorkspaceClient wc = createWsClient(wsURL,token);

        // currently only supports local files;
        // to work in UI, needs to support shock files also
        String filePath = params.getFile().getPath();

        // read CSV file into matrix object
        DataMatrix dm = new DataMatrix().withOntologiesMapped(new Long(0L));
            
        BufferedReader infile = IO.openReader(filePath);
        String buffer = null;
        int inDimension = 0;
        int nDimensions = 0;
        while ((buffer = infile.readLine()) != null) {
            String[] f = buffer.split(",");
            if (f[0].equals("name")) {
                dm.setName(f[1]);
            }
            else if (f[0].equals("description")) {
                dm.setDescription(f[1]);
            }
            else if (f[1].equals("values")) {
                dm.setDescription(f[1]);
            }
        }
        infile.close();

        // save it
        String dmRef =
            getRefFromObjectInfo(wc.saveObjects(new SaveObjectsParams()
                                                .withWorkspace(params.getWorkspaceName())
                                                .withObjects(Arrays.asList(new ObjectSaveData()
                                                                           .withType("KBaseGenerics.DataMatrix")
                                                                           .withName(params.getMatrixName())
                                                                           .withData(new UObject(dm))))).get(0));
        
        
        ImportDataMatrixResult rv = new ImportDataMatrixResult()
            .withMatrixRef(dmRef);

        return rv;
    }
}
      
