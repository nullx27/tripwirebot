package tech.grimm.tripwirebot.zkill;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class KillmailEvent extends ApplicationEvent {

    @Getter
    private ZKillmail killmail;

    public KillmailEvent(Object source, ZKillmail killmail) {
        super(source);
        this.killmail = killmail;
    }
}