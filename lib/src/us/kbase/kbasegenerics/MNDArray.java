
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
 * <p>Original spec-file type: M_NDArray</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "data_type",
    "n_dimensions",
    "dim_context",
    "typed_values"
})
public class MNDArray {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    private MTerm dataType;
    @JsonProperty("n_dimensions")
    private Long nDimensions;
    @JsonProperty("dim_context")
    private List<MDimensionContext> dimContext;
    /**
     * <p>Original spec-file type: M_TypedValues</p>
     * 
     * 
     */
    @JsonProperty("typed_values")
    private MTypedValues typedValues;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public MNDArray withName(String name) {
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

    public MNDArray withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    public MTerm getDataType() {
        return dataType;
    }

    /**
     * <p>Original spec-file type: M_Term</p>
     * 
     * 
     */
    @JsonProperty("data_type")
    public void setDataType(MTerm dataType) {
        this.dataType = dataType;
    }

    public MNDArray withDataType(MTerm dataType) {
        this.dataType = dataType;
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

    public MNDArray withNDimensions(Long nDimensions) {
        this.nDimensions = nDimensions;
        return this;
    }

    @JsonProperty("dim_context")
    public List<MDimensionContext> getDimContext() {
        return dimContext;
    }

    @JsonProperty("dim_context")
    public void setDimContext(List<MDimensionContext> dimContext) {
        this.dimContext = dimContext;
    }

    public MNDArray withDimContext(List<MDimensionContext> dimContext) {
        this.dimContext = dimContext;
        return this;
    }

    /**
     * <p>Original spec-file type: M_TypedValues</p>
     * 
     * 
     */
    @JsonProperty("typed_values")
    public MTypedValues getTypedValues() {
        return typedValues;
    }

    /**
     * <p>Original spec-file type: M_TypedValues</p>
     * 
     * 
     */
    @JsonProperty("typed_values")
    public void setTypedValues(MTypedValues typedValues) {
        this.typedValues = typedValues;
    }

    public MNDArray withTypedValues(MTypedValues typedValues) {
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
        return ((((((((((((((("MNDArray"+" [name=")+ name)+", description=")+ description)+", dataType=")+ dataType)+", nDimensions=")+ nDimensions)+", dimContext=")+ dimContext)+", typedValues=")+ typedValues)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
