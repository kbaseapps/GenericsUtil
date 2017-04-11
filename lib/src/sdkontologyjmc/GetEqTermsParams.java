
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
 * <p>Original spec-file type: GetEqTermsParams</p>
 * <pre>
 * get equivalent terms
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology_trans_ref",
    "term_ids"
})
public class GetEqTermsParams {

    @JsonProperty("ontology_trans_ref")
    private java.lang.String ontologyTransRef;
    @JsonProperty("term_ids")
    private List<String> termIds;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("ontology_trans_ref")
    public java.lang.String getOntologyTransRef() {
        return ontologyTransRef;
    }

    @JsonProperty("ontology_trans_ref")
    public void setOntologyTransRef(java.lang.String ontologyTransRef) {
        this.ontologyTransRef = ontologyTransRef;
    }

    public GetEqTermsParams withOntologyTransRef(java.lang.String ontologyTransRef) {
        this.ontologyTransRef = ontologyTransRef;
        return this;
    }

    @JsonProperty("term_ids")
    public List<String> getTermIds() {
        return termIds;
    }

    @JsonProperty("term_ids")
    public void setTermIds(List<String> termIds) {
        this.termIds = termIds;
    }

    public GetEqTermsParams withTermIds(List<String> termIds) {
        this.termIds = termIds;
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
        return ((((((("GetEqTermsParams"+" [ontologyTransRef=")+ ontologyTransRef)+", termIds=")+ termIds)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
