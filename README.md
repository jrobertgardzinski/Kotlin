# Instrukcja uruchomienia projektu

Przed uruchomieniem projektu należy z poziomu jego głównego katalogu odpalić CLI, a następnie uruchomić polecenie

`docker-compose -f docker-compose.yml up`

Następnie wystarczy uruchomić Intellij, wczytać projekt i uruchomić aplikację lub jej testy jednostkowe z poziomu IDE. 

Poniżej kilka przykładowy URL zaimplementowanej usługi:

http://localhost:8080/transactions?account_type=1,2,3&customer_id=1

Parametry wywołań GET przyjmują argumenty zgodne z poleceniem tj. poodzielane przecinkami id lub string ALL. W przypadku niepodania parametru, przypisany zostanie domyślnie string ALL.
