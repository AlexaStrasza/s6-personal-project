package com.alexstrasza.inventory.components;

import com.alexstrasza.inventory.dao.InventoryDao;
import com.alexstrasza.inventory.dao.UsersDao;
import com.alexstrasza.inventory.entity.ItemBase;
import com.alexstrasza.inventory.entity.InventoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class InventoryManager
{
    @Autowired
    RabbitMessager messager;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private UsersDao userDao;

    public InventoryEntity GetInventory(String user)
    {
        return inventoryDao.findByUser(userDao.findByUsername(user));
    }

    public void AddItemToPlayer(String user, ItemBase item)
    {
        InventoryEntity inventoryEntity = GetInventory(user);

        System.out.println("Adding <" + item.stackSize + "x " + item.itemBaseId + "> to user <" + user + ">");

        var addedToAStack = false;

        for (ItemBase itemBase : inventoryEntity.getInventory())
        {
            if(itemBase.itemBaseId == item.itemBaseId)
            {
                itemBase.stackSize += item.stackSize;
                addedToAStack = true;
                break;
            }
        }

        if (addedToAStack) return;

        int openSlot = inventoryEntity.GetNextOpenSlot();
        item.usedSlot = openSlot;
        inventoryEntity.usedSlots.add(openSlot);
        inventoryEntity.inventory.add(item);

        inventoryDao.save(inventoryEntity);

        messager.SendInventoryUpdate(item, user, "add");
    }

    public boolean RemoveItemFromPlayer(String user, ItemBase item)
    {
        InventoryEntity inventoryEntity = GetInventory(user);

        System.out.println("Removing <" + item.stackSize + "x " + item.itemBaseId + "> from user <" + user + ">");

        for (ItemBase itemBase : inventoryEntity.inventory)
        {
            if(itemBase.itemBaseId == item.itemBaseId)
            {
                if (itemBase.stackSize < item.stackSize) return false;

                itemBase.stackSize -= item.stackSize;
                if (itemBase.stackSize == 0)
                {
                    inventoryEntity.inventory.remove(itemBase);
                }
                inventoryDao.save(inventoryEntity);
                messager.SendInventoryUpdate(item, user, "remove");
                return true;
            }
        }
        return false;
    }
}
