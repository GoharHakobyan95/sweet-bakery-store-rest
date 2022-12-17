package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.addressDto.AddressResponseDto;
import am.itspace.sweetbakerystorecommon.dto.addressDto.CreateAddressDto;
import am.itspace.sweetbakerystorecommon.dto.addressDto.UpdateAddressDto;
import am.itspace.sweetbakerystorecommon.entity.Address;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.AddressService;
import am.itspace.sweetbakerystorerest.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/addresses")
public class AddressEndpoint {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @GetMapping
    public List<AddressResponseDto> getAllAddresses(@PageableDefault(size = 9) Pageable pageable,
                                                    @AuthenticationPrincipal CurrentUser currentUser) {

        Page<Address> paginated = addressService.findPaginated(pageable);
        log.info("Endpoint /api/addresses called by {}", currentUser.getUser().getEmail());
        return paginated.stream().map(addressMapper::map).toList();
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> creteAddress(@RequestBody CreateAddressDto createAddressDto,
                                                @AuthenticationPrincipal CurrentUser currentUser) {
        Address savedAddress = addressService.saveAddress(addressMapper.map(createAddressDto));
        log.info("Endpoint /api/addresses created by {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(addressMapper.map(savedAddress));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") int id,
                                                 @RequestBody UpdateAddressDto updateAddressDto,
                                                 @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Endpoint /api/addresses updated by {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(addressService.saveAddress(addressMapper.map(updateAddressDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AddressResponseDto> deleteAddress(@PathVariable("id") int id,
                                           @AuthenticationPrincipal CurrentUser currentUser) {
        addressService.deleteAddressById(id);
        log.info("Endpoint /api/addresses deleted by {}", currentUser.getUser().getEmail());
        return ResponseEntity.noContent().build();

    }

}
