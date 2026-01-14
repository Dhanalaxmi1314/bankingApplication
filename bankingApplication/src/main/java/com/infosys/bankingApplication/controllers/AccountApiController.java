package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.services.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts") // API prefix to separate from UI
public class AccountApiController {

    private final AccountService accountService;

    public AccountApiController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ---------- GET ALL ----------
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // ---------- GET BY ID ----------
    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    // ---------- CREATE ----------
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        accountService.createAccount(account.getName(), account.getEmail(), account.getBalance());
        return account;
    }

    // ---------- DEPOSIT ----------
    @PostMapping("/{id}/deposit")
    public Account deposit(@PathVariable Long id, @RequestParam double amount) {
        accountService.deposit(id, amount);
        return accountService.getAccountById(id);
    }

    // ---------- WITHDRAW ----------
    @PostMapping("/{id}/withdraw")
    public Account withdraw(@PathVariable Long id, @RequestParam double amount) {
        accountService.withdraw(id, amount);
        return accountService.getAccountById(id);
    }

    // ---------- TRANSFER ----------
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount) {
        accountService.transfer(fromId, toId, amount);
        return "Transfer successful";
    }
}
