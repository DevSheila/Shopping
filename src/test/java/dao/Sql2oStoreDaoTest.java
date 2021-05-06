package dao;

import models.Items;
import models.Store;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Sql2oStoreDaoTest {
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
    public void addingStoreSetsId() {
        Store store =setUpStore();
        assertEquals(1,store.getId());
    }

    @Test
    public void getAll() {
        Store store = setUpStore();
        Store store2= setUpStore();
        assertEquals(2,storeDao.getAll().size());
    }

    @Test
    public void deleteById() {
        Store store =setUpStore();
        Store store1 = setUpStore();
        assertEquals(2,storeDao.getAll().size());
        storeDao.deleteById(store.getId());
        assertEquals(1,storeDao.getAll().size());
    }

    @Test
    public void clearAll() {
        Store store =setUpStore();
        Store store1 = setUpStore();
        storeDao.clearAll();
        assertEquals(0,storeDao.getAll().size());
    }

    @Test
    public void storesReturnItemsCorrecty() throws Exception {
        Items testItem = setUpItems();
        itemsDao.add(testItem);

        Items testItem2 = setUpItems();
        itemsDao.add(testItem2);

        Store testStore = setUpStore();
        storeDao.add(testStore);
        storeDao.addStoreToItem(testStore, testItem);
        storeDao.addStoreToItem(testStore,testItem2);

        Items[] items ={testItem, testItem2};

        assertEquals(Arrays.asList(items),storeDao.getAllItemsByStore(testStore.getId()));
    }

    public Store setUpStore() {
        Store store =  new Store("Naivas", "Syokimau", "www.naivas.com");
        storeDao.add(store);
        return store;
    }

    public Items setUpItems() {
        Items item = new Items("Bread", "festive","62", 1,1);
        itemsDao.add(item);
        return item;
    }
}