package Model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportBillDetail2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int lineId;
    private Ingredient2 ingredient;
    private BigDecimal quantity;
    private BigDecimal importPrice;
    private BigDecimal total;

    public ImportBillDetail2() {
    }

    public ImportBillDetail2(int id, Ingredient2 ingredient, BigDecimal quantity,
                             BigDecimal importPrice, BigDecimal total) {
        this.id = id;
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.total = total;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLineId() { return lineId; }
    public void setLineId(int lineId) { this.lineId = lineId; }
    public Ingredient2 getIngredient() { return ingredient; }
    public void setIngredient(Ingredient2 ingredient) { this.ingredient = ingredient; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getImportPrice() { return importPrice; }
    public void setImportPrice(BigDecimal importPrice) { this.importPrice = importPrice; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public void recalculateTotal() {
        total = quantity.multiply(importPrice);
    }
}
