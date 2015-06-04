package com.vkclient.Classes;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class User {
    public final static String[] relationshipStatus = {"nonset", "single","in a relationship","engaged","married","married","actively searching","in love"};
    private  int id;
    private  String name;
    private  String status;
    private  DateTime birthDate;
    private String bdDateString;
    private String city;
    private String univers;
    private String relationship;
    private String dateFormat;
    private  String photo;
    private String langs;

    public User()
    {
        this.name = null;
        this.birthDate = null;
        this.dateFormat = null;
        this.photo=null;
        this.id=1;


    }
    public User(int id,String name, DateTime birthDate, String dateFormat) {
        this.name = name;
        this.birthDate = birthDate;
        this.dateFormat = dateFormat;
        this.photo=null;
        this.id=id;
    }

    public User(int id,String name, DateTime birthDate, String photo, String dateFormat ) {
        this.name = name;
        this.birthDate = birthDate;
        this.dateFormat = dateFormat;
        this.photo=photo;
        this.id=id;
    }
    public DateTime getBirthDate() {
        return birthDate;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBdDateString(String bdDateString) {
        this.bdDateString = bdDateString;
    }

    public String getBdDateString() {
        return bdDateString;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setUnivers(String univers) {
        this.univers = univers;
    }

    public String getUnivers() {
        return univers;
    }
    public static String getLangs(JSONObject r) {
        try {
            String langs = "";
            JSONArray langsArray = r.getJSONObject("personal").getJSONArray("langs");
            for (int i = 0; i < langsArray.length(); i++)
                langs += langsArray.getString(i) + "; ";
            return langs;
        } catch (JSONException e) {
            return "-";
        }
    }

    public void setLangs(String langs) {
        this.langs = langs;
    }

    public String getLangs() {
        return langs;
    }

    public static User parseUserFromJSON(JSONObject r) throws JSONException {
        User result = new User();
        result.setId(Integer.parseInt(r.getString("id")));
        if (r.getString("last_name") != null && r.getString("first_name") != null)
            result.setName(r.getString("first_name") + " " + r.getString("last_name"));
        if(r.has("status"))result.setStatus(r.getString("status"));
        if(r.has("bdate")) result.setBdDateString(r.getString("bdate"));
        if(r.has("city")) result.setCity(r.getJSONObject("city").getString("title"));
        if(r.has("relation")) result.setRelationship(User.relationshipStatus[Integer.parseInt(r.getString("relation"))]);
        if(r.has("universities")) result.setUnivers(r.getJSONArray("universities").getJSONObject(0).getString("name"));
        result.setLangs(User.getLangs(r));
        return result;
    }
}
