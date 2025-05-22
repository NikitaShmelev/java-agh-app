package com.example.demo.service;

import com.example.demo.dao.RateDao;
import com.example.demo.entity.Rate;

import java.util.List;

public class RateService {
    private final RateDao dao = new RateDao();

    public void addRate(Rate r)           { dao.save(r); }

    /** zwraca tablicę Object[]{ String nazwa, Long liczba, Double średnia } */
    public List<Object[]> stats()         { return dao.statsPerGroup(); }
}
