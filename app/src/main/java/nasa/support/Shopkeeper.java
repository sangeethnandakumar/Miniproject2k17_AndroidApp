package nasa.support;

/**
 * Created by Sangeeth Nandakumar on 13-02-2017.
 */

public class Shopkeeper
{
    private String id;
    private String firstname;
    private String lastname;
    private String device;
    private String gender;

    public Shopkeeper(String id, String firstname, String lastname, String device, String gender) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.device = device;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
