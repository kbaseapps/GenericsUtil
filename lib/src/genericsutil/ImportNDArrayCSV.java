
package genericsutil;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: ImportNDArrayCSV</p>
 * <pre>
 * matrix_name - name of object
 * workspace_name - workspace it gets saved to
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "file",
    "matrix_name",
    "workspace_name",
    "metadata"
})
public class ImportNDArrayCSV {

    /**
     * <p>Original spec-file type: File</p>
     * 
     * 
     */
    @JsonProperty("file")
    private File file;
    @JsonProperty("matrix_name")
    private java.lang.String matrixName;
    @JsonProperty("workspace_name")
    private java.lang.String workspaceName;
    @JsonProperty("metadata")
    private Map<String, String> metadata;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    /**
     * <p>Original spec-file type: File</p>
     * 
     * 
     */
    @JsonProperty("file")
    public File getFile() {
        return file;
    }

    /**
     * <p>Original spec-file type: File</p>
     * 
     * 
     */
    @JsonProperty("file")
    public void setFile(File file) {
        this.file = file;
    }

    public ImportNDArrayCSV withFile(File file) {
        this.file = file;
        return this;
    }

    @JsonProperty("matrix_name")
    public java.lang.String getMatrixName() {
        return matrixName;
    }

    @JsonProperty("matrix_name")
    public void setMatrixName(java.lang.String matrixName) {
        this.matrixName = matrixName;
    }

    public ImportNDArrayCSV withMatrixName(java.lang.String matrixName) {
        this.matrixName = matrixName;
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

    public ImportNDArrayCSV withWorkspaceName(java.lang.String workspaceName) {
        this.workspaceName = workspaceName;
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

    public ImportNDArrayCSV withMetadata(Map<String, String> metadata) {
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
        return ((((((((((("ImportNDArrayCSV"+" [file=")+ file)+", matrixName=")+ matrixName)+", workspaceName=")+ workspaceName)+", metadata=")+ metadata)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
