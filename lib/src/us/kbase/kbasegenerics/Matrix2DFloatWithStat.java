
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
 * <p>Original spec-file type: Matrix2DFloatWithStat</p>
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
    "value_type",
    "value_units",
    "avg_values",
    "std_values",
    "se_values"
})
public class Matrix2DFloatWithStat {

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
    @JsonProperty("avg_values")
    private List<List<Double>> avgValues;
    @JsonProperty("std_values")
    private List<List<Double>> stdValues;
    @JsonProperty("se_values")
    private List<List<Double>> seValues;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Matrix2DFloatWithStat withName(String name) {
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

    public Matrix2DFloatWithStat withDescription(String description) {
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

    public Matrix2DFloatWithStat withDataType(Term dataType) {
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

    public Matrix2DFloatWithStat withMatrixContext(List<ContextItem> matrixContext) {
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

    public Matrix2DFloatWithStat withXContext(DimensionContext xContext) {
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

    public Matrix2DFloatWithStat withYContext(DimensionContext yContext) {
        this.yContext = yContext;
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

    public Matrix2DFloatWithStat withValueType(Term valueType) {
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

    public Matrix2DFloatWithStat withValueUnits(Term valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    @JsonProperty("avg_values")
    public List<List<Double>> getAvgValues() {
        return avgValues;
    }

    @JsonProperty("avg_values")
    public void setAvgValues(List<List<Double>> avgValues) {
        this.avgValues = avgValues;
    }

    public Matrix2DFloatWithStat withAvgValues(List<List<Double>> avgValues) {
        this.avgValues = avgValues;
        return this;
    }

    @JsonProperty("std_values")
    public List<List<Double>> getStdValues() {
        return stdValues;
    }

    @JsonProperty("std_values")
    public void setStdValues(List<List<Double>> stdValues) {
        this.stdValues = stdValues;
    }

    public Matrix2DFloatWithStat withStdValues(List<List<Double>> stdValues) {
        this.stdValues = stdValues;
        return this;
    }

    @JsonProperty("se_values")
    public List<List<Double>> getSeValues() {
        return seValues;
    }

    @JsonProperty("se_values")
    public void setSeValues(List<List<Double>> seValues) {
        this.seValues = seValues;
    }

    public Matrix2DFloatWithStat withSeValues(List<List<Double>> seValues) {
        this.seValues = seValues;
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
        return ((((((((((((((((((((((((("Matrix2DFloatWithStat"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", matrixContext=")+ matrixContext)+", xContext=")+ xContext)+", yContext=")+ yContext)+", valueType=")+ valueType)+", valueUnits=")+ valueUnits)+", avgValues=")+ avgValues)+", stdValues=")+ stdValues)+", seValues=")+ seValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
