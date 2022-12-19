package guru.springframework.msscbrewery.web.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.*;

@JsonTest
class BeerDtoTest extends BaseTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testSerializedDto() throws JsonProcessingException {
        BeerDto beerDto = getDto();
        String jsonString = objectMapper.writeValueAsString(beerDto);
        System.out.println(jsonString);
    }

    @Test
    void testDeserializedDto() throws JsonProcessingException {
        String json = "{\"id\":\"09da9560-d32e-472d-9570-f2d9d1635c80\",\"version\":null,\"createdDate\":\"2022-12-19T09:21:40.3747269+02:00\",\"lastModifiedDate\":\"2022-12-19T09:21:40.3757096+02:00\",\"beerName\":\"BeerName\",\"beerStyle\":\"ALE\",\"upc\":2342987548954,\"price\":9.87,\"quantityOnHand\":null}";
        BeerDto dto = objectMapper.readValue(json, BeerDto.class);
        System.out.println(dto);
    }
}