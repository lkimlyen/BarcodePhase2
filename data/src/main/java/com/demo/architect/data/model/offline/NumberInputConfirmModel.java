package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInputConfirm;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class NumberInputConfirmModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int masterDetailId;
    private int numberConfirmed;
    private int numberOut;
    private int numberScanOut;
    private int timesInput;

    public NumberInputConfirmModel() {
    }

    public NumberInputConfirmModel(int id, int masterDetailId, int numberConfirmed, int numberOut, int numberScanOut, int timesInput) {
        this.id = id;
        this.masterDetailId = masterDetailId;
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

    public static RealmList<NumberInputConfirmModel> createOrUpdate(Realm realm, List<NumberInputConfirm> list, int masterDetailId, int numberOut, int numberTotal, int sumTotalScan) {
        RealmList<NumberInputConfirmModel> realmList = new RealmList<>();
        RealmResults<NumberInputConfirmModel> results = realm.where(NumberInputConfirmModel.class).equalTo("masterDetailId", masterDetailId).findAll();
        if (results.size() == 0) {
            for (NumberInputConfirm numberInput : list) {
                if (numberInput.getNumberConfirmed() < numberTotal) {
                    NumberInputConfirmModel numberInputModel = new NumberInputConfirmModel(id(realm) + 1, masterDetailId, 0,
                            numberOut, numberOut - numberInput.getNumberConfirmed(), numberInput.getTimesInput());
                    numberInputModel = realm.copyToRealm(numberInputModel);
                    realmList.add(numberInputModel);
                }

            }
        } else {
            // int numberOutCurrent = results.max("numberOut").intValue();
            for (NumberInputConfirm numberInputConfirm : list) {
                if (numberInputConfirm.getNumberConfirmed() < numberTotal) {
                    NumberInputConfirmModel numberInputConfirmModel = results.where().equalTo("timesInput", numberInputConfirm.getTimesInput()).findFirst();
                    if (numberInputConfirmModel == null) {
                        NumberInputConfirmModel numberInputModel = new NumberInputConfirmModel(id(realm) + 1, masterDetailId, 0,
                                numberOut, numberOut - numberInputConfirm.getNumberConfirmed(), numberInputConfirm.getTimesInput());
                        numberInputModel = realm.copyToRealm(numberInputModel);
                        realmList.add(numberInputModel);
                    } else {
                        if (sumTotalScan < numberOut) {
                            numberInputConfirmModel.setNumberOut(numberOut);
                            numberInputConfirmModel.setNumberScanOut(numberInputConfirmModel.getNumberScanOut()+(numberOut - sumTotalScan));
                        }
                        realmList.add(numberInputConfirmModel);
                    }
                }
            }

        }

        return realmList;

    }


    public int getId() {
        return id;
    }

    public int getNumberConfirmed() {
        return numberConfirmed;
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

    public void setNumberConfirmed(int numberConfirmed) {
        this.numberConfirmed = numberConfirmed;
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
}
