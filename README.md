# MINIMARKET PLUS

Backend REST para la gestión de un minimarket, desarrollado con Spring Boot 3 y Spring Security (Arquitectura Stateless con JWT).

## Requisitos
* Java 17
* Maven (incluido via `./mvnw`)

## Ejecución local
```bash
cd minimarket
./mvnw spring-boot:run
```
La aplicación queda disponible en http://localhost:8080.


AUTENTICACION Y PRUEBAS (JWT)
La API utiliza JSON WEB TOKENS (JWT). Para acceder a rutas protegidas, debes enviar el token en la cabcera HTTP.

-  Obtener Token (login):
   
curl -X POST http://localhost:8080/api/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin", "password":"admin123"}'

-  Consumir endopoint protegido:

curl -X GET http://localhost:8080/api/ventas \
-H "Authorization: Bearer <TU_TOKEN_AQUI>"

 
 BASE DE DATOS (entorno local)
   Consola H2: http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:minimarketdb
   Usuario: sa
   contraseña: (vacia)


##   USUARIOS DE PRUEBA:  
| Usuario   | Contraseña    | Rol      |
|-----------|---------------|----------|
| admin     | admin123      | ADMIN    |
| gerente   | gerente123    | GERENTE  |
| empleado  | empleado123   | EMPLEADO |
| cliente   | cliente123    | CLIENTE  |


##ROLES Y PERMISOS:

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

TESTING Y COBERTURA DE CÓDIGO

El proyecto cuenta con una suite de pruebas unitarias y de integración orientada al comportamiento guiado por pruebas (TDD). Valida flujos de seguridad (RBAC) y reglas de negocio críticas, como disponibilidad de stock en el carrito y la consistencia de metadatos en el inventario.


Ejecutar pruebas unitarias
Para correr la suite de pruebas automatizadas aisladas con JUnit 5 y Mockito:

``` bash
cd minimarket
./mvnw clean test
```

Reporte de Cobertura (JaCoCo)

El proyecto está configurado con JaCoCo y cumple con una métrica de cobertura de código exigida superior al 80% en la capa de servicios transaccionales. Para instrumentar el código y generar el reporte HTML, ejecuta:
 
``` bash
cd minimarket
./mvnw clean verify
```
Una vez que el proceso de "build" finalice, puedes abrir el reporte de auditoría en tu navegador navegando a la siguiente ruta dentro del proyecto:
* Ruta del reporte: target/site/jacoco/index.html
