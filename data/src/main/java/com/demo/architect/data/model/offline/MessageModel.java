package com.demo.architect.data.model.offline;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import static java.util.Objects.requireNonNull;

public class MessageModel extends RealmObject {

    @Required
    @PrimaryKey
    private String uuid;

    @Required
    private String content;

    private String info;

    public MessageModel() {}

    public MessageModel(String content, String info) {
        this.uuid = UUID.randomUUID().toString();
        this.content = content;
        this.info = info;
    }


}
