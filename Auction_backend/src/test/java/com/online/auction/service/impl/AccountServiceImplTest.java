package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setUserId(1);
        account.setFunds(130.0f);
    }

    @Test
    void GetAccountBalanceSuccessTest() throws ServiceException {
        when(accountRepository.findByUserId(anyInt())).thenReturn(account);

        double balance = accountService.getAccountBalance(1);

        assertEquals(130.0f, balance);
    }

    @Test
    void GetAccountBalanceAccountNotFoundTest(){
        when(accountRepository.findByUserId(anyInt())).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accountService.getAccountBalance(1);
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }

    @Test
    void addFundsSuccessTest() throws ServiceException {
        when(accountRepository.findByUserId(anyInt())).thenReturn(account);

        accountService.addFunds(1, 50.0f);

        verify(accountRepository, times(1)).save(account);
        assertEquals(180.0f, account.getFunds());
    }

    @Test
    void addFundsAccountNotFoundTest() {
        when(accountRepository.findByUserId(anyInt())).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            accountService.addFunds(1, 50.0f);
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }
}
