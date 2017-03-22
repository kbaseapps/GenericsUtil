
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
 * <p>Original spec-file type: Value</p>
 * <pre>
 * scalar_type can be one of:
 *     object_ref
 *     oterm_ref
 *     int
 *     float
 *     boolean
 *     string
 * @optional object_ref oterm_ref int_value float_value string_value boolean_value
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "scalar_type",
    "object_ref",
    "oterm_ref",
    "int_value",
    "float_value",
    "boolean_value",
    "string_value"
})
public class Value {

    @JsonProperty("scalar_type")
    private String scalarType;
    @JsonProperty("object_ref")
    private String objectRef;
    @JsonProperty("oterm_ref")
    private String otermRef;
    @JsonProperty("int_value")
    private Long intValue;
    @JsonProperty("float_value")
    private Double floatValue;
    @JsonProperty("boolean_value")
    private Long booleanValue;
    @JsonProperty("string_value")
    private String stringValue;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("scalar_type")
    public String getScalarType() {
        return scalarType;
    }

    @JsonProperty("scalar_type")
    public void setScalarType(String scalarType) {
        this.scalarType = scalarType;
    }

    public Value withScalarType(String scalarType) {
        this.scalarType = scalarType;
        return this;
    }

    @JsonProperty("object_ref")
    public String getObjectRef() {
        return objectRef;
    }

    @JsonProperty("object_ref")
    public void setObjectRef(String objectRef) {
        this.objectRef = objectRef;
    }

    public Value withObjectRef(String objectRef) {
        this.objectRef = objectRef;
        return this;
    }

    @JsonProperty("oterm_ref")
    public String getOtermRef() {
        return otermRef;
    }

    @JsonProperty("oterm_ref")
    public void setOtermRef(String otermRef) {
        this.otermRef = otermRef;
    }

    public Value withOtermRef(String otermRef) {
        this.otermRef = otermRef;
        return this;
    }

    @JsonProperty("int_value")
    public Long getIntValue() {
        return intValue;
    }

    @JsonProperty("int_value")
    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    public Value withIntValue(Long intValue) {
        this.intValue = intValue;
        return this;
    }

    @JsonProperty("float_value")
    public Double getFloatValue() {
        return floatValue;
    }

    @JsonProperty("float_value")
    public void setFloatValue(Double floatValue) {
        this.floatValue = floatValue;
    }

    public Value withFloatValue(Double floatValue) {
        this.floatValue = floatValue;
        return this;
    }

    @JsonProperty("boolean_value")
    public Long getBooleanValue() {
        return booleanValue;
    }

    @JsonProperty("boolean_value")
    public void setBooleanValue(Long booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Value withBooleanValue(Long booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }

    @JsonProperty("string_value")
    public String getStringValue() {
        return stringValue;
    }

    @JsonProperty("string_value")
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Value withStringValue(String stringValue) {
        this.stringValue = stringValue;
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
        return ((((((((((((((((("Value"+" [scalarType=")+ scalarType)+", objectRef=")+ objectRef)+", otermRef=")+ otermRef)+", intValue=")+ intValue)+", floatValue=")+ floatValue)+", booleanValue=")+ booleanValue)+", stringValue=")+ stringValue)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
