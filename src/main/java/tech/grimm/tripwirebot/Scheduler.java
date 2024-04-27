package tech.grimm.tripwirebot;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tech.grimm.tripwirebot.tripwire.TripwireApiService;
import tech.grimm.tripwirebot.tripwire.TripwireSignature;
import tech.grimm.tripwirebot.zkill.ZKillService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final TripwireApiService tripwireApiService;
    private final ZKillService zKillService;

    private List<TripwireSignature> wormholes = new ArrayList<>();
    private final List<TripwireSignature> subscriptions = new ArrayList<>();


    @Scheduled(fixedRate = 20000)
    public void cron() throws Exception {
        List<TripwireSignature> newList = tripwireApiService.getWormholes();

        List<TripwireSignature> newWormholes = getNewWormholes(newList);
        List<TripwireSignature> removedWormholes = getRemovedWormholes(newList);

        // TODO [AP]: better error handling I guess
        if (!newWormholes.isEmpty()) newWormholes.forEach(wormhole -> {
            try {
                if (!subscriptions.contains(wormhole)) {
                    zKillService.subscribe(wormhole.systemID());
                    subscriptions.add(wormhole);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        if (!removedWormholes.isEmpty()) newWormholes.forEach(wormhole -> {
            try {
                if (subscriptions.contains(wormhole)) {
                    zKillService.unsubscribe(wormhole.systemID());
                    subscriptions.remove(wormhole);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        this.wormholes = newList;
    }


    private List<TripwireSignature> getNewWormholes(List<TripwireSignature> newWormholes) {
        return newWormholes.stream().filter(item -> !this.wormholes.contains(item)).toList();
    }

    private List<TripwireSignature> getRemovedWormholes(List<TripwireSignature> newWormholes) {
        return this.wormholes.stream().filter(item -> !newWormholes.contains(item)).toList();
    }
}