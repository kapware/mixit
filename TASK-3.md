# Notification Service
Zaimplementuj, stosując test driven development, serwis notyfikacji `NotificationService`.
Serwis ma za zadanie przygotować żądanie (http json request) który zostanie wysłany do końcówki 
zadanej właściwością (w pliku `application-*.properties`) o nazwie `mail.server.api`.
Request ma zostać wysłany po odebraniu zdarzenia `PlayerCreatedEvent` z kolejki `player.q`.

Wskazówka: do testów wykorzystaj wiremock.