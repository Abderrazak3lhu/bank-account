package com.talan.bankaccount.bankaccount;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
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

    @Before
    public void initialise() {
        restTemplate = new TestRestTemplate();
        OperationDTO operationDTO = new OperationDTO(1L, 1000D);
        request = new HttpEntity<>(operationDTO);
    }

    @Test
    public void depositMoney_validAccount_depositSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort("/deposit"), HttpMethod.POST, request, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //suppose that account amount is 0 and id is 1;
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(1000D);

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
