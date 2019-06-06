package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodTagRelation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "METHOD_TAG_RELATION".
*/
public class MethodTagRelationDao extends AbstractDao<MethodTagRelation, String> {

    public static final String TABLENAME = "METHOD_TAG_RELATION";

    /**
     * Properties of entity MethodTagRelation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property MethodId = new Property(1, String.class, "MethodId", false, "METHOD_ID");
        public final static Property TagId = new Property(2, String.class, "TagId", false, "TAG_ID");
    }


    public MethodTagRelationDao(DaoConfig config) {
        super(config);
    }
    
    public MethodTagRelationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"METHOD_TAG_RELATION\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"METHOD_ID\" TEXT," + // 1: MethodId
                "\"TAG_ID\" TEXT);"); // 2: TagId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"METHOD_TAG_RELATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MethodTagRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(2, MethodId);
        }
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(3, TagId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MethodTagRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(2, MethodId);
        }
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(3, TagId);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MethodTagRelation readEntity(Cursor cursor, int offset) {
        MethodTagRelation entity = new MethodTagRelation( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // MethodId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // TagId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MethodTagRelation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMethodId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTagId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MethodTagRelation entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MethodTagRelation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MethodTagRelation entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}