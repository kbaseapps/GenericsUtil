
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
 * <p>Original spec-file type: Array</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "data_type",
    "array_context",
    "x_context",
    "value_type",
    "value_units",
    "values"
})
public class Array {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("data_type")
    private Term dataType;
    @JsonProperty("array_context")
    private List<ContextItem> arrayContext;
    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("x_context")
    private DimensionContext xContext;
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
     * <p>Original spec-file type: Values</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    private Values values;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Array withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Array withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("data_type")
    public Term getDataType() {
        return dataType;
    }

    /**
     * <p>Original spec-file type: Term</p>
     * <pre>
     * @optional oterm_ref oterm_name
     * </pre>
     * 
     */
    @JsonProperty("data_type")
    public void setDataType(Term dataType) {
        this.dataType = dataType;
    }

    public Array withDataType(Term dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("array_context")
    public List<ContextItem> getArrayContext() {
        return arrayContext;
    }

    @JsonProperty("array_context")
    public void setArrayContext(List<ContextItem> arrayContext) {
        this.arrayContext = arrayContext;
    }

    public Array withArrayContext(List<ContextItem> arrayContext) {
        this.arrayContext = arrayContext;
        return this;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("x_context")
    public DimensionContext getXContext() {
        return xContext;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("x_context")
    public void setXContext(DimensionContext xContext) {
        this.xContext = xContext;
    }

    public Array withXContext(DimensionContext xContext) {
        this.xContext = xContext;
        return this;
    }

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

    public Array withValueType(Term valueType) {
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

    public Array withValueUnits(Term valueUnits) {
        this.valueUnits = valueUnits;
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

    public Array withValues(Values values) {
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
        return ((((((((((((((((((("Array"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", arrayContext=")+ arrayContext)+", xContext=")+ xContext)+", valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
