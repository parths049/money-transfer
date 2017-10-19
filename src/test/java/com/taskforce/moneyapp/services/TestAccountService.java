package com.taskforce.moneyapp.services;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.moneytransfer.model.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/demo.sql
 * <p>
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',100.0000,'USD'); --ID =1
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',200.0000,'USD'); --ID =2
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test2',500.0000,'EUR'); --ID =3
 * INSERT INTO Account (UserName,Balance,CurrencyCode) VALUES ('test1',500.0000,'EUR'); --ID =4
 */

public class TestAccountService extends TestService {


    /*
    TC A1 Positive Category = AccountService
    Scenario: test get user account by user name
              return 200 OK
     */
    @Test
    public void testGetAccountByUserName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertTrue(statusCode == 200);
        //check the content
        String jsonString = EntityUtils.toString(response.getEntity());
        Account account = mapper.readValue(jsonString, Account.class);
        assertTrue(account.getUserName().equals("test2"));
    }

    /*
    TC A2 Positive Category = AccountService
    Scenario: test get all user accounts
              return 200 OK
    */
    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        //check the content
        String jsonString = EntityUtils.toString(response.getEntity());
        Account[] accounts = mapper.readValue(jsonString, Account[].class);
        assertTrue(accounts.length > 0);
    }

    /*
    TC A3 Positive Category = AccountService
    Scenario: test get account balance given account ID
              return 200 OK
    */
    @Test
    public void testGetAccountBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1/balance").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        //check the content, assert user test2 have balance 100
        String balance = EntityUtils.toString(response.getEntity());
        BigDecimal res = new BigDecimal(balance).setScale(4, RoundingMode.HALF_EVEN);
        BigDecimal db = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
        assertTrue(res.equals(db));
    }

    /*
    TC A4 Positive Category = AccountService
    Scenario: test create new user account
              return 200 OK
    */
    @Test
    public void testCreateAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        BigDecimal balance = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
        Account acc = new Account("test2", balance, "CNY");
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        Account aAfterCreation = mapper.readValue(jsonString, Account.class);
        assertTrue(aAfterCreation.getUserName().equals("test2"));
        assertTrue(aAfterCreation.getCurrencyCode().equals("CNY"));
    }

    /*
    TC A5 Negative Category = AccountService
    Scenario: test create user account already existed.
              return 500 INTERNAL SERVER ERROR
    */
    @Test
    public void testCreateExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        Account acc = new Account("test1", new BigDecimal(0), "USD");
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 500);

    }

    /*
    TC A6 Positive Category = AccountService
    Scenario: delete valid user account
              return 200 OK
    */
    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/3").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
    }


    /*
    TC A7 Negative Category = AccountService
    Scenario: test delete non-existent account. return 404 NOT FOUND
              return 404 NOT FOUND
    */
    @Test
    public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/300").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 404);
    }


}
