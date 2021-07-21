package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.constant.ItemStorageConstant;
import com.training.springbootbuyitem.entity.model.User;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.request.*;
import com.training.springbootbuyitem.entity.response.*;
import com.training.springbootbuyitem.enums.EnumOperation;
import com.training.springbootbuyitem.service.UserService;
import com.training.springbootbuyitem.service.ItemService;
import com.training.springbootbuyitem.utils.LogInterceptor;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@Slf4j
public class BuyController implements IBuyController {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home() {
        return "This is what i was looking for";
    }

    /**
     * @JavaDoc ModelMapper is a mapping tool easily configurable to accommodate most application defined entities check
     * some configuration example at: http://modelmapper.org/user-manual/
     */
    @Autowired
    private ModelMapper mapper;

    @Override
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    @ServiceOperation("createItem")
    public ResponseEntity<CreateItemResponseDto> createItem(@RequestBody @Valid CreateItemRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.CreateItem.name());
        return new ResponseEntity<>(mapper.map(itemService.save(mapper.map(request, Item.class)), CreateItemResponseDto.class), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("item/{id}")
    @ServiceOperation("getItem")
    public ResponseEntity<GetItemResponseDto> getItem(@PathVariable("id") Long id) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.GetItem.name());
        return new ResponseEntity<>(mapper.map(itemService.get(id), GetItemResponseDto.class), HttpStatus.OK);
    }

    @PostMapping("item/get")
    @ServiceOperation("loadItems")
    public ResponseEntity<List<GetItemResponseDto>> getItem(@RequestBody LoadItemsRequestDto idList) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.GetItem.name());
        return new ResponseEntity<>(itemService.get(idList.getIdList()).stream().map(i -> mapper.map(i, GetItemResponseDto.class))
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    @PatchMapping("item/{id}")
    @ServiceOperation("updateItem")
    public ResponseEntity<UpdateItemResponseDto> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.UpdateItem.name());
        item.setItemUid(id);
        return new ResponseEntity<>(mapper.map(itemService.update(item), UpdateItemResponseDto.class), HttpStatus.OK);
    }

    @RequestMapping(value = "item/", method = RequestMethod.PATCH)
    @ServiceOperation("updateItems")
    public ResponseEntity<List<UpdateItemResponseDto>> updateItem(@RequestBody UpdateItemsRequestDto itemList) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.UpdateItem.name());
        log.info("TRACE IDDDDD");
        log.info(MDC.get(ItemStorageConstant.TRACE_ID));
        log.info(MDC.get(ItemStorageConstant.OPERATION));
//        return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(itemService.update(itemList.getItemList()).stream()
                .map(i -> mapper.map(i, UpdateItemResponseDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("item/{id}")
    @ServiceOperation("deleteItem")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.DeleteItem.name());
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("item/all")
    @ServiceOperation("listItems")
    public ResponseEntity<List<GetItemResponseDto>> listItems() {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.ListItem.name());
        return new ResponseEntity<>(itemService.list().stream().map(i -> mapper.map(i, GetItemResponseDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @Override
    @PostMapping("item/{id}/dispatch")
    @ServiceOperation("dispatchItem")
    public ResponseEntity<HttpStatus> dispatchItem(@PathVariable("id") Long id,
                                                   @RequestBody @Valid DispatchItemRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.DispatchItem.name());
        itemService.dispatch(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @ServiceOperation("item/blockItem")
    @RequestMapping(value = "/{id}/block", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> blockItem(@PathVariable("id") Long id,
                                                @RequestBody @Valid DispatchItemRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.BlockItem.name());
        itemService.block(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @ServiceOperation("item/blockItem")
    @RequestMapping(value = "/{id}/{user}/block", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> blockItemForUser(@PathVariable("id") Long id, @PathVariable("user") Long userId,
                                                       @RequestBody @Valid DispatchItemRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.BlockItem.name());
        itemService.blockItemForUser(id, userId, request.getQuantity());
        MDC.clear();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("item/{id}/restock")
    @ServiceOperation("restockItem")
    public ResponseEntity<HttpStatus> restockItem(@PathVariable("id") Long id,
                                                  @RequestBody RestockItemRequestDto request) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.RestockItem.name());
        itemService.restock(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/purchase")
    @ServiceOperation("createPurchase")
    public ResponseEntity<CreatePurchaseResponseDto> createPurchase(@RequestBody() @Valid CreatePurchaseRequestDto purchase) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.CreatePurchase.name());
        return new ResponseEntity<>(mapper.map(itemService.startPurchaseTransaction(purchase), CreatePurchaseResponseDto.class), HttpStatus.OK);
    }

    @DeleteMapping("purchase/confirm")
    @ServiceOperation("confirmPurchase")
    public ResponseEntity<HttpStatus> commitPurchase(@PathVariable("id") Long id) {
        MDC.put(ItemStorageConstant.OPERATION, EnumOperation.RestockItem.name());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("purchase/{id}/cancel")
    @ServiceOperation("cancelPurchase")
    public ResponseEntity<HttpStatus> cancelPurchase(@PathVariable("id") Long id) {
        itemService.cancelPurchase(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
