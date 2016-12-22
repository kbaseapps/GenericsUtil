
package us.kbase.kbasegenerics;

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
 * <p>Original spec-file type: MetadataItem</p>
 * <pre>
 * A single piece of metadata (i.e., metadatum)
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "original_description",
    "measurement",
    "units",
    "context"
})
public class MetadataItem {

    @JsonProperty("original_description")
    private java.lang.String originalDescription;
    @JsonProperty("measurement")
    private java.lang.String measurement;
    @JsonProperty("units")
    private java.lang.String units;
    @JsonProperty("context")
    private List<String> context;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("original_description")
    public java.lang.String getOriginalDescription() {
        return originalDescription;
    }

    @JsonProperty("original_description")
    public void setOriginalDescription(java.lang.String originalDescription) {
        this.originalDescription = originalDescription;
    }

    public MetadataItem withOriginalDescription(java.lang.String originalDescription) {
        this.originalDescription = originalDescription;
        return this;
    }

    @JsonProperty("measurement")
    public java.lang.String getMeasurement() {
        return measurement;
    }

    @JsonProperty("measurement")
    public void setMeasurement(java.lang.String measurement) {
        this.measurement = measurement;
    }

    public MetadataItem withMeasurement(java.lang.String measurement) {
        this.measurement = measurement;
        return this;
    }

    @JsonProperty("units")
    public java.lang.String getUnits() {
        return units;
    }

    @JsonProperty("units")
    public void setUnits(java.lang.String units) {
        this.units = units;
    }

    public MetadataItem withUnits(java.lang.String units) {
        this.units = units;
        return this;
    }

    @JsonProperty("context")
    public List<String> getContext() {
        return context;
    }

    @JsonProperty("context")
    public void setContext(List<String> context) {
        this.context = context;
    }

    public MetadataItem withContext(List<String> context) {
        this.context = context;
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
        return ((((((((((("MetadataItem"+" [originalDescription=")+ originalDescription)+", measurement=")+ measurement)+", units=")+ units)+", context=")+ context)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
