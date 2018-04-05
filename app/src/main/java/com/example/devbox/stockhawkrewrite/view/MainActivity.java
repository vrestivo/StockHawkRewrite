package com.example.devbox.stockhawkrewrite.view;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
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

public class MainActivity extends AppCompatActivity implements IStockListView,
        IStockListView.IShowStockDetail {

    public static String ACTION_ADD_STOCK = "ACTION_ADD_STOCK";
    private String LOG_TAG = getClass().getCanonicalName();
    private IStockListPresenter mPresenter;
    private RecyclerView mStockRecyclerView;
    private StockListAdapter mAdapter;
    private FloatingActionButton mAddStockButton;
    private ItemTouchHelper.SimpleCallback mSwipeCallback;
    private MyIdlingResources myIdlingResources;
    private AddAStockDialog mAddAStockDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStockRecyclerView = findViewById(R.id.stock_list);
        mAdapter = new StockListAdapter(this);
        mStockRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mStockRecyclerView.setAdapter(mAdapter);
        setupSwipeCallback();
        new ItemTouchHelper(mSwipeCallback).attachToRecyclerView(mStockRecyclerView);


        mAddStockButton = findViewById(R.id.add_stock_button);
        mAddStockButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddStockDialog();
                    }
                }
        );
    }


    private void setupSwipeCallback(){
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
        //TODO remove test logic
        if (myIdlingResources != null) {
            myIdlingResources.setIdleState(false);
        }

        if(mPresenter!=null && stockToAdd != null){
            mPresenter.addAStock(stockToAdd);
        }
    }

    @Override
    public void showStockList() {

    }

    private void showAddStockDialog(){
        if(mAddAStockDialog==null) {
            mAddAStockDialog = new AddAStockDialog();
        }
            mAddAStockDialog.show(getSupportFragmentManager(), AddAStockDialog.ADD_STOCK_DIALOG_TAG);
    }

    @Override
    public void forceDataUpdate() {
        if(mPresenter!=null){
            mPresenter.refreshStockData();
        }
    }

    @Override
    public void onStockListLoaded(List<StockDto> stockDtoList) {
        if(stockDtoList == null){
            showListIsEmpty();
            return;
        }

        if(mAdapter!=null){
            mAdapter.setStockList(stockDtoList);
            //TODO delete
            Toast.makeText(this, "newStockList size: " + stockDtoList.size(), Toast.LENGTH_SHORT).show();
        }
        //todo remove test logic
        if(myIdlingResources!=null){
            myIdlingResources.setIdleState(true);
        }
    }

    @Override
    public void showListIsEmpty() {
        //TODO implement
    }


    @VisibleForTesting
    public MyIdlingResources getMyIdlingResource(){
        if(myIdlingResources == null){
            myIdlingResources = MyIdlingResources.getInstance();
        }
        return myIdlingResources;
    }

    @Override
    public void showStockDetails(String ticker) {
        //TODO launch stock detail activity
    }
}
