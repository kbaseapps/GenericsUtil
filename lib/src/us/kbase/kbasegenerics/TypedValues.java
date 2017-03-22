
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
 * <p>Original spec-file type: TypedValues</p>
 * <pre>
 * @optional value_units value_context
 * </pre>
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
public class TypedValues {

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
    @JsonProperty("value_context")
    private List<TypedValue> valueContext;
    /**
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values boolean_values
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

    public TypedValues withValueType(Term valueType) {
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

    public TypedValues withValueUnits(Term valueUnits) {
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

    public TypedValues withValueContext(List<TypedValue> valueContext) {
        this.valueContext = valueContext;
        return this;
    }

    /**
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values boolean_values
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
     * @optional object_refs oterm_refs int_values float_values string_values boolean_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    public void setValues(Values values) {
        this.values = values;
    }

    public TypedValues withValues(Values values) {
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
        return ((((((((((("TypedValues"+" [valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", valueContext=")+ valueContext)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
