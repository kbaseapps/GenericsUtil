
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
 * <p>Original spec-file type: DimensionContextItem</p>
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
    "values"
})
public class DimensionContextItem {

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
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    private Values values;
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

    public DimensionContextItem withProperty(Term property) {
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

    public DimensionContextItem withUnits(Term units) {
        this.units = units;
        return this;
    }

    /**
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    public Values getValues() {
        return values;
    }

    /**
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    public void setValues(Values values) {
        this.values = values;
    }

    public DimensionContextItem withValues(Values values) {
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
        return ((((((((("DimensionContextItem"+" [property=")+ property)+", units=")+ units)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
