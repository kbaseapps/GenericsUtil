
package us.kbase.kbasegenerics;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: Term</p>
 * <pre>
 * @optional oterm_ref oterm_name
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "term_name",
    "oterm_ref",
    "oterm_name"
})
public class Term {

    @JsonProperty("term_name")
    private String termName;
    @JsonProperty("oterm_ref")
    private String otermRef;
    @JsonProperty("oterm_name")
    private String otermName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("term_name")
    public String getTermName() {
        return termName;
    }

    @JsonProperty("term_name")
    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Term withTermName(String termName) {
        this.termName = termName;
        return this;
    }

    @JsonProperty("oterm_ref")
    public String getOtermRef() {
        return otermRef;
    }

    @JsonProperty("oterm_ref")
    public void setOtermRef(String otermRef) {
        this.otermRef = otermRef;
    }

    public Term withOtermRef(String otermRef) {
        this.otermRef = otermRef;
        return this;
    }

    @JsonProperty("oterm_name")
    public String getOtermName() {
        return otermName;
    }

    @JsonProperty("oterm_name")
    public void setOtermName(String otermName) {
        this.otermName = otermName;
    }

    public Term withOtermName(String otermName) {
        this.otermName = otermName;
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
        return ((((((((("Term"+" [termName=")+ termName)+", otermRef=")+ otermRef)+", otermName=")+ otermName)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
