package com.moglix.csv.CSV2Database.model;

public class User {
    private int Vendor_ID;
    private int asn_Quantity;

    public User() {
    }

    public User(int vendor_ID, int asn_Quantity) {
        Vendor_ID = vendor_ID;
        this.asn_Quantity = asn_Quantity;
    }

    public int getVendor_ID() {
        return Vendor_ID;
    }

    public void setVendor_ID(int vendor_ID) {
        Vendor_ID = vendor_ID;
    }

    public int getAsn_Quantity() {
        return asn_Quantity;
    }

    public void setAsn_Quantity(int asn_Quantity) {
        this.asn_Quantity = asn_Quantity;
    }
}
