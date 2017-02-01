
package us.kbase.kbasegenerics;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: M_TypedValue</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "value_type",
    "value_units",
    "value"
})
public class MTypedValue {

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    private MTerm valueType;
    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    private MTerm valueUnits;
    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value")
    private MTerm value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    public MTerm getValueType() {
        return valueType;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    public void setValueType(MTerm valueType) {
        this.valueType = valueType;
    }

    public MTypedValue withValueType(MTerm valueType) {
        this.valueType = valueType;
        return this;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    public MTerm getValueUnits() {
        return valueUnits;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    public void setValueUnits(MTerm valueUnits) {
        this.valueUnits = valueUnits;
    }

    public MTypedValue withValueUnits(MTerm valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value")
    public MTerm getValue() {
        return value;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value")
    public void setValue(MTerm value) {
        this.value = value;
    }

    public MTypedValue withValue(MTerm value) {
        this.value = value;
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
        return ((((((((("MTypedValue"+" [valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", value=")+ value)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
