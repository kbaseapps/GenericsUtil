/*
A KBase module: GenericsUtil
*/

module GenericsUtil {
    typedef int boolean;

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
};
