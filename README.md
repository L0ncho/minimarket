# MINIMARKET PLUS

Backend REST para la gestión de un minimarket, desarrollado con Spring Boot 3 y Spring Security (Arquitectura Stateless con JWT).

## Requisitos
* Java 17
* Maven (incluido via `./mvnw`)

## Ejecución local
```bash
mvn spring-boot:run
```
La aplicación queda disponible en http://localhost:8080.

## Autenticación y pruebas (JWT)

La API utiliza JSON Web Tokens (JWT). Para acceder a rutas protegidas, debes enviar el token en la cabecera HTTP `Authorization: Bearer <token>`.

**Obtener token (login):**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin", "password":"Admin123!"}'
```

**Registrar nuevo usuario:**

```bash
curl -X POST http://localhost:8080/api/auth/registro \
  -H "Content-Type: application/json" \
  -d '{"username":"nuevo", "password":"Password123!"}'
```

**Consumir endpoint protegido:**

```bash
curl -X GET http://localhost:8080/api/ventas \
  -H "Authorization: Bearer <TU_TOKEN_AQUI>"
```

### Política de contraseñas

Las contraseñas en registro y alta de usuarios deben cumplir:

* Mínimo 8 caracteres
* Al menos una mayúscula, una minúscula, un número y un carácter especial (`!@#$%^&*`, etc.)

### Protección del login (entorno local)

* Bloqueo temporal tras intentos fallidos (en memoria; se reinicia al reiniciar la aplicación)
* Rate limiting por IP en `POST /api/auth/login`

Propiedades configurables en `application.properties` bajo `security.login.*`.

## Base de datos (entorno local)

* Consola H2: http://localhost:8080/h2-console
* JDBC URL: `jdbc:h2:mem:minimarketdb`
* Usuario: `sa`
* Contraseña: (vacía)

## Usuarios de prueba

| Usuario   | Contraseña     | Rol      |
|-----------|----------------|----------|
| admin     | Admin123!      | ADMIN    |
| gerente   | Gerente123!    | GERENTE  |
| empleado  | Empleado123!   | EMPLEADO |
| cliente   | Cliente123!    | CLIENTE  |

## Roles y permisos

| Recurso | Público | CLIENTE | EMPLEADO | GERENTE | ADMIN |
|---------|---------|---------|----------|---------|-------|
| GET productos / categorías | Si | Si | Si | Si | Si |
| POST/PUT/DELETE productos | — | — | — | Si | Si |
| POST/PUT/DELETE categorías | — | — | — | Si | Si |
| Carrito | — | Si | — | — | Si |
| GET inventario | — | — | Si | Si | Si |
| POST/PUT/DELETE inventario | — | — | — | Si | Si |
| Ventas / detalle ventas | — | — | Si | Si | Si |
| Usuarios | — | — | — | — | Si |
| /public/** | Si | Si | Si | Si | Si |

## Tests

```bash
cd minimarket
mvn clean test
```

## Reporte de Cobertura (JaCoCo)

Visita [docs/coverage.md](docs/coverage.md) para ver más detalle.