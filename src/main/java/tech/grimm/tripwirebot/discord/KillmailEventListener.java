package tech.grimm.tripwirebot.discord;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tech.grimm.tripwirebot.zkill.KillmailEvent;

@Component
@RequiredArgsConstructor
public class KillmailEventListener implements ApplicationListener<KillmailEvent> {

    private final DiscordService discordService;

    @Override
    public void onApplicationEvent(KillmailEvent event) {
        System.out.println(event.getKillmail());
        discordService.sendKillmailToWebHook(event.getKillmail());
    }
}