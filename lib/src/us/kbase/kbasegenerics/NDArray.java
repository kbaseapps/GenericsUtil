
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
 * <p>Original spec-file type: NDArray</p>
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
    "dimension_number",
    "dimensions_context",
    "value_type",
    "value_units",
    "values"
})
public class NDArray {

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
    @JsonProperty("dimension_number")
    private Long dimensionNumber;
    @JsonProperty("dimensions_context")
    private List<DimensionContext> dimensionsContext;
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

    public NDArray withName(String name) {
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

    public NDArray withDescription(String description) {
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

    public NDArray withDataType(Term dataType) {
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

    public NDArray withArrayContext(List<ContextItem> arrayContext) {
        this.arrayContext = arrayContext;
        return this;
    }

    @JsonProperty("dimension_number")
    public Long getDimensionNumber() {
        return dimensionNumber;
    }

    @JsonProperty("dimension_number")
    public void setDimensionNumber(Long dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
    }

    public NDArray withDimensionNumber(Long dimensionNumber) {
        this.dimensionNumber = dimensionNumber;
        return this;
    }

    @JsonProperty("dimensions_context")
    public List<DimensionContext> getDimensionsContext() {
        return dimensionsContext;
    }

    @JsonProperty("dimensions_context")
    public void setDimensionsContext(List<DimensionContext> dimensionsContext) {
        this.dimensionsContext = dimensionsContext;
    }

    public NDArray withDimensionsContext(List<DimensionContext> dimensionsContext) {
        this.dimensionsContext = dimensionsContext;
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

    public NDArray withValueType(Term valueType) {
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

    public NDArray withValueUnits(Term valueUnits) {
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

    public NDArray withValues(Values values) {
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
        return ((((((((((((((((((((("NDArray"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", arrayContext=")+ arrayContext)+", dimensionNumber=")+ dimensionNumber)+", dimensionsContext=")+ dimensionsContext)+", valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
