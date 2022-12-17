package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.orderDto.CreateOrderDto;
import am.itspace.sweetbakerystorecommon.dto.orderDto.OrderResponseDto;
import am.itspace.sweetbakerystorecommon.entity.Order;
import am.itspace.sweetbakerystorecommon.entity.Product;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.OrderService;
import am.itspace.sweetbakerystorecommon.service.ProductService;
import am.itspace.sweetbakerystorerest.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderEndpoint {
    private final OrderService orderService;
    private final ProductService productService;

    private final OrderMapper orderMapper;

    // Endpoint for user to show his all orders
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Endpoint orders called by {}", currentUser.getUser().getEmail());
        List<Order> allOrders = orderService.getAllOrders(currentUser.getUser());
        return ResponseEntity.ok(orderMapper.map(allOrders));
    }

    // Endpoint for get user  by orderId
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable("id") int orderId,
                                                     @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to see a single order", currentUser.getUser().getEmail());
        Optional<Order> optOrder = orderService.findById(orderId);
        if (optOrder.isEmpty()) {
            log.warn("There is no order by id /{}/", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(orderMapper.map(optOrder.get()));
    }

    @PostMapping
    //User create an order
    public ResponseEntity<OrderResponseDto> createAnOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                                   @Valid @RequestBody CreateOrderDto createOrderDto,
                                                   @RequestParam("id") Integer productId,
                                                   @RequestParam("quantity") Integer quantity) {
        log.info("User {} wants to create a new order", currentUser.getUser().getEmail());
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty() ||
                optProduct.get().getInStore() == 0 ||
                optProduct.get().getInStore() < quantity) {
            log.warn("Product doesn't exist or the count of product in order is more than count in store.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product product = optProduct.get();
        log.info("New order has been created for user {}", currentUser.getUser().getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderMapper.map(orderService.saveOrder(currentUser.getUser(), createOrderDto, product, quantity)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponseDto> delete(@PathVariable("id") int orderId,
                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {} wants to remove an order", currentUser.getUser().getEmail());
        Optional<Order> orderOpt = orderService.findById(orderId);
        if (orderOpt.isEmpty()) {
            log.warn("Order not found by id {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!orderOpt.get().getUser().equals(currentUser.getUser())) {
            log.warn("User {} wanted to remove user {} order", currentUser.getUser().getEmail(), orderOpt.get().getUser().getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        orderService.delete(orderOpt.get());
        log.info("User {} removed the order", currentUser.getUser().getEmail());
        return ResponseEntity.ok().build();
    }


}


