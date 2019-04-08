/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Android_Development_IDE\\Eclipse_IDE\\MyWork\\AidlClientDemo\\src\\com\\shen\\aidlservicedemo\\Person.aidl
 */
package com.shen.aidlservicedemo;
public interface Person extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.shen.aidlservicedemo.Person
{
private static final java.lang.String DESCRIPTOR = "com.shen.aidlservicedemo.Person";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.shen.aidlservicedemo.Person interface,
 * generating a proxy if needed.
 */
public static com.shen.aidlservicedemo.Person asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.shen.aidlservicedemo.Person))) {
return ((com.shen.aidlservicedemo.Person)iin);
}
return new com.shen.aidlservicedemo.Person.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setName(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setAge:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setAge(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_display:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.display();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.shen.aidlservicedemo.Person
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setName(java.lang.String name) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(name);
mRemote.transact(Stub.TRANSACTION_setName, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setAge(int age) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(age);
mRemote.transact(Stub.TRANSACTION_setAge, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String display() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_display, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setAge = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_display = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void setName(java.lang.String name) throws android.os.RemoteException;
public void setAge(int age) throws android.os.RemoteException;
public java.lang.String display() throws android.os.RemoteException;
}
