
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
 * <p>Original spec-file type: term_info_list</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "terms"
})
public class TermInfoList {

    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("terms")
    private List<String> terms;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public TermInfoList withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("terms")
    public List<String> getTerms() {
        return terms;
    }

    @JsonProperty("terms")
    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public TermInfoList withTerms(List<String> terms) {
        this.terms = terms;
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
        return ((((((("TermInfoList"+" [name=")+ name)+", terms=")+ terms)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
