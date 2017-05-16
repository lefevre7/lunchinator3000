package com.lunchinator3000;

/**
 * Created by Jeremy L on 5/11/2017.
 */
/*abstract static class AbstractRestaurant {
    private String name;
    private int id;

    public AbstractRestaurant() {

    }
    public AbstractRestaurant(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}*/

public interface AbstractRestaurant extends Restaurant{
    String name = "";
    Integer id = null;


    public String getName();

    public void setName(String name);

    public int getId();

    public void setId(int id);
}
