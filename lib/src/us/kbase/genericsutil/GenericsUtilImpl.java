package us.kbase.genericsutil;

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

import kbasereport.*;
import sdkontologyjmc.*;
import us.kbase.kbasegenerics.*;
import us.kbase.kbaseontology.OntologyDictionary;

import org.strbio.IO;
import org.strbio.io.*;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

import com.opencsv.*;

import static java.lang.ProcessBuilder.Redirect;

/**
   This class implements methods (currently just importing)
   required to deal with KBase Generics
*/
public class GenericsUtilImpl {
    protected static java.io.File tempDir = new java.io.File("/kb/module/work/tmp/");

    protected static WorkspaceClient wc = null;

    protected static OntologyData od = null;

    /**
       class encapsulating all data we've received from the
       Ontology service
    */
    public static class OntologyData {
        /**
           ontology service client
        */
        public SdkOntologyJmcClient oc;

        /**
           map of ontology prefix to dictionary reference
        */
        public HashMap<String,String> prefixMap;

        /**
           map of ontology references to term names
        */
        public HashMap<String,String> refToTerm;

        /**
           map of ontology references to data types
        */
        public HashMap<String,String> refToDataType;

        /**
           map of ontology references to object refs
        */
        public HashMap<String,String> refToObjectType;
        
        /**
           map of ontology references to oterm refs
        */
        public HashMap<String,String> refToOtermType;

        /**
           map of ontology references to synonyms
        */
        public HashMap<String,List<String>> refToSynonyms;

        /**
           map of ontology references to (direct, is_a) parents
        */
        public HashMap<String,List<String>> refToParents;
        
        /**
           list of references to map
        */
        public HashSet<String> unmappedRefs;

        /**
           init service with public data and data in current workspace.
           If ws is null, uses public data only.
        */
        public OntologyData(AuthToken token, String ws) throws Exception {
            oc = new SdkOntologyJmcClient(new URL(System.getenv("SDK_CALLBACK_URL")),token);
            oc.setAuthAllowedForHttp(true);

            // start with public ontologies
            List<String> ontologies = oc.listPublicOntologies();
            /*
            System.out.println("Public ontologies:");
            if (ontologies==null)
                System.out.println("  NONE");
            else
                for (String ontology : ontologies)
                    System.out.println("  "+ontology);
            */

            // add private ontologies from user's workspace
            if (ws!=null) {
                List<String> privateOntologies = oc.listOntologies(new ListOntologiesParams().withWorkspaceNames(Arrays.asList(ws)));
                /*
                System.out.println("Private ontologies:");
                if (privateOntologies==null)
                    System.out.println("  NONE");
                else
                    for (String ontology : privateOntologies)
                        System.out.println("  "+ontology);
                */
                ontologies.addAll(privateOntologies);
            }

            // map prefix of each ontology to the object ref
            prefixMap = new HashMap<String,String>();
            OntologyOverviewOut ooo = oc.ontologyOverview(new OntologyOverviewParams().withOntologyDictionaryRef(ontologies));
            for (OverViewInfo oi : ooo.getDictionariesMeta()) {
                String prefix = oi.getOntology().toUpperCase();
                String ref = oi.getDictionaryRef();
                Long nTerms = oi.getNumberOfTerms();
                List<String> idRules = oi.getNamespaceIdRule();
                if (prefix != null) {
                    prefixMap.put(prefix,ref);
                    // System.out.println("mapped prefix "+prefix+" to "+ref+", "+nTerms+" terms");
                }
                if (idRules != null) {
                    for (String rule : idRules) {
                        int pos1 = rule.indexOf(" ");
                        int pos2 = rule.indexOf(":");
                        if (pos1 > pos2)
                            pos1 = -1;
                        if (pos2 > 0) {
                            prefix = rule.substring(pos1+1,pos2);
                            prefixMap.put(prefix,ref);
                            // System.out.println("mapped prefix "+prefix+" to "+ref+", "+nTerms+" terms");
                        }
                    }
                }
            }

            // initialize term maps
            refToTerm = new HashMap<String,String>();
            refToDataType = new HashMap<String,String>();
            refToObjectType = new HashMap<String,String>();
            refToOtermType = new HashMap<String,String>();
            refToParents = new HashMap<String,List<String>>();
            refToSynonyms = new HashMap<String,List<String>>();
            unmappedRefs = new HashSet<String>();
        }

        /**
           Map a list of references in a dictionary, to avoid expensive calls
           to map individual terms.
        */
        public void mapRefs(String prefix, List<String> refs) throws Exception {
            System.out.println("mapping refs for prefix "+prefix);
            String dictRef = prefixMap.get(prefix);
            if (dictRef==null)
                throw new IllegalArgumentException("No ontology dictionary available for prefix '"+prefix+"'");
            // System.err.println("debug1: "+dictRef);
            // System.err.println("debug2: "+refs.toString());
            GetOntologyTermsOut out = oc.getOntologyTerms(new GetOntologyTermsParams().withOntologyDictionaryRef(dictRef).withTermIds(refs));
            // System.err.println("debug3: "+out.toString());
            if (out==null)
                throw new IllegalArgumentException("Couldn't map ontologies (error 1) in dictionary "+dictRef);
            Map<String,TermInfo> termInfoMap = out.getTermInfo();
            if (termInfoMap==null)
                throw new IllegalArgumentException("Couldn't map ontologies (error 2) in dictionary "+dictRef);
            for (String ref : termInfoMap.keySet()) {
                TermInfo termInfo = termInfoMap.get(ref);
                if ((termInfo != null) && (termInfo.getName() != null)) {
                    String term = termInfo.getName();
                    refToTerm.put(ref,term);
                    List<String> synonyms = termInfo.getSynonym();
                    if (synonyms != null)
                        refToSynonyms.put(ref,synonyms);
                    List<String> parents = termInfo.getIsA();
                    if (parents != null) {
                        refToParents.put(ref,parents);
                        for (String parent : parents) {
                            int pos = parent.indexOf(" ");
                            if (pos==-1)
                                addRef(parent);
                            else
                                addRef(parent.substring(0,pos));
                        }
                    }
                    List<String> xrefs = termInfo.getXref();
                    if (xrefs != null) {
                        for (String xref : xrefs) {
                            if (xref.startsWith("Ref: "))
                                refToObjectType.put(ref,xref.substring(5));
                            else if (xref.startsWith("ORef: "))
                                refToOtermType.put(ref,xref.substring(6));
                        }
                    }
                    List<String> pvs = termInfo.getPropertyValue();
                    if (pvs != null) {
                        for (String pv : pvs) {
                            if (pv.startsWith("data_type "))
                                refToDataType.put(ref,pv.substring(10));
                        }
                    }
                }
            }
        }

        /**
           Map all unmapped references.  Throws exception if any
           can't be mapped.  Clears list of any mapped references.
        */
        public void mapRefs() throws Exception {
            System.out.println("mapping all unmapped references");
            HashSet<String> prefixes = new HashSet<String>();
            for (String ref : unmappedRefs) {
                int pos = ref.indexOf(":");
                if (pos==-1)
                    throw new IllegalArgumentException("Invalid format for reference '"+ref+"'; should be in format prefix:number");
                prefixes.add(ref.substring(0,pos));
            }
            for (String prefix : prefixes) {
                ArrayList<String> refs = new ArrayList<String>();
                for (String ref : unmappedRefs) {
                    if (ref.startsWith(prefix+":"))
                        refs.add(ref);
                }
                mapRefs(prefix,refs);
                unmappedRefs.removeAll(refs);
            }
            if (unmappedRefs.size() > 0)
                mapRefs();
        }

        /**
           Add a reference to the list of references to map,
           if not already mapped.
        */
        public void addRef(String ref) {
            if (refToTerm.get(ref)==null)
                unmappedRefs.add(ref);
        }

        /**
           Return a term for a reference.  Throws exception if unmappable.
        */
        public String getTerm(String ref) throws Exception {
            String rv = refToTerm.get(ref);
            if (rv==null)
                throw new IllegalArgumentException("Couldn't map ontology reference '"+ref+"'");
            return rv;
        }

        /**
           Return a data type for a reference, or null if not found
        */
        public String getDataType(String ref) {
            return refToDataType.get(ref);
        }

        /**
           Return a object ref type for a reference, or null if not found
        */
        public String getObjectType(String ref) {
            return refToObjectType.get(ref);
        }

