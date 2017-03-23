package genericsutil;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
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

import com.opencsv.*;

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
       helper function to quote a string, but only if contains commas
    */
    public static String maybeQuote(String s) {
        if (s.indexOf(",")>-1)
            return "\""+s+"\"";
        return s;
    }

    /**
       helper function to unquote a string
    */
    public static String unquote(String s) {
        if ((s.startsWith("\"")) &&
            (s.endsWith("\"")))
            return s.substring(1,s.length()-1);
        return s;
    }

    /**
       helper function to re-assemble part of a CSV line.  Like
       substring, includes all fields from firstField to lastField-1,
       separated by ", "
    */
    public static String joinString(String [] f, int firstField, int lastField) {
        String rv = maybeQuote(f[firstField].trim());
        for (int i=firstField+1; i<lastField; i++)
            rv += ", "+maybeQuote(f[i].trim());
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
       splits on commas, then trims parts.
    */
    public static String[] splitTrim(String x) throws Exception {
        CSVParser parser = new CSVParser();
        String[] rv = parser.parseLine(x);
        for (int i=0; i<rv.length; i++)
            rv[i] = rv[i].trim();
        return rv;
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
       Makes a HNDArray object from CSV file.
       Reads NDArrays also, modeling them as an HNDArray with
       a single value in the heterogeneous dimension
    */
    public static HNDArray parseCSV(String filePath) throws Exception {
        // read CSV file into HNDArray object
        HNDArray hnda = new HNDArray();
        boolean isHeterogeneous = true; // assume HNDA unless we see
        // a "values" annotation
        
        BufferedReader infile = IO.openReader(filePath);
        String buffer = null;
        int inDimension = 0;
        Long[] dLengths = null;
        List<DimensionContext> dContexts = null;
        Values curValues = null;
        boolean setupHets = false;
        while ((buffer = infile.readLine()) != null) {
            String[] f = splitTrim(buffer);
            if ((f==null) || (f.length < 1))
                continue;
            if (f[0].equals("name"))
                hnda.setName(f[1]);
            else if (f[0].equals("description")) {
                if (f.length < 2)
                    throw new Exception("Bad format for description; need at least 2 columns; got: "+buffer);
                hnda.setDescription(f[1]);
            }
            else if (f[0].equals("type"))
                hnda.setDataType(makeTerm(f[1]));
            else if (f[0].equals("values")) {
                if (f.length < 2)
                    throw new Exception("Bad format for values; need at least 2 columns; got: "+buffer);
                isHeterogeneous = false;
                hnda.setTypedValues(Arrays.asList(makeTVS(joinString(f,1))));
            }
            else if (f[0].equals("meta")) {
                TypedValue tv = makeTV(joinString(f,1));
                List<TypedValue> tvl = hnda.getArrayContext();
                if (tvl==null)
                    tvl = new ArrayList<TypedValue>();
                tvl.add(tv);
                hnda.setArrayContext(tvl);
            }
            else if (f[0].equals("size")) {
                if (f.length < 2)
                    throw new Exception("Bad format for size; need at least 2 columns; got: "+buffer);
                int nDimensions = f.length - 1;
                hnda.setNDimensions(new Long((long)nDimensions));
                dLengths = new Long[nDimensions];
                dContexts = new ArrayList<DimensionContext>(nDimensions);
                for (int i=0; i<nDimensions; i++) {
                    dLengths[i] = new Long(StringUtil.atol(f[i+1]));
                    dContexts.add(new DimensionContext()
                                  .withSize(dLengths[i]));
                }
                hnda.setDimContext(dContexts);
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
                // set data type for entire dimension if not already set
                // by another dmeta record
                if (dc.getDataType()==null)
                    dc.setDataType(makeTerm(f[2]));

                // set values type; could be the same as the data type
                // if uploader doesn't specify a different type
                TypedValues tvs = null;
                if (f.length > 3)
                    tvs = makeTVS(joinString(f,3));
                else
                    tvs = makeTVS(maybeQuote(f[2]));
                tvs.setValues(curValues);
                List<TypedValues> tvsl = dc.getTypedValues();
                if (tvsl==null)
                    tvsl = new ArrayList<TypedValues>();
                tvsl.add(tvs);
                dc.setTypedValues(tvsl);

                // if this is first set of dimension metadata,
                // we need to set up array of heterogenous value types
                if ((inDimension == 1) &&
                    (isHeterogeneous) &&
                    (hnda.getTypedValues()==null)) {
                    tvsl = Arrays.asList(new TypedValues[(int)dLength]);
                    hnda.setTypedValues(tvsl);
                    setupHets = true;
                }
                else
                    setupHets = false;
            }
            else if (f[0].equals("data")) {
                if (f.length > 1)
                    throw new Exception("Bad format for data; need only 1 column; got: "+buffer);
                inDimension = dLengths.length+1;
                int firstHomogeneousDimension = 0;
                int nTVS = 1;
                if (isHeterogeneous) {
                    firstHomogeneousDimension = 1;
                    nTVS = (int)(dLengths[0].longValue());
                }
                // length of each data values array
                // is product of all homogeneous dimensions
                long dLength = 1L;
                for (int i=firstHomogeneousDimension; i<dLengths.length; i++)
                    dLength *= dLengths[i];
                for (int i=0; i<nTVS; i++) {
                    curValues = new Values()
                        .withScalarType("string")
                        .withStringValues(Arrays.asList(new String[(int)dLength]));
                    hnda.getTypedValues().get(i).setValues(curValues);
                }
            }
            else {
                // first index must be numeric value valid for current
                // dimension (i.e., between 1 and the dimension length)
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
                    // if heterogenous data, set curValues to correct set
                    int firstHomogeneousDimension = 0;
                    if (isHeterogeneous) {
                        firstHomogeneousDimension = 1;
                        curValues = hnda.getTypedValues().get((int)(indices[0])-1).getValues();
                    }
                    // use row major order to find real index:
                    index = 0L;
                    for (int i=firstHomogeneousDimension; i<dLengths.length; i++) {
                        long k = 1L;
                        for (int j=i+1; j<dLengths.length; j++)
                            k *= dLengths[j].longValue();
                        index += (indices[i]-1) * k;
                    }
                }
                else { // single-dimensional data
                    if (f.length < 2) {
                        throw new Exception("Bad format; was expecting 2 columns instead of "+(f.length)+" in line: "+buffer);
                    }
                    val = joinString(f,1);

                    // convert from 1-based to 0-based indexing
                    index--;
                }

                if (setupHets) {
                    // treat each row like a "values" line
                    TypedValues tvs = makeTVS(val);
                    hnda.getTypedValues().set((int)index, tvs);
                }

                // store the value in the string data array
                // see not above about converting to the correct
                // type at mapping time
                curValues.getStringValues().set((int)index,unquote(val));
            }
        }
        infile.close();

        return hnda;
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
       update a Values object from String to Boolean
    */
    public static void makeBooleanValues(Values v) {
        List<String> sv = v.getStringValues();
        int l = sv.size();
        List<Long> bv = new ArrayList<Long>(l);
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val == null)
                bv.add(null);
            else
                bv.add(new Long(StringUtil.atol(val)));
        }
        v.setStringValues(null);
        v.setBooleanValues(bv);
        v.setScalarType("boolean");
    }
    
    /**
       update a Values object from String to Int
    */
    public static void makeIntValues(Values v) {
        List<String> sv = v.getStringValues();
        int l = sv.size();
        List<Long> iv = new ArrayList<Long>(l);
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val == null)
                iv.add(null);
            else
                iv.add(new Long(StringUtil.atol(val)));
        }
        v.setStringValues(null);
        v.setIntValues(iv);
        v.setScalarType("int");
    }

    /**
       update a Values object from String to another type, based
       on a reference term
    */
    public static void transformValues(String ref, Values v) {
        List<String> sv = v.getStringValues();
        boolean allNumeric = true;
        boolean allBoolean = true;
        boolean allInt = true;
        int l = sv.size();
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val != null) {
                allNumeric &= Pattern.matches("^-?[0-9]*\\.?[0-9]+$",val);
                allInt &= Pattern.matches("^-?[0-9]+$",val);
                allBoolean &= Pattern.matches("^[01]$",val);
            }
        }
        if (allBoolean)
            makeBooleanValues(v);
        else if (allNumeric) {
            // kludge: should get this from ontology service
            if (allInt && (ref.equals("ME:0000005")))
                makeIntValues(v);
            else
                makeFloatValues(v);
        }
    }

    /**
       update a Value object from String to Float
    */
    public static void makeFloatValue(Value v) {
        String sv = v.getStringValue();
        if (sv == null)
            v.setFloatValue(null);
        else
            v.setFloatValue(new Double(StringUtil.atod(sv)));
        v.setStringValue(null);
        v.setScalarType("float");
    }

    /**
       update a Value object from String to Boolean
    */
    public static void makeBooleanValue(Value v) {
        String sv = v.getStringValue();
        if (sv == null)
            v.setBooleanValue(null);
        else
            v.setBooleanValue(new Long(StringUtil.atol(sv)));
        v.setStringValue(null);
        v.setScalarType("boolean");
    }
    
    /**
       update a Value object from String to Int
    */
    public static void makeIntValue(Value v) {
        String sv = v.getStringValue();
        if (sv == null)
            v.setIntValue(null);
        else
            v.setIntValue(new Long(StringUtil.atol(sv)));
        v.setStringValue(null);
        v.setScalarType("int");
    }

    /**
       update a Value object from String to another type, based
       on a reference term
    */
    public static void transformValue(String ref, Value v) {
        String sv = v.getStringValue();
        boolean isNumeric = Pattern.matches("^-?[0-9]*\\.?[0-9]+$",sv);
        boolean isBoolean = Pattern.matches("^[01]$",sv);
        boolean isInt = Pattern.matches("^-?[0-9]+$",sv);
        if (isBoolean)
            makeBooleanValue(v);
        else if (isNumeric) {
            // kludge: should get this from ontology service
            if (isInt && (ref.equals("ME:0000005")))
                makeIntValue(v);
            else
                makeFloatValue(v);
        }
    }
    
    /**
       update a pre-mapped term with a mapping (in angle brackets).
       Returns true if it mapped.
    */
    public static boolean map(Term t) {
        boolean rv = false;
        if (t==null)
            return rv;
        String name = t.getTermName();
        if (name.endsWith(">")) {
            int pos = name.lastIndexOf(" <");
            String ref = name.substring(pos+2,name.length()-1);
            name = name.substring(0,pos);
            t.setTermName(name);
            t.setOtermRef(ref);
            t.setOtermName(name);
            rv = true;
        }
        return rv;
    }

    /**
       update a pre-mapped value with a reference.  Returns true if it mapped.
    */
    public static boolean map(Value v) {
        boolean rv = false;
        if (v==null)
            return rv;
        String sv = v.getStringValue();
        if (sv.endsWith(">")) {
            int pos = sv.lastIndexOf(" <");
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
            rv = true;
        }
        return rv;
    }

    /**
       update pre-mapped values with a reference.  Returns true if it mapped.
    */
    public static boolean map(Values v) throws Exception {
        boolean rv = false;
        if (v==null)
            return rv;
        List<String> svs = v.getStringValues();
        if (svs.get(0).endsWith(">")) {
            int l = svs.size();
            List<String> refs = new ArrayList<String>(l);
            for (int i=0; i<l; i++) {
                String sv = svs.get(i);
                int pos = sv.lastIndexOf(" <");
                if (pos==-1)
                    throw new Exception("error mapping value '"+sv+"': either all or no values must be mapped in the file");
                String ref = sv.substring(pos+2,sv.length()-1);
                sv = sv.substring(0,pos);
                svs.set(i,sv);
                refs.add(ref);
            }
            if (refs.get(0).indexOf(":")>-1) {
                v.setOtermRefs(refs);
                v.setScalarType("oterm_ref");
            }
            else {
                v.setObjectRefs(refs);
                v.setScalarType("object_ref");
            }
            rv = true;
        }
        return rv;
    }
    
    /**
       Map premapped types in a TypedValue.  Returns true if any mapped.
    */
    public static boolean map(TypedValue tv) {
        boolean rv = false;
        if (tv==null)
            return rv;
        Term t = tv.getValueType();
        rv |= map(t);
        Value v = tv.getValue();
        rv |= map(v);
        rv |= map(tv.getValueUnits());
        if (t != null) {
            String ref = t.getOtermRef();
            if ((ref != null) &&
                (ref.indexOf(":") > -1))
                transformValue(ref, v);
        }
        return rv;
    }

    /**
       Map premapped types in a TypedValues.  Returns true if any mapped.
    */
    public static boolean map(TypedValues tvs) throws Exception {
        boolean rv = false;
        if (tvs==null)
            return rv;
        Term t = tvs.getValueType();
        rv |= map(t);
        List<TypedValue> vc = tvs.getValueContext();
        if (vc != null) {
            for (TypedValue tv : vc)
                rv |= map(tv);
        }
        Values v = tvs.getValues();
        rv |= map(v);
        rv |= map(tvs.getValueUnits());
        if (t != null) {
            String ref = t.getOtermRef();
            if ((ref != null) &&
                (ref.indexOf(":") > -1))
                transformValues(ref, v);
        }
        return rv;
    }

    /**
       Map premapped types in a DimensionContext.  Returns true if any mapped.
    */
    public static boolean map(DimensionContext dc) throws Exception {
        boolean rv = false;
        if (dc==null)
            return rv;
        rv |= map(dc.getDataType());
        for (TypedValues tvs : dc.getTypedValues())
            rv |= map(tvs);
        return rv;
    }
    
    /**
       Map premapped types in NDArray.  Returns true if any mapped.
    */
    public static boolean map(NDArray nda) throws Exception {
        boolean rv = false;
        rv |= map(nda.getDataType());
        for (DimensionContext dc : nda.getDimContext())
            rv |= map(dc);
        List<TypedValue> arrayContext = nda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= map(tv);
        }
        rv |= map(nda.getTypedValues());
        return rv;
    }

    /**
       Map premapped types in HNDArray.  Returns true if any mapped.
    */
    public static boolean map(HNDArray hnda) throws Exception {
        boolean rv = false;
        rv |= map(hnda.getDataType());
        for (DimensionContext dc : hnda.getDimContext())
            rv |= map(dc);
        List<TypedValue> arrayContext = hnda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= map(tv);
        }
        for (TypedValues tv : hnda.getTypedValues())
            rv |= map(tv);
        return rv;
    }

    /**
       Convert a HNDArray to a NDArray
    */
    public static NDArray makeNDArray(HNDArray hnda) throws Exception {
        int numHet = hnda.getTypedValues().size();
        if (numHet > 1)
            throw new Exception("Data are not in a homogeneous array; did you forget a 'values' line when importing?");
        NDArray rv = new NDArray()
            .withName(hnda.getName())
            .withDescription(hnda.getDescription())
            .withDataType(hnda.getDataType())
            .withArrayContext(hnda.getArrayContext())
            .withNDimensions(hnda.getNDimensions())
            .withDimContext(hnda.getDimContext())
            .withTypedValues(hnda.getTypedValues().get(0));
        return rv;
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
        HNDArray hnda = parseCSV(filePath);

        // map any pre-mapped values
        /*
        SdkOntologyClient oc = new SdkOntologyClient(new URL(System.getenv("SDK_CALLBACK_URL")),token);
        oc.setAuthAllowedForHttp(true);
        List<String> publicOntologies = oc.listPublicOntologies();
        System.out.println("Public ontologies:");
        if (publicOntologies==null)
            System.out.println("NONE");
        else
            for (String ontology : publicOntologies)
                System.out.println("  "+ontology);
        */
        boolean mapped = map(hnda);

        // save as other type if necessary
        String objectType = params.getObjectType();
        Object o = hnda;
        NDArray nda = null;
        if (objectType.equals("KBaseGenerics.NDArray")) {
            nda = makeNDArray(hnda);
            o = nda;
        }

        // make metadata
        Map<String,String> metadata = new HashMap<String,String>();
        if (mapped)
            metadata.put("mapped","true");
        else
            metadata.put("mapped","false");
        metadata.put("n_dimensions", hnda.getNDimensions().toString());
        String typeDescriptor = hnda.getDataType().getTermName();
        if (nda != null)
            typeDescriptor += ", "+nda.getTypedValues().getValueType().getTermName();
        typeDescriptor += " (";
        for (DimensionContext dc : hnda.getDimContext()) {
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
      
