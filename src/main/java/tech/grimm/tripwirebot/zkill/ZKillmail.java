package tech.grimm.tripwirebot.zkill;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ZKillmail(
        @JsonProperty("action") String action,
        @JsonProperty("killID") String killId,
        @JsonProperty("hash") String hash,
        @JsonProperty("character_id") String characterId,
        @JsonProperty("corporation_id") String corporationId,
        @JsonProperty("alliance_id") String allianceId,
        @JsonProperty("ship_type_id") String shipTypeId,
        @JsonProperty("group_id") String groupId,
        @JsonProperty("url") String url,
        @JsonProperty("channel") String channel

) {
}