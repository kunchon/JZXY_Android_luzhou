package cn.cdjzxy.android.monitoringassistant.utils;

import android.util.Log;

import com.bumptech.glide.util.LogTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;

/**
 * 分瓶信息复制类
 * 2019/6/14 嘉泽——昆虫
 */
public class BottleSplitUtils {
    private static final String TAG = "BottleSplitUtils";

    /**
     * 判断采样单下包含itemId的分瓶信息是否存在
     *
     * @param itemId
     * @param samplingId
     * @return
     */
    public static SamplingFormStand isBottleExists(String itemId, String samplingId) {
        List<SamplingFormStand> formStantdsList = DbHelpUtils.getSamplingFormStandList(samplingId);
        if (!RxDataTool.isEmpty(formStantdsList)) {
            for (SamplingFormStand formStand : formStantdsList) {
                if (formStand.getMonitemIds().contains(itemId)) {
                    return formStand;
                }
            }
        }
        return null;
    }

    /**
     * 删除包含itemId的分瓶信息
     * 19年4月26日 向昆杰更改  添加itemIdSize字段 itemIdSize=0的话
     * 是当前样品采集的itemId 如果一共只出现了一次 就直接删除
     * 如果大于1次 证明其他样品采集也有 可不用删除
     *
     * @param itemId 监测项目Id
     */
    public static void deleteBottleByItemId(String itemId, Sampling mSample) {
        if (itemId == null || itemId.equals("")) return;
        int itemIdSize = 0;//记录当前itemId出现的次数，如果只有一次就删除包含删除包含itemId的分瓶信息
        List<SamplingContent> samplingContents = mSample.getSamplingContentResults();
        if (!RxDataTool.isEmpty(samplingContents)) {
            for (SamplingContent content : samplingContents) {
                String monItemIds = content.getMonitemId();
                if (!RxDataTool.isEmpty(monItemIds) &&
                        !RxDataTool.isEmpty(itemId) &&
                        monItemIds.contains(itemId)) {
                    itemIdSize++;
                    if (itemIdSize > 1) break;
                }
            }
        }
        //只出现了一次
        if (itemIdSize <= 1) {
            SamplingFormStand samplingFormStand = isBottleExists(itemId, mSample.getId());
            if (!RxDataTool.isNull(samplingFormStand)) {
                String[] ids = samplingFormStand.getMonitemIds().split(",");
                List<String> monItemIdList = new ArrayList<>();
                List<String> monItemNameList = new ArrayList<>();
                for (String id : ids) {
                    if (!id.equals(itemId)) {
                        monItemIdList.add(id);
                        String itemName = TagsUtils.getMonItemNameById(id, mSample);
                        if (RxDataTool.isEmpty(itemName)) {
                            monItemNameList.add(" ");
                        } else {
                            monItemNameList.add(itemName);
                        }
                    }
                }
                samplingFormStand.setMonitemIds(RxDataTool.listToStr(monItemIdList));
                samplingFormStand.setMonitemName(RxDataTool.listToStr(monItemNameList));
                if (monItemIdList == null || monItemIdList.size() == 0)
                    DBHelper.get().getSamplingFormStandDao().delete(samplingFormStand);
                else
                    DBHelper.get().getSamplingFormStandDao().update(samplingFormStand);
            }
        }
    }

    /**
     * 统计样品数量
     * 安装样品
     *
     * @return
     */
    public static int setSamplingCount(SamplingContent samplingContent, Sampling sample) {
        List<SamplingFormStand> stanTdList = new ArrayList<>();
        int i = 0;
        if (samplingContent.getMonitemId() != null && !samplingContent.getMonitemId().equals("")) {
            String[] monIds = samplingContent.getMonitemId().split(",");
            for (String id : monIds) {
                List<SamplingFormStand> stanTds = DbHelpUtils.getSamplingStanTdList(sample.getId(), id);
                if (!RxDataTool.isNull(stanTds)) {
                    if (!stanTdList.contains(stanTds)) {
                        stanTdList.addAll(stanTds);
                    }
                } else {
                    Log.e(TAG, "countSamplingCount: " + "异常分瓶信息里面没有找到监测项目");
                    i++;
                }
            }
        }
        i += stanTdList.size();
        return i;
    }


