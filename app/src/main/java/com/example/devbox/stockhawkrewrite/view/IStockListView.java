package com.example.devbox.stockhawkrewrite.view;

import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

/**
 * TOTO finish
 */

public interface IStockListView {

    public interface IShowStockDetail{
        void showStockDetails(String ticker);
    }

    void displayError(String errorMessage);

    void addAStock(String stockToAdd);

    void showStockList();
    void forceDataUpdate();
    void onStockListLoaded(List<StockDto> stockDtoList);
    void showListIsEmpty();
}

