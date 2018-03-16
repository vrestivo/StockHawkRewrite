package com.example.devbox.stockhawkrewrite.exceptions;

/**
 * Created by devbox on 3/16/18.
 */

public class EmptyStockListException extends StockHawkException {
    public EmptyStockListException(String message) {
        super(message);
    }
}
