package com.example.devbox.stockhawkrewrite.view;

import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

/**
 * TOTO finish
 */

public interface IStockListView {
    void displayError(String errorMessage);

    void addAStock(String stockToAdd);

    void showStockList();
    void showStockDetails();
    void forceDataUpdate();
    void onStockListLoaded(List<StockDto> stockDtoList);
    void showListIsEmpty();
}