        /**
           Return a oterm ref type for a reference, or null if not found
        */
        public String getOtermType(String ref) {
            return refToOtermType.get(ref);
        }
        
        /**
           Checks whether a term matches a reference, or its synonyms,
           or its parents.  Case insensitive.
        */
        public boolean matches(String ref, String term) throws Exception {
            term = term.toLowerCase();
            String match = refToTerm.get(ref);
            // System.out.println("debug5: "+ref+" "+term+" "+match);
            if (match.toLowerCase().equals(term))
                return true;
            List<String> synonyms = refToSynonyms.get(ref);
            if (synonyms != null) {
                for (String synonym : synonyms) {
                    int pos = synonym.indexOf(" EXACT");
                    if (pos > 0)
                        synonym = synonym.substring(0,pos);
                    synonym = unquote(synonym);
                    // System.out.println("debug4: "+term+" "+synonym);
                    if (synonym.toLowerCase().equals(term))
                        return true;
                }
            }
            List<String> parents = refToParents.get(ref);
            if (parents != null) {
                for (String parent : parents) {
                    int pos = parent.indexOf(" ");
                    if (pos > 0)
                        parent = parent.substring(0,pos);
                    if (matches(parent, term))
                        return true;
                }
            }
            return false;
        }
    }

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
       Helper function to get reference when listing/saving an object
    */
    private static String getRefFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
        return info.getE7() + "/" + info.getE1() + "/" + info.getE5();
    }

    /**
       Helper function to get type when listing/saving an object
    */
    private static String getTypeFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
        return info.getE3();
    }

    /**
       Helper function to get name when listing/saving an object
    */
    private static String getNameFromObjectInfo(Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info) {
        return info.getE2();
    }
    
    /**
       Helper function to get refs when listing objects
    */
    public static ArrayList<String> getRefsFromObjectInfo(List<Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>>> info) {
        if (info==null)
            return null;
        
        ArrayList<String> rv = new ArrayList<String>();
        for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> tuple : info) {
            rv.add(getRefFromObjectInfo(tuple));
        }
        return rv;
    }

    /**
       Helper function to get types when listing objects
    */
    public static ArrayList<String> getTypesFromObjectInfo(List<Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>>> info) {
        if (info==null)
            return null;
        
        ArrayList<String> rv = new ArrayList<String>();
        for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> tuple : info) {
            rv.add(getTypeFromObjectInfo(tuple));
        }
        return rv;
    }
    
    /**
       Save NDArray object to workspace, returning reference.
    */
    public static String saveObject(String ws,
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
                                         String shockURL,
                                         AuthToken token,
                                         java.io.File f,
                                         boolean gzip) throws Exception {

        // System.err.println("shock cmd equivalent to "+"/usr/bin/curl -k -X GET "+shockURL+" -H \"Authorization: OAuth "+token.toString()+"\""+(gzip ? "| /bin/gzip" : ""));
        
        BasicShockClient shockClient = new BasicShockClient(new URL(shockURL), token);
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
       store a file in Shock; returns ID
       If file doesn't exist or can't be read, returns null.
    */
    public static String toShock(String shockURL,
                                 AuthToken token,
                                 java.io.File f) throws Exception {
        if (!f.canRead())
            return null;

        BasicShockClient shockClient = new BasicShockClient(new URL(shockURL), token);
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        ShockNode sn = shockClient.addNode(is,f.getName(),null);
        is.close();
        String shockNodeID = sn.getId().getId();
        return shockNodeID;
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
                // make user specify details for now;
                // units and context should be auto-detected!
                List<TypedValue> vc = new ArrayList<TypedValue>();
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
                                   String prefix,
                                   String suffix,
                                   String shockURL,
                                   AuthToken token) throws Exception {
        String rv = f.getPath();
        if (rv==null) {
            System.out.println("Getting file from Shock");
            java.io.File tmpFile = java.io.File.createTempFile(prefix, suffix, tempDir);
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
                // see note above about converting to the correct
                // type at mapping time
                curValues.getStringValues().set((int)index,unquote(val));
            }
        }
        infile.close();

        return hnda;
    }

    /**
       make a Term into a String
    */
    public static String toString(Term t, boolean includeRef) {
        String rv = t.getTermName();
        if (includeRef && (t.getOtermRef() != null))
            rv += " <"+t.getOtermRef()+">";
        return rv;
    }

    /**
       make a Value into a String
    */
    public static String toString(Value v, boolean includeRef) {
        String scalarType = v.getScalarType();
        if (scalarType.equals("int"))
            return new Long(v.getIntValue()).toString();
        else if (scalarType.equals("float"))
            return new Double(v.getFloatValue()).toString();
        else if (scalarType.equals("boolean"))
            return new Long(v.getBooleanValue()).toString();

        String rv = v.getStringValue();
        if (includeRef) {
            if (scalarType.equals("object_ref"))
                rv += " <"+v.getObjectRef()+">";
            else if (scalarType.equals("oterm_ref"))
                rv += " <"+v.getOtermRef()+">";
        }
        return rv;
    }

    /**
       write Values to a CVSWriter.  prefix indicates the non-heterogeneous
       dimension index, which will be prepended to each line.  If null,
       nothing will be prepended.
    */
    public static void writeValues(Long prefix, List<Long> dLengths, Values v, boolean includeRefs, CSVWriter outfile) {
        int nDimensions = dLengths.size();
        int length = 1;
        ArrayList<String> line = new ArrayList<String>();
        Long[] indices = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++) {
            indices[i] = new Long(1L);
            length *= (int)(dLengths.get(i).longValue());
        }
        String scalarType = v.getScalarType();

        // get correct set(s) of values
        Object[] objects = null;
        Object[] refs = null;
        if (scalarType.equals("int"))
            objects = v.getIntValues().toArray();
        else if (scalarType.equals("float"))
            objects = v.getFloatValues().toArray();
        else if (scalarType.equals("boolean"))
            objects = v.getBooleanValues().toArray();
        else {
            objects = v.getStringValues().toArray();
            if (scalarType.equals("object_ref"))
                refs = v.getObjectRefs().toArray();
            else if (scalarType.equals("oterm_ref"))
                refs = v.getOtermRefs().toArray();
        }

        // print out the values
        for (int i=0; i<length; i++) {
            if (objects[i] != null) {
                if (prefix != null)
                    line.add(prefix.toString());
                for (int j=0; j<nDimensions; j++)
                    line.add(indices[j].toString());
                String s = objects[i].toString();
                if (includeRefs &&
                    (refs != null) &&
                    (refs[i] != null))
                    s += " <"+refs[i].toString()+">";
                line.add(s);
                outfile.writeNext(line.toArray(new String[line.size()]),false);
                line.clear();
            }
            // increment all indices
            if (i<length-1) {
                indices[nDimensions-1]++;
                for (int j=nDimensions-1; j>=0; j--) {
                    if (indices[j] > dLengths.get(j)) {
                        indices[j] = new Long(1L);
                        indices[j-1]++;
                    }
                }
            }
        }
    }

    /**
       make a TypedValue (including the value) into
       an ArrayList of Strings
    */
    public static ArrayList<String> toStrings(TypedValue tv, boolean includeRefs) {
        ArrayList<String> rv = new ArrayList<String>();
        Term t = tv.getValueType();
        rv.add(toString(t,includeRefs));
        Value v = tv.getValue();
        rv.add(toString(v,includeRefs));
        t = tv.getValueUnits();
        if (t != null)
            rv.add(toString(t,includeRefs));
        return rv;
    }

    /**
       make a TypedValue (including the value) into
       a single descriptive string
    */
    public static String toString(TypedValue tv, boolean includeRefs) {
        Term t = tv.getValueType();
        String rv = toString(t,includeRefs);
        Value v = tv.getValue();
        rv += " = "+toString(v,includeRefs);
        t = tv.getValueUnits();
        if (t != null)
            rv += " ("+toString(t,includeRefs)+")";
        return rv;
    }
    
    /**
       make TypedValues metadata (but not the values) into
       an ArrayList of Strings
    */
    public static ArrayList<String> toStrings(TypedValues tvs, boolean includeRefs) {
        ArrayList<String> rv = new ArrayList<String>();
        rv.add(toString(tvs.getValueType(),includeRefs));
        if (tvs.getValueContext() != null)
            for (TypedValue tv : tvs.getValueContext())
                rv.addAll(toStrings(tv,includeRefs));
        Term t = tvs.getValueUnits();
        if (t != null)
            rv.add(toString(t,includeRefs));
        return rv;
    }

    /**
       make TypedValues metadata (but not the values) into
       a descriptive String
    */
    public static String toString(TypedValues tvs, boolean includeRefs) {
        String rv = toString(tvs.getValueType(),includeRefs);
        if (tvs.getValueContext() != null)
            for (TypedValue tv : tvs.getValueContext())
                rv += "; "+toString(tv,includeRefs);
        Term t = tvs.getValueUnits();
        if (t != null)
            rv += " ("+toString(t,includeRefs)+")";
        return rv;
    }
    
    /**
       Writes a HNDArray object to a CSV file.
    */
    public static void writeCSV(HNDArray hnda, boolean includeRefs, CSVWriter outfile) throws Exception {
        // check whether HNDArray is really heterogenous
        int numHet = hnda.getTypedValues().size();
        boolean isHeterogeneous = (numHet > 1);

        // write out common headers
        ArrayList<String> line = new ArrayList<String>();
        if (hnda.getName() != null) {
            line.add("name");
            line.add(hnda.getName());
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }
        if (hnda.getDescription() != null) {
            line.add("description");
            line.add(hnda.getDescription());
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }
        if (hnda.getDataType() != null) {
            line.add("type");
            line.add(toString(hnda.getDataType(),includeRefs));
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }
        if (!isHeterogeneous) {
            line.add("values");
            line.addAll(toStrings(hnda.getTypedValues().get(0),includeRefs));
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }

        // write array metadata
        if (hnda.getArrayContext() != null) {
            for (TypedValue tv : hnda.getArrayContext()) {
                line.add("meta");
                line.addAll(toStrings(tv,includeRefs));
                outfile.writeNext(line.toArray(new String[line.size()]),false);
                line.clear();
            }
        }

        // write array dimensions
        line.add("size");
        ArrayList<Long> dLengths = new ArrayList<Long>();
        for (DimensionContext dc : hnda.getDimContext()) {
            dLengths.add(dc.getSize());
            line.add(dc.getSize().toString());
        }
        outfile.writeNext(line.toArray(new String[line.size()]),false);
        line.clear();

        // write metadata for each dimension
        Integer i = 1;
        for (DimensionContext dc : hnda.getDimContext()) {
            for (TypedValues tvs : dc.getTypedValues()) {
                line.add("dmeta");
                line.add(i.toString());
                line.add(toString(dc.getDataType(),includeRefs));
                line.addAll(toStrings(tvs,includeRefs));
                outfile.writeNext(line.toArray(new String[line.size()]),false);
                line.clear();
                writeValues(null, Arrays.asList(dc.getSize()), tvs.getValues(), includeRefs, outfile);
            }
            i++;
        }

        line.add("data");
        outfile.writeNext(line.toArray(new String[line.size()]),false);
        line.clear();

        // write out the actual data
        Long prefix = null;
        if (isHeterogeneous) {
            prefix = dLengths.get(0);
            dLengths.remove(0);
        }
        for (TypedValues tvs : hnda.getTypedValues())
            writeValues(prefix, dLengths, tvs.getValues(), includeRefs, outfile);
        
        outfile.flush();
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
       update a Values object from anything else to String
    */
    public static void makeStringValues(Values v) {
        String scalarType = v.getScalarType();
        if (scalarType.equals("int")) {
            List<Long> iv = v.getIntValues();
            int l = iv.size();
            List<String> sv = new ArrayList<String>(l);
            for (int i=0; i<l; i++) {
                Long val = iv.get(i);
                if (val == null)
                    sv.add(null);
                else
                    sv.add(val.toString());
            }
            v.setIntValues(null);
            v.setStringValues(sv);
        }
        else if (scalarType.equals("float")) {
            List<Double> fv = v.getFloatValues();
            int l = fv.size();
            List<String> sv = new ArrayList<String>(l);
            for (int i=0; i<l; i++) {
                Double val = fv.get(i);
                if (val == null)
                    sv.add(null);
                else
                    sv.add(val.toString());
            }
            v.setFloatValues(null);
            v.setStringValues(sv);
        }
        else if (scalarType.equals("boolean")) {
            List<Long> bv = v.getBooleanValues();
            int l = bv.size();
            List<String> sv = new ArrayList<String>(l);
            for (int i=0; i<l; i++) {
                Long val = bv.get(i);
                if (val == null)
                    sv.add(null);
                else
                    sv.add(val.toString());
            }
            v.setBooleanValues(null);
            v.setStringValues(sv);
        }
        // refs and orefs are already stored in strings,
        // so no need to change those
        v.setScalarType("string");
    }

    /**
       Gets list of relevant objects from Values
    */
    public static List getObjects(Values v) {
        String scalarType = v.getScalarType();
        List rv = null;
        if (scalarType.equals("int"))
            rv = v.getIntValues();
        else if (scalarType.equals("float"))
            rv = v.getFloatValues();
        else if (scalarType.equals("boolean"))
            rv = v.getBooleanValues();
        else if (scalarType.equals("string"))
            rv = v.getStringValues();
        else if (scalarType.equals("oterm_ref"))
            rv = v.getOtermRefs();
        else if (scalarType.equals("object_ref"))
            rv = v.getObjectRefs();
        return rv;
    }

    /**
       return a Values object that only includes unique
       values from another object
    */
    public static Values findUniqueValues(Values v) {
        String scalarType = v.getScalarType();
        Values rv = new Values().withScalarType(scalarType);
        List oldV = getObjects(v);
        List newV = null;

        if (!scalarType.equals("string")) {
            // converting to LinkedHashSet preserves order
            // while keeping only unique values
            LinkedHashSet lhs = new LinkedHashSet();
            lhs.addAll(oldV);
            newV = new ArrayList(lhs);
        }

        if (scalarType.equals("int"))
            rv.setIntValues(newV);
        else if (scalarType.equals("float"))
            rv.setFloatValues(newV);
        else if (scalarType.equals("boolean"))
            rv.setBooleanValues(newV);
        else if (scalarType.equals("oterm_ref"))
            rv.setOtermRefs(newV);
        else if (scalarType.equals("object_ref"))
            rv.setObjectRefs(newV);

        // ref types also store data in the string array
        if ((scalarType.equals("string")) ||
            (scalarType.equals("oterm_ref")) ||
            (scalarType.equals("object_ref"))) {
            oldV = v.getStringValues();
            LinkedHashSet lhs = new LinkedHashSet();
            lhs.addAll(oldV);
            newV = new ArrayList(lhs);
            rv.setStringValues(newV);
        }
        return rv;
    }

    /**
       return a Values object with one or more dimensions
       fixed.  Non-fixed indices should be left null, and fixed
       indices should be set to the 1-based index to fix.
    */
    public static Values fixValues(Values v,
                                   List<Long> dLengths,
                                   List<Long> fixedIndices) {
        String scalarType = v.getScalarType();
        Values rv = new Values().withScalarType(scalarType);
        List oldV = null;
        List oldRefs = null;
        if (scalarType.equals("int"))
            oldV = v.getIntValues();
        else if (scalarType.equals("float"))
            oldV = v.getFloatValues();
        else if (scalarType.equals("boolean"))
            oldV = v.getBooleanValues();
        else if (scalarType.equals("string"))
            oldV = v.getStringValues();
        else if (scalarType.equals("oterm_ref")) {
            oldV = v.getStringValues();
            oldRefs = v.getOtermRefs();
        }
        else if (scalarType.equals("object_ref")) {
            oldV = v.getStringValues();
            oldRefs = v.getObjectRefs();
        }

        // loop over array to get only the right data
        // this is inefficient and could be made faster if bottleneck!
        List newV = new ArrayList();
        List newRefs = new ArrayList();
        int nDimensions = dLengths.size();
        int length = 1;
        ArrayList<String> line = new ArrayList<String>();
        Long[] indices = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++) {
            indices[i] = new Long(1L);
            length *= (int)(dLengths.get(i).longValue());
        }
        for (int i=0; i<length; i++) {
            boolean keep = true;
            for (int j=0; j<nDimensions; j++)
                if ((fixedIndices.get(j) != null) &&
                    (!fixedIndices.get(j).equals(indices[j]))) {
                    keep = false;
                    j = nDimensions;
                }
            if (keep) {
                newV.add(oldV.get(i));
                if (oldRefs != null)
                    newRefs.add(oldRefs.get(i));
            }
            // increment all indices
            if (i<length-1) {
                indices[nDimensions-1]++;
                for (int j=nDimensions-1; j>=0; j--) {
                    if (indices[j] > dLengths.get(j)) {
                        indices[j] = new Long(1L);
                        indices[j-1]++;
                    }
                }
            }
        }

        // save in new object
        if (scalarType.equals("int"))
            rv.setIntValues(newV);
        else if (scalarType.equals("float"))
            rv.setFloatValues(newV);
        else if (scalarType.equals("boolean"))
            rv.setBooleanValues(newV);
        else if (scalarType.equals("string"))
            rv.setStringValues(newV);
        else if (scalarType.equals("oterm_ref")) {
            rv.setStringValues(newV);
            rv.setOtermRefs(newRefs);
        }
        else if (scalarType.equals("object_ref")) {
            rv.setStringValues(newV);
            rv.setObjectRefs(newRefs);
        }

        return rv;
    }

    /**
       Find the index in a dimension that matches a combination
       of unique value indices.  All values are 1-based.
    */
    public static Long mergeUniqueIndices(DimensionContext dc,
                                          List<Long> valueIndices) throws Exception {
        // kepp list of possible answers
        HashSet<Long> remainingIndices = new HashSet<Long>();
        int dLength = (int)(dc.getSize().longValue());
        for (int i=0; i<dLength; i++)
            remainingIndices.add(new Long(i+1));
        
        List<TypedValues> tvs = dc.getTypedValues();
        int nIndices = tvs.size();
        if (nIndices != valueIndices.size())
            throw new Exception("to find unique index for dimension, must fix all value indices");
        for (int i=0; i<nIndices; i++) {
            Values v = tvs.get(i).getValues();
            Values uv = findUniqueValues(v);
            String scalarType = v.getScalarType();

            List objects = getObjects(v);
            List uniqueObjects = getObjects(uv);
            
            for (int j=0; j<dLength; j++) {
                if (!remainingIndices.contains(new Long(j+1)))
                    continue;
                    
                Object o = objects.get(j);
                Object uo = uniqueObjects.get((int)(valueIndices.get(i).longValue())-1);
                if (o==null) {
                    if (uo != null)
                        remainingIndices.remove(new Long(j+1));
                    continue;
                }

                if (!o.equals(uo))
                    remainingIndices.remove(new Long(j+1));
            }
        }

        // make sure there is ony one answer
        if (remainingIndices.size() != 1)
            throw new Exception("Error - unique indices not found for dimension");
        List<Long> list = new ArrayList<Long>(remainingIndices);
        return list.get(0);
    }

    /**
       checks whether a dimension has unique subindices (required
       for mergeUniqueIndices to work).
    */
    public static boolean hasUniqueSubindices(DimensionContext dc) {
        int dLength = (int)(dc.getSize().longValue());
        List<TypedValues> tvs = dc.getTypedValues();
        int nIndices = tvs.size();

        // keep track of objects, and of unique objects,
        // in each subindex.
        List<List> objects = new ArrayList<List>();
        List<List> uniqueObjects = new ArrayList<List>();

        for (int i=0; i<nIndices; i++) {
            Values v = tvs.get(i).getValues();
            Values uv = findUniqueValues(v);
            String scalarType = v.getScalarType();
            objects.add(getObjects(v));
            uniqueObjects.add(getObjects(uv));
        }

        // check that each index can be described by a
        // unique combination of subindices
        HashSet<String> usedIndices = new HashSet<String>();
        for (int i=0; i<dLength; i++) {
            String subindexCombo = "";
            for (int j=0; j<nIndices; j++) {
                Object o = objects.get(j).get(i);
                int k = uniqueObjects.get(j).indexOf(o);
                subindexCombo += "_"+k;
            }
            if (usedIndices.contains(subindexCombo))
                return false;
            usedIndices.add(subindexCombo);
        }
        return true;
    }
    
    /**
       check object references to be sure they're real and readable
    */
    public static void checkObjects(List<String> objectRefs, String objectType) throws Exception {
        // parse out object type
        int pos1 = objectType.indexOf(".");
        if (pos1 == -1)
            throw new IllegalArgumentException("Object type "+objectType+" not parseable");
        int pos2 = objectType.indexOf(".", pos1+1);
        if (pos2 > pos1)
            return;  // should we check subobject?
        List<ObjectSpecification> objects = new ArrayList<ObjectSpecification>();
        for (String objectRef : objectRefs) {
            if (objectRef != null)
                objects.add(new ObjectSpecification().withRef(objectRef));
        }
        GetObjectInfo3Results goir = wc.getObjectInfo3(new GetObjectInfo3Params().withObjects(objects));
        for (String oType : getTypesFromObjectInfo(goir.getInfos())) {
            if (!oType.startsWith(objectType))
                throw new Exception("Object type "+oType+" not compatible with type "+objectType);
        }
    }

    /**
       update a Values object from String to ObjectRef, check refs
    */
    public static void makeObjectRefValues(Values v, String objectType) throws Exception {
        if (v.getScalarType().equals("object_ref"))
            return; // already pre-mapped

        List<String> sv = v.getStringValues();
        int l = sv.size();
        List<String> ov = new ArrayList<String>(l);
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val == null)
                ov.add(null);
            else
                ov.add(val);
        }
        checkObjects(ov, objectType);
        v.setStringValues(null);
        v.setObjectRefs(ov);
        v.setScalarType("object_ref");
    }

    /**
       update a Values object from String to OtermRef, add refs to
       be checked later
    */
    public static void makeOtermRefValues(Values v) throws Exception {
        if (v.getScalarType().equals("oterm_ref"))
            return; // already pre-mapped

        List<String> sv = v.getStringValues();
        int l = sv.size();
        List<String> ov = new ArrayList<String>(l);
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val == null)
                ov.add(null);
            else {
                ov.add(val);
                od.addRef(val); // put ref on list to check
            }
        }
        v.setStringValues(null);
        v.setOtermRefs(ov);
        v.setScalarType("oterm_ref");
    }
    
    /**
       update a Values object from String to another type, based
       on a reference term
    */
    public static void transformValues(Term valueType, Values v) throws Exception {
        List<String> sv = v.getStringValues();
        boolean allNumeric = true;
        boolean allBoolean = true;
        boolean allInt = true;
        boolean allOtermRef = true;
        boolean allObjectRef = true;
        int l = sv.size();
        for (int i=0; i<l; i++) {
            String val = sv.get(i);
            if (val != null) {
                allNumeric &= Pattern.matches("^-?[0-9]*\\.?[0-9]+$",val);
                allInt &= Pattern.matches("^-?[0-9]+$",val);
                allBoolean &= Pattern.matches("^[01]$",val);
                allOtermRef &= Pattern.matches("^.+:[0-9]+$",val);
                allObjectRef &= Pattern.matches("^[0-9]+/[0-9]+",val);
            }
        }
        String dataType = null;
        String ref = valueType.getOtermRef();
        String objectType = null;
        if (ref != null) {
            dataType = od.getDataType(ref);
            objectType = od.getObjectType(ref);
            if ((objectType != null) || (od.getOtermType(ref) != null))
                dataType = "ref";
        }
        if (dataType==null) {
            // guess based on values
            if (allBoolean)
                makeBooleanValues(v);
            else if (allNumeric)
                makeFloatValues(v);
        }
        else {
            if (dataType.equals("boolean")) {
                if (allBoolean)
                    makeBooleanValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be boolean");
            }
            else if (dataType.equals("int")) {
                if (allInt)
                    makeIntValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be integers");
            }
            else if (dataType.equals("float")) {
                if (allNumeric)
                    makeFloatValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be numeric");
            }
            else if ((dataType.equals("ref")) &&
                     (v.getScalarType().equals("string"))) {
                if (allOtermRef)
                    makeOtermRefValues(v);
                else if ((allObjectRef) && (objectType != null))
                    makeObjectRefValues(v,objectType);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be references");
            }
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
       update a Value object from String to ObjectRef, check refs
    */
    public static void makeObjectRefValue(Value v, String objectType) throws Exception {
        if (v.getScalarType().equals("object_ref"))
            return; // already pre-mapped

        String sv = v.getStringValue();
        if (sv == null)
            v.setObjectRef(null);
        else
            v.setObjectRef(sv);
        checkObjects(Arrays.asList(sv), objectType);
        v.setStringValue(null);
        v.setScalarType("object_ref");
    }

    /**
       update a Value object from String to OtermRef, add ref to
       be checked later
    */
    public static void makeOtermRefValue(Value v) throws Exception {
        if (v.getScalarType().equals("oterm_ref"))
            return; // already pre-mapped
        
        String sv = v.getStringValue();
        if (sv == null)
            v.setOtermRef(null);
        else {
            v.setOtermRef(sv);
            od.addRef(sv); // put ref on list to check
        }
        v.setStringValue(null);
        v.setScalarType("oterm_ref");
    }
    
    /**
       update a Value object from String to another type, based
       on a reference term
    */
    public static void transformValue(Term valueType, Value v) throws Exception {
        String sv = v.getStringValue();
        boolean isNumeric = Pattern.matches("^-?[0-9]*\\.?[0-9]+$",sv);
        boolean isBoolean = Pattern.matches("^[01]$",sv);
        boolean isInt = Pattern.matches("^-?[0-9]+$",sv);
        boolean isOtermRef = Pattern.matches("^.+:[0-9]+$",sv);
        boolean isObjectRef = Pattern.matches("^[0-9]+/[0-9]+",sv);

        String dataType = null;
        String ref = valueType.getOtermRef();
        String objectType = null;
        if (ref != null) {
            dataType = od.getDataType(ref);
            objectType = od.getObjectType(ref);
            if ((objectType != null) || (od.getOtermType(ref) != null))
                dataType = "ref";
        }
        if (dataType==null) {
            // guess based on values
            if (isBoolean)
                makeBooleanValue(v);
            else if (isNumeric)
                makeFloatValue(v);
        }
        else {
            if (dataType.equals("boolean")) {
                if (isBoolean)
                    makeBooleanValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be boolean");
            }
            else if (dataType.equals("int")) {
                if (isInt)
                    makeIntValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be integers");
            }
            else if (dataType.equals("float")) {
                if (isNumeric)
                    makeFloatValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be numeric");
            }
            else if ((dataType.equals("ref")) &&
                     (v.getScalarType().equals("string"))) {
                if (isOtermRef)
                    makeOtermRefValue(v);
                else if ((isObjectRef) && (objectType != null))
                    makeObjectRefValue(v,objectType);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be references");
            }
        }
    }
    
    /**
       update a pre-mapped term with a mapping (in angle brackets).
       Returns true if it mapped.

       In step 1, just gathers any terms to be mapped.
       In step 2, maps them.
    */
    public static boolean map(Term t, int step) throws Exception {
        boolean rv = false;
        if (t==null)
            return rv;
        String name = t.getTermName();
        if (name.endsWith(">")) {
            int pos = name.lastIndexOf(" <");
            String ref = name.substring(pos+2,name.length()-1);
            if (step==1) {
                od.addRef(ref);
            }
            else {
                name = name.substring(0,pos);
                t.setTermName(name);
                t.setOtermRef(ref);
                String dictName = od.getTerm(ref);
                if (!od.matches(ref,name))
                    System.out.println("mapping "+name+" to dictionary term "+dictName);
                t.setOtermName(dictName);
            }
            rv = true;
        }
        return rv;
    }

    /**
       update a pre-mapped value with a reference.  Returns true if it mapped.
       In step 1, just gathers any terms to be mapped.
       In step 2, maps them.
    */
    public static boolean map(Value v, int step) throws Exception {
        boolean rv = false;
        if (v==null)
            return rv;
        String sv = v.getStringValue();
        if (sv.endsWith(">")) {
            int pos = sv.lastIndexOf(" <");
            String ref = sv.substring(pos+2,sv.length()-1);
            if (step==1) {
                if (ref.indexOf(":")>-1)
                    od.addRef(ref);
            }
            else {
                sv = sv.substring(0,pos);
                v.setStringValue(sv);
                if (ref.indexOf(":")>-1) {
                    v.setOtermRef(ref);
                    v.setScalarType("oterm_ref");
                    String dictName = od.getTerm(ref);
                    if (!sv.toLowerCase().equals(dictName.toLowerCase()))
                        System.out.println("mapping "+sv+" to "+dictName);
                }
                else {
                    v.setObjectRef(ref);
                    v.setScalarType("object_ref");
                }
            }
            rv = true;
        }
        return rv;
    }

    /**
       update pre-mapped values with a reference.  Returns true if it mapped.
    */
    public static boolean map(Values v, int step) throws Exception {
        boolean rv = false;
        if (v==null)
            return rv;
        List<String> svs = v.getStringValues();
        int l = svs.size();
        List<String> refs = new ArrayList<String>(l);
        boolean oTerm = false;
        for (int i=0; i<l; i++) {
            String sv = svs.get(i);
            int pos;
            if ((sv != null) &&
                (sv.endsWith(">")) &&
                ((pos = sv.lastIndexOf(" <")) > -1)) {

                String ref = sv.substring(pos+2,sv.length()-1);
                rv = true;
                if (ref.indexOf(":")>-1)
                    oTerm = true;
                if (step==1) {
                    if (oTerm)
                        od.addRef(ref);
                }
                else {
                    sv = sv.substring(0,pos);
                    svs.set(i,sv);
                    refs.add(ref);
                    if (oTerm) {
                        String dictName = od.getTerm(ref);
                        if (!sv.toLowerCase().equals(dictName.toLowerCase()))
                            System.out.println("mapping "+sv+" to "+dictName);
                    }
                }
            }
            else if (step > 1)
                refs.add(null);
        }
        if ((step > 1) && (rv)) {
            if (oTerm) {
                v.setOtermRefs(refs);
                v.setScalarType("oterm_ref");
            }
            else {
                v.setObjectRefs(refs);
                v.setScalarType("object_ref");
            }
        }
        return rv;
    }
    
    /**
       Map premapped types in a TypedValue.  Returns true if any mapped.
    */
    public static boolean map(TypedValue tv, int step) throws Exception {
        boolean rv = false;
        if (tv==null)
            return rv;
        Term t = tv.getValueType();
        rv |= map(t,step);
        Value v = tv.getValue();
        rv |= map(v,step);
        rv |= map(tv.getValueUnits(),step);
        if ((t != null) && (step > 1))
            transformValue(t, v);
        return rv;
    }

    /**
       Map premapped types in a TypedValues.  Returns true if any mapped.
    */
    public static boolean map(TypedValues tvs, int step) throws Exception {
        boolean rv = false;
        if (tvs==null)
            return rv;
        Term t = tvs.getValueType();
        rv |= map(t,step);
        List<TypedValue> vc = tvs.getValueContext();
        if (vc != null) {
            for (TypedValue tv : vc)
                rv |= map(tv,step);
        }
        Values v = tvs.getValues();
        rv |= map(v,step);
        rv |= map(tvs.getValueUnits(),step);
        if ((t != null) && (step > 1))
            transformValues(t, v);
        return rv;
    }

    /**
       Map premapped types in a DimensionContext.  Returns true if any mapped.
    */
    public static boolean map(DimensionContext dc, int step) throws Exception {
        boolean rv = false;
        if (dc==null)
            return rv;
        rv |= map(dc.getDataType(),step);
        for (TypedValues tvs : dc.getTypedValues())
            rv |= map(tvs,step);
        return rv;
    }
    
    /**
       Map premapped types in NDArray.  Returns true if any mapped.
    */
    public static boolean map(NDArray nda, int step) throws Exception {
        boolean rv = false;
        rv |= map(nda.getDataType(),step);
        for (DimensionContext dc : nda.getDimContext())
            rv |= map(dc,step);
        List<TypedValue> arrayContext = nda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= map(tv,step);
        }
        rv |= map(nda.getTypedValues(),step);
        return rv;
    }

    /**
       Map premapped types in HNDArray.  Returns true if any mapped.
    */
    public static boolean map(HNDArray hnda, int step) throws Exception {
        boolean rv = false;
        rv |= map(hnda.getDataType(),step);
        for (DimensionContext dc : hnda.getDimContext())
            rv |= map(dc,step);
        List<TypedValue> arrayContext = hnda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= map(tv,step);
        }
        for (TypedValues tv : hnda.getTypedValues())
            rv |= map(tv,step);
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
       Convert a NDArray to a HNDArray
    */
    public static HNDArray makeHNDArray(NDArray nda) throws Exception {
        HNDArray rv = new HNDArray()
            .withName(nda.getName())
            .withDescription(nda.getDescription())
            .withDataType(nda.getDataType())
            .withArrayContext(nda.getArrayContext())
            .withNDimensions(nda.getNDimensions())
            .withDimContext(nda.getDimContext())
            .withTypedValues(Arrays.asList(nda.getTypedValues()));
        return rv;
    }
    
    /**
       Imports a generic object from CSV file.
    */
    public static ImportResult importCSV(String wsURL,
                                         String shockURL,
                                         AuthToken token,
                                         ImportCSVParams params) throws Exception {
        wc = createWsClient(wsURL,token);
        od = new OntologyData(token, params.getWorkspaceName());

        // for provenance
        String methodName = "GenericsUtil.importCSV";
        List<UObject> methodParams = Arrays.asList(new UObject(params));

        // looks for local file; if not given, get from shock
        String filePath = setupFile(params.getFile(),
                                    "mat",
                                    ".csv",
                                    shockURL,
                                    token);

        // parse it into the object
        HNDArray hnda = parseCSV(filePath);

        // map any pre-mapped values
        boolean mapped = map(hnda,1);
        if (mapped) {
            od.mapRefs();
            mapped = map(hnda,2);
            od.mapRefs();
        }

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
        String scalarType = null;
        String valuesType = null;
        for (TypedValues tv : hnda.getTypedValues()) {
            if (scalarType == null) {
                scalarType = "";
                valuesType = "";
            }
            else {
                scalarType += ", ";
                valuesType += ", ";
            }
            scalarType += tv.getValues().getScalarType();
            valuesType += toString(tv,false);
        }
        metadata.put("scalar_type",scalarType);
        metadata.put("value_type",valuesType);
        
        typeDescriptor += " < ";
        String dimensionSize = "< ";
        for (DimensionContext dc : hnda.getDimContext()) {
            if (!typeDescriptor.endsWith(" < ")) {
                typeDescriptor += ", ";
                dimensionSize += ", ";
            }
            typeDescriptor += dc.getDataType().getTermName();
            dimensionSize += dc.getSize();
        }
        typeDescriptor += " >";
        dimensionSize += " >";
        metadata.put("data_type",typeDescriptor);
        metadata.put("dimension_size",dimensionSize);

        // save in workspace
        String objectRef = saveObject(params.getWorkspaceName(),
                                      params.getObjectName(),
                                      objectType,
                                      o,
                                      metadata,
                                      makeProvenance(objectType+" imported from CSV file",
                                                     methodName,
                                                     methodParams));

        ImportResult rv = new ImportResult()
            .withObjectRef(objectRef);

        // clean up tmp file if we used one
        if (params.getFile().getPath()==null) {
            java.io.File f = new java.io.File(filePath);
            f.delete();
        }

        return rv;
    }

    /**
       Imports an OntologyDictionary from an OBO file
    */
    public static ImportResult importOBO(String wsURL,
                                         String shockURL,
                                         AuthToken token,
                                         ImportOBOParams params) throws Exception {
        wc = createWsClient(wsURL,token);

        // for provenance
        String methodName = "GenericsUtil.importOBO";
        List<UObject> methodParams = Arrays.asList(new UObject(params));

        // looks for local file; if not given, get from shock
        String filePath = setupFile(params.getFile(),
                                    "dic",
                                    ".obo",
                                    shockURL,
                                    token);

        // turn it into a JSON file
        java.io.File tmpFile = java.io.File.createTempFile("dic", ".json", tempDir);
        ProcessBuilder pb = new ProcessBuilder("/kb/module/dependencies/bin/ont.pl",
                                               "--from-obo",
                                               filePath);
        pb.redirectOutput(Redirect.to(tmpFile));
        pb.start().waitFor();

        // load the JSON file
        ObjectMapper mapper = new ObjectMapper();
        OntologyDictionary o = mapper.readValue(tmpFile, OntologyDictionary.class);

        // save in workspace
        String objectRef = saveObject(params.getWorkspaceName(),
                                      params.getObjectName(),
                                      "KBaseOntology.OntologyDictionary",
                                      o,
                                      null,
                                      makeProvenance("KBaseOntology.OntologyDictionary imported from OBO file",
                                                     methodName,
                                                     methodParams));

        ImportResult rv = new ImportResult()
            .withObjectRef(objectRef);

        // clean up tmp files
        tmpFile.delete();
        if (params.getFile().getPath()==null) {
            java.io.File f = new java.io.File(filePath);
            f.delete();
        }

        return rv;
    }

    /**
       Exports a generic object to a CSV file
    */
    public static ExportResult exportCSV(String wsURL,
                                         String shockURL,
                                         AuthToken token,
                                         ExportParams params) throws Exception {
        wc = createWsClient(wsURL,token);

        // figure out name and type of object
        GetObjectInfo3Results goir = wc.getObjectInfo3(new GetObjectInfo3Params().withObjects(Arrays.asList(new ObjectSpecification().withRef(params.getInputRef()))));
        String oType = getTypeFromObjectInfo(goir.getInfos().get(0));
        String oName = getNameFromObjectInfo(goir.getInfos().get(0));

        // get from workspace as a HNDArray
        HNDArray hnda = null;
        if (oType.startsWith("KBaseGenerics.NDArray")) {
            NDArray nda = wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(params.getInputRef()))).get(0).getData().asClassInstance(NDArray.class);
            hnda = makeHNDArray(nda);
        }
        else {
            hnda = wc.getObjects(Arrays.asList(new ObjectIdentity().withRef(params.getInputRef()))).get(0).getData().asClassInstance(HNDArray.class);
        }

        // export to CSV file, which inexplicably has
        // to be inside of a zip file.
        java.io.File tmpFile = java.io.File.createTempFile("mat", ".zip", tempDir);
        tmpFile.delete();
        ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tmpFile)));
        CSVWriter outfile = new CSVWriter(new OutputStreamWriter(zout));

        if (!oName.toLowerCase().endsWith(".csv"))
            oName += ".csv";
        zout.putNextEntry(new ZipEntry(oName));
        zout.flush();
        writeCSV(hnda, true, outfile);
        outfile.flush();
        zout.flush();
        zout.closeEntry();
        outfile.close();

        // save in Shock
        String shockID = toShock(shockURL, token, tmpFile);
        ExportResult rv = new ExportResult()
            .withShockId(shockID);

        // clean up tmp file
        tmpFile.delete();

        return rv;
    }

    /**
       List Generic objects in one or more workspaces, with
       optional filters
    */
    public static ListGenericObjectsResult listGenericObjects(String wsURL,
                                                              AuthToken token,
                                                              ListGenericObjectsParams params) throws Exception {

        // make sure caller specified workspace names
        List<String> wsNames = params.getWorkspaceNames();
        if (wsNames==null)
            throw new Exception("Must specify workspace names when listing generic objects");

        wc = createWsClient(wsURL,token);
        ArrayList<String> matchingObjects = new ArrayList<String>();

        // see if caller is filtering by object type
        List<String> objectTypes = params.getAllowedObjectTypes();
        // if not, look for all generic types
        if (objectTypes == null)
            objectTypes = Arrays.asList("KBaseGenerics.NDArray", "KBaseGenerics.HNDArray");

        // see if caller is filtering by anything else
        boolean needsMeta = false;
        if ((params.getAllowedDataTypes() != null) ||
            (params.getAllowedScalarTypes() != null) ||
            (params.getMinDimensions() != null) ||
            (params.getMaxDimensions() != null) ||
            (params.getLimitMapped() != null))
            needsMeta = true;
            
        // find all objects of each type
        for (String objectType : objectTypes) {
            if (!objectType.startsWith("KBaseGenerics."))
                throw new Exception("listGenericObjects can only list generic objects; invalid type "+objectType);
            if (!needsMeta) {
                matchingObjects.addAll(getRefsFromObjectInfo(wc.listObjects(new ListObjectsParams().withWorkspaces(wsNames).withType(objectType).withIncludeMetadata(new Long(0L)))));
            }
            else {
                for (Tuple11<Long, String, String, String, Long, String, Long, String, String, Long, Map<String,String>> info : wc.listObjects(new ListObjectsParams().withWorkspaces(wsNames).withType(objectType).withIncludeMetadata(new Long(1L)))) {
                    Map<String,String> meta = info.getE11();
                    if (meta==null)
                        continue;
                    if (params.getLimitMapped() != null) {
                        long l = params.getLimitMapped().longValue();
                        String mapped = meta.get("mapped");
                        if ((l==1L) && ((mapped==null) ||
                                        (!mapped.equals("true"))))
                            continue;
                        if ((l==2L) && (mapped != null) && (!mapped.equals("false")))
                            continue;
                    }
                    if (params.getMinDimensions() != null) {
                        long l = params.getMinDimensions().longValue();
                        String dims = meta.get("n_dimensions");
                        if (dims==null)
                            continue;
                        long d = StringUtil.atol(dims);
                        if (d < l)
                            continue;
                    }
                    if (params.getMaxDimensions() != null) {
                        long l = params.getMaxDimensions().longValue();
                        String dims = meta.get("n_dimensions");
                        if (dims==null)
                            continue;
                        long d = StringUtil.atol(dims);
                        if (d > l)
                            continue;
                    }
                    if (params.getAllowedDataTypes() != null) {
                        String dataType = meta.get("data_type");
                        if (dataType == null)
                            continue;
                        int pos = dataType.indexOf(" <");
                        if (pos > -1)
                            dataType = dataType.substring(0,pos);
                        
                        boolean found = false;
                        for (String okType : params.getAllowedDataTypes()) {
                            if (dataType.equals(okType))
                                found = true;
                        }
                        if (!found)
                            continue;
                    }
                    if (params.getAllowedScalarTypes() != null) {
                        String scalarType = meta.get("scalar_type");
                        if (scalarType == null)
                            continue;
                        
                        boolean found = false;
                        for (String okType : params.getAllowedScalarTypes()) {
                            if (scalarType.indexOf(okType) > -1)
                                found = true;
                        }
                        if (!found)
                            continue;
                    }

                    // if passed all filters, add it
                    matchingObjects.add(getRefFromObjectInfo(info));
                }
            }
        }

        ListGenericObjectsResult rv = new ListGenericObjectsResult()
            .withObjectIds(matchingObjects);
        return rv;
    }

    /**
       Gets metadata describing the data type and all dimensions of
       a generic object     
    */
    public static GetGenericMetadataResult getGenericMetadata(String wsURL,
                                                              AuthToken token,
                                                              GetGenericMetadataParams params) throws Exception {

        // make sure caller specified objects
        List<String> objectIDs = params.getObjectIds();
        if (objectIDs==null)
            throw new Exception("Must specify object ids when getting generic metadata");

        wc = createWsClient(wsURL,token);

        HashMap<String,GenericMetadata> objectInfo = new HashMap<String,GenericMetadata>();
        for (String objectID : objectIDs) {
            for (ObjectData od : wc.getObjects2(new GetObjects2Params().withObjects(Arrays.asList(new ObjectSpecification().withRef(objectID).withIncluded(Arrays.asList("/data_type","/n_dimensions","/dim_context"))))).getData()) {
                GenericMetadata gm = new GenericMetadata();
                gm.setObjectType(getTypeFromObjectInfo(od.getInfo()));
                HNDArray hnda = null;
                if (gm.getObjectType().startsWith("KBaseGenerics.NDArray")) {
                    NDArray nda = od.getData().asClassInstance(NDArray.class);
                    hnda = makeHNDArray(nda);
                }
                else
                    hnda = od.getData().asClassInstance(HNDArray.class);

                // get data type and n_dimensions from object data
                gm.setDataType(toString(hnda.getDataType(),false));
                gm.setNDimensions(hnda.getNDimensions());

                // get mapped status and scalar/values types from metadata
                Long isMapped = new Long(0L);
                Map<String,String> meta = od.getInfo().getE11();
                if (meta!=null) {
                    String mapped = meta.get("mapped");
                    if ((mapped != null) && (mapped.equals("true")))
                        isMapped = new Long(1L);
                    String valueType = meta.get("value_type");
                    if (valueType != null) {
                        gm.setValueTypes(Arrays.asList(valueType.split(", ")));
                    }
                    String scalarType = meta.get("scalar_type");
                    if (scalarType != null) {
                        gm.setScalarTypes(Arrays.asList(scalarType.split(", ")));
                    }
                }
                gm.setIsMapped(isMapped);

                // loop over all dimensions to get names and types
                List<String> valueTypes = new ArrayList<String>();
                List<String> scalarTypes = new ArrayList<String>();
                List<String> dimensionTypes = new ArrayList<String>();
                List<Long> dimensionSizes = new ArrayList<Long>();
                List<Long> hasUniqueSubindices = new ArrayList<Long>();
                List<List<String>> dimensionValueTypes = new ArrayList<List<String>>();
                List<List<String>> dimensionScalarTypes = new ArrayList<List<String>>();
                for (DimensionContext dc : hnda.getDimContext()) {
                    dimensionTypes.add(toString(dc.getDataType(),false));
                    dimensionSizes.add(dc.getSize());
                    hasUniqueSubindices.add(new Long(hasUniqueSubindices(dc) ? 1L : 0L));
                    List<String> dimensionValueType = new ArrayList<String>();
                    List<String> dimensionScalarType = new ArrayList<String>();
                    for (TypedValues tv : dc.getTypedValues()) {
                        dimensionValueType.add(toString(tv,false));
                        dimensionScalarType.add(tv.getValues().getScalarType());
                    }
                    dimensionValueTypes.add(dimensionValueType);
                    dimensionScalarTypes.add(dimensionScalarType);
                }
                gm.setDimensionTypes(dimensionTypes);
                gm.setDimensionSizes(dimensionSizes);
                gm.setHasUniqueSubindices(hasUniqueSubindices);
                gm.setDimensionValueTypes(dimensionValueTypes);
                gm.setDimensionScalarTypes(dimensionScalarTypes);

                // save data
                objectInfo.put(objectID,gm);
            }
        }

        GetGenericMetadataResult rv = new GetGenericMetadataResult()
            .withObjectInfo(objectInfo);
        return rv;
    }

    /**
       Translates a dimension label into the corresponding TypedValues
       object.  If no 2nd dimension specified, assumes 1st.
    */
    public static TypedValues getTypedValues(HNDArray hnda,
                                             String dimensionID) throws Exception {
        int pos = dimensionID.indexOf("/");
        int dim1 = 1;
        int dim2 = 1;
        if (pos<=0)
            dim1 = StringUtil.atoi(dimensionID);
        else {
            dim1 = StringUtil.atoi(dimensionID,0,pos);
            dim2 = StringUtil.atoi(dimensionID,pos+1);
        }
        List<DimensionContext> dcs = hnda.getDimContext();
        if ((dim1 < 1) || (dim1 > dcs.size()))
            throw new Exception("Error: dimension index '"+dimensionID+"'; out of bounds; first number must be in range 1-"+dcs.size());
        DimensionContext dc = dcs.get(dim1-1);
        List<TypedValues> tvs = dc.getTypedValues();
        if ((dim2 < 1) || (dim2 > tvs.size()))
            throw new Exception("Error: dimension index '"+dimensionID+"'; out of bounds; second number must be in range 1-"+tvs.size());
        return(tvs.get(dim2-1));
    }

    /**
       Gets dimension labels for specified dimensions of
       a generic object
    */
    public static GetGenericDimensionLabelsResult
        getGenericDimensionLabels(String wsURL,
                                  AuthToken token,
                                  GetGenericDimensionLabelsParams params) throws Exception {

        String objectID = params.getObjectId();
        if (objectID==null)
            throw new Exception("Must specify object id when getting generic dimension labels");
        List<String> dimensionIDs = params.getDimensionIds();
        if (dimensionIDs==null)
            throw new Exception("Must specify at least one dimension id when getting dimension labels");

        wc = createWsClient(wsURL,token);
        Map<String,Values> labels = new HashMap<String,Values>();
        // get just the dimension metadata
        ObjectData od = wc.getObjects2(new GetObjects2Params().withObjects(Arrays.asList(new ObjectSpecification().withRef(objectID).withIncluded(Arrays.asList("/dim_context"))))).getData().get(0);
        String objectType = getTypeFromObjectInfo(od.getInfo());
        HNDArray hnda = null;
        if (objectType.startsWith("KBaseGenerics.NDArray")) {
            NDArray nda = od.getData().asClassInstance(NDArray.class);
            hnda = makeHNDArray(nda);
        }
        else
            hnda = od.getData().asClassInstance(HNDArray.class);
        
        for (String dimensionID : dimensionIDs) {
            Values v = getTypedValues(hnda, dimensionID).getValues();
            
            if ((params.getConvertToString() != null) &&
                (params.getConvertToString().longValue()==1L)) {
                // convert Values scalar type to string
                makeStringValues(v);
            }
            
            if ((params.getUniqueValues() != null) &&
                (params.getUniqueValues().longValue()==1L)) {
                // make Values unique
                v = findUniqueValues(v);
            }
            
            labels.put(dimensionID,v);
        }
        
        GetGenericDimensionLabelsResult rv =
            new GetGenericDimensionLabelsResult()
            .withDimensionLabels(labels);
        return rv;
    }

    /**
       Gets subset of data for a generic object     
    */
    public static GetGenericDataResult getGenericData(String wsURL,
                                                      AuthToken token,
                                                      GetGenericDataParams params) throws Exception {

        String objectID = params.getObjectId();
        if (objectID==null)
            throw new Exception("Must specify object id when getting generic data");

        wc = createWsClient(wsURL,token);

        // get the object, including values and dimension metadata
        ObjectData od = wc.getObjects2(new GetObjects2Params().withObjects(Arrays.asList(new ObjectSpecification().withRef(objectID).withIncluded(Arrays.asList("/dim_context","/typed_values"))))).getData().get(0);
        String objectType = getTypeFromObjectInfo(od.getInfo());
        HNDArray hnda = null;
        boolean isHeterogeneous = true;
        if (objectType.startsWith("KBaseGenerics.NDArray")) {
            NDArray nda = od.getData().asClassInstance(NDArray.class);
            isHeterogeneous = false;
            hnda = makeHNDArray(nda);
        }
        else
            hnda = od.getData().asClassInstance(HNDArray.class);

        // calculate all valid dimension ids.
        // if unique subindices in a dimension, then we can
        // fix subindices; otherwise, we must fix the whole dimension
        LinkedHashSet<String> allDimensionIDs = new LinkedHashSet<String>();
        List<DimensionContext> dcs = hnda.getDimContext();
        List<Long> dLengths = new ArrayList<Long>();
        for (int i=0; i<dcs.size(); i++) {
            DimensionContext dc = dcs.get(i);
            dLengths.add(new Long((long)dc.getSize()));
            if (hasUniqueSubindices(dc)) {
                List<TypedValues> tvs = dc.getTypedValues();
                for (int j=0; j<tvs.size(); j++)
                    allDimensionIDs.add((i+1)+"/"+(j+1));
            }
            else
                allDimensionIDs.add((i+1)+"");
        }

        // make sure all dimension ids are either constant or variable
        // and that there are no invalid ids specified
        List<String> variableDimensionIDs = params.getVariableDimensionIds();
        Map<String,Long> constantDimensionIDMap = params.getConstantDimensionIds();
        if (variableDimensionIDs==null)
            variableDimensionIDs = new ArrayList<String>();
        if (constantDimensionIDMap==null)
            constantDimensionIDMap = new LinkedHashMap<String,Long>();
        Set<String> constantDimensionIDs = constantDimensionIDMap.keySet();
        Set<String> checkDimensionIDs = new HashSet<String>(constantDimensionIDs);
        checkDimensionIDs.addAll(variableDimensionIDs);
        for (String dimensionID : checkDimensionIDs) {
            if (!allDimensionIDs.contains(dimensionID)) {
                boolean found = false;
                String dim1 = new String(dimensionID);
                int pos = dim1.indexOf("/");
                if (pos > 0)
                    dim1 = dim1.substring(0,pos);
                
                for (String dimensionID2 : allDimensionIDs) {
                    if (dimensionID2.startsWith(dim1+"/")) {
                        found = true;
                        continue;
                    }
                }
                if (!found)
                    throw new Exception("Dimension index "+dimensionID+" invalid for object");
            }
        }
        // once all are known to be valid, remove them from the full set
        for (String dimensionID : checkDimensionIDs) {
            if (allDimensionIDs.contains(dimensionID))
                allDimensionIDs.remove(dimensionID);
            else {
                boolean found = false;
                String dim1 = new String(dimensionID);
                int pos = dim1.indexOf("/");
                if (pos > 0)
                    dim1 = dim1.substring(0,pos);
                for (Iterator<String> iterator = allDimensionIDs.iterator(); iterator.hasNext();) {
                    String dimensionID2 = iterator.next();
                    if (dimensionID2.startsWith(dim1+"/")) {
                        found = true;
                        allDimensionIDs.remove(dimensionID2);
                    }
                }
                if (!found)
                    throw new Exception("Dimension index "+dimensionID+" used in both constant and variable dimension");
            }
        }
        // and save any remaining dimensions as variable
        variableDimensionIDs.addAll(allDimensionIDs);

        // must be at least 2 variable dimensions
        // to return X and Y
        int nVariableDimensions = variableDimensionIDs.size();
        if (nVariableDimensions < 2)
            throw new Exception("There must be at least two variable dimensions");

        // make the first variable dimension the X axis
        String dimX = variableDimensionIDs.get(0);
        Values valuesX = getTypedValues(hnda,dimX).getValues();
        if (dimX.indexOf("/") > 0)
            valuesX = findUniqueValues(valuesX);

        // set up loop over all other variable dimensions
        List<String> seriesLabels = new ArrayList<String>();
        List<Values> valuesY = new ArrayList<Values>();
        List<List> variableValues = new ArrayList<List>();
        List<String> variableNames = new ArrayList<String>();
        List<Long> curIndex = new ArrayList<Long>();
        int nSeries = 1;
        for (int i=1; i<nVariableDimensions; i++) {
            TypedValues tv = getTypedValues(hnda,variableDimensionIDs.get(i));
            Values v = tv.getValues();
            if (variableDimensionIDs.get(i).indexOf("/") > 0)
                v = findUniqueValues(v);
            List objects = getObjects(v);
            variableValues.add(objects);
            nSeries *= objects.size();
            variableNames.add(toString(tv,false));
            curIndex.add(new Long(1L));
        }

        // loop over all variable dimensions
        for (int i=0; i<nSeries; i++) {
            // fix all constant and variable dimensions
            HashMap<String,Long> fixedDimensionMap = new LinkedHashMap<String,Long>();
            fixedDimensionMap.putAll(constantDimensionIDMap);
            String seriesLabel = "";
            for (int j=0; j<variableNames.size(); j++) {
                if (j>0)
                    seriesLabel += " & ";
                fixedDimensionMap.put(variableDimensionIDs.get(j+1),
                                      curIndex.get(j));
                seriesLabel += variableNames.get(j)
                    + " = "
                    + variableValues.get(j).get((int)(curIndex.get(j).longValue())-1);
            }
            
            // calculate the unique indices for each dimension
            List<Long> fixedIndices = new ArrayList<Long>();
            for (int j=0; j<dLengths.size(); j++) {
                String dim1 = (j+1)+"";
                if (fixedDimensionMap.get(dim1) != null)
                    fixedIndices.add(fixedDimensionMap.get(dim1));
                else {
                    // see if all subindices are fixed
                    DimensionContext dc = dcs.get(j);
                    boolean found = true;
                    List<Long> valueIndices = new ArrayList<Long>();
                    List<TypedValues> tvs = dc.getTypedValues();
                    for (int k=0; k<tvs.size(); k++) {
                        String dimensionID = dim1 + "/" + (k+1);
                        Long l = fixedDimensionMap.get(dimensionID);
                        if (l==null)
                            found = false;
                        else
                            valueIndices.add(l);
                    }
                    if (found)
                        fixedIndices.add(mergeUniqueIndices(dc,
                                                            valueIndices));
                    else
                        fixedIndices.add(null);
                }
            }
            
            // find the right set of values and fix them
            Long hetIndex = new Long(1L);
            if (isHeterogeneous) {
                hetIndex = fixedIndices.get(0);
                if (hetIndex==null)
                    throw new Exception("If getting data from heterogeneous matrix, the heterogeneous dimension can't be the X axis");
                hetIndex++;
                fixedIndices.remove(0);
            }
            Values v = hnda.getTypedValues().get((int)(hetIndex.longValue())-1).getValues();
            Values seriesValues = fixValues(v,
                                            dLengths,
                                            fixedIndices);

            // save the data for return to caller
            valuesY.add(seriesValues);
            seriesLabels.add(seriesLabel);
            
            // increment all indices
            Long l = curIndex.get(0);
            l++;
            curIndex.set(0,l);
            for (int j=0; j<variableNames.size()-1; j++) {
                if (curIndex.get(j) > variableValues.get(j).size()) {
                    curIndex.set(j,new Long(1L));
                    l = curIndex.get(j+1);
                    l++;
                    curIndex.set(j+1,l);
                }
            }
        }

        GetGenericDataResult rv = new GetGenericDataResult()
            .withValuesX(valuesX)
            .withValuesY(valuesY)
            .withSeriesLabels(seriesLabels);
        
        return rv;
    }
}
