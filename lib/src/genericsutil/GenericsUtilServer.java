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
 * A KBase module: GenericsUtil
 * </pre>
 */
public class GenericsUtilServer extends JsonServerServlet {
    private static final long serialVersionUID = 1L;
    private static final String version = "0.0.1";
    private static final String gitUrl = "git@github.com:kbaseapps/GenericsUtil.git";
    private static final String gitCommitHash = "b81e47fa3f16bb157865c16f5710c5095b29fc64";

    //BEGIN_CLASS_HEADER
    //END_CLASS_HEADER

    public GenericsUtilServer() throws Exception {
        super("GenericsUtil");
        //BEGIN_CONSTRUCTOR
        //END_CONSTRUCTOR
    }

    /**
     * <p>Original spec-file function name: import_data_matrix_csv</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link genericsutil.ImportDataMatrixCSV ImportDataMatrixCSV}
     * @return   parameter "result" of type {@link genericsutil.ImportDataMatrixResult ImportDataMatrixResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.import_data_matrix_csv", async=true)
    public ImportDataMatrixResult importDataMatrixCsv(ImportDataMatrixCSV params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ImportDataMatrixResult returnVal = null;
        //BEGIN import_data_matrix_csv
        //END import_data_matrix_csv
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
