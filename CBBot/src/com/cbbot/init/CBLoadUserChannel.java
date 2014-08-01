package com.cbbot.init;

import java.util.List;

import com.cbbot.CBInfo;
import com.cbbot.channel.CBChannel;
import com.cbbot.channel.CBUserChannel;
import com.cbbot.user.CBUser;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelGroupClient;

public class CBLoadUserChannel extends CBLoad{

	public CBLoadUserChannel(CBInfo info) {
		this.loadAll(info);
	}

	private void loadAll(CBInfo info){
		TS3Api api = info.getApi();
		
		for(int j = 0; j < info.getUserChannel().getSubChannels().size(); j++){
			//Der Channel im UserChannel bereich
			CBChannel channel = info.getUserChannel().getSubChannels().get(j);
			//Setze diesen Channel als UserChannel
			channel.setUserChannel(true);
			//Erstelle einen neuen UserChannel
			CBUserChannel userChannel = new CBUserChannel(info, channel.getChannelDatabaseID());
			//Hole die Liste aller Clients mit einer Channel Gruppe
			List<ChannelGroupClient> channelGroupClients = api.getChannelGroupClientsByChannelId(userChannel.getChannelDatabaseID());
			
			for(int i = 0; i < channelGroupClients.size(); i++){
				//Hole mir den User
				CBUser user = info.getUserByClientDatabaseID(channelGroupClients.get(i).getClientDatabaseId());
				//Meine abfrage ob der User Online ist oder nicht
				if(user != null && channelGroupClients.get(i).getChannelId() == userChannel.getChannelDatabaseID()){
					//Füge diesen User dem Channel hinzu
					userChannel.getChannelAdmins().add(user);
					//Füge diesen UserChannel dem User mit der ChannelAdmin Gruppe hinzu
					info.getUserByClientDatabaseID(channelGroupClients.get(i).getClientDatabaseId()).addPrivateChannel(userChannel);
				}
				
			}
			//Füge den Channel dem UserChannel Bereich hinzu
			info.getUserChannels().add(userChannel);
		}
		//Für alle User channel
		for(int i = 0; i < info.getUserChannels().size(); i++){
			//Hole mir die ChannelGruppenClient Liste anhand des UserChannels
			List<ChannelGroupClient> channelGroupClients = api.getChannelGroupClientsByChannelId(info.getUserChannels().get(i).getChannelDatabaseID());
			//solange j kleiner als die ChannelGruppen Liste ist
			for(int j = 0; j < channelGroupClients.size(); j++){
				//Erstelle die Fehlenden einträge
				info.getUserChannels().get(i).createDB(info);
			}
		}
	}
}
