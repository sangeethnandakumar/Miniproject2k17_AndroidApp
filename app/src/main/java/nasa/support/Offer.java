package nasa.support;

/**
 * Created by Sangeeth Nandakumar on 22-03-2017.
 */

public class Offer
{
    private int id;
    private int shopid;
    private String offer;
    private String desc;
    private String start;
    private String stop;

    public Offer(int id, int shopid, String offer, String desc, String start, String stop) {
        this.id = id;
        this.shopid = shopid;
        this.offer = offer;
        this.desc = desc;
        this.start = start;
        this.stop = stop;
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

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }
}
