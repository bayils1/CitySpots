package com.application.cityspots;

public class Type {
    private String TypeID;
    private String TypeName;
    private String UserID;

    public Type(){}
    public Type(String TypeName, String userID){
        this.TypeName = TypeName;
        this.UserID = userID;
    }
    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
