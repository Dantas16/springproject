package com.training.springbootbuyitem.controller;

import com.training.springbootbuyitem.entity.model.Buyer;
import com.training.springbootbuyitem.entity.model.Item;
import com.training.springbootbuyitem.entity.request.CreateBuyerRequestDto;
import com.training.springbootbuyitem.entity.request.CreateItemRequestDto;
import com.training.springbootbuyitem.entity.request.DispatchItemRequestDto;
import com.training.springbootbuyitem.entity.request.RestockItemRequestDto;
import com.training.springbootbuyitem.entity.response.*;
import com.training.springbootbuyitem.service.BuyerService;
import com.training.springbootbuyitem.service.ItemService;
import com.training.springbootbuyitem.utils.annotation.ServiceOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private ItemService itemService;

    @Autowired
    private BuyerService buyerService;

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
//    @PostMapping
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ServiceOperation("createItem")
    public ResponseEntity<CreateItemResponseDto> createItem(@RequestBody @Valid CreateItemRequestDto request) {
        log.info("Post mapping create Item");
        return new ResponseEntity<>(mapper.map(itemService.save(mapper.map(request, Item.class)), CreateItemResponseDto.class), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("/{id}")
    @ServiceOperation("getItem")
    public ResponseEntity<GetItemResponseDto> getItem(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mapper.map(itemService.get(id), GetItemResponseDto.class), HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    @ServiceOperation("updateItem")
    public ResponseEntity<UpdateItemResponseDto> updateItem(@PathVariable("id") Long id, @RequestBody Item item) {
        item.setItemUid(id);
        return new ResponseEntity<>(mapper.map(itemService.update(item), UpdateItemResponseDto.class), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    @ServiceOperation("deleteItem")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/all")
    @ServiceOperation("listItems")
    public ResponseEntity<List<GetItemResponseDto>> listItems() {
        return new ResponseEntity<>(itemService.list().stream().map(i -> mapper.map(i, GetItemResponseDto.class)).collect(
                Collectors.toList()), HttpStatus.OK);
    }

    @Override
    @PostMapping("/{id}/dispatch")
    @ServiceOperation("dispatchItem")
    public ResponseEntity<HttpStatus> dispatchItem(@PathVariable("id") Long id,
                                                   @RequestBody DispatchItemRequestDto request) {
        itemService.dispatch(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @ServiceOperation("blockItem")
    @RequestMapping(value = "/{id}/block", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> blockItem(@PathVariable("id") Long id,
                                                @RequestBody DispatchItemRequestDto request) {
        itemService.block(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @ServiceOperation("blockItem")
    @RequestMapping(value = "/{id}/{user}/block", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> blockItemForUser(@PathVariable("id") Long id, @PathVariable("user") Long userId,
                                                       @RequestBody DispatchItemRequestDto request) {
        itemService.block(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @Override
    @PostMapping("/{id}/restock")
    @ServiceOperation("restockItem")
    public ResponseEntity<HttpStatus> restockItem(@PathVariable("id") Long id,
                                                  @RequestBody RestockItemRequestDto request) {
        itemService.restock(id, request.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Buyer mappings

    @Override
    @RequestMapping(value = "buyer", method = RequestMethod.POST)
    @ServiceOperation("createBuyer")
    public ResponseEntity<CreateBuyerResponseDto> createBuyer( @RequestBody @Valid CreateBuyerRequestDto request) {
        return new ResponseEntity<>(mapper.map(buyerService.save(mapper.map(request, Buyer.class)), CreateBuyerResponseDto.class), HttpStatus.CREATED);
    }

    @Override
    @GetMapping("buyer/{id}")
    @ServiceOperation("getBuyer")
    public ResponseEntity<GetBuyerResponseDto> getBuyer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mapper.map(buyerService.get(id), GetBuyerResponseDto.class), HttpStatus.OK);
    }

    @Override
    @GetMapping("buyer/all")
    @ServiceOperation("listBuyers")
    public ResponseEntity<List<GetBuyerResponseDto>> listBuyers() {
        return new ResponseEntity<>(buyerService.list().stream().map(i -> mapper.map(i, GetBuyerResponseDto.class)).collect(
                Collectors.toList()), HttpStatus.OK);
    }

}
