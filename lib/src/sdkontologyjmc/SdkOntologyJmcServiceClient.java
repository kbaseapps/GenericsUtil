package sdkontologyjmc;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonClientCaller;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.RpcContext;
import us.kbase.common.service.UnauthorizedException;

/**
 * <p>Original spec-file module name: sdk_ontology_jmc</p>
 * <pre>
 * A KBase module: sdk_ontology_jmc
 * </pre>
 */
public class SdkOntologyJmcServiceClient {
    private JsonClientCaller caller;
    private String serviceVersion = "dev";
    private static URL DEFAULT_URL = null;
    static {
        try {
            DEFAULT_URL = new URL("https://kbase.us/services/service_wizard");
        } catch (MalformedURLException mue) {
            throw new RuntimeException("Compile error in client - bad url compiled");
        }
    }

    /** Constructs a client with the default url and no user credentials.*/
    public SdkOntologyJmcServiceClient() {
       caller = new JsonClientCaller(DEFAULT_URL);
        caller.setDynamic(true);
    }


    /** Constructs a client with a custom URL and no user credentials.
     * @param url the URL of the service.
     */
    public SdkOntologyJmcServiceClient(URL url) {
        caller = new JsonClientCaller(url);
        caller.setDynamic(true);
    }
    /** Constructs a client with a custom URL.
     * @param url the URL of the service.
     * @param token the user's authorization token.
     * @throws UnauthorizedException if the token is not valid.
     * @throws IOException if an IOException occurs when checking the token's
     * validity.
     */
    public SdkOntologyJmcServiceClient(URL url, AuthToken token) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, token);
        caller.setDynamic(true);
    }

    /** Constructs a client with a custom URL.
     * @param url the URL of the service.
     * @param user the user name.
     * @param password the password for the user name.
     * @throws UnauthorizedException if the credentials are not valid.
     * @throws IOException if an IOException occurs when checking the user's
     * credentials.
     */
    public SdkOntologyJmcServiceClient(URL url, String user, String password) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, user, password);
        caller.setDynamic(true);
    }

    /** Constructs a client with a custom URL
     * and a custom authorization service URL.
     * @param url the URL of the service.
     * @param user the user name.
     * @param password the password for the user name.
     * @param auth the URL of the authorization server.
     * @throws UnauthorizedException if the credentials are not valid.
     * @throws IOException if an IOException occurs when checking the user's
     * credentials.
     */
    public SdkOntologyJmcServiceClient(URL url, String user, String password, URL auth) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, user, password, auth);
        caller.setDynamic(true);
    }

    /** Constructs a client with the default URL.
     * @param token the user's authorization token.
     * @throws UnauthorizedException if the token is not valid.
     * @throws IOException if an IOException occurs when checking the token's
     * validity.
     */
    public SdkOntologyJmcServiceClient(AuthToken token) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(DEFAULT_URL, token);
        caller.setDynamic(true);
    }

    /** Constructs a client with the default URL.
     * @param user the user name.
     * @param password the password for the user name.
     * @throws UnauthorizedException if the credentials are not valid.
     * @throws IOException if an IOException occurs when checking the user's
     * credentials.
     */
    public SdkOntologyJmcServiceClient(String user, String password) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(DEFAULT_URL, user, password);
        caller.setDynamic(true);
    }

    /** Get the token this client uses to communicate with the server.
     * @return the authorization token.
     */
    public AuthToken getToken() {
        return caller.getToken();
    }

    /** Get the URL of the service with which this client communicates.
     * @return the service URL.
     */
    public URL getURL() {
        return caller.getURL();
    }

    /** Set the timeout between establishing a connection to a server and
     * receiving a response. A value of zero or null implies no timeout.
     * @param milliseconds the milliseconds to wait before timing out when
     * attempting to read from a server.
     */
    public void setConnectionReadTimeOut(Integer milliseconds) {
        this.caller.setConnectionReadTimeOut(milliseconds);
    }

    /** Check if this client allows insecure http (vs https) connections.
     * @return true if insecure connections are allowed.
     */
    public boolean isInsecureHttpConnectionAllowed() {
        return caller.isInsecureHttpConnectionAllowed();
    }

    /** Deprecated. Use isInsecureHttpConnectionAllowed().
     * @deprecated
     */
    public boolean isAuthAllowedForHttp() {
        return caller.isAuthAllowedForHttp();
    }

    /** Set whether insecure http (vs https) connections should be allowed by
     * this client.
     * @param allowed true to allow insecure connections. Default false
     */
    public void setIsInsecureHttpConnectionAllowed(boolean allowed) {
        caller.setInsecureHttpConnectionAllowed(allowed);
    }

    /** Deprecated. Use setIsInsecureHttpConnectionAllowed().
     * @deprecated
     */
    public void setAuthAllowedForHttp(boolean isAuthAllowedForHttp) {
        caller.setAuthAllowedForHttp(isAuthAllowedForHttp);
    }

    /** Set whether all SSL certificates, including self-signed certificates,
     * should be trusted.
     * @param trustAll true to trust all certificates. Default false.
     */
    public void setAllSSLCertificatesTrusted(final boolean trustAll) {
        caller.setAllSSLCertificatesTrusted(trustAll);
    }
    
    /** Check if this client trusts all SSL certificates, including
     * self-signed certificates.
     * @return true if all certificates are trusted.
     */
    public boolean isAllSSLCertificatesTrusted() {
        return caller.isAllSSLCertificatesTrusted();
    }
    /** Sets streaming mode on. In this case, the data will be streamed to
     * the server in chunks as it is read from disk rather than buffered in
     * memory. Many servers are not compatible with this feature.
     * @param streamRequest true to set streaming mode on, false otherwise.
     */
    public void setStreamingModeOn(boolean streamRequest) {
        caller.setStreamingModeOn(streamRequest);
    }

    /** Returns true if streaming mode is on.
     * @return true if streaming mode is on.
     */
    public boolean isStreamingModeOn() {
        return caller.isStreamingModeOn();
    }

    public void _setFileForNextRpcResponse(File f) {
        caller.setFileForNextRpcResponse(f);
    }

    public String getServiceVersion() {
        return this.serviceVersion;
    }

    public void setServiceVersion(String newValue) {
        this.serviceVersion = newValue;
    }

    /**
     * <p>Original spec-file function name: list_ontology_terms</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link sdkontologyjmc.ListOntologyTermsParams ListOntologyTermsParams}
     * @return   parameter "output" of type {@link sdkontologyjmc.OntologyTermsOut OntologyTermsOut}
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public OntologyTermsOut listOntologyTerms(ListOntologyTermsParams params, RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<OntologyTermsOut>> retType = new TypeReference<List<OntologyTermsOut>>() {};
        List<OntologyTermsOut> res = caller.jsonrpcCall("sdk_ontology_jmc.list_ontology_terms", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: ontology_overview</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link sdkontologyjmc.OntologyOverviewParams OntologyOverviewParams}
     * @return   parameter "output" of type {@link sdkontologyjmc.OntologyOverviewOut OntologyOverviewOut}
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public OntologyOverviewOut ontologyOverview(OntologyOverviewParams params, RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<OntologyOverviewOut>> retType = new TypeReference<List<OntologyOverviewOut>>() {};
        List<OntologyOverviewOut> res = caller.jsonrpcCall("sdk_ontology_jmc.ontology_overview", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: list_public_ontologies</p>
     * <pre>
     * </pre>
     * @return   instance of original type "public_ontologies" (List public ontologies) &rarr; list of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public List<String> listPublicOntologies(RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        TypeReference<List<List<String>>> retType = new TypeReference<List<List<String>>>() {};
        List<List<String>> res = caller.jsonrpcCall("sdk_ontology_jmc.list_public_ontologies", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: list_public_translations</p>
     * <pre>
     * </pre>
     * @return   instance of original type "public_translations" (List public translations) &rarr; list of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public List<String> listPublicTranslations(RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        TypeReference<List<List<String>>> retType = new TypeReference<List<List<String>>>() {};
        List<List<String>> res = caller.jsonrpcCall("sdk_ontology_jmc.list_public_translations", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: get_ontology_terms</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link sdkontologyjmc.GetOntologyTermsParams GetOntologyTermsParams}
     * @return   parameter "output" of type {@link sdkontologyjmc.GetOntologyTermsOut GetOntologyTermsOut}
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public GetOntologyTermsOut getOntologyTerms(GetOntologyTermsParams params, RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<GetOntologyTermsOut>> retType = new TypeReference<List<GetOntologyTermsOut>>() {};
        List<GetOntologyTermsOut> res = caller.jsonrpcCall("sdk_ontology_jmc.get_ontology_terms", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: get_equivalent_terms</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link sdkontologyjmc.GetEqTermsParams GetEqTermsParams}
     * @return   parameter "output" of type {@link sdkontologyjmc.GetEqTermsOut GetEqTermsOut}
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public GetEqTermsOut getEquivalentTerms(GetEqTermsParams params, RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<GetEqTermsOut>> retType = new TypeReference<List<GetEqTermsOut>>() {};
        List<GetEqTermsOut> res = caller.jsonrpcCall("sdk_ontology_jmc.get_equivalent_terms", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    /**
     * <p>Original spec-file function name: annotationtogo</p>
     * <pre>
     * </pre>
     * @param   params   instance of type {@link sdkontologyjmc.ElectronicAnnotationParams ElectronicAnnotationParams}
     * @return   parameter "output" of type {@link sdkontologyjmc.ElectronicAnnotationResults ElectronicAnnotationResults}
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public ElectronicAnnotationResults annotationtogo(ElectronicAnnotationParams params, RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(params);
        TypeReference<List<ElectronicAnnotationResults>> retType = new TypeReference<List<ElectronicAnnotationResults>>() {};
        List<ElectronicAnnotationResults> res = caller.jsonrpcCall("sdk_ontology_jmc.annotationtogo", args, retType, true, true, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }

    public Map<String, Object> status(RpcContext... jsonRpcContext) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        TypeReference<List<Map<String, Object>>> retType = new TypeReference<List<Map<String, Object>>>() {};
        List<Map<String, Object>> res = caller.jsonrpcCall("sdk_ontology_jmc.status", args, retType, true, false, jsonRpcContext, this.serviceVersion);
        return res.get(0);
    }
}