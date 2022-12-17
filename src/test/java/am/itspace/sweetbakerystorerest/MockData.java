package am.itspace.sweetbakerystorerest;

import am.itspace.sweetbakerystorecommon.entity.Category;
import am.itspace.sweetbakerystorecommon.entity.Role;
import am.itspace.sweetbakerystorecommon.entity.User;

public class MockData {

    public static User user() {
        return User.builder()
                .name("poxos")
                .surname("poxosyan")
                .email("poxos@mail.com")
                .phone("093896357")
                .password("asdasdasd45679")
                .role(Role.ADMIN)
                .build();
    }

    public static Category category(User user) {
        return Category.builder()
                .name("Cakes")
                .description("Cakes with cream and chocolate")
                .user(user)
                .build();
    }
}
