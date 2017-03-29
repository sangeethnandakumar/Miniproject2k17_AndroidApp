package nasa.support;

import android.content.Context;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sangeeth Nandakumar on 18-02-2017.
 * Highly optimised Json parserclass with multiple listners
 */

public class JsonParser
{
    //Data members
    private Context context;
    private String jsontext;
    private List<Customer> customers=new ArrayList<>();
    private List<Shopkeeper> shopkeepers=new ArrayList<>();
    private List<Shop> shops=new ArrayList<>();
    private List<Product> products=new ArrayList<>();
    private List<Offer> offers=new ArrayList<>();

    //Listner object
    public OnCustomersParserListner customer_listner;
    public OnShopkeepersParserListner shopkeeper_listner;
    public OnShopsParserListner shop_listner;
    public OnProductsParserListner product_listner;
    public OnOffersParserListner offer_listner;

    // Interface Listners
    public interface OnCustomersParserListner
    {
        public void onCustomersParsed(List<Customer> customers);
    }
    public interface OnShopkeepersParserListner
    {
        public void onShopkeepersParsed(List<Shopkeeper> shopkeepers);
    }
    public interface OnShopsParserListner
    {
        public void onShopsParsed(List<Shop> shops);
    }
    public interface OnProductsParserListner
    {
        public void onProductsParsed(List<Product> products);
    }
    public interface OnOffersParserListner
    {
        public void onOffersParsed(List<Offer> offers);
    }



    //On Jsonparse listner
    public void setOnJsonParseListner(OnCustomersParserListner mylistner) { customer_listner=mylistner; }
    public void setOnJsonParseListner(OnShopkeepersParserListner mylistner) { shopkeeper_listner=mylistner; }
    public void setOnJsonParseListner(OnShopsParserListner mylistner) { shop_listner=mylistner; }
    public void setOnJsonParseListner(OnProductsParserListner mylistner) { product_listner=mylistner; }
    public void setOnJsonParseListner(OnOffersParserListner mylistner) { offer_listner=mylistner; }


    //Constructor
    public JsonParser(Context context, String jsontext)
    {
        this.context = context;
        this.jsontext = jsontext;
    }

