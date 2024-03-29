package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ENVIR_POINT".
*/
public class EnvirPointDao extends AbstractDao<EnvirPoint, String> {

    public static final String TABLENAME = "ENVIR_POINT";

    /**
     * Properties of entity EnvirPoint.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Name = new Property(1, String.class, "Name", false, "NAME");
        public final static Property Code = new Property(2, String.class, "Code", false, "CODE");
        public final static Property Longtitude = new Property(3, double.class, "Longtitude", false, "LONGTITUDE");
        public final static Property Latitude = new Property(4, double.class, "Latitude", false, "LATITUDE");
        public final static Property TagId = new Property(5, String.class, "TagId", false, "TAG_ID");
        public final static Property TagName = new Property(6, String.class, "TagName", false, "TAG_NAME");
        public final static Property UpdateTime = new Property(7, String.class, "UpdateTime", false, "UPDATE_TIME");
    }


    public EnvirPointDao(DaoConfig config) {
        super(config);
    }
    
    public EnvirPointDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ENVIR_POINT\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"NAME\" TEXT," + // 1: Name
                "\"CODE\" TEXT," + // 2: Code
                "\"LONGTITUDE\" REAL NOT NULL ," + // 3: Longtitude
                "\"LATITUDE\" REAL NOT NULL ," + // 4: Latitude
                "\"TAG_ID\" TEXT," + // 5: TagId
                "\"TAG_NAME\" TEXT," + // 6: TagName
                "\"UPDATE_TIME\" TEXT);"); // 7: UpdateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ENVIR_POINT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EnvirPoint entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(3, Code);
        }
        stmt.bindDouble(4, entity.getLongtitude());
        stmt.bindDouble(5, entity.getLatitude());
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(6, TagId);
        }
 
        String TagName = entity.getTagName();
        if (TagName != null) {
            stmt.bindString(7, TagName);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(8, UpdateTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EnvirPoint entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
 
        String Code = entity.getCode();
        if (Code != null) {
            stmt.bindString(3, Code);
        }
        stmt.bindDouble(4, entity.getLongtitude());
        stmt.bindDouble(5, entity.getLatitude());
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(6, TagId);
        }
 
        String TagName = entity.getTagName();
        if (TagName != null) {
            stmt.bindString(7, TagName);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(8, UpdateTime);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public EnvirPoint readEntity(Cursor cursor, int offset) {
        EnvirPoint entity = new EnvirPoint( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Code
            cursor.getDouble(offset + 3), // Longtitude
            cursor.getDouble(offset + 4), // Latitude
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // TagId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // TagName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // UpdateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EnvirPoint entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLongtitude(cursor.getDouble(offset + 3));
        entity.setLatitude(cursor.getDouble(offset + 4));
        entity.setTagId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTagName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUpdateTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(EnvirPoint entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(EnvirPoint entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(EnvirPoint entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
