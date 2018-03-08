package com.xhj.imserver.imserver;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChannelGroups {

	private static final Map<String,ChannelId> userList = new ConcurrentHashMap();
	
	private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("ChannelGroups",
			GlobalEventExecutor.INSTANCE);

	public static void putUser(String uname,ChannelId id) {
		userList.put(uname,id);
	}
	
	public static void romveUser(String uname) {
		userList.remove(uname);
	}
	
	public static boolean isContainUname(String uname) {
		if(userList.containsKey(uname)) {
			return true;
		}
		return false;
	}
	
	public static ChannelId getChannelId(String uname) {
		return userList.get(uname);
	}
	
	public static void add(Channel channel) {
		CHANNEL_GROUP.add(channel);
	}
	
	public static Channel getChannel(ChannelId channelId) {
		return CHANNEL_GROUP.find(channelId);
	}

	public static ChannelGroupFuture broadcast(Object msg) {
		return CHANNEL_GROUP.writeAndFlush(msg);
	}

	public static ChannelGroupFuture broadcast(Object msg, ChannelMatcher matcher) {
		return CHANNEL_GROUP.writeAndFlush(msg, matcher);
	}

	public static ChannelGroup flush() {
		return CHANNEL_GROUP.flush();
	}

	public static boolean discard(Channel channel) {
		return CHANNEL_GROUP.remove(channel);
	}

	public static ChannelGroupFuture disconnect() {
		return CHANNEL_GROUP.disconnect();
	}

	public static ChannelGroupFuture disconnect(ChannelMatcher matcher) {
		return CHANNEL_GROUP.disconnect(matcher);
	}

	public static boolean contains(Channel channel) {
		return CHANNEL_GROUP.contains(channel);
	}

	public static int size() {
		return CHANNEL_GROUP.size();
	}

	public static void romveUser(ChannelId id) {
		if(userList.containsValue(id)){
			Iterator<String> iterator = userList.keySet().iterator();
			if(userList.size()>0) {
				while(iterator.hasNext()) {
					String key = iterator.next();
					if(userList.get(key)==id){
						userList.remove(key);
						break;
					}
				}
			}	
		}
	}
}
