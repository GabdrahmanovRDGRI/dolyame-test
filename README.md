# Баг с DolyameObserver после смерти процесса приложения

<img width="872" alt="image" src="https://github.com/user-attachments/assets/7467d8c1-1d42-405c-9539-9f82e5d7ce02">

Бага заключается в следующем:
1. Создается DolyameObserver, привязывается к лайфсайклу фрагмента/активити, происходит вызов onCreate.
2. observersCount становится 1, значит getKey() вернет "dolyameResult:1"
3. Происходит вызов метода DolyameSdk.openDolyame(...) с DolyameObserver
4. Далее идет стандартное флоу, результат приходит в DolyameObserver
5. Перезаходим в экран, чтобы создался новый DolyameObserver
6. observersCount становится 2, значит getKey() вернет "dolyameResult:2"
7. Происходит вызов метода DolyameSdk.openDolyame(...) с DolyameObserver
8. Имитируем уничтожение процесса приложения системой - можно с помощью отзыва пермишена
9. Возвращаемся в приложение, восстанавливается библиотечный экран Долями
10. Завершаем флоу и возвращаемся на экран вызова метода
11. На экране происходит пересоздание DolyameObserver, но getKey() вернет "dolyameResult:1", т.к. observersCount после пересоздания процесса сбросится до 0
12. Из-за ключа "dolyameResult:1" мы не получим результат, т.к. результат будет возвращен по ключу "dolyameResult:2"

Т.е. проблема возникает из-за сброса observersCount при пересоздании процесса, что приводит к несоответствию ключей результата

[Скринкаст бага](https://github.com/GabdrahmanovRDGRI/dolyame-test/blob/master/dolyame_observers_count_bug.webm) 
