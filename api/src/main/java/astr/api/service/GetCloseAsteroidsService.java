package astr.api.service;

import astr.api.clientconfig.nasaclient.NeoWsClient;
import astr.api.core.Asteroid;
import astr.api.mapper.AsteroidMapper;
import astr.api.service.exception.InvalidDateRangeException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GetCloseAsteroidsService {
    private final NeoWsClient client;
    private final AsteroidMapper mapper;

    public GetCloseAsteroidsService(NeoWsClient client, AsteroidMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    public List<Asteroid> execute(LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Start date must be before end date");
        }

        var response = client.fetchNeoWsFeed(
                startDate,
                endDate);

        return mapper.toDomainList(response);
    }
}
