package com.training.springbootbuyitem.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePurchaseResponseDto {
    private Long id;
    private Date date;
    private String itemName;
    private BigInteger quantity;
    private BigInteger quantityDispatched;
    private BigDecimal price;
    private String state;
}
