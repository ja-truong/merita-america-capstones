package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping("/{transferId}")
    public Transfer listTransferByTransferId(@PathVariable Long transferId) {

        return transferDao.getTransfer(transferId);
    }

    @GetMapping("/users/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable Long userId) {

        return transferDao.listAllRelatedTransfers(userId);
    }

    @GetMapping("/pending/{userId}")
    public List<Transfer> getPendingTransfers(@PathVariable Long userId) {

        return transferDao.listRelatedPendingTransfers(userId);
    }

    @PostMapping("")
    public Transfer createNewTransfer(@RequestBody Transfer transfer) {

        return transferDao.createTransfer(transfer);
    }

    @PutMapping("/")
    public void updateTransfer(@RequestBody Transfer transfer) {

        transferDao.updateTransfer(transfer);
    }

    @DeleteMapping("/{transferId}")
    public void deleteTransfer(@PathVariable Long transferId) {

        transferDao.deleteTransfer(transferId);
    }

    @RequestMapping(value = "/send/{userId}", method = {RequestMethod.PUT, RequestMethod.PUT, RequestMethod.POST})
    public Transfer createSendTransfer(@PathVariable Long userId, @RequestBody TransferDTO transferDTO, Principal principal) {

        int principalId = userDao.findIdByUsername(principal.getName());
        Account account = accountDao.getAccountByUserId(Long.valueOf(principalId));

        if (account.getUserId().equals(userId) && account.getBalance() >= transferDTO.getAmount()) {
            //update account balances for sender and receiver
            accountDao.updateSenderForSendTran(userId, transferDTO.getAmount());
            accountDao.updateReceiverForSendTran(transferDTO.getReceiverId(), transferDTO.getAmount());
            //create new transfer record
            return transferDao.createSendTran(transferDTO, userId);
        }

        return null;
    }

    @PutMapping("/reject/{transferId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String rejectRequestTransfer(@PathVariable Long transferId, Principal principal) {

        int principalId = userDao.findIdByUsername(principal.getName());
        Account principalAccount = accountDao.getAccountByUserId(Long.valueOf(principalId));

        Long accountTo = transferDao.getTransfer(transferId).getAccountTo();
        Account toAccount = accountDao.getAccount(accountTo);

        if (principalAccount.getUserId().equals(toAccount.getUserId())) {
            int x = transferDao.rejectRequest(transferId);
            if (x > 0) {
                return "*** Successful transaction. Transfer record updated. ***";
            }
        }

        return "*** Unsuccessful transaction. Invalid transfer Id. ***";
    }

    @RequestMapping(value = "/approve/{transferId}", method = {RequestMethod.PUT, RequestMethod.POST})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String approveRequestTransfer(@PathVariable Long transferId, Principal principal) {

        int principalId = userDao.findIdByUsername(principal.getName());
        Account principalAccount = accountDao.getAccountByUserId(Long.valueOf(principalId));

        Long accountTo = transferDao.getTransfer(transferId).getAccountTo();
        Account toAccount = accountDao.getAccount(accountTo);

        if (principalAccount.getUserId().equals(toAccount.getUserId())) {
            //update transfer record to Approved
            Transfer transfer = transferDao.approveRequest(transferId);
            if (transfer != null) {
                accountDao.updateAccountForApprovedRequest(transfer);
                return "*** Successful transaction. Transfer and Account records updated. ***";
            }
        }

        return "*** Unsuccessful transaction. ***";
    }

}
