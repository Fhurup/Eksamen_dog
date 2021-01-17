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
public class FactsDTO {
    List<String> facts = new ArrayList();

    public FactsDTO(List<String> fact) {
        this.facts = fact;
    }

    public List<String> getAll() {
        return facts;
    }

}
