package genericsutil.test;

import genericsutil.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.ini4j.Ini;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import genericsutil.GenericsUtilServer;
import us.kbase.auth.AuthToken;
import us.kbase.auth.AuthService;
import us.kbase.common.service.JsonServerSyslog;
import us.kbase.common.service.RpcContext;
import us.kbase.common.service.UObject;
import us.kbase.workspace.CreateWorkspaceParams;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.ProvenanceAction;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;
import us.kbase.workspace.WorkspaceIdentity;

import us.kbase.common.service.RpcContext;

public class GenericsUtilServerTest {
    private static AuthToken token = null;
    private static Map<String, String> config = null;
    private static WorkspaceClient wsClient = null;
    private static String wsName = null;
    private static GenericsUtilServer impl = null;
    
    @BeforeClass
    public static void init() throws Exception {
        //TODO AUTH make configurable?
        // token = AuthService.validateToken(System.getenv("KB_AUTH_TOKEN"));
        token = new AuthToken(System.getenv("KB_AUTH_TOKEN"));
        String configFilePath = System.getenv("KB_DEPLOYMENT_CONFIG");
        File deploy = new File(configFilePath);
        Ini ini = new Ini(deploy);
        config = ini.get("GenericsUtil");
        wsClient = new WorkspaceClient(new URL(config.get("workspace-url")), token);
        wsClient.setAuthAllowedForHttp(true);
        // These lines are necessary because we don't want to start linux syslog bridge service
        JsonServerSyslog.setStaticUseSyslog(false);
        JsonServerSyslog.setStaticMlogFile(new File(config.get("scratch"), "test.log").getAbsolutePath());
        impl = new GenericsUtilServer();
    }
    
    private static String getWsName() throws Exception {
        if (wsName == null) {
            long suffix = System.currentTimeMillis();
            wsName = "test_GenericsUtil_" + suffix;
            wsClient.createWorkspace(new CreateWorkspaceParams().withWorkspace(wsName));
        }
        return wsName;
    }
    
    private static RpcContext getContext() {
        return new RpcContext().withProvenance(Arrays.asList(new ProvenanceAction()
            .withService("GenericsUtil").withMethod("please_never_use_it_in_production")
            .withMethodParams(new ArrayList<UObject>())));
    }
    
    @AfterClass
    public static void cleanup() {
        if (wsName != null) {
            try {
                wsClient.deleteWorkspace(new WorkspaceIdentity().withWorkspace(wsName));
                System.out.println("Test workspace was deleted");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    

    /**
       import some growth data
    */
    public void testImportGrowth() throws Exception {
        ImportNDArrayCSV params = new ImportNDArrayCSV()
            .withFile(new genericsutil.File().withPath("/kb/module/test/data/GrowthMatrix_nitrate.csv"))
            .withMatrixName("nitrate_growth")
            .withWorkspaceName("jmc:1480966800200");
        ImportNDArrayResult rv = impl.importNdarrayCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import Growth test finished");
    }

    /**
       import some fitness data
    */
    @Test
    public void testImportFitness() throws Exception {
        ImportNDArrayCSV params = new ImportNDArrayCSV()
            .withFile(new genericsutil.File().withPath("/kb/module/test/data/Fitness_jw710.csv"))
            .withMatrixName("fitness_jw710")
            .withWorkspaceName("jmc:1480966800200");
        ImportNDArrayResult rv = impl.importNdarrayCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import simple fitness test finished");
    }

    /**
       import some multi-strain fitness data
    */
    @Test
    public void testImportFitness2() throws Exception {
        ImportNDArrayCSV params = new ImportNDArrayCSV()
            .withFile(new genericsutil.File().withPath("/kb/module/test/data/Fitness_multi.csv"))
            .withMatrixName("fitness_multiple_dvh_strains")
            .withWorkspaceName("jmc:1480966800200");
        ImportNDArrayResult rv = impl.importNdarrayCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import multi-strain fitness test finished");
    }
}
