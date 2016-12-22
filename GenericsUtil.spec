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
    } ImportDataMatrixCSV;

    typedef structure {
        string matrix_ref;
    } ImportDataMatrixResult;

    funcdef import_data_matrix_csv(ImportDataMatrixCSV params)
            returns (ImportDataMatrixResult result) authentication required;
};
