package com.alexstrasza.auctioning.models;

public class ItemBase
{
    // Base id without any data would be enough, clients should have an up to date item database to retrieve data from
    public int itemBaseId;
    public int stackSize;
    public int usedSlot;

    public ItemBase() { }

    public ItemBase(int id, int amount)
    {
        itemBaseId = id;
        stackSize = amount;
    }
    // Possibly should be removed
//    public String itemStatData;

}
