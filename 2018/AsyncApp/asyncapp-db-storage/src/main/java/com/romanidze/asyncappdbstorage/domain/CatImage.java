package com.romanidze.asyncappdbstorage.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import java.io.Serializable;
import java.util.Set;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "image_of_cat")
public class CatImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL")
    private Long id;

    @Column(name = "image_api_id")
    private String idOfImageFromAPI;

    @Column(name = "url")
    private String imageURL;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_to_image",
               joinColumns = @JoinColumn(name = "image_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

}
