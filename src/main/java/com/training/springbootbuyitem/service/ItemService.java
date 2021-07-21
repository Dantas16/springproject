package com.training.springbootbuyitem.service;

import com.training.springbootbuyitem.entity.model.BlockedItem;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.model.Purchase;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.request.CreatePurchaseRequestDto;
import com.training.springbootbuyitem.entity.response.CreatePurchaseResponseDto;
import com.training.springbootbuyitem.enums.EnumEntity;
import com.training.springbootbuyitem.enums.EnumItemState;
import com.training.springbootbuyitem.enums.EnumPurchaseState;
import com.training.springbootbuyitem.error.EntityNotFoundException;
import com.training.springbootbuyitem.error.NotEnoughStockException;
import com.training.springbootbuyitem.repository.BlockedItemRepository;
import com.training.springbootbuyitem.repository.ItemRepository;
import com.training.springbootbuyitem.repository.PurchaseRepository;
import com.training.springbootbuyitem.utils.properties.ItemStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService implements IItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BlockedItemRepository blockedItemRepository;

    @Autowired
    private ItemStorageProperties itemStorageProperties;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /**
     * @JavaDoc RestTemplate is a synchronous Http Client which is supported by Pivotal development team take into
     * consideration this client is deprecated and shall not be supported for LTS use instead the newly Http Client
     * WebClient which is capable of synchronous & asynchronous invocations check some code samples at:
     * https://spring.io/guides/gs/consuming-rest/
     */
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IUserService userService;

    @Override
    public List<Item> list() {
        return itemRepository.findAll();
    }

    @Override
    public Item get(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(EnumEntity.ITEM.name(), id));
    }

    // TODO - ex 10
    @Override
    public List<Item> get(List<Long> id) {

        return itemRepository.findAllById(id);
    }

    @Override
    public void delete(Long id) {
        itemRepository.delete(get(id));
    }

    private boolean isValidState(String state) {
        try {
            EnumItemState.valueOf(state.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private Item updateItem(Item persistedItem, Item item) {
        // erro?
//		if (!StringUtils.hasText(item.getName())) {
//			persistedItem.setName(item.getName());
//		}
        if (StringUtils.hasText(item.getName())) {
            persistedItem.setName(item.getName());
        }
        if (!StringUtils.isEmpty(item.getDescription())) {
            persistedItem.setDescription(item.getDescription());
        }
        if (!StringUtils.isEmpty(item.getMarket())) {
            persistedItem.setMarket(item.getMarket());
        }
        if (item.getStock() != null && item.getStock().intValue() >= 0) {
            persistedItem.setStock(item.getStock());
        }
        if (item.getPriceTag() != null && item.getPriceTag().longValue() >= 0.0) {
            persistedItem.setPriceTag(item.getPriceTag());
        }
        if (isValidState(item.getState())) {
            persistedItem.setState(item.getState().toUpperCase(Locale.ROOT));
        }
        persistedItem.setPurchases(item.getPurchases());

        return persistedItem;
    }

    @Override
    public Item update(Item item) {
        Item persistedItem = get(item.getItemUid());

        updateItem(persistedItem, item);

        log.info(persistedItem.toString());
        itemRepository.save(persistedItem);
        return persistedItem;
    }

    @Override
    public List<Item> update(List<Item> itemList) {

        List<Item> persistedList = itemList.stream().map(persistedItem -> {
            Item item = get(persistedItem.getItemUid());
            persistedItem.setPurchases(item.getPurchases());
            return updateItem(persistedItem, item);
        }).collect(Collectors.toList());

        return itemRepository.saveAll(persistedList);
    }

    @Override
    public Item save(Item item) {
        item.setState(EnumItemState.AVAILABLE.name());
        log.info(item.toString());
        return itemRepository.save(item);
    }

    @Override
    public void restock(Long id, Integer quantity) {
        Item item = get(id);
        item.setStock(item.getStock().add(BigInteger.valueOf(quantity)));
        itemRepository.save(item);
    }


    //TODO create the dispatch method that use "quantity"  items from item stock for the item represented by id
    @Override
    public void dispatch(Long id, Integer quantity) {
        Purchase purchase = purchaseRepository.findOldestToDispatch(id);
        log.info("---PURCHASE----");
        log.info(purchase.getItem().getName());

        BlockedItem blockedItem = getBlockedItem(purchase.getItem().getItemUid());
        Item item = get(purchase.getItem().getItemUid());

        BigInteger quantityLeftToDispatch = purchase.getQuantity().subtract(purchase.getQuantityDispatched());
        BigInteger amountToDispatch = BigInteger.valueOf(quantity);
        int dispatchedQuantityComparison = BigInteger.valueOf(quantity).compareTo(quantityLeftToDispatch);

        if (dispatchedQuantityComparison >= 0) {
            amountToDispatch = quantityLeftToDispatch;
            setFinalizedPurchase(purchase);
        }

        purchase.setQuantityDispatched(purchase.getQuantityDispatched().add(amountToDispatch));
        blockedItem.setReservedQuantity(blockedItem.getReservedQuantity().subtract(amountToDispatch));
        item.setStock(item.getStock().subtract(amountToDispatch));

        BigInteger leftOverQuantity = BigInteger.valueOf(quantity).subtract(amountToDispatch);
        if (leftOverQuantity.compareTo(BigInteger.valueOf(0)) > 0) {
            dispatch(id, leftOverQuantity.intValue());
        }

        purchaseRepository.save(purchase);
        blockedItemRepository.save(blockedItem);
        save(item);
    }

    @Override
    public void block(Long id, Integer quantity) {
        Item item = get(id);
        BlockedItem blockedItem = blockedItemExists(id) ? getBlockedItem(id) : new BlockedItem(id, item);
//		blockedItem.addToReserve(BigInteger.valueOf(quantity));
        addToReservedItem(blockedItem, BigInteger.valueOf(quantity));
        blockedItemRepository.save(blockedItem);
    }

    @Override
    public void blockItemForUser(Long id, Long userId, Integer quantity) {
        User user = userService.get(userId);
        Item item = get(id);
        BlockedItem blockedItem = blockedItemExists(id) ? getBlockedItem(id) : new BlockedItem(id, item);

//		blockedItem.addToReserve(BigInteger.valueOf(quantity));
        addToReservedItem(blockedItem, BigInteger.valueOf(quantity));

        Purchase purchase = new Purchase(user, item, Date.valueOf(LocalDate.now()),
                BigInteger.valueOf(quantity), BigInteger.valueOf(0),
                item.getPriceTag().multiply(BigDecimal.valueOf(quantity)));

        user.setPurchase(purchase);

        userService.update(user);
        blockedItemRepository.save(blockedItem);
        purchaseRepository.save(purchase);
//		save(item);
    }

    public CreatePurchaseResponseDto startPurchaseTransaction(CreatePurchaseRequestDto purchaseDetails) {
        User user = userService.get((long) purchaseDetails.getUserId());
        Item item = get((long) purchaseDetails.getItemId());

        if (!hasStockToReserve(item.getItemUid(), purchaseDetails.getQuantity())) {
            throw new NotEnoughStockException(item.getName(), item.getItemUid());
        }

        block(item.getItemUid(), purchaseDetails.getQuantity());

        Purchase purchase = new Purchase(user, item, Date.valueOf(LocalDate.now()),
                BigInteger.valueOf(purchaseDetails.getQuantity()), BigInteger.valueOf(0),
                item.getPriceTag().multiply(BigDecimal.valueOf(purchaseDetails.getQuantity())));

        user.setPurchase(purchase);
        userService.update(user);
        purchaseRepository.save(purchase);
        return CreatePurchaseResponseDto.builder().id(purchase.getId())
                                    .price(purchase.getPrice())
                                    .date(purchase.getDate())
                                    .state(purchase.getState())
                                    .quantity(purchase.getQuantity())
                                    .quantityDispatched(purchase.getQuantityDispatched())
                                    .itemName(item.getName())
                                    .build();
    }

    private void addToReservedItem(BlockedItem reservedItem, BigInteger quantity) {
        if (reservedItem.getItem().getStock().subtract(reservedItem.getReservedQuantity())
                .compareTo(quantity) < 0) {
            throw new NotEnoughStockException(EnumEntity.ITEM.name(), reservedItem.getId());
        }
        reservedItem.setReservedQuantity(reservedItem.getReservedQuantity().add(quantity));
    }

    public void cancelPurchase(Long id) {
        Purchase purchase = purchaseRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(EnumEntity.PURCHASE_TRANSACTION.name(), id));
        setCanceledPurchase(purchase);
        purchaseRepository.save(purchase);
    }

    private boolean blockedItemExists(Long id) {
        return blockedItemRepository.existsById(id);
    }

    private boolean ItemExists(Long id) {
        return itemRepository.existsById(id);
    }

    private BlockedItem getBlockedItem(Long id) {
        return blockedItemRepository.getOne(id);
    }

    private void setFinalizedPurchase(Purchase purchase) {
        purchase.setState(EnumPurchaseState.FINALIZED.name());
    }

    private void setCanceledPurchase(Purchase purchase) {
        purchase.setState(EnumPurchaseState.CANCELLED.name());
    }

    private boolean hasStockToReserve(Long id, Integer quantity) {

        BigInteger reservedStock = blockedItemExists(id) ? getBlockedItem(id).getReservedQuantity() :
                BigInteger.valueOf(0);

        Item item = get(id);

        BigInteger quantityAvailableToReserve = item.getStock().subtract(reservedStock);
        return quantityAvailableToReserve.compareTo(BigInteger.valueOf(quantity)) >= 0;
    }
}
