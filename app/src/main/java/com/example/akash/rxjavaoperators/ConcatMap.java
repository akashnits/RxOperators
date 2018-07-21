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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ConcatMap extends AppCompatActivity {

    private Disposable disposable;
    public static final String TAG = ConcatMap.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat_map);

        getUserObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Function<User, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(User user) throws Exception {
                        return getAddressObservable(user);
                    }
                })
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable= d;
                    }

                    @Override
                    public void onNext(User user) {
                        Log.v(TAG, user.getName() + ", " + user.getAddress());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "All users emitted!");
                    }
                });
    }

    private Observable<User> getUserObservable(){
        final List<User> userList= new ArrayList<>();

        userList.add(new User("Akash", "23"));
        userList.add(new User("Rishabh", "12"));
        userList.add(new User("Josh", "23"));
        userList.add(new User("Cart", "12"));
        userList.add(new User("Michael", "23"));
        userList.add(new User("Nobody", "12"));

        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {

                for(User user: userList) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(user);
                    }
                }

                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());
    }


    private Observable<User> getAddressObservable(final User user){

        /*In real scenario, we gonna get this address from network call*/

        user.setAddress("Menlo park");
        return Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {

                if(!emitter.isDisposed()){
                    Thread.sleep(500);
                    emitter.onNext(user);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
