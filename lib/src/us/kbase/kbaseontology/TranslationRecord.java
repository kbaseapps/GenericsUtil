
package us.kbase.kbaseontology;

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
 * <p>Original spec-file type: TranslationRecord</p>
 * <pre>
 * @optional name
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "equiv_terms"
})
public class TranslationRecord {

    @JsonProperty("name")
    private String name;
    @JsonProperty("equiv_terms")
    private List<EquivalentTerm> equivTerms;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public TranslationRecord withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("equiv_terms")
    public List<EquivalentTerm> getEquivTerms() {
        return equivTerms;
    }

    @JsonProperty("equiv_terms")
    public void setEquivTerms(List<EquivalentTerm> equivTerms) {
        this.equivTerms = equivTerms;
    }

    public TranslationRecord withEquivTerms(List<EquivalentTerm> equivTerms) {
        this.equivTerms = equivTerms;
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
        return ((((((("TranslationRecord"+" [name=")+ name)+", equivTerms=")+ equivTerms)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
