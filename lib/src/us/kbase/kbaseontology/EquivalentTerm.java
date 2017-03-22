
package us.kbase.kbaseontology;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: EquivalentTerm</p>
 * <pre>
 * @optional equiv_name
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "equiv_term",
    "equiv_name"
})
public class EquivalentTerm {

    @JsonProperty("equiv_term")
    private String equivTerm;
    @JsonProperty("equiv_name")
    private String equivName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("equiv_term")
    public String getEquivTerm() {
        return equivTerm;
    }

    @JsonProperty("equiv_term")
    public void setEquivTerm(String equivTerm) {
        this.equivTerm = equivTerm;
    }

    public EquivalentTerm withEquivTerm(String equivTerm) {
        this.equivTerm = equivTerm;
        return this;
    }

    @JsonProperty("equiv_name")
    public String getEquivName() {
        return equivName;
    }

    @JsonProperty("equiv_name")
    public void setEquivName(String equivName) {
        this.equivName = equivName;
    }

    public EquivalentTerm withEquivName(String equivName) {
        this.equivName = equivName;
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
        return ((((((("EquivalentTerm"+" [equivTerm=")+ equivTerm)+", equivName=")+ equivName)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
