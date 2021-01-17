package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BreedDTOs;
import dto.CompleteDogDTO;
import dto.DogDTO;
import dto.DogDTOs;
import entities.Dog;
import entities.Phone;
import entities.Role;
import entities.Search;
import entities.User;
import facades.DogFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import fetcher.dogFetcher;
import utils.EMF_Creator;
import utils.HttpUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

/**
 * @author lam@cphbusiness.dk
 */
@Path("dogs")
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    private static EntityManagerFactory emf;

    @Context
    SecurityContext securityContext;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final DogFacade FACADE = DogFacade.getDogFacade(EMF);

    private static ExecutorService es = Executors.newCachedThreadPool();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mydogs/{user}")
    public String getMyDogs(@PathParam("user") String user) throws IOException {
        DogDTOs dogsDTO = FACADE.getDogs(user);

        return GSON.toJson(dogsDTO);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("adddog/{user}")
    public String addFavorit(@PathParam("user") String user, String dog) {
        DogDTO dogDTO = GSON.fromJson(dog, DogDTO.class);
        DogDTO dAdded = FACADE.addDog(dogDTO, user);
        return GSON.toJson(dAdded);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("breeds")
    public String getBreeds() throws IOException {
        dogFetcher df = new dogFetcher();

        BreedDTOs breedDTOs = df.getBreeds();

        return GSON.toJson(breedDTOs);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search/{breed}")
    public String searchBreed(@PathParam("breed") String breed) throws IOException, InterruptedException, TimeoutException, TimeoutException, ExecutionException {
        FACADE.search(breed);

        dogFetcher df = new dogFetcher();

        CompleteDogDTO completeDogDTO = df.searchDog(es, breed);

        return GSON.toJson(completeDogDTO);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("breedsearch/{breed}")
    @RolesAllowed("admin")
    public String getBreedSearches(@PathParam("breed") String breed) throws IOException {
        String amountOfBreedSearches = FACADE.breedSearches(breed);

        return amountOfBreedSearches;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("totalsearches")
    @RolesAllowed("admin")
    public String getAmountOfSearches() throws IOException {
        String amountOfSearches = FACADE.totalSearches();

        return amountOfSearches;
    }
    
//    @Path("dogname/{name}/user/{user}")
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    public String deleteFavorit(@PathParam("user") String user, @PathParam("name") String name) {
//        String s = FACADE.deleteDog(user, name);
//        return GSON.toJson(s);
//    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("populate")
    public String populate() throws IOException {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        User user = new User("user", "hello");
        User admin = new User("admin", "with");
        User both = new User("user_admin", "you");
        Phone phone = new Phone("12345678", "iPhone");
        Phone phone2 = new Phone("12345679", "Samsung");
        Phone phone3 = new Phone("12345688", "Nokia");
        Phone phone4 = new Phone("32132132", "Nejia");
        Phone phone5 = new Phone("89898989", "Jokia");
        Dog dog = new Dog("John", "lille", "14", "Beagle");
        Search search = new Search("Beagle");

        em.getTransaction().begin();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        user.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);
        user.addPhone(phone);
        admin.addPhone(phone2);
        both.addPhone(phone3);
        user.addPhone(phone4);
        admin.addPhone(phone5);
        user.addDog(dog);
        em.persist(search);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);
        em.getTransaction().commit();

        return GSON.toJson("hej");
    }

}
