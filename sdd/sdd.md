# Описание дизайна программного обеспечения (SDD)

Для системы «Копилка целей» (GoalJar)

Версия 1.0 

Подготовил(а): IDSsssss

---

## Оглавление

* [1. Введение](#1-введение)
  * [1.1 Назначение документа](#11-назначение-документа)
  * [1.2 Область применения](#12-область-применения)
  * [1.3 Определения, акронимы и сокращения](#13-определения-акронимы-и-сокращения)
  * [1.4 Ссылки](#14-ссылки)
* [2. Обзор дизайна](#2-обзор-дизайна)
  * [2.1 Задачи стейкхолдеров](#21-задачи-стейкхолдеров)
  * [2.2 Выбранные точки зрения (9 Viewpoints)](#22-выбранные-точки-зрения-9-viewpoints)
* [3. Проектные представления (Design Views)](#3-проектные-представления-design-views)
  * [3.1 VIEW-CTX: Контекстное представление](#31-view-ctx-контекстное-представление)
  * [3.2 VIEW-COMP: Композиция подсистем](#32-view-comp-композиция-подсистем)
  * [3.3 VIEW-LOG: Логическая структура классов](#33-view-log-логическая-структура-классов)
  * [3.4 VIEW-INF: Информационная модель данных](#34-view-inf-информационная-модель-данных)
  * [3.5 VIEW-INTF: Спецификация экранов](#35-view-intf-спецификация-экранов)
  * [3.6 VIEW-SEQ: Взаимодействие в рантайме](#36-view-seq-взаимодействие-в-рантайме)
  * [3.7 VIEW-ALG: Алгоритм расчета прогноза](#37-view-alg-алгоритм-расчета-прогноза)
  * [3.8 VIEW-STAT: Динамика состояний цели](#38-view-stat-динамика-состояний-цели)
  * [3.9 VIEW-PAT: Архитектурные паттерны](#39-view-pat-архитектурные-паттерны)
* [4. Архитектурные решения (ADR Summary)](#4-архитектурные-решения-adr-summary)
* [5. Приложения](#5-приложения)

---

## 1. Введение

### 1.1 Назначение документа
Настоящий документ Software Design Description (SDD) представляет архитектурное и детальное проектное описание программного продукта «Копилка целей» (GoalJar). Документ разработан в соответствии со стандартом IEEE 1016-2009 и определяет **КАК** реализуются требования, зафиксированные в согласованном документе SRS v0.3.
Целевая аудитория: разработчики Desktop (Java/JavaFX), инженеры по качеству (QA), системные администраторы и Заказчик проекта.

### 1.2 Область применения
Дизайн охватывает полную реализацию версии 1.0 системы GoalJar:
- **Приложение:** настольное приложение на Java 17 + JavaFX 21 для Windows/macOS/Linux.
- **Хранилище:** локальная СУБД SQLite с миграциями схемы.
- **Сеть:** отсутствует (полностью автономная работа).

### 1.3 Определения, акронимы и сокращения

| Термин | Определение |
|--------|-------------|
| SDD | Software Design Description — описание проектирования ПО по стандарту IEEE 1016 |
| ADR | Architecture Decision Record — протокол архитектурного решения по шаблону MADR |
| DTO | Data Transfer Object — объект передачи данных между слоями |
| UI | User Interface — пользовательский интерфейс |
| SQLite | Встраиваемая реляционная СУБД |

### 1.4 Ссылки
1. IEEE Std 1016-2009: IEEE Standard for Information Technology — Systems Design — Software Design Descriptions.
2. ISO/IEC/IEEE 42010:2011: Systems and software engineering — Architecture description.
3. SRS: «Копилка целей», Спецификация требований к ПО v0.3 (`/srs/srs.md`).
4. ADR: Протоколы архитектурных решений (`/srs/adr/`).

---

## 2. Обзор дизайна

### 2.1 Задачи стейкхолдеров

| Стейкхолдер | Ключевые интересы и задачи | Адресующие проектные представления (Views) |
|-------------|----------------------------|---------------------------------------------|
| **Заказчик (Product Owner)** | Точность прогноза с учётом исключённых месяцев, мотивация пользователя (вехи) | VIEW-CTX, VIEW-ALG, VIEW-STAT |
| **Пользователи** | Быстрый запуск, локальная безопасность, наглядный прогресс | VIEW-INTF, VIEW-SEQ, VIEW-RES |
| **Команда разработки** | Модульность, разделение ответственности, четкие контракты | VIEW-COMP, VIEW-LOG, VIEW-PAT |

### 2.2 Выбранные точки зрения (9 Viewpoints)
1. **Context** — внешние границы (локальное приложение).
2. **Composition** — слои и модули.
3. **Logical** — доменные классы и сервисы.
4. **Information** — ERD и схема SQLite.
5. **Interface** — спецификация экранов.
6. **Interaction** — диаграммы последовательности (внесение пополнения).
7. **Algorithm** — логика вычисления прогноза.
8. **State Dynamics** — статус цели (Активна, Достигнута, Просрочена).
9. **Patterns** — слоистая архитектура, Repository, DTO.

---

## 3. Проектные представления (Design Views)

### 3.1 VIEW-CTX: Контекстное представление
- **ID:** VIEW-CTX-001
- **Адресуемые требования SRS:** REQ-INT-001, REQ-SEC-001, CON-04.
- **Описание:** Пользователь взаимодействует с настольным приложением. Все данные хранятся локально. Сетевые соединения отсутствуют.

Пользователь ➔ Приложение GoalJar (UI + Services + Repositories) ➔ Локальная БД SQLite

---

### 3.2 VIEW-COMP: Композиция подсистем
- **ID:** VIEW-COMP-001
- **Адресуемые требования SRS:** REQ-MAINT-001, CON-06.
- **Описание:** Модульная организация кодовой базы.

* **finance-tracker (Parent POM)**
  * goaljar-core (Бизнес-логика, Сервисы, DTO)
  * goaljar-data (Репозитории, SQLite, Миграции)
  * goaljar-ui (JavaFX, FXML, CSS)

---

### 3.3 VIEW-LOG: Логическая структура классов и слоев
- **ID:** VIEW-LOG-001
- **Адресуемые требования SRS:** REQ-MAINT-001.
- **Описание:** Приложение строго разделено на три слоя:
  1. **UI Layer:** Экраны (`MainScreen`, `GoalScreen`, `GoalForm`).
  2. **Service Layer:** Бизнес-логика (`GoalService`, `DepositService`, `ProgressCalculator`).
  3. **Repository Layer:** Работа с БД (`GoalRepository`, `DepositRepository`).

---

### 3.4 VIEW-INF: Информационная модель данных (SQLite)
- **ID:** VIEW-INF-001
- **Адресуемые требования SRS:** REQ-REL-001, REQ-REL-003.
- **Описание:** Реляционная структура данных.

```sql
CREATE TABLE goal (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    target_amount NUMERIC NOT NULL,
    target_date DATE NOT NULL,
    category TEXT,
    archived BOOLEAN DEFAULT 0
);

CREATE TABLE deposit (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    goal_id INTEGER NOT NULL,
    amount NUMERIC NOT NULL,
    deposit_date DATE NOT NULL,
    comment TEXT,
    FOREIGN KEY(goal_id) REFERENCES goal(id) ON DELETE CASCADE
);

CREATE TABLE excluded_month (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    goal_id INTEGER NOT NULL,
    month TEXT NOT NULL, -- YYYY-MM
    FOREIGN KEY(goal_id) REFERENCES goal(id) ON DELETE CASCADE,
    UNIQUE(goal_id, month)
);

### 3.5 VIEW-INTF: Спецификация экранов
- **ID:** VIEW-INTF-001
- **Адресуемые требования SRS:** REQ-INT-002, REQ-INT-003, REQ-FUNC-006.
- **Описание:** Основные экраны.
  - Главный экран (Сводка): Список карточек целей, суммарный прогресс, кнопка «+ Новая цель».
  - Экран цели: Шкала с вехами, накоплено/остаток, кнопка «Пополнить», история.
  - Диалог цели: Поля ввода, выбор исключённых месяцев.

---

### 3.6 VIEW-SEQ: Взаимодействие в рантайме
- **ID:** VIEW-SEQ-001
- **Адресуемые требования SRS:** REQ-FUNC-003, REQ-FUNC-004.
- **Описание:** Сценарий внесения пополнения.

1. Пользователь нажимает «Пополнить» и вводит сумму.
2. UI вызывает `DepositService.addDeposit()`.
3. Сервис валидирует сумму (>0) и дату.
4. `DepositRepository` выполняет INSERT в БД в транзакции.
5. `ProgressCalculator` пересчитывает прогресс и ежемесячный взнос.
6. UI обновляет шкалу и историю.

---

### 3.7 VIEW-ALG: Алгоритм расчета прогноза
- **ID:** VIEW-ALG-001
- **Адресуемые требования SRS:** REQ-FUNC-004, REQ-FUNC-013.
- **Псевдокод алгоритма:**

```java
public BigDecimal calculateMonthlyPlan(Goal goal, List<Deposit> deposits) {
    BigDecimal accumulated = deposits.stream().map(Deposit::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal remaining = goal.getTargetAmount().subtract(accumulated);
    if (remaining.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ZERO;

    long totalMonths = ChronoUnit.MONTHS.between(YearMonth.now(), YearMonth.from(goal.getTargetDate()));
    long excluded = goal.getExcludedMonths().stream()
        .filter(m -> !m.isBefore(YearMonth.now()) && !m.isAfter(YearMonth.from(goal.getTargetDate())))
        .count();
    long activeMonths = totalMonths - excluded;

    if (activeMonths <= 0) {
        return null; // Показать предупреждение "Нет активных месяцев"
    }

    return remaining.divide(BigDecimal.valueOf(activeMonths), 0, RoundingMode.CEILING);
}

---

### 3.8 VIEW-STAT: Динамика состояний цели
- **ID:** VIEW-STAT-001
- **Адресуемые требования SRS:** REQ-FUNC-005.
- **Описание:** Конечный автомат статуса цели.

- `ACTIVE` (Активна): Текущая дата < целевой, накоплено < цели.
- `COMPLETED` (Достигнута): Накоплено >= цели.
- `OVERDUE` (Просрочена): Текущая дата > целевой, накоплено < цели.

---

### 3.9 VIEW-PAT: Архитектурные и проектные паттерны
- **ID:** VIEW-PAT-001
- **Адресуемые требования SRS:** REQ-MAINT-001.
- **Описание:** 
  - **Layered Architecture:** UI -> Domain -> Data.
  - **DTO Pattern:** Передача данных между UI и Service.
  - **Repository Pattern:** Абстракция над SQLite.
  - **MVVM:** В UI (привязка свойств JavaFX к моделям).

---

## 4. Архитектурные решения (ADR Summary)

В проекте зафиксированы архитектурные решения по шаблону MADR:

| Номер | Заголовок решения | Статус | Адресуемые требования SRS |
|---|---|---|---|
| **ADR-001** | Выбор графического фреймворка JavaFX для настольного клиента | proposed | REQ-INT-001, REQ-INT-004, REQ-INT-005 |
| **ADR-002** | Использование локальной СУБД SQLite для хранения данных | proposed | REQ-INT-006, REQ-REL-001, REQ-SEC-001 |
| **ADR-003** | Модель учёта исключённых месяцев при расчёте прогноза | proposed | REQ-FUNC-004, REQ-FUNC-013 |

Полные тексты протоколов решений приведены в каталоге `/srs/adr/`.

---

## 5. Приложения

### Матрица трассируемости «Требования SRS -> Проектные представления SDD»

| ID требования SRS | Наименование требования | Реализующее проектное представление SDD |
|-------------------|-------------------------|------------------------------------------|
| REQ-INT-001 | Структура главного окна | VIEW-CTX-001, VIEW-COMP-001 |
| REQ-INT-006 | Интерфейс локального хранилища | VIEW-INF-001, VIEW-LOG-001 |
| REQ-FUNC-004 | Расчёт прогресса и прогноза | VIEW-ALG-001, VIEW-SEQ-001 |
| REQ-FUNC-013 | Исключение месяцев | VIEW-INF-001, VIEW-ALG-001 |
| REQ-SEC-001 | Локальное хранение без сети | VIEW-CTX-001 |
