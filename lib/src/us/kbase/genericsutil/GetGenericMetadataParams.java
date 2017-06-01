
package us.kbase.genericsutil;

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
 * <p>Original spec-file type: GetGenericMetadataParams</p>
 * <pre>
 * Get metadata describing the dimensions of one or more generic objects
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_ids"
})
public class GetGenericMetadataParams {

    @JsonProperty("object_ids")
    private List<String> objectIds;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("object_ids")
    public List<String> getObjectIds() {
        return objectIds;
    }

    @JsonProperty("object_ids")
    public void setObjectIds(List<String> objectIds) {
        this.objectIds = objectIds;
    }

    public GetGenericMetadataParams withObjectIds(List<String> objectIds) {
        this.objectIds = objectIds;
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
        return ((((("GetGenericMetadataParams"+" [objectIds=")+ objectIds)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
