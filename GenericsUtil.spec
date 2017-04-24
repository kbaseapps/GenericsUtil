/*
A KBase module: GenericsUtil
*/

module GenericsUtil {
    typedef structure {
        string path;
        string shock_id;
    } File;

    typedef mapping<string, string> usermeta;


    /*
    Import a CSV file into a NDArray or HNDArray
    */
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
};
