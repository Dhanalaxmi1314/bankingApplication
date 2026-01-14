package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.models.AccountStatus;
import com.infosys.bankingApplication.services.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ---------- DASHBOARD ----------
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }

    // ---------- CREATE ACCOUNT ----------
    @GetMapping("/create")
    public String createAccountForm(Model model) {
        model.addAttribute("account", new Account());
        return "create-account";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute Account account, Model model) {
        accountService.createAccount(account.getName(), account.getEmail(), account.getBalance());
        model.addAttribute("success", "Account created successfully!");
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }

    // ---------- DEPOSIT ----------
    @PostMapping("/deposit")
    public String deposit(@RequestParam Long id, @RequestParam double amount, Model model) {
        try {
            accountService.deposit(id, amount);
            model.addAttribute("success", "Deposited ₹" + amount + " to account ID " + id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }

    // ---------- WITHDRAW ----------
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam Long id, @RequestParam double amount, Model model) {
        try {
            accountService.withdraw(id, amount);
            model.addAttribute("success", "Withdrew ₹" + amount + " from account ID " + id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }

    // ---------- TRANSFER ----------
    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam double amount, Model model) {
        try {
            accountService.transfer(fromId, toId, amount);
            model.addAttribute("success", "Transferred ₹" + amount + " from account ID " + fromId + " to account ID " + toId);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }

    // ---------- BLOCK / UNBLOCK ----------
    @PostMapping("/{id}/block")
    public String toggleBlock(@PathVariable Long id, Model model) {
        var acc = accountService.getAccountById(id);
        acc.setStatus(acc.getStatus() == AccountStatus.ACTIVE ? AccountStatus.BLOCKED : AccountStatus.ACTIVE);
        accountService.save(acc);
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "dashboard";
    }
}
