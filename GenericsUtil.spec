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

        string matrix_name;
        string workspace_name;

        usermeta metadata;
    } ImportNDArrayCSV;

    typedef structure {
        string matrix_ref;
    } ImportNDArrayResult;

    funcdef import_ndarray_csv(ImportNDArrayCSV params)
      returns (ImportNDArrayResult result) authentication required;
};
