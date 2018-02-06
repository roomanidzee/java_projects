package ru.itis.romanov_andrey.perpenanto.models.usermodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;

/**
 * 01.01.2018
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
public class FileInfo {

    private Long id;
    private String originalName;
    private String encodedName;
    private Long size;
    private String type;
    private String url;

}
