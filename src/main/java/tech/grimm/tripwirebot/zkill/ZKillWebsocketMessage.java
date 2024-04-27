package tech.grimm.tripwirebot.zkill;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ZKillWebsocketMessage(@JsonProperty("action") String action, @JsonProperty("channel") String channel) {
}