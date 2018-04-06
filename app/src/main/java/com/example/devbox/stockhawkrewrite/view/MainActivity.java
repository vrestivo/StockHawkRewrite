package com.example.devbox.stockhawkrewrite.view;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.Util.MyIdlingResources;
import com.example.devbox.stockhawkrewrite.model.StockDto;
import com.example.devbox.stockhawkrewrite.presenter.IStockListPresenter;
import com.example.devbox.stockhawkrewrite.presenter.StockListPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IStockListView,
        IStockListView.IShowStockDetail {

    private String LOG_TAG = getClass().getCanonicalName();
    private IStockListPresenter mPresenter;
    private RecyclerView mStockRecyclerView;
    private StockListAdapter mAdapter;
    private FloatingActionButton mAddStockButton;
    private ItemTouchHelper.SimpleCallback mSwipeCallback;
    private MyIdlingResources myIdlingResources;
    private AddAStockDialog mAddAStockDialog;
    private TextView mEmptyListMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyListMessage = findViewById(R.id.stock_empty_list_message);
        mStockRecyclerView = findViewById(R.id.stock_list);
        mAdapter = new StockListAdapter(this, getApplicationContext());
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
        mPresenter.refreshStockData();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.cleanup();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh_stock_data:
                forceDataUpdate();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void displayMessage(String message) {
        showToast(message);
    }

    @Override
    public void addAStock(String stockToAdd){
        //TODO remove test logic
        setIdlingResourcesIdleState(false);

        if(mPresenter!=null && stockToAdd != null){
            mPresenter.addAStock(stockToAdd);
        }
    }


    private void showToast(String message){
        Toast.makeText(getApplicationContext(),
                message,
                Toast.LENGTH_SHORT)
                .show();
    }


    private void showAddStockDialog(){
        if(mAddAStockDialog==null) {
            mAddAStockDialog = new AddAStockDialog();
        }
            mAddAStockDialog.show(getSupportFragmentManager(), AddAStockDialog.ADD_STOCK_DIALOG_TAG);
    }


    @Override
    public void forceDataUpdate() {
        //TODO remove test logic
        setIdlingResourcesIdleState(false);

        if(mPresenter!=null){
            mPresenter.refreshStockData();
        }
    }


    @Override
    public void onStockListLoaded(List<StockDto> stockDtoList) {
        if(stockDtoList == null || stockDtoList.size() < 1){
            showEmptyList();
            return;
        }
        else if(mAdapter!=null){
            showStockList();
            mAdapter.setStockList(stockDtoList);
        }

        //todo remove test logic
        setIdlingResourcesIdleState(true);
    }


    @Override
    public void showEmptyList() {
        mStockRecyclerView.setVisibility(View.GONE);
        mEmptyListMessage.setVisibility(View.VISIBLE);
    }


    @Override
    public void showStockList() {
        mEmptyListMessage.setVisibility(View.GONE);
        mStockRecyclerView.setVisibility(View.VISIBLE);
    }


    @VisibleForTesting
    public MyIdlingResources getMyIdlingResource(){
        if(myIdlingResources == null){
            myIdlingResources = MyIdlingResources.getInstance();
        }
        return myIdlingResources;
    }

    //TODO remove test logic
    private void setIdlingResourcesIdleState(boolean idle){
        if(myIdlingResources!=null){
            myIdlingResources.setIdleState(idle);
        }
    }

    @Override
    public void showStockDetails(String ticker) {
        if(ticker!=null) {
            Intent showStockDetailsIntent = new Intent(this, StockDetailActivity.class);
            showStockDetailsIntent.putExtra(StockDetailActivity.EXTRA_TICKER, ticker);
            startActivity(showStockDetailsIntent);
        }

    }
}
