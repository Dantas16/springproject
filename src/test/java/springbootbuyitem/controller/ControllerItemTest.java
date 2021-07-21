package springbootbuyitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.request.CreateItemRequestDto;
import com.training.springbootbuyitem.service.ItemService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ControllerItemTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private static final String ITEM_NAME = "banana";
    private static final long ID = 1l;

    @Test
    @DisplayName("GET '/' - default msg")
    public void defaultMsgTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This is what i was looking for")));
    }

    @Test
    @DisplayName("POST '/' - create item")
    public void createItemTest() throws Exception {
        CreateItemRequestDto requestItem = CreateItemRequestDto.builder()
                .name(ITEM_NAME)
                .priceTag(BigDecimal.TEN)
                .stock(BigInteger.TEN)
                .market("PT")
                .build();

        Item responseItem = Item.builder()
                .itemUid(ID)
                .build();

        when(itemService.save(any())).thenReturn(responseItem);

        this.mockMvc.perform(post("/item")
                .content(asJsonString(requestItem)))
                .andExpect(status().isCreated());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
