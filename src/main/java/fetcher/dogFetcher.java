/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fetcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BreedDTOs;
import dto.CompleteDogDTO;
import dto.FactsDTO;
import dto.searchDogDTO;
import dto.searchDogImageDTO;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;

/**
 *
 * @author fh
 */
public class dogFetcher {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public BreedDTOs getBreeds() throws IOException {
        String breeds = HttpUtils.fetchData("https://dog-info.cooljavascript.dk/api/breed");
        BreedDTOs breedDTOs = GSON.fromJson(breeds, BreedDTOs.class);

        return breedDTOs;
    }

    public CompleteDogDTO searchDog(ExecutorService threadPool, String search) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        Callable<searchDogDTO> searchDogTask = new Callable<searchDogDTO>() {
            @Override
            public searchDogDTO call() throws IOException {
                String searchResultDog = HttpUtils.fetchData("https://dog-info.cooljavascript.dk/api/breed/" + search);
                searchDogDTO sDogDTO = GSON.fromJson(searchResultDog, searchDogDTO.class);
                return sDogDTO;
            }
        };

        Callable<searchDogImageDTO> searchDogImageTask = new Callable<searchDogImageDTO>() {
            @Override
            public searchDogImageDTO call() throws IOException {
                String searchResultImage = HttpUtils.fetchData("https://dog-image.cooljavascript.dk/api/breed/random-image/" + search);
                searchDogImageDTO sDogImageDTO = GSON.fromJson(searchResultImage, searchDogImageDTO.class);
                return sDogImageDTO;
            }
        };

        Callable<FactsDTO> factsTask = new Callable<FactsDTO>() {
            @Override
            public FactsDTO call() throws IOException {
                String randomDogFact = HttpUtils.fetchData("https://dog-api.kinduff.com/api/facts");
                FactsDTO facts = GSON.fromJson(randomDogFact, FactsDTO.class);
                return facts;
            }
        };

        Future<searchDogDTO> futureSearchDog = threadPool.submit(searchDogTask);
        Future<searchDogImageDTO> futureSearchDogImage = threadPool.submit(searchDogImageTask);
        Future<FactsDTO> futureFacts = threadPool.submit(factsTask);

        searchDogDTO sDogDTO = futureSearchDog.get(3, TimeUnit.SECONDS);
        searchDogImageDTO sDogImageDTO = futureSearchDogImage.get(3, TimeUnit.SECONDS);
        FactsDTO facts = futureFacts.get(3, TimeUnit.SECONDS);

        CompleteDogDTO completeDogDTO = new CompleteDogDTO(sDogDTO, sDogImageDTO, facts);

        return completeDogDTO;

    }

}

//      String searchResultDog = HttpUtils.fetchData("https://dog-info.cooljavascript.dk/api/breed/"+ search);
// searchDogDTO sDogDTO = GSON.fromJson(searchResultDog, searchDogDTO.class);
// String searchResultImage = HttpUtils.fetchData("https://dog-image.cooljavascript.dk/api/breed/random-image/" + search);
//    searchDogImageDTO sDogImageDTO = GSON.fromJson(searchResultImage, searchDogImageDTO.class);
//
//       
//        String randomDogFact = HttpUtils.fetchData("https://dog-api.kinduff.com/api/facts");
//
//    
//        FactsDTO facts = GSON.fromJson(randomDogFact, FactsDTO.class);
