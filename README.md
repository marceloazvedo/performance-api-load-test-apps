# performance-api-load-test-apps

Aplicações para realizar teste de carga com o objetivo de comparar duas
aplicações, uma reativa e a outra tradicional escritas em Spring.

É necessário subir o container na raiz dessa pasta:
```shell
docker compose up -d
```

Esse docker vai subir os seguintes serviços:
- PostgreSQL: Banco de Dados;
- PGAdmin: Interface visual para realizar interação com o banco;
- Mockserver: Servidor para criação de mocks com base em requests REST.

## Configurando mocks
Caso o mock que sobe nesse docker-compose.yml não inicialize com sua
configuração realize as requests abaixo para inicia-ló:

### Cria mock de Gateway de pagamento
```shell
curl --request PUT \
  --url http://localhost:1080/mockserver/expectation \
  --header 'Content-Type: application/json' \
  --data '{
    "httpRequest": {
      "method": "POST",
      "path": "/gateway/pay"
    },
    "httpResponse": {
      "body": {
        "code": "uuid",
        "message": "SUCCESS"
      },
      "delay": {
        "timeUnit": "SECONDS",
        "value": 1
      }
    }
  }'
```

### Cria mock de validação de token de Seller
```shell
curl --request PUT \
  --url http://localhost:1080/mockserver/expectation \
  --header 'Content-Type: application/json' \
  --data '{
    "httpRequest": {
      "method": "GET",
      "path": "/sellers/authorization"
    },
    "httpResponse": {
      "body": "OK",
      "delay": {
        "timeUnit": "MILLISECONDS",
        "value": 200
      }
    }
  }'
```

### Cria mock de analise de risco
```shell
curl --request PUT \
  --url http://localhost:1080/mockserver/expectation \
  --header 'Content-Type: application/json' \
  --data '{
    "httpRequest": {
      "method": "POST",
      "path": "/risk-analysis/validate"
    },
    "httpResponse": {
      "body": {
        "recommendation": "APPROVE"
      },
      "delay": {
        "timeUnit": "MILLISECONDS",
        "value": 500
      }
    }
  }'
```

Verificando mocks:
### Gateway de pagamento
```shell
curl --request POST \
  --url http://localhost:1080/risk-analysis/validate \
  --header 'User-Agent: insomnia/8.6.1'
```
### Analise de risco
```shell
curl --request POST \
  --url http://localhost:1080/risk-analysis/validate \
  --header 'User-Agent: insomnia/8.6.1'
```
### Validação de token de seller
```shell
curl --request GET \
  --url http://localhost:1080/sellers/authorization \
  --header 'User-Agent: insomnia/8.6.1'
```

## Testando as aplicações
Realize a request abaixo para verificar se a app está funcionando corretamente:
```shell
curl --request POST \
  --url http://localhost:8081/orders \
  --header 'Authorization: 4c5fd74e-4b5e-47ce-87ef-3e328395c030' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/8.6.1' \
  --data '{
	"reference_id": "2b7694a8-4cd1-424a-abb9-481a03162fb7",
	"payment": {
		"amount": 100000,
		"payment_method": {
			"type": "CREDIT_CARD",
			"installments": 3,
			"card": {
				"number": "1111222233334444",
				"holder": "Farmindrongo Pernelope",
				"cvv": "123",
				"expiration_date": "12-2031"
			}
		}
	},
	"items": [
		{
			"name": "lampada magica",
			"quantity": 2,
			"value": 50000
		},
		{
			"name": "camisa de força",
			"quantity": 7,
			"value": 55000
		}
	],
	"customer": {
		"full_name": "Farmindrongo Pernelope",
		"tax_id": "11111111111",
		"address": {
			"street": "rua dos bobos",
			"number": "AP 101",
			"state": "RN",
			"city": "Santana do Seridó",
			"locality": "Centro",
			"zipCode": "59350000",
			"complement": null
		}
	}
}'
```