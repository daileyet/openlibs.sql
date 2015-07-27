package openthinks.libs.sql;


public class MessageE extends openthinks.libs.sql.entity.Entity{

	private String messageId;
	private String locale;
	private String content;

	/* (non-Javadoc)
	 * @see i18n.IMessage#getContent()
	 */
	public String getContent() {
		return content;
	}

	/* (non-Javadoc)
	 * @see i18n.IMessage#getLocale()
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageE [messageId=" + messageId + ", locale=" + locale + ", content=" + content + "]";
	}
}
