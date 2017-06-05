
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
 * <p>Original spec-file type: GenericMetadata</p>
 * <pre>
 * Basic metadata about an object:
 * object_type - e.g., KBaseGenerics.HNDArray‑4.0
 * data_type - e.g., microbial growth
 * n_dimensions - number of dimensions
 * is_mapped - 0 or 1 indicating mapped status
 * value_types - list of value types in the object (there will only be 1 for NDArray objects), e.g., "specific activity"
 * scalar_types - list of scalar types in the object (there will only be 1 for NDArray objects), e.g., "float"
 * dimension_types - a string describing each dimension (e.g., "media name")
 * dimension_sizes - size (length) of each dimension
 * dimension_value_types - a string describing each context of each dimension (e.g., "media name")
 * dimension_scalar_types - type of values in each context of each dimension (e.g., "int")
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "object_type",
    "data_type",
    "n_dimensions",
    "is_mapped",
    "value_types",
    "scalar_types",
    "dimension_types",
    "dimension_sizes",
    "has_unique_subindices",
    "dimension_value_types",
    "dimension_scalar_types"
})
public class GenericMetadata {

    @JsonProperty("object_type")
    private java.lang.String objectType;
    @JsonProperty("data_type")
    private java.lang.String dataType;
    @JsonProperty("n_dimensions")
    private java.lang.Long nDimensions;
    @JsonProperty("is_mapped")
    private java.lang.Long isMapped;
    @JsonProperty("value_types")
    private List<String> valueTypes;
    @JsonProperty("scalar_types")
    private List<String> scalarTypes;
    @JsonProperty("dimension_types")
    private List<String> dimensionTypes;
    @JsonProperty("dimension_sizes")
    private List<Long> dimensionSizes;
    @JsonProperty("has_unique_subindices")
    private List<Long> hasUniqueSubindices;
    @JsonProperty("dimension_value_types")
    private List<List<String>> dimensionValueTypes;
    @JsonProperty("dimension_scalar_types")
    private List<List<String>> dimensionScalarTypes;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("object_type")
    public java.lang.String getObjectType() {
        return objectType;
    }

    @JsonProperty("object_type")
    public void setObjectType(java.lang.String objectType) {
        this.objectType = objectType;
    }

    public GenericMetadata withObjectType(java.lang.String objectType) {
        this.objectType = objectType;
        return this;
    }

    @JsonProperty("data_type")
    public java.lang.String getDataType() {
        return dataType;
    }

    @JsonProperty("data_type")
    public void setDataType(java.lang.String dataType) {
        this.dataType = dataType;
    }

    public GenericMetadata withDataType(java.lang.String dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("n_dimensions")
    public java.lang.Long getNDimensions() {
        return nDimensions;
    }

    @JsonProperty("n_dimensions")
    public void setNDimensions(java.lang.Long nDimensions) {
        this.nDimensions = nDimensions;
    }

    public GenericMetadata withNDimensions(java.lang.Long nDimensions) {
        this.nDimensions = nDimensions;
        return this;
    }

    @JsonProperty("is_mapped")
    public java.lang.Long getIsMapped() {
        return isMapped;
    }

    @JsonProperty("is_mapped")
    public void setIsMapped(java.lang.Long isMapped) {
        this.isMapped = isMapped;
    }

    public GenericMetadata withIsMapped(java.lang.Long isMapped) {
        this.isMapped = isMapped;
        return this;
    }

    @JsonProperty("value_types")
    public List<String> getValueTypes() {
        return valueTypes;
    }

    @JsonProperty("value_types")
    public void setValueTypes(List<String> valueTypes) {
        this.valueTypes = valueTypes;
    }

    public GenericMetadata withValueTypes(List<String> valueTypes) {
        this.valueTypes = valueTypes;
        return this;
    }

    @JsonProperty("scalar_types")
    public List<String> getScalarTypes() {
        return scalarTypes;
    }

    @JsonProperty("scalar_types")
    public void setScalarTypes(List<String> scalarTypes) {
        this.scalarTypes = scalarTypes;
    }

    public GenericMetadata withScalarTypes(List<String> scalarTypes) {
        this.scalarTypes = scalarTypes;
        return this;
    }

    @JsonProperty("dimension_types")
    public List<String> getDimensionTypes() {
        return dimensionTypes;
    }

    @JsonProperty("dimension_types")
    public void setDimensionTypes(List<String> dimensionTypes) {
        this.dimensionTypes = dimensionTypes;
    }

    public GenericMetadata withDimensionTypes(List<String> dimensionTypes) {
        this.dimensionTypes = dimensionTypes;
        return this;
    }

    @JsonProperty("dimension_sizes")
    public List<Long> getDimensionSizes() {
        return dimensionSizes;
    }

    @JsonProperty("dimension_sizes")
    public void setDimensionSizes(List<Long> dimensionSizes) {
        this.dimensionSizes = dimensionSizes;
    }

    public GenericMetadata withDimensionSizes(List<Long> dimensionSizes) {
        this.dimensionSizes = dimensionSizes;
        return this;
    }

    @JsonProperty("has_unique_subindices")
    public List<Long> getHasUniqueSubindices() {
        return hasUniqueSubindices;
    }

    @JsonProperty("has_unique_subindices")
    public void setHasUniqueSubindices(List<Long> hasUniqueSubindices) {
        this.hasUniqueSubindices = hasUniqueSubindices;
    }

    public GenericMetadata withHasUniqueSubindices(List<Long> hasUniqueSubindices) {
        this.hasUniqueSubindices = hasUniqueSubindices;
        return this;
    }

    @JsonProperty("dimension_value_types")
    public List<List<String>> getDimensionValueTypes() {
        return dimensionValueTypes;
    }

    @JsonProperty("dimension_value_types")
    public void setDimensionValueTypes(List<List<String>> dimensionValueTypes) {
        this.dimensionValueTypes = dimensionValueTypes;
    }

    public GenericMetadata withDimensionValueTypes(List<List<String>> dimensionValueTypes) {
        this.dimensionValueTypes = dimensionValueTypes;
        return this;
    }

    @JsonProperty("dimension_scalar_types")
    public List<List<String>> getDimensionScalarTypes() {
        return dimensionScalarTypes;
    }

    @JsonProperty("dimension_scalar_types")
    public void setDimensionScalarTypes(List<List<String>> dimensionScalarTypes) {
        this.dimensionScalarTypes = dimensionScalarTypes;
    }

    public GenericMetadata withDimensionScalarTypes(List<List<String>> dimensionScalarTypes) {
        this.dimensionScalarTypes = dimensionScalarTypes;
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
        return ((((((((((((((((((((((((("GenericMetadata"+" [objectType=")+ objectType)+", dataType=")+ dataType)+", nDimensions=")+ nDimensions)+", isMapped=")+ isMapped)+", valueTypes=")+ valueTypes)+", scalarTypes=")+ scalarTypes)+", dimensionTypes=")+ dimensionTypes)+", dimensionSizes=")+ dimensionSizes)+", hasUniqueSubindices=")+ hasUniqueSubindices)+", dimensionValueTypes=")+ dimensionValueTypes)+", dimensionScalarTypes=")+ dimensionScalarTypes)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
