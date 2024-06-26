package com.example.weightlosscontrolandroid;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class RenderChart {
    private List<Double> Weight;
    private Context context;
    private BarChart barChart;

    public RenderChart(Context context, List<Double> Weight, BarChart barChart) {
        this.context = context;
        this.Weight = Weight;
        this.barChart = barChart;
    }

    public void renderBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < Weight.size(); i++) {
            entries.add(new BarEntry(i, Weight.get(i).floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Weight change chart");
        int greenColor = ContextCompat.getColor(context, R.color.Green);
        int whiteColor = ContextCompat.getColor(context, R.color.white);
        dataSet.setColor(greenColor);
        dataSet.setValueTextColor(whiteColor);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(whiteColor);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTextColor(whiteColor);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTextColor(whiteColor);
        rightAxis.setAxisMinimum(0f);

        Legend legend = barChart.getLegend();
        legend.setTextColor(whiteColor);

        barChart.getDescription().setEnabled(false);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();
    }
}
