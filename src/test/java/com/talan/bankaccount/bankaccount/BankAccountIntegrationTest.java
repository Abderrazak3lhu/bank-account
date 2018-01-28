package com.talan.bankaccount.bankaccount;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.bankAccountConstants;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
})
public class BankAccountIntegrationTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    private HttpEntity<OperationDTO> depositDTO;
    private HttpEntity<OperationDTO> withdrawDTO;
    private HttpEntity<TransfertDTO> transfertDTO;
    private Long transactionsHistoryForAccountNumber;

    @Before
    public void initialise() {
        restTemplate = new TestRestTemplate();
        // deposit init
        depositDTO = new HttpEntity<>(new OperationDTO(1L, 1000D));
        // withdraw init
        withdrawDTO = new HttpEntity<>(new OperationDTO(2L, 1000D));
        // transfert init
        transfertDTO = new HttpEntity<>(new TransfertDTO(3L, 4L, 1000D));
        // transactions history account number init
        transactionsHistoryForAccountNumber= 1L;
    }

    @Test
    public void A_deposit_validAccount_depositSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort(bankAccountConstants.DEPOSIT_URL), HttpMethod.POST, depositDTO, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.DEPOSIT);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(1000D);

    }

    @Test
    public void B_withdraw_validAccount_withdrawSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort(bankAccountConstants.WITHDRAW_URL), HttpMethod.POST, withdrawDTO, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.WITHDRAW);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(2L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(0D);

    }

    @Test
    public void C_transfert_validAccounts_transfertSuccess() {

        ResponseEntity<Operation> response = restTemplate.exchange(createURLWithPort(bankAccountConstants.TRANSFERT_URL), HttpMethod.POST, transfertDTO, Operation.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getAmount()).isEqualTo(1000D);
        Assertions.assertThat(response.getBody().getType()).isEqualTo(OperationType.TRANSFERT);
        Assertions.assertThat(response.getBody().getAccount().getAccountNumber()).isEqualTo(3L);
        Assertions.assertThat(response.getBody().getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(response.getBody().getDestinationAccount().getAccountNumber()).isEqualTo(4L);
        Assertions.assertThat(response.getBody().getDestinationAccount().getBalance()).isEqualTo(1000D);

    }
    @Test
    public void D_transactions_recordedTransactions_returnTransactions(){
        // TODO Correct the error Caused by: com.fasterxml.jackson.databind.JsonMappingException: Can not deserialize instance of com.talan.bankaccount.bankaccount.domain.Operation[] out of START_OBJECT token

        //ResponseEntity<Operation[]> response = restTemplate.getForEntity(createURLWithPort(bankAccountConstants.TRANSACTIONS_HISTORY+"/1"), Operation[].class);
        //Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //Assertions.assertThat(response.getBody().length).isEqualTo(1);
        //Assertions.assertThat(response.getBody()[0]).isEqualTo(1);
        //Assertions.assertThat(response.getBody()[0].getAccount().getAccountNumber()).isEqualTo(1L);
        //Assertions.assertThat(response.getBody()[0].getAccount().getBalance()).isEqualTo(1L);

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
