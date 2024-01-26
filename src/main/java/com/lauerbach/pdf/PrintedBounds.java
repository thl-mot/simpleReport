package com.lauerbach.pdf;

public class PrintedBounds {
	float top, left, right, bottom;

	public PrintedBounds(float x1, float y1, float x2, float y2) {
		super();
		this.left = Math.min(x1, x2);
		this.top = Math.min(y1, y2);
		this.right = Math.max(x1, x2);
		this.bottom = Math.max(y1, y2);
	}

	public void merge(PrintedBounds child) {
		this.top = Math.min(this.top, child.top);
		this.bottom = Math.max(this.bottom, child.bottom);
		this.left = Math.min(this.left, child.left);
		this.right = Math.max(this.right, child.right);
	}

	public float getTop() {
		return top;
	}

	public float getLeft() {
		return left;
	}

	public float getRight() {
		return right;
	}

	public float getBottom() {
		return bottom;
	}
	
	public float getWidth() {
		return right-left;
	}
	
	public float getHeight() {
		return bottom-top;
	}

	@Override
	public String toString() {
		return "bounds: "+left+" "+top+"    "+right+" "+bottom;
	}
}