    /**
     * 根据itemId创建或者更新分瓶信息
     *
     * @param itemId
     */
    public static void createAndUpdateBottle(String itemId, Sampling mSample) {
        //获取存在包含该itemId的分瓶信息
        SamplingFormStand samplingFormStand = isBottleExists(itemId, mSample.getId());

        //如果存在包含该itemId的分瓶信息则不用管
        if (RxDataTool.isNull(samplingFormStand)) {
            //获取与itemId同一个分瓶信息
            SamplingFormStand theSameStandBottle = getTheSameStandBottleByItemId(itemId, mSample);
            // 据itemId获取对应的name
            String itemName = TagsUtils.getMonItemNameById(itemId, mSample);
            //如果存在标准的分瓶信息  则跟新否则新增
            if (RxDataTool.isNull(theSameStandBottle)) {
                SamplingFormStand bottleSplit = new SamplingFormStand();
                bottleSplit.setAnalysisSite("");
                bottleSplit.setSaveTimes("");
                bottleSplit.setMonitemIds(itemId);
                bottleSplit.setId(UUID.randomUUID().toString());
                bottleSplit.setMonitemName(itemName);
                List<String> items = new ArrayList<>();
                items.add(itemId);
                bottleSplit.setMonItems(items);
                bottleSplit.setStandNo(generateNewBottleIndex(mSample.getId()));
                bottleSplit.setIndex(generateNewBottleIndex(mSample.getId()));
                bottleSplit.setSamplingId(mSample.getId());
                bottleSplit.setUpdateTime(RxDateTool.getDate());
                bottleSplit.setCount(1);

                //获取包含itemId的标准，如果有则使用标准的信息新增，如果没有则用默认信息新增
                SamplingStantd samplingStantd = getSamplingStantdByMonItem(itemName, mSample.getTagId());
                if (!RxDataTool.isNull(samplingStantd)) {
                    bottleSplit.setContainer(samplingStantd.getContaner());
                    bottleSplit.setSamplingAmount(samplingStantd.getCapacity());
                    bottleSplit.setSaveMehtod(samplingStantd.getSaveDescription());
                    // oldMonitemIds.add(bottleSplit);
                } else {
                    bottleSplit.setContainer("");
                    bottleSplit.setSamplingAmount("");
                    bottleSplit.setSaveMehtod("");
                    // oldMonitemIds.add(bottleSplit);
                }

                DBHelper.get().getSamplingFormStandDao().insertInTx(bottleSplit);
            } else {//存在则更新
                theSameStandBottle.setMonitemIds(theSameStandBottle.getMonitemIds() + "," + itemId);
                theSameStandBottle.setMonitemName(theSameStandBottle.getMonitemName() + "," + itemName);
                DBHelper.get().getSamplingFormStandDao().updateInTx(theSameStandBottle);
            }
        }
    }

    /**
     * monitemName 对应的 SamplingStantd
     *
     * @param monitemName
     * @param tagId
     * @return
     */
    public static SamplingStantd getSamplingStantdByMonItem(String monitemName, String tagId) {
        List<SamplingStantd> stantdsList = DbHelpUtils.getSamplingStantdList(tagId);
        if (!RxDataTool.isEmpty(stantdsList)) {
            for (SamplingStantd samplingStantd : stantdsList) {
                List<String> monItemsList = samplingStantd.getMonItems();
                if (!RxDataTool.isEmpty(monitemName) && !RxDataTool.isEmpty(monItemsList) && monItemsList.contains(monitemName)) {
                    return samplingStantd;
                }
            }
        }
        return null;
    }

    /**
     * 产生新的分瓶信息的index
     *
     * @return
     */
    public static int generateNewBottleIndex(String samplingId) {
        List<SamplingFormStand> formStandsList = DbHelpUtils.getSamplingFormStandList(samplingId);
        if (!RxDataTool.isEmpty(formStandsList)) {
            return formStandsList.get(formStandsList.size() - 1).getIndex() + 1;
        }
        return 1;
    }

    /**
     * 更新分瓶信息的index
     */
    public static void updateAllBottleIndex(String samplingId) {
        List<SamplingFormStand> samplingFormStandList = DbHelpUtils.getSamplingFormStandList(samplingId);
        if (!RxDataTool.isEmpty(samplingFormStandList)) {
            for (int i = 0; i < samplingFormStandList.size(); i++) {
                samplingFormStandList.get(i).setIndex(i + 1);
                samplingFormStandList.get(i).setStandNo(i + 1);
            }
            DBHelper.get().getSamplingFormStandDao().updateInTx(samplingFormStandList);
        }
    }

    /**
     * 更新分瓶信息的index
     */
    public static void updateAllBottleIndex(Sampling sampling) {
        List<SamplingFormStand> samplingFormStandList = sampling.getSamplingFormStandResults();
        if (!RxDataTool.isEmpty(samplingFormStandList)) {
            for (int i = 0; i < samplingFormStandList.size(); i++) {
                samplingFormStandList.get(i).setIndex(i + 1);
                samplingFormStandList.get(i).setStandNo(i + 1);
            }
        }
        sampling.setSamplingFormStandResults(samplingFormStandList);
    }

