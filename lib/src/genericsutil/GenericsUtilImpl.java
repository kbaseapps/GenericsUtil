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
import us.kbase.workspace.ObjectIdentity;

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
       look up the type of an object
    */
    public static String lookupWsType(WorkspaceClient wc,
                                      String ref) throws Exception {

        return wc.getObjectInfoNew(new GetObjectInfoNewParams()
                                   .withObjects(Arrays.asList(new ObjectIdentity().withRef(ref)))).get(0).getE3();
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

    /**
       Imports a DataMatrix object from CSV file
    */
    public static ImportDataMatrixResult importDataMatrixCSV(String wsURL,
                                                             String shockURL,
                                                             String serviceWizardURL,
                                                             AuthToken token,
                                                             ImportDataMatrixCSV params) {
        WorkspaceClient wc = createWsClient(wsURL,token);

        String dmRef = null;
        
        ImportDataMatrixResult rv = new ImportDataMatrixResult()
            .withMatrixRef(dmRef);

        return rv;
    }
}
      
