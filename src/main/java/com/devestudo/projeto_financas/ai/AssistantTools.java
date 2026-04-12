package com.devestudo.projeto_financas.ai;

import com.devestudo.projeto_financas.entities.dtos.response.ExpenseTotalResponseDto;
import com.devestudo.projeto_financas.enums.PaymentMethod;
import com.devestudo.projeto_financas.exception.BusinessException;
import com.devestudo.projeto_financas.services.ExpenseRegistryService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AssistantTools {

    private final ExpenseRegistryService expenseRegistryService;

    public AssistantTools(ExpenseRegistryService expenseRegistryService) {
        this.expenseRegistryService = expenseRegistryService;
    }

    @Tool("Busca gastos do usuário por forma de pagamento e período (máx 30 dias)")
    public ExpenseTotalResponseDto getExpensesByPayment(String paymentMethod,
                                                        String dateStart,
                                                        String dateEnd){


        //Validar e converter a forma de pagamento para SWITH CASE
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new BusinessException("Informe a forma de pagamento (crédito ou débito)");
        }

        String normalized = paymentMethod.trim().toUpperCase(); //trim() → remove espaços antes/depois

        PaymentMethod method = switch (normalized){ //converter String → Enum PaymentMethod

            case "CREDITO", "CRÉDITO" -> PaymentMethod.CREDITO;
            case "DEBITO", "DÉBITO" -> PaymentMethod.DEBITO;
            default -> throw new BusinessException(
                    "Forma de pagamento inválida. Use CRÉDITO ou DÉBITO");
         };

        //Converte datas
        LocalDate start = null;
        LocalDate end = null;

        try {
            if (dateStart != null && !dateStart.isBlank()) {
                start = LocalDate.parse(dateStart); //Pega essa String (dateStart) e transforma em um LocalDate para guardar dentro de start
            }

            if (dateEnd != null && !dateEnd.isBlank()) {
                end = LocalDate.parse(dateEnd); //Pega essa String (endStart) e transforma em um LocalDate para guardar dentro de start
            }

        } catch (Exception e) {
            throw new BusinessException("Formato de data inválido. Use yyyy-MM-dd");
        }

        return expenseRegistryService.findExpensesByFilter(method, start, end);
    }
}
