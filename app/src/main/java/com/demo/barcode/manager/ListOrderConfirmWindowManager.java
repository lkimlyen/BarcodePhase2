package com.demo.barcode.manager;

import com.demo.architect.data.model.OrderConfirmEntity;
import com.demo.architect.data.model.OrderConfirmWindowEntity;

import java.util.ArrayList;
import java.util.List;

public class ListOrderConfirmWindowManager {
    private List<OrderConfirmWindowEntity> listOrder = new ArrayList<>();
    private static ListOrderConfirmWindowManager instance;

    public static ListOrderConfirmWindowManager getInstance() {
        if (instance == null) {
            instance = new ListOrderConfirmWindowManager();
        }
        return instance;
    }

    public List<OrderConfirmWindowEntity> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderConfirmWindowEntity> listOrder) {
        this.listOrder = listOrder;
    }

    public OrderConfirmWindowEntity getDetailByProductDetailId(long productDetailID) {
        for (OrderConfirmWindowEntity requestEntity : listOrder) {
            if (requestEntity.getProductSetDetailID() == productDetailID) {
                return requestEntity;
            }
        }
        return null;
    }

//    public OrderConfirmEntity getOrderById(long id) {
//
//    }

}
