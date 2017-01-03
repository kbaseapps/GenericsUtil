
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
 * <p>Original spec-file type: Values</p>
 * <pre>
 * @optional object_refs oterm_refs int_values float_values string_values
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "scalar_type",
    "object_refs",
    "oterm_refs",
    "int_values",
    "float_values",
    "string_values"
})
public class Values {

    @JsonProperty("scalar_type")
    private java.lang.String scalarType;
    @JsonProperty("object_refs")
    private List<String> objectRefs;
    @JsonProperty("oterm_refs")
    private List<String> otermRefs;
    @JsonProperty("int_values")
    private List<Long> intValues;
    @JsonProperty("float_values")
    private List<Double> floatValues;
    @JsonProperty("string_values")
    private List<String> stringValues;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("scalar_type")
    public java.lang.String getScalarType() {
        return scalarType;
    }

    @JsonProperty("scalar_type")
    public void setScalarType(java.lang.String scalarType) {
        this.scalarType = scalarType;
    }

    public Values withScalarType(java.lang.String scalarType) {
        this.scalarType = scalarType;
        return this;
    }

    @JsonProperty("object_refs")
    public List<String> getObjectRefs() {
        return objectRefs;
    }

    @JsonProperty("object_refs")
    public void setObjectRefs(List<String> objectRefs) {
        this.objectRefs = objectRefs;
    }

    public Values withObjectRefs(List<String> objectRefs) {
        this.objectRefs = objectRefs;
        return this;
    }

    @JsonProperty("oterm_refs")
    public List<String> getOtermRefs() {
        return otermRefs;
    }

    @JsonProperty("oterm_refs")
    public void setOtermRefs(List<String> otermRefs) {
        this.otermRefs = otermRefs;
    }

    public Values withOtermRefs(List<String> otermRefs) {
        this.otermRefs = otermRefs;
        return this;
    }

    @JsonProperty("int_values")
    public List<Long> getIntValues() {
        return intValues;
    }

    @JsonProperty("int_values")
    public void setIntValues(List<Long> intValues) {
        this.intValues = intValues;
    }

    public Values withIntValues(List<Long> intValues) {
        this.intValues = intValues;
        return this;
    }

    @JsonProperty("float_values")
    public List<Double> getFloatValues() {
        return floatValues;
    }

    @JsonProperty("float_values")
    public void setFloatValues(List<Double> floatValues) {
        this.floatValues = floatValues;
    }

    public Values withFloatValues(List<Double> floatValues) {
        this.floatValues = floatValues;
        return this;
    }

    @JsonProperty("string_values")
    public List<String> getStringValues() {
        return stringValues;
    }

    @JsonProperty("string_values")
    public void setStringValues(List<String> stringValues) {
        this.stringValues = stringValues;
    }

    public Values withStringValues(List<String> stringValues) {
        this.stringValues = stringValues;
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
        return ((((((((((((((("Values"+" [scalarType=")+ scalarType)+", objectRefs=")+ objectRefs)+", otermRefs=")+ otermRefs)+", intValues=")+ intValues)+", floatValues=")+ floatValues)+", stringValues=")+ stringValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
