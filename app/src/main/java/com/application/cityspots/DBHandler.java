package com.application.cityspots;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CitySpots";
    public static final String TABLE_User = "User";
    public static final String COLUMN_UserID = "userID";
    public static final String COLUMN_FullName = "fullName";
    public static final String COLUMN_Username = "username";
    public static final String COLUMN_Password = "password";
    public static final String COLUMN_City = "city";
    public static final String COLUMN_DefaultSpotFilter = "spotFilter";
    private static final int DATABASE_VERSION = 4;
    private final String[] userDefinition = Global.getUserDefinition();

    public static final String TABLE_Spot = "spot";
    public static final String COLUMN_SpotID = "spotID";
    public static final String COLUMN_SpotName = "spotName";
    public static final String COLUMN_SpotType = "spotType";
    public static final String COLUMN_Tag = "tag";
    public static final String COLUMN_Image = "image";
    public static final String COLUMN_Location = "location";
    public static final String COLUMN_FKUserID = "fkUserID";
    private final String[] spotDefinition = Global.getSpotDefinition();

    public static final String TABLE_SpotType = "spotType";
    public static final String COLUMN_SpotTypeID = "spotTypeID";
    public static final String COLUMN_SpotTypeName = "spotTypeName";
    //Uses
    //public static final String COLUMN_FKUserID = "fkUserID";

    public static final String TABLE_SpotLocation = "spotLocation";
    public static final String COLUMN_SpotLocationID = "spotLocationID";
    public static final String COLUMN_SpotLocationName = "spotLocationName";
    //Uses
    //public static final String COLUMN_FKUserID = "fkUserID";

    private static final String createUserTable = "create table " + TABLE_User +
            "(" + COLUMN_UserID + " integer primary key autoincrement, " +
            COLUMN_FullName + " text, " +
            COLUMN_Username + " text not null unique," +
            COLUMN_Password + " text not null," +
            COLUMN_City + " text, " +
            COLUMN_DefaultSpotFilter + " text, " +
            "Constraint username_unique UNIQUE (" + COLUMN_Username + ")" +
            "); ";

    private static final String createSpotTable = "create table " + TABLE_Spot +
            "(" + COLUMN_SpotID + " integer primary key autoincrement, " +
            COLUMN_SpotName + " text not null, " +
            COLUMN_SpotType + " text not null," +
            COLUMN_Tag + " text not null," +
            COLUMN_Image + " blob not null," +
            COLUMN_Location + " text not null," +
            COLUMN_FKUserID + " text);";


    private static final String createSpotTypeTable = "create table " + TABLE_SpotType +
            "(" + COLUMN_SpotTypeID + " integer primary key autoincrement, " +
            COLUMN_SpotTypeName + " text not null, " +
            COLUMN_FKUserID + " text);";

    private static final String createSpotLocationTable = "create table " + TABLE_SpotLocation +
            "(" + COLUMN_SpotLocationID + " integer primary key autoincrement, " +
            COLUMN_SpotLocationName + " text not null, " +
            COLUMN_FKUserID + " text);";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.println(Log.DEBUG, "Create User Table", createUserTable);
        sqLiteDatabase.execSQL(createUserTable);
        sqLiteDatabase.execSQL(createSpotTable);
        sqLiteDatabase.execSQL(createSpotTypeTable);
        sqLiteDatabase.execSQL(createSpotLocationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Spot);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SpotType);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SpotLocation);
        onCreate(sqLiteDatabase);
    }

    public void createTestUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Spot);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SpotType);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SpotLocation);
        onCreate(db);

        values.put(COLUMN_FullName, "Test User");
        values.put(COLUMN_Username, "test");
        values.put(COLUMN_Password, "test");
        values.put(COLUMN_City, "City");
        values.put(COLUMN_DefaultSpotFilter, "Type");
        db.insert(TABLE_User, null, values);
        db.close();
    }

    public boolean validUser(String username) {
        //noinspection UnusedAssignment
        int valid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_User +
                " where " + COLUMN_Username + "='" + username + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 1;
    }

    public int spotCount(String userID) {
        //noinspection UnusedAssignment
        int valid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "=" + userID + "");

        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid;
    }

    public boolean validPassword(String username, String password) {
        //noinspection UnusedAssignment
        int valid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_User +
                " where " + COLUMN_Username + "='" + username + "' And "
                + COLUMN_Password + "='" + password + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 1;
    }

    public User getUser(String username, String password) {

        User returnUser = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder columns = new StringBuilder(userDefinition[0]);
        for (int i = 1; i < userDefinition.length; i++) {
            columns.append(",").append(userDefinition[i]);
        }
        String query = "select " + columns + " from " + TABLE_User +
                " where " + COLUMN_Username + "='" + username + "' And " + COLUMN_Password + "='" + password + "'";

        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            for (int i = 0; i < userDefinition.length; i++) {
                if (c.getString(i) != null)
                    returnUser.setUserValue(c.getString(i), userDefinition[i]);
            }
        }
        db.close();
        c.close();
        return returnUser;
    }

    public Spot getSpot(String spotID) {
        Spot returnSpot = new Spot();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder columns = new StringBuilder(spotDefinition[0]);
        for (int i = 1; i < spotDefinition.length; i++) {
            columns.append(",").append(spotDefinition[i]);
        }
        String query = "select " + columns + " from " + TABLE_Spot +
                " where " + COLUMN_SpotID + "=" + spotID + "";
        Log.println(Log.DEBUG, "Query", query);
        //null is all columns

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            for (int i = 0; i < spotDefinition.length; i++) {
                if (!spotDefinition[i].equals("image")) {
                    if (c.getString(i) != null) {
                        Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                        Log.println(Log.DEBUG, "CursorString", "a " + c.getString(i));
                        returnSpot.setSpotValue(c.getString(i), spotDefinition[i]);
                    }
                } else {
                    Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                    returnSpot.setPhoto(c.getBlob(i));
                }
            }
        }
        db.close();
        c.close();
        return returnSpot;
    }

    public List<String> getUserSpotTypes(String userID) {
        List<String> returnSpotTypes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select DISTINCT " + COLUMN_SpotType + " from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "=" + userID + "";
        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        returnSpotTypes.add("All Spot Types");
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnSpotTypes.add(c.getString(c.getColumnIndex(COLUMN_SpotType)));
        }
        db.close();
        c.close();
        return returnSpotTypes;
    }

    public List<Spot> getUserSpotBySpotType(String userID, String spinnerFilter) {
        List<Spot> returnSpotList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.println(Log.DEBUG, "Query", spinnerFilter);

        StringBuilder columns = new StringBuilder(spotDefinition[0]);
        for (int i = 1; i < spotDefinition.length; i++) {
            columns.append(",").append(spotDefinition[i]);
        }
        String query = "select " + columns + " from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "=" + userID + "";

        if (!(spinnerFilter.equalsIgnoreCase("All Spot Types")))
            query = query + " AND " + COLUMN_SpotType + "='" + spinnerFilter + "'";
        Log.println(Log.DEBUG, "Spinner", query);

        //if (!spinnerFilter.trim().equalsIgnoreCase("All"));
        //  query += " AND " + COLUMN_SpotType + "='" + spinnerFilter + "'";
        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Spot temp = new Spot();
            for (int i = 0; i < spotDefinition.length; i++) {
                if (!spotDefinition[i].equals("image")) {
                    if (c.getString(i) != null) {
                        Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                        Log.println(Log.DEBUG, "CursorString", "a " + c.getString(i));
                        temp.setSpotValue(c.getString(i), spotDefinition[i]);
                    }
                } else {
                    Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                    temp.setPhoto(c.getBlob(i));
                }
            }
            returnSpotList.add(temp);
        }
        db.close();
        c.close();
        return returnSpotList;
    }

    public List<Spot> getUserSpotBySpotLocation(String userID, String spinnerFilter) {
        List<Spot> returnSpotList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.println(Log.DEBUG, "Query", spinnerFilter);

        StringBuilder columns = new StringBuilder(spotDefinition[0]);
        for (int i = 1; i < spotDefinition.length; i++) {
            columns.append(",").append(spotDefinition[i]);
        }
        String query = "select " + columns + " from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "=" + userID + "";

        if (!(spinnerFilter.equalsIgnoreCase("All Locations")))
            query = query + " AND " + COLUMN_Location + "='" + spinnerFilter + "'";
        Log.println(Log.DEBUG, "Spinner", query);

        //if (!spinnerFilter.trim().equalsIgnoreCase("All"));
        //  query += " AND " + COLUMN_SpotType + "='" + spinnerFilter + "'";
        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Spot temp = new Spot();
            for (int i = 0; i < spotDefinition.length; i++) {
                if (!spotDefinition[i].equals("image")) {
                    if (c.getString(i) != null) {
                        Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                        Log.println(Log.DEBUG, "CursorString", "a " + c.getString(i));
                        temp.setSpotValue(c.getString(i), spotDefinition[i]);
                    }
                } else {
                    Log.println(Log.DEBUG, "Spot Definition", spotDefinition[i]);
                    temp.setPhoto(c.getBlob(i));
                }
            }
            returnSpotList.add(temp);
        }
        db.close();
        c.close();
        return returnSpotList;
    }

    public void createUser(User newUser) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FullName, newUser.getFullName());
        values.put(COLUMN_Username, newUser.getUsername());
        values.put(COLUMN_Password, newUser.getPassword());
        values.put(COLUMN_City, newUser.getCity());
        values.put(COLUMN_DefaultSpotFilter, newUser.getDefaultSpotFilter());
        db.insert(TABLE_User, null, values);
        db.close();
    }

    public void createSpot(Spot newSpot, User currentUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotName, newSpot.getSpotName());
        values.put(COLUMN_SpotType, newSpot.getSpotType());
        values.put(COLUMN_Tag, newSpot.getTag());
        values.put(COLUMN_Image, newSpot.getByteArray());
        values.put(COLUMN_Location, newSpot.getLocation());
        values.put(COLUMN_FKUserID, currentUser.getUserID());
        db.insert(TABLE_Spot, null, values);
        db.close();
    }

    public void updateSpot(Spot updateSpot) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotName, updateSpot.getSpotName());
        values.put(COLUMN_SpotType, updateSpot.getSpotType());
        values.put(COLUMN_Tag, updateSpot.getTag());
        values.put(COLUMN_Image, updateSpot.getByteArray());
        values.put(COLUMN_Location, updateSpot.getLocation());
        String where = COLUMN_SpotID + " = ?";
        String[] whereArgs = new String[]{updateSpot.getSpotID()};
        db.update(TABLE_Spot, values, where, whereArgs);
        db.close();
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FullName, user.getFullName());
        values.put(COLUMN_City, user.getCity());
        String where = COLUMN_UserID + " = ?";
        String[] whereArgs = new String[]{user.getUserID()};
        db.update(TABLE_User, values, where, whereArgs);
        db.close();
    }

    public void createSpotType(Type newSpotType) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotTypeName, newSpotType.getTypeName());
        values.put(COLUMN_FKUserID, newSpotType.getUserID());
        db.insert(TABLE_SpotType, null, values);
        db.close();
    }

    public void createSpotLocation(Location newSpotLocation, User currentUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotTypeName, newSpotLocation.getLocationName());
        values.put(COLUMN_FKUserID, currentUser.getUserID());
        db.insert(TABLE_SpotLocation, null, values);
        db.close();
    }

    public void deleteSpot(String spotID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteStatement = "DELETE FROM " +
                TABLE_Spot + " WHERE " +
                COLUMN_SpotID + " = '" +
                spotID + "';";
        db.execSQL(deleteStatement);
        db.close();
    }


    public List<Type> getSpotTypes(String userID) {
        List<Type> returnSpotTypeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + TABLE_SpotType +
                " where " + COLUMN_FKUserID + "=" + userID + "";


        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Type tempType = new Type();
            tempType.setTypeID(c.getString(c.getColumnIndex(COLUMN_SpotTypeID)));
            tempType.setTypeName(c.getString(c.getColumnIndex(COLUMN_SpotTypeName)));
            tempType.setUserID(c.getString(c.getColumnIndex(COLUMN_FKUserID)));
            returnSpotTypeList.add(tempType);
        }
        db.close();
        c.close();
        return returnSpotTypeList;
    }
}