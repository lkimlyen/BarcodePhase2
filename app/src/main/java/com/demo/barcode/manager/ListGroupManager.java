package com.demo.barcode.manager;

import com.demo.architect.data.model.GroupEntity;
import com.demo.architect.data.model.ProductGroupEntity;
import com.demo.architect.data.model.offline.GroupCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListGroupManager {
    private List<GroupEntity> listGroup = new ArrayList<>();
    private static ListGroupManager instance;

    public static ListGroupManager getInstance() {
        if (instance == null) {
            instance = new ListGroupManager();
        }
        return instance;
    }

    public List<GroupEntity> getListGroup() {
        return listGroup;
    }

    public void setListGroup(List<GroupEntity> listGroup) {
        this.listGroup = listGroup;
    }

    public ProductGroupEntity getProductById(long productId) {
        for (GroupEntity requestEntity : listGroup) {
            for (ProductGroupEntity productGroupEntity : requestEntity.getProducGroupList()) {
                if (productGroupEntity.getProductDetailID() == productId) {
                    return productGroupEntity;
                }
            }
        }
        return null;
    }

    public List<ProductGroupEntity> getListProductByGroupCode(String groupCode) {
        List<ProductGroupEntity> list = new ArrayList<>();
        for (GroupEntity requestEntity : listGroup) {
            if (requestEntity.getGroupCode().equals(groupCode)) {
                list.addAll(requestEntity.getProducGroupList());
                break;
            }
        }
        return list;
    }

    public GroupEntity getGroupEntityByGroupCode(String groupCode) {

        for (GroupEntity requestEntity : listGroup) {
            if (requestEntity.getGroupCode().equals(groupCode)) {
                return requestEntity;
            }
        }
        return null;
    }


    public HashMap<String,List<ProductGroupEntity>> getListGroupCode() {
        HashMap<String,List<ProductGroupEntity>> list = new HashMap<>();
        for (GroupEntity requestEntity : listGroup) {
            list.put(requestEntity.getGroupCode(),requestEntity.getProducGroupList());

        }
        return list;
    }

}