    //Json pre-parsing
    public void parseCustomers()
    {
        // If json is true
        if (jsontext != null)
        {
            try
            {
                //Create a JSON object
                JSONObject jsonObj = new JSONObject(jsontext);
                // Getting JSON Array node
                JSONArray json = jsonObj.getJSONArray("details");
                // Looping through All Contacts
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject details = json.getJSONObject(i);
                    //Get all customer details from JSON file
                    String id = details.getString("id");
                    String firstname = details.getString("firstname");
                    String lastname = details.getString("lastname");
                    String username = details.getString("username");
                    String password = details.getString("password");
                    String device = details.getString("device");
                    //Add to list
                    customers.add(new Customer(id,firstname,lastname,username,password,device));
                }
                customer_listner.onCustomersParsed(customers);
            }
            catch (final JSONException e)
            {
                Toast.makeText(context, "Error in parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "JSON text is empty", Toast.LENGTH_SHORT).show();
        }
    }

    //Json pre-parsing
    public void parseShopkeepers()
    {
        // If json is true
        if (jsontext != null)
        {
            try
            {
                //Create a JSON object
                JSONObject jsonObj = new JSONObject(jsontext);
                // Getting JSON Array node
                JSONArray json = jsonObj.getJSONArray("details");
                // Looping through All Contacts
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject details = json.getJSONObject(i);
                    //Get all customer details from JSON file
                    String id = details.getString("id");
                    String firstname = details.getString("firstname");
                    String lastname = details.getString("lastname");
                    String device = details.getString("device");
                    String gender = details.getString("gender");
                    //Add to list
                    shopkeepers.add(new Shopkeeper(id,firstname,lastname,device,gender));
                }
                shopkeeper_listner.onShopkeepersParsed(shopkeepers);
            }
            catch (final JSONException e)
            {
                Toast.makeText(context, "Error in parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "JSON text is empty", Toast.LENGTH_SHORT).show();
        }
    }

    //Json pre-parsing
    public void parseShops()
    {
        // If json is true
        if (jsontext != null)
        {
            try
            {
                //Create a JSON object
                JSONObject jsonObj = new JSONObject(jsontext);
                // Getting JSON Array node
                JSONArray json = jsonObj.getJSONArray("details");
                // Looping through All Contacts
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject details = json.getJSONObject(i);
                    //Get all customer details from JSON file
                    String id=details.getString("id");
                    String shopname=details.getString("shopname");
                    String shoptype=details.getString("shoptype");
                    String shopdesc=details.getString("shopdesc");
                    String lattitude=details.getString("lattitude");
                    String longitude=details.getString("longitude");
                    String owner=details.getString("owner");
                    String rating=details.getString("rating");
                    String debitcard=details.getString("debitcard");
                    String paytm=details.getString("paytm");
                    String openat=details.getString("openat");
                    String closeat=details.getString("closeat");
                    String imageurl=details.getString("imageurl");
                    //Add to list
                    shops.add(new Shop(Integer.parseInt(id),shopname,shoptype,shopdesc,Double.parseDouble(lattitude),Double.parseDouble(longitude),Integer.parseInt(owner),Double.parseDouble(rating),debitcard,paytm,openat,closeat,imageurl));
                }
                shop_listner.onShopsParsed(shops);
            }
            catch (final JSONException e)
            {
                Toast.makeText(context, "Error in parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "JSON text is empty", Toast.LENGTH_SHORT).show();
        }
    }

    //Json pre-parsing
    public void parseProducts()
    {
        // If json is true
        if (jsontext != null)
        {
            try
            {
                //Create a JSON object
                JSONObject jsonObj = new JSONObject(jsontext);
                // Getting JSON Array node
                JSONArray json = jsonObj.getJSONArray("details");
                // Looping through All Contacts
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject details = json.getJSONObject(i);
                    //Get all customer details from JSON file
                    String id = details.getString("id");
                    String shopid = details.getString("shopid");
                    String product = details.getString("product");
                    String company = details.getString("company");
                    String price = details.getString("price");
                    String quantity = details.getString("quantity");
                    String type = details.getString("type");
                    //Add to list
                    products.add(new Product(Integer.parseInt(id),Integer.parseInt(shopid),product,company,Double.parseDouble(price),Integer.parseInt(quantity),type));
                }
                product_listner.onProductsParsed(products);
            }
            catch (final JSONException e)
            {
                Toast.makeText(context, "Error in parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "JSON text is empty", Toast.LENGTH_SHORT).show();
        }
    }

    //Json pre-parsing
    public void parseOffers()
    {
        // If json is true
        if (jsontext != null)
        {
            try
            {
                //Create a JSON object
                JSONObject jsonObj = new JSONObject(jsontext);
                // Getting JSON Array node
                JSONArray json = jsonObj.getJSONArray("details");
                // Looping through All Contacts
                for (int i = 0; i < json.length(); i++)
                {
                    JSONObject details = json.getJSONObject(i);
                    //Get all customer details from JSON file
                    String id = details.getString("id");
                    String shopid = details.getString("shopid");
                    String name = details.getString("name");
                    String desc = details.getString("desc");
                    String added = details.getString("added");
                    String expiry = details.getString("expiry");
                    //Add to list
                    offers.add(new Offer(Integer.parseInt(id), Integer.parseInt(shopid), name, desc, added, expiry));
                }
                offer_listner.onOffersParsed(offers);
            }
            catch (final JSONException e)
            {
                Toast.makeText(context, "Error in parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(context, "JSON text is empty", Toast.LENGTH_SHORT).show();
        }
    }
}
