
package us.kbase.kbasegenerics;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: DimensionMetadataItem</p>
 * <pre>
 * One piece of metadata describing a dimension of a matrix; e.g., row or column.  The DataValues
 * object must be the same length as the dimension, even if some of the actual values are null.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "metadata",
    "values"
})
public class DimensionMetadataItem {

    /**
     * <p>Original spec-file type: MetadataItem</p>
     * <pre>
     * A single piece of metadata (i.e., metadatum)
     * </pre>
     * 
     */
    @JsonProperty("metadata")
    private MetadataItem metadata;
    /**
     * <p>Original spec-file type: DataValues</p>
     * <pre>
     * This stores values for either data or metadata, along with
     * metadata describing it.  This is just like DataValue, but with an array
     * of data.  n is the length of the list being used.  Other lists are null.
     * The list being used may include some null values.
     * </pre>
     * 
     */
    @JsonProperty("values")
    private DataValues values;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * <p>Original spec-file type: MetadataItem</p>
     * <pre>
     * A single piece of metadata (i.e., metadatum)
     * </pre>
     * 
     */
    @JsonProperty("metadata")
    public MetadataItem getMetadata() {
        return metadata;
    }

    /**
     * <p>Original spec-file type: MetadataItem</p>
     * <pre>
     * A single piece of metadata (i.e., metadatum)
     * </pre>
     * 
     */
    @JsonProperty("metadata")
    public void setMetadata(MetadataItem metadata) {
        this.metadata = metadata;
    }

    public DimensionMetadataItem withMetadata(MetadataItem metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * <p>Original spec-file type: DataValues</p>
     * <pre>
     * This stores values for either data or metadata, along with
     * metadata describing it.  This is just like DataValue, but with an array
     * of data.  n is the length of the list being used.  Other lists are null.
     * The list being used may include some null values.
     * </pre>
     * 
     */
    @JsonProperty("values")
    public DataValues getValues() {
        return values;
    }

    /**
     * <p>Original spec-file type: DataValues</p>
     * <pre>
     * This stores values for either data or metadata, along with
     * metadata describing it.  This is just like DataValue, but with an array
     * of data.  n is the length of the list being used.  Other lists are null.
     * The list being used may include some null values.
     * </pre>
     * 
     */
    @JsonProperty("values")
    public void setValues(DataValues values) {
        this.values = values;
    }

    public DimensionMetadataItem withValues(DataValues values) {
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
        return ((((((("DimensionMetadataItem"+" [metadata=")+ metadata)+", values=")+ values)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
