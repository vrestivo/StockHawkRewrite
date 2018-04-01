package com.example.devbox.stockhawkrewrite.view;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
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
    private ItemTouchHelper.SimpleCallback mSwipeCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT && mPresenter!=null){
                    mPresenter.deleteAStock(mAdapter.getTickerAtPosition(viewHolder.getAdapterPosition()));
                }
            }
        };

        mStockRecyclerView = findViewById(R.id.stock_list);
        mAdapter = new StockListAdapter();
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStockRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(mSwipeCallback).attachToRecyclerView(mStockRecyclerView);


        mEnterStockField = findViewById(R.id.ticker_input_edit_text);
        InputFilter lengthLimit = new InputFilter.LengthFilter(5);
        InputFilter capsOnly = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                for (int c = i; c < i1; c++) {
                    if (!Character.isUpperCase(charSequence.charAt(c))) {
                        return "";
                    }
                }
                return null;
            }
        };
        mEnterStockField.setFilters(new InputFilter[]{capsOnly, lengthLimit});

        mAddStockButton = findViewById(R.id.add_stock_button);
        mAddStockButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO remove test logic
                        if(getCountingIdlingResource()!=null){
                            getCountingIdlingResource().setIdleState(false);
                        }

                        String ticker = mEnterStockField.getText().toString();
                        addAStock(ticker);
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
    public void addAStock(String stockToAdd){
        if(mPresenter!=null && stockToAdd != null){
            mPresenter.addAStock(stockToAdd);
        }
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
        //todo remove test logic
        if(getCountingIdlingResource()!=null){
            getCountingIdlingResource().setIdleState(true);
        }
    }

    @Override
    public void showListIsEmpty() {

    }


    @VisibleForTesting
    public MyIdlingResources getCountingIdlingResource(){
        return MyIdlingResources.getInstance();
    }

}
