
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
 * <p>Original spec-file type: M_Term</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "term_name",
    "oterms"
})
public class MTerm {

    @JsonProperty("term_name")
    private String termName;
    @JsonProperty("oterms")
    private List<OTerm> oterms;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("term_name")
    public String getTermName() {
        return termName;
    }

    @JsonProperty("term_name")
    public void setTermName(String termName) {
        this.termName = termName;
    }

    public MTerm withTermName(String termName) {
        this.termName = termName;
        return this;
    }

    @JsonProperty("oterms")
    public List<OTerm> getOterms() {
        return oterms;
    }

    @JsonProperty("oterms")
    public void setOterms(List<OTerm> oterms) {
        this.oterms = oterms;
    }

    public MTerm withOterms(List<OTerm> oterms) {
        this.oterms = oterms;
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
        return ((((((("MTerm"+" [termName=")+ termName)+", oterms=")+ oterms)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
