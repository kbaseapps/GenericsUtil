package genericsutil.test;

import us.kbase.genericsutil.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.*;

import junit.framework.Assert;

import org.ini4j.Ini;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import us.kbase.genericsutil.GenericsUtilServer;
import us.kbase.auth.AuthConfig;
import us.kbase.auth.AuthToken;
import us.kbase.auth.ConfigurableAuthService;
import us.kbase.common.service.JsonServerSyslog;
import us.kbase.common.service.RpcContext;
import us.kbase.common.service.UObject;
import us.kbase.workspace.CreateWorkspaceParams;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.ProvenanceAction;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;
import us.kbase.workspace.WorkspaceIdentity;
import us.kbase.kbaseontology.OntologyDictionary;

import us.kbase.common.service.RpcContext;

public class GenericsUtilServerTest {
    private static AuthToken token = null;
    private static Map<String, String> config = null;
    private static WorkspaceClient wsClient = null;
    private static String wsName = null;
    private static GenericsUtilServer impl = null;
    
    @BeforeClass
    public static void init() throws Exception {
        String configFilePath = System.getenv("KB_DEPLOYMENT_CONFIG");
        File deploy = new File(configFilePath);
        Ini ini = new Ini(deploy);
        config = ini.get("GenericsUtil");
        // Token validation
        String authUrl = config.get("auth-service-url");
        String authUrlInsecure = config.get("auth-service-url-allow-insecure");
        ConfigurableAuthService authService = new ConfigurableAuthService(new AuthConfig().withKBaseAuthServerURL(new URL(authUrl)).withAllowInsecureURLs("true".equals(authUrlInsecure)));
        token = authService.validateToken(System.getenv("KB_AUTH_TOKEN"));
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
       import some pre-mapped growth data
    */
    @Test
    public void testImportGrowthPreMapped() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/20160823-MT123_c_source2_generic.csv"))
            .withObjectName("20160823-MT123_c_source2")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import Premapped Growth test finished");
    }

