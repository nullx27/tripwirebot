package tech.grimm.tripwirebot.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.grimm.tripwirebot.tripwire.TripwireApiService;
import tech.grimm.tripwirebot.tripwire.TripwireSignature;
import tech.grimm.tripwirebot.zkill.ZKillmail;

@Service
@RequiredArgsConstructor
public class DiscordService {

    @Value("${tripwire.discord-webhook}")
    private String webhookUrl;

    private final TripwireApiService tripwireApiService;

    public void sendKillmailToWebHook(ZKillmail killmail) {
        String systemId = killmail.channel().split(":")[1];
        TripwireSignature signature = tripwireApiService.getSignatureFromSystemId(systemId);

        System.out.println("Sending Killmail to Webhook");

        WebhookClient webhookClient = new WebhookClientBuilder(webhookUrl).build();
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setAuthor(new WebhookEmbed.EmbedAuthor("Tripwire Killmail Bot", "", ""))
                .setTitle(new WebhookEmbed.EmbedTitle("Kill detected in Chain", killmail.url()))
                //.addField(new WebhookEmbed.EmbedField(true, "Signature", signature.signatureID()))
                //.addField(new WebhookEmbed.EmbedField(true, "Name", signature.name()))
                .addField(new WebhookEmbed.EmbedField(true, "System", signature.systemID()))
                .build();

        webhookClient.send(embed);
    }
}