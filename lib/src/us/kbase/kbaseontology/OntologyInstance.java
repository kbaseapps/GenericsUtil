
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
 * <p>Original spec-file type: OntologyInstance</p>
 * <pre>
 * @optional is_anonymous name namespace alt_id def comment subset synonym xref instance_of property_value relationship created_by creation_date is_obsolete replaced_by consider
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "is_anonymous",
    "name",
    "namespace",
    "alt_id",
    "def",
    "comment",
    "subset",
    "synonym",
    "xref",
    "instance_of",
    "property_value",
    "relationship",
    "created_by",
    "creation_date",
    "is_obsolete",
    "replaced_by",
    "consider"
})
public class OntologyInstance {

    @JsonProperty("id")
    private java.lang.String id;
    @JsonProperty("is_anonymous")
    private java.lang.String isAnonymous;
    @JsonProperty("name")
    private java.lang.String name;
    @JsonProperty("namespace")
    private java.lang.String namespace;
    @JsonProperty("alt_id")
    private List<String> altId;
    @JsonProperty("def")
    private List<String> def;
    @JsonProperty("comment")
    private List<String> comment;
    @JsonProperty("subset")
    private List<String> subset;
    @JsonProperty("synonym")
    private List<String> synonym;
    @JsonProperty("xref")
    private List<String> xref;
    @JsonProperty("instance_of")
    private List<String> instanceOf;
    @JsonProperty("property_value")
    private List<String> propertyValue;
    @JsonProperty("relationship")
    private List<String> relationship;
    @JsonProperty("created_by")
    private java.lang.String createdBy;
    @JsonProperty("creation_date")
    private java.lang.String creationDate;
    @JsonProperty("is_obsolete")
    private java.lang.String isObsolete;
    @JsonProperty("replaced_by")
    private List<String> replacedBy;
    @JsonProperty("consider")
    private List<String> consider;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public OntologyInstance withId(java.lang.String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("is_anonymous")
    public java.lang.String getIsAnonymous() {
        return isAnonymous;
    }

    @JsonProperty("is_anonymous")
    public void setIsAnonymous(java.lang.String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public OntologyInstance withIsAnonymous(java.lang.String isAnonymous) {
        this.isAnonymous = isAnonymous;
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

    public OntologyInstance withName(java.lang.String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("namespace")
    public java.lang.String getNamespace() {
        return namespace;
    }

    @JsonProperty("namespace")
    public void setNamespace(java.lang.String namespace) {
        this.namespace = namespace;
    }

    public OntologyInstance withNamespace(java.lang.String namespace) {
        this.namespace = namespace;
        return this;
    }

    @JsonProperty("alt_id")
    public List<String> getAltId() {
        return altId;
    }

    @JsonProperty("alt_id")
    public void setAltId(List<String> altId) {
        this.altId = altId;
    }

    public OntologyInstance withAltId(List<String> altId) {
        this.altId = altId;
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

    public OntologyInstance withDef(List<String> def) {
        this.def = def;
        return this;
    }

    @JsonProperty("comment")
    public List<String> getComment() {
        return comment;
    }

    @JsonProperty("comment")
    public void setComment(List<String> comment) {
        this.comment = comment;
    }

    public OntologyInstance withComment(List<String> comment) {
        this.comment = comment;
        return this;
    }

    @JsonProperty("subset")
    public List<String> getSubset() {
        return subset;
    }

    @JsonProperty("subset")
    public void setSubset(List<String> subset) {
        this.subset = subset;
    }

    public OntologyInstance withSubset(List<String> subset) {
        this.subset = subset;
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

    public OntologyInstance withSynonym(List<String> synonym) {
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

    public OntologyInstance withXref(List<String> xref) {
        this.xref = xref;
        return this;
    }

    @JsonProperty("instance_of")
    public List<String> getInstanceOf() {
        return instanceOf;
    }

    @JsonProperty("instance_of")
    public void setInstanceOf(List<String> instanceOf) {
        this.instanceOf = instanceOf;
    }

    public OntologyInstance withInstanceOf(List<String> instanceOf) {
        this.instanceOf = instanceOf;
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

    public OntologyInstance withPropertyValue(List<String> propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    @JsonProperty("relationship")
    public List<String> getRelationship() {
        return relationship;
    }

    @JsonProperty("relationship")
    public void setRelationship(List<String> relationship) {
        this.relationship = relationship;
    }

    public OntologyInstance withRelationship(List<String> relationship) {
        this.relationship = relationship;
        return this;
    }

    @JsonProperty("created_by")
    public java.lang.String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("created_by")
    public void setCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
    }

    public OntologyInstance withCreatedBy(java.lang.String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonProperty("creation_date")
    public java.lang.String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(java.lang.String creationDate) {
        this.creationDate = creationDate;
    }

    public OntologyInstance withCreationDate(java.lang.String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @JsonProperty("is_obsolete")
    public java.lang.String getIsObsolete() {
        return isObsolete;
    }

    @JsonProperty("is_obsolete")
    public void setIsObsolete(java.lang.String isObsolete) {
        this.isObsolete = isObsolete;
    }

    public OntologyInstance withIsObsolete(java.lang.String isObsolete) {
        this.isObsolete = isObsolete;
        return this;
    }

    @JsonProperty("replaced_by")
    public List<String> getReplacedBy() {
        return replacedBy;
    }

    @JsonProperty("replaced_by")
    public void setReplacedBy(List<String> replacedBy) {
        this.replacedBy = replacedBy;
    }

    public OntologyInstance withReplacedBy(List<String> replacedBy) {
        this.replacedBy = replacedBy;
        return this;
    }

    @JsonProperty("consider")
    public List<String> getConsider() {
        return consider;
    }

    @JsonProperty("consider")
    public void setConsider(List<String> consider) {
        this.consider = consider;
    }

    public OntologyInstance withConsider(List<String> consider) {
        this.consider = consider;
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
        return ((((((((((((((((((((((((((((((((((((((("OntologyInstance"+" [id=")+ id)+", isAnonymous=")+ isAnonymous)+", name=")+ name)+", namespace=")+ namespace)+", altId=")+ altId)+", def=")+ def)+", comment=")+ comment)+", subset=")+ subset)+", synonym=")+ synonym)+", xref=")+ xref)+", instanceOf=")+ instanceOf)+", propertyValue=")+ propertyValue)+", relationship=")+ relationship)+", createdBy=")+ createdBy)+", creationDate=")+ creationDate)+", isObsolete=")+ isObsolete)+", replacedBy=")+ replacedBy)+", consider=")+ consider)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
