
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
 * <p>Original spec-file type: M_TypedValues</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "value_type",
    "value_units",
    "value_context",
    "values"
})
public class MTypedValues {

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    private us.kbase.kbasegenerics.MTerm valueType;
    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    private us.kbase.kbasegenerics.MTerm valueUnits;
    @JsonProperty("value_context")
    private List<TypedValue> valueContext;
    @JsonProperty("values")
    private List<us.kbase.kbasegenerics.MTerm> values;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    public us.kbase.kbasegenerics.MTerm getValueType() {
        return valueType;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_type")
    public void setValueType(us.kbase.kbasegenerics.MTerm valueType) {
        this.valueType = valueType;
    }

    public MTypedValues withValueType(us.kbase.kbasegenerics.MTerm valueType) {
        this.valueType = valueType;
        return this;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    public us.kbase.kbasegenerics.MTerm getValueUnits() {
        return valueUnits;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("value_units")
    public void setValueUnits(us.kbase.kbasegenerics.MTerm valueUnits) {
        this.valueUnits = valueUnits;
    }

    public MTypedValues withValueUnits(us.kbase.kbasegenerics.MTerm valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    @JsonProperty("value_context")
    public List<TypedValue> getValueContext() {
        return valueContext;
    }

    @JsonProperty("value_context")
    public void setValueContext(List<TypedValue> valueContext) {
        this.valueContext = valueContext;
    }

    public MTypedValues withValueContext(List<TypedValue> valueContext) {
        this.valueContext = valueContext;
        return this;
    }

    @JsonProperty("values")
    public List<us.kbase.kbasegenerics.MTerm> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<us.kbase.kbasegenerics.MTerm> values) {
        this.values = values;
    }

    public MTypedValues withValues(List<us.kbase.kbasegenerics.MTerm> values) {
        this.values = values;
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
        return ((((((((((("MTypedValues"+" [valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", valueContext=")+ valueContext)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
