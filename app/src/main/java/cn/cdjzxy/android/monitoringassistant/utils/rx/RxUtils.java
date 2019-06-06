/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.cdjzxy.android.monitoringassistant.utils.rx;




import cn.cdjzxy.android.monitoringassistant.base.mvp.BasePresenter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


/**
 * 放置便于使用 RxJava 的一些工具方法
 */
public class RxUtils {

    private RxUtils() {
    }

    /**
     * Rxjava公用操作部分
     *
     * @param basePresenter
     * @param view
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final BasePresenter basePresenter, final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(3, 2))
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) {
                                basePresenter.addDispose(disposable);
                                view.showLoading();//显示进度条
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() {
                                view.hideLoading();//隐藏进度条
                            }
                        });
            }
        };
    }

    /**
     * Rxjava公用操作部分
     *
     * @param basePresenter
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> applySchedulers(final BasePresenter basePresenter) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .retryWhen(new RetryWithDelay(3, 2))
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) {
                                basePresenter.addDispose(disposable);

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() {

                            }
                        });
            }
        };
    }

}