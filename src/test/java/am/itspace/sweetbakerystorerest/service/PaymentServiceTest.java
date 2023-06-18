package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.dto.orderDto.CheckoutDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.CreatePaymentDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.PaymentResponseDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.UpdatePaymentDto;
import am.itspace.sweetbakerystorecommon.entity.*;
import am.itspace.sweetbakerystorecommon.repository.PaymentRepository;
import am.itspace.sweetbakerystorecommon.service.OrderService;
import am.itspace.sweetbakerystorecommon.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService testObject;
    @Mock
    private ModelMapper modelMapper;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1)
                .name("John")
                .surname("Smith")
                .role(Role.USER)
                .createAt(new Date())
                .isActive(true)
                .email("John@mail.ru")
                .password("john888")
                .phone("77777777")
                .verifyToken("token")
                .address(new Address(5, "165 apt.", new City(1, "Yerevan")))
                .build();
    }


    @Test
    public void when_saveWithCheckoutDto_then_objectShouldBeStored() {
        //given
        CheckoutDto checkoutDto = new CheckoutDto("123456", "505", new Date(),
                Status.PENDING, CardType.VISA, 2, 3);

        Payment payment = Payment.builder()
                .user(user)
                .status(Status.PAYED)
                .cardNumber(checkoutDto.getCardNumber())
                .cvcCode(checkoutDto.getCvcCode())
                .cardType(checkoutDto.getCardType())
                .expirationDate(checkoutDto.getExpirationDate())
                .build();
        when(paymentRepository.save(payment)).thenReturn(payment);

        //when
        testObject.save(checkoutDto, user);

        //then
        verify(paymentRepository, times(1)).save(payment);
        verify(orderService, times(1)).saveOrder(checkoutDto, payment, user);
    }

    @Test
    public void when_saveCard_then_objectShouldBeStored() {
        // given
        CreatePaymentDto createPaymentDto = CreatePaymentDto.builder().build();
        Payment payment = Payment.builder()
                .user(user)
                .cardNumber(createPaymentDto.getCardNumber())
                .cardType(createPaymentDto.getCardType())
                .cvcCode(createPaymentDto.getCvcCode())
                .expirationDate(createPaymentDto.getExpirationDate())
                .status(createPaymentDto.getStatus())
                .build();
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(modelMapper.map(payment, Payment.class)).thenReturn(payment);

        // when
        Payment response = testObject.saveCard(createPaymentDto, user);

        // then
        assertEquals(payment, response);
        verify(paymentRepository, times(1)).save(payment);
        verify(modelMapper, times(1)).map(payment, Payment.class);
    }

    @Test
    public void when_findById_then_objectShouldBeRetrieved() {
        // given
        int id = 1;
        Payment payment = Payment.builder().id(1).build();
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        // when
        Optional<Payment> response = testObject.findById(id);

        // then
        assertEquals(payment, response.get());
        verify(paymentRepository, times(1)).findById(id);
    }


    @Test
    public void when_update_then_objectShouldBeUpdated() {
        //given
        UpdatePaymentDto updatePaymentDto = new UpdatePaymentDto(1, "5555777788889999", "505", new Date(),
                Status.PAYED, CardType.VISA);
        Payment payment = Payment.builder()
                .id(updatePaymentDto.getId())
                .cardNumber(updatePaymentDto.getCardNumber())
                .cvcCode(updatePaymentDto.getCvcCode())
                .status(updatePaymentDto.getStatus())
                .expirationDate(updatePaymentDto.getExpirationDate())
                .user(user)
                .build();
        PaymentResponseDto paymentResponseDto = PaymentResponseDto.builder()
                .cardNumber(payment.getCardNumber())
                .cardType(payment.getCardType())
                .cvcCode(payment.getCvcCode())
                .status(payment.getStatus())
                .expirationDate(payment.getExpirationDate())
                .build();
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(modelMapper.map(payment, PaymentResponseDto.class)).thenReturn(paymentResponseDto);

        // when
        PaymentResponseDto response = testObject.update(updatePaymentDto, user);

        // then
        assertEquals(paymentResponseDto, response);
        verify(paymentRepository, times(1)).save(payment);
        verify(modelMapper, times(1)).map(payment, PaymentResponseDto.class);
    }

    @Test
    public void when_findPaginated_then_pageShouldBeRetrieved() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        List<Payment> list = new ArrayList<>();
        list.add(Payment.builder().id(1).cardNumber("12345").build());
        list.add(Payment.builder().id(2).cardNumber("56789").build());
        Page<Payment> paymentPages = new PageImpl<>(list, pageable, list.size());
        when(paymentRepository.findAll(pageable)).thenReturn(paymentPages);

        // when
        Page<Payment> response = testObject.findPaginated(pageable);

        // then
        assertEquals(paymentPages, response);
        assertEquals(2, response.getContent().size());
        assertEquals("12345", response.getContent().get(0).getCardNumber());
        assertEquals("56789", response.getContent().get(1).getCardNumber());
        verify(paymentRepository, times(1)).findAll(pageable);
    }

    @Test
    public void when_deleteById_then_objectShouldBeDeleted() {
        int id = 1;
        testObject.deleteById(id);
        verify(paymentRepository, times(1)).deleteById(id);
    }

}

