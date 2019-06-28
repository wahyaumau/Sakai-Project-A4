/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.model;

import java.util.Date;

/**
 *
 * @author wahyu mau
 */
public class PollCategory {
    private Long id;
    private String name;

    public PollCategory() {
    }

    public PollCategory(Long id) {
        this.id = id;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
