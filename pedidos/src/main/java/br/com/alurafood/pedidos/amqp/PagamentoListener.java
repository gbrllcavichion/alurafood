package br.com.alurafood.pedidos.amqp;

import br.com.alurafood.pedidos.dto.*;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.*;

@Component
public class PagamentoListener {

    @RabbitListener(queues = "pagamento.detales.pedido")
    public void recebeMensagem(PagamentoDto pagamento) {
        String mensagem = """
                Dados do pagamento: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s
                """.formatted(pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getValor(),
                pagamento.getStatus());

        System.out.println("Recebi a mensagem " + mensagem);
    }
}
