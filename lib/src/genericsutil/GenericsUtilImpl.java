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
                                    Map<String,String> metadata,
                                    List<ProvenanceAction> provenance) throws Exception {
        // we may want to calculate some workspace metadata on the
        // matrix when saving it, or create search indices here.
        ObjectSaveData data = new ObjectSaveData()
            .withType(objectType)
            .withProvenance(provenance)
            .withData(new UObject(o))
            .withMeta(metadata)
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
       parse a description into a TypedValue
    */
    public static TypedValue makeTV(String description) throws Exception {
        String[] f = splitTrim(description);
        TypedValue rv = new TypedValue()
            .withValueType(makeTerm(f[0]))
            .withValue(makeValue(f[1]));
        if (f.length == 3)
            rv.setValueUnits(makeTerm(f[2]));
        if (f.length > 3) {
            throw new Exception("Error in typed value '"+description+"'; need only 2 or 3 comma-separated fields: item, value [, units]");
        }
        return rv;
    }

    /**
       parse a description into a TypedValues, minus the
       values.
    */
    public static TypedValues makeTVS(String description) throws Exception {
        String[] f = splitTrim(description);
        TypedValues rv = new TypedValues();
        if (f.length > 1) {
            rv.setValueType(makeTerm(f[0]));
            if (f.length > 2) {
                List<TypedValue> vc = new ArrayList<TypedValue>();
                // make user specify type for now;
                // it should be auto-detected!
                int nContext = 0;
                if ((f.length % 2) == 0)
                    nContext = (f.length-2)/2;
                else
                    nContext = (f.length-1)/2;
                for (int i=0; i<nContext; i++)
                    vc.add(makeTV(joinString(f,i*2+1,i*2+3)));
                rv.setValueContext(vc);
                if ((f.length % 2) == 0)
                    rv.setValueUnits(makeTerm(f[f.length-1]));
            }
            else
                rv.setValueUnits(makeTerm(f[f.length-1]));
        }
        else {
            rv.setValueType(makeTerm(f[0]));
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
            if (f[0].equals("name"))
                nda.setName(joinString(f,1));
            else if (f[0].equals("description")) {
                if (f.length < 2)
                    throw new Exception("Bad format for description; need at least 2 columns; got: "+buffer);
                nda.setDescription(joinString(f,1));
            }
            else if (f[0].equals("type"))
                nda.setDataType(makeTerm(joinString(f,1)));
            else if (f[0].equals("values")) {
                if (f.length < 3)
                    throw new Exception("Bad format for values; need at least 3 columns; got: "+buffer);
                nda.setTypedValues(makeTVS(joinString(f,1)));
            }
            else if (f[0].equals("meta")) {
                TypedValue tv = makeTV(joinString(f,1));
                List<TypedValue> tvl = nda.getArrayContext();
                if (tvl==null)
                    tvl = new ArrayList<TypedValue>();
                tvl.add(tv);
                nda.setArrayContext(tvl);
            }
            else if (f[0].equals("size")) {
                if (f.length < 2)
                    throw new Exception("Bad format for size; need at least 2 columns; got: "+buffer);
                int nDimensions = f.length - 1;
                nda.setNDimensions(new Long((long)nDimensions));
                dLengths = new Long[nDimensions];
                dContexts = new ArrayList<DimensionContext>(nDimensions);
                for (int i=0; i<nDimensions; i++) {
                    dLengths[i] = new Long(StringUtil.atol(f[i+1]));
                    dContexts.add(new DimensionContext()
                                  .withSize(dLengths[i]));
                }
                nda.setDimContext(dContexts);
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
                dc.setDataType(makeTerm(f[2]));
                TypedValues tvs = null;
                if (f.length > 3)
                    tvs = makeTVS(joinString(f,3));
                else
                    tvs = makeTVS(f[2]);
                tvs.setValues(curValues);
                List<TypedValues> tvsl = dc.getTypedValues();
                if (tvsl==null)
                    tvsl = new ArrayList<TypedValues>();
                tvsl.add(tvs);
                dc.setTypedValues(tvsl);
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
                nda.getTypedValues().setValues(curValues);
            }
            else {
                // must be numeric value greater than 0
                long index = StringUtil.atol(f[0]);
                String val = null;
                if (index <= 0L)
                    throw new Exception("Bad format; was expecting comma-separated index (or indices) starting with 1; got: "+buffer);
                if ((inDimension <= dLengths.length) &&
                    (index > dLengths[inDimension-1].longValue()))
                    throw new Exception("Bad format; index '"+index+"' is greater than the dimension length ("+dLengths[inDimension-1].longValue()+") for dimension "+(inDimension)+" in line: "+buffer);

                // handle multi-dimensional data
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
                    for (int i=1; i<dLengths.length; i++)
                        indices[i] = StringUtil.atol(f[i]);
                    // check they're not too large or small
                    for (int i=0; i<dLengths.length; i++) {
                        if (indices[i] <= 0L)
                            throw new Exception("Bad format; all indices should start at 1; bad line is: "+buffer);
                        if (indices[i] > dLengths[i].longValue())
                            throw new Exception("Bad format; index '"+indices[i]+"' is greater than the dimension length ("+dLengths[i].longValue()+") for dimension "+(i+1)+" in line: "+buffer);
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
                        throw new Exception("Bad format; was expecting 2 columns instead of "+(f.length)+" in line: "+buffer);
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
                v.setScalarType("oterm_ref");
            }
            else {
                v.setObjectRef(ref);
                v.setScalarType("object_ref");
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
            v.setScalarType("oterm_ref");
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
       Map premapped types in a TypedValue
    */
    public static void map(TypedValue tv) {
        if (tv==null)
            return;
        map(tv.getValueType());
        map(tv.getValue());
        Term u = tv.getValueUnits();
        if (u != null) {
            map(u);
        }
    }

    /**
       Map premapped types in a TypedValues
    */
    public static void map(TypedValues tvs) {
        if (tvs==null)
            return;
        map(tvs.getValueType());
        List<TypedValue> vc = tvs.getValueContext();
        if (vc != null) {
            for (TypedValue tv : vc)
                map(tv);
        }
        Values v = tvs.getValues();
        map(v);
        Term u = tvs.getValueUnits();
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
        map(dc.getDataType());
        for (TypedValues tvs : dc.getTypedValues())
            map(tvs);
    }
    
    /**
       Map premapped types in NDArray
    */
    public static void map(NDArray nda) {
        map(nda.getDataType());
        for (DimensionContext dc : nda.getDimContext())
            map(dc);
        map(nda.getTypedValues());
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
        boolean DEBUG = false;
        if (DEBUG)
            map(nda);

        // save as other type if necessary (not implemented)
        String objectType = params.getObjectType();
        Object o = nda;

        // make metadata
        Map<String,String> metadata = new HashMap<String,String>();
        metadata.put("mapped","false");
        metadata.put("n_dimensions", nda.getNDimensions().toString());
        String typeDescriptor = nda.getDataType().getTermName();
        typeDescriptor += " <"+nda.getTypedValues().getValueType().getTermName()+"> (";
        for (DimensionContext dc : nda.getDimContext()) {
            if (!typeDescriptor.endsWith("("))
                typeDescriptor += " x ";
            typeDescriptor += dc.getDataType().getTermName();
        }
        typeDescriptor += ")";
        metadata.put("data_type",typeDescriptor);

        // save in workspace
        String objectRef = saveObject(wc,
                                      params.getWorkspaceName(),
                                      params.getObjectName(),
                                      objectType,
                                      o,
                                      metadata,
                                      makeProvenance(objectType+" imported from CSV file",
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
      
