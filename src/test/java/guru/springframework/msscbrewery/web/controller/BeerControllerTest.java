package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @Test
    void getBeerById() throws Exception {
        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID())
                .beerName("testBeerName")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .build();

        when(beerService.getBeerById(any())).thenReturn(beerDto);
        mockMvc.perform(get("/api/v1/beer/" + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beerName").value("testBeerName"))
                ;
    }


    @Test
    void saveNewBeer() throws Exception {
        BeerDto beerDtoIn = BeerDto.builder()
                .beerName("testBeerName")
                .upc(9874659283476529L)
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal(12.12))
                .build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDtoIn);

        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID())
                .beerName("testBeerName")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .build();

        when(beerService.saveNewBeer(any())).thenReturn(beerDto);
        mockMvc.perform(post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = BeerDto.builder()
                .beerName("Random")
                .upc(9874659283476529L)
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal(12.12))
                .build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBeerById() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+UUID.randomUUID()))
                        .andExpect(status().isNoContent());
    }


}