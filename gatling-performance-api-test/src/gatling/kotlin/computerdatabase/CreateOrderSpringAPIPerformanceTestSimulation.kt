package computerdatabase

import io.gatling.javaapi.core.*
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.http.HttpDsl.*
import io.gatling.javaapi.http.internal.HttpCheckBuilders
import java.util.UUID

class CreateOrderSpringAPIPerformanceTestSimulation : Simulation() {

    companion object {
        var FINAL_LOAD = 750
        var CONSTANT_DURATION = 30L
    }

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
                            {
                              "reference_id": "#{uuid}",
                              "payment": {
                                "amount": 32750,
                                "payment_method": {
                                  "type": "CREDIT_CARD",
                                  "installments": 5,
                                  "card": {
                                    "number": "4539511619543487",
                                    "holder": "Alcides Trombone",
                                    "cvv": "482",
                                    "expiration_date": "09-2029"
                                  }
                                }
                              },
                              "items": [
                                {
                                  "name": "Caneca Mágica do Dragão",
                                  "quantity": 1,
                                  "value": 32750
                                }
                              ],
                              "customer": {
                                "full_name": "Alcides Trombone",
                                "tax_id": "03658924713",
                                "address": {
                                  "street": "Avenida das Palmeiras",
                                  "number": "SN 204",
                                  "state": "SP",
                                  "city": "Campinas",
                                  "locality": "Jardim das Estrelas",
                                  "zipCode": "13040000",
                                  "complement": "Próximo à padaria"
                                }
                              }
                            }
                            """.trimMargin()
                            )
                        ).check(HttpCheckBuilders.status().`is`(200))
                )

        setUp(
            scenario.injectOpen(
                rampUsers(FINAL_LOAD).during(CONSTANT_DURATION),
                constantUsersPerSec(FINAL_LOAD.toDouble()).during(CONSTANT_DURATION),
                rampUsers(0).during(CONSTANT_DURATION),
            ).protocols(http)
        )
    }
}
