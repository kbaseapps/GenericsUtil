/*
  A KBase module: GenericsUtil.  Utilities for manipulating
  Generic objects.
*/

module GenericsUtil {
    typedef int boolean;

    /*
    Values object from KBaseGenerics.spec; for some reason,
    #include did not work here:
    */
    typedef string data_type;
    typedef string oterm_ref;
    typedef string object_ref;
    typedef structure{
        data_type   scalar_type;

        list<object_ref>  object_refs;
        list<oterm_ref>   oterm_refs;
	list<int>         int_values;
	list<float>       float_values;
        list<boolean>     boolean_values;
        list<string>      string_values;
    } Values;

    /*
    Import a CSV file into a NDArray or HNDArray.

    "File" and "usermeta" are common to all import methods.
    */
    typedef structure {
        string path;
        string shock_id;
    } File;

    typedef mapping<string, string> usermeta;

    typedef structure {
	File file;

        string workspace_name;
        string object_name;
	string object_type;

        usermeta metadata;
    } ImportCSVParams;

    typedef structure {
        string object_ref;
    } ImportResult;

    funcdef import_csv(ImportCSVParams params)
      returns (ImportResult result) authentication required;


    /*
    Import an OBO file into an OntologyDictionary
    */
    typedef structure {
	File file;

        string workspace_name;
        string object_name;

        usermeta metadata;
    } ImportOBOParams;

    funcdef import_obo(ImportOBOParams params)
      returns (ImportResult result) authentication required;


    /*
    Exporter for generic objects as CSV files
    */
    typedef structure {
        string input_ref;
    } ExportParams;

    typedef structure {
        string shock_id;
    } ExportResult;

    funcdef export_csv(ExportParams params)
      returns (ExportResult result) authentication required;


    /*
    List generic objects in one or more workspaces
    
    optional parameters:
    allowed_object_types - limits to specific types of object, e.g., KBaseGenerics.NDArray (version number is optional)
    allowed_data_types - limits to specific data types, e.g., microbial growth
    allowed_scalar_types - limits to specific scalar types, e.g., object_ref, int, float (see KBaseGenerics.spec for valid types).  HNDArrays must have at least one dimension that passes.
    min_dimensions - limits to generics with minimum number of dimensions
    max_dimensions - limits to generics with max number of dimensions
    limit_mapped - if 0 (or unset) returns all objects.  if 1, returns only mapped objects.  if 2, returns only umapped objects
    */
    typedef structure {
	list<string> workspace_names;
	list<string> allowed_object_types;
	list<string> allowed_data_types;
	list<string> allowed_scalar_types;
	int min_dimensions;
	int max_dimensions;
	int limit_mapped;
    } ListGenericObjectsParams;

    typedef structure {
	list<string> object_ids;
    } ListGenericObjectsResult;

    funcdef list_generic_objects(ListGenericObjectsParams params)
      returns (ListGenericObjectsResult result) authentication required;


    /*
    Get metadata describing the dimensions of one or more generic objects
    */
    typedef structure {
        list<string> object_ids;
    } GetGenericMetadataParams;

    /*
    Basic metadata about an object:

    object_type - e.g., KBaseGenerics.HNDArray‑4.0
    data_type - e.g., microbial growth
    n_dimensions - number of dimensions
    is_mapped - 0 or 1 indicating mapped status
    value_types - list of value types in the object (there will only be 1 for NDArray objects), e.g., "specific activity"
    scalar_types - list of scalar types in the object (there will only be 1 for NDArray objects), e.g., "float"
    dimension_types - a string describing each dimension (e.g., "media name")
    dimension_sizes - size (length) of each dimension
    dimension_value_types - a string describing each context of each dimension (e.g., "media name")
    dimension_scalar_types - type of values in each context of each dimension (e.g., "int")
    */
    typedef structure {
	string object_type;
	string data_type;
	int n_dimensions;
	boolean is_mapped;
	list<string> value_types;
	list<string> scalar_types;
	list<string> dimension_types;
	list<int> dimension_sizes;
	list<list<string>> dimension_value_types;
	list<list<string>> dimension_scalar_types;
    } GenericMetadata;

    /*
    maps object ids to structure with metadata
    */
    typedef structure {
        mapping<string,GenericMetadata> object_info;
    } GetGenericMetadataResult;

    funcdef get_generic_metadata(GetGenericMetadataParams params)
      returns (GetGenericMetadataResult result) authentication required;


    /*
    gets labels for list of dimension axes for a generic object.

    User will pass in the numeric indices of all dimensions they care
    about (e.g., 1/1 will mean 1st dimension, 1st data type, 2/1 = 2nd
    dimension, 1st data type), and an optional flag, convert_to_string.
    The API will return a hash mapping each of the dimension indices to
    a Values object.  The Values will either contain the scalar type in
    the original format, or if the convert_to_string flag is set, will
    convert the scalar type to strings.
    */
    typedef structure {
	string object_id;
        list<string> dimension_ids;
	boolean convert_to_string;
    } GetGenericDimensionLabelsParams;

    typedef structure {
        mapping<string,Values> dimension_labels;
    } GetGenericDimensionLabelsResult;

    funcdef get_generic_dimension_labels(GetGenericDimensionLabelsParams params)
      returns (GetGenericDimensionLabelsResult result) authentication required;


    /*
    gets subset of generic data as a 2D matrix

    Users will pass in the dimension indices to use as variables (1st
    is X axis; 2nd is Y axis), and which dimension indices to fix to
    particular constants (indicated as a 1-based index into the list
    of dimension labels in tha dimension), and the method will select
    out the subset of data from the generic object and return a 2D
    array of numeric data.
    */
    typedef structure {
	string object_id;
        list<string> variable_dimension_ids;
	mapping<string,int> constant_dimension_ids;
    } GetGenericDataParams;

    typedef structure {
        list<list<float>> data_2d_float;
    } GetGenericDataResult;

    funcdef get_generic_data(GetGenericDataParams params)
      returns (GetGenericDataResult result) authentication required;
};
