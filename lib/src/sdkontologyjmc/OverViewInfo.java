
package sdkontologyjmc;

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
    "dictionary_ref",
    "namespace_id_rule"
})
public class OverViewInfo {

    @JsonProperty("ontology")
    private java.lang.String ontology;
    @JsonProperty("namespace")
    private java.lang.String namespace;
    @JsonProperty("data_version")
    private java.lang.String dataVersion;
    @JsonProperty("format_version")
    private java.lang.String formatVersion;
    @JsonProperty("number_of_terms")
    private Long numberOfTerms;
    @JsonProperty("dictionary_ref")
    private java.lang.String dictionaryRef;
    @JsonProperty("namespace_id_rule")
    private List<String> namespaceIdRule;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("ontology")
    public java.lang.String getOntology() {
        return ontology;
    }

    @JsonProperty("ontology")
    public void setOntology(java.lang.String ontology) {
        this.ontology = ontology;
    }

    public OverViewInfo withOntology(java.lang.String ontology) {
        this.ontology = ontology;
        return this;
    }

    @JsonProperty("namespace")
    public java.lang.String getNamespace() {
        return namespace;
    }

    @JsonProperty("namespace")
    public void setNamespace(java.lang.String namespace) {
        this.namespace = namespace;
    }

    public OverViewInfo withNamespace(java.lang.String namespace) {
        this.namespace = namespace;
        return this;
    }

    @JsonProperty("data_version")
    public java.lang.String getDataVersion() {
        return dataVersion;
    }

    @JsonProperty("data_version")
    public void setDataVersion(java.lang.String dataVersion) {
        this.dataVersion = dataVersion;
    }

    public OverViewInfo withDataVersion(java.lang.String dataVersion) {
        this.dataVersion = dataVersion;
        return this;
    }

    @JsonProperty("format_version")
    public java.lang.String getFormatVersion() {
        return formatVersion;
    }

    @JsonProperty("format_version")
    public void setFormatVersion(java.lang.String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public OverViewInfo withFormatVersion(java.lang.String formatVersion) {
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
    public java.lang.String getDictionaryRef() {
        return dictionaryRef;
    }

    @JsonProperty("dictionary_ref")
    public void setDictionaryRef(java.lang.String dictionaryRef) {
        this.dictionaryRef = dictionaryRef;
    }

    public OverViewInfo withDictionaryRef(java.lang.String dictionaryRef) {
        this.dictionaryRef = dictionaryRef;
        return this;
    }

    @JsonProperty("namespace_id_rule")
    public List<String> getNamespaceIdRule() {
        return namespaceIdRule;
    }

    @JsonProperty("namespace_id_rule")
    public void setNamespaceIdRule(List<String> namespaceIdRule) {
        this.namespaceIdRule = namespaceIdRule;
    }

    public OverViewInfo withNamespaceIdRule(List<String> namespaceIdRule) {
        this.namespaceIdRule = namespaceIdRule;
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
        return ((((((((((((((((("OverViewInfo"+" [ontology=")+ ontology)+", namespace=")+ namespace)+", dataVersion=")+ dataVersion)+", formatVersion=")+ formatVersion)+", numberOfTerms=")+ numberOfTerms)+", dictionaryRef=")+ dictionaryRef)+", namespaceIdRule=")+ namespaceIdRule)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
