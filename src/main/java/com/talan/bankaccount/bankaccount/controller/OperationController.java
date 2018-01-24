package com.talan.bankaccount.bankaccount.controller;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OperationController {

    @Autowired
    OperationService operationService;
    @PostMapping(value = "/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation deposit(@RequestBody OperationDTO operationDTO) {
        throw new UnsupportedOperationException();
    }

}
