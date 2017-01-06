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
    public static String saveObject(WorkspaceClient wc,
                                    String ws,
                                    String name,
                                    String objectType,
                                    Object o,
                                    List<ProvenanceAction> provenance) throws Exception {
        // we may want to calculate some workspace metadata on the
        // matrix when saving it, or create search indices here.
        ObjectSaveData data = new ObjectSaveData()
            .withType(objectType)
            .withProvenance(provenance)
            .withData(new UObject(o))
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
       helper function to re-assemble part of a CSV line.  Like
       substring, includes all fields from firstField to lastField-1,
       separated by ", "
    */
    public static String joinString(String [] f, int firstField, int lastField) {
        String rv = f[firstField].trim();
        for (int i=firstField+1; i<lastField; i++)
            rv += ", "+f[i].trim();
        return rv;
    }

    /**
       helper function to re-assemble part of a CSV line.  Assembles
       all fields starting from 0-indexed firstFields, separated
       by ", "
    */
    public static String joinString(String [] f, int firstField) {
        return joinString(f,firstField,f.length);
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

    /**
       splits on commas, then trims parts.  This should be replaced
       with a "real" CSV parser, like OpenCSV.
    */
    public static String[] splitTrim(String x) {
        String[] f = x.split(",");
        for (int i=0; i<f.length; i++)
            f[i] = f[i].trim();
        return f;
    }
    
    /**
       parse a description into a ContextItem
    */
    public static ContextItem makeCI(String description) throws Exception {
        String[] f = splitTrim(description);
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

    /**
       parse a description into a DimensionContextItem, minus the
       values.
    */
    public static DimensionContextItem makeDCI(String description) throws Exception {
        String[] f = splitTrim(description);
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
       Set up a file, either Shock or local; returns path to the
       file.
    */
    public static String setupFile(File f,
                                   String shockURL,
                                   AuthToken token) throws Exception {
        String rv = f.getPath();
        if (rv==null) {
            System.out.println("Getting file from Shock");
            java.io.File tmpFile = java.io.File.createTempFile("mat", ".csv", tempDir);
            tmpFile.delete();
            tmpFile = fromShock(f.getShockId(),
                                shockURL,
                                token,
                                tmpFile,
                                false);
            rv = tmpFile.getPath();
        }
        else
            System.out.println("Reading local file "+rv);
        return rv;
    }

    /**
       Makes a NDArray object from CSV file.
    */
    public static NDArray parseCSV(String filePath) throws Exception {
        // read CSV file into NDArray object
        NDArray nda = new NDArray();
            
        BufferedReader infile = IO.openReader(filePath);
        String buffer = null;
        int inDimension = 0;
        Long[] dLengths = null;
        List<DimensionContext> dContexts = null;
        Values curValues = null;
        while ((buffer = infile.readLine()) != null) {
            String[] f = splitTrim(buffer);
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

        return nda;
    }

    /**
       turn a NDArray into a Matrix2D
    */
    public static Matrix2D makeMatrix2D(NDArray nda) throws Exception {
        int nDimensions = (int)nda.getDimensionNumber().longValue();
        if (nDimensions!=2)
            throw new Exception("Can't make a 2D Matrix out of a "+nDimensions+"-Dimensional Array");
        
        Matrix2D rv = new Matrix2D()
            .withName(nda.getName())
            .withDescription(nda.getDescription())
            .withDataType(nda.getDataType())
            .withMatrixContext(nda.getArrayContext())
            .withValueType(nda.getValueType())
            .withValueUnits(nda.getValueUnits());

        List<DimensionContext> dContexts = nda.getDimensionsContext();
        rv.setXContext(dContexts.get(0));
        rv.setYContext(dContexts.get(1));

        // map values in row major order
        Values oldV = nda.getValues();
        String scalarType = oldV.getScalarType();
        Values2D newV = new Values2D().withScalarType(scalarType);
        Long[] dLengths = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++)
            dLengths[i] = dContexts.get(i).getDimensionSize();
        long[] indices = new long[dLengths.length];

        if (scalarType.equals("string")) {
            newV.setStringValues(new ArrayList<List<String>>((int)dLengths[0].longValue()));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++)
                newV.getStringValues().add(Arrays.asList(new String[(int)dLengths[1].longValue()]));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++) {
                    long index = 0L;
                    for (int i=0; i<dLengths.length; i++) {
                        long k = 1L;
                        for (int j=i+1; j<dLengths.length; j++)
                            k *= dLengths[j].longValue();
                        index += indices[i] * k;
                    }
                    String val = oldV.getStringValues().get((int)index);
                    newV.getStringValues().get((int)indices[0]).set((int)indices[1],val);
                }
            }
        }
        else if (scalarType.equals("float")) {
            newV.setFloatValues(new ArrayList<List<Double>>((int)dLengths[0].longValue()));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++)
                newV.getFloatValues().add(Arrays.asList(new Double[(int)dLengths[1].longValue()]));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++) {
                    long index = 0L;
                    for (int i=0; i<dLengths.length; i++) {
                        long k = 1L;
                        for (int j=i+1; j<dLengths.length; j++)
                            k *= dLengths[j].longValue();
                        index += indices[i] * k;
                    }
                    Double val = oldV.getFloatValues().get((int)index);
                    newV.getFloatValues().get((int)indices[0]).set((int)indices[1],val);
                }
            }
        }
        rv.setValues(newV);
        return rv;
    }

    /**
       turn a NDArray into a Matrix3D
    */
    public static Matrix3D makeMatrix3D(NDArray nda) throws Exception {
        int nDimensions = (int)nda.getDimensionNumber().longValue();
        if (nDimensions!=3)
            throw new Exception("Can't make a 3D Matrix out of a "+nDimensions+"-Dimensional Array");
        
        Matrix3D rv = new Matrix3D()
            .withName(nda.getName())
            .withDescription(nda.getDescription())
            .withDataType(nda.getDataType())
            .withMatrixContext(nda.getArrayContext())
            .withValueType(nda.getValueType())
            .withValueUnits(nda.getValueUnits());

        List<DimensionContext> dContexts = nda.getDimensionsContext();
        rv.setXContext(dContexts.get(0));
        rv.setYContext(dContexts.get(1));
        rv.setZContext(dContexts.get(2));

        // map values in row major order
        Values oldV = nda.getValues();
        String scalarType = oldV.getScalarType();
        Values3D newV = new Values3D().withScalarType(scalarType);
        Long[] dLengths = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++)
            dLengths[i] = dContexts.get(i).getDimensionSize();
        long[] indices = new long[dLengths.length];
        if (scalarType.equals("string")) {
            newV.setStringValues(new ArrayList<List<List<String>>>((int)dLengths[0].longValue()));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                List<List<String>> lls = new ArrayList<List<String>>((int)dLengths[1].longValue());
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++)
                    lls.add(Arrays.asList(new String[(int)dLengths[2].longValue()]));
                newV.getStringValues().add(lls);
            }
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++) {
                    for (indices[2]=0; indices[2]<dLengths[2].longValue(); indices[2]++) {
                        long index = 0L;
                        for (int i=0; i<dLengths.length; i++) {
                            long k = 1L;
                            for (int j=i+1; j<dLengths.length; j++)
                                k *= dLengths[j].longValue();
                            index += indices[i] * k;
                        }
                        String val = oldV.getStringValues().get((int)index);
                        newV.getStringValues().get((int)indices[0]).get((int)indices[1]).set((int)indices[2],val);
                    }
                }
            }
        }
        else if (scalarType.equals("float")) {
            newV.setFloatValues(new ArrayList<List<List<Double>>>((int)dLengths[0].longValue()));
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                List<List<Double>> lls = new ArrayList<List<Double>>((int)dLengths[1].longValue());
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++)
                    lls.add(Arrays.asList(new Double[(int)dLengths[2].longValue()]));
                newV.getFloatValues().add(lls);
            }
            for (indices[0]=0; indices[0]<dLengths[0].longValue(); indices[0]++) {
                for (indices[1]=0; indices[1]<dLengths[1].longValue(); indices[1]++) {
                    for (indices[2]=0; indices[2]<dLengths[2].longValue(); indices[2]++) {
                        long index = 0L;
                        for (int i=0; i<dLengths.length; i++) {
                            long k = 1L;
                            for (int j=i+1; j<dLengths.length; j++)
                                k *= dLengths[j].longValue();
                            index += indices[i] * k;
                        }
                        Double val = oldV.getFloatValues().get((int)index);
                        newV.getFloatValues().get((int)indices[0]).get((int)indices[1]).set((int)indices[2],val);
                    }
                }
            }
        }            
        rv.setValues(newV);        
        return rv;
    }    

    /**
       turn a NDArray into an Array
    */
    public static Array makeArray(NDArray nda) throws Exception {
        int nDimensions = (int)nda.getDimensionNumber().longValue();
        if (nDimensions!=1)
            throw new Exception("Can't make a 1-Dimensional Array out of a "+nDimensions+"-Dimensional Array");
        
        Array rv = new Array()
            .withName(nda.getName())
            .withDescription(nda.getDescription())
            .withDataType(nda.getDataType())
            .withArrayContext(nda.getArrayContext())
            .withValueType(nda.getValueType())
            .withValueUnits(nda.getValueUnits())
            .withXContext(nda.getDimensionsContext().get(0))
            .withValues(nda.getValues());
        
        return rv;
    }

    /**
       update a pre-mapped term with a mapping (in parentheses)
    */
    public static void map(Term t) {
        if (t==null)
            return;
        String name = t.getTermName();
        if (name.endsWith(")")) {
            int pos = name.indexOf(" (");
            String ref = name.substring(pos+2,name.length()-1);
            name = name.substring(0,pos);
            t.setTermName(name);
            t.setOtermRef(ref);
            t.setOtermName(name);
        }
    }

    /**
       update a pre-mapped value with a reference.
    */
    public static void map(Value v) {
        if (v==null)
            return;
        String sv = v.getStringValue();
        if (sv.endsWith(")")) {
            int pos = sv.indexOf(" (");
            String ref = sv.substring(pos+2,sv.length()-1);
            sv = sv.substring(0,pos);
            v.setStringValue(sv);
            if (ref.indexOf(":")>-1) {
                v.setOtermRef(ref);
                v.setScalarType("oterm");
            }
            else {
                v.setObjectRef(ref);
                v.setScalarType("object");
            }
        }
    }

    /**
       update pre-mapped values with a reference.
    */
    public static void map(Values v) {
        if (v==null)
            return;
        List<String> svs = v.getStringValues();
        if (svs.get(0).endsWith(")")) {
            int l = svs.size();
            List<String> refs = new ArrayList<String>(l);
            for (int i=0; i<l; i++) {
                String sv = svs.get(i);
                int pos = sv.indexOf(" (");
                String ref = sv.substring(pos+2,sv.length()-1);
                sv = sv.substring(0,pos);
                svs.set(i,sv);
                refs.add(ref);
            }
            v.setOtermRefs(refs);
            v.setScalarType("oterm");
        }
    }
    
    /**
       update a Values object from String to Float
    */
    public static void makeFloatValues(Values v) {
        List<String> sv = v.getStringValues();
        int l = sv.size();
        List<Double> fv = new ArrayList<Double>(l);
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val == null)
                fv.add(null);
            else
                fv.add(new Double(StringUtil.atod(val)));
        }
        v.setStringValues(null);
        v.setFloatValues(fv);
        v.setScalarType("float");
    }
    
    /**
       Map premapped types in a ContextItem
    */
    public static void map(ContextItem ci) {
        if (ci==null)
            return;
        map(ci.getProperty());
        map(ci.getValue());
        Term u = ci.getUnits();
        if (u != null) {
            map(u);
        }
    }

    /**
       Map premapped types in a DimensionContextItem
    */
    public static void map(DimensionContextItem dci) {
        if (dci==null)
            return;
        map(dci.getProperty());
        Values v = dci.getValues();
        map(v);
        Term u = dci.getUnits();
        if (u != null) {
            map(u);
            String ref = u.getOtermRef();
            if ((ref != null) &&
                (ref.indexOf(":") > -1))
                makeFloatValues(v);
        }
    }

    /**
       Map premapped types in a DimensionContext
    */
    public static void map(DimensionContext dc) {
        if (dc==null)
            return;
        for (DimensionContextItem dci : dc.getItems())
            map(dci);
    }
    
    /**
       Map premapped types in NDArray
    */
    public static void map(NDArray nda) {
        map(nda.getDataType());
        for (ContextItem ci : nda.getArrayContext())
            map(ci);
        for (DimensionContext dc : nda.getDimensionsContext())
            map(dc);
        map(nda.getValueType());
        map(nda.getValueUnits());
        makeFloatValues(nda.getValues());
    }
    
    /**
       Imports a generic object from CSV file.
       Needs a lot more format checking!
    */
    public static ImportCSVResult importCSV(String wsURL,
                                            String shockURL,
                                            AuthToken token,
                                            ImportCSVParams params) throws Exception {
        WorkspaceClient wc = createWsClient(wsURL,token);

        // for provenance
        String methodName = "GenericsUtil.importCSV";
        List<UObject> methodParams = Arrays.asList(new UObject(params));

        // looks for local file; if not given, get from shock
        String filePath = setupFile(params.getFile(), shockURL, token);

        // parse it into the object
        NDArray nda = parseCSV(filePath);

        // pre-map, for debugging
        boolean DEBUG = true;
        if (DEBUG)
            map(nda);

        // convert to other object types if necessary
        String objectType = params.getObjectType();
        Object o = nda;
        if (objectType.equals("KBaseGenerics.Matrix2D"))
            o = makeMatrix2D(nda);
        else if (objectType.equals("KBaseGenerics.Matrix3D"))
            o = makeMatrix3D(nda);
        else if (objectType.equals("KBaseGenerics.Array"))
            o = makeArray(nda);

        // save in workspace
        String objectRef = saveObject(wc,
                                      params.getWorkspaceName(),
                                      params.getObjectName(),
                                      params.getObjectType(),
                                      o,
                                      makeProvenance(params.getObjectType()+" imported from CSV file",
                                                     methodName,
                                                     methodParams));

        ImportCSVResult rv = new ImportCSVResult()
            .withObjectRef(objectRef);

        // clean up tmp file if we used one
        if (params.getFile().getPath()==null) {
            java.io.File f = new java.io.File(filePath);
            f.delete();
        }

        return rv;
    }
}
      
