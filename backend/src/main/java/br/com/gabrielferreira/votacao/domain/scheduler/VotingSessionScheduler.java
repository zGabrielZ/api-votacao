package br.com.gabrielferreira.votacao.domain.scheduler;

import br.com.gabrielferreira.votacao.domain.services.VotingSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "voting-session.scheduler.enabled",
        havingValue = "true"
)
@Slf4j
public class VotingSessionScheduler {

    private final VotingSessionService votingSessionService;

    @Scheduled(
            initialDelayString = "${voting-session.scheduler.interval}",
            fixedRateString = "${voting-session.scheduler.interval}"
    )
    public void closeExpiredVotingSessions() {
        log.info("Closing expired voting sessions...");
        votingSessionService.closeExpiredVotingSessions();
        log.info("Closed expired voting sessions...");
    }
}
