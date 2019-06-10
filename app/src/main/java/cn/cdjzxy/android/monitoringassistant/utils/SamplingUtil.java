package cn.cdjzxy.android.monitoringassistant.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFileDownloadBean;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxFileTool;


/**
 * 2019年5月7日 嘉泽向昆杰   优化代码——进行中
 */
public class SamplingUtil {
    private static final String TAG = "SamplingUtil";
    public static final String INTENT_PROJECT_ID = "projectId";//intent传递  任务id
    public static final String INTENT_SAMPLE_ID = "sampleId";//intent传递  采样单id
    public static final String INTENT_IS_NEW_CREATE = "isNewCreate";//intent 传递 是否新建采样单
    public static final String INTENT_FORM_SELECT_ID = "formSelectId";//intent 传递 是否新建采样单
    /**
     * 降水表单路径/路径
     */
    public static final String PATH_PRECIPITATION = "/FormTemplate/FILL_JS_GAS_XD";
    public static final String NAME_PRECIPITATION = "降水采样及样品交接记录（新都）";

    /**
     * 废水表单路径/路径
     */
    public static final String PATH_WASTE_WATER = "/FormTemplate/FILL_WATER_NEW_XD";
    public static final String NAME_WASTE_WATER = "水和废水样品采集与交接记录（新都）";

    /**
     * 仪器法表单路径/路径
     */
    public static final String PATH_INSTRUMENTAL = "/FormTemplate/FILL_YQF_WATER";
    public static final String NAME_INSTRUMENTAL = "现场监测仪器法";

    /**
     * 工业企业厂界噪声监测记录
     */
    public static final String PATH_NOISE_FACTORY = "/FormTemplate/FILL_GYZS_VOICE_XD";
    public static final String NAME_NOISE_FACTORY = "工业企业厂界噪声监测记录";

    public static final int SAMPLING_MESSAGE = 1001;//采样点消息
    public static final int NET_RESPONSE_CODE_259 = 259;
    public static final int NET_RESPONSE_SAMPLING_DIFFER = -100;//采样单和服务器上不一致
    public static final int VIEW_UPDATE_LOADING_TXT = 10;//更新加载框

    public static final String SAMPLING_TYPE_PT = "普通样";
    public static final String SAMPLING_TYPE_PX = "平行样";
    public static final String SAMPLING_TYPE_KB = "全程序空白";

    public static final int DATA_PAGE_SIZE = 800;

    /**
     * 判断当前表单是否是本人的
     *
     * @return@true本人表单，@false他人表单
     */
    public static boolean isMySampling(String submitId) {
        UserInfo user = UserInfoHelper.get().getUserInfo();
        return user.getId().equals(submitId);
    }

    /**
     * 判断当前表单 采样人员是否有该用户
     *
     * @param sampleUserId 采样人员
     * @param userId       该用户
     * @return@true本人表单，@false他人表单
     */
    public static boolean sampleContainsUser(String sampleUserId, String userId) {
        if (RxDataTool.isEmpty(sampleUserId)) return false;
        if (RxDataTool.isEmpty(userId)) return false;
        return sampleUserId.contains(userId);
    }

    /**
     * 判断当前表单 采样人员是否有该用户
     *
     * @param sampleUserId 采样人员
     * @return@true本人表单，@false他人表单
     */
    public static boolean sampleContainsUser(String sampleUserId) {
        if (RxDataTool.isEmpty(sampleUserId)) return false;
        UserInfo user = UserInfoHelper.get().getUserInfo();
        return sampleContainsUser(sampleUserId, user.getId());
    }

    /**
     * 判断表单是否提交
     *
     * @param status {@sampling.getStatus:0等待提交，1等待收样审核，2等待室主任审核，}
     *               {@3等待质控审核，4打回重新提交，5作废，6通过审核，7等待检验审核，8协助人员审核，9撤回待提交，10app上传服务器保存}
     * @return true 提交 false 未提交
     */
    public static boolean samplingIsSubmit(int status) {
        return status != 0 && status != 4 && status != 9;
    }

