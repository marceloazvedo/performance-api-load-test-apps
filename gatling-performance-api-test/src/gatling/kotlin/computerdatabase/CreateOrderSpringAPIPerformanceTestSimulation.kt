package computerdatabase

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.http.internal.HttpCheckBuilders
import java.util.UUID
import java.util.Iterator

class CreateOrderSpringAPIPerformanceTestSimulation : Simulation() {

    init {
        val http = http
            .baseUrl("http://localhost:8081")
            .header("Authorization", "c722d70b-6af6-4525-b768-6c5d5d558b31")
            .header("Content-Type", "application/json")

        val scenario =
            scenario("Load testing creating orders")
                .exec { session -> session.set("uuid", "REFE_${UUID.randomUUID().toString().uppercase()}") }
                .exec(
                    http("POST /orders")
                        .post("/orders")
                        .body(
                            StringBody(
                                """
                            |{
                            |   "reference_id": "#{uuid}",
                            |   "payment": {
                            |       "amount": 100000,
                            |       "payment_method": {
                            |           "type": "CREDIT_CARD",
                            |           "installments": 3,
                            |           "card": {
                            |               "number": "1111222233334444",
                            |               "holder": "Farmindrongo Pernelope",
                            |               "cvv": "123",
                            |               "expiration_date": "12-2031"
                            |           }
                            |       }
                            |   },
                            |   "items": [
                            |       {
                            |           "name": "lampada magica",
                            |           "quantity": 2,
                            |           "value": 50000
                            |       }
                            |   ],
                            |   "customer": {
                            |       "full_name": "Farmindrongo Pernelope",
                            |       "tax_id": "11111111111",
                            |       "address": {
                            |           "street": "rua dos bobos",
                            |           "number": "AP 101",
                            |           "state": "RN",
                            |           "city": "Santana do Seridó",
                            |           "locality": "Centro",
                            |           "zipCode": "59350000",
                            |           "complement": null
                            |       }
                            |   }
                            |}
                            """.trimMargin()
                            )
                        ).check(HttpCheckBuilders.status().`is`(200))
                )

        setUp(
            scenario.injectOpen(
                constantUsersPerSec(2.0).during(1),
            ).protocols(http)
        )
    }
}
