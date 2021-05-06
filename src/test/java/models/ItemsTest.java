package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() {
        Items testItems = setUpItems();
        assertEquals("Bread", testItems.getName());
    }

    @Test
    public void getPrice() {
        Items testItems = setUpItems();
        assertEquals(50, testItems.getPrice());
    }

    @Test
    public void getStoreId() {
        Items testItems = setUpItems();
        assertEquals(1,testItems.getStoreId());
    }

    @Test
    public void setName() {
        Items testItems = setUpItems();
        testItems.setName("Eggs");
        assertNotEquals("Bread",testItems.getName());
    }

    @Test
    public void setSoreId() {
        Items testItems = setUpItems();
        testItems.setStoreId(2);
        assertNotEquals(1,testItems.getStoreId());
    }

    @Test
    public void setPrice() {
        Items testItems = setUpItems();
        testItems.setPrice(20);
        assertNotEquals(50,testItems.getPrice());
    }

    @Test
    public void setAndGetId() {
        Items testItems = setUpItems();
        testItems.setId(1);
        assertEquals(1,testItems.getId());
    }

    public Items setUpItems(){
        return new Items("Bread","united","15",50,1);
    }
}