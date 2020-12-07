package org.andrey.test_chat.domain;

public class MessageDTO {

	private String text;
	private String author;
	
	public MessageDTO(Message msg) {
		this.text = msg.getText();
		this.author = msg.getAuthor();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
}
