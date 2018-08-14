package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NumberInputModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int numberTotal;
    private int numberScanned;
    private int numberRest;
    private int times;

    public NumberInputModel() {
    }

    public NumberInputModel(int id, int numberTotal, int numberScanned, int numberRest, int times) {
        this.id = id;
        this.numberTotal = numberTotal;
        this.numberScanned = numberScanned;
        this.numberRest = numberRest;
        this.times = times;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(NumberInputModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static NumberInputModel create(Realm realm, NumberInput numberInput) {
        NumberInputModel numberInputModel = new NumberInputModel(id(realm)+1, numberInput.getNumberTotalInput(),
                numberInput.getNumberSuccess(), numberInput.getNumberWaitting(), numberInput.getTimesInput());
        numberInputModel = realm.copyToRealm(numberInputModel);
        return numberInputModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberTotal() {
        return numberTotal;
    }

    public void setNumberTotal(int numberTotal) {
        this.numberTotal = numberTotal;
    }

    public int getNumberScanned() {
        return numberScanned;
    }

    public void setNumberScanned(int numberScanned) {
        this.numberScanned = numberScanned;
    }

    public int getNumberRest() {
        return numberRest;
    }

    public void setNumberRest(int numberRest) {
        this.numberRest = numberRest;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
