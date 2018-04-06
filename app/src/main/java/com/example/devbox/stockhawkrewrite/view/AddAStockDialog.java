package com.example.devbox.stockhawkrewrite.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.devbox.stockhawkrewrite.R;

public class AddAStockDialog extends AppCompatDialogFragment {

    public static String ADD_STOCK_DIALOG_TAG = "ADD_STOCK_DIALOG_TAG";
    private TextInputEditText mEnterStockField;
    private Button mOkButton;
    private Button mCancelButton;
    private MainActivity mMainActivity;


    //empty constructor for fragment manager
    public AddAStockDialog() {

    }


    public static AddAStockDialog newInstance(String title) {
        AddAStockDialog dialog = new AddAStockDialog();
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_stock_dialog, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        mEnterStockField = view.findViewById(R.id.dialog_input_stock_ticker);
        mOkButton = view.findViewById(R.id.dialog_ok_button);
        mCancelButton = view.findViewById(R.id.dialog_cancel_button);
        setOnClickListeners();
        setInputFilters();

        getDialog().setTitle(R.string.dialog_title);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMainActivity = null;
    }

    private void setInputFilters() {
        if (mEnterStockField != null) {
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
            mEnterStockField.setFilters(new InputFilter[]{lengthLimit, capsOnly});
        }
    }

    private void setOnClickListeners(){
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainActivity!=null){
                    mMainActivity.addAStock(mEnterStockField.getText().toString());
                }
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
