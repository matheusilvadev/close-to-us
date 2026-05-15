package astr.api.service;

import astr.api.clientconfig.nasaclient.NasaFeedResponse;
import astr.api.clientconfig.nasaclient.NeoWsClient;
import astr.api.core.Asteroid;
import astr.api.mapper.AsteroidMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class GetCloseAsteroidsServiceTest {

    private final NeoWsClient client = mock(NeoWsClient.class);

    private final AsteroidMapper mapper = mock(AsteroidMapper.class);

    private final GetCloseAsteroidsService useCase = new GetCloseAsteroidsService(client, mapper);

    @Test
    void shouldReturnMappedAsteroids() {

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();

        NasaFeedResponse response = mock(NasaFeedResponse.class);

        List<Asteroid> asteroids = List.of(mock(Asteroid.class));

        when(client.fetchNeoWsFeed(start, end))
                .thenReturn(response);

        when(mapper.toDomainList(response))
                .thenReturn(asteroids);

        List<Asteroid> result = useCase.execute(start, end);

        assertThat(result)
                .hasSize(1);

        verify(client)
                .fetchNeoWsFeed(start, end);

        verify(mapper)
                .toDomainList(response);
    }

}