# Restaurant Management System

Java 17 Maven WAR for the two agreed modules: add dishes and import ingredients.
The application targets Apache Tomcat 9 and uses `javax.servlet`, JSP, JSTL 1.2,
and PostgreSQL JDBC. The DAOs use quoted PostgreSQL identifiers and
`PreparedStatement`.

## Database configuration

The app connects directly to the existing Supabase PostgreSQL database. Configure
`src/main/resources/db.properties` locally:

```properties
db.url=jdbc:postgresql://<HOST>:5432/postgres?sslmode=require
db.user=<USER>
db.password=<PASSWORD>
```

Use the direct PostgreSQL connection values shown in Supabase's Connect dialog.
The application also accepts `SUPABASE_DB_URL`, `SUPABASE_DB_USER`, and
`SUPABASE_DB_PASSWORD` environment variables, which take precedence over the
properties file. `db.properties` is ignored by Git; commit only
`db.properties.example`. Do not commit credentials.

Login joins `tblUser2` with `tblStaff2`; the `position` must be exactly
`manager` or `warehouse`. It then reads the staff restaurant ID for the session.
The supplied sample users are `manager01` and `warehouse01`, both with password
`123456`.

Each DAO operation opens and closes its own JDBC connection through `DAO2`.
This avoids sharing a single static connection between concurrent servlet
requests.

## Build and deploy

From the project directory:

```powershell
mvn clean package
```

Deploy `target/restaurant-management.war` to Apache Tomcat 9. The welcome page is
`Login2.jsp`. All module URLs require an authenticated staff session.

## Functional check

1. Replace the three placeholders in `src/main/resources/db.properties` with
   the PostgreSQL host, username, and password from Supabase. Keep the port at
   `5432`, database at `postgres`, and `sslmode=require`. Alternatively, set the
   three `SUPABASE_DB_*` environment variables for Tomcat.
2. Run `mvn clean package`, then deploy `target/restaurant-management.war` to
   Tomcat 9 and start Tomcat.
3. Open `http://localhost:8080/restaurant-management/`. Sign in with
   `manager01` / `123456`, add a dish with an existing category, then check
   `"tblDish2"` in Supabase Table Editor.
4. Sign out and sign in with `warehouse01` / `123456`. Search for or add a
   supplier, search for or add an ingredient, create an import bill, and use
   **Pay**. Check `"tblSupplier2"`, `"tblIngredient2"`,
   `"tblImportBill2"`, and `"tblImportBillDetail2"` in Supabase Table Editor.
   The payment should create the bill and details and update the ingredient's
   `"CurrentAmount"` and `"AveragePrice"` together.

To test the database from PowerShell after building and filling in credentials:

```powershell
mvn test-compile
$pg = Get-ChildItem "$env:USERPROFILE\.m2\repository\org\postgresql\postgresql\42.7.4" -Filter "postgresql-42.7.4.jar" | Select-Object -First 1 -ExpandProperty FullName
java -cp "target\test-classes;target\classes;$pg" smoke.DBConnectionTest2
```

The test prints the row count from `"tblUser2"` and is not exposed as a web route.

## Images and supplier ingredients

When adding a dish, you can paste an image URL or upload a PNG, JPEG, or GIF
image up to 5 MB. Uploaded images are stored outside the WAR under
`%CATALINA_BASE%\uploads\dishes`; the database stores a generated image path.
Dish images are served by the application, and remain available when the WAR
is redeployed as long as that upload directory is preserved.

Ingredient search and selection are scoped to the current supplier through
`tblSupplierIngredient2`. New ingredients are linked to the supplier selected
in the import flow. Use **Liên kết nguyên liệu đã có** to associate an existing
ingredient with another supplier; existing ingredient records without a link
will not appear in that supplier's import search until linked.

## Module routes

- `/management/home` — manager home
- `/management/dishes` — dish list
- `/management/dishes/add` — add dish
- `/warehouse/home` — warehouse home
- `/warehouse/suppliers` — search/select supplier
- `/warehouse/suppliers/add` — add supplier
- `/warehouse/ingredients` — search ingredients and manage import lines
- `/warehouse/ingredients/add` — add ingredient
- `/warehouse/import-bill` — review, print, and pay the import bill
