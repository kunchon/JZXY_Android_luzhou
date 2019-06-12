package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Dic;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Enterprise;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.gps.GpsBean;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormFlow;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingUser;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Table;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;

import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.DevicesDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.DicDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnterpriseDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodDevRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodTagRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemTagRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.RightsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UnitDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.WeatherDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.GpsBeanDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormFlowDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingUserDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TableDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig devicesDaoConfig;
    private final DaoConfig dicDaoConfig;
    private final DaoConfig enterpriseDaoConfig;
    private final DaoConfig enterRelatePointDaoConfig;
    private final DaoConfig envirPointDaoConfig;
    private final DaoConfig methodDevRelationDaoConfig;
    private final DaoConfig methodsDaoConfig;
    private final DaoConfig methodTagRelationDaoConfig;
    private final DaoConfig monItemMethodRelationDaoConfig;
    private final DaoConfig monItemsDaoConfig;
    private final DaoConfig monItemTagRelationDaoConfig;
    private final DaoConfig rightsDaoConfig;
    private final DaoConfig tagsDaoConfig;
    private final DaoConfig unitDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig weatherDaoConfig;
    private final DaoConfig gpsBeanDaoConfig;
    private final DaoConfig msgDaoConfig;
    private final DaoConfig projectDaoConfig;
    private final DaoConfig projectContentDaoConfig;
    private final DaoConfig projectDetialDaoConfig;
    private final DaoConfig formDaoConfig;
    private final DaoConfig formFlowDaoConfig;
    private final DaoConfig formSelectDaoConfig;
    private final DaoConfig samplingDaoConfig;
    private final DaoConfig samplingContentDaoConfig;
    private final DaoConfig samplingDetailDaoConfig;
    private final DaoConfig samplingFileDaoConfig;
    private final DaoConfig samplingFormStandDaoConfig;
    private final DaoConfig samplingStantdDaoConfig;
    private final DaoConfig samplingUserDaoConfig;
    private final DaoConfig tableDaoConfig;
    private final DaoConfig userInfoDaoConfig;

    private final DevicesDao devicesDao;
    private final DicDao dicDao;
    private final EnterpriseDao enterpriseDao;
    private final EnterRelatePointDao enterRelatePointDao;
    private final EnvirPointDao envirPointDao;
    private final MethodDevRelationDao methodDevRelationDao;
    private final MethodsDao methodsDao;
    private final MethodTagRelationDao methodTagRelationDao;
    private final MonItemMethodRelationDao monItemMethodRelationDao;
    private final MonItemsDao monItemsDao;
    private final MonItemTagRelationDao monItemTagRelationDao;
    private final RightsDao rightsDao;
    private final TagsDao tagsDao;
    private final UnitDao unitDao;
    private final UserDao userDao;
    private final WeatherDao weatherDao;
    private final GpsBeanDao gpsBeanDao;
    private final MsgDao msgDao;
    private final ProjectDao projectDao;
    private final ProjectContentDao projectContentDao;
    private final ProjectDetialDao projectDetialDao;
    private final FormDao formDao;
    private final FormFlowDao formFlowDao;
    private final FormSelectDao formSelectDao;
    private final SamplingDao samplingDao;
    private final SamplingContentDao samplingContentDao;
    private final SamplingDetailDao samplingDetailDao;
    private final SamplingFileDao samplingFileDao;
    private final SamplingFormStandDao samplingFormStandDao;
    private final SamplingStantdDao samplingStantdDao;
    private final SamplingUserDao samplingUserDao;
    private final TableDao tableDao;
    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        devicesDaoConfig = daoConfigMap.get(DevicesDao.class).clone();
        devicesDaoConfig.initIdentityScope(type);

        dicDaoConfig = daoConfigMap.get(DicDao.class).clone();
        dicDaoConfig.initIdentityScope(type);

        enterpriseDaoConfig = daoConfigMap.get(EnterpriseDao.class).clone();
        enterpriseDaoConfig.initIdentityScope(type);

        enterRelatePointDaoConfig = daoConfigMap.get(EnterRelatePointDao.class).clone();
        enterRelatePointDaoConfig.initIdentityScope(type);

        envirPointDaoConfig = daoConfigMap.get(EnvirPointDao.class).clone();
        envirPointDaoConfig.initIdentityScope(type);

        methodDevRelationDaoConfig = daoConfigMap.get(MethodDevRelationDao.class).clone();
        methodDevRelationDaoConfig.initIdentityScope(type);

        methodsDaoConfig = daoConfigMap.get(MethodsDao.class).clone();
        methodsDaoConfig.initIdentityScope(type);

        methodTagRelationDaoConfig = daoConfigMap.get(MethodTagRelationDao.class).clone();
        methodTagRelationDaoConfig.initIdentityScope(type);

        monItemMethodRelationDaoConfig = daoConfigMap.get(MonItemMethodRelationDao.class).clone();
        monItemMethodRelationDaoConfig.initIdentityScope(type);

        monItemsDaoConfig = daoConfigMap.get(MonItemsDao.class).clone();
        monItemsDaoConfig.initIdentityScope(type);

        monItemTagRelationDaoConfig = daoConfigMap.get(MonItemTagRelationDao.class).clone();
        monItemTagRelationDaoConfig.initIdentityScope(type);

        rightsDaoConfig = daoConfigMap.get(RightsDao.class).clone();
        rightsDaoConfig.initIdentityScope(type);

        tagsDaoConfig = daoConfigMap.get(TagsDao.class).clone();
        tagsDaoConfig.initIdentityScope(type);

        unitDaoConfig = daoConfigMap.get(UnitDao.class).clone();
        unitDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        weatherDaoConfig = daoConfigMap.get(WeatherDao.class).clone();
        weatherDaoConfig.initIdentityScope(type);

        gpsBeanDaoConfig = daoConfigMap.get(GpsBeanDao.class).clone();
        gpsBeanDaoConfig.initIdentityScope(type);

        msgDaoConfig = daoConfigMap.get(MsgDao.class).clone();
        msgDaoConfig.initIdentityScope(type);

        projectDaoConfig = daoConfigMap.get(ProjectDao.class).clone();
        projectDaoConfig.initIdentityScope(type);

        projectContentDaoConfig = daoConfigMap.get(ProjectContentDao.class).clone();
        projectContentDaoConfig.initIdentityScope(type);

        projectDetialDaoConfig = daoConfigMap.get(ProjectDetialDao.class).clone();
        projectDetialDaoConfig.initIdentityScope(type);

        formDaoConfig = daoConfigMap.get(FormDao.class).clone();
        formDaoConfig.initIdentityScope(type);

        formFlowDaoConfig = daoConfigMap.get(FormFlowDao.class).clone();
        formFlowDaoConfig.initIdentityScope(type);

        formSelectDaoConfig = daoConfigMap.get(FormSelectDao.class).clone();
        formSelectDaoConfig.initIdentityScope(type);

        samplingDaoConfig = daoConfigMap.get(SamplingDao.class).clone();
        samplingDaoConfig.initIdentityScope(type);

        samplingContentDaoConfig = daoConfigMap.get(SamplingContentDao.class).clone();
        samplingContentDaoConfig.initIdentityScope(type);

        samplingDetailDaoConfig = daoConfigMap.get(SamplingDetailDao.class).clone();
        samplingDetailDaoConfig.initIdentityScope(type);

        samplingFileDaoConfig = daoConfigMap.get(SamplingFileDao.class).clone();
        samplingFileDaoConfig.initIdentityScope(type);

        samplingFormStandDaoConfig = daoConfigMap.get(SamplingFormStandDao.class).clone();
        samplingFormStandDaoConfig.initIdentityScope(type);

        samplingStantdDaoConfig = daoConfigMap.get(SamplingStantdDao.class).clone();
        samplingStantdDaoConfig.initIdentityScope(type);

        samplingUserDaoConfig = daoConfigMap.get(SamplingUserDao.class).clone();
        samplingUserDaoConfig.initIdentityScope(type);

        tableDaoConfig = daoConfigMap.get(TableDao.class).clone();
        tableDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        devicesDao = new DevicesDao(devicesDaoConfig, this);
        dicDao = new DicDao(dicDaoConfig, this);
        enterpriseDao = new EnterpriseDao(enterpriseDaoConfig, this);
        enterRelatePointDao = new EnterRelatePointDao(enterRelatePointDaoConfig, this);
        envirPointDao = new EnvirPointDao(envirPointDaoConfig, this);
        methodDevRelationDao = new MethodDevRelationDao(methodDevRelationDaoConfig, this);
        methodsDao = new MethodsDao(methodsDaoConfig, this);
        methodTagRelationDao = new MethodTagRelationDao(methodTagRelationDaoConfig, this);
        monItemMethodRelationDao = new MonItemMethodRelationDao(monItemMethodRelationDaoConfig, this);
        monItemsDao = new MonItemsDao(monItemsDaoConfig, this);
        monItemTagRelationDao = new MonItemTagRelationDao(monItemTagRelationDaoConfig, this);
        rightsDao = new RightsDao(rightsDaoConfig, this);
        tagsDao = new TagsDao(tagsDaoConfig, this);
        unitDao = new UnitDao(unitDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        weatherDao = new WeatherDao(weatherDaoConfig, this);
        gpsBeanDao = new GpsBeanDao(gpsBeanDaoConfig, this);
        msgDao = new MsgDao(msgDaoConfig, this);
        projectDao = new ProjectDao(projectDaoConfig, this);
        projectContentDao = new ProjectContentDao(projectContentDaoConfig, this);
        projectDetialDao = new ProjectDetialDao(projectDetialDaoConfig, this);
        formDao = new FormDao(formDaoConfig, this);
        formFlowDao = new FormFlowDao(formFlowDaoConfig, this);
        formSelectDao = new FormSelectDao(formSelectDaoConfig, this);
        samplingDao = new SamplingDao(samplingDaoConfig, this);
        samplingContentDao = new SamplingContentDao(samplingContentDaoConfig, this);
        samplingDetailDao = new SamplingDetailDao(samplingDetailDaoConfig, this);
        samplingFileDao = new SamplingFileDao(samplingFileDaoConfig, this);
        samplingFormStandDao = new SamplingFormStandDao(samplingFormStandDaoConfig, this);
        samplingStantdDao = new SamplingStantdDao(samplingStantdDaoConfig, this);
        samplingUserDao = new SamplingUserDao(samplingUserDaoConfig, this);
        tableDao = new TableDao(tableDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(Devices.class, devicesDao);
        registerDao(Dic.class, dicDao);
        registerDao(Enterprise.class, enterpriseDao);
        registerDao(EnterRelatePoint.class, enterRelatePointDao);
        registerDao(EnvirPoint.class, envirPointDao);
        registerDao(MethodDevRelation.class, methodDevRelationDao);
        registerDao(Methods.class, methodsDao);
        registerDao(MethodTagRelation.class, methodTagRelationDao);
        registerDao(MonItemMethodRelation.class, monItemMethodRelationDao);
        registerDao(MonItems.class, monItemsDao);
        registerDao(MonItemTagRelation.class, monItemTagRelationDao);
        registerDao(Rights.class, rightsDao);
        registerDao(Tags.class, tagsDao);
        registerDao(Unit.class, unitDao);
        registerDao(User.class, userDao);
        registerDao(Weather.class, weatherDao);
        registerDao(GpsBean.class, gpsBeanDao);
        registerDao(Msg.class, msgDao);
        registerDao(Project.class, projectDao);
        registerDao(ProjectContent.class, projectContentDao);
        registerDao(ProjectDetial.class, projectDetialDao);
        registerDao(Form.class, formDao);
        registerDao(FormFlow.class, formFlowDao);
        registerDao(FormSelect.class, formSelectDao);
        registerDao(Sampling.class, samplingDao);
        registerDao(SamplingContent.class, samplingContentDao);
        registerDao(SamplingDetail.class, samplingDetailDao);
        registerDao(SamplingFile.class, samplingFileDao);
        registerDao(SamplingFormStand.class, samplingFormStandDao);
        registerDao(SamplingStantd.class, samplingStantdDao);
        registerDao(SamplingUser.class, samplingUserDao);
        registerDao(Table.class, tableDao);
        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        devicesDaoConfig.clearIdentityScope();
        dicDaoConfig.clearIdentityScope();
        enterpriseDaoConfig.clearIdentityScope();
        enterRelatePointDaoConfig.clearIdentityScope();
        envirPointDaoConfig.clearIdentityScope();
        methodDevRelationDaoConfig.clearIdentityScope();
        methodsDaoConfig.clearIdentityScope();
        methodTagRelationDaoConfig.clearIdentityScope();
        monItemMethodRelationDaoConfig.clearIdentityScope();
        monItemsDaoConfig.clearIdentityScope();
        monItemTagRelationDaoConfig.clearIdentityScope();
        rightsDaoConfig.clearIdentityScope();
        tagsDaoConfig.clearIdentityScope();
        unitDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        weatherDaoConfig.clearIdentityScope();
        gpsBeanDaoConfig.clearIdentityScope();
        msgDaoConfig.clearIdentityScope();
        projectDaoConfig.clearIdentityScope();
        projectContentDaoConfig.clearIdentityScope();
        projectDetialDaoConfig.clearIdentityScope();
        formDaoConfig.clearIdentityScope();
        formFlowDaoConfig.clearIdentityScope();
        formSelectDaoConfig.clearIdentityScope();
        samplingDaoConfig.clearIdentityScope();
        samplingContentDaoConfig.clearIdentityScope();
        samplingDetailDaoConfig.clearIdentityScope();
        samplingFileDaoConfig.clearIdentityScope();
        samplingFormStandDaoConfig.clearIdentityScope();
        samplingStantdDaoConfig.clearIdentityScope();
        samplingUserDaoConfig.clearIdentityScope();
        tableDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
    }

    public DevicesDao getDevicesDao() {
        return devicesDao;
    }

    public DicDao getDicDao() {
        return dicDao;
    }

    public EnterpriseDao getEnterpriseDao() {
        return enterpriseDao;
    }

    public EnterRelatePointDao getEnterRelatePointDao() {
        return enterRelatePointDao;
    }

    public EnvirPointDao getEnvirPointDao() {
        return envirPointDao;
    }

    public MethodDevRelationDao getMethodDevRelationDao() {
        return methodDevRelationDao;
    }

    public MethodsDao getMethodsDao() {
        return methodsDao;
    }

    public MethodTagRelationDao getMethodTagRelationDao() {
        return methodTagRelationDao;
    }

    public MonItemMethodRelationDao getMonItemMethodRelationDao() {
        return monItemMethodRelationDao;
    }

    public MonItemsDao getMonItemsDao() {
        return monItemsDao;
    }

    public MonItemTagRelationDao getMonItemTagRelationDao() {
        return monItemTagRelationDao;
    }

    public RightsDao getRightsDao() {
        return rightsDao;
    }

    public TagsDao getTagsDao() {
        return tagsDao;
    }

    public UnitDao getUnitDao() {
        return unitDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public GpsBeanDao getGpsBeanDao() {
        return gpsBeanDao;
    }

    public MsgDao getMsgDao() {
        return msgDao;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public ProjectContentDao getProjectContentDao() {
        return projectContentDao;
    }

    public ProjectDetialDao getProjectDetialDao() {
        return projectDetialDao;
    }

    public FormDao getFormDao() {
        return formDao;
    }

    public FormFlowDao getFormFlowDao() {
        return formFlowDao;
    }

    public FormSelectDao getFormSelectDao() {
        return formSelectDao;
    }

    public SamplingDao getSamplingDao() {
        return samplingDao;
    }

    public SamplingContentDao getSamplingContentDao() {
        return samplingContentDao;
    }

    public SamplingDetailDao getSamplingDetailDao() {
        return samplingDetailDao;
    }

    public SamplingFileDao getSamplingFileDao() {
        return samplingFileDao;
    }

    public SamplingFormStandDao getSamplingFormStandDao() {
        return samplingFormStandDao;
    }

    public SamplingStantdDao getSamplingStantdDao() {
        return samplingStantdDao;
    }

    public SamplingUserDao getSamplingUserDao() {
        return samplingUserDao;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
