package com.alexstrasza.auctioning;

public class ItemBase
{
    // Base id without any data would be enough, clients should have an up to date item database to retrieve data from
    public int itemBaseId;
    public int stackSize;

    // Possibly should be removed
//    public String itemStatData;

}
