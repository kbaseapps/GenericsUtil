
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
 * <p>Original spec-file type: OntologyTermsOut</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology",
    "namespace",
    "term_id"
})
public class OntologyTermsOut {

    @JsonProperty("ontology")
    private java.lang.String ontology;
    @JsonProperty("namespace")
    private java.lang.String namespace;
    @JsonProperty("term_id")
    private List<String> termId;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("ontology")
    public java.lang.String getOntology() {
        return ontology;
    }

    @JsonProperty("ontology")
    public void setOntology(java.lang.String ontology) {
        this.ontology = ontology;
    }

    public OntologyTermsOut withOntology(java.lang.String ontology) {
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

    public OntologyTermsOut withNamespace(java.lang.String namespace) {
        this.namespace = namespace;
        return this;
    }

    @JsonProperty("term_id")
    public List<String> getTermId() {
        return termId;
    }

    @JsonProperty("term_id")
    public void setTermId(List<String> termId) {
        this.termId = termId;
    }

    public OntologyTermsOut withTermId(List<String> termId) {
        this.termId = termId;
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
        return ((((((((("OntologyTermsOut"+" [ontology=")+ ontology)+", namespace=")+ namespace)+", termId=")+ termId)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
