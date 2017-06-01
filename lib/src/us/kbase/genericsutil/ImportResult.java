
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
 * <p>Original spec-file type: ImportResult</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_ref"
})
public class ImportResult {

    @JsonProperty("object_ref")
    private String objectRef;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("object_ref")
    public String getObjectRef() {
        return objectRef;
    }

    @JsonProperty("object_ref")
    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public ImportResult withObjectRef(String objectRef) {
        this.objectRef = objectRef;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ((((("ImportResult"+" [objectRef=")+ objectRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
