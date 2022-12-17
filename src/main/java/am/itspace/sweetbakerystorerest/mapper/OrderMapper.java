package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.orderDto.CreateOrderDto;
import am.itspace.sweetbakerystorecommon.dto.orderDto.OrderResponseDto;
import am.itspace.sweetbakerystorecommon.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order map(CreateOrderDto createOrderDto);

    OrderResponseDto map(Order order);

    List<OrderResponseDto> map(List<Order> orders);

}
