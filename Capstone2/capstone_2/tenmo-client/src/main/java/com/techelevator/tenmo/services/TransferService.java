package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransactionData;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;
    private final String TRANSFER_PATH = "transfers/";
    private final String SEND_PATH = "send/";
    private final String USER_PATH = "users/";
    private final String PENDING_PATH = "pending/";
    private final String REJECT_PATH = "reject/";
    private final String APPROVE_PATH = "approve/";

    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer[] getTransfersByUserId(Long userId) throws TransferServiceException {

        Transfer[] transfers = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + TRANSFER_PATH + USER_PATH + userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            throw new TransferServiceException(e.getMessage());
        }

        return transfers;
    }

    public Transfer[] getPendingRequests(Long userId) throws TransferServiceException {

        Transfer[] transfers = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + TRANSFER_PATH + PENDING_PATH + userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (ResourceAccessException | RestClientResponseException e) {
            throw new TransferServiceException(e.getMessage());
        }

        return transfers;
    }

    public Transfer createTransfer(Transfer transfer) throws TransferServiceException {

        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        Transfer newTransfer = null;
        try {
            newTransfer = restTemplate.postForObject(baseUrl + TRANSFER_PATH, entity, Transfer.class);
        } catch (RestClientResponseException e) {
            throw new TransferServiceException(e.getMessage());
        } catch (ResourceAccessException e) {
            throw new TransferServiceException(e.getMessage());
        } catch (RestClientException e) {
            throw new TransferServiceException(e.getMessage());
        }

        return newTransfer;
    }

    public boolean processSendTransaction(Long userId, TransactionData transactionData) throws TransferServiceException {

        boolean isSuccess = false;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<TransactionData> entity = new HttpEntity<>(transactionData, headers);
            restTemplate.put(baseUrl + TRANSFER_PATH + SEND_PATH + userId, entity);
            isSuccess = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new TransferServiceException(e.getMessage());
        }
        return isSuccess;
    }

    public void rejectRequestTransaction(Long transferId) throws TransferServiceException {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Long> entity = new HttpEntity<>(transferId, headers);
            restTemplate.put(baseUrl + TRANSFER_PATH + REJECT_PATH + transferId, entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new TransferServiceException(e.getMessage());
        }
    }

    public void approveRequestTransaction(Long transferId) throws TransferServiceException {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Long> entity = new HttpEntity<>(transferId, headers);
            restTemplate.put(baseUrl + TRANSFER_PATH + APPROVE_PATH + transferId, entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new TransferServiceException(e.getMessage());
        }
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
