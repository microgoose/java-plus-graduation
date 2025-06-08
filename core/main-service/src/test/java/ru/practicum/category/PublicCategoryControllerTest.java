package ru.practicum.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.MainService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.GetCategoriesParams;
import ru.practicum.category.service.CategoryService;
import ru.practicum.config.StatsClientConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MainService.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class PublicCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private DiscoveryClient discoveryClient;

    @MockBean
    private StatsClientConfig statsClientConfig;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CategoryDto goodCat1 = CategoryDto.builder()
            .id(1L)
            .name("Football")
            .build();

    private final CategoryDto goodCat2 = CategoryDto.builder()
            .id(2L)
            .name("Music")
            .build();

    @Test
    @SneakyThrows
    public void getInfoById_whenCorrectId_thenGetCategory() {
        // Настройка моков
        when(statsClientConfig.getServiceId()).thenReturn("stats-service");
        ServiceInstance mockInstance = mock(ServiceInstance.class);
        when(mockInstance.getHost()).thenReturn("localhost");
        when(mockInstance.getPort()).thenReturn(9090);
        when(discoveryClient.getInstances("stats-service")).thenReturn(java.util.List.of(mockInstance));

        Long id = 1L;
        when(categoryService.getCategory(id)).thenReturn(goodCat1);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/categories/{catId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();

        assertNotNull(mvcResult.getResponse());
        verify(categoryService, times(1)).getCategory(id);
    }

    @Test
    @SneakyThrows
    public void getInfoById_whenInvalidId_thenBadRequest() {
        // Настройка моков
        when(statsClientConfig.getServiceId()).thenReturn("stats-service");
        ServiceInstance mockInstance = mock(ServiceInstance.class);
        when(mockInstance.getHost()).thenReturn("localhost");
        when(mockInstance.getPort()).thenReturn(9090);
        when(discoveryClient.getInstances("stats-service")).thenReturn(java.util.List.of(mockInstance));

        Long id = 0L;
        when(categoryService.getCategory(id))
                .thenThrow(new RuntimeException("Got incorrect requestBody"));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/categories/{catId}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void getCatList_whenCallWithoutParams_thenGetCategories() {
        // Настройка моков
        when(statsClientConfig.getServiceId()).thenReturn("stats-service");
        ServiceInstance mockInstance = mock(ServiceInstance.class);
        when(mockInstance.getHost()).thenReturn("localhost");
        when(mockInstance.getPort()).thenReturn(9090);
        when(discoveryClient.getInstances("stats-service")).thenReturn(java.util.List.of(mockInstance));

        List<CategoryDto> cats = List.of(goodCat1, goodCat2);
        ArgumentCaptor<GetCategoriesParams> searchParam = ArgumentCaptor.forClass(GetCategoriesParams.class);

        when(categoryService.getCatList(any())).thenReturn(cats);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        assertNotNull(mvcResult.getResponse());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CategoryDto> factList = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        assertEquals(2, factList.size());
        assertTrue(factList.contains(goodCat2));
        verify(categoryService).getCatList(searchParam.capture());

        assertEquals(0, searchParam.getValue().getFrom());
        assertEquals(10, searchParam.getValue().getSize());
    }

    @Test
    @SneakyThrows
    public void getCatList_whenCallWithParams_thenGetCategories() {
        // Настройка моков
        when(statsClientConfig.getServiceId()).thenReturn("stats-service");
        ServiceInstance mockInstance = mock(ServiceInstance.class);
        when(mockInstance.getHost()).thenReturn("localhost");
        when(mockInstance.getPort()).thenReturn(9090);
        when(discoveryClient.getInstances("stats-service")).thenReturn(java.util.List.of(mockInstance));

        List<CategoryDto> cats = List.of(goodCat1, goodCat2);
        ArgumentCaptor<GetCategoriesParams> searchParam = ArgumentCaptor.forClass(GetCategoriesParams.class);

        when(categoryService.getCatList(any())).thenReturn(cats);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/categories")
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(5))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        assertNotNull(mvcResult.getResponse());

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<CategoryDto> factList = objectMapper.readValue(jsonResponse, new TypeReference<>() {});
        assertEquals(2, factList.size());
        assertTrue(factList.contains(goodCat2));
        verify(categoryService).getCatList(searchParam.capture());

        assertEquals(1, searchParam.getValue().getFrom());
        assertEquals(5, searchParam.getValue().getSize());
    }
}