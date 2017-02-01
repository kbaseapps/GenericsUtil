
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
 * <p>Original spec-file type: OTerm</p>
 * <pre>
 * //-----------------
 * // Types used for mapping
 * //-----------------
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "ontology_id",
    "oterm_id",
    "oterm_name",
    "value_scalar_type",
    "value_mapper",
    "value_validator_method",
    "value_validation_pattern",
    "rank_score"
})
public class OTerm {

    @JsonProperty("ontology_id")
    private String ontologyId;
    @JsonProperty("oterm_id")
    private String otermId;
    @JsonProperty("oterm_name")
    private String otermName;
    @JsonProperty("value_scalar_type")
    private String valueScalarType;
    @JsonProperty("value_mapper")
    private String valueMapper;
    @JsonProperty("value_validator_method")
    private String valueValidatorMethod;
    @JsonProperty("value_validation_pattern")
    private String valueValidationPattern;
    @JsonProperty("rank_score")
    private Double rankScore;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ontology_id")
    public String getOntologyId() {
        return ontologyId;
    }

    @JsonProperty("ontology_id")
    public void setOntologyId(String ontologyId) {
        this.ontologyId = ontologyId;
    }

    public OTerm withOntologyId(String ontologyId) {
        this.ontologyId = ontologyId;
        return this;
    }

    @JsonProperty("oterm_id")
    public String getOtermId() {
        return otermId;
    }

    @JsonProperty("oterm_id")
    public void setOtermId(String otermId) {
        this.otermId = otermId;
    }

    public OTerm withOtermId(String otermId) {
        this.otermId = otermId;
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

    public OTerm withOtermName(String otermName) {
        this.otermName = otermName;
        return this;
    }

    @JsonProperty("value_scalar_type")
    public String getValueScalarType() {
        return valueScalarType;
    }

    @JsonProperty("value_scalar_type")
    public void setValueScalarType(String valueScalarType) {
        this.valueScalarType = valueScalarType;
    }

    public OTerm withValueScalarType(String valueScalarType) {
        this.valueScalarType = valueScalarType;
        return this;
    }

    @JsonProperty("value_mapper")
    public String getValueMapper() {
        return valueMapper;
    }

    @JsonProperty("value_mapper")
    public void setValueMapper(String valueMapper) {
        this.valueMapper = valueMapper;
    }

    public OTerm withValueMapper(String valueMapper) {
        this.valueMapper = valueMapper;
        return this;
    }

    @JsonProperty("value_validator_method")
    public String getValueValidatorMethod() {
        return valueValidatorMethod;
    }

    @JsonProperty("value_validator_method")
    public void setValueValidatorMethod(String valueValidatorMethod) {
        this.valueValidatorMethod = valueValidatorMethod;
    }

    public OTerm withValueValidatorMethod(String valueValidatorMethod) {
        this.valueValidatorMethod = valueValidatorMethod;
        return this;
    }

    @JsonProperty("value_validation_pattern")
    public String getValueValidationPattern() {
        return valueValidationPattern;
    }

    @JsonProperty("value_validation_pattern")
    public void setValueValidationPattern(String valueValidationPattern) {
        this.valueValidationPattern = valueValidationPattern;
    }

    public OTerm withValueValidationPattern(String valueValidationPattern) {
        this.valueValidationPattern = valueValidationPattern;
        return this;
    }

    @JsonProperty("rank_score")
    public Double getRankScore() {
        return rankScore;
    }

    @JsonProperty("rank_score")
    public void setRankScore(Double rankScore) {
        this.rankScore = rankScore;
    }

    public OTerm withRankScore(Double rankScore) {
        this.rankScore = rankScore;
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
        return ((((((((((((((((((("OTerm"+" [ontologyId=")+ ontologyId)+", otermId=")+ otermId)+", otermName=")+ otermName)+", valueScalarType=")+ valueScalarType)+", valueMapper=")+ valueMapper)+", valueValidatorMethod=")+ valueValidatorMethod)+", valueValidationPattern=")+ valueValidationPattern)+", rankScore=")+ rankScore)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
