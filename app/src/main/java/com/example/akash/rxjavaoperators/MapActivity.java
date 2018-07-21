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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*RxJava operator - Map()
* This transforms each item and emits modified item
* Here, we gonna modify user item to contain email address
*/

public class MapActivity extends AppCompatActivity {

    public static final String TAG = MapActivity.class.getSimpleName();


    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getUserObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<User, User>() {
                    @Override
                    public User apply(User user) throws Exception {

                        user.setEmail(String.format("%s@rxjava.com", user.getName()));
                        return user;
                    }
                }).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable= d;
            }

            @Override
            public void onNext(User user) {
                Log.v(TAG, user.getEmail());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private Observable<User> getUserObservable(){
        final List<User> userList= new ArrayList<>();

        userList.add(new User("Akash", "23"));
        userList.add(new User("Rishabh", "12"));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