    /**
       import some growth data
    */
    // @Test
    public void testImportGrowth() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_nitrate_multi.csv"))
            .withObjectName("nitrate_growth")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import Growth test finished");
    }

    /**
       import some growth data
    */
    // @Test
    public void testImportGrowth2() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_nitrate_simple.csv"))
            .withObjectName("nitrate_growth")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import Growth Simple test finished");
    }

    /**
       export some growth data
    */
    // @Test
    public void testExportGrowth2() throws Exception {
        ExportParams params = new ExportParams()
            .withInputRef("14956/7/35");
        ExportResult rv = impl.exportCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Export Growth Simple test finished");
    }

    /**
       list growth data
    */
    // @Test
    public void testListGrowth() throws Exception {
        ListGenericObjectsParams params = new ListGenericObjectsParams()
            .withWorkspaceNames(Arrays.asList("jmc:1480966800200"));
        ListGenericObjectsResult rv = impl.listGenericObjects(params, token, (RpcContext)null);
        for (String id : rv.getObjectIds()) {
            System.out.println("object found: "+id);
        }
        params = new ListGenericObjectsParams()
            .withWorkspaceNames(Arrays.asList("jmc:1480966800200"))
            .withAllowedDataTypes(Arrays.asList("microbial growth"))
            .withAllowedObjectTypes(Arrays.asList("KBaseGenerics.NDArray"))
            .withAllowedScalarTypes(Arrays.asList("float"))
            .withMinDimensions(new Long(2L))
            .withMaxDimensions(new Long(5L))
            .withLimitMapped(new Long(1L));
        rv = impl.listGenericObjects(params, token, (RpcContext)null);
        for (String id : rv.getObjectIds()) {
            System.out.println("growth object found: "+id);
        }
        System.out.println("List growth test finished");
    }

    /**
       test getting generic metadata
    */
    @Test
    public void testGetGenericMetadata() throws Exception {
        GetGenericMetadataParams params = new GetGenericMetadataParams()
            .withObjectIds(Arrays.asList("14956/7/47", "14956/43/15", "14956/45/25"));
        GetGenericMetadataResult rv = impl.getGenericMetadata(params, token, (RpcContext)null);
        System.out.println(rv.toString());
        System.out.println("Get metadata test finished");
    }

    /**
       test getting axis labels
    */
    @Test
    public void testGetGenericDimensionLabels() throws Exception {
        GetGenericDimensionLabelsParams params = new GetGenericDimensionLabelsParams()
            .withObjectId("14956/45/25")
            .withConvertToString(new Long(1L))
            .withDimensionIds(Arrays.asList("1/1","2/3"));
        GetGenericDimensionLabelsResult rv = impl.getGenericDimensionLabels(params, token, (RpcContext)null);
        System.out.println(rv.toString());
        System.out.println("Get labels test finished");
    }
    
    /**
       import some fitness data
    */
    // @Test
    public void testImportFitness() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_jw710_random.csv"))
            .withObjectName("fitness_jw710")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import simple fitness test finished");
    }

    /**
       import some small fitness data
    */
    // @Test
    public void testImportFitness2() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_jw710_random_small.csv"))
            .withObjectName("fitness_jw710_small")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import small fitness test finished");
    }
    
    /**
       import some multi-strain fitness data
    */
    // @Test
    public void testImportFitness3() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_multi_random.csv"))
            .withObjectName("fitness_multiple_dvh_strains")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import multi-strain fitness test finished");
    }

    /**
       import some enzyme activity data
    */
    // @Test
    public void testImportEA() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/activity_single.csv"))
            .withObjectName("enzyme_activity_dvh")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import enzyme activity test finished");
    }

    /**
       import some enzyme activity data, replicates
    */
    // @Test
    public void testImportEA2() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/activity_replicates.csv"))
            .withObjectName("enzyme_activity_dvh_replicates")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import enzyme activity replicates test finished");
    }

    /**
       import some enzyme activity data, statistics
    */
    // @Test
    public void testImportEA3() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/activity_statistics.csv"))
            .withObjectName("enzyme_activity_dvh_stats")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import enzyme activity statistics test finished");
    }

    /**
       import some taxonomic data
    */
    // @Test    
    public void testImportTax() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/otu_abundance.csv"))
            .withObjectName("taxonomic_abundance")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import taxonomic data test finished");
    }

    /**
       import some taxonomic data
    */
    // @Test
    public void testImportTax2() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/otu_abundance_multiwell.csv"))
            .withObjectName("taxonomic_abundance_multiwell")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import multiwell taxonomic data test finished");
    }

    /**
       import some taxonomic data
    */
    // @Test
    public void testImportTax3() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/otu_abundance_timeseries.csv"))
            .withObjectName("taxonomic_abundance_timeseries")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import time series taxonomic data test finished");
    }

    /**
       import some environmental parameters data
    */
    @Test
    public void testImportEP() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/heterogenous_sampling.csv"))
            .withObjectName("heterogenous_sampling")
            .withObjectType("KBaseGenerics.HNDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import heterogenous environmental parameters data test finished");
    }
    
    /**
       import some bad data
    */
    // @Test(expected=Exception.class)
    public void testBad1() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_typed_bad1.csv"))
            .withObjectName("bad_growth_data_1")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.fail("Should have thrown exception");
    }
    
    /**
       import some bad data, 2
    */
    // @Test(expected=Exception.class)
    public void testBad2() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_typed_bad2.csv"))
            .withObjectName("bad_growth_data_2")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.fail("Should have thrown exception");
    }
    
    /**
       import some bad data, 3
    */
    // @Test(expected=Exception.class)
    public void testBad3() throws Exception {
        ImportCSVParams params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_typed_bad3.csv"))
            .withObjectName("bad_growth_data_3")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.fail("Should have thrown exception");
    }

    /**
       import some ontology dictionaries
    */
    // @Test
    public void testImportDictionaries() throws Exception {
        ImportOBOParams params = new ImportOBOParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/data_type_ontology.obo"))
            .withObjectName("data_type_ontology")
            .withWorkspaceName("jmc:1480966800200");
        ImportResult rv = impl.importObo(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);

        params = new ImportOBOParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/context_measurement_ontology.obo"))
            .withObjectName("context_measurement_ontology")
            .withWorkspaceName("jmc:1480966800200");
        rv = impl.importObo(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import dictionaries data test finished");
    }
    
    /**
       import some pre-typemapped data
    */
    // @Test
    public void testImportMapped() throws Exception {
        ImportCSVParams params = null;
        ImportResult rv = null;

        /*
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_typed.csv"))
            .withObjectName("m1")
            .withObjectType("KBaseGenerics.Matrix2D")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/growth_typed_2.csv"))
            .withObjectName("m2")
            .withObjectType("KBaseGenerics.Matrix2D")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        */
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_typed_small.csv"))
            .withObjectName("Shewanella_fitness_2D")
            .withObjectType("KBaseGenerics.Matrix2D")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_typed_small.csv"))
            .withObjectName("Shewanella_fitness_ND")
            .withObjectType("KBaseGenerics.NDArray")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/fitness_typed_small_2.csv"))
            .withObjectName("Shewanella_fitness_1_condition")
            .withObjectType("KBaseGenerics.Array")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/otu_abundance_typed.csv"))
            .withObjectName("Taxonomy_one_sample")
            .withObjectType("KBaseGenerics.Array")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        params = new ImportCSVParams()
            .withFile(new us.kbase.genericsutil.File().withPath("/kb/module/test/data/otu_abundance_multiwell_typed.csv"))
            .withObjectName("Taxonomy_multiple_samples")
            .withObjectType("KBaseGenerics.Matrix2D")
            .withWorkspaceName("psnovichkov:1480537596117");
        rv = impl.importCsv(params, token, (RpcContext)null);
        Assert.assertNotNull(rv);
        System.out.println("Import typed data test finished");
    }
}
