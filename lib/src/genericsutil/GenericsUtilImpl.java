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
       Imports a DataMatrix object from CSV file.
       Needs error checking!
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
        List<Long> dLength = null;
        List<List<DimensionMetadataItem>> dMeta = null;
        DimensionMetadataItem curDMI = null;
        while ((buffer = infile.readLine()) != null) {
            String[] f = buffer.split(",");
            if ((f==null) || (f.length < 1))
                continue;
            if (f[0].equals("name")) {
                dm.setName(f[1]);
            }
            else if (f[0].equals("description")) {
                dm.setDescription(f[1]);
            }
            else if (f[0].equals("values")) {
                String original = f[1];
                for (int i=2; i<f.length; i++)
                    original += ", "+f[i];
                MatrixMetadataItem valuesMeta = new MatrixMetadataItem()
                    .withMetadata(new MetadataItem().withOriginalDescription(original));
                dm.setValuesMetadata(valuesMeta);
            }
            else if (f[0].equals("meta")) {
                String original = f[1];
                for (int i=2; i<f.length; i++)
                    original += ", "+f[i];
                MatrixMetadataItem matrixMetaItem = new MatrixMetadataItem()
                    .withMetadata(new MetadataItem().withOriginalDescription(original));
                List<MatrixMetadataItem> matrixMetadata = dm.getMatrixMetadata();
                if (matrixMetadata==null)
                    matrixMetadata = new ArrayList<MatrixMetadataItem>();
                matrixMetadata.add(matrixMetaItem);
                dm.setMatrixMetadata(matrixMetadata);
            }
            else if (f[0].equals("size")) {
                nDimensions = f.length - 1;
                System.out.println("dimensions = "+nDimensions);
                dm.setNDimensions(new Long((long)nDimensions));
                dLength = new ArrayList<Long>(nDimensions);
                dMeta = new ArrayList<List<DimensionMetadataItem>>(nDimensions);
                for (int i=0; i<nDimensions; i++) {
                    System.out.println("dimensionLength = "+f[i+1]);
                    dLength.set(i,new Long(StringUtil.atol(f[i+1])));
                    dMeta.set(i,new ArrayList<DimensionMetadataItem>());
                }
                dm.setDimensionLength(dLength);
                dm.setDimensionMetadata(dMeta);
            }
            else if (f[0].equals("dmeta")) {
                inDimension = StringUtil.atoi(f[1]);
                String original = f[2];
                for (int i=3; i<f.length; i++)
                    original += ", "+f[i];
                List<DimensionMetadataItem> dMetaI = dMeta.get(inDimension-1);
                curDMI = new DimensionMetadataItem().
                    withMetadata(new MetadataItem().withOriginalDescription(original));
                dMetaI.add(curDMI);
            }
            else if (f[0].equals("data")) {
                inDimension = nDimensions+1;
            }
            else {
                // must be numeric value greater than 0
                if (inDimension <= nDimensions) {
                    // parse curDMI values
                }
                else {
                    // parse data values
                }
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
      
