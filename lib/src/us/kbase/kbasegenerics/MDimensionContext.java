
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
 * <p>Original spec-file type: M_DimensionContext</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "data_type",
    "size",
    "typed_values"
})
public class MDimensionContext {

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    private MTerm dataType;
    @JsonProperty("size")
    private Long size;
    @JsonProperty("typed_values")
    private List<MTypedValues> typedValues;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    public MTerm getDataType() {
        return dataType;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    public void setDataType(MTerm dataType) {
        this.dataType = dataType;
    }

    public MDimensionContext withDataType(MTerm dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Long size) {
        this.size = size;
    }

    public MDimensionContext withSize(Long size) {
        this.size = size;
        return this;
    }

    @JsonProperty("typed_values")
    public List<MTypedValues> getTypedValues() {
        return typedValues;
    }

    @JsonProperty("typed_values")
    public void setTypedValues(List<MTypedValues> typedValues) {
        this.typedValues = typedValues;
    }

    public MDimensionContext withTypedValues(List<MTypedValues> typedValues) {
        this.typedValues = typedValues;
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
        return ((((((((("MDimensionContext"+" [dataType=")+ dataType)+", size=")+ size)+", typedValues=")+ typedValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
