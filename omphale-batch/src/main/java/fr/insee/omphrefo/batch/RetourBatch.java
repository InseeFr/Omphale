package fr.insee.omphrefo.batch;

public class RetourBatch {
	
    private static final String ERROR = " Erreur ! ";
    private int status;
    private String commentPlus;

    public RetourBatch(int status, String commentPlus) {
        this.status = status;
        this.commentPlus = commentPlus;
    }

    public RetourBatch(int status) {
        this.status = status;
        this.commentPlus = ERROR;
    }

    public RetourBatch(ECodeRetour e) {
        this.status = e.getCode();
        this.commentPlus = e.getLibelle();
    }

    public String getCommentPlus() {
        return commentPlus;
    }

    public void setCommentPlus(String commentPlus) {
        this.commentPlus = commentPlus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
