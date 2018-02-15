package com.marica.m_note.pojoclass;

/**
 * Created by Neston on 31/12/2017.
 */

public class Note {
    private String note_title,note_detail;
    String created_at,updated_at,id;

    public Note(String note_title, String note_detail, String created_at, String updated_at) {
        this.note_title=note_title;
        this.note_detail=note_detail;
        this.created_at=created_at;
        this.updated_at=updated_at;
        this.id=id;

    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_detail() {
        return note_detail;
    }

    public void setNote_detail(String note_detail) {
        this.note_detail = note_detail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
