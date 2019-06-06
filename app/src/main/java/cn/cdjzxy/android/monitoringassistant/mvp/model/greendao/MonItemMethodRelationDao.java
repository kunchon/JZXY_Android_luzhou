package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MON_ITEM_METHOD_RELATION".
*/
public class MonItemMethodRelationDao extends AbstractDao<MonItemMethodRelation, String> {

    public static final String TABLENAME = "MON_ITEM_METHOD_RELATION";

    /**
     * Properties of entity MonItemMethodRelation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property MonItemId = new Property(1, String.class, "MonItemId", false, "MON_ITEM_ID");
        public final static Property MethodId = new Property(2, String.class, "MethodId", false, "METHOD_ID");
    }


    public MonItemMethodRelationDao(DaoConfig config) {
        super(config);
    }
    
    public MonItemMethodRelationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MON_ITEM_METHOD_RELATION\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"MON_ITEM_ID\" TEXT," + // 1: MonItemId
                "\"METHOD_ID\" TEXT);"); // 2: MethodId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MON_ITEM_METHOD_RELATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MonItemMethodRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MonItemId = entity.getMonItemId();
        if (MonItemId != null) {
            stmt.bindString(2, MonItemId);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(3, MethodId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MonItemMethodRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MonItemId = entity.getMonItemId();
        if (MonItemId != null) {
            stmt.bindString(2, MonItemId);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(3, MethodId);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MonItemMethodRelation readEntity(Cursor cursor, int offset) {
        MonItemMethodRelation entity = new MonItemMethodRelation( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // MonItemId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // MethodId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MonItemMethodRelation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMonItemId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMethodId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MonItemMethodRelation entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MonItemMethodRelation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MonItemMethodRelation entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
