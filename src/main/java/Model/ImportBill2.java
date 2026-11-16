package Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ImportBill2 implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date importDate;
    private BigDecimal totalAmount;
    private String payMethod;
    private String state;
    private String note;
    private Supplier2 supplier;
    private Staff2 staff;
    private List<ImportBillDetail2> details = new ArrayList<ImportBillDetail2>();

    public ImportBill2() {
    }

    public ImportBill2(int id, Date importDate, BigDecimal totalAmount, String payMethod, String state,
                       String note, Supplier2 supplier, Staff2 staff, List<ImportBillDetail2> details) {
        this.id = id;
        this.importDate = importDate;
        this.totalAmount = totalAmount;
        this.payMethod = payMethod;
        this.state = state;
        this.note = note;
        this.supplier = supplier;
        this.staff = staff;
        this.details = details == null ? new ArrayList<ImportBillDetail2>() : details;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getImportDate() { return importDate; }
    public void setImportDate(Date importDate) { this.importDate = importDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String payMethod) { this.payMethod = payMethod; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Supplier2 getSupplier() { return supplier; }
    public void setSupplier(Supplier2 supplier) { this.supplier = supplier; }
    public Staff2 getStaff() { return staff; }
    public void setStaff(Staff2 staff) { this.staff = staff; }
    public List<ImportBillDetail2> getDetails() { return details; }
    public void setDetails(List<ImportBillDetail2> details) {
        this.details = details == null ? new ArrayList<ImportBillDetail2>() : details;
    }
}
