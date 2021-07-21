package springbootbuyitem.repository;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
public class TestUserRepository {

    @Autowired
    private UserRepository userRepository;

    @Sql("/delete_all.sql")
    @Test
    public void createUserTest() {
        User user = User.builder()
                .name("userTest")
                .username("usernameTest")
                .password("passwordTest")
                .build();

        User userSaved = userRepository.save(user);
        assertThat("Should have same name", userSaved, hasProperty("name", is("userTest")));
    }
}
