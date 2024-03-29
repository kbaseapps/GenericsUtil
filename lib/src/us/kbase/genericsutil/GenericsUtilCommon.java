package us.kbase.genericsutil;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import java.net.URL;

import us.kbase.kbasegenerics.*;

import org.strbio.IO;
import org.strbio.io.*;
import org.strbio.math.*;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

import com.opencsv.*;

/**
   This class implements methods for manipulating Generics that
   are not dependent on other KBase services
*/
public class GenericsUtilCommon {
    protected static OntologyData2 ontologyData = null;

    /**
      formats for "toString" methods:

      PRINT_NAME = "concentration (milligram per kilogram)"
      PRINT_OREF = "concentration <ME:0000129> (milligram per kilogram <UO:0000308>)"
      PRINT_BRIEF = "concentration, milligram per kilogram"
    */
    public static final int PRINT_NAME = 0;
    public static final int PRINT_OREF = 1;
    public static final int PRINT_BRIEF = 2;

    /**
       class encapsulating all the ontology data we've loaded
    */
    public static class OntologyData2 {
        /**
           map of ontology term ids to strings
        */
        public HashMap<String,String> oTermToString;

        /**
           list of valid microtypes (oterms)
        */
        public HashSet<String> microtypes;

        /**
           list of valid properties (oterms)
        */
        public HashSet<String> validProperties;

        /**
           list of valid dimensions (oterms)
        */
        public HashSet<String> validDimensions;

        /**
           list of valid dimension vars (oterms)
        */
        public HashSet<String> validDimensionVars;
        
        /**
           map of case sensitive strings to ontology term ids
        */
        public HashMap<String,List<String>> stringToOTerms;

        /**
           map of lower case strings to ontology term ids
        */
        public HashMap<String,List<String>> stringToOTermsLC;
        
        /**
           map of ontology term ids to data types
        */
        public HashMap<String,String> oTermToDataType;

        /**
           map of ontology term ids to valid units (term ids)
        */
        public HashMap<String,List<String>> oTermToValidUnits;

        /**
           map of ontology term ids to valid units parent (term ids)
        */
        public HashMap<String,List<String>> oTermToValidUnitsParents;
            
        /**
           map of ontology term ids to (direct, is_a) parents
        */
        public HashMap<String,List<String>> oTermToParents;

        /**
           true if no key contains a comma followed by a space
        */
        private boolean isCommaSafe;

        /**
           true if no key contains a semicolon followed by a space
        */
        private boolean isSemicolonSafe;
        
        /**
           map a term
        */
        private void mapStringToOTerm(String key, String oTerm) {
            if (key.indexOf(", ") > -1)
                isCommaSafe = false;
            if (key.indexOf("; ") > -1)
                isSemicolonSafe = false;
            List<String> terms = stringToOTerms.get(key);
            if (terms == null) {
                terms = new ArrayList<String>();
                stringToOTerms.put(key,terms);
            }
            if (!terms.contains(oTerm))
                terms.add(oTerm);
            String lcKey = key.toLowerCase();
            terms = stringToOTermsLC.get(lcKey);
            if (terms == null) {
                terms = new ArrayList<String>();
                stringToOTermsLC.put(lcKey,terms);
            }
            if (!terms.contains(oTerm))
                terms.add(oTerm);
        }

        /**
           load in a dictionary, mapping synonyms and terms
        */
        public void loadOBO(String fileName) throws Exception {
            BufferedReader infile = IO.openReader(fileName);
            String oTerm = null;
            String buffer = null;
            while ((buffer = infile.readLine()) != null) {
                if (buffer.startsWith("id: "))
                    oTerm = buffer.substring(4);
                else if (buffer.startsWith("name: ")) {
                    String key = buffer.substring(6);
                    mapStringToOTerm(key.toLowerCase(),oTerm);
                    oTermToString.put(oTerm,key);
                }
                else if (buffer.startsWith("synonym: ")) {
                    int pos = buffer.indexOf("EXACT [");
                    if (pos == -1)
                        pos = buffer.indexOf("RELATED [");
                    if (pos == -1)
                        pos = buffer.indexOf("RELATED InChIKey [");
                    if (pos > -1) {
                        mapStringToOTerm(buffer.substring(10,pos-2),
                                         oTerm);
                    }
                }
                else if (buffer.startsWith("is_a: ")) {
                    String parent = buffer.replace("is_a: ","");
                    int pos = parent.indexOf(" !");
                    if (pos > -1) {
                        parent = parent.substring(0,pos);
                    }
                    List<String> terms = oTermToParents.get(oTerm);
                    if (terms == null) {
                        terms = new ArrayList<String>();
                        oTermToParents.put(oTerm,terms);
                    }
                    terms.add(parent);
                }
                else if (buffer.startsWith("property_value: data_type \"")) {
                    String dataType = buffer.replace("property_value: data_type \"","");
                    int pos = dataType.indexOf("\"");
                    dataType = dataType.substring(0,pos);
                    oTermToDataType.put(oTerm,dataType);
                }
                else if (buffer.startsWith("property_value: valid_units_parent \"")) {
                    String units = buffer.replace("property_value: valid_units_parent \"","");
                    int pos = units.indexOf("\"");
                    units = units.substring(0,pos);
                    oTermToValidUnitsParents.put(oTerm,Arrays.asList(units.split(" ")));
                }
                else if (buffer.startsWith("property_value: valid_units \"")) {
                    String units = buffer.replace("property_value: valid_units \"","");
                    int pos = units.indexOf("\"");
                    units = units.substring(0,pos);
                    oTermToValidUnits.put(oTerm,Arrays.asList(units.split(" ")));
                }
                else if (buffer.startsWith("property_value: is_valid_dimension_variable \"")) {
                    validDimensionVars.add(oTerm);
                }
                else if (buffer.startsWith("property_value: is_valid_dimension \"")) {
                    validDimensions.add(oTerm);
                }
                else if (buffer.startsWith("property_value: is_microtype \"")) {
                    microtypes.add(oTerm);
                }
                else if (buffer.startsWith("property_value: is_valid_property \"")) {
                    validProperties.add(oTerm);
                }
                else if (buffer.startsWith("xref: Ref: ")) {
                    String dataType = buffer.replace("xref: Ref", "ref");
                    oTermToDataType.put(oTerm,dataType);
                }
                else if (buffer.startsWith("xref: ORef: ")) {
                    String dataType = buffer.replace("xref: ORef", "oref");
                    oTermToDataType.put(oTerm,dataType);
                }
            }
            infile.close();
        }

        /**
           lookup term in all dictionaries; ambiguity results in null return
        */
        public String lookupString(String key) throws Exception {
            List<String> vals = stringToOTerms.get(key);
            if (vals==null) {
                String lcKey = key.toLowerCase();
                vals = stringToOTermsLC.get(lcKey);
            }
            if (vals==null) {
                return null;
            }
            if (vals.size() > 1)
                return null;
            return vals.get(0);
        }

        /**
           lookup term in all dictionaries; first matching term is returned, or null if no match
        */
        public String lookupStringFirst(String key) throws Exception {
            List<String> vals = stringToOTerms.get(key);
            if (vals==null) {
                String lcKey = key.toLowerCase();
                vals = stringToOTermsLC.get(lcKey);
            }
            if (vals==null)
                return null;
            return vals.get(0);
        }

        /**
           lookup term in all dictionaries, under a specified parent.
           first matching term is returned, or null if no match
        */
        public String lookupStringUnder(String key,
                                        String parentORef) throws Exception {
            List<String> vals = stringToOTerms.get(key);
            if (vals!=null)
                for (String val : vals)
                    if (inTree(val, parentORef))
                        return val;
            String lcKey = key.toLowerCase();
            vals = stringToOTermsLC.get(lcKey);
            if (vals!=null)
                for (String val : vals)
                    if (inTree(val, parentORef))
                        return val;
            return null;
        }
        
        /**
           lookup term in a dictionary, with specified term prefix.  first matching term is returned, or null if no match
        */
        public String lookupString(String key,
                                   String prefix) throws Exception {
            List<String> vals = stringToOTerms.get(key);
            if (vals!=null)
                for (String val : vals)
                    if (val.startsWith(prefix))
                        return val;
            String lcKey = key.toLowerCase();
            vals = stringToOTermsLC.get(lcKey);
            if (vals!=null)
                for (String val : vals)
                    if (val.startsWith(prefix))
                        return val;
            return null;
        }
        
        /**
           lookup term in all dictionaries
        */
        public String lookupOTerm(String key) throws Exception {
            return oTermToString.get(key);
        }

