package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInputConfirm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NumberInputConfirmModel extends RealmObject {
    @PrimaryKey
    private long id;
    private long masterDetailId;
    private double numberInput;
    private double numberConfirmed;
    private double numberOut;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMasterDetailId() {
        return masterDetailId;
    }

    public void setMasterDetailId(long masterDetailId) {
        this.masterDetailId = masterDetailId;
    }

    public double getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(double numberInput) {
        this.numberInput = numberInput;
    }

    public double getNumberConfirmed() {
        return numberConfirmed;
    }

    public void setNumberConfirmed(double numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
    }

    public double getNumberScanOut() {
        return numberScanOut;
    }

    public void setNumberScanOut(double numberScanOut) {
        this.numberScanOut = numberScanOut;
    }

    public int getTimesInput() {
        return timesInput;
    }

    public void setTimesInput(int timesInput) {
        this.timesInput = timesInput;
    }

    private double numberScanOut;
    private int timesInput;

    public NumberInputConfirmModel() {
    }

    public NumberInputConfirmModel(long id, long masterDetailId, double numberInput, double numberConfirmed, double numberOut, double numberScanOut, int timesInput) {
        this.id = id;
        this.masterDetailId = masterDetailId;
        this.numberInput = numberInput;
        this.numberConfirmed = numberConfirmed;
        this.numberOut = numberOut;
        this.numberScanOut = numberScanOut;
        this.timesInput = timesInput;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(NumberInputConfirmModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }


    public static NumberInputConfirmModel create(Realm realm, NumberInputConfirm numberInputConfirm, long masterDetailId, double numberOut, double numberTotal) {
        double numberRest = numberTotal - numberInputConfirm.getNumberConfirmed();
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


    public double getNumberOut() {
        return numberOut;
    }

    public void setNumberOut(double numberOut) {
        this.numberOut = numberOut;
    }
}