    /**
     * 判断当前表单能否编辑
     * 通过采样单状态 和采样人员是否包含是否有本人
     *
     * @param sampling
     * @return true 可以编辑 false  不能编辑
     */
    public static boolean setSampeIsCanEdit(Sampling sampling) {
        return (!samplingIsSubmit(sampling.getStatus()))
                && sampleContainsUser(sampling.getSamplingUserId());
    }

    /**
     * 创建基础的采样单
     *
     * @param projectId    任务id
     * @param formSelectId 分类id
     * @return 包含基础信息的采样单
     */
    public static Sampling createSample(String projectId, String formSelectId) {
        if (RxDataTool.isEmpty(projectId) || RxDataTool.isEmpty(formSelectId)) {
            throw new NullPointerException(String.format("创建采样单，任务id：%s,分类id:%s"));
        }
        Project project = DbHelpUtils.getDbProject(projectId);
        FormSelect formSelect = DbHelpUtils.getDbFormSelect(formSelectId);
        Sampling sampling = new Sampling();
        sampling.setId(UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setTagName(TagsUtils.getTagsName(RxDataTool.strToList(formSelect.getTagId())));//这里可能有很多tagName
        sampling.setMontype(project.getTypeCode());
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(formSelect.getFormName());
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        //        sampling.setFormFlows(formSelect.getFormFlows().toString());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatusName("进行中");
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(RxDateTool.getCurTimeString());
        sampling.setSamplingDetailResults(new ArrayList<>());
        sampling.setSamplingFiless(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        sampling.setLayTableCheckbox("on");
        return sampling;
    }

    /**
     * 创建采样单:水和废水
     *
     * @return
     */
    public static Sampling createWaterSample(String projectId, String formSelectId) {
        Sampling sampling = createSample(projectId, formSelectId);
        return sampling;
    }

    /**
     * 创建仪器法单子
     *
     * @param projectId
     * @param formSelectId
     * @return
     */
    public static Sampling createInstrumentalSampling(String projectId, String formSelectId) {
        Sampling sampling = createSample(projectId, formSelectId);
        HashMap<String, String> privateData = new HashMap<>();
        privateData.put("CaleValue", "");
        privateData.put("RPDValue", "");
        privateData.put("SamplingOnTime", "");
        privateData.put("HasPX", "false");
        privateData.put("FormTypeName", "");
        privateData.put("SourceWay", "");
        privateData.put("SourceDate", "");
        privateData.put("DeviceText", "");
        sampling.setPrivateData(com.alibaba.fastjson.JSONObject.toJSONString(privateData));
        return sampling;
    }

    /**
     * 创建采样单：噪声
     *
     * @param projectId
     * @param formSelectId
     * @return
     */
    public static Sampling createNoiseSample(String projectId, String formSelectId) {
        Sampling sampling = createSample(projectId, formSelectId);
        sampling.setMonitemName("工业企业厂界噪声");
        MonItems monItems = DbHelpUtils.getMonItems("工业企业厂界噪声");
        sampling.setMonitemId(RxDataTool.isNull(monItems) ? "" : monItems.getId());//为了检测方法
        return sampling;

    }

    /**
     * 创建采样单编号:年月日+账号+流水号
     *
     * @return
     */
    public static String createSamplingNo() {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = RxDateTool.getCurTimeString().replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
        samplingNo.append(UserInfoHelper.get().getUser().getIntId()).append("-");
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();

        if (RxDataTool.isEmpty(samplings)) {
            samplingNo.append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!RxDataTool.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append(StringUtil.autoGenericCode(1, 2));
            }
        }
        return samplingNo.toString();
    }

    /**
     * 创建采样单编号:年月日-账号-流水号
     *
     * @return
     */
    public static String createSamplingNo(String data) {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = data.replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();

        if (RxDataTool.isEmpty(samplings)) {
            samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!RxDataTool.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append("-").append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
            }
        }
        return samplingNo.toString();
    }

