/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.DogDTO;
import dto.DogDTOs;
import dto.searchDTO;
import entities.Dog;
import entities.Search;
import entities.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author fh
 */
public class DogFacade {

    private static EntityManagerFactory emf;
    private static DogFacade instance;

    private DogFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static DogFacade getDogFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DogFacade();
        }
        return instance;
    }

    public DogDTO addDog(DogDTO dogDTO, String name) {
        EntityManager em = emf.createEntityManager();

        try {

            Dog dog = new Dog(dogDTO.getName(), dogDTO.getDateOfBirth(), dogDTO.getInfo(), dogDTO.getBreed());

            User user = em.find(User.class, name);
            user.addDog(dog);
            em.getTransaction().begin();

            em.persist(dog);

            em.getTransaction().commit();

            return new DogDTO(dog);
        } finally {
            em.close();
        }
    }

    public DogDTOs getDogs(String name) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Dog> query = em.createQuery("SELECT d FROM Dog d WHERE d.user.userName =:name", Dog.class);
            query.setParameter("name", name);
            List<Dog> dogs = query.getResultList();
            DogDTOs all = new DogDTOs(dogs);
            return all;

        } finally {
            em.close();
        }
    }

    public searchDTO search(String breedSearch) {
        EntityManager em = emf.createEntityManager();
        try {
            Search search = new Search(breedSearch);

            em.getTransaction().begin();

            em.persist(search);

            em.getTransaction().commit();

            return new searchDTO(search);

        } finally {
            em.close();
        }
    }

    public String totalSearches() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Search> query = em.createQuery("select s from Search s", entities.Search.class);
            List<Search> Searches = query.getResultList();
            return "[" + Searches.size() + "]";

        } finally {
            em.close();
        }
    }

    public String breedSearches(String breed) {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<Search> query = em.createQuery("select s from Search s WHERE s.breed=:breed", entities.Search.class);
            query.setParameter("breed", breed);
            List<Search> Searches = query.getResultList();
            return "[" + Searches.size() + "]";

        } finally {
            em.close();
        }
    }

}
