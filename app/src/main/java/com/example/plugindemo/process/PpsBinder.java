package com.example.plugindemo.process;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PpsBinder extends Binder {
    private final PluginProcessService mPps;
    static final String DESCRIPTOR = PpsBinder.class.getName();
    static final int TRANSACTION_startPlugin = (FIRST_CALL_TRANSACTION);
    static final int TRANSACTION_initPlugin = (FIRST_CALL_TRANSACTION)+1;

    PpsBinder(PluginProcessService pps) {
        mPps = pps;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code){
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_startPlugin: {
                data.enforceInterface(DESCRIPTOR);
                mPps.startPlugin();
            }
            case TRANSACTION_initPlugin: {
                data.enforceInterface(DESCRIPTOR);
                mPps.initPlugin();
            }
            default:
                return false;
        }
    }
}
