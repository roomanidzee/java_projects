package ru.itis.romanov_andrey.perpenanto.models.usermodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;

import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.List;

/**
 * 01.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"profiles"})
@ToString(exclude = {"profiles"})
@Entity
@Table(name = "files_of_service")
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encoded_name")
    private String storageFileName;

    @Column(name = "original_name")
    private String originalFileName;

    @Column(name = "file_size")
    private long size;

    @Column(name = "file_type")
    private String type;

    @Column(name = "file_url")
    private String url;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "file_to_profile",
               joinColumns = @JoinColumn(name = "file_id"),
               inverseJoinColumns = @JoinColumn(name = "profile_id"))
    private List<Profile> profiles;

}
