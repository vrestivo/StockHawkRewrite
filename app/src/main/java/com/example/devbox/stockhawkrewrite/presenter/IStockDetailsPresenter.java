package com.example.devbox.stockhawkrewrite.presenter;

import com.example.devbox.stockhawkrewrite.model.StockDto;

public interface IStockDetailsPresenter {


    void getStockByTicker(String tickerToGet);

    void onDataLoaded(StockDto stockDto);

    void notifyError(String errorMassage);

    void cleanup();


}