    /**
     * 获取采样单下面是否和监测项目的itemId一样的标准的分瓶信息
     *
     * @param itemId
     * @param mSample
     * @return
     */
    public static SamplingFormStand getTheSameStandBottleByItemId(String itemId, Sampling mSample) {
        String itemName = TagsUtils.getMonItemNameById(itemId, mSample);
        SamplingStantd samplingStantd = getSamplingStantdByMonItem(itemName, mSample.getTagId());
        //查询所有bottle
        List<SamplingFormStand> formStantdsList = DbHelpUtils.getSamplingFormStandList(mSample.getId());

        if (!RxDataTool.isEmpty(formStantdsList)) {
            for (SamplingFormStand formStand : formStantdsList) {
                String idsStr = formStand.getMonitemIds();
                if (!RxDataTool.isEmpty(idsStr)) {
                    String[] idsArry = idsStr.split(",");
                    if (!RxDataTool.isEmpty(idsArry)) {
                        String firstId = idsArry[0];
                        String monItemName = TagsUtils.getMonItemNameById(firstId, mSample);
                        if (!RxDataTool.isEmpty(monItemName) && !RxDataTool.isNull(samplingStantd) && !RxDataTool.isEmpty(samplingStantd.getMonItems()) && samplingStantd.getMonItems().contains(monItemName)) {
                            return formStand;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取采样单里面的所有样品的MonitIds取并集并去重
     *
     * @param samplingId
     * @return
     */
    public static List<String> getAllJcMonitIds(String samplingId) {
        List<String> ids = new ArrayList<>();
        List<SamplingContent> samplingContentList = DbHelpUtils.getSamplingContentList(samplingId);
        if (!RxDataTool.isEmpty(samplingContentList)) {
            for (SamplingContent samplingContent : samplingContentList) {
                String idsStr = samplingContent.getMonitemId();
                if (!RxDataTool.isEmpty(idsStr)) {
                    String[] idsArray = idsStr.split(",");
                    if (!RxDataTool.isEmpty(idsArray)) {
                        for (String idStr : idsArray) {
                            if (!ids.contains(idStr)) {
                                ids.add(idStr);
                            }
                        }
                    }
                }
            }
        }
        return ids;
    }

    /**
     * 此方法主要是分瓶监测项目更改了  对应的样品采集数量也应该更改
     *
     * @param sample
     * @return
     */
    public static List<SamplingContent> setSamplingCountList(Sampling sample) {
        List<SamplingContent> list = new ArrayList<>();
        if (sample == null && sample.getSamplingContentResults() == null) {
            return list;
        }
        if (sample.getSamplingFormStandResults() == null && sample.getSamplingFormStandResults().size() == 0)
            return list;
        for (SamplingContent content : sample.getSamplingContentResults()) {
            List<SamplingFormStand> stanTdList = new ArrayList<>();
            int i = 0;
            if (content.getMonitemId() != null && !content.getMonitemId().equals("")) {
                String[] monIds = content.getMonitemId().split(",");
                for (String id : monIds) {
                    List<SamplingFormStand> stanTds = DbHelpUtils.getSamplingStanTdList(sample.getId(), id);
                    if (!RxDataTool.isNull(stanTds)) {
                        if (!stanTdList.contains(stanTds)) {
                            stanTdList.addAll(stanTds);
                        }
                    } else {
                        Log.e(TAG, "countSamplingCount: " + "异常分瓶信息里面没有找到监测项目");
                        i++;
                    }
                }
            }

            i += stanTdList.size();
            content.setSamplingCount(i);
            list.add(content);
            generateSamplingDetails(content);
        }
        return list;
    }

    /**
     * 更改样品采集下面监测项目的样品数量
     *
     * @param content
     * @return
     */
    public static void generateSamplingDetails(SamplingContent content) {
        if (RxDataTool.isNull(content)) return;
        List<SamplingDetail> samplingDetailsList = DbHelpUtils.getSamplingDetailList(
                content.getSamplingId(), content.getProjectId(), content.getSampingCode(),
                content.getFrequecyNo(), content.getSamplingType());

        if (!RxDataTool.isEmpty(samplingDetailsList)) {
            for (SamplingDetail detail : samplingDetailsList) {
                detail.setSamplingCount(content.getSamplingCount());
            }
            DBHelper.get().getSamplingDetailDao().updateInTx(samplingDetailsList);
        }
    }
}
