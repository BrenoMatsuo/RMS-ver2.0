package Model;

import java.math.BigDecimal;

public class Dish2 {
    private int id;
    private String name;
    private BigDecimal price;
    private String picture;
    private String state;
    private String description;
    private Category2 category;

    public Dish2() {
    }

    public Dish2(int id, String name, BigDecimal price, String picture, String state,
                 String description, Category2 category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.picture = picture;
        this.state = state;
        this.description = description;
        this.category = category;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category2 getCategory() { return category; }
    public void setCategory(Category2 category) { this.category = category; }
}
