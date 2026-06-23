# Cobertura de código con JaCoCo

Este proyecto usa [JaCoCo](https://www.jacoco.org/jacoco/) para medir la cobertura de tests unitarios e integración.

## Comandos

```bash
# Ejecutar tests (JaCoCo instrumenta automáticamente vía prepare-agent)
mvn clean test

# Generar reporte HTML de cobertura
mvn verify
```

También puedes generar solo el reporte si ya ejecutaste los tests:

```bash
mvn jacoco:report
```

## Reporte

Tras `mvn verify`, abre el reporte en:

```
minimarket/target/site/jacoco/index.html
```

El reporte muestra cobertura por paquete, clase y método (instrucciones, ramas y líneas).

## Configuración

El plugin `jacoco-maven-plugin` (v0.8.12) está definido en `minimarket/pom.xml`:

- **prepare-agent**: se ejecuta en la fase `test` e instrumenta las clases antes de que Surefire corra los tests.
- **report**: se ejecuta en la fase `verify` y genera el reporte HTML en `target/site/jacoco/`.

### Exclusiones

Las siguientes clases se excluyen del reporte por ser arranque o seed de datos:

- `MinimarketApplication`
- `config/DataInitializer`

## Dependencias de test

Los tests usan JUnit 5 y Mockito (incluidos en `spring-boot-starter-test`). Mockito se usa en tests unitarios de servicios donde se simulan repositorios y dependencias externas.
