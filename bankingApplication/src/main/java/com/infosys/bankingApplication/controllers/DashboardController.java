package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AccountRepository repository;

    public DashboardController(AccountRepository repository) {
        this.repository = repository;
    }

    // Just show the dashboard view
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("accounts", repository.findAll());
        return "dashboard";
    }
}
