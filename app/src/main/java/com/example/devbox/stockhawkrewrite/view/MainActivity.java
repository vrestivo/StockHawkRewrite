package com.example.devbox.stockhawkrewrite.view;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;
import com.example.devbox.stockhawkrewrite.presenter.StockListPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IStockListView {

    private String LOG_TAG = getClass().getCanonicalName();
    private IStockListPresenter mPresenter;
    private RecyclerView mStockRecyclerView;
    private StockListAdapter mAdapter;
    private TextInputEditText mEnterStockField;
    private Button mAddStockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStockRecyclerView = findViewById(R.id.stocks_list);
        mAdapter = new StockListAdapter();
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStockRecyclerView.setAdapter(mAdapter);
        mEnterStockField = findViewById(R.id.ticker_input_edit_text);
        //TODO add input filter
        mAddStockButton = findViewById(R.id.add_stock_button);
        mAddStockButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ticker = mEnterStockField.getText().toString();
                        mPresenter.addAStock(ticker);
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter = new StockListPresenter(this, getApplicationContext());
        mPresenter.loadStocks();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG,"_in onStop()");
        mPresenter.cleanup();
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG,"_in onPause()");
        super.onPause();
    }

    @Override
    public void displayError(String errorMessage) {
        Toast.makeText(getApplicationContext(),
                errorMessage,
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showStockList() {

    }

    @Override
    public void showStockDetails() {

    }

    @Override
    public void forceDataUpdate() {

    }

    @Override
    public void onStockListLoaded(List<StockDto> stockDtoList) {
        if(mAdapter!=null){
            mAdapter.setStockList(stockDtoList);
            //TODO delete
            Toast.makeText(this, "newStockList size: " + stockDtoList.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showListIsEmpty() {

    }

}
