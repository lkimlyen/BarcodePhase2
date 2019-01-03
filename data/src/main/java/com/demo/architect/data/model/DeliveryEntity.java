package com.demo.architect.data.model;

import java.util.List;

public class DeliveryEntity {
    private String MaPhieu;
    private List<PrintConfirmEntity> list;

    public DeliveryEntity(String maPhieu, List<PrintConfirmEntity> list) {
        MaPhieu = maPhieu;
        this.list = list;
    }
}
