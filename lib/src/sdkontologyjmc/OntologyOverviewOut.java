
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
 * <p>Original spec-file type: OntologyOverviewOut</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "dictionaries_meta"
})
public class OntologyOverviewOut {

    @JsonProperty("dictionaries_meta")
    private List<OverViewInfo> dictionariesMeta;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dictionaries_meta")
    public List<OverViewInfo> getDictionariesMeta() {
        return dictionariesMeta;
    }

    @JsonProperty("dictionaries_meta")
    public void setDictionariesMeta(List<OverViewInfo> dictionariesMeta) {
        this.dictionariesMeta = dictionariesMeta;
    }

    public OntologyOverviewOut withDictionariesMeta(List<OverViewInfo> dictionariesMeta) {
        this.dictionariesMeta = dictionariesMeta;
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
        return ((((("OntologyOverviewOut"+" [dictionariesMeta=")+ dictionariesMeta)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
