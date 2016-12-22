
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
 * <p>Original spec-file type: MatrixMetadataItem</p>
 * <pre>
 * One piece of metadata describing the entire matrix.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "metadata",
    "value"
})
public class MatrixMetadataItem {

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
     * <p>Original spec-file type: DataValue</p>
     * <pre>
     * This stores a single piece of metadata.  Depending on the setting
     * of "primitive type", the data will be stored in one of the
     * following places:
     * int - int_data
     * boolean - int_data (as a 0 or 1)
     * float - float_data
     * string - string_data
     * object - object_ref_data (NOTE: subobject refs may need to be stored as 2 strings?
     *                           We may also want to store the type of object that the ref maps to, which
     *                           is currently stored in the ontology; e.g., KBaseGenomeAnnotations.Taxon)
     * ontology - ontology_ref_data (NOTE: could also store in same string as object)
     * </pre>
     * 
     */
    @JsonProperty("value")
    private DataValue value;
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

    public MatrixMetadataItem withMetadata(MetadataItem metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * <p>Original spec-file type: DataValue</p>
     * <pre>
     * This stores a single piece of metadata.  Depending on the setting
     * of "primitive type", the data will be stored in one of the
     * following places:
     * int - int_data
     * boolean - int_data (as a 0 or 1)
     * float - float_data
     * string - string_data
     * object - object_ref_data (NOTE: subobject refs may need to be stored as 2 strings?
     *                           We may also want to store the type of object that the ref maps to, which
     *                           is currently stored in the ontology; e.g., KBaseGenomeAnnotations.Taxon)
     * ontology - ontology_ref_data (NOTE: could also store in same string as object)
     * </pre>
     * 
     */
    @JsonProperty("value")
    public DataValue getValue() {
        return value;
    }

    /**
     * <p>Original spec-file type: DataValue</p>
     * <pre>
     * This stores a single piece of metadata.  Depending on the setting
     * of "primitive type", the data will be stored in one of the
     * following places:
     * int - int_data
     * boolean - int_data (as a 0 or 1)
     * float - float_data
     * string - string_data
     * object - object_ref_data (NOTE: subobject refs may need to be stored as 2 strings?
     *                           We may also want to store the type of object that the ref maps to, which
     *                           is currently stored in the ontology; e.g., KBaseGenomeAnnotations.Taxon)
     * ontology - ontology_ref_data (NOTE: could also store in same string as object)
     * </pre>
     * 
     */
    @JsonProperty("value")
    public void setValue(DataValue value) {
        this.value = value;
    }

    public MatrixMetadataItem withValue(DataValue value) {
        this.value = value;
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
        return ((((((("MatrixMetadataItem"+" [metadata=")+ metadata)+", value=")+ value)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
