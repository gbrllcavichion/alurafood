package br.com.alurafood.pagamentos.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.boot.context.event.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;


@Configuration
public class PagamentoAMQPConfiguration {

    @Bean
    public Queue criaFila(){
        // return QueueBuilder.nonDurable("pagamento.concluido").build();
        return new Queue("pagamento.concluido", false);
    }

    @Bean
    public RabbitAdmin criaRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

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

}
