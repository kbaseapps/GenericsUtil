
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
import us.kbase.common.service.Tuple2;


/**
 * <p>Original spec-file type: OntologyTerm</p>
 * <pre>
 * @optional is_anonymous name namespace alt_id def comment subset synonym xref builtin property_value is_a intersection_of union_of equivalent_to disjoint_from relationship created_by creation_date is_obsolete replaced_by consider relationship_closure
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
    "builtin",
    "property_value",
    "is_a",
    "intersection_of",
    "union_of",
    "equivalent_to",
    "disjoint_from",
    "relationship",
    "created_by",
    "creation_date",
    "is_obsolete",
    "replaced_by",
    "consider",
    "relationship_closure"
})
public class OntologyTerm {

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
    @JsonProperty("builtin")
    private List<String> builtin;
    @JsonProperty("property_value")
    private List<String> propertyValue;
    @JsonProperty("is_a")
    private List<String> isA;
    @JsonProperty("intersection_of")
    private List<String> intersectionOf;
    @JsonProperty("union_of")
    private List<String> unionOf;
    @JsonProperty("equivalent_to")
    private List<String> equivalentTo;
    @JsonProperty("disjoint_from")
    private List<String> disjointFrom;
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
    @JsonProperty("relationship_closure")
    private Map<String, List<Tuple2 <String, Long>>> relationshipClosure;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public OntologyTerm withId(java.lang.String id) {
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

    public OntologyTerm withIsAnonymous(java.lang.String isAnonymous) {
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

    public OntologyTerm withName(java.lang.String name) {
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

    public OntologyTerm withNamespace(java.lang.String namespace) {
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

    public OntologyTerm withAltId(List<String> altId) {
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

    public OntologyTerm withDef(List<String> def) {
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

    public OntologyTerm withComment(List<String> comment) {
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

    public OntologyTerm withSubset(List<String> subset) {
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

    public OntologyTerm withSynonym(List<String> synonym) {
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

    public OntologyTerm withXref(List<String> xref) {
        this.xref = xref;
        return this;
    }

    @JsonProperty("builtin")
    public List<String> getBuiltin() {
        return builtin;
    }

    @JsonProperty("builtin")
    public void setBuiltin(List<String> builtin) {
        this.builtin = builtin;
    }

    public OntologyTerm withBuiltin(List<String> builtin) {
        this.builtin = builtin;
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

    public OntologyTerm withPropertyValue(List<String> propertyValue) {
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

    public OntologyTerm withIsA(List<String> isA) {
        this.isA = isA;
        return this;
    }

    @JsonProperty("intersection_of")
    public List<String> getIntersectionOf() {
        return intersectionOf;
    }

    @JsonProperty("intersection_of")
    public void setIntersectionOf(List<String> intersectionOf) {
        this.intersectionOf = intersectionOf;
    }

    public OntologyTerm withIntersectionOf(List<String> intersectionOf) {
        this.intersectionOf = intersectionOf;
        return this;
    }

    @JsonProperty("union_of")
    public List<String> getUnionOf() {
        return unionOf;
    }

    @JsonProperty("union_of")
    public void setUnionOf(List<String> unionOf) {
        this.unionOf = unionOf;
    }

    public OntologyTerm withUnionOf(List<String> unionOf) {
        this.unionOf = unionOf;
        return this;
    }

    @JsonProperty("equivalent_to")
    public List<String> getEquivalentTo() {
        return equivalentTo;
    }

    @JsonProperty("equivalent_to")
    public void setEquivalentTo(List<String> equivalentTo) {
        this.equivalentTo = equivalentTo;
    }

    public OntologyTerm withEquivalentTo(List<String> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    @JsonProperty("disjoint_from")
    public List<String> getDisjointFrom() {
        return disjointFrom;
    }

    @JsonProperty("disjoint_from")
    public void setDisjointFrom(List<String> disjointFrom) {
        this.disjointFrom = disjointFrom;
    }

    public OntologyTerm withDisjointFrom(List<String> disjointFrom) {
        this.disjointFrom = disjointFrom;
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

    public OntologyTerm withRelationship(List<String> relationship) {
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

    public OntologyTerm withCreatedBy(java.lang.String createdBy) {
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

    public OntologyTerm withCreationDate(java.lang.String creationDate) {
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

    public OntologyTerm withIsObsolete(java.lang.String isObsolete) {
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

    public OntologyTerm withReplacedBy(List<String> replacedBy) {
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

    public OntologyTerm withConsider(List<String> consider) {
        this.consider = consider;
        return this;
    }

    @JsonProperty("relationship_closure")
    public Map<String, List<Tuple2 <String, Long>>> getRelationshipClosure() {
        return relationshipClosure;
    }

    @JsonProperty("relationship_closure")
    public void setRelationshipClosure(Map<String, List<Tuple2 <String, Long>>> relationshipClosure) {
        this.relationshipClosure = relationshipClosure;
    }

    public OntologyTerm withRelationshipClosure(Map<String, List<Tuple2 <String, Long>>> relationshipClosure) {
        this.relationshipClosure = relationshipClosure;
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
        return ((((((((((((((((((((((((((((((((((((((((((((((((((("OntologyTerm"+" [id=")+ id)+", isAnonymous=")+ isAnonymous)+", name=")+ name)+", namespace=")+ namespace)+", altId=")+ altId)+", def=")+ def)+", comment=")+ comment)+", subset=")+ subset)+", synonym=")+ synonym)+", xref=")+ xref)+", builtin=")+ builtin)+", propertyValue=")+ propertyValue)+", isA=")+ isA)+", intersectionOf=")+ intersectionOf)+", unionOf=")+ unionOf)+", equivalentTo=")+ equivalentTo)+", disjointFrom=")+ disjointFrom)+", relationship=")+ relationship)+", createdBy=")+ createdBy)+", creationDate=")+ creationDate)+", isObsolete=")+ isObsolete)+", replacedBy=")+ replacedBy)+", consider=")+ consider)+", relationshipClosure=")+ relationshipClosure)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
