package net.runelite.client.plugins.mkutils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.PlayerAppearance;
import net.runelite.api.kit.KitType;
import net.runelite.api.queries.InventoryItemQuery;
import net.runelite.api.queries.InventoryWidgetItemQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

@Slf4j
@Singleton
public class EquipmentUtils
{
	@Inject
	private Client client;

	@Inject
	private MouseUtils mouse;

	@Inject
	private MenuUtils menu;

	@Inject
	private BankUtils bank;

	@Inject
	private ExecutorService executorService;

	@Inject
	private ItemManager itemManager;

	public void openEquipment()
	{
		if (client == null || client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		client.runScript(915, Tab.EQUIPMENT.ordinal());
	}

	public boolean isOpen()
	{
		Widget equipment = client.getWidget(WidgetInfo.EQUIPMENT);
		if (equipment == null)
		{
			return false;
		}
		return !equipment.isHidden();
	}

	public boolean isItemEquipped(Collection<Integer> itemIds)
	{
		assert client.isClientThread();

		ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipmentContainer != null)
		{
			Item[] items = equipmentContainer.getItems();
			for (Item item : items)
			{
				if (itemIds.contains(item.getId()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public int getWornItemId(KitType slot)
	{
		assert client.isClientThread();

		Player local = client.getLocalPlayer();
		if (local == null)
		{
			return -1;
		}

		PlayerAppearance appearance = local.getPlayerAppearance();
		if (appearance != null)
		{
			return appearance.getEquipmentId(slot);
		}

		return -1;
	}

	public List<WidgetItem> getItems(Collection<Integer> ids)
	{
		Widget equipmentWidget = client.getWidget(WidgetInfo.EQUIPMENT);
		List<WidgetItem> matchedItems = new ArrayList<>();

		if (equipmentWidget != null)
		{
			Collection<WidgetItem> items = equipmentWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (ids.contains(item.getId()))
				{
					matchedItems.add(item);
				}
			}
			return matchedItems;
		}
		return null;
	}

	//Requires Inventory visible or returns empty
	public List<WidgetItem> getItems(String itemName)
	{
		return new InventoryWidgetItemQuery()
			.filter(i -> client.getItemDefinition(i.getId())
				.getName()
				.toLowerCase()
				.contains(itemName))
			.result(client)
			.list;
	}

	public Collection<WidgetItem> getAllItems()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			return inventoryWidget.getWidgetItems();
		}
		return null;
	}

	public Collection<Integer> getAllItemIDs()
	{
		Collection<WidgetItem> inventoryItems = getAllItems();
		if (inventoryItems != null)
		{
			Set<Integer> inventoryIDs = new HashSet<>();
			for (WidgetItem item : inventoryItems)
			{
				if (inventoryIDs.contains(item.getId()))
				{
					continue;
				}
				inventoryIDs.add(item.getId());
			}
			return inventoryIDs;
		}
		return null;
	}

	public List<Item> getAllItemsExcept(List<Integer> exceptIDs)
	{
		exceptIDs.add(-1); //empty inventory slot
		ItemContainer inventoryContainer = client.getItemContainer(InventoryID.INVENTORY);
		if (inventoryContainer != null)
		{
			Item[] items = inventoryContainer.getItems();
			List<Item> itemList = new ArrayList<>(Arrays.asList(items));
			itemList.removeIf(item -> exceptIDs.contains(item.getId()));
			return itemList.isEmpty() ? null : itemList;
		}
		return null;
	}

	public WidgetItem getWidgetItem(int id)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (item.getId() == id)
				{
					return item;
				}
			}
		}
		return null;
	}

	public WidgetItem getWidgetItem(Collection<Integer> ids)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (ids.contains(item.getId()))
				{
					return item;
				}
			}
		}
		return null;
	}

	public Item getItemExcept(List<Integer> exceptIDs)
	{
		exceptIDs.add(-1); //empty inventory slot
		ItemContainer inventoryContainer = client.getItemContainer(InventoryID.INVENTORY);
		if (inventoryContainer != null)
		{
			Item[] items = inventoryContainer.getItems();
			List<Item> itemList = new ArrayList<>(Arrays.asList(items));
			itemList.removeIf(item -> exceptIDs.contains(item.getId()));
			return itemList.isEmpty() ? null : itemList.get(0);
		}
		return null;
	}

	public WidgetItem getItemMenu(ItemManager itemManager, String menuOption, int opcode, Collection<Integer> ignoreIDs)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (ignoreIDs.contains(item.getId()))
				{
					continue;
				}
				String[] menuActions = itemManager.getItemDefinition(item.getId()).getInventoryActions();
				for (String action : menuActions)
				{
					if (action != null && action.equals(menuOption))
					{
						return item;
					}
				}
			}
		}
		return null;
	}

	public WidgetItem getItemMenu(Collection<String> menuOptions)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				String[] menuActions = itemManager.getItemDefinition(item.getId()).getInventoryActions();
				for (String action : menuActions)
				{
					if (action != null && menuOptions.contains(action))
					{
						return item;
					}
				}
			}
		}
		return null;
	}

	public WidgetItem getWidgetItemMenu(ItemManager itemManager, String menuOption, int opcode)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				String[] menuActions = itemManager.getItemDefinition(item.getId()).getInventoryActions();
				for (String action : menuActions)
				{
					if (action != null && action.equals(menuOption))
					{
						return item;
					}
				}
			}
		}
		return null;
	}

	public int getItemCount(int id, boolean stackable)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		int total = 0;
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (item.getId() == id)
				{
					if (stackable)
					{
						return item.getQuantity();
					}
					total++;
				}
			}
		}
		return total;
	}

	public boolean containsItem(int itemID)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}

		return new InventoryItemQuery(InventoryID.INVENTORY)
			.idEquals(itemID)
			.result(client)
			.size() >= 1;
	}

	public boolean containsItem(String itemName)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}

		WidgetItem inventoryItem = new InventoryWidgetItemQuery()
			.filter(i -> client.getItemDefinition(i.getId())
				.getName()
				.toLowerCase()
				.contains(itemName))
			.result(client)
			.first();

		return inventoryItem != null;
	}

	public boolean containsStackAmount(int itemID, int minStackAmount)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}
		Item item = new InventoryItemQuery(InventoryID.INVENTORY)
			.idEquals(itemID)
			.result(client)
			.first();

		return item != null && item.getQuantity() >= minStackAmount;
	}

	public boolean containsItemAmount(int id, int amount, boolean stackable, boolean exactAmount)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		int total = 0;
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (item.getId() == id)
				{
					if (stackable)
					{
						total = item.getQuantity();
						break;
					}
					total++;
				}
			}
		}
		return (!exactAmount || total == amount) && (total >= amount);
	}

	public boolean containsItemAmount(Collection<Integer> ids, int amount, boolean stackable, boolean exactAmount)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		int total = 0;
		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
			for (WidgetItem item : items)
			{
				if (ids.contains(item.getId()))
				{
					if (stackable)
					{
						total = item.getQuantity();
						break;
					}
					total++;
				}
			}
		}
		return (!exactAmount || total == amount) && (total >= amount);
	}

	public boolean containsItem(Collection<Integer> itemIds)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}
		return getItems(itemIds).size() > 0;
	}

	public boolean containsAllOf(Collection<Integer> itemIds)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}
		for (int item : itemIds)
		{
			if (!containsItem(item))
			{
				return false;
			}
		}
		return true;
	}

	public boolean containsExcept(Collection<Integer> itemIds)
	{
		if (client.getItemContainer(InventoryID.INVENTORY) == null)
		{
			return false;
		}
		Collection<WidgetItem> inventoryItems = getAllItems();
		List<Integer> depositedItems = new ArrayList<>();

		for (WidgetItem item : inventoryItems)
		{
			if (!itemIds.contains(item.getId()))
			{
				return true;
			}
		}
		return false;
	}
}
