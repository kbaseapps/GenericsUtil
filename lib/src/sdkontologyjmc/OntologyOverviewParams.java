
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
 * <p>Original spec-file type: OntologyOverviewParams</p>
 * <pre>
 * Ontology overview
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology_dictionary_ref"
})
public class OntologyOverviewParams {

    @JsonProperty("ontology_dictionary_ref")
    private List<String> ontologyDictionaryRef;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("ontology_dictionary_ref")
    public List<String> getOntologyDictionaryRef() {
        return ontologyDictionaryRef;
    }

    @JsonProperty("ontology_dictionary_ref")
    public void setOntologyDictionaryRef(List<String> ontologyDictionaryRef) {
        this.ontologyDictionaryRef = ontologyDictionaryRef;
    }

    public OntologyOverviewParams withOntologyDictionaryRef(List<String> ontologyDictionaryRef) {
        this.ontologyDictionaryRef = ontologyDictionaryRef;
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
        return ((((("OntologyOverviewParams"+" [ontologyDictionaryRef=")+ ontologyDictionaryRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
