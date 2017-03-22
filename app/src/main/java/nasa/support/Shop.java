package nasa.support;


import java.io.Serializable;

/**
 * Created by Sangeeth Nandakumar on 13-02-2017.
 */

public class Shop implements Serializable
{
    private int id;
    private String shopname;
    private String shoptype;
    private String shopdesc;
    private double lattitude;
    private double longitude;
    private int owner;
    private double rating;
    private String debitcard;
    private String paytm;
    private String openat;
    private String closeat;
    private String imageurl;

    public Shop(int id, String shopname, String shoptype, String shopdesc, double lattitude, double longitude, int owner, double rating, String debitcard, String paytm, String openat, String closeat, String imageurl) {
        this.id = id;
        this.shopname = shopname;
        this.shoptype = shoptype;
        this.shopdesc = shopdesc;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.owner = owner;
        this.rating = rating;
        this.debitcard = debitcard;
        this.paytm = paytm;
        this.openat = openat;
        this.closeat = closeat;
        this.imageurl = imageurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoptype() {
        return shoptype;
    }

    public void setShoptype(String shoptype) {
        this.shoptype = shoptype;
    }

    public String getShopdesc() {
        return shopdesc;
    }

    public void setShopdesc(String shopdesc) {
        this.shopdesc = shopdesc;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDebitcard() {
        return debitcard;
    }

    public void setDebitcard(String debitcard) {
        this.debitcard = debitcard;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public String getOpenat() {
        return openat;
    }

    public void setOpenat(String openat) {
        this.openat = openat;
    }

    public String getCloseat() {
        return closeat;
    }

    public void setCloseat(String closeat) {
        this.closeat = closeat;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
