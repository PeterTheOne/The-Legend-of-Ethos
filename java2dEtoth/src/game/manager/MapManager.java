package game.manager;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.EtothGame;
import game.Map;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.IsNoDirectoryException;
import game.exceptions.NotFoundException;
import game.fileFilters.XMLFileFilter;

public class MapManager {

	private EtothGame game;
	private ArrayList<Map> maps;
	private Map currentMap;

	public MapManager(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException, CanNotReadFileException, 
			IsNoDirectoryException, FolderContainsNoFilesException, 
			NotFoundException {
		this.game = game;
		loadMapsArray();
		currentMap = getMap(game.STARTMAPFILENAME);
	}

	private void loadMapsArray() throws ParserConfigurationException, 
			SAXException, IOException, CanNotReadFileException, 
			IsNoDirectoryException, FolderContainsNoFilesException, 
			NotFoundException {
		File mapsPathAbs = game.MAPPATH.getAbsoluteFile();
		if (!mapsPathAbs.isDirectory()) {
			throw new IsNoDirectoryException();
		}

		maps = new ArrayList<Map>();
		File[] fileList = mapsPathAbs.listFiles(new XMLFileFilter());
		if (((Integer)fileList.length).equals(0)) {
			throw new FolderContainsNoFilesException();
		}
		for (int i = 0; i < fileList.length; i++) {
			maps.add(new Map(game, fileList[i]));
		}
	}

	public Map getMap(String name) throws NotFoundException {
		for (Map map : maps) {
			if(map.getName().equals(name)) {
				return map;
			}
		}
		throw new NotFoundException("Map not Found");
	}

	public Map getCurrentMap() {
		return currentMap;
	}

	public void update(long elapsedTime) throws Exception {
		currentMap.update(elapsedTime);
	}

	public void render(Graphics2D g) {
		currentMap.render(g);
	}

	public void changeMap(String target) throws NotFoundException {
		currentMap = getMap(target);
		currentMap.playEnterMapSound();
	}
}
