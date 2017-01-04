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
       Save NDArray object to workspace, returning reference.
    */
    public static String saveNDArray(WorkspaceClient wc,
                                        String ws,
                                        String name,
                                        NDArray nda,
                                        List<ProvenanceAction> provenance) throws Exception {
        // we may want to calculate some workspace metadata on the
        // matrix when saving it, or create search indices here.
        ObjectSaveData data = new ObjectSaveData()
            .withType("KBaseGenerics.NDArray")
            .withProvenance(provenance)
            .withData(new UObject(nda))
            .withName(name);
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
    public static String joinString(String [] f, int firstField, int lastField) {
        String rv = f[firstField].trim();
        for (int i=firstField+1; i<lastField+1; i++)
            rv += ", "+f[i].trim();
        return rv;
    }

    /**
       helper function to re-assemble part of a CSV line
    */
    public static String joinString(String [] f, int firstField) {
        return joinString(f,firstField,f.length-1);
    }
    
    /**
       helper function to make a new (unmapped) Term from a description
    */
    public static Term makeTerm(String description) {
        Term rv = new Term().withTermName(description);
        return rv;
    }

    /**
       helper function to make a new (unmapped) Value from a description
    */
    public static Value makeValue(String description) {
        Value rv = new Value()
            .withScalarType("string")
            .withStringValue(description);
        return rv;
    }
    
    public static ContextItem makeCI(String description) throws Exception {
        String[] f = description.split(",");
        ContextItem rv = new ContextItem()
            .withProperty(makeTerm(f[0]))
            .withValue(makeValue(f[1]));
        if (f.length == 3)
            rv.setUnits(makeTerm(f[2]));
        if (f.length > 3) {
            throw new Exception("Error in context item '"+description+"'; need only 2 or 3 comma-separated fields: item, value [, units]");
        }
        return rv;
    }

    public static DimensionContextItem makeDCI(String description) throws Exception {
        String[] f = description.split(",");
        DimensionContextItem rv = new DimensionContextItem();
        if (f.length > 1) {
            rv.setProperty(makeTerm(joinString(f,0,f.length-1)));
            rv.setUnits(makeTerm(f[f.length-1]));
        }
        else {
            rv.setProperty(makeTerm(f[0]));
        }
        return rv;
    }
    
    /**
       Imports a NDArray object from CSV file.
       Needs a lot more format checking!
    */
    public static ImportNDArrayResult importNDArrayCSV(String wsURL,
                                                       String shockURL,
                                                       AuthToken token,
                                                       ImportNDArrayCSV params) throws Exception {
        WorkspaceClient wc = createWsClient(wsURL,token);

        // for provenance
        String methodName = "GenericsUtil.importNDArrayCSV";
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

        // read CSV file into NDArray object
        NDArray nda = new NDArray();
            
        BufferedReader infile = IO.openReader(filePath);
        String buffer = null;
        int inDimension = 0;
        Long[] dLengths = null;
        List<DimensionContext> dContexts = null;
        Values curValues = null;
        while ((buffer = infile.readLine()) != null) {
            String[] f = buffer.split(",");
            if ((f==null) || (f.length < 1))
                continue;
            if (f[0].equals("name")) {
                nda.setName(joinString(f,1));
            }
            else if (f[0].equals("description")) {
                if (f.length < 2)
                    throw new Exception("Bad format for description; need at least 2 columns; got: "+buffer);
                nda.setDescription(joinString(f,1));
            }
            else if (f[0].equals("type")) {
                nda.setDataType(makeTerm(joinString(f,1)));
            }
            else if (f[0].equals("values")) {
                if (f.length != 3)
                    throw new Exception("Bad format for values; need 3 columns; got: "+buffer);
                nda.setValueType(makeTerm(f[1]));
                nda.setValueUnits(makeTerm(f[2]));
            }
            else if (f[0].equals("meta")) {
                ContextItem ci = makeCI(joinString(f,1));
                List<ContextItem> cis = nda.getArrayContext();
                if (cis==null)
                    cis = new ArrayList<ContextItem>();
                cis.add(ci);
                nda.setArrayContext(cis);
            }
            else if (f[0].equals("size")) {
                if (f.length < 2)
                    throw new Exception("Bad format for size; need at least 2 columns; got: "+buffer);
                int nDimensions = f.length - 1;
                nda.setDimensionNumber(new Long((long)nDimensions));
                dLengths = new Long[nDimensions];
                dContexts = new ArrayList<DimensionContext>(nDimensions);
                for (int i=0; i<nDimensions; i++) {
                    dLengths[i] = new Long(StringUtil.atol(f[i+1]));
                    dContexts.add(new DimensionContext()
                                  .withDimensionSize(dLengths[i]));
                }
                nda.setDimensionsContext(dContexts);
            }
            else if (f[0].equals("dmeta")) {
                if (f.length < 3)
                    throw new Exception("Bad format for dmeta; need at least 3 columns; got: "+buffer);
                inDimension = StringUtil.atoi(f[1]);
                DimensionContext dc = dContexts.get(inDimension-1);
                long dLength = dLengths[inDimension-1].longValue();
                curValues = new Values()
                    .withScalarType("string")
                    .withStringValues(Arrays.asList(new String[(int)dLength]));
                DimensionContextItem dci = makeDCI(joinString(f,2));
                dci.setValues(curValues);
                List<DimensionContextItem> dcis = dc.getItems();
                if (dcis==null)
                    dcis = new ArrayList<DimensionContextItem>();
                dcis.add(dci);
                dc.setItems(dcis);
            }
            else if (f[0].equals("data")) {
                if (f.length > 1)
                    throw new Exception("Bad format for data; need only 1 column; got: "+buffer);
                inDimension = dLengths.length+1;
                long dLength = 1L;
                for (int i=0; i<dLengths.length; i++)
                    dLength *= dLengths[i];
                curValues = new Values()
                    .withScalarType("string")
                    .withStringValues(Arrays.asList(new String[(int)dLength]));
                nda.setValues(curValues);
            }
            else {
                // must be numeric value greater than 0
                long index = StringUtil.atol(f[0]);
                String val = null;
                if (index <= 0L)
                    throw new Exception("Bad format; was expecting comma-separated index (or indices) starting with 1; got: "+buffer);
                if ((inDimension > dLengths.length) &&
                    (dLengths.length > 1)) {
                    // multidimensional data
                    if (f.length < dLengths.length+1) {
                        throw new Exception("Bad format; was expecting "+(dLengths.length+1)+" columns instead of "+(f.length)+": "+buffer);
                    }
                    val = joinString(f,dLengths.length);
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
                    if (f.length < 2) {
                        throw new Exception("Bad format; was expecting 2 columns instead of "+(f.length)+": "+buffer);
                    }
                    val = joinString(f,1);

                    // convert from 1-based to 0-based indexing
                    index--;
                }

                // store the value in the string data array
                // see not above about converting to the correct
                // type at mapping time
                curValues.getStringValues().set((int)index,val);
            }
        }
        infile.close();

        // save in workspace
        String ndaRef = saveNDArray(wc,
                                    params.getWorkspaceName(),
                                    params.getMatrixName(),
                                    nda,
                                    makeProvenance("Generic N-dimensional Array",
                                                   methodName,
                                                   methodParams));

        ImportNDArrayResult rv = new ImportNDArrayResult()
            .withMatrixRef(ndaRef);

        // clean up tmp file if we used one
        if (isShockFile) {
            java.io.File f = new java.io.File(filePath);
            f.delete();
        }

        return rv;
    }
}
      
