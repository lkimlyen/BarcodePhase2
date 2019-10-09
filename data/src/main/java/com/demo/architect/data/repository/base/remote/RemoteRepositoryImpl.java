package com.demo.architect.data.repository.base.remote;

//import javax.inject.Singleton;


/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class RemoteRepositoryImpl implements RemoteRepository {

    private final static String TAG = RemoteRepositoryImpl.class.getName();

    private RemoteApiInterface mRemoteApiInterface;

    public RemoteRepositoryImpl(RemoteApiInterface mRemoteApiInterface) {
        this.mRemoteApiInterface = mRemoteApiInterface;
    }


}


