package cn.cdjzxy.android.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Dic;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DIC".
*/
public class DicDao extends AbstractDao<Dic, String> {

    public static final String TABLENAME = "DIC";

    /**
     * Properties of entity Dic.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Code = new Property(1, String.class, "Code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
        public final static Property Type = new Property(3, int.class, "Type", false, "TYPE");
    }


    public DicDao(DaoConfig config) {
        super(config);
    }
    
    public DicDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DIC\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"CODE\" TEXT," + // 1: Code
                "\"NAME\" TEXT," + // 2: Name
                "\"TYPE\" INTEGER NOT NULL );"); // 3: Type
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DIC\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Dic entity) {
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
        stmt.bindLong(4, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Dic entity) {
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
        stmt.bindLong(4, entity.getType());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Dic readEntity(Cursor cursor, int offset) {
        Dic entity = new Dic( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Name
            cursor.getInt(offset + 3) // Type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Dic entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Dic entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Dic entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Dic entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
