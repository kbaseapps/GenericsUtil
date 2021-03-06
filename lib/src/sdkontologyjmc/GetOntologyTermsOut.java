
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
 * <p>Original spec-file type: GetOntologyTermsOut</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "term_info"
})
public class GetOntologyTermsOut {

    @JsonProperty("term_info")
    private Map<String, TermInfo> termInfo;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("term_info")
    public Map<String, TermInfo> getTermInfo() {
        return termInfo;
    }

    @JsonProperty("term_info")
    public void setTermInfo(Map<String, TermInfo> termInfo) {
        this.termInfo = termInfo;
    }

    public GetOntologyTermsOut withTermInfo(Map<String, TermInfo> termInfo) {
        this.termInfo = termInfo;
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
        return ((((("GetOntologyTermsOut"+" [termInfo=")+ termInfo)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
