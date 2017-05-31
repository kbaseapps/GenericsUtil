
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
 * <p>Original spec-file type: GetGenericDimensionLabelsResult</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "dimension_labels"
})
public class GetGenericDimensionLabelsResult {

    @JsonProperty("dimension_labels")
    private Map<String, Values> dimensionLabels;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("dimension_labels")
    public Map<String, Values> getDimensionLabels() {
        return dimensionLabels;
    }

    @JsonProperty("dimension_labels")
    public void setDimensionLabels(Map<String, Values> dimensionLabels) {
        this.dimensionLabels = dimensionLabels;
    }

    public GetGenericDimensionLabelsResult withDimensionLabels(Map<String, Values> dimensionLabels) {
        this.dimensionLabels = dimensionLabels;
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
        return ((((("GetGenericDimensionLabelsResult"+" [dimensionLabels=")+ dimensionLabels)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
