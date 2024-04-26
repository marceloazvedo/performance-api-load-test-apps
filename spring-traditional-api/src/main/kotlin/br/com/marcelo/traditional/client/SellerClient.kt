package br.com.marcelo.traditional.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "sellerClient", url = "\${clients.seller}")
interface SellerClient {
    @GetMapping("/sellers/authorization")
    fun validateToken(
        @RequestParam("authorization-token") authorizationToken: String,
    ): String
}
