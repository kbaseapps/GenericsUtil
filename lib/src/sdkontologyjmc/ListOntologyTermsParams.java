
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
 * <p>Original spec-file type: ListOntologyTermsParams</p>
 * <pre>
 * workspace - the name of the workspace for input/output
 * ontology_dictionary - reference to ontology dictionary
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology_dictionary_ref"
})
public class ListOntologyTermsParams {

    @JsonProperty("ontology_dictionary_ref")
    private String ontologyDictionaryRef;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ontology_dictionary_ref")
    public String getOntologyDictionaryRef() {
        return ontologyDictionaryRef;
    }

    @JsonProperty("ontology_dictionary_ref")
    public void setOntologyDictionaryRef(String ontologyDictionaryRef) {
        this.ontologyDictionaryRef = ontologyDictionaryRef;
    }

    public ListOntologyTermsParams withOntologyDictionaryRef(String ontologyDictionaryRef) {
        this.ontologyDictionaryRef = ontologyDictionaryRef;
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
        return ((((("ListOntologyTermsParams"+" [ontologyDictionaryRef=")+ ontologyDictionaryRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
