# Финансовый трекер

REST API для учёта доходов и расходов пользователей.

## Оглавление

- [О проекте](#о-проекте)
- [Основные возможности](#основные-возможности)
- [Технологии](#технологии)
- [Требования](#требования)
- [Установка и запуск](#установка-и-запуск)
- [Тестирование](#тестирование)
- [API Endpoints](#api-endpoints)
- [Авторы](#авторы)

## О проекте

Финансовый трекер позволяет пользователю управлять своими транзакциями и предоставляет REST API для:

- Создания, чтения, обновления и удаления транзакций и пользователей (CRUD операции)
- Построения отчётов (общий баланс, расходы по категориям, месячный отчёт)

## Основные возможности

- **Управление транзакциями** - полный CRUD для транзакций
- **Управление пользователями** - полный CRUD для пользователей
- **Отчетность** - получение баланса, расходов по категориям, месячный отчет
- **Валидация** - защита от невалидных данных
- **Логирование** - логирование всех операций

## Технологии

- Java 21 - основной язык,
- Spring Boot 3.5.0 - фреймворк для построения приложения,
- Spring Data JPA - фреймворк для работы с базой данных,
- Gradle - сборщик приложения
- PostgreSQL - база данных
- Liquibase - миграции базы данных,
- Swagger - документирование,
- Lombok - уменьшение шаблонного кода,
- SLF4J - логирование
- JUnit - тестирование

## Требования

Проект собран с использованием следующих инструментов:

- **Java 21** или выше
- **Gradle 8.10** или выше
- **PostgreSQL 14** или выше

## Установка и запуск

### 1. Клонирование репозитория

```bash
git clone https://github.com/TanyaBrikman/financetracker.git
cd financetracker
```

### 2. Соберите и запустите проект с помощью Gradle
```bash 
./gradlew clean build
```

### 3. Настройка базы данных PostgreSQL
- Запустите через Docker:
```bash
docker-compose up
```
Приложение будет доступно по адресу: `http://localhost:8080`

Swagger UI доступен по адресу: `http://localhost:8080/swagger-ui/index.html`

## Тестирование

Запуск всех тестов:

```bash
./gradlew test
```

Запуск конкретного теста:
- JUnit тесты:
```bash
./gradlew test --tests *TransactionServiceTest
```
- Интеграционные тесты:
```bash
./gradlew test --tests *TransactionControllerTest
```

## API Endpoints

### Пользователи

| Метод  | URL             | Описание                    |
|--------|-----------------|-----------------------------|
| POST   | /api/users      | Создать пользователя        |
| GET    | /api/users      | Получить всех пользователей |
| GET    | /api/users/{id} | Получить пользователя по id |            
| DELETE | /api/users/{id} | Удалить пользователя по id  |

### Транзакции

| Метод  | URL                       | Описание                        |
|--------|---------------------------|---------------------------------|
| POST   | /api/transactions         | Создать транзакцию              |
| GET    | /api/transactions         | Получить все транзакции         |
| GET    | /api/transactions/{id}    | Получить транзакцию по id       |
| GET    | /api/transactions/filters | Получить транзакции по фильтрам |
| PUT    | /api/transactions/{id}    | Обновить транзакцию             |
| DELETE | /api/transactions/{id}    | Удалить транзакцию по id        |

### Отчёты

| Метод | URL                              | Описание                    |
|-------|----------------------------------|-----------------------------|
| GET   | /api/report/balance              | Получить баланс             |
| GET   | /api/report/expenses-by-category | Получить всех пользователей |
| GET   | /api/report/monthly-summary      | Получить пользователя по id |

### Примеры запросов:

- Создание пользователя:

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Jon",
    "age": 30,
    "email": "Jon@example.com"
  }'
```

- Создание транзакции:

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
  "amount": 3000,
  "type": "EXPENSE",
  "categoryType": "FOOD",
  "description": "Ужин",
  "transactionDate": "2026-06-20",
  "userId": 1
}'
```

- Фильтрация транзакций:

```bash
curl -X GET "http://localhost:8080/api/transactions/filters?startDate=2025-01-01&endDate=2026-06-20&category=FOOD&type=EXPENSE"
```

- Получение баланса:

```bash
curl -X GET "http://localhost:8080/api/report/balance?userId=1&startDate=2026-06-20&endDate=2026-07-07"
```

### Авторы

- author: Татьяна Брикман
- email: tanyabrikman@mail.ru
- github: https://github.com/TanyaBrikman