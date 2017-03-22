
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
 * <p>Original spec-file type: OntologyTypedef</p>
 * <pre>
 * @optional is_anonymous name namespace alt_id def comment subset synonym xref property_value domain range builtin holds_over_chain is_anti_symmetric is_cyclic is_reflexive is_symmetric is_transitive is_functional is_inverse_functional is_a intersection_of union_of equivalent_to disjoint_from inverse_of transitive_over equivalent_to_chain disjoint_over relationship is_obsolete created_by creation_date replaced_by consider expand_assertion_to expand_expression_to is_metadata_tag is_class_level
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
    "property_value",
    "domain",
    "range",
    "builtin",
    "holds_over_chain",
    "is_anti_symmetric",
    "is_cyclic",
    "is_reflexive",
    "is_symmetric",
    "is_transitive",
    "is_functional",
    "is_inverse_functional",
    "is_a",
    "intersection_of",
    "union_of",
    "equivalent_to",
    "disjoint_from",
    "inverse_of",
    "transitive_over",
    "equivalent_to_chain",
    "disjoint_over",
    "relationship",
    "is_obsolete",
    "created_by",
    "creation_date",
    "replaced_by",
    "consider",
    "expand_assertion_to",
    "expand_expression_to",
    "is_metadata_tag",
    "is_class_level"
})
public class OntologyTypedef {

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
    @JsonProperty("property_value")
    private List<String> propertyValue;
    @JsonProperty("domain")
    private List<String> domain;
    @JsonProperty("range")
    private List<String> range;
    @JsonProperty("builtin")
    private List<String> builtin;
    @JsonProperty("holds_over_chain")
    private List<String> holdsOverChain;
    @JsonProperty("is_anti_symmetric")
    private java.lang.String isAntiSymmetric;
    @JsonProperty("is_cyclic")
    private java.lang.String isCyclic;
    @JsonProperty("is_reflexive")
    private java.lang.String isReflexive;
    @JsonProperty("is_symmetric")
    private java.lang.String isSymmetric;
    @JsonProperty("is_transitive")
    private java.lang.String isTransitive;
    @JsonProperty("is_functional")
    private java.lang.String isFunctional;
    @JsonProperty("is_inverse_functional")
    private java.lang.String isInverseFunctional;
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
    @JsonProperty("inverse_of")
    private List<String> inverseOf;
    @JsonProperty("transitive_over")
    private List<String> transitiveOver;
    @JsonProperty("equivalent_to_chain")
    private List<String> equivalentToChain;
    @JsonProperty("disjoint_over")
    private List<String> disjointOver;
    @JsonProperty("relationship")
    private List<String> relationship;
    @JsonProperty("is_obsolete")
    private java.lang.String isObsolete;
    @JsonProperty("created_by")
    private java.lang.String createdBy;
    @JsonProperty("creation_date")
    private java.lang.String creationDate;
    @JsonProperty("replaced_by")
    private List<String> replacedBy;
    @JsonProperty("consider")
    private List<String> consider;
    @JsonProperty("expand_assertion_to")
    private List<String> expandAssertionTo;
    @JsonProperty("expand_expression_to")
    private List<String> expandExpressionTo;
    @JsonProperty("is_metadata_tag")
    private java.lang.String isMetadataTag;
    @JsonProperty("is_class_level")
    private java.lang.String isClassLevel;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("id")
    public java.lang.String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public OntologyTypedef withId(java.lang.String id) {
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

    public OntologyTypedef withIsAnonymous(java.lang.String isAnonymous) {
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

    public OntologyTypedef withName(java.lang.String name) {
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

    public OntologyTypedef withNamespace(java.lang.String namespace) {
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

    public OntologyTypedef withAltId(List<String> altId) {
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

    public OntologyTypedef withDef(List<String> def) {
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

    public OntologyTypedef withComment(List<String> comment) {
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

    public OntologyTypedef withSubset(List<String> subset) {
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

    public OntologyTypedef withSynonym(List<String> synonym) {
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

    public OntologyTypedef withXref(List<String> xref) {
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

    public OntologyTypedef withPropertyValue(List<String> propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    @JsonProperty("domain")
    public List<String> getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(List<String> domain) {
        this.domain = domain;
    }

    public OntologyTypedef withDomain(List<String> domain) {
        this.domain = domain;
        return this;
    }

    @JsonProperty("range")
    public List<String> getRange() {
        return range;
    }

    @JsonProperty("range")
    public void setRange(List<String> range) {
        this.range = range;
    }

    public OntologyTypedef withRange(List<String> range) {
        this.range = range;
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

    public OntologyTypedef withBuiltin(List<String> builtin) {
        this.builtin = builtin;
        return this;
    }

    @JsonProperty("holds_over_chain")
    public List<String> getHoldsOverChain() {
        return holdsOverChain;
    }

    @JsonProperty("holds_over_chain")
    public void setHoldsOverChain(List<String> holdsOverChain) {
        this.holdsOverChain = holdsOverChain;
    }

    public OntologyTypedef withHoldsOverChain(List<String> holdsOverChain) {
        this.holdsOverChain = holdsOverChain;
        return this;
    }

    @JsonProperty("is_anti_symmetric")
    public java.lang.String getIsAntiSymmetric() {
        return isAntiSymmetric;
    }

    @JsonProperty("is_anti_symmetric")
    public void setIsAntiSymmetric(java.lang.String isAntiSymmetric) {
        this.isAntiSymmetric = isAntiSymmetric;
    }

    public OntologyTypedef withIsAntiSymmetric(java.lang.String isAntiSymmetric) {
        this.isAntiSymmetric = isAntiSymmetric;
        return this;
    }

    @JsonProperty("is_cyclic")
    public java.lang.String getIsCyclic() {
        return isCyclic;
    }

    @JsonProperty("is_cyclic")
    public void setIsCyclic(java.lang.String isCyclic) {
        this.isCyclic = isCyclic;
    }

    public OntologyTypedef withIsCyclic(java.lang.String isCyclic) {
        this.isCyclic = isCyclic;
        return this;
    }

    @JsonProperty("is_reflexive")
    public java.lang.String getIsReflexive() {
        return isReflexive;
    }

    @JsonProperty("is_reflexive")
    public void setIsReflexive(java.lang.String isReflexive) {
        this.isReflexive = isReflexive;
    }

    public OntologyTypedef withIsReflexive(java.lang.String isReflexive) {
        this.isReflexive = isReflexive;
        return this;
    }

    @JsonProperty("is_symmetric")
    public java.lang.String getIsSymmetric() {
        return isSymmetric;
    }

    @JsonProperty("is_symmetric")
    public void setIsSymmetric(java.lang.String isSymmetric) {
        this.isSymmetric = isSymmetric;
    }

    public OntologyTypedef withIsSymmetric(java.lang.String isSymmetric) {
        this.isSymmetric = isSymmetric;
        return this;
    }

    @JsonProperty("is_transitive")
    public java.lang.String getIsTransitive() {
        return isTransitive;
    }

    @JsonProperty("is_transitive")
    public void setIsTransitive(java.lang.String isTransitive) {
        this.isTransitive = isTransitive;
    }

    public OntologyTypedef withIsTransitive(java.lang.String isTransitive) {
        this.isTransitive = isTransitive;
        return this;
    }

    @JsonProperty("is_functional")
    public java.lang.String getIsFunctional() {
        return isFunctional;
    }

    @JsonProperty("is_functional")
    public void setIsFunctional(java.lang.String isFunctional) {
        this.isFunctional = isFunctional;
    }

    public OntologyTypedef withIsFunctional(java.lang.String isFunctional) {
        this.isFunctional = isFunctional;
        return this;
    }

    @JsonProperty("is_inverse_functional")
    public java.lang.String getIsInverseFunctional() {
        return isInverseFunctional;
    }

    @JsonProperty("is_inverse_functional")
    public void setIsInverseFunctional(java.lang.String isInverseFunctional) {
        this.isInverseFunctional = isInverseFunctional;
    }

    public OntologyTypedef withIsInverseFunctional(java.lang.String isInverseFunctional) {
        this.isInverseFunctional = isInverseFunctional;
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

    public OntologyTypedef withIsA(List<String> isA) {
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

    public OntologyTypedef withIntersectionOf(List<String> intersectionOf) {
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

    public OntologyTypedef withUnionOf(List<String> unionOf) {
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

    public OntologyTypedef withEquivalentTo(List<String> equivalentTo) {
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

    public OntologyTypedef withDisjointFrom(List<String> disjointFrom) {
        this.disjointFrom = disjointFrom;
        return this;
    }

    @JsonProperty("inverse_of")
    public List<String> getInverseOf() {
        return inverseOf;
    }

    @JsonProperty("inverse_of")
    public void setInverseOf(List<String> inverseOf) {
        this.inverseOf = inverseOf;
    }

    public OntologyTypedef withInverseOf(List<String> inverseOf) {
        this.inverseOf = inverseOf;
        return this;
    }

    @JsonProperty("transitive_over")
    public List<String> getTransitiveOver() {
        return transitiveOver;
    }

    @JsonProperty("transitive_over")
    public void setTransitiveOver(List<String> transitiveOver) {
        this.transitiveOver = transitiveOver;
    }

    public OntologyTypedef withTransitiveOver(List<String> transitiveOver) {
        this.transitiveOver = transitiveOver;
        return this;
    }

    @JsonProperty("equivalent_to_chain")
    public List<String> getEquivalentToChain() {
        return equivalentToChain;
    }

    @JsonProperty("equivalent_to_chain")
    public void setEquivalentToChain(List<String> equivalentToChain) {
        this.equivalentToChain = equivalentToChain;
    }

    public OntologyTypedef withEquivalentToChain(List<String> equivalentToChain) {
        this.equivalentToChain = equivalentToChain;
        return this;
    }

    @JsonProperty("disjoint_over")
    public List<String> getDisjointOver() {
        return disjointOver;
    }

    @JsonProperty("disjoint_over")
    public void setDisjointOver(List<String> disjointOver) {
        this.disjointOver = disjointOver;
    }

    public OntologyTypedef withDisjointOver(List<String> disjointOver) {
        this.disjointOver = disjointOver;
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

    public OntologyTypedef withRelationship(List<String> relationship) {
        this.relationship = relationship;
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

    public OntologyTypedef withIsObsolete(java.lang.String isObsolete) {
        this.isObsolete = isObsolete;
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

    public OntologyTypedef withCreatedBy(java.lang.String createdBy) {
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

    public OntologyTypedef withCreationDate(java.lang.String creationDate) {
        this.creationDate = creationDate;
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

    public OntologyTypedef withReplacedBy(List<String> replacedBy) {
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

    public OntologyTypedef withConsider(List<String> consider) {
        this.consider = consider;
        return this;
    }

    @JsonProperty("expand_assertion_to")
    public List<String> getExpandAssertionTo() {
        return expandAssertionTo;
    }

    @JsonProperty("expand_assertion_to")
    public void setExpandAssertionTo(List<String> expandAssertionTo) {
        this.expandAssertionTo = expandAssertionTo;
    }

    public OntologyTypedef withExpandAssertionTo(List<String> expandAssertionTo) {
        this.expandAssertionTo = expandAssertionTo;
        return this;
    }

    @JsonProperty("expand_expression_to")
    public List<String> getExpandExpressionTo() {
        return expandExpressionTo;
    }

    @JsonProperty("expand_expression_to")
    public void setExpandExpressionTo(List<String> expandExpressionTo) {
        this.expandExpressionTo = expandExpressionTo;
    }

    public OntologyTypedef withExpandExpressionTo(List<String> expandExpressionTo) {
        this.expandExpressionTo = expandExpressionTo;
        return this;
    }

    @JsonProperty("is_metadata_tag")
    public java.lang.String getIsMetadataTag() {
        return isMetadataTag;
    }

    @JsonProperty("is_metadata_tag")
    public void setIsMetadataTag(java.lang.String isMetadataTag) {
        this.isMetadataTag = isMetadataTag;
    }

    public OntologyTypedef withIsMetadataTag(java.lang.String isMetadataTag) {
        this.isMetadataTag = isMetadataTag;
        return this;
    }

    @JsonProperty("is_class_level")
    public java.lang.String getIsClassLevel() {
        return isClassLevel;
    }

    @JsonProperty("is_class_level")
    public void setIsClassLevel(java.lang.String isClassLevel) {
        this.isClassLevel = isClassLevel;
    }

    public OntologyTypedef withIsClassLevel(java.lang.String isClassLevel) {
        this.isClassLevel = isClassLevel;
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
        return ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("OntologyTypedef"+" [id=")+ id)+", isAnonymous=")+ isAnonymous)+", name=")+ name)+", namespace=")+ namespace)+", altId=")+ altId)+", def=")+ def)+", comment=")+ comment)+", subset=")+ subset)+", synonym=")+ synonym)+", xref=")+ xref)+", propertyValue=")+ propertyValue)+", domain=")+ domain)+", range=")+ range)+", builtin=")+ builtin)+", holdsOverChain=")+ holdsOverChain)+", isAntiSymmetric=")+ isAntiSymmetric)+", isCyclic=")+ isCyclic)+", isReflexive=")+ isReflexive)+", isSymmetric=")+ isSymmetric)+", isTransitive=")+ isTransitive)+", isFunctional=")+ isFunctional)+", isInverseFunctional=")+ isInverseFunctional)+", isA=")+ isA)+", intersectionOf=")+ intersectionOf)+", unionOf=")+ unionOf)+", equivalentTo=")+ equivalentTo)+", disjointFrom=")+ disjointFrom)+", inverseOf=")+ inverseOf)+", transitiveOver=")+ transitiveOver)+", equivalentToChain=")+ equivalentToChain)+", disjointOver=")+ disjointOver)+", relationship=")+ relationship)+", isObsolete=")+ isObsolete)+", createdBy=")+ createdBy)+", creationDate=")+ creationDate)+", replacedBy=")+ replacedBy)+", consider=")+ consider)+", expandAssertionTo=")+ expandAssertionTo)+", expandExpressionTo=")+ expandExpressionTo)+", isMetadataTag=")+ isMetadataTag)+", isClassLevel=")+ isClassLevel)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
