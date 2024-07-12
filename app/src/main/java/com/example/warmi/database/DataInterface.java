package com.example.warmi.database;

public class DataInterface {
    public interface DataStatus {
        void DataIsInserted();
        void DataIsUpdated();
        void DataOperationFailed(Exception e);
    }
}
