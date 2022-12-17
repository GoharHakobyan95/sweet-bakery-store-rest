package am.itspace.sweetbakerystorerest.actions;

import am.itspace.sweetbakerystorecommon.entity.Category;
import am.itspace.sweetbakerystorecommon.entity.User;
import am.itspace.sweetbakerystorerest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static am.itspace.sweetbakerystorerest.JsonParser.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestConfiguration
@RequiredArgsConstructor
public class CategoryEdnpointsResultActions {

    private final MockMvc mvc;
    private final JwtTokenUtil tokenUtil;

    public ResultActions findAllResultActions() throws Exception{
        return  mvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions createCategoryResultActions(Category category) throws Exception{
      return   mvc.perform(post("/api/categories")
                .header("Authorization", getToken(category.getUser()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(category)));
    }

    private String getToken(User user) {
        return "Bearer " + tokenUtil.generateToken(user.getEmail());
    }



}
