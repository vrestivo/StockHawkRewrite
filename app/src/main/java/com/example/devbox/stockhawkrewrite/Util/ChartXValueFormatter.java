package com.example.devbox.stockhawkrewrite.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChartXValueFormatter implements IAxisValueFormatter {

    String format = "dd MMM yy";

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date((long)value));
    }

}