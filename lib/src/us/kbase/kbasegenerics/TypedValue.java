
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
 * <p>Original spec-file type: TypedValue</p>
 * <pre>
 * @optional value_units
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "value_type",
    "value_units",
    "value"
})
public class TypedValue {

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_type")
    private Term valueType;
    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_units")
    private Term valueUnits;
    /**
     * <p>Original spec-file type: Value</p>
     * <pre>
     * scalar_type can be one of:
     *     object_ref
     *     oterm_ref
     *     int
     *     float
     *     boolean
     *     string
     * @optional object_ref oterm_ref int_value float_value string_value
     * </pre>
     * 
     */
    @JsonProperty("value")
    private Value value;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_type")
    public Term getValueType() {
        return valueType;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_type")
    public void setValueType(Term valueType) {
        this.valueType = valueType;
    }

    public TypedValue withValueType(Term valueType) {
        this.valueType = valueType;
        return this;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_units")
    public Term getValueUnits() {
        return valueUnits;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("value_units")
    public void setValueUnits(Term valueUnits) {
        this.valueUnits = valueUnits;
    }

    public TypedValue withValueUnits(Term valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    /**
     * <p>Original spec-file type: Value</p>
     * <pre>
     * scalar_type can be one of:
     *     object_ref
     *     oterm_ref
     *     int
     *     float
     *     boolean
     *     string
     * @optional object_ref oterm_ref int_value float_value string_value
     * </pre>
     * 
     */
    @JsonProperty("value")
    public Value getValue() {
        return value;
    }

    /**
     * <p>Original spec-file type: Value</p>
     * <pre>
     * scalar_type can be one of:
     *     object_ref
     *     oterm_ref
     *     int
     *     float
     *     boolean
     *     string
     * @optional object_ref oterm_ref int_value float_value string_value
     * </pre>
     * 
     */
    @JsonProperty("value")
    public void setValue(Value value) {
        this.value = value;
    }

    public TypedValue withValue(Value value) {
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
        return ((((((((("TypedValue"+" [valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", value=")+ value)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
