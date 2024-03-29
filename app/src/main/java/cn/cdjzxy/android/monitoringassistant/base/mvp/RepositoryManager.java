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
package cn.cdjzxy.android.monitoringassistant.base.mvp;

import android.app.Application;
import android.content.Context;

import cn.cdjzxy.android.monitoringassistant.base.integration.cache.Cache;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.CacheType;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;

import me.jessyan.autosize.utils.Preconditions;
import retrofit2.Retrofit;

/**
 * ================================================
 * 用来管理所有业务逻辑的仓库,网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 所有仓库不直接持有却通过 RepositoryManager 拿到需要的请求层做数据处理的好处是
 * 仓库可以直接和对应的请求层解耦,比如网路请求层,需要从 Retrofit 替换为其他网络请求库,这时仓库就不会受到影响
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.3">RepositoryManager wiki 官方文档</a>
 * Created by JessYan on 16/03/2017 14:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Lazy<RxCache> mRxCache;
    @Inject
    Application mApplication;
    @Inject
    Cache.Factory mCachefactory;
    private Cache<String, IModel> mRepositoryCache;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    @Inject
    public RepositoryManager() {
    }

    /**
     * 根据传入的 Class 创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return
     */
    @Override
    public synchronized <T extends IModel> T createRepository(Class<T> repository) {
        if (mRepositoryCache == null)
            mRepositoryCache = mCachefactory.build(CacheType.REPOSITORY_CACHE);
        Preconditions.checkNotNull(mRepositoryCache, "Cannot return null from a Cache.Factory#build(int) method");
        T repositoryInstance = (T) mRepositoryCache.get(repository.getCanonicalName());
        if (repositoryInstance == null) {
            Constructor<? extends IModel> constructor = findConstructorForClass(repository);
            try {
                repositoryInstance = (T) constructor.newInstance(this);
            } catch (InstantiationException e) {
                throw new RuntimeException("Unable to invoke " + constructor, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to invoke " + constructor, e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Create repository error", e);
            }
            mRepositoryCache.put(repository.getCanonicalName(), repositoryInstance);
        }
        return repositoryInstance;
    }


    /**
     * 根据传入的 Class 创建对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public synchronized <T> T createRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null)
            mRetrofitServiceCache = mCachefactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        Preconditions.checkNotNull(mRetrofitServiceCache
                , "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = mRetrofit.get().create(service);
            mRetrofitServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }


    /**
     * 根据传入的 Class 创建对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    @Override
    public synchronized <T> T createCacheService(Class<T> cache) {
        if (mCacheServiceCache == null)
            mCacheServiceCache = mCachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cache.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.get().using(cache);
            mCacheServiceCache.put(cache.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }


    private static Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor;

        String clsName = cls.getName();

        try {
            //noinspection unchecked
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor(IRepositoryManager.class);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + clsName, e);
        }

        return bindingCtor;
    }
}
