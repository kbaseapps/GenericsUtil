
package us.kbase.genericsutil;

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
 * <p>Original spec-file type: GetGenericDataParams</p>
 * <pre>
 * gets subset of generic data as a 2D matrix
 * Users passes in the dimension indices to use as variables (1st
 * one must be X axis; additional variables will lead to additional
 * series being returned).
 * User selects which dimension indices to fix to
 * particular constants.  This can be done one of two ways:  either
 * by fixing an entire dimension (e.g., "2" for the 2nd dimension)
 * to an index in the complete list
 * of labels, or by fixing a dimension index (e.g., "2/3" for the
 *  3rd type of values in the 2nd dimension) to an index in the
 * list of unique labels for that dimension index.
 * return values:
 * data_x_float is a list of x-axis values
 * data_y_float is a list of y-axis values, 1 per series.  The number
 *   of series depends on the number of variable dimensions.
 * series_labels will show which variable index values correspond
 *   to which series
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_id",
    "variable_dimension_ids",
    "constant_dimension_ids"
})
public class GetGenericDataParams {

    @JsonProperty("object_id")
    private java.lang.String objectId;
    @JsonProperty("variable_dimension_ids")
    private List<String> variableDimensionIds;
    @JsonProperty("constant_dimension_ids")
    private Map<String, Long> constantDimensionIds;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("object_id")
    public java.lang.String getObjectId() {
        return objectId;
    }

    @JsonProperty("object_id")
    public void setObjectId(java.lang.String objectId) {
        this.objectId = objectId;
    }

    public GetGenericDataParams withObjectId(java.lang.String objectId) {
        this.objectId = objectId;
        return this;
    }

    @JsonProperty("variable_dimension_ids")
    public List<String> getVariableDimensionIds() {
        return variableDimensionIds;
    }

    @JsonProperty("variable_dimension_ids")
    public void setVariableDimensionIds(List<String> variableDimensionIds) {
        this.variableDimensionIds = variableDimensionIds;
    }

    public GetGenericDataParams withVariableDimensionIds(List<String> variableDimensionIds) {
        this.variableDimensionIds = variableDimensionIds;
        return this;
    }

    @JsonProperty("constant_dimension_ids")
    public Map<String, Long> getConstantDimensionIds() {
        return constantDimensionIds;
    }

    @JsonProperty("constant_dimension_ids")
    public void setConstantDimensionIds(Map<String, Long> constantDimensionIds) {
        this.constantDimensionIds = constantDimensionIds;
    }

    public GetGenericDataParams withConstantDimensionIds(Map<String, Long> constantDimensionIds) {
        this.constantDimensionIds = constantDimensionIds;
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
        return ((((((((("GetGenericDataParams"+" [objectId=")+ objectId)+", variableDimensionIds=")+ variableDimensionIds)+", constantDimensionIds=")+ constantDimensionIds)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
