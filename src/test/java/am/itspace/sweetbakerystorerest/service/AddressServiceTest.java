package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.Address;
import am.itspace.sweetbakerystorecommon.entity.City;
import am.itspace.sweetbakerystorecommon.repository.AddressRepository;
import am.itspace.sweetbakerystorecommon.repository.CityRepository;
import am.itspace.sweetbakerystorecommon.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private CityRepository cityRepository;
    @Mock
    private AddressRepository addressRepository;

    //Given that we have initialised a AddressService, CityRepository and AddressRepository,
    private City city;
    private Address address;

    // When an instance of City and Address are created
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressService = new AddressService(addressRepository, cityRepository);
        city = new City("Yerevan");
        address = new Address("Isahakyan", city);
    }


    // Then setup method will be used to initialise the mocks and assign their respective values.
    @Test
    void testSaveAddressAdd() {
        given(addressRepository.save(address)).willReturn(address);
        Address result = addressService.saveAddress(address);
        assertNotNull(result);
        assertEquals(address, result);
    }

    /* Given that an Address is saved, when saveAddress method is called,
    then the Address should not be null and should equal the address that is saved.*/
    @Test
    void testSave() {
        given(cityRepository.save(city)).willReturn(city);
        given(addressRepository.save(address)).willReturn(address);
        addressService.save(address, city);
        assertNotNull(address);
        assertNotNull(city);
    }

    @Test
    void testFindAll() {
        ArrayList<Address> list = new ArrayList<>();
        list.add(address);
        given(addressRepository.findAll()).willReturn(list);
        List<Address> result = addressService.findAll();
        assertNotNull(result);
        assertEquals(list, result);
    }

    /* Given that an Address and City is saved,
    when save method is called, then the Address and City should not be null.*/
    @Test
    void testFindById() {
        given(addressRepository.findById(1)).willReturn(Optional.of(address));
        Optional<Address> result = addressService.findById(1);
        assertNotNull(result);
        assertEquals(address, result.get());
    }

}