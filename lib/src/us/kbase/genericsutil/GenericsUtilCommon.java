package us.kbase.genericsutil;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;
import java.net.URL;

import us.kbase.kbasegenerics.*;
import us.kbase.kbaseontology.OntologyDictionary;

import org.strbio.IO;
import org.strbio.io.*;
import org.strbio.util.*;
import com.fasterxml.jackson.databind.*;

import com.opencsv.*;

/**
   This class implements methods for manipulating Generics that
   are not dependent on other KBase services
*/
public class GenericsUtilCommon {
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
        List objects = getObjects(v);
        List refs = getRefs(v);

        // print out the values
        for (int i=0; i<length; i++) {
            if (objects.get(i) != null) {
                if (prefix != null)
                    line.add(prefix.toString());
                for (int j=0; j<nDimensions; j++)
                    line.add(indices[j].toString());
                String s = objects.get(i).toString();
                if (includeRefs &&
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
        else if ((scalarType.equals("string")) ||
                 (scalarType.equals("oterm_ref")) ||
                 (scalarType.equals("object_ref")))
            rv = v.getStringValues();
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
       return a Values object with one or more dimensions
       fixed.  Non-fixed indices should be left null, and fixed
       indices should be set to the 1-based index to fix.
    */
    public static Values fixValues(Values v,
                                   List<Long> dLengths,
                                   List<Long> fixedIndices) {
        Values rv = new Values().withScalarType(v.getScalarType());
        List oldV = getObjects(v);
        List oldRefs = getRefs(v);

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

        // System.out.println("Values: "+v.toString());
        // System.out.println("dLengths: "+dLengths.toString());
        // System.out.println("fixed: "+fixedIndices.toString());
        
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
        setObjects(rv,newV);
        setRefs(rv,newRefs);

        return rv;
    }

    /**
       remove values from both X and Y axes where X or Y is null
    */
    public static void removeNullValues(Values valuesX, Values valuesY) throws Exception{
        List oldVX = getObjects(valuesX);
        List oldVY = getObjects(valuesY);
        int n = oldVX.size();
        if (n != oldVY.size()) {
            System.out.println("valuesX: "+valuesX.toString());
            System.out.println("valuesY: "+valuesY.toString());
            throw new Exception("When removing null values, X and Y arrays must be same size; X size is "+n+" and Y size is "+oldVY.size());
        }
           
        List oldRX = getRefs(valuesX);
        List oldRY = getRefs(valuesY);
        List newVX = new ArrayList();
        List newVY = new ArrayList();
        List newRX = new ArrayList();
        List newRY = new ArrayList();

        for (int i=0; i<n; i++) {
            Object vx = oldVX.get(i);
            Object vy = oldVY.get(i);
            if ((vx != null) && (vy != null)) {
                newVX.add(vx);
                newVY.add(vy);
                if (oldRX != null)
                    newRX.add(oldRX.get(i));
                if (oldRY != null)
                    newRY.add(oldRY.get(i));
            }
        }

        setObjects(valuesX,newVX);
        setObjects(valuesY,newVY);
        setRefs(valuesX,newRX);
        setRefs(valuesY,newRY);
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
