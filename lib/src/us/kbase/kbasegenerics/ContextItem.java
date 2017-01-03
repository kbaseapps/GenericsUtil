
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
 * <p>Original spec-file type: ContextItem</p>
 * <pre>
 * @optional units
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "property",
    "units",
    "value"
})
public class ContextItem {

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("property")
    private Term property;
    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("units")
    private Term units;
    /**
     * <p>Original spec-file type: Value</p>
     * <pre>
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
    @JsonProperty("property")
    public Term getProperty() {
        return property;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("property")
    public void setProperty(Term property) {
        this.property = property;
    }

    public ContextItem withProperty(Term property) {
        this.property = property;
        return this;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("units")
    public Term getUnits() {
        return units;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("units")
    public void setUnits(Term units) {
        this.units = units;
    }

    public ContextItem withUnits(Term units) {
        this.units = units;
        return this;
    }

    /**
     * <p>Original spec-file type: Value</p>
     * <pre>
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
     * @optional object_ref oterm_ref int_value float_value string_value
     * </pre>
     * 
     */
    @JsonProperty("value")
    public void setValue(Value value) {
        this.value = value;
    }

    public ContextItem withValue(Value value) {
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
        return ((((((((("ContextItem"+" [property=")+ property)+", units=")+ units)+", value=")+ value)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
