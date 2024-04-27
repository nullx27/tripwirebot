package tech.grimm.tripwirebot.tripwire;


import com.fasterxml.jackson.annotation.JsonProperty;

public record TripwireSignature(
        @JsonProperty("id") String id,
        @JsonProperty("signatureID") String signatureID,
        @JsonProperty("systemID") String systemID,
        @JsonProperty("type") String type,
        @JsonProperty("name") String name,
        @JsonProperty("bookmark") String bookmark,
        @JsonProperty("lifeTime") String lifeTime,
        @JsonProperty("lifeLeft") String lifeLeft,
        @JsonProperty("lifeLength") String lifeLength,
        @JsonProperty("createdByID") String createdByID,
        @JsonProperty("createdByName") String createdByName,
        @JsonProperty("modifiedByID") String modifiedByID,
        @JsonProperty("modifiedByName") String modifiedByName,
        @JsonProperty("modifiedTime") String modifiedTime,
        @JsonProperty("maskID") String maskID
) {
}