//package com.a360play.a360nautica.data.booking;
//
//import android.os.Build;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import java.io.Serializable;
//
//public class AccesoriesGroup implements Serializable {
//    private int id;
//    private String item;
//	  private String amount;
//	  private String currency;
//	  private int itemcount;
//	  private boolean isSelected;
//
//
//    public AccesoriesGroup(int id, String item, String amount , String currency , int itemcount , boolean isSelected ) {
//        this.id = id;
//        this.item = item;
//		this.amount =amount;
//		this.currency=currency;
//		this.itemcount=itemcount;
//		this.isSelected=isSelected;
//    }
//
//
//    public AccesoriesGroup(Parcel in) {
//        this.id = id;
//        this.item = item;
//        this.amount =amount;
//        this.currency=currency;
//        this.itemcount=itemcount;
//        this.isSelected=isSelected;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getItem() {
//        return item;
//    }
//
//    public void setItem(String item) {
//        this.item = item;
//    }
//
//    public String getAmount() {
//        return amount;
//    }
//
//    public void setAmount(String amount) {
//        this.amount = amount;
//    }
//
//    public String getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
//
//    public int getItemcount() {
//        return itemcount;
//    }
//
//    public void setItemcount(int itemcount) {
//        this.itemcount = itemcount;
//    }
//
//    public boolean isSelected() {
//        return isSelected;
//    }
//
//    public void setSelected(boolean selected) {
//        isSelected = selected;
//    }
//
//  /*
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.item);
//        dest.writeString(this.amount);
//        dest.writeString(this.currency);
//        dest.writeInt(this.itemcount);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            dest.writeBoolean(this.isSelected);
//        }
//    }
//
//    public static final Creator CREATOR = new Creator() {
//        public AccesoriesGroup createFromParcel(Parcel in) {
//            return new AccesoriesGroup(in);
//        }
//
//        public AccesoriesGroup[] newArray(int size) {
//            return new AccesoriesGroup[size];
//        }
//    };*/
//
//
//}