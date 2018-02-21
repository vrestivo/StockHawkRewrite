package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.IYFNetDao;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;

import junit.framework.Assert;

import org.junit.Test;

import java.util.List;

/**
 * Created by devbox on 2/20/18.
 */

public class YFNetDaoTest {

    private IYFNetDao mYFNetDao;
    private String[] mStocksToSearch;
    private String[] mValidStockTickers = {"TSLA", "IBM", "BA"};
    private List<StockDto> mStockDtoList;


    @Test
    public void yFNetDaotest(){
        givenInitializedYFNetDao();
        whenSearchForValidStocks();
        returnsStockDtoList();
    }

    private void givenInitializedYFNetDao() {
        mYFNetDao = new YFNetDao();
        Assert.assertNotNull(mYFNetDao);
    }

    private void whenSearchForValidStocks() {
        mStockDtoList = mYFNetDao.fetchStocks();
        Assert.assertNotNull(mStockDtoList);
    }

    private void returnsStockDtoList() {
        int counter = 0;

        for(StockDto stockDto : mStockDtoList){
            Assert.assertEquals(stockDto.getmTicker(), mValidStockTickers[counter]);
            counter++;
        }
    }


}
