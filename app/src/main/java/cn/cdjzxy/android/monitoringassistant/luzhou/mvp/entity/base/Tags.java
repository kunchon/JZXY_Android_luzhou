package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Tags {

    /**
     * Id : 20366344-3f7b-4b57-879a-076ccfdf9334
     * Name : 区域噪声
     * ParentId : 8a08d498-9936-4b2e-bbcb-cc79a061f73b
     * level : 1
     */
    @Id
    private String Id;
    private String Name;
    private String ParentId;
    private int    level;

    @ToMany
    @JoinEntity(
            entity = MonItemTagRelation.class,
            sourceProperty = "TagId",
            targetProperty = "MonItemId"
    )
    List<MonItems> mMonItems;


    @ToMany
    @JoinEntity(
            entity = MethodTagRelation.class,
            sourceProperty = "TagId",
            targetProperty = "MethodId"
    )
    List<Methods> mMethods;
    /**
     * Used to resolve relations
     */
    @Keep()
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Keep()
    private transient TagsDao    myDao;


    @Keep()
    public Tags(String Id, String Name, String ParentId, int level) {
        this.Id = Id;
        this.Name = Name;
        this.ParentId = ParentId;
        this.level = level;
    }


    @Keep()
    public Tags() {
    }


    public String getId() {
        return this.Id;
    }


    public void setId(String Id) {
        this.Id = Id;
    }


    public String getName() {
        return this.Name;
    }


    public void setName(String Name) {
        this.Name = Name;
    }


    public String getParentId() {
        return this.ParentId;
    }


    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }


    public int getLevel() {
        return this.level;
    }


    public void setLevel(int level) {
        this.level = level;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Keep()
    public List<MonItems> getMMonItems() {
        if (mMonItems == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MonItemsDao targetDao = daoSession.getMonItemsDao();
            List<MonItems> mMonItemsNew = targetDao._queryTags_MMonItems(Id);
            synchronized (this) {
                if (mMonItems == null) {
                    mMonItems = mMonItemsNew;
                }
            }
        }
        return mMonItems;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Keep()
    public synchronized void resetMMonItems() {
        mMonItems = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Keep()
    public List<Methods> getMMethods() {
        if (mMethods == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MethodsDao targetDao = daoSession.getMethodsDao();
            List<Methods> mMethodsNew = targetDao._queryTags_MMethods(Id);
            synchronized (this) {
                if (mMethods == null) {
                    mMethods = mMethodsNew;
                }
            }
        }
        return mMethods;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Keep()
    public synchronized void resetMMethods() {
        mMethods = null;
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Keep()
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Keep()
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Keep()
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Keep()
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTagsDao() : null;
    }

}
