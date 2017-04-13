
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
 * <p>Original spec-file type: termInfo</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "name",
    "def",
    "synonym",
    "xref",
    "property_value",
    "is_a"
})
public class TermInfo {

    @JsonProperty("id")
    private java.lang.String id;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("def")
    private List<String> def;
    @JsonProperty("synonym")
    private List<String> synonym;
    @JsonProperty("xref")
    private List<String> xref;
    @JsonProperty("property_value")
    private List<String> propertyValue;
    @JsonProperty("is_a")
    private List<String> isA;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public TermInfo withId(java.lang.String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public java.lang.String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public TermInfo withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("def")
    public List<String> getDef() {
        return def;
    }

    @JsonProperty("def")
    public void setDef(List<String> def) {
        this.def = def;
    }

    public TermInfo withDef(List<String> def) {
        this.def = def;
        return this;
    }

    @JsonProperty("synonym")
    public List<String> getSynonym() {
        return synonym;
    }

    @JsonProperty("synonym")
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    public TermInfo withSynonym(List<String> synonym) {
        this.synonym = synonym;
        return this;
    }

    @JsonProperty("xref")
    public List<String> getXref() {
        return xref;
    }

    @JsonProperty("xref")
    public void setXref(List<String> xref) {
        this.xref = xref;
    }

    public TermInfo withXref(List<String> xref) {
        this.xref = xref;
        return this;
    }

    @JsonProperty("property_value")
    public List<String> getPropertyValue() {
        return propertyValue;
    }

    @JsonProperty("property_value")
    public void setPropertyValue(List<String> propertyValue) {
        this.propertyValue = propertyValue;
    }

    public TermInfo withPropertyValue(List<String> propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    @JsonProperty("is_a")
    public List<String> getIsA() {
        return isA;
    }

    @JsonProperty("is_a")
    public void setIsA(List<String> isA) {
        this.isA = isA;
    }

    public TermInfo withIsA(List<String> isA) {
        this.isA = isA;
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
        return ((((((((((((((((("TermInfo"+" [id=")+ id)+", name=")+ name)+", def=")+ def)+", synonym=")+ synonym)+", xref=")+ xref)+", propertyValue=")+ propertyValue)+", isA=")+ isA)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
