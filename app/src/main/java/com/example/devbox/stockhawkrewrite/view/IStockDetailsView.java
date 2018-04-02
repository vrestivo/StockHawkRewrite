package com.example.devbox.stockhawkrewrite.view;

import com.example.devbox.stockhawkrewrite.model.StockDto;

public interface IStockDetailsView {

    void getStockData(String stockTicker);
    void onDataLoaded(StockDto stockDto);
    void showEmpty();
    void cleanup();
    void displayError(String ErrorMessage);


}
