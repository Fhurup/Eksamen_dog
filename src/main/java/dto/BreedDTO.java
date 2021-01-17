/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Breed;

/**
 *
 * @author fh
 */
public class BreedDTO {

    private String breed;

    public BreedDTO(String breed) {
        this.breed = breed;
    }

    public BreedDTO(Breed breed) {
        this.breed = breed.getName();
    }

}
