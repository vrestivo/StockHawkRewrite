package com.example.devbox.stockhawkrewrite;

import com.example.devbox.stockhawkrewrite.exceptions.InvalidStockException;
import com.example.devbox.stockhawkrewrite.exceptions.StockHawkException;
import com.example.devbox.stockhawkrewrite.exceptions.UnableToDownloadDataException;
import com.example.devbox.stockhawkrewrite.model.IYFNetDao;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.model.YFNetDao;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by devbox on 2/20/18.
 */

public class YFNetDaoTest {

    private IYFNetDao mYFNetDao;
    private String[] mValidStockTickers = {"TSLA", "IBM", "BA"};
    private String[] mValidnAndInvaliStockTickers = {"TSLA", "ZZZZzzZZ", "IBM", "AAAAAAAAA", "BA"};
    private String[] mInvalidStockTickers = {"AAAAzzz", "ZZZaaaaAA", "AABBBYYYY", "AAAYYYYBBB"};
    private String mInvalidStockTicker = "ZZZZZZZzz";
    private List<StockDto> mStockDtoList;


    @After
    public void cleanup(){
        mStockDtoList = null;
        mYFNetDao = null;
    }

    @Test
    public void yFNetDaoDownloadValidSTocksTest() {
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
            tickers.contains(stockDto.getTicker());
        }
    }


    @Test(expected = InvalidStockException.class)
    public void yFNetDaoDownloadInvalidSingleStockTest() {
        givenInitializedYFNetDao();
        whenFetchingAnInvalidStockThrowsInvalidStockException();
    }

    private void whenFetchingAnInvalidStockThrowsInvalidStockException() {
       StockDto stock = mYFNetDao.fetchASingleStock(mInvalidStockTicker);
       Assert.assertNull(stock);
    }


    @Test
    public void yFNetDaoDownloadStocksWithAnInvalidStockTicker(){
        givenInitializedYFNetDao();
        whenFetchingValidAndInvalidStocksExceptionIsNotThrown();
        validStocksAreReturned();
    }

    private void whenFetchingValidAndInvalidStocksExceptionIsNotThrown() {
       mStockDtoList = mYFNetDao.fetchStocks(mValidnAndInvaliStockTickers);
    }

    private void validStocksAreReturned() {
        Assert.assertNotNull(mStockDtoList);
        List<String> stockValidTickerList = Arrays.asList(mValidStockTickers);
        for(StockDto stock : mStockDtoList){
            Assert.assertTrue("results do not contain given ticker",
                    stockValidTickerList.contains(stock.getTicker()));
        }
    }

    @Test(expected = InvalidStockException.class)
    public void yFNetDaoDownloadInvalidStockListTest(){
        givenInitializedYFNetDao();
        whenDownloadingInvalidStockListExceptionIsThrown();
    }

    private void whenDownloadingInvalidStockListExceptionIsThrown() {
        mStockDtoList = mYFNetDao.fetchStocks(mInvalidStockTickers);
        Assert.assertNull(mStockDtoList);
    }


    //Uncomment to test without network connection
    /*
    @Test(expected = UnableToDownloadDataException.class)
    public void yFNetDaoNoConnectionTest(){
        givenInitializedYFNetDao();
        whenFetchingValidStocksThrowsUnableToDownLoadDataExceptionOnConnectionIssues();
    }
    */

    private void whenFetchingValidStocksThrowsUnableToDownLoadDataExceptionOnConnectionIssues() {
        givenInitializedYFNetDao();
        mStockDtoList = mYFNetDao.fetchStocks(mValidStockTickers);
    }

}
