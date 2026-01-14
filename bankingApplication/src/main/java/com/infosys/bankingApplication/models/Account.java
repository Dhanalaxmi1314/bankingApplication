package com.infosys.bankingApplication.models;

import jakarta.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private double balance;

    private boolean lowBalanceAlertSent;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public Account() {
        this.status = AccountStatus.ACTIVE;
        this.lowBalanceAlertSent = false;
    }

    public Account(String name, String email, double balance) {
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.lowBalanceAlertSent = false;
        this.status = AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean isLowBalanceAlertSent() { return lowBalanceAlertSent; }
    public void setLowBalanceAlertSent(boolean lowBalanceAlertSent) { this.lowBalanceAlertSent = lowBalanceAlertSent; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    // Helper method
    public boolean isBlocked() {
        return this.status == AccountStatus.BLOCKED;
    }
}
