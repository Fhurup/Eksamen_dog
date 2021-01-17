/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fh
 */
public class BreedDTOs {

    List<BreedDTO> dogs = new ArrayList();

    public BreedDTOs(List<BreedDTO> breedsDTO) {
        this.dogs = breedsDTO;
    }

    public List<BreedDTO> getAll() {
        return dogs;
    }

}
