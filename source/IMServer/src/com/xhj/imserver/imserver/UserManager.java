package com.xhj.imserver.imserver;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.xhj.imserver.utils.Log;
import io.netty.channel.socket.SocketChannel;

public class UserManager {

	private static UserManager manager;
	private static Map<String, SocketChannel> userList = new ConcurrentHashMap();
	private static Map<String, CopyOnWriteArrayList<String>> groupList = new ConcurrentHashMap();

	private UserManager() {
	}

	public static UserManager getInstance() {
		if (manager == null) {
			synchronized (UserManager.class) {
				if (manager == null) {
					manager = new UserManager();
				}
			}
		}
		return manager;
	}

	@SuppressWarnings("unchecked")
	public void addUser(String groupID, String clientID, SocketChannel channel) {
		userList.put(clientID, channel);
		if (groupList.get(groupID) == null) {
			Log.debug("addUser()... create new group-> " + groupID + ",  " + clientID);
			CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<String>();
			users.add(clientID);
			groupList.put(groupID, users);
		} else {
			Log.debug("addUser()... " + groupID + ",  " + clientID);
			groupList.get(groupID).add(clientID);
		}
	}

	public SocketChannel getUserChannel(String clientID) {
		return (SocketChannel) userList.get(clientID);
	}

	public void removeUser(String groupID, String clientID) {
		Log.debug("removeUser()... " + groupID + ",  " + clientID);
		userList.remove(clientID);
		CopyOnWriteArrayList<String> list = groupList.get(groupID);
		int count = list.size();
		for (int i = 0; i < count; i++) {
			if (list.get(i).equalsIgnoreCase(clientID)) {
				groupList.get(groupID).remove(i);
				break;
			}
		}
	}

	public void removeChannel(SocketChannel channel) {
		Iterator<Entry<String, SocketChannel>> entries = userList.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, SocketChannel> entry = entries.next();
			if (entry.getValue().equals(channel)) {
				entries.remove();
				Log.debug("removeChannel()... " + entry.getKey());
				return;
			}
		}
	}

	/**
	 * work in single thread.
	 * 
	 * @param groupID
	 * @return
	 */
	public CopyOnWriteArrayList<String> getUserListInGroup(String groupID) {
		return groupList.get(groupID);
	}

	public int getTotalUserCount() {
		return userList.size();
	}

	public int getGroupSize(String groupID) {
		return groupList.get(groupID).size();
	}

	public void clearGroup(String groupID) {
		groupList.get(groupID).clear();
	}

	public void clearAll() {
		groupList.clear();
		userList.clear();
	}
}
