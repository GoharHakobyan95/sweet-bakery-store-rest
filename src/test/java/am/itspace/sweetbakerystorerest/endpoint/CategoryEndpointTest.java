package am.itspace.sweetbakerystorerest.endpoint;

import am.itspace.sweetbakerystorecommon.entity.User;
import am.itspace.sweetbakerystorecommon.repository.CategoryRepository;
import am.itspace.sweetbakerystorecommon.repository.UserRepository;
import am.itspace.sweetbakerystorerest.actions.CategoryEdnpointsResultActions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static am.itspace.sweetbakerystorerest.MockData.category;
import static am.itspace.sweetbakerystorerest.MockData.user;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Import({CategoryEdnpointsResultActions.class})
class CategoryEndpointTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryEdnpointsResultActions resultActions;

    private User user;

    @BeforeEach
    void settUp() {
        user = userRepository.save(user());
        categoryRepository.saveAll(List.of(category(user), category(user)));
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void findAllCategories() throws Exception {
        resultActions.findAllResultActions()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void createCategory() throws Exception {
        var category = category(user);
        resultActions.createCategoryResultActions(category)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", hasToString(category.getName())))
                .andExpect(jsonPath("$.description", hasToString(category.getDescription())))
                .andExpect(jsonPath("$.user.name", hasToString(category.getUser().getName())));

    }

}