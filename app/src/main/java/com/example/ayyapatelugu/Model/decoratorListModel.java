package com.example.ayyapatelugu.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class decoratorListModel {

    private decoratormodelResult[] result;


    public decoratormodelResult[] getResult() {
        return result;
    }

    public void setResult(decoratormodelResult[] result) {
        this.result = result;
    }
}
