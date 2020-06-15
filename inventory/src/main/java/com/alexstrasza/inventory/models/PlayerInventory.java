package com.alexstrasza.inventory.models;

import java.util.*;

public class PlayerInventory
{
    public List<ItemBase> inventory = new ArrayList<>();
    public List<Integer> usedSlots = new ArrayList<>();
//    public HashMap<Integer, ItemBase> inventory = new HashMap<>();
    public String user;

    public PlayerInventory() {}

    public boolean DoesPlayerOwnItems(int itemId, int amount)
    {
        for (ItemBase value : inventory)
        {
            if (value.itemBaseId == itemId)
            {
                if (value.stackSize >= amount)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private int GetNextOpenSlot()
    {
        int i = 0;
        boolean foundSlot = false;
        while (!foundSlot)
        {
            if(!usedSlots.contains(i))
            {
                foundSlot = true;
            }
            else
            {
                i++;
            }
        }

        return i;
    }

    public void AddItemToInventory(ItemBase item)
    {
        var addedToAStack = false;

        for (ItemBase itemBase : inventory)
        {
            if(itemBase.itemBaseId == item.itemBaseId)
            {
                itemBase.stackSize += item.stackSize;
                addedToAStack = true;
                break;
            }
        }

        if (addedToAStack) return;

        int openSlot = GetNextOpenSlot();
        item.usedSlot = openSlot;
        usedSlots.add(openSlot);
        inventory.add(item);
    }

    public boolean RemoveItemFromInventory(ItemBase item)
    {
        for (ItemBase itemBase : inventory)
        {
            if(itemBase.itemBaseId == item.itemBaseId)
            {
                if (itemBase.stackSize < item.stackSize) return false;

                itemBase.stackSize -= item.stackSize;
                if (itemBase.stackSize == 0)
                {
                    inventory.remove(itemBase);
                }
                return true;
            }
        }

        return false;
    }
}
