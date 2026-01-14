package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BalanceMonitorService {

    private static final double THRESHOLD = 500;

    private final EmailService emailService;
    private final AccountRepository repository;

    public BalanceMonitorService(EmailService emailService,
                                 AccountRepository repository) {
        this.emailService = emailService;
        this.repository = repository;
    }

    public void check(Account account) {
        if (account.getBalance() < THRESHOLD && !account.isLowBalanceAlertSent()) {
            emailService.sendLowBalanceAlert(
                    account.getEmail(),
                    account.getName(),
                    account.getBalance()
            );

            account.setLowBalanceAlertSent(true);
            repository.save(account);
        }

        if (account.getBalance() >= THRESHOLD && account.isLowBalanceAlertSent()) {
            account.setLowBalanceAlertSent(false);
            repository.save(account);
        }
    }
}
