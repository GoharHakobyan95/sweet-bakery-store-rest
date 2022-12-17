package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.dto.userDto.CreateUserDto;
import am.itspace.sweetbakerystorecommon.dto.userDto.UserAuthDto;
import am.itspace.sweetbakerystorecommon.dto.userDto.UserAuthResponseDto;
import am.itspace.sweetbakerystorecommon.dto.userDto.UserDto;
import am.itspace.sweetbakerystorecommon.entity.Role;
import am.itspace.sweetbakerystorecommon.entity.User;
import am.itspace.sweetbakerystorecommon.service.UserService;

import am.itspace.sweetbakerystorerest.mapper.UserMapper;
import am.itspace.sweetbakerystorerest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserEndpoint {

    private final UserMapper userMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;


    @PostMapping("/user")
    public ResponseEntity<UserDto> register(@RequestBody CreateUserDto createUserDto) {
        Optional<User> existingUser = userService.findByEmail(createUserDto.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userMapper.map(createUserDto);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateAt(new Date());
        user.setActive(true);
        log.info("User with username {} registered։", user.getEmail());
        User saveUser = userService.save(user);
        return ResponseEntity.ok(userMapper.map(saveUser));
    }

    @PostMapping("/user/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthDto userAuthDto) {
        Optional<User> byEmail = userService.findByEmail(userAuthDto.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            if (passwordEncoder.matches(userAuthDto.getPassword(), user.getPassword())) {
                log.info("User with username {} get auth token", user.getEmail());
                return ResponseEntity.ok(UserAuthResponseDto.builder()
                        .token(jwtTokenUtil.generateToken(user.getEmail()))
                        .user(userMapper.map(user))
                        .build()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
