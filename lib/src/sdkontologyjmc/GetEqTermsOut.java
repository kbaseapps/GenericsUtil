
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
 * <p>Original spec-file type: GetEqTermsOut</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "term_info_list"
})
public class GetEqTermsOut {

    @JsonProperty("term_info_list")
    private Map<String, List<String>> termInfoList;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("term_info_list")
    public Map<String, List<String>> getTermInfoList() {
        return termInfoList;
    }

    @JsonProperty("term_info_list")
    public void setTermInfoList(Map<String, List<String>> termInfoList) {
        this.termInfoList = termInfoList;
    }

    public GetEqTermsOut withTermInfoList(Map<String, List<String>> termInfoList) {
        this.termInfoList = termInfoList;
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
        return ((((("GetEqTermsOut"+" [termInfoList=")+ termInfoList)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
