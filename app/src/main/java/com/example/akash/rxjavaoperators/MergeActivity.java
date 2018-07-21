package com.example.akash.rxjavaoperators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.akash.rxjavaoperators.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MergeActivity extends AppCompatActivity {

    public static final String TAG= MergeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);


        Observable.merge(getMaleUser(), getFemaleUser())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        Log.v(TAG, user.getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Observable<User> getMaleUser(){
       String[] names= new String[]{"Alex", "Chris", "Nathan"};
        final List<User> userList= new ArrayList<>();
        for(String name: names){
            User user= new User();
            user.setName(name);
            userList.add(user);
        }

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for(User user: userList){
                    if(!emitter.isDisposed()){
                        Thread.sleep(1000);
                        emitter.onNext(user);
                    }
                }
                emitter.onComplete();
            }
        })
        .subscribeOn(Schedulers.io());
    }

    private Observable<User> getFemaleUser(){
        String[] names= new String[]{"Shirley", "Pooja", "Dhinchak"};
        final List<User> userList= new ArrayList<>();
        for(String name: names){
            User user= new User();
            user.setName(name);
            userList.add(user);
        }

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for(User user: userList){
                    if(!emitter.isDisposed()){
                        Thread.sleep(500);
                        emitter.onNext(user);
                    }
                }
                emitter.onComplete();
            }
        })
        .subscribeOn(Schedulers.io());
    }
}
