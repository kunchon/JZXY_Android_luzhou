package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MON_ITEMS".
*/
public class MonItemsDao extends AbstractDao<MonItems, String> {

    public static final String TABLENAME = "MON_ITEMS";

    /**
     * Properties of entity MonItems.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Code = new Property(1, String.class, "Code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
    }

    private Query<MonItems> tags_MMonItemsQuery;

    public MonItemsDao(DaoConfig config) {
        super(config);
    }
    
    public MonItemsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MON_ITEMS\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"CODE\" TEXT," + // 1: Code
                "\"NAME\" TEXT);"); // 2: Name
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MON_ITEMS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MonItems entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(2, Code);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MonItems entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(2, Code);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MonItems readEntity(Cursor cursor, int offset) {
        MonItems entity = new MonItems( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // Name
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MonItems entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MonItems entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MonItems entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MonItems entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "mMonItems" to-many relationship of Tags. */
    public List<MonItems> _queryTags_MMonItems(String TagId) {
        synchronized (this) {
            if (tags_MMonItemsQuery == null) {
                QueryBuilder<MonItems> queryBuilder = queryBuilder();
                queryBuilder.join(MonItemTagRelation.class, MonItemTagRelationDao.Properties.MonItemId)
                    .where(MonItemTagRelationDao.Properties.TagId.eq(TagId));
                tags_MMonItemsQuery = queryBuilder.build();
            }
        }
        Query<MonItems> query = tags_MMonItemsQuery.forCurrentThread();
        query.setParameter(0, TagId);
        return query.list();
    }

}