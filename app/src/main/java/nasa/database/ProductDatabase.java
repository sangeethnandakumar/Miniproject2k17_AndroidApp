package nasa.database;

import java.util.LinkedList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nasa.support.Product;

public class ProductDatabase extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Products";
    //Constructor
    public ProductDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Create table on create
        db.execSQL("CREATE TABLE Products(" +
                "id NUMBER(8)," +
                "shopid NUMBER(8)," +
                "product VARCHAR(25)," +
                "company VARCHAR(25)," +
                "price NUMBER(5)," +
                "quantity NUMBER(5)," +
                "type VARCHAR(25))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table
        db.execSQL("DROP TABLE IF EXISTS Products");
        // Create fresh table
        this.onCreate(db);
    }

    private static final String[] COLUMNS = {"id","shopid","product","company","price","quantity","type"};

    // GET A SINGLE PRODUCT FROM ID
    public Product getProduct(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query("Products", COLUMNS, " id = ?",
                        new String[] { String.valueOf(id) },
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        Product product = new Product(0,0,"","",0,0,"");
        product.setId(Integer.parseInt(cursor.getString(0)));
        product.setShopid(Integer.parseInt(cursor.getString(1)));
        product.setProduct(cursor.getString(2));
        product.setCompany(cursor.getString(3));
        product.setPrice(Double.parseDouble(cursor.getString(4)));
        product.setQuantity(Integer.parseInt(cursor.getString(5)));
        product.setType(cursor.getString(6));
        return product;
    }

    // GET ALL LIST OF PRODUCTS
    public List<Product> getAllProducts()
    {
        List<Product> products = new LinkedList<Product>();
        String query = "SELECT  * FROM Products";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Product product = null;
        if (cursor.moveToFirst())
        {
            do
            {
                Product myproduct = new Product(0,0,"","",0,0,"");
                myproduct.setId(Integer.parseInt(cursor.getString(0)));
                myproduct.setShopid(Integer.parseInt(cursor.getString(1)));
                myproduct.setProduct(cursor.getString(2));
                myproduct.setCompany(cursor.getString(3));
                myproduct.setPrice(Double.parseDouble(cursor.getString(4)));
                myproduct.setQuantity(Integer.parseInt(cursor.getString(5)));
                myproduct.setType(cursor.getString(6));
                products.add(myproduct);
            } while (cursor.moveToNext());
        }
        return products;
    }

    // DROP THE PRODUCT TABLE
    public void dropTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE Products");
    }

    //ADD A PRODUCT
    public void addProduct(Product product)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String id=String.valueOf(product.getId());
        String shopid=String.valueOf(product.getShopid());
        String item=product.getProduct();
        String company=product.getCompany();
        String price=String.valueOf(product.getPrice());
        String quantity=String.valueOf(product.getQuantity());
        String type=product.getType();
        db.execSQL("INSERT INTO Products VALUES("+id+","+shopid+",'"+item+"','"+company+"',"+price+","+quantity+",'"+type+"')");
    }
}