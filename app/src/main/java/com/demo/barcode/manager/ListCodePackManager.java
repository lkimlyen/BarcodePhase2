package com.demo.barcode.manager;

import com.demo.architect.data.model.ApartmentEntity;
import com.demo.architect.data.model.CodePackEntity;

import java.util.List;

public class ListCodePackManager {
    private List<CodePackEntity> list;
    private static ListCodePackManager instance;

    public static ListCodePackManager getInstance() {
        if (instance == null) {
            instance = new ListCodePackManager();
        }
        return instance;
    }

    public void setListCodePack(List<CodePackEntity> list) {
        this.list = list;
    }


    public CodePackEntity getCodePackById(String serialPack) {
        CodePackEntity codePackEntity = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSttPack().contains(serialPack)) {
                codePackEntity = list.get(i);
                break;
            }
        }
        return codePackEntity;

    }
}
