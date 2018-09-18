package com.demo.barcode.manager;

import com.demo.architect.data.model.OrderConfirmEntity;

import java.util.ArrayList;
import java.util.List;

public class ListOrderConfirmManager {
    private List<OrderConfirmEntity> listOrder = new ArrayList<>();
    private static ListOrderConfirmManager instance;

    public static ListOrderConfirmManager getInstance() {
        if (instance == null) {
            instance = new ListOrderConfirmManager();
        }
        return instance;
    }

    public List<OrderConfirmEntity> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderConfirmEntity> listOrder) {
        this.listOrder = listOrder;
    }

    public OrderConfirmEntity getDetailByProductDetailId(int productDetailID) {
        for (OrderConfirmEntity requestEntity : listOrder) {
            if (requestEntity.getProductDetailID() == productDetailID) {
                return requestEntity;
            }
        }
        return null;
    }

//    public OrderConfirmEntity getOrderById(int id) {
//
//    }

}
