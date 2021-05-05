package dao;

import models.Items;
import models.Store;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oItemsDao implements ItemsDao {
    private final Sql2o sql2o;

    public Sql2oItemsDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Items item) {
        String sql = "INSERT INTO items (name, brand ,quantity, price, storeid) VALUES (:name, :brand, :quantity,:price, :storeId)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(item)
                    .executeUpdate()
                    .getKey();
            item.setId(id);
            String joinQuery = "INSERT INTO stores_items (storeid, itemid) VALUES (:storeId, :itemId)";
                con.createQuery(joinQuery)
                        .addParameter("storeId", item.getStoreId())
                        .addParameter("itemId", item.getId())
                        .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addItemToStore(Items item, Store store) {
        String sql = "INSERT INTO stores_items (storeid, itemid) VALUES (:storeId, :itemId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("storeId", store.getId())
                    .addParameter("itemId", item.getId())
                    .executeUpdate();
        }catch(Sql2oException ex){
            System.out.println(ex);
        }

    }

    @Override
    public List<Items> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM items")
                    .executeAndFetch(Items.class);
        }
    }

    @Override
    public List<Store> getAllStoresForItem(int itemId) {
        List<Store> stores = new ArrayList();

        String joinQuery = "SELECT storeid FROM stores_items WHERE itemid = :itemId";

        try (Connection con = sql2o.open()) {
            List<Integer> allStoreIds = con.createQuery(joinQuery)
                    .addParameter("itemId", itemId)
                    .executeAndFetch(Integer.class);
            for (Integer storeId : allStoreIds) {
                String storeQuery = "SELECT * FROM stores WHERE id = :storeId";
                stores.add(
                        con.createQuery(storeQuery)
                                .addParameter("storeId", storeId)
                                .executeAndFetchFirst(Store.class));
            }
        }catch (Sql2oException ex){
            System.out.println(ex);
        }
        return stores;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from items WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public Items findById(int id) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM items WHERE id=:id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(Items.class);
        }
    }

    @Override
    public List<Items> findByName(String itemName) {
        try(Connection con = sql2o.open()){
            return con.createQuery("SELECT * FROM items WHERE name=:itemName ORDER BY price ASC")
                    .addParameter("itemName", itemName)
                    .executeAndFetch(Items.class);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE from items";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

}
