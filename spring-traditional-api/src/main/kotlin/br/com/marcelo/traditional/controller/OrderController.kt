package br.com.marcelo.traditional.controller

import br.com.marcelo.traditional.context.OrderContext
import br.com.marcelo.traditional.dto.OrderDTO
import br.com.marcelo.traditional.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
) {
    @PostMapping
    fun create(
        @RequestBody createOrder: OrderDTO,
        @RequestHeader("Authorization") accessToken: String?,
    ): ResponseEntity<OrderDTO> =
        ResponseEntity.ok(
            orderService.create(
                OrderContext(
                    sellerAccessToken = accessToken,
                    order = createOrder,
                ),
            ),
        )
}
