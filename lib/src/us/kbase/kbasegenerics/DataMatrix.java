
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
 * <p>Original spec-file type: DataMatrix</p>
 * <pre>
 * A multi-dimensional generic data matrix.  When matrix is first uploaded, ontologies
 * will not yet be mapped, so ontologies_mapped will be 0 and all text descriptions will be
 * stored in the "original_description" string of MetadataItem objects.  After mapping
 * all ontological terms and references to other KBase objects, ontologies_mapped will be
 * set to 1, and we will allow users to use the object (e.g., display graphs).
 * n_dimensions - number of dimensions of data
 * dimension_length - the length of each dimension
 * data are stored in the "data" array, in row major order
 * values_metadata describes what the data are (e.g., units, what is being measured)
 * matrix_metadata contains other optional properties describing the whole matrix (e.g.,
 *                                                                                 other metadata describing the experiment)
 * dimension_metadata stores a list of properties of each dimension.  Each dimension may
 * have multiple types of metadata; e.g., nitrate concentration and formate concentration.
 * @optional name description matrix_metadata
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "description",
    "ontologies_mapped",
    "n_dimensions",
    "dimension_length",
    "data",
    "values_metadata",
    "matrix_metadata",
    "dimension_metadata"
})
public class DataMatrix {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("ontologies_mapped")
    private java.lang.Long ontologiesMapped;
    @JsonProperty("n_dimensions")
    private java.lang.Long nDimensions;
    @JsonProperty("dimension_length")
    private List<Long> dimensionLength;
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
    @JsonProperty("data")
    private DataValues data;
    /**
     * <p>Original spec-file type: MatrixMetadataItem</p>
     * <pre>
     * One piece of metadata describing the entire matrix.
     * </pre>
     * 
     */
    @JsonProperty("values_metadata")
    private us.kbase.kbasegenerics.MatrixMetadataItem valuesMetadata;
    @JsonProperty("matrix_metadata")
    private List<us.kbase.kbasegenerics.MatrixMetadataItem> matrixMetadata;
    @JsonProperty("dimension_metadata")
    private List<List<DimensionMetadataItem>> dimensionMetadata;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public DataMatrix withName(String name) {
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

    public DataMatrix withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("ontologies_mapped")
    public java.lang.Long getOntologiesMapped() {
        return ontologiesMapped;
    }

    @JsonProperty("ontologies_mapped")
    public void setOntologiesMapped(java.lang.Long ontologiesMapped) {
        this.ontologiesMapped = ontologiesMapped;
    }

    public DataMatrix withOntologiesMapped(java.lang.Long ontologiesMapped) {
        this.ontologiesMapped = ontologiesMapped;
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

    public DataMatrix withNDimensions(java.lang.Long nDimensions) {
        this.nDimensions = nDimensions;
        return this;
    }

    @JsonProperty("dimension_length")
    public List<Long> getDimensionLength() {
        return dimensionLength;
    }

    @JsonProperty("dimension_length")
    public void setDimensionLength(List<Long> dimensionLength) {
        this.dimensionLength = dimensionLength;
    }

    public DataMatrix withDimensionLength(List<Long> dimensionLength) {
        this.dimensionLength = dimensionLength;
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
    @JsonProperty("data")
    public DataValues getData() {
        return data;
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
    @JsonProperty("data")
    public void setData(DataValues data) {
        this.data = data;
    }

    public DataMatrix withData(DataValues data) {
        this.data = data;
        return this;
    }

    /**
     * <p>Original spec-file type: MatrixMetadataItem</p>
     * <pre>
     * One piece of metadata describing the entire matrix.
     * </pre>
     * 
     */
    @JsonProperty("values_metadata")
    public us.kbase.kbasegenerics.MatrixMetadataItem getValuesMetadata() {
        return valuesMetadata;
    }

    /**
     * <p>Original spec-file type: MatrixMetadataItem</p>
     * <pre>
     * One piece of metadata describing the entire matrix.
     * </pre>
     * 
     */
    @JsonProperty("values_metadata")
    public void setValuesMetadata(us.kbase.kbasegenerics.MatrixMetadataItem valuesMetadata) {
        this.valuesMetadata = valuesMetadata;
    }

    public DataMatrix withValuesMetadata(us.kbase.kbasegenerics.MatrixMetadataItem valuesMetadata) {
        this.valuesMetadata = valuesMetadata;
        return this;
    }

    @JsonProperty("matrix_metadata")
    public List<us.kbase.kbasegenerics.MatrixMetadataItem> getMatrixMetadata() {
        return matrixMetadata;
    }

    @JsonProperty("matrix_metadata")
    public void setMatrixMetadata(List<us.kbase.kbasegenerics.MatrixMetadataItem> matrixMetadata) {
        this.matrixMetadata = matrixMetadata;
    }

    public DataMatrix withMatrixMetadata(List<us.kbase.kbasegenerics.MatrixMetadataItem> matrixMetadata) {
        this.matrixMetadata = matrixMetadata;
        return this;
    }

    @JsonProperty("dimension_metadata")
    public List<List<DimensionMetadataItem>> getDimensionMetadata() {
        return dimensionMetadata;
    }

    @JsonProperty("dimension_metadata")
    public void setDimensionMetadata(List<List<DimensionMetadataItem>> dimensionMetadata) {
        this.dimensionMetadata = dimensionMetadata;
    }

    public DataMatrix withDimensionMetadata(List<List<DimensionMetadataItem>> dimensionMetadata) {
        this.dimensionMetadata = dimensionMetadata;
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
        return ((((((((((((((((((((("DataMatrix"+" [name=")+ name)+", description=")+ description)+", ontologiesMapped=")+ ontologiesMapped)+", nDimensions=")+ nDimensions)+", dimensionLength=")+ dimensionLength)+", data=")+ data)+", valuesMetadata=")+ valuesMetadata)+", matrixMetadata=")+ matrixMetadata)+", dimensionMetadata=")+ dimensionMetadata)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
