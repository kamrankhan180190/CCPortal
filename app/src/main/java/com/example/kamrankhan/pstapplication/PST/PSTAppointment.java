
package com.example.kamrankhan.pstapplication.PST;

import com.example.kamrankhan.pstapplication.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;


public class PSTAppointment extends PSTMessage {

	PSTAppointment(PSTFile theFile, com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode descriptorIndexNode)
		throws PSTException, IOException
	{
		super(theFile, descriptorIndexNode);
	}

	PSTAppointment(PSTFile theFile, com.example.kamrankhan.pstapplication.PST.DescriptorIndexNode folderIndexNode, PSTTableBC table, HashMap<Integer, PSTDescriptorItem> localDescriptorItems)
	{
		super(theFile, folderIndexNode, table, localDescriptorItems);
	}

	public boolean getSendAsICAL() {
		return (getBooleanItem(pstFile.getNameToIdMapItem(0x00008200, PSTFile.PSETID_Appointment)));
	}
	public int getBusyStatus()
	{
		return getIntItem(pstFile.getNameToIdMapItem(0x00008205, PSTFile.PSETID_Appointment));
	}
	public boolean getShowAsBusy() {
		return getBusyStatus() == 2;
	}
	public String getLocation() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008208, PSTFile.PSETID_Appointment));
	}
	public Date getStartTime() {
		return getDateItem(pstFile.getNameToIdMapItem(0x0000820d, PSTFile.PSETID_Appointment));
	}
	public PSTTimeZone getStartTimeZone() {
		return getTimeZoneItem(pstFile.getNameToIdMapItem(0x0000825e, PSTFile.PSETID_Appointment));
	}
	public Date getEndTime() {
		return getDateItem(pstFile.getNameToIdMapItem(0x0000820e, PSTFile.PSETID_Appointment));
	}
	public PSTTimeZone getEndTimeZone() {
		return getTimeZoneItem(pstFile.getNameToIdMapItem(0x0000825f, PSTFile.PSETID_Appointment));
	}
	
	public PSTTimeZone getRecurrenceTimeZone() {
		String desc = getStringItem(pstFile.getNameToIdMapItem(0x00008234, PSTFile.PSETID_Appointment));
		if ( desc!= null && desc.length() != 0 ) {
			byte[] tzData = getBinaryItem(pstFile.getNameToIdMapItem(0x00008233, PSTFile.PSETID_Appointment));
			if ( tzData != null && tzData.length != 0 ) {
				return new PSTTimeZone(desc, tzData);
			}
		}
		return null;
	}
	public int getDuration() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008213, PSTFile.PSETID_Appointment));
	}
	public int getColor() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008214, PSTFile.PSETID_Appointment));
	}
	public boolean getSubType() {
		return (getIntItem(pstFile.getNameToIdMapItem(0x00008215, PSTFile.PSETID_Appointment)) != 0);
	}
	public int getMeetingStatus() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008217, PSTFile.PSETID_Appointment));
	}
	public int getResponseStatus() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008218, PSTFile.PSETID_Appointment));
	}
	public boolean isRecurring() {
		return getBooleanItem(pstFile.getNameToIdMapItem(0x00008223, PSTFile.PSETID_Appointment));
	}
	public Date getRecurrenceBase() {
		return getDateItem(pstFile.getNameToIdMapItem(0x00008228, PSTFile.PSETID_Appointment));
	}
	public int getRecurrenceType() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008231, PSTFile.PSETID_Appointment));
	}
	public String getRecurrencePattern() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008232, PSTFile.PSETID_Appointment));
	}
	public byte[] getRecurrenceStructure() {
		return getBinaryItem(pstFile.getNameToIdMapItem(0x00008216, PSTFile.PSETID_Appointment));
	}
	public byte[] getTimezone() {
		return getBinaryItem(pstFile.getNameToIdMapItem(0x00008233, PSTFile.PSETID_Appointment));
	}
	public String getAllAttendees() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008238, PSTFile.PSETID_Appointment));
	}
	public String getToAttendees() {
		return getStringItem(pstFile.getNameToIdMapItem(0x0000823b, PSTFile.PSETID_Appointment));
	}
	public String getCCAttendees() {
		return getStringItem(pstFile.getNameToIdMapItem(0x0000823c, PSTFile.PSETID_Appointment));
	}
	public int getAppointmentSequence() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008201, PSTFile.PSETID_Appointment));
	}
	
	// online meeting properties
	public boolean isOnlineMeeting() {
		return (getBooleanItem(pstFile.getNameToIdMapItem(0x00008240, PSTFile.PSETID_Appointment)));
	}
	public int getNetMeetingType() {
		return getIntItem(pstFile.getNameToIdMapItem(0x00008241, PSTFile.PSETID_Appointment));
	}
	public String getNetMeetingServer() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008242, PSTFile.PSETID_Appointment));
	}
	public String getNetMeetingOrganizerAlias() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008243, PSTFile.PSETID_Appointment));
	}
	public boolean getNetMeetingAutostart() {
		return (getIntItem(pstFile.getNameToIdMapItem(0x00008245, PSTFile.PSETID_Appointment)) != 0);
	}
	public boolean getConferenceServerAllowExternal() {
		return (getBooleanItem(pstFile.getNameToIdMapItem(0x00008246, PSTFile.PSETID_Appointment)));
	}
	public String getNetMeetingDocumentPathName() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008247, PSTFile.PSETID_Appointment));
	}
	public String getNetShowURL() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008248, PSTFile.PSETID_Appointment));
	}
	public Date getAttendeeCriticalChange() {
		return getDateItem(pstFile.getNameToIdMapItem(0x00000001, PSTFile.PSETID_Meeting));
	}
	public Date getOwnerCriticalChange() {
		return getDateItem(pstFile.getNameToIdMapItem(0x0000001a, PSTFile.PSETID_Meeting));
	}
	public String getConferenceServerPassword() {
		return getStringItem(pstFile.getNameToIdMapItem(0x00008249, PSTFile.PSETID_Appointment));
	}
	
	public boolean getAppointmentCounterProposal() {
		return (getBooleanItem(pstFile.getNameToIdMapItem(0x00008257, PSTFile.PSETID_Appointment)));
	}

	public boolean isSilent() {
		return (getBooleanItem(pstFile.getNameToIdMapItem(0x00000004, PSTFile.PSETID_Meeting)));
	}

	public String getRequiredAttendees() {
		return getStringItem(this.pstFile.getNameToIdMapItem(0x00000006, PSTFile.PSETID_Meeting));
	}
	
	public int getLocaleId() {
		return getIntItem(0x3ff1);
	}

	public byte[] getGlobalObjectId() {
		return getBinaryItem(pstFile.getNameToIdMapItem(0x00000003, PSTFile.PSETID_Meeting));
	}
}
