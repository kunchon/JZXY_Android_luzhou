package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.gps.GpsBean;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Table;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormFlow;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingUser;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Dic;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Enterprise;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;

import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.GpsBeanDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TableDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormFlowDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingUserDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserInfoDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UnitDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.DevicesDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.RightsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.WeatherDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodTagRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.DicDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodDevRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.EnterpriseDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemTagRelationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig gpsBeanDaoConfig;
    private final DaoConfig msgDaoConfig;
    private final DaoConfig formDaoConfig;
    private final DaoConfig samplingFormStandDaoConfig;
    private final DaoConfig samplingContentDaoConfig;
    private final DaoConfig samplingDaoConfig;
    private final DaoConfig tableDaoConfig;
    private final DaoConfig samplingFileDaoConfig;
    private final DaoConfig formFlowDaoConfig;
    private final DaoConfig formSelectDaoConfig;
    private final DaoConfig samplingUserDaoConfig;
    private final DaoConfig samplingStantdDaoConfig;
    private final DaoConfig samplingDetailDaoConfig;
    private final DaoConfig userInfoDaoConfig;
    private final DaoConfig projectDetialDaoConfig;
    private final DaoConfig projectDaoConfig;
    private final DaoConfig projectContentDaoConfig;
    private final DaoConfig monItemMethodRelationDaoConfig;
    private final DaoConfig tagsDaoConfig;
    private final DaoConfig monItemsDaoConfig;
    private final DaoConfig unitDaoConfig;
    private final DaoConfig envirPointDaoConfig;
    private final DaoConfig devicesDaoConfig;
    private final DaoConfig rightsDaoConfig;
    private final DaoConfig weatherDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig enterRelatePointDaoConfig;
    private final DaoConfig methodsDaoConfig;
    private final DaoConfig methodTagRelationDaoConfig;
    private final DaoConfig dicDaoConfig;
    private final DaoConfig methodDevRelationDaoConfig;
    private final DaoConfig enterpriseDaoConfig;
    private final DaoConfig monItemTagRelationDaoConfig;

    private final GpsBeanDao gpsBeanDao;
    private final MsgDao msgDao;
    private final FormDao formDao;
    private final SamplingFormStandDao samplingFormStandDao;
    private final SamplingContentDao samplingContentDao;
    private final SamplingDao samplingDao;
    private final TableDao tableDao;
    private final SamplingFileDao samplingFileDao;
    private final FormFlowDao formFlowDao;
    private final FormSelectDao formSelectDao;
    private final SamplingUserDao samplingUserDao;
    private final SamplingStantdDao samplingStantdDao;
    private final SamplingDetailDao samplingDetailDao;
    private final UserInfoDao userInfoDao;
    private final ProjectDetialDao projectDetialDao;
    private final ProjectDao projectDao;
    private final ProjectContentDao projectContentDao;
    private final MonItemMethodRelationDao monItemMethodRelationDao;
    private final TagsDao tagsDao;
    private final MonItemsDao monItemsDao;
    private final UnitDao unitDao;
    private final EnvirPointDao envirPointDao;
    private final DevicesDao devicesDao;
    private final RightsDao rightsDao;
    private final WeatherDao weatherDao;
    private final UserDao userDao;
    private final EnterRelatePointDao enterRelatePointDao;
    private final MethodsDao methodsDao;
    private final MethodTagRelationDao methodTagRelationDao;
    private final DicDao dicDao;
    private final MethodDevRelationDao methodDevRelationDao;
    private final EnterpriseDao enterpriseDao;
    private final MonItemTagRelationDao monItemTagRelationDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        gpsBeanDaoConfig = daoConfigMap.get(GpsBeanDao.class).clone();
        gpsBeanDaoConfig.initIdentityScope(type);

        msgDaoConfig = daoConfigMap.get(MsgDao.class).clone();
        msgDaoConfig.initIdentityScope(type);

        formDaoConfig = daoConfigMap.get(FormDao.class).clone();
        formDaoConfig.initIdentityScope(type);

        samplingFormStandDaoConfig = daoConfigMap.get(SamplingFormStandDao.class).clone();
        samplingFormStandDaoConfig.initIdentityScope(type);

        samplingContentDaoConfig = daoConfigMap.get(SamplingContentDao.class).clone();
        samplingContentDaoConfig.initIdentityScope(type);

        samplingDaoConfig = daoConfigMap.get(SamplingDao.class).clone();
        samplingDaoConfig.initIdentityScope(type);

        tableDaoConfig = daoConfigMap.get(TableDao.class).clone();
        tableDaoConfig.initIdentityScope(type);

        samplingFileDaoConfig = daoConfigMap.get(SamplingFileDao.class).clone();
        samplingFileDaoConfig.initIdentityScope(type);

        formFlowDaoConfig = daoConfigMap.get(FormFlowDao.class).clone();
        formFlowDaoConfig.initIdentityScope(type);

        formSelectDaoConfig = daoConfigMap.get(FormSelectDao.class).clone();
        formSelectDaoConfig.initIdentityScope(type);

        samplingUserDaoConfig = daoConfigMap.get(SamplingUserDao.class).clone();
        samplingUserDaoConfig.initIdentityScope(type);

        samplingStantdDaoConfig = daoConfigMap.get(SamplingStantdDao.class).clone();
        samplingStantdDaoConfig.initIdentityScope(type);

        samplingDetailDaoConfig = daoConfigMap.get(SamplingDetailDao.class).clone();
        samplingDetailDaoConfig.initIdentityScope(type);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        projectDetialDaoConfig = daoConfigMap.get(ProjectDetialDao.class).clone();
        projectDetialDaoConfig.initIdentityScope(type);

        projectDaoConfig = daoConfigMap.get(ProjectDao.class).clone();
        projectDaoConfig.initIdentityScope(type);

        projectContentDaoConfig = daoConfigMap.get(ProjectContentDao.class).clone();
        projectContentDaoConfig.initIdentityScope(type);

        monItemMethodRelationDaoConfig = daoConfigMap.get(MonItemMethodRelationDao.class).clone();
        monItemMethodRelationDaoConfig.initIdentityScope(type);

        tagsDaoConfig = daoConfigMap.get(TagsDao.class).clone();
        tagsDaoConfig.initIdentityScope(type);

        monItemsDaoConfig = daoConfigMap.get(MonItemsDao.class).clone();
        monItemsDaoConfig.initIdentityScope(type);

        unitDaoConfig = daoConfigMap.get(UnitDao.class).clone();
        unitDaoConfig.initIdentityScope(type);

        envirPointDaoConfig = daoConfigMap.get(EnvirPointDao.class).clone();
        envirPointDaoConfig.initIdentityScope(type);

        devicesDaoConfig = daoConfigMap.get(DevicesDao.class).clone();
        devicesDaoConfig.initIdentityScope(type);

        rightsDaoConfig = daoConfigMap.get(RightsDao.class).clone();
        rightsDaoConfig.initIdentityScope(type);

        weatherDaoConfig = daoConfigMap.get(WeatherDao.class).clone();
        weatherDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        enterRelatePointDaoConfig = daoConfigMap.get(EnterRelatePointDao.class).clone();
        enterRelatePointDaoConfig.initIdentityScope(type);

        methodsDaoConfig = daoConfigMap.get(MethodsDao.class).clone();
        methodsDaoConfig.initIdentityScope(type);

        methodTagRelationDaoConfig = daoConfigMap.get(MethodTagRelationDao.class).clone();
        methodTagRelationDaoConfig.initIdentityScope(type);

        dicDaoConfig = daoConfigMap.get(DicDao.class).clone();
        dicDaoConfig.initIdentityScope(type);

        methodDevRelationDaoConfig = daoConfigMap.get(MethodDevRelationDao.class).clone();
        methodDevRelationDaoConfig.initIdentityScope(type);

        enterpriseDaoConfig = daoConfigMap.get(EnterpriseDao.class).clone();
        enterpriseDaoConfig.initIdentityScope(type);

        monItemTagRelationDaoConfig = daoConfigMap.get(MonItemTagRelationDao.class).clone();
        monItemTagRelationDaoConfig.initIdentityScope(type);

        gpsBeanDao = new GpsBeanDao(gpsBeanDaoConfig, this);
        msgDao = new MsgDao(msgDaoConfig, this);
        formDao = new FormDao(formDaoConfig, this);
        samplingFormStandDao = new SamplingFormStandDao(samplingFormStandDaoConfig, this);
        samplingContentDao = new SamplingContentDao(samplingContentDaoConfig, this);
        samplingDao = new SamplingDao(samplingDaoConfig, this);
        tableDao = new TableDao(tableDaoConfig, this);
        samplingFileDao = new SamplingFileDao(samplingFileDaoConfig, this);
        formFlowDao = new FormFlowDao(formFlowDaoConfig, this);
        formSelectDao = new FormSelectDao(formSelectDaoConfig, this);
        samplingUserDao = new SamplingUserDao(samplingUserDaoConfig, this);
        samplingStantdDao = new SamplingStantdDao(samplingStantdDaoConfig, this);
        samplingDetailDao = new SamplingDetailDao(samplingDetailDaoConfig, this);
        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);
        projectDetialDao = new ProjectDetialDao(projectDetialDaoConfig, this);
        projectDao = new ProjectDao(projectDaoConfig, this);
        projectContentDao = new ProjectContentDao(projectContentDaoConfig, this);
        monItemMethodRelationDao = new MonItemMethodRelationDao(monItemMethodRelationDaoConfig, this);
        tagsDao = new TagsDao(tagsDaoConfig, this);
        monItemsDao = new MonItemsDao(monItemsDaoConfig, this);
        unitDao = new UnitDao(unitDaoConfig, this);
        envirPointDao = new EnvirPointDao(envirPointDaoConfig, this);
        devicesDao = new DevicesDao(devicesDaoConfig, this);
        rightsDao = new RightsDao(rightsDaoConfig, this);
        weatherDao = new WeatherDao(weatherDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        enterRelatePointDao = new EnterRelatePointDao(enterRelatePointDaoConfig, this);
        methodsDao = new MethodsDao(methodsDaoConfig, this);
        methodTagRelationDao = new MethodTagRelationDao(methodTagRelationDaoConfig, this);
        dicDao = new DicDao(dicDaoConfig, this);
        methodDevRelationDao = new MethodDevRelationDao(methodDevRelationDaoConfig, this);
        enterpriseDao = new EnterpriseDao(enterpriseDaoConfig, this);
        monItemTagRelationDao = new MonItemTagRelationDao(monItemTagRelationDaoConfig, this);

        registerDao(GpsBean.class, gpsBeanDao);
        registerDao(Msg.class, msgDao);
        registerDao(Form.class, formDao);
        registerDao(SamplingFormStand.class, samplingFormStandDao);
        registerDao(SamplingContent.class, samplingContentDao);
        registerDao(Sampling.class, samplingDao);
        registerDao(Table.class, tableDao);
        registerDao(SamplingFile.class, samplingFileDao);
        registerDao(FormFlow.class, formFlowDao);
        registerDao(FormSelect.class, formSelectDao);
        registerDao(SamplingUser.class, samplingUserDao);
        registerDao(SamplingStantd.class, samplingStantdDao);
        registerDao(SamplingDetail.class, samplingDetailDao);
        registerDao(UserInfo.class, userInfoDao);
        registerDao(ProjectDetial.class, projectDetialDao);
        registerDao(Project.class, projectDao);
        registerDao(ProjectContent.class, projectContentDao);
        registerDao(MonItemMethodRelation.class, monItemMethodRelationDao);
        registerDao(Tags.class, tagsDao);
        registerDao(MonItems.class, monItemsDao);
        registerDao(Unit.class, unitDao);
        registerDao(EnvirPoint.class, envirPointDao);
        registerDao(Devices.class, devicesDao);
        registerDao(Rights.class, rightsDao);
        registerDao(Weather.class, weatherDao);
        registerDao(User.class, userDao);
        registerDao(EnterRelatePoint.class, enterRelatePointDao);
        registerDao(Methods.class, methodsDao);
        registerDao(MethodTagRelation.class, methodTagRelationDao);
        registerDao(Dic.class, dicDao);
        registerDao(MethodDevRelation.class, methodDevRelationDao);
        registerDao(Enterprise.class, enterpriseDao);
        registerDao(MonItemTagRelation.class, monItemTagRelationDao);
    }
    
    public void clear() {
        gpsBeanDaoConfig.clearIdentityScope();
        msgDaoConfig.clearIdentityScope();
        formDaoConfig.clearIdentityScope();
        samplingFormStandDaoConfig.clearIdentityScope();
        samplingContentDaoConfig.clearIdentityScope();
        samplingDaoConfig.clearIdentityScope();
        tableDaoConfig.clearIdentityScope();
        samplingFileDaoConfig.clearIdentityScope();
        formFlowDaoConfig.clearIdentityScope();
        formSelectDaoConfig.clearIdentityScope();
        samplingUserDaoConfig.clearIdentityScope();
        samplingStantdDaoConfig.clearIdentityScope();
        samplingDetailDaoConfig.clearIdentityScope();
        userInfoDaoConfig.clearIdentityScope();
        projectDetialDaoConfig.clearIdentityScope();
        projectDaoConfig.clearIdentityScope();
        projectContentDaoConfig.clearIdentityScope();
        monItemMethodRelationDaoConfig.clearIdentityScope();
        tagsDaoConfig.clearIdentityScope();
        monItemsDaoConfig.clearIdentityScope();
        unitDaoConfig.clearIdentityScope();
        envirPointDaoConfig.clearIdentityScope();
        devicesDaoConfig.clearIdentityScope();
        rightsDaoConfig.clearIdentityScope();
        weatherDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        enterRelatePointDaoConfig.clearIdentityScope();
        methodsDaoConfig.clearIdentityScope();
        methodTagRelationDaoConfig.clearIdentityScope();
        dicDaoConfig.clearIdentityScope();
        methodDevRelationDaoConfig.clearIdentityScope();
        enterpriseDaoConfig.clearIdentityScope();
        monItemTagRelationDaoConfig.clearIdentityScope();
    }

    public GpsBeanDao getGpsBeanDao() {
        return gpsBeanDao;
    }

    public MsgDao getMsgDao() {
        return msgDao;
    }

    public FormDao getFormDao() {
        return formDao;
    }

    public SamplingFormStandDao getSamplingFormStandDao() {
        return samplingFormStandDao;
    }

    public SamplingContentDao getSamplingContentDao() {
        return samplingContentDao;
    }

    public SamplingDao getSamplingDao() {
        return samplingDao;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public SamplingFileDao getSamplingFileDao() {
        return samplingFileDao;
    }

    public FormFlowDao getFormFlowDao() {
        return formFlowDao;
    }

    public FormSelectDao getFormSelectDao() {
        return formSelectDao;
    }

    public SamplingUserDao getSamplingUserDao() {
        return samplingUserDao;
    }

    public SamplingStantdDao getSamplingStantdDao() {
        return samplingStantdDao;
    }

    public SamplingDetailDao getSamplingDetailDao() {
        return samplingDetailDao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public ProjectDetialDao getProjectDetialDao() {
        return projectDetialDao;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public ProjectContentDao getProjectContentDao() {
        return projectContentDao;
    }

    public MonItemMethodRelationDao getMonItemMethodRelationDao() {
        return monItemMethodRelationDao;
    }

    public TagsDao getTagsDao() {
        return tagsDao;
    }

    public MonItemsDao getMonItemsDao() {
        return monItemsDao;
    }

    public UnitDao getUnitDao() {
        return unitDao;
    }

    public EnvirPointDao getEnvirPointDao() {
        return envirPointDao;
    }

    public DevicesDao getDevicesDao() {
        return devicesDao;
    }

    public RightsDao getRightsDao() {
        return rightsDao;
    }

    public WeatherDao getWeatherDao() {
        return weatherDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public EnterRelatePointDao getEnterRelatePointDao() {
        return enterRelatePointDao;
    }

    public MethodsDao getMethodsDao() {
        return methodsDao;
    }

    public MethodTagRelationDao getMethodTagRelationDao() {
        return methodTagRelationDao;
    }

    public DicDao getDicDao() {
        return dicDao;
    }

    public MethodDevRelationDao getMethodDevRelationDao() {
        return methodDevRelationDao;
    }

    public EnterpriseDao getEnterpriseDao() {
        return enterpriseDao;
    }

    public MonItemTagRelationDao getMonItemTagRelationDao() {
        return monItemTagRelationDao;
    }

}
