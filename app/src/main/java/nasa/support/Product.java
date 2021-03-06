package nasa.support;

/**
 * Created by Sangeeth Nandakumar on 13-02-2017.
 */

public class Product
{
    private int id;
    private int shopid;
    private String product;
    private String company;
    private double price;
    private int quantity;
    private String type;
    private String specs;
    private double tax;
    private String image;

    public Product(int id, int shopid, String product, String company, double price, int quantity, String type, String specs, double tax, String image) {
        this.id = id;
        this.shopid = shopid;
        this.product = product;
        this.company = company;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.specs = specs;
        this.tax = tax;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
