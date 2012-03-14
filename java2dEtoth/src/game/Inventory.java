package game;

import game.exceptions.FolderContainsNoFilesException;
import game.tileObjects.Item;
import game.tileObjects.MonsterItem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Inventory {

	//TODO:cleanup!!
	
	private EtothGame game;
	private ArrayList<ArrayList<Item>> items;
	
	public Inventory(EtothGame game) {
		this.game = game;
		items = new ArrayList<ArrayList<Item>>();
	}
	
	public ArrayList<ArrayList<Item>> getInventory() {
		sortInventory();
		return items;
	}

	public ArrayList<ArrayList<Item>> getMonsterInventory() {
		sortInventory();
		ArrayList<ArrayList<Item>> inv = new ArrayList<ArrayList<Item>>();
		for (ArrayList<Item> arrayListOfItems : items) {
			inv.add(new ArrayList<Item>());
			for (Item item : arrayListOfItems) {
				inv.get(inv.size() - 1).add(item.clone());
			}
		}
		ArrayList<ArrayList<Item>> removeList = new ArrayList<ArrayList<Item>>();
		for (ArrayList<Item> arrayListOfItems : inv) {
			if (arrayListOfItems.get(0) instanceof MonsterItem) {
			} else {

				removeList.add(arrayListOfItems);
			}
		}
		
		for (ArrayList<Item> arrayList : removeList) {
			inv.remove(arrayList);
		}
		return inv;
	}
	
	private void sortInventory() {
		if (items.size() > 1) {
			boolean switchOperation;
			do {
				switchOperation = false;
				for (int i = 0; i < items.size() - 1; i++) {
					if (
							items.get(i).get(0).getName().compareTo(
									items.get(i + 1).get(0).getName()
								)
						>= 0
					) {
						switchOperation = true;
						ArrayList<Item> tmp = items.get(i);
						//TODO: Workkks?
						items.set(i, items.get(i + 1));
						items.set(i + 1, tmp);
					}
				}
			} while (switchOperation);
		}
	}

	public void removeFromInventory(int selected) {
		ArrayList<Item> aLI = items.get(selected);
		int size = aLI.size();
		if (size > 1) {
			aLI.remove(size - 1);
		} else {
			items.remove(selected);
		}
	}

	public boolean isTypeInInventory(int itemType) {
		for (ArrayList<Item> arrayListOfItems : items) {
			if (arrayListOfItems.get(0).getType() == itemType) {
				return true;
			}
		}
		return false;
	}

	public void addToInventory(Item item, boolean msg) 
			throws FolderContainsNoFilesException {
		if (item != null) {
			if (msg) {
				String info = game.gameTexts.getText("itemfound") + item.toString();
				game.msgMana.setMsg(info);
			}
			game.mapMana.getCurrentMap().removeItem(item);
			
			game.gameSounds.playSound("itemFound");
			
			for (ArrayList<Item> arrayListOfItems : items) {
				if (arrayListOfItems.get(0).getType() == item.getType()) {
					arrayListOfItems.add(item);
					return;
				}
			}
			ArrayList<Item> arrayListOfItems = new ArrayList<Item>();
			arrayListOfItems.add(item);
			items.add(arrayListOfItems);
		}
	}

	public void removeFromInventoryByType(int itemType) {
		int i = -1;
		for (ArrayList<Item> arrayListOfItems : items) {
			if (arrayListOfItems.get(0).getType() == itemType) {
				i = items.indexOf(arrayListOfItems);
				break;
			}
		}
		if (i != -1) {
			removeFromInventory(i);
		}
	}
	
	public int getMonsterCount() {
		int result = 0;
		for (ArrayList<Item> arrayListOfItems : items) {
			if (arrayListOfItems.get(0) instanceof MonsterItem) {
				result++;
			}
		}
		return result;
	}
	
	public int getSize() {
		return items.size();
	}
	
	
	
}
