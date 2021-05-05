package dao;

import models.Items;
import models.Store;

import java.util.List;

public interface ItemsDao {

    //create
    void add(Items item);
    void addItemToStore(Items item, Store store);

    //read
    List<Items> getAll();
    List<Store> getAllStoresForItem(int id);
    Items findById(int id);
    List<Items> findByName(String name);
    //update

    //delete
    void deleteById(int id);

    void clearAll();
}
