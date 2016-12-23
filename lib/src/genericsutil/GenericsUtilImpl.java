package genericsutil;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import java.net.URL;

import us.kbase.auth.*;
import us.kbase.common.service.*;
import us.kbase.workspace.*;
import us.kbase.shock.client.*;

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

    /**
       Helper function to get reference when saving an object
    */
    private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
        return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
    }

    /**
       Save DataMatrix object to workspace, returning reference.
    */
    public static String saveDataMatrix(WorkspaceClient wc,
                                        String ws,
                                        String dmName,
                                        DataMatrix dm,
                                        List<ProvenanceAction> provenance) throws Exception {
        // we may want to calculate some workspace metadata on the
        // matrix when saving it, or create search indices here.
        ObjectSaveData data = new ObjectSaveData()
            .withType("KBaseGenerics.DataMatrix")
            .withProvenance(provenance)
            .withData(new UObject(dm))
            .withName(dmName);
        return getRefFromObjectInfo(wc.saveObjects(new SaveObjectsParams().withWorkspace(ws).withObjects(Arrays.asList(data))).get(0));
    }

    /**
       Make a provenance object
    */
    public static List<ProvenanceAction> makeProvenance(String description,
                                                        String methodName,
                                                        List<UObject> methodParams) throws Exception {

        // to get version:
        GenericsUtilServer server = new GenericsUtilServer();
        
        return new ArrayList<ProvenanceAction>
            (Arrays.asList(new ProvenanceAction()
                           .withDescription(description)
                           .withService("GenericsUtil")
                           .withServiceVer((String)server.status().get("version"))
                           .withMethod(methodName)
                           .withMethodParams(methodParams)));
    }

    /**
       Load a file from Shock; returns File, or null if file couldn't be read.
       If the file from Shock is 0-length, it is deleted and null is returned.
    */
    public static java.io.File fromShock(String shockID,
                                         String shockUrl,
                                         AuthToken token,
                                         java.io.File f,
                                         boolean gzip) throws Exception {

        // System.err.println("shock cmd equivalent to "+"/usr/bin/curl -k -X GET "+shockUrl+" -H \"Authorization: OAuth "+token.toString()+"\""+(gzip ? "| /bin/gzip" : ""));
        
        BasicShockClient shockClient = new BasicShockClient(new URL(shockUrl), token);
        ShockNode sn = shockClient.getNode(new ShockNodeId(shockID));
        OutputStream os = new FileOutputStream(f);
        if (gzip)
            os = new GZIPOutputStream(os);

        shockClient.getFile(sn,os);

        if (f.length()==0)
            f.delete();

        if (!f.canRead())
            return null;
        
        return f;
    }

    /**
       helper function to re-assemble part of a CSV line
    */
    public static String joinString(String [] f, int firstField) {
        String rv = f[firstField].trim();
        for (int i=firstField+1; i<f.length; i++)
            rv += ", "+f[i].trim();
        return rv;
    }
    
    /**
       helper function to make a metadata item from a description
    */
    public static MetadataItem makeMDI(String originalDescription) {
        MetadataItem rv = new MetadataItem()
            .withOriginalDescription(originalDescription);
        return rv;
    }

    /**
       Imports a DataMatrix object from CSV file.
       Needs a lot more format checking!
    */
    public static ImportDataMatrixResult importDataMatrixCSV(String wsURL,
                                                             String shockURL,
                                                             AuthToken token,
                                                             ImportDataMatrixCSV params) throws Exception {
        WorkspaceClient wc = createWsClient(wsURL,token);

        // for provenance
        String methodName = "GenericsUtil.importDataMatrixCSV";
        List<UObject> methodParams = Arrays.asList(new UObject(params));

        // looks for local file; if not given, get from shock
        String filePath = params.getFile().getPath();
        boolean isShockFile = false;
        if (filePath==null) {
            isShockFile = true;
            System.out.println("Getting file from Shock");
            java.io.File tmpFile = java.io.File.createTempFile("mat", ".csv", tempDir);
            tmpFile.delete();
            tmpFile = fromShock(params.getFile().getShockId(),
                                shockURL,
                                token,
                                tmpFile,
                                false);
            filePath = tmpFile.getPath();
        }
        else
            System.out.println("Reading local file "+filePath);

        // read CSV file into matrix object
        DataMatrix dm = new DataMatrix().withOntologiesMapped(new Long(0L));
            
        BufferedReader infile = IO.openReader(filePath);
        String buffer = null;
        int inDimension = 0;
        Long[] dLengths = null;
        List<List<DimensionMetadataItem>> dMeta = null;
        DataValues curDV = null;
        while ((buffer = infile.readLine()) != null) {
            String[] f = buffer.split(",");
            if ((f==null) || (f.length < 1))
                continue;
            if (f[0].equals("name")) {
                dm.setName(joinString(f,1));
            }
            else if (f[0].equals("description")) {
                if (f.length < 2)
                    throw new Exception("Bad format for description; need at least 2 columns; got: "+buffer);
                dm.setDescription(joinString(f,1));
            }
            else if (f[0].equals("values")) {
                if (f.length < 3)
                    throw new Exception("Bad format for values; need at least 3 columns; got: "+buffer);
                MatrixMetadataItem valuesMeta = new MatrixMetadataItem()
                    .withMetadata(makeMDI(joinString(f,1)));
                dm.setValuesMetadata(valuesMeta);
                // NOTE TO PAVEL:  Values metadata should probably
                // just be a MetadataItem, not a MatrixMetadataItem;
                // I can't think of a circumstance where you need values here.
                // --JMC
            }
            else if (f[0].equals("meta")) {
                MatrixMetadataItem mmdi = new MatrixMetadataItem()
                    .withMetadata(makeMDI(joinString(f,1)));
                List<MatrixMetadataItem> mmd = dm.getMatrixMetadata();
                if (mmd==null)
                    mmd = new ArrayList<MatrixMetadataItem>();
                mmd.add(mmdi);
                dm.setMatrixMetadata(mmd);
            }
            else if (f[0].equals("size")) {
                if (f.length < 2)
                    throw new Exception("Bad format for size; need at least 2 columns; got: "+buffer);
                int nDimensions = f.length - 1;
                dm.setNDimensions(new Long((long)nDimensions));
                dLengths = new Long[nDimensions];
                dMeta = new ArrayList<List<DimensionMetadataItem>>(nDimensions);
                for (int i=0; i<nDimensions; i++) {
                    dLengths[i] = new Long(StringUtil.atol(f[i+1]));
                    dMeta.add(new ArrayList<DimensionMetadataItem>());
                }
                dm.setDimensionLength(Arrays.asList(dLengths));
                dm.setDimensionMetadata(dMeta);
            }
            else if (f[0].equals("dmeta")) {
                if (f.length < 4)
                    throw new Exception("Bad format for dmeta; need at least 4 columns; got: "+buffer);
                inDimension = StringUtil.atoi(f[1]);
                List<DimensionMetadataItem> dmdis = dMeta.get(inDimension-1);
                long dLength = dLengths[inDimension-1].longValue();
                // NOTE TO PAVEL:
                // I'm initially storing all values as strings.
                // They should be converted to other data types
                // (references, integers, floats, etc)
                // at the time of Ontology mapping.
                // The correct primitive type is stored in the
                // ontology; e.g.: property_value: data_type float
                // --JMC
                curDV = new DataValues()
                    .withN(new Long(dLength))
                    .withPrimitiveType("string")
                    .withStringData(Arrays.asList(new String[(int)dLength]));
                DimensionMetadataItem dmdi = new DimensionMetadataItem()
                    .withMetadata(makeMDI(joinString(f,2)))
                    .withValues(curDV);
                dmdis.add(dmdi);
            }
            else if (f[0].equals("data")) {
                if (f.length > 1)
                    throw new Exception("Bad format for data; need only 1 column; got: "+buffer);
                inDimension = dLengths.length+1;
                long dLength = 1L;
                for (int i=0; i<dLengths.length; i++)
                    dLength *= dLengths[i];
                curDV = new DataValues()
                    .withN(new Long(dLength))
                    .withPrimitiveType("string")
                    .withStringData(Arrays.asList(new String[(int)dLength]));
                dm.setData(curDV);
            }
            else {
                // must be numeric value greater than 0
                long index = StringUtil.atol(f[0]);
                if (index <= 0L)
                    throw new Exception("Bad format; was expecting comma-separated index (or indices) starting with 1; got: "+buffer);
                if ((inDimension > dLengths.length) &&
                    (dLengths.length > 1)) {
                    // multidimensional data
                    if (f.length != dLengths.length+1) {
                        throw new Exception("Bad format; was expecting "+(dLengths.length+1)+" columns instead of "+(f.length)+": "+buffer);
                    }
                    // get all the dimensions
                    long[] indices = new long[dLengths.length];
                    indices[0] = index;
                    for (int i=1; i<dLengths.length; i++) {
                        indices[i] = StringUtil.atol(f[i]);
                        if (indices[i] <= 0L)
                            throw new Exception("Bad format; was expecting comma-separated index (or indices) starting with 1; got: "+buffer);
                    }
                    // use row major order to find real index:
                    index = 0L;
                    for (int i=0; i<dLengths.length; i++) {
                        long k = 1L;
                        for (int j=i+1; j<dLengths.length; j++)
                            k *= dLengths[j].longValue();
                        index += (indices[i]-1) * k;
                    }
                }
                else {
                    if (f.length != 2) {
                        throw new Exception("Bad format; was expecting "+(dLengths.length+1)+" columns instead of "+(f.length)+": "+buffer);
                    }
                    // convert from 1-based to 0-based indexing
                    index--;
                }

                // store the value in the string data array
                // see not above about converting to the correct
                // type at mapping time
                curDV.getStringData().set((int)index,f[f.length-1]);
            }
        }
        infile.close();

        // save in workspace
        String dmRef = saveDataMatrix(wc,
                                      params.getWorkspaceName(),
                                      params.getMatrixName(),
                                      dm,
                                      makeProvenance("Data Matrix",
                                                     methodName,
                                                     methodParams));

        ImportDataMatrixResult rv = new ImportDataMatrixResult()
            .withMatrixRef(dmRef);

        // clean up tmp file if we used one
        if (isShockFile) {
            java.io.File f = new java.io.File(filePath);
            f.delete();
        }

        return rv;
    }
}
      
