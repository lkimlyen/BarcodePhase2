package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInputConfirm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NumberInputConfirmModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int masterDetailId;
    private int numberInput;
    private int numberConfirmed;
    private int numberOut;
    private int numberScanOut;
    private int timesInput;

    public NumberInputConfirmModel() {
    }

    public NumberInputConfirmModel(int id, int masterDetailId, int numberInput, int numberConfirmed, int numberOut, int numberScanOut, int timesInput) {
        this.id = id;
        this.masterDetailId = masterDetailId;
        this.numberInput = numberInput;
        this.numberConfirmed = numberConfirmed;
        this.numberOut = numberOut;
        this.numberScanOut = numberScanOut;
        this.timesInput = timesInput;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(NumberInputConfirmModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }


    public static NumberInputConfirmModel create(Realm realm, NumberInputConfirm numberInputConfirm, int masterDetailId, int numberOut, int numberTotal) {
        int numberRest = numberTotal - numberInputConfirm.getNumberConfirmed();
        NumberInputConfirmModel numberInputModel = null;
        if (numberRest >= numberOut) {
            numberInputModel = new NumberInputConfirmModel(id(realm) + 1, masterDetailId, 0,
                    numberInputConfirm.getNumberConfirmed(),  numberOut, numberOut, numberInputConfirm.getTimesInput());
            numberInputModel = realm.copyToRealm(numberInputModel);
        } else {

            numberInputModel = new NumberInputConfirmModel(id(realm) + 1, masterDetailId, 0,
                    numberInputConfirm.getNumberConfirmed(), numberOut, numberRest, numberInputConfirm.getTimesInput());
            numberInputModel = realm.copyToRealm(numberInputModel);
        }

        return numberInputModel;

    }

    public int getId() {
        return id;
    }

    public int getNumberInput() {
        return numberInput;
    }

    public int getTimesInput() {
        return timesInput;
    }


    public void setId(int id) {
        this.id = id;
    }


    public int getMasterDetailId() {
        return masterDetailId;
    }

    public void setMasterDetailId(int masterDetailId) {
        this.masterDetailId = masterDetailId;
    }

    public void setNumberInput(int numberInput) {
        this.numberInput = numberInput;
    }

    public int getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(int numberOut) {
        this.numberOut = numberOut;
    }

    public int getNumberScanOut() {
        return numberScanOut;
    }

    public void setNumberScanOut(int numberScanOut) {
        this.numberScanOut = numberScanOut;
    }

    public void setTimesInput(int timesInput) {
        this.timesInput = timesInput;
    }

    public int getNumberConfirmed() {
        return numberConfirmed;
    }

    public void setNumberConfirmed(int numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
    }
}
