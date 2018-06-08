package ds.AuditJar;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;


public class AuditJar implements Serializable{

/*EventId - UUID
eventName - String
eventType - String
eventDate - timestamp
userId - String
oldValue - BaseEntity
newValue - BaseEntity */
	/**-------audit id reference-------.*/
	public String auditId;
	
  /**--------EventId----------.*/
  public Long eventId;
  
  /**------------event  names-----------.*/
  public String eventName;
  


  /**---- EVENT TTYPES----------.*/
  public String eventType;
  
  /**---------EVENT STAMP-----------.*/
  public Date eventDate;
  
  /**------userId---------.*/
  public String userId;
  
  /**--------old value--------------.*/
  public Object oldValue;
  
  /**---------new value ----------.*/
  public Object newValue;
  
	public AuditJar()
	{
	  
	}
	
	
	
  @Override
	public String toString() {
		return "AuditJar [auditId=" + auditId + ", eventId=" + eventId + ", eventName=" + eventName + ", eventType="
				+ eventType + ", eventDate=" + eventDate + ", userId=" + userId + ", oldValue=" + oldValue
				+ ", newValue=" + newValue + "]";
	}



public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
	public String getAuditId() {
    return auditId;
  }

  public void setAuditId(String auditId) {
    this.auditId = auditId;
  }

  public long getEventId() {
    return eventId;
  }

  public void setEventId(long eventId) {
    this.eventId = eventId;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public void setEventDate(Date eventDate) {
    this.eventDate = eventDate;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Object getOldValue() {
    return oldValue;
  }

  public void setOldValue(Object oldValue) {
    this.oldValue = oldValue;
  }

  public Object getNewValue() {
    return newValue;
  }

  public void setNewValue(Object newValue) {
    this.newValue = newValue;
  }



}