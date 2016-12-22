
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
 * <p>Original spec-file type: DataValues</p>
 * <pre>
 * This stores values for either data or metadata, along with
 * metadata describing it.  This is just like DataValue, but with an array
 * of data.  n is the length of the list being used.  Other lists are null.
 * The list being used may include some null values.
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "primitive_type",
    "n",
    "object_ref_data",
    "ontology_ref_data",
    "int_data",
    "float_data",
    "string_data"
})
public class DataValues {

    @JsonProperty("primitive_type")
    private java.lang.String primitiveType;
    @JsonProperty("n")
    private java.lang.Long n;
    @JsonProperty("object_ref_data")
    private List<String> objectRefData;
    @JsonProperty("ontology_ref_data")
    private List<String> ontologyRefData;
    @JsonProperty("int_data")
    private List<Long> intData;
    @JsonProperty("float_data")
    private List<Double> floatData;
    @JsonProperty("string_data")
    private List<String> stringData;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("primitive_type")
    public java.lang.String getPrimitiveType() {
        return primitiveType;
    }

    @JsonProperty("primitive_type")
    public void setPrimitiveType(java.lang.String primitiveType) {
        this.primitiveType = primitiveType;
    }

    public DataValues withPrimitiveType(java.lang.String primitiveType) {
        this.primitiveType = primitiveType;
        return this;
    }

    @JsonProperty("n")
    public java.lang.Long getN() {
        return n;
    }

    @JsonProperty("n")
    public void setN(java.lang.Long n) {
        this.n = n;
    }

    public DataValues withN(java.lang.Long n) {
        this.n = n;
        return this;
    }

    @JsonProperty("object_ref_data")
    public List<String> getObjectRefData() {
        return objectRefData;
    }

    @JsonProperty("object_ref_data")
    public void setObjectRefData(List<String> objectRefData) {
        this.objectRefData = objectRefData;
    }

    public DataValues withObjectRefData(List<String> objectRefData) {
        this.objectRefData = objectRefData;
        return this;
    }

    @JsonProperty("ontology_ref_data")
    public List<String> getOntologyRefData() {
        return ontologyRefData;
    }

    @JsonProperty("ontology_ref_data")
    public void setOntologyRefData(List<String> ontologyRefData) {
        this.ontologyRefData = ontologyRefData;
    }

    public DataValues withOntologyRefData(List<String> ontologyRefData) {
        this.ontologyRefData = ontologyRefData;
        return this;
    }

    @JsonProperty("int_data")
    public List<Long> getIntData() {
        return intData;
    }

    @JsonProperty("int_data")
    public void setIntData(List<Long> intData) {
        this.intData = intData;
    }

    public DataValues withIntData(List<Long> intData) {
        this.intData = intData;
        return this;
    }

    @JsonProperty("float_data")
    public List<Double> getFloatData() {
        return floatData;
    }

    @JsonProperty("float_data")
    public void setFloatData(List<Double> floatData) {
        this.floatData = floatData;
    }

    public DataValues withFloatData(List<Double> floatData) {
        this.floatData = floatData;
        return this;
    }

    @JsonProperty("string_data")
    public List<String> getStringData() {
        return stringData;
    }

    @JsonProperty("string_data")
    public void setStringData(List<String> stringData) {
        this.stringData = stringData;
    }

    public DataValues withStringData(List<String> stringData) {
        this.stringData = stringData;
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
        return ((((((((((((((((("DataValues"+" [primitiveType=")+ primitiveType)+", n=")+ n)+", objectRefData=")+ objectRefData)+", ontologyRefData=")+ ontologyRefData)+", intData=")+ intData)+", floatData=")+ floatData)+", stringData=")+ stringData)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
