
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
 * <p>Original spec-file type: HNDArray</p>
 * <pre>
 * @optional array_context
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "data_type",
    "array_context",
    "n_dimensions",
    "dim_context",
    "typed_values"
})
public class HNDArray {

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
    private List<TypedValue> arrayContext;
    @JsonProperty("n_dimensions")
    private Long nDimensions;
    @JsonProperty("dim_context")
    private List<DimensionContext> dimContext;
    @JsonProperty("typed_values")
    private List<TypedValues> typedValues;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public HNDArray withName(String name) {
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

    public HNDArray withDescription(String description) {
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

    public HNDArray withDataType(Term dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("array_context")
    public List<TypedValue> getArrayContext() {
        return arrayContext;
    }

    @JsonProperty("array_context")
    public void setArrayContext(List<TypedValue> arrayContext) {
        this.arrayContext = arrayContext;
    }

    public HNDArray withArrayContext(List<TypedValue> arrayContext) {
        this.arrayContext = arrayContext;
        return this;
    }

    @JsonProperty("n_dimensions")
    public Long getNDimensions() {
        return nDimensions;
    }

    @JsonProperty("n_dimensions")
    public void setNDimensions(Long nDimensions) {
        this.nDimensions = nDimensions;
    }

    public HNDArray withNDimensions(Long nDimensions) {
        this.nDimensions = nDimensions;
        return this;
    }

    @JsonProperty("dim_context")
    public List<DimensionContext> getDimContext() {
        return dimContext;
    }

    @JsonProperty("dim_context")
    public void setDimContext(List<DimensionContext> dimContext) {
        this.dimContext = dimContext;
    }

    public HNDArray withDimContext(List<DimensionContext> dimContext) {
        this.dimContext = dimContext;
        return this;
    }

    @JsonProperty("typed_values")
    public List<TypedValues> getTypedValues() {
        return typedValues;
    }

    @JsonProperty("typed_values")
    public void setTypedValues(List<TypedValues> typedValues) {
        this.typedValues = typedValues;
    }

    public HNDArray withTypedValues(List<TypedValues> typedValues) {
        this.typedValues = typedValues;
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
        return ((((((((((((((((("HNDArray"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", arrayContext=")+ arrayContext)+", nDimensions=")+ nDimensions)+", dimContext=")+ dimContext)+", typedValues=")+ typedValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
