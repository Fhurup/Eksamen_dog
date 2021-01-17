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
      
    public CompleteDogDTO searchDog(String search) throws IOException{
        String searchResultDog = HttpUtils.fetchData("https://dog-info.cooljavascript.dk/api/breed/"+ search);
        String searchResultImage = HttpUtils.fetchData("https://dog-image.cooljavascript.dk/api/breed/random-image/" + search);
        String randomDogFact = HttpUtils.fetchData("https://dog-api.kinduff.com/api/facts");
        
        searchDogDTO sDogDTO = GSON.fromJson(searchResultDog, searchDogDTO.class);
        searchDogImageDTO sDogImageDTO = GSON.fromJson(searchResultImage, searchDogImageDTO.class);
        FactsDTO facts = GSON.fromJson(randomDogFact, FactsDTO.class);
        
        CompleteDogDTO completeDogDTO = new CompleteDogDTO(sDogDTO, sDogImageDTO, facts);
        
       return completeDogDTO;
        
    }
}
