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
    private static final String gitCommitHash = "5d6a6313ecf4461836d82b9264708c1c4fbadbcf";

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
     * @return   parameter "result" of type {@link genericsutil.ImportCSVResult ImportCSVResult}
     */
    @JsonServerMethod(rpc = "GenericsUtil.import_csv", async=true)
    public ImportCSVResult importCsv(ImportCSVParams params, AuthToken authPart, RpcContext jsonRpcContext) throws Exception {
        ImportCSVResult returnVal = null;
        //BEGIN import_csv
        returnVal = GenericsUtilImpl.importCSV(wsUrl,
                                               shockUrl,
                                               authPart,
                                               params);
        //END import_csv
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
