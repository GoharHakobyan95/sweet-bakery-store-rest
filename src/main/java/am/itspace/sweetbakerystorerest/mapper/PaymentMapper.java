package am.itspace.sweetbakerystorerest.mapper;

import am.itspace.sweetbakerystorecommon.dto.paymentDto.CreatePaymentDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.PaymentResponseDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.UpdatePaymentDto;
import am.itspace.sweetbakerystorecommon.entity.Payment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment map(CreatePaymentDto createPaymentDto);


    PaymentResponseDto map(Payment payment);

    Payment map(UpdatePaymentDto updatePaymentDto);

    List<PaymentResponseDto> map(List<Payment> payments);
}
