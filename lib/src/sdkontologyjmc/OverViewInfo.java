
package sdkontologyjmc;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: overViewInfo</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology",
    "namespace",
    "data_version",
    "format_version",
    "number_of_terms",
    "dictionary_ref"
})
public class OverViewInfo {

    @JsonProperty("ontology")
    private String ontology;
    @JsonProperty("namespace")
    private String namespace;
    @JsonProperty("data_version")
    private String dataVersion;
    @JsonProperty("format_version")
    private String formatVersion;
    @JsonProperty("number_of_terms")
    private Long numberOfTerms;
    @JsonProperty("dictionary_ref")
    private String dictionaryRef;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ontology")
    public String getOntology() {
        return ontology;
    }

    @JsonProperty("ontology")
    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public OverViewInfo withOntology(String ontology) {
        this.ontology = ontology;
        return this;
    }

    @JsonProperty("namespace")
    public String getNamespace() {
        return namespace;
    }

    @JsonProperty("namespace")
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public OverViewInfo withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    @JsonProperty("data_version")
    public String getDataVersion() {
        return dataVersion;
    }

    @JsonProperty("data_version")
    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public OverViewInfo withDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
        return this;
    }

    @JsonProperty("format_version")
    public String getFormatVersion() {
        return formatVersion;
    }

    @JsonProperty("format_version")
    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public OverViewInfo withFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
        return this;
    }

    @JsonProperty("number_of_terms")
    public Long getNumberOfTerms() {
        return numberOfTerms;
    }

    @JsonProperty("number_of_terms")
    public void setNumberOfTerms(Long numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
    }

    public OverViewInfo withNumberOfTerms(Long numberOfTerms) {
        this.numberOfTerms = numberOfTerms;
        return this;
    }

    @JsonProperty("dictionary_ref")
    public String getDictionaryRef() {
        return dictionaryRef;
    }

    @JsonProperty("dictionary_ref")
    public void setDictionaryRef(String dictionaryRef) {
        this.dictionaryRef = dictionaryRef;
    }

    public OverViewInfo withDictionaryRef(String dictionaryRef) {
        this.dictionaryRef = dictionaryRef;
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
        return ((((((((((((((("OverViewInfo"+" [ontology=")+ ontology)+", namespace=")+ namespace)+", dataVersion=")+ dataVersion)+", formatVersion=")+ formatVersion)+", numberOfTerms=")+ numberOfTerms)+", dictionaryRef=")+ dictionaryRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
