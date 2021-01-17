package facades;

import dto.DogDTO;
import entities.Dog;
import utils.EMF_Creator;
import entities.RenameMe;
import entities.Role;
import entities.Search;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class DogFacadeTest {

    User user;
    User admin;
    User both;
    Role userRole;
    Role adminRole;
    Dog dog;
    Search search;
    Search search2;
    Search search3;
    
    private static EntityManagerFactory emf;
    private static DogFacade facade;

    public DogFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = DogFacade.getDogFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
   @Test
    public void testGetDogs() {
        assertEquals(1, facade.getDogs(user.getUserName()).getAll().size(), "Expect 1 dog");
    }

    @Test
    public void testAddDog() {
        Dog dog2 = new Dog("Johnny", "igår", "Tyk", "beagle");
        DogDTO dDTO = new DogDTO(dog2);
        DogDTO dDTOadded = facade.addDog(dDTO, user.getUserName());
        assertEquals(dDTO.getName(), dDTOadded.getName(), "Expect the same name");
        assertEquals(2, facade.getDogs(user.getUserName()).getAll().size(), "Excepts two dogs");
    }
    
    @Test
    public void testSearch() {
        facade.search("beagle");
        assertEquals("[4]", facade.totalSearches(), "Expect 4 searches");
    }
    
    @Test
    public void testTotalSearches() {
        assertEquals("[3]", facade.totalSearches(), "Expect 4 searches");
    }
    
    @Test
    public void testBreedSearches() {
        assertEquals("[2]", facade.breedSearches("beagle"), "Expect 4 searches");
    }
}


