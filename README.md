Проверить производительность системы обработки сообщений:

Producer (JMeter) → Kafka → Consumer (Spring Boot) → PostgreSQL

Основная задача — определить предельную пропускную способность 
системы и выявить узкие места (bottlenecks).

Запуск контейнеров: docker compose up -d

Запуск заглушки: cd kafka-consumer-app && java -jar target/kafka-consumer-app-1.0.0.jar

Запуск теста jmeter: jmeter -n -t kafka-load-test.jmx -l results.jtl -e -o report

Управление задержкой заглушки: curl -X POST "http://localhost:8080/api/delay?ms=0"