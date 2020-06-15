package com.alexstrasza.webapi.models;

public class ItemBase
{
    // Base id without any data would be enough, clients should have an up to date item database to retrieve data from
    public int itemBaseId;
    public int stackSize;
    public int usedSlot;

    public ItemBase() { }
    // Possibly should be removed
//    public String itemStatData;

}
