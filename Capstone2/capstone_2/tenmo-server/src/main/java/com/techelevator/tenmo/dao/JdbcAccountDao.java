package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(Long accountId) {

        Account account = null;

        String sql = "SELECT * FROM accounts WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }

        return account;
    }

    @Override
    public Account getAccountByUserId(Long userId) {

        return getAccount(getAccountIdByUserId(userId));
    }

    @Override
    public Account createAccount(Account account) {

        Account newAccount = new Account();
        newAccount.setUserId(account.getUserId());
        newAccount.setBalance(account.getBalance());
        String insertSql = "INSERT INTO accounts " +
                "(user_id, balance) " +
                "VALUES (?, ?) " +
                "RETURNING account_id;";
        Long accountId = jdbcTemplate.queryForObject(insertSql, Long.class, newAccount.getUserId(), newAccount.getBalance());

        if (accountId != 0) {
            newAccount.setAccountId(accountId);
        }

        return newAccount;
    }

    @Override
    public void updateAccount(Account account, Long accountId) {

        String updateSql = "UPDATE accounts " +
                "SET user_id = ?, " +
                "balance = ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(updateSql, account.getUserId(), account.getBalance(), accountId);
    }

    @Override
    public void deleteAccount(Long userId) {

        Long accountId = getAccountIdByUserId(userId);

        if (accountId != 0) {
            String deleteSql = "DELETE FROM accounts " +
                    "WHERE account_id = ?;";
            jdbcTemplate.update(deleteSql, accountId);
        }
    }

    @Override
    public void updateSenderForSendTran(Long senderId, double amount) {

        Account account = getAccountByUserId(senderId);
        account.deductBalance(amount);
        updateAccount(account, account.getAccountId());
    }

    @Override
    public void updateReceiverForSendTran(Long receiverId, double amount) {

        Account account = getAccountByUserId(receiverId);
        account.increaseBalance(amount);
        updateAccount(account, account.getAccountId());
    }

    @Override
    public void updateAccountForApprovedRequest(Transfer transfer) {

        //update From Account
        Account account = getAccount(transfer.getAccountFrom());
        account.increaseBalance(transfer.getAmount());
        updateAccount(account, account.getAccountId());
        //update To Account
        account = getAccount(transfer.getAccountTo());
        account.deductBalance(transfer.getAmount());
        updateAccount(account, account.getAccountId());
    }

    private Long getAccountIdByUserId(Long userId) {

        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
