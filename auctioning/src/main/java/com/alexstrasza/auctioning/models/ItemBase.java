package com.alexstrasza.auctioning.models;

import javax.persistence.*;

@Entity
//@Table(name = "items")
public class ItemBase
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    // Base id without any data would be enough, clients should have an up to date item database to retrieve data from
    public int itemBaseId;
    public int stackSize;
    public int usedSlot;

    public ItemBase() { }

    public ItemBase(int itemBaseId, int stackSize)
    {
        this.itemBaseId = itemBaseId;
        this.stackSize = stackSize;
    }

    public ItemBase(int itemBaseId, int stackSize, int slot)
    {
        this.itemBaseId = itemBaseId;
        this.stackSize = stackSize;
        this.usedSlot = slot;
    }
}
