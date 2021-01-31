package net.runelite.client.plugins.mkutils;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemDefinition;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Varbits;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static net.runelite.client.plugins.mkutils.MKUtils.iterating;
import static net.runelite.client.plugins.mkutils.MKUtils.sleep;

@Slf4j
@Singleton
public class InventoryUtils
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

	@Inject
	private EquipmentUtils equipmentUtils;

	@Inject
	private PlayerUtils playerUtils;

	public void openInventory()
	{
		if (client == null || client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		client.runScript(915, Tab.INVENTORY.ordinal()); //open inventory
	}

	public boolean isFull()
	{
		return getEmptySlots() <= 0;
	}

	public boolean isEmpty()
	{
		return getEmptySlots() >= 28;
	}

	public boolean isOpen()
	{
		Widget inventory = client.getWidget(WidgetInfo.INVENTORY);
		if (inventory == null)
		{
			return false;
		}
		return !inventory.isHidden();
	}

	public int getEmptySlots()
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		if (inventoryWidget != null)
		{
			return 28 - inventoryWidget.getWidgetItems().size();
		}
		else
		{
			return -1;
		}
	}

	public List<WidgetItem> getItems(Collection<Integer> ids)
	{
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		List<WidgetItem> matchedItems = new ArrayList<>();

		if (inventoryWidget != null)
		{
			Collection<WidgetItem> items = inventoryWidget.getWidgetItems();
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

	private boolean hasStaffEquipped(int elementalRuneId, boolean allowVariants)
	{
		String[] typeNames;
		switch (elementalRuneId)
		{
			case ItemID.AIR_RUNE:
				typeNames = allowVariants ? new String[]{ "air", "smoke", "mist", "dust" } : new String[]{ "air" };
				break;
			case ItemID.EARTH_RUNE:
				typeNames = allowVariants ? new String[]{ "earth", "mud", "dust", "lava" } : new String[]{ "earth" };
				break;
			case ItemID.WATER_RUNE:
				typeNames = allowVariants ? new String[]{ "water", "mud", "steam", "mist", "kodai" } : new String[]{ "water" };
				break;
			case ItemID.FIRE_RUNE:
				typeNames = allowVariants ? new String[]{ "fire", "lava", "steam", "smoke" } : new String[]{ "fire" };
				break;
			default:
				return false;
		}
		int wepId = equipmentUtils.getWornItemId(KitType.WEAPON);
		if (wepId > 0)
		{
			ItemDefinition wornWeaponDef = client.getItemDefinition(wepId);
			String name = wornWeaponDef.getName();
			if (name != null)
			{
				for (String type : typeNames)
				{
					if (name.toLowerCase().contains(type.toLowerCase()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isElementalRune(int runeId)
	{
		return runeId == ItemID.AIR_RUNE || runeId == ItemID.EARTH_RUNE || runeId == ItemID.WATER_RUNE || runeId == ItemID.FIRE_RUNE;
	}

	public List<Integer> getVariantRuneIds(int runeId)
	{
		switch (runeId)
		{
			case ItemID.AIR_RUNE:
				return List.of(ItemID.AIR_RUNE, ItemID.DUST_RUNE, ItemID.SMOKE_RUNE, ItemID.MIST_RUNE);
			case ItemID.EARTH_RUNE:
				return List.of(ItemID.EARTH_RUNE, ItemID.DUST_RUNE, ItemID.LAVA_RUNE, ItemID.MUD_RUNE);
			case ItemID.WATER_RUNE:
				return List.of(ItemID.WATER_RUNE, ItemID.MUD_RUNE, ItemID.STEAM_RUNE, ItemID.MIST_RUNE);
			case ItemID.FIRE_RUNE:
				return List.of(ItemID.FIRE_RUNE, ItemID.LAVA_RUNE, ItemID.STEAM_RUNE, ItemID.SMOKE_RUNE);
			default:
				return new ArrayList<>();
		}
	}

	/**
	 *
	 * @param elementalRuneId The elemental rune item id to check for.
	 * @param minQuantity How many of this rune in inventory / rune pouch.
	 * @return Whether or not the player has the elemental rune quantity through itself and variants
	 * Cumulatively counts each variant towards the minimum, for example 1 air rune + 2 smoke runes = 3 total air runes.
	 */
	private boolean hasElementalRune(int elementalRuneId, int minQuantity, boolean includeRunePouch)
	{
		int[] ids;
		switch (elementalRuneId)
		{
			case ItemID.AIR_RUNE:
				ids = new int[]{ ItemID.AIR_RUNE, ItemID.DUST_RUNE, ItemID.SMOKE_RUNE, ItemID.MIST_RUNE };
				break;
			case ItemID.EARTH_RUNE:
				ids = new int[]{ ItemID.EARTH_RUNE, ItemID.DUST_RUNE, ItemID.LAVA_RUNE, ItemID.MUD_RUNE };
				break;
			case ItemID.WATER_RUNE:
				ids = new int[]{ ItemID.WATER_RUNE, ItemID.MUD_RUNE, ItemID.STEAM_RUNE, ItemID.MIST_RUNE };
				break;
			case ItemID.FIRE_RUNE:
				ids = new int[]{ ItemID.FIRE_RUNE, ItemID.LAVA_RUNE, ItemID.STEAM_RUNE, ItemID.SMOKE_RUNE };
				break;
			default:
				return false;
		}

		int accumulator = 0;
		boolean checkRunePouch = includeRunePouch && containsItem(ItemID.RUNE_POUCH);

		for (int id : ids)
		{
			accumulator += getItemCount(id, true);

			if (checkRunePouch)
			{
				accumulator += runePouchQuanitity(id);
			}

			if (accumulator >= minQuantity)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @param runeId The itemId of the rune
	 * @param minQuantity Minimum quantity of runes to pass
	 * @param allowVariants Allow variants of elemental runes, e.g. smoke, lava
	 * @param includeRunePouch Check for runes in the rune pouch, requires rune pouch in inventory
	 * @return Whether or not the player has enough of the right runes to pass
	 */
	public boolean hasRune(int runeId, int minQuantity, boolean allowVariants, boolean includeRunePouch)
	{
		if (runeId <= 0)
		{
			return false;
		}

		// Check inventory,
		// Then runepouch + inventory (if applicable)
		// Then elemental staffs & variants (if applicable)
		int inventoryQuantity = getItemCount(runeId, true);
		if (inventoryQuantity >= minQuantity || (includeRunePouch && (containsItem(ItemID.RUNE_POUCH) && inventoryQuantity + runePouchQuanitity(runeId) >= minQuantity)))
		{
			return true;
		}

		if (runeId == ItemID.AIR_RUNE || runeId == ItemID.EARTH_RUNE || runeId == ItemID.WATER_RUNE || runeId == ItemID.FIRE_RUNE) // Elemental: check for variants in inventory, rune pouch, and equipment
		{
			// Staffs
			if (hasStaffEquipped(runeId, allowVariants))
			{
				return true;
			}

			// Tome of fire with 1 page = unlimited fire runes
			if (runeId == ItemID.FIRE_RUNE && equipmentUtils.isItemEquipped(Collections.singleton(ItemID.TOME_OF_FIRE)))
			{
				return true;
			}

			if (allowVariants)
			{
				return hasElementalRune(runeId, minQuantity, includeRunePouch);
			}
		}

		return false;
	}

	// Basic hasRune method, only 1 needed to satisfy, does not care for variants and checks rune pouch.
	public boolean hasRune(int runeId)
	{
		return hasRune(runeId, 1, false, true);
	}

	public boolean runePouchContains(int id)
	{
		Set<Integer> runePouchIds = new HashSet<>();
		if (client.getVar(Varbits.RUNE_POUCH_RUNE1) != 0)
		{
			runePouchIds.add(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE1)).getItemId());
		}
		if (client.getVar(Varbits.RUNE_POUCH_RUNE2) != 0)
		{
			runePouchIds.add(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE2)).getItemId());
		}
		if (client.getVar(Varbits.RUNE_POUCH_RUNE3) != 0)
		{
			runePouchIds.add(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE3)).getItemId());
		}
		for (int runePouchId : runePouchIds)
		{
			if (runePouchId == id)
			{
				return true;
			}
		}
		return false;
	}

	public boolean runePouchContains(Collection<Integer> ids)
	{
		for (int runeId : ids)
		{
			if (!runePouchContains(runeId))
			{
				return false;
			}
		}
		return true;
	}

	public int runePouchQuanitity(int id)
	{
		Map<Integer, Integer> runePouchSlots = new HashMap<>();
		if (client.getVar(Varbits.RUNE_POUCH_RUNE1) != 0)
		{
			runePouchSlots.put(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE1)).getItemId(), client.getVar(Varbits.RUNE_POUCH_AMOUNT1));
		}
		if (client.getVar(Varbits.RUNE_POUCH_RUNE2) != 0)
		{
			runePouchSlots.put(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE2)).getItemId(), client.getVar(Varbits.RUNE_POUCH_AMOUNT2));
		}
		if (client.getVar(Varbits.RUNE_POUCH_RUNE3) != 0)
		{
			runePouchSlots.put(Runes.getRune(client.getVar(Varbits.RUNE_POUCH_RUNE3)).getItemId(), client.getVar(Varbits.RUNE_POUCH_AMOUNT3));
		}
		if (runePouchSlots.containsKey(id))
		{
			return runePouchSlots.get(id);
		}
		return 0;
	}

	/* Interaction methods */

	public void dropItem(WidgetItem item)
	{
		assert !client.isClientThread();

		menu.setEntry(new MenuEntry("", "", item.getId(), MenuOpcode.ITEM_DROP.getId(), item.getIndex(), 9764864, false));
		mouse.click(item.getCanvasBounds());
	}

	public void dropItems(Collection<Integer> ids, boolean dropAll, int minDelayBetween, int maxDelayBetween)
	{
		if (bank.isOpen() || bank.isDepositBoxOpen())
		{
			log.info("can't drop item, bank is open");
			return;
		}
		Collection<WidgetItem> inventoryItems = getAllItems();
		executorService.submit(() ->
		{
			try
			{
				iterating = true;
				for (WidgetItem item : inventoryItems)
				{
					if (ids.contains(item.getId())) //6512 is empty widget slot
					{
						log.info("dropping item: " + item.getId());
						sleep(minDelayBetween, maxDelayBetween);
						dropItem(item);
						if (!dropAll)
						{
							break;
						}
					}
				}
				iterating = false;
			}
			catch (Exception e)
			{
				iterating = false;
				e.printStackTrace();
			}
		});
	}

	public void dropAllExcept(Collection<Integer> ids, boolean dropAll, int minDelayBetween, int maxDelayBetween)
	{
		if (bank.isOpen() || bank.isDepositBoxOpen())
		{
			log.info("can't drop item, bank is open");
			return;
		}
		Collection<WidgetItem> inventoryItems = getAllItems();
		executorService.submit(() ->
		{
			try
			{
				iterating = true;
				for (WidgetItem item : inventoryItems)
				{
					if (ids.contains(item.getId()))
					{
						log.info("not dropping item: " + item.getId());
						continue;
					}
					sleep(minDelayBetween, maxDelayBetween);
					dropItem(item);
					if (!dropAll)
					{
						break;
					}
				}
				iterating = false;
			}
			catch (Exception e)
			{
				iterating = false;
				e.printStackTrace();
			}
		});
	}

	public void dropInventory(boolean dropAll, int minDelayBetween, int maxDelayBetween)
	{
		if (bank.isOpen() || bank.isDepositBoxOpen())
		{
			log.info("can't drop item, bank is open");
			return;
		}
		Collection<Integer> inventoryItems = getAllItemIDs();
		dropItems(inventoryItems, dropAll, minDelayBetween, maxDelayBetween);
	}

	public void itemsInteract(Collection<Integer> ids, int opcode, boolean exceptItems, boolean interactAll, int minDelayBetween, int maxDelayBetween)
	{
		Collection<WidgetItem> inventoryItems = getAllItems();
		executorService.submit(() ->
		{
			try
			{
				iterating = true;
				for (WidgetItem item : inventoryItems)
				{
					if ((!exceptItems && ids.contains(item.getId()) || (exceptItems && !ids.contains(item.getId()))))
					{
						log.info("interacting inventory item: {}", item.getId());
						sleep(minDelayBetween, maxDelayBetween);
						menu.setEntry(new MenuEntry("", "", item.getId(), opcode, item.getIndex(), WidgetInfo.INVENTORY.getId(),
							false));
						mouse.click(item.getCanvasBounds());
						if (!interactAll)
						{
							break;
						}
					}
				}
				iterating = false;
			}
			catch (Exception e)
			{
				iterating = false;
				e.printStackTrace();
			}
		});
	}

	public void combineItems(Collection<Integer> ids, int item1ID, int opcode, boolean exceptItems, boolean interactAll, int minDelayBetween, int maxDelayBetween)
	{
		WidgetItem item1 = getWidgetItem(item1ID);
		if (item1 == null)
		{
			log.info("combine item1 item not found in inventory");
			return;
		}
		Collection<WidgetItem> inventoryItems = getAllItems();
		executorService.submit(() ->
		{
			try
			{
				iterating = true;
				for (WidgetItem item : inventoryItems)
				{
					if ((!exceptItems && ids.contains(item.getId()) || (exceptItems && !ids.contains(item.getId()))))
					{
						log.info("interacting inventory item: {}", item.getId());
						sleep(minDelayBetween, maxDelayBetween);
						menu.setModifiedEntry(new MenuEntry("", "", item1.getId(), opcode, item1.getIndex(), WidgetInfo.INVENTORY.getId(),
							false), item.getId(), item.getIndex(), MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
						mouse.click(item1.getCanvasBounds());
						if (!interactAll)
						{
							break;
						}
					}
				}
				iterating = false;
			}
			catch (Exception e)
			{
				iterating = false;
				e.printStackTrace();
			}
		});
	}

}
