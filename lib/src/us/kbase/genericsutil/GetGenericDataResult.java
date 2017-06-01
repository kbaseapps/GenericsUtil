
package us.kbase.genericsutil;

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
 * <p>Original spec-file type: GetGenericDataResult</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "data_2d_float"
})
public class GetGenericDataResult {

    @JsonProperty("data_2d_float")
    private List<List<Double>> data2dFloat;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("data_2d_float")
    public List<List<Double>> getData2dFloat() {
        return data2dFloat;
    }

    @JsonProperty("data_2d_float")
    public void setData2dFloat(List<List<Double>> data2dFloat) {
        this.data2dFloat = data2dFloat;
    }

    public GetGenericDataResult withData2dFloat(List<List<Double>> data2dFloat) {
        this.data2dFloat = data2dFloat;
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
        return ((((("GetGenericDataResult"+" [data2dFloat=")+ data2dFloat)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
