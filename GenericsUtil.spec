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
    matrix_name - name of object
    workspace_name - workspace it gets saved to
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
    } ImportCSVResult;

    funcdef import_csv(ImportCSVParams params)
      returns (ImportCSVResult result) authentication required;
};