        /**
           lookup data type, cache it if obtained from parents
        */
        public String lookupDataType(String key) throws Exception {
            String rv = oTermToDataType.get(key);
            if (rv==null) {
                List<String> parents = oTermToParents.get(key);
                if (parents != null) {
                    for (String term : parents) {
                        rv = lookupDataType(term);
                        if (rv != null) {
                            oTermToDataType.put(key,rv);
                            return rv;
                        }
                    }
                }
            }
            return rv;
        }

        /**
           are there valid units?
        */
        public boolean validUnits(String oTerm, String unitsTerm) throws Exception {
            List<String> validUnits = oTermToValidUnits.get(oTerm);
            List<String> validUnitsParents = oTermToValidUnitsParents.get(oTerm);
            if ((validUnits == null) &&
                (validUnitsParents == null))
                return (inTree(unitsTerm,"UO:0000000"));

            if (validUnits != null) {
                for (String units : validUnits)
                    if (unitsTerm.equals(units))
                        return true;
            }

            if (validUnitsParents != null) {
                for (String units : validUnitsParents)
                    if ((inTree(unitsTerm, units)) &&
                        (!unitsTerm.equals(units)))
                        return true;
            }
            return false;
        }
        
        /**
           check lineage; is a (possible) child somewhere in the
           tree rooted by a (potential) parent node.
        */
        public boolean inTree(String child, String parent) {
            if (parent.equals(child))
                return true;
            List<String> directParents = oTermToParents.get(child);
            if (directParents != null)
                for (String directParent : directParents)
                    if (inTree(directParent, parent))
                        return true;
            return false;
        }

        /**
           Checks whether a term matches a reference, or its synonyms,
           or its parents.  Case insensitive.
        */
        public boolean matches(String ref, String term) throws Exception {
            term = term.toLowerCase();
            String match = oTermToString.get(ref);
            if (match == null)
                throw new Exception("Unknown term ID "+ref);
            // System.out.println("debug5: "+ref+" "+term+" "+match);
            if (match.toLowerCase().equals(term))
                return true;
            List<String> vals = stringToOTermsLC.get(term);
            if (vals!=null)
                for (String val : vals)
                    if (val.equals(term))
                        return true;
            List<String> parents = oTermToParents.get(ref);
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

        public boolean isCommaSafe() {
            return isCommaSafe;
        }

        public boolean isSemicolonSafe() {
            return isSemicolonSafe;
        }

        public OntologyData2() {
            oTermToString = new HashMap<String,String>();
            oTermToDataType = new HashMap<String,String>();
            oTermToParents = new HashMap<String,List<String>>();
            stringToOTerms = new HashMap<String,List<String>>();
            stringToOTermsLC = new HashMap<String,List<String>>();
            microtypes = new HashSet<String>();
            validProperties = new HashSet<String>();
            validDimensions = new HashSet<String>();
            validDimensionVars = new HashSet<String>();
            oTermToValidUnits = new HashMap<String,List<String>>();
            oTermToValidUnitsParents = new HashMap<String,List<String>>();
            isCommaSafe = true;
            isSemicolonSafe = true;
        }
    }

