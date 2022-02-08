# Дипломная работа. Поисковая система

Наша цель - разработать класс поискового движка, который способен быстро находить указанное слово среди pdf-файлов, причём ранжировать результаты по количеству вхождений. Также у нас будет сервер, который обслуживает входящие запросы с помощью этого движка. Но обо всём по-порядку.

## Заготовка проекта
Сделайте Fork этого репозитория в свой гитхаб-аккаунт через кнопку слева вверху:
![image](https://user-images.githubusercontent.com/53707586/152979745-ef37549f-9207-46b6-a160-33d16f3e5360.png)

Склонируйте форкнутый репозиторий и откройте его в идее как мавен-проект.

Помимо стандартных папок и файлов, в папке вашего проекта будет папка `pdfs` - в ней находятся .pdf-файлы, по которым будет искать поисковый движок.
![image](https://user-images.githubusercontent.com/53707586/152980485-675392c1-4d98-4e22-abdc-7a85678f53a7.png)

В проекте есть заготвки кода:

| Класс      | Описание |
| ----------- | ----------- |
| `Main`      | Сейчас в нём находится заготовка использования поискового движка. После его реализации, содержимое `main` нужно будет заменить на запуск сервераЮ обслуживающего поисковые запросы       |
| `PageEntry`   | Класс, описывающий один элемент результата одного поиска. Он состоит из имени пдф-файла, номера страницы и количества раз, которое встретилось это слово на ней        |
| `SearchEngine`   | Интерфейс, описывающий поисковый движок. Всё что должен уметь делать поисковый движок, это на запрос из слова отвечать списком элементов результата ответа        |
| `BooleanSearchEngine`   | Реализация поискового движка, которую вам предстоит написать. Слово `Boolean` пришло из теории информационного поиска, тк наш движок будет искать в тексте ровно то слово, которое было указано, без использования синонимов и прочих приёмов нечётного поиска        |

## PDF
Для работы с пдф мы будем использовать библиотеку `com.itextpdf:itext7-core:7.1.15`, которая уже подключена в ваш `pom.xml`:
```xml
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext7-core</artifactId>
            <version>7.1.15</version>
            <type>pom</type>
        </dependency>
```

Основные инструменты из этой библиотеки будут выглядеть следующим образом:

Чтобы создать объект пдф-документа, нужно указать объект `File` этого документа следующим классам: `var doc = new PdfDocument(new PdfReader(pdf));`.
Чтобы получить объект одной страницы документа, нужно вызвать `doc.getPage(номерСтраницы)`. Полистайте методы `doc` чтобы найти способ узнать количество страниц в документе.
Чтобы получить текст со страницы, используйте `var text = PdfTextExtractor.getTextFromPage(page);`.
Чтобы разбить текст на слова (а они в этих документах разделены могут быть не только пробелами), используйте `var words = text.split("\\P{IsAlphabetic}+");`.

## Индексация и поиск
Здесь речь пойдёт о реализации класса `BooleanSearchEngine`. Мы хотим чтобы поиск работал быстро, поэтому предпочтём сканирование всех пдф-ок в конструкторе класса с сохранением информации для каждого слова, чтобы метод `search` отрабатывал быстро, по сути возвращая уже посчитанный в конструкторе для слова ответ. Т.е. в конструкторе для каждого слова нужно сохранить готовый ответ `List<PageEntry>` (подумайте, во что из Collections Framework это можео сделать). Такое предварительное сканирование файлов по которым мы будем искать называется _индексацией_.

В итоге, вам нужно реализовать логику индексации в конструкторе. Сканируя каждый пдф-файл вы перебираете его страницы, для каждой страницы извлекаете из неё слова и подсчитываете их количество. После чего, для каждого уникального слова создаёте объект `PageEntry` и сохраняете в поле (а что за поле - предлагалось подумать выше). Учтите также, что мы хотим регистронезависимый поиск, т.е. по слову "бизнес" должны учитываться и "бизнес", и "Бизнес" в документах.

Также, списки ответов для каждого слова должны быть отсортированы в порядке уменьшения поля `count`. Для этого предлагается классу `PageEntry` сразу реализовывать интерфейс `Comparable`.

После того как вы это реализуете, можете протестировать работу вашего движка в `Main`. Протестируйте на тех словах, на которых хотите. Вот пример работы на слове "бизнес":
```
[PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=12, count=6}, PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=4, count=3}, PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=5, count=3}, PageEntry{pdf=1. DevOps_MLops.pdf, page=5, count=2}, PageEntry{pdf=Что такое блокчейн.pdf, page=1, count=2}, PageEntry{pdf=Что такое блокчейн.pdf, page=3, count=2}, PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=2, count=1}, PageEntry{pdf=Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf, page=11, count=1}, PageEntry{pdf=1. DevOps_MLops.pdf, page=3, count=1}, PageEntry{pdf=1. DevOps_MLops.pdf, page=4, count=1}, PageEntry{pdf=Что такое блокчейн.pdf, page=2, count=1}, PageEntry{pdf=Что такое блокчейн.pdf, page=4, count=1}, PageEntry{pdf=Что такое блокчейн.pdf, page=5, count=1}, PageEntry{pdf=Что такое блокчейн.pdf, page=7, count=1}, PageEntry{pdf=Что такое блокчейн.pdf, page=9, count=1}, PageEntry{pdf=Продвижение игр.pdf, page=7, count=1}, PageEntry{pdf=Как управлять рисками IT-проекта.pdf, page=2, count=1}]
```

## Сервер
После завершения работы над движком, вам следует написать сервер по примеру того, как вы уже делали в предыдущих заданиях. В `main` должен запускаться сервер, слушающий порт `8989` и на GET-запросы вида `/{word}` отвечать результатом вызова метода `search(word)`, но в виде JSON-текста (библиотеку для работы с JSON подключите к `pom.xml`).

Пример ответа на запрос `/бизнес`:
```json
[
  {
    "pdfName": "Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf",
    "page": 12,
    "count": 6
  },
  {
    "pdfName": "Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf",
    "page": 4,
    "count": 3
  },
  {
    "pdfName": "Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf",
    "page": 5,
    "count": 3
  },
  {
    "pdfName": "1. DevOps_MLops.pdf",
    "page": 5,
    "count": 2
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 1,
    "count": 2
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 3,
    "count": 2
  },
  {
    "pdfName": "Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf",
    "page": 2,
    "count": 1
  },
  {
    "pdfName": "Этапы оценки проекта_ понятия, методы и полезные инструменты.pdf",
    "page": 11,
    "count": 1
  },
  {
    "pdfName": "1. DevOps_MLops.pdf",
    "page": 3,
    "count": 1
  },
  {
    "pdfName": "1. DevOps_MLops.pdf",
    "page": 4,
    "count": 1
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 2,
    "count": 1
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 4,
    "count": 1
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 5,
    "count": 1
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 7,
    "count": 1
  },
  {
    "pdfName": "Что такое блокчейн.pdf",
    "page": 9,
    "count": 1
  },
  {
    "pdfName": "Продвижение игр.pdf",
    "page": 7,
    "count": 1
  },
  {
    "pdfName": "Как управлять рисками IT-проекта.pdf",
    "page": 2,
    "count": 1
  }
]
```

## Как сдавать
Тесты писать не нужно. Прикрепите ссылку на ваш публичный гит-репо с решением.
