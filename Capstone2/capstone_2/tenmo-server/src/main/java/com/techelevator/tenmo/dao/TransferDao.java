package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(Long transferId);

    List<Transfer> listAllRelatedTransfers(Long userId);

    List<Transfer> listRelatedPendingTransfers(Long userId);

    Transfer createTransfer(Transfer transfer);

    void updateTransfer(Transfer transfer);

    void deleteTransfer(Long transferId);

    Transfer createSendTran(TransferDTO transferDTO, Long userId);

    int rejectRequest(Long transferId);

    Transfer approveRequest(Long transferId);
}
