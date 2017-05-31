package genericsutil;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;
import us.kbase.common.service.JsonServerSyslog;
import us.kbase.common.service.RpcContext;

//BEGIN_HEADER
//END_HEADER

/**
 * <p>Original spec-file module name: GenericsUtil</p>
 * <pre>
 * A KBase module: GenericsUtil.  Utilities for manipulating
 * Generic objects.
 * </pre>
 */
public class GenericsUtilServer extends JsonServerServlet {
    private static final long serialVersionUID = 1L;
    private static final String version = "0.0.1";
    private static final String gitUrl = "git@github.com:kbaseapps/GenericsUtil.git";
    private static final String gitCommitHash = "c5fc1a680de2162087ef827d7a24b39ee660406b";

    //BEGIN_CLASS_HEADER
    private final String wsUrl;
    private final String shockUrl;
    private final String serviceWizardUrl;
    //END_CLASS_HEADER

    public GenericsUtilServer() throws Exception {
        super("GenericsUtil");
        //BEGIN_CONSTRUCTOR
        wsUrl = config.get("workspace-url");
        shockUrl = config.get("shock-url");
        String x = config.get("service-wizard-url");
        if ((x==null) || (!x.startsWith("http")))
            serviceWizardUrl = "https://ci.kbase.us/services/service_wizard";
        else
            serviceWizardUrl = x;
        //END_CONSTRUCTOR
    }

    /**
     * <p>Original spec-file function name: import_csv</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.ImportCSVParams ImportCSVParams}
     * @return   parameter "result" of type {@link genericsutil.ImportResult ImportResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.import_csv", async=true)
    public ImportResult importCsv(ImportCSVParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ImportResult returnVal = null;
        //BEGIN import_csv
        returnVal = GenericsUtilImpl.importCSV(wsUrl,
                                               shockUrl,
                                               authPart,
                                               params);
        //END import_csv
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: import_obo</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.ImportOBOParams ImportOBOParams}
     * @return   parameter "result" of type {@link genericsutil.ImportResult ImportResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.import_obo", async=true)
    public ImportResult importObo(ImportOBOParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ImportResult returnVal = null;
        //BEGIN import_obo
        returnVal = GenericsUtilImpl.importOBO(wsUrl,
                                               shockUrl,
                                               authPart,
                                               params);
        //END import_obo
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: export_csv</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.ExportParams ExportParams}
     * @return   parameter "result" of type {@link genericsutil.ExportResult ExportResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.export_csv", async=true)
    public ExportResult exportCsv(ExportParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ExportResult returnVal = null;
        //BEGIN export_csv
        returnVal = GenericsUtilImpl.exportCSV(wsUrl,
                                               shockUrl,
                                               authPart,
                                               params);
        //END export_csv
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: list_generic_objects</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.ListGenericObjectsParams ListGenericObjectsParams}
     * @return   parameter "result" of type {@link genericsutil.ListGenericObjectsResult ListGenericObjectsResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.list_generic_objects", async=true)
    public ListGenericObjectsResult listGenericObjects(ListGenericObjectsParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ListGenericObjectsResult returnVal = null;
        //BEGIN list_generic_objects
        returnVal = GenericsUtilImpl.listGenericObjects(wsUrl,
                                                        authPart,
                                                        params);
        //END list_generic_objects
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: get_generic_metadata</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.GetGenericMetadataParams GetGenericMetadataParams}
     * @return   parameter "result" of type {@link genericsutil.GetGenericMetadataResult GetGenericMetadataResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.get_generic_metadata", async=true)
    public GetGenericMetadataResult getGenericMetadata(GetGenericMetadataParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        GetGenericMetadataResult returnVal = null;
        //BEGIN get_generic_metadata
        returnVal = GenericsUtilImpl.getGenericMetadata(wsUrl,
                                                        authPart,
                                                        params);
        //END get_generic_metadata
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: get_generic_dimension_labels</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.GetGenericDimensionLabelsParams GetGenericDimensionLabelsParams}
     * @return   parameter "result" of type {@link genericsutil.GetGenericDimensionLabelsResult GetGenericDimensionLabelsResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.get_generic_dimension_labels", async=true)
    public GetGenericDimensionLabelsResult getGenericDimensionLabels(GetGenericDimensionLabelsParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        GetGenericDimensionLabelsResult returnVal = null;
        //BEGIN get_generic_dimension_labels
        //END get_generic_dimension_labels
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: get_generic_data</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.GetGenericDataParams GetGenericDataParams}
     * @return   parameter "result" of type {@link genericsutil.GetGenericDataResult GetGenericDataResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.get_generic_data", async=true)
    public GetGenericDataResult getGenericData(GetGenericDataParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        GetGenericDataResult returnVal = null;
        //BEGIN get_generic_data
        //END get_generic_data
        return returnVal;
    }
    @JsonServerMethod(rpc = "GenericsUtil.status")
    public Map<String, Object> status() {
        Map<String, Object> returnVal = null;
        //BEGIN_STATUS
        returnVal = new LinkedHashMap<String, Object>();
        returnVal.put("state", "OK");
        returnVal.put("message", "");
        returnVal.put("version", version);
        returnVal.put("git_url", gitUrl);
        returnVal.put("git_commit_hash", gitCommitHash);
        //END_STATUS
        return returnVal;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            new GenericsUtilServer().startupServer(Integer.parseInt(args[0]));
        } else if (args.length == 3) {
            JsonServerSyslog.setStaticUseSyslog(false);
            JsonServerSyslog.setStaticMlogFile(args[1] + ".log");
            new GenericsUtilServer().processRpcCall(new File(args[0]), new File(args[1]), args[2]);
        } else {
            System.out.println("Usage: <program> <server_port>");
            System.out.println("   or: <program> <context_json_file> <output_json_file> <token>");
            return;
        }
    }
}
