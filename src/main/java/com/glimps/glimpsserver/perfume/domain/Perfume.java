package com.glimps.glimpsserver.perfume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "perfume")
@Entity

public class Perfume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String perfumeName;

    private double overallRatings;
    private double longevityRatings;
    private double sillageRatings;

    private Integer reviewCnt;


    @Builder.Default
    @OneToMany(mappedBy = "perfume")
    private List<PerfumeTags> perfumeTags = new ArrayList<>();



}
