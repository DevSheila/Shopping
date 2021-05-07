import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import models.Items;
import models.Store;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.ModelAndView;


import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;

import java.net.URI;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
    private static HttpURLConnection connection;
    private static Object HttpURLConnection;
    private static final String ITEMS_API_URL ="http://3263e75dc6b8.ngrok.io/items";
    private static final String STORES_API_URL ="http://3263e75dc6b8.ngrok.io/stores";
    private static final String STOREID_API_URL ="http://3263e75dc6b8.ngrok.io/stores/";
    private static final String ITEMNAME_API_URL ="http://3263e75dc6b8.ngrok.io/items/search/";
    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");
        String main = "templates/main.hbs";
        get("/",(request, response) ->{
            Map<String,Object> model = new HashMap<String,Object>();

            return new ModelAndView(model, "main.hbs");

        },new HandlebarsTemplateEngine());



        get("/items", (request, response) ->{

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest requests = HttpRequest.newBuilder()
                    .GET()
                    .header("accept","application/json")
                    .uri(URI.create(ITEMS_API_URL))
                    .build();
            HttpResponse<String> responses = client.send(requests,HttpResponse.BodyHandlers.ofString());
            //parse JSON to objects


            ObjectMapper mapper = new ObjectMapper();
            List<Items> item = mapper.readValue(responses.body(), new TypeReference<List<Items>>() {});

            item.forEach(System.out::println);

            Map<String, Object> model = new HashMap<>();
            model.put("items", item);
//            System.out.println(posts);
            return new ModelAndView(model, "items.hbs");
        } , new HandlebarsTemplateEngine());


        get("/stores", (request, response) ->{

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest requests = HttpRequest.newBuilder()
                    .GET()
                    .header("accept","application/json")
                    .uri(URI.create(STORES_API_URL))
                    .build();
            HttpResponse<String> responses = client.send(requests,HttpResponse.BodyHandlers.ofString());
            //parse JSON to objects

            ObjectMapper mapper = new ObjectMapper();
            List<Store> stores = mapper.readValue(responses.body(), new TypeReference<List<Store>>() {});

            stores.forEach(System.out::println);

            Map<String, Object> model = new HashMap<>();
            model.put("stores", stores);
            return new ModelAndView(model, "stores.hbs");
        } , new HandlebarsTemplateEngine());



        get("/stores/:storeid", (request, response) ->{
            int id = Integer.parseInt(request.params("storeid"));
            System.out.println(id);
            String iD =Integer.toString(id);

//            int id = Integer.parseInt(request.params("storeId"));
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest requests = HttpRequest.newBuilder()
                    .GET()
                    .header("accept","application/json")
                    .uri(URI.create(STOREID_API_URL+iD))
                    .build();
            HttpResponse<String> responses = client.send(requests,HttpResponse.BodyHandlers.ofString());
            //parse JSON to objects

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
             List<Store> stores = mapper.readValue(responses.body(),new TypeReference<List<Store>>() {});

            stores.forEach(System.out::println);

            Map<String, Object> model = new HashMap<>();
            model.put("stores", stores);
            return new ModelAndView(model, "stores.hbs");
        } , new HandlebarsTemplateEngine());

        post("items/searchItem", (request, response) ->{
            String itemName = request.queryParams("inputName");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest requests = HttpRequest.newBuilder()
                    .GET()
                    .header("accept","application/json")
                    .uri(URI.create(ITEMNAME_API_URL+itemName))
                    .build();
            HttpResponse<String> responses = client.send(requests,HttpResponse.BodyHandlers.ofString());
            //parse JSON to objects

            ObjectMapper mapper = new ObjectMapper();
            List<Items> searchItem = mapper.readValue(responses.body(), new TypeReference<List<Items>>() {});

            searchItem.forEach(System.out::println);

            Map<String, Object> model = new HashMap<>();
            model.put("items", searchItem);
            return new ModelAndView(model, "items.hbs");
        } , new HandlebarsTemplateEngine());


    }
}