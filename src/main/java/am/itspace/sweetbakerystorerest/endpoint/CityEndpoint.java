package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.cityDto.CityResponseDto;
import am.itspace.sweetbakerystorecommon.dto.cityDto.CreateCityDto;
import am.itspace.sweetbakerystorecommon.dto.cityDto.UpdateCityDto;
import am.itspace.sweetbakerystorecommon.entity.City;
import am.itspace.sweetbakerystorecommon.security.CurrentUser;
import am.itspace.sweetbakerystorecommon.service.CityService;
import am.itspace.sweetbakerystorerest.exception.BaseException;
import am.itspace.sweetbakerystorerest.mapper.CityMapper;
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
@RequestMapping("/api/city")
@RequiredArgsConstructor
@Slf4j
public class CityEndpoint {

    private final CityService cityService;
    private final CityMapper cityMapper;

    @GetMapping
    public ResponseEntity<List<City>> getAllCities(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Endpoint cities called by {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(cityMapper.map(cityService.findAllCities()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDto> getCityById(@AuthenticationPrincipal CurrentUser currentUser,
                                                       @PathVariable("id") int id) {
        log.info("User {} wants to get city", currentUser.getUser().getEmail());
        Optional<City> cityById = cityService.findById(id);
        if (cityById.isEmpty()) {
            log.warn("There is no city by id /{}/", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(cityMapper.map(cityById.get()));
    }


    @PostMapping
    private ResponseEntity<City> saveCity(@AuthenticationPrincipal CurrentUser currentUser,
                                       @RequestBody @Valid CreateCityDto createCityDto) {
        log.info("City {} has been saved by Admin ", currentUser.getUser().getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.save(cityMapper.map(createCityDto)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCity(@AuthenticationPrincipal CurrentUser currentUser,
                                        @Valid @RequestBody UpdateCityDto updateCityDto,
                                        @PathVariable("id") int id) {
        log.info("Admin {} wants to update city", currentUser.getUser().getEmail());
        Optional<City> cityById = cityService.findById(id);
        if (cityById.isEmpty()) {
            log.warn("City with id {} doesn't exist.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        CityResponseDto cityResponseDto = cityService.updateCity(cityById.get(), updateCityDto);
        log.info("City {} has been updated on {}, by Admin {}", cityResponseDto.getName(), currentUser.getUser().getEmail());
        return ResponseEntity.ok((cityMapper.map(updateCityDto)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UpdateCityDto> removeCityById(@AuthenticationPrincipal CurrentUser currentUser,
                                                        @PathVariable("id") int id) {
        log.info("Admin {} wants to remove a city", currentUser.getUser().getEmail());
        Optional<City> cityById = cityService.findById(id);
        if (cityById.isEmpty()) {
            log.warn("City with id {} doesn't exist", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        cityService.deleteById(cityById.get().getId());
        log.info("City has been removed {} by Admin {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok().build();
    }
}
