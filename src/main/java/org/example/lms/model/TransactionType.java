package org.example.lms.model;

public class TransactionType {
    private int TypeID;
    private String TypeDescription;

    public TransactionType(int typeID, String typeDescription) {
        TypeID = typeID;
        TypeDescription = typeDescription;
    }

    public int getTypeID() {
        return TypeID;
    }

    public void setTypeID(int typeID) {
        TypeID = typeID;
    }

    public String getTypeDescription() {
        return TypeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        TypeDescription = typeDescription;
    }
}
