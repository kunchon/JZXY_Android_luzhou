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

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodDevRelation;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Devices;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DEVICES".
*/
public class DevicesDao extends AbstractDao<Devices, String> {

    public static final String TABLENAME = "DEVICES";

    /**
     * Properties of entity Devices.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Name = new Property(1, String.class, "Name", false, "NAME");
        public final static Property Specification = new Property(2, String.class, "Specification", false, "SPECIFICATION");
        public final static Property SystemCode = new Property(3, String.class, "SystemCode", false, "SYSTEM_CODE");
        public final static Property DevCode = new Property(4, String.class, "DevCode", false, "DEV_CODE");
        public final static Property DepartmentId = new Property(5, String.class, "DepartmentId", false, "DEPARTMENT_ID");
        public final static Property DepartmentName = new Property(6, String.class, "DepartmentName", false, "DEPARTMENT_NAME");
        public final static Property Company = new Property(7, String.class, "Company", false, "COMPANY");
        public final static Property PurchasingDate = new Property(8, String.class, "PurchasingDate", false, "PURCHASING_DATE");
        public final static Property RePrice = new Property(9, float.class, "RePrice", false, "RE_PRICE");
        public final static Property StoreLoaction = new Property(10, String.class, "StoreLoaction", false, "STORE_LOACTION");
        public final static Property ExpireDate = new Property(11, String.class, "ExpireDate", false, "EXPIRE_DATE");
        public final static Property Manager = new Property(12, String.class, "Manager", false, "MANAGER");
        public final static Property State = new Property(13, int.class, "State", false, "STATE");
        public final static Property IsForceChecked = new Property(14, boolean.class, "IsForceChecked", false, "IS_FORCE_CHECKED");
        public final static Property CertCode = new Property(15, String.class, "CertCode", false, "CERT_CODE");
        public final static Property SourceWay = new Property(16, String.class, "SourceWay", false, "SOURCE_WAY");
        public final static Property SourceDate = new Property(17, String.class, "SourceDate", false, "SOURCE_DATE");
    }

    private Query<Devices> methods_MDevicesQuery;

    public DevicesDao(DaoConfig config) {
        super(config);
    }
    
    public DevicesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DEVICES\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"NAME\" TEXT," + // 1: Name
                "\"SPECIFICATION\" TEXT," + // 2: Specification
                "\"SYSTEM_CODE\" TEXT," + // 3: SystemCode
                "\"DEV_CODE\" TEXT," + // 4: DevCode
                "\"DEPARTMENT_ID\" TEXT," + // 5: DepartmentId
                "\"DEPARTMENT_NAME\" TEXT," + // 6: DepartmentName
                "\"COMPANY\" TEXT," + // 7: Company
                "\"PURCHASING_DATE\" TEXT," + // 8: PurchasingDate
                "\"RE_PRICE\" REAL NOT NULL ," + // 9: RePrice
                "\"STORE_LOACTION\" TEXT," + // 10: StoreLoaction
                "\"EXPIRE_DATE\" TEXT," + // 11: ExpireDate
                "\"MANAGER\" TEXT," + // 12: Manager
                "\"STATE\" INTEGER NOT NULL ," + // 13: State
                "\"IS_FORCE_CHECKED\" INTEGER NOT NULL ," + // 14: IsForceChecked
                "\"CERT_CODE\" TEXT," + // 15: CertCode
                "\"SOURCE_WAY\" TEXT," + // 16: SourceWay
                "\"SOURCE_DATE\" TEXT);"); // 17: SourceDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DEVICES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Devices entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
 
        String Specification = entity.getSpecification();
        if (Specification != null) {
            stmt.bindString(3, Specification);
        }
 
        String SystemCode = entity.getSystemCode();
        if (SystemCode != null) {
            stmt.bindString(4, SystemCode);
        }
 
        String DevCode = entity.getDevCode();
        if (DevCode != null) {
            stmt.bindString(5, DevCode);
        }
 
        String DepartmentId = entity.getDepartmentId();
        if (DepartmentId != null) {
            stmt.bindString(6, DepartmentId);
        }
 
        String DepartmentName = entity.getDepartmentName();
        if (DepartmentName != null) {
            stmt.bindString(7, DepartmentName);
        }
 
        String Company = entity.getCompany();
        if (Company != null) {
            stmt.bindString(8, Company);
        }
 
        String PurchasingDate = entity.getPurchasingDate();
        if (PurchasingDate != null) {
            stmt.bindString(9, PurchasingDate);
        }
        stmt.bindDouble(10, entity.getRePrice());
 
        String StoreLoaction = entity.getStoreLoaction();
        if (StoreLoaction != null) {
            stmt.bindString(11, StoreLoaction);
        }
 
        String ExpireDate = entity.getExpireDate();
        if (ExpireDate != null) {
            stmt.bindString(12, ExpireDate);
        }
 
        String Manager = entity.getManager();
        if (Manager != null) {
            stmt.bindString(13, Manager);
        }
        stmt.bindLong(14, entity.getState());
        stmt.bindLong(15, entity.getIsForceChecked() ? 1L: 0L);
 
        String CertCode = entity.getCertCode();
        if (CertCode != null) {
            stmt.bindString(16, CertCode);
        }
 
        String SourceWay = entity.getSourceWay();
        if (SourceWay != null) {
            stmt.bindString(17, SourceWay);
        }
 
        String SourceDate = entity.getSourceDate();
        if (SourceDate != null) {
            stmt.bindString(18, SourceDate);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Devices entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(2, Name);
        }
 
        String Specification = entity.getSpecification();
        if (Specification != null) {
            stmt.bindString(3, Specification);
        }
 
        String SystemCode = entity.getSystemCode();
        if (SystemCode != null) {
            stmt.bindString(4, SystemCode);
        }
 
        String DevCode = entity.getDevCode();
        if (DevCode != null) {
            stmt.bindString(5, DevCode);
        }
 
        String DepartmentId = entity.getDepartmentId();
        if (DepartmentId != null) {
            stmt.bindString(6, DepartmentId);
        }
 
        String DepartmentName = entity.getDepartmentName();
        if (DepartmentName != null) {
            stmt.bindString(7, DepartmentName);
        }
 
        String Company = entity.getCompany();
        if (Company != null) {
            stmt.bindString(8, Company);
        }
 
        String PurchasingDate = entity.getPurchasingDate();
        if (PurchasingDate != null) {
            stmt.bindString(9, PurchasingDate);
        }
        stmt.bindDouble(10, entity.getRePrice());
 
        String StoreLoaction = entity.getStoreLoaction();
        if (StoreLoaction != null) {
            stmt.bindString(11, StoreLoaction);
        }
 
        String ExpireDate = entity.getExpireDate();
        if (ExpireDate != null) {
            stmt.bindString(12, ExpireDate);
        }
 
        String Manager = entity.getManager();
        if (Manager != null) {
            stmt.bindString(13, Manager);
        }
        stmt.bindLong(14, entity.getState());
        stmt.bindLong(15, entity.getIsForceChecked() ? 1L: 0L);
 
        String CertCode = entity.getCertCode();
        if (CertCode != null) {
            stmt.bindString(16, CertCode);
        }
 
        String SourceWay = entity.getSourceWay();
        if (SourceWay != null) {
            stmt.bindString(17, SourceWay);
        }
 
        String SourceDate = entity.getSourceDate();
        if (SourceDate != null) {
            stmt.bindString(18, SourceDate);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Devices readEntity(Cursor cursor, int offset) {
        Devices entity = new Devices( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Specification
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // SystemCode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // DevCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // DepartmentId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // DepartmentName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Company
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // PurchasingDate
            cursor.getFloat(offset + 9), // RePrice
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // StoreLoaction
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // ExpireDate
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Manager
            cursor.getInt(offset + 13), // State
            cursor.getShort(offset + 14) != 0, // IsForceChecked
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // CertCode
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // SourceWay
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17) // SourceDate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Devices entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSpecification(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSystemCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDevCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDepartmentId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDepartmentName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCompany(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPurchasingDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRePrice(cursor.getFloat(offset + 9));
        entity.setStoreLoaction(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setExpireDate(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setManager(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setState(cursor.getInt(offset + 13));
        entity.setIsForceChecked(cursor.getShort(offset + 14) != 0);
        entity.setCertCode(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setSourceWay(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setSourceDate(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Devices entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Devices entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Devices entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "mDevices" to-many relationship of Methods. */
    public List<Devices> _queryMethods_MDevices(String MethodId) {
        synchronized (this) {
            if (methods_MDevicesQuery == null) {
                QueryBuilder<Devices> queryBuilder = queryBuilder();
                queryBuilder.join(MethodDevRelation.class, MethodDevRelationDao.Properties.DevId)
                    .where(MethodDevRelationDao.Properties.MethodId.eq(MethodId));
                methods_MDevicesQuery = queryBuilder.build();
            }
        }
        Query<Devices> query = methods_MDevicesQuery.forCurrentThread();
        query.setParameter(0, MethodId);
        return query.list();
    }

}