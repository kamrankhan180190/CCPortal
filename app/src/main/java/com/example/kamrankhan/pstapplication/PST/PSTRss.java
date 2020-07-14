
package com.example.kamrankhan.pstapplication.PST;

import com.example.kamrankhan.pstapplication.PST.*;
import com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode;
import com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem;
import com.example.kamrankhan.pstapplication.PST.PSTException;
import com.example.kamrankhan.pstapplication.PST.PSTFile;

import java.io.IOException;
import java.util.HashMap;


public class PSTRss extends com.example.kamrankhan.pstapplication.PST.PSTMessage {

	/**
	 * @param theFile
	 * @param descriptorIndexNode
	 * @throws com.example.kamrankhan.pstapplication.PST.PSTException
	 * @throws IOException
	 */
	public PSTRss(com.example.kamrankhan.pstapplication.PST.PSTFile theFile, com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode descriptorIndexNode)
			throws PSTException, IOException {
		super(theFile, descriptorIndexNode);
	}

	/**
	 * @param theFile
	 * @param folderIndexNode
	 * @param table
	 * @param localDescriptorItems
	 */
	public PSTRss(com.example.kamrankhan.pstapplication.PST.PSTFile theFile, DescriptorIndexNode folderIndexNode,
				  PSTTableBC table,
				  HashMap<Integer, com.example.kamrankhan.pstapplication.PST.PSTDescriptorItem> localDescriptorItems) {
		super(theFile, folderIndexNode, table, localDescriptorItems);
	}
	
	/**
	 * Channel
	 */
	public String getPostRssChannelLink() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008900, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Item link
	 */
	public String getPostRssItemLink() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008901, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Item hash Integer 32-bit signed
	 */
	public int getPostRssItemHash() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008902, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Item GUID
	 */
	public String getPostRssItemGuid() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008903, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Channel GUID
	 */
	public String getPostRssChannel() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008904, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Item XML
	 */
	public String getPostRssItemXml() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008905, com.example.kamrankhan.pstapplication.PST.PSTFile.PSETID_PostRss));
	}
	/**
	 * Subscription
	 */
	public String getPostRssSubscription() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008906, PSTFile.PSETID_PostRss));
	}

	public String toString() {
		return
		 "Channel ASCII or Unicode string values: "+ getPostRssChannelLink() + "\n" +
		 "Item link ASCII or Unicode string values: "+ getPostRssItemLink() + "\n" +
		 "Item hash Integer 32-bit signed: "+ getPostRssItemHash() + "\n" +
		 "Item GUID ASCII or Unicode string values: "+ getPostRssItemGuid() + "\n" +
		 "Channel GUID ASCII or Unicode string values: "+ getPostRssChannel() + "\n" +
		 "Item XML ASCII or Unicode string values: "+ getPostRssItemXml() + "\n" +
		 "Subscription ASCII or Unicode string values: "+ getPostRssSubscription();
	}
}
