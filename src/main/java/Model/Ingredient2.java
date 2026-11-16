package Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Ingredient2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String unit;
    private BigDecimal currentAmount;
    private BigDecimal averagePrice;

    public Ingredient2() {
    }

    public Ingredient2(int id, String name, String unit, BigDecimal currentAmount, BigDecimal averagePrice) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.currentAmount = currentAmount;
        this.averagePrice = averagePrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(BigDecimal currentAmount) { this.currentAmount = currentAmount; }
    public BigDecimal getAveragePrice() { return averagePrice; }
    public void setAveragePrice(BigDecimal averagePrice) { this.averagePrice = averagePrice; }
}
