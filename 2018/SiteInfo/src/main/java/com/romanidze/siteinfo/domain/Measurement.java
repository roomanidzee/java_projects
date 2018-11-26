package com.romanidze.siteinfo.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 03.11.2018
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
@Table("measurement")
public class Measurement {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userID;

    @Column("page_id")
    private Long pageID;

    @Column("attendance_date")
    private String attendanceDate;

}
