package com.devestudo.projeto_financas.controller;

import com.devestudo.projeto_financas.entities.User;
import com.devestudo.projeto_financas.entities.dtos.response.CategorySummaryDto;
import com.devestudo.projeto_financas.services.ExpenseSummaryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/charts")
public class ExpenseControllerSummary {

    private final ExpenseSummaryService service;

    public ExpenseControllerSummary(ExpenseSummaryService service) {
        this.service = service;
    }

    @GetMapping("/expenses")
    public List<CategorySummaryDto> summary(@AuthenticationPrincipal User user,
                                            @RequestParam(required = false) LocalDate startDate,
                                            @RequestParam(required = false) LocalDate endDate
    ) {
        return service.getSummaryByUser(user.getId(), startDate, endDate);
    }


}
