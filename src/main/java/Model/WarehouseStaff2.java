package Model;

public class WarehouseStaff2 extends Staff2 {
    public WarehouseStaff2() {
        setPosition("warehouse");
    }

    public WarehouseStaff2(int id, int restaurantId, User2 user) {
        super(id, "warehouse", restaurantId, user);
    }
}
