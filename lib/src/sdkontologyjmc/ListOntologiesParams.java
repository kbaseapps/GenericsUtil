
package sdkontologyjmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: ListOntologiesParams</p>
 * <pre>
 * List all ontologies in one or more workspaces
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "workspace_names"
})
public class ListOntologiesParams {

    @JsonProperty("workspace_names")
    private List<String> workspaceNames;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("workspace_names")
    public List<String> getWorkspaceNames() {
        return workspaceNames;
    }

    @JsonProperty("workspace_names")
    public void setWorkspaceNames(List<String> workspaceNames) {
        this.workspaceNames = workspaceNames;
    }

    public ListOntologiesParams withWorkspaceNames(List<String> workspaceNames) {
        this.workspaceNames = workspaceNames;
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
        return ((((("ListOntologiesParams"+" [workspaceNames=")+ workspaceNames)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
