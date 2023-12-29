package com.tesla.colegio.model.bean;

import java.util.List;

public class MenuItem {
	@Override
	public String toString() {
		return "MenuItem [key=" + key + ", title=" + title + ", expanded=" + expanded + ", folder=" + folder
				+ ", active=" + active + ", selected=" + selected + ", children=" + children + "]";
	}

	private Integer key;
	private String title;
	private boolean expanded ;
	private boolean folder;
	private boolean active;
	private boolean selected;
	
	List<MenuItem> children;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
