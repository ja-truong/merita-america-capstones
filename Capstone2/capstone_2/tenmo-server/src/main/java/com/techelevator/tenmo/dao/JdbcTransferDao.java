package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer getTransfer(Long transferId) {

        Transfer transfer = null;
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, " +
                "t.account_from, t.account_to, t.amount, tt.transfer_type_desc, " +
                "ts.transfer_status_desc FROM transfers t " +
                "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowSet.next()) {
            transfer = mapRowToTransfer(rowSet);
        }

        return transfer;
    }

    @Override
    public List<Transfer> listAllRelatedTransfers(Long userId) {

        List<Transfer> transfers = new ArrayList<>();
        SqlRowSet rowSet;

        Long accountId = getAccountId(userId);
        if (accountId != 0) {
            String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, " +
                    "t.account_from, t.account_to, t.amount, tt.transfer_type_desc, " +
                    "ts.transfer_status_desc FROM transfers t " +
                    "JOIN transfer_types tt ON t.transfer_type_id = tt.transfer_type_id " +
                    "JOIN transfer_statuses ts ON t.transfer_status_id = ts.transfer_status_id " +
                    "WHERE account_from = ? or account_to = ? " +
                    "ORDER BY t.transfer_id;";
            rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while (rowSet.next()) {
                transfers.add(mapRowToTransfer(rowSet));
            }
        }

        return transfers;
    }

    @Override
    public List<Transfer> listRelatedPendingTransfers(Long userId) {

        List<Transfer> transfers = new ArrayList<>();
        SqlRowSet rowSet;

        Long accountId = getAccountId(userId);
        if (accountId != 0) {
            String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, " +
                    "t.account_from, t.account_to, t.amount, tt.transfer_type_desc, " +
                    "ts.transfer_status_desc FROM transfers t " +
                    "JOIN transfer_types tt ON tt.transfer_type_desc = 'Request' " +
                    "JOIN transfer_statuses ts ON ts.transfer_status_desc = 'Pending' " +
                    "WHERE tt.transfer_type_id = t.transfer_type_id " +
                    "AND ts.transfer_status_id = t.transfer_status_id " +
                    "AND t.account_to = ? " +
                    "ORDER BY t.transfer_id;";
            rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            while (rowSet.next()) {
                transfers.add(mapRowToTransfer(rowSet));
            }
        }

        return transfers;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {

        Transfer newTransfer = new Transfer();
        newTransfer.setTransferTypeDesc(transfer.getTransferTypeDesc());
        newTransfer.setTransferStatusDesc(transfer.getTransferStatusDesc());
        newTransfer.setAccountFrom(transfer.getAccountFrom());
        newTransfer.setAccountTo(transfer.getAccountTo());
        newTransfer.setAmount(transfer.getAmount());
        String insertSql = "INSERT INTO transfers " +
                "(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES ((SELECT transfer_type_id FROM transfer_types tt WHERE tt.transfer_type_desc = ?), " +
                "(SELECT transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = ?), ?, ?, ?) " +
                "RETURNING transfer_id;";
        Long transferId = jdbcTemplate.queryForObject(insertSql, Long.class, newTransfer.getTransferTypeDesc(), newTransfer.getTransferStatusDesc(), newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());

        if (transferId != 0) {
            newTransfer.setTransferId(transferId);
            //get created Transfer to populate type and status
            Transfer tempTransfer = getTransfer(transferId);
            newTransfer.setTransferTypeId(tempTransfer.getTransferTypeId());
            newTransfer.setTransferStatusId(tempTransfer.getTransferStatusId());
        }

        return newTransfer;
    }

    @Override
    public void updateTransfer(Transfer transfer) {

        String updateSql = "UPDATE transfers " +
                "SET transfer_type_id = ?, " +
                "transfer_status_id = ?, " +
                "account_from = ?, " +
                "account_to = ?, " +
                "amount = ? " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(updateSql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferId());
    }

    @Override
    public void deleteTransfer(Long transferId) {

        String deleteSql = "DELETE FROM transfers " +
                "WHERE transfer_id = ?;";
        jdbcTemplate.update(deleteSql, transferId);
    }

    private Long getAccountId(Long userId) {

        String sql = "SELECT account_id FROM accounts " +
                "WHERE user_id = ?;";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }

    @Override
    public Transfer createSendTran(TransferDTO transferDTO, Long userId) {

        Transfer transfer = new Transfer();
        //populate new transfer record
        transfer.setTransferTypeDesc("Send");
        transfer.setTransferStatusDesc("Approved");
        transfer.setAccountFrom(getAccountId(userId));
        transfer.setAccountTo(getAccountId(transferDTO.getReceiverId()));
        transfer.setAmount(transferDTO.getAmount());
        return createTransfer(transfer);
    }

    @Override
    public int rejectRequest(Long transferId) {

        int x = 0;
        Transfer transfer = getTransfer(transferId);
        if (transfer != null) {
            String updateSql = "UPDATE transfers " +
                    "SET transfer_status_id = " +
                    "(SELECT ts.transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = 'Rejected') " +
                    "WHERE transfer_id = ? " +
                    "AND transfer_type_id = " +
                    "(SELECT tt.transfer_type_id FROM transfer_types tt WHERE tt.transfer_type_desc = 'Request') " +
                    "AND transfer_status_id = " +
                    "(SELECT ts.transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = 'Pending');";
            x = jdbcTemplate.update(updateSql, transfer.getTransferId());
        }

        return x;
    }

    @Override
    public Transfer approveRequest(Long transferId) {

        int x = 0;
        Transfer transfer = getTransfer(transferId);
        if (transfer != null) {
            String updateSql = "UPDATE transfers " +
                    "SET transfer_status_id = " +
                    "(SELECT ts.transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = 'Approved') " +
                    "WHERE transfer_id = ? " +
                    "AND transfer_type_id = " +
                    "(SELECT tt.transfer_type_id FROM transfer_types tt WHERE tt.transfer_type_desc = 'Request') " +
                    "AND transfer_status_id = " +
                    "(SELECT ts.transfer_status_id FROM transfer_statuses ts WHERE ts.transfer_status_desc = 'Pending');";
            x = jdbcTemplate.update(updateSql, transfer.getTransferId());
        }

        if (x == 1) {
            return transfer;
        } else {
            return null;
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rs.getLong("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        transfer.setTransferTypeDesc(rs.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(rs.getString("transfer_status_desc"));

        return transfer;
    }
}
