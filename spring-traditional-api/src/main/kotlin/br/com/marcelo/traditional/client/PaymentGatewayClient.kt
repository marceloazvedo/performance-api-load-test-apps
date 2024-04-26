package br.com.marcelo.traditional.client

import br.com.marcelo.traditional.dto.PaymentMethodDTO
import br.com.marcelo.traditional.dto.PaymentResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "paymentGatewayClient", url = "\${clients.payment-gateway}")
interface PaymentGatewayClient {
    @PostMapping("/gateway/pay")
    fun pay(paymentMethodDTO: PaymentMethodDTO): PaymentResponse?
}
