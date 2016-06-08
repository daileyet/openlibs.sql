package com.openthinks.libs.sql;

import com.openthinks.libs.sql.entity.Entity;

public class Message extends Entity {

	private String message_id;
	private String message_locale;
	private String message_content;

	public String getContent() {
		return message_content;
	}

	public String getLocale() {
		return message_locale;
	}

	public String getId() {
		return message_id;
	}

	public void setId(String messageId) {
		this.message_id = messageId;
	}

	public void setLocale(String locale) {
		this.message_locale = locale;
	}

	public void setContent(String content) {
		this.message_content = content;
	}

	@Override
	public String toString() {
		return "MessageE [message_id=" + message_id + ", message_locale=" + message_locale + ", message_content="
				+ message_content + "]";
	}

}
