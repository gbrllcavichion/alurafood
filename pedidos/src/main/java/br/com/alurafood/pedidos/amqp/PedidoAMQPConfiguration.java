package br.com.alurafood.pedidos.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.boot.context.event.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;

import javax.ws.rs.core.*;

@Configuration
public class PedidoAMQPConfiguration {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }


    @Bean
    public Queue filaDetalhesPedidos() {
        return QueueBuilder
                .nonDurable("pagamentos.detalhes-pedido")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("pagamentos.ex")
                .build();
    }

    @Bean
    public Binding bindPagamentoPedido(FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(filaDetalhesPedidos())
                .to(fanoutExchange);
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }
}
