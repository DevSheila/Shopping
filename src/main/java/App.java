import com.google.gson.Gson;
import dao.Sql2oItemsDao;
import dao.Sql2oStoreDao;
import exceptions.ApiException;
import models.Items;
import models.Store;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
//    static int getHerokuAssignedPort() {
//        ProcessBuilder processBuilder = new ProcessBuilder();
//        if (processBuilder.environment().get("PORT") != null) {
//            return Integer.parseInt(processBuilder.environment().get("PORT"));
//        }
//        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
//    }

    public static void main(String[] args) {

//        port(getHerokuAssignedPort());
        staticFileLocation("/public");

        Sql2oItemsDao itemDao;
        Sql2oStoreDao storeDao;
        Connection conn;
        Gson gson = new Gson();

//        String connectionString = "jdbc:postgresql://ec2-52-87-107-83.compute-1.amazonaws.com:5432/dc04vf9j5qb3g5";
//        Sql2o sql2o = new Sql2o(connectionString, "ohypnznmmpragr", "206b9a6ca39927ad20f09d316bf2d8e773cd93cc520509aee42c279981fb3ecc"); //!

        String connectionString = "jdbc:postgresql://localhost:5432/shopping";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "Password");

        storeDao = new Sql2oStoreDao(sql2o);
        itemDao = new Sql2oItemsDao(sql2o);
        conn = sql2o.open();

        //CREATE
        post("/stores/new", "application/json", (req, res) -> {
            Store store = gson.fromJson(req.body(), Store.class);
            storeDao.add(store);
            res.status(201);
            return gson.toJson(store);
        });
        post("/items/new", "application/json", (req, res) -> {
            Items item = gson.fromJson(req.body(), Items.class);
            itemDao.add(item);
            res.status(201);
            return gson.toJson(item);
        });

        //READ

        get("/","application/json", (request, response) -> {
            if (itemDao.getAll().size() > 0) {
                return gson.toJson(itemDao.getAll());
            } else {
                return "{\"message\":\"I'm sorry, but no items are currently listed in the database.\"}";
            }
        });

        get("/stores", "application/json", (req, res) -> {
            if (storeDao.getAll().size() > 0) {
                return gson.toJson(storeDao.getAll());
            } else {
                return "{\"message\":\"I'm sorry, but no stores are currently listed in the database.\"}";
            }
        });


        get("/stores/:id", "application/json", (req, res) -> {
            int storeId = Integer.parseInt(req.params("id"));
            Store storeToFind = storeDao.findById(storeId);
            if (storeToFind == null) {
                throw new ApiException(404, String.format("No stores with the id: \"%s\" exists", req.params("id")));
            }
            return gson.toJson(storeToFind);
        });
        get("/stores/:id/items", "application/json", (req, res) -> {
            int storeId = Integer.parseInt(req.params("id"));
            Store storeToFind = storeDao.findById(storeId);
            if (storeToFind == null) {
                throw new ApiException(404, String.format("No store with the id: \"%s\" exists", req.params("id")));
            } else if (storeDao.getAllItemsByStore(storeId).size() == 0) {
                return "{\"message\":\"I'm sorry, but no items are listed for this store.\"}";
            } else {
                return gson.toJson(storeDao.getAllItemsByStore(storeId));
            }
        });

        get("/stores/:storeId/items/:itemId", "application/json", (req, res) -> {
            int storeId = Integer.parseInt(req.params("storeId"));
            int itemId = Integer.parseInt(req.params("itemId"));
            Store store = storeDao.findById(storeId);
            Items item = itemDao.findById(itemId);


            if (store != null && item != null) {
                itemDao.addItemToStore(item, store);
                res.status(201);
                return gson.toJson(String.format("Store '%s' and Item '%s' have been associated", item.getName(), store.getName()));
            } else {
                throw new ApiException(404, String.format("Store or Item does not exist"));
            }
        });

        get("/items", "application/json", (req, res) -> {
            if (itemDao.getAll().size() > 0) {
                return gson.toJson(itemDao.getAll());
            } else {
                return "{\"message\":\"I'm sorry, but no items are currently listed in the database.\"}";
            }
        });

        get("/items/:id", "application/json", (req, res) -> {
            int itemId = Integer.parseInt(req.params("id"));
            Items itemToFind = itemDao.findById(itemId);
            if (itemToFind == null) {
                throw new ApiException(404, String.format("No items with the id: \"%s\" exists", req.params("id")));
            }
            return gson.toJson(itemToFind);
        });
        get("/items/:id/stores", "application/json", (req, res) -> {
            int itemId = Integer.parseInt(req.params("id"));
            Items itemToFind = itemDao.findById(itemId);
            if (itemToFind == null) {
                throw new ApiException(404, String.format("No items with the id: \"%s\" exists", req.params("id")));
            } else if (itemDao.getAllStoresForItem(itemId).size() == 0) {
                return "{\"message\":\"I'm sorry, but no stores are listed for this item.\"}";
            } else {
                return gson.toJson(itemDao.getAllStoresForItem(itemId));
            }
        });

        get("/items/search/:name", "application/json", (req, res) -> {
            String itemName = req.params("name");

            if (itemDao.findByName(itemName).size() == 0) {
                throw new ApiException(404, String.format("No items with the name: \"%s\" exists", req.params("name")));

            }else if(itemDao.findByName(itemName)==null){
                throw new ApiException(404, String.format("There are no items matching search parameters"));
            } else {
                return gson.toJson(itemDao.findByName(itemName));

            }
        });


        exception(ApiException.class, (exception, req, res) -> {
            ApiException err = exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatusCode());
            res.body(gson.toJson(jsonMap));
        });
        after((req, res) -> {
            res.type("application/json");
        });

    }
}