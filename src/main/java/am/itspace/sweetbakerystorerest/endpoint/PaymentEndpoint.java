package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.cityDto.UpdateCityDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.CreatePaymentDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.PaymentResponseDto;
import am.itspace.sweetbakerystorecommon.dto.paymentDto.UpdatePaymentDto;
import am.itspace.sweetbakerystorecommon.entity.Payment;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.PaymentService;
import am.itspace.sweetbakerystorerest.mapper.PaymentMapper;
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
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentEndpoint {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    //   Admin can get all user's payments
    @GetMapping
    public ResponseEntity<List<?>> getAllPayments(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Endpoint payments called by {}", currentUser.getUser().getEmail());
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(paymentMapper.map(payments));
    }

    //    Add card by users
    @PostMapping
    private ResponseEntity<?> saveCardDetails(@AuthenticationPrincipal CurrentUser currentUser,
                                              @RequestBody @Valid CreatePaymentDto createPaymentDto) {
        log.info("Card {} has been saved by User ", currentUser.getUser().getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.saveCard(createPaymentDto, currentUser.getUser()));
    }


    // Endpoint for user to get his all payments
    @GetMapping()
    public ResponseEntity<List<PaymentResponseDto>> getAllPaymentsByUserId(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {} wants to get his all payments", currentUser.getUser().getEmail());
        Optional<List<Payment>> paymentsOpt = paymentService.findByUserId(currentUser.getUser());
        if (paymentsOpt.isEmpty()) {
            log.warn("There is no card by user /{}/", currentUser.getUser().getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(paymentMapper.map(paymentsOpt.get()));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCity(@AuthenticationPrincipal CurrentUser currentUser,
                                        @RequestBody UpdatePaymentDto updatePaymentDto,
                                        @PathVariable("id") int id) {
        log.info("User {} wants to update his card details", currentUser.getUser().getEmail());
        Optional<Payment> paymentOpt = paymentService.findById(id);
        if (paymentOpt.isEmpty()) {
            log.warn("Card with id {} doesn't exist.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        PaymentResponseDto updatedCardDetails = paymentService.update(paymentOpt.get(), updatePaymentDto, currentUser.getUser());
        log.info("Card {} has been updated on {}, by User {}", updatedCardDetails, currentUser.getUser().getEmail());
        return ResponseEntity.ok((paymentMapper.map(updatePaymentDto)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UpdateCityDto> removeCityById(@AuthenticationPrincipal CurrentUser currentUser,
                                                        @PathVariable("id") int id) {
        log.info("User {} wants to remove a card", currentUser.getUser().getEmail());
        Optional<Payment> paymentOpt = paymentService.findById(id);
        if (paymentOpt.isEmpty()) {
            log.warn("Card with id {} doesn't exist", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        paymentService.deleteById(paymentOpt.get().getId());
        log.info("Card has been removed {} by User {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok().build();
    }

}
