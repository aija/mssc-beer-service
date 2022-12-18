package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
//intentional import change to support documentation generating
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.guru", uriPort = 80)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "guru.springframework.msscbrewery.web.mappers")
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
        mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID())
                .param("iscold", "yes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beerName").value("testBeerName"))
                .andDo(document("v1/beer",
                        pathParameters(
                            parameterWithName("beerId").description("UUID of desired beer to get")
                        ),
                        requestParameters(
                            parameterWithName("iscold").description("Is Beer Cold Query param")
                        ),
                        responseFields(
                            fieldWithPath("id").description("Id of Beer"),
                            fieldWithPath("version").description("Version Number"),
                            fieldWithPath("createdDate").description("Date Created"),
                            fieldWithPath("lastModifiedDate").description("Last Modified Date"),
                            fieldWithPath("beerName").description("Beer Name"),
                            fieldWithPath("beerStyle").description("Beer Style"),
                            fieldWithPath("upc").description("UPC of Beer"),
                            fieldWithPath("price").description("Price"),
                            fieldWithPath("quantityOnHand").description("Quantity on Hand")
                        )
                ));
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

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        BeerDto beerDto = BeerDto.builder().id(UUID.randomUUID())
                .beerName("testBeerName")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .build();

        when(beerService.saveNewBeer(any())).thenReturn(beerDto);
        mockMvc.perform(post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer",
                    requestFields(
                        fields.withPath("id").ignored(),
                        fields.withPath("version").ignored(),
                        fields.withPath("createdDate").ignored(),
                        fields.withPath("lastModifiedDate").ignored(),
                        fields.withPath("beerName").description("Name of the Beer"),
                        fields.withPath("beerStyle").description("Style of the Beer"),
                        fields.withPath("upc").description("Beer UPC").attributes(),
                        fields.withPath("price").description("Beer Price"),
                        fields.withPath("quantityOnHand").ignored()
                    )
                ));
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

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}