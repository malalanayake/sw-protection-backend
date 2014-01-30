/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sw.protection.backend.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author dinuka
 */
@Entity
public class CompanyClient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
	return super.equals(obj); // To change body of generated methods, choose
	// Tools | Templates.
    }

    @Override
    public int hashCode() {
	return super.hashCode(); // To change body of generated methods, choose
	// Tools | Templates.
    }

    @Override
    public String toString() {
	return "com.sw.protection.backend.entity.CompanyClient[ id=" + id
		+ " ]";
    }

}
