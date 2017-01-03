
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
 * <p>Original spec-file type: Values3D</p>
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
public class Values3D {

    @JsonProperty("scalar_type")
    private java.lang.String scalarType;
    @JsonProperty("object_refs")
    private List<List<List<String>>> objectRefs;
    @JsonProperty("oterm_refs")
    private List<List<List<String>>> otermRefs;
    @JsonProperty("int_values")
    private List<List<List<Long>>> intValues;
    @JsonProperty("float_values")
    private List<List<List<Double>>> floatValues;
    @JsonProperty("string_values")
    private List<List<List<String>>> stringValues;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("scalar_type")
    public java.lang.String getScalarType() {
        return scalarType;
    }

    @JsonProperty("scalar_type")
    public void setScalarType(java.lang.String scalarType) {
        this.scalarType = scalarType;
    }

    public Values3D withScalarType(java.lang.String scalarType) {
        this.scalarType = scalarType;
        return this;
    }

    @JsonProperty("object_refs")
    public List<List<List<String>>> getObjectRefs() {
        return objectRefs;
    }

    @JsonProperty("object_refs")
    public void setObjectRefs(List<List<List<String>>> objectRefs) {
        this.objectRefs = objectRefs;
    }

    public Values3D withObjectRefs(List<List<List<String>>> objectRefs) {
        this.objectRefs = objectRefs;
        return this;
    }

    @JsonProperty("oterm_refs")
    public List<List<List<String>>> getOtermRefs() {
        return otermRefs;
    }

    @JsonProperty("oterm_refs")
    public void setOtermRefs(List<List<List<String>>> otermRefs) {
        this.otermRefs = otermRefs;
    }

    public Values3D withOtermRefs(List<List<List<String>>> otermRefs) {
        this.otermRefs = otermRefs;
        return this;
    }

    @JsonProperty("int_values")
    public List<List<List<Long>>> getIntValues() {
        return intValues;
    }

    @JsonProperty("int_values")
    public void setIntValues(List<List<List<Long>>> intValues) {
        this.intValues = intValues;
    }

    public Values3D withIntValues(List<List<List<Long>>> intValues) {
        this.intValues = intValues;
        return this;
    }

    @JsonProperty("float_values")
    public List<List<List<Double>>> getFloatValues() {
        return floatValues;
    }

    @JsonProperty("float_values")
    public void setFloatValues(List<List<List<Double>>> floatValues) {
        this.floatValues = floatValues;
    }

    public Values3D withFloatValues(List<List<List<Double>>> floatValues) {
        this.floatValues = floatValues;
        return this;
    }

    @JsonProperty("string_values")
    public List<List<List<String>>> getStringValues() {
        return stringValues;
    }

    @JsonProperty("string_values")
    public void setStringValues(List<List<List<String>>> stringValues) {
        this.stringValues = stringValues;
    }

    public Values3D withStringValues(List<List<List<String>>> stringValues) {
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
        return ((((((((((((((("Values3D"+" [scalarType=")+ scalarType)+", objectRefs=")+ objectRefs)+", otermRefs=")+ otermRefs)+", intValues=")+ intValues)+", floatValues=")+ floatValues)+", stringValues=")+ stringValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
