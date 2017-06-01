
package us.kbase.genericsutil;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: ImportCSVParams</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "file",
    "workspace_name",
    "object_name",
    "object_type",
    "metadata"
})
public class ImportCSVParams {

    /**
     * <p>Original spec-file type: File</p>
     * <pre>
     * Import a CSV file into a NDArray or HNDArray.
     * "File" and "usermeta" are common to all import methods.
     * </pre>
     * 
     */
    @JsonProperty("file")
    private File file;
    @JsonProperty("workspace_name")
    private java.lang.String workspaceName;
    @JsonProperty("object_name")
    private java.lang.String objectName;
    @JsonProperty("object_type")
    private java.lang.String objectType;
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * <p>Original spec-file type: File</p>
     * <pre>
     * Import a CSV file into a NDArray or HNDArray.
     * "File" and "usermeta" are common to all import methods.
     * </pre>
     * 
     */
    @JsonProperty("file")
    public File getFile() {
        return file;
    }

    /**
     * <p>Original spec-file type: File</p>
     * <pre>
     * Import a CSV file into a NDArray or HNDArray.
     * "File" and "usermeta" are common to all import methods.
     * </pre>
     * 
     */
    @JsonProperty("file")
    public void setFile(File file) {
        this.file = file;
    }

    public ImportCSVParams withFile(File file) {
        this.file = file;
        return this;
    }

    @JsonProperty("workspace_name")
    public java.lang.String getWorkspaceName() {
        return workspaceName;
    }

    @JsonProperty("workspace_name")
    public void setWorkspaceName(java.lang.String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public ImportCSVParams withWorkspaceName(java.lang.String workspaceName) {
        this.workspaceName = workspaceName;
        return this;
    }

    @JsonProperty("object_name")
    public java.lang.String getObjectName() {
        return objectName;
    }

    @JsonProperty("object_name")
    public void setObjectName(java.lang.String objectName) {
        this.objectName = objectName;
    }

    public ImportCSVParams withObjectName(java.lang.String objectName) {
        this.objectName = objectName;
        return this;
    }

    @JsonProperty("object_type")
    public java.lang.String getObjectType() {
        return objectType;
    }

    @JsonProperty("object_type")
    public void setObjectType(java.lang.String objectType) {
        this.objectType = objectType;
    }

    public ImportCSVParams withObjectType(java.lang.String objectType) {
        this.objectType = objectType;
        return this;
    }

    @JsonProperty("metadata")
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public ImportCSVParams withMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((((("ImportCSVParams"+" [file=")+ file)+", workspaceName=")+ workspaceName)+", objectName=")+ objectName)+", objectType=")+ objectType)+", metadata=")+ metadata)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
