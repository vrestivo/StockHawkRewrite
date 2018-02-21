package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.model.IYFNetDao;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;
import junit.framework.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;


/**
 * Created by devbox on 2/20/18.
 */

public class YFNetDaoTest {

    private IYFNetDao mYFNetDao;
    private String[] mValidStockTickers = {"TSLA", "IBM", "BA"};
    private List<StockDto> mStockDtoList;


    @Test
    public void yFNetDaotest() {
        givenInitializedYFNetDao();
        whenFetchingValidStocks();
        returnsStockDtoList();
    }

    private void givenInitializedYFNetDao() {
        mYFNetDao = new YFNetDao();
        Assert.assertNotNull(mYFNetDao);
    }

    private void whenFetchingValidStocks() {
        mStockDtoList = mYFNetDao.fetchStocks(mValidStockTickers);
        Assert.assertNotNull(mStockDtoList);
    }

    private void returnsStockDtoList() {
        for(StockDto stockDto : mStockDtoList){
            List<String> tickers = Arrays.asList(mValidStockTickers);
            tickers.contains(stockDto.getmTicker());
        }
    }


}
