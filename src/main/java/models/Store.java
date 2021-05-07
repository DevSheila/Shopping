package models;

import java.util.Objects;

public class Store {
    private int id;
    private String name;
    private String address;
    private String website;

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Store(String name, String address, String website) {
        this.name = name;
        this.address = address;
        this.website = website;
    }
    public Store() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return id == store.id &&
                Objects.equals(name, store.name) &&
                Objects.equals(website, store.website)&&
                Objects.equals(address, store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,id,address,website);
    }
}
