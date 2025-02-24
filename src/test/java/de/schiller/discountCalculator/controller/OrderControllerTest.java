package de.schiller.discountCalculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.schiller.discountCalculator.dto.ItemRequest;
import de.schiller.discountCalculator.dto.OrderRequest;
import de.schiller.discountCalculator.dto.OrderResponse;
import de.schiller.discountCalculator.exception.GlobalExceptionHandler;
import de.schiller.discountCalculator.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        OrderRequest orderRequest = new OrderRequest("customer", List.of(new ItemRequest("product", BigDecimal.TEN, 2)));
        OrderResponse orderResponse = new OrderResponse(1L, "customer", BigDecimal.valueOf(20), BigDecimal.valueOf(20), 0.0);

        when(orderService.createOrder(orderRequest)).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerName").value("customer"))
                .andExpect(jsonPath("$.totalAmount").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$.discountedAmount").value(BigDecimal.valueOf(20)))
                .andExpect(jsonPath("$.discountPercentage").value(0.0));

    }

    @Test
    void testCreateOrder_Failure_MissingCustomerAndItems() throws Exception {
        OrderRequest orderRequest = new OrderRequest(null, null);

        mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.customerName").value("Customer Name is required"))
                .andExpect(jsonPath("$.errors.items").value("At least one item is required"));
    }

    @Test
    void testCreateOrder_Failure_EmptyCustomerAndItems() throws Exception {
        OrderRequest orderRequest = new OrderRequest("", List.of());

        mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.customerName").value("Customer Name is required"))
                .andExpect(jsonPath("$.errors.items").value("At least one item is required"));
    }

    @Test
    void testCreateOrder_Failure_ItemFaultyData() throws Exception {
        OrderRequest orderRequest = new OrderRequest("customer", List.of(new ItemRequest("", BigDecimal.valueOf(-1), 0)));

        mockMvc.perform(post("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors['items[0].productName']").value("Product Name is required"))
                .andExpect(jsonPath("$.errors['items[0].price']").value("Price needs to be positive or zero"))
                .andExpect(jsonPath("$.errors['items[0].quantity']").value("Quantity needs to be positive"));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        Long orderId = 1L;
        OrderResponse orderResponse = new OrderResponse(orderId, "customer", BigDecimal.ONE, BigDecimal.ONE, 0.0);
        when(orderService.getOrderById(orderId)).thenReturn(orderResponse);

        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.customerName").value("customer"))
                .andExpect(jsonPath("$.totalAmount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.discountedAmount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$.discountPercentage").value(0.0));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(null);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        List<OrderResponse> mockOrders = List.of(
                new OrderResponse(1L, "customer", BigDecimal.ONE, BigDecimal.ONE, 0.0),
                new OrderResponse(2L, "customer2", BigDecimal.valueOf(100), BigDecimal.valueOf(95), 5.0)
        );
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        mockMvc.perform(get("/orders")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("customer"))
                .andExpect(jsonPath("$[0].totalAmount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$[0].discountedAmount").value(BigDecimal.ONE))
                .andExpect(jsonPath("$[0].discountPercentage").value(0.0))
                .andExpect(jsonPath("$[1].orderId").value(2L))
                .andExpect(jsonPath("$[1].customerName").value("customer2"))
                .andExpect(jsonPath("$[1].totalAmount").value(BigDecimal.valueOf(100)))
                .andExpect(jsonPath("$[1].discountedAmount").value(BigDecimal.valueOf(95)))
                .andExpect(jsonPath("$[1].discountPercentage").value(5.0));
    }

    @Test
    void testGetAllOrders_EmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/orders")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testDeleteOrderById_Success() throws Exception {
        doNothing().when(orderService).deleteOrderById(anyLong());

        mockMvc.perform(delete("/orders/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteOrderById_Failure() throws Exception {
        doThrow(new ResourceNotFoundException("Order not found with id: 1L")).when(orderService).deleteOrderById(anyLong());

        mockMvc.perform(delete("/orders/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}