package nasa.support;


/**
 * Created by Sangeeth Nandakumar on 14-02-2017.
 */

public class Menu
{
    private String menuitem;
    private int image;

    public Menu(String menuitem, int image) {
        this.menuitem = menuitem;
        this.image = image;
    }

    public String getMenuitem() {
        return menuitem;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setMenuitem(String menuitem) {
        this.menuitem = menuitem;
    }
}
