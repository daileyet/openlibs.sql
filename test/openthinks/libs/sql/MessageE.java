package openthinks.libs.sql;

public class MessageE extends openthinks.libs.sql.entity.Entity {

	private String message_id;
	private String message_locale;
	private String message_content;

	/* (non-Javadoc)
	 * @see i18n.IMessage#getContent()
	 */
	public String getContent() {
		return message_content;
	}

	/* (non-Javadoc)
	 * @see i18n.IMessage#getLocale()
	 */
	public String getLocale() {
		return message_locale;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return message_id;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.message_id = messageId;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.message_locale = locale;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.message_content = content;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageE [messageId=" + message_id + ", locale=" + message_locale + ", content=" + message_content + "]";
	}
}
