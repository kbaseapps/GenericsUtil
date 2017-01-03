
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
 * <p>Original spec-file type: Matrix3D</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "data_type",
    "matrix_context",
    "x_context",
    "y_context",
    "z_context",
    "value_type",
    "value_units",
    "values"
})
public class Matrix3D {

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
    @JsonProperty("matrix_context")
    private List<ContextItem> matrixContext;
    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("x_context")
    private DimensionContext xContext;
    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("y_context")
    private DimensionContext yContext;
    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("z_context")
    private DimensionContext zContext;
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
     * <p>Original spec-file type: Values3D</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    private Values3D values;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Matrix3D withName(String name) {
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

    public Matrix3D withDescription(String description) {
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

    public Matrix3D withDataType(Term dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("matrix_context")
    public List<ContextItem> getMatrixContext() {
        return matrixContext;
    }

    @JsonProperty("matrix_context")
    public void setMatrixContext(List<ContextItem> matrixContext) {
        this.matrixContext = matrixContext;
    }

    public Matrix3D withMatrixContext(List<ContextItem> matrixContext) {
        this.matrixContext = matrixContext;
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

    public Matrix3D withXContext(DimensionContext xContext) {
        this.xContext = xContext;
        return this;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("y_context")
    public DimensionContext getYContext() {
        return yContext;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("y_context")
    public void setYContext(DimensionContext yContext) {
        this.yContext = yContext;
    }

    public Matrix3D withYContext(DimensionContext yContext) {
        this.yContext = yContext;
        return this;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("z_context")
    public DimensionContext getZContext() {
        return zContext;
    }

    /**
     * <p>Original spec-file type: DimensionContext</p>
     * 
     * 
     */
    @JsonProperty("z_context")
    public void setZContext(DimensionContext zContext) {
        this.zContext = zContext;
    }

    public Matrix3D withZContext(DimensionContext zContext) {
        this.zContext = zContext;
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

    public Matrix3D withValueType(Term valueType) {
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

    public Matrix3D withValueUnits(Term valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    /**
     * <p>Original spec-file type: Values3D</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    public Values3D getValues() {
        return values;
    }

    /**
     * <p>Original spec-file type: Values3D</p>
     * <pre>
     * @optional object_refs oterm_refs int_values float_values string_values
     * </pre>
     * 
     */
    @JsonProperty("values")
    public void setValues(Values3D values) {
        this.values = values;
    }

    public Matrix3D withValues(Values3D values) {
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
        return ((((((((((((((((((((((("Matrix3D"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", matrixContext=")+ matrixContext)+", xContext=")+ xContext)+", yContext=")+ yContext)+", zContext=")+ zContext)+", valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
