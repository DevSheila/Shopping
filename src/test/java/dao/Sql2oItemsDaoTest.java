package dao;

import models.Items;
import models.Store;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;

public class Sql2oItemsDaoTest {
    private Connection conn;
    private Sql2oItemsDao itemsDao;
    private Sql2oStoreDao storeDao;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:DB/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        itemsDao = new Sql2oItemsDao(sql2o);
        storeDao = new Sql2oStoreDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingItemSetsId() throws Exception {
        Items testItems = setUpItems();
        assertEquals(1, testItems.getId());
    }

    @Test
    public void getAll() {
        Items item1 = setUpItems();
        Items item2 = setUpItems();
        assertEquals(2, itemsDao.getAll().size());
    }

    @Test
    public void deleteById() {
        Items testItem = setUpItems();
        Items otherItem = setUpItems();
        assertEquals(2, itemsDao.getAll().size());
        itemsDao.deleteById(testItem.getId());
        assertEquals(1, itemsDao.getAll().size());
    }

    @Test
    public void clearAll() {
        Items item = setUpItems();
        Items other = setUpItems();
        itemsDao.clearAll();
        assertEquals(0, itemsDao.getAll().size());
    }

    @Test
    public void addItemsToStoreAddsItemsCorrectly() throws Exception {
        Store testStore = setUpStore();
        Store altStore = setUpStore();

        storeDao.add(testStore);
        storeDao.add(altStore);

        Items testItems = setUpItems();

        itemsDao.add(testItems);

        itemsDao.addItemToStore(testItems, testStore);
        itemsDao.addItemToStore(testItems, altStore);

        assertEquals(2, itemsDao.getAllStoresForItem(testItems.getId()).size());
    }

    public Items setUpItems() {
        Items item = new Items("Bread", "festive", "half loaf",25,1);
        itemsDao.add(item);
        return item;
    }

    public Store setUpStore() {
        Store store = new Store("Naivas", "Syokimau", "www.naivas.com");
        storeDao.add(store);
        return store;
    }

}