    public GenericsUtilCommon(List<String> dictionaries) throws Exception {
        ontologyData = new OntologyData2();
        if (dictionaries != null) {
            for (String dict : dictionaries) {
                ontologyData.loadOBO(dict);
            }
        }
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
       helper function to make a new pre-mapped Term
    */
    public static Term makeTerm(String termName,
                                String termRef) {
        return new Term()
            .withTermName(termName)
            .withOtermRef(termRef)
            .withOtermName(termName);
    }

    /**
       helper function to turn a Oterm value into a Term
    */
    public static Term makeTerm(Value v) throws Exception {
        if (!v.getScalarType().equals("oterm_ref"))
            throw new Exception("can only turn Oterm values into Terms");
        Term rv = new Term()
            .withOtermRef(v.getOtermRef());
        String sv = v.getStringValue();
        if (sv != null)
            rv.setTermName(sv);
        try {
            sv = ontologyData.lookupOTerm(rv.getOtermRef());
            rv.setOtermName(sv);
        }
        catch (Exception e) {
        }
        return rv;        
    }

    /**
       helper function to make a new mapped Term from a description
    */
    public static Term mapTerm(String description) throws Exception {
        String oTerm = ontologyData.lookupStringFirst(description);
        if (oTerm==null)
            throw new Exception("unmappable term "+description);
        String canonicalName = ontologyData.lookupOTerm(oTerm);
        return new Term()
            .withTermName(description)
            .withOtermRef(oTerm)
            .withOtermName(canonicalName);
    }

    /**
       helper function to make a new mapped Term from a description,
       in a particular ontology
    */
    public static Term mapTerm(String description,
                               String prefix) throws Exception {
        String oTerm = ontologyData.lookupString(description, prefix);
        if (oTerm==null)
            throw new Exception("unmappable term "+description+" under "+prefix);
        String canonicalName = ontologyData.lookupOTerm(oTerm);
        return new Term()
            .withTermName(description)
            .withOtermRef(oTerm)
            .withOtermName(canonicalName);
    }
    
    /**
       helper function to make a new mapped Term from a description,
       in a particular part of an ontology tree
    */
    public static Term mapTermUnder(String description,
                                    String parentORef) throws Exception {
        String oTerm = ontologyData.lookupStringUnder(description, parentORef);
        if (oTerm==null)
            throw new Exception("unmappable term "+description+" under "+parentORef);
        String canonicalName = ontologyData.lookupOTerm(oTerm);
        return new Term()
            .withTermName(description)
            .withOtermRef(oTerm)
            .withOtermName(canonicalName);
    }
    

    /**
       helper function to make a new (unmapped) string value
    */
    public static Value makeValue(String val) {
        Value rv = new Value()
            .withScalarType("string")
            .withStringValue(val);
        return rv;
    }

    /**
       helper function to make a new pre-mapped Value
    */
    public static Value makeValue(String termName,
                                  String termRef) {
        return new Value()
            .withScalarType("oterm_ref")
            .withOtermRef(termRef)
            .withStringValue(termName);
    }

    /**
       Helper function to make a new mapped Value from a description.
       Fails if not mappable.
    */
    public static Value mapValue(String description) throws Exception {
        String oTerm = ontologyData.lookupStringFirst(description);
        if (oTerm==null)
            throw new Exception("unmappable value "+description);
        return new Value()
            .withScalarType("oterm_ref")
            .withOtermRef(oTerm)
            .withStringValue(description);
    }

    /**
       Helper function to make a new mapped Value from a description
       If mapping fails, keeps value as string
    */
    public static Value mapValueFallback(String description) {
        try {
            return mapValue(description);
        }
        catch (Exception e) {
            return makeValue(description);
        }
    }
    
    /**
       Helper function to make a new mapped Value from a description.
       Fails if not mappable.
    */
    public static Value mapValueUnder(String description,
                                      String parentORef) throws Exception {
        String oTerm = ontologyData.lookupStringUnder(description,
                                                      parentORef);
        if (oTerm==null)
            throw new Exception("unmappable value "+description+" under parent "+parentORef);
        return new Value()
            .withScalarType("oterm_ref")
            .withOtermRef(oTerm)
            .withStringValue(description);
    }
    
    /**
       helper function to make a new float value
    */
    public static Value makeValue(double val) {
        return new Value()
            .withScalarType("float")
            .withFloatValue(new Double(val));
    }

    /**
       helper function to make a new int value
    */
    public static Value makeValue(long val) {
        return new Value()
            .withScalarType("int")
            .withIntValue(new Long(val));
    }
    
    /**
       helper function to make a new boolean value
    */
    public static Value makeValue(boolean val) {
        return new Value()
            .withScalarType("boolean")
            .withBooleanValue(new Long(val ? 1L : 0L));
    }

    /**
       Helper function to make a new mapped set of oterm_ref Values from a
       list of descriptions.  Maps null strings to null oterms.
       Fails if any strings not mappable.
    */
    public static Values mapValues(List<String> descriptions) throws Exception {
        ArrayList<String> oTerms = new ArrayList<String>();
        for (String description : descriptions) {
            if (description==null)
                oTerms.add(null);
            else {
                String oTerm = ontologyData.lookupStringFirst(description);
                if (oTerm==null)
                    throw new Exception("unmappable value "+description);
                oTerms.add(oTerm);
            }
        }
        return new Values()
            .withScalarType("oterm_ref")
            .withOtermRefs(oTerms)
            .withStringValues(descriptions);
    }

    /**
       Helper function to make a new mapped set of oterm_ref Values from a
       list of descriptions.  Maps null strings to null.
       Inserts null oterms if any strings not mappable.
    */
    public static Values mapValuesFallback(List<String> descriptions) throws Exception {
        ArrayList<String> oTerms = new ArrayList<String>();
        for (String description : descriptions) {
            if (description==null)
                oTerms.add(null);
            else {
                String oTerm = ontologyData.lookupStringFirst(description);
                oTerms.add(oTerm);
            }
        }
        return new Values()
            .withScalarType("oterm_ref")
            .withOtermRefs(oTerms)
            .withStringValues(descriptions);
    }
    
    /**
       Helper function to make a new mapped set of oterm_ref Values from a
       list of descriptions, under a parent term.  Maps null strings
       to null.  Fails if any strings not mappable.
    */
    public static Values mapValues(List<String> descriptions,
                                   String parentORef) throws Exception {
        ArrayList<String> oTerms = new ArrayList<String>();
        for (String description : descriptions) {
            if (description==null)
                oTerms.add(null);
            else {
                String oTerm = ontologyData.lookupStringUnder(description,
                                                          parentORef);
                if (oTerm==null)
                    throw new Exception("unmappable value "+description+" under parent "+parentORef);
                oTerms.add(oTerm);
            }
        }
        return new Values()
            .withScalarType("oterm_ref")
            .withOtermRefs(oTerms)
            .withStringValues(descriptions);
    }

    /**
       Map description into typed value.  Looks for units in parentheses, if applicable
    */
    public static TypedValue mapTV(String description) throws Exception {
        int pos = description.indexOf("=");
        String term = description.substring(0,pos).trim();
        String value = description.substring(pos+1).trim();
        TypedValue rv = new TypedValue()
            .withValueType(mapTerm(term));

        // look for units
        pos = value.indexOf(" (");
        if (pos > -1) {
            String units = value.substring(pos+2);
            int pos2 = units.indexOf(")");
            if (pos2 == units.length()-1) {
                units = units.substring(0,pos2).trim();
                value = value.substring(0,pos);
                try {
                    rv.setValueUnits(mapTermUnder(units,"UO:0000000"));
                }
                catch (Exception e) {
                    rv.setValueUnits(mapTerm(units));
                }
            }
        }

        String dataType = ontologyData.lookupDataType(rv.getValueType().getOtermRef());
        if (dataType==null)
            dataType = "string";
        if (dataType.equals("int"))
            rv.setValue(makeValue((long)Double.parseDouble(value)));
        else if (dataType.equals("float"))
            rv.setValue(makeValue(Double.parseDouble(value)));
        else if (dataType.equals("boolean"))
            rv.setValue(makeValue(StringUtil.atol(value) != 0L));
        else if (dataType.equals("string"))
            rv.setValue(mapValueFallback(value));
        else if (dataType.startsWith("ref:")) {
            Value v = makeValue(value);
            v.setScalarType("object_ref");
            v.setObjectRef(v.getStringValue());
            rv.setValue(v);
        }
        else if (dataType.startsWith("oref:")) {
            String parentORef = dataType.substring(5).trim();
            rv.setValue(mapValueUnder(value, parentORef));
        }

        return rv;
    }

    /**
       map description into a list of TypedValue objects.  Splits on commas with spaces after them.
    */
    public static ArrayList<TypedValue> mapTVList(String description) throws Exception {
        // do warning instead when mapping in other direction:
        // if (!ontologyData.isCommaSafe())
        // throw new Exception("Error - ontology is not comma-safe");
        ArrayList<TypedValue> rv = new ArrayList<TypedValue>();
        for (String d : description.split(", ")) {
            rv.add(mapTV(d));
        }
        return rv;
    }

    /**
       map description into a list of lists of TypedValue objects.  Splits on semicolons with spaces after then, then re-splits on commas with spaces after them.
    */
    public static ArrayList<ArrayList<TypedValue>> mapTVListList(String description) throws Exception {
        // do warning instead when mapping in other direction:
        // if (!ontologyData.isSemicolonSafe())
        // throw new Exception("Error - ontology is not semicolon-safe");
        ArrayList<ArrayList<TypedValue>> rv = new ArrayList<ArrayList<TypedValue>>();
        for (String d : description.split("; ")) {
            rv.add(mapTVList(d));
        }
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
       parse a description into a TypedValues, with a required prefix
       for the term type
    */
    public static TypedValues mapTVs(String description,
                                     String prefix) throws Exception {
        String[] f = splitTrim(description);
        TypedValues rv = new TypedValues();
        if (prefix == null)
            rv.setValueType(mapTerm(f[0]));
        else
            rv.setValueType(mapTerm(f[0],prefix));
        ArrayList<TypedValue> vcs = null;
        for (int i=1; i<f.length; i++) {
            String s = f[i];
            if (s.indexOf("=") > -1) {
                if (vcs==null)
                    vcs = new ArrayList<TypedValue>();
                vcs.add(mapTV(s));
            }
            else if (i==f.length-1) {
                try {
                    rv.setValueUnits(mapTermUnder(s,"UO:0000000"));
                }
                catch (Exception e) {
                    rv.setValueUnits(mapTerm(s));
                }
            }
            else
                throw new Exception("unable to parse "+description);
        }
        if (vcs != null)
            rv.setValueContext(vcs);
        String dataType = ontologyData.lookupDataType(rv.getValueType().getOtermRef());
        if (dataType==null)
            dataType = "string";
        if (dataType.equals("int")) {
            rv.setValues(new Values()
                         .withScalarType(dataType)
                         .withIntValues(new ArrayList<Long>()));
        }
        else if (dataType.equals("float")) {
            rv.setValues(new Values()
                         .withScalarType(dataType)
                         .withFloatValues(new ArrayList<Double>()));
        }
        else if (dataType.equals("boolean")) {
            rv.setValues(new Values()
                         .withScalarType(dataType)
                         .withBooleanValues(new ArrayList<Long>()));
        }
        else if (dataType.equals("string")) {
            rv.setValues(new Values()
                         .withScalarType(dataType)
                         .withStringValues(new ArrayList<String>()));
        }
        else if (dataType.startsWith("ref:")) {
            rv.setValues(new Values()
                         .withScalarType("object_ref")
                         .withObjectRefs(new ArrayList<String>())
                         .withStringValues(new ArrayList<String>()));
        }
        else if (dataType.startsWith("oref:")) {
            rv.setValues(new Values()
                         .withScalarType("oterm_ref")
                         .withOtermRefs(new ArrayList<String>())
                         .withStringValues(new ArrayList<String>()));
        }
        else
            throw new Exception("unimplemented data type "+dataType);
        return rv;
    }

    /**
       parse a description into a TypedValues
    */
    public static TypedValues mapTVs(String description) throws Exception {
        // System.err.println("mapping "+description);
        return mapTVs(description, (String)null);
    }    

    /**
       parse a description and list of values into a TypedValues,
       mapping the values as appropriate.  This version restricts
       the data type to a given prefix.
    */
    public static TypedValues mapTVs(String description,
                                     String prefix,
                                     List<String> valueStrings) throws Exception {
        TypedValues rv = mapTVs(description, prefix);

        String dataType = ontologyData.lookupDataType(rv.getValueType().getOtermRef());
        if (dataType==null)
            dataType = "string";

        Values v = rv.getValues();
        for (String value : valueStrings) {
            if (dataType.equals("int"))
                v.getIntValues().add(new Long((long)Double.parseDouble(value)));
            else if (dataType.equals("float"))
                v.getFloatValues().add(new Double(Double.parseDouble(value)));
            else if (dataType.equals("boolean"))
                v.getBooleanValues().add(new Long(StringUtil.atol(value) == 0L ? 0L : 1L));
            else if (dataType.equals("string"))
                v.getStringValues().add(value);
            else if (dataType.startsWith("ref:")) {
                v.getStringValues().add(value);
                v.getObjectRefs().add(value);
            }
            else if (dataType.startsWith("oref:")) {
                String parentORef = dataType.substring(5).trim();
                Value vTmp = mapValueUnder(value, parentORef);
                v.getStringValues().add(value);
                v.getOtermRefs().add(vTmp.getOtermRef());
            }
        }
        return rv;
    }

    /**
       parse a description and list of values into a TypedValues,
       mapping the values as appropriate.
    */
    public static TypedValues mapTVs(String description,
                                     List<String> valueStrings) throws Exception {
        return mapTVs(description, null, valueStrings);
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
       Strip a pre-mapped value from a string
       */
    public static String stripTerm(String name) throws Exception {
        if (name.endsWith(">")) {
            int pos = name.lastIndexOf(" <");
            String ref = name.substring(pos+2,name.length()-1);
            name = name.substring(0,pos);
        }
        return name;
    }

    /**
       Make TypedValues description line back into English,
       stripping out terms.
    */
    public static String stripTVS(String description) throws Exception {
        String[] f = splitTrim(description);
        String rv = stripTerm(f[0]);
        if (f.length > 1) {
            if (f.length > 2) {
                // make user specify details for now;
                // units and context should be auto-detected!
                int nContext = 0;
                if ((f.length % 2) == 0)
                    nContext = (f.length-2)/2;
                else
                    nContext = (f.length-1)/2;
                for (int i=0; i<nContext; i++) {
                    String t = stripTerm(f[i*2+1]);
                    String v = stripTerm(f[i*2+2]);
                    rv += ", "+t+"="+v;
                }
                if ((f.length % 2) == 0)
                    rv += ", "+stripTerm(f[f.length-1]);
            }
            else
                rv += ", "+stripTerm(f[f.length-1]);
        }
        return rv;
    }
    
    /**
       check validity of Term
    */
    public static void validate(Term t) throws Exception {
        String oTerm = t.getOtermRef();
        if (t==null)
            throw new Exception("Unmapped term "+toString(t,PRINT_NAME));
        String canonicalName = ontologyData.lookupOTerm(oTerm);
        if (canonicalName==null)
            throw new Exception("Unable to look up "+toString(t,PRINT_OREF));
        if (!canonicalName.equals(t.getOtermName()))
            throw new Exception("Invalid canonical name for "+toString(t,PRINT_OREF)+": "+t.getOtermName()+" vs "+canonicalName);
    }

    /**
       check validity of TypedValue
    */
    public static void validate(TypedValue tv) throws Exception {
        Term t = tv.getValueType();
        validate(t);
        String oTerm = t.getOtermRef();
        if (!ontologyData.microtypes.contains(oTerm))
            throw new Exception("Invalid microtype "+toString(t,PRINT_OREF));
        if (!ontologyData.validProperties.contains(oTerm))
            throw new Exception("Invalid property "+toString(t,PRINT_OREF));
        String valueType = ontologyData.oTermToDataType.get(oTerm);
        if (valueType==null)
            throw new Exception("Unknown value type for "+toString(t,PRINT_OREF));
        Value v = tv.getValue();
        if (v == null)
            throw new Exception("Null value for "+toString(t,PRINT_OREF));
        if (valueType.startsWith("oref: ")) {
            String valueTypeRef = valueType.replace("oref: ","");
            if (ontologyData.lookupOTerm(valueTypeRef)==null)
                throw new Exception("Invalid constraint "+valueTypeRef+"; dictionary not loaded?");
            valueType = "oterm_ref";
            String valueRef = v.getOtermRef();
            if (!ontologyData.inTree(valueRef,valueTypeRef))
                throw new Exception("Invalid oref value for "+toString(t,PRINT_OREF)+": expected "+valueTypeRef+" got "+valueRef);
        }
        if (valueType.startsWith("ref: ")) {
            // can't validate these currently
            valueType = "object_ref";
        }
        String scalarType = v.getScalarType();
        if (!valueType.equals(scalarType) &&
            (!(scalarType.equals("oterm_ref") &&
               valueType.equals("string"))) &&
            (!(scalarType.equals("float") &&
               valueType.equals("int"))) &&
            (!(scalarType.equals("int") &&
               valueType.equals("float"))))
            throw new Exception("Mismatched value type for "+toString(t,PRINT_OREF)+": expected "+valueType+" got "+scalarType);
        Term u = tv.getValueUnits();
        if (u == null) {
            if (valueType.equals("int") || (valueType.equals("float")))
                throw new Exception("No units provided for "+toString(t,PRINT_OREF));
        }
        else {
            validate(u);
            if (valueType.equals("string") ||
                (valueType.equals("oterm_ref")) ||
                (valueType.equals("object_ref")))
                throw new Exception("Units "+toString(u,PRINT_OREF)+" should not be provided for "+toString(t,PRINT_OREF));
            if (!ontologyData.validUnits(oTerm,u.getOtermRef()))
                throw new Exception("Units "+toString(u,PRINT_OREF)+" not valid for "+toString(t,PRINT_OREF));
        }
    }

    /**
       check validity of TypedValues
    */
    public static void validate(TypedValues tvs,
                                int expectedSize) throws Exception {
        Term t = tvs.getValueType();
        validate(t);
        String oTerm = t.getOtermRef();
        if (!ontologyData.microtypes.contains(oTerm))
            throw new Exception("Invalid microtype "+toString(t,PRINT_OREF));
        if (!ontologyData.validDimensionVars.contains(oTerm))
            throw new Exception("Invalid dimension variable "+toString(t,PRINT_OREF));
        String valueType = ontologyData.oTermToDataType.get(oTerm);
        if (valueType==null)
            throw new Exception("Unknown value type for "+toString(t,PRINT_OREF));
        Values vals = tvs.getValues();
        if (vals == null)
            throw new Exception("Null value for "+toString(t,PRINT_OREF));
        String scalarType = vals.getScalarType();
        String valueTypeRef = null;
        if (valueType.startsWith("oref: ")) {
            valueTypeRef = valueType.replace("oref: ","");
            if (ontologyData.lookupOTerm(valueTypeRef)==null)
                throw new Exception("Invalid constraint "+valueTypeRef+"; dictionary not loaded?");
            valueType = "oterm_ref";
        }
        if (valueType.startsWith("ref: ")) {
            valueTypeRef = valueType.replace("ref: ","");
            valueType = "object_ref";
        }
        
        if (!valueType.equals(scalarType) &&
            (!(scalarType.equals("oterm_ref") &&
               valueType.equals("string"))) &&
            (!(scalarType.equals("float") &&
               valueType.equals("int"))) &&
            (!(scalarType.equals("int") &&
               valueType.equals("float"))))
            throw new Exception("Mismatched value type for "+toString(t,PRINT_OREF)+": expected "+valueType+" got "+scalarType);

        List objects = getObjects(vals);
        List<String> refs = (List<String>)getRefs(vals);
        if (objects == null)
            throw new Exception("Missing values for "+toString(t,PRINT_OREF)+": expected "+scalarType);
        if (objects.size() != expectedSize)
            throw new Exception("Wrong number of values for "+toString(t,PRINT_OREF)+": expected "+expectedSize+" got "+objects.size());
        if ((scalarType.equals("oterm_ref")) ||
            (scalarType.equals("object_ref"))) {
            if (refs==null)
                throw new Exception("Missing refs for "+toString(t,PRINT_OREF)+": expected "+scalarType);
            if (refs.size() != objects.size())
                throw new Exception("Different number of refs and objects for "+toString(t,PRINT_OREF));
        }
        if ((scalarType.equals("oterm_ref")) && (valueTypeRef != null)) {
            for (String valueRef : refs)
                if ((valueRef != null) && (!ontologyData.inTree(valueRef,valueTypeRef)))
                    throw new Exception("Invalid oref value for "+toString(t,PRINT_OREF)+": expected "+valueTypeRef+" got "+valueRef);
        }

        List<TypedValue> tvl = tvs.getValueContext();
        if (tvl != null)
            for (TypedValue tv : tvl)
                validate(tv);
        
        Term u = tvs.getValueUnits();
        if (u == null) {
            if (valueType.equals("int") || (valueType.equals("float")))
                throw new Exception("No units provided for "+toString(t,PRINT_OREF));
        }
        else {
            validate(u);
            if (valueType.equals("string") ||
                (valueType.equals("oterm_ref")) ||
                (valueType.equals("object_ref")))
                throw new Exception("Units "+toString(u,PRINT_OREF)+" should not be provided for "+toString(t,PRINT_OREF));
            if (!ontologyData.validUnits(oTerm,u.getOtermRef()))
                throw new Exception("Units "+toString(u,PRINT_OREF)+" not valid for "+toString(t,PRINT_OREF));
        }
    }

    /**
       check validity of DimensionContext
    */
    public static void validate(DimensionContext dc) throws Exception {
        Term t = dc.getDataType();
        validate(t);
        String oTerm = t.getOtermRef();
        if (!ontologyData.validDimensions.contains(oTerm))
            throw new Exception("Invalid dimension "+toString(t,PRINT_OREF));
        if (dc.getSize()==null)
            throw new Exception("Unknown length for dimension "+toString(t,PRINT_OREF));
        int size = (int)dc.getSize().longValue();
        for (TypedValues tvs : dc.getTypedValues()) {
            List objects = getObjects(tvs.getValues());
            if (objects.size() != size)
                throw new Exception("Invalid size for typed values in dimension "+toString(t,PRINT_OREF));
            validate(tvs,size);
        }
    }

    /**
       Get links to static objects from TypedValue.  Returns null if none.
    */
    public static HashMap<String,Set<String>> getRefLinks(TypedValue tv) throws Exception {
        HashMap<String,Set<String>> rv = null;
        Term t = tv.getValueType();
        String oTerm = t.getOtermRef();
        String valueType = ontologyData.oTermToDataType.get(oTerm);
        if (valueType.startsWith("ref: ")) {
            valueType = valueType.replace("ref: ","");
            String value = tv.getValue().getObjectRef();
            rv = new HashMap<String,Set<String>>();
            rv.put(valueType,new HashSet<String>(Arrays.asList(value)));
        }
        return rv;
    }

    /**
       Get links to static objects from TypedValues.  Returns null if none.
    */
    public static HashMap<String,Set<String>> getRefLinks(TypedValues tvs) throws Exception {
        HashMap<String,Set<String>> rv = null;
        Term t = tvs.getValueType();
        String oTerm = t.getOtermRef();
        String valueType = ontologyData.oTermToDataType.get(oTerm);
        if (valueType.startsWith("ref: ")) {
            valueType = valueType.replace("ref: ","");
            List<String> values = (List<String>)getRefs(tvs.getValues());
            Set<String> uniqueValues = new HashSet<String>();
            for (String value : values) {
                if ((value != null) && (!uniqueValues.contains(value)))
                    uniqueValues.add(value);
            }
            if (uniqueValues.size()==0) // e.g., if all values where null
                return null;
            rv = new HashMap<String,Set<String>>();
            rv.put(valueType,uniqueValues);
        }
        return rv;
    }

    /**
       merge ref links from src into dest, returning dest
    */
    private static HashMap<String,Set<String>> mergeRefLinks(HashMap<String,Set<String>> dest, HashMap<String,Set<String>> src) throws Exception {
        if (src==null)
            return dest;
        if (dest==null)
            dest = new HashMap<String,Set<String>>();
        for (String key : src.keySet()) {
            Set<String> oldVals = dest.get(key);
            if (oldVals==null)
                oldVals = new HashSet<String>();
            oldVals.addAll(src.get(key));
            dest.put(key,oldVals);
        }
        return dest;
    }

    /**
       Get links to static objects from DimensionContext.  Returns null if none.
    */
    public static HashMap<String,Set<String>> getRefLinks(DimensionContext dc) throws Exception {
        HashMap<String,Set<String>> rv = null;
        for (TypedValues tvs : dc.getTypedValues()) {
            HashMap<String,Set<String>> rls = getRefLinks(tvs);
            rv = mergeRefLinks(rv, rls);
        }
        return rv;
    }
    
    /**
       Get links to static objects from HNDArray.  Returns null if none.
    */
    public static HashMap<String,Set<String>> getRefLinks(HNDArray hnda) throws Exception {
        HashMap<String,Set<String>> rv = null;
        if (hnda.getArrayContext() != null)
            for (TypedValue tv : hnda.getArrayContext()) {
                HashMap<String,Set<String>> rls = getRefLinks(tv);
                rv = mergeRefLinks(rv, rls);
            }
        for (DimensionContext dc : hnda.getDimContext()) {
            HashMap<String,Set<String>> rls = getRefLinks(dc);
            rv = mergeRefLinks(rv, rls);
        }
        for (TypedValues tvs : hnda.getTypedValues()) {
            HashMap<String,Set<String>> rls = getRefLinks(tvs);
            rv = mergeRefLinks(rv, rls);
        }
        return rv;
    }

    /**
       update a Values object from String to another type, based
       on a reference term
    */
    public static void transformValues(Term valueType, Values v) throws Exception {
        List<String> sv = v.getStringValues();
        String inferredType = guessValueType(sv);
        String dataType = null;
        String ref = valueType.getOtermRef();
        String objectType = null;
        if (ref != null) {
            dataType = ontologyData.lookupDataType(ref);
        }
        if (dataType==null) {
            // guess based on values
            if (inferredType.equals("boolean"))
                makeBooleanValues(v);
            else if (inferredType.equals("float") ||
                     (inferredType.equals("int")))
                makeFloatValues(v);
        }
        else {
            if (dataType.equals("boolean")) {
                if (inferredType.equals("boolean"))
                    makeBooleanValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be boolean");
            }
            else if (dataType.equals("int")) {
                if ((inferredType.equals("int")) ||
                    (inferredType.equals("boolean")))
                    makeIntValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be integers");
            }
            else if (dataType.equals("float")) {
                if ((inferredType.equals("float")) ||
                    (inferredType.equals("int")) ||
                    (inferredType.equals("boolean")))
                    makeFloatValues(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be numeric");
            }
            else if ((dataType.equals("ref")) &&
                     (v.getScalarType().equals("string"))) {
                if (inferredType.equals("oterm_ref")) {
                    makeOtermRefValues(v);
                    for (String val : v.getOtermRefs()) {
                        if (val != null)
                            ontologyData.lookupOTerm(val);
                    }
                }
                else if (inferredType.equals("object_ref"))
                    makeObjectRefValues(v,null);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be references");
            }
        }
    }

    /**
       update a Value object from String to another type, based
       on a reference term
    */
    public static void transformValue(Term valueType, Value v) throws Exception {
        String sv = v.getStringValue();
        String inferredType = guessValueType(Arrays.asList(sv));
        String dataType = null;
        String ref = valueType.getOtermRef();
        if (ref != null)
            dataType = ontologyData.lookupDataType(ref);
        if (dataType==null) {
            // guess based on values
            if (inferredType.equals("boolean"))
                makeBooleanValue(v);
            else if (inferredType.equals("float") ||
                     (inferredType.equals("int")))
                makeFloatValue(v);
        }
        else {
            if (dataType.equals("boolean")) {
                if (inferredType.equals("boolean"))
                    makeBooleanValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be boolean");
            }
            else if (dataType.equals("int")) {
                if ((inferredType.equals("int")) ||
                    (inferredType.equals("boolean")))
                    makeIntValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be integers");
            }
            else if (dataType.equals("float")) {
                if ((inferredType.equals("float")) ||
                    (inferredType.equals("int")) ||
                    (inferredType.equals("boolean")))
                    makeFloatValue(v);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be numeric");
            }
            else if ((dataType.equals("ref")) &&
                     (v.getScalarType().equals("string"))) {
                if (inferredType.equals("oterm_ref")) {
                    makeOtermRefValue(v);
                    ontologyData.lookupOTerm(v.getOtermRef());
                }
                else if (inferredType.equals("object_ref"))
                    makeObjectRefValue(v,null);
                else
                    throw new Exception("Data for objects of type "+valueType.getTermName()+" must be references");
            }
        }
    }

    /**
       update a pre-mapped term with a mapping (in angle brackets).
       Returns true if it mapped.
    */
    public static boolean mapPremapped(Term t) throws Exception {
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
            String dictName = ontologyData.lookupOTerm(ref);
            if (!ontologyData.matches(ref,name))
                System.out.println("mapping "+name+" to dictionary term "+dictName);
            t.setOtermName(dictName);
            rv = true;
        }
        return rv;
    }

    /**
       update a pre-mapped value with a reference.  Returns true if it mapped.
    */
    public static boolean mapPremapped(Value v) throws Exception {
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
                String dictName = ontologyData.lookupOTerm(ref);
                if (!sv.toLowerCase().equals(dictName.toLowerCase()))
                    System.out.println("mapping "+sv+" to "+dictName);
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
    public static boolean mapPremapped(Values v) throws Exception {
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
                sv = sv.substring(0,pos);
                svs.set(i,sv);
                refs.add(ref);
                if (oTerm) {
                    String dictName = ontologyData.lookupOTerm(ref);
                    if (!sv.toLowerCase().equals(dictName.toLowerCase()))
                        System.out.println("mapping "+sv+" to "+dictName);
                }
            }
            else
                refs.add(null);
        }
        if (rv) {
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
    public static boolean mapPremapped(TypedValue tv) throws Exception {
        boolean rv = false;
        if (tv==null)
            return rv;
        Term t = tv.getValueType();
        rv |= mapPremapped(t);
        Value v = tv.getValue();
        rv |= mapPremapped(v);
        rv |= mapPremapped(tv.getValueUnits());
        if (t != null) 
            transformValue(t, v);
        return rv;
    }

    /**
       Map premapped types in a TypedValues.  Returns true if any mapped.
    */
    public static boolean mapPremapped(TypedValues tvs) throws Exception {
        boolean rv = false;
        if (tvs==null)
            return rv;
        Term t = tvs.getValueType();
        rv |= mapPremapped(t);
        List<TypedValue> vc = tvs.getValueContext();
        if (vc != null) {
            for (TypedValue tv : vc)
                rv |= mapPremapped(tv);
        }
        Values v = tvs.getValues();
        rv |= mapPremapped(v);
        rv |= mapPremapped(tvs.getValueUnits());
        if (t != null)
            transformValues(t, v);
        return rv;
    }

    /**
       Map premapped types in a DimensionContext.  Returns true if any mapped.
    */
    public static boolean mapPremapped(DimensionContext dc) throws Exception {
        boolean rv = false;
        if (dc==null)
            return rv;
        rv |= mapPremapped(dc.getDataType());
        for (TypedValues tvs : dc.getTypedValues())
            rv |= mapPremapped(tvs);
        return rv;
    }
    
    /**
       Map premapped types in NDArray.  Returns true if any mapped.
    */
    public static boolean mapPremapped(NDArray nda) throws Exception {
        boolean rv = false;
        rv |= mapPremapped(nda.getDataType());
        for (DimensionContext dc : nda.getDimContext())
            rv |= mapPremapped(dc);
        List<TypedValue> arrayContext = nda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= mapPremapped(tv);
        }
        rv |= mapPremapped(nda.getTypedValues());
        return rv;
    }

    /**
       Map premapped types in HNDArray.  Returns true if any mapped.
    */
    public static boolean mapPremapped(HNDArray hnda) throws Exception {
        boolean rv = false;
        rv |= mapPremapped(hnda.getDataType());
        for (DimensionContext dc : hnda.getDimContext())
            rv |= mapPremapped(dc);
        List<TypedValue> arrayContext = hnda.getArrayContext();
        if (arrayContext != null) {
            for (TypedValue tv : arrayContext)
                rv |= mapPremapped(tv);
        }
        for (TypedValues tv : hnda.getTypedValues())
            rv |= mapPremapped(tv);
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
                    // use row major order (C-style) to find real index:
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

                    // convert the value back to string, losing the mapped terms
                    val = stripTVS(val);
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
    public static String toString(Term t, int printMode) {
        String rv = t.getTermName();
        if (rv==null)
            rv = t.getOtermName();
        if ((printMode==PRINT_OREF) && (t.getOtermRef() != null))
            rv += " <"+t.getOtermRef()+">";
        return rv;
    }

    /**
       make a Value into a String
    */
    public static String toString(Value v, int printMode) {
        String scalarType = v.getScalarType();
        if (scalarType.equals("int"))
            return new Long(v.getIntValue()).toString();
        else if (scalarType.equals("float"))
            return new Double(v.getFloatValue()).toString();
        else if (scalarType.equals("boolean"))
            return new Long(v.getBooleanValue()).toString();

        String rv = v.getStringValue();
        if (rv==null) {
            if (scalarType.equals("object_ref"))
                rv = v.getObjectRef();
            else if (scalarType.equals("oterm_ref")) {
                rv = v.getOtermRef();
                // translate oterm back to english, if we can:
                if (rv != null) {
                    try {
                        rv = ontologyData.lookupOTerm(rv);
                    }
                    catch (Exception e) {
                        rv = v.getOtermRef();
                    }
                }
            }

        }
            
        if (printMode==PRINT_OREF) {
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
    public static void writeValues(Long prefix, List<Long> dLengths, Values v, int printMode, CSVWriter outfile) {
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
        List objects = getObjects(v);
        List refs = getRefs(v);

        // only refs are available for DC-style objects
        if (objects==null)
            objects = refs;

        // print out the values
        for (int i=0; i<length; i++) {
            if (objects.get(i) != null) {
                if (prefix != null)
                    line.add(prefix.toString());
                for (int j=0; j<nDimensions; j++)
                    line.add(indices[j].toString());
                String s = objects.get(i).toString();
                if ((objects==refs) && (scalarType.equals("oterm_ref"))) {
                    // try to translate oterm back to english
                    if (s != null) {
                        try {
                            s = ontologyData.lookupOTerm(s);
                        }
                        catch (Exception e) {
                            s = objects.get(i).toString();
                        }
                    }
                }
                if ((printMode==PRINT_OREF) &&
                    (refs != null) &&
                    (refs.get(i) != null))
                    s += " <"+refs.get(i).toString()+">";
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
    public static ArrayList<String> toStrings(TypedValue tv, int printMode) {
        ArrayList<String> rv = new ArrayList<String>();
        Term t = tv.getValueType();
        rv.add(toString(t,printMode));
        Value v = tv.getValue();
        rv.add(toString(v,printMode));
        t = tv.getValueUnits();
        if (t != null)
            rv.add(toString(t,printMode));
        return rv;
    }

    /**
       make a TypedValue (including the value) into
       a single descriptive string
    */
    public static String toString(TypedValue tv, int printMode) {
        Term t = tv.getValueType();
        String rv = toString(t,printMode);
        Value v = tv.getValue();
        if (printMode==PRINT_BRIEF)
            rv += "="+toString(v,printMode);
        else
            rv += " = "+toString(v,printMode);
        t = tv.getValueUnits();
        if (t != null) {
            if (printMode==PRINT_BRIEF)
                rv += ", "+toString(t,printMode);
            else
                rv += " ("+toString(t,printMode)+")";
        }
        return rv;
    }

    /**
       make a list of TypedValue objects (including the values) into
       a single descriptive string, separated by commas.  Throws
       error if printMode is PRINT_BRIEF, since reverse parsing will
       be ambiguous.  Throws error if any of the objects have commas
       in the names.
    */
    public static String toString(List<TypedValue> tvl, int printMode) {
        if (printMode==PRINT_BRIEF)
            throw new IllegalArgumentException("printMode cannot be PRINT_BRIEF when printing lists of TypedValue objects");
        String rv = null;
        for (TypedValue tv : tvl) {
            String s = toString(tv, printMode);
            if (s.indexOf(',') > -1)
                throw new IllegalArgumentException("list of TypedValue objects cannot be parsed because term '"+s+"' contains a comma");
            if (rv==null)
                rv = s;
            else
                rv += ", "+s;
        }
        return rv;
    }

    /**
       make a list of lists of TypedValue objects (including the values) into
       a single descriptive string, with the outer list separated by
       semicolons and the inner list separated by commas.  Throws
       error if printMode is PRINT_BRIEF, since reverse parsing will
       be ambiguous.
    */
    public static String toStringLL(List<List<TypedValue>> tvll, int printMode) {
        if (printMode==PRINT_BRIEF)
            throw new IllegalArgumentException("printMode cannot be PRINT_BRIEF when printing lists of TypedValue objects");
        String rv = null;
        for (List<TypedValue> tvl : tvll) {
            String s = toString(tvl, printMode);
            if (s.indexOf(';') > -1)
                throw new IllegalArgumentException("list of lists of TypedValue objects cannot be parsed because list '"+s+"' contains a semicolon");
            if (rv==null)
                rv = s;
            else
                rv += "; "+s;
        }
        return rv;
    }
    
    /**
       make TypedValues metadata (but not the values) into
       an ArrayList of Strings
    */
    public static ArrayList<String> toStrings(TypedValues tvs, int printMode) {
        ArrayList<String> rv = new ArrayList<String>();
        rv.add(toString(tvs.getValueType(),printMode));
        if (tvs.getValueContext() != null)
            for (TypedValue tv : tvs.getValueContext())
                rv.addAll(toStrings(tv,printMode));
        Term t = tvs.getValueUnits();
        if (t != null)
            rv.add(toString(t,printMode));
        return rv;
    }

    /**
       make TypedValues metadata (but not the values) into
       a descriptive String
    */
    public static String toString(TypedValues tvs, int printMode) {
        String rv = toString(tvs.getValueType(),printMode);
        if (tvs.getValueContext() != null)
            for (TypedValue tv : tvs.getValueContext())
                if (printMode==PRINT_BRIEF)
                    rv += ", "+toString(tv,printMode);
                else
                    rv += "; "+toString(tv,printMode);
        Term t = tvs.getValueUnits();
        if (t != null)
            if (printMode==PRINT_BRIEF)
                rv += ", "+toString(t,printMode);
            else
                rv += " ("+toString(t,printMode)+")";
        return rv;
    }
    
    /**
       Writes a HNDArray object to a CSV file.
    */
    public static void writeCSV(HNDArray hnda, int printMode, CSVWriter outfile) throws Exception {
        // check whether HNDArray is really heterogenous
        boolean isHeterogeneous = isHeterogeneous(hnda);

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
            line.add(toString(hnda.getDataType(),printMode));
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }
        if (!isHeterogeneous) {
            line.add("values");
            line.addAll(toStrings(hnda.getTypedValues().get(0),printMode));
            outfile.writeNext(line.toArray(new String[line.size()]),false);
            line.clear();
        }

        // write array metadata
        if (hnda.getArrayContext() != null) {
            for (TypedValue tv : hnda.getArrayContext()) {
                line.add("meta");
                line.addAll(toStrings(tv,printMode));
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
                line.add(toString(dc.getDataType(),printMode));
                line.addAll(toStrings(tvs,printMode));
                outfile.writeNext(line.toArray(new String[line.size()]),false);
                line.clear();
                if ((i==1) && (isHeterogeneous)) {
                    Integer j = 1;
                    // first dimension should encode heterogenous measurements
                    for (TypedValues tvs2 : hnda.getTypedValues()) {
                        line.add(j.toString());
                        line.addAll(toStrings(tvs2,printMode));
                        outfile.writeNext(line.toArray(new String[line.size()]),false);
                        line.clear();
                        j++;
                    }
                }
                else {
                    // first dimension is just values
                    writeValues(null, Arrays.asList(dc.getSize()), tvs.getValues(), printMode, outfile);
                }
            }
            i++;
        }

        line.add("data");
        outfile.writeNext(line.toArray(new String[line.size()]),false);
        line.clear();

        // write out the actual data
        Long prefix = null;
        if (isHeterogeneous) {
            prefix = new Long(0L);
            dLengths.remove(0);
        }
        for (TypedValues tvs : hnda.getTypedValues()) {
            if (prefix != null)
                prefix++; // first dimension will print as 1, not 0
            writeValues(prefix, dLengths, tvs.getValues(), printMode, outfile);
        }
        
        outfile.flush();
    }

    public static String getDescriptor(HNDArray hnda) {
        String typeDescriptor = hnda.getDataType().getOtermName();
        typeDescriptor += " <";
        for (DimensionContext dc : hnda.getDimContext()) {
            if (!typeDescriptor.endsWith(" <")) {
                typeDescriptor += ", ";
            }
            typeDescriptor += dc.getDataType().getOtermName();
        }
        typeDescriptor += ">";
        return typeDescriptor;
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
                fv.add(new Double(Double.parseDouble(val)));
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
                iv.add(new Long((long)Double.parseDouble(val)));
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
        // so usually no need to change those.  Look them up
        // if they're not already there
        List<String> sv = v.getStringValues();
        if (sv == null) {
            List<String> refs = getRefs(v);
            if (scalarType.equals("object_ref"))
                sv = new ArrayList<String>(refs);
            else {
                int l = refs.size();
                sv = new ArrayList<String>(l);
                for (int i=0; i<l; i++) {
                    String s = refs.get(i);
                    if (s != null) {
                        try {
                            s = ontologyData.lookupOTerm(s);
                        }
                        catch (Exception e) {
                            s = refs.get(i);
                        }
                    }
                    sv.add(s);
                }
            }
            v.setStringValues(sv);
        }
        v.setScalarType("string");
    }

    /**
       guess the type of data from a list of strings
    */
    public static String guessValueType(List<String> strings) throws Exception {
        boolean allNumeric = true;
        boolean allBoolean = true;
        boolean allInt = true;
        boolean allOtermRef = true;
        boolean allObjectRef = true;
        int l = strings.size();
        for (int i=0; i<l; i++) {
            String val = strings.get(i);
            if (val != null) {
                allNumeric &= Pattern.matches("^-?\\d+(?:\\.\\d+)?(?:[eE][-+]?\\d+)?$",val);
                allInt &= Pattern.matches("^-?[0-9]+(?:\\.[0-9]+[eE]\\+?[0-9]+)?$",val);
                allBoolean &= Pattern.matches("^[01]$",val);
                allOtermRef &= Pattern.matches("^.+:[0-9]+$",val);
                allObjectRef &= Pattern.matches("^[0-9]+/[0-9]+",val);
            }
        }
        if (allBoolean)
            return "boolean";
        else if (allInt)
            return "int";
        else if (allNumeric)
            return "float";
        else if (allOtermRef)
            return "oterm_ref";
        else if (allObjectRef)
            return "object_ref";
        return "string";
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
        else if ((scalarType.equals("string")) ||
                 (scalarType.equals("oterm_ref")) ||
                 (scalarType.equals("object_ref")))
            rv = v.getStringValues();
        if ((rv==null) && ((scalarType.equals("oterm_ref")) ||
                           (scalarType.equals("object_ref"))))
            rv = getRefs(v); // for DC objects
        return rv;
    }
    
    /**
       Gets list of relevant refs from Values, or null
       if it doesn't have the right scalar type
    */
    public static List getRefs(Values v) {
        String scalarType = v.getScalarType();
        List rv = null;
        if (scalarType.equals("oterm_ref"))
            rv = v.getOtermRefs();
        else if (scalarType.equals("object_ref"))
            rv = v.getObjectRefs();
        return rv;
    }

    /**
       Sets list of relevant objects in Values
    */
    public static void setObjects(Values v, List objects) {
        String scalarType = v.getScalarType();
        if (scalarType.equals("int"))
            v.setIntValues(objects);
        else if (scalarType.equals("float"))
            v.setFloatValues(objects);
        else if (scalarType.equals("boolean"))
            v.setBooleanValues(objects);
        else if ((scalarType.equals("string")) ||
                 (scalarType.equals("oterm_ref")) ||
                 (scalarType.equals("object_ref")))
            v.setStringValues(objects);
    }
    
    /**
       Sets list of relevant refs in Values; does nothing
       if not a reference type
    */
    public static void setRefs(Values v, List refs) {
        String scalarType = v.getScalarType();
        if (scalarType.equals("oterm_ref"))
            v.setOtermRefs(refs);
        else if (scalarType.equals("object_ref"))
            v.setObjectRefs(refs);
    }

    /**
       Copy values, so that items can be removed from lists.
       Does not make deep copy of objects inside of the lists.
    */
    public static Values copyValues(Values v) {
        Values rv = new Values().withScalarType(v.getScalarType());
        List objects = getObjects(v);
        List refs = getRefs(v);
        setObjects(rv,new ArrayList(objects));
        if (refs != null)
            setRefs(rv,new ArrayList(refs));
        return rv;
    }
    
    /**
       return a Values object that only includes unique
       values from another object
    */
    public static Values findUniqueValues(Values v) {
        Values rv = new Values().withScalarType(v.getScalarType());
        List oldObjects = getObjects(v);
        List oldRefs = getRefs(v);
        if (oldObjects==null) // for DC objects pointing to refs or objects
            oldObjects = oldRefs;
        if (oldObjects==null)
            throw new IllegalArgumentException("Values have null objects; scalar type is "+v.getScalarType());
        int n = oldObjects.size();

        List newObjects = new ArrayList();
        List newRefs = new ArrayList();

        HashSet h = new HashSet();
        for (int i=0; i<n; i++) {
            Object o = oldObjects.get(i);
            if (!h.contains(o)) {
                h.add(o);
                newObjects.add(o);
                if (oldRefs != null)
                    newRefs.add(oldRefs.get(i));
            }
        }

        setObjects(rv,newObjects);
        setRefs(rv,newRefs);

        return rv;
    }

    /**
       return a Values object that only includes unique
       values from another object
    */
    public static boolean hasUniqueValues(Values v) {
        Values rv = new Values().withScalarType(v.getScalarType());
        List oldObjects = getObjects(v);
        List oldRefs = getRefs(v);
        if (oldObjects==null) // for DC objects pointing to refs or objects
            oldObjects = oldRefs;
        if (oldObjects==null)
            throw new IllegalArgumentException("Values have null objects; scalar type is "+v.getScalarType());
        int n = oldObjects.size();

        Values uv = findUniqueValues(v);
        List newObjects = getObjects(uv);
        int un = newObjects.size();

        return (n==un);
    }
    
    /**
       return a Values object with one or more dimensions
       fixed.  Non-fixed indices should be left null, and fixed
       indices should be set to the 1-based index to fix.
    */
    public static Values fixValues(Values v,
                                   ArrayList<Long> dLengths,
                                   ArrayList<HashSet<Long>> fixedIndices) {
        Values rv = new Values().withScalarType(v.getScalarType());
        List oldV = getObjects(v);
        List oldRefs = getRefs(v);

        // loop over array to get only the right data
        // this is inefficient and could be made faster if bottleneck!
        List newV = new ArrayList();
        List newRefs = new ArrayList();
        int nDimensions = dLengths.size();
        int length = 1;
        Long[] indices = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++) {
            indices[i] = new Long(1L);
            length *= (int)(dLengths.get(i).longValue());
        }

        // System.out.println("Values: "+v.toString());
        // System.out.println("dLengths: "+dLengths.toString());
        // System.out.println("fixed: "+fixedIndices.toString());
        
        for (int i=0; i<length; i++) {
            boolean keep = true;
            for (int j=0; j<nDimensions; j++)
                if ((fixedIndices.get(j) != null) &&
                    (!fixedIndices.get(j).contains(indices[j]))) {
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
        setObjects(rv,newV);
        setRefs(rv,newRefs);

        return rv;
    }

    /**
       return a Values object with one or more dimensions
       reduced by replacing with an average and standard deviation.
       If Values are a string/oref type, throws error.
       Null values in input are not considered in the calculation.
       SD of one or fewer values is calculated as null.
       Average/SD is effectively the last dimension in the
       returned object; i.e., average and sd are stored adjacent
       to each other in the returned values.
    */
    public static Values averageSDValues(Values v,
                                         List<Long> dLengths,
                                         List<Boolean> avgIndices) {
        String scalarType = v.getScalarType();
        if ((scalarType.equals("string")) ||
            (scalarType.equals("oterm_ref")) ||
            (scalarType.equals("object_ref")))
            throw new IllegalArgumentException("Input values to averageSD must have numeric type");

        Values rv = new Values().withScalarType("float");
        List oldV = getObjects(v);

        // calculate length of values before and after averaging, and
        // set up array of DVectors with the number of avgs and sds to return
        ArrayList<DVector> numbersToAverage = new ArrayList<DVector>();
        int nDimensions = dLengths.size();
        int startLength = 1; // before averaging anything
        int endLength = 1; // matrix size after averaging
        Long[] indices = new Long[nDimensions];
        for (int i=0; i<nDimensions; i++) {
            indices[i] = new Long(1L);
            startLength *= (int)(dLengths.get(i).longValue());
            if (!avgIndices.get(i))
                endLength *= (int)(dLengths.get(i).longValue());
        }
        for (int i=0; i<endLength; i++) {
            DVector dv = new DVector();
            dv.data = new double[0];
            numbersToAverage.add(dv);
        }

        // loop over array to put the right data into the dvectors
        DVector dv = new DVector(1);
        for (int i=0; i<startLength; i++) {
            // calculate which place this will be in the final
            // vector
            int endIndex = 0;
            for (int j=0; j<nDimensions; j++) {
                if (!avgIndices.get(j)) {
                    int l = 1;
                    for (int k=j+1; k<nDimensions; k++)
                        if (!avgIndices.get(k))
                            l *= (int)(dLengths.get(k).longValue());
                    endIndex += (indices[j]-1) * l;
                }
            }

            // System.err.println("averaging, i="+i);
            // System.err.println("endindex="+endIndex);

            // append value to end of DVector, if not null
            Object val = oldV.get(i);
            if (val != null) {
                if (val instanceof Boolean)
                    dv.data[0] = (((Boolean)val).booleanValue() ? 1.0 : 0.0);
                else
                    dv.data[0] = (((Number)val).doubleValue());
                numbersToAverage.get(endIndex).append(dv);
            }

            // increment all indices
            if (i<startLength-1) {
                indices[nDimensions-1]++;
                for (int j=nDimensions-1; j>=0; j--) {
                    if (indices[j] > dLengths.get(j)) {
                        indices[j] = new Long(1L);
                        indices[j-1]++;
                    }
                }
            }
        }

        // calculate all avg and sd, then save in new object
        List<Double> newV = new ArrayList<Double>();
        for (int i=0; i<endLength; i++) {
            dv = numbersToAverage.get(i);
            Double avg = null;
            Double sd = null;
            if (dv.data.length > 0)
                avg = new Double(dv.average());
            if (dv.data.length > 1)
                sd = new Double(dv.stdev());
            newV.add(avg);
            newV.add(sd);
        }
        setObjects(rv,newV);

        return rv;
    }

    /**
       remove values from multiple axes where any values are null
    */
    public static void removeNullValues(List<Values> valuesList) throws Exception{
        int nAxes = valuesList.size();
        
        if (nAxes < 1)
            return;

        ArrayList<List> oldVList = new ArrayList<List>();
        ArrayList<List> oldRList = new ArrayList<List>();
        ArrayList<List> newVList = new ArrayList<List>();
        ArrayList<List> newRList = new ArrayList<List>();

        int nObjects = 0;
        for (int i=0; i<nAxes; i++) {
            List oldV = getObjects(valuesList.get(i));
            oldVList.add(oldV);
            if (i==0)
                nObjects = oldV.size();
            else if (nObjects != oldV.size()) {
                throw new Exception("When removing null values, arrays must be same size; first size is "+nObjects+" and next size is "+oldV.size());
            }
            List oldR = getRefs(valuesList.get(i));
            oldRList.add(oldR);
            List newV = new ArrayList();
            newVList.add(newV);
            List newR = new ArrayList();
            newRList.add(newR);
        }

        for (int i=0; i<nObjects; i++) {
            boolean allPresent = true;
            for (int j=0; j<nAxes; j++) {
                List oldV = oldVList.get(j);
                Object v = oldV.get(i);
                if (v == null) {
                    allPresent = false;
                    j = nAxes;
                }
            }
            if (allPresent) {
                for (int j=0; j<nAxes; j++) {
                    List oldV = oldVList.get(j);
                    List newV = newVList.get(j);
                    List oldR = oldRList.get(j);
                    List newR = newRList.get(j);
                    newV.add(oldV.get(i));
                    if (oldR != null)
                        newR.add(oldR.get(i));
                }
            }
        }

        for (int i=0; i<nAxes; i++) {
            setObjects(valuesList.get(i),newVList.get(i));
            setRefs(valuesList.get(i),newRList.get(i));
        }        
    }

    /**
       Find the indices in a dimension that matches a combination
       of unique value indices.  All values are 1-based.  If not
       all indices are specified (i.e., some are null), then
       multiple indices will be returned.
    */
    public static HashSet<Long> mergeUniqueIndices(DimensionContext dc,
                                                   List<Long> valueIndices) throws Exception {
        // kepp list of possible answers
        HashSet<Long> remainingIndices = new HashSet<Long>();
        int dLength = (int)(dc.getSize().longValue());
        for (int i=0; i<dLength; i++)
            remainingIndices.add(new Long(i+1));
        
        List<TypedValues> tvs = dc.getTypedValues();
        int nIndices = tvs.size();
        if (nIndices != valueIndices.size())
            throw new Exception("to find unique indices for dimension, must fix all value indices, or explicitly mark them null");
        for (int i=0; i<nIndices; i++) {
            if (valueIndices.get(i) != null) { // skip if not fixing this index
                Values v = tvs.get(i).getValues();
                // System.err.println("checking "+toString(tvs.get(i).getValueType(),PRINT_BRIEF));
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
        }

        return remainingIndices;
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
            // System.err.println("checking "+toString(tvs.get(i).getValueType(),PRINT_BRIEF));
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
       checks whether a dimension has pseudo-dimensions, where
       a combination of each variable's unique indices specifies
       each index in the dimension.
    */
    public static boolean hasPseudoDimensions(DimensionContext dc) {
        if (!hasUniqueSubindices(dc))
            return false;
        
        int dLength = (int)(dc.getSize().longValue());
        List<TypedValues> tvs = dc.getTypedValues();
        int nIndices = tvs.size();

        int expectedDLength = 1;

        for (int i=0; i<nIndices; i++) {
            Values v = tvs.get(i).getValues();
            Values uv = findUniqueValues(v);
            int nUnique = getObjects(uv).size();
            expectedDLength *= nUnique;
        }

        if (dLength == expectedDLength)
            return true;
        else
            return false;
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
            else
                ov.add(val);
        }
        v.setStringValues(null);
        v.setOtermRefs(ov);
        v.setScalarType("oterm_ref");
    }
    
    /**
       update a Value object from String to Float
    */
    public static void makeFloatValue(Value v) {
        String sv = v.getStringValue();
        if (sv == null)
            v.setFloatValue(null);
        else
            v.setFloatValue(new Double(Double.parseDouble(sv)));
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
        else
            v.setOtermRef(sv);
        v.setStringValue(null);
        v.setScalarType("oterm_ref");
    }
    
    /**
       Convert a HNDArray to a NDArray
    */
    public static NDArray makeNDArray(HNDArray hnda) throws Exception {
         if (!isHeterogeneous(hnda))
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
       test whether a HNDArray is really heterogenous, or if it's
       save to convert to a NDArray
    */
    public static boolean isHeterogeneous(HNDArray hnda) throws Exception {
        int numHet = hnda.getTypedValues().size();
        return (numHet > 1);
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
}
