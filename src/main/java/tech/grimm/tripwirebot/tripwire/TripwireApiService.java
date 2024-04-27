package tech.grimm.tripwirebot.tripwire;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class TripwireApiService {

    @Value("${tripwirebot.user}")
    private String tripwireUser;
    @Value("${tripwirebot.password}")
    private String tripwirePassword;
    @Value("${tripwirebot.server-url}")
    private String tripwireServerUrl;

    @Value("${tripwirebot.maskID}")
    private String tripwiremaskId;

    private List<TripwireSignature> filteredSignatures = new ArrayList<>();


    public List<TripwireSignature> getWormholes() {

        WebClient client = WebClient.create();
        TripwireSignature[] signatures = client.get()
                .uri(this.tripwireServerUrl + "/api?q=/signatures&maskID=" + this.tripwiremaskId)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((this.tripwireUser + ":" + this.tripwirePassword).getBytes(StandardCharsets.UTF_8)))
                .retrieve()
                .bodyToMono(TripwireSignature[].class)
                .block();

        // TODO [AP]: this needs to be looked at again
        if (signatures == null) return new ArrayList<>();

        // filter signatures for wormholes and remove entries without or invalid systemIDs
        List<TripwireSignature> wormholes = Arrays.stream(signatures)
                .filter(entry -> Objects.equals(entry.type(), "wormhole"))
                .filter(entry -> !entry.systemID().isEmpty())
                .filter(entry -> entry.systemID().length() == 8) // for some reason tripwire returns entries with single digit systemids smh
                .collect(Collectors.toList());

        this.filteredSignatures = wormholes;

        return wormholes;
    }

    public TripwireSignature getSignatureFromSystemId(String systemId) {
        return this.filteredSignatures.stream().filter(item -> item.systemID().equals(systemId)).findFirst().orElse(null);
    }

}