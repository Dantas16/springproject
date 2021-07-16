package com.training.springbootbuyitem.error;

import com.training.springbootbuyitem.constant.BuyItemConstant;

public class NotEnoughStockException extends RuntimeException{
    public NotEnoughStockException(String entity, Long id) {
        this(String.format(BuyItemConstant.NOT_ENOUGH_STOCK_MSG, entity, id));
    }
    private NotEnoughStockException(String message) {
        super(message);
    }
}
