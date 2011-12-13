package game.quest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import game.EtothGame;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.IOHelper;
import game.math.Vector2d;
import game.quest.objectives.FindItemObjective;
import game.quest.objectives.HaveFollowing;
import game.quest.objectives.NpcInteractObjective;
import game.quest.objectives.NpcWinObjective;
import game.quest.objectives.QuestObjective;
import game.quest.rewards.AddFollowing;
import game.quest.rewards.ChangeDialogReward;
import game.quest.rewards.CreateInfoReward;
import game.quest.rewards.CreateNpcReward;
import game.quest.rewards.GameOutroReward;
import game.quest.rewards.GetItemReward;
import game.quest.rewards.MsgReward;
import game.quest.rewards.QuestReward;
import game.quest.rewards.RemoveInfoReward;
import game.quest.rewards.RemoveItemReward;
import game.quest.rewards.RemoveNpcReward;
import game.quest.rewards.StopFollowing;
import game.tileObjects.Info;

public class QuestManager {
	
	EtothGame game;
	ArrayList<Quest> quests;
	
	public QuestManager(EtothGame game) throws FolderContainsNoFilesException, 
			CanNotReadFileException, SAXException, IOException, 
			ParserConfigurationException, NotFoundException {
		this.game = game;
		quests = new ArrayList<Quest>();

		File mapFilePath = new File(game.XMLPATH + File.separator + 
				"quests.xml");
		loadXML(mapFilePath);
	}
	
	private void loadXML(File mapFilePath) throws ParserConfigurationException, 
			SAXException, IOException, CanNotReadFileException, 
			NotFoundException, FolderContainsNoFilesException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(mapFilePath);

		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("quest");
		Quest quest;
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			Element fstElmnt = (Element) fstNode;
			
