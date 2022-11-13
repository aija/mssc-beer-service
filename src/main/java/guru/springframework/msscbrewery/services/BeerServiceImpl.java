package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder().id(UUID.randomUUID())
                                .beerName("Galaxy Cat")
                                .beerStyle(BeerStyleEnum.PALE_ALE)
                                .build();
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDto beerDto) {
        //todo impl - would add real impl to update beer
        log.debug("Updating a beer....");
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        log.debug("Deleting a beer... ");
    }
}
