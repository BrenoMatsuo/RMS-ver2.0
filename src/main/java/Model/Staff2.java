package Model;

import java.io.Serializable;

public class Staff2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String position;
    private int restaurantId;
    private User2 user;

    public Staff2() {
    }

    public Staff2(int id, String position, int restaurantId, User2 user) {
        this.id = id;
        this.position = position;
        this.restaurantId = restaurantId;
        this.user = user;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }
    public User2 getUser() { return user; }
    public void setUser(User2 user) { this.user = user; }
}
