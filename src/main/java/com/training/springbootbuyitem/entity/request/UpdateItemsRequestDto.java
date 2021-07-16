package com.training.springbootbuyitem.entity.request;

import com.training.springbootbuyitem.entity.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemsRequestDto {
    private List<Item> itemList;
}
