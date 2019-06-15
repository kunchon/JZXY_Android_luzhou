package cn.cdjzxy.android.monitoringassistant.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

import static android.content.ContentValues.TAG;


/**
 * 数据库工具类
 * 2019年5月7日 嘉泽向昆杰   优化代码——进行中
 */
public class DbHelpUtils {
    /**
     * 初始化数据库
     *
     * @param context Context
     */
    public static void initDb(Context context) {
        DBHelper.init(context, UserInfoHelper.get().getUserName());//初始化创建数据库
    }


    /**
     * 通过{@ProjectDao.Properties.Id}查找Project表
     * Project 任务表
     *
     * @param id 主键ID
     * @return Project任务表
     */
    public static Project getDbProject(String id) {
        if (RxDataTool.isEmpty(id)) return new Project();
        return DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(id)).unique();
    }


    /**
     * 通过{@SamplingDao.Properties.Id}查找Sampling表
     * Sampling 采样表
     *
     * @param id 主键id
     * @return Sampling 采样表
     */
    public static Sampling getDbSampling(String id) {
        if (RxDataTool.isEmpty(id)) {
            Log.e(TAG, "getDbSampling:数据库查找采样单id为空");
            return new Sampling();
        }
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 通过{@SamplingDao.Properties.Id}查找Sampling表
     * Sampling 采样表
     *
     * @param projectId 项目id
     * @return Sampling 采样表
     */
    public static List<Sampling> getDbSampling(String projectId, String path) {
        if (RxDataTool.isEmpty(projectId)) return new ArrayList<>();
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.FormPath.eq(path)).
                orderDesc(SamplingDao.Properties.SamplingNo).list();
    }

    /**
     * 查找是否存在相同采样单id的采样单
     * 通过{@SamplingDao.Properties.SamplingNo}查找Sampling表
     * Sampling 采样表
     *
     * @param samplingNo 采样单编号
     * @return Sampling 采样表
     */
    public static List<Sampling> getDbSampsByNo(String samplingNo) {
        if (RxDataTool.isEmpty(samplingNo)) return new ArrayList<>();
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.eq(samplingNo)).list();
    }

    /**
     * 根据tagId查找要素采样单
     * 通过{@SamplingDao.Properties.ProjectId，ParentTagId}查找Sampling表
     * Sampling 采样表
     *
     * @param projectId 任务id
     * @param tagId     tagId
     * @return List<Sampling> 采样表
     */
    public static List<Sampling> getDbSamplesByTagId(String projectId, String tagId) {
        if (RxDataTool.isEmpty(projectId) || RxDataTool.isEmpty(tagId)) return new ArrayList<>();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.ParentTagId.eq(tagId)).
                orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (RxDataTool.isEmpty(samplings)) return new ArrayList<>();
        else return samplings;
    }

    /**
     * 通过{@MonItemsDao.Properties.Name}查找MonItems表
     * MonItems 监测项目表
     *
     * @param name 监测项目名称
     * @return MonItems 监测项目表
     */
    public static MonItems getMonItems(String name) {
        if (RxDataTool.isEmpty(name)) return new MonItems();
        try {
            return DBHelper.get().getMonItemsDao().queryBuilder().
                    where(MonItemsDao.Properties.Name.eq(name)).unique();
        } catch (Exception e) {
            List<MonItems> list = DBHelper.get().getMonItemsDao().queryBuilder().
                    where(MonItemsDao.Properties.Name.eq(name)).list();
            Log.e(TAG, "getMonItems: " +
                    String.format("监测项目表数据异常：名称:%s, 数量：%d", name, list.size()));
            return list.get(0);
        }
    }

    /**
     * 通过Id查找 SamplingContent
     *
     * @param id 主键id
     * @return SamplingContent
     */
    public static SamplingContent getSamplingContent(String id) {
        return DBHelper.get().getSamplingContentDao().queryBuilder().where(
                SamplingContentDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 查找平行样
     *
     * @param projectId
     * @param samplingId
     * @param frequecyNo
     * @return
     */
    public static SamplingContent getSamplingContent(String projectId, String samplingId,
                                                     int frequecyNo) {
        return DBHelper.get().getSamplingContentDao().
                queryBuilder().where(SamplingContentDao.Properties.ProjectId.
                eq(projectId), SamplingContentDao.Properties.SamplingId.
                eq(samplingId), SamplingContentDao.Properties.FrequecyNo.
                eq(frequecyNo), SamplingContentDao.Properties.SamplingType
                .eq(1)).unique();
    }

    /**
     * 通过{@SamplingContentDao{@ProjectId,SamplingId,SampingCode,SamplingType }}查找SamplingContent表
     * SamplingContent 采样单数据表
     *
     * @param projectId    任务id
     * @param samplingId   采样单id
     * @param samplingCode 样品编码
     * @param samplingType 样品类型
     * @return SamplingContent 采样单数据表
     */
    public static List<SamplingContent> getSamplingContentList(String projectId, String samplingId,
                                                               String samplingCode, int samplingType) {
        List<SamplingContent> contentList = DBHelper.get().
                getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.ProjectId.eq(projectId),
                        SamplingContentDao.Properties.SamplingId.eq(samplingId),
                        SamplingContentDao.Properties.SampingCode.eq(samplingCode),
                        SamplingContentDao.Properties.SamplingType.eq(samplingType)).list();
        return contentList;
    }

    /**
     * 通过{@SamplingContentDao{@ProjectId,SamplingId }}查找SamplingContent表
     * SamplingContent 采样单数据表
     *
     * @param projectId  任务id
     * @param samplingId 采样单id
     * @return SamplingContent 采样单数据表
     */
    public static List<SamplingContent> getSamplingContentList(String projectId, String samplingId) {
        List<SamplingContent> contentList = DBHelper.get().
                getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.ProjectId.eq(projectId),
                        SamplingContentDao.Properties.SamplingId.eq(samplingId)).list();
        return contentList;
    }

    /**
     * 通过{@SamplingContentDao{SamplingId }}查找SamplingContent表
     * SamplingContent 采样单数据表
     *
     * @param samplingId 采样单id
     * @return SamplingContent 采样单数据表
     */
    public static List<SamplingContent> getSamplingContentList(String samplingId) {
        List<SamplingContent> contentList = DBHelper.get().
                getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.SamplingId.eq(samplingId)).list();
        return contentList;
    }

    /**
     * 通过{@SamplingFormStandDao.Properties.Id}查找SamplingFormStand表
     * SamplingFormStand 分瓶信息表
     *
     * @param id 主键id
     * @return MonItems 分瓶信息表
     */
    public static SamplingFormStand getSamplingFormStandDaoForId(String id) {
        if (id == null || id.equals("")) return null;
        return DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.
                Properties.Id.eq(id)).
                unique();
    }

    /**
     * 按照{@SamplingFormStandDao.Properties.SamplingId}查找数据库所有的SamplingFormStandDao表
     * SamplingFormStand 分瓶信息表
     *
     * @param SamplingId 主键id
     * @return List<SamplingFormStand>：查询所有的分瓶信息表
     */
    public static List<SamplingFormStand> getSamplingFormStandList(String SamplingId) {
        if (RxDataTool.isEmpty(SamplingId)) return new ArrayList<>();
        List<SamplingFormStand> list = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                where(SamplingFormStandDao.
                        Properties.SamplingId.eq(SamplingId)).list();
        if (list == null) return new ArrayList<>();
        else return list;
    }

    /**
     * 按照{@SamplingFormStandDao.Properties{@SamplingId,MonitemIds}}查找数据库所有的SamplingFormStandDao表
     * SamplingFormStand 分瓶信息表
     *
     * @param samplingId 采样单id
     * @param itemId     监测项目id
     * @return List<SamplingFormStand>：查询所有的分瓶信息表
     */
    public static List<SamplingFormStand> getSamplingFormStanTdList(String samplingId, String itemId) {
        List<SamplingFormStand>
                stanTdList = DBHelper.get().getSamplingFormStandDao().queryBuilder()
                .where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId),
                        SamplingFormStandDao.Properties.MonitemIds.like(itemId + "%")).list();
        if (stanTdList == null) return new ArrayList<>();
        else return stanTdList;
    }

    /**
     * 查询数据库所有的任务表
     *
     * @return List<Project>：查询所有的任务表
     */
    public static List<Project> getProjectList() {
        return DBHelper.get().getProjectDao().loadAll();
    }

    /**
     * 按照{@ProjectDao.Properties.PlanEndTime}降序查找数据库所有的任务表
     *
     * @return List<Project>：查询所有的任务表
     */
    public static List<Project> getProjectListDesc() {
        return DBHelper.get().getProjectDao().queryBuilder().orderDesc(ProjectDao.Properties.PlanEndTime).list();
    }

    /**
     * 查找数据库所有的任务表的数量
     *
     * @return long 任务表数量
     */
    public static long getProJectSize() {
        return DBHelper.get().getProjectDao().count();
    }


    /**
     * 按照{@ProjectContentDao.Properties.ProjectId}查找数据库所有的ProjectDetialDao表
     * ProjectContent 任务数据表
     *
     * @param projectId 任务id
     * @return List<ProjectContent>：查询所有的任务数据表
     */
    public static List<ProjectContent> getProjectContentList(String projectId) {
        List<ProjectContent> projectContentList = DBHelper.get().getProjectContentDao().
                queryBuilder().where(ProjectContentDao.Properties.ProjectId.eq(projectId)).list();
        if (projectContentList == null) {
            return new ArrayList<>();
        } else {
            return projectContentList;
        }
    }

    /**
     * 按照{@ProjectContentDao.Properties{@ProjectId,AddressId,TagId}}查找数据库所有的ProjectDetialDao表
     * ProjectDetial 任务数据表
     *
     * @param projectId 任务id
     * @param addressId 点位id
     * @param tagId     要素ID
     * @return List<ProjectDetial>：查询所有的任务数据表
     */
    public static List<ProjectDetial> getProjectDetialList(String projectId, String addressId, String tagId) {
        if (projectId == null || projectId.equals("")) return new ArrayList<>();
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId),
                        ProjectDetialDao.Properties.AddressId.eq(addressId),
                        ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }
    }

    /**
     * 按照{@ProjectContentDao.Properties{@ProjectId,AddressId,TagId}}查找数据库所有的ProjectDetialDao表
     * ProjectDetial 任务数据表
     *
     * @param projectId 任务id
     * @param tagId     要素ID
     * @return List<ProjectDetial>：查询所有的任务数据表
     */
    public static List<ProjectDetial> getProjectDetialList(String projectId, String tagId) {
        if (projectId == null || projectId.equals("")) return new ArrayList<>();
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId),
                        ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }
    }

    /**
     * 按照{@ProjectContentDao.Properties@ProjectId}查找数据库所有的ProjectDetialDao表
     * ProjectDetial 任务数据表
     *
     * @param projectId 任务id
     * @return List<ProjectDetial>：查询所有的任务数据表
     */
    public static List<ProjectDetial> getProjectDetialList(String projectId) {
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId)
                ).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }
    }


    /**
     * 通过{@SamplingContentDao{@ProjectId,SamplingId }}查找数据库所有的SamplingDetailDao表
     * SamplingDetail 采样单数据表
     *
     * @param samplingId 采样单id
     * @return SamplingDetail 采样单数据表
     */
    public static List<SamplingDetail> getSamplingDetailList(String samplingId) {
        if (RxDataTool.isEmpty(samplingId)) return new ArrayList<>();
        return DBHelper.get().getSamplingDetailDao().queryBuilder().
                where(SamplingDetailDao.Properties.SamplingId.eq(samplingId)).list();
    }

    /**
     * 通过{@SamplingContentDao{@Id,SampingCode }}查找数据库所有的SamplingDetailDao表
     * SamplingDetail 采样单数据表
     *
     * @param id         主键id
     * @param sampleCode 采样单编号
     * @return SamplingDetail 采样单数据表
     */
    public static List<SamplingDetail> getSamplingDetailList(String id, String sampleCode) {
        if (RxDataTool.isEmpty(id)) return new ArrayList<>();
        if (RxDataTool.isEmpty(sampleCode)) return new ArrayList<>();
        return DBHelper.get().getSamplingDetailDao().queryBuilder().where(
                SamplingDetailDao.Properties.Id.eq(id),
                SamplingDetailDao.Properties.SampingCode.eq(sampleCode)
        ).list();
    }

    /**
     * 查找数据库所有的SamplingDetailDao表
     * @param samplingId 采样id
     * @param sampleCode 采样编号
     * @param samplingType  采样type
     * @return List<SamplingDetail>
     */
    public static List<SamplingDetail> getSamplingDetailList(String samplingId, String sampleCode,
                                                             int samplingType) {
        return DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.
                        Properties.SamplingId.eq(samplingId),
                SamplingDetailDao.Properties.SampingCode.eq(sampleCode),
                SamplingDetailDao.Properties.SamplingType.eq(samplingType)
        ).build().list();
    }

    /**
     * 查找数据库所有的SamplingDetailDao表
     * @param samplingId 采样id
     * @param projectId 任务ID
     * @param sampleCode code
     * @param frequecyNo no
     * @param samplingType type
     * @return  List<SamplingDetail>
     */
    public static List<SamplingDetail> getSamplingDetailList(String samplingId, String projectId,
                                                             String sampleCode,
                                                             int frequecyNo, int samplingType) {
        return   DBHelper.get().getSamplingDetailDao().
                queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingId),
                SamplingDetailDao.Properties.ProjectId.eq(projectId),
                SamplingDetailDao.Properties.SampingCode.eq(sampleCode),
                SamplingDetailDao.Properties.FrequecyNo.eq(frequecyNo),
                SamplingDetailDao.Properties.SamplingType.eq(samplingType)).list();
    }


    /**
     * 通过{@MsgDao.Properties.MsgStatus}查找数据库所有的MsgDao表
     * MsgDao 消息
     *
     * @param state 消息状态 {@0未读，1已读}
     * @return 返回的消息表
     */
    public static List<Msg> getMsgListForState(int state) {
        return DBHelper.get().getMsgDao().queryBuilder().where(MsgDao.Properties.MsgStatus.eq(state)).list();
    }

    /**
     * 通过{@MsgDao.Properties.SendTime}查找数据库所有的MsgDao表
     * MsgDao 消息表
     *
     * @return 返回降序的消息表
     */
    public static List<Msg> getMsgListForSendTime() {
        return DBHelper.get().getMsgDao().queryBuilder().orderDesc(MsgDao.Properties.SendTime).list();
    }

    /**
     * 通过{(UserDao.Properties.Id}查找数据库所有的UserDao表
     * UserDao 用户表
     *
     * @param userIds 查找的用户id集合
     * @return List<User>用户表
     */
    public static List<User> getUserList(List<String> userIds) {
        return DBHelper.get().getUserDao().queryBuilder().where(UserDao.Properties.Id.in(userIds)).list();
    }


    /**
     * 通过{TagsDao.Properties.Id}查找数据库所有的TagsDao表
     * TagsDao 要素分类表
     *
     * @param tagId 要素分类表
     * @return Tags 要素
     */
    public static Tags getTags(String tagId) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
    }


    /**
     * 通过{TagsDao.Properties.Id}查找数据库所有的TagsDao表
     * TagsDao 要素分类表
     *
     * @param stringList 要素分类表
     * @return Tags 要素
     */
    public static List<Tags> getTags(List<String> stringList) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.in(stringList)).list();
    }

    public static List<Tags> getTagsForParentId(String parentId) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.ParentId.eq(parentId)).list();
    }

    /**
     * 获取所以的要素分类表
     *
     * @return
     */
    public static List<Tags> getTags() {
        return DBHelper.get().getTagsDao().loadAll();
    }

    /**
     * 通过{@FormSelectDao.Properties.FormId}查找FormSelect表
     * FormSelect 表单分类表（采样单分类）
     *
     * @param id 主键id
     * @return FormSelect表单分类表（采样单分类）
     */
    public static FormSelect getDbFormSelect(String id) {
        if (RxDataTool.isEmpty(id)) return new FormSelect();
        return DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.
                FormId.eq(id)).unique();
    }

    /**
     * 通过{@FormSelectDao.Properties.tagParentId}查找FormSelect表
     * FormSelect 表单分类表（采样单分类）
     *
     * @param tagParentId tagParentId
     * @return List<FormSelect>
     */
    public static List<FormSelect> getDbFormSelectForTagParentId(String tagParentId) {
        if (RxDataTool.isEmpty(tagParentId)) return new ArrayList<>();
        return DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.
                TagParentId.eq(tagParentId)).list();
    }

    /**
     * 查找数据库表单分类
     *
     * @return 表单分类集合
     */
    public static List<Form> getFormList() {
        return DBHelper.get().getFormDao().loadAll();
    }

    /**
     * 通过{SamplingFileDao.Properties.Id}查找数据库所有的数据库文件
     *
     * @param id 采样单id
     * @return SamplingFile 采样单文件
     */
    public static SamplingFile getSamplingFile(String id) {
        return DBHelper.get().getSamplingFileDao().
                queryBuilder().where(SamplingFileDao.Properties.Id.eq(id)).unique();
    }


    /**
     * 获取环境点位
     *
     * @param strings 点位集合
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointList(List<String> strings) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.
                Properties.Id.in(strings)).list();
    }

    /**
     * 获取环境点位
     *
     * @param tagIds tagId
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointListForTagId(List<String> tagIds) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.
                Properties.TagId.in(tagIds)).list();
    }

    public static List<EnvirPoint> getEnvirPointListForTagId(String tagId) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.
                Properties.TagId.in(tagId)).list();
    }

    /**
     * 获取环境点位
     *
     * @param string 点位ID
     * @return 环境质量点位信息
     */
    public static EnvirPoint getEnvirPointList(String string) {
        if (RxDataTool.isEmpty(string)) return new EnvirPoint();
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.
                Id.eq(string)).unique();
    }

    /**
     * 获取环境点位
     *
     * @param tagId 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointList(String tagId, List<String> pointIds) {
        return DBHelper
                .get()
                .getEnvirPointDao()
                .queryBuilder()
                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.in(pointIds))
                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取环境点位
     *
     * @param tagId 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointListNotInIds(String tagId, List<String> ids) {
        return DBHelper
                .get()
                .getEnvirPointDao()
                .queryBuilder()
                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.notIn(ids))
                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取污染源点位
     *
     * @param mRcvId 监测单位ID
     * @param ids    点位集合
     * @return List<EnterRelatePoint>
     */
    public static List<EnterRelatePoint> getEnterRelatePointList(String mRcvId, List<String> ids) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.in(ids))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取污染源点位
     *
     * @param mRcvId 监测单位ID
     * @param ids    点位集合
     * @return List<EnterRelatePoint>
     */
    public static List<EnterRelatePoint> getEnterRelatePointListNotIds(String mRcvId, List<String> ids) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.notIn(ids))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取污染源点位
     *
     * @param mRcvId 监测单位ID
     * @return List<EnterRelatePoint>
     */
    public static List<EnterRelatePoint> getEnterRelatePointList(String mRcvId) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 根据监测项目获取监测方法
     *
     * @param monItemId 监测项目id
     * @return 监测方法list
     */
    public static List<MonItemMethodRelation> getMonItemMethodRelationList(String monItemId) {
        return DBHelper.get().getMonItemMethodRelationDao().queryBuilder().
                where(MonItemMethodRelationDao.Properties.MonItemId.eq(monItemId)).list();
    }

    /**
     * 根据方法id获取方法详情
     *
     * @param methodIdList 方法id集合
     * @return 方法详情集合
     */
    public static List<Methods> getMethodList(List<String> methodIdList) {
        if (RxDataTool.isEmpty(methodIdList)) return new ArrayList<>();
        return DBHelper.get().getMethodsDao().
                queryBuilder().where(MethodsDao.Properties.Id.
                in(methodIdList)).list();
    }

    /**
     * 获取天气
     *
     * @return 天气
     */
    public static List<Weather> getWeatherList() {
        return DBHelper.get().getWeatherDao().queryBuilder().list();
    }

    /**
     * 获取采样规范
     *
     * @param tagId 要素Id
     * @return 采样规范列表
     */
    public static List<SamplingStantd> getSamplingStantdList(String tagId) {
        return DBHelper.get().getSamplingStantdDao().
                queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(tagId)).list();
    }

    /**
     * 根据采样id和监测项目id 查找分瓶
     *
     * @param samplingId
     * @param itemId
     * @return
     */
    public static List<SamplingFormStand> getSamplingStanTdList(String samplingId, String itemId) {
        List<SamplingFormStand>
                stanTdList = DBHelper.get().getSamplingFormStandDao().queryBuilder()
                .where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId),
                        SamplingFormStandDao.Properties.MonitemIds.like(itemId + "%")).list();
        if (stanTdList == null) return new ArrayList<>();
        else return stanTdList;
    }
    /**
     * 根据i采样d获取分瓶集合
     *
     * @param id
     * @return
     */
    public static List<SamplingFormStand> getSamplingFormStandListForSampId(String id) {
        if (RxDataTool.isEmpty(id)) return new ArrayList<>();
        List<SamplingFormStand> list = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                where(SamplingFormStandDao.
                        Properties.SamplingId.eq(id)).list();
        if (list == null) return new ArrayList<>();
        else return list;
    }
}
