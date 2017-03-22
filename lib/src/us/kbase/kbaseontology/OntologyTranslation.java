
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
 * <p>Original spec-file type: OntologyTranslation</p>
 * <pre>
 * @optional comment
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "comment",
    "ontology1",
    "ontology2",
    "translation"
})
public class OntologyTranslation {

    @JsonProperty("comment")
    private java.lang.String comment;
    @JsonProperty("ontology1")
    private java.lang.String ontology1;
    @JsonProperty("ontology2")
    private java.lang.String ontology2;
    @JsonProperty("translation")
    private Map<String, TranslationRecord> translation;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("comment")
    public java.lang.String getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    public OntologyTranslation withComment(java.lang.String comment) {
        this.comment = comment;
        return this;
    }

    @JsonProperty("ontology1")
    public java.lang.String getOntology1() {
        return ontology1;
    }

    @JsonProperty("ontology1")
    public void setOntology1(java.lang.String ontology1) {
        this.ontology1 = ontology1;
    }

    public OntologyTranslation withOntology1(java.lang.String ontology1) {
        this.ontology1 = ontology1;
        return this;
    }

    @JsonProperty("ontology2")
    public java.lang.String getOntology2() {
        return ontology2;
    }

    @JsonProperty("ontology2")
    public void setOntology2(java.lang.String ontology2) {
        this.ontology2 = ontology2;
    }

    public OntologyTranslation withOntology2(java.lang.String ontology2) {
        this.ontology2 = ontology2;
        return this;
    }

    @JsonProperty("translation")
    public Map<String, TranslationRecord> getTranslation() {
        return translation;
    }

    @JsonProperty("translation")
    public void setTranslation(Map<String, TranslationRecord> translation) {
        this.translation = translation;
    }

    public OntologyTranslation withTranslation(Map<String, TranslationRecord> translation) {
        this.translation = translation;
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
        return ((((((((((("OntologyTranslation"+" [comment=")+ comment)+", ontology1=")+ ontology1)+", ontology2=")+ ontology2)+", translation=")+ translation)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
