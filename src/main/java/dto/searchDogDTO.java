/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author fh
 */
public class searchDogDTO {
    private String breed;
    private String info;
    private String wikipedia;

    public searchDogDTO(String breed, String info, String wikipedia) {
        this.breed = breed;
        this.info = info;
        this.wikipedia = wikipedia;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }
    
    
}