			String startText = fstElmnt.getAttribute("startText");
			String endText = fstElmnt.getAttribute("endText");
			//TODO: error Exception when these are not initialized while parsing
			QuestObjective startObjective = null;
			ArrayList<QuestReward> startRews = new ArrayList<QuestReward>();
			QuestObjective endObjective = null;
			ArrayList<QuestReward> beforeEndRewards = new ArrayList<QuestReward>();
			ArrayList<QuestObjective> objs = new ArrayList<QuestObjective>();
			ArrayList<QuestReward> rews = new ArrayList<QuestReward>();
			
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nodeLst2 = fstNode.getChildNodes();
				for (int j = 0; j < nodeLst2.getLength(); j++) {
					Node fstNode2 = nodeLst2.item(j);
					if (fstNode2.getNodeType() == Node.ELEMENT_NODE) {
						Element fstElmnt2 = (Element) fstNode2;
	
						if (fstElmnt2.getNodeName().equals("startObjective")) {
							startObjective = loadObjective(fstElmnt2);
						} else if (fstElmnt2.getNodeName().equals("startRewards")) {
							NodeList nodeLst5 = fstNode2.getChildNodes();
							for (int k = 0; k < nodeLst5.getLength(); k++) {
								Node fstNode5 = nodeLst5.item(k);
								if (fstNode5.getNodeType() == Node.ELEMENT_NODE) {
									Element fstElmnt5 = (Element) fstNode5;
									if (fstElmnt5.getNodeName().equals("startReward")) {
										QuestReward rew = loadReward(fstElmnt5);
										if (rew == null) {
											System.out.println("error");
										}
										startRews.add(rew);
									}
								}
							}
						} else if (fstElmnt2.getNodeName().equals("endObjective")) {
							endObjective = loadObjective(fstElmnt2);
						} else if (fstElmnt2.getNodeName().equals("beforeEndRewards")) {
							NodeList nodeLst6 = fstNode2.getChildNodes();
							for (int k = 0; k < nodeLst6.getLength(); k++) {
								Node fstNode6 = nodeLst6.item(k);
								if (fstNode6.getNodeType() == Node.ELEMENT_NODE) {
									Element fstElmnt6 = (Element) fstNode6;
									if (fstElmnt6.getNodeName().equals("beforeEndReward")) {
										QuestReward rew = loadReward(fstElmnt6);
										if (rew == null) {
											System.out.println("error");
										}
										beforeEndRewards.add(rew);
									}
								}
							}
						} else if (fstElmnt2.getNodeName().equals("objectives")) {
							NodeList nodeLst3 = fstNode2.getChildNodes();
							for (int k = 0; k < nodeLst3.getLength(); k++) {
								Node fstNode3 = nodeLst3.item(k);
								if (fstNode3.getNodeType() == Node.ELEMENT_NODE) {
									Element fstElmnt3 = (Element) fstNode3;
									if (fstElmnt3.getNodeName().equals("objective")) {
										QuestObjective obj = loadObjective(fstElmnt3);
										if (obj == null) {
											System.out.println("error");
										}
										objs.add(obj);
									}
								}
							}
						} else if (fstElmnt2.getNodeName().equals("rewards")) {
							NodeList nodeLst4 = fstNode2.getChildNodes();
							for (int k = 0; k < nodeLst4.getLength(); k++) {
								Node fstNode4 = nodeLst4.item(k);
								if (fstNode4.getNodeType() == Node.ELEMENT_NODE) {
									Element fstElmnt4 = (Element) fstNode4;
									if (fstElmnt4.getNodeName() == "reward") {
										QuestReward rew = loadReward(fstElmnt4);
										if (rew == null) {
											System.out.println("error");
										}
										rews.add(rew);
									}
								}
							}
						}	
					}
				}
			}
			quest = new Quest(game, startText, endText, startObjective, 
					endObjective);
			for (QuestObjective questObjective : objs) {
				quest.addObjective(questObjective);
			}
			for (QuestReward questReward : rews) {
				quest.addReward(questReward);
			}
			for (QuestReward questStartReward : startRews) {
				quest.addStartRewards(questStartReward);
			}
			for (QuestReward beforeEndReward : beforeEndRewards) {
				quest.addBeforeEndRewards(beforeEndReward);
			}
			quests.add(quest);
		}
		
	}

	private QuestObjective loadObjective(Element ele) {
		String questObjType = ele.getAttribute("objType");
		QuestObjective questObj = null;
		if (questObjType.equals("npcInteract")) {
			String mapName = ele.getAttribute("mapName");
			int npcId = Integer.parseInt((String) ele.getAttribute("npcId"));
			questObj = new NpcInteractObjective(game, mapName, npcId);
		} else if (questObjType.equals("npcWin")) {
			String mapName = ele.getAttribute("mapName");
			int npcId = Integer.parseInt((String) ele.getAttribute("npcId"));
			questObj = new NpcWinObjective(game, mapName, npcId);
		} else if (questObjType.equals("findItem")) {
			int itemType = Integer.parseInt((String) ele.getAttribute("itemType"));
			questObj = new FindItemObjective(game, itemType);
		} else if (questObjType.equals("haveFollowing")) {
			String mapName = ele.getAttribute("mapName");
			int npcType = Integer.parseInt((String) ele.getAttribute("npcType"));
			questObj = new HaveFollowing(game, mapName, npcType);
		} else if (questObjType.equals("null")) {
			questObj = null;
		} else {
			//TODO: exception/error
			System.out.println("no cat: " + questObjType);
		}
		return questObj;
	}

	private QuestReward loadReward(Element ele) throws CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException, 
			NotFoundException, FolderContainsNoFilesException {
		String questRewType = ele.getAttribute("rewType");
		QuestReward questRew = null;
		if (questRewType.equals("changeDialog")) {
			String mapName = ele.getAttribute("mapName");
			int npcId = Integer.parseInt((String) ele.getAttribute("npcId"));
			String dialogPath = ele.getAttribute("dialogPath");
			questRew = new ChangeDialogReward(game, mapName, npcId, dialogPath);
		} else if (questRewType.equals("getItem")) {
			int itemType = Integer.parseInt((String) ele.getAttribute("itemType"));
			questRew = new GetItemReward(game, itemType);
		} else if (questRewType.equals("removeNpc")) {
			String mapName = ele.getAttribute("mapName");
			int npcId = Integer.parseInt((String) ele.getAttribute("npcId"));
			questRew = new RemoveNpcReward(game, mapName, npcId);
		} else if (questRewType.equals("createNpc")) {
			String mapName = ele.getAttribute("mapName");
			int type = Integer.parseInt((String) ele.getAttribute("type"));
			int x = Integer.parseInt((String) ele.getAttribute("x"));
			int y = Integer.parseInt((String) ele.getAttribute("y"));
			Vector2d tilePos = new Vector2d(x, y);
			String dialogPath = ele.getAttribute("dialogPath");
			String direction = ele.getAttribute("direction");
			Boolean attacks = IOHelper.XMLreadBoolean(ele, "attacks");
			questRew = new CreateNpcReward(game, mapName, type, tilePos, dialogPath, direction, attacks);
		} else if (questRewType.equals("createInfo")){
			File imgPath = new File (game.INOFSIMGPATH + File.separator 
					+ ele.getAttribute("img"));
			BufferedImage[] images = IOHelper.getImages(game, imgPath);
			int x = Integer.parseInt((String) ele.getAttribute("x"));
			int y = Integer.parseInt((String) ele.getAttribute("y"));
			Vector2d tilePos = new Vector2d(x, y);
			boolean solid = IOHelper.XMLreadBooleanSafe(ele, "solid");
			String infoStr = ele.getAttribute("infoStr");
			boolean light = IOHelper.XMLreadBooleanSafe(ele, "light");
			Info info = new Info(game, images, tilePos, solid, infoStr, light);
			String mapName = ele.getAttribute("mapName");
			questRew = new CreateInfoReward(game, mapName, info);
		} else if (questRewType.equals("removeItem")) {
			int itemType = Integer.parseInt((String) ele.getAttribute("itemType"));
			questRew = new RemoveItemReward(game, itemType);
		} else if (questRewType.equals("gameOutro")) {
			questRew = new GameOutroReward(game);
		} else if (questRewType.equals("msg")) {
			String text = ele.getAttribute("text");
			questRew = new MsgReward(game, text);
		} else if (questRewType.equals("removeInfo")) {
			String mapName = ele.getAttribute("mapName");
			int x = Integer.parseInt((String) ele.getAttribute("x"));
			int y = Integer.parseInt((String) ele.getAttribute("y"));
			Vector2d infoPos = new Vector2d(x, y);
			questRew = new RemoveInfoReward(game, mapName, infoPos);
		} else if (questRewType.equals("npcFollow")) {
			String mapName = ele.getAttribute("mapName");
			int npcId = Integer.parseInt((String) ele.getAttribute("npcId"));
			questRew = new AddFollowing(game, mapName, npcId);
		} else if (questRewType.equals("stopFollowing")) {
			questRew = new StopFollowing(game);
		} else if (questRewType.equals("null")) {
			questRew = null;
		} else {
			//TODO: exception/error
			System.out.println("no cat: " + questRewType);
		}
		return questRew;
	}

	public void update(long elapsedTime) throws Exception {
		for (Quest quest : quests) {
			quest.update(elapsedTime);
		}
	}
	
	public void updateNpcInteract(int npcMapId) throws Exception {
		for (Quest quest : quests) {
			quest.updateNpcInteract(npcMapId);
		}
	}
	
	public void updateNpcWin(int npcMapId) throws Exception {
		for (Quest quest : quests) {
			quest.updateNpcWin(npcMapId);
		}
	}
}
