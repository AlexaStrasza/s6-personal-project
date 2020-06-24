package com.alexstrasza.auctioning.models;

public class DataContainer
{
    public String heldString;
    public int heldInt;

    public DataContainer()
    {

    }
    public DataContainer(int intValue)
    {
        heldInt = intValue;
    }
    public DataContainer(String stringValue)
    {
        heldString = stringValue;
    }
    public DataContainer(String stringValue, int intValue)
    {
        heldString = stringValue;
        heldInt = intValue;
    }
}
