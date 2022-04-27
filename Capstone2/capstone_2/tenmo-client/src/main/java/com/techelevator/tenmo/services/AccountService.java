package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;
    private String accountsPath = "accounts/";

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Account getAccount(Long userId) throws AccountServiceException {
        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "accounts/u/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (ResourceAccessException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (RestClientException e) {
            throw new AccountServiceException(e.getMessage());
        }

        return account;
    }

    public Account getAccountByAccountId(Long accountId) throws AccountServiceException {

        Account account = null;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "accounts" + "?account_id=" + accountId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (ResourceAccessException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (RestClientException e) {
            throw new AccountServiceException(e.getMessage());
        }

        return account;
    }

    public double getBalance() throws AccountServiceException {

        try {
            ResponseEntity<Double> response = restTemplate.exchange(baseUrl + accountsPath + "balance/", HttpMethod.GET, makeAuthEntity(), double.class);
            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (ResourceAccessException e) {
            throw new AccountServiceException(e.getMessage());
        } catch (RestClientException e) {
            throw new AccountServiceException(e.getMessage());
        }
    }

    public boolean updateAccount(@RequestBody Account account) throws AccountServiceException {

        boolean isSuccess = false;
        HttpEntity<Account> entity = makeAccountEntity(account);
        try {
            restTemplate.put(baseUrl + accountsPath + account.getAccountId(), entity);
            isSuccess = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            throw new AccountServiceException(e.getMessage());
        }

        return isSuccess;
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
