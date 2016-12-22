
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "primitive_type",
    "object_ref_data",
    "ontology_ref_data",
    "int_data",
    "float_data",
    "string_data"
})
public class DataValue {

    @JsonProperty("primitive_type")
    private String primitiveType;
    @JsonProperty("object_ref_data")
    private String objectRefData;
    @JsonProperty("ontology_ref_data")
    private String ontologyRefData;
    @JsonProperty("int_data")
    private Long intData;
    @JsonProperty("float_data")
    private Double floatData;
    @JsonProperty("string_data")
    private String stringData;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("primitive_type")
    public String getPrimitiveType() {
        return primitiveType;
    }

    @JsonProperty("primitive_type")
    public void setPrimitiveType(String primitiveType) {
        this.primitiveType = primitiveType;
    }

    public DataValue withPrimitiveType(String primitiveType) {
        this.primitiveType = primitiveType;
        return this;
    }

    @JsonProperty("object_ref_data")
    public String getObjectRefData() {
        return objectRefData;
    }

    @JsonProperty("object_ref_data")
    public void setObjectRefData(String objectRefData) {
        this.objectRefData = objectRefData;
    }

    public DataValue withObjectRefData(String objectRefData) {
        this.objectRefData = objectRefData;
        return this;
    }

    @JsonProperty("ontology_ref_data")
    public String getOntologyRefData() {
        return ontologyRefData;
    }

    @JsonProperty("ontology_ref_data")
    public void setOntologyRefData(String ontologyRefData) {
        this.ontologyRefData = ontologyRefData;
    }

    public DataValue withOntologyRefData(String ontologyRefData) {
        this.ontologyRefData = ontologyRefData;
        return this;
    }

    @JsonProperty("int_data")
    public Long getIntData() {
        return intData;
    }

    @JsonProperty("int_data")
    public void setIntData(Long intData) {
        this.intData = intData;
    }

    public DataValue withIntData(Long intData) {
        this.intData = intData;
        return this;
    }

    @JsonProperty("float_data")
    public Double getFloatData() {
        return floatData;
    }

    @JsonProperty("float_data")
    public void setFloatData(Double floatData) {
        this.floatData = floatData;
    }

    public DataValue withFloatData(Double floatData) {
        this.floatData = floatData;
        return this;
    }

    @JsonProperty("string_data")
    public String getStringData() {
        return stringData;
    }

    @JsonProperty("string_data")
    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    public DataValue withStringData(String stringData) {
        this.stringData = stringData;
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
        return ((((((((((((((("DataValue"+" [primitiveType=")+ primitiveType)+", objectRefData=")+ objectRefData)+", ontologyRefData=")+ ontologyRefData)+", intData=")+ intData)+", floatData=")+ floatData)+", stringData=")+ stringData)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