    /**
     * 样品编码规则 样品性质年月日——采样单流水号账号——采样号
     * 样品性质：
     * 1.水质
     * 地下水、海水、废水、地表水     ===》  DXS/HS/FS/DBS
     * 2.空气
     * 无组织废气、环境空气、室内空气  ===》  WF/HK/SK
     * 3.废气
     * 有组织废气                  ===》  YF
     * 4.降水
     * 降水                       ===》  JS
     * <p>
     * <p>
     * 注这里只返回：年月日——采样单流水号账号
     *
     * @return 年月日——采样单流水号账号
     */
    public static String createSamplingFrequecyNo() {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = RxDateTool.getCurTimeString().replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();
        if (RxDataTool.isEmpty(samplings)) {
            samplingNo.append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!RxDataTool.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append(StringUtil.autoGenericCode(1, 2));
            }
        }
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
        return samplingNo.toString();
    }

    /**
     * 样品编码规则 样品性质年月日——采样单流水号账号——采样号
     * 样品性质：
     * 1.水质
     * 地下水、海水、废水、地表水     ===》  DXS/HS/FS/DBS
     * 2.空气
     * 无组织废气、环境空气、室内空气  ===》  WF/HK/SK
     * 3.废气
     * 有组织废气                  ===》  YF
     * 4.降水
     * 降水                       ===》  JS
     * <p>
     * <p>
     * 注这里只返回：年月日——采样单流水号账号
     *
     * @return 年月日——采样单流水号账号
     */
    public static String createSamplingFrequecyNo(String data) {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = data.replace("-", "").substring(2);
        samplingNo.append(dateStr);
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();
        if (RxDataTool.isEmpty(samplings)) {
            samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!RxDataTool.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2));
                samplingNo.append("-").append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
            }
        }
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
        return samplingNo.toString();
    }

    /**
     * 判断当前表单能否编辑
     * 通过采样单状态
     * {@sampling.getStatus:0等待提交，1等待收样审核，2等待室主任审核，
     * 3等待质控审核，4打回重新提交，5作废，6通过审核，7等待检验审核，8协助人员审核，9撤回待提交，10app上传服务器保存
     * }
     * 和采样是否有本人有关
     *
     * @param sampling
     * @return
     */
    public static boolean setSampleIsCanEdit(Sampling sampling) {
        return (sampling.getStatus() == 0 ||
                sampling.getStatus() == 4 ||
                sampling.getStatus() == 9
        )
                && sampling.getSamplingUserId().
                contains(UserInfoHelper.get().getUserInfo().getId());
    }

    /**
     * 判断采样单是否完成
     *
     * @param sampling
     * @return
     */
    public static boolean isSamplingFinsh(Sampling sampling) {
        switch (sampling.getFormPath()) {
            case PATH_PRECIPITATION://降水
                return isPrecipitationSamplingFinish(sampling);
            case PATH_WASTE_WATER://水和废水
                return isWasterSamplingFinish(sampling);
            case PATH_INSTRUMENTAL://仪器法
                return isInstrumentSamplingFinish(sampling) == "";
            case PATH_NOISE_FACTORY://噪声
                return isNoiseSamplingFinsh(sampling);
            default://还没有开发
                return false;

        }
    }

    /**
     * 判断噪声表是否采样完成
     *
     * @param sampling
     * @return
     */
    public static boolean isNoiseSamplingFinsh(Sampling sampling) {
        if (RxDataTool.isEmpty(sampling.getSamplingTimeBegin())) {//检测日期必填项
            return false;
        }
        if (RxDataTool.isEmpty(sampling.getPrivateData()))//采样检测信息
            return false;
        NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(),
                NoisePrivateData.class);
        return !RxDataTool.isEmpty(privateData.getMianNioseAddr());
    }

    /**
     * 降水采样单采样是否完成
     *
     * @return
     */
    public static boolean isPrecipitationSamplingFinish(Sampling mSampling) {
        if (RxDataTool.isEmpty(mSampling.getSamplingDetailResults())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSampling.getTagName())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSampling.getAddressName())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSampling.getPrivateData())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSampling.getMethodName())) {
            return false;
        }
        return !RxDataTool.isEmpty(mSampling.getDeviceName());
    }

    /**
     * 采样是否完成
     * 仪器法表
     *
     * @return
     */
    public static String isInstrumentSamplingFinish(Sampling sampling) {
        if (RxDataTool.isEmpty(sampling.getSamplingUserName())) {
            return "请选择“监测人”";
        }

        if (RxDataTool.isEmpty(sampling.getSamplingTimeBegin())) {
            return "请选择“分析开始日期”";
        }
        if (RxDataTool.isEmpty(sampling.getSamplingTimeEnd())) {
            return "请选择“分析结束日期”";
        }

        if (RxDataTool.isEmpty(sampling.getMonitemId())) {
            return "请选择“监测项目”";
        }

        if (RxDataTool.isEmpty(sampling.getMethodName())) {
            return "请选择“监测方法”";
        }

        if (RxDataTool.isEmpty(sampling.getDeviceName())) {
            return "请选择“监测设备”";
        }

        if (RxDataTool.isEmpty(sampling.getSamplingDetailYQFs())) {
            return "请添加监测结果";
        }

        for (SamplingDetail detail : sampling.getSamplingDetailYQFs()) {
            if (RxDataTool.isEmpty(detail.getPrivateDataStringValue("SamplingOnTime"))) {
                return String.format("监测结果[%s]需选择分析时间", detail.getSampingCode());//没有填写分析时间
            }
            if (RxDataTool.isEmpty(detail.getPrivateDataStringValue("CaleValue"))) {
                return String.format("监测结果[%s]需填写分析结果", detail.getSampingCode());//没有填写分析结果
            }
            if (RxDataTool.isEmpty(detail.getPrivateDataStringValue("ValueUnit"))) {
                return String.format("监测结果[%s]需填写结果单位", detail.getSampingCode());//没有填写结果单位
            }
        }

        return "";
    }

    /**
     * 判断水和废水采样单是否完成
     *
     * @param mSample
     * @return
     */
    public static boolean isWasterSamplingFinish(Sampling mSample) {
        if (RxDataTool.isEmpty(mSample.getSamplingDetailResults())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSample.getSamplingUserId())) {
            return false;
        }
        if (RxDataTool.isEmpty(mSample.getTagId())) {
            return false;
        }
        return !RxDataTool.isEmpty(mSample.getAddressId());
    }

    /**
     * 设置采样单的MonitemName
     *
     * @param mSampling
     * @return
     */
    public static String setPrecipiationMonitemName(Project mProject, Sampling mSampling) {
        if (mSampling.getAddressId() == null || mSampling.getAddressId().equals("")) return "";
        if (mSampling.getTagId() == null || mSampling.getTagId().equals("")) return "";
        List<ProjectDetial> projectDetialList = DbHelpUtils.getProjectDetialList(mProject.getId()
                , mSampling.getAddressId(), mSampling.getTagId());
        if (RxDataTool.isEmpty(projectDetialList)) {
            return "";
        }
        StringBuilder stringBuilderName = new StringBuilder();


        for (ProjectDetial content : projectDetialList) {
            stringBuilderName.append(content.getMonItemName()).append(",");
        }
        if (stringBuilderName.lastIndexOf(",") > 0) {
            stringBuilderName.deleteCharAt(stringBuilderName.lastIndexOf(","));
        }
        return stringBuilderName.toString();
    }

    /**
     * 设置采样单的MonitemId
     *
     * @param mSampling
     * @return
     */
    public static String setPrecipiationMonitemId(Project mProject, Sampling mSampling) {
        if (mSampling.getAddressId() == null || mSampling.getAddressId().equals("")) return "";
        if (mSampling.getTagId() == null || mSampling.getTagId().equals("")) return "";
        List<ProjectDetial> projectDetialList = DbHelpUtils.getProjectDetialList(mProject.getId()
                , mSampling.getAddressId(), mSampling.getTagId());
        if (RxDataTool.isEmpty(projectDetialList)) {
            return "";
        }
        StringBuilder stringBuilderId = new StringBuilder();

        for (ProjectDetial content : projectDetialList) {
            stringBuilderId.append(content.getMonItemId()).append(",");
        }
        if (stringBuilderId.lastIndexOf(",") > 0)
            stringBuilderId.deleteCharAt(stringBuilderId.lastIndexOf(","));
        return stringBuilderId.toString();
    }

    /**
     * 批量保存采样单
     *
     * @param samplingList
     */
    public static void saveSample(List<Sampling> samplingList) {
        new Thread() {
            @Override
            public void run() {
                for (Sampling sampling : samplingList) {
                    saveSample(sampling);
                }
            }
        }.start();
    }

    /**
     * 保存采样单
     *
     * @param sampling
     */
    public static void saveSample(Sampling sampling) {
        String formName = sampling.getFormName();
        if (!RxDataTool.isNull(formName) && formName.equals(NAME_PRECIPITATION)) {
            sampling.setFormPath(PATH_PRECIPITATION);
        } else if (!RxDataTool.isNull(formName) && formName.equals(NAME_WASTE_WATER)) {
            sampling.setFormPath(PATH_WASTE_WATER);
        } else if (!RxDataTool.isNull(formName) && formName.equals(NAME_INSTRUMENTAL)) {
            sampling.setFormPath(PATH_INSTRUMENTAL);
        } else if (!RxDataTool.isNull(formName) && formName.equals(NAME_NOISE_FACTORY)) {
            sampling.setFormPath(PATH_NOISE_FACTORY);
        }
        //处理有相同SamplingNo不同id的情况
        List<Sampling> localSamplings = DbHelpUtils.getDbSampsByNo(sampling.getSamplingNo());
        if (!RxDataTool.isEmpty(localSamplings)) {
            for (Sampling s : localSamplings) {
                if (s.getId().equals(sampling.getId())) {
                    DBHelper.get().getSamplingDao().update(sampling);
                } else {
                    DBHelper.get().getSamplingDao().delete(s);
                }
            }
        }
        //删除采样单对应的SamplingDetail
        List<SamplingDetail> samplingDetailList = DbHelpUtils.getSamplingDetailList(sampling.getId());
        if (!RxDataTool.isEmpty(samplingDetailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
        }
        //插入服务器返回的SamplingDetail
        List<SamplingDetail> samplingDetails = sampling.getSamplingDetailResults();
        if (!RxDataTool.isEmpty(samplingDetails)) {
            DBHelper.get().getSamplingDetailDao().insertInTx(samplingDetails);
        }

        //删除采样单对应的SamplingContent
        List<SamplingContent> dbContentList = DbHelpUtils.
                getSamplingContent(sampling.getId());
        if (!RxDataTool.isEmpty(dbContentList)) {
            DBHelper.get().getSamplingContentDao().deleteInTx(dbContentList);
        }
        //插入服务器返回的SamplingContent
        List<SamplingContent> samplingContents = sampling.getSamplingContentResults();
        if (!RxDataTool.isEmpty(samplingContents)) {
            for (SamplingContent content : samplingContents) {
                if (RxDataTool.isEmpty(content.getId())) {
                    content.setId(UUID.randomUUID().toString());
                }
                DBHelper.get().getSamplingContentDao().insert(content);
            }

        }
        //文件
        List<SamplingFile> samplingFileList = sampling.getHasFile() == null ?
                new ArrayList<>() : sampling.getHasFile();
        if (samplingFileList != null && samplingFileList.size() > 0) {
            List<String> fileIdS = new ArrayList<>();
            for (SamplingFile samplingFile : samplingFileList) {
                fileIdS.add(samplingFile.getId());
                //从数据库查询对应的文件，Id由于是服务端提供的，所以不会变，但是SamplingId可能会变化，所以这里做一次更新
                SamplingFile dbFile = DbHelpUtils.getSamplingFile(samplingFile.getId());
                //数据库不存在  或者文件不存在需要去下载
                if (RxDataTool.isEmpty(dbFile) || !RxFileTool.isFileExists(dbFile.getFilePath())) {
                    downloadSamplingFile(samplingFile);
                }
            }
            sampling.setFileIds(new Gson().toJson(fileIdS));
        }
        switch (sampling.getFormPath()) {
            case PATH_NOISE_FACTORY: //噪声表_工厂企业噪声
                if (sampling.getPrivateData() != null) {
                    NoisePrivateData privateData = new Gson().
                            fromJson(sampling.getPrivateData(), NoisePrivateData.class);
                    if (privateData.getImageSYT() != null && !privateData.getImageSYT().equals("")) {
                        //todo 下载噪声表文件
                        downloadSamplingFile(sampling);
                    }
                }
                break;
            case PATH_WASTE_WATER://仪器法
                break;
            case PATH_INSTRUMENTAL://水和废水
                //删除本地对应的分瓶信息
                List<SamplingFormStand> formStantdsList = DbHelpUtils.getSamplingFormStandList(sampling.getId());
                if (!RxDataTool.isEmpty(formStantdsList))
                    DBHelper.get().getSamplingFormStandDao().deleteInTx(formStantdsList);
                //新增分瓶信息
                List<SamplingFormStand> samplingFormStands = sampling.getSamplingFormStandResults();
                if (!RxDataTool.isEmpty(samplingFormStands)) {
                    DBHelper.get().getSamplingFormStandDao().insertInTx(samplingFormStands);
                }
                break;
            case PATH_PRECIPITATION://降水
                break;
        }
        sampling.setIsCanEdit(SamplingUtil.setSampleIsCanEdit(sampling));
        sampling.setIsLocal(false);
        sampling.setIsFinish(SamplingUtil.isSamplingFinsh(sampling));
        if (DbHelpUtils.getDbSampling(sampling.getId()) != null) {
            DBHelper.get().getSamplingDao().update(sampling);
        } else {
            DBHelper.get().getSamplingDao().insert(sampling);
        }
    }

    /**
     * 下载采样单文件
     *
     * @param samplingFile 采样文件
     */
    public static void downloadSamplingFile(SamplingFile samplingFile) {
        Log.i(TAG, samplingFile.toString());
        new DownloadSamplingFileAsyncTask().execute(samplingFile);
    }

    static class DownloadSamplingFileAsyncTask extends AsyncTask<SamplingFile, Void, Void> {
        @Override
        protected Void doInBackground(SamplingFile... samplingFiles) {
            SamplingFile samplingFile = samplingFiles[0];
            if (RxDataTool.isEmpty(samplingFile)) return null;
            UserInfo userInfo = UserInfoHelper.get().getUserInfo();
            String url = userInfo.getWebUrl() + samplingFile.getFilePath();
            String savePath = RxFileTool.getAppPublicPath(userInfo.getName());
            String filePath = RxFileTool.downloadFile(url, samplingFile.getFileName(), savePath);
            if (RxDataTool.isEmpty(filePath)) {//下载失败
                return null;
            }
            samplingFile.setFilePath(filePath);
            samplingFile.setIsUploaded(true);
            DBHelper.get().getSamplingFileDao().insert(samplingFile);
            return null;
        }
    }

    /**
     * 下载采样单补充字段的文件
     *
     * @param sampling 采样文件
     */
    public static void downloadSamplingFile(Sampling sampling) {
        new DownloadSamplingAsyncTask().execute(sampling);
    }

    static class DownloadSamplingAsyncTask extends AsyncTask<Sampling, Void, Void> {
        @Override
        protected Void doInBackground(Sampling... samplings) {
            Sampling sampling = samplings[0];
            if (RxDataTool.isEmpty(sampling)) return null;
            UserInfo userInfo = UserInfoHelper.get().getUserInfo();
            switch (sampling.getFormPath()) {
                case PATH_NOISE_FACTORY:
                    NoisePrivateData privateData = new Gson().
                            fromJson(sampling.getPrivateData(), NoisePrivateData.class);
                    if (RxDataTool.isEmpty(privateData) || RxDataTool.isEmpty(privateData.getImageSYT())) {
                        return null;
                    } else {
                        String url = userInfo.getWebUrl() + privateData.getImageSYT();
                        String savePath = RxFileTool.getAppPublicPath(userInfo.getName());
                        String filePath = RxFileTool.downloadFile(url, savePath);
                        if (RxDataTool.isEmpty(filePath)) {//下载失败
                            return null;
                        }
                        privateData.setImageSYT(filePath);
                        sampling.setPrivateData(new Gson().toJson(privateData));
                        if (RxDataTool.isEmpty(DbHelpUtils.getDbSampling(sampling.getId()))) {
                            DBHelper.get().getSamplingDao().update(sampling);
                        } else {
                            DBHelper.get().getSamplingDao().insert(sampling);
                        }
                    }
                    break;
            }
            return null;
        }
    }

    /*********************************采样单上传*****************************************/

    public static void unloadSample(Sampling sampling) {

    }
}
