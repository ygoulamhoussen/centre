package com.formation.centre.dto;

import java.util.List;

public class ProprieteDetailDTO {

    private ProprieteDTO propriete;
    private List<CompositionAcquisitionDTO> compositions;
    private List<DocumentDTO> documents;

    public ProprieteDetailDTO(ProprieteDTO toDTO, List<CompositionAcquisitionDTO> compositions, List<DocumentDTO> documents) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Getters et setters
    public ProprieteDTO getPropriete() {
        return propriete;
    }

    public void setPropriete(ProprieteDTO propriete) {
        this.propriete = propriete;
    }

    public List<CompositionAcquisitionDTO> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<CompositionAcquisitionDTO> compositions) {
        this.compositions = compositions;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }
}
