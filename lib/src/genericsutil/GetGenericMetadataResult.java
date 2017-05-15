
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
 * <p>Original spec-file type: GetGenericMetadataResult</p>
 * <pre>
 * maps object ids to structure with metadata
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_info"
})
public class GetGenericMetadataResult {

    @JsonProperty("object_info")
    private Map<String, GenericMetadata> objectInfo;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("object_info")
    public Map<String, GenericMetadata> getObjectInfo() {
        return objectInfo;
    }

    @JsonProperty("object_info")
    public void setObjectInfo(Map<String, GenericMetadata> objectInfo) {
        this.objectInfo = objectInfo;
    }

    public GetGenericMetadataResult withObjectInfo(Map<String, GenericMetadata> objectInfo) {
        this.objectInfo = objectInfo;
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
        return ((((("GetGenericMetadataResult"+" [objectInfo=")+ objectInfo)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
