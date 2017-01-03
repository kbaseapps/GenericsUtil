
package genericsutil;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: ImportNDArrayResult</p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "matrix_ref"
})
public class ImportNDArrayResult {

    @JsonProperty("matrix_ref")
    private String matrixRef;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("matrix_ref")
    public String getMatrixRef() {
        return matrixRef;
    }

    @JsonProperty("matrix_ref")
    public void setMatrixRef(String matrixRef) {
        this.matrixRef = matrixRef;
    }

    public ImportNDArrayResult withMatrixRef(String matrixRef) {
        this.matrixRef = matrixRef;
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
        return ((((("ImportNDArrayResult"+" [matrixRef=")+ matrixRef)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
