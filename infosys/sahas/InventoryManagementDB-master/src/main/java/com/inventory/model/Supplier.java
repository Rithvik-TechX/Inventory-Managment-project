package com.inventory.model;

public class Supplier {
    private int id;
    private String name;
    private String contactInfo;
    private String address;

    public Supplier() {}
    public Supplier(String name, String contactInfo, String address) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }
    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }
    public String getContactInfo()        { return contactInfo; }
    public void setContactInfo(String c)  { this.contactInfo = c; }
    public String getAddress()            { return address; }
    public void setAddress(String a)      { this.address = a; }

    @Override
    public String toString() {
        return "Supplier{id=" + id + ", name='" + name +
                "', contact='" + contactInfo + "'}";
    }
}