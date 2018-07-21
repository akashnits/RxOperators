package com.example.akash.rxjavaoperators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;


import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class BufferActivity extends AppCompatActivity {

    @BindView(R.id.layout_tap_area)
    Button layoutTapArea;



    private int maxTaps=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        ButterKnife.bind(this);

        final Button layoutTapArea = findViewById(R.id.layout_tap_area);


        final TextView tapResultMaxCount= findViewById(R.id.tap_result_max_count);
        final TextView tapResult= findViewById(R.id.tap_result);

        RxView.clicks(layoutTapArea)
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        return 1;
                    }
                })
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        if(integers.size() > 0){
                            maxTaps= integers.size() > maxTaps ? integers.size(): maxTaps;
                            tapResultMaxCount.setText(String.valueOf(maxTaps));
                            tapResult.setText(String.valueOf(integers.size()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
