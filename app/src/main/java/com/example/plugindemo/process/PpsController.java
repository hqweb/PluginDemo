package com.example.plugindemo.process;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class PpsController {
    final private IBinder mRemote;

    PpsController(IBinder remote) {
        mRemote = remote;
    }

    public void startPlugin() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(PpsBinder.DESCRIPTOR);
            mRemote.transact(PpsBinder.TRANSACTION_startPlugin, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }

    public void initPlugin() throws RemoteException {
        Parcel _data = Parcel.obtain();
        Parcel _reply = Parcel.obtain();
        try {
            _data.writeInterfaceToken(PpsBinder.DESCRIPTOR);
            mRemote.transact(PpsBinder.TRANSACTION_initPlugin, _data, _reply, 0);
        } finally {
            _reply.recycle();
            _data.recycle();
        }
    }


}
