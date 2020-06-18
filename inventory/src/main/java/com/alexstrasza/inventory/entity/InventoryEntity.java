package com.alexstrasza.inventory.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "inventory", uniqueConstraints=@UniqueConstraint(columnNames="user"))
public class InventoryEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<ItemBase> getInventory()
    {
        return inventory;
    }

    public void setInventory(List<ItemBase> inventory)
    {
        this.inventory = inventory;
    }

    public Set<Integer> getUsedSlots()
    {
        return usedSlots;
    }

    public void setUsedSlots(Set<Integer> usedSlots)
    {
        this.usedSlots = usedSlots;
    }

    public UsersEntity getUser()
    {
        return user;
    }

    public void setUser(UsersEntity user)
    {
        this.user = user;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ItemBase> inventory = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="usedslots", joinColumns=@JoinColumn(name="OWNER_ID")
    )
    public Set<Integer> usedSlots = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    public UsersEntity user;

    public InventoryEntity()
    {
    }

    public InventoryEntity(UsersEntity user)
    {
        this.user = user;
    }

    public InventoryEntity(UsersEntity user, List<ItemBase> inventory, Set<Integer> usedSlots)
    {
        this.user = user;
        this.inventory = inventory;
        this.usedSlots = usedSlots;
    }

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

    public int GetNextOpenSlot()
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
}
