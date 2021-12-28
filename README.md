# Generator-Ewolucyjny
Program ma na celu symulować środowisko, w którym znajdują się zwierzęta oraz rośliny.

### Kluczowe reguły środowiska
- Codziennie zwierzę traci pewną ilość energii, gdy spadnie ona do zera, zwierzę umiera.
- Codziennie zwierzę wykonuje losowy ruch, może być to również obrót.
- Zjedzenie rośliny zwiększa poziom energii zwierzęcia.
- Dwa zwierzęta mogą mieć dziecko, jeżeli się spotkają i mają odpowiednio wysokie poziomy energii.
- Symulacja kończy się gdy umrze ostatnie zwierzę.

## Demo
![Demo](https://github.com/proman3419/Generator-Ewolucyjny/blob/master/demo.gif)

## Użyte technologie
- Java 16
- JavaFX 17
- Gradle 7.1

## Uwagi
- Nie zaimplementowano następujących funkcjonalności z punktu 5.
> 5. Po zatrzymaniu programu można:
>   * wskazać pojedyncze zwierzę, co powoduje wyświetlenie jego genomu,
>   * wskazać pojedyncze zwierzę, w celu śledzenia jego historii (śledzenie rozpoczyna się w danym momencie, więc nie
>     uwzględnia wcześniejszych dzieci, ani potomków; wartości mają być aktualizowane na bieżąco):
>     * określenia liczby wszystkich dzieci,
>     * określenia liczby wszystkich potomków,
>     * określenia epoki, w której zmarło,
>   * wskazać wszystkie zwierzęta z dominującym genomem,
- Preferowane są mapy o większej wysokości niż szerokości ze względu na układ elementów.
- Preferowana wielkość okna to ``1920x1080``, aplikacja nie jest w pełni responsywna.
