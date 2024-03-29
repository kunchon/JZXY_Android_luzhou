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

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodTagRelation;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "METHODS".
*/
public class MethodsDao extends AbstractDao<Methods, String> {

    public static final String TABLENAME = "METHODS";

    /**
     * Properties of entity Methods.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Code = new Property(1, String.class, "Code", false, "CODE");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
        public final static Property Type = new Property(3, String.class, "Type", false, "TYPE");
        public final static Property LabelType = new Property(4, int.class, "LabelType", false, "LABEL_TYPE");
    }

    private DaoSession daoSession;

    private Query<Methods> tags_MMethodsQuery;

    public MethodsDao(DaoConfig config) {
        super(config);
    }
    
    public MethodsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"METHODS\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"CODE\" TEXT," + // 1: Code
                "\"NAME\" TEXT," + // 2: Name
                "\"TYPE\" TEXT," + // 3: Type
                "\"LABEL_TYPE\" INTEGER NOT NULL );"); // 4: LabelType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"METHODS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Methods entity) {
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
 
        String Type = entity.getType();
        if (Type != null) {
            stmt.bindString(4, Type);
        }
        stmt.bindLong(5, entity.getLabelType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Methods entity) {
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
 
        String Type = entity.getType();
        if (Type != null) {
            stmt.bindString(4, Type);
        }
        stmt.bindLong(5, entity.getLabelType());
    }

    @Override
    protected final void attachEntity(Methods entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Methods readEntity(Cursor cursor, int offset) {
        Methods entity = new Methods( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Code
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Type
            cursor.getInt(offset + 4) // LabelType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Methods entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLabelType(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Methods entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Methods entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Methods entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "mMethods" to-many relationship of Tags. */
    public List<Methods> _queryTags_MMethods(String TagId) {
        synchronized (this) {
            if (tags_MMethodsQuery == null) {
                QueryBuilder<Methods> queryBuilder = queryBuilder();
                queryBuilder.join(MethodTagRelation.class, MethodTagRelationDao.Properties.MethodId)
                    .where(MethodTagRelationDao.Properties.TagId.eq(TagId));
                tags_MMethodsQuery = queryBuilder.build();
            }
        }
        Query<Methods> query = tags_MMethodsQuery.forCurrentThread();
        query.setParameter(0, TagId);
        return query.list();
    }

}
