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
            COLUMN_SpotTypeName + " text not null unique, " +
            COLUMN_FKUserID + " text);";

    private static final String createSpotLocationTable = "create table " + TABLE_SpotLocation +
            "(" + COLUMN_SpotLocationID + " integer primary key autoincrement, " +
            COLUMN_SpotLocationName + " text not null unique, " +
            COLUMN_FKUserID + " text);";

    /*****************************************
     Constructor
     *****************************************/
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*****************************************
     OnCreate/Upgrade
     *****************************************/
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


    /*****************************************
     User Functions
     *****************************************/
    //Create
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

    //Read

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

    //Update

    public void updateUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FullName, user.getFullName());
        values.put(COLUMN_City, user.getCity());
        values.put(COLUMN_DefaultSpotFilter, user.getDefaultSpotFilter());
        String where = COLUMN_UserID + " = ?";
        String[] whereArgs = new String[]{user.getUserID()};
        db.update(TABLE_User, values, where, whereArgs);
        db.close();
    }


    /*****************************************
     Spot Functions
     *****************************************/

    //Create
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

    //Read
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

    public List<String> getUserLocationTypes(String userID) {
        List<String> returnSpotLocations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select DISTINCT " + COLUMN_Location + " from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "=" + userID + "";
        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        returnSpotLocations.add("All Spot Locations");
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            returnSpotLocations.add(c.getString(c.getColumnIndex(COLUMN_Location)));
        }
        db.close();
        c.close();
        return returnSpotLocations;
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

        if (!(spinnerFilter.equalsIgnoreCase("All Spot Locations")))
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

    //Update
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

    //Delete
    public void deleteSpot(String spotID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteStatement = "DELETE FROM " +
                TABLE_Spot + " WHERE " +
                COLUMN_SpotID + " = '" +
                spotID + "';";
        db.execSQL(deleteStatement);
        db.close();
    }


    /*****************************************
     Spot Location Functions
     *****************************************/
    //Create
    //Read
    //Update
    //Delete

    public void createSpotLocation(Location newSpotLocation) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotLocationName, newSpotLocation.getLocationName());
        values.put(COLUMN_FKUserID, newSpotLocation.getUserID());
        db.insert(TABLE_SpotLocation, null, values);
        db.close();
    }

    public void updateSpotLocation(Location updateSpotLocation, String oldSpotLocation) {

        List<Spot> userSpots = getUserSpotBySpotLocation(updateSpotLocation.getUserID(), oldSpotLocation);
        for (Spot currentSpot : userSpots) {
            currentSpot.setLocation(updateSpotLocation.getLocationName());
            updateSpot(currentSpot);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotLocationName, updateSpotLocation.getLocationName());
        values.put(COLUMN_FKUserID, updateSpotLocation.getUserID());
        String where = COLUMN_SpotLocationID + " = ?";
        String[] whereArgs = new String[]{updateSpotLocation.getLocationID()};
        db.update(TABLE_SpotLocation, values, where, whereArgs);
        db.close();
    }

    public List<Location> getLocations(String userID) {
        List<Location> returnLocationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + TABLE_SpotLocation +
                " where " + COLUMN_FKUserID + "=" + userID + "";


        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Location tempLocation = new Location();
            tempLocation.setLocationID(c.getString(c.getColumnIndex(COLUMN_SpotLocationID)));
            tempLocation.setLocationName(c.getString(c.getColumnIndex(COLUMN_SpotLocationName)));
            tempLocation.setUserID(c.getString(c.getColumnIndex(COLUMN_FKUserID)));
            returnLocationList.add(tempLocation);
        }
        db.close();
        c.close();
        return returnLocationList;
    }

    public boolean validSpotLocationDelete(String userID, String spotLocation) {
        //noinspection UnusedAssignment
        int valid = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "='" + userID +
                "' and " + COLUMN_Location + "='" + spotLocation + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 0;
    }

    public void deleteSpotLocation(String spotLocationID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteStatement = "DELETE FROM " +
                TABLE_SpotLocation + " WHERE " +
                COLUMN_SpotLocationID + " = '" +
                spotLocationID + "';";
        db.execSQL(deleteStatement);
        db.close();
    }
    public List<Location> getSpotLocations(String userID) {
        List<Location> returnSpotLocationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + TABLE_SpotLocation +
                " where " + COLUMN_FKUserID + "=" + userID + "";

        Log.println(Log.DEBUG, "Query", query);
        //null is all columns
        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()) {
            Location tempLocation = new Location();
            tempLocation.setLocationID(c.getString(c.getColumnIndex(COLUMN_SpotLocationID)));
            tempLocation.setLocationName(c.getString(c.getColumnIndex(COLUMN_SpotLocationName)));
            tempLocation.setUserID(c.getString(c.getColumnIndex(COLUMN_FKUserID)));
            returnSpotLocationList.add(tempLocation);
        }
        db.close();
        c.close();
        return returnSpotLocationList;
    }

    public boolean validSpotLocation(Location spotLocation) {
        //noinspection UnusedAssignment
        int valid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_SpotLocation +
                " where " + COLUMN_SpotLocationName + "='" + spotLocation.getLocationName() + "' and "
                + COLUMN_FKUserID + "='" + spotLocation.getUserID() + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 0;
    }
    /*****************************************
     Spot Type Functions
     *****************************************/
    //Create
    public void createSpotType(Type newSpotType) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotTypeName, newSpotType.getTypeName());
        values.put(COLUMN_FKUserID, newSpotType.getUserID());
        db.insert(TABLE_SpotType, null, values);
        db.close();
    }

    //Read
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

    //Update
    public void updateSpotType(Type updateSpotType, String oldSpotType) {

        List<Spot> userSpots = getUserSpotBySpotType(updateSpotType.getUserID(), oldSpotType);
        for (Spot currentSpot : userSpots) {
            currentSpot.setSpotType(updateSpotType.getTypeName());
            updateSpot(currentSpot);
        }

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SpotTypeName, updateSpotType.getTypeName());
        values.put(COLUMN_FKUserID, updateSpotType.getUserID());
        String where = COLUMN_SpotTypeID + " = ?";
        String[] whereArgs = new String[]{updateSpotType.getTypeID()};
        db.update(TABLE_SpotType, values, where, whereArgs);
        db.close();
    }

    public boolean validSpotTypeDelete(String userID, String SpotType) {
        //noinspection UnusedAssignment
        int valid = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_Spot +
                " where " + COLUMN_FKUserID + "='" + userID +
                "' and " + COLUMN_SpotType + "='" + SpotType + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 0;
    }

    public boolean validSpotType(Type spotType) {
        //noinspection UnusedAssignment
        int valid = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteStatement state = db.compileStatement("select count(*) from " + TABLE_SpotType +
                " where " + COLUMN_SpotTypeName + "='" + spotType.getTypeName() + "' and "
                + COLUMN_FKUserID + "='" + spotType.getUserID() + "'");
        valid = Integer.parseInt(state.simpleQueryForString());
        state.close();
        db.close();
        Log.println(Log.DEBUG, "Valid Count", valid + "");
        return valid == 0;
    }

    //Delete
    public void deleteSpotType(String spotTypeID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteStatement = "DELETE FROM " +
                TABLE_SpotType + " WHERE " +
                COLUMN_SpotTypeID + " = '" +
                spotTypeID + "';";
        db.execSQL(deleteStatement);
        db.close();
    }

}