package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Transaction {
    private Integer id;
    private User canteen;
    private LocalDateTime timestamp;
    private Integer totalPrice;
    private Integer totalPaid;
    private Integer changeBack;
    private ArrayList<TransactionDetail> transactionDetail;

    public Transaction(Integer id, User canteen, LocalDateTime timestamp, Integer totalPrice, Integer totalPaid, Integer changeBack, ArrayList<TransactionDetail> transactionDetail) {
        this.id = id;
        this.canteen = canteen;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.totalPaid = totalPaid;
        this.changeBack = changeBack;
        this.transactionDetail = transactionDetail;
    }

    public Integer getId() {
        return id;
    }

    public StringProperty getFormattedId() {
        return new SimpleStringProperty(String.format("#%06d", id));
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCanteen() {
        return canteen;
    }

    public void setCanteen(User canteen) {
        this.canteen = canteen;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<TransactionDetail> getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(ArrayList<TransactionDetail> transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Integer totalPaid) {
        this.totalPaid = totalPaid;
    }

    public Integer getChangeBack() {
        return changeBack;
    }

    public void setChangeBack(Integer changeBack) {
        this.changeBack = changeBack;
    }

    public Integer calculateTotalPrice() {
        int totalPrice = 0;
        for (TransactionDetail detail : transactionDetail) {
            totalPrice += detail.getPrice();
        }
        return totalPrice;
    }
}
