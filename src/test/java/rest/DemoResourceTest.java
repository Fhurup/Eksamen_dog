package rest;

import dto.DogDTO;
import entities.Dog;
import entities.RenameMe;
import entities.Role;
import entities.Search;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class DemoResourceTest {

    User user;
    User admin;
    User both;
    Role userRole;
    Role adminRole;
    Dog dog;
    Search search;
    Search search2;
    Search search3;

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from Dog").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("DELETE from Search").executeUpdate();

            dog = new Dog("John", "lille", "14", "Beagle");
            search = new Search("Beagle");
            search2 = new Search("Beagle");
            search3 = new Search("atika");
            userRole = new Role("user");
            adminRole = new Role("admin");
            user = new User("user", "test");
            user.addRole(userRole);
            admin = new User("admin", "test");
            admin.addRole(adminRole);
            both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            user.addDog(dog);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(search);
            em.persist(search2);
            em.persist(search3);
            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testCountAll() throws Exception {
        given()
                .contentType("application/json")
                .get("/dogs/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(equalTo("[3]"));
    }

    @Test
    public void testGetSearch() throws Exception {
        given()
                .contentType("application/json")
                .get("/dogs/search/beagle").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("info", notNullValue())
                .body("wikipedia", equalTo("https://en.wikipedia.org/wiki/Beagle"))
                .body("image", notNullValue())
                .body("facts", notNullValue());
    }

    @Test
    public void testGetBreeds() {
        given()
                .contentType("application/json")
                .get("/dogs/breeds").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("dogs.size()", is(59));
    }

    @Test
    public void testAddDog() throws Exception {

        Dog dog2 = new Dog("Johnny", "igår", "Tyk", "beagle");
        DogDTO dDTO = new DogDTO(dog2);
        given()
                .contentType("application/json")
                .body(dDTO)
                .when()
                .post("/dogs/adddog/" + user.getUserName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo("Johnny"))
                .body("dateOfBirth", equalTo("igår"))
                .body("info", equalTo("Tyk"))
                .body("breed", equalTo("beagle"));

        List<DogDTO> DogDTOs;
        DogDTOs = given()
                .contentType("application/json")
                .when()
                .get("/dogs/mydogs/" + user.getUserName()).then()
                .extract().body().jsonPath().getList("all", DogDTO.class);

        assertThat(DogDTOs, iterableWithSize(2));
    }

    @Test
    public void testGetAllFavorites() throws Exception {

        List<DogDTO> DogDTOs;
        DogDTOs = given()
                .contentType("application/json")
                .when()
                .get("/dogs/mydogs/" + user.getUserName()).then()
                .extract().body().jsonPath().getList("all", DogDTO.class);

        assertThat(DogDTOs, iterableWithSize(1));
    }

    @Test
    public void testGetAllSearches() throws Exception {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/dogs/totalsearches").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(equalTo("[3]"));
    }
    
    @Test
    public void testGetAllBreedSearches() throws Exception {
        login("admin", "test");
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/dogs/breedsearch/" + search.getBreed()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(equalTo("[2]"));
    }

}
