kafka-console-producer \
  --bootstrap-server kafka:9092 \
  --topic players \
  --property 'parse.key=true' \
  --property 'key.separator=|' < players.json

kafka-console-producer \
  --bootstrap-server kafka:9092 \
  --topic games \
  --property 'parse.key=true' \
  --property 'key.separator=|' < games.json

kafka-console-producer \
  --bootstrap-server kafka:9092 \
  --topic score-events < score-events.json