package com.alexstrasza.auctioning.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FilterOptions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String name;
    public String type;
    public String rarity;
    public boolean buyout;

    public FilterOptions() { }
}