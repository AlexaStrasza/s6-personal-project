package com.alexstrasza.inventory.components;

import com.alexstrasza.inventory.dao.InventoryDao;
import com.alexstrasza.inventory.dao.UsersDao;
import com.alexstrasza.inventory.entity.ItemBase;
import com.alexstrasza.inventory.entity.InventoryEntity;
import com.alexstrasza.inventory.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class InventoryManager
{
    @Autowired
    RabbitMessenger messenger;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private UsersDao userDao;

    public InventoryEntity GetInventory(String user)
    {
        UsersEntity usersEntity = userDao.findByUsername(user);
        System.out.println("Retrieved user object: " + usersEntity);
        InventoryEntity inventoryEntity = inventoryDao.findByUser(usersEntity);
        System.out.println(inventoryEntity);
        return inventoryEntity;
    }

    public void AddItemToPlayer(String user, ItemBase item)
    {
        InventoryEntity inventoryEntity = GetInventory(user);

        if (inventoryEntity != null)
        {
            System.out.println("Adding <" + item.stackSize + "x " + item.itemBaseId + "> to user <" + user + ">");

            var addedToAStack = false;

            for (ItemBase itemBase : inventoryEntity.inventory)
            {
                if(itemBase.itemBaseId == item.itemBaseId)
                {
                    itemBase.stackSize += item.stackSize;
                    addedToAStack = true;
                    break;
                }
            }

            if (!addedToAStack)
            {
                int openSlot = inventoryEntity.GetNextOpenSlot();
                item.usedSlot = openSlot;
                inventoryEntity.usedSlots.add(openSlot);
                inventoryEntity.inventory.add(item);
            }

            inventoryDao.save(inventoryEntity);

            messenger.SendInventoryUpdate(item, user, "add");
        }
    }

    public boolean RemoveItemFromPlayer(String user, ItemBase item)
    {
        InventoryEntity inventoryEntity = GetInventory(user);

        System.out.println("Removing <" + item.stackSize + "x " + item.itemBaseId + "> from user <" + user + ">");
        if (inventoryEntity != null)
        {
            for (ItemBase itemBase : inventoryEntity.inventory)
            {
                if (itemBase.itemBaseId == item.itemBaseId)
                {
                    if (itemBase.stackSize < item.stackSize) return false;

                    itemBase.stackSize -= item.stackSize;
                    if (itemBase.stackSize == 0)
                    {
                        inventoryEntity.inventory.remove(itemBase);
                    }
                    inventoryDao.save(inventoryEntity);
                    messenger.SendInventoryUpdate(item, user, "remove");
                    return true;
                }
            }
        }
        return false;
    }
}
