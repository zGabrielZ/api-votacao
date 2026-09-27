package br.com.gabrielferreira.votacao.domain.scheduler;

import br.com.gabrielferreira.votacao.domain.services.VotingSessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@DisplayName("Unit tests for VotingSessionScheduler")
@ExtendWith(MockitoExtension.class)
class VotingSessionSchedulerTest {

    @InjectMocks
    private VotingSessionScheduler scheduler;

    @Mock
    private VotingSessionService votingSessionService;

    @Test
    void givenSchedulerExecutionWhenCloseExpiredVotingSessionsThenCallService() {
        scheduler.closeExpiredVotingSessions();

        verify(votingSessionService).closeExpiredVotingSessions();
    }
}
