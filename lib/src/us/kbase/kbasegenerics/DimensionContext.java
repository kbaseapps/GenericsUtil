
package us.kbase.kbasegenerics;

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
 * <p>Original spec-file type: DimensionContext</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "dimension_size",
    "items"
})
public class DimensionContext {

    @JsonProperty("dimension_size")
    private Long dimensionSize;
    @JsonProperty("items")
    private List<DimensionContextItem> items;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("dimension_size")
    public Long getDimensionSize() {
        return dimensionSize;
    }

    @JsonProperty("dimension_size")
    public void setDimensionSize(Long dimensionSize) {
        this.dimensionSize = dimensionSize;
    }

    public DimensionContext withDimensionSize(Long dimensionSize) {
        this.dimensionSize = dimensionSize;
        return this;
    }

    @JsonProperty("items")
    public List<DimensionContextItem> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<DimensionContextItem> items) {
        this.items = items;
    }

    public DimensionContext withItems(List<DimensionContextItem> items) {
        this.items = items;
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
        return ((((((("DimensionContext"+" [dimensionSize=")+ dimensionSize)+", items=")+ items)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
