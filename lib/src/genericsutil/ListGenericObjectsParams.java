
package genericsutil;

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
 * <p>Original spec-file type: ListGenericObjectsParams</p>
 * <pre>
 * List generic objects in one or more workspaces
 * optional parameters:
 * allowed_object_types - limits to specific types of object, e.g., KBaseGenerics.NDArray (version number is optional)
 * allowed_data_types - limits to specific data types, e.g., microbial growth
 * allowed_scalar_types - limits to specific scalar types, e.g., object_ref, int, float (see KBaseGenerics.spec for valid types).  HNDArrays must have at least one dimension that passes.
 * min_dimensions - limits to generics with minimum number of dimensions
 * max_dimensions - limits to generics with max number of dimensions
 * limit_mapped - if 0 (or unset) returns all objects.  if 1, returns only mapped objects.  if 2, returns only umapped objects
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "workspace_names",
    "allowed_object_types",
    "allowed_data_types",
    "allowed_scalar_types",
    "min_dimensions",
    "max_dimensions",
    "limit_mapped"
})
public class ListGenericObjectsParams {

    @JsonProperty("workspace_names")
    private List<String> workspaceNames;
    @JsonProperty("allowed_object_types")
    private List<String> allowedObjectTypes;
    @JsonProperty("allowed_data_types")
    private List<String> allowedDataTypes;
    @JsonProperty("allowed_scalar_types")
    private List<String> allowedScalarTypes;
    @JsonProperty("min_dimensions")
    private Long minDimensions;
    @JsonProperty("max_dimensions")
    private Long maxDimensions;
    @JsonProperty("limit_mapped")
    private Long limitMapped;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("workspace_names")
    public List<String> getWorkspaceNames() {
        return workspaceNames;
    }

    @JsonProperty("workspace_names")
    public void setWorkspaceNames(List<String> workspaceNames) {
        this.workspaceNames = workspaceNames;
    }

    public ListGenericObjectsParams withWorkspaceNames(List<String> workspaceNames) {
        this.workspaceNames = workspaceNames;
        return this;
    }

    @JsonProperty("allowed_object_types")
    public List<String> getAllowedObjectTypes() {
        return allowedObjectTypes;
    }

    @JsonProperty("allowed_object_types")
    public void setAllowedObjectTypes(List<String> allowedObjectTypes) {
        this.allowedObjectTypes = allowedObjectTypes;
    }

    public ListGenericObjectsParams withAllowedObjectTypes(List<String> allowedObjectTypes) {
        this.allowedObjectTypes = allowedObjectTypes;
        return this;
    }

    @JsonProperty("allowed_data_types")
    public List<String> getAllowedDataTypes() {
        return allowedDataTypes;
    }

    @JsonProperty("allowed_data_types")
    public void setAllowedDataTypes(List<String> allowedDataTypes) {
        this.allowedDataTypes = allowedDataTypes;
    }

    public ListGenericObjectsParams withAllowedDataTypes(List<String> allowedDataTypes) {
        this.allowedDataTypes = allowedDataTypes;
        return this;
    }

    @JsonProperty("allowed_scalar_types")
    public List<String> getAllowedScalarTypes() {
        return allowedScalarTypes;
    }

    @JsonProperty("allowed_scalar_types")
    public void setAllowedScalarTypes(List<String> allowedScalarTypes) {
        this.allowedScalarTypes = allowedScalarTypes;
    }

    public ListGenericObjectsParams withAllowedScalarTypes(List<String> allowedScalarTypes) {
        this.allowedScalarTypes = allowedScalarTypes;
        return this;
    }

    @JsonProperty("min_dimensions")
    public Long getMinDimensions() {
        return minDimensions;
    }

    @JsonProperty("min_dimensions")
    public void setMinDimensions(Long minDimensions) {
        this.minDimensions = minDimensions;
    }

    public ListGenericObjectsParams withMinDimensions(Long minDimensions) {
        this.minDimensions = minDimensions;
        return this;
    }

    @JsonProperty("max_dimensions")
    public Long getMaxDimensions() {
        return maxDimensions;
    }

    @JsonProperty("max_dimensions")
    public void setMaxDimensions(Long maxDimensions) {
        this.maxDimensions = maxDimensions;
    }

    public ListGenericObjectsParams withMaxDimensions(Long maxDimensions) {
        this.maxDimensions = maxDimensions;
        return this;
    }

    @JsonProperty("limit_mapped")
    public Long getLimitMapped() {
        return limitMapped;
    }

    @JsonProperty("limit_mapped")
    public void setLimitMapped(Long limitMapped) {
        this.limitMapped = limitMapped;
    }

    public ListGenericObjectsParams withLimitMapped(Long limitMapped) {
        this.limitMapped = limitMapped;
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
        return ((((((((((((((((("ListGenericObjectsParams"+" [workspaceNames=")+ workspaceNames)+", allowedObjectTypes=")+ allowedObjectTypes)+", allowedDataTypes=")+ allowedDataTypes)+", allowedScalarTypes=")+ allowedScalarTypes)+", minDimensions=")+ minDimensions)+", maxDimensions=")+ maxDimensions)+", limitMapped=")+ limitMapped)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
