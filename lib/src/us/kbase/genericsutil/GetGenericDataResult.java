
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
    "data_x_float",
    "data_y_float",
    "series_labels"
})
public class GetGenericDataResult {

    @JsonProperty("data_x_float")
    private List<Double> dataXFloat;
    @JsonProperty("data_y_float")
    private List<List<Double>> dataYFloat;
    @JsonProperty("series_labels")
    private List<String> seriesLabels;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("data_x_float")
    public List<Double> getDataXFloat() {
        return dataXFloat;
    }

    @JsonProperty("data_x_float")
    public void setDataXFloat(List<Double> dataXFloat) {
        this.dataXFloat = dataXFloat;
    }

    public GetGenericDataResult withDataXFloat(List<Double> dataXFloat) {
        this.dataXFloat = dataXFloat;
        return this;
    }

    @JsonProperty("data_y_float")
    public List<List<Double>> getDataYFloat() {
        return dataYFloat;
    }

    @JsonProperty("data_y_float")
    public void setDataYFloat(List<List<Double>> dataYFloat) {
        this.dataYFloat = dataYFloat;
    }

    public GetGenericDataResult withDataYFloat(List<List<Double>> dataYFloat) {
        this.dataYFloat = dataYFloat;
        return this;
    }

    @JsonProperty("series_labels")
    public List<String> getSeriesLabels() {
        return seriesLabels;
    }

    @JsonProperty("series_labels")
    public void setSeriesLabels(List<String> seriesLabels) {
        this.seriesLabels = seriesLabels;
    }

    public GetGenericDataResult withSeriesLabels(List<String> seriesLabels) {
        this.seriesLabels = seriesLabels;
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
        return ((((((((("GetGenericDataResult"+" [dataXFloat=")+ dataXFloat)+", dataYFloat=")+ dataYFloat)+", seriesLabels=")+ seriesLabels)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
