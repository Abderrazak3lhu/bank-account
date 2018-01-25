package com.talan.bankaccount.bankaccount;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.TransfertDTO;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    private HttpEntity<OperationDTO> request;
    private HttpEntity<TransfertDTO> transfertRequest;

    @Before
    public void initialise() {
        restTemplate = new TestRestTemplate();
        // deposit and withdraw init
        OperationDTO operationDTO = new OperationDTO(1L, 1000D);
        request = new HttpEntity<>(operationDTO);
        // transfert init
        TransfertDTO transfertDTO = new TransfertDTO(1L,2L,1000D);
        transfertRequest = new HttpEntity<>(transfertDTO);
    }

    @Test
    public void deposit_validAccount_depositSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort("/deposit"), HttpMethod.POST, request, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //suppose that account amount is 0 and id is 1;
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(1000D);

    }
    @Test
    public void withdraw_validAccount_withdrawSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort("/withdraw"), HttpMethod.POST, request, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //suppose that account amount is 1000 and id is 1;
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.WITHDRAW);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(0D);

    }
    @Test
    public void transfert_validAccounts_transfertSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort("/transfert"), HttpMethod.POST, transfertRequest, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //suppose that main account balance is 1000 with id 0 and destination account balance with id 1 and the transfered amount is 1000;
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.TRANSFERT);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(response.getBody().getDestinationAccount().getAccountNumber()).isEqualTo(2L);
        Assertions.assertThat(response.getBody().getDestinationAccount().getBalance()).isEqualTo(1000D);

    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
