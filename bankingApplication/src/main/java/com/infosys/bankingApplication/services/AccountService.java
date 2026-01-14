package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.exceptions.AccountBlockedException;
import com.infosys.bankingApplication.exceptions.AccountNotFoundException;
import com.infosys.bankingApplication.exceptions.InsufficientFundsExceptions;
import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.models.AccountStatus;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BalanceMonitorService balanceMonitorService; // Add this

    // Inject BalanceMonitorService via constructor
    public AccountService(AccountRepository accountRepository,
                          BalanceMonitorService balanceMonitorService) {
        this.accountRepository = accountRepository;
        this.balanceMonitorService = balanceMonitorService;
    }

    // ---------------- GET ALL ----------------
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // ---------------- GET BY ID ----------------
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account ID " + id + " not found"));
    }

    // ---------------- CREATE ACCOUNT ----------------
    public void createAccount(String name, String email, double balance) {
        Account account = new Account(name, email, balance);
        accountRepository.save(account);

        // Check if new account balance is low
        balanceMonitorService.check(account);
    }

    // ---------------- DEPOSIT ----------------
    @Transactional
    public void deposit(Long id, double amount) throws AccountNotFoundException, AccountBlockedException {
        Account acc = getAccountById(id);
        if (acc.getStatus() == AccountStatus.BLOCKED)
            throw new AccountBlockedException("Account is BLOCKED");

        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);

        // Check for low balance after deposit
        balanceMonitorService.check(acc);
    }

    // ---------------- WITHDRAW ----------------
    @Transactional
    public void withdraw(Long id, double amount) throws AccountNotFoundException, AccountBlockedException, InsufficientFundsExceptions {
        Account acc = getAccountById(id);
        if (acc.getStatus() == AccountStatus.BLOCKED)
            throw new AccountBlockedException("Account is BLOCKED");
        if (acc.getBalance() < amount)
            throw new InsufficientFundsExceptions("Insufficient balance");

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);

        // Check for low balance after withdrawal
        balanceMonitorService.check(acc);
    }

    // ---------------- TRANSFER ----------------
    @Transactional
    public void transfer(Long fromId, Long toId, double amount) throws AccountNotFoundException, AccountBlockedException, InsufficientFundsExceptions {
        if (fromId.equals(toId))
            throw new IllegalArgumentException("Cannot transfer to same account");

        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);

        if (from.getStatus() == AccountStatus.BLOCKED)
            throw new AccountBlockedException("Sender account is BLOCKED");
        if (from.getBalance() < amount)
            throw new InsufficientFundsExceptions("Insufficient balance in sender account");

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        // Check for low balance on both accounts
        balanceMonitorService.check(from);
        balanceMonitorService.check(to);
    }

    // ---------------- SAVE ACCOUNT ----------------
    // For block/unblock
    public void save(Account account) {
        accountRepository.save(account);
        balanceMonitorService.check(account); // optional: check after block/unblock
    }
}
