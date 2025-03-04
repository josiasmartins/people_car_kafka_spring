# Producer kafka with spring

Alterar a compatibilidade do schema via rest. None: ignore a compatibilidade
```bash
curl -X PUT http://localhost:8081/config -H "Content-Type: application/json" -d '{"compatibility": "NONE"}'
```