package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.NumberInput;
import com.demo.architect.data.model.NumberInputConfirm;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NumberInputConfirmModel extends RealmObject {
    @PrimaryKey
    private int id;
    private int numberConfirmed;
    private int timesInput;

    public NumberInputConfirmModel() {
    }

    public NumberInputConfirmModel(int id, int numberConfirmed, int timesInput) {
        this.id = id;
        this.numberConfirmed = numberConfirmed;
        this.timesInput = timesInput;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(NumberInputConfirmModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static NumberInputConfirmModel create(Realm realm, NumberInputConfirm numberInput) {
        NumberInputConfirmModel numberInputModel = new NumberInputConfirmModel(id(realm), numberInput.getNumberConfirmed(),
                numberInput.getTimesInput());
        numberInputModel = realm.copyToRealm(numberInputModel);
        return numberInputModel;
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
}
