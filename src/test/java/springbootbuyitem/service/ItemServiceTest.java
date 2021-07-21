package springbootbuyitem.service;

import com.training.springbootbuyitem.BuyItemApplication;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.repository.ItemRepository;
import com.training.springbootbuyitem.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = BuyItemApplication.class)
@RunWith(SpringRunner.class)
public class ItemServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private static final String ITEM_NAME = "banana";
    private static final long ID = 1l;

    @Test
    public void save() {
        Item item = Item.builder().
                name(ITEM_NAME).priceTag(BigDecimal.ONE).stock(BigInteger.ONE).build();

        Item persist = item;
        persist.setItemUid(ID);
        when(itemRepository.save(item)).thenReturn(persist);
        item = itemService.save(item);
        assertThat(item.getItemUid(), is(ID));
    }

    @Test
    public void Get() {
        Item item = Item.builder().
                name(ITEM_NAME).priceTag(BigDecimal.ONE).stock(BigInteger.ONE).build();
        Item persist = item;
        persist.setItemUid(ID);
        when(itemRepository.findById(ID)).thenReturn(Optional.of(persist));

        item = itemService.get(ID);

        assertThat("Should match item id", item, hasProperty("itemUid", is(ID)));
        assertThat("Should match item name", item, hasProperty("name", is(ITEM_NAME)));
        assertThat("Should match item stock", item, hasProperty("priceTag", is(BigDecimal.ONE)));
    }

    @Test(expected = EntityNotFoundException.class)
    public void errorGet() {
        when(itemRepository.findById(ID)).thenReturn(Optional.empty());
        itemService.get(ID);
    }


}