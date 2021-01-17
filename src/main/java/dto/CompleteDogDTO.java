/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author fh
 */
public class CompleteDogDTO {
    private String breed;
    private String info;
    private String wikipedia;
    private String image;
    private List<String> facts;

    public CompleteDogDTO(searchDogDTO sDogDTO,searchDogImageDTO sDogImageDTO, FactsDTO facts){
        this.breed = sDogDTO.getBreed();
        this.info = sDogDTO.getInfo();
        this.wikipedia = sDogDTO.getWikipedia();
        this.image = sDogImageDTO.getImage();
        this.facts = facts.getAll();
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getFacts() {
        return facts;
    }

    public void setFacts(List<String> facts) {
        this.facts = facts;
    }
    
    
}
