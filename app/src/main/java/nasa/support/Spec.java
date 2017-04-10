package nasa.support;

/**
 * Created by Sangeeth Nandakumar on 06-04-2017.
 */

public class Spec {
    public String property;
    public String value;
    public int color;

    public Spec(String property, String value, int color) {
        this.property = property;
        this.value = value;
        this.color = color;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
