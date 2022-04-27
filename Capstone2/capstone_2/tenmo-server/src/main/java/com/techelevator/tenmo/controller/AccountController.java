package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/accounts")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping("/u/{id}")// /u/ is unique endpoint to signal it is from a userId
    public Account getAccountByUserId(@PathVariable Long id) {

        return accountDao.getAccountByUserId(id);
    }

    @GetMapping("")
    public Account getAccountByAccountId(@RequestParam(name = "account_id") Long accountId) {

        return accountDao.getAccount(accountId);
    }

    @GetMapping("/balance")
    public double getUserBalance(Principal principal) {

        int principalId = userDao.findIdByUsername(principal.getName());
        Account account = accountDao.getAccountByUserId(Long.valueOf(principalId));
        return account.getBalance();
    }

    @PostMapping("")
    public Account createAccount(@RequestBody Account account) {

        return accountDao.createAccount(account);
    }

    @PutMapping("/{accountId}")
    public void updateAccount(@RequestBody Account account, @PathVariable Long accountId) {

        accountDao.updateAccount(account, accountId);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long userId) {

        accountDao.deleteAccount(userId);
    }
}
