
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
 * <p>Original spec-file type: GetGenericDimensionLabelsParams</p>
 * <pre>
 * gets labels for list of dimension axes for a generic object.
 * User will pass in the numeric indices of all dimensions they care
 * about (e.g., 1/1 will mean 1st dimension, 1st data type, 2/1 = 2nd
 * dimension, 1st data type), and an optional flag, convert_to_string.
 * The API will return a hash mapping each of the dimension indices to
 * a Values object.  The Values will either contain the scalar type in
 * the original format, or if the convert_to_string flag is set, will
 * convert the scalar type to strings.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_id",
    "dimension_ids",
    "convert_to_string"
})
public class GetGenericDimensionLabelsParams {

    @JsonProperty("object_id")
    private java.lang.String objectId;
    @JsonProperty("dimension_ids")
    private List<String> dimensionIds;
    @JsonProperty("convert_to_string")
    private Long convertToString;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("object_id")
    public java.lang.String getObjectId() {
        return objectId;
    }

    @JsonProperty("object_id")
    public void setObjectId(java.lang.String objectId) {
        this.objectId = objectId;
    }

    public GetGenericDimensionLabelsParams withObjectId(java.lang.String objectId) {
        this.objectId = objectId;
        return this;
    }

    @JsonProperty("dimension_ids")
    public List<String> getDimensionIds() {
        return dimensionIds;
    }

    @JsonProperty("dimension_ids")
    public void setDimensionIds(List<String> dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public GetGenericDimensionLabelsParams withDimensionIds(List<String> dimensionIds) {
        this.dimensionIds = dimensionIds;
        return this;
    }

    @JsonProperty("convert_to_string")
    public Long getConvertToString() {
        return convertToString;
    }

    @JsonProperty("convert_to_string")
    public void setConvertToString(Long convertToString) {
        this.convertToString = convertToString;
    }

    public GetGenericDimensionLabelsParams withConvertToString(Long convertToString) {
        this.convertToString = convertToString;
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
        return ((((((((("GetGenericDimensionLabelsParams"+" [objectId=")+ objectId)+", dimensionIds=")+ dimensionIds)+", convertToString=")+ convertToString)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
