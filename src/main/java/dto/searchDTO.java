/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Search;

/**
 *
 * @author fh
 */
public class searchDTO {
    private String breed;

    public searchDTO(String breed) {
        this.breed = breed;
    }
    
    public searchDTO(Search search){
        this.breed = search.getBreed();
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
    
    
}
