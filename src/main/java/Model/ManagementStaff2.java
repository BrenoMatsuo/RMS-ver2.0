package Model;

public class ManagementStaff2 extends Staff2 {
    public ManagementStaff2() {
        setPosition("manager");
    }

    public ManagementStaff2(int id, int restaurantId, User2 user) {
        super(id, "manager", restaurantId, user);
    }
}
