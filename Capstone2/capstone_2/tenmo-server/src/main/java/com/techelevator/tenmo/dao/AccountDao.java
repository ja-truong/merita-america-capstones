package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;

public interface AccountDao {

    Account getAccount(Long accountId);

    Account getAccountByUserId(Long userId);

    Account createAccount(Account account);

    void updateAccount(Account account, Long accountId);

    void deleteAccount(Long userId);

    void updateSenderForSendTran(Long senderId, double amount);

    void updateReceiverForSendTran(Long receiverId, double amount);

    void updateAccountForApprovedRequest(Transfer transfer);
}
