package cn.cdjzxy.android.monitoringassistant.utils;

import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.repository.RepositoryFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


public class FileDownloaderUtils {
    private static final String TAG = "FileDownloaderUtils";

    /**
     * 启动多个下载
     *
     * @param urls     下载地址
     * @param dirPath  保存路径
     * @param fileName 文件名称
     */
    public static void downLoadFiles(List<String> urls, String dirPath, String fileName) {

        if (RxDataTool.isEmpty(urls)) {
            return;
        }

        FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new FileDownloadLargeFileListener() {
            //等待，已经进入下载队列
            @Override
            protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            //下载进度回调
            @Override
            protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            //暂停下载
            @Override
            protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            //完成整个下载过程
            @Override
            protected void completed(BaseDownloadTask task) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                Log.e(TAG, "error: " + e);
            }

            //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });

        List<BaseDownloadTask> tasks = new ArrayList<>();
        for (String url : urls) {
            tasks.add(FileDownloader.getImpl().create(getFileDownUrl(url)).setPath(getFileSavePath(dirPath, fileName)));
        }
        queueSet.disableCallbackProgressTimes(); // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
        queueSet.setAutoRetryTimes(1);// 所有任务在下载失败的时候都自动重试1次
        queueSet.downloadSequentially(tasks);// 串行执行该任务队列
        queueSet.start();//启动下载
    }

    /**
     * 获取文件下载地址
     *
     * @param url
     * @return 用户登录的返回的url
     */
    private static String getFileDownUrl(String url) {
        UserInfo userInfo = UserInfoHelper.get().getUserInfo();
        if (RxDataTool.isEmpty(userInfo)) {
            return url;
        }
        return userInfo.getWebUrl() + url;
    }

    /**
     * 获取文件保存地址
     *
     * @param dirPath  目录地址
     * @param fileName 文件名
     * @return
     */
    private static String getFileSavePath(String dirPath, String fileName) {
        return dirPath + "/" + fileName;
    }

    /**
     * 启动单任务下载
     *
     * @param url      下载地址
     * @param filePath 保存路径
     * @param fileName 文件名称
     */
    public static void downLoadFile(String url, String filePath, String fileName) {
        FileDownloader.getImpl().create(getFileDownUrl(url))
                .setPath(getFileSavePath(filePath, fileName))
                .setListener(new FileDownloadListener() {
                    //等待，已经进入下载队列
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, String.format("pending==>task:%s 已经下载的字节数:%i  文件总大小:%i", task.toString(), soFarBytes, totalBytes));
                    }

                    //下载进度回调
                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        Log.i(TAG, String.format("pending==>task:%s etag:%s,是否成功断点续传：%b 已经下载的字节数:%i , 文件总大小:%i",
                                task.toString(),etag, soFarBytes, totalBytes));
                    }

                    //暂停下载
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.i(TAG, String.format("pending==>task:%s 已经下载的字节数:%i  文件总大小:%i", task.toString(), soFarBytes, totalBytes));
                    }

                    //在完成前同步调用该方法，此时已经下载完成
                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.i(TAG, String.format("pending==>task:%s ", task.toString()));
                    }

                    //重试之前把将要重试是第几次回调回来
                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    //	完成整个下载过程
                    @Override
                    protected void completed(BaseDownloadTask task) {
                    }

                    //	暂停下载
                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    //	下载出现错误
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.e(TAG, "error: "+e );
                    }

                    //在下载队列中(正在等待/正在下载)已经存在相同下载连接与相同存储路径的任务
                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }
}
