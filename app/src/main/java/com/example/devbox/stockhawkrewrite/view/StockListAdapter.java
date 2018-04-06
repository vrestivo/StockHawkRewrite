package com.example.devbox.stockhawkrewrite.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.devbox.stockhawkrewrite.R;
import com.example.devbox.stockhawkrewrite.model.StockDto;

import java.util.List;

/**
 * ListView Adapter Stub
 */

public class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockListItemViewHolder> {

    private List<StockDto> mStockList;
    private IStockListView.IShowStockDetail mCallback;
    private Context mContext;

    public StockListAdapter(IStockListView.IShowStockDetail callback, Context context) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    public StockListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View viewHolderRoot = LayoutInflater
               .from(parent.getContext())
               .inflate(R.layout.stock_list_item, parent, false);

       return new StockListItemViewHolder(viewHolderRoot);
    }

    @Override
    public void onBindViewHolder(StockListItemViewHolder holder, int position) {
        if(mStockList!=null && position < mStockList.size()){
            holder.mmId = mStockList.get(position).getId();
            holder.mmTicker.setText(mStockList.get(position).getTicker());
            holder.mmPrice.setText(String.valueOf(mStockList.get(position).getRegPrice()));
            float priceChange = (float) mStockList.get(position).getChangePercent();
            if(priceChange<0){
                holder.mmPriceChange.setBackground(mContext.getDrawable(R.drawable.change_red));
            }
            holder.mmPriceChange.setText(String.valueOf(priceChange));
        }
    }

    public void setStockList(List<StockDto> newList){
        mStockList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mStockList!=null){
            return mStockList.size();
        }
        return 0;
    }

    public String getTickerAtPosition(int position){
        if(mStockList!=null && position < mStockList.size()){
           return mStockList.get(position).getTicker();
        }
        return "";
    }

    public class StockListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mmRootView;
        public int mmId;
        public TextView mmTicker;
        public TextView mmPrice;
        public TextView mmPriceChange;

        public StockListItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mmRootView = itemView;
            mmTicker = mmRootView.findViewById(R.id.stock_list_item_ticker);
            mmPrice = mmRootView.findViewById(R.id.stock_list_item_price);
            mmPriceChange = mmRootView.findViewById(R.id.stock_list_item_price_change);
        }

        @Override
        public void onClick(View v) {
            //TODO implement
            if(mCallback!=null){
                Log.v("__stockAdapter", "clicking ticker: " + mmTicker.getText().toString());
                mCallback.showStockDetails(mmTicker.getText().toString());
            }
        }
    }

}
