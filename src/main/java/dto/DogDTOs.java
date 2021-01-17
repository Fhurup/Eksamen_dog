/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Dog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fh
 */
public class DogDTOs {
         List<DogDTO> all = new ArrayList();

    public DogDTOs(List<Dog> dogEntities) {
        dogEntities.forEach((f) -> {
            all.add(new DogDTO(f));
        });
    }

    public List<DogDTO> getAll() {
        return all;
    }
}
