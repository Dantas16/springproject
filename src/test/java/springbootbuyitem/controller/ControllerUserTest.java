package springbootbuyitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.configuration.auth.JwtTokenUtil;
import com.training.springbootbuyitem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Slf4j
public class ControllerUserTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MockMvc mockMvc;

    private static final String USERNAME = "username1";
    private static final String PASSWORD = "password1";
    private static final String mockToken = "mockToken";

    @Test
    @DisplayName("GET '/user/all' - unauthorized")
    public void unauthorizedTest() throws Exception {
        this.mockMvc.perform(get("/user/all"))
                .andExpect(status().isUnauthorized());
    }

    //    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginDto authenticationRequest) {
//
//        final UserDetails userDetails = userService
//                .loadUserByUsername(authenticationRequest.getUsername());
//
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return new ResponseEntity<>(mapper.map(new UserLoginResponseDto(token), UserLoginResponseDto.class), HttpStatus.OK);
//    }

    @Test
    @DisplayName("POST '/user/login' - login")
    public void loginTest() throws Exception {
//        UserAuth user = UserAuth.builder()
//                .build();
//
//        UserLoginDto userLoginDto = UserLoginDto.builder()
//                .username(USERNAME)
//                .password(PASSWORD)
//                .build();
//
//        when(userService.loadUserByUsername(any())).thenReturn(user);
//        when(jwtTokenUtil.generateToken(any())).thenReturn(mockToken);
//
//        this.mockMvc.perform(post("/user/login")
//                .content(asJsonString(userLoginDto)))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$.jwtToken", is(mockToken)));
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
