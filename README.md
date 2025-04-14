
# 🛠️ Proyecto Spring Boot + Flyway + PostgreSQL

## 📦 Dependencias iniciales

El proyecto se ha creado con las siguientes dependencias:

- Spring JPA  
- PostgreSQL Driver  
- Flyway Migration  
- Lombok  
- Java + Maven

---

## 🐘 PostgreSQL con Docker

Se ha creado una base de datos PostgreSQL con Docker.  
**Usuario:** `postgres`  
**Contraseña:** `1234`  

```yaml
version: '3.1'

services:
  db:
    image: postgres
    ports:
      - "5432:5432"
    restart: always
    environment:
      POSTGRES_PASSWORD: 1234
```

---

## 🧩 Estructura del proyecto

### 📁 `domain` (o `models`, `entities`)

Se ha creado una clase `Book`:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String isbn;
    private String title;
}
```

Se convierte en entidad JPA:

```java
@Entity
@Table(name = "books")
public class Book {
    @Id
    private String isbn;
    private String title;
}
```

---

### 📁 `repositories`

Se crea la interfaz `BookRepository`:

```java
@Repository
public interface BookRepository extends CrudRepository<Book, String> {
}
```

---

## 🚀 Probar funcionalidad en `FlywayApplication.java`

Se implementa `CommandLineRunner` para probar la conexión:

```java
@SpringBootApplication
public class FlywayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FlywayApplication.class, args);
    }

    private final BookRepository bookRepository;

    public FlywayApplication(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.bookRepository.findAll().forEach(book -> {
            System.out.println(book.toString());
        });
    }
}
```

---

## ⚙️ Configuración `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=1234
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=none
```

> ❗ Se establece `ddl-auto=none` para evitar que Hibernate modifique la estructura de la base de datos automáticamente.  
Flyway se encargará de las migraciones.

---

## 🐦 Flyway: Migraciones de base de datos

Ubicación de scripts: `resources/db/migration`

### 🔄 ¿Cómo funciona?

- Busca scripts SQL (o Java) en `db/migration`
- Se ejecutan en orden (`V1__`, `V2__`, etc)
- Guarda un historial en la tabla `flyway_schema_history`
- Spring Boot ejecuta los scripts automáticamente al iniciar

---

### 🧾 Primera migración: `V1__initialising_schema.sql`

```sql
CREATE TABLE books (
    "isbn"  text NOT NULL,
    "title" text,
    CONSTRAINT books_pkey PRIMARY KEY ("isbn")
);
```

> ✅ Al ejecutar la app, se crea la tabla `books` y el historial queda registrado en `flyway_schema_history`.

---

### ➕ Segunda migración: `V2__add_category_to_book.sql`

```sql
ALTER TABLE books ADD category text;
```

> ✅ La columna `category` se añade a `books` y se registra en el historial.

---

### ⚠️ Advertencia importante

**❌ No se debe modificar un script ya ejecutado (como `V1`).**

Si lo haces, Flyway detectará el cambio y **lanzará un error** al no coincidir con el historial de ejecuciones.

---

## ✅ Resultado

- Flyway gestiona las migraciones de forma segura y automática.
- La estructura de base de datos se mantiene coherente entre todos los entornos.
- Se evita que Hibernate modifique la base de datos sin control.
