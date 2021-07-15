package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBuyerResponseDto extends CreateBuyerResponseDto{
    private String name;
}
