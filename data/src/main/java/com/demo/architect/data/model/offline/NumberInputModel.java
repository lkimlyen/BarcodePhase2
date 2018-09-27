package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NumberInputModel extends RealmObject {
   @PrimaryKey
    private long id;
    private double numberTotal;
    private double numberSuccess;
    private double numberScanned;
    private double numberRest;
    private int times;

    public NumberInputModel() {
    }

    public NumberInputModel(long id, double numberTotal, double numberSuccess, double numberScanned, double numberRest, int times) {
        this.id = id;
        this.numberTotal = numberTotal;
        this.numberSuccess = numberSuccess;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.times = times;
    }

    public static long id(Realm realm) {
       long nextId = 0;
        Number maxValue = realm.where(NumberInputModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
       nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static NumberInputModel create(Realm realm, NumberInput numberInput) {
        NumberInputModel numberInputModel = new NumberInputModel(id(realm) + 1, numberInput.getNumberTotalInput(),
                numberInput.getNumberSuccess(), numberInput.getNumberSuccess(), numberInput.getNumberWaitting(), numberInput.getTimesInput());
        numberInputModel = realm.copyToRealm(numberInputModel);
        return numberInputModel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(double numberTotal) {
        this.numberTotal = numberTotal;
    }

    public double getNumberSuccess() {
        return numberSuccess;
    }

    public void setNumberSuccess(double numberSuccess) {
        this.numberSuccess = numberSuccess;
    }

    public double getNumberScanned() {
        return numberScanned;
    }

    public void setNumberScanned(double numberScanned) {
        this.numberScanned = numberScanned;
    }

    public double getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(double numberRest) {
        this.numberRest = numberRest;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
